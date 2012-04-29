/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.botbeans.math.LogicExpression;
import org.botbeans.common.widgets.BotbeansWidget;
import org.botbeans.shapes.GenericEdge;
import org.botbeans.shapes.ShapeTopComponent;
import org.botbeans.shapes.StringPropertyEditor;
import org.botbeans.blocks.controller.ExpressionCanvas;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.xml.sax.SAXException;

/**
 *
 * @author Apocas
 */
public class ConditionNode extends GenericNode {

    private String condition = "a < 5";

    @SuppressWarnings("unchecked")
    public ConditionNode(int id, Image image) {
        this.image = image;
        this.componente = 11;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");


        entradas_limite = 1;
        saidas_limite = 2;

        try {
            PropertySupport.Reflection cyclesProp = new PropertySupport.Reflection<String>(this, String.class, "condition");
            cyclesProp.setPropertyEditorClass(StringPropertyEditor.class);
            cyclesProp.setName("Condition");
            cyclesProp.setShortDescription("Condition to be tested.");
            prop.put(cyclesProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        updateDescription();
    }

    public void updateDescription() {
        //description = condition;
        description = "Click here";
    }

    /**
     * @return the cycles
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setCondition(String dd) {
        String oldatr = this.condition;
        this.condition = dd;
        updateDescription(getIcon());
        fire("condition", oldatr, dd);
    }

    @Override
    public String getHash() {
        BotbeansWidget bw = (BotbeansWidget) ShapeTopComponent.getLastActivatedComponent().getScene().findWidget(this);
        //this.description = ((TestCanvas) (bw.getComponent())).getSavestring();
        //return getHashNode() + term + description;
        return getHashNode() + term + ShapeTopComponent.getLastActivatedComponent().getScene().getVariablesNamesString() + term + ((ExpressionCanvas) (bw.getComponent())).getSavestring().replaceAll("\n", "").replaceAll("\r", "");
        //return getHashNode();
    }

    @Override
    protected void processHash(String hash) {
        //String[] aux = hash.split(term);
        //condition = aux[4];
    }

    @Override
    public GenericNode execute(ShapeTopComponent tp) {
        String xml = ((ExpressionCanvas) ((BotbeansWidget) tp.getScene().findWidget(this)).getComponent()).getSavestring();

        try {
            condition = ShapesUtilities.convertXMLCondition(xml);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        }

        //new DebugDialog(xml);

        boolean condi = false;
        GenericNode node_exec = null;
        try {
            condi = LogicExpression.process(this.getCondition(), tp.getMemory());
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        for (int v = 0; v < tp.getScene().getEdges().toArray().length; v++) {
            GenericEdge edgetemp2 = (GenericEdge) tp.getScene().getEdges().toArray()[v];
            GenericNode nodeorigem2 = tp.getScene().getEdgeSource(edgetemp2);
            if (nodeorigem2.equals(this)) {
                if (edgetemp2.getTipo() == 1 && condi) {
                    //verdadeiro
                    node_exec = tp.getScene().getEdgeTarget(edgetemp2);
                } else if (edgetemp2.getTipo() == 2 && !condi) {
                    //falso
                    node_exec = tp.getScene().getEdgeTarget(edgetemp2);
                }
            }
        }
        return node_exec;
    }
}
