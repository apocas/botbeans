/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.common;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.windows.TopComponent;

/**
 *
 * @author Apocas
 */
public class BotbeansUtilities {
//    public static final String LANG = ((System.getProperty("application.home") != null)
//                ? System.getProperty("application.home")
//                : System.getProperty("user.dir")) + "/support/lang_def.xml";
    //public static final String LANG = "support/lang_def.xml";
    public static final String LANG = "org/botbeans/blocks/resources/lang_def.xml";
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    public static boolean compareImages(Image image1, Image image2) {
        int width = 73;
        int height = 73;

        if (image1 == null || image2 == null) {
            return false;
        }

        int[] pixels1 = image1 != null ? new int[width * height] : null;
        if (image1 != null) {
            try {
                PixelGrabber pg = new PixelGrabber(image1, 0, 0, width, height, pixels1, 0, width);
                pg.grabPixels();
                if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                    Message msg = new NotifyDescriptor.Message("Erro: compara_imagens()", NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(msg);
                }
            } catch (InterruptedException e) {
                Message msg = new NotifyDescriptor.Message("Erro: compara_imagens()", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
                e.printStackTrace();
            }
        }

        int[] pixels2 = image2 != null ? new int[width * height] : null;

        if (image2 != null) {
            try {
                PixelGrabber pg = new PixelGrabber(image2, 0, 0, width, height, pixels2, 0, width);
                pg.grabPixels();

                if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                    Message msg = new NotifyDescriptor.Message("Erro: compara_imagens()", NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(msg);
                }
            } catch (InterruptedException e) {
                Message msg = new NotifyDescriptor.Message("Erro: compara_imagens()", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
                e.printStackTrace();
            }
        }
        for (int i = 0; i < (width * height); i++) {
            if (pixels1[i] != pixels2[i]) {
                return false;
            }
        }

        return true;
    }
}
