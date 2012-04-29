package org.botbeans.shapes;

import org.botbeans.shapes.nodes.GenericNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

/**
 *
 * @author Reinhard Umgeher
 */
public class GenericPropertyNode extends AbstractNode {

    private GenericNode node;

    /**
     * Creates a wrapper node of a given target. The wrapper node is used to
     * handle the target by the properties window.
     * @param node The given target.
     */
    public GenericPropertyNode(GenericNode node) {
        super(Children.LEAF);
        this.node = node;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set prop = Sheet.createPropertiesSet();


        prop.setName("properties");
        prop.setDisplayName("Properties");

        prop = node.getProp();

        sheet.put(prop);
        return sheet;
    }
}
