package com.documment.controller;

import com.documment.domain.DocumentElement;
import com.documment.operations.Operation;
import com.documment.service.CollaborationService;
import com.documment.service.DocumentSessionManager;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Handles incoming WebSocket messages for document collaboration.
 */
@Controller
public class DocumentController {

    private final CollaborationService collaborationService;
    private final DocumentSessionManager sessionManager;

    public DocumentController(CollaborationService collaborationService, 
                              DocumentSessionManager sessionManager) {
        this.collaborationService = collaborationService;
        this.sessionManager = sessionManager;
    }

    /**
     * Receives an operation from a client via /app/document/{documentId}/edit.
     */
    @MessageMapping("/document/{documentId}/edit")
    public void processOperation(@DestinationVariable String documentId, 
                                 @Payload Operation operation,
                                 SimpMessageHeaderAccessor headerAccessor) {
        // Orchestrate the collaborative update
        collaborationService.handleOperation(documentId, operation);
    }

    /**
     * Handles user joining a document session.
     */
    @MessageMapping("/document/{documentId}/join")
    public List<DocumentElement> joinDocument(@DestinationVariable String documentId, 
                                              SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        sessionManager.addSession(documentId, sessionId);
        
        // Return the current state to the joining user
        return collaborationService.getDocumentState(documentId);
    }
}
