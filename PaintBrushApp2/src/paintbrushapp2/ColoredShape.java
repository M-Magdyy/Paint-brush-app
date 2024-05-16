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
public abstract class ColoredShape implements Drawable{
    int x1, y1, x2, y2;
    Color color;
    int strokeSize;
    boolean isDotted;
    boolean isFilled;
    
     @Override
    public abstract void draw(Graphics2D g2d);

    public ColoredShape(int x1, int y1, int x2, int y2, Color color, int strokeSize, boolean isDotted, boolean isFilled) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.strokeSize = strokeSize;
        this.isDotted = isDotted;
        this.isFilled = isFilled;
    }
}

