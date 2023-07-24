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

import com.google.inject.Inject
import com.google.inject.Singleton
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.ReasonForSecurity
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.model.DeclarationHttpResponse
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import uk.gov.hmrc.cdsreimbursementclaimstubs.views.html.generate_mrn
import uk.gov.hmrc.govukfrontend.views.viewmodels.select.Select
import uk.gov.hmrc.govukfrontend.views.viewmodels.select.SelectItem

import scala.collection.immutable.ListMap

@Singleton
class GenerateMrnController @Inject() (
                                    val controllerComponents: MessagesControllerComponents,
                                    generateMrn: generate_mrn
                                  )()
  extends FrontendBaseController {

  def show(): Action[AnyContent] = Action { implicit request =>
    val responseTypeSelect: Select = Select(
      id = "response-type-selector",
      name = "Response Type Selector",
      items = DeclarationHttpResponse.responseTypeMapping.map {
        case (shortForm, className) => SelectItem(
          value = Some(shortForm),
          text = s"${shortForm} - ${className.getSimpleName.replace("$", "")}"
        )
      }.toSeq,
      attributes = Map("onchange" -> "handleChange()")
    )

    val numbers: Seq[Int] = 0 to 99;
    val eoriSelector: Select = Select(
      id = "eori-picker",
      name = "EORI Picker",
      items = numbers.map(num =>
        SelectItem(
          value = Some("%02d".format(num)),
          text = s"${"%02d".format(num)} - ${if (num < 50) "GB" else "XI"}"
        )
      ).toSeq,
      attributes = Map("onchange" -> "handleChange()"),
      formGroupClasses = "select-margin"
    )

    val rfsSelect: Select = Select(
      id = "rfs-selector",
      name = "Reason For Security Selector",
      items = ReasonForSecurity.mapping.map { case (name: String, rfs: ReasonForSecurity) =>
        SelectItem(
          value = Some(rfs.acc14Code),
          text = s"${rfs.dec64DisplayString} - ${rfs.acc14Code}"
        )
      }.toSeq,
      attributes = Map("onchange" -> "handleChange()")
    )




    Ok(generateMrn(responseTypeSelect, eoriSelector, rfsSelect))
  }
}
