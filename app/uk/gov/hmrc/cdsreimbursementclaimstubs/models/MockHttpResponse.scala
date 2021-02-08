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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models

import cats.syntax.eq._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14ErrorResponse.Acc14ErrorResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14Response.Acc14ResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.DeclarationResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ids.{EORI, MRN}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.Tpi05ErrorResponse.Tpi05ErrorResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.Tpi05Response.Tpi05ResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.{SubmitClaimResponse, WafErrorResponse}

final case class MockHttpResponse(
  mrnPredicate: MRN => Boolean,
  eoriPredicate: EORI => Boolean,
  submitClaimResponse: SubmitClaimResponse,
  declarationResponse: DeclarationResponse
)

object MockHttpResponse {

  def getSubmitClaimHttpResponse(eori: EORI): Option[MockHttpResponse] = httpResponses.find(_.eoriPredicate(eori))
  def getDeclarationHttpResponse(mrn: MRN): Option[MockHttpResponse]   = httpResponses.find(_.mrnPredicate(mrn))

  private val httpResponses: List[MockHttpResponse] =
    List(
      MockHttpResponse(
        _ === MRN("10ABCDEFGHIJKLMNO0"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("10ABCDEFGHIJKLMNO0")))
      ),
      MockHttpResponse(
        _ === MRN("10ABCDEFGHIJKLMNO1"),
        _ === EORI("AA12345678901234Y"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE("10ABCDEFGHIJKLMNO1")))
      ),
      MockHttpResponse(
        _ === MRN("20ABCDEFGHIJKLMNO1"),
        _ === EORI("AB12345678901234A"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_BUT_ERROR_RETURN_IN_PAYLOAD)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("20ABCDEFGHIJKLMNO1")))
      ),
      MockHttpResponse(
        _ === MRN("90ABCDEFGHIJKLMNO0"),
        _ === EORI("AE12345678901234Z"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.MISSING_MANDATORY_FIELD))),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("90ABCDEFGHIJKLMNO0")))
      ),
      MockHttpResponse(
        _ === MRN("30ABCDEFGHIJKLMNO0"),
        _ === EORI("AC12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("40ABCDEFGHIJKLMNO1"),
        _ === EORI("AD12345678901234A"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_WITH_MISMATCH_ON_EORI("AD12345678901234A")))
      ),
      MockHttpResponse(
        _ === MRN("10ABCDEFGHIJKLMNO0"),
        _ === EORI("AB12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("20ABCDEFGHIJKLMNO0"),
        _ === EORI("AC12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_BUT_ERROR_RETURN_IN_PAYLOAD)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("20ABCDEFGHIJKLMNO0"),
        _ === EORI("BB12345678901234Y"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.MISSING_MANDATORY_FIELD))),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("20ABCDEFGHIJKLMNO0"),
        _ === EORI("CB12345678901234X"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.PATTERN_ERROR))),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("30ABCDEFGHIJKLMNO1"),
        _ === EORI("DB12345678901234W"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.NO_BEARER_TOKEN))),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("30ABCDEFGHIJKLMNO1"),
        _ === EORI("EB12345678901234V"),
        SubmitClaimResponse(Left(Left(WafErrorResponse.FORBIDDEN))),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("30ABCDEFGHIJKLMNO1"),
        _ === EORI("FB12345678901234U"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.HTTP_METHOD_NOT_ALLOWED))),
        DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE("30ABCDEFGHIJKLMNO1")))
      ),
      MockHttpResponse(
        _ === MRN("30ABCDEFGHIJKLMNO1"),
        _ === EORI("GB12345678901234T"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.TIME_OUT))),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("30ABCDEFGHIJKLMNO1")))
      ),
      MockHttpResponse(
        _ === MRN("21ABCDEFGHIJKLMNO0"),
        _ === EORI("GB12345678901234T"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.TIME_OUT))),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("22ABCDEFGHIJKLMNO1"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.TIME_OUT))),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("AA12345678901234Z")))
      ),
      MockHttpResponse(
        _ === MRN("41ABCDEFGHIJKLMNO1"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.BAD_REQUEST_MISSING_DECLARATION)))
      ),
      MockHttpResponse(
        _ === MRN("41ABCDEFGHIJKLMNO2"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.NO_SECURITY_DEPOSITS)))
      ),
      MockHttpResponse(
        _ === MRN("40ABCDEFGHIJKLMNO3"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.HTTP_METHOD_NOT_ALLOWED)))
      ),
      MockHttpResponse(
        _ === MRN("50ABCDEFGHIJKLMNO1"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.TIME_OUT)))
      )
    )
}
