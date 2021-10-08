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
    case class OK_FULL_RESPONSE(declarationId: String, importerEORI: String, declarantEORI: String) extends Acc14ResponseType
    case class OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES(declarationId: String, importerEORI: String, declarantEORI: String) extends Acc14ResponseType
    case class OK_WITH_MISMATCH_ON_EORI(declarationId: String) extends Acc14ResponseType
    case class OK_FULL_RESPONSE_NORTHERN_IRELAND (declarationId: String, importerEORI: String, declarantEORI: String) extends Acc14ResponseType
    case class OK_RESPONSE_NO_CONTACT_DETAILS(declarationId: String, importerEORI: String, declarantEORI: String) extends Acc14ResponseType
  }

  def returnAcc14Response(acc14ResponseType: Acc14ResponseType): Acc14Response =
    acc14ResponseType match {
      case Acc14ResponseType.OK_MINIMUM_RESPONSE => getMinimumAcc14Response
      case Acc14ResponseType.OK_PARTIAL_RESPONSE(declarationId) => getPartialAcc14Response(declarationId)
      case Acc14ResponseType.OK_FULL_RESPONSE(declarationId, importerEORI, declarantEORI) => getFullAcc14Response(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES(declarationId, importerEORI, declarantEORI) => getFullAcc14ResponseWithAdditionalTaxCodes(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_WITH_MISMATCH_ON_EORI(declarationId) => getEoriMismatchResponse(declarationId)
      case Acc14ResponseType.OK_FULL_RESPONSE_NORTHERN_IRELAND(declarationId, importerEORI, declarantEORI) => getFullAcc14ResponseWithNorthernIrelandTaxCodes(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS(declarationId, importerEORI, declarantEORI) => getFullAcc14WithoutContactDetails(declarationId,importerEORI,declarantEORI)
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

  def getFullAcc14Response(declarationId: String, importerEORI: String, declarantEORI: String) = Acc14Response(
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
         |				"declarantEORI": "$declarantEORI",
         |				"legalName": "Foxpro Central LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "12 Skybricks Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Info Tech LTD",
         |					"addressLine1": "45 Church Road",
         |					"addressLine3": "Leeds",
         |					"postalCode": "LS1 2HA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"consigneeDetails": {
         |				"consigneeEORI": "$importerEORI",
         |				"legalName": "IT Solutions LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "19 Bricks Road",
         |					"addressLine3": "Newcastle",
         |					"postalCode": "NE12 5BT",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Online Sales LTD",
         |					"addressLine1": "11 Mount Road",
         |					"addressLine3": "London",
         |					"postalCode": "E10 7PP",
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
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A95",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )

  def getFullAcc14ResponseWithAdditionalTaxCodes(declarationId: String, importerEORI: String, declarantEORI: String) = Acc14Response(
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
         |				"declarantEORI": "$declarantEORI",
         |				"legalName": "Foxpro Central LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "12 Skybricks Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Info Tech LTD",
         |					"addressLine1": "45 Church Road",
         |					"addressLine3": "Leeds",
         |					"postalCode": "LS1 2HA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"consigneeDetails": {
         |				"consigneeEORI": "$importerEORI",
         |				"legalName": "IT Solutions LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "19 Bricks Road",
         |					"addressLine3": "Newcastle",
         |					"postalCode": "NE12 5BT",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Online Sales LTD",
         |					"addressLine1": "11 Mount Road",
         |					"addressLine3": "London",
         |					"postalCode": "E10 7PP",
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
         |					"taxType": "A20",
         |					"amount": "218.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A35",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |        {
         |					"taxType": "A95",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |        {
         |					"taxType": "421",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |        {
         |					"taxType": "623",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": ""
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
         |				"declarantEORI": "GB03I52858027018",
         |				"legalName": "Starbucks LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "16 Buckingham Road",
         |					"addressLine3": "London",
         |					"postalCode": "E10 6EA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Core Central LTD",
         |					"addressLine1": "46 Motorway Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"consigneeDetails": {
         |				"consigneeEORI": "GB03I52858027018",
         |				"legalName": "Expertise LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "1 Cross Road",
         |					"addressLine3": "Aberdeen",
         |					"postalCode": "AB10 1AA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Bright Central LTD",
         |					"addressLine1": "101 Skylands Road",
         |					"addressLine3": "Brighton",
         |					"postalCode": "BN1 1BN",
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
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A95",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )


  def getFullAcc14ResponseWithNorthernIrelandTaxCodes(declarationId: String, importerEORI: String, declarantEORI: String) = Acc14Response(
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
         |				"declarantEORI": "$declarantEORI",
         |				"legalName": "Foxpro Central LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "12 Skybricks Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Info Tech LTD",
         |					"addressLine1": "45 Church Road",
         |					"addressLine3": "Leeds",
         |					"postalCode": "LS1 2HA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"consigneeDetails": {
         |				"consigneeEORI": "$importerEORI",
         |				"legalName": "IT Solutions LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "19 Bricks Road",
         |					"addressLine3": "Newcastle",
         |					"postalCode": "NE12 5BT",
         |					"countryCode": "GB"
         |				},
         |				"contactDetails": {
         |					"contactName": "Online Sales LTD",
         |					"addressLine1": "11 Mount Road",
         |					"addressLine3": "London",
         |					"postalCode": "E10 7PP",
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
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "421",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "623",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "99C",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )

  def getFullAcc14WithoutContactDetails(movementReferenceNumber: String, importerEORI: String, declarantEORI: String) = Acc14Response(
    Json.parse(
      s"""
         |{
         |	"overpaymentDeclarationDisplayResponse": {
         |		"responseCommon": {
         |			"status": "OK",
         |			"processingDate": "2021-02-12T11:34:54Z"
         |		},
         |		"responseDetail": {
         |			"declarationId": "$movementReferenceNumber",
         |			"acceptanceDate": "2021-02-12",
         |			"procedureCode": "2",
         |			"declarantDetails": {
         |				"declarantEORI": "$declarantEORI",
         |				"legalName": "Foxpro Central LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "12 Skybricks Road",
         |					"addressLine3": "Coventry",
         |					"postalCode": "CV3 6EA",
         |					"countryCode": "GB"
         |				}
         |			},
         |			"consigneeDetails": {
         |				"consigneeEORI": "$importerEORI",
         |				"legalName": "IT Solutions LTD",
         |				"establishmentAddress": {
         |					"addressLine1": "19 Bricks Road",
         |					"addressLine3": "Newcastle",
         |					"postalCode": "NE12 5BT",
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
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A95",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )


}
