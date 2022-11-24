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
        createNDRCCase(index, Open),
        createNDRCCase(index + amountOfEach, PendingQueried),
        createNDRCCase(index + (amountOfEach * 2), NDRCClosed)
      )
    }

    val newIndex = amountOfEach * 3

    val sctyCases = (newIndex until newIndex + amountOfEach).flatMap { index =>
      Seq(
        createSCTYCase(index, Open),
        createSCTYCase(index + amountOfEach, Pending),
        createSCTYCase(index + (amountOfEach * 2), SCTYClosed)
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
  

  def tpi01SetCaseStatus(index: Int, caseStatus: CaseStatus): ResponseDetail = {

    val ndrcCases = Seq(createNDRCCase(s"100$index".toInt, caseStatus))

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

  sealed trait CaseStatus

  def caseStatusNDRC(caseStatus: String): CaseStatus =
    caseStatus match {
      case "1" => Open
      case "2" => OpenAnalysis
      case "3" => PendingApproval
      case "4" => PendingQueried
      case "5" => ResolvedWithdrawn
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
    }
  
    def caseStatusSCTY(caseStatus: String): CaseStatus =
    caseStatus match {
      case "1" => Open
      case "2" => PendingApproval
      case "3" => PendingPayment
      case "4" => PartialRefund
      case "5" => ResolvedRefund
      case "6" => ResolvedWithdrawn
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

  case object Open extends CaseStatus {
    override def toString: String = "Open"
  }

  case object Pending extends CaseStatus {
    override def toString: String = "Pending-Approval"
  }

  case object SCTYClosed extends CaseStatus {
    override def toString: String = "Closed-C18 Raised"
  }

  case object NDRCClosed extends CaseStatus {
    override def toString: String = "Resolved-Approved"
  }

  case object OpenAnalysis extends CaseStatus {
    override def toString: String = "Open-Analysis"
  }

  case object PendingApproval extends CaseStatus {
    override def toString: String = "Pending-Approval"
  }

  case object PendingQueried extends CaseStatus {
    override def toString: String = "Pending-Queried"
  }

  case object ResolvedWithdrawn extends CaseStatus {
    override def toString: String = "Resolved-Withdrawn"
  }

  case object RejectedFailedValidation extends CaseStatus {
    override def toString: String = "Rejected-Failed Validation"
  }

  case object ResolvedRejected extends CaseStatus {
    override def toString: String = "Resolved-Rejected"
  }

  case object OpenRework extends CaseStatus {
    override def toString: String = "Open-Rework"
  }

  case object Paused extends CaseStatus {
    override def toString: String = "Paused"
  }

  case object ResolvedNoReply extends CaseStatus {
    override def toString: String = "Resolved-No Reply"
  }

  case object ResolvedRefused extends CaseStatus {
    override def toString: String = "Resolved-Refused"
  }

  case object PendingPaymentConfirmation extends CaseStatus {
    override def toString: String = "Pending Payment Confirmation"
  }
  case object ResolvedApproved extends CaseStatus {
    override def toString: String = "Resolved-Approved"
  }

  case object ResolvedPartialRefused extends CaseStatus {
    override def toString: String = "Resolved-Partial Refused"
  }

  case object PendingDecisionLetter extends CaseStatus {
    override def toString: String = "Pending Decision Letter"
  }

  case object Approved extends CaseStatus {
    override def toString: String = "Approved"
  }

  case object AnalysisRework extends CaseStatus {
    override def toString: String = "Analysis-Rework"
  }

  case object ReworkPaymentDetails extends CaseStatus {
    override def toString: String = "Rework-Payment Details"
  }

  case object PendingRTBH extends CaseStatus {
    override def toString: String = "Pending-RTBH"
  }

  case object RTBHSent extends CaseStatus {
    override def toString: String = "RTBH-Sent"
  }

  case object ReplyToRTBH extends CaseStatus {
    override def toString: String = "Reply To RTBH"
  }

  case object PendingComplianceRecommendation extends CaseStatus {
    override def toString: String = "Pending-Compliance Recommendation"
  }

  case object PendingComplianceCheckQuery extends CaseStatus {
    override def toString: String = "Pending-Compliance Check Query"
  }

  case object PendingComplianceCheck extends CaseStatus {
    override def toString: String = "Pending-Compliance Check"
  }
   case object PendingPayment extends CaseStatus {
    override def toString: String = "Pending-Payment"
  }
   case object PartialRefund extends CaseStatus {
    override def toString: String = "Partial Refund"
  }
  case object ResolvedRefund extends CaseStatus {
    override def toString: String = "Resolved-Refund"
  }
  case object PendingQuery extends CaseStatus {
    override def toString: String = "Pending-Query"
  }
  case object ResolvedManualBTA extends CaseStatus {
    override def toString: String = "Resolved-Manual BTA"
  }
  case object PendingC18 extends CaseStatus {
    override def toString: String = "Pending-C18"
  }
  case object ClosedC18Raised extends CaseStatus {
    override def toString: String = "Closed-C18 Raised"
  }
  case object RTBHLetterInitiated extends CaseStatus {
    override def toString: String = "RTBH Letter Initiated"
  }
  case object AwaitingRTBHLetterResponse extends CaseStatus {
    override def toString: String = "Awaiting RTBH Letter Response"
  }
  case object ReminderLetterInitiated extends CaseStatus {
    override def toString: String = "Reminder Letter Initiated"
  }
  case object AwaitingReminderLetterResponse extends CaseStatus {
    override def toString: String = "Awaiting Reminder Letter Response"
  }
  case object DecisionLetterInitiated extends CaseStatus {
    override def toString: String = "Decision Letter Initiated"
  }
  case object PartialBTA extends CaseStatus {
    override def toString: String = "Partial BTA"
  }
  case object PartialBTARefund extends CaseStatus {
    override def toString: String = "Partial BTA/Refund"
  }
  case object ResolvedAutoBTA extends CaseStatus {
    override def toString: String = "Resolved-Auto BTA"
  }
  case object ResolvedManualBTARefund extends CaseStatus {
    override def toString: String = "Resolved-Manual BTA/Refund"
  }
  case object OpenExtensionGranted extends CaseStatus {
    override def toString: String = "Open-Extension Granted"
  }    
  
  private def createNDRCCase(value: Int, status: CaseStatus): NDRCCaseDetails = {
    val eori = f"GB$value%012d"
    NDRCCaseDetails(
      s"NDRC-${value.toString}",
      Some("MRN23014"),
      "20200501",
      if (
        status == NDRCClosed || status == ResolvedWithdrawn || status == RejectedFailedValidation || status == ResolvedRejected ||
        status == ResolvedNoReply || status == ResolvedRefused || status == ResolvedApproved || status == ResolvedPartialRefused
      )
        Some("20210501")
      else None,
      status.toString,
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

  private def createSCTYCase(value: Int, status: CaseStatus): SCTYCaseDetails = {
    val eori = f"GB$value%012d"
    SCTYCaseDetails(
      s"SCTY-${value.toString}",
      Some("123456789A12122022"),
      LocalDate.now().minusDays(value).format(DateTimeFormatter.ofPattern("yyyyMMdd")),
      if (status == SCTYClosed) Some(LocalDate.now().minusDays(value).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
      else None,
      "ACS",
      status.toString,
      eori,
      eori,
      Some(eori),
      Some(s"${value}00.00"),
      Some(s"${value}00.00"),
      Some("KWMREF1")
    )
  }
}
