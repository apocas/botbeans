/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import java.util.Set;
import org.botbeans.shapes.ShapeTopComponent;
import org.botbeans.shapes.StringPropertyEditor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.nodes.PropertySupport;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Apocas
 */
public class FunctionNode extends GenericNode {

    public String fname = "Project Name";

    @SuppressWarnings("unchecked")
    public FunctionNode(int id, Image image) {
        this.image = image;
        this.componente = 16;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");

        entradas_limite = 1;
        saidas_limite = 1;

        description = "";


        try {
            PropertySupport.Reflection nameProp = new PropertySupport.Reflection<String>(this, String.class, "fname");
            nameProp.setPropertyEditorClass(StringPropertyEditor.class);
            nameProp.setName("Name");
            nameProp.setShortDescription("Name of the other project to link to.");
            prop.put(nameProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void processHash(String hash) {
        fname = hash.split(term)[4];
    }

    @Override
    void updateDescription() {
        description = fname;
    }

    @Override
    public String getHash() {
        return getHashNode() + term + fname;
    }

    @Override
    public GenericNode execute(final ShapeTopComponent tp) {
        Set<TopComponent> comps = TopComponent.getRegistry().getOpened();
        for (final TopComponent tpc : comps) {
            if (tpc instanceof ShapeTopComponent) {
                if (((ShapeTopComponent) tpc).getProjectName().equals(fname)) {
                    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

                        public void run() {
                            tpc.requestActive();
                        }
                    });
                    ShapeTopComponent stpc = (ShapeTopComponent) tpc;
                    stpc.setRunDialog(tp.getRunDialog());
                    //stpc.mergeMemory(tp.getMemory());
                    stpc.executa(false);

                    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

                        public void run() {
                            tp.requestActive();
                        }
                    });
                    return tp.proximoNode(this);
                }
            }
        }

        Message msg = new NotifyDescriptor.Message("Couldnt find the targeted project in function node.", NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(msg);

        return null;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setFname(String dd) {
        if (dd != null || !dd.isEmpty()) {
            char c = dd.charAt(0);
            if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                String oldatr = this.fname;
                this.fname = dd;
                updateDescription(getIcon());
                fire("vname", oldatr, dd);
            } else {
                Message msg = new NotifyDescriptor.Message("Error: first character must be a letter!", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
            }
        }
    }

    public String getFname() {
        return fname;
    }
}
