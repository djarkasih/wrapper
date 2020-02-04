package id.chataja.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"id.chataja.security","id.chataja.middleware"})
@SpringBootApplication
public class ApiWrapperApplication {

    public static void main(String[] args) {
	SpringApplication.run(ApiWrapperApplication.class, args);
    }

}
