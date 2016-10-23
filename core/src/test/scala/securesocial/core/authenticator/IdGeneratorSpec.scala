package securesocial.core.authenticator

import org.specs2.mutable.Specification

class OAuth1ClientSpec extends Specification {

  "IdGenerator" should {
    "correctly encode byte arrays" in {
      val bytes: Array[Byte] = Array(0x01, 0x10, 0xaf, 0xfa).map(_.toByte)
      IdGenerator.toHexString(bytes) === "0110affa"
    }
  }
}
