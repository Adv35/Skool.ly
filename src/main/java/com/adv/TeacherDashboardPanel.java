package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Dashboard-Panel fuer Lehrkraefte.
 * Hauptansicht nach dem Login eines Lehrers.
 * @author Advik Vattamwar
 * @version 10.01.2026
 */
public class TeacherDashboardPanel extends CommonJPanel implements ActionListener {
    private App mainApp;
    private JLabel welcomeLabel;
    private JPanel coursesPanel;
    private CourseDataAccess courseDataAccess;
    private User teacher;


    /**
     * Konstruktor fuer das TeacherDashboardPanel.
     * Erstellt die UI-Komponenten, Layouts und Backend-Instanzen.
     * @param mainApp Referenz auf das Hauptfenster.
     */
    public TeacherDashboardPanel(App mainApp) {
        this.mainApp = mainApp;
        this.courseDataAccess = new CourseDataAccess();

        setLayout(new BorderLayout(0, 50));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //--- Kopfzeile ---
        welcomeLabel = new JLabel("Willkommen!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        // --- Kurs-Bereich ---
        coursesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));

        // --- scrollbarer Bereich ---
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }


    /**
     * Laedt die Daten fuer den gegebenen Lehrer und aktualisiert das Panel.
     * Erstellt Buttons fuer alle Kurse, die der Lehrer unterrichtet.
     * @param teacher Der eingeloggte Lehrer.
     */
    public void loadTeacherData(User teacher) {
        this.teacher = teacher;
        welcomeLabel.setText("Willkommen, " + teacher.getFirstName() + " " + teacher.getLastName() + "!");

        coursesPanel.removeAll();

        ArrayList<Course> courses = courseDataAccess.findCoursesByTeacherId(teacher.getId());

        if (courses.isEmpty()) {
            // Label dass sagt, dass es keine Kurse gibt
            JLabel noCoursesLabel = new JLabel("Keine Kurse zugewiesen.");
            noCoursesLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            coursesPanel.add(noCoursesLabel);
        } else {
            // Für jeden Kurs des Lehrers einen Button erstellen
            for (Course course : courses) {
                JButton courseButton = new JButton("<html><center>" + course.getNAME() + "</center></html>");
                courseButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                courseButton.setActionCommand(course.getId());
                courseButton.addActionListener(this);
                courseButton.setPreferredSize(new Dimension(200, 150));

                JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonWrapper.add(courseButton);

                coursesPanel.add(buttonWrapper);

            }
        }
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    /**
     * Aktualisiert die Daten des aktuellen Lehrers.
     */
    @Override
    public void refreshData() {
        if (this.teacher != null) {
            loadTeacherData(this.teacher);
        }
    }

    /**
     * Behandelt Klicks auf die Kurs-Buttons.
     * @param e Das ActionEvent des Buttons.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Panel auf die Kursseite des Lehrers wechseln
        String courseId = e.getActionCommand();
        mainApp.getTeacherCoursePanel().loadCourseData(courseId, this.teacher);
        mainApp.showPanel(App.TEACHER_COURSE_PANEL);

    }
}
