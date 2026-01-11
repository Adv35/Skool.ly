package com.adv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Das Hauptfenster.
 * Es handelt sich hier um das JFrame, welches der "Rahmen" des ganzen ist. Auf dem JFrame sind alle Panels.
 * Die Klasse steuert mithilfe des CardLayout, welches Panel gerade geladen ist und ermoeglicht leichtes swichten.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public class App extends JFrame implements ActionListener {

    // Zeigt genau eine Komponente (hier: Panels)
    // Jedes dieser Komponenten ist eine "Karte"
    // https://docs.oracle.com/javase/7/docs/api/java/awt/CardLayout.html
    private CardLayout cardLayout;

    // Ermöglicht es, Panels in den Hintergrund/Vordergrund zu positionieren
    // https://www.geeksforgeeks.org/java/java-jlayeredpane/
    private JLayeredPane layeredPane;
    private JPanel mainPanel;

    // SidePanel und der Button zum SidePanel
    private JButton menuButton;
    private SideMenuPanel sideMenuPanel;

    // --- Alle Panels ---
    private LoginPanel loginPanel;

    private StudentDashboardPanel studentDashboardPanel;
    private StudentCoursePanel studentCoursePanel;

    private TeacherDashboardPanel teacherDashboardPanel;
    private TeacherCoursePanel teacherCoursePanel;
    private TeacherGradingPanel teacherGradingPanel;

    private AdminDashboardPanel adminDashboardPanel;
    private AdminUserPanel adminUserPanel;
    private AdminCoursePanel adminCoursePanel;
    private AdminEnrollmentPanel adminEnrollmentPanel;
    private AdminPasswordResetPanel adminPasswordResetPanel;

    private UserPasswordResetPanel userPasswordResetPanel;


    // Panel Namen für das CardLayout
    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String STUDENT_DASHBOARD_PANEL = "StudentDashboardPanel";
    public static final String STUDENT_COURSE_PANEL = "StudentCoursePanel";
    public static final String TEACHER_DASHBOARD_PANEL = "TeacherDashboardPanel";
    public static final String TEACHER_COURSE_PANEL = "TeacherCoursePanel";
    public static final String TEACHER_GRADING_PANEL = "TeacherGradingPanel";
    public static final String ADMIN_DASHBOARD_PANEL = "AdminDashboardPanel";
    public static final String ADMIN_USER_PANEL = "AdminUserPanel";
    public static final String ADMIN_COURSE_PANEL = "AdminCoursePanel";
    public static final String ADMIN_ENROLLMENT_PANEL = "AdminEnrollmentPanel";
    public static final String ADMIN_PASSWORD_RESET_PANEL = "AdminPasswordResetPanel";
    public static final String USER_PASSWORD_RESET_PANEL = "UserPasswordResetPanel";

    // Damit man weiß, in welchem Panel man sich gerade befindet
    private String currentPanelName;

    /**
     *  Konstruktor des Panels.
     *  Bereitet das Frame vor und setzt Icon.
     *  Baut das CardLayout auf und fuegt alle Panels da rein.
     * **/
    public App() {
        setTitle("Skool.ly");
        getLogoIcon();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // zentriert das Fenster

        // JLayeredPane ermöglicht es, Komponenten übereinander zu legen
        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);


        // --- Menü Button ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEADING));
        menuButton = new JButton("☰");
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 30));
        menuButton.setBackground(Color.lightGray);
        menuButton.addActionListener(this);
        topBar.add(menuButton);

        // Die Top-Bar liegt auf der untersten Ebene (im layeredPane) und bekommt eine feste Position und Größe
        topBar.setBounds(0, 0, 800, 60);
        layeredPane.add(topBar, Integer.valueOf(2));

        // Seitenleistenmenü erstellen
        sideMenuPanel = new SideMenuPanel(this);

        // CardLayout erstellen
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Das Hauptpanel füllt den gesamten Bereich unter der Top-Bar
        mainPanel.setBounds(0, 60, 800, 500);
        layeredPane.add(mainPanel, Integer.valueOf(1));

        // Alle Panels dem mainPanel hinzufügen
        loginPanel = new LoginPanel(this);
        mainPanel.add(loginPanel, LOGIN_PANEL);

        studentDashboardPanel = new StudentDashboardPanel(this);
        mainPanel.add(studentDashboardPanel, STUDENT_DASHBOARD_PANEL);

        studentCoursePanel = new StudentCoursePanel(this);
        mainPanel.add(studentCoursePanel, STUDENT_COURSE_PANEL);

        teacherDashboardPanel = new TeacherDashboardPanel(this);
        mainPanel.add(teacherDashboardPanel, TEACHER_DASHBOARD_PANEL);

        teacherCoursePanel = new TeacherCoursePanel(this);
        mainPanel.add(teacherCoursePanel, TEACHER_COURSE_PANEL);

        teacherGradingPanel = new TeacherGradingPanel(this);
        mainPanel.add(teacherGradingPanel, TEACHER_GRADING_PANEL);

        adminDashboardPanel = new AdminDashboardPanel(this);
        mainPanel.add(adminDashboardPanel, ADMIN_DASHBOARD_PANEL);

        adminUserPanel = new AdminUserPanel(this);
        mainPanel.add(adminUserPanel, ADMIN_USER_PANEL);

        adminCoursePanel = new AdminCoursePanel(this);
        mainPanel.add(adminCoursePanel, ADMIN_COURSE_PANEL);

        adminEnrollmentPanel = new AdminEnrollmentPanel(this);
        mainPanel.add(adminEnrollmentPanel, ADMIN_ENROLLMENT_PANEL);

        adminPasswordResetPanel = new AdminPasswordResetPanel(this);
        mainPanel.add(adminPasswordResetPanel, ADMIN_PASSWORD_RESET_PANEL);

        userPasswordResetPanel = new UserPasswordResetPanel(this);
        mainPanel.add(userPasswordResetPanel, USER_PASSWORD_RESET_PANEL);

        // Die Seitenleiste liegt auf einer höheren Ebene und ist anfangs unsichtbar
        sideMenuPanel.setBounds(0, 0, sideMenuPanel.getPreferredSize().width, 600);
        layeredPane.add(sideMenuPanel, JLayeredPane.PALETTE_LAYER);
        sideMenuPanel.setVisible(false);

        showPanel(LOGIN_PANEL);
    }

    // --- GETTER ---
    /** Getter-Methode.
     * @return Gibt studentDashboardPanel zurueck.
     * **/
    public StudentDashboardPanel getStudentDashboardPanel() {
        return studentDashboardPanel;
    }

    /** Getter-Methode.
     * @return Gibt studentCoursePanel zurueck.
     * **/
    public StudentCoursePanel getStudentCoursePanel() {
        return studentCoursePanel;
    }

    /** Getter-Methode.
     * @return Gibt teacherDashboardPanel zurueck.
     * **/
    public TeacherDashboardPanel getTeacherDashboardPanel() {
        return teacherDashboardPanel;
    }

    /** Getter-Methode.
     * @return Gibt teacherCoursePanel zurueck.
     * **/
    public TeacherCoursePanel getTeacherCoursePanel() {
        return teacherCoursePanel;
    }

    /** Getter-Methode.
     * @return Gibt teacherGradingPanel zurueck.
     * **/
    public TeacherGradingPanel getTeacherGradingPanel() {
        return teacherGradingPanel;
    }

    /** Getter-Methode.
     * @return Gibt adminDashboardPanel zurueck.
     * **/
    public AdminDashboardPanel getAdminDashboardPanel() {
        return adminDashboardPanel;
    }

    /** Getter-Methode.
     * @return Gibt adminUserPanel zurueck.
     * **/
    public AdminUserPanel getAdminUserPanel() {
        return adminUserPanel;
    }

    /** Getter-Methode.
     * @return Gibt AdminCoursePanel zurueck.
     * **/
    public AdminCoursePanel getAdminCoursePanel() {
        return adminCoursePanel;
    }

    /** Getter-Methode.
     * @return Gibt adminEnrollmentPanel zurueck.
     * **/
    public AdminEnrollmentPanel getAdminEnrollmentPanel() {
        return adminEnrollmentPanel;
    }

    /** Getter-Methode.
     * @return Gibt adminPasswortResetPanel zurueck.
     * **/
    public AdminPasswordResetPanel getAdminPasswordResetPanel() {
        return adminPasswordResetPanel;
    }

    /** Getter-Methode.
     * @return Gibt userPasswordResetPanel zurueck.
     * **/
    public UserPasswordResetPanel getUserPasswordResetPanel() {
        return userPasswordResetPanel;
    }

    /** Getter-Methode.
     * @return Gibt sideMenuPanel zurueck.
     * **/
    public SideMenuPanel getSideMenuPanel() {
        return sideMenuPanel;
    }

    /** Zeigt das gegebene Panel.
     * @param panelName Der Name des Panels.
     * **/
    public void showPanel(String panelName) {
        this.currentPanelName = panelName;
        cardLayout.show(mainPanel, panelName);
    }

    /** zeigt Seitenleiste an **/
    public void showSideMenu() {
        sideMenuPanel.setVisible(true);
    }

    /** versteckt Seitenleiste **/
    public void hideSideMenu() {
        sideMenuPanel.setVisible(false);
    }

    /** Geht zurueck zu LoginPanel **/
    public void logout() {
        hideSideMenu();
        showPanel(LOGIN_PANEL);
    }

    /**
     * Ruft die refreshData Methode des jeweiligen Panels auf.
     * **/
    public void refresh() {
        hideSideMenu();
        if (currentPanelName.equals(TEACHER_DASHBOARD_PANEL)) {
            teacherDashboardPanel.refreshData();
        } else if (currentPanelName.equals(TEACHER_COURSE_PANEL)) {
            teacherCoursePanel.refreshData();
        } else if (currentPanelName.equals(STUDENT_DASHBOARD_PANEL)) {
            studentDashboardPanel.refreshData();
        } else if (currentPanelName.equals(STUDENT_COURSE_PANEL)) {
            studentCoursePanel.refreshData();
        } else if (currentPanelName.equals(TEACHER_GRADING_PANEL)) {
            teacherGradingPanel.refreshData();
        } else if (currentPanelName.equals(ADMIN_DASHBOARD_PANEL)) {
            adminDashboardPanel.refreshData();
        } else if (currentPanelName.equals(ADMIN_USER_PANEL)) {
            adminUserPanel.refreshData();
        } else if (currentPanelName.equals(ADMIN_COURSE_PANEL)) {
            adminCoursePanel.refreshData();
        } else if (currentPanelName.equals(ADMIN_PASSWORD_RESET_PANEL)) {
            adminPasswordResetPanel.refreshData();
        } else if (currentPanelName.equals(USER_PASSWORD_RESET_PANEL)) {
            userPasswordResetPanel.refreshData();
        } else if (currentPanelName.equals(ADMIN_ENROLLMENT_PANEL)) {
            adminEnrollmentPanel.refreshData();
        } else if (currentPanelName.equals(LOGIN_PANEL)) {
            loginPanel.refreshData();
        }
        revalidate();
        repaint();
    }

    /** Methode implementiert von dem Interface Actionlistener.
     * Handling von Backend PasswortManagment und Zurueckgehen zum Dashboard des Admins.
     * @param e Das ActionEvent, das die Buttons zum ActionListener geben.
     * **/
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuButton) {
            // Der Sandwich-Button zeigt das Menü immer an. Geschlossen wird es nur über das "X" im Panel.
            showSideMenu();
        }
    }

    /**
     * Ladet das App Logo aus dem resources\icons Folder und setzt es zum App Icon :)
     * **/
    private void getLogoIcon() {
        java.net.URL imgURL = getClass().getResource("/icons/appIcon.png");
        if (imgURL != null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image img = toolkit.createImage(imgURL);
            setIconImage(img);
        }
    }
}