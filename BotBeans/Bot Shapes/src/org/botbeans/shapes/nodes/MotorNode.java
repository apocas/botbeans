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
public class MotorNode extends GenericNode {

    private int distance = 20;
    private int speed = 15;

    @SuppressWarnings("unchecked")
    public MotorNode(int id, Image image) {
        this.image = image;
        this.componente = 19;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        description = "Motor";

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "distance");
            cyclesProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            cyclesProp.setName("Angle");
            cyclesProp.setShortDescription("Angle in degrees.");

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
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setDistance(Integer dd) {
        int oldatr = this.distance;
        this.distance = dd;
        fire("distance", oldatr, dd);
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
        description = "Motor actuator";
    }

    @Override
    public String getHash() {
        return getHashNode() + term + distance + term + speed;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        distance = Integer.parseInt(aux[4]);
        speed = Integer.parseInt(aux[5]);
    }

    @Override
    @SuppressWarnings({"unchecked", "fallthrough"})
    public GenericNode execute(ShapeTopComponent tp) {
        int speed = this.getSpeed();
        if (speed > 500) {
            speed = 500;
        }
        try {
            tp.send(new int[]{3, 20, speed, this.getDistance()});
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        try {
            tp.getIn().readInt();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return tp.proximoNode(this);
    }
}
