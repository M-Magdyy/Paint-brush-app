/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paintbrushapp2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 *
 * @author Muhammed Magdy
 */
public class CheckBoxManager {
    public static JCheckBox createDottedCheckBox(JCheckBox dottedCheckBox, PaintPanel paintPanel) {
        dottedCheckBox = new JCheckBox("Dotted Line");
        dottedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle checkbox action, if needed
            }
        });
        return dottedCheckBox;
    }

    public static JCheckBox createFilledCheckBox(PaintPanel paintPanel) {
        final JCheckBox filledCheckBox = new JCheckBox("Filled");
        filledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.setFilled(filledCheckBox.isSelected());
            }
        });
        return filledCheckBox;
    }
}
