/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paintbrushapp2;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author Muhammed Magdy
 */
public class PaintBrushApp2 {
    public static void main(String[] args) {
        JFrame f= new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Paint brush GUI");
        f.setBackground(Color.white);
        PaintPanel paintPanel = new PaintPanel();
        ToolBar toolbar = new ToolBar(paintPanel);
        f.setJMenuBar(toolbar);
        f.add(paintPanel, BorderLayout.CENTER);
        f.pack();
        f.setSize(1000,600);
        f.setVisible(true);
    }
    
}
