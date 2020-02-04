package id.chataja.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"id.chataja.security","id.chataja.middleware"})
@EnableJpaRepositories("id.chataja.security.jpa")
@EntityScan("id.chataja.security.model")
@EnableScheduling
public class ApiWrapperApplication {

    public static void main(String[] args) {
	SpringApplication.run(ApiWrapperApplication.class, args);
    }

}
