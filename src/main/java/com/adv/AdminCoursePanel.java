package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Das JPanel, in welchem das Formular zur Erstellung eines neuen Kurses ist. Nur fuer Administratoren vorgesehen.
 * Gehoert zur GUI.
 * @author Advik Vattamwar
 * @version 05.01.2026
 **/
public class AdminCoursePanel extends CommonJPanel implements ActionListener {

    // Das Hauptobjekt / Steuerobjekt von App.java
    private App mainApp;

    private CourseDataAccess courseDataAccess;
    private UserDataAccess userDataAccess;

    // Formular - Komponenten
    private JTextField courseNameField;
    private JTextArea descriptionArea;
    private JComboBox<User> teacherComboBox;
    private JButton saveButton;
    private JButton backButton;

    /**
     *  Konstruktor des Panels.
     *  Baut das Formular mit seinem TextFeldern, Buttons etc.
     * @param mainApp - Das Hauptpanel
     * **/
    public AdminCoursePanel(App mainApp) {
        this.mainApp = mainApp;
        this.courseDataAccess = new CourseDataAccess();
        this.userDataAccess = new UserDataAccess();

        // BorderLayout: https://www.geeksforgeeks.org/java/java-awt-borderlayout-class/
        setLayout(new BorderLayout());
        // EmptyBorder: https://codingtechroom.com/question/understanding-emptyborder-in-java-swing
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Titel ---
        JLabel titleLabel = new JLabel("Neuen Kurs erstellen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // --- Formular Panel ---
            // GridBagLayout: https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        JPanel formPanel = new JPanel(new GridBagLayout());

            // Liefert Bedingungen, wo die Elemente sein sollen
        GridBagConstraints gbc = new GridBagConstraints();

            // Alle Elemente haben einen Abstand von 5 zueinander
        gbc.insets = new Insets(5, 5, 5, 5);

            // Bei vorhandenem Platz in horizontaler Richtung strecken
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Felder initialisieren
        this.courseNameField = new JTextField(20);
        this.descriptionArea = new JTextArea(5, 20);
        this.descriptionArea.setPreferredSize(descriptionArea.getPreferredSize());
        this.teacherComboBox = new JComboBox<>();
        this.teacherComboBox.setRenderer(new UserComboBoxRenderer());

        // Felder zum Panel hinzufügen
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel courseNameLabel = new JLabel("Kursname:");
        formPanel.add(courseNameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(courseNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Beschreibung:");
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(descriptionArea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel teacherSelectLabel = new JLabel("Lehrkraft:");
        formPanel.add(teacherSelectLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(teacherComboBox, gbc);

        gbc.gridy = 3;
        saveButton = new JButton("Speichern");
        saveButton.addActionListener(this);
        formPanel.add(saveButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Zurück-Button ---
        backButton = new JButton("Zurück");
        backButton.addActionListener(this);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(backButton);
        add(southPanel, BorderLayout.SOUTH);

        refreshData();
    }

    /**
     * Methode wird von CommonJPanel geerbt.
     * Leert alle Elemente des Formulars und aktualisiert die ComboBox (falls neue Lehrer dazugekommen sind)
     * **/
    @Override
    public void refreshData() {
        teacherComboBox.removeAllItems();
        ArrayList<User> teachers = userDataAccess.findUsersByRole("teacher");
        for (User teacher : teachers) {
            teacherComboBox.addItem(teacher);
        }
        resetForm();
    }

    /** Methode implementiert von dem Interface Actionlistener.
     * Handling von Backend Kurserstellung und Zurueckgehen zum Dashboard der Admin.
     * @param e Das ActionEvent, das die Buttons zum ActionListener geben.
     * **/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Die Daten aus den Elementen holen
            String courseName = courseNameField.getText();
            String description = descriptionArea.getText();
            User selectedTeacher = (User) teacherComboBox.getSelectedItem();

            // if-Bedingung - vermeidet Error in der Datenbank
            if (courseName.isEmpty() || selectedTeacher == null) {
                JOptionPane.showMessageDialog(mainApp, "Bitte Kursname und Lehrer auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Course newCourse = new Course(courseName, selectedTeacher.getId(), description);

            // Neuen Kurs erstellen
            if (courseDataAccess.createCourse(newCourse)) {
                JOptionPane.showMessageDialog(mainApp, String.format("Kurs %s erfolgreich erstellt", courseName));
                resetForm();
            } else {
                JOptionPane.showMessageDialog(mainApp, "Fehler beim Erstellen des Kurses. \n Möglicherweise gibt es schon einen Kurs mit demselben Namen?", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            resetForm();
            mainApp.showPanel(App.ADMIN_DASHBOARD_PANEL);
        }
    }

    /**
     * Leert das Formular.
     * **/
    private void resetForm() {
        courseNameField.setText(null);
        descriptionArea.setText(null);
        teacherComboBox.setSelectedIndex(-1);
    }


}
