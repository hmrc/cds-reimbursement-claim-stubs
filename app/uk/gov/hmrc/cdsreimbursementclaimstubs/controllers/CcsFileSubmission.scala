/*
 * Copyright 2022 HM Revenue & Customs
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

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.syntax.EitherSyntax
import org.xml.sax.ErrorHandler
import play.api.http.MimeTypes
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.Dec64._
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import javax.xml.parsers.SAXParserFactory
import javax.xml.validation.SchemaFactory
import scala.util.Try
import scala.xml.parsing.NoBindingFactoryAdapter
import scala.xml.{NodeSeq, SAXParseException}

@Singleton
class CcsFileSubmission @Inject()(cc: ControllerComponents)
    extends BackendController(cc)
    with Logging
    with EitherSyntax {

  lazy val saxParserFactory: SAXParserFactory = {
    val schemaLang = javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI
    val xsdFile    = getClass.getResourceAsStream("/resources/DEC64-v1-0-0-file-upload.xsd")
//    val xsdStream  = new javax.xml.transform.stream.StreamSource(xsdFile)
//    val schema     = SchemaFactory.newInstance(schemaLang).newSchema(xsdStream)
    val factory    = SAXParserFactory.newInstance()
    factory.setNamespaceAware(true)
//    factory.setSchema(schema)
    factory
  }

  def submitFile: Action[NodeSeq] = Action(parse.xml) { request =>
    val result = for {
      _ <- validateXML(request.body.toString()).toEither.leftMap(errors => badRequest(errors.mkString(", ")))
    } yield ()

    logger.info(s"uploading file")

    result
      .fold(
        error => {
          logger.warn(s"Returning http status 400: ${error.message}")
          Status(error.status)(error.body)
        },
        _ => {
          logger.info(s"successfully uploaded file")
          NoContent.as(MimeTypes.XML)
        }
      )
  }

  def validateXML(xml: String): Validated[List[String], scala.xml.Elem] =
    Validated
      .fromTry(Try {
        var saxParserValidationResult: Validated[List[String], Unit] = Valid(())
        val elem                                                     = new scala.xml.factory.XMLLoader[scala.xml.Elem] {
          override def parser  = saxParserFactory.newSAXParser()
          override def adapter = new NoBindingFactoryAdapter with ErrorHandler {
            override def error(e: SAXParseException): Unit      =
              saxParserValidationResult = saxParserValidationResult.combine(Invalid(List(e.getMessage())))
            override def fatalError(e: SAXParseException): Unit =
              saxParserValidationResult = saxParserValidationResult.combine(Invalid(List(e.getMessage())))
          }
        }.loadString(xml)
        saxParserValidationResult.map(_ => elem)
      })
      .leftMap(e => List(e.getMessage()))
      .andThen(identity)

}
