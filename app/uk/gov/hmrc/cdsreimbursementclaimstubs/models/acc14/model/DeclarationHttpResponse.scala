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

import cats.implicits.catsSyntaxEq
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ReasonForSecurity
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14Response.Acc14ResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.DeclarationResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.responses.ErrorResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ids.MRN
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.WafErrorResponse

object DeclarationHttpResponse {
  def getResponse(mrn: MRN, reasonForSecurity: String = "IPR"): Option[DeclarationResponse] = {
    val importerEori: String = s"""GB0000000000000${mrn.value.substring(16, 18)}"""
    val exporterEori: String = s"""GB1000000000000${mrn.value.substring(16, 18)}"""

    val reasonForSecuritySelected = ReasonForSecurity.values.find(reason => reason === mrn.value.substring(5, 8)).getOrElse(reasonForSecurity)

    val response: DeclarationResponse = mrn.value.substring(3, 5) match {
      case "MR" => DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      case "PR" => DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE(mrn.value, importerEori)))
      case "FR" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE(mrn.value, importerEori, exporterEori)))
      case "FS" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_SUBSIDY(mrn.value, importerEori, exporterEori)))
      case "O1" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_1(mrn.value, importerEori, exporterEori)))
      case "O2" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_2(mrn.value, importerEori, exporterEori)))
      case "O3" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_3(mrn.value, importerEori, exporterEori)))
      case "OV" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_VAT(mrn.value, importerEori, exporterEori)))
      case "FV" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_VAT(mrn.value, importerEori, exporterEori)))
      case "AT" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES(mrn.value, importerEori, exporterEori)))
      case "ME" => DeclarationResponse(Right(Acc14ResponseType.OK_WITH_MISMATCH_ON_EORI(mrn.value)))
      case "NI" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_NORTHERN_IRELAND(mrn.value, importerEori, exporterEori)))
      case "ND" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS(mrn.value, importerEori, exporterEori)))
      case "NC" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONSIGNEE(mrn.value, importerEori)))
      case "NB" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_BANK_DETAILS(mrn.value, reasonForSecuritySelected, importerEori, exporterEori)))
      case "SR" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecuritySelected, importerEori, exporterEori)))
      case "CS" => DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(mrn.value, reasonForSecuritySelected, importerEori, exporterEori)))
      case "NS" => DeclarationResponse(Right(Acc14ResponseType.OK_NO_CONSIGNEE_RESPONSE_SECURITIES(mrn.value, reasonForSecuritySelected, importerEori)))
      case "AS" => DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES_SECURITIES(mrn.value, reasonForSecuritySelected, importerEori, exporterEori)))
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