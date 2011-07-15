/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.palette;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author pedrodias
 */
public class CategoryChildFactory extends ChildFactory<String> {

    @Override
    protected boolean createKeys(List<String> list) {
        list.addAll(Arrays.asList(ShapesUtilities.categories));
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        Node node = new AbstractNode(Children.create(new ShapeChildFactory(key), true));
        node.setDisplayName(key);
        return node;
    }

    private static class ShapeChildFactory extends ChildFactory<Shape> {

        String category = null;

        public ShapeChildFactory(String key) {
            this.category = key;
        }

        @Override
        protected boolean createKeys(List<Shape> list) {
            String[][] items = ShapesUtilities.items;
            for (int i = 0; i < items.length; i++) {
                if (category.equals(items[i][1])) {
                    Shape item = new Shape();
//                    item.setNumber(new Integer(items[i][0]));
                    item.setCategory(items[i][1]);
                    item.setImage(items[i][2]);
                    item.setTitle(items[i][3]);
                    list.add(item);
                }
            }

            return true;
        }

        @Override
        protected Node createNodeForKey(final Shape key) {
            AbstractNode node = new AbstractNode(Children.LEAF) {

                @Override
                public Transferable drag() throws IOException {
                    return key;
                }
            };
            //node.setName(key.getTitle());
            node.setIconBaseWithExtension(key.getImage());
            return node;
        }
    }
}
