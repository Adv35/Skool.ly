package com.adv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Das Dashboard-Panel fuer die Admins. Gehoert zur GUI. Von hier aus koennen Admins verschiedenste Sachen machen.
 * Leicht zu erweitern mit weiteren Klassen.
 * @author Advik Vattamwar
 * @version 05.01.2026
 * **/
public class AdminDashboardPanel extends CommonJPanel implements ActionListener {

    // Das Hauptobjekt / Steuerobjekt von App.java
    private App mainApp;
    private User currentAdmin;

    // Dashboard - Komponenten
    private JLabel welcomeLabel;
    private JButton createUserButton;
    private JButton createCourseButton;
    private JButton enrollStudentButton;
    private JButton userPasswordResetButton;

    /**
     * Konstruktor des Admins Dashboard Panels.
     * Bereitet das Panel mit seinen Komponenten vor.
     * @param mainApp Referenz auf das Hauptfenster
     **/
    public AdminDashboardPanel(App mainApp) {
        this.mainApp = mainApp;

        setLayout(new BorderLayout(0, 50));
        setBorder(new EmptyBorder(20,20,20,20));

        // --- TITEL ---
        welcomeLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        // --- Menü-Buttons initialisieren ---
        JPanel menuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- Buttons zum Panel hinzufügen ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        createUserButton = createMenuButton("Benutzer erstellen");
        menuPanel.add(createUserButton, gbc);

        gbc.gridy++;
        createCourseButton = createMenuButton("Kurs erstellen");
        menuPanel.add(createCourseButton, gbc);

        gbc.gridy++;
        enrollStudentButton = createMenuButton("Schüler einschreiben");
        menuPanel.add(enrollStudentButton, gbc);

        gbc.gridy++;
        userPasswordResetButton = createMenuButton("Benutzerpasswort zurücksetzen");
        menuPanel.add(userPasswordResetButton, gbc);

        add(menuPanel, BorderLayout.CENTER);
    }

    /**
     * Setzt die Willkommensnachricht neu, mit dem Nachnamen.
     * @param admin Das User-Objekt mit dem angemeldetem Admin.
     * **/
    public void loadAdminData(User admin) {
        this.currentAdmin = admin;
        welcomeLabel.setText("Willkommen, Admin " + admin.getLastName());
    }


    /**
     * Methode wird von CommonJPanel geerbt.
     * Ladet alle Daten neu, die aktualisiert geworden sein koennten (in der Datenbank)
     * **/
    @Override
    public void refreshData() {
        if (this.currentAdmin != null) {
            loadAdminData(this.currentAdmin);
        }
    }


    /** Methode implementiert von dem Interface Actionlistener.
     * Handling von Backend Kurserstellung und Zurueckgehen zum Dashboard des Admins.
     * @param e Das ActionEvent, das die Buttons zum ActionListener geben.
     * **/
    @Override
    public void actionPerformed(ActionEvent e) {
        // Wechsle zu dem Panel, wenn der Button zu dem Formular geklickt wurde
        if (e.getSource() == createUserButton) {
            mainApp.showPanel(App.ADMIN_USER_PANEL);
        } else if (e.getSource() == createCourseButton) {
            mainApp.showPanel(App.ADMIN_COURSE_PANEL);
        } else if (e.getSource() == enrollStudentButton) {
            mainApp.showPanel(App.ADMIN_ENROLLMENT_PANEL);
        } else if (e.getSource() == userPasswordResetButton) {
            // Z.108 braucht man, damit ein Admin nicht sein eigenes Passwort zurücksetzen kann, ohne sein altes zu kennen.
            // Das ist wichtig, falls er eingeloggt sein Laptop offen da lässt oder so.
            mainApp.getAdminPasswordResetPanel().loadAdminData(currentAdmin);
            mainApp.showPanel(App.ADMIN_PASSWORD_RESET_PANEL);
        }
    }

    /**
     * Template um die Buttons zu erstellen.
     * **/
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setPreferredSize(new Dimension(300, 50));
        button.addActionListener(this);
        return button;
    }
}
