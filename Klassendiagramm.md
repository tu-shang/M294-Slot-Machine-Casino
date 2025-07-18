# Klassendiagramm - Slot Machine Casino

## UML Klassendiagramm

```mermaid
classDiagram
    class Player {
        -Long id
        -String name
        -int coins
        +Player()
        +Player(String name, int coins)
        +getId() Long
        +getName() String
        +getCoins() int
        +setName(String name) void
        +setCoins(int coins) void
        +toString() String
    }

    class PlayerController {
        -PlayerRepository repository
        +PlayerController(PlayerRepository repository)
        +getAllPlayers() List~Player~
        +createPlayer(Player player) Player
        +deletePlayer(Long id) void
        +rechargePlayer(Long id) Player
        -validatePlayer(Player player) void
    }

    class SlotController {
        -PlayerRepository repository
        -Random random
        -String[] symbols
        +SlotController(PlayerRepository repository)
        +pullSlot(Long playerId) Map~String,Object~
        -getRandomSymbol() String
    }

    class PlayerRepository {
        <<interface>>
        +findAll() List~Player~
        +findById(Long id) Optional~Player~
        +save(Player player) Player
        +deleteById(Long id) void
    }

    class GlobalExceptionHandler {
        +handleIllegalArgument(IllegalArgumentException ex) ResponseEntity~String~
        +handleRuntimeException(RuntimeException ex) ResponseEntity~String~
    }

    PlayerController --> PlayerRepository : uses
    SlotController --> PlayerRepository : uses
    PlayerController --> Player : manages
    SlotController --> Player : uses
    PlayerRepository --> Player : stores
```

## Erklärung der Beziehungen

- **PlayerController** verwaltet Player-Entitäten über das PlayerRepository
- **SlotController** nutzt Player-Daten für Spiellogik über das PlayerRepository  
- **PlayerRepository** ist die Datenzugriffsschicht für Player-Entitäten
- **GlobalExceptionHandler** behandelt Exceptions global für alle Controller
- **Player** ist die zentrale Entität mit Attributen id, name und coins

## Technologie-Stack
- **Backend**: Spring Boot 3.5.3 mit JPA/Hibernate
- **Database**: MySQL für Persistierung
- **Frontend**: React 19.1.0 mit Vite
- **Testing**: Vitest mit React Testing Library
