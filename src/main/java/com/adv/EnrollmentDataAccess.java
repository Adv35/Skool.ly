package com.adv;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * EnrollmentDataAccess ist verantwortlich fuer alle Datenbankzugriffsaufgaben, die mit der Typklasse Enrollemnt zu tun haben.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class EnrollmentDataAccess {
    private final Database db = new Database();

    /**
     * Fuegt in der Datenbank eine Einschreibung eines Schuelers in einen Kurs hinzu.
     * @param enrollment Die zu einfuegende Einschreibung
     * @return true -> Gelungen; false -> Nicht Gelungen
     * **/
    public boolean enrollStudent(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?::uuid, ?::uuid)";

        try (Connection conn = db.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, enrollment.getStudentId());
            preparedStatement.setString(2, enrollment.getCourseId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sucht alle Einschreibungen in der Datenbank zusammen, die zu einem Kurs sind.
     * @param courseId Die ID des Kurses
     * @return Eine ArrayList mit allen Einschreibungen, die zu dem Kurs sind.
     * **/
    public ArrayList<Enrollment> getEnrollmentsByCourseId(String courseId) {
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE course_id = ?::uuid";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    enrollments.add(mapRowToEnrollment(resultSet));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching Enrollments by CourseID: " + e.getMessage());
        }
        return enrollments;
    }

    /**
     * Fuegt in der Datenbank eine Einschreibung eines Schuelers in einen Kurs hinzu.
     * @param studentId Der eingeschriebene Schueler.
     * @param courseId Der Kurs in den eingeschrieben wurde.
     * @return true -> Eingeschrieben; false -> Nicht Eingeschrieben
     * **/
    public boolean checkEnrollment(String studentId, String courseId) throws RuntimeException {
        String sql = "SELECT enrollment_id FROM enrollments WHERE student_id = ?::uuid AND course_id = ?::uuid";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, courseId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Prüfen der Einschreibung", e);
        }
    }

    /**
     * Ordnet jedem Datensatz aus resultSet in ein neues Enrollment-Objekt zu
     * **/
    private Enrollment mapRowToEnrollment(ResultSet resultSet) throws SQLException {
        return new Enrollment(
                resultSet.getString("enrollment_id"),
                resultSet.getString("student_id"),
                resultSet.getString("course_id")
        );

    }



}
