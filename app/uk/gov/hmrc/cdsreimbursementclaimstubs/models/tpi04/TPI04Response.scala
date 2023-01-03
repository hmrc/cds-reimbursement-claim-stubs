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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi04

import play.api.libs.json.{JsObject, Json, OWrites}
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.TimeUtils

case class TPI04Response(
  caseFound: Boolean,
  caseNumber: Option[String],
  caseStatus: Option[String]
)

object TPI04Response {
  implicit val tpi04ReponseWrites: OWrites[TPI04Response] = new OWrites[TPI04Response] {
    override def writes(o: TPI04Response): JsObject =
      Json.obj(
        "getExistingClaimResponse" -> Json.obj(
          "responseCommon" -> Json
            .obj(
              "status"           -> "OK",
              "processingDate"   -> s"${TimeUtils.iso8061DateTimeNow}",
              "CDFPayService"    -> "SCTY",
              "CDFPayCaseFound"  -> o.caseFound,
              "goods"            -> Json.obj(
                "itemNumber"       -> "0001",
                "goodsDescription" -> "Something expensive"
              ),
              "totalClaimAmount" -> "1000.00"
            )
            .++(
              o.caseNumber.map(caseNumber => Json.obj("CDFPayCaseNumber" -> caseNumber)).getOrElse(Json.obj())
            )
            .++(
              o.caseStatus.map(status => Json.obj("caseStatus" -> status)).getOrElse(Json.obj())
            )
        )
      )
  }
}
