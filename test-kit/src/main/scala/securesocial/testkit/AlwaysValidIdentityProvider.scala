package securesocial.testkit

import play.api.Logger
import securesocial.plugin._
import play.api.mvc.{Result, Request}
import securesocial.plugin.IdentityId

class AlwaysValidIdentityProvider(app:play.api.Application) extends IdentityProvider(app){
  val logger = Logger("securesocial.stubs.AlwaysValidIdentityProvider")
  def authMethod: AuthenticationMethod = AuthenticationMethod("naive")


  override def doAuth()(implicit request: Request[play.api.mvc.AnyContent]): Either[Result, SocialUser] ={
    val userId = request.body.toString
    val r =Right(SocialUserGenerator.socialUserGen(IdentityId(userId, id), authMethod).sample.get)
    r
  }


  def fillProfile(user: SocialUser): SocialUser = {
    user
  }

  def id: String = "naive"
}