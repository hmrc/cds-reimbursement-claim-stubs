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

import cats.syntax.EitherSyntax
import com.eclipsesource.schema.drafts.Version4
import com.eclipsesource.schema.drafts.Version4._
import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import play.twirl.api.Html
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.DeclarationErrorResponse._
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.DeclarationInfoResponses._
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.DeclarationInfoHelpers._
import scala.io.Source

@Singleton
class DeclarationInfoRequestController @Inject()(cc: ControllerComponents) extends BackendController(cc) with Logging with EitherSyntax{
  lazy val schemaToBeValidated: SchemaType = {
    Json
      .fromJson[SchemaType](
        Json.parse(
          Source
            .fromInputStream(
              this.getClass.getResourceAsStream("/resources/ACC14-v1-3-0-overpayment-display-request-schema.json")
            )
            .mkString
        )
      )
      .get
  }


  val getDeclaration: Action[JsValue] = Action(parse.json) { implicit request =>
    val validator = SchemaValidator(Some(Version4))

    (for {
      _ <- Either.cond(request.method.toUpperCase == "POST", "POST", WrongMethod)
      _ <- request.headers.get("Accept").map(_.toLowerCase == "application/json").toRight(WrongAcceptHeader)
      _ <- request.headers.get("Content-Type").map(_.toLowerCase == "application/json").toRight(WrongContentTypeHeader)
      _ <- request.headers.get("X-Correlation-ID").toRight(NoCorrelationIdHeader)
      _ <- request.headers.get("X-Forwarded-Host").toRight(NoXForwaredHostHeader)
      _ <- request.headers.get("Authorization").toRight(NoAuthorizationHeader)
      _ <- validator.validate(schemaToBeValidated, request.body).asEither.leftMap(err => makeEis(err.toString(), BAD_REQUEST))
      decId <- (request.body \ "overpaymentDeclarationDisplayRequest" \ "requestDetail" \ "declarationId")
        .toEither.map(_.as[String]).leftMap(_ => NoDeclarationId)
      validDecId <- Either.cond(isMRNValid(decId), decId, DeclarationIdInvalid)
    } yield validDecId
      ).fold(
      error => {
        logger.warn(s"Returning http status ${error.errorDetail.errorCode}: ${error}")
        toErrorResponse(error)
      },
      declarationId => declarationId match {
        case "57WAFResponse11111" => Forbidden(Html(WafError.value))
        case "57TimeoutResponse1" => toErrorResponse(Timeout)
        case "57CouldNotProcess1" => toErrorResponse(CouldNotBeProcessed)
        case "57NoDeclarationF11" => toErrorResponse(NoDeclarationFound)
        case "57NoSecurityDepos1" => toErrorResponse(NoSecurityDeposits)
        case "57EmptyResponse111" => toOkResponse(emptyResponse)
        case "57MinimumResponse1" => toOkResponse(minimumResponse(declarationId))
        case _ => toOkResponse(fullResponse(declarationId))
      }
    )

  }

}
