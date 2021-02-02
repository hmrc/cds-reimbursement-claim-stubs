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

package uk.gov.hmrc.cdsreimbursementclaimstubs.models

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}

import scala.xml.Elem

final case class Dec64Error(status:Int, message:String, body:Elem)

object Dec64 {

  def badRequest(message:String) = error(400,message)

  def error(statusCode:Int, errorMessage:String):Dec64Error = {
    val dateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z").withZone(ZoneId.systemDefault())

    val body = <cds:errorDetail xmlns:cds="http://www.hmrc.gsi.gov.uk/cds">
      <cds:timestamp>{LocalDateTime.now().format(dateFormat)}</cds:timestamp>
      <cds:correlationId/>
      <cds:errorCode>{statusCode}</cds:errorCode>
      <cds:errorMessage>{errorMessage}</cds:errorMessage>
      <cds:source>a012-ct-proxy-sequence-in step 99</cds:source>
      <cds:sourceFaultDetail>
        <cds:detail>{errorMessage}</cds:detail>
      </cds:sourceFaultDetail>
    </cds:errorDetail>

    Dec64Error(statusCode,errorMessage,body)
  }
}
