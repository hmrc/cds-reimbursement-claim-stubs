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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import org.scalatest.OptionValues.convertOptionToValuable
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.Application
import play.api.http.ContentTypes
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsObject, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.SchemaValidation

class TPIControllerSpec extends AnyWordSpec with Matchers with SchemaValidation {

  val headers: Seq[(String, String)] = Seq(
    "Date"             -> "Fri, 16 Aug 2019 18:15:41 GMT",
    "X-Correlation-ID" -> "some-id",
    "X-Forwarded-Host" -> "MDTP",
    "Content-Type"     -> "application/json",
    "Accept"           -> "application/json",
    "Authorization"    -> "Bearer test1234567"
  )

  "retrieveReimbursementClaims" should {
    testGetReimbursementClaims("return 200 - success response no claims found")(
      "GB744638982002",
      "tpi01/response-200-no-claims-found.json",
      OK
    )

    Set(
      "GB0144638982000",
      "GB0244638982000",
      "GB0344638982000",
      "GB0444638982000",
      "GB0544638982000",
      "GB3744638982000",
      "GB4044638982000",
      "GB4144638982000",
      "GB5044638982000"
    ).foreach { eori =>
      testGetValidReimbursementClaims(s"return 200 - success response NDRC for $eori")(
        eori
      )
    }

    testGetReimbursementClaims("return 200 - success response NDRC")(
      "GB744638982006",
      "tpi01/response-200-NDRC.json",
      OK
    )
    testGetReimbursementClaims("return 200 - success response SCTY")(
      "GB744638982007",
      "tpi01/response-200-SCTY.json",
      OK
    )
    testGetValidReimbursementClaims("return 200 - valid success response for GB744638982001")(
      "GB744638982001"
    )
    testGetValidReimbursementClaims("return 200 - valid success response for GB000000000001")(
      "GB000000000001"
    )
    testGetValidReimbursementClaims("return 200 - valid success response for GB100000000001")(
      "GB100000000001"
    )
    testGetReimbursementClaims("return 400 - field missing")(
      "TPI01MISSING",
      "tpi01/response-400-mandatory-missing-field.json",
      BAD_REQUEST
    )
    testGetReimbursementClaims("return 400 - pattern error")(
      "TPI01PATTERN",
      "tpi01/response-400-pattern-error.json",
      BAD_REQUEST
    )
    testGetReimbursementClaims("return 500")(
      "TPI01500",
      "tpi01/response-500-system-timeout.json",
      INTERNAL_SERVER_ERROR
    )
  }

  "getSpecificClaim" should {
    testGetSpecificClaim("return 200 - success response no claims found")(
      "4374422407",
      "tpi02/response-200-no-claims-found.json",
      OK
    )
    testGetSpecificClaim("return 200 - SCTY full")(
      "SCTY-2109",
      "tpi02/response-200-SCTY-2109.json",
      OK
    )
    testGetSpecificClaim("return 200 - SCTY minimal")(
      "SCTY-2110",
      "tpi02/response-200-SCTY-2110.json",
      OK
    )
    testGetSpecificClaim("return 400 - field missing")(
      "4374422406",
      "tpi02/response-400-mandatory-missing-field.json",
      BAD_REQUEST
    )
    testGetSpecificClaim("return 400 - pattern error")(
      "4374422405",
      "tpi02/response-400-pattern-error.json",
      BAD_REQUEST
    )
    testGetSpecificClaim("return 500")("4374422404", "tpi02/response-500-system-timeout.json", INTERNAL_SERVER_ERROR)
  }

  "modulo1 function" should {
    "produce values confined in the range" in {
      val m = app.injector.instanceOf[TPI01Controller].modulo1 _
      for (size <- 2 to 25; i <- 1 until size) {
        m(i, size) mustBe i
        m(size, size) mustBe size
        m(size + i, size) mustBe i
      }
    }
  }

  def app: Application = GuiceApplicationBuilder().configure("metrics.enabled" -> false).build()

  def createValidGetRequest(eori: String): FakeRequest[JsObject] =
    FakeRequest("POST", routes.TPI01Controller.getReimbursementClaims.url)
      .withHeaders(headers: _*)
      .withBody(Json.parse(requestJson(eori)).as[JsObject])

  def createValidGetSpecificClaimRequest(cdfPayCaseNumber: String): FakeRequest[JsObject] =
    FakeRequest("POST", routes.TPI02Controller.getSpecificClaim.url)
      .withHeaders(headers: _*)
      .withBody(Json.parse(requestJsonGetSpecificClaim(cdfPayCaseNumber)).as[JsObject])

  def testGetReimbursementClaims(
    expectedResponse: String
  )(testEori: String, expectedResponseFileName: String, expectedStatusCode: Int): Unit =
    expectedResponse when {
      s"EORI is $testEori is supplied in request" in {
        running(app) {

          val response = route(
            app,
            createValidGetRequest(testEori)
              .withHeaders("Authorization" -> "Bearer test1234567")
          ).value
          status(response) mustBe expectedStatusCode
          contentType(response) mustBe Some(ContentTypes.JSON)
          contentAsString(response) mustEqual jsonDataFromFile(expectedResponseFileName).toString()
        }
      }
    }

  def testGetValidReimbursementClaims(
    expectedResponse: String
  )(testEori: String): Unit =
    expectedResponse when {
      s"EORI is $testEori is supplied in request" in {
        running(app) {

          val response = route(
            app,
            createValidGetRequest(testEori)
              .withHeaders("Authorization" -> "Bearer test1234567")
          ).value
          status(response) mustBe OK
          contentType(response) mustBe Some(ContentTypes.JSON)
        }
      }
    }

  def testGetSpecificClaim(
    expectedResponse: String
  )(testCdfPayCaseNumber: String, expectedResponseFileName: String, expectedStatusCode: Int): Unit =
    expectedResponse when {
      s"CDFpayCaseNumber is $testCdfPayCaseNumber is supplied in request" in {
        running(app) {

          val response = route(
            app,
            createValidGetSpecificClaimRequest(testCdfPayCaseNumber)
              .withHeaders("Authorization" -> "Bearer test1234567")
          ).value
          status(response) mustBe expectedStatusCode
          contentType(response) mustBe Some(ContentTypes.JSON)
          contentAsString(response) mustEqual jsonDataFromFile(expectedResponseFileName).toString()
        }
      }
    }

  def requestJson(eori: String): String                             =
    s"""
       |{
       |	"getPostClearanceCasesRequest": {
       |		"requestCommon": {
       |			"originatingSystem": "MDTP",
       |			"receiptDate": "2021-04-20T12:07:54Z",
       |			"acknowledgementReference": "1234567890123456789012345678901"
       |		},
       |		"requestDetail": {
       |			"EORI": "$eori",
       |      "appType": "A"
       |		}
       |	}
       |}
       |
       |""".stripMargin
  def requestJsonGetSpecificClaim(cdfPayCaseNumber: String): String =
    s"""
       |{
       |	"getSpecificCaseRequest": {
       |		"requestCommon": {
       |			"originatingSystem": "MDTP",
       |			"receiptDate": "2021-04-20T12:07:54Z",
       |			"acknowledgementReference": "12345678901234567890123456789012"
       |		},
       |		"requestDetail": {
       |			"CDFPayService": "NDRC",
       |			"CDFPayCaseNumber": "$cdfPayCaseNumber"
       |		}
       |	}
       |}
       |""".stripMargin
}
