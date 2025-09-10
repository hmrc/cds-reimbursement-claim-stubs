/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import cats.syntax.EitherSyntax
import cats.syntax.eq._
import com.eclipsesource.schema.drafts.Version4
import com.eclipsesource.schema.drafts.Version4._
import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Request}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.MockHttpResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.{Acc14ErrorResponse, Acc14Request, Acc14Response}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ids.MRN
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.io.Source

@Singleton
class DeclarationController @Inject() (cc: ControllerComponents)
    extends BackendController(cc)
    with Logging
    with EitherSyntax {

  lazy val schemaToBeValidated: SchemaType =
    Json
      .fromJson[SchemaType](
        Json.parse(
          Source
            .fromInputStream(
              this.getClass.getResourceAsStream("/resources/ACC14-v1-3-0-overpayment-display-request-schema.json")
            )
            .mkString
        )
      )
      .get

  def getDeclaration: Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    val validator = SchemaValidator(Some(Version4))
    val payload   = request.body
    validator
      .validate(schemaToBeValidated, payload)
      .fold(
        e => {
          logger.warn(s"Could not validate nor parse request body: $e")
          BadRequest
        },
        json =>
          (json \ "overpaymentDeclarationDisplayRequest" \ "requestDetail").asOpt[Acc14Request] match {
            case None =>
              logger.warn("could not find declaration id in the request")
              BadRequest
            case Some(request @ Acc14Request(_, Some(reasonForSecurity))) =>
              MockHttpResponse.getSecuritiesDeclaration(MRN(request.declarationId), reasonForSecurity) match {
                case Some(declarationResponse) =>
                  declarationResponse.response match {
                    case Left(value) =>
                      value match {
                        case Left(wafErrorResponse) => Forbidden(Json.toJson(wafErrorResponse.value))
                        case Right(acc14ErrorResponse) =>
                          Acc14ErrorResponse.returnAcc14ErrorResponse(acc14ErrorResponse) match {
                            case error if error.httpStatus === BAD_REQUEST =>
                              BadRequest(error.value)
                            case error if error.httpStatus === METHOD_NOT_ALLOWED =>
                              MethodNotAllowed(error.value)
                            case error =>
                              InternalServerError(error.value)
                          }
                      }
                    case Right(acc14Response) =>
                      val response = Acc14Response
                        .returnAcc14Response(acc14Response)
                        .withMrn(request.requestedDeclarationId)
                        .withDeclarantXiEori(request.shouldReturnDeclarantXiEori)
                        .withConsigneeXiEori(request.shouldReturnConsigneeXiEori)
                      logger
                        .info(
                          s"acc-14 profile returned for ${request.requestedDeclarationId} and $reasonForSecurity is: $response"
                        )
                      Ok(response.value)
                  }
                case None =>
                  logger
                    .warn(
                      s"could not find declaration with MRN: ${request.declarationId} and reason for security $reasonForSecurity"
                    )
                  BadRequest
              }
            case Some(request) =>
              MockHttpResponse.getDeclarationHttpResponse(MRN(request.declarationId)) match {
                case Some(httpResponse) =>
                  logger.info(s"declaration id received: ${request.requestedDeclarationId}")
                  httpResponse.declarationResponse.response match {
                    case Left(value) =>
                      value match {
                        case Left(wafErrorResponse) => Forbidden(Json.toJson(wafErrorResponse.value))
                        case Right(acc14ErrorResponse) =>
                          val error = Acc14ErrorResponse.returnAcc14ErrorResponse(acc14ErrorResponse)
                          (error.httpStatus, error.value) match {
                            case (BAD_REQUEST, responseBody) => BadRequest(Json.toJson(responseBody))
                            case (UNAUTHORIZED, responseBody) => Unauthorized(Json.toJson(responseBody))
                            case (METHOD_NOT_ALLOWED, responseBody) => MethodNotAllowed(Json.toJson(responseBody))
                            case (INTERNAL_SERVER_ERROR, responseBody) => InternalServerError(Json.toJson(responseBody))
                            case (status, responseBody) => Status(status)(Json.toJson(responseBody))
                          }
                      }
                    case Right(acc14Response) =>
                      val response = Acc14Response
                        .returnAcc14Response(acc14Response)
                        .withMrn(request.requestedDeclarationId)
                        .withDeclarantXiEori(request.shouldReturnDeclarantXiEori)
                        .withConsigneeXiEori(request.shouldReturnConsigneeXiEori)
                      logger.info(s"acc-14 profile returned is: $response")
                      Ok(response.value)
                  }
                case None =>
                  logger.warn(s"could not find declaration with MRN: ${request.declarationId}")
                  BadRequest
              }
          }
      )
  }

}
