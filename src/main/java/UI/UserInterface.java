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
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author HCastilloR
 */
public class UserInterface {

    public static final Dimension DEFAULT_DIMENSIONS;
    public static final double CANVAS_HEIGHT_PROPORTION;
    public static final double IMAGE_HEIGHT_PROPORTION;
    public static final double TEXT_HEIGHT_PROPORTION;
    private final JFrame frame;
    private final Canvas canvas;
    private final JPanel input;
    private final JTextField textfield;
    private final JButton button;
    private final JFileChooser filechooser;

    static {
        DEFAULT_DIMENSIONS = new Dimension(800, 600);
        CANVAS_HEIGHT_PROPORTION = 5d / 6d;
        IMAGE_HEIGHT_PROPORTION = 7d / 8d;
        TEXT_HEIGHT_PROPORTION = 1d / 8d;
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
            g.setFont(Utilities.Utilities.DEFAULT_FONT);
            int wid = getWidth();
            int hei = getHeight();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, wid, hei);
            if (bf != null) {
                g.drawImage(bf, 0, 0, wid, (int) (hei * IMAGE_HEIGHT_PROPORTION), null);
            }
            if (text != null && !text.isBlank()) {
                g.setColor(Color.WHITE);
                g.setFont(g.getFont().deriveFont(32.0f));
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getMaxAscent() + fm.getMaxDescent();
                int textX = (wid - textWidth) / 2;
                int textY = ((int) (hei * IMAGE_HEIGHT_PROPORTION)) + ((int) (hei * TEXT_HEIGHT_PROPORTION / 2)) + (textHeight / 2);
                g.drawString(text, textX, textY);
                g.dispose();
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
        canvas.repaint();
    }

    public void setText(String text) {
        canvas.setText(text == null ? "" : text);
        canvas.repaint();
    }

    public UserInterface() {
        frame = new JFrame("Captioneer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        JPanel contentPane = new JPanel(new BorderLayout());
        input = new JPanel(new BorderLayout());
        textfield = new JTextField();
        input.add(textfield, BorderLayout.CENTER);
        textfield.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                StringBuilder sb = new StringBuilder(textfield.getText());
                sb.append(e.getKeyChar());
                setText(sb.toString());
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        button = new JButton("Guardar");
        input.add(button, BorderLayout.AFTER_LINE_ENDS);
        button.addActionListener((ActionEvent ae) -> {
            button.setEnabled(false);
            if (canvas.bf != null && canvas.text != null && !canvas.text.isBlank()) {
                new Thread(() -> {
                    try {
                        Utilities.Utilities.CaptionAndSavePicture(canvas.bf, canvas.text, "Testxd.png");
                    } catch (IOException ex) {
                        Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        button.setEnabled(true);
                    }
                }).start();
            }
        });
        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension cp_dim = contentPane.getSize();
                canvas.setPreferredSize(new Dimension(cp_dim.width, (int) (cp_dim.height * CANVAS_HEIGHT_PROPORTION)));
            }
        });
        contentPane.setPreferredSize(DEFAULT_DIMENSIONS);
        frame.setContentPane(contentPane);
        contentPane.add(canvas, BorderLayout.CENTER);
        contentPane.add(input, BorderLayout.PAGE_END);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
