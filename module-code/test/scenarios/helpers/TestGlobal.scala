package scenarios.helpers

import com.google.inject.Singleton
import securesocial.plugin.services.UserService
import securesocial.plugin.{ BasicProfile, RuntimeEnvironment }

/**
 * Created by dverdone on 8/6/15.
 */

case class TestGlobal() extends RuntimeEnvironment.Default {
  type U = DemoUser
  lazy override val userService: UserService[DemoUser] = null

}
