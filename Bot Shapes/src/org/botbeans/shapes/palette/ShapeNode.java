package org.botbeans.shapes.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 * 
 * @author Apocas
 */
public class ShapeNode extends AbstractNode {

    private Shape shape;

    public ShapeNode(Shape key) {
        super(Children.LEAF, Lookups.fixed(new Object[]{key}));
        this.shape = key;
        setIconBaseWithExtension(key.getImage());
    }

//    public int getNumero() {
//        return shape.getNumber();
//    }
}
