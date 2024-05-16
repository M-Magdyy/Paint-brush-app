/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paintbrushapp2;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Muhammed Magdy
 */
public class ColoredLine extends ColoredShape {
    public ColoredLine(int x1, int y1, int x2, int y2, Color color, int strokeSize, boolean isDotted) {
        super(x1, y1, x2, y2, color, strokeSize, isDotted, false);
    }
    @Override
    public void draw(Graphics2D g2d) {
        
    }
}