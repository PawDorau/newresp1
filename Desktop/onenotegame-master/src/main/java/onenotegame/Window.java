/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onenotegame;

/**
 *
 * @author pdora
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Window extends Canvas{
    Panel panel;
    JFrame window;
    BufferedImage hIcon;
    
    public  Window(int WIDTH ,int HEIGHT, String title){
        window = new JFrame(title);

        window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        window.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        window.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        try {
            hIcon = ImageIO.read(getClass().getResource("/hicon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon hIcon2 = new ImageIcon(hIcon);

        //panel menu
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(WIDTH-100, 200));
        window.add(menu, BorderLayout.SOUTH);
        menu.setLayout(new FlowLayout());
        ((FlowLayout)menu.getLayout()).setHgap(75);
        ((FlowLayout)menu.getLayout()).setVgap(75);

        
        //samouczek
        JButton Help = new JButton("");
        Help.setPreferredSize(new Dimension(150,75));
        menu.add(Help, FlowLayout.LEFT);
        Help.setIcon(hIcon2);
        Help.setBackground(Color.WHITE);
        Help.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
        ClickEvent e = new ClickEvent();
        Help.addActionListener(e);
        
        //panel rozgrywki
        panel = new Panel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        window.getContentPane().add(panel,BorderLayout.CENTER );
        
        window.pack();
        window.setVisible(true);

    }
    
     //samouczek event
    public class ClickEvent implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
           //wyświtlenie samouczka
           Guide guide = new Guide(panel);
           window.add(guide);
           window.setVisible(true);
        }
}
}