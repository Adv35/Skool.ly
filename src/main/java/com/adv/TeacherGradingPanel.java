package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Das Panel fuers Vergeben von Noten durch eine Lehrkraft.
 * @author Advik Vattamwar
 * @version 03.01.2026
 */
public class TeacherGradingPanel extends CommonJPanel implements ActionListener {
    private App mainApp;

    // Backend-Zugriff
    private GradeDataAccess gradeDataAccess;
    private CourseDataAccess courseDataAccess;
    private GradeCalc gradeCalc;

    private String currentCourseId;
    private User currentStudent;
    private User currentTeacher;

    // Swing Komponenten
    private JPanel contentPanel;
    private JButton backButton;
    private JButton addGradeButton;
    private JTextField gradeValueField;
    private JComboBox<String> gradeTypeComboBox;
    private JTextField gradeDescriptionField;

    /**
     * Konstruktor fuer das TeacherGradingPanel.
     * Initialisiert das Layout, die Backend-Instanzen und die UI-Komponenten.
     * @param mainApp Referenz auf das Hauptfenster.
     */
    public TeacherGradingPanel(App mainApp) {
        this.mainApp = mainApp;
        this.gradeDataAccess = new GradeDataAccess();
        this.courseDataAccess = new CourseDataAccess();
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

        // --- Zurück - Button ---
        backButton = new JButton("Zurück");
        backButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Laedt die Notendaten fuer einen Schueler in einem Kurs.
     * Zeigt den aktuellen Schnitt, ein Formular zum Hinzufuegen von Noten und eine Liste der bisherigen Noten an.
     * @param courseId Die ID des Kurses.
     * @param student Der Schueler, der benotet werden soll.
     * @param teacher Der Lehrer, der die Benotung macht.
     */
    public void loadGradingData(String courseId, User student, User teacher) {
        this.currentCourseId = courseId;
        this.currentTeacher = teacher;
        this.currentStudent = student;

        contentPanel.removeAll();

        Course course = courseDataAccess.findCourseById(courseId);

        // --- Kopfzeile ---
        JLabel titleLabel = new JLabel("Notenvergabe: " + student.getFirstName() + " " + student.getLastName());
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        JLabel courseLabel = new JLabel("Kurs: " + course.getNAME());
        courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(courseLabel);

        contentPanel.add(Box.createVerticalStrut(10));

        // Durchschnitt
        float average = gradeCalc.getStudentCourseAvg(student.getId(), courseId);
        String averageText = Float.isNaN(average)? "N/A" : String.format("%.1f", average);
        JLabel averageLabel = new JLabel("Aktueller Schnitt: " + averageText + "    ");
        averageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        averageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(averageLabel);

        courseLabel.add(Box.createVerticalStrut(50));

        // Formular für neue Note
        addSectionHeader("Neue Note eintragen");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setMaximumSize(new Dimension(600, 150));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

            // Formularkomponenten anordnen und hinzufügen
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Note (Punkte): "), gbc);

        gbc.gridx = 1;
        gradeValueField = new JTextField(10);
        formPanel.add(gradeValueField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Typ:"), gbc);

        gbc.gridx = 1;
        gradeTypeComboBox = new JComboBox<>();
        HashMap<String, Float> weights = gradeDataAccess.getWeightsForCourse(courseId);
        //ComboBox mit Notentypen füllen
        for (String type : weights.keySet()) {
            gradeTypeComboBox.addItem(type);
        }
        gradeTypeComboBox.setSelectedIndex(-1);
        formPanel.add(gradeTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Beschreibung:"), gbc);

        gbc.gridx = 1;
        gradeDescriptionField = new JTextField(20);
        formPanel.add(gradeDescriptionField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        addGradeButton = new JButton("Speichern");
        addGradeButton.addActionListener(this);
        formPanel.add(addGradeButton, gbc);

        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        addSectionHeader("Bisherige Noten");

        ArrayList<StudentGradeDetail> grades = gradeDataAccess.getGradesForStudentInCourse(student.getId(), courseId);
        if (grades.isEmpty()) {
            addInfoLabel("Keine Noten vorhanden.");
        } else {
            // Für jeden Notentyp durchlaufen
            for (String type : weights.keySet()) {
                ArrayList<StudentGradeDetail> typeGrades = new ArrayList<>();
                for (StudentGradeDetail detail : grades) {
                    // Alle Noten zu diesem Notentyp raussuchen
                    if(type.equals(detail.getGradeType())) typeGrades.add(detail);
                }

                if (!typeGrades.isEmpty()) {
                    // Titel für Notentyp
                    contentPanel.add(Box.createVerticalStrut(10));
                    JLabel typeHeader = new JLabel(type);
                    typeHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    typeHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
                    contentPanel.add(typeHeader);

                    // Für jede Note, die zu dem Notentyp gehört:
                    for (StudentGradeDetail detail: typeGrades) {
                        // Note anzeigen
                        JPanel gradeRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        String text = String.format("%.1f - %s", Math.abs(detail.getGradeValue()), detail.getGradeDescription());
                        JLabel detailLabel = new JLabel(text);
                        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                        // --- Note löschen Button ---
                        JButton deleteButton = new JButton("X");
                        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        deleteButton.setForeground(Color.RED);
                        deleteButton.setBorderPainted(false);
                        deleteButton.setContentAreaFilled(false);
                        deleteButton.setFocusPainted(false);
                        // Lambda - Ausdruck, man kann direkt ein ohne die actionPerformed Methode es so machen
                        // (Wenn ich das nur vorher gewusst hätte...)
                        deleteButton.addActionListener(e -> deleteGrade(detail.getGradeId()));

                        gradeRow.add(detailLabel);
                        gradeRow.add(deleteButton);
                        gradeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, gradeRow.getPreferredSize().height));

                        contentPanel.add(gradeRow);
                    }
                }
            }
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Aktualisiert die Ansicht (z.B. nach dem Hinzufuegen oder Loeschen einer Note).
     */
    @Override
    public void refreshData() {
        if(currentCourseId != null && currentStudent != null && currentTeacher != null) {
            loadGradingData(currentCourseId, currentStudent, currentTeacher);
        }
    }

    /**
     * Behandelt Klicks auf Buttons.
     * @param e Das ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            // Zurück
            mainApp.getTeacherCoursePanel().loadCourseData(currentCourseId, this.currentTeacher);
            mainApp.showPanel(App.TEACHER_COURSE_PANEL);
        } else if (e.getSource() == addGradeButton) {
            try {
                // Alle Daten holen
                float value = Float.parseFloat(gradeValueField.getText());
                String type = (String) gradeTypeComboBox.getSelectedItem();
                String description = gradeDescriptionField.getText();

                if(type == null) {
                    JOptionPane.showMessageDialog(this, "Bitte einen Notentyp auswählen.");
                    return;
                }

                // Aus Daten eine neue Note erstellen
                Grade newGrade = new Grade(
                        currentStudent.getId(),
                        currentCourseId,
                        value,
                        description,
                        type,
                        currentTeacher.getId()
                );

                // Note in der Datenbank erstellen
                if (gradeDataAccess.createGrade(newGrade)) {
                    JOptionPane.showMessageDialog(this, "Note erfolgreich gespeichert.");
                    loadGradingData(currentCourseId, currentStudent, currentTeacher);
                } else {
                    JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Note.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bitte eine gültige Zahl für die Note eingeben.");
            }

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
     * Methode um oben den Lambda Ausdruck klein zu halten.
     * Loescht die Note durch gradeDataAccess.
     * **/
    private void deleteGrade(String gradeId) {
        if (gradeDataAccess.deleteGrade(gradeId)) {
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Fehler beim Löschen der Note.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
