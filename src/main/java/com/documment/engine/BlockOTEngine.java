package com.documment.engine;

import com.documment.operations.Operation;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation of the OTEngine specifically for block-based document elements.
 */
@Service
public class BlockOTEngine implements OTEngine {

    @Override
    public Operation transform(Operation incoming, List<Operation> history) {
        Operation transformed = incoming;
        
        for (Operation historyOp : history) {
            if (transformed == null) break;
            
            // We transform the incoming operation against each operation in the history
            transformed = transformed.transform(historyOp);
        }
        
        return transformed;
    }
}
