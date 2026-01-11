package com.adv;

/** Kurs - Klasse. Ein Objekt der Klasse entspricht einer Reihe der Tabelle courses in der Datenbank.
 * Hat alle Details zu einem Kurs.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public class Course {
    private final String COURSE_ID;
    private final String NAME;
    private final String TEACHER_ID;
    private final String DESCRIPTION;

    /** Konstruktor mit KursID (falls die Note aus der Datenbank kommt) **/
    public Course(String courseId, String name, String teacherId, String description) {
        this.COURSE_ID = courseId;
        this.NAME = name;
        this.TEACHER_ID = teacherId;
        this.DESCRIPTION = description;
    }

    /** Konstruktor ohne KursID (falls die Note in die Datenbank muss) **/
    public Course (String name, String teacherId, String description) {
        this(null, name, teacherId, description);
    }

    /** Getter-Methode.
     * @return Gibt die ID des Kurses zurueck.
     * **/
    public String getId() {
        return COURSE_ID;
    }

    /** Getter-Methode.
     * @return Gibt den Namen des Kurses zurueck.
     * **/
    public String getNAME() {
        return NAME;
    }

    /** Getter-Methode.
     * @return Gibt die ID der unterrichtenden Lehrkraft zurueck.
     * **/
    public String getTeacherId() {
        return TEACHER_ID;
    }

    /** Getter-Methode.
     * @return Gibt die Beschreibung des Kurses zurueck.
     * **/
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Abbildung eines Kurses in einem String mit All seinen Werten.
     * **/
    @Override
    public String toString() {
        return "Course{" +
                "COURSE_ID='" + COURSE_ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", TEACHER_ID='" + TEACHER_ID + '\'' +
                ", DESCRIPTION='" + DESCRIPTION + '\'' +
                '}';
    }
}
