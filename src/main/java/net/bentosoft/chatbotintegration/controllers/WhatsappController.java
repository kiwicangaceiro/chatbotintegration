package net.bentosoft.chatbotintegration.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bentosoft.chatbotintegration.services.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappController {

    private final OpenAIService openAIService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public WhatsappController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping
    public ResponseEntity<String> processWhatsappMessage(@RequestBody String input) {
        try {
            JsonNode rootNode = objectMapper.readTree(input);
            JsonNode messagesNode = rootNode.path("messages");
            if (messagesNode.isMissingNode() || !messagesNode.isArray() || messagesNode.size() == 0) {
                return ResponseEntity.badRequest().body("Invalid message format");
            }
            String message = messagesNode.get(0).path("body").asText();
            return ResponseEntity.ok(openAIService.getResponse(message));
        } catch (Exception e) {
            // Logging the exception would be a good idea here.
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
