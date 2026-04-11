package com.documment.repository;

import com.documment.operations.Operation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the OperationStore.
 * Good for development and testing.
 */
@Repository
public class InMemoryOperationStore implements OperationStore {
    
    // documentId -> List of operations in applied order
    private final Map<String, List<Operation>> historyMap = new ConcurrentHashMap<>();

    @Override
    public void save(String documentId, Operation operation) {
        historyMap.computeIfAbsent(documentId, k -> Collections.synchronizedList(new ArrayList<>()))
                  .add(operation);
    }

    @Override
    public List<Operation> getHistory(String documentId, long sinceVersion) {
        List<Operation> fullHistory = historyMap.getOrDefault(documentId, Collections.emptyList());
        
        synchronized (fullHistory) {
            if (sinceVersion >= fullHistory.size()) {
                return Collections.emptyList();
            }
            return new ArrayList<>(fullHistory.subList((int) sinceVersion, fullHistory.size()));
        }
    }

    @Override
    public long getLatestVersion(String documentId) {
        return historyMap.getOrDefault(documentId, Collections.emptyList()).size();
    }
}
