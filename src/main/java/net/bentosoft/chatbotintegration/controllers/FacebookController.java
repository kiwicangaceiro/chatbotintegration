package net.bentosoft.chatbotintegration.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bentosoft.chatbotintegration.services.FacebookService;
import net.bentosoft.chatbotintegration.services.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facebook")
public class FacebookController {

    private final OpenAIService openAIService;
    private final FacebookService facebookService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public FacebookController(OpenAIService openAIService, FacebookService facebookService) {
        this.openAIService = openAIService;
        this.facebookService = facebookService;
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyWebhook(@RequestParam String hub_challenge) {
        return ResponseEntity.ok(hub_challenge);
    }

    @PostMapping("/process")
    public ResponseEntity<String> processFacebookMessage(@RequestBody String input) {
        try {
            JsonNode rootNode = objectMapper.readTree(input);
            JsonNode messagingArray = rootNode.path("entry").get(0).path("messaging");

            if (messagingArray.isMissingNode() || !messagingArray.isArray() || messagingArray.size() == 0) {
                return ResponseEntity.badRequest().body("Invalid message format");
            }

            JsonNode messaging = messagingArray.get(0);
            String senderId = messaging.path("sender").path("id").asText();
            String text = messaging.path("message").path("text").asText();

            String response = openAIService.getResponse(text);
            facebookService.sendMessage(senderId, response);

            return ResponseEntity.ok("Message processed successfully");
        } catch (Exception e) {
            // Logging the exception would be a good idea here.
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
