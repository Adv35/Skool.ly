package com.adv;

import javax.swing.*;

/**
 * Alle Panels der App muessen die refresh-Funktion haben, daher die CommonJPanel, 
 * von der alle Panels erben und somit auch die Methode implementieren muessen.
 * @author Advik Vattamwar
 * @version 10.01.2026
 * **/
public abstract class CommonJPanel extends JPanel {
    public abstract void refreshData();

}
