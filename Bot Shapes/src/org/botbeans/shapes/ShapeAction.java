package org.botbeans.shapes;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * 
 * @author Apocas
 */
public class ShapeAction extends AbstractAction {

    public ShapeAction() {
        super(NbBundle.getMessage(ShapeAction.class, "CTL_ShapeAction"));
        //putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ShapeTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = ShapeTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
