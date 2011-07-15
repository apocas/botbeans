/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.awt.Toolkit;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JButton;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.botbeans.shapes//Memoria//EN",
autostore = false)
public final class MemoriaTopComponent extends TopComponent {

    private static MemoriaTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "MemoriaTopComponent";
    private Vector<JButton> variables = new Vector<JButton>();

    public MemoriaTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MemoriaTopComponent.class, "CTL_MemoriaTopComponent"));
        setToolTipText(NbBundle.getMessage(MemoriaTopComponent.class, "HINT_MemoriaTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        //putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        this.setName("Start Window");

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/botbeans/shapes/icons/Start.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(MemoriaTopComponent.class, "MemoriaTopComponent.jButton1.text")); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(200, 100));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ShapeTopComponent stc = ShapeTopComponent.getLastActivatedComponent();
        if (stc != null) {
            stc.executa(true);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized MemoriaTopComponent getDefault() {
        if (instance == null) {
            instance = new MemoriaTopComponent();
        }
        return instance;
    }

    public void addVariable(String var) {
        JButton jb = new JButton(var);
        variables.add(jb);
        jPanel1.add(jb);
    }

    /**
     * Obtain the MemoriaTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized MemoriaTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MemoriaTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MemoriaTopComponent) {
            return (MemoriaTopComponent) win;
        }
        Logger.getLogger(MemoriaTopComponent.class.getName()).warning(
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
