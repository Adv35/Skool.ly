package com.adv;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Main KLasse. Startet die App.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        // "SwingUtilities.invokeLater()" -> Best-Practice, um GUIs zu starten
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
}