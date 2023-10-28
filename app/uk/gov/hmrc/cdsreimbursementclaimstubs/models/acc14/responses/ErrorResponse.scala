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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.responses

import play.api.http.Status
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.model.{DeclarationErrorResponse, ErrorDetail, SourceFaultDetail}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.TimeUtils

case class ErrorResponse(declarationErrorResponse: DeclarationErrorResponse, status: Int)

object ErrorResponse {

  def MAKE_BAD_REQUEST_MISSING_DECLARATION_RESPONSE: ErrorResponse = create(
    "314ae2f9-d968-4f28-b9be-d9a72913fc71",
    Status.BAD_REQUEST,
    "No declaration found",
    "Backend",
    Seq("086 - No declaration found")
  )

  def MAKE_NO_SECURITY_DEPOSIT_RESPONSE: ErrorResponse = create(
    "314ae2f9-d968-4f28-b9be-d9a72913fc71",
    Status.BAD_REQUEST,
    "No security deposits exist for Declaration ID and reason for security",
    "Backend",
    Seq("072 - No security deposits exist for Declaration ID and reason for security")
  )

  def MAKE_TIME_OUT_RESPONSE: ErrorResponse = create(
    "314ae2f9-d968-4f28-b9be-d9a72913fc71",
    Status.INTERNAL_SERVER_ERROR,
    "Send timeout",
    "ct-api",
    Seq("101504 - Send timeout")
  )

  def MAKE_HTTP_METHOD_NOT_ALLOWED_RESPONSE: ErrorResponse = create(
    "314ae2f9-d968-4f28-b9be-d9a72913fc71",
    Status.METHOD_NOT_ALLOWED,
    "HTTP METHOD NOT ALLOWED",
    "ct-api",
    Seq("101504 - HTTP METHOD NOT ALLOWED")
  )

 def create(correlationId: String, status: Int, errorMessage: String,  source: String, sourceFaultDetail: Seq[String]): ErrorResponse = {
   ErrorResponse(DeclarationErrorResponse(
     ErrorDetail(
       Some(TimeUtils.iso8061DateTimeNow),
       Some(correlationId),
       Some(status.toString),
       Some(errorMessage),
       Some(source),
       SourceFaultDetail(sourceFaultDetail)
     )
   ), status)
 }
}
