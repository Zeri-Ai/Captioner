/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
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
        private String text;

        @Override
        public final void setDoubleBuffered(boolean aFlag) {
            super.setDoubleBuffered(aFlag); //To change body of generated methods, choose Tools | Templates.
        }

        public void setImage(BufferedImage bf) {
            this.bf = bf;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        protected void paintComponent(Graphics grphcs) {
            Graphics2D g = (Graphics2D) grphcs;
            int wid = getWidth();
            int hei = getHeight();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, wid, hei);
            if (bf != null) {
                g.drawImage(bf, 0, 0, wid, hei * 7 / 8, null);
            }
            if (text != null && !text.isBlank()) {
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                g.setFont(g.getFont().deriveFont(32.0f));
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getMaxAscent() + fm.getMaxDescent();
                int textX = (wid-textWidth)/2;
                int textY = (hei*7/8) + (hei/16) + (textHeight/2);
                g.drawString(text, textX, textY);
            }
        }

        public Canvas() {
            super();
            setDoubleBuffered(true);
            bf = null;
            text = "";
        }

    }

    public void setImage(BufferedImage bf) {
        canvas.setImage(bf);
    }

    public void setText(String text) {
        canvas.setText(text == null ? "" : text);
    }

    public UserInterface() {
        frame = new JFrame("Captioneer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setPreferredSize(DEFAULT_DIMENSIONS);
        frame.setContentPane(contentPane);
        canvas.setPreferredSize(CANVAS_DEFAULT_DIMENSIONS);
        contentPane.add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
