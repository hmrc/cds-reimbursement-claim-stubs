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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14

import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.TimeUtils

final case class Acc14ErrorResponse(value: JsValue, httpStatus: Int)

object Acc14ErrorResponse {

  sealed trait Acc14ErrorResponseType extends Product with Serializable
  object Acc14ErrorResponseType {
    case object BAD_REQUEST_MISSING_DECLARATION extends Acc14ErrorResponseType
    case object NO_SECURITY_DEPOSITS extends Acc14ErrorResponseType
    case object TIME_OUT extends Acc14ErrorResponseType
    case object HTTP_METHOD_NOT_ALLOWED extends Acc14ErrorResponseType
  }

  def returnAcc14ErrorResponse(acc14ErrorResponseType: Acc14ErrorResponseType): Acc14ErrorResponse =
    acc14ErrorResponseType match {
      case Acc14ErrorResponseType.BAD_REQUEST_MISSING_DECLARATION => makeBadRequestMissingDeclarationResponse
      case Acc14ErrorResponseType.NO_SECURITY_DEPOSITS => makeNoSecurityDepositResponse
      case Acc14ErrorResponseType.TIME_OUT => makeTimeOutResponse
      case Acc14ErrorResponseType.HTTP_METHOD_NOT_ALLOWED => makeHttpMethodNotAllowedResponse
    }

  def makeBadRequestMissingDeclarationResponse = Acc14ErrorResponse(
    Json.parse(
      s"""
        |{
        |    "errorDetail": {
        |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
        |        "correlationId": "314ae2f9-d968-4f28-b9be-d9a72913fc71",
        |        "errorCode": "400",
        |        "errorMessage": "No declaration found",
        |        "source": "Backend",
        |        "sourceFaultDetail": {
        |            "detail": [
        |                "086 - No declaration found"
        |            ]
        |        }
        |    }
        |}
        |""".stripMargin
    ),
    Status.BAD_REQUEST
  )

  def makeNoSecurityDepositResponse = Acc14ErrorResponse(
    Json.parse(
      s"""
        |{
        |    "errorDetail": {
        |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
        |        "correlationId": "314ae2f9-d968-4f28-b9be-d9a72913fc71",
        |        "errorCode": "400",
        |        "errorMessage": "No security deposits exist for Declaration ID and reason for security",
        |        "source": "Backend",
        |        "sourceFaultDetail": {
        |            "detail": [
        |                "072 - No security deposits exist for Declaration ID and reason for security"
        |            ]
        |        }
        |    }
        |}
        |""".stripMargin
    ),
    Status.BAD_REQUEST
  )

  def makeTimeOutResponse = Acc14ErrorResponse(
    Json.parse(
      s"""
        |{
        |    "errorDetail": {
        |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
        |        "correlationId": "",
        |        "errorCode": "500",
        |        "errorMessage": "Send timeout",
        |        "source": "ct-api",
        |        "sourceFaultDetail": {
        |            "detail": [
        |                "101504 - Send timeout"
        |            ]
        |        }
        |    }
        |}
        |""".stripMargin
    ),
    Status.INTERNAL_SERVER_ERROR
  )

  def makeHttpMethodNotAllowedResponse = Acc14ErrorResponse(
    Json.parse(
      s"""
         |{
         |    "errorDetail": {
         |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
         |        "correlationId": "",
         |        "errorCode": "405",
         |        "errorMessage": "HTTP METHOD NOT ALLOWED",
         |        "source": "ct-api",
         |        "sourceFaultDetail": {
         |            "detail": [
         |                "101504 - HTTP METHOD NOT ALLOWED"
         |            ]
         |        }
         |    }
         |}
         |""".stripMargin
    ),
    Status.METHOD_NOT_ALLOWED
  )

}
