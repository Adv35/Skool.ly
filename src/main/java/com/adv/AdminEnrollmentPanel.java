package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Das Panel, welches auftaucht, wenn ein Admin den Button im AdminDashboard klickt, um einen Schueler einzuschreiben
 *
 * @author Advik Vattamwar
 * @version 10.01.2026
 *
 **/
public class AdminEnrollmentPanel extends CommonJPanel implements ActionListener {

    // Das Hauptobjekt / Steuerobjekt von App.java
    private App mainApp;

    // Backend Zugriff
    private EnrollmentDataAccess enrollmentDataAccess;
    private CourseDataAccess courseDataAccess;
    private UserDataAccess userDataAccess;

    // Swing Elemente
    private JComboBox<User> studentComboBox;
    private JComboBox<Course> courseComboBox;
    private JButton saveButton;
    private JButton backButton;

    /**
     *  Konstruktor des Panels.
     *  Baut das Formular mit seinem TextFeldern, Buttons etc.
     * @param mainApp - Das Hauptpanel
     * **/
    public AdminEnrollmentPanel(App mainApp) {
        this.mainApp = mainApp;
        this.enrollmentDataAccess = new EnrollmentDataAccess();
        this.courseDataAccess = new CourseDataAccess();
        this.userDataAccess = new UserDataAccess();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Titel ---
        JLabel titleLabel = new JLabel("Schüler einschreiben", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // --- Formular Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Felder initialisieren ---
        studentComboBox = new JComboBox<>();
        studentComboBox.setRenderer(new UserComboBoxRenderer()); // Wiederverwenden des Renderers, dass für AdminCoursePanel gemacht wurde

        courseComboBox = new JComboBox<>();
        courseComboBox.setRenderer(new CourseComboBoxRenderer()); // Neue Renderklasse für diese ComboBox


        // --- Felder zum Panel hinzufügen ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel studentUsernameLabel = new JLabel("Schüler auswählen");
        formPanel.add(studentUsernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(studentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel courseNameLabel = new JLabel("Kurs auswählen");
        formPanel.add(courseNameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(courseComboBox, gbc);

        //Speichern - Button
        gbc.gridy = 2;
        saveButton = new JButton("Speichern");
        saveButton.addActionListener(this);
        formPanel.add(saveButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Zurück - Button
        backButton = new JButton("Zurück");
        backButton.addActionListener(this);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(backButton);
        add(southPanel, BorderLayout.SOUTH);

        refreshData();

    }

    /** Methode wird von CommonJPanel geerbt.
     * Ladet alle Daten neu, die aktualisiert geworden sein koennten (in der Datenbank)
     * **/
    @Override
    public void refreshData() {
        loadData();
        resetForm();
    }

    /** Methode implementiert von dem Interface Actionlistener.
     * Handling von Backend Einschreibung und Zurueckgehen zum Dashboard des Admins.
     * @param e Das ActionEvent, das die Buttons zum ActionListener geben.
     * **/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Daten aus den Swing Elementen holen
            User selectedStudent = (User) studentComboBox.getSelectedItem();
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();

            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(mainApp, "Bitte einen Schüler und einen Kurs auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Enrollment enrollment = new Enrollment(selectedStudent.getId(), selectedCourse.getId());
                // Prüfen, ob die Einschreibung schon existiert.
                if (enrollmentDataAccess.checkEnrollment(selectedStudent.getId(), selectedCourse.getId())) {
                    JOptionPane.showMessageDialog(mainApp, "Der Schüler ist schon in dem Kurs eingeschrieben", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } // Wenn nicht, dann kann man die Einschreibung machen.
                else if (enrollmentDataAccess.enrollStudent(enrollment)) {
                    JOptionPane.showMessageDialog(mainApp, String.format("%s wurde erfolgreich in den Kurs %s eingeschrieben", selectedStudent.getFirstName() + " " + selectedStudent.getLastName(), selectedCourse.getNAME()));
                } else {
                    JOptionPane.showMessageDialog(mainApp, "Fehler beim Einschreiben des Schülers in den Kurs.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }

                // Felder und ComboBox-Inhalt erneuern
                refreshData();

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(mainApp, "Fehler beim Einschreiben des Schülers in den Kurs.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == backButton) {
            mainApp.showPanel(App.ADMIN_DASHBOARD_PANEL);
        }
    }

    /**
     * Setzt die Formularinhalte wieder zurueck/leer
     * **/
    private void resetForm() {
        studentComboBox.setSelectedIndex(-1);
        courseComboBox.setSelectedIndex(-1);
    }

    /**
     * Die ComboBoxen werden aktualisiert (wichtig, falls z.B. in der Zwischenzeit ein neuer Lehrer/Schueler erstellt wurde)
     * **/
    private void loadData() {
        // Alle Schüler in die ComboBox
        studentComboBox.removeAllItems();
        ArrayList<User> students = userDataAccess.findUsersByRole("student");
        for (User student : students) {
            studentComboBox.addItem(student);
        }
        // Alle Kurse in die ComboBox
        courseComboBox.removeAllItems();
        ArrayList<Course> courses = courseDataAccess.findAllCourses();
        for (Course course : courses) {
            courseComboBox.addItem(course);
        }
    }
}
