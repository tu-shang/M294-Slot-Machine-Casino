package ch.slotmachine.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Backend Application - Hauptklasse der Spring Boot Anwendung
 * 
 * Diese Klasse ist der Einstiegspunkt der gesamten Backend-Anwendung.
 * Spring Boot startet von hier aus und konfiguriert automatisch:
 * - Embedded Tomcat Server (Port 8080)
 * - JPA/Hibernate für Datenbankzugriff
 * - REST Controllers für API-Endpunkte
 * - Dependency Injection Container
 * - Auto-Configuration basierend auf Classpath
 */
@SpringBootApplication // Kombination aus @Configuration, @EnableAutoConfiguration, @ComponentScan
public class BackendApplication {

	/**
	 * Main-Methode - Startet die Spring Boot Anwendung
	 * 
	 * SpringApplication.run() führt folgende Schritte aus:
	 * 1. ApplicationContext erstellen
	 * 2. Auto-Configuration ausführen
	 * 3. Component Scanning (findet alle @Controller, @Service, etc.)
	 * 4. Embedded Server starten (Tomcat auf Port 8080)
	 * 5. Datenbank-Verbindung herstellen
	 * 6. JPA Entities zu Tabellen mappen
	 * 
	 * @param args Kommandozeilen-Argumente (nicht verwendet)
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		
		// Nach dem Start ist die Anwendung verfügbar unter:
		// http://localhost:8080
		
		System.out.println("🎰 Slot Machine Backend gestartet!");
		System.out.println("📡 Server läuft auf: http://localhost:8080");
		System.out.println("🎮 API Endpunkte:");
		System.out.println("   GET    /player           - Alle Spieler");
		System.out.println("   POST   /player           - Neuen Spieler erstellen");
		System.out.println("   DELETE /player/{id}      - Spieler löschen");
		System.out.println("   PUT    /player/recharge/{id} - Coins aufladen");
		System.out.println("   POST   /slot/pull/{id}   - Slot Machine spielen");
	}
	
	/*
	 * ===== SPRING BOOT MAGIC ERKLÄRUNG FÜR DOZENTEN =====
	 * 
	 * 1. @SpringBootApplication macht folgendes automatisch:
	 *    - @Configuration: Klasse kann Spring Beans definieren
	 *    - @EnableAutoConfiguration: Spring konfiguriert sich selbst
	 *    - @ComponentScan: Findet alle Spring Components im Package
	 * 
	 * 2. AUTO-CONFIGURATION erkennt automatisch:
	 *    - MySQL Connector → Datenbank-Config
	 *    - JPA Dependency → Hibernate Setup
	 *    - Web Dependency → REST API Setup
	 *    - application.properties → Custom Configuration
	 * 
	 * 3. EMBEDDED SERVER:
	 *    - Kein externes Tomcat nötig
	 *    - Server ist Teil der JAR-Datei
	 *    - Einfaches Deployment: java -jar backend.jar
	 * 
	 * 4. PRODUCTION DEPLOYMENT:
	 *    - ./mvnw clean package → Erstellt JAR-Datei
	 *    - java -jar target/backend-0.0.1-SNAPSHOT.jar
	 *    - Oder als Docker Container
	 *    - Oder auf Cloud Platforms (Heroku, AWS, Azure)
	 * 
	 * 5. DEVELOPMENT VORTEILE:
	 *    - Hot Reload mit Spring DevTools
	 *    - Automatische Restart bei Code-Änderungen
	 *    - Kein Application Server Setup nötig
	 *    - Schneller Entwicklungszyklus
	 */

}
