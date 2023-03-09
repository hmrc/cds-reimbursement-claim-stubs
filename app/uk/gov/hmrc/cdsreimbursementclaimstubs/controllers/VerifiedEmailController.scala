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

import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.verifiedemail.VerifiedEmailResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue

@Singleton()
class VerifiedEmailController @Inject() (cc: ControllerComponents) extends BackendController(cc) {

  private val successfullResponse =
    VerifiedEmailResponse("someemail@mail.com", "2007-03-20T01:02:03.000Z")

  private val undeliverableMessage = Json.parse("""|{
        |  "subject": "subject-example",
        |  "eventId": "example-id",
        |  "groupId": "example-group-id",
        |  "timestamp": "2021-05-14T10:59:45.811+01:00",
        |  "event": {
        |    "id": "example-id",
        |    "event": "someEvent",
        |    "emailAddress": "email@email.com",
        |    "detected": "2021-05-14T10:59:45.811+01:00",
        |    "code": 12,
        |    "reason": "Inbox full",
        |    "enrolment": "HMRC-CUS-ORG~EORINumber~testEori"
        |  }
        |}""".stripMargin)

  private val undeliverableResponse =
    VerifiedEmailResponse("someemail@mail.com", "2007-03-20T01:02:03.000Z", undeliverable = Some(undeliverableMessage))

  private val serviceUnavailableResponse: JsValue =
    Json.parse("""{ "error": 503, "errorMessage": "Service Unavailable" }""")

  final def getVerifiedEmail(eori: String): Action[AnyContent] =
    Action { _ =>
      eori match {
        case "NOEMAIL" | "EORINOTIMESTAMP" | "GB999999999999999" => NotFound
        case "ETMP503ERROR" => ServiceUnavailable(serviceUnavailableResponse)
        case "GB123456789012" => Ok(Json.toJson(undeliverableResponse))
        case "GB333186811543" | _ => Ok(Json.toJson(successfullResponse))
      }
    }

}
