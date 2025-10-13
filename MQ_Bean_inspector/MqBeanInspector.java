import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MqBeanInspector {

    @Autowired
    private ApplicationContext ctx;

    @PostConstruct
    public void inspectFactories() {
        Map<String, ConnectionFactory> factories = ctx.getBeansOfType(ConnectionFactory.class);
        factories.forEach((name, factory) -> {
            System.out.println("Bean name: " + name + ", class: " + factory.getClass());
            if (factory instanceof CachingConnectionFactory) {
                CachingConnectionFactory cache = (CachingConnectionFactory) factory;
                System.out.println(" -> target: " + cache.getTargetConnectionFactory().getClass());
                System.out.println(" -> session cache size: " + cache.getSessionCacheSize());
            }
        });
    }
}
