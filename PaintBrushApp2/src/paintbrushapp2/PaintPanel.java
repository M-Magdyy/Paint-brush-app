/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paintbrushapp2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import paintbrushapp2.ButtonManager;
import paintbrushapp2.CheckBoxManager;
import paintbrushapp2.ColoredFreeHandDrawing;
import paintbrushapp2.ColoredLine;
import paintbrushapp2.ColoredOval;
import paintbrushapp2.ColoredRectangle;
import paintbrushapp2.ColoredShape;
import paintbrushapp2.Eraser;

public class PaintPanel extends JPanel {
    private Color selectedColor = Color.BLACK;
    private int x1, y1, x2, y2;
    private String[] shapeTypes = {"Rectangle", "Oval", "Line"};
    private String currentShape;
    private int strokeSize = 1;
    private boolean freeHandMode = false;
    private boolean eraseMode = false;
    private ArrayList<Point> currentFreeHandDrawing = new ArrayList<>();
    private ArrayList<Point> erasePoints = new ArrayList<>();
    private ArrayList<ColoredRectangle> coloredRectangles = new ArrayList<>();
    private ArrayList<ColoredOval> coloredOvals = new ArrayList<>();
    private ArrayList<ColoredLine> coloredLines = new ArrayList<>();
    private ArrayList<ColoredFreeHandDrawing> coloredFreeDrawings = new ArrayList<>();
    private ArrayList<Eraser> eraser = new ArrayList<>();
    private ArrayList<ColoredFreeHandDrawing> eraserDrawings = new ArrayList<>();
    private ArrayList<Drawable> drawings = new ArrayList<>();
    private ArrayList<ColoredShape> persistentDrawings = new ArrayList<>();
    private JCheckBox dottedCheckBox;
    private JCheckBox filledCheckBox;
    private boolean isFilled = false;
    private String lastDrawnShape;
    private Stack<Object> drawingHistory = new Stack<>();
    private BufferedImage canvas;
    private BufferedImage loadedImage;

