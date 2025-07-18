package ch.slotmachine.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.slotmachine.backend.model.Player;

/**
 * Player Repository - Datenzugriff für Player Entity
 * 
 * Diese Interface erweitert JpaRepository und bietet automatisch alle
 * Standard-CRUD-Operationen (Create, Read, Update, Delete) für Player-Objekte.
 * 
 * Spring Data JPA erstellt automatisch eine Implementierung zur Laufzeit.
 * Keine zusätzliche Implementierung nötig!
 * 
 * Verfügbare Methoden (automatisch generiert):
 * - findAll() - Alle Spieler laden
 * - findById(Long id) - Spieler nach ID suchen
 * - save(Player player) - Spieler speichern/updaten
 * - deleteById(Long id) - Spieler nach ID löschen
 * - count() - Anzahl Spieler zählen
 * - exists(Long id) - Prüfen ob Spieler existiert
 * 
 * @param <Player> Die Entity-Klasse
 * @param <Long> Der Typ des Primärschlüssels
 */
@Repository // Spring Annotation - kennzeichnet als Data Access Layer
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    // ===== AUTOMATISCH VERFÜGBARE METHODEN =====
    // findAll() -> List<Player>
    // findById(Long id) -> Optional<Player>
    // save(Player player) -> Player
    // deleteById(Long id) -> void
    // count() -> long
    // existsById(Long id) -> boolean
    
    // ===== CUSTOM QUERIES (falls benötigt) =====
    
    // Beispiel für custom Query (aktuell nicht verwendet):
    // Optional<Player> findByName(String name);
    // List<Player> findByCoinsGreaterThan(int coins);
    
    /*
     * Hinweis für Dozenten:
     * Spring Data JPA generiert automatisch SQL-Queries basierend auf Methodennamen.
     * Zum Beispiel würde "findByName" zu: SELECT * FROM player WHERE name = ?
     */
}
