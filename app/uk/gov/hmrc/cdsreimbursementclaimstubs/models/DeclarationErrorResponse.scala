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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models

import java.util.UUID

import play.api.http.Status._
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._
import uk.gov.hmrc.cdsreimbursementclaimstubs.controllers.CDSProfile
import uk.gov.hmrc.cdsreimbursementclaimstubs.controllers.CDSProfile.timestamp

object DeclarationErrorResponse {

  val CouldNotBeProcessed = makeBackend("003 - Could not be processed")
  val NoDeclarationFound = makeBackend("086 - No declaration found")
  val NoSecurityDeposits = makeBackend("072 - No security deposits exist for Declaration ID and reason for security")
  val Timeout = make("101504 - Send timeout", INTERNAL_SERVER_ERROR, "ct-api")
  val WafError = CDSProfile.EIS_403_WAF_ERROR

  val WrongMethod = makeEis("Only Http POST is accepted",METHOD_NOT_ALLOWED)
  val WrongAcceptHeader = makeEis("Incorrect Accept header")
  val WrongContentTypeHeader = makeEis("Wrong Content-Type header")
  val NoCorrelationIdHeader = makeEis("No X-Correlation-ID header")
  val NoXForwaredHostHeader = makeEis("No X-Forwarded-Host header")
  val NoAuthorizationHeader = makeEis("No Bearer Token in the Authorization header", UNAUTHORIZED)
  val NoDeclarationId = makeEis("No declarationID was provided")

  def makeEis(message: String, status: Int = BAD_REQUEST): EisErrorResponse = make(message,status,"eis")
  def makeBackend(message: String, status: Int = BAD_REQUEST): EisErrorResponse = make(message,status,"backend")
  def make(message: String, status: Int = BAD_REQUEST, source:String): EisErrorResponse = {
    EisErrorResponse(
      ErrorDetail(
        timestamp,
        UUID.randomUUID().toString,
        status.toString,
        message,
        source,
        Detail(List(message))
      )
    )
  }

  def toErrorResponse(eisError: EisErrorResponse):Result = Status(eisError.errorDetail.errorCode.toInt)(Json.toJson(eisError))

  def toOkResponse(in:String):Result = Ok(Json.parse(in))
}


