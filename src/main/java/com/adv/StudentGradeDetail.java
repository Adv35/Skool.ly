package com.adv;

import java.sql.Timestamp;

/**
 * Objekt fuer eine einzelne Note eines Studenten.
 * Diese Klasse speichert alle Infos zu einer Note, inklusive Infos zum Kurs und die Gewichtung.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class StudentGradeDetail {
    private final String GRADE_ID;
    private final String COURSE_ID;
    private final String COURSE_NAME;
    private final Float GRADE_VALUE; // Nutzen der Wrapper - Klasse, um NULL auch verarbeiten zu koennen
    private final String GRADE_DESCRIPTION;
    private final String GRADE_TYPE;
    private final Float WEIGHT;
    private final Timestamp CREATED_AT;

    /**Konstruktor von StudentGradeDetail**/
    public StudentGradeDetail(String gradeId,
                              String courseId,
                              String courseName,
                              Float gradeValue,
                              String gradeDescription,
                              String gradeType,
                              Float weight,
                              Timestamp createdAt) {
        this.GRADE_ID = gradeId;
        this.COURSE_ID = courseId;
        this.COURSE_NAME = courseName;
        this.GRADE_VALUE = gradeValue;
        this.GRADE_DESCRIPTION = gradeDescription;
        this.GRADE_TYPE = gradeType;
        this.WEIGHT = weight;
        this.CREATED_AT = createdAt;
    }

    /** Getter-Methode.
     * @return Gibt die ID der Note zurueck.
     * **/
    public String getGradeId() {
        return GRADE_ID;
    }

    /** Getter-Methode.
     * @return Gibt die ID des Kurses zurueck.
     * **/
    public String getCourseId() {
        return COURSE_ID;
    }

    /** Getter-Methode.
     * @return Gibt den Namen des Kurses zurueck.
     * **/
    public String getCourseName() {
        return COURSE_NAME;
    }

    /** Getter-Methode.
     * @return Gibt den Wert der Note zurueck.
     * **/
    public Float getGradeValue() {
        return GRADE_VALUE;
    }

    /** Getter-Methode.
     * @return Gibt die Beschreibung der Note zurueck.
     * **/
    public String getGradeDescription() {
        return GRADE_DESCRIPTION;
    }

    /** Getter-Methode.
     * @return Gibt den Notentyp zurueck.
     * **/
    public String getGradeType() {
        return GRADE_TYPE;
    }

    /** Getter-Methode.
     * @return Gibt die Gewichtung der Note zurueck.
     * **/
    public Float getWeight() {
        return WEIGHT;
    }

    /** Getter-Methode.
     * @return Gibt den Timestamp vom Erstellzeitpunkt zurueck.
     * **/
    public Timestamp getCreatedAt() {
        return CREATED_AT;
    }

    /**
     * Abbildung des StudentGradeDetail-Objekts mit allen Werten als String.
     * **/
    @Override
    public String toString() {
        return "StudentGradeDetail{" +
                "GRADE_ID='" + GRADE_ID + '\'' +
                ", COURSE_ID='" + COURSE_ID + '\'' +
                ", COURSE_NAME='" + COURSE_NAME + '\'' +
                ", GRADE_VALUE=" + GRADE_VALUE +
                ", GRADE_DESCRIPTION='" + GRADE_DESCRIPTION + '\'' +
                ", GRADE_TYPE='" + GRADE_TYPE + '\'' +
                ", WEIGHT=" + WEIGHT +
                ", CREATED_AT=" + CREATED_AT +
                '}';
    }
}
