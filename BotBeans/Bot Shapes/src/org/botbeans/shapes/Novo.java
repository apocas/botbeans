/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

@ActionID(id = "org.botbeans.shapes.Novo", category = "Edit")
@ActionRegistration(displayName = "CTL_Novo")
@ActionReferences(value = {
    @ActionReference(path = "Menu/File", position = -100),
    @ActionReference(path = "Ribbon/AppMenu", position = -100),
    @ActionReference(path = "Ribbon/TaskBar", position = -100),
    @ActionReference(path = "Toolbars/Ficheiro", position = -400)})
public final class Novo extends CallableSystemAction {

    int contador = 1;

    public void performAction() {
        ShapeTopComponent tc = new ShapeTopComponent();
        tc.setName(NbBundle.getMessage(ShapeTopComponent.class, "CTL_ShapeTopComponent") + " " + contador);
        tc.setProjectName(NbBundle.getMessage(ShapeTopComponent.class, "CTL_ShapeTopComponent") + " " + contador);
        tc.open();
        tc.requestActive();
        contador++;

        tc.getScene().bringMaintoFront();
        tc.getScene().addStartStop();

        ShapesUtilities.updateBlocks();
    }

    public String getName() {
        return NbBundle.getMessage(Novo.class, "CTL_Novo");
    }

    protected @Override
    String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_053.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected @Override
    boolean asynchronous() {
        return false;
    }
}
