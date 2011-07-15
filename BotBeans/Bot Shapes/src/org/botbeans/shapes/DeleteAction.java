/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.windows.TopComponent;

public final class DeleteAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc instanceof ShapeTopComponent) {
            try {
                ((ShapeTopComponent) tc).deleteWidget();
            } catch (Exception ioe) {
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
