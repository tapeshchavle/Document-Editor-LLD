# Collaborative Document Editor

A production-grade, real-time collaborative document editor (like Google Docs) built with Spring Boot and WebSockets. This project focuses on high-performance conflict resolution using **Operational Transformation (OT)** and a strictly **SOLID-compliant** extensible architecture.

## 🚀 Key Features

- **Real-time Collaboration**: Multiple users can edit the same document simultaneously.
- **Conflict Resolution**: OT Engine handles concurrent edits without locking the document.
- **Extensible Block Architecture**: Supports `Text` and `Image` blocks by default, with an architecture ready for `Video`, `Tables`, and more.
- **Event Sourcing**: Every operation is versioned and stored, allowing for full document history.

---

## 🏗️ System Architecture & Class Diagram

The system is designed with a high degree of decoupling. The core logic (OT) is separated from the communication (WebSockets) and storage (Snapshots/History).

```mermaid
classDiagram
    class DocumentElement {
        <<interface>>
        +getId() String
        +getType() String
        +copy() DocumentElement
    }
    class TextBlock {
        +String content
        +copy() DocumentElement
    }
    class ImageBlock {
        +String url
        +String caption
        +copy() DocumentElement
    }
    DocumentElement <|.. TextBlock
    DocumentElement <|.. ImageBlock

    class Operation {
        <<interface>>
        +getUserId() String
        +getBaseVersion() long
        +apply(List~DocumentElement~) List~DocumentElement~
        +transform(Operation) Operation
    }
    class AbstractOperation {
        -String userId
        -long baseVersion
    }
    class InsertBlockOperation {
        -int index
        -DocumentElement element
    }
    class DeleteBlockOperation {
        -int index
    }
    Operation <|.. AbstractOperation
    AbstractOperation <|-- InsertBlockOperation
    AbstractOperation <|-- DeleteBlockOperation
    AbstractOperation <|-- NoOpOperation

    class CollaborationService {
        -OperationStore operationStore
        -DocumentSnapshotStore snapshotStore
        -OTEngine otEngine
        -MessageBroadcaster broadcaster
        +handleOperation(String, Operation)
        +getDocumentState(String)
    }

    class OTEngine {
        <<interface>>
        +transform(Operation, List~Operation~) Operation
    }
    class BlockOTEngine {
        +transform(Operation, List~Operation~) Operation
    }
    OTEngine <|.. BlockOTEngine

    CollaborationService --> OTEngine
    CollaborationService --> OperationStore
    CollaborationService --> DocumentSnapshotStore
    CollaborationService --> MessageBroadcaster
```

---

## 🔄 Application Flow (Operational Transformation)

When a client sends an update, the the server ensures that it is "transformed" against any other changes that happened since the client last synced.

```mermaid
sequenceDiagram
    participant UserA as Client A
    participant Srv as Collaboration Service
    participant Hist as Operation Store
    participant Snap as Snapshot Store
    participant All as All Clients

    UserA->>Srv: Sends Insert(p=2, v=10)
    Note over Srv: Lock Document
    Srv->>Hist: Get History since v=10
    Hist-->>Srv: [Concurrent Op v=11]
    Note over Srv: Transform User A's Op against v=11
    Note over Srv: New Op becomes Insert(p=3, v=11)
    Srv->>Snap: Get Current State
    Snap-->>Srv: [Current Doc Elements]
    Srv->>Snap: Apply Transformed Op & Save v=12
    Srv->>Hist: Save Transformed Op
    Note over Srv: Unlock Document
    Srv->>All: Broadcast Transformed Op (User A receives ack)
```

---

## 🛠️ Design Principles (SOLID)

1.  **Single Responsibility Principle (SRP)**:
    - `OTEngine` handles only the math of conflict resolution.
    - `OperationStore` handles only the persistence of history.
    - `CollaborationService` orchestrates the flow without knowing *how* transformations happen.

2.  **Open/Closed Principle (OCP)**:
    - Adding a new content type (e.g., `TableBlock`) requires **zero** changes to the the core `CollaborationService`. You simply implement the `DocumentElement` interface.

3.  **Liskov Substitution Principle (LSP)**:
    - All `Operation` implementations (`Insert`, `Delete`, `NoOp`) can be used interchangeably by the transformation engine.

4.  **Dependency Inversion Principle (DIP)**:
    - `CollaborationService` depends on abstract interfaces (`OperationStore`, `MessageBroadcaster`) rather than concrete implementations (JDBC, WebSocket). This allows switching from In-Memory to Postgres/Kafka with minimal effort.

---

## 🚦 Getting Started

### Prerequisites
- Java 17+
- Maven (or use `./mvnw`)

### Running the Application
```bash
./mvnw clean spring-boot:run
```

The WebSocket endpoint is available at `/ws-document`. Clients can subscribe to titles via `/topic/document/{documentId}`.
