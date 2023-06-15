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
import com.eclipsesource.schema.drafts.Version4
import com.eclipsesource.schema.drafts.Version4._
import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Request}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.model.DeclarationHttpResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.responses.ErrorResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.{Acc14Request, Acc14Response}
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
              DeclarationHttpResponse.getResponse(MRN(request.declarationId), reasonForSecurity) match {
                case Some(declarationResponse) =>
                  declarationResponse.response match {
                    case Left(value) =>
                      value match {
                        case Left(wafErrorResponse) =>
                          logger
                            .info(
                              s"acc-14 profile returned for ${request.requestedDeclarationId} and $reasonForSecurity is: $wafErrorResponse"
                            )
                          Forbidden(Json.toJson(wafErrorResponse.value))
                        case Right(errorResponse: ErrorResponse) =>
                          logger
                            .info(
                              s"acc-14 profile returned for ${request.requestedDeclarationId} and $reasonForSecurity is: $errorResponse"
                            )
                          Status(errorResponse.status)(Json.toJson(errorResponse.declarationErrorResponse))
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
              DeclarationHttpResponse.getResponse(MRN(request.declarationId)) match {
                case Some(httpResponse) =>
                  logger.info(s"declaration id received: ${request.requestedDeclarationId}")
                  httpResponse.response match {
                    case Left(value) =>
                      value match {
                        case Left(wafErrorResponse) =>
                          logger
                            .info(
                              s"acc-14 profile returned for ${request.requestedDeclarationId} is: $wafErrorResponse"
                            )
                          Forbidden(Json.toJson(wafErrorResponse.value))
                        case Right(errorResponse: ErrorResponse) =>
                          logger
                            .info(
                              s"acc-14 profile returned for ${request.requestedDeclarationId} is: $errorResponse"
                            )
                          Status(errorResponse.status)(Json.toJson(errorResponse.declarationErrorResponse))
                      }
                    case Right(acc14Response) =>
                      val response = Acc14Response
                        .returnAcc14Response(acc14Response)
                        .withMrn(request.requestedDeclarationId)
                        .withDeclarantXiEori(request.shouldReturnDeclarantXiEori)
                        .withConsigneeXiEori(request.shouldReturnConsigneeXiEori)
                      logger.info(s"acc-14 profile returned for ${request.requestedDeclarationId} is: $response")
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
