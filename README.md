# Schulmanager – Aufgaben- und Terminverwaltung (JavaFX)

Dieses Projekt ist eine Desktop-Anwendung zur Verwaltung von Schulaufgaben mit Kalender- und To-Do-Funktionen.  
Es wurde im Rahmen eines Schulprojekts an der HTL Spengergasse entwickelt.

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

## 2. Projekt öffnen & starten (für Entwickler)

Voraussetzungen:

- Java 17  
- Maven  
- JavaFX (wird automatisch über Maven geladen)

Start in einer IDE (z. B. IntelliJ oder VS Code):  
Projekt als Maven-Projekt importieren und `Launcher.java` bzw. den Programmeinstieg starten.
Alternativ in der Powershell über:
```
mvn clean install javafx:run
```

---

## 3. Funktionen

- Übersicht von Aufgaben und Terminen  
- Kalenderansicht  
- GUI mit JavaFX-Komponenten  
- Eingabe, Bearbeitung und Löschung von Einträgen  
- Datenhaltung in CSV-Datei (lokal)

---

## 4. Technologien

- Java 17  
- JavaFX  
- Maven  
- CSV für Datenspeicherung

---

## 5. Struktur

- `src/` – Quellcode  
- `pom.xml` – Maven-Konfiguration  
- `data/` – gespeicherte Aufgaben/Termine
- `ProjektbeschreibungSchulManager` - Erklärung zur Anwendung des Programms
- `SchulManager.jar` – ausführbare Anwendung  
- `README.md`

---

## Hinweis

Dieses Projekt wurde im Rahmen des POS-Unterrichts (Programmentwicklung & Softwaredesign) umgesetzt.  
Es dient als einfaches, lokal ausführbares Tool zur Organisation des Schulalltags.
