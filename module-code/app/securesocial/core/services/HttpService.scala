/**
 * Copyright 2013-2014 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
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
 *
 */
package securesocial.plugin.services

import securesocial.core.FrameworkTypes

import scala.concurrent.ExecutionContext

/**
 * A mockable interface for the http client
 */
trait HttpService[Types <: FrameworkTypes] {
  def url(url: String): Types#HttpRequest
}

object HttpService {

  /**
   * A default implementation for HttpService based on the Play WS client.
   */
  import play.api.libs.ws.{ WSRequest, WSResponse }
  import securesocial.adapters.PlayAdapter.PlayTypes
  class Default extends HttpService[PlayTypes] {
    import play.api.Play.current
    import play.api.libs.ws.WS

    def url(url: String): WSRequest = WS.url(url)
  }
}
