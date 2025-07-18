package ch.slotmachine.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Player Entity - Repräsentiert einen Spieler in der Datenbank
 * 
 * Diese Klasse ist eine JPA Entity, die automatisch eine MySQL-Tabelle erstellt.
 * Jeder Spieler hat eine eindeutige ID, einen Namen und Coins zum Spielen.
 */
@Entity // JPA Annotation - macht diese Klasse zu einer Datenbank-Tabelle
@Table(name = "player") // Expliziter Tabellenname (optional)
public class Player {

    // ===== EIGENSCHAFTEN (werden zu Datenbank-Spalten) =====
    
    @Id // Primärschlüssel der Tabelle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Increment ID
    private Long id;
    
    @Column(nullable = false) // Name darf nicht null sein
    private String name;
    
    @Column(nullable = false) // Coins dürfen nicht null sein
    private int coins;

    // ===== KONSTRUKTOREN =====
    
    /**
     * Standard-Konstruktor (für JPA erforderlich)
     */
    public Player() {}

    /**
     * Konstruktor zum Erstellen eines neuen Spielers
     * @param name Der Name des Spielers
     * @param coins Die Anzahl Start-Coins
     */
    public Player(String name, int coins) {
        this.name = name;
        this.coins = coins;
    }

    // ===== GETTER UND SETTER (für JSON-Serialisierung erforderlich) =====
    
    /**
     * @return Die eindeutige ID des Spielers
     */
    public Long getId() {
        return id;
    }

    /**
     * @return Der Name des Spielers
     */
    public String getName() {
        return name;
    }

    /**
     * @return Die aktuellen Coins des Spielers
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Setzt den Namen des Spielers
     * @param name Der neue Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setzt die Coins des Spielers (z.B. nach Gewinn/Verlust)
     * @param coins Die neue Anzahl Coins
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * String-Darstellung für Debugging
     */
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coins=" + coins +
                '}';
    }
}
