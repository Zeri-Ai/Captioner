/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import UI.UserInterface;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 *
 * @author HCastilloR
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String fonts[]
                        = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

                for (int i = 0; i < fonts.length; i++) {
                    System.out.println(fonts[i]);
                }
                UserInterface ui = new UserInterface();
                File image = new File("src/main/java/Test/Test.png");
                System.out.println(image.getCanonicalPath());
                FileInputStream in = new FileInputStream(image);
                BufferedImage bf = ImageIO.read(in);
                ui.setImage(bf);
                ui.setText("Seggs");
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
