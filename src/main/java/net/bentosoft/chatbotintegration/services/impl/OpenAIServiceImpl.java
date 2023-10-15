package net.bentosoft.chatbotintegration.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bentosoft.chatbotintegration.services.OpenAIService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private static final URI OPENAI_ENDPOINT = URI.create("https://api.openai.com/v1/chat/completions");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String openaiApiKey;

    @Autowired
    public OpenAIServiceImpl(String openaiApiKey) {
        this.openaiApiKey = openaiApiKey;
    }

    @Override
    public String getResponse(String message) throws Exception {
        HttpPost request = createOpenAIRequest(message);
        String response = executeRequest(request);
        return extractContentFromResponse(response);
    }

    private HttpPost createOpenAIRequest(String message) throws JsonProcessingException {
        HttpPost request = new HttpPost(OPENAI_ENDPOINT);
        request.setEntity(new StringEntity(createRequestBody(message), "UTF-8"));
        setRequestHeaders(request);
        return request;
    }

    private String createRequestBody(String message) throws JsonProcessingException {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> messageMap = new HashMap<>();

        messageMap.put("role", "user");
        messageMap.put("content", message);

        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", Collections.singletonList(messageMap));

        return objectMapper.writeValueAsString(requestBody);
    }

    private void setRequestHeaders(HttpPost request) {
        request.setHeader("Authorization", "Bearer " + openaiApiKey);
        request.setHeader("Content-Type", "application/json");
    }

    private String executeRequest(HttpPost request) throws Exception {
        HttpClient client = HttpClients.createDefault();
        return EntityUtils.toString(client.execute(request).getEntity());
    }

    private String extractContentFromResponse(String response) throws JsonProcessingException {
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.getOrDefault("choices", Collections.emptyList());
        if (choices.isEmpty()) {
            throw new RuntimeException("Invalid response format from OpenAI");
        }
        Map<String, Object> choice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) choice.getOrDefault("message", Collections.emptyMap());
        return (String) message.getOrDefault("content", "");
    }
}
