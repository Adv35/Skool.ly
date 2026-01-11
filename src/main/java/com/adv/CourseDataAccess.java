package com.adv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * CourseDataAccess ist verantwortlich fuer alle Datenbankzugriffsaufgaben, die mit der Typklasse Course zu tun haben.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class CourseDataAccess {

    private final Database db = new Database();

    /**
     * Erstellt einen Kurs in der Datenbank.
     * @param course Das Kurs-Objekt, dessen Werte in der Datenbank gespeichert werden.
     *               → courseName, teacherId, description
     *
     * @return 1.) true - Die Erstellung des Eintrags war erfolgreich
     *         2.) false - Die Erstellung des Eintrags ist fehlgeschlagen
     * **/
    public boolean createCourse(Course course) {
        String sql = "INSERT INTO courses (course_name, teacher_id, description) VALUES (?, ?::uuid, ?)";

        try(Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, course.getNAME());
            preparedStatement.setString(2, course.getTeacherId());
            preparedStatement.setString(3, course.getDescription());


            // Wenn min. 1 Zeile hinzugefügt wurde, dann wird true zurückgegeben
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating course: " + e.getMessage());
        }
        return false;
    }

    // Every course of a teacher
    /**
     * Sucht alle Kurse in der Datenbank zusammen, die von einem Lehrer unterrichtet werden.
     * @param teacherId Die ID der Lehrkraft
     * @return Eine ArrayList mit allen Kurse, die die Lehrkraft unterrichtet.
     * **/
    public ArrayList<Course> findCoursesByTeacherId(String teacherId) {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE teacher_id = ?::uuid";

        try (Connection conn = db.connect();
              PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, teacherId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                // Solange es noch Einträge gibt...
                while (resultSet.next()) {
                    // mapRowToCourse - Macht aus einem Datensatz einen Kurs-Objekt
                    courses.add(mapRowToCourse(resultSet));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding course by teacher: " + e.getMessage());
        }
    return courses;
    }

    /** Sucht nach einem Kurs mithilfe der KursID
     * @param courseId Die KursID mit der gesucht wird.
     * @return Den gefundenen Kurs. Wenn keiner gefunden wird, dann wird null zurueckgegeben.
     * **/
    public Course findCourseById(String courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?::uuid";

        try(Connection conn = db.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) { // "if-clause" weil es nur einen Datensatz geben wird
                    return mapRowToCourse(resultSet);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding course by ID: " + e.getMessage());
        }
        return null;
    }

    /** Sucht nach allen Kursen, die von einem Schueler belegt werden.
     * @param studentId Die ID des Schuelers.
     * @return Eine ArrayList mit allen Kursen, in denen der Schueler eingeschrieben ist.
     * **/
    public ArrayList<Course> findCoursesByStudentId(String studentId) {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses, enrollments " +
                "WHERE courses.course_id = enrollments.course_id " +
                "AND enrollments.student_id = ?::uuid";

        try(Connection conn = db.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, studentId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(mapRowToCourse(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding courses by student ID: " + e.getMessage());
        }
        return courses;
    }

    /**
     * Gibt alle Kurse zurueck.
     * @return Eine ArrayList mit allen Kursen.
     * **/
    public ArrayList<Course> findAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_name ASC";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                courses.add(mapRowToCourse(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching all courses from Database: " + e.getMessage());
        }
        return courses;
    }

    /**
     * Sucht nach einem Kurs nach dem Kursnamen.
     * @param courseName Der Name des Kurses
     * @return Der Kurs mit dem Namen ODER falls nicht vorhanden null
     * **/
    public Course findCourseByName(String courseName) {
        String sql = "SELECT * FROM courses WHERE course_name = ?";

        try (Connection conn = db.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapRowToCourse(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching course from courseName: " + e.getMessage());
        }
        return null;
    }

    /**
     * Ordnet jedem Datensatz aus resultSet in ein neues Kurs-Objekt zu
     * **/
    private Course mapRowToCourse(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getString("course_id"),
                resultSet.getString("course_name"),
                resultSet.getString("teacher_id"),
                resultSet.getString("description")
        );
    }

}
