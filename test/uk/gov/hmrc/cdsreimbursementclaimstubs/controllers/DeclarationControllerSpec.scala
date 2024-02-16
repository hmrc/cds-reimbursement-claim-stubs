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

import org.apache.pekko.actor.ActorSystem
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

class DeclarationControllerSpec extends AnyWordSpec with Matchers with TypeCheckedTripleEquals {

  implicit val actorSystem: ActorSystem = ActorSystem()

  val ndrcSuccessfulMRNs = Seq(
    "01AAAAAAAAAAAAAAA1",
    "01AAAAAAAAAAAAAAA2",
    "02AAAAAAAAAAAAAAA1",
    "02AAAAAAAAAAAAAAA2",
    "03AAAAAAAAAAAAAAA1",
    "03AAAAAAAAAAAAAAA2",
    "04AAAAAAAAAAAAAAA1",
    "04AAAAAAAAAAAAAAA2",
    "05AAAAAAAAAAAAAAA1",
    "05AAAAAAAAAAAAAAA2",
    "06AAAAAAAAAAAAAAA1",
    "06AAAAAAAAAAAAAAA2",
    "07AAAAAAAAAAAAAAA1",
    "07AAAAAAAAAAAAAAA2",
    "08AAAAAAAAAAAAAAA1",
    "08AAAAAAAAAAAAAAA2",
    "09AAAAAAAAAAAAAAA1",
    "09AAAAAAAAAAAAAAA2",
    "10AAAAAAAAAAAAAAA1",
    "10AAAAAAAAAAAAAAA2",
    "10AAAAAAAAAAAAAAA3",
    "10AAAAAAAAAAAAAAA6",
    "10AAAAAAAAAAAAAAA7",
    "10AAAAAAAAAAAAAAA8",
    "10AAAAAAAAAAAAAAA9",
    "10AAAAAAAAAAAAAAB9",
    "10AAAAAAAAAAAAAAC9",
    "10AAAAAAAAAAAAAAD9",
    "10ABCDEFGHIJKLMNO0",
    "10ABCDEFGHIJKLMNO1",
    "10BBBBBBBBBBBBBBB1",
    "10BBBBBBBBBBBBBBB2",
    "10CCCCCCCCCCCCCCC1",
    "10CCCCCCCCCCCCCCC2",
    "10XXXXXXXXXXXXXXX1",
    "10YYYYYYYYYYYYYYY1",
    "10ZZZZZZZZZZZZZZZ1",
    "11AAAAAAAAAAAAAAA1",
    "11AAAAAAAAAAAAAAA2",
    "12AAAAAAAAAAAAAAA1",
    "12AAAAAAAAAAAAAAA2",
    "13AAAAAAAAAAAAAAA1",
    "13AAAAAAAAAAAAAAA2",
    "14AAAAAAAAAAAAAAA1",
    "14AAAAAAAAAAAAAAA2",
    "15AAAAAAAAAAAAAAA1",
    "15AAAAAAAAAAAAAAA2",
    "16AAAAAAAAAAAAAAA1",
    "16AAAAAAAAAAAAAAA2",
    "17AAAAAAAAAAAAAAA1",
    "17AAAAAAAAAAAAAAA2",
    "18AAAAAAAAAAAAAAA1",
    "18AAAAAAAAAAAAAAA2",
    "19AAAAAAAAAAAAAAA1",
    "19AAAAAAAAAAAAAAA2",
    "20AAAAAAAAAAAAAAA1",
    "20AAAAAAAAAAAAAAA2",
    "20AAAAAAAAAAAAAAA9",
    "20ABCDEFGHIJKLMNO0",
    "20ABCDEFGHIJKLMNO1",
    "20BBBBBBBBBBBBBBB1",
    "20BBBBBBBBBBBBBBB2",
    "20CCCCCCCCCCCCCCC1",
    "20CCCCCCCCCCCCCCC2",
    "21ABCDEFGHIJKLMNO0",
    "22ABCDEFGHIJKLMNO1",
    "30AAAAAAAAAAAAAAA9",
    "30ABCDEFGHIJKLMNO0",
    "30ABCDEFGHIJKLMNO1",
    "40AAAAAAAAAAAAAAA9",
    "40ABCDEFGHIJKLMNO1",
    "50AAAAAAAAAAAAAAA1",
    "90ABCDEFGHIJKLMNO0",
    "01XIDAAAAAAAAAAAA1",
    "01XIDAAAAAAAAAAAA2",
    "02XIDAAAAAAAAAAAA1",
    "02XIDAAAAAAAAAAAA2",
    "03XIDAAAAAAAAAAAA1",
    "03XIDAAAAAAAAAAAA2",
    "04XIDAAAAAAAAAAAA1",
    "04XIDAAAAAAAAAAAA2",
    "05XIDAAAAAAAAAAAA1",
    "05XIDAAAAAAAAAAAA2",
    "06XIDAAAAAAAAAAAA1",
    "06XIDAAAAAAAAAAAA2",
    "07XIDAAAAAAAAAAAA1",
    "07XIDAAAAAAAAAAAA2",
    "08XIDAAAAAAAAAAAA1",
    "08XIDAAAAAAAAAAAA2",
    "09XIDAAAAAAAAAAAA1",
    "09XIDAAAAAAAAAAAA2",
    "10XIDAAAAAAAAAAAA1",
    "10XIDAAAAAAAAAAAA2",
    "10XIDAAAAAAAAAAAA3",
    "10XIDAAAAAAAAAAAA6",
    "10XIDAAAAAAAAAAAA7",
    "10XIDAAAAAAAAAAAA8",
    "10XIDAAAAAAAAAAAA9",
    "10XIDAAAAAAAAAAAB9",
    "10XIDAAAAAAAAAAAC9",
    "10XIDAAAAAAAAAAAD9",
    "11XIDAAAAAAAAAAAA1",
    "11XIDAAAAAAAAAAAA2",
    "12XIDAAAAAAAAAAAA1",
    "12XIDAAAAAAAAAAAA2",
    "13XIDAAAAAAAAAAAA1",
    "13XIDAAAAAAAAAAAA2",
    "14XIDAAAAAAAAAAAA1",
    "14XIDAAAAAAAAAAAA2",
    "15XIDAAAAAAAAAAAA1",
    "15XIDAAAAAAAAAAAA2",
    "16XIDAAAAAAAAAAAA1",
    "16XIDAAAAAAAAAAAA2",
    "17XIDAAAAAAAAAAAA1",
    "17XIDAAAAAAAAAAAA2",
    "18XIDAAAAAAAAAAAA1",
    "18XIDAAAAAAAAAAAA2",
    "19XIDAAAAAAAAAAAA1",
    "19XIDAAAAAAAAAAAA2",
    "20XIDAAAAAAAAAAAA1",
    "20XIDAAAAAAAAAAAA2",
    "20XIDAAAAAAAAAAAA9",
    "30XIDAAAAAAAAAAAA9",
    "40XIDAAAAAAAAAAAA9",
    "50XIDAAAAAAAAAAAA1",
    "01XICAAAAAAAAAAAA1",
    "01XICAAAAAAAAAAAA2",
    "02XICAAAAAAAAAAAA1",
    "02XICAAAAAAAAAAAA2",
    "03XICAAAAAAAAAAAA1",
    "03XICAAAAAAAAAAAA2",
    "04XICAAAAAAAAAAAA1",
    "04XICAAAAAAAAAAAA2",
    "05XICAAAAAAAAAAAA1",
    "05XICAAAAAAAAAAAA2",
    "06XICAAAAAAAAAAAA1",
    "06XICAAAAAAAAAAAA2",
    "07XICAAAAAAAAAAAA1",
    "07XICAAAAAAAAAAAA2",
    "08XICAAAAAAAAAAAA1",
    "08XICAAAAAAAAAAAA2",
    "09XICAAAAAAAAAAAA1",
    "09XICAAAAAAAAAAAA2",
    "10XICAAAAAAAAAAAA1",
    "10XICAAAAAAAAAAAA2",
    "10XICAAAAAAAAAAAA3",
    "10XICAAAAAAAAAAAA6",
    "10XICAAAAAAAAAAAA7",
    "10XICAAAAAAAAAAAA8",
    "10XICAAAAAAAAAAAA9",
    "10XICAAAAAAAAAAAB9",
    "10XICAAAAAAAAAAAC9",
    "10XICAAAAAAAAAAAD9",
    "10DDDDDDDDDDDDDDD1",
    "10DDDDDDDDDDDDDDD2",
    "10DDDDDDDDDDDDDDD3",
    "10DDDDDDDDDDDDDDD4"
  )

