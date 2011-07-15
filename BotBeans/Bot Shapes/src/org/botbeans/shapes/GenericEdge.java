/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes;

import java.io.Serializable;

/**
 *
 * @author Apocas
 */
public class GenericEdge implements Serializable {

    private int tipo;

    public GenericEdge(int comp) {
        this.tipo = comp;
    }

    public GenericEdge() {
        this.tipo = 0;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
