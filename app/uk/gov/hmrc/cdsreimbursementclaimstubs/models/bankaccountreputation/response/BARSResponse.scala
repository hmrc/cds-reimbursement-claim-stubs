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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.bankaccountreputation.response

import play.api.libs.json.{Json, OFormat}

final case class BARSResponse(
  accountNumberWithSortCodeIsValid: ReputationResponse,
  sortCodeIsPresentOnEISCD: ReputationResponse,
  accountExists: Option[ReputationResponse] = None
)

object BARSResponse {

  implicit val personalCompleteResponseFormat: OFormat[BARSResponse] = Json.format[BARSResponse]
}

final case class BARSResponse2(
  accountNumberIsWellFormatted: ReputationResponse,
  sortCodeIsPresentOnEISCD: ReputationResponse,
  accountExists: Option[ReputationResponse] = None,
  accountName: Option[String] = None,
  nameMatches: Option[ReputationResponse] = None
) {
  def isSuccess: Boolean =
    accountNumberIsWellFormatted == ReputationResponse.Yes &&
      accountExists.contains(ReputationResponse.Yes) &&
      nameMatches.contains(ReputationResponse.Yes)
}

object BARSResponse2 {
  implicit val barsResponseFormat: OFormat[BARSResponse2] = Json.format[BARSResponse2]
}
