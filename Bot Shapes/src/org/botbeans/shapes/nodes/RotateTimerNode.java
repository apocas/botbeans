/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import java.io.IOException;
import org.botbeans.shapes.IntegerPropertyEditor;
import org.botbeans.shapes.ShapeTopComponent;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

/**
 *
 * @author Apocas
 */
public class RotateTimerNode extends GenericNode {

    private int time = 500;
    private int speed = 65;

    @SuppressWarnings("unchecked")
    public RotateTimerNode(int id, Image image, int comp) {
        this.image = image;
        this.componente = comp;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        description = "Rotate";

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "time");
            cyclesProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            cyclesProp.setName("Duration");
            cyclesProp.setShortDescription("Duration in seconds.");

            PropertySupport.Reflection speedProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "speed");
            speedProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            speedProp.setName("Speed");
            speedProp.setShortDescription("Speed in centimeters per second.");

            prop.put(cyclesProp);
            prop.put(speedProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

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
    public void setTime(Integer dd) {
        int oldatr = this.time;
        this.time = dd;
        fire("time", oldatr, dd);
    }

    /**
     * @return the cycles
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setSpeed(Integer dd) {
        int oldatr = this.speed;
        this.speed = dd;
        fire("speed", oldatr, dd);
    }

    @Override
    void updateDescription() {
        description = "Rotate";
    }

    @Override
    public String getHash() {
        return getHashNode() + term + time + term + speed;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        time = Integer.parseInt(aux[4]);
        speed = Integer.parseInt(aux[5]);
    }

    @Override
    @SuppressWarnings({"unchecked", "fallthrough"})
    public GenericNode execute(ShapeTopComponent tp) {
        int temp = 1;
        switch (this.getComponente()) {
            case 3:
                temp = -1;
            case 2:
                try {
                    tp.send(new int[]{3, 4, this.getSpeed(), temp * this.getTime()});
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
        }
        try {
            tp.getIn().readInt();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return tp.proximoNode(this);
    }
}
