package net.bentosoft.chatbotintegration.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class WhatsappControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testWhatsappEndpointValidPost() throws Exception {
        String validRequestBody = "{\"recipientId\":\"1234567890\", \"message\":\"Hello, world!\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/whatsapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testWhatsappEndpointInvalidHttpMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/whatsapp"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testWhatsappEndpointEmptyRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/whatsapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWhatsappEndpointInvalidJsonFormat() throws Exception {
        String invalidRequestBody = "{recipientId:\"1234567890\", \"message\":\"Hello, world!\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/whatsapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

}
