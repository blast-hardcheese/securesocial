import com.google.inject.{ TypeLiteral, Scopes, AbstractModule }
import net.codingwell.scalaguice.ScalaModule
import securesocial.plugin.{ BasicProfile, RuntimeEnvironment }
import service.{ MyEnvironment, DemoUser }

class DemoModule extends AbstractModule with ScalaModule {
  override def configure() {
    val environment: MyEnvironment = new MyEnvironment
    bind(new TypeLiteral[RuntimeEnvironment] {}).toInstance(environment)

  }
}
