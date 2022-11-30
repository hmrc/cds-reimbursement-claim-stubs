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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import play.api.libs.json.JsValue
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.mvc._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi01.TPI01Generation
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi02.TPI02Generation
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.{NDRC, SCTY, SchemaValidation}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton()
class TPI02Controller @Inject() (cc: ControllerComponents)
    extends BackendController(cc)
    with SchemaValidation
    with TPI01Generation
    with TPI02Generation {

  final val getSpecificClaim: Action[JsValue] =
    Action(parse.json) { implicit request =>
      validateRequest("tpi02/tpi02-request-schema.json") {
        val cdfPayCaseNumber =
          (request.body \ "getSpecificCaseRequest" \ "requestDetail" \ "CDFPayCaseNumber").as[String]
        cdfPayCaseNumber match {
          case "4374422407" =>
            parseResponse("tpi02/response-200-no-claims-found.json", Ok, Some("tpi02/tpi02-response-schema.json"))
          case "4374422406" => parseResponse("tpi02/response-400-mandatory-missing-field.json", BadRequest)
          case "4374422405" => parseResponse("tpi02/response-400-pattern-error.json", BadRequest)
          case "4374422404" => parseResponse("tpi02/response-500-system-timeout.json", InternalServerError)
          case e if e.startsWith("NDRC-200") =>
            tpi01AllSubstatusClaims().CDFPayCase.NDRCCases.find(_.CDFPayCaseNumber == e) match {
              case Some(value) =>
                println(value)
                val extractedIndex = e.replace("NDRC-200", "").toInt
                val claimType = if (extractedIndex % 2 == 0) "C285" else "C&E1179"
                tpi02Claim(
                  claimType,
                  NDRC,
                  e,
                  value.caseStatus,
                  value.closedDate.isDefined,
                  multiple = true,
                  entryNumber = false
                )
              case None =>
                parseResponse("tpi02/response-200-no-claims-found-ndrc.json", Ok, Some("tpi02/tpi02-response-schema.json"))
            }
          case e if e.startsWith("SCTY-200") =>
            tpi01AllSubstatusClaims().CDFPayCase.SCTYCases.find(_.CDFPayCaseNumber == e) match {
              case Some(value) =>
                tpi02Claim(
                  "",
                  SCTY,
                  e,
                  value.caseStatus,
                  value.closedDate.isDefined,
                  multiple = false,
                  entryNumber = true
                )
              case None =>
                parseResponse("tpi02/response-200-no-claims-found-scty.json", Ok, Some("tpi02/tpi02-response-schema.json"))
            }
          case e if e.startsWith("NDRC-100") =>
            val extractedIndex = e.replace("NDRC-100", "")
            tpi01SetCaseSubStatusNDRC(extractedIndex.toInt).CDFPayCase.NDRCCases
              .find(_.CDFPayCaseNumber == e) match {
              case Some(value) =>
                val claimType = if (extractedIndex.toInt % 2 == 0) "C285" else "C&E1179"
                tpi02Claim(
                  claimType,
                  NDRC,
                  e,
                  value.caseStatus,
                  value.closedDate.isDefined,
                  multiple = true,
                  entryNumber = false
                )
              case None =>
                parseResponse("tpi02/response-200-no-claims-found-ndrc.json", Ok, Some("tpi02/tpi02-response-schema.json"))
            }
          case e if e.startsWith("SCTY-100") =>
            val extractedIndex = e.replace("SCTY-100", "")
            tpi01SetCaseSubStatusSCTY(extractedIndex.toInt).CDFPayCase.SCTYCases
              .find(_.CDFPayCaseNumber == e) match {
              case Some(value) =>
                tpi02Claim(
                  "SCTY",
                  SCTY,
                  e,
                  value.caseStatus,
                  value.closedDate.isDefined,
                  multiple = true,
                  entryNumber = false
                )
              case None =>
                parseResponse("tpi02/response-200-no-claims-found-scty.json", Ok, Some("tpi02/tpi02-response-schema.json"))
            }
          case e if e.startsWith("NDRC") =>
            tpi01Claims(20).CDFPayCase.NDRCCases.find(_.CDFPayCaseNumber == e) match {
              case Some(value) =>
                val extractedIndex = e.replace("NDRC-", "").toInt
                val claimType      = if (extractedIndex % 2 == 0) "C285" else "C&E1179"
                tpi02Claim(
                  claimType,
                  NDRC,
                  e,
                  value.caseStatus,
                  value.closedDate.isDefined,
                  multiple = true,
                  entryNumber = false
                )
              case None =>
                parseResponse("tpi02/response-200-no-claims-found-ndrc.json", Ok, Some("tpi02/tpi02-response-schema.json"))
            }
          case e if e.startsWith("SCTY") =>
            tpi01Claims(20).CDFPayCase.SCTYCases.find(_.CDFPayCaseNumber == e) match {
              case Some(value) =>
                tpi02Claim(
                  "",
                  SCTY,
                  e,
                  value.caseStatus,
                  value.closedDate.isDefined,
                  multiple = false,
                  entryNumber = true
                )
              case None =>
                parseResponse("tpi02/response-200-no-claims-found-scty.json", Ok, Some("tpi02/tpi02-response-schema.json"))
            }
          case _ => parseResponse("tpi02/response-200-no-claims-found.json", Ok, Some("tpi02/tpi02-response-schema.json"))
        }
      }
    }

  private def parseResponse(filename: String, status: Status, schema: Option[String] = None): Result = {
    val response = jsonDataFromFile(filename)
    status match {
      case Ok =>
        validateResponse(schema.get, response)
        status(response)
      case _ => status(response)
    }
  }
}
