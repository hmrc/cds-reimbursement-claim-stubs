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

sealed trait CaseSubStatusSCTY {
  val caseStatus: CaseStatus
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