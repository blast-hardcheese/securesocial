package securesocial.plugin.authenticator

import securesocial.core._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import helpers.MockHttpService
import play.api.libs.oauth._
import oauth.signpost.exception.OAuthException
import play.api.libs.json.Json
import securesocial.plugin.services.HttpService
import play.api.libs.oauth.ServiceInfo
import play.api.libs.oauth.OAuth
import play.api.libs.oauth.RequestToken
import play.api.libs.oauth.ConsumerKey

class OAuth1ClientSpec extends Specification {

  "IdGenerator" should {
    "correctly encode byte arrays" in {
      val bytes: Array[Byte] = Array(0x01, 0x10, 0xaf, 0xfa).map(_.toByte)
      IdGenerator.toHexString(bytes) === "0110affa"
    }
  }
}
