package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Das Panel fuer die Detailansicht eines Kurses fuer einen Schueler.
 * Zeigt Kursinformationen, den Lehrer, den aktuellen Notendurchschnitt, die Gewichtungen und eine Liste aller Noten.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class StudentCoursePanel extends CommonJPanel implements ActionListener {

    private final App mainApp;
    private final GradeDataAccess gradeDataAccess;
    private final GradeCalc gradeCalc;

    private User student;
    private String currentCourseId;

    private final JPanel contentPanel;

    /**
     * Konstruktor fuer das StudentCoursePanel.
     * Baut die UI-Elemente auf.
     * @param mainApp Referenz auf das Hauptfenster.
     */
    public StudentCoursePanel(App mainApp) {
        this.mainApp = mainApp;
        this.gradeDataAccess = new GradeDataAccess();
        this.gradeCalc = new GradeCalc();

        setLayout(new BorderLayout());

        // Scrollbarer Bereich für Kursinhalt
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        //Zurück Button
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    /**
     * Laedt die Daten fuer einen Kurs und einen Studenten und zeigt sie an.
     * @param student Der eingeloggte Student.
     * @param courseId Die ID des anzuzeigenden Kurses.
     */
    public void loadCourseData(User student, String courseId) {
        this.student = student;
        this.currentCourseId = courseId;
        contentPanel.removeAll();

        // 1. Daten holen (Neue Klasse 'StudentCourseViewData' für Laufzeitoptimierung
        StudentCourseViewData data = gradeDataAccess.getStudentCourseViewData(student.getId(), courseId);

        float average = gradeCalc.getStudentCourseAvg(courseId, data.getGrades());

        // 2. Header (Kursname, Schnitt, Lehrer)
        JLabel courseNameLabel = new JLabel(data.getCourseName());
        courseNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        courseNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(courseNameLabel);

        contentPanel.add(Box.createVerticalStrut(10));

        String avgText = Float.isNaN(average)? "N/A" : String.format("%.1f", average);
        JLabel averageLabel = new JLabel("Durchschnitt: " + avgText + "   ");
        averageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        averageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(averageLabel);

        contentPanel.add(Box.createVerticalStrut(20));

        JLabel teacherLabel = new JLabel("Lehrkraft - " + data.getTeacherLastName());
        teacherLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        teacherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(teacherLabel);

        // 3. Gewichtungen anzeigen
        addSectionHeader("Gewichtung");
        if(data.getWeights().isEmpty()) {
            addInfoLabel("Keine Gewichtung definiert.");
        } else {
            for (HashMap.Entry<String, Float> entry : data.getWeights().entrySet()) {
                addInfoLabel("- " + entry.getKey() + " : " + entry.getValue() + "%");
            }
        }

        contentPanel.add(Box.createVerticalStrut(30));

        // 4. Notenübersicht anzeigen
        addSectionHeader("Notenübersicht:");

        ArrayList<StudentGradeDetail> grades = data.getGrades();
        if (grades.isEmpty()) {
            addInfoLabel("Keine Noten eingetragen.");

        } else {
            // Für jeden Notentyp
            for (String type : data.getWeights().keySet()) {
                ArrayList<StudentGradeDetail> typeGrades = new ArrayList<>();
                // Alle Noten zu diesem Notentyp raussuchen
                for (StudentGradeDetail g: grades) {
                    if (type.equals(g.getGradeType())) {
                        typeGrades.add(g);
                    }
                }

                // Wenn es Noten zu diesem Notentyp gibt...
                if  (!typeGrades.isEmpty()) {
                    contentPanel.add(Box.createVerticalStrut(15));

                    // -- Notentypüberschrift --
                    JLabel typeLabel = new JLabel(type + ":");
                    typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    contentPanel.add(typeLabel);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

                    // Alle Noten als Label unter dem jeweiligen Notentyp listen
                    for (StudentGradeDetail gradeDetail : typeGrades) {
                        String dateString = (gradeDetail.getCreatedAt() != null)? simpleDateFormat.format(gradeDetail.getCreatedAt()) : "";
                        String gradeText = String.format("%s - %s - %s",
                                gradeDetail.getGradeValue(),
                                gradeDetail.getGradeDescription(),
                                dateString);

                        JLabel gradeLabel = new JLabel(gradeText);
                        gradeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        contentPanel.add(gradeLabel);
                    }
                }
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();

    }

    /**
     * Laedt die Seite neu, um neue Noten etc. zu beruecksichtigen und hinzufuegen
     * **/
    @Override
    public void refreshData() {
        if (this.student != null && this.currentCourseId != null) {
            loadCourseData(this.student, this.currentCourseId);
        }
    }

    /**
     * Behandelt Klicks auf Buttons im Panel.
     * @param e Das ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainApp.showPanel(App.STUDENT_DASHBOARD_PANEL);
    }

    /**
     * Hilfsmethode, Erzeugt ein Titel Label mit fettgedruckter Schrift.
     * @param text Der Text, der auf dem JLabel stehen soll.
     * **/
    private void addSectionHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(5));
    }

    /**
     * Hilfsmethode, Erzeugt ein normales Label.
     * @param text Der Text, der auf dem JLabel stehen soll.
     * **/
    private void addInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(label);
    }
}
