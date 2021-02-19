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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.TimeUtils

final case class Acc14Response(
  value: JsValue
)

object Acc14Response {

  sealed trait Acc14ResponseType extends Product with Serializable
  object Acc14ResponseType {
    case object OK_MINIMUM_RESPONSE extends Acc14ResponseType
    case class OK_PARTIAL_RESPONSE(declarationId: String) extends Acc14ResponseType
    case class OK_FULL_RESPONSE(declarationId: String) extends Acc14ResponseType
    case class OK_WITH_MISMATCH_ON_EORI(declarationId: String) extends Acc14ResponseType
  }

  def returnAcc14Response(acc14ResponseType: Acc14ResponseType): Acc14Response =
    acc14ResponseType match {
      case Acc14ResponseType.OK_MINIMUM_RESPONSE => getMinimumAcc14Response
      case Acc14ResponseType.OK_PARTIAL_RESPONSE(declarationId) => getPartialAcc14Response(declarationId)
      case Acc14ResponseType.OK_FULL_RESPONSE(declarationId) => getFullAcc14Response(declarationId)
      case Acc14ResponseType.OK_WITH_MISMATCH_ON_EORI(declarationId) => getEoriMismatchResponse(declarationId)
    }

  def getMinimumAcc14Response = Acc14Response(
    Json.parse(
      s"""
     |{
     |    "overpaymentDeclarationDisplayResponse": {
     |        "responseCommon": {
     |            "status": "OK",
     |            "processingDate": "${TimeUtils.iso8061DateTimeNow}"
     |        }
     |    }
     |}
     |""".stripMargin
    )
  )

  def getPartialAcc14Response(declarationId: String) = Acc14Response(
    Json.parse(
      s"""
        |{
        |    "overpaymentDeclarationDisplayResponse": {
        |        "responseCommon": {
        |            "status": "OK",
        |            "processingDate": "${TimeUtils.iso8061DateTimeNow}"
        |        },
        |        "responseDetail": {
        |            "declarationId": "$declarationId",
        |            "acceptanceDate": "2019-08-13",
        |            "declarantReferenceNumber": "XFGLKJDSE5GDPOIJEW985T",
        |            "securityReason": "IPR",
        |            "btaDueDate": "2019-09-13",
        |            "procedureCode": "71",
        |            "btaSource": "DMS",
        |            "declarantDetails": {
        |                "declarantEORI": "GB3745678934000",
        |                "legalName": "Fred Bloggs and Co Ltd",
        |                "establishmentAddress": {
        |                    "addressLine1": "10 Rillington Place",
        |                    "addressLine2": "Pimlico",
        |                    "addressLine3": "London",
        |                    "postalCode": "W11 1RH",
        |                    "countryCode": "GB"
        |                },
        |                "contactDetails": {
        |                    "contactName": "Angela Smith",
        |                    "addressLine1": "J P Jones Insolvency Ltd",
        |                    "addressLine2": "14 Briar Lane",
        |                    "addressLine3": "Pimlico",
        |                    "postalCode": "W11 1QT",
        |                    "countryCode": "GB",
        |                    "telephone": "0270 112 3476",
        |                    "emailAddress": "fred@bloggs.com"
        |                }
        |            },
        |            "accountDetails": [
        |                {
        |                    "accountType": "a",
        |                    "accountNumber": "a",
        |                    "eori": "a",
        |                    "legalName": "a",
        |                    "contactDetails": {
        |                        "contactName": "a",
        |                        "addressLine1": "a",
        |                        "addressLine2": "a",
        |                        "addressLine3": "a",
        |                        "addressLine4": "a",
        |                        "postalCode": "a",
        |                        "countryCode": "AD",
        |                        "telephone": "a",
        |                        "emailAddress": "a"
        |                    }
        |                }
        |            ]
        |        }
        |    }
        |}
        |""".stripMargin
    )
  )

