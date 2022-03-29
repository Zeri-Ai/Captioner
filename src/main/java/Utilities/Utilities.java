/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author HCastilloR
 */
public abstract class Utilities {

    public static final double HEIGHT_PROPORTION;

    static {
        HEIGHT_PROPORTION = 1.1d;
    }

    public static final void CaptionAndSavePicture(BufferedImage picture, String text, String filename) throws IOException {
        File outputFile = new File(filename);
        int width = picture.getWidth();
        int old_height = picture.getHeight();
        BufferedImage result = new BufferedImage(width, (int) (old_height * HEIGHT_PROPORTION), BufferedImage.TYPE_4BYTE_ABGR);
        int height = result.getHeight();
        Graphics2D g = result.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.drawImage(picture, 0, 0, null);
        {
            int text_height_cap = height - old_height;
            float font_size = 1.0f;
            boolean is_capped = false;
            while (!is_capped) {
                g.setFont(g.getFont().deriveFont(font_size));
                FontMetrics fm = g.getFontMetrics();
                int text_height = fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
                if (text_height >= text_height_cap || fm.stringWidth(text) >= width) {
                    g.setFont(g.getFont().deriveFont(font_size - 0.1f));
                    is_capped = true;
                } else {
                    font_size += 0.1f;
                }
            }
        }
        g.setColor(Color.BLUE);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g);
        System.out.println(r);
        g.fillRect(width/2, 0, (int)r.getWidth(), (int)r.getHeight());
        int fontSize = fm.getAscent() + fm.getDescent();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = /*old_height + ((height - old_height) / 2)*/ + (fontSize);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
        ImageIO.write(result, "png", outputFile);
        g.dispose();
    }
}
