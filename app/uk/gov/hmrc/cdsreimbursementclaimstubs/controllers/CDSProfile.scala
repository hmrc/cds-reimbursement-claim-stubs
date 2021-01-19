/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import cats.implicits.catsSyntaxEq
import org.scalacheck.Gen
import play.api.http.Status
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.GenUtils.sample
import uk.gov.hmrc.cdsreimbursementclaimstubs.models._

import java.time.Instant
import java.util.UUID

final case class CDSProfile(
  predicate: EORI => Boolean,
  submitClaimResponse: SubmitClaimResponse
)

object CDSProfile {
  val timestamp: String = Instant.now.toString

  val EIS_200_OK: EisResponse = EisResponse(
    PostNewClaimsResponse(
      ResponseCommon(
        ResponseCommonStatus.OK,
        timestamp,
        Some(CDFinancialPayService.SCTY),
        Some(caseNumber),
        None,
        None,
        List.empty
      )
    )
  )

  val EIS_400_BAD_REQUEST_MISSING_MANDATORY_FIELD: EisErrorResponse = EisErrorResponse(
    ErrorDetail(
      timestamp,
      UUID.randomUUID().toString,
      Status.BAD_REQUEST.toString,
      "Invalid Message",
      "EIS",
      Detail(List(""""\"[[string '' is too short (length:0, required minimum: 1)]]\"""""))
    )
  )

  val EIS_400_BAD_REQUEST_JSON_SCHEMA_ERROR: EisErrorResponse = EisErrorResponse(
    ErrorDetail(
      timestamp,
      UUID.randomUUID().toString,
      Status.BAD_REQUEST.toString,
      "Invalid Message",
      "JSON validation",
      Detail(
        List(
          """"\"[string '123456789012345679' is too long (length: 18, maximum allowed: 17), ECMA 262 regex '^-?[0-9]{17}$' does not match input string '123456789012345679']\"""""
        )
      )
    )
  )

  val EIS_401_BAD_REQUEST: EisErrorResponse = EisErrorResponse(
    ErrorDetail(
      timestamp,
      UUID.randomUUID().toString,
      Status.UNAUTHORIZED.toString,
      "Bearer Token Missing or Invalid",
      "EIS",
      Detail(List.empty)
    )
  )

  val EIS_403__WAF_ERROR: WAFErrorResponse = WAFErrorResponse(
    """
      |<html>
      |<head>
      | <meta content="HTML Tidy for Java (vers. 26 Sep 2004), see www.w3.org" name="generator"/>
      | <title>403 Forbidden</title>
      |</head>
      | <body bgcolor="white">
      |   <center>
      |     <h1>403 Forbidden</h1>
      |   </center>
      |   <hr/>
      |   <center>nginx</center>
      |</body>
      |</html>
      |""".stripMargin
  )

  val EIS_405_METHOD_NOT_ALLOWED: EisErrorResponse = EisErrorResponse(
    ErrorDetail(
      timestamp,
      UUID.randomUUID().toString,
      Status.METHOD_NOT_ALLOWED.toString,
      "Invalid HTTP method",
      "EIS",
      Detail(List.empty)
    )
  )

  val EIS_500_CDFPAY_TIMEOUT: EisErrorResponse = EisErrorResponse(
    ErrorDetail(
      timestamp,
      UUID.randomUUID().toString,
      Status.INTERNAL_SERVER_ERROR.toString,
      "Error connecting to the server",
      "ct-api",
      Detail(List("101504 - Send timeout"))
    )
  )

  private def nRandomDigits(n: Int): String =
    List.fill(n)(sample(Gen.numChar)).mkString("")

  private def caseNumber: String =
    s"${nRandomDigits(25)}"

  def getCDSProfile(overPaymentClaim: OverPaymentClaim): Option[CDSProfile] =
    cdsProfiles.find(_.predicate(EORI(overPaymentClaim.claimantEORI)))

  private val cdsProfiles: List[CDSProfile] =
    List(
      CDSProfile(
        _ === EORI("AB12345678901234Z"),
        SubmitClaimResponse(Right(EIS_200_OK))
      ),
      CDSProfile(
        _ === EORI("BB12345678901234Y"),
        SubmitClaimResponse(Left(Right(EIS_400_BAD_REQUEST_JSON_SCHEMA_ERROR)))
      ),
      CDSProfile(
        _ === EORI("CB12345678901234X"),
        SubmitClaimResponse(Left(Right(EIS_400_BAD_REQUEST_MISSING_MANDATORY_FIELD)))
      ),
      CDSProfile(
        _ === EORI("DB12345678901234W"),
        SubmitClaimResponse(Left(Right(EIS_401_BAD_REQUEST)))
      ),
      CDSProfile(
        _ === EORI("EB12345678901234V"),
        SubmitClaimResponse(Left(Left(EIS_403__WAF_ERROR)))
      ),
      CDSProfile(
        _ === EORI("FB12345678901234U"),
        SubmitClaimResponse(Left(Right(EIS_405_METHOD_NOT_ALLOWED)))
      ),
      CDSProfile(
        _ === EORI("GB12345678901234T"),
        SubmitClaimResponse(Left(Right(EIS_500_CDFPAY_TIMEOUT)))
      )
    )

}
