/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

@ActionID(id = "org.botbeans.shapes.Abrir", category = "Edit")
@ActionRegistration(displayName = "CTL_Abrir")
@ActionReferences(value = {
    @ActionReference(path = "Menu/File", position = 100),
    @ActionReference(path = "Ribbon/AppMenu", position = -100),
    @ActionReference(path = "Ribbon/TaskBar", position = 100),
    @ActionReference(path = "Toolbars/Ficheiro", position = -250)})
public final class Abrir extends CallableSystemAction {

    @Override
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
