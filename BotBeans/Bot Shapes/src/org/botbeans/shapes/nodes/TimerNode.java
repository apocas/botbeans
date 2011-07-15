/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import org.botbeans.shapes.IntegerPropertyEditor;
import org.botbeans.shapes.ShapeTopComponent;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

/**
 *
 * @author Apocas
 */
public class TimerNode extends GenericNode {

    private int time = 5000;

    @SuppressWarnings("unchecked")
    public TimerNode(int id, Image image) {
        this.image = image;
        this.componente = 13;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 5;
        saidas_limite = 1;

        updateDescription();

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "time");
            cyclesProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            cyclesProp.setName("Time");
            cyclesProp.setShortDescription("Time for sleep, in milliseconds.");
            prop.put(cyclesProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

    }

    public void updateDescription() {
        description = time + " ms";
    }

    /**
     * @return the cycles
     */
    public Integer getTime() {
        return time;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setTime(Integer cycles) {
        int oldatr = this.time;
        this.time = cycles;
        updateDescription(getIcon());
        fire("time", oldatr, cycles);
    }

    @Override
    public String getHash() {
        return getHashNode() + term + time;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        time = Integer.parseInt(aux[4]);
    }

    @Override
    public GenericNode execute(ShapeTopComponent tp) {
        try {
            Thread.sleep(this.getTime());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        return tp.proximoNode(this);
    }
}
