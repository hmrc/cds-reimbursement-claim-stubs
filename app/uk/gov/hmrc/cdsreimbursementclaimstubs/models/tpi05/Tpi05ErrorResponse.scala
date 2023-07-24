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

import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.{GenUtils, TimeUtils}
import play.twirl.api.HtmlFormat.Appendable
final case class Tpi05ErrorResponse(value: JsValue, httpStatus: Int)

object Tpi05ErrorResponse {

  sealed trait Tpi05ErrorResponseType extends Product with Serializable
  object Tpi05ErrorResponseType {
    case object MISSING_MANDATORY_FIELD extends Tpi05ErrorResponseType
    case object PATTERN_ERROR extends Tpi05ErrorResponseType
    case object TIME_OUT extends Tpi05ErrorResponseType
    case object HTTP_METHOD_NOT_ALLOWED extends Tpi05ErrorResponseType
    case object NO_BEARER_TOKEN extends Tpi05ErrorResponseType
  }

  def returnTpi05ErrorHttpResponse(responseType: Tpi05ErrorResponseType): Tpi05ErrorResponse =
    responseType match {
      case Tpi05ErrorResponseType.MISSING_MANDATORY_FIELD => getMissMandatoryFieldErrorResponse
      case Tpi05ErrorResponseType.PATTERN_ERROR => getPatternErrorErrorResponse
      case Tpi05ErrorResponseType.TIME_OUT => getTimeoutErrorResponse
      case Tpi05ErrorResponseType.HTTP_METHOD_NOT_ALLOWED => getHttpMethodNotAllowedErrorResponse
      case Tpi05ErrorResponseType.NO_BEARER_TOKEN => getNoBearerTokenErrorResponse
    }

  def getMissMandatoryFieldErrorResponse = Tpi05ErrorResponse(
    Json.parse(
      s"""
      |{
      |    "errorDetail": {
      |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
      |        "correlationId": "${GenUtils.correlationId}",
      |        "errorCode": "400",
      |        "errorMessage": "Invalid message",
      |        "source": "EIS",
      |        "sourceFaultDetail": {
      |            "detail": [ "invalid message"]
      |        }
      |    }
      |}
      |""".stripMargin
    ),
    Status.BAD_REQUEST
  )

  def getPatternErrorErrorResponse = Tpi05ErrorResponse(
    Json.parse(
      s"""
        |{
        |    "errorDetail": {
        |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
        |        "correlationId": "${GenUtils.correlationId}",
        |        "errorCode": "400",
        |        "errorMessage": "Invalid message",
        |        "source": "JSON validation",
        |        "sourceFaultDetail": {
        |            "detail": ["JSON validation"]
        |        }
        |    }
        |}
        |""".stripMargin
    ),
    Status.BAD_REQUEST
  )

  def getTimeoutErrorResponse = Tpi05ErrorResponse(
    Json.parse(
      s"""
      |{
      |    "errorDetail": {
      |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
      |        "correlationId": "${GenUtils.correlationId}",
      |        "errorCode": "500",
      |        "errorMessage": "Error connecting to the server",
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

  def getHttpMethodNotAllowedErrorResponse = Tpi05ErrorResponse(
    Json.parse(
      s"""
        |{
        |    "errorDetail": {
        |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
        |        "correlationId": "${GenUtils.correlationId}",
        |        "errorCode": "405",
        |        "errorMessage": "Unsupported HTTP method",
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
    Status.METHOD_NOT_ALLOWED
  )

  def getNoBearerTokenErrorResponse = Tpi05ErrorResponse(
    Json.parse(
      s"""
         |{
         |    "errorDetail": {
         |        "timestamp": "${TimeUtils.iso8061DateTimeNow}",
         |        "correlationId": "${GenUtils.correlationId}",
         |        "errorCode": "401",
         |        "errorMessage": "No Bearer Token",
         |        "source": "ct-api",
         |        "sourceFaultDetail": {
         |            "detail": [
         |                "101504 - No Bearer Token"
         |            ]
         |        }
         |    }
         |}
         |""".stripMargin
    ),
    Status.UNAUTHORIZED
  )

}
