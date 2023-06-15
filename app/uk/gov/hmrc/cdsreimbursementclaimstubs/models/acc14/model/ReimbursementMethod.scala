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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.acc14.model

import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.EnumerationFormat

sealed trait ReimbursementMethod extends Product with Serializable {
  val code: String
}

object ReimbursementMethod extends EnumerationFormat[ReimbursementMethod] {

  final case object BankTransfer extends ReimbursementMethod {
    override def toString: String = "Bank Transfer"
    override val code: String = "001"
  }

  final case object Deferment extends ReimbursementMethod {
    override def toString: String = "Deferment"
    override val code: String = "002"
  }

  final case object PayableOrder extends ReimbursementMethod {
    override def toString: String = "Payable Order"
    override val code: String = "003"
  }

  final case object GeneralGuarantee extends ReimbursementMethod {
    override def toString: String = "General Guarantee"
    override val code: String = "004"
  }

  final case object IndividualGuarantee extends ReimbursementMethod {
    override def toString: String = "Individual Guarantee"
    override val code: String = "005"
  }

  final case object Subsidy extends ReimbursementMethod {
    override def toString: String = "Subsidy"
    override val code: String = "006"
  }

  lazy val values: Set[ReimbursementMethod] =
    Set(BankTransfer, Deferment, PayableOrder, GeneralGuarantee, IndividualGuarantee, Subsidy)
}