  val ndrcFailingMRNs = Seq(
    ("01CEPAAAAAAAAAAAA1", 400),
    ("01ACSAAAAAAAAAAAA1", 400),
    ("01CRQAAAAAAAAAAAA1", 400),
    ("01CSDAAAAAAAAAAAA1", 400),
    ("01ENUAAAAAAAAAAAA1", 400),
    ("01IPRAAAAAAAAAAAA1", 400),
    ("01MDCAAAAAAAAAAAA1", 400),
    ("01MDLAAAAAAAAAAAA1", 400),
    ("01MDPAAAAAAAAAAAA1", 400),
    ("01MODAAAAAAAAAAAA1", 400),
    ("01OPRAAAAAAAAAAAA1", 400),
    ("01PDDAAAAAAAAAAAA1", 400),
    ("01REDAAAAAAAAAAAA1", 400),
    ("01TASIXAAAAAAAAAA1", 400),
    ("01TATHRAAAAAAAAAA1", 400),
    ("01TATWOAAAAAAAAAA1", 400),
    ("01TTFAAAAAAAAAAAA1", 400),
    ("41ABCDEFGHIJKLMNO1", 400),
    ("41ABCDEFGHIJKLMNO2", 400),
    ("60ABCDEFGHIJKLMNO1", 400),
    ("40ABCDEFGHIJKLMNO3", 405),
    ("50ABCDEFGHIJKLMNO1", 500)
  )

