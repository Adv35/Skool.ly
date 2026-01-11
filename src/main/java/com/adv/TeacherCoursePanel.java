package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Das Panel fuer die Kursansicht fuer eine Lehrkraft.
 * Hier kann der Lehrer Kurseinstellungen vornehmen (Gewichtungen verwalten) und
 * sieht eine Liste der eingeschriebenen Schueler.
 * Von hier aus kommt man auch zur Notenvergabe fuer einzelne Schueler.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class TeacherCoursePanel extends CommonJPanel implements ActionListener {
    private App mainApp;
    private CourseDataAccess courseDataAccess;
    private EnrollmentDataAccess enrollmentDataAccess;
    private UserDataAccess userDataAccess;
    private GradeDataAccess gradeDataAccess;
    private GradeCalc gradeCalc;

    private String currentCourseId;
    private User student;
    private User teacher;

    private JPanel contentPanel;
    private JButton backButton;
    private JButton editWeightsButton;
    private JButton studentButton;

    /**
     * Konstruktor fuer das TeacherCoursePanel.
     * Erstellt das Layout, das Backend und die UI-Komponenten.
     * @param mainApp Hauptfenster.
     */
    public TeacherCoursePanel(App mainApp) {
        this.mainApp = mainApp;
        this.courseDataAccess = new CourseDataAccess();
        this.enrollmentDataAccess = new EnrollmentDataAccess();
        this.userDataAccess = new UserDataAccess();
        this.gradeDataAccess = new GradeDataAccess();
        this.gradeCalc = new GradeCalc();

        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- scrollbarer Bereich ---
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // --- Zurück-Button ---
        backButton = new JButton("Zurück");
        backButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Laedt die Daten fuer einen spezifischen Kurs und zeigt sie an.
     * @param courseId Die ID des Kurses.
     * @param teacher Der eingeloggte Lehrer.
     */
    public void loadCourseData(String courseId, User teacher) {
        this.teacher = teacher;
        this.currentCourseId = courseId;
        contentPanel.removeAll();

        Course course = courseDataAccess.findCourseById(courseId);

        // --- Kopfzeile ---
        JLabel courseNameLabel = new JLabel(course.getNAME());
        courseNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        courseNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(courseNameLabel);

        JLabel courseAverageLabel = new JLabel();
        float courseAvgGrade = gradeCalc.getCourseAvg(courseId);
        courseAverageLabel.setText(String.format("Kursdurchschnitt - %.1f", courseAvgGrade));
        courseAverageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        courseAverageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(courseAverageLabel);

        contentPanel.add(Box.createVerticalStrut(30));

        // --- Gewichtung ---
        addSectionHeader("Gewichtung");
        HashMap<String, Float> weights = gradeDataAccess.getWeightsForCourse(courseId);
        if (weights.isEmpty()) {
            addInfoLabel("Keine Gewichtungen definiert.");

        } else {

            for (HashMap.Entry<String, Float> entry : weights.entrySet()) {
                // Gewichte und Notentypen auflisten
                JPanel weightRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                JLabel weightLabel = new JLabel(entry.getKey() + ": " + entry.getValue() + "%");
                weightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                // Button, um Gewicht & Notentyp zu löschen
                JButton deleteWeightButton = new JButton("X");
                deleteWeightButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                deleteWeightButton.setForeground(Color.RED);
                deleteWeightButton.setBorderPainted(false);
                deleteWeightButton.setContentAreaFilled(false);
                deleteWeightButton.setFocusPainted(false);

                deleteWeightButton.addActionListener(e -> {
                    deleteWeight(entry.getKey());
                });

                weightRow.add(weightLabel);
                weightRow.add(deleteWeightButton);
                //Die Höhe darf nicht zu groß sein
                // Integer.MAX_VALUE -> Länge ist uns egal
                weightRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, weightRow.getPreferredSize().height));
                contentPanel.add(weightRow);
            }
        }

        // --- Gewichte hinzufügen/ändern - Button ---
        editWeightsButton = new JButton("Gewichtung hinzufügen/ändern");
        editWeightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editWeightsButton.addActionListener(this);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(editWeightsButton);
        contentPanel.add(Box.createVerticalStrut(10));

        // --- Schülerliste ---
        addSectionHeader("Schülerliste");
        ArrayList<Enrollment> enrollments = enrollmentDataAccess.getEnrollmentsByCourseId(courseId);
        if (enrollments.isEmpty()) {
            addInfoLabel("Keine Schüler eingeschrieben.");
        } else {
            // Alle Einschreibungen in den Kurs durchgehen
            for (Enrollment enrollment : enrollments) {
                student = userDataAccess.findUserById(enrollment.getStudentId());
                if (student != null) {
                    // Für jeden Schüler einen Button für das GradingPanel
                    studentButton = new JButton(student.getFirstName() + " " + student.getLastName());
                    studentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    studentButton.setMaximumSize(new Dimension(300, 40));
                    studentButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                    studentButton.addActionListener(this);

                    contentPanel.add(studentButton);
                    contentPanel.add(Box.createVerticalStrut(10));
                }
            }
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Zeigt einen Formular in einem Dialog an, um Gewichtungen fuer Notentypen hinzuzufuegen oder zu aendern.
     * @param courseId Die ID des Kurses, fuer den die Gewichtung geaendert werden soll.
     */
    private void showEditWeightDialog(String courseId) {
        JComboBox<String> typeComboBox;
        JTextField weightField = new JTextField();

        // Gehardcodete GradeTypes. In der Datenbank als ENUM.
        String[] possibleTypes = new String[] {"Schriftlich", "Mündlich", "Fachpraktisch", "Test"};
        typeComboBox = new JComboBox<>(possibleTypes);

        JLabel typeLabel = new JLabel("Typ:");

        // Anordnung der Elemente
        Object[] message = {
                typeLabel, typeComboBox,
                "Gewichtung (in Prozent): " , weightField
        };

        // Dialog mit dem Formular anzeigen
        int option = JOptionPane.showConfirmDialog(this,
                message,
                "Gewichtung setzen",
                JOptionPane.OK_CANCEL_OPTION);

        // Wenn "OK" geklickt wurde
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Daten aus Komponenten holen
                String type = (String) typeComboBox.getSelectedItem();
                float weight = Math.abs(Float.parseFloat(weightField.getText()));

                //Gewichtung ändern
                gradeDataAccess.setWeightForGradeType(courseId, type, weight);
                loadCourseData(courseId, this.teacher);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Bitte geben Sie eine gültige Zahl im Bereich 0 - 100 ein.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Ein Fehler ist aufgetreten. Bitte zeigen Sie das ihrem Support: " + e.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Aktualisiert die Ansicht des aktuellen Kurses (z.B. nach Aenderungen an den Gewichten).
     */
    @Override
    public void refreshData() {
        if (this.currentCourseId != null && this.teacher != null) {
            loadCourseData(this.currentCourseId, this.teacher);
        }
    }

    /**
     * Behandelt Klicks auf Buttons (Zurueck, Gewichtung aendern, Schueler auswaehlen etc).
     * @param e Das ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            mainApp.showPanel(App.TEACHER_DASHBOARD_PANEL);
        } else if (e.getSource() == editWeightsButton) {
            showEditWeightDialog(currentCourseId);
        } else if (e.getSource() == studentButton) {
            mainApp.getTeacherGradingPanel().loadGradingData(currentCourseId, student, this.teacher);
            mainApp.showPanel(App.TEACHER_GRADING_PANEL);
        }
    }

    /**
     * Hilfsmethode, Erzeugt ein Titel Label mit fettgedruckter Schrift.
     * @param text Der Text, der auf dem JLabel stehen soll.
     * **/
    private void addSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(10));
    }

    /**
     * Hilfsmethode, Erzeugt ein normales Label.
     * @param text Der Text, der auf dem JLabel stehen soll.
     * **/
    private void addInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(label);
    }

    /**
     * Methode, die aufgerufen wird, wenn ein deleteWeightButton geklickt wird.
     * Loescht die Gewichtung und den Notentyp im Kurs.
     * Wenn dadurch auch andere Noten geloescht werden muessen, wird die Lehrkraft gewarnt und ggf. die Noten geloescht.
     * @param gradeType Der Notentyp der Note
     * **/
    private void deleteWeight(String gradeType) {
        if (gradeDataAccess.courseHasGradesInType(currentCourseId, gradeType)) {
            int confirmDeleteOption = JOptionPane.showConfirmDialog(this,
                    "Wenn Sie diese Gewichtung löschen, werden alle Noten dazu gelöscht! \n" +
                            "Wollen Sie wirklich fortfahren?", "Warnung!", JOptionPane.OK_CANCEL_OPTION);

            if (confirmDeleteOption == JOptionPane.CANCEL_OPTION) {
                return;

            } else if (confirmDeleteOption == JOptionPane.OK_OPTION) {

                if (!gradeDataAccess.deleteAllGradesForTypeInCourse(currentCourseId, gradeType)) {
                    JOptionPane.showMessageDialog(this, "Das Löschen der Noten ist fehlgeschlagen. Probieren Sie es später noch einmal.");
                    return;
                }
            }
        }
        if (gradeDataAccess.deleteWeightForGradeType(currentCourseId, gradeType)) {
            loadCourseData(currentCourseId, this.teacher);
        }

    }
}
