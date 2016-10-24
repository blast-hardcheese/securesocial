package securesocial

import securesocial.core.FrameworkTypes

import play.api.libs.ws.{ WSRequest, WSResponse }

sealed trait PlayTypes extends FrameworkTypes {
  type HttpRequest = WSRequest
  type HttpResponse = WSResponse
}
