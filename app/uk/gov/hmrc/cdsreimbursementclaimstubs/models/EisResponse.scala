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

import julienrf.json.derived
import play.api.libs.json._

sealed trait ParameterNameEnum
object ParameterNameEnum {
  final case object POSITION extends ParameterNameEnum
  implicit val format = derived.oformat[ParameterNameEnum]()
}

sealed trait ParameterValueEnum
object ParameterValueEnum {
  final case object FAIL extends ParameterValueEnum
  implicit val format = derived.oformat[ParameterValueEnum]()
}

final case class ReturnParameter(paramName: ParameterNameEnum, paramValue: ParameterValueEnum)

object ReturnParameter{
  implicit val format: OFormat[ReturnParameter] = derived.oformat[ReturnParameter]()
}

sealed trait ResponseCommonStatus
object ResponseCommonStatus {
  final case object OK extends ResponseCommonStatus
  implicit val format: OFormat[ResponseCommonStatus] = derived.oformat[ResponseCommonStatus]()
}

sealed trait CDFinancialPayService
object CDFinancialPayService {
  final case object NDRC extends CDFinancialPayService
  final case object SCTY extends CDFinancialPayService

  implicit val format: OFormat[CDFinancialPayService] = derived.oformat[CDFinancialPayService]()
}

final case class ResponseCommon(
  status: ResponseCommonStatus,
  processingDateTime: String,
  CdfPayService: Option[CDFinancialPayService],
  CdfPayCaseNumber: Option[String],
  correlationId: Option[String],
  errorMessage: Option[String],
  returnParameters: List[ReturnParameter]
)

object ResponseCommon {
  implicit val format: OFormat[ResponseCommon] = Json.format[ResponseCommon]
}
final case class PostNewClaimsResponse(responseCommon: ResponseCommon)

object PostNewClaimsResponse {
  implicit val format: OFormat[PostNewClaimsResponse] = Json.format[PostNewClaimsResponse]
}

final case class EisResponse(
  postNewClaimsResponse: PostNewClaimsResponse
)

object EisResponse {
  implicit val format: OFormat[EisResponse] = derived.oformat[EisResponse]()
}
