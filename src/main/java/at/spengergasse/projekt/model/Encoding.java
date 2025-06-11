package at.spengergasse.projekt.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Die {@code Encoding}-Klasse dient zur Erzeugung kryptographischer Hash-Werte
 * aus Strings oder Dateien mithilfe verschiedener Algorithmen.
 * <p>
 * Unterstützt werden unter anderem:
 * <ul>
 *     <li>MD2</li>
 *     <li>MD5</li>
 *     <li>SHA-1</li>
 *     <li>SHA-256</li>
 *     <li>SHA-384</li>
 *     <li>SHA-512</li>
 * </ul>
 * Die Ausgabe kann als Hex- oder Base64-String erfolgen.
 *
 * @author Leo Fanzott
 */
public class Encoding {

    private byte[] hash;
    private EncodingType encodingType;
    private MessageDigest md;
    private File fileToEncode;
    private String stringToEncode;

    /**
     * Enumeration aller unterstützten Kodier-Algorithmen.
     */
    public static enum EncodingType {
        MD2, MD5, SHA1, SHA256, SHA384, SHA512
    }

    /**
     * Erzeugt einen Hash aus einem String.
     *
     * @param stringToEncode Der zu kodierende String
     * @param encodingType   Der zu verwendende Hash-Algorithmus
     * @throws NoSuchAlgorithmException Wenn der Algorithmus nicht existiert
     * @throws EncodingException        Wenn kein EncodingType angegeben ist
     */
    public Encoding(String stringToEncode, EncodingType encodingType)
            throws NoSuchAlgorithmException, EncodingException {
        if (encodingType == null)
            throw new EncodingException("Value null for encodingType is not allowed!");
        this.encodingType = encodingType;
        this.fileToEncode = null;
        this.stringToEncode = stringToEncode;
        setMessageDigest();
        hash = md.digest(stringToEncode.getBytes());
    }

    /**
     * Erzeugt einen Hash aus einer Datei.
     *
     * @param fileToEncode Die Datei, die gehasht werden soll
     * @param encodingType Der Hash-Algorithmus
     * @throws NoSuchAlgorithmException Wenn der Algorithmus nicht existiert
     * @throws EncodingException        Wenn ungültige Datei oder Algorithmus
     * @throws IOException              Bei Fehlern im Dateizugriff
     */
    public Encoding(File fileToEncode, EncodingType encodingType)
            throws NoSuchAlgorithmException, EncodingException, IOException {
        if (encodingType == null)
            throw new EncodingException("Value null for encodingType is not allowed!");
        this.encodingType = encodingType;
        this.fileToEncode = fileToEncode;
        this.stringToEncode = null;
        setMessageDigest();
        if (!fileToEncode.isFile())
            throw new EncodingException(fileToEncode.getName() + " is not a file!");
        BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(fileToEncode));
        byte[] buffer = new byte[1024];
        int readBuffer = bfis.read(buffer);
        while (readBuffer != -1) {
            md.update(buffer, 0, readBuffer);
            readBuffer = bfis.read(buffer);
        }
        bfis.close();
        hash = md.digest();
    }

    /**
     * Initialisiert den entsprechenden {@code MessageDigest} basierend auf dem EncodingType.
     *
     * @throws NoSuchAlgorithmException Falls der Algorithmus nicht vorhanden ist
     */
    private void setMessageDigest() throws NoSuchAlgorithmException {
        switch (encodingType) {
            case SHA512 -> md = MessageDigest.getInstance("SHA-512");
            case SHA384 -> md = MessageDigest.getInstance("SHA-384");
            case SHA256 -> md = MessageDigest.getInstance("SHA-256");
            case SHA1 -> md = MessageDigest.getInstance("SHA-1");
            case MD5 -> md = MessageDigest.getInstance("MD5");
            case MD2 -> md = MessageDigest.getInstance("MD2");
            default -> throw new NoSuchAlgorithmException();
        }
    }

    /**
     * Gibt den Hash als Hexadezimal-Zeichenkette zurück.
     *
     * @return Hex-String der Kodierung
     */
    public String bytesToHex() {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Gibt den Hash als Base64-kodierten String zurück.
     *
     * @return Base64-String der Kodierung
     */
    public String getHashcodeAsBase64String() {
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Gibt das Hash-Ergebnis als Byte-Array zurück.
     *
     * @return Byte-Array mit dem Hashwert
     */
    public byte[] getHashcode() {
        return hash;
    }

    /**
     * Gibt den Hashwert im Terminal aus.
     * Zeigt dabei auch Informationen zur Eingabequelle und Algorithmus an.
     */
    public void printHash() {
        int i = 0;
        if (stringToEncode == null) {
            System.out.println("\n\nEncoded file: " + fileToEncode.getName());
        } else {
            System.out.println("\n\nEncoded string: " + stringToEncode);
        }
        System.out.println("-----------------------------------------");
        for (byte b : hash) {
            System.out.printf("%02x", b);
            i++;
        }
        System.out.println("\nLength: " + (i * 8) + " Bit | Encoding type: " + encodingType);
    }

    /**
     * Einstiegspunkt für Tests über Kommandozeile.
     * Demonstriert verschiedene Hash-Ausgaben.
     *
     * @param args Dateinamen für Datei-Hashing (optional)
     */
    public static void main(String[] args) {
        try {
            Encoding encode = new Encoding("test", EncodingType.SHA512);
            encode.printHash();

            encode = new Encoding("12345678", EncodingType.SHA256);
            encode.printHash();

            encode = new Encoding("test", EncodingType.SHA256);
            encode.printHash();

            encode = new Encoding("test", EncodingType.SHA1);
            encode.printHash();

            encode = new Encoding("moodle", EncodingType.MD5);
            encode.printHash();

            encode = new Encoding("test", EncodingType.MD2);
            encode.printHash();

            File file = new File(args[0]);
            encode = new Encoding(file, EncodingType.MD2);
            encode.printHash();

            file = new File(args[1]);
            encode = new Encoding(file, EncodingType.MD2);
            encode.printHash();

        } catch (NoSuchAlgorithmException | EncodingException | IOException e) {
            e.printStackTrace();
        }
    }
}
