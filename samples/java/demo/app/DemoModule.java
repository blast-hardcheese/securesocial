import com.google.inject.AbstractModule;
import securesocial.plugin.RuntimeEnvironment;
import service.MyEnvironment;


public class DemoModule extends AbstractModule {
    @Override
    protected void configure() {
        MyEnvironment environment = new MyEnvironment();
        bind(RuntimeEnvironment.class).toInstance(environment);
    }
}
