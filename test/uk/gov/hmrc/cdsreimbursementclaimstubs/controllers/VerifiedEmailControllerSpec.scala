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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.test.Helpers._
import play.api.test._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.verifiedemail.VerifiedEmailResponse

class VerifiedEmailControllerSpec extends AnyWordSpec with Matchers {

  val request = FakeRequest("GET", "/")

  "A VerifiedEmailController" when {
    val controller = new VerifiedEmailController(Helpers.stubControllerComponents())

    "/customs-data-store/eori/:eori/verified-email " should {

      "return 200 (OK) with verified email address" in {
        val result = controller.getVerifiedEmail("GB000000000000001").apply(request)
        status(result)                                  shouldBe OK
        contentAsJson(result).as[VerifiedEmailResponse] shouldBe
          VerifiedEmailResponse("someemail@mail.com", "2007-03-20T01:02:03.000Z")
      }

      "return 404 (NOT FOUND) for GB999999999999999" in {
        val result = controller.getVerifiedEmail("GB999999999999999").apply(request)
        status(result) shouldBe NOT_FOUND
      }

      "return 404 (NOT FOUND) for NOEMAIL" in {
        val result = controller.getVerifiedEmail("NOEMAIL").apply(request)
        status(result) shouldBe NOT_FOUND
      }
    }
  }

}
