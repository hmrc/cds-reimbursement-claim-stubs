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

import akka.actor.ActorSystem
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.request.{BarsAccount, BarsAddress, BarsBusiness, BarsBusinessAssessRequest, BarsPersonalAssessRequest, BarsSubject}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response.ReputationResponse._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response.{BARSResponse2, ReputationErrorResponse}

class BankAccountReputationController2Spec extends AnyWordSpec with Matchers with TypeCheckedTripleEquals {

  implicit val actorSystem = ActorSystem()

  "A BankAccountReputationController" when {
    val controller = new BankAccountReputationController(Helpers.stubControllerComponents())

    "processing a good bars personal assess request" should {
      val jsonAccount: JsValue = Json.toJson(
        BarsPersonalAssessRequest(
          BarsAccount("123456", "12345678"),
          BarsSubject(None, Some("M Test"), None, None, None, Some(BarsAddress(List.empty[String], None, None)))
        )
      )
      val request              = FakeRequest("POST", "/verify/personal")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyPersonal.apply(request)

      "return 200 (OK)" in {
        status(result) should ===(OK)
      }
      "return accountNumberIsWellFormatted, sortCodeIsPresentOnEISCD, accountExists" in {
        contentAsJson(result).as[BARSResponse2] should ===(BARSResponse2(Yes, Yes, Some(Yes), None, Some(Yes)))
      }
    }

    "processing a good bars personal assess request with partial name match" should {
      val jsonAccount: JsValue = Json.toJson(
        BarsPersonalAssessRequest(
          BarsAccount("123456", "11001011"),
          BarsSubject(None, Some("M Test"), None, None, None, Some(BarsAddress(List.empty[String], None, None)))
        )
      )
      val request              = FakeRequest("POST", "/verify/personal")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyPersonal.apply(request)

      "return 200 (OK)" in {
        status(result) should ===(OK)
      }
      "return accountNumberIsWellFormatted, sortCodeIsPresentOnEISCD, accountExists" in {
        contentAsJson(result).as[BARSResponse2] should ===(
          BARSResponse2(Indeterminate, Yes, Some(Yes), Some("M TEST"), Some(Partial))
        )
      }
    }

    "processing a good bars business assess request" should {
      val jsonAccount =
        Json.toJson(BarsBusinessAssessRequest(BarsAccount("654321", "87654321"), Option.empty[BarsBusiness]))
      val request     = FakeRequest("POST", "/verify/business")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result      = controller.verifyBusiness.apply(request)

      "return 200 (OK)" in {
        status(result) should ===(OK)
      }
      "return accountNumberIsWellFormatted, sortCodeIsPresentOnEISCD, accountExists" in {
        contentAsJson(result).as[BARSResponse2] should ===(BARSResponse2(Yes, Yes, Some(Yes), None, Some(Yes)))
      }
    }

    "processing a malformed bars personal bank assess request" should {
      val jsonAccount = Json.toJson((BarsAccount("123456", "12345678"), Option.empty[BarsBusiness]))
      val request     = FakeRequest("POST", "/verify/personal")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result      = controller.verifyBusiness.apply(request)

      "return 400 (BAD_REQUEST)" in {
        status(result) should ===(BAD_REQUEST)
      }
      "return a json error response for personal request parse failure" in {
        contentAsString(
          result
        ) shouldBe "JsResultException(errors:List((,List(JsonValidationError(List(error.expected.jsobject),WrappedArray())))))"
      }
    }

    "processing a malformed bars business bank assess request" should {
      val jsonAccount = Json.toJson((BarsAccount("654321", "87654321"), Option.empty[BarsBusiness]))
      val request     = FakeRequest("POST", "/verify/business")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result      = controller.verifyBusiness.apply(request)

      "return 400 (BAD_REQUEST)" in {
        status(result) should ===(BAD_REQUEST)
      }
      "return a json error response for business request parse failure" in {
        contentAsString(result) should ===(
          "JsResultException(errors:List((,List(JsonValidationError(List(error.expected.jsobject),WrappedArray())))))"
        )
      }
    }

    "processing a special account bars request that generates a 400 from the personal endpoint test stub " should {
      val jsonAccount: JsValue = Json.toJson(
        BarsPersonalAssessRequest(
          BarsAccount("123456", "90909091"),
          BarsSubject(None, Some("M Test"), None, None, None, Some(BarsAddress(List.empty[String], None, None)))
        )
      )
      val request              = FakeRequest("POST", "/verify/personal")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyPersonal.apply(request)

      "return 400 (BAD_REQUEST)" in {
        status(result) should ===(BAD_REQUEST)
      }
      "return an appropriate error response" in {
        contentAsJson(result).as[ReputationErrorResponse] should ===(
          ReputationErrorResponse("BAD_REQUEST", "please check API reference")
        )
      }
    }

    "processing a special account bars request that generates a 400 from the business endpoint test stub " should {
      val jsonAccount: JsValue =
        Json.toJson(BarsBusinessAssessRequest(BarsAccount("123456", "90909091"), Option.empty[BarsBusiness]))
      val request              = FakeRequest("POST", "/verify/business")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyBusiness.apply(request)

      "return 400 (BAD_REQUEST)" in {
        status(result) should ===(BAD_REQUEST)
      }
      "return an appropriate error response" in {
        contentAsJson(result).as[ReputationErrorResponse] should ===(
          ReputationErrorResponse("BAD_REQUEST", "please check API reference")
        )
      }
    }

    "processing a special account bars request that generates a 503 from the personal endpoint test stub " should {
      val jsonAccount: JsValue = Json.toJson(
        BarsPersonalAssessRequest(
          BarsAccount("123456", "90909090"),
          BarsSubject(None, Some("M Test"), None, None, None, Some(BarsAddress(List.empty[String], None, None)))
        )
      )
      val request              = FakeRequest("POST", "/verify/personal")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyPersonal.apply(request)

      "return 503 (SERVICE_UNAVAILABLE)" in {
        status(result) should ===(SERVICE_UNAVAILABLE)
      }
      "return an appropriate error response" in {
        contentAsJson(result).as[ReputationErrorResponse] should ===(
          ReputationErrorResponse("SERVICE_UNAVAILABLE", "please try again later")
        )
      }
    }

    "processing a special account bars request that generates a 503 from the business endpoint test stub " should {
      val jsonAccount: JsValue =
        Json.toJson(BarsBusinessAssessRequest(BarsAccount("123456", "90909090"), Option.empty[BarsBusiness]))
      val request              = FakeRequest("POST", "/verify/business")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyBusiness.apply(request)

      "return 503 (SERVICE_UNAVAILABLE)" in {
        status(result) should ===(SERVICE_UNAVAILABLE)
      }
      "return an appropriate error response" in {
        contentAsJson(result).as[ReputationErrorResponse] should ===(
          ReputationErrorResponse("SERVICE_UNAVAILABLE", "please try again later")
        )
      }
    }

    "processing an non existent account number" should {
      val jsonAccount: JsValue = Json.toJson(
        BarsPersonalAssessRequest(
          BarsAccount("123456", "11004004"),
          BarsSubject(None, Some("M Test"), None, None, None, Some(BarsAddress(List.empty[String], None, None)))
        )
      )
      val request              = FakeRequest("POST", "/verify/personal")
        .withHeaders(("Content-Type", "application/json"))
        .withBody(jsonAccount)
      val result               = controller.verifyPersonal.apply(request)

      "return 200 (OK)" in {
        status(result) should ===(OK)
      }
      "return accountNumberIsWellFormatted = no, sortCodeIsPresentOnEISCD = yes, accountExists = no" in {
        contentAsJson(result).as[BARSResponse2] should ===(BARSResponse2(No, Yes, Some(No), None, None))
      }
    }
  }
}
