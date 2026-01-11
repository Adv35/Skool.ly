package com.adv;

import javax.swing.*;
import java.awt.*;

/**
 * Diese Klasse ist dafuer da, z.B. bei der Klasse AdminCoursePanel.java in der TeacherComboBox die Lehrernamen in der Auswahl anzuzeigen,
 * aber trotzdem in der ComboBox die User-Objekte zu speichern.
 * Fande ich eleganter, als die toString() - Methode anzupassen, da ich bei der Methode noch ueberall denselben Standard habe.
 * Habe mich von folgenden Quellen hierzu inspirieren lassen :) (natuerlich angepasst auf meinen Use-Case):
 * <a href="https://codingtechroom.com/question/-customize-combobox-display-swing">...</a>
 * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/ListCellRenderer.html">...</a>
 * <a href="https://www.codejava.net/java-se/swing/create-custom-gui-for-jcombobox">...</a>
 *
 * @author Advik Vattamwar
 * @version 16.12.2025
 **/

public class UserComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list,
                value,
                index,
                isSelected,
                cellHasFocus);
        // Hier wird geprüft, ob es sich beim ausgewählten Objekt auch um einen User handelt.
        if (value instanceof User) {
            // Default: label.setText(value.toString());
            // Daher habe ich das hier zu dem Vor- und Nachnamen der Lehrkraft ersetzt.
            // So kann ich eine schöne Ansicht ermöglichen und trotzdem die User-Objekte in z.B. AdminCoursePanel.java nutzen.
            label.setText(((User) value).getFirstName() + " " + ((User) value).getLastName());
        }
        // JLabel erbt von Component
        return label;
    }
}
