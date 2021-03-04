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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.request.BarsPersonalAssessRequest
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response.{BARSResponse, ReputationErrorResponse, ReputationResponse}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response.ReputationResponse._
import cats.implicits._

import scala.util.Try

@Singleton()
class BankAccountReputationController @Inject()(cc: ControllerComponents) extends BackendController(cc) with Logging {

  def personalReputation(): Action[JsValue] = Action(parse.json) { implicit request =>
    (for {
      assessRequest <- Try(request.body.as[BarsPersonalAssessRequest]).toEither.leftMap(error => InternalServerError(error.getMessage))
      _ <- Either.cond(
        assessRequest.account.accountNumber.length == 8, (), BadRequest(Json.toJson(ReputationErrorResponse("INVALID_ACCOUNT_NUMBER", s"${assessRequest.account.accountNumber}: invalid account number"))))
      _ <- Either.cond(assessRequest.account.sortCode.length == 6, (), BadRequest(Json.toJson(ReputationErrorResponse("INVALID_SORTCODE", s"${assessRequest.account.sortCode}: invalid sortcode"))))
    } yield Ok(Json.toJson(parseValidAccountNumber(assessRequest.account.accountNumber)))).merge
  }

  def businessReputation(): Action[JsValue] = Action(parse.json) { implicit request =>
    (for {
      assessRequest <- Try(request.body.as[BarsPersonalAssessRequest]).toEither.leftMap(error => InternalServerError(error.getMessage))
      _ <- Either.cond(assessRequest.account.accountNumber.length == 8, (), BadRequest(Json.toJson(ReputationErrorResponse("INVALID_ACCOUNT_NUMBER", s"${assessRequest.account.accountNumber}: invalid account number"))))
      _ <- Either.cond(assessRequest.account.sortCode.length == 6, (), BadRequest(Json.toJson(ReputationErrorResponse("INVALID_SORTCODE", s"${assessRequest.account.sortCode}: invalid sortcode"))))
    } yield Ok(Json.toJson(parseValidAccountNumber(assessRequest.account.accountNumber)))).merge
  }

  def parseValidAccountNumber(accountNumber: String): BARSResponse = {
    accountNumber match {
      case "11001001" => BARSResponse(Yes, Yes, Some(Yes))
      case "11001002" => BARSResponse(Yes, Yes, Some(Indeterminate))
      case "11001003" => BARSResponse(Yes, Yes, Some(Error))
      case "11001004" => BARSResponse(Yes, Yes, Some(No))

      case "11002001" => BARSResponse(Indeterminate, Yes, Some(Yes))
      case "11002002" => BARSResponse(Indeterminate, Yes, Some(Indeterminate))
      case "11002003" => BARSResponse(Indeterminate, Yes, Some(Error))
      case "11002004" => BARSResponse(Indeterminate, Yes, Some(No))

      case "11003001" => BARSResponse(Error, Yes, Some(Yes))
      case "11003002" => BARSResponse(Error, Yes, Some(Indeterminate))
      case "11003003" => BARSResponse(Error, Yes, Some(Error))
      case "11003004" => BARSResponse(Error, Yes, Some(No))

      case "11004004" => BARSResponse(No, Yes, Some(No))

      case "11004009" => BARSResponse(No, Yes, None)

      case _ => BARSResponse(Yes, Yes, Some(Yes))
    }
  }


}

