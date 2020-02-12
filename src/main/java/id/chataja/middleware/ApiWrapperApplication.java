package id.chataja.middleware;

import id.chataja.middleware.service.RequestInterceptor;
import id.chataja.security.jpa.ClientRepository;
import id.chataja.security.model.Client;
import id.chataja.security.model.Application;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import id.chataja.security.jpa.ApplicationRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"id.chataja.security","id.chataja.middleware"})
@EnableJpaRepositories("id.chataja.security.jpa")
@EntityScan("id.chataja.security.model")
@EnableScheduling
public class ApiWrapperApplication {

    Logger logger = LoggerFactory.getLogger(ApiWrapperApplication.class);
    
    private Client client = null;

    @Autowired
    private Environment env;
    
    @Autowired
    private RequestInterceptor interceptor;
    
    @Autowired
    private ClientRepository clientRepo;
    
    @Autowired
    private ApplicationRepository sdkRepo;
    
    public static void main(String[] args) {
	SpringApplication.run(ApiWrapperApplication.class, args);
    }
    
    @Bean
    public Client getClient() {
        if (client == null) {
            
            String clientName = env.getProperty("wrapper.client.name");
            logger.info("clientName = " + clientName);

            client = clientRepo.findByClientName(clientName);

        }
        
        return client;
    }

    @Bean
    public CommandLineRunner setup() {
        final String subProp = "wrapper.header";
        return args -> {
            
            Map<String,String> headers = new HashMap();
            
            Application sdk = null;
            
            Client cli = getClient();
            if (client != null) {
                logger.info("client = " + client.toString());
                Optional<Application> res = sdkRepo.findById(client.getAppId());
                if (res.isPresent()) {
                    sdk = res.get();
                }
            } else {
                logger.info("client = bull");                
            }
            
            if (sdk != null) {
                headers.put(Application.QISCUS_SDK_APP_ID, sdk.getQiscusSdkAppID());
                headers.put(Application.QISCUS_SDK_SECRET, sdk.getQiscusSdkSecret());
            }
            
            logger.info("Override headers : " + headers.toString());
            
            if (headers.size() > 0) {
                interceptor.addHeaders(headers);
            }
            
        };
    }
    
}
