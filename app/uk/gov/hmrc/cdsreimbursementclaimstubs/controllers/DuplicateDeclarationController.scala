/*
 * Copyright 2022 HM Revenue & Customs
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
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi04.{TPI04Request, TPI04Response}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import scala.io.Source

@Singleton
class DuplicateDeclarationController @Inject() (cc: ControllerComponents) extends BackendController(cc) with Logging {

  private lazy val schemaToBeValidated: SchemaType = Json.fromJson[SchemaType](schemaJson).get
  private lazy val validator                       = SchemaValidator(Some(Version4))
  private lazy val schemaJson                      = Json.parse(
    Source
      .fromInputStream(
        this.getClass.getResourceAsStream("/resources/TPI04_EIS_Request.json")
      )
      .mkString
  )

  // TODO: Add extract cases when we have concrete examples.
  def getDuplicateDeclarations: Action[JsValue] = Action(parse.json) { implicit request =>
    val payload = request.body
    println(Json.prettyPrint(payload))
    validator
      .validate(schemaToBeValidated, payload)
      .fold(
        error => {
          logger.warn(s"Could not validate nor parse request body: $error")
          BadRequest
        },
        json =>
          (json \ "getExistingClaimRequest" \ "requestDetail").asOpt[TPI04Request] match {
            case None =>
              logger.warn(
                s"We could not find either the declaration id or the reason for security, but this will only happen if the schema changes"
              )
              BadRequest
            case Some(TPI04Request(_, "ACS")) =>
              Ok(Json.toJson(TPI04Response(caseFound = true, Some("SEC-1234"), Some("Open"))))
            case Some(TPI04Request("08AAAAAAAAAAAAAAA2", "RED")) =>
              Ok(Json.toJson(TPI04Response(caseFound = true, Some("SEC-1234"), Some("Open"))))
            case Some(TPI04Request("30ABCDEFGHIJKLMNO1", _)) =>
              Ok(Json.toJson(TPI04Response(caseFound = true, Some("SEC-0003"), Some("Open"))))
            case _ =>
              Ok(Json.toJson(TPI04Response(caseFound = false, None, None)))
          }
      )
  }
}
