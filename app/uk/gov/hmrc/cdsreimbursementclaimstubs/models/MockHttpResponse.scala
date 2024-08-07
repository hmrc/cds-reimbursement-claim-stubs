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

  def getSubmitClaimHttpResponse(eori: EORI): Option[MockHttpResponse] =
    httpResponses
      .find(_.eoriPredicate(eori))
      .orElse(httpResponses.headOption)

  def getDeclarationHttpResponse(mrn: MRN): Option[MockHttpResponse] =
    httpResponses
      .find(_.mrnPredicate(mrn))
      .orElse(None)

  def findMRN(mrn: String): Option[MockHttpResponse] = httpResponses.find(_.mrnPredicate(MRN(mrn)))

  private val httpResponses: List[MockHttpResponse] =
    List(
      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (standard scenario)
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("02AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("02AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("03AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("03AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("04AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("04AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("05AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("05AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("06AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("06AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("07AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("07AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("08AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("08AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("09AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("09AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAA00"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAAAA1",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("A00", "12345.67", "GB000000000000", 0))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAB00"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAAAA1",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("B00", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAA413"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAAAA1",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("413", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAA481"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAAAA1",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("481", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("10AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAA301"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAA301",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("301", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAA311"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAA311",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("311", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAA301311"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAA301311",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("301", "12345.67", "GB000000000000", 0),
                ("311", "12345.67", "GB000000000000", 1)
              )
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAA00301"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAA00301",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("A00", "12345.67", "GB000000000000", 0),
                ("301", "12345.67", "GB000000000000", 1)
              )
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAA00311"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAA00311",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("A00", "12345.67", "GB000000000000", 0),
                ("311", "12345.67", "GB000000000000", 1)
              )
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("11AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("11AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("12AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("12AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("13AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("13AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("14AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("14AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("15AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("15AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("16AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("16AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("17AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("17AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("18AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("18AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("19AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("19AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("20AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("20AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001"))
        )
      ),
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("02AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("02AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("03AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("03AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("04AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("04AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("05AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("05AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("06AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("06AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("07AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("07AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("08AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("08AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("09AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("09AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("10AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("11AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("11AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("12AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("12AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("13AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("13AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("14AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("14AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("15AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("15AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("16AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("16AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("17AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("17AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("18AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("18AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      MockHttpResponse(
        _ === MRN("19AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES("19AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("20AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("20AAAAAAAAAAAAAAA2", "GB000000000000002", "GB000000000000002"))
        )
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (other scenario)
      MockHttpResponse(
        _ === MRN("50AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000005"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES("50AAAAAAAAAAAAAAA1", "GB000000000000005", "GB000000000000005")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA3"),
        _ === EORI("GB000000000000002"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_NORTHERN_IRELAND("10AAAAAAAAAAAAAAA3", "GB000000000000002", "GB000000000000002")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA6"),
        _ === EORI("GB000000000000006"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("10AAAAAAAAAAAAAAA6", "GB000000000000006", "GB000000000000006")
          )
        )
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (other scenario - different duties)
      MockHttpResponse(
        _ === MRN("10XXXXXXXXXXXXXXX1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_OTHER_DUTIES_1("10XXXXXXXXXXXXXXX1", "GB000000000000001", "GB000000000000001")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10YYYYYYYYYYYYYYY1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_OTHER_DUTIES_2("10YYYYYYYYYYYYYYY1", "GB000000000000001", "GB000000000000001")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10ZZZZZZZZZZZZZZZ1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_OTHER_DUTIES_3("10ZZZZZZZZZZZZZZZ1", "GB000000000000001", "GB000000000000001")
          )
        )
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (other duties and VAT duties)
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA7"),
        _ === EORI("GB000000000000007"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_OTHER_DUTIES_VAT("10AAAAAAAAAAAAAAA7", "GB000000000000007", "GB000000000000007")
          )
        )
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_FULL_RESPONSE (VAT duties)
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA8"),
        _ === EORI("GB000000000000008"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE_VAT("10AAAAAAAAAAAAAAA8", "GB000000000000008", "GB000000000000008"))
        )
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_PARTIAL_RESPONSE
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAA9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("10AAAAAAAAAAAAAAA9", "GB000000000000091", "GB000000000000092")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA1",
                "GB000000000000001",
                "GB000000000000001",
                false,
                false
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA2",
                "GB000000000000001",
                "GB000000000000001",
                true,
                false
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA3"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA3",
                "GB000000000000001",
                "GB000000000000001",
                false,
                true
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA4"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA4",
                "GB000000000000001",
                "GB000000000000002",
                false,
                false
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA5"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA5",
                "GB000000000000001",
                "GB000000000000002",
                true,
                false
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA6"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA6",
                "GB000000000000001",
                "GB000000000000002",
                false,
                true
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA7"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA7",
                "GB000000000000002",
                "GB000000000000001",
                false,
                false
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA8"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA8",
                "GB000000000000002",
                "GB000000000000001",
                true,
                false
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAAA9"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS(
                "60AAAAAAAAAAAAAAA9",
                "GB000000000000002",
                "GB000000000000001",
                false,
                true
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60AAAAAAAAAAAAAA10"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "60AAAAAAAAAAAAAA10",
              "GB000000000000001",
              "GB000000000000002",
              duties = Seq(("A00", "12345.67", "GB000000000000", 0))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD1",
                "GB000000000000001",
                "GB000000000000001",
                false,
                false,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD2"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD2",
                "GB000000000000001",
                "GB000000000000001",
                true,
                false,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD3"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD3",
                "GB000000000000001",
                "GB000000000000001",
                false,
                true,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD4"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD4",
                "GB000000000000001",
                "GB000000000000002",
                false,
                false,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD5"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD5",
                "GB000000000000001",
                "GB000000000000002",
                true,
                false,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD6"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD6",
                "GB000000000000001",
                "GB000000000000002",
                false,
                true,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD7"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD7",
                "GB000000000000002",
                "GB000000000000001",
                false,
                false,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD8"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD8",
                "GB000000000000002",
                "GB000000000000001",
                true,
                false,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("60DDDDDDDDDDDDDDD9"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SUBSIDY(
                "60DDDDDDDDDDDDDDD9",
                "GB000000000000002",
                "GB000000000000001",
                false,
                true,
                Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("55AAAAAAAAAAAAAAA0"),
        _ === EORI("GB000000000000050"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("55AAAAAAAAAAAAAAA0", "GB000000000000050", "GB000000000000001")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("55AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000051"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("55AAAAAAAAAAAAAAA1", "GB000000000000051", "GB000000000000001")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("55AAAAAAAAAAAAAAA2"),
        _ === EORI("GB000000000000052"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("55AAAAAAAAAAAAAAA2", "GB000000000000052", "GB000000000000001")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("55AAAAAAAAAAAAAAA3"),
        _ === EORI("GB000000000000053"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("55AAAAAAAAAAAAAAA3", "GB000000000000001", "GB000000000000053")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("55AAAAAAAAAAAAAAA4"),
        _ === EORI("GB000000000000054"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("55AAAAAAAAAAAAAAA4", "GB000000000000001", "GB000000000000054")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("55AAAAAAAAAAAAAAA5"),
        _ === EORI("GB000000000000055"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("55AAAAAAAAAAAAAAA5", "GB000000000000001", "GB000000000000055")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("20AAAAAAAAAAAAAAA9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("20AAAAAAAAAAAAAAA9", "GB000000000000091", "GB000000000000093")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("30AAAAAAAAAAAAAAA9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONTACT_DETAILS("30AAAAAAAAAAAAAAA9", "GB000000000000094", "GB000000000000092")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("40AAAAAAAAAAAAAAA9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE("40AAAAAAAAAAAAAAA9", "GB000000000000091", "GB000000000000092")
          )
        )
      ),
      //TPI05 OK_RESPONSE, ACC14 OK_PARTIAL_RESPONSE
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAB9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONSIGNEE("10AAAAAAAAAAAAAAB9", "GB000000000000091")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAC9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONSIGNEE("10AAAAAAAAAAAAAAC9", "GB000000000000091")
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAAAD9"),
        _ === EORI("GB000000000000090"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_RESPONSE_NO_CONSIGNEE("10AAAAAAAAAAAAAAD9", "GB000000000000091")
          )
        )
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
        _ === MRN("10DDDDDDDDDDDDDDD1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY("10DDDDDDDDDDDDDDD1", "GB000000000000001", "GB000000000000001", Seq("006"))
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD2"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY(
                "10DDDDDDDDDDDDDDD2",
                "GB000000000000001",
                "GB000000000000001",
                Seq("006", "001")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD3"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY(
                "10DDDDDDDDDDDDDDD3",
                "GB000000000000001",
                "GB000000000000001",
                Seq("006", "003")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD4"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY(
                "10DDDDDDDDDDDDDDD4",
                "GB000000000000001",
                "GB000000000000001",
                Seq("006", "001", "002", "003")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD5"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY("10DDDDDDDDDDDDDDD5", "GB000000000000001", "GB000000000000001", Seq("006"))
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD6"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY("10DDDDDDDDDDDDDDD6", "GB000000000000001", "GB000000000000001", Seq("006"))
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD7"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY("10DDDDDDDDDDDDDDD7", "GB000000000000001", "GB000000000000001", Seq("006"))
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD8"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY("10DDDDDDDDDDDDDDD8", "GB000000000000001", "GB000000000000001", Seq("006"))
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10DDDDDDDDDDDDDDD9"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_SUBSIDY("10DDDDDDDDDDDDDDD9", "GB000000000000001", "GB000000000000001", Seq("006"))
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10ABCDEFGHIJKLMNO0"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("10ABCDEFGHIJKLMNO0", "GB123456789012345", "GB123456789012345"))
        )
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
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("20ABCDEFGHIJKLMNO1", "GB123456789012345", "GB123456789012345"))
        )
      ),
      MockHttpResponse(
        _ === MRN("90ABCDEFGHIJKLMNO0"),
        _ === EORI("AE12345678901234Z"),
        SubmitClaimResponse(Left(Right(Tpi05ErrorResponseType.MISSING_MANDATORY_FIELD))),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("90ABCDEFGHIJKLMNO0", "GB123456789012345", "GB123456789012345"))
        )
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
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("30ABCDEFGHIJKLMNO1", "GB123456789012345", "GB123456789012345"))
        )
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
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("AA12345678901234Z", "GB123456789012345", "GB123456789012345"))
        )
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
      ),
      MockHttpResponse(
        _ === MRN("70AAAAAAAAAAAAAAA1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType
              .OK_FULL_RESPONSE_DUPLICATED_ADDRESS_LINES("70AAAAAAAAAAAAAAA1", "GB000000000000001", "GB000000000000001")
          )
        )
      )
    ) ++ createPaymentMethodsForEoriEnding("01") ++
      createPaymentMethodsForEoriEnding("02")

  def createPaymentMethodsForEoriEnding(eoriEnding: String): List[MockHttpResponse] =
    createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact("00", eoriEnding) ++
      createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact("01", eoriEnding) ++
      createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact("02", eoriEnding) ++
      createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact("03", eoriEnding) ++
      createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact("04", eoriEnding) ++
      createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact("05", eoriEnding)

  def createEveryCombinationOfPaymentMethodsWithAndWithoutConsigneeAndDeclarantContact(
    prependMrn: String,
    eoriEnding: String
  ): List[MockHttpResponse] =
    createEveryCombinationOfPaymentMethods(
      prependMrn,
      eoriEnding,
      withConsigneeContactDetails = false,
      withDeclarantContactDetails = false
    ) ++
      createEveryCombinationOfPaymentMethods(
        prependMrn,
        eoriEnding,
        withConsigneeContactDetails = true,
        withDeclarantContactDetails = false
      ) ++
      createEveryCombinationOfPaymentMethods(
        prependMrn,
        eoriEnding,
        withConsigneeContactDetails = false,
        withDeclarantContactDetails = true
      ) ++
      createEveryCombinationOfPaymentMethods(
        prependMrn,
        eoriEnding,
        withConsigneeContactDetails = true,
        withDeclarantContactDetails = true
      )

  def createEveryCombinationOfPaymentMethods(
    prependMrn: String,
    eoriEnding: String,
    withConsigneeContactDetails: Boolean,
    withDeclarantContactDetails: Boolean
  ): List[MockHttpResponse] =
    List(
      createMockHttpResponseWithPaymentMethods(
        "001",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "002",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "003",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "002",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "003",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "002",
        "003",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "002",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "003",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "002",
        "003",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "002",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "003",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "002",
        "003",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      ),
      createMockHttpResponseWithPaymentMethods(
        "001",
        "002",
        "003",
        "006",
        prependMrn = prependMrn,
        eoriEnding = eoriEnding,
        withConsigneeContactDetails = withConsigneeContactDetails,
        withDeclarantContactDetails = withDeclarantContactDetails
      )
    )

  def createMockHttpResponseWithPaymentMethods(
    first: String = "AAA",
    second: String = "AAA",
    third: String = "AAA",
    fourth: String = "AAA",
    prependMrn: String = "00",
    eoriEnding: String = "01",
    withConsigneeContactDetails: Boolean = true,
    withDeclarantContactDetails: Boolean = true
  ): MockHttpResponse = {
    val consigneeLetter: String = if (withConsigneeContactDetails) "A" else "B"
    val declarantLetter: String = if (withDeclarantContactDetails) "A" else "B"
    val mrn                     = s"$prependMrn$consigneeLetter$declarantLetter$first$second$third$fourth$eoriEnding"
    val eori                    = s"GB0000000000000$eoriEnding"
    MockHttpResponse(
      _ === MRN(mrn),
      _ === EORI(eori),
      SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
      DeclarationResponse(
        Right(
          Acc14ResponseType
            .OK_FULL_RESPONSE_SUBSIDY(
              mrn,
              eori,
              eori,
              Seq(first, second, third, fourth).filter(_ != "AAA"),
              withConsigneeContactDetails = withConsigneeContactDetails,
              withDeclarantContactDetails = withDeclarantContactDetails
            )
        )
      )
    )
  }

  // If any extra securities use cases are identified all you need to do is add extra case
  // statements to match the new criteria.
  def getSecuritiesDeclaration(mrn: MRN, reasonForSecurity: String): Option[DeclarationResponse] =
    (mrn, reasonForSecurity) match {
      case (MRN("01AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("02AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("03AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("04AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("05AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("06AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("07AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("08AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("09AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("10AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("11AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("12AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("13AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("14AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("15AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("16AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("17AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("18AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("19AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("20AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("02AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("03AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("04AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("05AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("06AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("07AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("08AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("09AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("10AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("11AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("12AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("13AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("14AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("15AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("16AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("17AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("18AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("19AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES_SECURITIES(
                mrn.value,
                reasonForSecurity,
                "GB000000000000002",
                "GB000000000000002"
              )
            )
          )
        )
      case (MRN("20AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000002", "GB000000000000002")
            )
          )
        )
      case (MRN("30ABCDEFGHIJKLMNO1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("41ABCDEFGHIJKLMNO2"), "RED") =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES_SECURITIES(
                mrn.value,
                reasonForSecurity,
                "AA12345678901234Z",
                "AA12345678901234Z"
              )
            )
          )
        )
      case (MRN("41ABCDEFGHIJKLMNO2"), _) =>
        Some(DeclarationResponse(Left(Right(Acc14ErrorResponseType.NO_SECURITY_DEPOSITS))))
      case (MRN("41ABCDEFGHIJKLMNO1"), _) =>
        Some(DeclarationResponse(Left(Right(Acc14ErrorResponseType.BAD_REQUEST_MISSING_DECLARATION))))
      case (MRN("50ABCDEFGHIJKLMNO1"), _) => Some(DeclarationResponse(Left(Right(Acc14ErrorResponseType.TIME_OUT))))
      case (MRN("40ABCDEFGHIJKLMNO3"), _) =>
        Some(DeclarationResponse(Left(Right(Acc14ErrorResponseType.HTTP_METHOD_NOT_ALLOWED))))
      case (MRN("60ABCDEFGHIJKLMNO1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_BANK_DETAILS(mrn.value, reasonForSecurity, "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01MDPAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "MDP", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01MDLAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "MDL", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01ACSAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "ACS", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01CEPAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "CEP", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01CSDAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "CSD", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01TTFAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "T24", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01TASIXAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "TA6", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01TATHRAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "TA3", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01TATWOAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "TA2", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01IPRAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "IPR", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01OPRAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "OPR", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01ENUAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "ENU", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01REDAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "RED", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01MODAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "MOD", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01MDCAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "MDC", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01CRQAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "CRQ", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("01PDDAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType.OK_FULL_RESPONSE_SECURITIES(mrn.value, "PDD", "GB000000000000001", "GB000000000000001")
            )
          )
        )
      case (MRN("10AAAAAAAAAAAAAAA9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000091",
                  "GB000000000000092"
                )
            )
          )
        )
      case (MRN("55AAAAAAAAAAAAAAA0"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000050",
                  "GB000000000000001"
                )
            )
          )
        )
      case (MRN("55AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000051",
                  "GB000000000000001"
                )
            )
          )
        )
      case (MRN("55AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000052",
                  "GB000000000000001"
                )
            )
          )
        )
      case (MRN("55AAAAAAAAAAAAAAA3"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000053"
                )
            )
          )
        )
      case (MRN("55AAAAAAAAAAAAAAA4"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000054"
                )
            )
          )
        )
      case (MRN("55AAAAAAAAAAAAAAA5"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000055"
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA1"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  false,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA2"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  true,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA3"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  false,
                  true
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA4"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000002",
                  false,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA5"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000002",
                  true,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA6"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000002",
                  false,
                  true
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA7"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000002",
                  "GB000000000000001",
                  false,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA8"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000002",
                  "GB000000000000001",
                  true,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAAA9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000002",
                  "GB000000000000001",
                  false,
                  false
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAAA10"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES_GUARANTEE(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000002",
                  false,
                  true
                )
            )
          )
        )
      case (MRN("20AAAAAAAAAAAAAAA9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000091",
                  "GB000000000000093"
                )
            )
          )
        )
      case (MRN("30AAAAAAAAAAAAAAA9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_NO_CONTACT_DETAILS_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000094",
                  "GB000000000000092"
                )
            )
          )
        )
      case (MRN("40AAAAAAAAAAAAAAA9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000091",
                  "GB000000000000092"
                )
            )
          )
        )

      case (MRN("10AAAAAAAAAAAAAAB9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_NO_CONSIGNEE_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000091"
                )
            )
          )
        )

      case (MRN("10AAAAAAAAAAAAAAC9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_NO_CONSIGNEE_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000091"
                )
            )
          )
        )

      case (MRN("10AAAAAAAAAAAAAAD9"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_NO_CONSIGNEE_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000091"
                )
            )
          )
        )

      case _ => None
    }
}
