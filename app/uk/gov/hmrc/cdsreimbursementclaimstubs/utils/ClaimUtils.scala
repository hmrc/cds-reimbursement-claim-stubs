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

import play.api.mvc.Result
import play.api.mvc.Results.Ok
import play.api.mvc.Results.Status
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.SchemaValidation

object ClaimUtils  extends SchemaValidation{
  // Claim Types
  val C285: String = "C285"
  val CnE1179: String = "C&E1179"

  // NDRC CaseStatuses
  val NDRC_OPEN: String = "Open"
  val NDRC_OPEN_ANALYSIS: String = "Open-Analysis"
  val NDRC_PENDING_APPROVAL: String = "Pending-Approval"
  val NDRC_PENDING_QUERIED: String = "Pending-Queried"
  val NDRC_RESOLVED_WITHDRAWN: String = "Resolved-Withdrawn"
  val NDRC_REJECTED_FAILED_VALIDATION: String = "Rejected-Failed Validation"
  val NDRC_RESOLVED_REJECTED: String = "Resolved-Rejected"
  val NDRC_OPEN_REWORK: String = "Open-Rework"
  val NDRC_PAUSED: String = "Paused"
  val NDRC_RESOLVED_NO_REPLY: String = "Resolved-No Reply"
  val NDRC_RTBHSENT: String = "RTBH-Sent"
  val NDRC_RESOLVED_REFUSED: String = "Resolved-Refused"
  val NDRC_PENDING_PAYMENT_CONFIRMATION: String = "Pending Payment Confirmation"
  val NDRC_RESOLVED_APPROVED: String = "Resolved-Approved"
  val NDRC_RESOLVED_PARTIAL_REFUSED: String = "Resolved-Partial Refused"
  val NDRC_PENDING_DECISION_LETTER: String = "Pending Decision Letter"
  val NDRC_APPROVED: String = "Approved"
  val NDRC_ANALYSIS_REWORK: String = "Analysis-Rework"
  val NDRC_REWORK_PAYMENT_DETAILS: String = "Rework-Payment Details"
  val NDRC_PENDING_RTBH: String = "Pending-RTBH"
  val NDRC_REPLY_TO_RTBH: String = "Reply To RTBH"
  val NDRC_PENDING_COMPLIANCE_RECOMMENDATION: String = "Pending-Compliance Recommendation"
  val NDRC_PENDING_COMPLIANCE_CHECK_QUERY: String = "Pending-Compliance Check Query"
  val NDRC_PENDING_COMPLIANCE_CHECK: String = "Pending-Compliance Check"

  // SCTY CaseStatuses
  val SCTY_OPEN: String = "Open"
  val SCTY_PENDING_APPROVAL: String = "Pending-Approval"
  val SCTY_PENDING_PAYMENT: String = "Pending-Payment"
  val SCTY_PARTIAL_REFUND: String = "Partial Refund"
  val SCTY_RESOLVED_REFUND: String = "Resolved-Refund"
  val SCTY_PENDING_QUERY: String = "Pending-Query"
  val SCTY_RESOLVED_MANUAL_Bta: String = "Resolved-Manual BTA"
  val SCTY_PENDING_C18: String = "Pending-C18"
  val SCTY_CLOSED_C18_RAISED: String = "Closed-C18 Raised"
  val SCTY_RTBHLETTER_INITIATED: String = "RTBH Letter Initiated"
  val SCTY_AWAITING_RTBHLETTER_RESPONSE: String = "Awaiting RTBH Letter Response"
  val SCTY_REMINDER_LETTER_INITIATED: String = "Reminder Letter Initiated"
  val SCTY_AWAITING_REMINDER_LETTER_RESPONSE: String = "Awaiting Reminder Letter Response"
  val SCTY_DECISION_LETTER_INITIATED: String = "Decision Letter Initiated"
  val SCTY_PARTIAL_Bta: String = "Partial BTA"
  val SCTY_PARTIAL_Bta_REFUND: String = "Partial BTA/Refund"
  val SCTY_RESOLVED_AUTO_Bta: String = "Resolved-Auto BTA"
  val SCTY_RESOLVED_MANUAL_Bta_REFUND: String = "Resolved-Manual BTA/Refund"
  val SCTY_OPEN_EXTENSION_GRANTED: String = "Open-Extension Granted"
  val SCTY_RESOLVED_WITHDRAWN: String = "Resolved-Withdrawn"


  val NO_CLAIMS_FOUND: Result = parseResponse("tpi02/response-200-no-claims-found.json", Ok, Some("tpi02/tpi02-response-schema.json"))

  private def parseResponse(filename: String, status: Status, schema: Option[String] = None): Result = {
    val response = jsonDataFromFile(filename)
    status match {
      case Ok =>
        validateResponse(schema.get, response)
        status(response)
      case _ => status(response)
    }
  }
}
