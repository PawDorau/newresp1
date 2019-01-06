/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onenotegame;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author pdora
 */
public class Guide extends JFrame {
    
    int gWIDTH = 1024, gHEIGHT = 768;
    int option = 0;
    JButton hButton;
    JFrame hPanel;
    BufferedImage hImage;
    Panel panel;

    public Guide(Panel panel){
        hPanel = new JFrame();
        hPanel.setPreferredSize(new Dimension(gWIDTH, gHEIGHT));
        hPanel.setMaximumSize(new Dimension(gWIDTH, gHEIGHT));
        hPanel.setMinimumSize(new Dimension(gWIDTH, gHEIGHT));

        try {
            hImage = ImageIO.read(getClass().getResource("/hImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon hImage2 = new ImageIcon(hImage);

        hButton = new JButton("");
        hPanel.add(hButton);
        hButton.setBounds(50,50,500,500);
        hButton.setIcon(hImage2);
        ClickEvent en = new ClickEvent();
        hButton.addActionListener(en);

        hPanel.pack();;
        hPanel.setVisible(true);
    }
    
        public class ClickEvent implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent en){
               hPanel.setVisible(false);
        }
}
}