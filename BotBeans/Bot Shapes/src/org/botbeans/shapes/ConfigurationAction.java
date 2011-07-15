/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.windows.TopComponent;

public final class ConfigurationAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc instanceof ShapeTopComponent) {
            try {
                ((ShapeTopComponent) tc).config();
            } catch (Exception ioe) {
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
