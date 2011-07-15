/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.blocks;

import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JButton;
import org.botbeans.blocks.controller.ExpressionCanvas;
import org.botbeans.blocks.controller.WorkspaceController;
import org.botbeans.common.BotbeansUtilities;
import org.botbeans.common.widgets.BotbeansWidget;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.windows.Mode;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.botbeans.blocks//Blocks//EN",
autostore = false)
public final class BlocksTopComponent extends TopComponent {

    private static BlocksTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "BlocksTopComponent";
    private WorkspaceController wc;
    private BotbeansWidget widget;

    public BlocksTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(BlocksTopComponent.class, "CTL_BlocksTopComponent"));
        setToolTipText(NbBundle.getMessage(BlocksTopComponent.class, "HINT_BlocksTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        wc = new WorkspaceController();
        //wc.resetWorkspace();

        wc.setVariableNames(new Vector<String>());
        wc.setBotbeansWidget(null);

        wc.setLangDefFilePath(BotbeansUtilities.LANG);
        wc.loadFreshWorkspace();

        canvas.add(wc.getWorkspacePanel());
        canvas.revalidate();
    }

    public void loadWidget(BotbeansWidget widget, Vector<String> vars) {
        this.widget = widget;
        wc.setVariableNames(vars);
        wc.setLangDefFilePath(BotbeansUtilities.LANG);

        wc.loadFreshWorkspace();

        if (!((ExpressionCanvas) widget.getComponent()).getSavestring().equals("")) {
            wc.loadProject(((ExpressionCanvas) widget.getComponent()).getSavestring());
        }

        //canvas.repaint();
    }

//    @Override
//    public void requestVisible() {
//        Mode mode = WindowManager.getDefault().findMode("rightSlidingSide");
//        //Mode mode = WindowManager.getDefault().findMode("explorer");
//        if (mode != null) {
////            wc.loadFreshWorkspace();
////            this.revalidate();
////            this.repaint();
//            mode.dockInto(this);
//            //this.open();
//        }
//    }
//    @Override
//    public void open() {
//        Mode mode = WindowManager.getDefault().findMode("rightSlidingSide");
//        //Mode mode = WindowManager.getDefault().findMode("explorer");
//        if (mode != null) {
////            wc.loadFreshWorkspace();
////            this.revalidate();
////            this.repaint();
//            mode.dockInto(this);
//        }
//        //super.open();
//    }
    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();

        if (widget != null) {
            wc.sendWidget(widget);
        }

        this.widget = null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        canvas.setLayout(new java.awt.BorderLayout());
        add(canvas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel canvas;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized BlocksTopComponent getDefault() {
        if (instance == null) {
            instance = new BlocksTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the BlocksTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized BlocksTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(BlocksTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof BlocksTopComponent) {
            return (BlocksTopComponent) win;
        }
        Logger.getLogger(BlocksTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
