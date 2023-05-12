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


sealed trait CaseSubStatusNDRC {
  val caseStatus: CaseStatus
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