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
import org.scalacheck.Gen
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.EisResponse._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.GenUtils.sample
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.{EisResponse, PostNewClaimsResponse, ResponseCommon}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.time.LocalDateTime
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

  def submitClaim(): Action[JsValue] = Action(parse.json) { implicit request =>
    val validator = SchemaValidator(Some(Version4))

    validator
      .validate(schemaToBeValidated, request.body)
      .fold(
        e => {
          logger.warn(s"Could not validate nor parse request body: $e")
          BadRequest
        },
        _ => Ok(Json.toJson(makeEisResponse()))
      )
  }

  private def nRandomDigits(n: Int): String =
    List.fill(n)(sample(Gen.numChar)).mkString("")

  private def ndrcCaseNumber(): String =
    s"NDRC-${nRandomDigits(4)}"

  def makeEisResponse(): EisResponse =
    EisResponse(
      PostNewClaimsResponse(
        ResponseCommon(
          "OK", //TODO: will write a generator once we know what all the enumerations are
          LocalDateTime.now,
          ndrcCaseNumber(),
          "NDRC"
        )
      )
    )
}
