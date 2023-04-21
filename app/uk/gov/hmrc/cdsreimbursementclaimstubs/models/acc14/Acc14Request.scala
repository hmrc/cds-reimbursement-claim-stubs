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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Acc14Request(requestedDeclarationId: String, securityReason: Option[String]) {

  val declarationId = requestedDeclarationId
    .replace("XIDC", "AAAA")
    .replace("XICD", "AAAA")
    .replace("XID", "AAA")
    .replace("XIC", "AAA")

  def shouldReturnDeclarantXiEori: Boolean =
    requestedDeclarationId.contains("XID") ||
      requestedDeclarationId.contains("XIDC") ||
      requestedDeclarationId.contains("XICD")

  def shouldReturnConsigneeXiEori: Boolean =
    requestedDeclarationId.contains("XIC") ||
      requestedDeclarationId.contains("XIDC") ||
      requestedDeclarationId.contains("XICD")
}

object Acc14Request {
  implicit val acc14RequestReads: Reads[Acc14Request] = (
    (JsPath \ "declarationId").read[String] and
      (JsPath \ "securityReason").readNullable[String]
  )(Acc14Request.apply _)
}
