/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author HCastilloR
 */
public class UserInterface {

    public static final Dimension DEFAULT_DIMENSIONS;
    public static final Dimension CANVAS_DEFAULT_DIMENSIONS;
    private final JFrame frame;
    private final Canvas canvas;

    static {
        DEFAULT_DIMENSIONS = new Dimension(800, 600);
        CANVAS_DEFAULT_DIMENSIONS = new Dimension(DEFAULT_DIMENSIONS.width, DEFAULT_DIMENSIONS.height / 6 * 5);
    }

    private class Canvas extends JPanel {

        private BufferedImage bf;

        @Override
        public final void setDoubleBuffered(boolean aFlag) {
            super.setDoubleBuffered(aFlag); //To change body of generated methods, choose Tools | Templates.
        }

        public void setImage(BufferedImage bf) {
            this.bf = bf;
        }
        
        @Override
        protected void paintComponent(Graphics grphcs){
            Graphics2D g = (Graphics2D) grphcs;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            int wid = bf.getWidth();
            int hei = bf.getHeight();
            g.drawImage(bf, 0, 0, wid, hei, null);
        }

        public Canvas() {
            super();
            setDoubleBuffered(true);
            bf = null;
        }

    }

    public void setImage(BufferedImage bf) {
        canvas.setImage(bf);
    }

    public UserInterface() {
        frame = new JFrame("Captioneer");
        canvas = new Canvas();
    }
}
