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
import play.api.mvc.Result
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi01.{CaseSubStatusNDRC, CaseSubStatusSCTY, OpenNDRC, OpenSCTY, ResolvedApproved, ResolvedRefund}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi02.TPI02Generation
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.{NDRC, Service}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.ClaimUtils._

trait Claim extends TPI02Generation{
  val claimType: String
  val caseNumber: String
  val service: Service
  val caseStatus: String
  val closed: Boolean
  val multiple: Boolean
  val entryNumber: Boolean

  val toTPI02: Result
}

case class NDRCClaim(
                      claimType: String,
                      caseNumber: String,
                      caseSubStatus: CaseSubStatusNDRC,
                      closed: Boolean,
                      multiple: Boolean,
                      entryNumber: Boolean
                    ) extends Claim {
  val service: Service = NDRC
  val caseStatus: String = caseSubStatus.caseStatus.toString
  val toTPI02: Result = {
    tpi02Claim(claimType, service, caseNumber, caseStatus, closed, multiple, entryNumber)
  }
}

case class SCTYClaim(
                      claimType: String,
                      caseNumber: String,
                      caseSubStatus: CaseSubStatusSCTY,
                      closed: Boolean,
                      multiple: Boolean,
                      entryNumber: Boolean
                    ) extends Claim {
  val service: Service = NDRC
  val caseStatus: String = caseSubStatus.caseStatus.toString
  val toTPI02: Result = {
    tpi02Claim(claimType, service, caseNumber, caseStatus, closed, multiple, entryNumber)
  }
}

object Claim {

  val NDRC_CASE_WITH_CASE_STATUS_OPEN: NDRCClaim = NDRCClaim(C285, "NDRC-9000", OpenNDRC, closed = false, multiple = false, entryNumber = true)
  val NDRC_CASE_WITH_CASE_STATUS_CLOSED: NDRCClaim = NDRCClaim(CnE1179, "NDRC-9001", ResolvedApproved, closed = false, multiple = false, entryNumber = true)
  val SCTY_CASE_WITH_CASE_STATUS_OPEN: SCTYClaim = SCTYClaim(C285, "SCTY-9000", OpenSCTY, closed = false, multiple = false, entryNumber = true)
  val SCTY_CASE_WITH_CASE_STATUS_CLOSED: SCTYClaim = SCTYClaim(CnE1179, "SCTY-9001", ResolvedRefund, closed = false, multiple = false, entryNumber = true)

  val get: List[Claim] = List(
    NDRC_CASE_WITH_CASE_STATUS_OPEN,
    NDRC_CASE_WITH_CASE_STATUS_CLOSED,
    SCTY_CASE_WITH_CASE_STATUS_OPEN,
    SCTY_CASE_WITH_CASE_STATUS_CLOSED
  )

  def exists(cdfPayCaseNumber: String): Boolean = get.exists(claim => claim.caseNumber === cdfPayCaseNumber)

  def find(cdfPayCaseNumber: String): Result =
    get
      .find(claim => claim.caseNumber === cdfPayCaseNumber)
      .map(e => e.toTPI02)
      .getOrElse(NO_CLAIMS_FOUND)


}
