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

import play.api.mvc._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.SchemaValidation
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi01.SUB09Generation
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton()
class SUB09Controller @Inject() (cc: ControllerComponents)
    extends BackendController(cc)
    with SchemaValidation
    with SUB09Generation {

  private val eoriRegex = "^[A-Z]{2}[0-9A-Z]+$"

  private def shouldHaveXiEori(eoriGB: String): Boolean =
    eoriGB.takeRight(2).toInt % 2 == 0

  final def getSubscription(EORI: String, regime: String, acknowledgementReference: String): Action[AnyContent] =
    Action { _ =>
      if (EORI == "GB0144638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-200-001.json",
          Ok,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB0244638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-200-002.json",
          Ok,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB0344638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-200-003.json",
          Ok,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB0444638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-200-004.json",
          Ok,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB0544638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-200-005.json",
          Ok,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB3744638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-200-037.json",
          Ok,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB4044638982000")
        parseResponse(
          "sub09/businessErrorExample-InvalidData-400.json",
          BadRequest,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB4144638982000")
        parseResponse(
          "sub09/businessErrorExample-SubscriptionDisplay-ResponseNotReturnedFromBackend.json",
          NotFound,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB4244638982000")
        parseResponse(
          "sub09/companyInformationErrorResponse.json",
          BadRequest,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI == "GB5044638982000")
        parseResponse(
          "sub09/systemErrorExample-Timeout.json",
          InternalServerError,
          Some("sub09/sub09-response-schema.json")
        )
      else if (EORI.matches(eoriRegex) && regime == "CDS" && acknowledgementReference.length() == 32)
        validateResponse(
          "sub09/sub09-response-schema.json",
          if (shouldHaveXiEori(EORI))
            getSubscriptionWithXiEori(EORI, EORI.replace("GB", "XI"))
          else
            getSubscriptionWithoutXiEori(EORI)
        )
      else
        Results.BadRequest

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
