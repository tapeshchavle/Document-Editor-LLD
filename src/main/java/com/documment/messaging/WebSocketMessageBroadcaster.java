package com.documment.messaging;

import com.documment.operations.Operation;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Implementation of MessageBroadcaster using Spring's SimpMessagingTemplate (WebSockets).
 */
@Component
public class WebSocketMessageBroadcaster implements MessageBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMessageBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcast(String documentId, Operation operation) {
        // Send the transformed operation to all subscribers of the document topic
        String destination = "/topic/document/" + documentId;
        messagingTemplate.convertAndSend(destination, operation);
    }
}
