# Schulmanager – Aufgaben- und Terminverwaltung (JavaFX)

Dieses Projekt ist eine Desktop-Anwendung zur Verwaltung von Schulaufgaben mit Kalender- und To-Do-Funktionen.  
Es wurde im Rahmen eines Schulprojekts an der HTL Spengergasse entwickelt.

---

## Voraussetzungen

- Java JDK 17 oder neuer  
- Maven (für Build und Entwicklung)

> Hinweis: Falls `java` nicht erkannt wird, ist Java nicht installiert oder nicht im PATH verfügbar.

---

## 1. Repository herunterladen

```
git clone https://github.com/dwolken/2CHIF-POS-SchulManager.git
cd 2CHIF-POS-SchulManager
```

oder ZIP-Datei herunterladen und entpacken.

---

## 2. Anwendung starten

Das Repository enthält eine ausführbare JAR-Datei.

Starten der Anwendung:

```
java -jar SchulManager-1.0-SNAPSHOT.jar
```

---

## 3. Projekt öffnen & starten (für Entwickler)

Start in einer IDE (z. B. IntelliJ oder VS Code):  
Projekt als Maven-Projekt importieren und `Launcher.java` bzw. den Programmeinstieg starten.

Alternativ in der Powershell über:
```
mvn clean install javafx:run
```

---

## 4. Funktionen

- Übersicht von Aufgaben und Terminen  
- Kalenderansicht  
- GUI mit JavaFX-Komponenten  
- Eingabe, Bearbeitung und Löschung von Einträgen  
- Datenhaltung in CSV-Datei (lokal)

---

## 5. Technologien

- Java 17  
- JavaFX  
- Maven  
- CSV für Datenspeicherung

---

## 6. Struktur

- `src/` – Quellcode  
- `pom.xml` – Maven-Konfiguration  
- `data/` – gespeicherte Aufgaben/Termine/Benutzer
- `ProjektbeschreibungSchulManager` - Erklärung zur Anwendung des Programms
- `SchulManager.jar` – ausführbare Anwendung  
- `README.md`

---

## Hinweis

Dieses Projekt wurde im Rahmen des POS-Unterrichts (Programmentwicklung & Softwaredevelopment) umgesetzt.  
Es dient als einfaches, lokal ausführbares Tool zur Organisation des Schulalltags.
