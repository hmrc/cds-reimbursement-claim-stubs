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
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.SchemaValidation

class SUB09ControllerSpec extends AnyWordSpec with Matchers with SchemaValidation {

  val headers: Seq[(String, String)] = Seq(
    "Date"             -> "Fri, 16 Aug 2019 18:15:41 GMT",
    "X-Correlation-ID" -> "some-id",
    "X-Forwarded-Host" -> "MDTP",
    "Content-Type"     -> "application/json",
    "Accept"           -> "application/json",
    "Authorization"    -> "Bearer test1234567"
  )

  "getSubscription" should {
    testGetSubscription("return 200 - success response subscription with XI EORI")(
      "GB744638982002",
      "sub09/companyInformationResponse.json",
      OK
    )

    testGetSubscription("return 200 - success response subscription without XI EORI")(
      "GB744638982001",
      "sub09/companyInformationNoXiEori.json",
      OK
    )

    testGetSubscription("return 200 - business error 001")(
      "GB0144638982000",
      "sub09/businessErrorExample-InvalidData-200-001.json",
      OK
    )

    testGetSubscription("return 200 - business error 002")(
      "GB0244638982000",
      "sub09/businessErrorExample-InvalidData-200-002.json",
      OK
    )

    testGetSubscription("return 200 - business error 003")(
      "GB0344638982000",
      "sub09/businessErrorExample-InvalidData-200-003.json",
      OK
    )

    testGetSubscription("return 200 - business error 004")(
      "GB0444638982000",
      "sub09/businessErrorExample-InvalidData-200-004.json",
      OK
    )

    testGetSubscription("return 200 - business error 005")(
      "GB0544638982000",
      "sub09/businessErrorExample-InvalidData-200-005.json",
      OK
    )

    testGetSubscription("return 200 - business error 037")(
      "GB3744638982000",
      "sub09/businessErrorExample-InvalidData-200-037.json",
      OK
    )

    testGetSubscription("return 400 - business error")(
      "GB4044638982000",
      "sub09/businessErrorExample-InvalidData-400.json",
      BAD_REQUEST
    )

    testGetSubscription("return 400 - business error ResponseNotReturnedFromBackend")(
      "GB4144638982000",
      "sub09/businessErrorExample-SubscriptionDisplay-ResponseNotReturnedFromBackend.json",
      NOT_FOUND
    )

    testGetSubscription("return 400 - companyInformationErrorResponse")(
      "GB4244638982000",
      "sub09/companyInformationErrorResponse.json",
      BAD_REQUEST
    )

    testGetSubscription("return 500 - companyInformationErrorResponse")(
      "GB5044638982000",
      "sub09/systemErrorExample-Timeout.json",
      INTERNAL_SERVER_ERROR
    )
  }

  def app: Application = GuiceApplicationBuilder().configure("metrics.enabled" -> false).build()

  def createValidGetSubscriptionRequest(eori: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(
      "GET",
      routes.SUB09Controller.getSubscription(eori, "CDS", "01234567890123456789012345678901").url
    )
      .withHeaders(headers: _*)

  def testGetSubscription(
    expectedResponse: String
  )(testEori: String, expectedResponseFileName: String, expectedStatusCode: Int): Unit =
    expectedResponse when {
      s"EORI is $testEori is supplied in request" in {
        running(app) {
          val response = route(
            app,
            createValidGetSubscriptionRequest(testEori)
              .withHeaders("Authorization" -> "Bearer test1234567")
          ).value
          status(response) mustBe expectedStatusCode
          contentType(response) mustBe Some(ContentTypes.JSON)
          contentAsString(response) mustEqual jsonDataFromFile(expectedResponseFileName).toString()
        }
      }
    }
}
