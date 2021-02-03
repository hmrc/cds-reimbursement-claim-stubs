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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.syntax.EitherSyntax
import javax.inject.{Inject, Singleton}
import javax.xml.parsers.SAXParserFactory
import javax.xml.validation.SchemaFactory
import org.xml.sax.ErrorHandler
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.Dec64._
import uk.gov.hmrc.cdsreimbursementclaimstubs.models.Dec64Error
import uk.gov.hmrc.cdsreimbursementclaimstubs.utils.Logging
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.util.Try
import scala.xml.parsing.NoBindingFactoryAdapter
import scala.xml.{NodeSeq, SAXParseException}

@Singleton
class FileUploadController @Inject()(cc:ControllerComponents) extends BackendController(cc) with Logging with EitherSyntax {

  val saxParserFactory: SAXParserFactory = {
    val schemaLang = javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI
    val xsdFile = getClass.getResourceAsStream("/resources/DEC64-v1-0-0-file-upload.xsd")
    val xsdStream = new javax.xml.transform.stream.StreamSource(xsdFile)
    val schema = SchemaFactory.newInstance(schemaLang).newSchema(xsdStream)
    val factory = SAXParserFactory.newInstance()
    factory.setNamespaceAware(true)
    factory.setSchema(schema)
    factory
  }

  val uploadFile:Action[AnyContent] = Action { request =>
    (for {
      _ <- Either.cond(request.method.toUpperCase == "POST", "POST", badRequest("Only Http POST is accepted"))
      _ <- request.headers.get("Accept").map(_.toLowerCase == "application/xml").toRight(badRequest("Incorrect Accept header"))
      _ <- request.headers.get("Content-Type").map(_.toLowerCase == "application/xml; charset=UTF-8").toRight(badRequest("Wrong Content-Type header"))
      _ <- request.headers.get("X-Correlation-ID").toRight(badRequest("No X-Correlation-ID header"))
      _ <- request.headers.get("X-Forwarded-Host").toRight(badRequest("No X-Forwarded-Host header"))
      _ <- request.headers.get("Authorization").toRight(badRequest("No Bearer Token in the Authorization header"))
      _ <- request.headers.get("Date").toRight(badRequest("Missing Date header"))
      soap <- request.body.asXml.toRight(badRequest("Body cannot be parsed as xml"))
      xml <- Right(removeSoapEnvelopeAndBody(soap))
      _ <- validateXML(xml.toString()).toEither.leftMap(errors => badRequest(errors.mkString(", ")))
      declarationId <- extractPropery("DeclarationId",xml)
      _ <- hasProperty("Eori",xml)
      //TODO add other required properties when they are defined
    } yield declarationId )
      .fold(error => {
        logger.warn(s"Returning http status 400: ${error.message}")
        Status(error.status)(error.body)
      },
        declarationId => declarationId match {
          case "64DECInvalidData11" => BadRequest(badRequest("Invalid Data").body)
          case "64DECAuthFailed111" => InternalServerError( error(500,"Authentication Failed").body)
          case _ => NoContent
        }

      )
  }

  def hasProperty(name:String, elem:NodeSeq):Either[Dec64Error,Unit] =
    Either.cond((elem \\ "properties" \ "property" \ "name").map(_.text).contains(name),(),badRequest(s"Missing property: $name"))

  def extractPropery(name:String, elem:NodeSeq):Either[Dec64Error,String] = {
    val property = elem \\ "properties" \ "property"
    val keys = (property \ "name").map(_.text)
    val values = (property \ "value").map(_.text)
    Either.cond(keys.contains(name), values(keys.indexOf(name)), badRequest(s"Missing property: $name"))
  }

  def removeSoapEnvelopeAndBody(ns:NodeSeq):NodeSeq = ns \ "Body" \ "_"

  def validateXML(xml: String): Validated[List[String], scala.xml.Elem] =
    Validated
      .fromTry(Try {
        var saxParserValidationResult: Validated[List[String], Unit] = Valid(())
        val elem = new scala.xml.factory.XMLLoader[scala.xml.Elem] {
          override def parser = saxParserFactory.newSAXParser()
          override def adapter = new NoBindingFactoryAdapter with ErrorHandler {
              override def error(e: SAXParseException): Unit =
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
