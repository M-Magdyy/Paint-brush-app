/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paintbrushapp2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ToolBar extends JMenuBar 
{
    private PaintPanel panel;
    
    public ToolBar(PaintPanel panel)
    {
        this.panel=panel;
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent e )
            {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) 
                {
                File selectedFile = fileChooser.getSelectedFile();
                panel.loadImage(selectedFile);
                
                }
            }
        });
        
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent e )
            {
               JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                panel.saveDrawing(selectedFile);
                }
            }
        });
       fileMenu.add(openItem);
       fileMenu.add(saveItem);
       this.add(fileMenu);
     
    }
    
}