/*
 * Copyright 2021 HM Revenue & Customs
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
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.MockHttpResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.{Acc14ErrorResponse, Acc14Response}
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
    validator
      .validate(schemaToBeValidated, request.body)
      .fold(
        e => {
          logger.warn(s"Could not validate nor parse request body: $e")
          BadRequest
        },
        json =>
          (json \ "overpaymentDeclarationDisplayRequest" \ "requestDetail" \ "declarationId")
            .asOpt[String] match {
            case None =>
              logger.warn("could not find declaration id")
              BadRequest
            case Some(str) =>
              MockHttpResponse.getDeclarationHttpResponse(MRN(str)) match {
                case Some(httpResponse) =>
                  logger.info(s"declaration id received :${str}")
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
                          }
                      }
                    case Right(acc14Response) => {
                      logger.info(s"acc-14 profile returned is : ${acc14Response}")
                      Ok(Json.toJson(Acc14Response.returnAcc14Response(acc14Response).value))
                    }
                  }
                case None =>
                  logger.warn(s"could not find profile with claimant eori: $str")
                  BadRequest
              }
          }
      )
  }
}