  def getFullAcc14Response(declarationId: String) = Acc14Response(
    Json.parse(
      s"""
        |{
        |	"overpaymentDeclarationDisplayResponse": {
        |		"responseCommon": {
        |			"status": "OK",
        |			"processingDate": "2021-02-12T11:34:54Z"
        |		},
        |		"responseDetail": {
        |			"declarationId": "$declarationId",
        |			"acceptanceDate": "2021-02-12",
        |			"procedureCode": "2",
        |			"declarantDetails": {
        |				"declarantEORI": "AA12345678901234Z",
        |				"legalName": "Automation Central LTD",
        |				"establishmentAddress": {
        |					"addressLine1": "10 Automation Road",
        |					"addressLine3": "Coventry",
        |					"postalCode": "CV3 6EA",
        |					"countryCode": "GB"
        |				},
        |				"contactDetails": {
        |					"contactName": "Automation Central LTD",
        |					"addressLine1": "10 Automation Road",
        |					"addressLine3": "Coventry",
        |					"postalCode": "CV3 6EA",
        |					"countryCode": "GB"
        |				}
        |			},
        |			"consigneeDetails": {
        |				"consigneeEORI": "AA12345678901234Z",
        |				"legalName": "Automation Central LTD",
        |				"establishmentAddress": {
        |					"addressLine1": "10 Automation Road",
        |					"addressLine3": "Coventry",
        |					"postalCode": "CV3 6EA",
        |					"countryCode": "GB"
        |				},
        |				"contactDetails": {
        |					"contactName": "Automation Central LTD",
        |					"addressLine1": "10 Automation Road",
        |					"addressLine3": "Coventry",
        |					"postalCode": "CV3 6EA",
        |					"countryCode": "GB",
        |         "telephone": "+4420723934397",
        |         "emailAddress" : "automation@gmail.com"
        |				}
        |			},
        |			"bankDetails": {
        |				"consigneeBankDetails": {
        |					"accountHolderName": "CDS E2E To E2E Bank",
        |					"sortCode": "308844",
        |					"accountNumber": "12345678"
        |				},
        |				"declarantBankDetails": {
        |					"accountHolderName": "CDS E2E To E2E Bank",
        |					"sortCode": "308844",
        |					"accountNumber": "12345678"
        |				}
        |			},
        |			"ndrcDetails": [
        |				{
        |					"taxType": "A80",
        |					"amount": "218.00",
        |					"paymentMethod": "001",
        |					"paymentReference": "GB201430007000"
        |				},
        |				{
        |					"taxType": "A95",
        |					"amount": "211.00",
        |					"paymentMethod": "001",
        |					"paymentReference": "GB201430007000"
        |				},
        |				{
        |					"taxType": "A90",
        |					"amount": "228.00",
        |					"paymentMethod": "001",
        |					"paymentReference": "GB201430007000"
        |				},
        |				{
        |					"taxType": "A85",
        |					"amount": "171.00",
        |					"paymentMethod": "001",
        |					"paymentReference": "GB201430007000"
        |				}
        |			]
        |		}
        |	}
        |}
        |""".stripMargin
    )
  )

  def getEoriMismatchResponse(declarationId: String) = Acc14Response(
    Json.parse(
      s"""
         |{
         |	"overpaymentDeclarationDisplayResponse": {
         |		"responseCommon": {
         |			"status": "OK",
         |			"processingDate": "2021-02-12T11:34:54Z"
         |		},
         |		"responseDetail": {
         |			"declarationId": "$declarationId",
         |			"acceptanceDate": "2021-02-12",
         |			"procedureCode": "2",
         |			"declarantDetails": {
         |				"declarantEORI": "9GB03I52858027018",
         |				"legalName": "Automation Central LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "10 Automation Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Automation Central LTD",
         |					"addressLine1": "10 Automation Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"consigneeDetails": {
         |				"consigneeEORI": "9GB03I52858027018",
         |				"legalName": "Automation Central LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "10 Automation Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Automation Central LTD",
         |					"addressLine1": "10 Automation Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"bankDetails": {
         |				"consigneeBankDetails": {
         |					"accountHolderName": "CDS E2E To E2E Bank",
         |					"sortCode": "308844",
         |					"accountNumber": "12345678"
         |				},
         |				"declarantBankDetails": {
         |					"accountHolderName": "CDS E2E To E2E Bank",
         |					"sortCode": "308844",
         |					"accountNumber": "12345678"
         |				}
         |			},
         |			"ndrcDetails": [
         |				{
         |					"taxType": "A80",
         |					"amount": "218.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000"
         |				},
         |				{
         |					"taxType": "A95",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )

}
