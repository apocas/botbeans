/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Toolkit;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
@ActionID(id = "org.botbeans.shapes.Bottao_stop", category = "Edit")
@ActionRegistration(displayName = "CTL_Bottao_stop")
@ActionReferences(value = {
    @ActionReference(path = "Menu/Run", position = -40),
    @ActionReference(path = "Toolbars/Grafo", position = -40),
    @ActionReference(path = "Ribbon/TaskPanes/Main/Execution", position = -40)})

public final class Bottao_stop extends CallableSystemAction {

    public void performAction() {
        ShapeTopComponent stc = ShapeTopComponent.getLastActivatedComponent();
        if (stc != null) {
            stc.para();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public String getName() {
        return NbBundle.getMessage(Bottao_stop.class, "CTL_Bottao_stop");
    }

    @Override
    protected String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_058.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
