package id.chataja.middleware;

import id.chataja.middleware.service.RequestInterceptor;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"id.chataja.security","id.chataja.middleware"})
@EnableJpaRepositories("id.chataja.security.jpa")
@EntityScan("id.chataja.security.model")
@EnableScheduling
public class ApiWrapperApplication {

    Logger logger = LoggerFactory.getLogger(ApiWrapperApplication.class);

    @Autowired
    private Environment env;
    
    @Autowired
    private RequestInterceptor interceptor;
    
    public static void main(String[] args) {
	SpringApplication.run(ApiWrapperApplication.class, args);
    }

    @Bean
    public CommandLineRunner setup() {
        final String subProp = "wrapper.header";
        return args -> {
            
            Map<String,String> headers = new HashMap();
            
            String[] keys = env.getProperty("wrapper.header.overrides", String[].class);
            logger.info("Override header names : " + keys);
            
            String keyVar;
            for (String key : keys) {
                keyVar = subProp + "." + key.toLowerCase();
                headers.put(key, env.getProperty(keyVar));
            }
            //logger.info("Override headers : " + headers.toString());
            
            interceptor.addHeaders(headers);
            
        };
    }
    
}
