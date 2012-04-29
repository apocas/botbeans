/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.nodes;

import java.awt.Image;
import org.botbeans.shapes.ShapeTopComponent;

/**
 *
 * @author Apocas
 */
public class FinishNode extends GenericNode {

    public FinishNode(int id, Image image) {
        this.image = image;
        this.componente = 15;
        this.id = id;

        entradas_limite = 999;
        saidas_limite = 0;

        description = "End";
    }

    @Override
    protected void processHash(String hash) {
    }

    @Override
    void updateDescription() {
        description = "End";
    }

    @Override
    public String getHash() {
        return getHashNode();
    }

    @Override
    public GenericNode execute(ShapeTopComponent tp) {
        return tp.proximoNode(this);
    }
}
