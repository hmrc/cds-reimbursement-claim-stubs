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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models

import scala.collection.mutable

case class ReasonForSecurity(val acc14Code: String, val dec64DisplayString: String)
object ReasonForSecurity {

  val mapping: mutable.LinkedHashMap[String, ReasonForSecurity] = mutable.LinkedHashMap(
    "AccountSales" -> ReasonForSecurity("ACS", "CAP Account Sales"),
    "CommunitySystemsOfDutyRelief" -> ReasonForSecurity("MDC", "Missing Document: CSDR"),
    "EndUseRelief" -> ReasonForSecurity("ENU", "End Use"),
    "InwardProcessingRelief" -> ReasonForSecurity("IPR", "Inward Processing Relief"),
    "ManualOverrideDeposit" -> ReasonForSecurity("MOD", "Manual Override Deposit"),
    "MissingLicenseQuota" -> ReasonForSecurity("MDL", "Missing Document Licence Quota"),
    "MissingPreferenceCertificate" -> ReasonForSecurity("MDP", "Missing Document Preference"),
    "OutwardProcessingRelief" -> ReasonForSecurity("OPR", "Outward Processing Relief"),
    "RevenueDispute" -> ReasonForSecurity("RED", "Revenue Dispute"),
    "TemporaryAdmission2Y" -> ReasonForSecurity("T24", "Temporary Admission (2 years Expiration)"),
    "TemporaryAdmission6M" -> ReasonForSecurity("TA6", "Temporary Admission (6 months Expiration)"),
    "TemporaryAdmission3M" -> ReasonForSecurity("TA3", "Temporary Admission (3 months Expiration)"),
    "TemporaryAdmission2M" -> ReasonForSecurity("TA2", "Temporary Admission (2 months Expiration)"),
    "UKAPEntryPrice" -> ReasonForSecurity("CEP", "CAP Entry Price"),
    "UKAPSafeguardDuties" -> ReasonForSecurity("CSD", "CAP Safeguard Duties"),
    "ProvisionalDuty" -> ReasonForSecurity("PDD", "Provisional Duty"),
    "Quota" -> ReasonForSecurity("CRQ", "Quota"))

  val values: Seq[String] = mapping.values.map(item => item.acc14Code).toSeq



  def ofIndex(index: Int): String =
    values(Math.abs(index) % values.size)

  def of(value: String): String   =
    ofIndex(value.filter(Character.isDigit).toInt)

}
