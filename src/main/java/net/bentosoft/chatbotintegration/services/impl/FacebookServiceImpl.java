package net.bentosoft.chatbotintegration.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bentosoft.chatbotintegration.services.FacebookService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacebookServiceImpl implements FacebookService {

    private static final URI FACEBOOK_ENDPOINT = URI.create("https://graph.facebook.com/v13.0/me/messages");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String facebookPageAccessToken;

    @Autowired
    public FacebookServiceImpl(String facebookPageAccessToken) {
        this.facebookPageAccessToken = facebookPageAccessToken;
    }

    @Override
    public void sendMessage(String recipientId, String message) throws Exception {
        HttpPost request = createFacebookRequest(recipientId, message);
        executeRequest(request);
    }

    private HttpPost createFacebookRequest(String recipientId, String message) throws JsonProcessingException {
        HttpPost request = new HttpPost(FACEBOOK_ENDPOINT + "?access_token=" + facebookPageAccessToken);
        request.setEntity(new StringEntity(createRequestBody(recipientId, message), "UTF-8"));
        setRequestHeaders(request);
        return request;
    }

    private String createRequestBody(String recipientId, String message) throws JsonProcessingException {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> recipientMap = new HashMap<>();
        Map<String, String> messageMap = new HashMap<>();

        recipientMap.put("id", recipientId);
        messageMap.put("text", message);

        requestBody.put("recipient", recipientMap);
        requestBody.put("message", messageMap);

        return objectMapper.writeValueAsString(requestBody);
    }

    private void setRequestHeaders(HttpPost request) {
        request.setHeader("Content-Type", "application/json");
    }

    private void executeRequest(HttpPost request) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            String entityString = EntityUtils.toString(response.getEntity());
            throw new RuntimeException("Failed to send message to Facebook. Status code: " + statusCode + ", Response: " + entityString);
        }
    }
}
