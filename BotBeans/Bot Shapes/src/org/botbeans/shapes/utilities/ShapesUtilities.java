/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.utilities;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.botbeans.common.BotbeansUtilities;
import org.botbeans.shapes.ShapeTopComponent;
import org.botbeans.shapes.palette.Shape;
import org.netbeans.spi.palette.PaletteController;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Apocas
 */
public class ShapesUtilities {

    public static final String categories[] = new String[]{"Control", "Sensors", "Logic", "Data", "Misc", "Custom blocks"};
    public static final String icons_path = "org/botbeans/shapes/icons/";
    public static final String figures_path = "org/botbeans/shapes/palette/figures/";
    public static final String figures[] = new String[]{
        ShapesUtilities.figures_path + "frente.png",//0/
        ShapesUtilities.figures_path + "tras.png",//1/
        ShapesUtilities.figures_path + "esquerda.png",//2/
        ShapesUtilities.figures_path + "direita.png",//3/
        ShapesUtilities.figures_path + "frenteen.png",//4/
        ShapesUtilities.figures_path + "trasen.png",//5/
        ShapesUtilities.figures_path + "esquerdaen.png",//6/
        ShapesUtilities.figures_path + "direitaen.png",//7/
        ShapesUtilities.figures_path + "sonar.png",//8/
        ShapesUtilities.figures_path + "compass.png",//9/
        ShapesUtilities.figures_path + "variable.png",//10/
        ShapesUtilities.figures_path + "condition.png",//11/
        ShapesUtilities.figures_path + "junction.png",//12/
        ShapesUtilities.figures_path + "timer.png",//13/
        ShapesUtilities.figures_path + "start.png",//14/
        ShapesUtilities.figures_path + "stop.png",//15/
        ShapesUtilities.figures_path + "function.png",//16/
        ShapesUtilities.figures_path + "sound.png",//17/
        ShapesUtilities.figures_path + "microphone.png",//18/
        ShapesUtilities.figures_path + "motor.png",//19/
    };
    public static final String[][] items = new String[][]{
        {"0", categories[0], figures[0], "Move forward time based."},
        {"1", categories[0], figures[1], "Move backward time based."},
        {"2", categories[0], figures[2], "Rotate left time based."},
        {"3", categories[0], figures[3], "Rotate right time based."},
        {"4", categories[0], figures[4], "Move forward distance based."},
        {"5", categories[0], figures[5], "Move backward distance based."},
        {"6", categories[0], figures[6], "Rotate left in degrees."},
        {"7", categories[0], figures[7], "Rotate right in degrees."},
        {"0", categories[1], figures[8], "Ultrasonic sensor."},
        {"1", categories[1], figures[9], "Compass sensor."},
        {"2", categories[1], figures[18], "Microphone."},
        {"0", categories[3], figures[10], "Variable."},
        {"0", categories[2], figures[11], "Decision."},
        {"1", categories[2], figures[12], "Junction."},
        {"2", categories[2], figures[14], "Starting point."},
        {"3", categories[2], figures[15], "Finishing point."},
        {"0", categories[4], figures[13], "Timer."},
        {"1", categories[4], figures[16], "Function."},
        {"2", categories[4], figures[17], "Speaker."},
        {"3", categories[4], figures[19], "Motor."}
    };

