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
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    private final JButton saveButton;
    private final JButton openButton;
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
            this.repaint();
        }

        public void setText(String text) {
            this.text = text;
            this.repaint();
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
                int textHeight = fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
                int textX = (wid - textWidth) / 2;
                int textY = ((int) (hei * IMAGE_HEIGHT_PROPORTION)) + ((int) (hei * TEXT_HEIGHT_PROPORTION / 2)) + (textHeight / 2) - fm.getDescent();
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
    }

    public void setText(String text) {
        canvas.setText(text == null ? "" : text);
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
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setText(textfield.getText());
            }
        });
        saveButton = new JButton("Guardar");
        input.add(saveButton, BorderLayout.AFTER_LINE_ENDS);
        openButton = new JButton("Abrir");
        filechooser = new JFileChooser(new File(System.getProperty("user.dir")));
        input.add(openButton, BorderLayout.BEFORE_LINE_BEGINS);
        openButton.addActionListener((ActionEvent ae) -> {
            int selection = filechooser.showOpenDialog(frame);
            if (selection != JFileChooser.APPROVE_OPTION) {
                return;
            }
            try {
                canvas.setImage(ImageIO.read(filechooser.getSelectedFile()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(canvas, "El archivo no es válido.");
            }
        });
        saveButton.addActionListener((ActionEvent ae) -> {
            saveButton.setEnabled(false);
            if (canvas.bf != null && canvas.text != null && !canvas.text.isBlank()) {
                new Thread(() -> {
                    try {
                        int selection = filechooser.showSaveDialog(frame);
                        if (selection != JFileChooser.APPROVE_OPTION){
                            return;
                        }
                        if (filechooser.getSelectedFile().exists()){
                            selection = JOptionPane.showConfirmDialog(frame, "El archivo ya existe, sobreescribir?", "Archivo existente", JOptionPane.YES_NO_OPTION);
                            if (selection != JOptionPane.YES_OPTION){
                                return;
                            }
                        }
                        Utilities.Utilities.CaptionAndSavePicture(canvas.bf, canvas.text, filechooser.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(frame, "Guardado!");
                    } catch (IOException ex) {
                        Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(frame, "Error al guardar.");
                    } finally {
                        saveButton.setEnabled(true);
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
        frame.pack();
        frame.setLocationRelativeTo(null);
        canvas.requestFocus();
        frame.setVisible(true);
    }
}
