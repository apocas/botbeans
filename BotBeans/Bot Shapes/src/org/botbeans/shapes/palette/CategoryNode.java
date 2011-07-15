package org.botbeans.shapes.palette;

import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Apocas
 */
public class CategoryNode extends AbstractNode {

    public CategoryNode(Category category) {
        super(new ShapeChildren(category), Lookups.singleton(category));
        setDisplayName(category.getName());
    }
}
