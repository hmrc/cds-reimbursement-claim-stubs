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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.{GenUtils, TimeUtils}

final case class Tpi05Response(value: JsValue)

object Tpi05Response {

  sealed trait Tpi05ResponseType extends Product with Serializable
  object Tpi05ResponseType {
    case object OK_RESPONSE extends Tpi05ResponseType
    case object OK_BUT_ERROR_RETURN_IN_PAYLOAD extends Tpi05ResponseType
  }

  def returnTpi05HttpResponse(responseType: Tpi05ResponseType): Tpi05Response =
    responseType match {
      case Tpi05ResponseType.OK_RESPONSE => getOkResponse
      case Tpi05ResponseType.OK_BUT_ERROR_RETURN_IN_PAYLOAD => getOkResponseWithErrorInPayload
    }

  def getOkResponse: Tpi05Response = Tpi05Response(
    Json.parse(
      s"""
        |{
        |    "postNewClaimsResponse": {
        |        "responseCommon": {
        |            "status": "OK",
        |            "processingDate": "${TimeUtils.iso8061DateTimeNow}",
        |            "CDFPayService": "NDRC",
        |            "CDFPayCaseNumber": "${GenUtils.caseNumber}",
        |            "correlationId": "${GenUtils.correlationId}"
        |        }
        |    }
        |}
        |""".stripMargin
    )
  )

  def getOkResponseWithErrorInPayload: Tpi05Response = Tpi05Response(
    Json.parse(
      s"""
    |{
    |    "postNewClaimsResponse": {
    |        "responseCommon": {
    |            "status": "OK",
    |            "processingDate": "${TimeUtils.iso8061DateTimeNow}",
    |            "correlationId": "${GenUtils.correlationId}",
    |            "errorMessage": "Invalid Claim Type",
    |            "returnParameters": [
    |                {
    |                    "paramName": "POSITION",
    |                    "paramValue": "FAIL"
    |                }
    |            ]
    |        }
    |    }
    |}
    |""".stripMargin
    )
  )

}
