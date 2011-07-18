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

@ActionID(id = "org.botbeans.shapes.DeleteAction", category = "Edit")
@ActionRegistration(iconInMenu = true, displayName = "#CTL_DeleteAction", iconBase = "org/botbeans/shapes/icons/Computer_File_028.gif")
@ActionReferences(value = {
    @ActionReference(path = "Shortcuts", name = "DELETE"),
    @ActionReference(path = "Toolbars/Editar", position = -950),
    @ActionReference(path = "Ribbon/TaskPanes/Main/Edit", position = -950)})
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
