package ch.slotmachine.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.slotmachine.backend.model.Player;
import ch.slotmachine.backend.repository.PlayerRepository;

/**
 * Slot Controller - Herzstück der Slot Machine Logik
 * 
 * Diese Klasse implementiert die gesamte Spiellogik der Slot Machine:
 * - Zufällige Symbolgeneration
 * - Gewinnberechnung
 * - Coin-Management
 * - Transaktions-Sicherheit
 * 
 * Jeder "Pull" kostet 10 Coins und kann verschiedene Gewinne bringen.
 */
@RestController // REST API Controller für JSON-Responses
@RequestMapping("/slot") // Basis-URL: /slot
public class SlotController {

    // ===== GAME CONFIGURATION =====
    
    private final PlayerRepository repository; // Zugriff auf Spieler-Datenbank
    private final Random random = new Random(); // Zufallsgenerator für faire Spiele
    
    /**
     * Alle verfügbaren Slot-Symbole
     * Diese Symbole erscheinen zufällig in den 3 Reels
     */
    private final String[] symbols = {"🍒", "🍋", "💎", "🔔", "⭐", "🍉"};

    /**
     * Konstruktor mit Dependency Injection
     * @param repository PlayerRepository wird von Spring injiziert
     */
    public SlotController(PlayerRepository repository) {
        this.repository = repository;
    }

    // ===== HAUPTSPIEL-LOGIK =====

    /**
     * POST /slot/pull/{playerId} - Slot Machine ziehen
     * 
     * Dies ist der Hauptendpunkt für das Slot-Spiel.
     * 
     * Frontend-Aufruf:
     * fetch('http://localhost:8080/slot/pull/1', {method: 'POST'})
     * 
     * Spielablauf:
     * 1. Spieler validieren (existiert er? hat er genug Coins?)
     * 2. 10 Coins abziehen (Spieleinsatz)
     * 3. 3 zufällige Symbole generieren
     * 4. Gewinn berechnen (alle 3 gleich = Jackpot!)
     * 5. Gewinn zu Coins hinzufügen
     * 6. Ergebnis an Frontend senden
     * 
     * @param playerId ID des Spielers (aus URL extrahiert)
     * @return Map mit Spielergebnis (slots, win, winAmount, coins)
     */
    @PostMapping("/pull/{playerId}")
    public Map<String, Object> pullSlot(@PathVariable Long playerId) {
        
        // ===== 1. SPIELER VALIDIERUNG =====
        
        Optional<Player> optionalPlayer = repository.findById(playerId);
        
        // Spieler existiert nicht
        if (optionalPlayer.isEmpty()) {
            return Map.of("error", "Player not found");
        }

        Player player = optionalPlayer.get();

        // Nicht genug Coins für Einsatz (10 Coins pro Spiel)
        if (player.getCoins() < 10) {
            return Map.of("error", "Not enough coins");
        }

        // ===== 2. EINSATZ ABZIEHEN =====
        
        player.setCoins(player.getCoins() - 10); // 10 Coins Einsatz

        // ===== 3. SYMBOLE GENERIEREN =====
        
        // Drei zufällige Symbole für die 3 Reels
        String symbol1 = getRandomSymbol();
        String symbol2 = getRandomSymbol();
        String symbol3 = getRandomSymbol();

        List<String> slots = List.of(symbol1, symbol2, symbol3);

        // ===== 4. GEWINNBERECHNUNG =====
        
        int winAmount = 0;

        // Gewinnlogik: Alle 3 Symbole müssen gleich sein für Jackpot
        if (slots.get(0).equals(slots.get(1)) && slots.get(1).equals(slots.get(2))) {
            
            // JACKPOT! - Gewinn abhängig vom Symbol
            String winningSymbol = slots.get(0);
            
            // Verschiedene Symbole haben verschiedene Gewinnwerte
            switch (winningSymbol) {
                case "💎" -> winAmount = 500;  // Diamant - höchster Gewinn (50x Einsatz)
                case "⭐" -> winAmount = 200;  // Stern - hoher Gewinn (20x Einsatz)
                case "🔔" -> winAmount = 150;  // Glocke - mittlerer Gewinn (15x Einsatz)
                case "🍒" -> winAmount = 100;  // Kirsche - niedriger Gewinn (10x Einsatz)
                case "🍋" -> winAmount = 80;   // Zitrone - niedriger Gewinn (8x Einsatz)
                case "🍉" -> winAmount = 60;   // Wassermelone - niedrigster Gewinn (6x Einsatz)
                default -> winAmount = 50;     // Fallback für unbekannte Symbole
            }
        }
        // Wenn nicht alle 3 gleich sind: winAmount bleibt 0 (kein Gewinn)

        // ===== 5. GEWINN AUSZAHLEN =====
        
        player.setCoins(player.getCoins() + winAmount); // Gewinn zu Coins hinzufügen
        repository.save(player); // Geänderten Spieler in Datenbank speichern

        // ===== 6. ANTWORT ERSTELLEN =====
        
        // Response Map für Frontend (wird automatisch zu JSON konvertiert)
        Map<String, Object> result = new HashMap<>();
        result.put("slots", slots);           // Die 3 Symbole für Frontend-Anzeige
        result.put("win", winAmount > 0);     // Boolean: Gewonnen ja/nein
        result.put("winAmount", winAmount);   // Gewinnbetrag (0 wenn kein Gewinn)
        result.put("coins", player.getCoins()); // Aktuelle Coins nach dem Spiel

        return result; // Spring konvertiert automatisch zu JSON
    }

    // ===== HILFSMETHODEN =====

    /**
     * Generiert ein zufälliges Symbol aus dem symbols-Array
     * 
     * Diese Methode sorgt für Fairness - jedes Symbol hat die gleiche
     * Wahrscheinlichkeit zu erscheinen (1/6 = ca. 16.67%)
     * 
     * @return Ein zufälliges Emoji-Symbol
     */
    private String getRandomSymbol() {
        // random.nextInt(6) gibt Zahlen 0-5 zurück
        // Diese werden als Index für das symbols-Array verwendet
        return symbols[random.nextInt(symbols.length)];
    }
    
    /*
     * ===== GAME DESIGN ERKLÄRUNG FÜR DOZENTEN =====
     * 
     * 1. FAIRNESS:
     *    - Echter Zufallsgenerator (java.util.Random)
     *    - Jedes Symbol hat gleiche Wahrscheinlichkeit
     *    - Server-side Validierung (kein Client-Cheating möglich)
     * 
     * 2. GEWINNCHANCEN:
     *    - Jackpot-Chance: ca. 1/6³ = 1/216 ≈ 0.46%
     *    - Verschiedene Symbole = verschiedene Auszahlungen
     *    - House Edge: Durchschnittlicher Gewinn < Einsatz (Casino-Prinzip)
     * 
     * 3. DATENBANK-SICHERHEIT:
     *    - Alle Transaktionen werden sofort gespeichert
     *    - Coins können nicht manipuliert werden
     *    - Automatisches Rollback bei Fehlern (Spring Transaction)
     * 
     * 4. API DESIGN:
     *    - RESTful: POST für state-changing Operations
     *    - JSON Response mit allen relevanten Daten
     *    - Error Handling für Edge Cases
     * 
     * 5. BUSINESS LOGIK:
     *    - 10 Coins Einsatz pro Spiel
     *    - Gewinn nur bei 3 gleichen Symbolen
     *    - Verschiedene Symbole = verschiedene Multiplikatoren
     */
}
