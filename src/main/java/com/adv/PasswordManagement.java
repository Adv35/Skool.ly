package com.adv;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Die Klasse PasswordManagement ist fuer das Hashen (mit Salt) und Pruefen von Passwoertern.
 * Der verwendete Algorithmus ist SHA-512.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class PasswordManagement {

    // 128 Bit langer Salt
    private static final int SALT_BYTE_SIZE = 16;
    // Hashing Algorithmus
    private static final String HASH_ALGORITHM = "SHA-512";

    /**
     * Hasht ein Passwort mit einem zufaellig generierten Salt.
     * Das Ergebnis ist ein String im Format "Salt:Hash", wobei beide Teile in Base64 sind.
     * @param password Das zu hashende Passwort.
     * @return Der kombinierte String aus Salt und Hash, getrennt durch einen Doppelpunkt.
     */
    public String hashPassword(String password) {
        // 1.) Generiere Salt
        byte[] salt = generateSalt();

        // 2.) Hashe das Passwort mit dem Salt
        String hashedPassword = hashPasswordSub(password, salt);

        // Base-64 kodierung -> Speichern in Datenbank als Text
        String saltString = Base64.getEncoder().encodeToString(salt);

        //Beides getrennt durch einen Doppelpunkt getrennt
        return saltString + ":" + hashedPassword;
    }

    /**
     * Ueberprueft, ob ein eingegebenes Passwort mit einem gespeicherten Hash passt.
     * Trennt den Salt aus dem gespeicherten Hash und hasht das eingegebene Passwort damit erneut.
     *
     * @param password Das eingegebene Passwort.
     * @param storedDbHash Der gespeicherte Hash aus der Datenbank.
     * @return true -> Passwort passt; false -> passt nicht
     */
    public boolean checkPassword(String password, String storedDbHash) {
        if (storedDbHash == null || !storedDbHash.contains(":")) {
            return false;
        }
        // Teilt den String beim Doppelpunkt → Teilen von Salt und Hash
        String[] parts = storedDbHash.split(":");
        if (parts.length != 2) {
            return false;
        }

        // Salt zurück in Bytes dekodieren
        byte[] salt = Base64.getDecoder().decode(parts[0]);

        // Mit demselben Salt das eingegebene Passwort hashen
        String hashOfAttempt = hashPasswordSub(password, salt);

        // Gucken, ob derselbe Hash rauskommt
        return hashOfAttempt.equals(parts[1]);
    }

    /**
     * Generiert einen sicheren, zufaelligen Salt.
     * SecureRandom, da normale Random-Klassen vorhersagbar sein koennen.
     *
     * @return Ein Byte-Array mit dem Salt.
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Die interne Methode, die das eigentliche Hashing durchfuehrt.
     *
     * @param password Das Passwort.
     * @param salt Der Salt.
     * @return Der Hash als Base64-kodierter String.
     */
    private String hashPasswordSub(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            // Salt zum Digest
            md.update(salt);
            // Password (als Bytes) und Hashen
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hashedPassword = md.digest();

            // Hash zu Base64
            return Base64.getEncoder().encodeToString(hashedPassword);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find following HashAlgorithm: " + HASH_ALGORITHM);
        }

    }




}
