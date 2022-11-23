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

import cats._
import cats.implicits._
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Result}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.request.{BarsBusinessAssessRequest, BarsPersonalAssessRequest}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response.ReputationResponse._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response.{BARSResponse, ReputationErrorResponse}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.util.Try

@Singleton()
class BankAccountReputationController @Inject() (cc: ControllerComponents) extends BackendController(cc) with Logging {

  val digitsOnly = "\\d+"

  def personalReputation(): Action[JsValue] = Action(parse.json) { implicit request =>
    logger.debug(s"processing personal request $request ${request.headers} ${request.body}")
    (for {
      assessRequest <-
        Try(request.body.as[BarsPersonalAssessRequest]).toEither.leftMap(error => BadRequest(error.getMessage))
      _             <- Either.cond(
                         isAccountNumberValid(assessRequest.account.accountNumber),
                         (),
                         invalidAccountNumber(assessRequest.account.sortCode)
                       )
      _             <-
        Either
          .cond(isSortCodeValid(assessRequest.account.sortCode), (), invalidSortCode(assessRequest.account.sortCode))
      _             <- specialAccountBehaviour(assessRequest.account.accountNumber)
    } yield Ok(Json.toJson(parseValidAccountNumber(assessRequest.account.accountNumber)))).merge
  }

  def businessReputation(): Action[JsValue] = Action(parse.json) { implicit request =>
    logger.debug(s"processing business request $request  ${request.headers} ${request.body}")
    (for {
      assessRequest <-
        Try(request.body.as[BarsBusinessAssessRequest]).toEither.leftMap(error => BadRequest(error.getMessage))
      _             <- Either.cond(
                         isAccountNumberValid(assessRequest.account.accountNumber),
                         (),
                         invalidAccountNumber(assessRequest.account.sortCode)
                       )
      _             <-
        Either
          .cond(isSortCodeValid(assessRequest.account.sortCode), (), invalidSortCode(assessRequest.account.sortCode))
      _             <- specialAccountBehaviour(assessRequest.account.accountNumber)
    } yield Ok(Json.toJson(parseValidAccountNumber(assessRequest.account.accountNumber)))).merge
  }

  def isSortCodeValid(sortCode: String): Boolean =
    sortCode.length == 6 && sortCode.matches(digitsOnly)

  def isAccountNumberValid(accountNumber: String): Boolean =
    accountNumber.length == 8 && accountNumber.matches(digitsOnly)

  def invalidAccountNumber(acountNumber: String): Result =
    BadRequest(Json.toJson(ReputationErrorResponse("INVALID_ACCOUNT_NUMBER", s"$acountNumber: invalid account number")))

  def invalidSortCode(sortCode: String): Result =
    BadRequest(Json.toJson(ReputationErrorResponse("INVALID_SORTCODE", s"$sortCode: invalid sortcode")))

  def specialAccountBehaviour(accountNumber: String): Either[Result, Unit] = accountNumber match {
    case "90909090" =>
      Left(ServiceUnavailable(Json.toJson(ReputationErrorResponse("SERVICE_UNAVAILABLE", "please try again later"))))
    case "90909091" =>
      Left(BadRequest(Json.toJson(ReputationErrorResponse("BAD_REQUEST", "please check API reference"))))
    case _ => Right(())
  }

  def parseValidAccountNumber(accountNumber: String): BARSResponse =
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
