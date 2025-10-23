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

package uk.gov.hmrc.cdsreimbursementclaimstubs.controllers

import play.api.Configuration
import play.api.libs.Files
import play.api.libs.json.*
import play.api.libs.ws.{WSClient, writeableOf_String}
import play.api.mvc.*
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.Files.TemporaryFile

@Singleton()
class UpscanController @Inject() (cc: ControllerComponents, wsClient: WSClient, configuration: Configuration)(implicit
  ec: ExecutionContext
) extends BackendController(cc) {

  val upscanInitiateRequestCache = new ConcurrentHashMap[String, UpscanController.UpscanInitiateRequest]()
  val fileUploadsCache           = new ConcurrentHashMap[String, TemporaryFile]()

  val baseUrl = configuration.get[String]("upscanStubHostUrl")

  final val prepareUpload: Action[AnyContent] =
    Action { request =>
      request.body.asJson
        .map(_.as[UpscanController.UpscanInitiateRequest])
        .match {
          case None => BadRequest
          case Some(upscanInitiateRequest) =>
            val upscanReference = UUID.randomUUID().toString()
            upscanInitiateRequestCache.put(upscanReference, upscanInitiateRequest)

            Ok(Json.parse(s"""{
    "reference": "$upscanReference",
    "uploadRequest": {
        "href": "$baseUrl${routes.UpscanController.upload.url}",
        "fields": {
            "acl": "private",
            "key": "$upscanReference",
            "x-amz-date": "${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}Z",
            "x-amz-algorithm": "AWS4-HMAC-SHA256",
            "x-amz-credential": "ASIAxxxxxxxxx/20180202/eu-west-2/s3/aws4_request",
            "x-amz-meta-callback-url": "${upscanInitiateRequest.callbackUrl}",
            "success_action_redirect": "${upscanInitiateRequest.successRedirect}",
            "error_action_redirect": "${upscanInitiateRequest.errorRedirect}",
            "x-amz-meta-upscan-initiate-response": "dummy",
            "x-amz-meta-upscan-initiate-received": "dummy",
            "x-amz-meta-request-id": "${UUID.randomUUID().toString()}",
            "x-amz-signature": "xyz",
            "x-amz-meta-session-id": "${UUID.randomUUID().toString()}",
            "policy": "xyz",
            "x-amz-meta-original-filename": "foo.bar"
        }
    }
}"""))
        }
    }

  val upload: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { request =>
    val body = request.body
    body.dataParts
      .get("key")
      .map { upscanReferences =>
        upscanReferences.headOption.match {
          case None => BadRequest
          case Some(upscanReference) =>
            body
              .file("file")
              .map { file =>
                Option(upscanInitiateRequestCache.get(upscanReference)).match {
                  case None => BadRequest
                  case Some(upscanInitiateRequest) =>
                    fileUploadsCache.put(upscanReference, file.ref)
                    Future {
                      Thread.sleep(1000)
                      wsClient
                        .url(upscanInitiateRequest.callbackUrl)
                        .withMethod("POST")
                        .withHttpHeaders(
                          "Content-Type" -> "application/json"
                        )
                        .withBody(
                          Json.prettyPrint(
                            Json.obj(
                              "reference"     -> JsString(upscanReference),
                              "fileStatus"    -> JsString("READY"),
                              "downloadUrl"   -> JsString(
                                baseUrl + routes.UpscanController.download(upscanReference).url
                              ),
                              "uploadDetails" -> Json.obj(
                                "uploadTimestamp" -> JsString(
                                  LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z"
                                ),
                                "checksum"        -> JsString(SHA256(file.transformRefToBytes().toByteBuffer.array())),
                                "fileName"        -> JsString(file.filename),
                                "fileMimeType"    -> JsString(file.contentType.getOrElse("application/octet-stream")),
                                "size"            -> JsNumber(file.fileSize)
                              )
                            )
                          )
                        )
                        .execute()
                    }
                    Redirect(
                      Call(
                        "GET",
                        upscanInitiateRequest.successRedirect + s"?key=$upscanReference&bucket=dummy"
                      )
                    )
                }
              }
              .getOrElse(
                Option(upscanInitiateRequestCache.get(upscanReference)).match {
                  case None => BadRequest
                  case Some(upscanInitiateRequest) =>
                    Redirect(Call("GET", upscanInitiateRequest.errorRedirect + s"?key=$upscanReference"))
                }
              )
        }
      }
      .getOrElse(BadRequest)
  }

  final def download(upscanReference: String): Action[AnyContent] =
    Action { _ =>
      Option(fileUploadsCache.get(upscanReference)).match {
        case None => BadRequest
        case Some(fileRef) =>
          Ok(java.nio.file.Files.readAllBytes(fileRef.path))
      }
    }

}

object UpscanController {

  case class UpscanInitiateRequest(
    callbackUrl: String,
    successRedirect: String,
    errorRedirect: String,
    minimumFileSize: Option[Int] = None,
    maximumFileSize: Option[Int] = None,
    consumingService: Option[String] = None
  )

  object UpscanInitiateRequest {
    implicit val format: Format[UpscanInitiateRequest] = Json.format[UpscanInitiateRequest]
  }
}

import java.security.MessageDigest

object SHA256 {

  def apply(bytes: Array[Byte]): String =
    MessageDigest
      .getInstance("SHA-256")
      .digest(bytes)
      .map("%02x".format(_))
      .mkString
}
