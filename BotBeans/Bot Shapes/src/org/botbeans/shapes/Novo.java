package org.botbeans.shapes;

import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

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

    protected
    @Override
    String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_053.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected
    @Override
    boolean asynchronous() {
        return false;
    }
}