    public PaintPanel() {
        canvas = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);// Initialize the canvas with a default size
        initializeUI();// Initialize the UI components
        setupListeners();// Set up event listeners
    }

    private void initializeUI() {
        this.setFocusable(true);
        setBackground(Color.white);
        this.add(ButtonManager.createColorButton(this)); // Add color button 

        // Add shape buttons to the panel
        for (String shapeType : shapeTypes) {
            this.add(ButtonManager.createShapeButton(this, shapeType));
        }

        this.add(ButtonManager.createFreeHandButton(this));

        dottedCheckBox = CheckBoxManager.createDottedCheckBox(new JCheckBox(), this);
        this.add(dottedCheckBox);

        filledCheckBox = CheckBoxManager.createFilledCheckBox(this);
        this.add(filledCheckBox);

        this.add(ButtonManager.createIncreaseStrokeButton(this));
        this.add(ButtonManager.createDecreaseStrokeButton(this));
        this.add(ButtonManager.createClearAllButton(this));
        this.add(ButtonManager.createUndoButton(this));
        this.add(ButtonManager.createEraserButton(this));
    }

    private void setupListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    private void handleMousePressed(MouseEvent e) {
        // Record starting coordinates for drawing
        x1 = e.getX();
        y1 = e.getY();

        // If not in freehand mode, add initial point for the shape being drawn
        if (!freeHandMode) {
            currentFreeHandDrawing.add(new Point(x1, y1));
           
            if (currentShape == null) { // If no shape is selected, default to rectangle
              // if (lastDrawnShape != null) {
                //  currentShape = lastDrawnShape;
                 // } else {
                 // currentShape = "Line";}
            currentShape = (lastDrawnShape != null) ? lastDrawnShape : "Line";
            }
           
        }
        if (eraseMode) {
        erasePoints.add(new Point(x1, y1));
        selectedColor = Color.WHITE;
    }
    }
    
    private void handleMouseReleased(MouseEvent e) {
    x2 = e.getX();
    y2 = e.getY();

    try {
        if (freeHandMode) {
            // Handle freehand mode
            boolean isDotted = dottedCheckBox.isSelected();
            if (!currentFreeHandDrawing.isEmpty()) {
                ColoredFreeHandDrawing freeHandDrawing = new ColoredFreeHandDrawing(selectedColor, new ArrayList<>(currentFreeHandDrawing), strokeSize, isDotted);
                drawings.add(freeHandDrawing);
                drawingHistory.push(freeHandDrawing);
                lastDrawnShape = "FreeHand";
            }
           currentFreeHandDrawing.clear(); // Clear only when starting a new freehand drawing
           storeDrawing();
        } else if (eraseMode) {
        boolean isDotted = dottedCheckBox.isSelected();
        if (!erasePoints.isEmpty()) {
            ColoredFreeHandDrawing eraserDrawing = new ColoredFreeHandDrawing(selectedColor, new ArrayList<>(erasePoints), strokeSize, isDotted);
            eraserDrawings.add(eraserDrawing);
            drawingHistory.push(eraserDrawing);
            lastDrawnShape = "Eraser";
        }
        erasePoints.clear();
        storeDrawing();
        }else if (currentShape != null) {
            // Handle other shapes
            boolean isDotted = dottedCheckBox.isSelected();
            switch (currentShape) {
                case "Rectangle":
                    ColoredRectangle rectangle = new ColoredRectangle(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), selectedColor, strokeSize, isDotted, isFilled);
                    drawings.add(rectangle);
                    persistentDrawings.add(rectangle);
                    lastDrawnShape = "Rectangle";
                    break;
                case "Oval":
                    ColoredOval oval = new ColoredOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), selectedColor, strokeSize, isDotted, isFilled);
                    drawings.add(oval);
                    persistentDrawings.add(oval);
                    lastDrawnShape = "Oval";
                    break;
                case "Line":
                    ColoredLine line = new ColoredLine(x1, y1, x2, y2, selectedColor, strokeSize, isDotted);
                    drawings.add(line);
                    persistentDrawings.add(line);
                    lastDrawnShape = "Line";
                    break;
            }
        }
        storeDrawing();
        repaint();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


    private void handleMouseDragged(MouseEvent e) {
        if (freeHandMode) {
            currentFreeHandDrawing.add(new Point(e.getX(), e.getY()));
            repaint();
        }
        else {
            x2 = e.getX();
            y2 = e.getY();
           // erasePoints.add(new Point(x2, y2));
            repaint();
        }
       if (eraseMode) {
        erasePoints.add(new Point(e.getX(), e.getY()));
        repaint();
    }
    }

    private void storeDrawing() {
        if (!freeHandMode) 
        {
           
          switch (currentShape){
                case "Rectangle":
                    drawingHistory.push(new ColoredRectangle(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), selectedColor, strokeSize, dottedCheckBox.isSelected(), isFilled));
                    break;
                case "Oval":
                    drawingHistory.push(new ColoredOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), selectedColor, strokeSize, dottedCheckBox.isSelected(), isFilled));
                    break;
                case "Line":
                    drawingHistory.push(new ColoredLine(x1, y1, x2, y2, selectedColor, strokeSize, dottedCheckBox.isSelected()));
                    break;
                default:
                    break;
                                }
          
        }
        else {
            boolean isDotted = dottedCheckBox.isSelected();
            drawingHistory.push(new ColoredFreeHandDrawing(selectedColor, new ArrayList<>(currentFreeHandDrawing), strokeSize, isDotted));
            //currentFreeHandDrawing.clear();
        }
    }

    public void undoLastAction() {
        if (!drawingHistory.isEmpty()) {
            drawingHistory.pop();
            updateDrawnShapesFromHistory();
            repaint();
        }
    }

    private void updateDrawnShapesFromHistory() {
    coloredRectangles.clear();
    coloredOvals.clear();
    coloredLines.clear();
    coloredFreeDrawings.clear();
    eraser.clear();
    eraserDrawings.clear();

    
    // Iterate through the drawing history stack and update the lists
    for (Object item : drawingHistory) {
        //This condition checks if the current item in the drawing history stack is an instance of ColoredShape. 
        //It's a way of determining if the item represents a colored shape.
        if (item instanceof ColoredShape) {
            ColoredShape coloredShape = (ColoredShape) item;//casts the item to a ColoredShape. This allows you to work with it as an object of the ColoredShape class
            if (coloredShape instanceof ColoredRectangle) {
                coloredRectangles.add((ColoredRectangle) coloredShape);
            } else if (coloredShape instanceof ColoredOval) {
                coloredOvals.add((ColoredOval) coloredShape);
            } else if (coloredShape instanceof ColoredLine) {
                coloredLines.add((ColoredLine) coloredShape);
            }else if (item instanceof ColoredFreeHandDrawing) {
            ColoredFreeHandDrawing freeHandDrawing = (ColoredFreeHandDrawing) item; //casting
            if (eraseMode) {
                if ("Eraser".equals(lastDrawnShape)) {
                    eraserDrawings.add(freeHandDrawing);
                }
            } else {
                coloredFreeDrawings.add(freeHandDrawing);
            }
        }
        }
    } 
}

  @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    // Draw the loaded image if it exists
    if (loadedImage != null) {
        g2d.drawImage(loadedImage, 0, 0, this);
    }

    // Draw shapes from drawings list
