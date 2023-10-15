package net.bentosoft.chatbotintegration.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FacebookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFacebookEndpointValidPost() throws Exception {
        String validRequestBody = "{\"recipientId\":\"1234567890\", \"message\":\"Hello, world!\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/facebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testFacebookEndpointInvalidHttpMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/facebook"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testFacebookEndpointEmptyRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/facebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFacebookEndpointInvalidJsonFormat() throws Exception {
        String invalidRequestBody = "{recipientId:\"1234567890\", \"message\":\"Hello, world!\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/facebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

}
