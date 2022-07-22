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
    case class OK_FULL_RESPONSE(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_OTHER_DUTIES_1(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_OTHER_DUTIES_2(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_OTHER_DUTIES_3(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_OTHER_DUTIES_VAT(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_VAT(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_WITH_MISMATCH_ON_EORI(declarationId: String) extends Acc14ResponseType
    case class OK_FULL_RESPONSE_NORTHERN_IRELAND(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_RESPONSE_NO_CONTACT_DETAILS(declarationId: String, importerEORI: String, declarantEORI: String)
        extends Acc14ResponseType
    case class OK_FULL_RESPONSE_SECURITIES(
      declarationId: String,
      reasonForSecurity: String,
      importerEORI: String,
      declarantEORI: String
    ) extends Acc14ResponseType
    case class OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES_SECURITIES(
      declarationId: String,
      reasonForSecurity: String,
      importerEORI: String,
      declarantEORI: String
    ) extends Acc14ResponseType
  }

  def returnAcc14Response(acc14ResponseType: Acc14ResponseType): Acc14Response =
    acc14ResponseType match {
      case Acc14ResponseType.OK_MINIMUM_RESPONSE => getMinimumAcc14Response
      case Acc14ResponseType.OK_PARTIAL_RESPONSE(declarationId) => getPartialAcc14Response(declarationId)
      case Acc14ResponseType.OK_FULL_RESPONSE(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14Response(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_1(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseOtherDuties1(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_2(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseOtherDuties2(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_3(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseOtherDuties3(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_OTHER_DUTIES_VAT(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseOtherDutiesAndVatDuties(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_VAT(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseVatDuties(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseWithAdditionalTaxCodes(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_WITH_MISMATCH_ON_EORI(declarationId) => getEoriMismatchResponse(declarationId)
      case Acc14ResponseType.OK_FULL_RESPONSE_NORTHERN_IRELAND(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14ResponseWithNorthernIrelandTaxCodes(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_RESPONSE_NO_CONTACT_DETAILS(declarationId, importerEORI, declarantEORI) =>
        getFullAcc14WithoutContactDetails(declarationId, importerEORI, declarantEORI)
      case Acc14ResponseType
            .OK_FULL_RESPONSE_SECURITIES(declarationId, reasonForSecurity, importerEORI, declarantEORI) =>
        getFullAcc14ResponseWithReasonForSecurity(declarationId, reasonForSecurity, importerEORI, declarantEORI)
      case Acc14ResponseType.OK_FULL_RESPONSE_ADDITIONAL_TAX_CODES_SECURITIES(
            declarationId,
            reasonForSecurity,
            importerEORI,
            declarantEORI
          ) =>
        getFullAcc14ResponseWithAdditionalTaxCodesAndReasonForSecurity(
          declarationId,
          reasonForSecurity,
          importerEORI,
          declarantEORI
        )
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

  def getFullAcc14ResponseWithReasonForSecurity(
    declarationId: String,
    reasonForSecurity: String,
    importerEORI: String,
    declarantEORI: String
  ) = Acc14Response(
    Json.parse(s"""
        |{
        |    "overpaymentDeclarationDisplayResponse":
        |    {
        |        "responseCommon":
        |        {
        |            "status": "OK",
        |            "processingDate": "2001-12-17T09:30:47Z"
        |        },
        |        "responseDetail":
        |        {
        |            "declarationId": "$declarationId",
        |            "acceptanceDate": "2019-08-13",
        |            "declarantReferenceNumber": "XFGLKJDSE5GDPOIJEW985T",
        |            "securityReason": "$reasonForSecurity",
        |            "btaDueDate": "2019-09-13",
        |            "procedureCode": "71",
        |            "btaSource": "DMS",
        |            "declarantDetails":
        |            {
        |                "declarantEORI": "$declarantEORI",
        |                "legalName": "Fred Bloggs and Co Ltd",
        |                "establishmentAddress":
        |                {
        |                    "addressLine1": "10 Rillington Place",
        |                    "addressLine2": "London",
        |                    "addressLine3": "Pimlico",
        |                    "postalCode": "W11 1RH",
        |                    "countryCode": "GB"
        |                },
        |                "contactDetails":
        |                {
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
        |            "consigneeDetails":
        |            {
        |                "consigneeEORI": "$importerEORI",
        |                "legalName": "Swift Goods Ltd",
        |                "establishmentAddress":
        |                {
        |                    "addressLine1": "14 Briar Lane",
        |                    "addressLine2": "London",
        |                    "addressLine3": "Pimlico",
        |                    "countryCode": "GB"
        |                },
        |                "contactDetails":
        |                {
        |                    "contactName": "Frank Sidebotham",
        |                    "addressLine1": "J P Jones Insolvency Ltd",
        |                    "addressLine2": "14 Briar Lane",
        |                    "addressLine3": "Pimlico",
        |                    "postalCode": "W11 1QT",
        |                    "countryCode": "GB",
        |                    "telephone": "0207 678 3243",
        |                    "emailAddress": "enquiries@swftgoods.com"
        |                }
        |            },
        |            "accountDetails":
        |            [
        |                {
        |                    "accountType": "001",
        |                    "accountNumber": "8901112",
        |                    "eori": "8432569",
        |                    "legalName": "Fred Bloggs and Co Ltd",
        |                    "contactDetails":
        |                    {
        |                        "contactName": "Angela Smith",
        |                        "addressLine1": "J P Jones Insolvency Ltd",
        |                        "addressLine2": "14 Briar Lane",
        |                        "addressLine3": "Holborn",
        |                        "addressLine4": "London",
        |                        "countryCode": "GB",
        |                        "telephone": "0270 112 3476",
        |                        "emailAddress": "fred@bloggs.com"
        |                    }
        |                },
        |                {
        |                    "accountType": "002",
        |                    "accountNumber": "8901113",
        |                    "eori": "8432563",
        |                    "legalName": "Fred Bloggs and Co Ltd",
        |                    "contactDetails":
        |                    {
        |                        "contactName": "Angela Smith",
        |                        "addressLine1": "J P Jones Insolvency Ltd",
        |                        "addressLine2": "14 Briar Lane",
        |                        "addressLine3": "London",
        |                        "countryCode": "GB",
        |                        "telephone": "0270 112 3476",
        |                        "emailAddress": "fred@bloggs.com"
        |                    }
        |                }
        |            ],
        |            "bankDetails":
        |            {
        |                "consigneeBankDetails":
        |                {
        |                    "accountHolderName": "Swift Goods Ltd",
        |                    "sortCode": "125841",
        |                    "accountNumber": "01478523"
        |                },
        |                "declarantBankDetails":
        |                {
        |                    "accountHolderName": "Fred Bloggs and Co Ltd",
        |                    "sortCode": "653214",
        |                    "accountNumber": "54789632"
        |                }
        |            },
        |            "securityDetails":
        |            [
        |                {
        |                    "securityDepositId": "ABC0123456",
        |                    "totalAmount": "14585.52",
        |                    "amountPaid": "14585.52",
        |                    "paymentMethod": "001",
        |                    "paymentReference": "SGL SECURITY 001",
        |                    "taxDetails":
        |                    [
        |                        {
        |                            "taxType": "A00",
        |                            "amount": "6000.00"
        |                        },
        |                        {
        |                            "taxType": "584",
        |                            "amount": "8085.52"
        |                        }
        |                    ]
        |                },
        |                {
        |                    "securityDepositId": "DEF6543210",
        |                    "totalAmount": "500.00",
        |                    "amountPaid": "300.00",
        |                    "paymentMethod": "002",
        |                    "paymentReference": "SGL SECURITY 002",
        |                    "taxDetails":
        |                    [
        |                        {
        |                            "taxType": "A00",
        |                            "amount": "100.00"
        |                        },
        |                        {
        |                            "taxType": "B00",
        |                            "amount": "200.00"
        |                        }
        |                    ]
        |                }
        |            ]
        |        }
        |    }
        |}
        |""".stripMargin)
  )

  def getFullAcc14ResponseWithAdditionalTaxCodesAndReasonForSecurity(
    declarationId: String,
    reasonForSecurity: String,
    importerEORI: String,
    declarantEORI: String
  ) = Acc14Response(
    Json.parse(
      s"""
         |{
         |    "overpaymentDeclarationDisplayResponse":
         |    {
         |        "responseCommon":
         |        {
         |            "status": "OK",
         |            "processingDate": "2001-12-17T09:30:47Z"
         |        },
         |        "responseDetail":
         |        {
         |            "declarationId": "$declarationId",
         |            "acceptanceDate": "2019-08-13",
         |            "declarantReferenceNumber": "XFGLKJDSE5GDPOIJEW985T",
         |            "securityReason": "$reasonForSecurity",
         |            "btaDueDate": "2019-09-13",
         |            "procedureCode": "71",
         |            "btaSource": "DMS",
         |            "declarantDetails":
         |            {
         |                "declarantEORI": "$declarantEORI",
         |                "legalName": "Fred Bloggs and Co Ltd",
         |                "establishmentAddress":
         |                {
         |                    "addressLine1": "10 Rillington Place",
         |                    "addressLine2": "London",
         |                    "addressLine3": "Pimlico",
         |                    "postalCode": "W11 1RH",
         |                    "countryCode": "GB"
         |                },
         |                "contactDetails":
         |                {
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
         |            "consigneeDetails":
         |            {
         |                "consigneeEORI": "$importerEORI",
         |                "legalName": "Swift Goods Ltd",
         |                "establishmentAddress":
         |                {
         |                    "addressLine1": "14 Briar Lane",
         |                    "addressLine2": "London",
         |                    "addressLine3": "Pimlico",
         |                    "countryCode": "GB"
         |                },
         |                "contactDetails":
         |                {
         |                    "contactName": "Frank Sidebotham",
         |                    "addressLine1": "J P Jones Insolvency Ltd",
         |                    "addressLine2": "14 Briar Lane",
         |                    "addressLine3": "Pimlico",
         |                    "postalCode": "W11 1QT",
         |                    "countryCode": "GB",
         |                    "telephone": "0207 678 3243",
         |                    "emailAddress": "enquiries@swftgoods.com"
         |                }
         |            },
         |            "accountDetails":
         |            [
         |                {
         |                    "accountType": "001",
         |                    "accountNumber": "8901112",
         |                    "eori": "8432569",
         |                    "legalName": "Fred Bloggs and Co Ltd",
         |                    "contactDetails":
         |                    {
         |                        "contactName": "Angela Smith",
         |                        "addressLine1": "J P Jones Insolvency Ltd",
         |                        "addressLine2": "14 Briar Lane",
         |                        "addressLine3": "Holborn",
         |                        "addressLine4": "London",
         |                        "countryCode": "GB",
         |                        "telephone": "0270 112 3476",
         |                        "emailAddress": "fred@bloggs.com"
         |                    }
         |                },
         |                {
         |                    "accountType": "002",
         |                    "accountNumber": "8901113",
         |                    "eori": "8432563",
         |                    "legalName": "Fred Bloggs and Co Ltd",
         |                    "contactDetails":
         |                    {
         |                        "contactName": "Angela Smith",
         |                        "addressLine1": "J P Jones Insolvency Ltd",
         |                        "addressLine2": "14 Briar Lane",
         |                        "addressLine3": "London",
         |                        "countryCode": "GB",
         |                        "telephone": "0270 112 3476",
         |                        "emailAddress": "fred@bloggs.com"
         |                    }
         |                }
         |            ],
         |            "bankDetails":
         |            {
         |                "consigneeBankDetails":
         |                {
         |                    "accountHolderName": "Swift Goods Ltd",
         |                    "sortCode": "125841",
         |                    "accountNumber": "01478523"
         |                },
         |                "declarantBankDetails":
         |                {
         |                    "accountHolderName": "Fred Bloggs and Co Ltd",
         |                    "sortCode": "653214",
         |                    "accountNumber": "54789632"
         |                }
         |            },
         |            "securityDetails":
         |            [
         |                {
         |                    "securityDepositId": "ABC0123456",
         |                    "totalAmount": "14585.52",
         |                    "amountPaid": "14585.52",
         |                    "paymentMethod": "001",
         |                    "paymentReference": "SGL SECURITY 001",
         |                    "taxDetails":
         |                    [
         |                        {
         |                            "taxType": "A20",
         |                            "amount": "6000.00"
         |                        },
         |                        {
         |                            "taxType": "A35",
         |                            "amount": "8085.52"
         |                        },
         |                        {
         |                            "taxType": "A90",
         |                            "amount": "5000.00"
         |                        },
         |                        {
         |                            "taxType": "A85",
         |                            "amount": "14126.28"
         |                        },
         |                        {
         |                            "taxType": "A95",
         |                            "amount": "7224.12"
         |                        },
         |                        {
         |                            "taxType": "421",
         |                            "amount": "4818.11"
         |                        },
         |                        {
         |                            "taxType": "623",
         |                            "amount": "9805.00"
         |                        }
         |                    ]
         |                },
         |                {
         |                    "securityDepositId": "DEF6543210",
         |                    "totalAmount": "500.00",
         |                    "amountPaid": "300.00",
         |                    "paymentMethod": "002",
         |                    "paymentReference": "SGL SECURITY 002",
         |                    "taxDetails":
         |                    [
         |                        {
         |                            "taxType": "A20",
         |                            "amount": "100.00"
         |                        },
         |                        {
         |                            "taxType": "A35",
         |                            "amount": "110.00"
         |                        },
         |                        {
         |                            "taxType": "A90",
         |                            "amount": "130.00"
         |                        },
         |                        {
         |                            "taxType": "A85",
         |                            "amount": "170.00"
         |                        },
         |                        {
         |                            "taxType": "A95",
         |                            "amount": "100.00"
         |                        },
         |                        {
         |                            "taxType": "421",
         |                            "amount": "300.00"
         |                        },
         |                        {
         |                            "taxType": "623",
         |                            "amount": "275.00"
         |                        }
         |                    ]
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
         |          "accountDetails":
         |          [
         |              {
         |                  "accountType": "a",
         |                  "accountNumber": "1",
         |                  "eori": "$importerEORI",
         |                  "legalName": "a",
         |                  "contactDetails":
         |                  {
         |                      "contactName": "a",
         |                      "addressLine1": "a",
         |                      "addressLine2": "a",
         |                      "addressLine3": "a",
         |                      "addressLine4": "a",
         |                      "postalCode": "a",
         |                      "countryCode": "GB",
         |                      "telephone": "a",
         |                      "emailAddress": "a"
         |                  }
         |              },
         |              {
         |                  "accountType": "b",
         |                  "accountNumber": "2",
         |                  "eori": "$importerEORI",
         |                  "legalName": "b",
         |                  "contactDetails":
         |                  {
         |                      "contactName": "b",
         |                      "addressLine1": "b",
         |                      "addressLine2": "b",
         |                      "addressLine3": "b",
         |                      "addressLine4": "b",
         |                      "postalCode": "b",
         |                      "countryCode": "GB",
         |                      "telephone": "b",
         |                      "emailAddress": "b"
         |                  }
         |              }
         |          ],
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
         |					"taxType": "A95",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )

  def getFullAcc14ResponseOtherDuties1(declarationId: String, importerEORI: String, declarantEORI: String) =
    Acc14Response(
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
         |					"taxType": "591",
         |					"amount": "10.10",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "487",
         |					"amount": "20.20",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "431",
         |					"amount": "30.30",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "433",
         |					"amount": "40.40",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "435",
         |					"amount": "50.50",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "446",
         |					"amount": "60.60",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "463",
         |					"amount": "70.70",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
      )
    )

  def getFullAcc14ResponseOtherDuties2(declarationId: String, importerEORI: String, declarantEORI: String) =
    Acc14Response(
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
         |					"taxType": "A50",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A70",
         |					"amount": "20.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "A80",
         |					"amount": "30.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "40.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "415",
         |					"amount": "50.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "419",
         |					"amount": "60.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "421",
         |					"amount": "70.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "422",
         |					"amount": "80.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "435",
         |					"amount": "90.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "473",
         |					"amount": "100.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "438",
         |					"amount": "110.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "451",
         |					"amount": "120.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "431",
         |					"amount": "130.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "487",
         |					"amount": "140.00",
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

  def getFullAcc14ResponseOtherDuties3(declarationId: String, importerEORI: String, declarantEORI: String) =
    Acc14Response(
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
         |					"taxType": "511",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "561",
         |					"amount": "20.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "589",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "595",
         |					"amount": "20.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "591",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "592",
         |					"amount": "20.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "99A",
         |					"amount": "10000000.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "99B",
         |					"amount": "20000000.00",
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

  def getFullAcc14ResponseOtherDutiesAndVatDuties(declarationId: String, importerEORI: String, declarantEORI: String) =
    Acc14Response(
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
         |					"taxType": "B00",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "B05",
         |					"amount": "20.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "589",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "595",
         |					"amount": "20.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "0"
         |				},
         |				{
         |					"taxType": "591",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "592",
         |					"amount": "20.00",
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

  def getFullAcc14ResponseVatDuties(declarationId: String, importerEORI: String, declarantEORI: String) = Acc14Response(
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
         |					"taxType": "B00",
         |					"amount": "10.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |           "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "B05",
         |					"amount": "20.00",
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

  def getFullAcc14ResponseWithAdditionalTaxCodes(declarationId: String, importerEORI: String, declarantEORI: String) =
    Acc14Response(
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
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A35",
         |					"amount": "211.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
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
         |          "cmaEligible": "1"
         |				},
         |        {
         |					"taxType": "A95",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
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
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )

  def getFullAcc14ResponseWithNorthernIrelandTaxCodes(
    declarationId: String,
    importerEORI: String,
    declarantEORI: String
  ) = Acc14Response(
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
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "623",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "99C",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
    )
  )

  def getFullAcc14WithoutContactDetails(movementReferenceNumber: String, importerEORI: String, declarantEORI: String) =
    Acc14Response(
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
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A90",
         |					"amount": "228.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				},
         |				{
         |					"taxType": "A85",
         |					"amount": "171.00",
         |					"paymentMethod": "001",
         |					"paymentReference": "GB201430007000",
         |          "cmaEligible": "1"
         |				}
         |			]
         |		}
         |	}
         |}
         |""".stripMargin
      )
    )

}
