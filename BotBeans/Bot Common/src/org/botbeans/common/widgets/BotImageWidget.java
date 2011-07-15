/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.common.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author apocas
 */
public class BotImageWidget extends ImageWidget {

    private String label = "";

    public BotImageWidget(Scene scene) {
        super(scene);
    }

    @Override
    protected void paintWidget() {
        super.paintWidget();
        Graphics2D g = this.getGraphics();
        //g.setColor(Color.ORANGE);
        g.drawString(label, this.getBounds().width / 2, this.getBounds().height / 2 - 10);
    }

    public void setLabel(String l) {
        this.label = l;
    }

    public String getLabel() {
        return label;
    }
}
