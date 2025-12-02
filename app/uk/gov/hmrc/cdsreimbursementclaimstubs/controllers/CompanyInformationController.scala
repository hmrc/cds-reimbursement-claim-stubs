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

import play.api.libs.json.{JsValue, Json}
import play.api.mvc.*
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.companyinformation.CompanyInformationResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.companyinformation.AddressInformation
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton()
class CompanyInformationController @Inject() (cc: ControllerComponents) extends BackendController(cc) {

  private val successfullResponse =
    CompanyInformationResponse(
      name = "Tony Stark",
      consent = "1",
      address = AddressInformation(
        streetAndNumber = "86 Mysore Road",
        city = "London",
        postalCode = Some("SW11 5RZ"),
        countryCode = "GB"
      )
    )

  private val noEmailResponse =
    CompanyInformationResponse(
      name = "NOEMAIL",
      consent = "0",
      address = AddressInformation(
        streetAndNumber = "My Company",
        city = "Shipley",
        postalCode = Some("BD18 3ER"),
        countryCode = "GB"
      )
    )

  private val serviceUnavailableResponse =
    Json.parse("""{ "error": 503, "errorMessage": "Service Unavailable" }""")

  final def getCompanyInformation(eori: String): Action[AnyContent] =
    Action { _ =>
      eori match {
        case "GB999999999999990" => NotFound
        case "NOEMAIL" => Ok(Json.toJson(noEmailResponse))
        case "ETMP503ERROR" => ServiceUnavailable(serviceUnavailableResponse)
        case "GB333186811543" => Ok(Json.toJson(successfullResponse.copy(consent = "0", name = "ABC Ltd")))
        case "EORINOTIMESTAMP" | _ => Ok(Json.toJson(successfullResponse))
      }

    }


  def thirdPartyCompaniInformation: Action[JsValue] = Action(parse.json) { request =>
    val requestBody = request.body
    (requestBody \ "eori").asOpt[String] match {
      case Some(eori) =>
        eori match {
          case "GB999999999999990" => NotFound
          case "NOEMAIL" => Ok(Json.toJson(noEmailResponse))
          case "ETMP503ERROR" => ServiceUnavailable(serviceUnavailableResponse)
          case "GB333186811543" => Ok(Json.toJson(successfullResponse.copy(consent = "0", name = "ABC Ltd")))
          case "EORINOTIMESTAMP" | _ => Ok(Json.toJson(successfullResponse))
        }
      case None =>
        BadRequest("Missing 'eori' field in JSON Body")
    }
  }
  
  
}
