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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models.tpi05

final case class WafErrorResponse(value: String) extends AnyVal

object WafErrorResponse {

  val FORBIDDEN = WafErrorResponse(
    """
      |<html>
      |    <head>
      |        <meta content="HTML Tidy for Java (vers. 26 Sep 2004), see www.w3.org" name="generator"/>
      |        <title>403 Forbidden</title>
      |    </head>
      |    <body bgcolor="white">
      |        <center>
      |            <h1>403 Forbidden</h1>
      |        </center>
      |        <hr/>
      |        <center>nginx</center>
      |    </body>
      |</html>
      |""".stripMargin
  )

}
