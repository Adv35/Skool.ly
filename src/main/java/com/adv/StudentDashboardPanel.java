package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Das Dashboard-Panel fuer Studenten.
 * Das ist die Hauptansicht nach dem Login eines Schuelers.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class StudentDashboardPanel extends CommonJPanel implements ActionListener {

    //Hauptframe
    private App mainApp;

    // Swing Komponenten
    private JLabel welcomeLabel;
    private JLabel overallAvgLabel;
    private JPanel coursesPanel;

    // Backend Nutzung:
    private GradeCalc gradeCalc;
    private CourseDataAccess courseDataAccess;

    private User student;

    /**
     * Konstruktor fuer das StudentDashboardPanel.
     * Baut die UI-Komponenten
     * @param mainApp Referenz auf das Hauptfenster.
     */
    public StudentDashboardPanel(App mainApp) {
        this.mainApp = mainApp;
        this.gradeCalc = new GradeCalc();
        this.courseDataAccess = new CourseDataAccess();

        setLayout(new BorderLayout(0, 50));
        setBorder(new EmptyBorder(20, 80, 20, 20));

        // --- Wilkommensnachricht ---
        JPanel topPanel = new JPanel(new GridBagLayout());

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        welcomeLabel = new JLabel("Willkommen!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelsPanel.add(welcomeLabel);

        // --- Notendurchschnitt ---
        overallAvgLabel = new JLabel("Gesamtdurchschnitt:       ");
        overallAvgLabel.setFont(new Font("Helvetica", Font.ITALIC, 16));
        overallAvgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelsPanel.add(overallAvgLabel);

        topPanel.add(labelsPanel);
        add(topPanel, BorderLayout.NORTH);


        // --- Panel für Kurse --
        coursesPanel = new JPanel();

        // coursesPanel in ein ScrollPane, damit scrollen geht
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBorder(null); // Default-Border entfernen
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }


    public void loadStudentData(User student) {
        this.student = student;

        welcomeLabel.setText("Willkommen, " + student.getFirstName() + " " + student.getLastName() + "!");

        // --- Gesamtdurchschnitt berechnen und anzeigen --
        float overallAvg = gradeCalc.getOverallStudentAvg(student.getId());
        if (!Float.isNaN(overallAvg)) {

            overallAvgLabel.setText(String.format("Gesamtdurchschnitt: %.1f ", overallAvg));
        } else {
            overallAvgLabel.setText("Gesamtdurchschnitt: N/A");
        }

        // --- Kursboxen machen ---
        coursesPanel.removeAll();// Alles im Panel resetten
        ArrayList<Course> courses = courseDataAccess.findCoursesByStudentId(student.getId());

        if (courses.isEmpty()) {
            // Falls es noch keine Kurse gibt, soll folgendes Label dort stehen
            coursesPanel.setLayout(new BorderLayout());
            JLabel noCoursesLabel = new JLabel("Noch in keine Kurse eingeschrieben.", SwingConstants.CENTER);
            noCoursesLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            coursesPanel.add(noCoursesLabel, BorderLayout.CENTER);

        } else {
            coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

            // Ermitteln der benötigten Reihen für die Kurskacheln
            int rows = (int) Math.ceil(courses.size() / 3.0); // 3.0, damit ein Float entsteht (braucht die Methode)

            // Array von Panels, wo die KursButtons reinkommen
            JPanel[] rowsPanels = new JPanel[rows];

            //Initialiseren des Arrays
            for (int i = 0; i < rows; i++) {
                rowsPanels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
                rowsPanels[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
                coursesPanel.add(rowsPanels[i]);
            }

            // Ein Button für jeden Kurs, um damit auf die KursPanels zu kommen
            for (int i = 0; i < courses.size(); i++) {
                JButton courseButton = new JButton("<html><center>" + courses.get(i).getName() + "</center></html>");
                courseButton.setActionCommand(courses.get(i).getId());
                courseButton.addActionListener(this);
                courseButton.setPreferredSize(new Dimension(200, 150));

                // 3 Kacheln in einer Reihe
                rowsPanels[i / 3].add(courseButton);
            }
        }

        coursesPanel.revalidate();
        coursesPanel.repaint();

    }

    /**
     * Aktualisiert die Daten des aktuellen Studenten (bei z.B. Datenbankaenderungen).
     */
    @Override
    public void refreshData() {
        if (this.student != null) {
            loadStudentData(student);
        }
    }

    /**
     * Behandelt Klicks auf die Kurs-Kacheln.
     * @param e Das ActionEvent des Buttons.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Auf die KursPanel wechseln
        String courseId = e.getActionCommand();
        mainApp.getStudentCoursePanel().loadCourseData(student, courseId);
        mainApp.showPanel(App.STUDENT_COURSE_PANEL);
    }
}
