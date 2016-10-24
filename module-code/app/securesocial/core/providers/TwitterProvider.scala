/**
 * Copyright 2012-2014 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
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
package securesocial.plugin.providers

import securesocial.core._
import securesocial.core.services.CacheService
import securesocial.plugin._
import play.api.libs.oauth.{ RequestToken, OAuthCalculator }
import play.api.libs.json.{ JsValue, JsResult, JsSuccess, Reads }
import play.api.libs.ws.WSResponse
import play.api.Logger
import TwitterProvider._
import scala.concurrent.{ ExecutionContext, Future }
import securesocial.plugin.services.{ RoutesService, HttpService }

/**
 * A Twitter Provider
 */
class TwitterProvider(
  routesService: RoutesService,
  cacheService: CacheService,
  client: OAuth1Client[WSResponse]) extends OAuth1Provider(
  routesService,
  cacheService,
  client
) {
  override val id = TwitterProvider.Twitter

  implicit val profileReads = new Reads[BasicProfile] {
    def reads(me: JsValue): JsResult[BasicProfile] = {
      val userId = (me \ Id).as[String]
      val name = (me \ Name).asOpt[String]
      val avatar = (me \ ProfileImage).asOpt[String]
      JsSuccess(BasicProfile(id, userId, None, None, name, None, avatar, authMethod))
    }
  }

  override def fillProfile(info: OAuth1Info): Future[BasicProfile] = {
    client.retrieveProfile[BasicProfile](TwitterProvider.VerifyCredentials, info).map(_.copy(oAuth1Info = Some(info))) recover {
      case e =>
        logger.error("[securesocial] error retrieving profile information from Twitter", e)
        throw new AuthenticationException()
    }
  }
}

object TwitterProvider {
  val VerifyCredentials = "https://api.twitter.com/1.1/account/verify_credentials.json"
  val Twitter = "twitter"
  val Id = "id_str"
  val Name = "name"
  val ProfileImage = "profile_image_url_https"
}
