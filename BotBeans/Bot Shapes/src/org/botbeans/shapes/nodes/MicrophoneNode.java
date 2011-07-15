/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import java.io.IOException;
import org.botbeans.math.MathExpression;
import org.botbeans.shapes.GenericEdge;
import org.botbeans.shapes.ShapeTopComponent;
import org.botbeans.shapes.StringPropertyEditor;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

/**
 *
 * @author Apocas
 */
public class MicrophoneNode extends GenericNode {

    private String angle = "a";

    @SuppressWarnings("unchecked")
    public MicrophoneNode(int id, Image image) {
        this.image = image;
        this.componente = 18;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        updateDescription();

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<String>(this, String.class, "angle");
            cyclesProp.setPropertyEditorClass(StringPropertyEditor.class);
            cyclesProp.setName("Variable");
            cyclesProp.setShortDescription("Varible where the sound intensity will be stored (dB).");
            prop.put(cyclesProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void updateDescription() {
        description = angle;
    }

    /**
     * @return the cycles
     */
    public String getAngle() {
        return angle;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setAngle(String dd) {
        String oldatr = this.angle;
        this.angle = dd;
        updateDescription(getIcon());
        fire("angle", oldatr, dd);
    }

    @Override
    public String getHash() {
        return getHashNode() + term + angle;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        angle = aux[4];
    }

    @Override
    @SuppressWarnings("unchecked")
    public GenericNode execute(ShapeTopComponent tp) {
        //GenericNode node_exec = null;
        int graus = 0;
        try {
            tp.send(new int[]{1, 19});
            graus = tp.getIn().readInt();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //System.out.println("Microphone: " + echo_sonar);
        tp.getMemory().put(this.getAngle(), graus);
        return tp.proximoNode(this);
    }
}
