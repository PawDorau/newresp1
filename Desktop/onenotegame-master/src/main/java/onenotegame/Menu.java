package onenotegame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Menu extends JPanel {

    private static final int mWIDTH = 924;
    private static final int mHEIGHT = 300;
    BufferedImage menubg;

    public Menu() {

            this.setPreferredSize(new Dimension(mWIDTH, mHEIGHT));
            this.setMaximumSize(new Dimension(mWIDTH, mHEIGHT));
            this.setMinimumSize(new Dimension(mWIDTH, mHEIGHT));
            this.setBounds(50, 50, mWIDTH, mHEIGHT);
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setVisible(true);

        try {
            menubg = ImageIO.read(getClass().getResource("/beeth.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g3) {
        super.paintComponent(g3);
        //Background
        g3.drawImage(menubg, 0, 0, null);
    }
}