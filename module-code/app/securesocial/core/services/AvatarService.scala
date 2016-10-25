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

import securesocial.adapters.PlayAdapter.PlayTypes

import play.api.libs.ws.{ WSRequest, WSResponse }

import scala.concurrent.{ ExecutionContext, Future }

/**
 * A mockable interface for the avatar service
 */
trait AvatarService {
  def urlFor(userId: String): Future[Option[String]]
}

object AvatarService {

  /**
   * A default implemtation
   * @param httpService
   */
  class Default(httpService: HttpService[PlayTypes])(implicit val executionContext: ExecutionContext) extends AvatarService {
    import _root_.java.security.MessageDigest

    private val logger = play.api.Logger("securesocial.plugin.providers.utils.AvatarService.Default")

    val GravatarUrl = "http://www.gravatar.com/avatar/%s?d=404"
    val Md5 = "MD5"

    override def urlFor(userId: String): Future[Option[String]] = {
      hash(userId).map(hash => {
        val url = GravatarUrl.format(hash)
        httpService.url(url).get().map { response =>
          if (response.status == 200) Some(url) else None
        } recover {
          case e =>
            logger.error("[securesocial] error invoking gravatar", e)
            None
        }
      }) getOrElse {
        Future.successful(None)
      }
    }

    private def hash(email: String): Option[String] = {
      val s = email.trim.toLowerCase
      if (s.length > 0) {
        Some(MessageDigest.getInstance(Md5).digest(s.getBytes).map("%02x".format(_)).mkString)
      } else {
        None
      }
    }
  }
}
