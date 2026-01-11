package com.adv;

import java.sql.Timestamp;

/**
 * Datenklasse, repraesentiert eine Note in einem Kurs zu einem Schueler mit der Gewichtung, die die Note hat und welchem Typ diese Note gehoert.
 * Die Klasse wurde im Rahmen einer Optimierung der Laufzeit implementiert und wird zurzeit nur zur Berechnung der Durchschnittsnote eines
 * Kurses genutzt.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/

public class CourseGradeDetail {

    private final String COURSE_NAME;
    private final String STUDENT_ID;
    private final Float GRADE_VALUE; // Nutzen der Wrapper - Klasse, um NULL auch verarbeiten zu können
    private final String GRADE_DESCRIPTION;
    private final String GRADE_TYPE;
    private final Float WEIGHT;
    private final Timestamp CREATED_AT;

    public CourseGradeDetail(String courseName,
                             String studentId,
                              Float gradeValue,
                              String gradeDescription,
                              String gradeType,
                              Float weight,
                              Timestamp createdAt) {

        this.COURSE_NAME = courseName;
        this.STUDENT_ID = studentId;
        this.GRADE_VALUE = gradeValue;
        this.GRADE_DESCRIPTION = gradeDescription;
        this.GRADE_TYPE = gradeType;
        this.WEIGHT = weight;
        this.CREATED_AT = createdAt;
    }

    /** Getter-Methode.
     * @return Gibt den Namen des Kurses zurueck.
     * **/
    public String getCourseName() {
        return COURSE_NAME;
    }

    /** Getter-Methode.
     * @return Gibt die ID des Schuelers zurueck.
     * **/
    public String getStudentId() {
        return STUDENT_ID;
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
     * @return Gibt die Gewichtung der Note/Notentyp zurueck.
     * **/
    public Float getWeight() {
        return WEIGHT;
    }

    /** Getter-Methode.
     * @return Gibt den Zeitpunkt zurueck, an dem die Note erstellt wurde.
     * **/
    public Timestamp getCreatedAt() {
        return CREATED_AT;
    }

    /**
     * Konvertiert das Objekt in ein Objekt der Klasse StudentGradeDetail
     * @param courseId Die ID des Kurses, worin die Note erzielt wurde.
     * @return Ein Objekt der Klasse StudentGradeDetail.
     * **/
    public StudentGradeDetail toStudentGradeDetail(String courseId) {
        return new StudentGradeDetail(
                null, // Quick Fix. Die Klasse wird momentan nur dafür verwendet, die Durchschnittsnoten eines Kurses zu berechnen, sodass die Konversion mit dem null nichts ausmacht.
                // Wenn die Klasse später weitere Funktionen bekommen soll, ist es schlau, dieses null zu ersetzen.
                // Ich hab mich dagegen entschieden, weil ich nicht noch mehr Last auf die Datenbank machen will
                //→ Denn das ist ja wieder mehr lag.
                courseId,
                this.COURSE_NAME,
                this.GRADE_VALUE,
                this.GRADE_DESCRIPTION,
                this.GRADE_TYPE,
                this.WEIGHT,
                this.CREATED_AT
        );
    }


    /**
     * Abbildung des Objekts mit den wichtigsten Werten als String.
     * **/
    @Override
    public String toString() {
        return "CourseGradeDetail{" +
                "GRADE_VALUE=" + GRADE_VALUE +
                ", GRADE_TYPE='" + GRADE_TYPE + '\'' +
                ", WEIGHT=" + WEIGHT +
                "}";
    }
}
