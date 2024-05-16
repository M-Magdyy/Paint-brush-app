/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paintbrushapp2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;

public class ButtonManager {
    public static JButton createColorButton(PaintPanel paintPanel) {
        JButton colorButton = new JButton("Colors");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(paintPanel, "Choose a Color", paintPanel.getSelectedColor());
                if (newColor != null) {
                    paintPanel.setSelectedColor(newColor);
                    System.out.println("Selected Color: " + newColor);
                }
            }
        });
        return colorButton;
    }

    public static JButton createShapeButton(PaintPanel paintPanel, String shapeType) {
        JButton shapeButton = new JButton(shapeType);
        shapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.setCurrentShape(shapeType);
                paintPanel.setFreeHandMode(false);
                paintPanel.setEraseMode(false);
            }
        });
        return shapeButton;
    }

    public static JButton createFreeHandButton(PaintPanel paintPanel) {
        JButton freeHandButton = new JButton("Free Hand");
        freeHandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.setCurrentShape(null);
                paintPanel.setEraseMode(false);
                paintPanel.setFreeHandMode(true);
                paintPanel.getCurrentFreeHandDrawing().clear();
            }
        });
        return freeHandButton;
    }
    
    public static JButton createEraserButton(PaintPanel paintPanel) {
        JButton eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.setCurrentShape(null);
                paintPanel.setFreeHandMode(false);
                paintPanel.setEraseMode(true);
                paintPanel.getCurrentEraseMode().clear();
            }
        });
        return eraserButton;
    }
    

    public static JButton createIncreaseStrokeButton(PaintPanel paintPanel) {
        JButton increaseStrokeButton = new JButton("Increase Stroke");
        increaseStrokeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.increaseStrokeSize();
                System.out.println("Stroke Increased: " + paintPanel.getStrokeSize());
            }
        });
        return increaseStrokeButton;
    }

    public static JButton createDecreaseStrokeButton(PaintPanel paintPanel) {
        JButton decreaseStrokeButton = new JButton("Decrease Stroke");
        decreaseStrokeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.decreaseStrokeSize();
                System.out.println("Stroke Decreased: " + paintPanel.getStrokeSize());
            }
        });
        return decreaseStrokeButton;
    }

    public static JButton createClearAllButton(PaintPanel paintPanel) {
        JButton clearAllButton = new JButton("Clear All");
        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.clearAllDrawings();
                System.out.println("All drawings cleared.");
            }
        });
        return clearAllButton;
    }

    public static JButton createUndoButton(PaintPanel paintPanel) {
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel.undoLastAction();
            }
        });
        return undoButton;
    }

}