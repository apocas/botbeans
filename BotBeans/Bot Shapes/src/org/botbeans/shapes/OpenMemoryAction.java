/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
id = "org.botbeans.shapes.OpenMemoryAction")
@ActionRegistration(iconBase = "org/botbeans/shapes/icons/Computer_File_056.gif",
displayName = "#CTL_OpenMemoryAction")
@ActionReferences({
     @ActionReference(path = "Ribbon/TaskPanes/Window/Windows", position = -45),
    @ActionReference(path = "Ribbon/AppMenu/Window/Windows", position = -100)
})
@Messages("CTL_OpenMemoryAction=Memory")
public final class OpenMemoryAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        TopComponent win = MemoryTopComponent.findInstance();

        Mode mode = WindowManager.getDefault().findMode("properties");
        mode.dockInto(win);
        win.open();
        win.requestActive();
    }
}
