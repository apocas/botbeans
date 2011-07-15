/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Toolkit;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class Bottao_run extends CallableSystemAction {

    public void performAction() {
        ShapeTopComponent stc = ShapeTopComponent.getLastActivatedComponent();
        if (stc != null) {
            stc.executa(true);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public String getName() {
        return NbBundle.getMessage(Bottao_run.class, "CTL_Bottao_run");
    }

    @Override
    protected String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_059.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
