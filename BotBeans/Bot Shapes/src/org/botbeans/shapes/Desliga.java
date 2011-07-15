package org.botbeans.shapes;


import org.botbeans.blocks.controller.WorkspaceController;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class Desliga extends CallableSystemAction {

    public void performAction() {
//        TopComponent tc = TopComponent.getRegistry().getActivated();
//        if (tc instanceof ShapeTopComponent) {
//            try {
//                ((ShapeTopComponent) tc).desliga();
//            } catch (Exception ioe) {
//
//            }
//        } else {
//            Toolkit.getDefaultToolkit().beep();
//        }
        //new DebugDialog("teste");
    }

    public String getName() {
        return NbBundle.getMessage(Desliga.class, "CTL_Desliga");
    }

    protected
    @Override
    String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_025.gif";
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
