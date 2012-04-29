/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.botbeans.blocks.controller.ExpressionCanvas;
import org.botbeans.common.widgets.BotbeansWidget;
import org.botbeans.math.MathExpression;
import org.botbeans.shapes.IntegerPropertyEditor;
import org.botbeans.shapes.ShapeTopComponent;
import org.botbeans.shapes.StringPropertyEditor;
import org.botbeans.shapes.dialogs.DebugDialog;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;
import org.xml.sax.SAXException;

/**
 *
 * @author Apocas
 */
public class VariableNode extends GenericNode {

    private String vname = "a";
    private int value = 0;

    @SuppressWarnings("unchecked")
    public VariableNode(int id, Image image) {
        this.image = image;
        this.componente = 10;
        this.id = id;

        prop.setName("properties");
        prop.setDisplayName("Properties");
        entradas_limite = 1;
        saidas_limite = 1;
        updateDescription();

        try {
            PropertySupport.Reflection nameProp = new PropertySupport.Reflection<String>(this, String.class, "vname");
            nameProp.setPropertyEditorClass(StringPropertyEditor.class);
            nameProp.setName("Name");
            nameProp.setShortDescription("Variable name, case sensitive, first character must be a letter.");

            PropertySupport.Reflection valueProp = new PropertySupport.Reflection<Integer>(this, Integer.class, "value");
            valueProp.setPropertyEditorClass(IntegerPropertyEditor.class);
            valueProp.setName("Value");
            valueProp.setShortDescription("Value for the variable, integer.");
            prop.put(nameProp);
            prop.put(valueProp);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void updateDescription() {
        //description = vname + " = " + value;
        //description = "Click here";
        description = vname;
    }

    /**
     * @return the cycles
     */
    public String getVname() {
        return vname;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setVname(String dd) {
        if (dd != null || !dd.isEmpty()) {
            char c = dd.charAt(0);
            if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                String oldatr = this.vname;
                this.vname = dd;
                updateDescription(getIcon());
                fire("vname", oldatr, dd);
            } else {
                Message msg = new NotifyDescriptor.Message("Error: first character must be a letter!", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
            }
        }
    }

    /**
     * @return the cycles
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setValue(Integer dd) {
        Integer oldatr = this.value;
        this.value = dd;
        updateDescription(getIcon());
        fire("value", oldatr, dd);
    }

    @Override
    public String getHash() {
        BotbeansWidget bw = (BotbeansWidget) ShapeTopComponent.getLastActivatedComponent().getScene().findWidget(this);
        return getHashNode() + term + ShapeTopComponent.getLastActivatedComponent().getScene().getVariablesNamesString() + term + ((ExpressionCanvas) (bw.getComponent())).getSavestring().replaceAll("\n", "").replaceAll("\r", "") + term + vname;
    }

    @Override
    protected void processHash(String hash) {
        String[] aux = hash.split(term);
        vname = aux[6];
        //value = Integer.parseInt(aux[5]);
        updateDescription();
    }

    @Override
    @SuppressWarnings("unchecked")
    public GenericNode execute(ShapeTopComponent tp) {
        if (tp.getMemory().get(this.getVname()) == null) {
            tp.getMemory().put(this.getVname(), this.getValue());
        }

        String xml = ((ExpressionCanvas) ((BotbeansWidget) tp.getScene().findWidget(this)).getComponent()).getSavestring();
        //new DebugDialog(xml);
        String condition = "";
        try {
            condition = ShapesUtilities.convertXMLCondition(xml);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        }

        MathExpression expr = new MathExpression(condition.trim());
        expr.swapVars(tp.getMemory());
        try {
            tp.getMemory().put(this.getVname(), expr.value());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //System.out.println(tp.getMemory());

        return tp.proximoNode(this);
    }
}
