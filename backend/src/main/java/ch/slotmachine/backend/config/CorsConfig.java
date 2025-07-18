package ch.slotmachine.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration - Cross-Origin Resource Sharing
 * 
 * Diese Klasse löst das CORS-Problem zwischen Frontend und Backend.
 * 
 * Das Problem:
 * - Frontend läuft auf http://localhost:5173 (React/Vite)
 * - Backend läuft auf http://localhost:8080 (Spring Boot)
 * - Browser blockiert standardmäßig Requests zwischen verschiedenen Ports (Same-Origin Policy)
 * 
 * Die Lösung:
 * - Backend erlaubt explizit Requests vom Frontend-Port
 * - Alle HTTP-Methoden und Headers werden erlaubt
 * - Sichere Konfiguration für Entwicklung
 */
@Configuration // Spring Configuration Klasse
public class CorsConfig {

    /**
     * CORS-Konfiguration als Spring Bean
     * 
     * Diese Methode erstellt eine WebMvcConfigurer-Bean, die CORS-Regeln definiert.
     * Spring Boot wendet diese Konfiguration automatisch auf alle Controllers an.
     * 
     * @return WebMvcConfigurer mit CORS-Einstellungen
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            
            /**
             * CORS-Mapping-Konfiguration
             * 
             * @param registry CorsRegistry zum Definieren der CORS-Regeln
             */
            @Override
            public void addCorsMappings(@org.springframework.lang.NonNull CorsRegistry registry) {
                registry
                    .addMapping("/**")  // Gilt für alle Endpunkte (/** = alle URLs)
                    
                    // Erlaubte Origins (Frontend-URLs)
                    .allowedOrigins("http://localhost:5173")  // React Development Server
                    
                    // Erlaubte HTTP-Methoden
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    
                    // Erlaubte Headers (alle Headers erlaubt)
                    .allowedHeaders("*");
                    
                    // Optional: Credentials erlauben (für Cookies/Authentication)
                    // .allowCredentials(true);
            }
        };
    }
    
    /*
     * ===== CORS ERKLÄRUNG FÜR DOZENTEN =====
     * 
     * 1. WARUM CORS?
     *    - Browser-Sicherheit: Verhindert Cross-Site Attacks
     *    - Same-Origin Policy: Nur Requests an gleiche Domain erlaubt
     *    - Development Problem: Frontend (5173) ≠ Backend (8080)
     * 
     * 2. WAS PASSIERT OHNE CORS?
     *    - Browser blockiert fetch() Requests
     *    - Console Error: "CORS policy: No 'Access-Control-Allow-Origin' header"
     *    - Frontend kann nicht mit Backend kommunizieren
     * 
     * 3. WIE CORS FUNKTIONIERT:
     *    - Preflight Request: Browser fragt Backend, ob Request erlaubt ist
     *    - Backend antwortet mit erlaubten Origins/Methods/Headers
     *    - Echter Request wird nur bei Erlaubnis gesendet
     * 
     * 4. PRODUCTION CONSIDERATIONS:
     *    - Niemals .allowedOrigins("*") in Production!
     *    - Spezifische Frontend-URLs konfigurieren
     *    - HTTPS verwenden
     *    - Minimale Permissions (nur nötige Methods/Headers)
     * 
     * 5. ALTERNATIVE LÖSUNGEN:
     *    - @CrossOrigin Annotation auf Controller-Level
     *    - Proxy-Konfiguration im Frontend
     *    - Reverse Proxy (nginx, Apache)
     */
}
