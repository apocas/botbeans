/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

@ActionID(id = "org.botbeans.shapes.ConfigurationAction", category = "Edit")
@ActionRegistration(iconInMenu = true, displayName = "#CTL_ConfigurationAction", iconBase = "org/botbeans/shapes/icons/Computer_File_097.gif")
@ActionReferences(value = {
    @ActionReference(path = "Menu/File", position = 763),
    @ActionReference(path = "Ribbon/AppMenuFooter", position = 200),
    @ActionReference(path = "Toolbars/Network", position = -50)})
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
