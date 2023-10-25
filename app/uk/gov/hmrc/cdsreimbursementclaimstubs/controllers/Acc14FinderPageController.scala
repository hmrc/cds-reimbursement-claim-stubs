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

import com.google.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.mvc.Http
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.declaration.request.{DeclarationRequest, OverpaymentDeclarationDisplayRequest, RequestCommon, RequestDetail}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.MockHttpResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14ErrorResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14ErrorResponse.Acc14ErrorResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.Acc14Response.Acc14ResponseType
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ids.MRN
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05.WafErrorResponse
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.FormHelper.movementReferenceNumberForm
import uk.gov.hmrc.cdsreimbursementclaimstubs.views.html.acc14_finder
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Acc14FinderPageController @Inject()(
  http: HttpClient,
  val controllerComponents: MessagesControllerComponents,
  acc14Finder: acc14_finder
)(implicit ec: ExecutionContext) extends FrontendBaseController {

  implicit class FormOps[A](val form: Form[A]) {
    def withDefault(optValue: Option[A]): Form[A] =
      optValue.map(form.fill).getOrElse(form)
  }



  def show(): Action[AnyContent] = Action { implicit request =>
    Ok(acc14Finder(movementReferenceNumberForm, routes.Acc14FinderPageController.submit()))
  }

  def submit(): Action[AnyContent] = Action.async { implicit request => {
    movementReferenceNumberForm.bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest(acc14Finder(formWithErrors, routes.Acc14FinderPageController.submit())))
        }, mrn => {
          val declarationRequest = DeclarationRequest(
            OverpaymentDeclarationDisplayRequest(
              RequestCommon("MDTP", "2023-04-21T07:35:39Z", "9474bdf1df434462b1cd911ddf64a3d5"),
              RequestDetail(mrn.value, None)
            ))
          http
            .POST[DeclarationRequest, HttpResponse](s"http://localhost:7502${routes.DeclarationController.getDeclaration.url}", declarationRequest)
            .map(item => {
              val responseType: Option[String] = MockHttpResponse
                .findMRN(mrn.value)
                .map(_.declarationResponse.response.fold(rightSide => rightSide.fold(
                  getName(_),
                  getName(_)),
                  getName(_)))
              Ok(acc14Finder(
                movementReferenceNumberForm.withDefault(Some(mrn)),
                routes.Acc14FinderPageController.submit(),
                result = Some(Json.prettyPrint(item.json)),
                responseType = responseType
              ))
            })

        })
  }}

  private def getName[T >: Object](obj: T): String = obj.getClass.getSimpleName.replace("$", "")
}
