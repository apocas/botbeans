/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.common.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.Serializable;
import javax.swing.JPanel;
import org.netbeans.api.visual.laf.LookFeel;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author pedrodias
 */
public class BotbeansWidget extends Widget implements Serializable {

    private BotImageWidget imageWidget;
    private LabelWidget labelWidget;
    private ComponentWidget componentWidget;

    public BotbeansWidget(Scene scene, boolean label) {
        super(scene);
        LookFeel lookFeel = getScene().getLookFeel();

        setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, -lookFeel.getMargin() + 1));
        //setLayout(LayoutFactory.createAbsoluteLayout());

        JPanel jp = new JPanel(new BorderLayout());
        jp.setOpaque(false);
        
        imageWidget = new BotImageWidget(scene);
        //imageWidget.setPreferredLocation(new Point(0, 0));
        addChild(imageWidget);

        componentWidget = new ComponentWidget(scene, jp);
        addChild(componentWidget);
        
        labelWidget = new LabelWidget(scene);
        //labelWidget.setPreferredLocation(new Point(5, 90));
        labelWidget.setFont(scene.getDefaultFont().deriveFont(14.0f));

        if (label == true) {
            addChild(labelWidget);
        }

        setState(ObjectState.createNormal());
    }

    public boolean isLabelActive() {
        for (Widget w : this.getChildren()) {
            if (w instanceof LabelWidget) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        LookFeel lookFeel = getScene().getLookFeel();
        labelWidget.setBorder(lookFeel.getBorder(state));
        labelWidget.setForeground(lookFeel.getForeground(state));
    }

    public void setLabel(String l) {
        labelWidget.setLabel(l);
    }

    public void setImage(Image im) {
        imageWidget.setImage(im);
    }

    public Component getComponent() {
        if (getComponentWidget() == null) {
            return null;
        } else {
            JPanel j = (JPanel) getComponentWidget().getComponent();
            if (j.getComponentCount() > 0) {
                return j.getComponent(0);
            } else {
                return null;
            }
        }
    }

    public void setComponent(Component c) {
        if (getComponentWidget() != null) {
            ((JPanel) getComponentWidget().getComponent()).removeAll();
            ((JPanel) getComponentWidget().getComponent()).add(c);
            ((JPanel) getComponentWidget().getComponent()).revalidate();
            ((JPanel) getComponentWidget().getComponent()).repaint();
        }
    }

    public LabelWidget getLabelWidget() {
        return labelWidget;
    }

    public BotImageWidget getImageWidget() {
        return imageWidget;
    }

    /**
     * @return the componentWidget
     */
    public ComponentWidget getComponentWidget() {
        return componentWidget;
    }
}