    public static int identifyPic(Image image) {
        Image[] images = new Image[figures.length];
        for (int i = 0; i < figures.length; i++) {
            try {
                //images[i] = ImageIO.read(files[i]);
                images[i] = ImageUtilities.loadImage(figures[i], true);
            } catch (Exception ex) {
                Message msg = new NotifyDescriptor.Message("Erro: identifica()", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
                ex.printStackTrace();
            }
        }

        int aux = -1;

        for (int i = 0; i < figures.length; i++) {
            if (BotbeansUtilities.compareImages(image, images[i])) {
                aux = i;
                break;
            }
        }
        return aux;

    }

    public static String convertXMLCondition(String xml) throws ParserConfigurationException, IOException, SAXException {
        String expr = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = dBuilder.parse(is);

        Element head = getElementbyID(doc, getHead(doc));

        //System.out.println(head.getAttribute("id"));
        //System.out.println(getLeft(doc, head));
        //System.out.println(getRight(doc, head));

        expr = getFamily(doc, head);
        expr = (String) expr.subSequence(1, expr.length() - 1);
        //System.out.println(expr);
        //new DebugDialog(expr);
        return expr;
    }

    private static String getFamily(Document doc, Element e) {
        if (e != null) {
            //System.out.println(getLabel(doc, e));
            try {
                return "(" + getFamily(doc, getElementbyID(doc, getLeft(doc, e))) + " " + getLabel(doc, e) + " " + getFamily(doc, getElementbyID(doc, getRight(doc, e))) + ")";
            } catch (ParserConfigurationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return "";
    }

    private static String getLabel(Document doc, Element e) {
        String name = e.getAttribute("genus-name");
        if (name.equals("sum")) {
            return "+";
        } else if (name.equals("greaterthan")) {
            return ">";
        } else if (name.equals("lessthan")) {
            return "<";
        } else {
            if (e.getAttribute("genus-name").equals("number") && e.getElementsByTagName("Label").getLength() == 0) {
                return "1";
            } else if (e.getAttribute("genus-name").equals("sonar") && e.getElementsByTagName("Label").getLength() == 0) {
                return "sonar";
            } else if (e.getAttribute("genus-name").equals("compass") && e.getElementsByTagName("Label").getLength() == 0) {
                return "compass";
            } else {
                try {
                    return e.getElementsByTagName("Label").item(0).getChildNodes().item(0).getNodeValue();
                } catch (Exception ee) {
                    return name;
                }
            }
        }
    }

    private static String getLeft(Document doc, Element e) {
        if (e.getElementsByTagName("Sockets").getLength() > 0) {
            return ((Element) (((Element) e.getElementsByTagName("Sockets").item(0)).getElementsByTagName("BlockConnector").item(0))).getAttribute("con-block-id");
        }
        return "";
    }

    private static String getRight(Document doc, Element e) {
        if (e.getElementsByTagName("Sockets").getLength() > 0) {
            return ((Element) (((Element) e.getElementsByTagName("Sockets").item(0)).getElementsByTagName("BlockConnector").item(1))).getAttribute("con-block-id");
        }
        return "";
    }

    private static String getFather(Document doc, Element e) {
        return ((Element) ((Element) e.getElementsByTagName("Plug").item(0)).getElementsByTagName("BlockConnector").item(0)).getAttribute("con-block-id");
    }

    private static String getBrother(Document doc, Element ee) {
        NodeList nl = doc.getElementsByTagName("Block");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if (((Element) ((Element) e.getElementsByTagName("Plug").item(0)).getElementsByTagName("BlockConnector").item(0)).getAttribute("con-block-id").equals(getFather(doc, ee))) {
                return e.getAttribute("id");
            }
        }
        return "";
    }

    private static String getHead(Document doc) {
        NodeList nl = doc.getElementsByTagName("Block");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if ((((Element) ((Element) e.getElementsByTagName("Plug").item(0)).getElementsByTagName("BlockConnector").item(0)).getAttribute("con-block-id")).equals("")) {
                return e.getAttribute("id");
            }
        }
        return "";
    }

    private static Element getElementbyID(Document doc, String id) throws ParserConfigurationException, IOException, SAXException {
        NodeList nl = doc.getElementsByTagName("Block");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if (e.getAttribute("id").equals(id)) {
                return e;
            }
        }
        return null;
    }

    public static void loadBlock(String name) {
        if (!findBlock(name)) {
            for (TopComponent t : TopComponent.getRegistry().getOpened()) {
                if (t instanceof ShapeTopComponent) {
                    PaletteController pc = ((ShapeTopComponent) t).getPaletteController();
                    AbstractNode c = pc.getRoot().lookup(AbstractNode.class);
                    Children ch = c.getChildren();

                    final Shape item = new Shape();
                    item.setCategory(ShapesUtilities.categories[5]);
                    item.setImage(ShapesUtilities.items[17][2]);
                    item.setTitle(name);
                    AbstractNode node = new AbstractNode(Children.LEAF) {

                        @Override
                        public Transferable drag() throws IOException {
                            return item;
                        }
                    };
                    node.setName(name);
                    node.setIconBaseWithExtension(ShapesUtilities.items[17][2]);
                    ((AbstractNode) ch.getNodes()[5]).getChildren().add(new Node[]{node});
                }
            }
        }
    }

    public static boolean findBlock(String name) {
        for (TopComponent t : TopComponent.getRegistry().getOpened()) {
            if (t instanceof ShapeTopComponent) {
                PaletteController pc = ((ShapeTopComponent) t).getPaletteController();
                AbstractNode c = pc.getRoot().lookup(AbstractNode.class);
                Children ch = c.getChildren();

                for (Node n : ((AbstractNode) ch.getNodes()[5]).getChildren().getNodes()) {
                    if (name.equals(n.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void removeBlock(String name) {
        for (TopComponent t : TopComponent.getRegistry().getOpened()) {
            if (t instanceof ShapeTopComponent) {
                PaletteController pc = ((ShapeTopComponent) t).getPaletteController();
                AbstractNode c = pc.getRoot().lookup(AbstractNode.class);
                Children ch = c.getChildren();

                for (Node n : ((AbstractNode) ch.getNodes()[5]).getChildren().getNodes()) {
                    if (name.equals(n.getName())) {
                        ((AbstractNode) ch.getNodes()[5]).getChildren().remove(new Node[]{n});
                    }
                }
            }
        }
    }

    public static void addBlocks(ArrayList<String> ns) {
        for (String s : ns) {
            loadBlock(s);
        }
    }

    public static void updateBlocks() {
        for (TopComponent t : TopComponent.getRegistry().getOpened()) {
            if (t instanceof ShapeTopComponent) {
                ((ShapeTopComponent) t).removeCustomBlocks();
            }
        }
        addBlocks(ShapeTopComponent.getProjectNames());
    }
}
