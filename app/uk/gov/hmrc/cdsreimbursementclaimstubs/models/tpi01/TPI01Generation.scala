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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi01

import uk.gov.hmrc.cdsreimbursementclaimstubs.models.SchemaValidation

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait TPI01Generation extends SchemaValidation {

  def tpi01Claims(amountOfEach: Int): ResponseDetail = {

    val ndrcCases = (1 to amountOfEach).flatMap { index =>
      Seq(
        createNDRCCase(index, OpenNDRC),
        createNDRCCase(index + amountOfEach, PendingQueried),
        createNDRCCase(index + (amountOfEach * 2), ResolvedApproved)
      )
    }

    val newIndex = amountOfEach * 3

    val sctyCases = (newIndex until newIndex + amountOfEach).flatMap { index =>
      Seq(
        createSCTYCase(index, OpenSCTY),
        createSCTYCase(index + amountOfEach, PendingC18),
        createSCTYCase(index + (amountOfEach * 2), ClosedC18Raised)
      )
    }

    val responseDetail = ResponseDetail(
      NDRCCasesFound = true,
      SCTYCasesFound = true,
      CDFPayCase = CDFPayCase(
        ndrcCases.size.toString,
        sctyCases.size.toString,
        ndrcCases,
        sctyCases
      )
    )

    responseDetail
  }

  def tpi01AllSubstatusClaims(): ResponseDetail = {
    val ndrcCaseStatuses: Seq[CaseSubStatusNDRC] = Seq(
      OpenNDRC,
      OpenAnalysis,
      PendingApprovalNDRC,
      PendingQueried,
      ResolvedWithdrawnNDRC,
      RejectedFailedValidation,
      ResolvedRejected,
      OpenRework,
      Paused,
      ResolvedNoReply,
      RTBHSent,
      ResolvedRefused,
      PendingPaymentConfirmation,
      ResolvedApproved,
      ResolvedPartialRefused,
      PendingDecisionLetter,
      Approved,
      AnalysisRework,
      ReworkPaymentDetails,
      ReplyToRTBH,
      PendingComplianceRecommendation,
      PendingComplianceCheckQuery,
      PendingComplianceCheck
    )

    val ndrcCases = ndrcCaseStatuses.zipWithIndex.flatMap { case (caseSubStatus, index) =>
      val caseNumber = s"200${index + 1}".toInt
      Seq(
        createNDRCCase(caseNumber, caseSubStatus)
      )
    }

    val sctyCaseStatuses: Seq[CaseSubStatusSCTY] = Seq(
      OpenSCTY,
      PendingApprovalSCTY,
      PendingPayment,
      PartialRefund,
      ResolvedRefund,
      PendingQuery,
      ResolvedManualBTA,
      PendingC18,
      ClosedC18Raised,
      RTBHLetterInitiated,
      AwaitingRTBHLetterResponse,
      ReminderLetterInitiated,
      AwaitingReminderLetterResponse,
      DecisionLetterInitiated,
      PartialBTA,
      PartialBTARefund,
      ResolvedAutoBTA,
      ResolvedManualBTARefund,
      OpenExtensionGranted,
      ResolvedWithdrawnSCTY
    )

    val sctyCases = sctyCaseStatuses.zipWithIndex.flatMap { case (caseSubStatus, index) =>
      val caseNumber = s"200${index + 1}".toInt
      Seq(
        createSCTYCase(caseNumber, caseSubStatus)
      )
    }

    val responseDetail = ResponseDetail(
      NDRCCasesFound = true,
      SCTYCasesFound = true,
      CDFPayCase = CDFPayCase(
        ndrcCases.size.toString,
        sctyCases.size.toString,
        ndrcCases,
        sctyCases
      )
    )

    responseDetail
  }

  def tpi01SetCaseSubStatusNDRC(caseSubStatusIndex: Int): ResponseDetail = {

    val caseNumber    = s"100$caseSubStatusIndex".toInt
    val caseSubStatus = caseSubStatusNDRC(caseSubStatusIndex)
    val ndrcCases     = Seq(createNDRCCase(caseNumber, caseSubStatus))

    val sctyCases = Seq()

    val responseDetail = ResponseDetail(
      NDRCCasesFound = true,
      SCTYCasesFound = true,
      CDFPayCase = CDFPayCase(
        ndrcCases.size.toString,
        sctyCases.size.toString,
        ndrcCases,
        sctyCases
      )
    )

    responseDetail
  }

  def tpi01SetCaseSubStatusSCTY(caseSubStatusIndex: Int): ResponseDetail = {

    val ndrcCases = Seq()

    val caseSubStatus = caseSubStatusSCTY(caseSubStatusIndex)
    val caseNumber    = s"100$caseSubStatusIndex".toInt
    val sctyCases     = Seq(createSCTYCase(caseNumber, caseSubStatus))

    val responseDetail = ResponseDetail(
      NDRCCasesFound = true,
      SCTYCasesFound = true,
      CDFPayCase = CDFPayCase(
        ndrcCases.size.toString,
        sctyCases.size.toString,
        ndrcCases,
        sctyCases
      )
    )

    responseDetail
  }

  sealed trait CaseStatus
  case object InProgress extends CaseStatus {
    override def toString: String = "In progress"
  }
  case object Pending extends CaseStatus {
    override def toString: String = "Pending"
  }
  case object Closed extends CaseStatus {
    override def toString: String = "Closed"
  }
  sealed trait CaseSubStatusNDRC {
    val caseStatus: CaseStatus
  }
  def caseSubStatusNDRC(caseSubStatusNDRC: Int): CaseSubStatusNDRC =
    modulo1(caseSubStatusNDRC, 24) match {
      case 1 => OpenNDRC
      case 2 => OpenAnalysis
      case 3 => PendingApprovalNDRC
      case 4 => PendingQueried
      case 5 => ResolvedWithdrawnNDRC
      case 6 => RejectedFailedValidation
      case 7 => ResolvedRejected
      case 8 => OpenRework
      case 9 => Paused
      case 10 => ResolvedNoReply
      case 11 => RTBHSent
      case 12 => ResolvedRefused
      case 13 => PendingPaymentConfirmation
      case 14 => ResolvedApproved
      case 15 => ResolvedPartialRefused
      case 16 => PendingDecisionLetter
      case 17 => Approved
      case 18 => AnalysisRework
      case 19 => ReworkPaymentDetails
      case 20 => ReworkPaymentDetails
      case 21 => ReplyToRTBH
      case 22 => PendingComplianceRecommendation
      case 23 => PendingComplianceCheckQuery
      case 24 => PendingComplianceCheck
    }
  case object OpenNDRC extends CaseSubStatusNDRC {
    override def toString: String = "Open"

    override val caseStatus: CaseStatus = InProgress
  }
  case object OpenAnalysis extends CaseSubStatusNDRC {
    override def toString: String = "Open-Analysis"

    override val caseStatus: CaseStatus = InProgress

  }
  case object PendingApprovalNDRC extends CaseSubStatusNDRC {
    override def toString: String = "Pending-Approval"

    override val caseStatus: CaseStatus = Pending

  }
  case object PendingQueried extends CaseSubStatusNDRC {
    override def toString: String = "Pending-Queried"

    override val caseStatus: CaseStatus = Pending

  }
  case object ResolvedWithdrawnNDRC extends CaseSubStatusNDRC {
    override def toString: String = "Resolved-Withdrawn"

    override val caseStatus: CaseStatus = Closed

  }
  case object RejectedFailedValidation extends CaseSubStatusNDRC {
    override def toString: String = "Rejected-Failed Validation"

    override val caseStatus: CaseStatus = Closed

  }
  case object ResolvedRejected extends CaseSubStatusNDRC {
    override def toString: String = "Resolved-Rejected"

    override val caseStatus: CaseStatus = Closed

  }
  case object OpenRework extends CaseSubStatusNDRC {
    override def toString: String = "Open-Rework"

    override val caseStatus: CaseStatus = InProgress

  }
  case object Paused extends CaseSubStatusNDRC {
    override def toString: String = "Paused"

    override val caseStatus: CaseStatus = InProgress

  }
  case object ResolvedNoReply extends CaseSubStatusNDRC {
    override def toString: String = "Resolved-No Reply"

    override val caseStatus: CaseStatus = Closed

  }
  case object ResolvedRefused extends CaseSubStatusNDRC {
    override def toString: String = "Resolved-Refused"

    override val caseStatus: CaseStatus = Closed

  }
  case object PendingPaymentConfirmation extends CaseSubStatusNDRC {
    override def toString: String = "Pending Payment Confirmation"

    override val caseStatus: CaseStatus = InProgress

  }
  case object ResolvedApproved extends CaseSubStatusNDRC {
    override def toString: String = "Resolved-Approved"

    override val caseStatus: CaseStatus = Closed

  }
  case object ResolvedPartialRefused extends CaseSubStatusNDRC {
    override def toString: String = "Resolved-Partial Refused"

    override val caseStatus: CaseStatus = Closed

  }
  case object PendingDecisionLetter extends CaseSubStatusNDRC {
    override def toString: String = "Pending Decision Letter"

    override val caseStatus: CaseStatus = InProgress

  }
  case object Approved extends CaseSubStatusNDRC {
    override def toString: String = "Approved"

    override val caseStatus: CaseStatus = InProgress

  }
  case object AnalysisRework extends CaseSubStatusNDRC {
    override def toString: String = "Analysis-Rework"

    override val caseStatus: CaseStatus = InProgress

  }
  case object ReworkPaymentDetails extends CaseSubStatusNDRC {
    override def toString: String = "Rework-Payment Details"

    override val caseStatus: CaseStatus = InProgress

  }

  case object RTBHSent extends CaseSubStatusNDRC {
    override def toString: String = "RTBH-Sent"

    override val caseStatus: CaseStatus = Pending

  }
  case object PendingComplianceRecommendation extends CaseSubStatusNDRC {
    override def toString: String = "Pending-Compliance Recommendation"

    override val caseStatus: CaseStatus = InProgress

  }
  case object PendingComplianceCheckQuery extends CaseSubStatusNDRC {
    override def toString: String = "Pending-Compliance Check Query"

    override val caseStatus: CaseStatus = Pending

  }
  case object PendingComplianceCheck extends CaseSubStatusNDRC {
    override def toString: String = "Pending-Compliance Check"

    override val caseStatus: CaseStatus = InProgress

  }
  case object ReplyToRTBH extends CaseSubStatusNDRC {
    override def toString: String = "Reply To RTBH"

    override val caseStatus: CaseStatus = Pending

  }
  sealed trait CaseSubStatusSCTY {
    val caseStatus: CaseStatus
  }
  def caseSubStatusSCTY(caseSubStatusSCTY: Int): CaseSubStatusSCTY =
    modulo1(caseSubStatusSCTY, 20) match {
      case 1 => OpenSCTY
      case 2 => PendingApprovalSCTY
      case 3 => PendingPayment
      case 4 => PartialRefund
      case 5 => ResolvedRefund
      case 6 => PendingQuery
      case 7 => ResolvedManualBTA
      case 8 => PendingC18
      case 9 => ClosedC18Raised
      case 10 => RTBHLetterInitiated
      case 11 => AwaitingRTBHLetterResponse
      case 12 => ReminderLetterInitiated
      case 13 => AwaitingReminderLetterResponse
      case 14 => DecisionLetterInitiated
      case 15 => PartialBTA
      case 16 => PartialBTARefund
      case 17 => ResolvedAutoBTA
      case 18 => ResolvedManualBTARefund
      case 19 => OpenExtensionGranted
      case 20 => ResolvedWithdrawnSCTY
    }
  case object OpenSCTY extends CaseSubStatusSCTY {
    override def toString: String = "Open"

    override val caseStatus: CaseStatus = InProgress

  }
  case object PendingApprovalSCTY extends CaseSubStatusSCTY {
    override def toString: String = "Pending-Approval"

    override val caseStatus: CaseStatus = InProgress

  }
  case object PendingPayment extends CaseSubStatusSCTY {
    override def toString: String = "Pending-Payment"

    override val caseStatus: CaseStatus = Pending
  }
  case object PartialRefund extends CaseSubStatusSCTY {
    override def toString: String = "Partial Refund"

    override val caseStatus: CaseStatus = Closed

  }
  case object ResolvedRefund extends CaseSubStatusSCTY {
    override def toString: String = "Resolved-Refund"

    override val caseStatus: CaseStatus = Closed

  }
  case object ResolvedWithdrawnSCTY extends CaseSubStatusSCTY {
    override def toString: String = "Resolved-Withdrawn"

    override val caseStatus: CaseStatus = Closed

  }
  case object PendingQuery extends CaseSubStatusSCTY {
    override def toString: String = "Pending-Query"

    override val caseStatus: CaseStatus = Pending

  }
  case object ResolvedManualBTA extends CaseSubStatusSCTY {
    override def toString: String = "Resolved-Manual BTA"

    override val caseStatus: CaseStatus = Closed

  }
  case object PendingC18 extends CaseSubStatusSCTY {
    override def toString: String = "Pending-C18"

    override val caseStatus: CaseStatus = Pending

  }
  case object ClosedC18Raised extends CaseSubStatusSCTY {
    override def toString: String = "Closed-C18 Raised"

    override val caseStatus: CaseStatus = Closed

  }
  case object RTBHLetterInitiated extends CaseSubStatusSCTY {
    override def toString: String = "RTBH Letter Initiated"

    override val caseStatus: CaseStatus = Pending

  }
  case object AwaitingRTBHLetterResponse extends CaseSubStatusSCTY {
    override def toString: String = "Awaiting RTBH Letter Response"

    override val caseStatus: CaseStatus = Pending

  }
  case object ReminderLetterInitiated extends CaseSubStatusSCTY {
    override def toString: String = "Reminder Letter Initiated"

    override val caseStatus: CaseStatus = Pending

  }
  case object AwaitingReminderLetterResponse extends CaseSubStatusSCTY {
    override def toString: String = "Awaiting Reminder Letter Response"

    override val caseStatus: CaseStatus = Pending

  }
  case object DecisionLetterInitiated extends CaseSubStatusSCTY {
    override def toString: String = "Decision Letter Initiated"

    override val caseStatus: CaseStatus = Pending

  }
  case object PartialBTA extends CaseSubStatusSCTY {
    override def toString: String = "Partial BTA"

    override val caseStatus: CaseStatus = Pending

  }
  case object PartialBTARefund extends CaseSubStatusSCTY {
    override def toString: String = "Partial BTA/Refund"

    override val caseStatus: CaseStatus = Pending

  }
  case object ResolvedAutoBTA extends CaseSubStatusSCTY {
    override def toString: String = "Resolved-Auto BTA"

    override val caseStatus: CaseStatus = Closed

  }
  case object ResolvedManualBTARefund extends CaseSubStatusSCTY {
    override def toString: String = "Resolved-Manual BTA/Refund"

    override val caseStatus: CaseStatus = Closed

  }
  case object OpenExtensionGranted extends CaseSubStatusSCTY {
    override def toString: String = "Open-Extension Granted"

    override val caseStatus: CaseStatus = InProgress

  }

  private def createNDRCCase(caseNumber: Int, subStatus: CaseSubStatusNDRC): NDRCCaseDetails = {
    val eori = f"GB$caseNumber%012d"
    NDRCCaseDetails(
      CDFPayCaseNumber = s"NDRC-${caseNumber.toString}",
      declarationID = Some("MRN23014"),
      claimStartDate = "20200501",
      closedDate =
        if (subStatus.caseStatus == Closed)
          Some("20210501")
        else None,
      caseStatus = subStatus.toString,
      declarantEORI = eori,
      importerEORI = eori,
      claimantEORI = Some(eori),
      totalCustomsClaimAmount = Some("9000.00"),
      totalVATClaimAmount = Some("9000.00"),
      totalExciseClaimAmount = Some("9000.00"),
      declarantReferenceNumber = Some("KWMREF1"),
      basisOfClaim = Some("Duplicate Entry")
    )
  }

  private def createSCTYCase(caseNumber: Int, subStatus: CaseSubStatusSCTY): SCTYCaseDetails = {
    val eori = f"GB$caseNumber%012d"
    val odd  = caseNumber % 2 == 1
    SCTYCaseDetails(
      CDFPayCaseNumber = s"SCTY-${caseNumber.toString}",
      declarationID = Some("12AA3456789ABCDEF2"),
      claimStartDate =
        if (odd) None else Some(LocalDate.now().minusDays(caseNumber).format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
      closedDate =
        if (subStatus.caseStatus == Closed)
          Some(LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        else None,
      reasonForSecurity = "ACS",
      caseStatus = subStatus.toString,
      declarantEORI = eori,
      importerEORI = if (odd) None else Some(eori),
      claimantEORI = Some(eori),
      totalCustomsClaimAmount = Some("9000.00"),
      totalVATClaimAmount = Some("9000.00"),
      declarantReferenceNumber = Some("KWMREF1")
    )
  }

  def modulo1(value: Int, size: Int): Int =
    ((value - 1) % size) + 1
}
