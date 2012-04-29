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
public class JunctionNode extends GenericNode {

    public JunctionNode(int id, Image image) {
        this.image = image;
        this.componente = 12;
        this.id = id;

        entradas_limite = 999;
        saidas_limite = 1;

        description = "Union";
    }

    @Override
    protected void processHash(String hash) {
    }

    @Override
    void updateDescription() {
        description = "Union";
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
