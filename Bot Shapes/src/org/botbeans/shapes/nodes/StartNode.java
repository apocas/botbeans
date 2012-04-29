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
public class StartNode extends GenericNode {

    public StartNode(int id, Image image) {
        this.image = image;
        this.componente = 14;
        this.id = id;

        entradas_limite = 0;
        saidas_limite = 1;

        description = "Start";
    }

    @Override
    protected void processHash(String hash) {
    }

    @Override
    void updateDescription() {
        description = "Start";
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
