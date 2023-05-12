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

import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.SchemaValidation
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi01.{PostClearanceCasesResponse, Response, ResponseCommon, TPI01Generation}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Customer
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton()
class TPI01Controller @Inject() (cc: ControllerComponents)
    extends BackendController(cc)
    with SchemaValidation
    with TPI01Generation {

  final val getReimbursementClaims: Action[JsValue] =
    Action(parse.json) { implicit request =>
      validateRequest("tpi01/tpi01-request-schema.json") {
        val eori = (request.body \ "getPostClearanceCasesRequest" \ "requestDetail" \ "EORI").as[String]

        eori match {
          case "GB744638982000" =>
            val detail         = tpi01Claims2(20)
            val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response       = Response(PostClearanceCasesResponse(responseCommon, Some(detail)))
            validateResponse("tpi01/tpi01-response-schema.json", Json.toJson(response))
          case "GB744638982001" =>
            val detail         = tpi01Claims(20)
            val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response       = Response(PostClearanceCasesResponse(responseCommon, Some(detail)))
            validateResponse("tpi01/tpi01-response-schema.json", Json.toJson(response))
          case "GB744638982002" =>
            parseResponse("tpi01/response-200-no-claims-found.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982003" =>
            parseResponse(
              "tpi01/response-link-tpi02-mandatory-missing.json",
              Ok,
              Some("tpi01/tpi01-response-schema.json")
            )
          case "GB744638982004" =>
            parseResponse("tpi01/response-link-tpi02-pattern-error.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982005" =>
            parseResponse("tpi01/response-link-tpi02-system-timeout.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982006" =>
            parseResponse("tpi01/response-200-NDRC.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982007" =>
            parseResponse("tpi01/response-200-SCTY.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982008" =>
            val detail         = tpi01AllSubstatusClaims()
            val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response       = Response(PostClearanceCasesResponse(responseCommon, Some(detail)))
            validateResponse("tpi01/tpi01-response-schema.json", Json.toJson(response))
          case "GB744638982009" =>
            parseResponse("tpi01/response-200-duplicate.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982010" =>
            parseResponse(
              "tpi01/response-200-possible-declaration-ids.json",
              Ok,
              Some("tpi01/tpi01-response-schema.json")
            )
          case "GB744638982011" =>
            parseResponse("tpi01/response-200-duplicate-2.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case "GB744638982012" =>
            val detail         = tpi01Claims(20)
            val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response       = Response(PostClearanceCasesResponse(responseCommon, Some(detail)))
            validateResponse("tpi01/tpi01-response-schema.json", Json.toJson(response))
          case "TPI01MISSING" => parseResponse("tpi01/response-400-mandatory-missing-field.json", BadRequest)
          case "TPI01PATTERN" => parseResponse("tpi01/response-400-pattern-error.json", BadRequest)
          case "TPI01500" => parseResponse("tpi01/response-500-system-timeout.json", InternalServerError)
          case "TPI01EORIERROR" =>
            parseResponse("tpi01/response-200-invalid-eori.json", Ok, Some("tpi01/tpi01-response-schema.json"))
          case e if e.startsWith("GB0000000000") =>
            val caseSubStatusIndex = e.replace("GB0000000000", "")
            val responseCommon     = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response           = Response(
              PostClearanceCasesResponse(responseCommon, Some(tpi01SetCaseSubStatusNDRC(caseSubStatusIndex.toInt)))
            )
            Ok(Json.toJson(response))
          case e if e.startsWith("XI0000000000") =>
            val caseSubStatusIndex = e.replace("XI0000000000", "")
            val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response = Response(
              PostClearanceCasesResponse(responseCommon, Some(tpi01SetCaseSubStatusNDRC(caseSubStatusIndex.toInt, isXiEori = true)))
            )
            Ok(Json.toJson(response))
          case e if e.startsWith("GB1000000000") =>
            val caseSubStatusIndex = e.replace("GB1000000000", "")
            val responseCommon     = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response           = Response(
              PostClearanceCasesResponse(responseCommon, Some(tpi01SetCaseSubStatusSCTY(caseSubStatusIndex.toInt)))
            )
            Ok(Json.toJson(response))
          case e if e.startsWith("XI1000000000") =>
            val caseSubStatusIndex = e.replace("XI1000000000", "")
            val responseCommon = ResponseCommon("OK", "2017-03-21T09:30:47Z")
            val response = Response(
              PostClearanceCasesResponse(responseCommon, Some(tpi01SetCaseSubStatusSCTY(caseSubStatusIndex.toInt, isXiEori = true)))
            )
            Ok(Json.toJson(response))
          case _ if Customer.exists(eori) =>
            Customer.find(eori)
          case _ =>
            parseResponse("tpi01/response-200-no-claims-found.json", Ok, Some("tpi01/tpi01-response-schema.json"))
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
