package com.documment.repository;

import com.documment.domain.DocumentElement;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the DocumentSnapshotStore.
 */
@Repository
public class InMemoryDocumentSnapshotStore implements DocumentSnapshotStore {
    
    // documentId -> List of document elements
    private final Map<String, List<DocumentElement>> snapshots = new ConcurrentHashMap<>();

    @Override
    public List<DocumentElement> getDocumentState(String documentId) {
        return snapshots.getOrDefault(documentId, Collections.synchronizedList(new ArrayList<>()));
    }

    @Override
    public void updateDocumentState(String documentId, List<DocumentElement> newState) {
        snapshots.put(documentId, Collections.synchronizedList(new ArrayList<>(newState)));
    }
}
