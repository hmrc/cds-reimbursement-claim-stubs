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

import com.eclipsesource.schema.SchemaType
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ids.EORI
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.{Tpi05ErrorResponse, Tpi05Response}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.{MockHttpResponse, SchemaValidation}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

@Singleton()
class ClaimController @Inject() (cc: ControllerComponents)
    extends BackendController(cc)
    with SchemaValidation
    with Logging {

  lazy val actualSchema: SchemaType = readSchema("tpi05/TPI05-v1-0-10-submit-claim-schema.json")
  lazy val nextSchema: SchemaType   = readSchema("tpi05/TPI05-v1-0-11-submit-claim-schema.json")

  val submitClaim: Action[JsValue] = Action(parse.json) { implicit request =>
    val payload = request.body

    logger.info(Json.prettyPrint(payload))

    validateRequest(actualSchema, nextSchema) {
      (payload \ "postNewClaimsRequest" \ "requestDetail" \ "claimantEORI")
        .asOpt[String] match {
        case None =>
          logger.warn("could not find claimant eori")
          BadRequest
        case Some(str) =>
          MockHttpResponse.getSubmitClaimHttpResponse(EORI(str)) match {
            case Some(httpResponse) =>
              logger.info(httpResponse.toString)
              httpResponse.submitClaimResponse.response match {
                case Left(value) =>
                  value match {
                    case Left(wAFErrorResponse) => Forbidden(Json.toJson(wAFErrorResponse.value))
                    case Right(tpi05ErrorResponse) =>
                      val error = Tpi05ErrorResponse.returnTpi05ErrorHttpResponse(tpi05ErrorResponse)
                      (error.httpStatus, error.value) match {
                        case (BAD_REQUEST, responseBody) => BadRequest(Json.toJson(responseBody))
                        case (UNAUTHORIZED, responseBody) => Unauthorized(Json.toJson(responseBody))
                        case (METHOD_NOT_ALLOWED, responseBody) => MethodNotAllowed(Json.toJson(responseBody))
                        case (INTERNAL_SERVER_ERROR, responseBody) => InternalServerError(Json.toJson(responseBody))
                        case _ => InternalServerError
                      }
                  }
                case Right(tpi05Response) =>
                  Ok(Json.toJson(Tpi05Response.returnTpi05HttpResponse(tpi05Response).value))
              }
            case None =>
              logger.warn(s"could not find profile with claimant eori: $str")
              BadRequest
          }
      }
    }
  }

}
