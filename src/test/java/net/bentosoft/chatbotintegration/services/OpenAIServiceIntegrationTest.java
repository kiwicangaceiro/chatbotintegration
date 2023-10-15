package net.bentosoft.chatbotintegration.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAIServiceIntegrationTest {

    @Autowired
    private OpenAIService openAIService;

    @Test
    public void testGetResponseFromOpenAI() throws Exception {
        String response = openAIService.getResponse("Hello, Chatbot!");
        assertNotNull(response);
    }
}
