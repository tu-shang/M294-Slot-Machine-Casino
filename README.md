# 🎰 Slot Machine Casino

Ein vollständiges Casino-Spiel mit Spring Boot Backend und React Frontend.

## 📋 Projektbeschreibung

Dieses Projekt ist eine virtuelle Slot Machine mit:
- **Player Management**: Spieler erstellen, löschen und Coins verwalten
- **Slot Machine**: 3-Reel Slot Machine mit realistischen Animationen
- **Gewinnlogik**: Verschiedene Gewinnkombinationen mit unterschiedlichen Auszahlungen
- **Moderne UI**: Casino-Design mit Glow-Effekten und animiertem Hebel

## 🏗️ Technologie Stack

### Backend (Spring Boot)
- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **MySQL Database**
- **Maven**

### Frontend (React)
- **React 19.1.0**
- **Vite** (Build Tool)
- **Modern CSS** mit Animationen
- **Responsive Design**

## 🚀 Installation & Start

### Voraussetzungen
- Java 17+
- Node.js 18+
- MySQL Server
- Maven

### Backend starten
```bash
cd backend
./mvnw spring-boot:run
```
Backend läuft auf: http://localhost:8080

### Frontend starten
```bash
cd frontend/frontend
npm install
npm run dev
```
Frontend läuft auf: http://localhost:5173

## 🗄️ Datenbank Setup

1. MySQL Server starten
2. Datenbank erstellen:
```sql
CREATE DATABASE slotmachine;
```

3. Die Tabellen werden automatisch von Spring Boot erstellt (hibernate.ddl-auto=update)

## 🎮 Spielregeln

### Einsatz
- Jeder Spin kostet **10 Coins**
- Spieler müssen mindestens 10 Coins haben

### Gewinnkombinationen (alle 3 Symbole gleich)
- 💎 Diamant: **500 Coins**
- 7️⃣ Sieben: **300 Coins** 
- ⭐ Stern: **200 Coins**
- 🔔 Glocke: **150 Coins**
- 🍒 Kirsche: **100 Coins**
- 🍋 Zitrone: **80 Coins**
- 🍉 Wassermelone: **60 Coins**

## 🔌 API Endpunkte

### Player Management
- `GET /player` - Alle Spieler abrufen
- `POST /player` - Neuen Spieler erstellen
- `DELETE /player/{id}` - Spieler löschen
- `PUT /player/recharge/{id}` - Coins auf 1000 aufladen

### Slot Machine
- `POST /slot/pull/{playerId}` - Slot Machine ziehen

## 📁 Projektstruktur

```
Slot-Machine/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/ch/slotmachine/backend/
│   │   ├── BackendApplication.java
│   │   ├── CorsConfig.java     # CORS Konfiguration
│   │   ├── Player.java         # Player Entity
│   │   ├── PlayerRepository.java
│   │   ├── PlayerController.java
│   │   └── SlotController.java # Slot Machine Logik
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/frontend/          # React Frontend
│   ├── src/
│   │   ├── App.jsx            # Hauptkomponente
│   │   ├── App.css            # Casino Styling
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## ✨ Features

### Frontend Features
- 🎨 **Modernes Casino-Design** mit dunklem Theme
- ⚡ **Realistische Reel-Animationen**
- 🕹️ **Funktionaler Hebel** (klickbar)
- 📱 **Responsive Design** für Mobile
- 🌟 **Glow-Effekte** bei Gewinnen
- 🎯 **Player Management Interface**

### Backend Features
- 🔒 **Datenpersistierung** mit MySQL
- 🎲 **Zufällige Symbolgeneration**
- 💰 **Gewinnlogik-Berechnung**
- 🔄 **CORS-Unterstützung** für Frontend
- ✅ **Input Validation**
- 🚫 **Error Handling**

## 🛠️ Entwicklung

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Build
```bash
cd frontend/frontend
npm run build
```

## 👨‍🏫 Für den Dozenten

Das Projekt demonstriert:
- **Full-Stack Entwicklung** (Frontend + Backend)
- **REST API Design** und Implementation
- **Datenbankintegration** mit JPA/Hibernate
- **Moderne Frontend-Technologien** (React, Vite)
- **Responsive Web Design**
- **Clean Code Prinzipien**
- **Projektstruktur** und Organisation

---
**Entwickelt für das Fachgespräch** 🎓
