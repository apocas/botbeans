package org.botbeans.shapes.palette;

import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * 
 * @author Apocas
 */
public class CategoryChildren extends Children.Keys {

    private String[] Categories = ShapesUtilities.categories;

    public CategoryChildren() {
    }

    protected Node[] createNodes(Object key) {
        Category obj = (Category) key;
        return new Node[]{new CategoryNode(obj)};
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addNotify() {
        super.addNotify();
        Category[] objs = new Category[Categories.length];
        for (int i = 0; i < objs.length; i++) {
            Category cat = new Category();
            cat.setName(Categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }
}
