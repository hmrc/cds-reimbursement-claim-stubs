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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import akka.actor.ActorSystem
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

class DeclarationControllerSpec extends AnyWordSpec with Matchers with TypeCheckedTripleEquals {

  val MRNs: Seq[(String, Int)] = Seq(
    ("00AMRAAAAAAAAAAA01", 200),
    ("00AMRAAAAAAAAAAA50", 200), // If the last number is 50 or above then the EORI sent back is XI
    ("00AMRAAAAAAAAAAAAA", 200), // Last two characters should be numbers but if not it shouldn't fail; those two numbers determine the EORI sent back
    ("00AFRAAAAAAAAAAA01", 200),
    ("00AFSAAAAAAAAAAA01", 200),
    ("00AMSAAAAAAAAAAA01", 200),
    ("00AO1AAAAAAAAAAA01", 200),
    ("00AO2AAAAAAAAAAA01", 200),
    ("00AO3AAAAAAAAAAA01", 200),
    ("00AOVAAAAAAAAAAA01", 200),
    ("00AFVAAAAAAAAAAA01", 200),
    ("00AATAAAAAAAAAAA01", 200),
    ("00AMEAAAAAAAAAAA01", 200),
    ("00ANIAAAAAAAAAAA01", 200),
    ("00ANDAAAAAAAAAAA01", 200),
    ("00ANCAAAAAAAAAAA01", 200),
    ("00ANBAAAAAAAAAAA01", 200),
    ("00ASRAAAAAAAAAAA01", 200), // When the security reason is not provided it should still not fail
    ("00ASRACSAAAAAAAA01", 200),
    ("00ASRMDCAAAAAAAA01", 200),
    ("00ASRENUAAAAAAAA01", 200),
    ("00ASRIPRAAAAAAAA01", 200),
    ("00ASRMODAAAAAAAA01", 200),
    ("00ASRMDLAAAAAAAA01", 200),
    ("00ASRMDPAAAAAAAA01", 200),
    ("00ASROPRAAAAAAAA01", 200),
    ("00ASRREDAAAAAAAA01", 200),
    ("00ASRT24AAAAAAAA01", 200),
    ("00ASRTA6AAAAAAAA01", 200),
    ("00ASRTA3AAAAAAAA01", 200),
    ("00ASRTA2AAAAAAAA01", 200),
    ("00ASRPDDAAAAAAAA01", 200),
    ("00ASRCRQAAAAAAAA01", 200),
    ("00ASRCEPAAAAAAAA01", 200),
    ("00ASRCSDAAAAAAAA01", 200),
    ("00ACSAAAAAAAAAAA01", 200),
    ("00ANSAAAAAAAAAAA01", 200),
    ("00AASAAAAAAAAAAA01", 200),
    ("00AWFAAAAAAAAAAA01", 403),
    ("00AEBAAAAAAAAAAA01", 400),
    ("00AESAAAAAAAAAAA01", 400),
    ("00AENAAAAAAAAAAA01", 405),
    ("00AETAAAAAAAAAAA01", 500),
    ("00AAAAAAAAAAAAAA01", 400), // Unknown response type which in this case is AA makes it default to BAD_REQUEST

  )

  val controller = new DeclarationController(Helpers.stubControllerComponents())

  "A DeclarationController" should {
    MRNs.foreach { case (mrn, expectedStatus) =>
      s"return status: $expectedStatus with ACC14 declaration for MRN: $mrn" in {
        val payload: JsValue =
          Json.parse(s"""|{
                        |    "overpaymentDeclarationDisplayRequest": {
                        |        "requestCommon": {
                        |            "originatingSystem": "MDTP",
                        |            "receiptDate": "2023-04-21T07:35:39Z",
                        |            "acknowledgementReference": "9474bdf1df434462b1cd911ddf64a3d5"
                        |        },
                        |        "requestDetail": {
                        |            "declarationId": "$mrn"
                        |        }
                        |    }
                        |}
                        |""".stripMargin)

        val request = FakeRequest("POST", "/accounts/overpaymentdeclarationdisplay/v1")
          .withHeaders(("Content-Type", "application/json"))
          .withBody(payload)
        val result  = controller.getDeclaration(request)

        status(result) should ===(expectedStatus)
      }
    }

    MRNs.foreach { case (mrn, expectedStatus) =>
            s"SECURITIES: return status: $expectedStatus with ACC14 declaration for MRN: $mrn" in {
              val payload: JsValue =
                Json.parse(s"""|{
                            |    "overpaymentDeclarationDisplayRequest": {
                            |        "requestCommon": {
                            |            "originatingSystem": "MDTP",
                            |            "receiptDate": "2023-04-21T07:35:39Z",
                            |            "acknowledgementReference": "9474bdf1df434462b1cd911ddf64a3d5"
                            |        },
                            |        "requestDetail": {
                            |            "declarationId": "$mrn",
                            |            "securityReason": "MDP"
                            |        }
                            |    }
                            |}
                            |""".stripMargin)

              val request = FakeRequest("POST", "/accounts/overpaymentdeclarationdisplay/v1")
                .withHeaders(("Content-Type", "application/json"))
                .withBody(payload)
              val result  = controller.getDeclaration(request)

              status(result) should ===(expectedStatus)
            }
          }

  }
}
