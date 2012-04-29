/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.blocks.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JPanel;

import javax.xml.parsers.ParserConfigurationException;
import org.botbeans.blocks.renderable.RenderableBlock;
import java.io.StringReader;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.botbeans.blocks.BlockConnectorShape;
import org.botbeans.blocks.BlockGenus;
import org.botbeans.blocks.BlockLinkChecker;
import org.botbeans.blocks.CommandRule;
import org.botbeans.blocks.SocketRule;
import org.botbeans.blocks.workspace.Page;
import org.botbeans.blocks.workspace.Workspace;
import org.botbeans.common.BotbeansUtilities;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Pedro Dias
 */
public class ExpressionCanvas extends JPanel {

    private Collection<RenderableBlock> c;
    private RenderableBlock orig;
    private String savestring = "";
    private static final Font font = new Font("Arial", Font.PLAIN, 20);

    public ExpressionCanvas() {
        //this.setBounds(this.getX(), this.getY(), 100, 100);
        setOpaque(false);
        setPreferredSize(new Dimension(200, 25));
    }

    public ExpressionCanvas(Collection<RenderableBlock> c) {
        this.c = c;
        try {
            orig = getFirst(c);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public ExpressionCanvas(String filePath, Vector<String> vnames, String save) {
        this.savestring = save;
        load(filePath, vnames, save);
    }

    private void load(String filePath, Vector<String> vnames, String save) {
        Element langDefRoot = setLangDefFilePath(filePath, vnames);

        BlockConnectorShape.loadBlockConnectorShapes(langDefRoot);
        BlockGenus.resetAllGenuses();
        BlockGenus.loadBlockGenera(langDefRoot);
        BlockLinkChecker.addRule(new CommandRule());
        BlockLinkChecker.addRule(new SocketRule());

        Workspace workspace = Workspace.getInstance();
        workspace.reset();
        workspace.loadWorkspaceFrom(null, langDefRoot);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(save)));
            Element root = doc.getDocumentElement();
            workspace.loadWorkspaceFrom(root, langDefRoot);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Page p = workspace.getBlockCanvas().getPages().get(0);
        Collection<RenderableBlock> c = p.getBlocks();

        for (RenderableBlock rb : p.getBlocks()) {
            rb.updateBuffImg();
        }

        this.c = c;
        try {
            orig = getFirst(c);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private RenderableBlock getFirst(Collection<RenderableBlock> c) throws ParserConfigurationException, SAXException, IOException {
        for (RenderableBlock rb : c) {
            String xml = rb.getSaveString();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = dBuilder.parse(is);
            if ((((Element) ((Element) doc.getElementsByTagName("Plug").item(0)).getElementsByTagName("BlockConnector").item(0)).getAttribute("con-block-id")).equals("")) {
                //long id = Long.parseLong(((Element) doc.getElementsByTagName("Block").item(0)).getAttribute("id"));
                return rb;
            }
        }

        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (c != null && !c.isEmpty()) {
//            for (RenderableBlock rb : c) {
//                g.drawImage(rb.getBuffImage(), rb.getX() - orig.getX(), rb.getY() - orig.getY(), this);
//                g.drawString(rb.blockLabel.getText(), (rb.getX() + rb.blockLabel.getJComponent().getX()) - orig.getX(), (rb.getHeight() / 2 + rb.getY() + rb.blockLabel.getJComponent().getY()) - orig.getY());
//                //rb.blockLabel.getJComponent().paint(g);
//                //System.out.println(rb.blockLabel.getText());
//            }
            BufferedImage bf = render();
            g.drawImage(bf, (this.getWidth() / 2) - (bf.getWidth() / 2), 0, this);
        }
//        else {
//            g.drawString("Press me", 50, 20);
//        }
    }

    public BufferedImage render() {
        if (orig != null) {
            BufferedImage bi = new BufferedImage(orig.getWidth(), orig.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            //g.rotate(Math.toRadians(45));

            for (RenderableBlock rb : c) {
                g.drawImage(rb.getBuffImage(), rb.getX() - orig.getX(), rb.getY() - orig.getY(), this);
                g.setFont(font);
                g.setColor(Color.BLACK);
                g.drawString(rb.blockLabel.getText(), (rb.getX() + rb.blockLabel.getJComponent().getX()) - orig.getX() + 2 + 2, (rb.getHeight() / 2 + rb.getY() + rb.blockLabel.getJComponent().getY()) - orig.getY() + 4 + 2);
                g.setColor(Color.WHITE);
                g.drawString(rb.blockLabel.getText(), (rb.getX() + rb.blockLabel.getJComponent().getX()) - orig.getX() + 2, (rb.getHeight() / 2 + rb.getY() + rb.blockLabel.getJComponent().getY()) - orig.getY() + 4);

                //rb.blockLabel.getJComponent().paint(g);
                //System.out.println(rb.blockLabel.getText());
            }

            g.dispose();
            if (bi.getWidth() < this.getWidth()) {
                return BotbeansUtilities.resize(bi, bi.getWidth(), getHeight());
            } else {
                return BotbeansUtilities.resize(bi, this.getWidth(), this.getHeight());
            }
        } else {
            return null;
        }
    }

    /**
     * @return the c
     */
    public Collection<RenderableBlock> getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(Collection<RenderableBlock> c) {
        this.c = c;
        try {
            orig = getFirst(c);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        this.setPreferredSize(new Dimension(orig.getWidth(), orig.getHeight()));
        //this.setBounds(this.getX(), this.getY(), orig.getWidth(), orig.getHeight());
        BufferedImage bufimage = new BufferedImage(orig.getWidth(), orig.getHeight(), BufferedImage.TYPE_INT_ARGB);
        paintComponent(bufimage.getGraphics());


//        try {
//            ImageIO.write(bufimage, "png", new File("xpto.png"));
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }

    }

    /**
     * @return the savestring
     */
    public String getSavestring() {
        return savestring;
    }

    /**
     * @param savestring the savestring to set
     */
    public void setSavestring(String savestring) {
        this.savestring = savestring;
    }

    /**
     * @param savestring the savestring to set
     */
    public void setSavestringAndLoad(String path, String savestring, Vector<String> vnames) {
        this.savestring = savestring;
        load(path, vnames, savestring);
    }

    private Element setLangDefFilePath(String filePath, Vector<String> vnames) {
        Element langDefRoot = null;
        String LANG_DEF_FILEPATH = filePath; //TODO do we really need to save the file path?
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            String langDefLocation = /*workingDirectory +*/ LANG_DEF_FILEPATH;
            
            //doc = builder.parse(new File(langDefLocation));
            doc = builder.parse(ExpressionCanvas.class.getClassLoader().getResourceAsStream(langDefLocation));
            langDefRoot = doc.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node genuses = langDefRoot.getElementsByTagName("BlockGenuses").item(0);
        Node drawer = langDefRoot.getElementsByTagName("BlockDrawer").item(2);
        for (String vname : vnames) {
            Element e0 = doc.createElement("BlockGenus");
            e0.setAttribute("name", vname);
            e0.setAttribute("kind", "data");
            e0.setAttribute("initlabel", vname);
            e0.setAttribute("editable-label", "no");
            e0.setAttribute("is-label-value", "yes");
            e0.setAttribute("color", "255 100 100");

            Element e1 = doc.createElement("description");
            Element e2 = doc.createElement("text");
            e2.appendChild(doc.createTextNode("Variable " + vname + "."));
            Element e3 = doc.createElement("BlockConnectors");
            Element e4 = doc.createElement("BlockConnector");
            e4.setAttribute("connector-kind", "plug");
            e4.setAttribute("connector-type", "number");
            e4.setAttribute("position-type", "mirror");
            e3.appendChild(e4);
            Element e5 = doc.createElement("LangSpecProperties");
            Element e6 = doc.createElement("LangSpecProperty");
            e6.setAttribute("key", "vm-cmd-name");
            e6.setAttribute("value", "eval-num");
            Element e7 = doc.createElement("LangSpecProperty");
            e7.setAttribute("key", "is-monitorable");
            e7.setAttribute("value", "yes");
            e5.appendChild(e6);
            e5.appendChild(e7);
            e0.appendChild(e1);
            e0.appendChild(e3);
            e0.appendChild(e5);
            genuses.appendChild(e0);

            Element ee0 = doc.createElement("BlockGenusMember");
            ee0.appendChild(doc.createTextNode(vname));
            drawer.appendChild(ee0);
        }


//        Transformer transformer = null;
//        try {
//            transformer = TransformerFactory.newInstance().newTransformer();
//        } catch (TransformerConfigurationException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        StreamResult result = new StreamResult(new StringWriter());
//        DOMSource source = new DOMSource(doc);
//        try {
//            transformer.transform(source, result);
//        } catch (TransformerException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        String xmlString = result.getWriter().toString();
//        Message msg = new NotifyDescriptor.Message(xmlString, NotifyDescriptor.ERROR_MESSAGE);
//        DialogDisplayer.getDefault().notify(msg);

        return langDefRoot;
    }
}
