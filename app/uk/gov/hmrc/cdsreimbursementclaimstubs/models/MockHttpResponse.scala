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

      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (standard scenario)
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("02AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("02AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("03AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("03AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("04AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("04AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("05AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("05AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("06AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("06AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("07AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("07AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("08AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("08AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("09AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("09AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("10AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("11AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("11AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("12AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("12AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("13AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("13AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("14AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("14AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("15AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("15AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("16AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("16AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("17AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("17AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("18AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("18AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("19AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("19AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("20AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("20AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("02AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("02AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("03AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("03AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("04AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("04AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("05AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("05AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("06AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("06AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("07AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("07AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("08AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("08AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("09AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("09AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("10AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("11AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("11AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("12AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("12AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("13AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("13AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("14AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("14AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("15AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("15AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("16AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("16AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("17AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("17AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("18AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("18AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("19AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("19AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("20AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("20AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")))
      ),

      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (other scenario)
      MockHttpResponse(
        _ === MRN("50AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000005"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES("50AAAAAAAAAAAAAAA1", "GB000000000000005", "GB000000000000005")))
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA3"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_NORTHERN_IRELAND("10AAAAAAAAAAAAAAA3", "GB000000000000002", "GB000000000000002")))
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA6"),
        _ === EORI("GB000000000000006"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS("10AAAAAAAAAAAAAAA6", "GB000000000000006", "GB000000000000006")))
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (other scenario - different duties)
      MockHttpResponse(
        _ === MRN("10XXXXXXXXXXXXXXX1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_1("10XXXXXXXXXXXXXXX1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("10YYYYYYYYYYYYYYY1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_2("10YYYYYYYYYYYYYYY1", "GB000000000000001", "GB000000000000001")))
      ),
      MockHttpResponse(
        _ === MRN("10ZZZZZZZZZZZZZZZ1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_3("10ZZZZZZZZZZZZZZZ1", "GB000000000000001", "GB000000000000001")))
      ),


      //TPI05 OK_RESPONSE, ACC14 OK_PARTIAL_RESPONSE
      MockHttpResponse(
        _ === MRN("10BBBBBBBBBBBBBBB1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE("10BBBBBBBBBBBBBBB1")))
      ),
      MockHttpResponse(
        _ === MRN("20BBBBBBBBBBBBBBB1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE("20BBBBBBBBBBBBBBB1")))
      ),
      MockHttpResponse(
        _ === MRN("10BBBBBBBBBBBBBBB2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE("10BBBBBBBBBBBBBBB2")))
      ),
      MockHttpResponse(
        _ === MRN("20BBBBBBBBBBBBBBB2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_PARTIAL_RESPONSE("20BBBBBBBBBBBBBBB2")))
      ),

      //TPI05 OK_RESPONSE, ACC14 OK_MINIMUM_RESPONSE
      MockHttpResponse(
        _ === MRN("10CCCCCCCCCCCCCCC1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("20CCCCCCCCCCCCCCC1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("10CCCCCCCCCCCCCCC2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),
      MockHttpResponse(
        _ === MRN("20CCCCCCCCCCCCCCC2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_MINIMUM_RESPONSE))
      ),



      MockHttpResponse(
        _ === MRN("10ABCDEFGHIJKLMNO0"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("10ABCDEFGHIJKLMNO0", "GB123456789012345", "GB123456789012345")))
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
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("20ABCDEFGHIJKLMNO1", "GB123456789012345", "GB123456789012345")))
      ),
      MockHttpResponse(
        _ === MRN("90ABCDEFGHIJKLMNO0"),
        _ === EORI("AE12345678901234Z"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.MISSING_MANDATORY_FIELD))),
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("90ABCDEFGHIJKLMNO0", "GB123456789012345", "GB123456789012345")))
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
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("30ABCDEFGHIJKLMNO1", "GB123456789012345", "GB123456789012345")))
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
        DeclarationResponse(Right(Acc14ResponseType.OK_FULL_RESPONSE("AA12345678901234Z", "GB123456789012345", "GB123456789012345")))
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
