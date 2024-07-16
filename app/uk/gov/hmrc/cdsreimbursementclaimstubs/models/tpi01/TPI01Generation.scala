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

import uk.gov.hmrc.cdsreimbursementclaimstubs.models.{ReasonForSecurity, SchemaValidation}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.{Customer, NDRCClaim, SCTYClaim}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait TPI01Generation extends SchemaValidation {

  def tpi01ClaimsWithEori(customer: Customer): ResponseDetail = {
    val eori: String                          = customer.eori
    val sctyClaims: Seq[SCTYClaim]            = customer.claims
      .filter(_.isInstanceOf[SCTYClaim])
      .map(_.asInstanceOf[SCTYClaim])
    val sctyCaseDetails: Seq[SCTYCaseDetails] = sctyClaims.map(claim => createSCTYCase(claim, eori))

    val ndrcClaims: Seq[NDRCClaim]            = customer.claims
      .filter(_.isInstanceOf[NDRCClaim])
      .map(_.asInstanceOf[NDRCClaim])
    val ndrcCaseDetails: Seq[NDRCCaseDetails] = ndrcClaims.map(claim => createNDRCCase(claim, eori))

    val responseDetail = ResponseDetail(
      NDRCCasesFound = true,
      SCTYCasesFound = true,
      CDFPayCase = CDFPayCase(
        ndrcCaseDetails.size.toString,
        sctyCaseDetails.size.toString,
        ndrcCaseDetails,
        sctyCaseDetails
      )
    )

    responseDetail

  }

  private def createNDRCCase(claim: NDRCClaim, eori: String): NDRCCaseDetails = {
    val caseNumber: Long = claim.caseNumber.toLong;
    NDRCCaseDetails(
      CDFPayCaseNumber = s"NDRC-${(caseNumber.toString + claim.service.toString)}",
      declarationID = Some("MRN23014"),
      claimStartDate = "20200501",
      closedDate =
        if (claim.caseSubStatus.caseStatus == Closed && claim.caseSubStatus != RejectedFailedValidation)
          Some("20210501")
        else None,
      caseStatus = claim.caseStatus,
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

  private def createSCTYCase(claim: SCTYClaim, eori: String): SCTYCaseDetails = {
    val caseNumber: Long = claim.caseNumber.toLong;
    val odd              = caseNumber % 2 == 1
    SCTYCaseDetails(
      CDFPayCaseNumber = s"SCTY-${(caseNumber.toString + claim.service.toString)}",
      declarationID = Some("12AA3456789ABCDEF2"),
      claimStartDate =
        if (odd) None else Some(LocalDate.now().minusDays(caseNumber).format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
      closedDate =
        if (claim.caseSubStatus.caseStatus == Closed)
          Some(LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        else None,
      reasonForSecurity = ReasonForSecurity.ofIndex(caseNumber),
      caseStatus = claim.caseSubStatus.toString,
      declarantEORI = eori,
      importerEORI = if (odd) None else Some(eori),
      claimantEORI = Some(eori),
      totalCustomsClaimAmount = Some("9000.00"),
      totalVATClaimAmount = Some("9000.00"),
      declarantReferenceNumber = Some("KWMREF1")
    )
  }
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

  def tpi01NDRCClaimsByNumberAndStatus(claimNumbers: (Long, CaseSubStatusNDRC)*): ResponseDetail = {

    val ndrcCases = claimNumbers.map { case (index, subStatus) =>
      createNDRCCase(index, subStatus)
    }

    val responseDetail = ResponseDetail(
      NDRCCasesFound = true,
      SCTYCasesFound = false,
      CDFPayCase = CDFPayCase(
        ndrcCases.size.toString,
        "0",
        ndrcCases,
        Seq.empty
      )
    )

    responseDetail
  }

  def tpi01Claims2(totalAmount: Int): ResponseDetail = {

    val ndrcCases = (1 to totalAmount).map { index =>
      createNDRCCase(index, caseSubStatusNDRC(index), 120000)
    }

    val sctyCases = ((totalAmount + 1) to (totalAmount * 2)).map { index =>
      createSCTYCase(index, caseSubStatusSCTY(index), 120000)
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

  def tpi01SetCaseSubStatusNDRC(caseSubStatusIndex: Int, isXiEori: Boolean = false): ResponseDetail = {

    val caseNumber    = s"100$caseSubStatusIndex".toInt
    val caseSubStatus = caseSubStatusNDRC(caseSubStatusIndex)
    val ndrcCases     = Seq(
      createNDRCCase(
        caseNumber,
        caseSubStatus,
        if (isXiEori) 500 else 0,
        if (isXiEori) "XI" else "GB"
      )
    )

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

  def tpi01SetCaseSubStatusSCTY(caseSubStatusIndex: Int, isXiEori: Boolean = false): ResponseDetail = {

    val ndrcCases = Seq()

    val caseSubStatus = caseSubStatusSCTY(caseSubStatusIndex)
    val caseNumber    = s"100$caseSubStatusIndex".toInt
    val sctyCases     = Seq(
      createSCTYCase(
        caseNumber,
        caseSubStatus,
        if (isXiEori) 500 else 0,
        if (isXiEori) "XI" else "GB"
      )
    )

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

  private def createNDRCCase(
    caseNumber: Long,
    subStatus: CaseSubStatusNDRC,
    caseNumberPrefix: Int = 0,
    eoriPrefix: String = "GB"
  ): NDRCCaseDetails = {
    val eori = eoriPrefix + f"${caseNumber + caseNumberPrefix}%012d"
    NDRCCaseDetails(
      CDFPayCaseNumber = s"NDRC-${(caseNumber + caseNumberPrefix).toString}",
      declarationID = Some("MRN23014"),
      claimStartDate = "20200501",
      closedDate =
        if (subStatus.caseStatus == Closed && subStatus != RejectedFailedValidation)
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

  private def createSCTYCase(
    caseNumber: Int,
    subStatus: CaseSubStatusSCTY,
    caseNumberPrefix: Int = 0,
    eoriPrefix: String = "GB"
  ): SCTYCaseDetails = {
    val eori = eoriPrefix + f"$caseNumber%012d"
    val odd  = caseNumber % 2 == 1
    SCTYCaseDetails(
      CDFPayCaseNumber = s"SCTY-${(caseNumber + caseNumberPrefix).toString}",
      declarationID = Some("12AA3456789ABCDEF2"),
      claimStartDate =
        if (odd) None else Some(LocalDate.now().minusDays(caseNumber).format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
      closedDate =
        if (subStatus.caseStatus == Closed)
          Some(LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        else None,
      reasonForSecurity = ReasonForSecurity.ofIndex(caseNumber),
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
