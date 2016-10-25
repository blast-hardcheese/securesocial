package securesocial.core

import scala.language.implicitConversions

trait FrameworkTypes {
  type HttpRequest
  type HttpResponse
}

trait Framework[T <: FrameworkTypes] {
  implicit def resp2OAuth2Info(resp: T#HttpResponse): OAuth2Info
}
