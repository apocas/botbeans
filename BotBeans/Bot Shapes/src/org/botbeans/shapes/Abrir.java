/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class Abrir extends CallableSystemAction {

    public void performAction() {
        ShapeTopComponent tc = new ShapeTopComponent();
        if (tc.abre() == -1) {
            tc.close();
        } else {
            tc.open();
            tc.requestActive();
        }

        tc.getScene().bringMaintoFront();

        //ShapesUtilities.loadBlock(tc.getProjectName());
        ShapesUtilities.updateBlocks();
    }

    public String getName() {
        return NbBundle.getMessage(Abrir.class, "CTL_Abrir");
    }

    @Override
    protected String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_064.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
