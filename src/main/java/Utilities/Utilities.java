/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.Color;
import java.awt.Font;
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
    public static final Font DEFAULT_FONT;

    static {
        HEIGHT_PROPORTION = 1.1d;
        DEFAULT_FONT = new Font("Impact", Font.PLAIN, 10);
    }

    public static final void CaptionAndSavePicture(BufferedImage picture, String text, String filename) throws IOException {
        File outputFile = new File(filename);
        int width = picture.getWidth();
        int old_height = picture.getHeight();
        BufferedImage result = new BufferedImage(width, (int) (old_height * HEIGHT_PROPORTION), BufferedImage.TYPE_4BYTE_ABGR);
        int height = result.getHeight();
        Graphics2D g = result.createGraphics();
        g.setFont(DEFAULT_FONT);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.drawImage(picture, 0, 0, null);
        {
            int text_height_cap = height - old_height - 7;
            if (text_height_cap < 0)
            {
                return;
            }
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
        FontMetrics fm = g.getFontMetrics();
        int fontSize = fm.getMaxAscent() + fm.getMaxDescent() + fm.getLeading();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = old_height + ((height - old_height) / 2) + (fontSize/2) - fm.getDescent();
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
        ImageIO.write(result, "png", outputFile);
        g.dispose();
    }
}
