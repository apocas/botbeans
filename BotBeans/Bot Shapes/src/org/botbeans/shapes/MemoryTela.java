/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.JPanel;
import org.openide.util.ImageUtilities;

/**
 *
 * @author pedrodias
 */
public class MemoryTela extends JPanel {

    private HashMap memory = null;
    private Image img;

    public MemoryTela(HashMap m) {
        img = ImageUtilities.loadImage("org/botbeans/shapes/palette/figures/variable.png");

        if (m == null) {
//            memory = new HashMap();
//            memory.put("a", 2);
//            memory.put("bbbb", 3);
//            memory.put("c", 16);
//            memory.put("aa", 2);
//            memory.put("bbb", 3);
//            memory.put("ccc", 16);
        } else {
            memory = m;
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (memory != null && !memory.isEmpty()) {
            int hi = this.getHeight();
            super.paintComponent(grphcs);
            int dy = hi / getMemory().size();
            if (dy > 75) {
                dy = 75;
            }
            for (int i = 0; i < getMemory().size(); i++) {
                String s = getMemory().keySet().toArray()[i] + " = ";
                grphcs.setFont(new Font("Monospaced", Font.BOLD, 20));
                grphcs.drawString(s, 10, (dy * i) + (dy / 2) + 5);
                int sw = grphcs.getFontMetrics().stringWidth(s);


                grphcs.setFont(new Font("Monospaced", Font.BOLD, 30));
                String ss = "" + getMemory().values().toArray()[i];
                int sv = grphcs.getFontMetrics().stringWidth(ss);
                int aux = (this.getWidth() - sw) / 2;

                grphcs.drawImage(getImg(), aux + sw - (getImg().getWidth(this) / 2), dy * i, dy, dy, this);


                grphcs.drawString(ss, aux + sw - (sv / 2), (dy * i) + (dy / 2) + 10);
                grphcs.drawLine(0, dy * i, this.getWidth(), dy * i);
            }
            grphcs.drawLine(0, dy * getMemory().size(), this.getWidth(), dy * getMemory().size());
        }
    }

    /**
     * @return the memory
     */
    public HashMap getMemory() {
        return memory;
    }

    /**
     * @param memory the memory to set
     */
    public void setMemory(HashMap memory) {
        this.memory = memory;
    }

    /**
     * @return the img
     */
    public Image getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(Image img) {
        this.img = img;
    }
}
