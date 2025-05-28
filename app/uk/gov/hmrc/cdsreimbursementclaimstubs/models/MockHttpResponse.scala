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

  def getSubmitClaimHttpResponse(eori: EORI, mrnOpt: Option[MRN]): Option[MockHttpResponse] =
    httpResponses
      .find(c => c.eoriPredicate(eori) && mrnOpt.forall(c.mrnPredicate(_)))
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
        _ === MRN("01AA99999999999999"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.BAD_REQUEST_MISSING_DECLARATION)))
      ),
      MockHttpResponse(
        _ === MRN("01AA99999999999998"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.BAD_REQUEST_MISSING_DECLARATION)))
      ),
      MockHttpResponse(
        _ === MRN("01AA99999999999997"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(Left(Right(Acc14ErrorResponseType.BAD_REQUEST_MISSING_DECLARATION)))
      ),
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAAAAAA1"),
        _ === EORI("HELLO"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAAAAAA1", "HELLO", "LEEDS"))
        )
      ),
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAHELLO1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAHELLO1", "HELLO", "LEEDS"))
        )
      ),
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAALEEDS1"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAALEEDS1", "GB000000000000001", "LEEDS"))
        )
      ),
      MockHttpResponse(
        _ === MRN("01AAAAAAAAAAAAAAA1"),
        _ === EORI("PL1"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("01AAAAAAAAAAAAAAA1", "PL1", "PL2"))
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
        _ === MRN("10AAAAAAAAAAAAA301"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAA301",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("301", "711.84", "GB201430007000", 1),
                ("311", "197.31", "GB201430007000", 1),
                ("312", "710.79", "GB201430007000", 1),
                ("313", "288.53", "GB201430007000", 1),
                ("314", "340.13", "GB201430007000", 1),
                ("315", "715.48", "GB201430007000", 1),
                ("321", "925.21", "GB201430007000", 1),
                ("322", "47.56", "GB201430007000", 1),
                ("323", "333.02", "GB201430007000", 1),
                ("324", "800.95", "GB201430007000", 1),
                ("325", "47.3", "GB201430007000", 1),
                ("331", "699.35", "GB201430007000", 1),
                ("333", "202.62", "GB201430007000", 1),
                ("334", "549.58", "GB201430007000", 1),
                ("335", "144.15", "GB201430007000", 1),
                ("341", "208.75", "GB201430007000", 1),
                ("343", "930.6", "GB201430007000", 1),
                ("344", "323.68", "GB201430007000", 1),
                ("345", "627.13", "GB201430007000", 1),
                ("351", "292.26", "GB201430007000", 1),
                ("352", "93.58", "GB201430007000", 1),
                ("353", "246.3", "GB201430007000", 1),
                ("353", "506.01", "GB201430007000", 1),
                ("354", "818.51", "GB201430007000", 1),
                ("355", "939.36", "GB201430007000", 1),
                ("356", "331.02", "GB201430007000", 1),
                ("357", "759.9", "GB201430007000", 1),
                ("358", "641.73", "GB201430007000", 1),
                ("359", "244.33", "GB201430007000", 1),
                ("360", "787.74", "GB201430007000", 1),
                ("361", "62.44", "GB201430007000", 1),
                ("362", "366.51", "GB201430007000", 1),
                ("363", "979.09", "GB201430007000", 1),
                ("364", "155.78", "GB201430007000", 1),
                ("365", "185.42", "GB201430007000", 1),
                ("366", "168.02", "GB201430007000", 1),
                ("367", "36.78", "GB201430007000", 1),
                ("368", "307.04", "GB201430007000", 1),
                ("369", "23.23", "GB201430007000", 1),
                ("370", "199.4", "GB201430007000", 1),
                ("371", "537.11", "GB201430007000", 1),
                ("372", "34.74", "GB201430007000", 1),
                ("373", "330.06", "GB201430007000", 1),
                ("374", "306.21", "GB201430007000", 1),
                ("375", "509.56", "GB201430007000", 1),
                ("376", "897.5", "GB201430007000", 1),
                ("377", "942.18", "GB201430007000", 1),
                ("378", "737.69", "GB201430007000", 1),
                ("379", "80.17", "GB201430007000", 1),
                ("380", "223.9", "GB201430007000", 1),
                ("407", "672.1", "GB201430007000", 1),
                ("411", "732.01", "GB201430007000", 1),
                ("412", "426.54", "GB201430007000", 1),
                ("413", "909.49", "GB201430007000", 1),
                ("415", "364.17", "GB201430007000", 1),
                ("419", "341.82", "GB201430007000", 1),
                ("421", "314.81", "GB201430007000", 1),
                ("422", "750.83", "GB201430007000", 1),
                ("423", "492.59", "GB201430007000", 1),
                ("425", "76.23", "GB201430007000", 1),
                ("429", "206.85", "GB201430007000", 1),
                ("431", "906.48", "GB201430007000", 1),
                ("433", "292.8", "GB201430007000", 1),
                ("435", "403.71", "GB201430007000", 1),
                ("438", "451.6", "GB201430007000", 1),
                ("440", "167.08", "GB201430007000", 1),
                ("441", "377.93", "GB201430007000", 1),
                ("442", "929.11", "GB201430007000", 1),
                ("443", "41.8", "GB201430007000", 1),
                ("444", "73.17", "GB201430007000", 1),
                ("445", "312.24", "GB201430007000", 1),
                ("446", "78.62", "GB201430007000", 1),
                ("447", "60.77", "GB201430007000", 1),
                ("451", "420.06", "GB201430007000", 1),
                ("461", "276.99", "GB201430007000", 1),
                ("462", "581.07", "GB201430007000", 1),
                ("463", "644.76", "GB201430007000", 1),
                ("473", "499.24", "GB201430007000", 1),
                ("481", "561.77", "GB201430007000", 1),
                ("483", "381.96", "GB201430007000", 1),
                ("485", "96.23", "GB201430007000", 1),
                ("487", "759.87", "GB201430007000", 1),
                ("511", "739.73", "GB201430007000", 1),
                ("520", "354.07", "GB201430007000", 1),
                ("521", "675.34", "GB201430007000", 1),
                ("522", "692.3", "GB201430007000", 1),
                ("540", "292.68", "GB201430007000", 1),
                ("541", "927.03", "GB201430007000", 1),
                ("542", "769.44", "GB201430007000", 1),
                ("546", "384.62", "GB201430007000", 1),
                ("551", "140.48", "GB201430007000", 1),
                ("556", "548.71", "GB201430007000", 1),
                ("561", "594.9", "GB201430007000", 1),
                ("570", "384.31", "GB201430007000", 1),
                ("571", "998.75", "GB201430007000", 1),
                ("572", "712.2", "GB201430007000", 1),
                ("589", "320.43", "GB201430007000", 1),
                ("591", "208.58", "GB201430007000", 1),
                ("592", "740.07", "GB201430007000", 1),
                ("595", "134.9", "GB201430007000", 1),
                ("597", "518.17", "GB201430007000", 1),
                ("611", "90.51", "GB201430007000", 1),
                ("615", "965.81", "GB201430007000", 1),
                ("619", "559.96", "GB201430007000", 1),
                ("623", "146.28", "GB201430007000", 1),
                ("627", "322.24", "GB201430007000", 1),
                ("633", "809.62", "GB201430007000", 1),
                ("99A", "565.29", "GB201430007000", 1),
                ("99B", "846.14", "GB201430007000", 1),
                ("99C", "862.26", "GB201430007000", 1),
                ("99D", "587.85", "GB201430007000", 1)
              )
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
        _ === MRN("10AAAAAAAAAAAAA900"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAA900",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("900", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAAAA901"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAAAA901",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(("901", "12345.67", "GB000000000000", 1))
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAA900901"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAA900901",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("900", "12345.67", "GB000000000000", 0),
                ("901", "12345.67", "GB000000000000", 1)
              )
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAA00900"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAA00900",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("A00", "12345.67", "GB000000000000", 0),
                ("900", "12345.67", "GB000000000000", 1)
              )
            )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10AAAAAAAAAAA00901"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              "10AAAAAAAAAAA00901",
              "GB000000000000001",
              "GB000000000000001",
              duties = Seq(
                ("A00", "12345.67", "GB000000000000", 0),
                ("901", "12345.67", "GB000000000000", 1)
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
                declarationId = "60AAAAAAAAAAAAAAA1",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = false
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
                declarationId = "60AAAAAAAAAAAAAAA2",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = true,
                includeDeclarantBankDetails = false
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
                declarationId = "60AAAAAAAAAAAAAAA3",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = true
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
                declarationId = "60AAAAAAAAAAAAAAA4",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000002",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = false
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
                declarationId = "60AAAAAAAAAAAAAAA5",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000002",
                includeConsigneeBankDetails = true,
                includeDeclarantBankDetails = false
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
                declarationId = "60AAAAAAAAAAAAAAA6",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000002",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = true
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
                declarationId = "60AAAAAAAAAAAAAAA7",
                importerEORI = "GB000000000000002",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = false
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
                declarationId = "60AAAAAAAAAAAAAAA8",
                importerEORI = "GB000000000000002",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = true,
                includeDeclarantBankDetails = false
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
                declarationId = "60AAAAAAAAAAAAAAA9",
                importerEORI = "GB000000000000002",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = true
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
                declarationId = "60DDDDDDDDDDDDDDD1",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = false,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD2",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = true,
                includeDeclarantBankDetails = false,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD3",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = true,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD4",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000002",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = false,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD5",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000002",
                includeConsigneeBankDetails = true,
                includeDeclarantBankDetails = false,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD6",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000002",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = true,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD7",
                importerEORI = "GB000000000000002",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = false,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD8",
                importerEORI = "GB000000000000002",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = true,
                includeDeclarantBankDetails = false,
                paymentMethods = Seq("006")
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
                declarationId = "60DDDDDDDDDDDDDDD9",
                importerEORI = "GB000000000000002",
                declarantEORI = "GB000000000000001",
                includeConsigneeBankDetails = false,
                includeDeclarantBankDetails = true,
                paymentMethods = Seq("006")
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
              .OK_FULL_RESPONSE_SUBSIDY(
                declarationId = "10DDDDDDDDDDDDDDD1",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006")
              )
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
                declarationId = "10DDDDDDDDDDDDDDD2",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006", "001")
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
                declarationId = "10DDDDDDDDDDDDDDD3",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006", "003")
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
                declarationId = "10DDDDDDDDDDDDDDD4",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006", "001", "002", "003")
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
              .OK_FULL_RESPONSE_SUBSIDY(
                declarationId = "10DDDDDDDDDDDDDDD5",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006")
              )
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
              .OK_FULL_RESPONSE_SUBSIDY(
                declarationId = "10DDDDDDDDDDDDDDD6",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006")
              )
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
              .OK_FULL_RESPONSE_SUBSIDY(
                declarationId = "10DDDDDDDDDDDDDDD7",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006")
              )
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
              .OK_FULL_RESPONSE_SUBSIDY(
                declarationId = "10DDDDDDDDDDDDDDD8",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006")
              )
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
              .OK_FULL_RESPONSE_SUBSIDY(
                declarationId = "10DDDDDDDDDDDDDDD9",
                importerEORI = "GB000000000000001",
                declarantEORI = "GB000000000000001",
                paymentMethods = Seq("006")
              )
          )
        )
      ),
      MockHttpResponse(
        _ === MRN("10ABCDEFGHIJKLMNO0"),
        _ === EORI("AA12345678901234Z"),
        SubmitClaimResponse(Right(Tpi05ResponseType.OK_RESPONSE)),
        DeclarationResponse(
          Right(
            Acc14ResponseType.OK_FULL_RESPONSE(
              declarationId = "10ABCDEFGHIJKLMNO0",
              importerEORI = "GB123456789012345",
              declarantEORI = "GB123456789012345"
            )
          )
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
        _ === MRN("10ABCDEFGHIJKLMNO2"),
        _ === EORI("GB000000000000001"),
        SubmitClaimResponse(Left(Left(WafErrorResponse.FORBIDDEN))),
        DeclarationResponse(
          Right(Acc14ResponseType.OK_FULL_RESPONSE("10ABCDEFGHIJKLMNO2", "GB000000000000001", "GB000000000000001"))
        )
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
      case (MRN("01AAAAAAAAAAAAA111"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 1,
                  numberOfSecuritiesDuties = 2
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA110"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 1,
                  numberOfSecuritiesDuties = 1
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA222"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 2,
                  numberOfSecuritiesDuties = 2
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA220"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 2,
                  numberOfSecuritiesDuties = 1
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA333"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 3,
                  numberOfSecuritiesDuties = 2
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA330"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 3,
                  numberOfSecuritiesDuties = 1
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA444"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 4,
                  numberOfSecuritiesDuties = 2
                )
            )
          )
        )
      case (MRN("01AAAAAAAAAAAAA440"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_FULL_RESPONSE_SECURITIES(
                  mrn.value,
                  reasonForSecurity,
                  "GB000000000000001",
                  "GB000000000000001",
                  numberOfSecurities = 4,
                  numberOfSecuritiesDuties = 1
                )
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000001",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000001",
                  includeConsigneeBankDetails = true,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000001",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = true
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000002",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000002",
                  includeConsigneeBankDetails = true,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000002",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = true
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000002",
                  declarantEORI = "GB000000000000001",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000002",
                  declarantEORI = "GB000000000000001",
                  includeConsigneeBankDetails = true,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000002",
                  declarantEORI = "GB000000000000001",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = false
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
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000002",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = true,
                  numberOfSecurities = 5,
                  numberOfSecuritiesDuties = 2
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAA111"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES_GUARANTEE(
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000002",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = true,
                  numberOfSecurities = 1,
                  numberOfSecuritiesDuties = 2
                )
            )
          )
        )
      case (MRN("60AAAAAAAAAAAAA110"), _) =>
        Some(
          DeclarationResponse(
            Right(
              Acc14ResponseType
                .OK_RESPONSE_SPECIFIC_BANK_DETAILS_SECURITIES_GUARANTEE(
                  declarationId = mrn.value,
                  reasonForSecurity = reasonForSecurity,
                  importerEORI = "GB000000000000001",
                  declarantEORI = "GB000000000000002",
                  includeConsigneeBankDetails = false,
                  includeDeclarantBankDetails = true,
                  numberOfSecurities = 1,
                  numberOfSecuritiesDuties = 1
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
