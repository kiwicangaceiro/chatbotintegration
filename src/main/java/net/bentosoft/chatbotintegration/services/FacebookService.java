package net.bentosoft.chatbotintegration.services;

public interface FacebookService {

    void sendMessage(String recipientId, String message) throws Exception;
}
