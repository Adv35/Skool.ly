package com.adv;

/** Enrollment - Klasse. Ein Objekt der Klasse entspricht einer Reihe der Tabelle enrollments in der Datenbank.
 * Hat alle Details zu einer Einschreibung eines Schuelers in einem Kurs (Die Relation, die man braucht, um eine n:n - Beziehung aufzuloesen).
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/

public class Enrollment {
    private final String ENROLLMENT_ID;
    private final String STUDENT_ID;
    private final String COURSE_ID;

    /**
     * Konstruktor mit Enrollment-ID (falls aus der Datenbank kommt)
     * **/
    public Enrollment(String enrollmentId, String studentId, String courseId) {
        this.ENROLLMENT_ID = enrollmentId;
        this.STUDENT_ID = studentId;
        this.COURSE_ID = courseId;
    }

    /**
     * Konstruktor ohne Enrollment-ID (falls in die Datenbank eingetragen werden muss)
     * **/
    public Enrollment(String studentId, String courseId) {
        this(null, studentId, courseId);
    }

    /** Getter-Methode.
     * @return Gibt die ID der Einschreibung zurueck.
     * **/
    public String getEnrollmentId() {
        return this.ENROLLMENT_ID;
    }

    /** Getter-Methode.
     * @return Gibt die ID des eingeschriebenen Schuelers zurueck.
     * **/
    public String getStudentId() {
        return this.STUDENT_ID;
    }

    /** Getter-Methode.
     * @return Gibt die ID des Kurses zurueck.
     * **/
    public String getCourseId() {
        return this.COURSE_ID;
    }

    /**
     * Abbildung einer Einschreibung in einem String mit All seinen Werten.
     * **/
    @Override
    public String toString() {
        return "Enrollment{" +
                "ENROLLMENT_ID='" + ENROLLMENT_ID + '\'' +
                ", STUDENT_ID='" + STUDENT_ID + '\'' +
                ", COURSE_ID='" + COURSE_ID + '\'' +
                '}';
    }


}
