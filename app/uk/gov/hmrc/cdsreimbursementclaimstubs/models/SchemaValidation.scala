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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models

import com.eclipsesource.schema.drafts.Version4._
import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.Results.{BadRequest, InternalServerError, Ok}
import play.api.mvc.{Request, Result}
import play.api.{Logger, LoggerLike}

import scala.concurrent.Future
import scala.io.Source

trait SchemaValidation {

  val validator: SchemaValidator = SchemaValidator()
  val log: LoggerLike            = Logger(this.getClass)

  def fValidateRequest(filename: String)(f: => Future[Result])(implicit request: Request[JsValue]): Future[Result] = {
    val result = validator.validate(getSchema(filename), request.body)
    result match {
      case JsSuccess(_, _) => f
      case JsError(errors) => log.error(errors.mkString(" ")); Future.successful(BadRequest)
    }
  }

  def validateRequest(filename: String)(f: => Result)(implicit request: Request[JsValue]): Result = {
    val result = validator.validate(getSchema(filename), request.body)
    result match {
      case JsSuccess(_, _) => f
      case JsError(errors) => log.error(errors.mkString(" ")); BadRequest
    }
  }

  def validateResponse(filename: String, response: JsValue): Result = {
    val result = validator.validate(getSchema(filename), response)
    result match {
      case JsSuccess(_, _) => Ok(response)
      case JsError(errors) => log.error(errors.mkString(" ")); InternalServerError
    }
  }

  private def getSchema(filename: String): SchemaType =
    Json.fromJson[SchemaType](jsonDataFromFile(filename)).get

  def responseWithEori(filename: String, myEori: String = "GB12345678901234"): JsValue =
    jsonDataFromFile(filename, _.replaceAll("MY_OWN_EORI", myEori))

  def jsonDataFromFile(filename: String, transform: String => String = identity): JsValue = {
    val in     = getClass.getResourceAsStream(s"/resources/$filename")
    val raw    = Source.fromInputStream(in).getLines.mkString
    val cooked = transform(raw)
    Json.parse(cooked)
  }
}
