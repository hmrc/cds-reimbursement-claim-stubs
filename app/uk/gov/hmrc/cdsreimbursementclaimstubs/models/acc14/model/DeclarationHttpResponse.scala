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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.model

import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14Response.Acc14ResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.DeclarationResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.responses.ErrorResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ids.MRN
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.WafErrorResponse

object DeclarationHttpResponse {
  def getResponse(mrn: MRN, reasonForSecurity: String = "IPR"): Option[DeclarationResponse] = {
    val response: DeclarationResponse = mrn.value.substring(3, 5) match {
      case "MR" => DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      case "PR" => DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE(mrn.value)))
      case "FR" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE(mrn.value, "GB000000000000001", "GB000000000000001")))
      case "FS" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_SUBSIDY(mrn.value, "GB000000000000001", "GB000000000000001")))
      case "O1" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_1(mrn.value, "GB000000000000001", "GB000000000000001")))
      case "O2" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_2(mrn.value, "GB000000000000001", "GB000000000000001")))
      case "O3" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_3(mrn.value, "GB000000000000001", "GB000000000000001")))
      case "OV" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_VAT(mrn.value, "GB000000000000007", "GB000000000000007")))
      case "FV" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_VAT(mrn.value, "GB000000000000008", "GB000000000000008")))
      case "AT" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES(mrn.value, "GB000000000000002", "GB000000000000002")))
      case "ME" => DeclarationResponse(Right(Acc14ResponseType.OK_WITH_MISMATCH_ON_EORI(mrn.value)))
      case "NI" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_NORTHERN_IRELAND(mrn.value, "GB000000000000002", "GB000000000000002")))
      case "ND" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS(mrn.value, "GB000000000000006", "GB000000000000006")))
      case "NC" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONSIGNEE(mrn.value, "GB000000000000091")))
      case "NB" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_BANK_DETAILS(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")))
      case "SR" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")))
      case "CS" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000091", "GB000000000000092")))
      case "NS" => DeclarationResponse(Right(Acc14ResponseType.OK_NO_CONSIGNEE_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000091")))
      case "AS" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")))
      case "WF" => DeclarationResponse(Left(Left(WafErrorResponse.FORBIDDEN)))
      case "EB" => DeclarationResponse(Left(Right(ErrorResponse.MAKE_BAD_REQUEST_MISSING_DECLARATION_RESPONSE)))
      case "ES" => DeclarationResponse(Left(Right(ErrorResponse.MAKE_NO_SECURITY_DEPOSIT_RESPONSE)))
      case "EN" => DeclarationResponse(Left(Right(ErrorResponse.MAKE_HTTP_METHOD_NOT_ALLOWED_RESPONSE)))
      case "ET" => DeclarationResponse(Left(Right(ErrorResponse.MAKE_TIME_OUT_RESPONSE)))
      case _ => DeclarationResponse(Left(Right(ErrorResponse.MAKE_BAD_REQUEST_MISSING_DECLARATION_RESPONSE)))
    }
    Some(response)
  }
}