/*    for (ColoredShape coloredShape : drawings) {
        drawColoredShape(g2d, coloredShape);
    }*/

    // Draw shapes from drawingHistory stack
    for (Object item : drawingHistory) {
        if (item instanceof ColoredShape) {
            ColoredShape coloredShape = (ColoredShape) item;
            drawColoredShape(g2d, coloredShape);
        }
        else if (item instanceof ColoredFreeHandDrawing) {
            ColoredFreeHandDrawing freeHandDrawing = (ColoredFreeHandDrawing) item;
            drawColoredFreeHandDrawing(g2d, freeHandDrawing);
        }
        
    }

    // Draw the current shape
    if (!freeHandMode && currentShape != null) {
        g2d.setColor(selectedColor);
        /*
        Stroke stroke;
        if (dottedCheckBox.isSelected()) {
    stroke = getDottedStroke(strokeSize);
       } else {
    stroke = getSolidStroke(strokeSize);
     }*/
        Stroke stroke = dottedCheckBox.isSelected() ? getDottedStroke(strokeSize) : getSolidStroke(strokeSize);
        g2d.setStroke(stroke);

        switch (currentShape) {
            case "Rectangle":
                g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                break;
            case "Oval":
                g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                break;
            case "Line":
                g2d.drawLine(x1, y1, x2, y2);
                break;
        }
    }

    // Draw the current freehand drawing
    if (freeHandMode && !currentFreeHandDrawing.isEmpty()) {
        drawColoredFreeHandDrawing(g2d, new ColoredFreeHandDrawing(selectedColor, new ArrayList<>(currentFreeHandDrawing), strokeSize, dottedCheckBox.isSelected()));
    }
/*
    // Draw the current erase mode
    for (ColoredFreeHandDrawing eraserDrawing : eraserDrawings) {
        drawColoredFreeHandDrawing(g2d, eraserDrawing);
    }    */
}

private void drawColoredShape(Graphics2D g2d, ColoredShape coloredShape) {
    g2d.setColor(coloredShape.color);
    Stroke stroke = coloredShape.isDotted ? getDottedStroke(coloredShape.strokeSize) : getSolidStroke(coloredShape.strokeSize);
    g2d.setStroke(stroke);

       if (coloredShape instanceof ColoredRectangle) {
        ColoredRectangle rectangle = (ColoredRectangle) coloredShape;
        if (rectangle.isFilled) {
            g2d.fillRect(rectangle.x1, rectangle.y1, rectangle.x2 - rectangle.x1, rectangle.y2 - rectangle.y1);
        } else {
            g2d.drawRect(rectangle.x1, rectangle.y1, rectangle.x2 - rectangle.x1, rectangle.y2 - rectangle.y1);
        }
    } else if (coloredShape instanceof ColoredOval) {
        ColoredOval oval = (ColoredOval) coloredShape;
        if (oval.isFilled) {
            g2d.fillOval(oval.x1, oval.y1, oval.x2 - oval.x1, oval.y2 - oval.y1);
        } else {
            g2d.drawOval(oval.x1, oval.y1, oval.x2 - oval.x1, oval.y2 - oval.y1);
        }
    } else if (coloredShape instanceof ColoredLine) {
        ColoredLine line = (ColoredLine) coloredShape;
        g2d.drawLine(line.x1, line.y1, line.x2, line.y2);
    }
}

