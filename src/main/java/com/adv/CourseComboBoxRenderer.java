package com.adv;

import javax.swing.*;
import java.awt.*;

/**
 * Aehnlich zu UserComboBoxRenderer.java, nur fuer Course - Objekte.
 **/

public class CourseComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list,
                value,
                index,
                isSelected,
                cellHasFocus);
        // Hier wird geprüft, ob es sich beim ausgewählten Objekt auch um ein Course-Objekt handelt.
        if (value instanceof Course) {
            // Default: label.setText(value.toString());
            label.setText(((Course) value).getNAME());
        }
        // JLabel erbt von Component
        return label;
    }
}
