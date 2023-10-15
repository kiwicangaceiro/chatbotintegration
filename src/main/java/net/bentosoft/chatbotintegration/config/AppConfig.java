package net.bentosoft.chatbotintegration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${openai.apikey}")
    private String openaiApiKey;

    @Value("${facebook.page.access.token}")
    private String facebookPageAccessToken;

    @Bean
    public String openaiApiKey() {
        return openaiApiKey;
    }

    @Bean
    public String facebookPageAccessToken() {
        return facebookPageAccessToken;
    }
}
