package at.spengergasse.projekt.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Hilfsklasse zur Erzeugung von Hash-Werten aus Strings oder Dateien
 * mittels diverser Kodierungsalgorithmen (MD5, SHA256, etc.).
 *
 * Unterstützt Ausgabe als Hex- oder Base64-String.
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
	 * Mögliche Hash-Algorithmen.
	 */
	public static enum EncodingType {
		MD2, MD5, SHA1, SHA256, SHA384, SHA512
	}

	/**
	 * Erstellt ein Encoding-Objekt für einen String.
	 *
	 * @param stringToEncode Der zu kodierende String
	 * @param encodingType   Der gewünschte Algorithmus
	 * @throws NoSuchAlgorithmException Wenn der Algorithmus nicht verfügbar ist
	 * @throws EncodingException        Wenn encodingType null ist
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
	 * Erstellt ein Encoding-Objekt für eine Datei.
	 *
	 * @param fileToEncode Die zu kodierende Datei
	 * @param encodingType Der gewünschte Algorithmus
	 * @throws NoSuchAlgorithmException Wenn der Algorithmus nicht verfügbar ist
	 * @throws EncodingException        Bei fehlerhafter Datei oder null-Werten
	 * @throws IOException              Falls beim Dateizugriff ein Fehler auftritt
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
	 * Initialisiert den MessageDigest je nach ausgewähltem Algorithmus.
	 *
	 * @throws NoSuchAlgorithmException Falls der Algorithmus nicht existiert
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
	 * Gibt den Hash als hexadezimale Zeichenkette zurück.
	 *
	 * @return Der Hash als Hex-String
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
	 * Gibt den Hashwert als Base64-codierte Zeichenkette zurück.
	 *
	 * @return Der Hash als Base64-String
	 */
	public String getHashcodeAsBase64String() {
		return Base64.getEncoder().encodeToString(hash);
	}

	/**
	 * Gibt das Hash-Ergebnis als Byte-Array zurück.
	 *
	 * @return Byte-Array mit Hashwert
	 */
	public byte[] getHashcode() {
		return hash;
	}

	/**
	 * Gibt den Hashwert im Terminal aus – inkl. Info zur Eingabeart und Bitlänge.
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
	 * Testmethode zum Ausprobieren verschiedener Kodierungen.
	 *
	 * @param args Dateinamen als Argumente für Datei-Hashing
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
