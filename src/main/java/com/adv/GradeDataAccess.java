package com.adv;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * GradeDataAccess ist verantwortlich fuer alle Datenbankzugriffsaufgaben, die mit der Typklasse Grade, StudentGradeDetail,
 * StudentCourseViewData und CourseGradeDetail zu tun haben.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class GradeDataAccess {

    private final Database db = new Database();

    /**
     * Erstellt eine Note in der Datenbank.
     * @param grade Das Grade-Objekt, welches als Datensatz in der Datenbank hinzugefuegt werden muss.
     * @return true -> Erstellung erfolgreich; false -> Erstellung fehlgeschlagen
     * **/
    public boolean createGrade(Grade grade) {
        String sql = "INSERT INTO grades (student_id, course_id, grade_value, grade_description, grade_type, entered_by) "+
                "VALUES (?::uuid, ?::uuid, ?, ?, ?::grade_type, ?::uuid)";

        try (Connection conn = db.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, grade.getStudentId());
            preparedStatement.setString(2, grade.getCourseId());
            preparedStatement.setFloat(3, grade.getGradeValue());
            preparedStatement.setString(4, grade.getGradeDescription());
            preparedStatement.setString(5, grade.getGradeType());
            preparedStatement.setString(6, grade.getEnteredBy());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            System.err.println("Error creating grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Holt alle Noten, die ein Schueler in einem Kurs hat.
     * @param studentId - Die Datenbank ID (In Table grades Fremdschluessel) des Schuelers
     * @param courseId - Die Datenbank ID (In Table grades Fremdschluessel) des Kurses, welchen der Schueler besucht
     * @return Gibt eine vollstaendige ArrayList mit StudentGradeDetail-Objekten zurueck. Also eine ArrayList mit Noten, die der Schueler in dem einen Kurs hat.
     **/
    public ArrayList<StudentGradeDetail> getGradesForStudentInCourse(String studentId, String courseId) {
        ArrayList<StudentGradeDetail> gradeDetails = new ArrayList<>();
        // Hol mir (...) aus der Kombination von grades und courses und Fülle sie auf mit Einträgen von gtw für den Schüler in dem Kurs
        String sql = "SELECT " +
                "g.grade_id, g.course_id, c.course_name, g.grade_value, g.grade_description, g.grade_type, gtw.weight, g.created_at " +
                "FROM grades AS g " +
                // JOIN - Die normalen Tabellenverknüpfungen die wir auch gemacht haben.
                "JOIN courses AS c " +
                "ON c.course_id = g.course_id " +
                // LEFT JOIN - Ergänzende Verknüpfung
                // Bsp: "Ergänze zu der Tabelle (courses, grades) überall dort gtw, wo die Kurs-Id passt
                // -> Wenn es kein gtw Eintrag gibt der Passt, geht die ganze Reihe NICHT weg, sondern dort wird bei weight einfach null stehen.
                "LEFT JOIN grade_type_weight AS gtw " +
                "ON gtw.course_id = g.course_id " +
                "AND gtw.grade_type = g.grade_type " +
                "WHERE g.student_id = ?::uuid " +
                "AND g.course_id = ?::uuid";

        try (Connection conn = db.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    gradeDetails.add(mapRowToStudentGradeDetail(resultSet));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting gradeDetails for student in course: " + e.getMessage());
        }
        return gradeDetails;
    }

    /**
     * Holt alle Noten (inkl. Gewichtungen, Notentyp, Kursname, usw) , die ein Schueler insgesamt hat.
     * @param studentId - Die Datenbank ID des Schuelers
     * @return Gibt eine vollstaendige ArrayList mit StudentGradeDetail-Objekten zurueck. Also eine ArrayList mit Noten, die der Schueler hat.
     **/
    public ArrayList<StudentGradeDetail> getAllGradesAndWeightsForStudent(String studentId) {
        ArrayList<StudentGradeDetail> gradeDetails = new ArrayList<>();
        // Hol mir (...) aus der Kombination von enrollments und courses und fülle sie auf mit Einträgen von grades und gtw
        // für den Schüler und sortiere sie nach den Namen der Kurse aufsteigend.
        String sql = "SELECT " +
                "g.grade_id, c.course_id, c.course_name, g.grade_value, g.grade_description, g.grade_type, gtw.weight, g.created_at " +
                "FROM enrollments AS e " +
                "JOIN courses AS c ON c.course_id = e.course_id " +
                "LEFT JOIN grades AS g ON g.student_id = e.student_id AND g.course_id = e.course_id " +
                "LEFT JOIN grade_type_weight AS gtw ON gtw.course_id = c.course_id AND gtw.grade_type = g.grade_type " +
                "WHERE e.student_id = ?::uuid " +
                "ORDER BY c.course_name ASC";

        try (Connection conn = db.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    gradeDetails.add(mapRowToStudentGradeDetail(resultSet));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting all grade gradeDetails for student: " + e.getMessage());
        }

        return gradeDetails;
    }

    /**
     * Holt alle Noten (inkl. Gewichtungen, Notentyp, Kursname, usw) , die ein Kurs insgesamt hat.
     * @param courseId - Die Datenbank ID des Kurses
     * @return Gibt eine vollstaendige ArrayList mit CourseGradeDetail-Objekten zurueck. Also eine ArrayList mit Noten, die der Kurs hat.
     **/
    public ArrayList<CourseGradeDetail> getAllGradesAndWeightsForCourse(String courseId) {
        ArrayList<CourseGradeDetail> gradeDetails = new ArrayList<>();
        // Hol mir für den Kurs xy (...) von der Kombination von enrollments, users, und courses und fülle die daraus ergebnde Tabelle mit Einträgen aus grades und gtw
        String sql = "SELECT" +
                " u.user_id, c.course_name, g.grade_value, g.grade_description, g.grade_type, gtw.weight, g.created_at " +
                "FROM enrollments AS e " +
                "JOIN users AS u ON u.user_id = e.student_id " +
                "JOIN courses AS c ON c.course_id = e.course_id " +
                "LEFT JOIN grades AS g ON g.student_id = e.student_id AND g.course_id = e.course_id " +
                "LEFT JOIN grade_type_weight AS gtw ON gtw.course_id = e.course_id AND gtw.grade_type = g.grade_type " +
                "WHERE e.course_id = ?::uuid";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    gradeDetails.add(mapRowToCourseGradeDetail(resultSet));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting all grade gradeDetails for course: " + e.getMessage());
        }
        return gradeDetails;
    }

    /**
     * Holt alle Noten, Gewichtungen, Kursname und Lehrername fuer die Kursansicht eines Schuelers
     * @param studentId  Die Datenbank ID des Schuelers
     * @param courseId Die Datenbank ID des Kurses
     * @return Gibt ein StudentCourseViewData Objekt zurueck.
     **/
    public StudentCourseViewData getStudentCourseViewData(String studentId, String courseId) {
        String courseName = "";
        String teacherLastName = "";
        HashMap<String, Float> weights = new HashMap<>();


        // Abfrage 1: Kursname, Lehrername, Notentypen und Gewichtung des Kurses holen
        String sql = "SELECT c.course_name, u.last_name, gtw.grade_type, gtw.weight " +
                "FROM courses c " +
                "JOIN users u ON c.teacher_id = u.user_id " +
                "LEFT JOIN grade_type_weight gtw ON c.course_id = gtw.course_id " +
                "WHERE c.course_id = ?::uuid";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    courseName = resultSet.getString("course_name");
                    teacherLastName = resultSet.getString("last_name");
                    String type = resultSet.getString("grade_type");
                    if (type != null) weights.put(type, resultSet.getFloat("weight"));
                }
            }

        } catch(SQLException e) {
            System.err.println("Error getting course view data: " + e.getMessage());
        }

        // Abfrage 2: Alle Noten des Schülers holen
        ArrayList<StudentGradeDetail> grades = getGradesForStudentInCourse(studentId, courseId);
        return new StudentCourseViewData(courseName, teacherLastName, weights, grades);
    }

    /**
     * Holt alle Gewichtungen und Notentypen eines Kurses.
     * @param courseId - Die Datenbank ID des Kurses.
     * @return Gibt eine HashMap mit den Notentypen und Gewichtungen eines Kurses zurueck.
     **/
    public HashMap<String, Float> getWeightsForCourse(String courseId) {
        HashMap<String, Float> weights = new HashMap<>();

        String sql = "SELECT grade_type, weight FROM grade_type_weight WHERE course_id = ?::uuid";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    weights.put(resultSet.getString("grade_type"), resultSet.getFloat("weight"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting weights: " + e.getMessage());
        }
        return weights;
    }

    /**
     * Setzt Gewichtung fuer einen Notentypen.
     * @param courseId Die Datenbank ID des Kurses, in welchem die Gewichtungen geaendert werden muessen.
     * @param gradeType Der Notentyp, zu welchem die Gewichtung geaendert werden muss.
     * @param weight Die neue Gewichtung.
     * @return true → Einfuegen/Editieren erfolgreich; false -> Nicht erfolgreich
     **/
    public boolean setWeightForGradeType(String courseId, String gradeType, float weight) {
        // Füge diese neue Gewichtung ein, und wenn es die schon gibt, dann aktualisiere es auf diesen Wert:
        String sql = "INSERT INTO grade_type_weight (course_id, grade_type, weight) VALUES (?::uuid, ?::grade_type, ?) " +
                "ON CONFLICT (course_id, grade_type) DO UPDATE SET weight = EXCLUDED.weight";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, gradeType);
            preparedStatement.setFloat(3, weight);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Couldn't set Weights for Course: " + e.getMessage());
            return false;
        }
    }


    /**
     * Loescht die Gewichtung zu einem Notentyp in einem Kurs.
     * @param courseId Die Datenbank ID des Kurses.
     * @param gradeType Der Notentyp, zu dem die Gewichtung geloescht werden muss.
     * @return true -> Loeschung erfolgreich; false -> Loeschung fehlgeschlagen
     **/
    public boolean deleteWeightForGradeType(String courseId, String gradeType) {
        String sql = "DELETE FROM grade_type_weight WHERE course_id = ?::uuid AND grade_type = ?::grade_type";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, gradeType);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting weight: " + e.getMessage());
        }
        return false;
    }

    /**
     * Loescht die Note.
     * @param gradeId Die Datenbank ID der Note.
     * @return true -> Loeschung erfolgreich; false -> Loeschung fehlgeschlagen
     **/
    public boolean deleteGrade(String gradeId) {
        String sql = "DELETE FROM grades WHERE grade_id = ?::uuid";
        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, gradeId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loescht die Noten zu einem Notentyp in einem Kurs.
     * @param courseId Die Datenbank ID des Kurses.
     * @param gradeType Der Notentyp, zu dem die Gewichtung geloescht werden muss.
     * @return true -> Loeschung erfolgreich; false -> Loeschung fehlgeschlagen
     **/
    public boolean deleteAllGradesForTypeInCourse(String courseId, String gradeType) {
        String sql = "DELETE FROM grades WHERE course_id = ?::uuid AND grade_type = ?::grade_type";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, gradeType);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting All Grades For Course with GradeType: " + e.getMessage());
            return false;
        }
    }

    /**
     * Prueft, ob ein Kurs zu einem bestimmten Notentyp Noten eingetragen hat.
     * @param courseId Die Datenbank ID des Kurses.
     * @param gradeType Der Notentyp, zu dem geprueft werden muss.
     * @return true -> Hat Noten; false -> Keine Noten
     **/
    public boolean courseHasGradesInType (String courseId, String gradeType) {
        String sql = "SELECT COUNT(grade_id) FROM grades WHERE course_id = ?::uuid AND grade_type = ?::grade_type";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, gradeType);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error counting all Grades of Course in gradeType: " + e.getMessage());
        }
        return false;
    }

    /**
     * Ordnet jedem Datensatz aus resultSet in ein neues CourseGradeDetail-Objekt zu
     * **/
    private CourseGradeDetail mapRowToCourseGradeDetail(ResultSet resultSet) throws SQLException {
        Float gradeValue;
        Float weight;

        if (resultSet.getObject("grade_value") != null) {
            gradeValue = resultSet.getFloat("grade_value");
        } else {
            gradeValue = null;
        }

        if (resultSet.getObject("weight") != null) {
            weight = resultSet.getFloat("weight");
        } else {
            weight = null;
        }

        return new CourseGradeDetail(
                resultSet.getString("course_name"),
                resultSet.getString("user_id"),
                gradeValue,
                resultSet.getString("grade_description"),
                resultSet.getString("grade_type"),
                weight,
                resultSet.getTimestamp("created_at")
        );
    }

    /**
     * Ordnet jedem Datensatz aus resultSet in ein neues StudentGradeDetail-Objekt zu
     * **/
    private StudentGradeDetail mapRowToStudentGradeDetail(ResultSet resultSet) throws SQLException {
        Float gradeValue;
        Float weight;

        if (resultSet.getObject("grade_value") != null) {
            gradeValue = resultSet.getFloat("grade_value");
        } else {
            gradeValue = null;
        }

        if (resultSet.getObject("weight") != null) {
            weight = resultSet.getFloat("weight");
        } else {
            weight = null;
        }


        return new StudentGradeDetail(
                resultSet.getString("grade_id"),
                resultSet.getString("course_id"),
                resultSet.getString("course_name"),
                gradeValue,
                resultSet.getString("grade_description"),
                resultSet.getString("grade_type"),
                weight,
                resultSet.getTimestamp("created_at")
        );
    }



}
