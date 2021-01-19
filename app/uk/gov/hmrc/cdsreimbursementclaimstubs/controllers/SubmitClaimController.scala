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

import com.eclipsesource.schema.drafts.Version4
import com.eclipsesource.schema.drafts.Version4._
import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.OverPaymentClaim
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.io.Source

@Singleton()
class SubmitClaimController @Inject() (cc: ControllerComponents) extends BackendController(cc) with Logging {

  lazy val schemaToBeValidated: SchemaType = Json
    .fromJson[SchemaType](
      Json.parse(
        Source
          .fromInputStream(
            this.getClass.getResourceAsStream("/resources/submit-claim-schema-1-0.json")
          )
          .mkString
      )
    )
    .get

  def submitClaim: Action[JsValue] = Action(parse.json) { implicit request =>
    val validator = SchemaValidator(Some(Version4))

    validator
      .validate(schemaToBeValidated, request.body)
      .fold(
        e => {
          logger.warn(s"Could not validate nor parse request body: $e")
          BadRequest
        },
        json =>
          json.asOpt[OverPaymentClaim] match {
            case None =>
              logger.warn("could not get overpayment claim data")
              BadRequest
            case Some(overPaymentClaim) =>
              CDSProfile.getCDSProfile(overPaymentClaim) match {
                case Some(cDSProfile) =>
                  cDSProfile.submitClaimResponse.response match {
                    case Left(value) =>
                      value match {
                        case Left(wAFErrorResponse) => Ok(Json.toJson(wAFErrorResponse))
                        case Right(eisErrorResponse) => Ok(Json.toJson(eisErrorResponse))
                      }
                    case Right(eisResponse) => Ok(Json.toJson(eisResponse))
                  }
                case None =>
                  logger.warn(s"could not find profile with claimant eori: ${overPaymentClaim.claimantEori}")
                  BadRequest
              }
          }
      )
  }
}
