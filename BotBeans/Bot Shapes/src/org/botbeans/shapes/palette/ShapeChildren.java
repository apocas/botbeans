package org.botbeans.shapes.palette;

import java.util.ArrayList;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 * 
 * @author Apocas
 */
public class ShapeChildren extends Index.ArrayChildren {

    private Category category;
    private String[][] items = ShapesUtilities.items;

    public ShapeChildren(Category Category) {
        this.category = Category;
    }

    @Override
    protected java.util.List<Node> initCollection() {
        ArrayList<Node> childrenNodes = new ArrayList<Node>(items.length);
        for (int i = 0; i < items.length; i++) {
            if (category.getName().equals(items[i][1])) {
                Shape item = new Shape();
//                item.setNumber(new Integer(items[i][0]));
                item.setCategory(items[i][1]);
                item.setImage(items[i][2]);
                ShapeNode sn = new ShapeNode(item);
                sn.setShortDescription(items[i][3]);
                childrenNodes.add(sn);
            }
        }
        return childrenNodes;
    }
}
