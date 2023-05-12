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

package uk.gov.hmrc.cdsreimbursementclaimstubs.utils

import cats.implicits.catsSyntaxEq
import play.api.libs.json.Json
import play.api.mvc.Result
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi01.{PostClearanceCasesResponse, Response, ResponseCommon, TPI01Generation}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Claim.{NDRC_CASE_WITH_CASE_STATUS_CLOSED, NDRC_CASE_WITH_CASE_STATUS_OPEN, SCTY_CASE_WITH_CASE_STATUS_CLOSED, SCTY_CASE_WITH_CASE_STATUS_OPEN};

case class Customer(eori: String, claims: List[Claim] = List()) extends TPI01Generation {

  // TPI01 Response
  val toTPI01: Result = {
    val detail = tpi01ClaimsWithEori(this);
    val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
    val response = Response(PostClearanceCasesResponse(responseCommon, Some(detail)))
    validateResponse("tpi01/tpi01-response-schema.json", Json.toJson(response))
  }

  // TPI02 Response
  def toTPI02(cdfPayCaseNumber: String): Result = {
    val maybeClaim: Option[Claim] = claims.find(claim => claim.caseNumber === cdfPayCaseNumber)
    val claim: Claim = maybeClaim.getOrElse(throw new Error("Claim not found"))
    claim.toTPI02
  }
}

object Customer {
  val ALL_POSSIBLE_CASE_STATUS: Customer = Customer.withAllPossibleClaims("GB744638982000")
  val NO_CLAIMS: Customer = Customer("GB744638982001")
  val NDRC_CASES_CLOSED_AND_IN_PROGRESS: Customer = Customer.withClaims(
    "GB744638982002",
    NDRC_CASE_WITH_CASE_STATUS_OPEN,
    NDRC_CASE_WITH_CASE_STATUS_CLOSED
  )

  val get: List[Customer] = List(
    ALL_POSSIBLE_CASE_STATUS,
    NO_CLAIMS,
    NDRC_CASES_CLOSED_AND_IN_PROGRESS
  )

  def withAllPossibleClaims(eori: String): Customer = {
    val claims: List[Claim] =  List(
      NDRC_CASE_WITH_CASE_STATUS_OPEN,
      NDRC_CASE_WITH_CASE_STATUS_CLOSED,
      SCTY_CASE_WITH_CASE_STATUS_OPEN,
      SCTY_CASE_WITH_CASE_STATUS_CLOSED
    )
    Customer(eori, claims)
  }

  val withEveryCaseStatus: Customer = {
    // Add multiple claims
    //    Claim.generateClaim()
    ???
  }

  def withClaims(eori: String, claims: Claim*): Customer = {
    Customer(eori, claims.toList)
  }

  def exists(eori: String): Boolean = get.exists(customer => customer.eori === eori)

  def find(eori: String): Result =
    get.find(customer => customer.eori === eori).getOrElse(throw new Error("Customer not found")).toTPI01
}
