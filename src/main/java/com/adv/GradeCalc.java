package com.adv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Klasse zur Berechnung von Durchschnitten aus den Noten. Verwendet Grade, StudentGradeDetail, CourseGradeDetail...
 * Gewichtungen sind in Prozent.
 * Noten sind in Punkten.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public class GradeCalc {
    private GradeDataAccess gradeDataAccess;

    public GradeCalc() {
        this.gradeDataAccess = new GradeDataAccess();
    }


    /**
     * Die Methode berechnet die Durchschnittsnote eines Schuelers in einem Kurs.
     * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
     *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
     *          - Wenn min. ein Notentyp (e.g. schriftlich, muendlich etc.) keine Noten eingetragen hat, werden die restlichen Gewichtungen der Notentypen auf ihre Anteile angepasst.
     *              → Kann zu verfaelschten Durchschnittsnoten fuehren, aber besser, als einfach nur ausrechnen zu lassen, wenn alle Noten vorhanden sind.
     *
     * @param studentId - Die Nutzer-ID des Schuelers aus der Datenbank
     * @param courseId - Die Kurs-ID aus der Datenbank
     *
     * @return Gibt die Durchschnittsnote eines Schuelers in einem Kurs zurueck. ODER Float.NaN, wenn keine Noten vorhanden sind.
     * **/
    public float getStudentCourseAvg(String studentId, String courseId) {
        if (studentId != null && courseId != null) {
            return calculateStudentCourseAvg(studentId, courseId);
        }

        return Float.NaN;
    }

    public float getStudentCourseAvg(String courseId, ArrayList<StudentGradeDetail> allDetails) {
        if (courseId != null && !allDetails.isEmpty()) {
            return calculateStudentCourseAvg(courseId, allDetails);
        }
        return Float.NaN;
    }



    /**
     * Die Methode berechnet die Durchschnittsnote eines Schuelers auf alle seine Kurse zusammen.
     * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
     *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
     *          - Alle Kurse haben dieselbe Gewichtung (1). LK-BK System gibt es noch nicht.
     *
     * @param studentId - Die Nutzer-ID des Schuelers aus der Datenbank
     * @return Gibt die insgesamte Durchschnittsnote eines Schuelers zurueck. ODER Float.NaN, wenn keine Noten vorhanden sind (durch allDetails oder dadurch, dass alle Kurse Float.NaN zurueckgegeben haben).
     * **/
    public float getOverallStudentAvg(String studentId) {
        if(studentId != null) {
            return calculateOverallStudentAvg(studentId);
        }
        return Float.NaN;
    }




    /**
     * Die Methode berechnet die Durchschnittsnote eines gesamten Kurses.
     * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
     *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
     *          - Wenn ein Schuelerschnitt NaN liefert, wird dieser nicht beruecksichtigt (fliegt in der Rechnung raus)
     *          - Alle Schueler haben dieselbe Gewichtung (1).
     *
     * @param courseId - Die Kurs-ID des Schuelers aus der Datenbank
     * @return Gibt die insgesamte Durchschnittsnote eines Kurses zurueck. ODER NaN, wenn keine Noten vorhanden sind.
     * **/
    public float getCourseAvg(String courseId) {
        if (courseId != null) {
            return calculateCourseAvg(courseId);
        }
        return Float.NaN;
    }


    /**
     * Die Methode berechnet die Durchschnittsnote eines gesamten Kurses.
     * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
     *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
     *          - Wenn ein Schuelerschnitt NaN liefert, wird dieser nicht beruecksichtigt (fliegt in der Rechnung raus)
     *          - Alle Schueler haben dieselbe Gewichtung (1).
     *
     * @param courseId - Die Kurs-ID des Schuelers aus der Datenbank
     * @return Gibt die insgesamte Durchschnittsnote eines Kurses zurueck. ODER NaN, wenn keine Noten vorhanden sind.
     * **/
    private float calculateCourseAvg(String courseId) {
        // Alle Noten, deren Notentypen und Gewichtungen von allen Schülern eines Kurses
       ArrayList<CourseGradeDetail> allDetails = gradeDataAccess.getAllGradesAndWeightsForCourse(courseId);

       if (allDetails.isEmpty()) {
           return Float.NaN;
       }

        // Geht die ArrayList durch, holt sich alle Schüler-IDs und speichert alle Unterschiedlichen in die Liste ab.
        // List Datentyp → Für die Methoden
       List<String> uniqueStudentIDs = allDetails.stream()
               .map(CourseGradeDetail::getStudentId)
               .distinct()
               .toList();

       // Summe aller Schüler-Schnitte
       float gradeSum = 0.0f;
       // Anzahl der Schüler
       float numberOfStudents = uniqueStudentIDs.size();


       // Für jeden Kursteilnehmer den Schnitt im Kurs berechnen und in gradeSum aufaddieren
       for (String studentId : uniqueStudentIDs) {
           ArrayList<StudentGradeDetail> gradeDetailsOfOneStudent = new ArrayList<>();

           for (CourseGradeDetail detail : allDetails) {
               if (detail.getStudentId().equals(studentId)) {
                   gradeDetailsOfOneStudent.add(detail.toStudentGradeDetail(courseId));
               }
           }

           gradeSum += calculateStudentCourseAvg(courseId, gradeDetailsOfOneStudent);

       }

       // numberOfStudents immer ungleich 0, weil bei gleich 0 es schon oben NaN zurückgegeben hätte
       return gradeSum / numberOfStudents;



    }



    /**
     * Die Methode berechnet die Durchschnittsnote eines Schuelers auf alle seine Kurse zusammen.
     * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
     *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
     *          - Alle Kurse haben dieselbe Gewichtung (1). LK-BK System gibt es noch nicht.
     *
     * @param studentId - Die Nutzer-ID des Schuelers aus der Datenbank
     * @return Gibt die insgesamte Durchschnittsnote eines Schuelers zurueck. ODER Float.NaN, wenn keine Noten vorhanden sind (durch allDetails oder dadurch, dass alle Kurse Float.NaN zurueckgegeben haben).
     * **/

    private float calculateOverallStudentAvg(String studentId) {
        // Liste mit allen Kursen, den Noten und der Gewichtung eines Schülers
        ArrayList<StudentGradeDetail> allDetails = gradeDataAccess.getAllGradesAndWeightsForStudent(studentId);

        if (allDetails.isEmpty()) {
            return Float.NaN;
        }


        // Geht die ArrayList durch, holt sich alle KursIDs und speichert alle Unterschiedlichen in die Liste ab.
        // List Datentyp → Für die Methoden
        List<String> uniqueCourseIDs = allDetails.stream()
                .map(StudentGradeDetail::getCourseId)
                .distinct()
                .toList();

        float totalAverageSum = 0.0f;
        int gradedCoursesCounter = 0;

        for (String courseId : uniqueCourseIDs) {
            // Für jeden Kurs den Durchschnitt berechnen lassen
            float courseAverage = calculateStudentCourseAvg(courseId, allDetails);

            if (!Float.isNaN(courseAverage)) {
                // Den Kursdurchschnitt zur Gesamtschnittsumme hinzufügen
                totalAverageSum += courseAverage;
                gradedCoursesCounter++;
            }
        }

        if (gradedCoursesCounter > 0) {
            // Alle Kursschnitte zu einem insgesamten Schnitt berechnen.
            return (totalAverageSum / gradedCoursesCounter);
        } else {
            return Float.NaN;
        }

    }

    /**
     * --- ANWENDUNG DER UEBERLADUNG VON METHODEN ---
     * Die Methode berechnet die Durchschnittsnote eines Schuelers in einem Kurs.
     * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
     *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
     *          - Wenn min. ein Notentyp (e.g. schriftlich, muendlich etc.) keine Noten eingetragen hat, werden die restlichen Gewichtungen der Notentypen auf ihre Anteile angepasst.
     *              → Kann zu verfaelschten Durchschnittsnoten fuehren, aber besser, als einfach nur ausrechnen zu lassen, wenn alle Noten vorhanden sind.
     *
     * @param studentId - Die Nutzer-ID des Schuelers aus der Datenbank
     * @param courseId - Die Kurs-ID aus der Datenbank
     *
     * @return Gibt die Durchschnittsnote eines Schuelers in einem Kurs zurueck. ODER Float.NaN, wenn keine Noten vorhanden sind.
     * **/

    private float calculateStudentCourseAvg(String studentId, String courseId) {
        ArrayList<StudentGradeDetail> studentCourseDetails = gradeDataAccess.getGradesForStudentInCourse(studentId, courseId);
        return calculateStudentCourseAvg(courseId, studentCourseDetails);
    }



/**
 * Die Methode berechnet die Durchschnittsnote eines Schuelers in einem Kurs.
 * Wichtig: - Gewichtung in Prozent. Summe aller Gewichtungen muessen 100.0 ergeben.
 *          - Note in Punkten. Skala kann selber bestimmt werden. Buchstaben, wie (A-F) sind nicht moeglich.
 *          - Wenn min. ein Notentyp (e.g. schfritlich, muendlich etc.) keine Noten eingetragen hat, werden die restlichen Gewichtungen der Notentypen auf ihre Anteile angepasst.
 *              → Kann zu verfaelschten Durchschnittsnoten fuehren, aber besser, als einfach nur ausrechnen zu lassen, wenn alle Noten vorhanden sind.
 *
 * @param courseId - Die Kurs-ID aus der Datenbank
 * @param allDetails - 1) Liste mit allen Kursen, den Noten und der Gewichtung eines Schuelers
 *                   - 2) Liste eines Kurses und Schuelers, mit allen Noten und deren Gewichtung dabei.
 *
 * @return Gibt die Durchschnittsnote eines Schuelers in einem Kurs zurueck. ODER Float.NaN, wenn keine Noten vorhanden sind.
 * **/

    private float calculateStudentCourseAvg(String courseId, ArrayList<StudentGradeDetail> allDetails) {
        ArrayList<String> relevantGradeTypes = new ArrayList<>();
        HashMap<String, Float> originalWeights = new HashMap<>();

        // Alle relevanten GradeTypes (schriftlich, mündlich etc.) für den Kurs speichern
        // Relevante GradeTypes sind die, dessen Kurs zu unsrem Kurs gehören und auch existieren.

        for (StudentGradeDetail detail : allDetails) {
            if (detail.getCourseId().equals(courseId)
                    && detail.getGradeType() != null
                    && !relevantGradeTypes.contains(detail.getGradeType())) {

                relevantGradeTypes.add(detail.getGradeType());

                //Notengewicht speichern, um später nach Notenverfügbarkeit zu prüfen
                if (detail.getWeight() != null) {
                    originalWeights.put(detail.getGradeType(), detail.getWeight());
                }
            }
        }

        // Nur GradeTypes mit Noten einsammeln

        // Hier kommen alle GradeTypes rein, die min. eine Note bei sich haben
        ArrayList<String> validGradeTypes = new ArrayList<>();

        // Benötigt, um später die Gewichte neu zu berechnen, falls min. 1 GradeType keine Noten zugewiesen hat.
        // Speichert, wie viel Gewicht noch insgesamt validiert da ist.
        float totalOriginalWeight = 0.0f;

        for (String gradeType : relevantGradeTypes) {
            boolean hasGrade = false;
            for (StudentGradeDetail detail : allDetails) {
                if (detail.getCourseId().equals(courseId) &&
                        detail.getGradeType().equals(gradeType) &&
                        detail.getGradeValue() != null) {
                    hasGrade = true;
                    break;
                }
            }
            if (hasGrade) {
                validGradeTypes.add(gradeType);

                // getOrDefault - Sucht nach Wert "bei gradeType" und wenn es das nicht gibt, wird 0.0 geliefert.
                // Kann passieren, wenn das Gewicht eines GradeTypes == null ist.
                // → Fehlervermeidung, denn float + null = ERROR
                totalOriginalWeight += originalWeights.getOrDefault(gradeType, 0.0f);
            }
        }

        // Keine Noten im Kurs -> NaN
        if (validGradeTypes.isEmpty()) return Float.NaN;

        // Notendurchschnitt berechnen & ggf. Gewicht anpassen
        float finalCourseGrade = 0.0f;

        for (String gradeType : validGradeTypes) {

            // Summe der vergebenen Noten für diesen gradeType
            float sumOfGradesForType = 0.0f;
            // Anzahl der Noten in dem GradeType
            int countOfGradesForType = 0;


            // Alle Noten des GradeTypes holen
            for (StudentGradeDetail detail : allDetails) {
                if (detail.getCourseId().equals(courseId)
                        && detail.getGradeType().equals(gradeType)
                        && detail.getGradeValue() != null) {

                    sumOfGradesForType += detail.getGradeValue();
                    countOfGradesForType++;
                }
            }

            // Berechnung des Typ-Durchschnitts
            if (countOfGradesForType > 0) {
                float averageForType = sumOfGradesForType / countOfGradesForType;
                float originalWeight = originalWeights.getOrDefault(gradeType, 0.0f);

                // Anpassung des Gewichtes, falls min. 1 Typ ausfällt
                float adjustedWeight = (totalOriginalWeight > 0) ? originalWeight / totalOriginalWeight : 0.0f;  // originalWeight = adjustedWeight, wenn totalOriginalWeight == 100 (Also hat jeder Typ min. 1 Note vergeben), daher kein if nötig

                // Durchschnittsgewichtsnote der finalen Note hinzufügen
                finalCourseGrade += averageForType * adjustedWeight;
            }
        }

        return finalCourseGrade;
    }
}
