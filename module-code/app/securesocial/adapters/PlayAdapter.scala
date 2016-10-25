package securesocial.adapters

import scala.language.implicitConversions

import securesocial.core.{ OAuth2Info, Framework, FrameworkTypes }

import play.api.libs.json._
import play.api.libs.ws.{ WSRequest, WSResponse }
import securesocial.plugin.OAuth2Constants

object PlayAdapter {
  sealed trait PlayTypes extends FrameworkTypes {
    type HttpRequest = WSRequest
    type HttpResponse = WSResponse
  }

  implicit object PlayFramework extends Framework[PlayTypes] {
    implicit def resp2OAuth2Info(response: PlayTypes#HttpResponse): OAuth2Info = {
      val json = response.json
      // logger.debug("[securesocial] got json back [" + json + "]")
      OAuth2Info(
        (json \ OAuth2Constants.AccessToken).as[String],
        (json \ OAuth2Constants.TokenType).asOpt[String],
        (json \ OAuth2Constants.ExpiresIn).asOpt[Int],
        (json \ OAuth2Constants.RefreshToken).asOpt[String]
      )
    }
  }
}
