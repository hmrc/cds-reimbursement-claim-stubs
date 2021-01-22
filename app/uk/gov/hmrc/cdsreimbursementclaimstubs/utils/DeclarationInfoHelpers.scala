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

package uk.gov.hmrc.cdsreimbursementclaimstubs.utils

object DeclarationInfoHelpers {
  def isMRNValid(in:String):Boolean = {
    val regex = """\d{2}[a-zA-Z]{2}\w{13}\d""".r
    in match {
      case regex(_*) => true
      case _ => false
    }
  }
}

object DeclarationInfoResponses {

  val emptyResponse =
    s"""{
       |  "overpaymentDeclarationDisplayResponse": {
       |    "responseCommon": {
       |      "status": "OK",
       |      "processingDate": "9447-42-84T35:41:48Z"
       |    }
       |  }
       |}""".stripMargin

  def minimumResponse(declarationId: String) =
  s"""{
     |  "overpaymentDeclarationDisplayResponse": {
     |    "responseCommon": {
     |      "status": "OK",
     |      "processingDate": "7709-60-22T91:30:62Z"
     |    },
     |    "responseDetail": {
     |      "declarationId": "$declarationId",
     |      "acceptanceDate": "2019-08-13",
     |      "procedureCode": "98",
     |      "declarantDetails": {
     |        "declarantEORI": "GB3745678934000",
     |        "legalName": "Nerbenzor Akroma",
     |        "establishmentAddress": {
     |          "addressLine1": "123 Erboro Street",
     |          "countryCode": "FI"
     |        }
     |      }
     |    }
     |  }
     |}""".stripMargin

  def fullResponse(declarationId: String) =
    s"""{
       |   "overpaymentDeclarationDisplayResponse":{
       |      "responseCommon":{
       |         "status":"OK",
       |         "processingDate":"2001-12-17T09:30:47Z"
       |      },
       |      "responseDetail":{
       |         "declarationId":"$declarationId",
       |         "acceptanceDate":"2019-08-13",
       |         "declarantReferenceNumber":"XFGLKJDSE5GDPOIJEW985T",
       |         "securityReason":"IPR",
       |         "btaDueDate":"2019-09-13",
       |         "procedureCode":"71",
       |         "btaSource":"DMS",
       |         "declarantDetails":{
       |            "declarantEORI":"GB3745678934000",
       |            "legalName":"Fred Bloggs and Co Ltd",
       |            "establishmentAddress":{
       |               "addressLine1":"10 Rillington Place",
       |               "addressLine2":"London",
       |               "addressLine3":"Pimlico",
       |               "postalCode":"W11 1RH",
       |               "countryCode":"GB"
       |            },
       |            "contactDetails":{
       |               "contactName":"Angela Smith",
       |               "addressLine1":"J P Jones Insolvency Ltd",
       |               "addressLine2":"14 Briar Lane",
       |               "addressLine3":"Pimlico",
       |               "postalCode":"W11 1QT",
       |               "countryCode":"GB",
       |               "telephone":"0270 112 3476",
       |               "emailAddress":"fred@bloggs.com"
       |            }
       |         },
       |         "consigneeDetails":{
       |            "consigneeEORI":"GB562485153000",
       |            "legalName":"Swift Goods Ltd",
       |            "establishmentAddress":{
       |               "addressLine1":"14 Briar Lane",
       |               "addressLine2":"London",
       |               "addressLine3":"Pimlico",
       |               "countryCode":"GB"
       |            },
       |            "contactDetails":{
       |               "contactName":"Frank Sidebotham",
       |               "addressLine1":"J P Jones Insolvency Ltd",
       |               "addressLine2":"14 Briar Lane",
       |               "addressLine3":"Pimlico",
       |               "postalCode":"W11 1QT",
       |               "countryCode":"GB",
       |               "telephone":"0207 678 3243",
       |               "emailAddress":"enquiries@swftgoods.com"
       |            }
       |         },
       |         "accountDetails":[
       |            {
       |               "accountType":"001",
       |               "accountNumber":"8901112",
       |               "eori":"8432569",
       |               "legalName":"Fred Bloggs and Co Ltd",
       |               "contactDetails":{
       |                  "contactName":"Angela Smith",
       |                  "addressLine1":"J P Jones Insolvency Ltd",
       |                  "addressLine2":"14 Briar Lane",
       |                  "addressLine3":"Holborn",
       |                  "addressLine4":"London",
       |                  "countryCode":"GB",
       |                  "telephone":"0270 112 3476",
       |                  "emailAddress":"fred@bloggs.com"
       |               }
       |            },
       |            {
       |               "accountType":"002",
       |               "accountNumber":"8901113",
       |               "eori":"8432563",
       |               "legalName":"Fred Bloggs and Co Ltd",
       |               "contactDetails":{
       |                  "contactName":"Angela Smith",
       |                  "addressLine1":"J P Jones Insolvency Ltd",
       |                  "addressLine2":"14 Briar Lane",
       |                  "addressLine3":"London",
       |                  "countryCode":"GB",
       |                  "telephone":"0270 112 3476",
       |                  "emailAddress":"fred@bloggs.com"
       |               }
       |            }
       |         ],
       |         "bankDetails":{
       |            "consigneeBankDetails":{
       |               "accountHolderName":"Swift Goods Ltd",
       |               "sortCode":"125841",
       |               "accountNumber":"01478523"
       |            },
       |            "declarantBankDetails":{
       |               "accountHolderName":"Fred Bloggs and Co Ltd",
       |               "sortCode":"653214",
       |               "accountNumber":"54789632"
       |            }
       |         },
       |         "securityDetails":[
       |            {
       |               "securityDepositId":"ABC0123456",
       |               "totalAmount":"14585.52",
       |               "amountPaid":"14585.52",
       |               "paymentMethod":"001",
       |               "paymentReference":"SGL SECURITY 001",
       |               "taxDetails":[
       |                  {
       |                     "taxType":"A00",
       |                     "amount":"6000.00"
       |                  },
       |                  {
       |                     "taxType":"584",
       |                     "amount":"8085.52"
       |                  }
       |               ]
       |            },
       |            {
       |               "securityDepositId":"DEF6543210",
       |               "totalAmount":"500.00",
       |               "amountPaid":"300.00",
       |               "paymentMethod":"002",
       |               "paymentReference":"SGL SECURITY 002",
       |               "taxDetails":[
       |                  {
       |                     "taxType":"B00",
       |                     "amount":"100.00"
       |                  },
       |                  {
       |                     "taxType":"478",
       |                     "amount":"200.00"
       |                  }
       |               ]
       |            }
       |         ]
       |      }
       |   }
       |}""".stripMargin

}
