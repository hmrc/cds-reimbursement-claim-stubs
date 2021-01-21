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

import play.api.libs.json.{Json, OFormat}

final case class Detail(detail: List[String])

object Detail {
  implicit val format = Json.format[Detail]
}

final case class ErrorDetail(
  timestamp: String,
  correlationId: String,
  errorCode: String,
  errorMessage: String,
  source: String,
  sourceFaultDetail: Detail
)

object ErrorDetail {
  implicit val format: OFormat[ErrorDetail] = Json.format[ErrorDetail]
}

final case class EisErrorResponse(errorDetail: ErrorDetail)

object EisErrorResponse {
  implicit val format: OFormat[EisErrorResponse] = Json.format[EisErrorResponse]
}