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
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.companyinformation._

class CompanyInformationControllerSpec extends AnyWordSpec with Matchers {

  val request = FakeRequest("GET", "/")

  "A CompanyInformationController" when {
    val controller = new CompanyInformationController(Helpers.stubControllerComponents())

    "/customs-data-store/eori/:eori/company-information" should {

      "return 200 (OK) with company information" in {
        val result = controller.getCompanyInformation("GB000000000000001").apply(request)
        status(result)                                       shouldBe OK
        contentAsJson(result).as[CompanyInformationResponse] shouldBe
          CompanyInformationResponse(
            name = "Tony Stark",
            consent = "1",
            address = AddressInformation(
              streetAndNumber = "86 Mysore Road",
              city = "London",
              postalCode = Some("SW11 5RZ"),
              countryCode = "GB"
            )
          )
      }

      "return 404 (NOT FOUND) for GB999999999999990" in {
        val result = controller.getCompanyInformation("GB999999999999990").apply(request)
        status(result) shouldBe NOT_FOUND
      }
    }
  }

}
