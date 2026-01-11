package com.adv;

/**
 * Die User-Klasse repraesentiert einen Benutzer der App.
 * Ein Objekt dieser Klasse entspricht einer Zeile in der Tabelle users in der Datenbank.
 * Benutzer koennen verschiedene Rollen haben (student, teacher, admin).
 *
 * @author Advik Vattamwar
 * @version 10.01.2026
 */

public class User {
    private final String USER_ID;
    private final String FIRST_NAME;
    private final String LAST_NAME;
    private final String USERNAME;
    private final String PASSWORD_HASH;
    private final String ROLE;

    /**
     * Konstruktor mit userID (fuer wenn aus der Datenbank geladen wird)
     *
     **/
    public User(String userId, String firstName, String lastName, String username, String passwordHash, String role) {
        this.USER_ID = userId;
        this.FIRST_NAME = firstName;
        this.LAST_NAME = lastName;
        this.USERNAME = username;
        this.PASSWORD_HASH = passwordHash;
        this.ROLE = role;

    }

    /**
     * Konstruktor ohne userID (wenn in die Datenbank geladen werden muss)
     *
     **/
    public User(String firstName, String lastName, String username, String passwordHash, String role) {
        this(null, firstName, lastName, username, passwordHash, role);
    }

    /**
     * Getter-Methode.
     *
     * @return Gibt die userID zurueck.
     *
     **/
    public String getId() {
        return USER_ID;
    }

    /**
     * Getter-Methode.
     *
     * @return Gibt den Vornamen zurueck.
     *
     **/
    public String getFirstName() {
        return FIRST_NAME;
    }

    /**
     * Getter-Methode.
     *
     * @return Gibt den Nachnamen zurueck.
     *
     **/
    public String getLastName() {
        return LAST_NAME;
    }

    /**
     * Getter-Methode.
     *
     * @return Gibt den Nutzernamen zurueck.
     *
     **/
    public String getUsername() {
        return USERNAME;
    }

    /**
     * Getter-Methode.
     *
     * @return Gibt das gehashte Passwort zurueck.
     *
     **/
    public String getPasswordHash() {
        return PASSWORD_HASH;
    }

    /**
     * Getter-Methode.
     *
     * @return Gibt die Rolle des Nutzers zurueck.
     *
     **/
    public String getRole() {
        return ROLE;
    }

    /**
     * Abbildung einer Note in einem String mit All seinen Werten ausser dem PasswortHash.
     *
     **/
    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", FIRST_NAME='" + FIRST_NAME + '\'' +
                ", LAST_NAME='" + LAST_NAME + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", ROLE='" + ROLE + '\'' +
                '}';
    }

}
