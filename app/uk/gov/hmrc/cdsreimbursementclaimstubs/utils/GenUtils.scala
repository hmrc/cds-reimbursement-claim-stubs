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

package uk.gov.hmrc.cdsreimbursementclaimstubs.utils

import org.scalacheck.Gen

import java.util.UUID

object GenUtils {

  def sample[A](g: Gen[A]): A =
    g.sample.getOrElse(sys.error(s"could not generate sample value"))

  def nRandomDigits(n: Int): String =
    List.fill(n)(sample(Gen.numChar)).mkString("")

  def nRandomAlphaNumericChars(n: Int): String =
    List.fill(n)(sample(Gen.alphaNumChar)).mkString("")

  def caseNumber: String =
    s"${nRandomDigits(25)}"

  def correlationId: String = UUID.randomUUID().toString

}
