package id.chataja.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"id.chataja.security","id.chataja.middleware"})
@SpringBootApplication
@EnableScheduling
public class ApiWrapperApplication {

    public static void main(String[] args) {
	SpringApplication.run(ApiWrapperApplication.class, args);
    }

}
