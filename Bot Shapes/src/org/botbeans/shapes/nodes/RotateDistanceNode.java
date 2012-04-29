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
public class RotateDistanceNode extends GenericNode {

    private int angle = 90;
    private int speed = 65;

    @SuppressWarnings("unchecked")
    public RotateDistanceNode(int id, Image image, int comp) {
        this.image = image;
        this.componente = comp;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        description = "Rotate";

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "angle");
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
    public Integer getAngle() {
        return angle;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setAngle(Integer dd) {
        int oldatr = this.angle;
        this.angle = dd;
        fire("angle", oldatr, dd);
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
        return getHashNode() + term + angle + term + speed;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        angle = Integer.parseInt(aux[4]);
        speed = Integer.parseInt(aux[5]);
    }

    @Override
    @SuppressWarnings({"unchecked", "fallthrough"})
    public GenericNode execute(ShapeTopComponent tp) {
        int temp = 1;
        switch (this.getComponente()) {
            case 7:
                temp = -1;
            case 6:
                try {
                    tp.send(new int[]{3, 3, this.getSpeed(), temp * this.getAngle()});
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
