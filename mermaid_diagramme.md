# 📊 Mermaid Diagramme - Slot Machine Casino

## Klassendiagramm

```mermaid
classDiagram
    class Player {
        -Long id
        -String name
        -int coins
        +getName() String
        +setName(String) void
        +getCoins() int
        +setCoins(int) void
    }
    
    class PlayerController {
        -PlayerRepository repository
        +getAllPlayers() List~Player~
        +getPlayerById(Long) Player
        +createPlayer(Player) Player
        +deletePlayer(Long) void
        +rechargeCoins(Long, Map) Player
    }
    
    class SlotController {
        -PlayerRepository repository
        -String[] symbols
        +pullSlot(Long, Map) Map
        -getRandomSymbol() String
        -calculateWin(String, String, String) int
    }
    
    class PlayerRepository {
        <<interface>>
        +findById(Long) Optional~Player~
        +save(Player) Player
        +deleteById(Long) void
        +findAll() List~Player~
    }
    
    PlayerController --> PlayerRepository : uses
    SlotController --> PlayerRepository : uses
    PlayerRepository --> Player : manages
```

---

## Application Flow Diagramm

```mermaid
flowchart TD
    A[START] --> B[Player Selection Screen]
    B --> C{Neuen Player erstellen?}
    C -->|JA| D[Player Form]
    D --> E[Player erstellt]
    E --> F[Existierenden Player wählen]
    C -->|NEIN| F
    F --> G[Slot Machine Screen]
    G --> H{Genügend Coins?}
    H -->|NEIN| I[Coins aufladen]
    I --> G
    H -->|JA| J[Slot Pull - 10 Coins]
    J --> K[Animation - 3 Sekunden]
    K --> L[Ergebnis anzeigen]
    L --> M{Gewinn?}
    M -->|JA| N[Coins hinzufügen]
    M -->|NEIN| O{Weiterspielen?}
    N --> O
    O -->|JA| G
    O -->|NEIN| P[Player wechseln]
    P --> B
```

---

## API Sequenz Diagramm

```mermaid
sequenceDiagram
    participant F as Frontend (React)
    participant PC as PlayerController
    participant SC as SlotController
    participant PR as PlayerRepository
    participant DB as MySQL Database
    
    F->>PC: POST /api/players {name, coins}
    PC->>PR: save(player)
    PR->>DB: INSERT INTO players...
    DB-->>PR: Player with ID
    PR-->>PC: Saved Player
    PC-->>F: JSON Response {id, name, coins}
    
    F->>SC: POST /api/slot/pull/1 {bet: 10}
    SC->>PR: findById(1)
    PR->>DB: SELECT * FROM players WHERE id=1
    DB-->>PR: Player object
    PR-->>SC: Player with coins
    SC->>SC: Game Logic (symbols, win calculation)
    SC->>PR: save(updatedPlayer)
    PR->>DB: UPDATE players SET coins=...
    DB-->>PR: Updated Player
    PR-->>SC: Saved Player
    SC-->>F: JSON {symbols, won, newCoins}
```

---

## Slot Machine Game Logic

```mermaid
flowchart TD
    A[Slot Pull Start] --> B[Validate Player & Bet]
    B --> C{Player exists & enough coins?}
    C -->|NEIN| D[Throw Exception]
    C -->|JA| E[Deduct 10 Coins]
    E --> F[Generate 3 Random Symbols]
    F --> G{All 3 symbols same?}
    G -->|JA| H[Calculate Win Multiplier]
    H --> I[💎💎💎 = Bet × 50]
    H --> J[⭐⭐⭐ = Bet × 20]
    H --> K[🔔🔔🔔 = Bet × 15]
    H --> L[🍉🍉🍉 = Bet × 10]
    H --> M[🍋🍋🍋 = Bet × 7]
    H --> N[🍒🍒🍒 = Bet × 5]
    I --> O[Add winnings to coins]
    J --> O
    K --> O
    L --> O
    M --> O
    N --> O
    G -->|NEIN| P[No win - coins stay deducted]
    O --> Q[Save updated Player]
    P --> Q
    Q --> R[Return game result JSON]
```

## System Architecture

```mermaid
graph TB
    subgraph "Frontend Layer"
        A[React App - localhost:5173]
        B[Vite Dev Server]
        C[Axios HTTP Client]
    end
    
    subgraph "Backend Layer"
        D[Spring Boot - localhost:8080]
        E[PlayerController]
        F[SlotController]
        G[GlobalExceptionHandler]
        H[CorsConfig]
    end
    
    subgraph "Data Layer"
        I[PlayerRepository]
        J[JPA/Hibernate]
        K[MySQL Database]
    end
    
    A --> C
    C --> D
    D --> E
    D --> F
    D --> G
    D --> H
    E --> I
    F --> I
    I --> J
    J --> K
```

## Entity Relationship Diagram

```mermaid
erDiagram
    PLAYERS {
        bigint id PK "AUTO_INCREMENT"
        varchar name "NOT NULL"
        int coins "NOT NULL"
    }
    
    PLAYERS ||--o{ GAME_SESSIONS : "conceptual relationship"
    
    GAME_SESSIONS {
        bigint id PK "Not implemented yet"
        bigint player_id FK
        int bet_amount
        string symbols
        int winnings
        datetime created_at
    }
```
