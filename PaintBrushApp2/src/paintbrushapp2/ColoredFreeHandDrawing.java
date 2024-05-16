/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paintbrushapp2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 *
 * @author Muhammed Magdy
 */
public class ColoredFreeHandDrawing implements Drawable {

    public final ArrayList<Point> points;
    public final Color color;
    public final int strokeSize;
    public final boolean isDotted;

    public ColoredFreeHandDrawing(Color color, ArrayList<Point> points, int strokeSize, boolean isDotted) {
        this.color = color;
        this.points = points;
        this.strokeSize = strokeSize;
        this.isDotted = isDotted;
    }
    private Stroke getDottedStroke(int size) {
        return new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{2f, 2f}, 0);
    }

    private Stroke getSolidStroke(int size) {
        return new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        Stroke stroke = isDotted ? getDottedStroke(strokeSize) : getSolidStroke(strokeSize);
        g2d.setStroke(stroke);
        ArrayList<Point> points = this.points;
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}