  val securitiesSuccessfulMRNs = Seq(
    "01AAAAAAAAAAAAAAA1",
    "01AAAAAAAAAAAAAAA2",
    "01ACSAAAAAAAAAAAA1",
    "01CEPAAAAAAAAAAAA1",
    "01CRQAAAAAAAAAAAA1",
    "01CSDAAAAAAAAAAAA1",
    "01ENUAAAAAAAAAAAA1",
    "01IPRAAAAAAAAAAAA1",
    "01MDCAAAAAAAAAAAA1",
    "01MDLAAAAAAAAAAAA1",
    "01MDPAAAAAAAAAAAA1",
    "01MODAAAAAAAAAAAA1",
    "01OPRAAAAAAAAAAAA1",
    "01PDDAAAAAAAAAAAA1",
    "01REDAAAAAAAAAAAA1",
    "01TASIXAAAAAAAAAA1",
    "01TATHRAAAAAAAAAA1",
    "01TATWOAAAAAAAAAA1",
    "01TTFAAAAAAAAAAAA1",
    "02AAAAAAAAAAAAAAA1",
    "02AAAAAAAAAAAAAAA2",
    "03AAAAAAAAAAAAAAA1",
    "03AAAAAAAAAAAAAAA2",
    "04AAAAAAAAAAAAAAA1",
    "04AAAAAAAAAAAAAAA2",
    "05AAAAAAAAAAAAAAA1",
    "05AAAAAAAAAAAAAAA2",
    "06AAAAAAAAAAAAAAA1",
    "06AAAAAAAAAAAAAAA2",
    "07AAAAAAAAAAAAAAA1",
    "07AAAAAAAAAAAAAAA2",
    "08AAAAAAAAAAAAAAA1",
    "08AAAAAAAAAAAAAAA2",
    "09AAAAAAAAAAAAAAA1",
    "09AAAAAAAAAAAAAAA2",
    "10AAAAAAAAAAAAAAA1",
    "10AAAAAAAAAAAAAAA2",
    "10AAAAAAAAAAAAAAA9",
    "10AAAAAAAAAAAAAAB9",
    "10AAAAAAAAAAAAAAC9",
    "10AAAAAAAAAAAAAAD9",
    "11AAAAAAAAAAAAAAA1",
    "11AAAAAAAAAAAAAAA2",
    "12AAAAAAAAAAAAAAA1",
    "12AAAAAAAAAAAAAAA2",
    "13AAAAAAAAAAAAAAA1",
    "13AAAAAAAAAAAAAAA2",
    "14AAAAAAAAAAAAAAA1",
    "14AAAAAAAAAAAAAAA2",
    "15AAAAAAAAAAAAAAA1",
    "15AAAAAAAAAAAAAAA2",
    "16AAAAAAAAAAAAAAA1",
    "16AAAAAAAAAAAAAAA2",
    "17AAAAAAAAAAAAAAA1",
    "17AAAAAAAAAAAAAAA2",
    "18AAAAAAAAAAAAAAA1",
    "18AAAAAAAAAAAAAAA2",
    "19AAAAAAAAAAAAAAA1",
    "19AAAAAAAAAAAAAAA2",
    "20AAAAAAAAAAAAAAA1",
    "20AAAAAAAAAAAAAAA2",
    "20AAAAAAAAAAAAAAA9",
    "30AAAAAAAAAAAAAAA9",
    "30ABCDEFGHIJKLMNO1",
    "40AAAAAAAAAAAAAAA9",
    "60ABCDEFGHIJKLMNO1"
  )

  val securitiesFailingMRNs = Seq(
    ("41ABCDEFGHIJKLMNO1", 400),
    ("41ABCDEFGHIJKLMNO2", 400),
    ("40ABCDEFGHIJKLMNO3", 405),
    ("50ABCDEFGHIJKLMNO1", 500)
  )

  val controller = new DeclarationController(Helpers.stubControllerComponents())

  "A DeclarationController" should {
    ndrcSuccessfulMRNs.foreach { mrn =>
      s"return 200 with ACC14 NDRC declaration $mrn" in {
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

        status(result) should ===(OK)
      }
    }

    ndrcFailingMRNs.foreach { case (mrn, expectedStatus) =>
      s"return $expectedStatus for NDRC $mrn" in {
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

    securitiesSuccessfulMRNs.foreach { mrn =>
      s"return 200 with ACC14 SECURITIES declaration $mrn" in {
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

        status(result) should ===(OK)
      }
    }

    securitiesFailingMRNs.foreach { case (mrn, expectedStatus) =>
      s"return $expectedStatus for SECURITIES $mrn" in {
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
