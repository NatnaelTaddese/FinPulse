package com.finpulse;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PWA(name = "Financial Intelligent platform ", shortName = "FinPulse App")
//@Theme("my-theme")
@Theme("finpulse-default-theme")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().load(); // Load .env
        System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
        System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
        System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
        System.setProperty("SPRING_SECURITY_USER_NAME", dotenv.get("SPRING_SECURITY_USER_NAME"));
        System.setProperty("SPRING_SECURITY_USER_PASSWORD", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));

        System.setProperty("ALIPAY_APP_ID", dotenv.get("ALIPAY_APP_ID"));
        System.setProperty("ALIPAY_PRIVATE_KEY", dotenv.get("ALIPAY_PRIVATE_KEY"));
        System.setProperty("ALIPAY_PUBLIC_KEY", dotenv.get("ALIPAY_PUBLIC_KEY"));
        System.setProperty("APPLICATION_PUBLIC_KEY", dotenv.get("APPLICATION_PUBLIC_KEY"));


        System.setProperty("jna.nosys", "true");

        SpringApplication.run(Application.class, args);

    }
}
