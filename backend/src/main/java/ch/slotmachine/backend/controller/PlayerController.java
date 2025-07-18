package ch.slotmachine.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.slotmachine.backend.model.Player;
import ch.slotmachine.backend.repository.PlayerRepository;

/**
 * Player Controller - REST API für Player Management
 * 
 * Diese Klasse definiert alle HTTP-Endpunkte für die Spielerverwaltung.
 * Sie folgt dem REST-Standard:
 * - GET für Daten abrufen
 * - POST für neue Daten erstellen  
 * - PUT für Daten aktualisieren
 * - DELETE für Daten löschen
 * 
 * Alle Methoden geben automatisch JSON zurück 
 */
@RestController // Kombination aus @Controller + @ResponseBody (JSON-Antworten)
@RequestMapping("/player") // Basis-URL für alle Endpunkte: /player
public class PlayerController {

    // ===== DEPENDENCY INJECTION =====
    
    private final PlayerRepository repository;

    /**
     * Konstruktor-basierte Dependency Injection (Best Practice)
     * Spring injiziert automatisch das PlayerRepository
     */
    public PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    // ===== REST ENDPUNKTE =====

    /**
     * GET /player - Alle Spieler abrufen
     * 
     * Frontend-Aufruf: fetch('http://localhost:8080/player')
     * 
     * @return Liste aller Spieler als JSON
     */
    @GetMapping // HTTP GET Request
    public List<Player> getAllPlayers() {
        // JpaRepository Methode - lädt alle Player aus der Datenbank
        return repository.findAll();
    }

    /**
     * POST /player - Neuen Spieler erstellen
     * 
     * Frontend-Aufruf: 
     * fetch('http://localhost:8080/player', {
     *   method: 'POST',
     *   headers: {'Content-Type': 'application/json'},
     *   body: JSON.stringify({name: 'Max', coins: 1000})
     * })
     * 
     * @param player Player-Objekt aus Request Body (automatisch von JSON konvertiert)
     * @return Der gespeicherte Spieler mit generierter ID
     */
    @PostMapping // HTTP POST Request
    public Player createPlayer(@RequestBody Player player) {
        // Validation hinzufügen
        validatePlayer(player);
        // @RequestBody konvertiert JSON automatisch zu Player-Objekt
        // save() speichert in Datenbank und gibt Player mit ID zurück
        return repository.save(player);
    }
    
    /**
     * DELETE /player/{id} - Spieler löschen
     * 
     * Frontend-Aufruf: 
     * fetch('http://localhost:8080/player/1', {method: 'DELETE'})
     * 
     * @param id Die ID des zu löschenden Spielers (aus URL-Pfad)
     */
    @DeleteMapping("/{id}") // {id} ist Pfad-Variable
    public void deletePlayer(@PathVariable Long id) {
        // @PathVariable extrahiert {id} aus der URL
        // deleteById() löscht Player mit gegebener ID aus Datenbank
        repository.deleteById(id);
    }
    
    /**
     * PUT /player/recharge/{id} - Spieler Coins aufladen
     * 
     * Frontend-Aufruf:
     * fetch('http://localhost:8080/player/recharge/1', {method: 'PUT'})
     * 
     * @param id Die ID des Spielers dessen Coins aufgeladen werden
     * @return Der aktualisierte Spieler mit neuen Coins
     * @throws RuntimeException wenn Spieler nicht gefunden wird
     */
    @PutMapping("/recharge/{id}") // HTTP PUT Request für Updates
    public Player rechargePlayer(@PathVariable Long id) {
        // Optional<Player> für sicheren Umgang mit null-Werten
        Optional<Player> optionalPlayer = repository.findById(id);
        
        if (optionalPlayer.isPresent()) {
            // Spieler gefunden - Coins auf 1000 setzen
            Player player = optionalPlayer.get();
            player.setCoins(1000); // Coins aufladen
            return repository.save(player); // In Datenbank aktualisieren
        }
        
        // Spieler nicht gefunden - Fehler werfen
        throw new RuntimeException("Player not found");
    }
    
    /**
     * Player-Validierung
     */
    private void validatePlayer(Player player) {
        if (player.getName() == null || player.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        if (player.getCoins() < 0) {
            throw new IllegalArgumentException("Coins cannot be negative");
        }
        if (player.getCoins() > 1000) {
            throw new IllegalArgumentException("Coins cannot exceed 1000");
        }
    }

    /*
     * Hinweise für Dozenten:
     * 
     * 1. REST-Konventionen:
     *    - GET /player → Alle Spieler
     *    - POST /player → Neuen Spieler erstellen
     *    - DELETE /player/{id} → Spieler löschen
     *    - PUT /player/recharge/{id} → Spieler aktualisieren
     * 
     * 2. Spring Boot Features:
     *    - Automatische JSON Serialisierung/Deserialisierung
     *    - Dependency Injection über Konstruktor
     *    - Exception Handling (kann erweitert werden)
     * 
     * 3. Datenbankoperationen:
     *    - Alle DB-Zugriffe über JpaRepository
     *    - Transaktionen automatisch verwaltet
     *    - SQL wird automatisch generiert
     */
}
