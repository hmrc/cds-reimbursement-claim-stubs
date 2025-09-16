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
import com.eclipsesource.schema.SchemaType

class ClaimControllerSpec extends AnyWordSpec with Matchers with SchemaValidation {

  val headers: Seq[(String, String)] = Seq(
    "Date"             -> "Fri, 16 Aug 2019 18:15:41 GMT",
    "X-Correlation-ID" -> "some-id",
    "X-Forwarded-Host" -> "MDTP",
    "Content-Type"     -> "application/json",
    "Accept"           -> "application/json",
    "Authorization"    -> "Bearer test1234567"
  )

  lazy val successResponseSchema = readSchema("tpi05/TPI05-v1-0-10-submit-claim-response-schema.json")

  "submitClaim" should {
    testSubmitClaim("return 200 success response for a valid NDRC claim")(
      "tpi05/FrontEnd_TPI05_Request_Sample_NDRC.json",
      OK,
      successResponseSchema
    )

    testSubmitClaim("return 200 success response for a valid SCTY claim")(
      "tpi05/FrontEnd_TPI05_Request_Sample_SCTY.json",
      OK,
      successResponseSchema
    )

  }

  def app: Application = GuiceApplicationBuilder().configure("metrics.enabled" -> false).build()

  def createValidPostClaimRequest(): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(
      "POST",
      routes.ClaimController.submitClaim.url
    )
      .withHeaders(headers: _*)

  def testSubmitClaim(
    expectedResponse: String
  )(requestPayload: String, expectedStatusCode: Int, responseSchema: SchemaType): Unit =
    expectedResponse in {
      running(app) {
        val response = route(
          app,
          createValidPostClaimRequest()
            .withHeaders("Authorization" -> "Bearer test1234567")
            .withBody(jsonDataFromFile(requestPayload))
        ).value
        status(response) mustBe expectedStatusCode
        contentType(response) mustBe Some(ContentTypes.JSON)
        SchemaValidation.validator
          .validate(responseSchema, contentAsJson(response))
          .fold(
            e => fail(e.toString()),
            _ => ()
          )
      }
    }
}
