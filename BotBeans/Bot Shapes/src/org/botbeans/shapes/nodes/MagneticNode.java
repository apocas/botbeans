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
public class MagneticNode extends GenericNode {

    private String angle = "a";

    @SuppressWarnings("unchecked")
    public MagneticNode(int id, Image image) {
        this.image = image;
        this.componente = 9;
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
            cyclesProp.setShortDescription("Varible where the angle in degrees will be stored.");
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
            tp.send(new int[]{1, 17});
            graus = tp.getIn().readInt();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //System.out.println("Sonar: " + echo_sonar);
        tp.getMemory().put(this.getAngle(), graus);
        return tp.proximoNode(this);

//        MathExpression mexpr = new MathExpression("");
//        mexpr.add(angle);
//        mexpr.swapVars(tp.getMemory());
//        double angle_n = 0;
//        try {
//            angle_n = mexpr.value();
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//
//        for (int v = 0; v < tp.getScene().getEdges().toArray().length; v++) {
//            GenericEdge edgetemp2 = (GenericEdge) tp.getScene().getEdges().toArray()[v];
//            GenericNode nodeorigem2 = tp.getScene().getEdgeSource(edgetemp2);
//            if (nodeorigem2.equals(this)) {
//                if (edgetemp2.getTipo() == 1 && graus > angle_n) {
//                    //maior
//                    node_exec = tp.getScene().getEdgeTarget(edgetemp2);
//                } else if (edgetemp2.getTipo() == 2 && graus < angle_n) {
//                    //menor
//                    node_exec = tp.getScene().getEdgeTarget(edgetemp2);
//                }
//            }
//        }
//        return node_exec;
    }
}
