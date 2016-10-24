package scenarios.helpers

import com.google.inject.Singleton
import securesocial.core.services.UserService
import securesocial.core.BasicProfile
import securesocial.plugin.RuntimeEnvironment

/**
 * Created by dverdone on 8/6/15.
 */

case class TestGlobal() extends RuntimeEnvironment.Default {
  type U = DemoUser
  lazy override val userService: UserService[DemoUser] = null
  override implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext
}
