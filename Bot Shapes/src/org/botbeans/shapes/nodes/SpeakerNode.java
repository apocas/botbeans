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
public class SpeakerNode extends GenericNode {

    private int frequency = 500;
    private int duration = 2;

    @SuppressWarnings("unchecked")
    public SpeakerNode(int id, Image image) {
        this.image = image;
        this.componente = 17;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        description = "Speaker";

        try {
            PropertySupport.Reflection frequencyProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "frequency");
            frequencyProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            frequencyProp.setName("Frequency");
            frequencyProp.setShortDescription("Frequency of sound.");

            PropertySupport.Reflection durationProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "duration");
            durationProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            durationProp.setName("Duration");
            durationProp.setShortDescription("Duration.");

            prop.put(frequencyProp);
            prop.put(durationProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @return the cycles
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setFrequency(Integer dd) {
        int oldatr = this.frequency;
        this.frequency = dd;
        fire("frequency", oldatr, dd);
    }

    /**
     * @return the cycles
     */
    public Integer getFrequency() {
        return frequency;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setDuration(Integer dd) {
        int oldatr = this.duration;
        this.duration = dd;
        fire("duration", oldatr, dd);
    }

    @Override
    void updateDescription() {
        description = "Speaker";
    }

    @Override
    public String getHash() {
        return getHashNode() + term + frequency + term + duration;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        frequency = Integer.parseInt(aux[4]);
        duration = Integer.parseInt(aux[5]);
    }

    @Override
    @SuppressWarnings({"unchecked", "fallthrough"})
    public GenericNode execute(ShapeTopComponent tp) {

        try {
            tp.send(new int[]{3, 18, this.getFrequency(), this.getDuration() * 1000});
            try {
                tp.getIn().readInt();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return tp.proximoNode(this);
    }
}
