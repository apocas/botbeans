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
public class SonarNode extends GenericNode {

    private String distance = "a";

    @SuppressWarnings("unchecked")
    public SonarNode(int id, Image image) {
        this.image = image;
        this.componente = 8;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        updateDescription();

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<String>(this, String.class, "distance");
            cyclesProp.setPropertyEditorClass(StringPropertyEditor.class);
            cyclesProp.setName("Variable");
            cyclesProp.setShortDescription("Varible where the distance in centimeters will be stored.");
            prop.put(cyclesProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void updateDescription() {
        description = distance;
    }

    /**
     * @return the cycles
     */
    public String getDistance() {
        return distance;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setDistance(String dd) {
        String oldatr = this.distance;
        this.distance = dd;
        updateDescription(getIcon());
        fire("distance", oldatr, dd);
    }

    @Override
    public String getHash() {
        return getHashNode() + term + distance;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        distance = aux[4];
    }

    @Override
    @SuppressWarnings("unchecked")
    public GenericNode execute(ShapeTopComponent tp) {
        GenericNode node_exec = null;
        int dist = 0;
        try {
            tp.send(new int[]{1, 16});
            dist = tp.getIn().readInt();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //System.out.println("Sonar: " + echo_sonar);


        tp.getMemory().put(this.getDistance(), dist);
        return tp.proximoNode(this);


//        MathExpression mexpr = new MathExpression("");
//        mexpr.add(distance);
//        mexpr.swapVars(tp.getMemory());
//        double distance_n = 0;
//        try {
//            distance_n = mexpr.value();
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//
//        for (int v = 0; v < tp.getScene().getEdges().toArray().length; v++) {
//            GenericEdge edgetemp2 = (GenericEdge) tp.getScene().getEdges().toArray()[v];
//            GenericNode nodeorigem2 = tp.getScene().getEdgeSource(edgetemp2);
//            if (nodeorigem2.equals(this)) {
//                if (edgetemp2.getTipo() == 1 && dist > distance_n) {
//                    //maior
//                    node_exec = tp.getScene().getEdgeTarget(edgetemp2);
//                } else if (edgetemp2.getTipo() == 2 && dist <= distance_n) {
//                    //menor
//                    node_exec = tp.getScene().getEdgeTarget(edgetemp2);
//                }
//            }
//        }
//        return node_exec;
    }
}
