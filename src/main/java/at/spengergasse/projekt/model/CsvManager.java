package at.spengergasse.projekt.model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet Benutzer- und Termindaten im Dateisystem.
 * Passwörter werden verschlüsselt gespeichert.
 */
public class CsvManager {

    private static Path BASE_DIR = Paths.get(System.getProperty("user.home"), "SchulManager", "data");
    private static final Path USER_FILE = Paths.get("data", "benutzer.csv");

    /**
     * Setzt das Verzeichnis zur Speicherung von Daten.
     * @param newBaseDir neues Verzeichnis
     */
    public static void setBaseDir(Path newBaseDir) {
        BASE_DIR = newBaseDir;
    }

    // ---------------- BENUTZER ----------------

    /**
     * Prüft, ob ein Benutzer bereits existiert.
     * @param username Benutzername
     * @return true wenn vorhanden
     */
    public static boolean userExists(String username) {
        if (!Files.exists(USER_FILE)) return false;
        try (BufferedReader reader = Files.newBufferedReader(USER_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Registriert einen neuen Benutzer mit verschlüsseltem Passwort.
     * @param username Benutzername
     * @param role Rolle (user/admin)
     * @param password Klartextpasswort
     * @return true bei Erfolg
     */
    public static boolean registerUser(String username, String role, String password) {
        try {
            if (userExists(username)) return false;
            ensureDirectory();
            String hashed = new Encoding(password, Encoding.EncodingType.SHA256).bytesToHex();
            try (BufferedWriter writer = Files.newBufferedWriter(USER_FILE, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(username + ";" + role + ";" + hashed);
                writer.newLine();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Prüft Benutzer-Login mit Hash-Vergleich.
     * @param username Benutzername
     * @param password Passwort
     * @return User-Objekt oder null bei Fehlschlag
     */
    public static User validateLogin(String username, String password) {
        if (!Files.exists(USER_FILE)) return null;
        try (BufferedReader reader = Files.newBufferedReader(USER_FILE)) {
            String line;
            String hashedInput = new Encoding(password, Encoding.EncodingType.SHA256).bytesToHex();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[0].equals(username) && parts[2].equals(hashedInput)) {
                    return new User(parts[0], parts[1], parts[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gibt alle Benutzer zurück.
     * @return Liste von Benutzern
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        if (!Files.exists(USER_FILE)) return users;
        try (BufferedReader reader = Files.newBufferedReader(USER_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Speichert alle Benutzer (z. B. nach Änderung).
     * @param users Liste
     */
    private static void saveAllUsers(List<User> users) {
        ensureDirectory();
        try (BufferedWriter writer = Files.newBufferedWriter(USER_FILE)) {
            for (User u : users) {
                writer.write(u.getUsername() + ";" + u.getRole() + ";" + u.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String username) {
        List<User> users = loadUsers();
        users.removeIf(u -> u.getUsername().equals(username));
        saveAllUsers(users);
    }

    public static void changeUsername(String oldUsername, String newUsername) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(oldUsername)) {
                user.setUsername(newUsername);
                break;
            }
        }
        saveAllUsers(users);
    }

    public static void changePassword(String username, String newPassword) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                try {
                    user.setPassword(new Encoding(newPassword, Encoding.EncodingType.SHA256).bytesToHex());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        saveAllUsers(users);
    }

    public static void addUser(String username, String role, String password) {
        try {
            List<User> users = loadUsers();
            String hash = new Encoding(password, Encoding.EncodingType.SHA256).bytesToHex();
            users.add(new User(username, role, hash));
            saveAllUsers(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- TERMINE ----------------

    public static List<Termin> loadTermine(String username) {
        List<Termin> termine = new ArrayList<>();
        Path path = BASE_DIR.resolve("termine_" + username + ".csv");
        if (!Files.exists(path)) return termine;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length == 4) {
                    String fach = parts[0];
                    LocalDate datum = parts[1].isEmpty() ? null : LocalDate.parse(parts[1]);
                    String typ = parts[2];
                    String notiz = parts[3];
                    termine.add(new Termin(fach, datum, typ, notiz));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return termine;
    }

    public static void saveTermine(String username, List<Termin> termine) {
        ensureDirectory();
        Path path = BASE_DIR.resolve("termine_" + username + ".csv");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Termin t : termine) {
                writer.write(
                        t.getFach() + ";" +
                                (t.getDatum() != null ? t.getDatum().toString() : "") + ";" +
                                t.getTyp() + ";" +
                                t.getNotiz()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt Datenordner, wenn nicht vorhanden.
     */
    private static void ensureDirectory() {
        try {
            if (!Files.exists(BASE_DIR)) {
                Files.createDirectories(BASE_DIR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt das Basisverzeichnis für Daten zurück.
     * @return
     */
    public static Path getBaseDir() {
        return BASE_DIR;
    }

}
