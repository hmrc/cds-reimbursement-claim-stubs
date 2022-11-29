/*
 * Copyright 2022 HM Revenue & Customs
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
      ResolvedRefused,
      PendingPaymentConfirmation,
      ResolvedApproved,
      ResolvedPartialRefused,
      PendingDecisionLetter,
      Approved,
      AnalysisRework,
      ReworkPaymentDetails,
      PendingRTBH,
      RTBHSent,
      PendingComplianceRecommendation,
      PendingComplianceCheckQuery,
      PendingComplianceCheck,
      ReplyToRTBH)

    val ndrcCases = ndrcCaseStatuses.zipWithIndex.flatMap { case (caseSubStatus, index) =>
      Seq(
        createNDRCCase(s"200$index".toInt, caseSubStatus),
      )
    }

    val ndrcLength = ndrcCaseStatuses.length

    val sctyCaseStatuses : Seq[CaseSubStatusSCTY] = Seq(
      OpenSCTY,
      PendingApprovalSCTY,
      PendingPayment,
      PartialRefund,
      ResolvedRefund,
      ResolvedWithdrawnSCTY,
      PendingQuery,
      ResolvedManualBTA,
      PendingC18,
      ClosedC18Raised,
      RTBHLetterInitiated,
      AwaitingRTBHLetterResponse,
      AwaitingReminderLetterResponse,
      DecisionLetterInitiated,
      PartialBTA,
      PartialBTARefund,
      ResolvedAutoBTA,
      ResolvedManualBTARefund,
      OpenExtensionGranted)

    val sctyCases = sctyCaseStatuses.zipWithIndex.flatMap { case (caseSubStatus, index) =>
      Seq(
        createSCTYCase(s"200${ndrcLength + index}".toInt, caseSubStatus),
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
  def tpi01SetCaseSubStatusNDRC(index: Int, caseSubStatus: CaseSubStatusNDRC): ResponseDetail = {

    val ndrcCases = Seq(createNDRCCase(index, caseSubStatus))

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

  def tpi01SetCaseSubStatusSCTY(index: Int, caseSubStatus: CaseSubStatusSCTY): ResponseDetail = {

    val ndrcCases = Seq()

    val sctyCases = Seq(createSCTYCase(index, caseSubStatus))

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
  def caseSubStatusNDRC(caseSubStatusNDRC: String): CaseSubStatusNDRC =
    caseSubStatusNDRC match {
      case "1" => OpenNDRC
      case "2" => OpenAnalysis
      case "3" => PendingApprovalNDRC
      case "4" => PendingQueried
      case "5" => ResolvedWithdrawnNDRC
      case "6" => RejectedFailedValidation
      case "7" => ResolvedRejected
      case "8" => OpenRework
      case "9" => Paused
      case "10" => ResolvedNoReply
      case "11" => ResolvedRefused
      case "12" => PendingPaymentConfirmation
      case "13" => ResolvedApproved
      case "14" => ResolvedPartialRefused
      case "15" => PendingDecisionLetter
      case "16" => Approved
      case "17" => AnalysisRework
      case "18" => ReworkPaymentDetails
      case "19" => PendingRTBH
      case "20" => RTBHSent
      case "21" => PendingComplianceRecommendation
      case "22" => PendingComplianceCheckQuery
      case "23" => PendingComplianceCheck
      case "24" => ReplyToRTBH
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
  case object PendingRTBH extends CaseSubStatusNDRC {
    override def toString: String = "Pending-RTBH"

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
  def caseSubStatusSCTY(caseSubStatusSCTY: String): CaseSubStatusSCTY =
    caseSubStatusSCTY match {
      case "1" => OpenSCTY
      case "2" => PendingApprovalSCTY
      case "3" => PendingPayment
      case "4" => PartialRefund
      case "5" => ResolvedRefund
      case "6" => ResolvedWithdrawnSCTY
      case "7" => PendingQuery
      case "8" => ResolvedManualBTA
      case "9" => PendingC18
      case "10" => ClosedC18Raised
      case "11" => RTBHLetterInitiated
      case "12" => AwaitingRTBHLetterResponse
      case "13" => ReminderLetterInitiated
      case "14" => AwaitingReminderLetterResponse
      case "15" => DecisionLetterInitiated
      case "16" => PartialBTA
      case "17" => PartialBTARefund
      case "18" => ResolvedAutoBTA
      case "19" => ResolvedManualBTARefund
      case "20" => OpenExtensionGranted
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

  private def createNDRCCase(value: Int, subStatus: CaseSubStatusNDRC): NDRCCaseDetails = {
    val eori = f"GB$value%012d"
    NDRCCaseDetails(
      s"NDRC-${value.toString}",
      Some("MRN23014"),
      "20200501",
      if (subStatus.caseStatus == Closed)
        Some("20210501")
      else None,
      subStatus.toString,
      eori,
      eori,
      Some(eori),
      Some(s"${value}00.00"),
      Some(s"${value}00.00"),
      Some(s"${value}00.00"),
      Some("KWMREF1"),
      Some("Duplicate Entry")
    )
  }

  private def createSCTYCase(value: Int, subStatus: CaseSubStatusSCTY): SCTYCaseDetails = {
    val eori = f"GB$value%012d"
    SCTYCaseDetails(
      s"SCTY-${value.toString}",
      Some("123456789A12122022"),
      LocalDate.now().minusDays(value).format(DateTimeFormatter.ofPattern("yyyyMMdd")),
      if (subStatus.caseStatus == Closed) Some(LocalDate.now().minusDays(value).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
      else None,
      "ACS",
      subStatus.toString,
      eori,
      eori,
      Some(eori),
      Some(s"${value}00.00"),
      Some(s"${value}00.00"),
      Some("KWMREF1")
    )
  }
}
