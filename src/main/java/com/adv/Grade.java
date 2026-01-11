package com.adv;

/** Noten-Klasse. Ein Objekt der Klasse entspricht einer Reihe der Tabelle grades in der Datenbank.
 * Hat alle Details zu einer Note.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public class Grade {

    private final String GRADE_ID;
    private final String STUDENT_ID;
    private final String COURSE_ID;
    private final float GRADE_VALUE;
    private final String GRADE_DESCRIPTION;
    private final String GRADE_TYPE;
    private final String ENTERED_BY;

    /**Konstruktor mit gradeId (falls aus der Datenbank kommt)**/
    public Grade(String gradeId,
                 String studentId,
                 String courseId,
                 float gradeValue,
                 String gradeDescription,
                 String gradeType,
                 String enteredBy) {

        this.GRADE_ID = gradeId;
        this.STUDENT_ID = studentId;
        this.COURSE_ID = courseId;
        this.GRADE_VALUE = gradeValue;
        this.GRADE_DESCRIPTION = gradeDescription;
        this.GRADE_TYPE = gradeType;
        this.ENTERED_BY = enteredBy;
    }

    /**Konstruktor ohne gradeId (falls in die Datenbank hereinmuss) **/
    public Grade(String studentId,
                 String courseId,
                 float gradeValue,
                 String gradeDescription,
                 String gradeType,
                 String enteredBy) {

        this(null, studentId, courseId, gradeValue, gradeDescription, gradeType, enteredBy);
    }

    /** Getter-Methode.
     * @return Gibt die ID der Note zurueck.
     * **/
    public String getGradeId() {
        return this.GRADE_ID;
    }

    /** Getter-Methode.
     * @return Gibt die ID des Schuelers zurueck.
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

    /** Getter-Methode.
     * @return Gibt den Wert der Note zurueck.
     * **/
    public float getGradeValue() {
        return this.GRADE_VALUE;
    }

    /** Getter-Methode.
     * @return Gibt die Beschreibung der Note zurueck.
     * **/
    public String getGradeDescription() {
        return this.GRADE_DESCRIPTION;
    }

    /** Getter-Methode.
     * @return Gibt den Notentyp zurueck.
     * **/
    public String getGradeType() {
        return this.GRADE_TYPE;
    }

    /** Getter-Methode.
     * @return Gibt die ID des Lehrers zurueck, welcher die Note eingetragen hat.
     * **/
    public String getEnteredBy() {
        return this.ENTERED_BY;
    }

    /**
     * Abbildung einer Note in einem String mit All seinen Werten.
     * **/
    @Override
    public String toString() {
        return "Grade{" +
                "GRADE_ID='" + GRADE_ID + '\'' +
                ", STUDENT_ID='" + STUDENT_ID + '\'' +
                ", COURSE_ID='" + COURSE_ID + '\'' +
                ", GRADE_VALUE=" + GRADE_VALUE +
                ", GRADE_DESCRIPTION='" + GRADE_DESCRIPTION + '\'' +
                ", GRADE_TYPE='" + GRADE_TYPE + '\'' +
                ", ENTERED_BY='" + ENTERED_BY + '\'' +
                '}';
    }
}