private void drawColoredFreeHandDrawing(Graphics2D g2d, ColoredFreeHandDrawing freeHandDrawing) {
    if(eraseMode){
    g2d.setColor(Color.WHITE);
    Stroke stroke = freeHandDrawing.isDotted ? getDottedStroke(freeHandDrawing.strokeSize) : getSolidStroke(freeHandDrawing.strokeSize);
    g2d.setStroke(stroke);
    ArrayList<Point> points = freeHandDrawing.points;
    for (int i = 1; i < points.size(); i++) {
        Point p1 = points.get(i - 1);
        Point p2 = points.get(i);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
    }else{
    g2d.setColor(freeHandDrawing.color);
    Stroke stroke = freeHandDrawing.isDotted ? getDottedStroke(freeHandDrawing.strokeSize) : getSolidStroke(freeHandDrawing.strokeSize);
    g2d.setStroke(stroke);
    ArrayList<Point> points = freeHandDrawing.points;
    for (int i = 1; i < points.size(); i++) {
        Point p1 = points.get(i - 1);
        Point p2 = points.get(i);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
    }
    
}
    
private Stroke getSolidStroke(int size) {
        return new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    private Stroke getDottedStroke(int size) {
        return new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public void setCurrentShape(String currentShape) {
        this.currentShape = currentShape;
    }

    public void setFreeHandMode(boolean freeHandMode) {
        this.freeHandMode = freeHandMode;
    }
    
    public ArrayList<Point> getCurrentFreeHandDrawing() {
        return currentFreeHandDrawing;
    }
    
    public void setEraseMode(boolean eraseMode) {
        this.eraseMode = eraseMode;
    }
    
    public ArrayList<Point> getCurrentEraseMode() {
        return erasePoints;
    }
    
    public void increaseStrokeSize() {
        strokeSize++;
    }

    public void decreaseStrokeSize() {
        if (strokeSize > 0) {
            strokeSize--;
        }
    }

    public void clearAllDrawings() {
        coloredRectangles.clear();
        coloredOvals.clear();
        coloredLines.clear();
        coloredFreeDrawings.clear();
        currentShape = null;
        currentFreeHandDrawing.clear();
        erasePoints.clear();
        drawingHistory.clear();
        repaint();
    }
    
    public void loadImage(File file) {
        try {
        BufferedImage loadedImage = ImageIO.read(file);
        int imgWidth = loadedImage.getWidth();
        int imgHeight = loadedImage.getHeight();

        // Calculate the dimensions to fit the entire image onto the panel
        double panelAspectRatio = (double) getWidth() / getHeight();
        double imageAspectRatio = (double) imgWidth / imgHeight;

        int newWidth, newHeight;
        if (panelAspectRatio > imageAspectRatio) {
            newWidth = (int) (getHeight() * imageAspectRatio);
            newHeight = getHeight();
        } else {
            newWidth = getWidth();
            newHeight = (int) (getWidth() / imageAspectRatio);
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(loadedImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        canvas = resizedImage;
        setPreferredSize(new Dimension(newWidth, newHeight)); // Set the preferred size of the panel

        // Repaint the panel to display the loaded image
        repaint();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }

    public void saveDrawing(File file) {
    try {
        int imageWidth = loadedImage != null ? loadedImage.getWidth() : 0;
        int imageHeight = loadedImage != null ? loadedImage.getHeight() : 0;
        
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        
        int combinedWidth = Math.max(imageWidth, canvasWidth);
        int combinedHeight = Math.max(imageHeight, canvasHeight);

        BufferedImage savedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = savedImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, combinedWidth, combinedHeight);

        if (loadedImage != null) {
            g2d.drawImage(loadedImage, 0, 0, null);
        }

        // Calculate the scaling factor to fit all the content within the canvas
        double scaleX = (double) combinedWidth / canvasWidth;
        double scaleY = (double) combinedHeight / canvasHeight;
        g2d.scale(scaleX, scaleY);

        // Draw the drawn content onto the saved image
        paintComponent(g2d);

        g2d.dispose();

        ImageIO.write(savedImage, "png", file); // Save as PNG, modify format if needed
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
}