package org.botbeans.shapes;

import java.util.Set;
import org.botbeans.control.ServerTopComponent;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(id = "org.botbeans.shapes.Liga", category = "Edit")
@ActionRegistration(displayName = "CTL_Liga")
@ActionReferences(value = {
    @ActionReference(path = "Ribbon/AppMenu", position = -100),
    @ActionReference(path = "Ribbon/TaskBar", position = 100),
    @ActionReference(path = "Toolbars/Network", position = -100)})
public final class Liga extends CallableSystemAction {

    public void performAction() {
//        TopComponent tc = TopComponent.getRegistry().getActivated();
//        if (tc instanceof ShapeTopComponent) {
//            try {
//                ((ShapeTopComponent) tc).liga();
//                ((ShapeTopComponent) tc).getScene().validate();
//                ((ShapeTopComponent) tc).getScene().revalidate();
//                ((ShapeTopComponent) tc).getScene().repaint();
//            } catch (Exception ioe) {
//            }
//        } else {
//            Toolkit.getDefaultToolkit().beep();
//        }


//        NotifyDescriptor d = new NotifyDescriptor.Message("teste", NotifyDescriptor.WARNING_MESSAGE);
//        DialogDisplayer.getDefault().notify(d);

//        Set<TopComponent> tcs = TopComponent.getRegistry().getOpened();
//        for (TopComponent tc : tcs) {
//            DebugDialog dg = new DebugDialog("" + tc.toString() + " - " + tc.getClass());
//        }

//        for (Node n : TopComponent.getRegistry().getCurrentNodes()) {
//            DebugDialog dg = new DebugDialog("" + n.toString() + " - " + n.getClass());
//        }

        //Main m = new Main();
        //m.setVisible(true);


        Set<TopComponent> tcs = TopComponent.getRegistry().getOpened();
        boolean flag = true;
        for (TopComponent tc : tcs) {
            if (tc instanceof ServerTopComponent) {
                tc.requestActive();
                flag = false;
                break;
            }
        }

        if (flag) {
            Mode mode = WindowManager.getDefault().findMode("rightSlidingSide");
            ServerTopComponent stc = ServerTopComponent.findInstance();
            mode.dockInto(stc);
            stc.open();
            stc.requestActive();
        }
    }

    public String getName() {
        return NbBundle.getMessage(Liga.class, "CTL_Liga");
    }

    protected @Override
    String iconResource() {
        return ShapesUtilities.icons_path + "Computer_File_024.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected @Override
    boolean asynchronous() {
        return false;
    }
}
