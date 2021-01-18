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

import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.time.LocalDateTime

final case class ResponseCommon(
  status: String,
  processingDateTime: LocalDateTime,
  cdfPayCaseNumber: String,
  cdfPayService: String
)

object ResponseCommon {
  implicit val responseCommonReads: Reads[ResponseCommon] = (
    (JsPath \ "Status").read[String] and
      (JsPath \ "ProcessingDateTime").read[LocalDateTime] and
      (JsPath \ "CdfPayCaseNumber").read[String] and
      (JsPath \ "CdfPayService").read[String]
  )(ResponseCommon.apply _)

  implicit val responseCommonWrites: Writes[ResponseCommon] = (
    (JsPath \ "Status").write[String] and
      (JsPath \ "ProcessingDateTime").write[LocalDateTime] and
      (JsPath \ "CdfPayCaseNumber").write[String] and
      (JsPath \ "CdfPayService").write[String]
  )(unlift(ResponseCommon.unapply))
}
final case class PostNewClaimsResponse(responseCommon: ResponseCommon)

object PostNewClaimsResponse {

  implicit val responseCommonReads: Reads[PostNewClaimsResponse] =
    (__ \ "ResponseCommon")
      .format[ResponseCommon]
      .inmap(PostNewClaimsResponse.apply, unlift(PostNewClaimsResponse.unapply))

  implicit val postNewClaimsResponseWrites: Writes[PostNewClaimsResponse] =
    (__ \ "ResponseCommon")
      .format[ResponseCommon]
      .inmap(PostNewClaimsResponse.apply, unlift(PostNewClaimsResponse.unapply))
}

final case class EisResponse(
  postNewClaimsResponse: PostNewClaimsResponse
)

object EisResponse {

  implicit val postNewClaimsResponseReads: Reads[EisResponse] =
    (__ \ "PostNewClaimsResponse")
      .format[PostNewClaimsResponse]
      .inmap(EisResponse.apply, unlift(EisResponse.unapply))

  implicit val postNewClaimsResponseWrites: Writes[EisResponse] =
    (__ \ "PostNewClaimsResponse")
      .format[PostNewClaimsResponse]
      .inmap(EisResponse.apply, unlift(EisResponse.unapply))
}
