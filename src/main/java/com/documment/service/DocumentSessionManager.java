package com.documment.service;

import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages active sessions for each document.
 * Tracks which users/connections are currently listening to a document's updates.
 */
@Service
public class DocumentSessionManager {
    
    // documentId -> Set of sessionIds
    private final Map<String, Set<String>> activeSessions = new ConcurrentHashMap<>();

    public void addSession(String documentId, String sessionId) {
        activeSessions.computeIfAbsent(documentId, k -> Collections.synchronizedSet(new HashSet<>()))
                      .add(sessionId);
    }

    public void removeSession(String documentId, String sessionId) {
        Set<String> sessions = activeSessions.get(documentId);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                activeSessions.remove(documentId);
            }
        }
    }

    public Set<String> getSessions(String documentId) {
        return activeSessions.getOrDefault(documentId, Collections.emptySet());
    }
}
