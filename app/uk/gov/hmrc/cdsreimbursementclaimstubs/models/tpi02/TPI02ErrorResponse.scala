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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi02

import play.api.libs.json.{Json, OFormat}

case class TPI02ErrorResponse(errorDetail: ErrorDetail)

case class ErrorDetail(
  timestamp: String,
  correlationId: String,
  errorCode: String,
  errorMessage: String,
  source: String,
  sourceFaultDetail: SourceFaultDetail
)

case class SourceFaultDetail(detail: Array[String])

object TPI02ErrorResponse {
  implicit val errorResponseFormat: OFormat[TPI02ErrorResponse] = Json.format[TPI02ErrorResponse]
}

object ErrorDetail {
  implicit val errorDetailFormat: OFormat[ErrorDetail] = Json.format[ErrorDetail]
}

object SourceFaultDetail {
  implicit val errorDetailFormat: OFormat[SourceFaultDetail] = Json.format[SourceFaultDetail]
}
