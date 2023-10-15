package net.bentosoft.chatbotintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.bentosoft.chatbotintegration"})
public class ChatbotintegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotintegrationApplication.class, args);
    }

}
