package com.adv;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Das Panel was erscheint, wenn die App gestartet wird, oder sich jemand ausloggt.
 * Bietet ein Formular, ueber den der Benutzer sich einloggen kann.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public class LoginPanel extends CommonJPanel implements ActionListener {

    private App mainApp;    // Assoziation auf HauptFrame, zum Wechseln der Panels

    // --- Backend Instanzen für Login ---
    private UserDataAccess userDataAccess;
    private PasswordManagement passwordManagement;

    // --- UI Elemente ---
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    /**
     * Konstruktor fuer das LoginPanel.
     * @param mainApp Referenz auf das Hauptfenster
     */
    public LoginPanel(App mainApp) {
        this.mainApp = mainApp;
        this.userDataAccess = new UserDataAccess();
        this.passwordManagement = new PasswordManagement();

        setLayout(new GridBagLayout());
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;

        // Logo laden und hinzufügen
        JLabel logoLabel = getLogoLabel();
        if (logoLabel != null) {
            outerGbc.insets = new Insets(0, 0, 30, 0); // Abstand zum Formular
            add(logoLabel, outerGbc);
            outerGbc.gridy++; // Nächste Zeile für das Formular
        }
        outerGbc.insets = new Insets(0, 0, 0, 0); // Reset Insets

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 10 in jeder Richtung Abstand, für Ästhetik
        gbc.insets = new Insets(10, 10, 10, 10);


        // --- UI Komponenten in formPanel erstellen ---
        JLabel usernameLabel = new JLabel("Benutzername:");
        usernameLabel.setPreferredSize(new Dimension(80, 10));
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Einloggen");
        loginButton.addActionListener(this);


        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        formPanel.add(loginButton, gbc);

        add(formPanel, outerGbc);

    }

    /***
     * Setzt Formularkomponenten zurueck.
     * */
    @Override
    public void refreshData() {
        usernameField.setText(null);
        passwordField.setText(null);
    }

    /** Methode implementiert von dem Interface Actionlistener.
     * Handling von Backend UserDataAccess und PasswordManagment.
     * @param e Das ActionEvent, das die Buttons zum ActionListener geben.
     * **/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // - Daten holen aus Komponenten -
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Nutzer holen:
            User user = userDataAccess.findUserByUsername(username);
            // Passwort prüfen
            if (user != null && passwordManagement.checkPassword(password, user.getPasswordHash())) {
                mainApp.getSideMenuPanel().setCurrentUser(user);
                JOptionPane.showMessageDialog(mainApp, "Login erfolgreich! Willkommen, " + user.getFirstName());
                refreshData();

                // Je nach Benutzer seinen Dashboard laden
                if (user.getRole().equals("student")) {
                    StudentDashboardPanel studentDashboardPanel = mainApp.getStudentDashboardPanel();
                    studentDashboardPanel.loadStudentData(user);
                    mainApp.showPanel(App.STUDENT_DASHBOARD_PANEL);
                } else if (user.getRole().equals("teacher")) {
                    TeacherDashboardPanel teacherDashboardPanel = mainApp.getTeacherDashboardPanel();
                    teacherDashboardPanel.loadTeacherData(user);
                    mainApp.showPanel(App.TEACHER_DASHBOARD_PANEL);
                } else if (user.getRole().equals("admin")) {
                    AdminDashboardPanel adminDashboardPanel = mainApp.getAdminDashboardPanel();
                    adminDashboardPanel.loadAdminData(user);
                    mainApp.showPanel(App.ADMIN_DASHBOARD_PANEL);
                } else {
                    JOptionPane.showMessageDialog(mainApp, "Login für Ihre Rolle nicht möglich. " +
                            "Bitte wenden Sie sich an den IT-Support!");
                }

            } else {
                JOptionPane.showMessageDialog(mainApp, "Login fehlgeschlagen. Bitte versuchen Sie es erneut.");
            }

        }

    }

    /**
     * Ladet das App Logo aus dem resources\icons Folder und bereitet es als JLabel vor.
     * @return JLabel Image
     * **/
    private JLabel getLogoLabel() {
        try {
            java.net.URL imgURL = getClass().getResource("/icons/logo.png");
            if (imgURL != null) {
                BufferedImage myPicture = ImageIO.read(imgURL);
                
                // Skaliert auf passende Breite, und berechnet die dazugehörig passende Höhe
                int targetWidth = 350;
                int targetHeight = (int) ((double) myPicture.getHeight() / myPicture.getWidth() * targetWidth);
                
                Image scaledImage = myPicture.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
