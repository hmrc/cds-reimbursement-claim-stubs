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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi02

import play.api.libs.json.Json
import play.api.mvc.Result
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi02.ndrc.{EntryDetail, NDRCAmounts, NDRCCase, NDRCDetail, ProcedureDetail}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi02.scty.{Goods, SCTYCase}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.{NDRC, ReasonForSecurity, SCTY, SchemaValidation, Service}

trait TPI02Generation extends SchemaValidation {

  def tpi02Claim(
    claimType: String,
    service: Service,
    caseNumber: String,
    caseStatus: String,
    closed: Boolean,
    multiple: Boolean,
    entryNumber: Boolean
  ): Result = {
    val response = TPI02Response(
      SpecificCaseResponse(
        ResponseCommon("OK", "2021-05-10T11:21:35Z", None, None, None),
        Some(
          ResponseDetail(
            service.toString,
            CDFPayCaseFound = true,
            if (service == NDRC)
              Some(
                createNDRCCase(
                  caseNumber,
                  caseStatus,
                  claimType,
                  closed,
                  entryNumber,
                  multiple
                )
              )
            else None,
            if (service == SCTY)
              Some(createSCTYCase(caseNumber, caseStatus, closed, entryNumber))
            else
              None
          )
        )
      )
    )
    validateResponse("tpi02/tpi02-response-schema.json", Json.toJson(response))
  }

  private def mrnDetails(multiple: Boolean): Option[Seq[ProcedureDetail]] =
    if (multiple) {
      Some((1 to 10).map(i => ProcedureDetail(s"MRN0000$i", mainDeclarationReference = i == 1)))
    } else {
      None
    }

  private def entryDetails(multiple: Boolean): Option[Seq[EntryDetail]] =
    if (multiple) {
      Some(
        Seq(
          EntryDetail("123456789A12122022", mainDeclarationReference = true),
          EntryDetail("987654321A12122022", mainDeclarationReference = false)
        )
      )
    } else {
      Some(Seq(EntryDetail("123456789A12122022", mainDeclarationReference = true)))
    }

  private def createSCTYCase(
    caseNumber: String,
    caseStatus: String,
    closed: Boolean,
    entryNumber: Boolean
  ): SCTYCase = {
    val odd = caseNumber.lastOption.exists(c => Character.isDigit(c) && c.toInt % 2 == 1)
    SCTYCase(
      CDFPayCaseNumber = caseNumber,
      declarationID = if (entryNumber) Some("123456789A12122022") else Some("MRN23014"),
      reasonForSecurity = ReasonForSecurity.of(caseNumber),
      procedureCode = "10",
      caseStatus = caseStatus,
      goods = Some(Seq(Goods("12", Some("Digital media")), Goods("13", Some(" ")), Goods("14", Some("drum kit")))),
      declarantEORI = if (isXiClaim(caseNumber)) "XI12345678914" else "GB12345678912",
      importerEORI = if (isXiClaim(caseNumber)) Some("XI98765432102") else if (odd) None else Some("GB98765432101"),
      claimantEORI = if (isXiClaim(caseNumber)) Some("XI98745632102") else Some("GB98745632101"),
      totalCustomsClaimAmount = Some("900000.00"),
      totalVATClaimAmount = Some("900000.00"),
      totalClaimAmount = Some("900000.00"),
      totalReimbursementAmount = Some("900000.00"),
      claimStartDate = if (odd) None else Some("20210501"),
      claimantName = Some("Claimant name"),
      claimantEmailAddress = Some("Claimant email address"),
      closedDate = if (closed) Some("20210501") else None,
      reimbursement = Some(Seq(Reimbursement("20210501", "6402.06", "A30", "P")))
    )
  }

  private def createNDRCCase(
    caseNumber: String,
    caseStatus: String,
    claimType: String,
    closed: Boolean,
    entryNumber: Boolean,
    multiple: Boolean
  ): NDRCCase =
    NDRCCase(
      NDRCAmounts = NDRCAmounts(
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00"),
        Some("900000.00")
      ),
      NDRCDetail = NDRCDetail(
        caseNumber,
        if (entryNumber) Some("123456789A12122022") else Some("MRN23014"),
        claimType,
        if (multiple) "Bulk" else "Individual",
        caseStatus,
        Some("A description of goods"),
        None,
        if (isXiClaim(caseNumber)) "XI12345678914" else "GB12345678912",
        if (isXiClaim(caseNumber)) "XI98765432102" else "GB98765432101",
        if (isXiClaim(caseNumber)) Some("XI98745632102") else Some("GB98745632101"),
        Some("Duty Suspension"),
        "20200501",
        Some("Claimant name"),
        Some("Claimant email address"),
        if (closed) Some("20210501") else None,
        if (entryNumber) Some(Seq.empty) else mrnDetails(multiple),
        if (entryNumber) entryDetails(multiple) else Some(Seq.empty),
        reimbursement = Some(Seq(Reimbursement("20200501", "6402.06", "A30", "P")))
      )
    )

  def isXiClaim(caseNumber: String): Boolean = {
    caseNumber
      .split("-")
      .lastOption.exists(x => x.startsWith("15") || x.startsWith("105"))
  }
}
