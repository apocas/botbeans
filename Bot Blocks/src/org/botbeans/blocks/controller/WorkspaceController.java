package org.botbeans.blocks.controller;

import com.intel.bluetooth.Utils;
import org.botbeans.common.widgets.BotbeansWidget;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.botbeans.blocks.workspace.SearchableContainer;
import org.botbeans.blocks.workspace.TrashCan;
import org.botbeans.blocks.workspace.Workspace;
import org.botbeans.blocks.BlockConnectorShape;
import org.botbeans.blocks.BlockGenus;
import org.botbeans.blocks.BlockLinkChecker;
import org.botbeans.blocks.CommandRule;
import org.botbeans.blocks.SocketRule;
import java.util.Collection;
import org.w3c.dom.Node;
import org.botbeans.blocks.renderable.RenderableBlock;
import org.botbeans.blocks.workspace.BlockCanvas;
import org.botbeans.blocks.workspace.Page;
import org.botbeans.common.BotbeansUtilities;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.ImageUtilities;

/**
 * 
 * The WorkspaceController is the starting point for any program using Open Blocks.
 * It contains a Workspace (the block programming area) as well as the Factories
 * (the palettes of blocks), and is responsible for setting up and laying out
 * the overall window including loading some WorkspaceWidgets like the TrashCan.
 * 
 * @author Ricarose Roque
 */
public class WorkspaceController {

    private static String LANG_DEF_FILEPATH = BotbeansUtilities.LANG;
    private static Element langDefRoot;
    //flags 
    private boolean isWorkspacePanelInitialized = false;
    /** The single instance of the Workspace Controller **/
    protected static Workspace workspace;
    protected JPanel workspacePanel;
    //flag to indicate if a new lang definition file has been set
    private boolean langDefDirty = true;
    //flag to indicate if a workspace has been loaded/initialized 
    private boolean workspaceLoaded = false;
    public static BotbeansWidget botwidget;
    private Vector<String> vnames;

    /**
     * Constructs a WorkspaceController instance that manages the
     * interaction with the codeblocks.Workspace
     *
     */
    public WorkspaceController() {
        workspace = Workspace.getInstance();
    }

    /*    */
    /**
     * Returns the single instance of this
     * @return the single instance of this
     *//*
    public static WorkspaceController getInstance(){
    return wc;
    }*/

    ////////////////////
    //  LANG DEF FILE //
    ////////////////////
    /**
     * Sets the file path for the language definition file, if the 
     * language definition file is located in 
     */
    public void setLangDefFilePath(String filePath) {

        LANG_DEF_FILEPATH = filePath; //TODO do we really need to save the file path?

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();

            String langDefLocation = /*workingDirectory +*/ LANG_DEF_FILEPATH;
            //doc = builder.parse(new File(langDefLocation));
            doc = builder.parse(WorkspaceController.class.getClassLoader().getResourceAsStream(langDefLocation));

            langDefRoot = doc.getDocumentElement();

            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;
        } catch (Exception e) {
            //e.printStackTrace();
            NotifyDescriptor ee = new NotifyDescriptor.Message(filePath + " - " + e.toString(), NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notifyLater(ee);
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
    }

    /**
     * Sets the contents of the Lang Def File to the specified 
     * String langDefContents
     * @param langDefContents String contains the specification of a language
     * definition file
     */
    public void setLangDefFileString(String langDefContents) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(langDefContents)));
            langDefRoot = doc.getDocumentElement();

            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the Lang Def File to the specified File langDefFile.  
     * @param langDefFile File contains the specification of the a language 
     * definition file.
     */
    public void setLangDefFile(File langDefFile) {
        //LANG_DEF_FILEPATH = langDefFile.getCanonicalPath();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();

            doc = builder.parse(langDefFile);

            langDefRoot = doc.getDocumentElement();

            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all the block genuses, properties, and link rules of 
     * a language specified in the pre-defined language def file.
     * @param root Loads the language specified in the Element root
     */
    public void loadBlockLanguage(Element root) {
        //load connector shapes
        //MUST load shapes before genuses in order to initialize connectors within
        //each block correctly
        BlockConnectorShape.loadBlockConnectorShapes(root);

        //load genuses
        BlockGenus.resetAllGenuses();
        BlockGenus.loadBlockGenera(root);

        //load rules
        BlockLinkChecker.addRule(new CommandRule());
        BlockLinkChecker.addRule(new SocketRule());

        //set the dirty flag for the language definition file 
        //to false now that the lang file has been loaded
        langDefDirty = false;
    }

    /**
     * Resets the current language within the active
     * Workspace.
     *
     */
    public void resetLanguage() {
        //clear shape mappings
        BlockConnectorShape.resetConnectorShapeMappings();
        //clear block genuses
        BlockGenus.resetAllGenuses();
        //clear all link rules
        BlockLinkChecker.reset();
    }

    ////////////////////////
    // SAVING AND LOADING //
    ////////////////////////
    /**
     * Returns the save string for the entire workspace.  This includes the block workspace, any 
     * custom factories, canvas view state and position, pages
     * @return the save string for the entire workspace.
     */
    public static String getSaveString() {
        StringBuffer saveString = new StringBuffer();
        //append the save data
        saveString.append("<?xml version=\"1.0\" encoding=\"ISO-8859\"?>");
        saveString.append("\r\n");
        //dtd file path may not be correct...
        //saveString.append("<!DOCTYPE StarLogo-TNG SYSTEM \""+SAVE_FORMAT_DTD_FILEPATH+"\">");
        //append root node
        saveString.append("<CODEBLOCKS>");
        saveString.append(workspace.getSaveString());
        saveString.append("</CODEBLOCKS>");
        return saveString.toString();
    }

    /**
     * Loads a fresh workspace based on the default specifications in the language 
     * definition file.  The block canvas will have no live blocks.   
     */
    public void loadFreshWorkspace() {
        //need to just reset workspace (no need to reset language) unless
        //language was never loaded
        //reset only if workspace actually exists
        if (workspaceLoaded) {
            resetWorkspace();
        }

        if (langDefDirty) {
            loadBlockLanguage(langDefRoot);
        }

        workspace.loadWorkspaceFrom(null, langDefRoot);

        workspaceLoaded = true;
    }

    /**
     * Loads the programming project from the specified file path.  
     * This method assumes that a Language Definition File has already 
     * been specified for this programming project.
     * @param path String file path of the programming project to load
     */
    public void loadProjectFromPath(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();

            doc = builder.parse(new File(path));

            Element projectRoot = doc.getDocumentElement();

            //load the canvas (or pages and page blocks if any) blocks from the save file
            //also load drawers, or any custom drawers from file.  if no custom drawers
            //are present in root, then the default set of drawers is loaded from 
            //langDefRoot
            workspace.loadWorkspaceFrom(projectRoot, langDefRoot);

            workspaceLoaded = true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the programming project specified in the projectContents.   
     * This method assumes that a Language Definition File has already been 
     * specified for this programming project.
     * @param projectContents
     */
    public void loadProject(String projectContents) {
        //need to reset workspace and language (only if new language has been set)

        //reset only if workspace actually exists
        if (workspaceLoaded) {
            resetWorkspace();
        }

        if (langDefDirty) {
            loadBlockLanguage(langDefRoot);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(projectContents)));
            Element root = doc.getDocumentElement();
            //load the canvas (or pages and page blocks if any) blocks from the save file
            //also load drawers, or any custom drawers from file.  if no custom drawers
            //are present in root, then the default set of drawers is loaded from 
            //langDefRoot
            workspace.loadWorkspaceFrom(root, langDefRoot);

            workspaceLoaded = true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads the programming project specified in the projectContents String, 
     * which is associated with the language definition file contained in the 
     * specified langDefContents.  All the blocks contained in projectContents
     * must have an associted block genus defined in langDefContents.
     * 
     * If the langDefContents have any workspace settings such as pages or 
     * drawers and projectContents has workspace settings as well, the 
     * workspace settings within the projectContents will override the 
     * workspace settings in langDefContents.  
     * 
     * NOTE: The language definition contained in langDefContents does 
     * not replace the default language definition file set by: setLangDefFilePath() or 
     * setLangDefFile().
     * 
     * @param projectContents
     * @param langDefContents String XML that defines the language of
     * projectContents
     */
    public void loadProject(String projectContents, String langDefContents) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document projectDoc;
        Document langDoc;
        try {
            builder = factory.newDocumentBuilder();
            projectDoc = builder.parse(new InputSource(new StringReader(projectContents)));
            Element projectRoot = projectDoc.getDocumentElement();
            langDoc = builder.parse(new InputSource(new StringReader(projectContents)));
            Element langRoot = langDoc.getDocumentElement();

            //need to reset workspace and language (if langDefContents != null)
            //reset only if workspace actually exists
            if (workspaceLoaded) {
                resetWorkspace();
            }

            if (langDefContents == null) {
                loadBlockLanguage(langDefRoot);
            } else {
                loadBlockLanguage(langRoot);
            }
            //TODO should verify that the roots of the two XML strings are valid
            workspace.loadWorkspaceFrom(projectRoot, langRoot);

            workspaceLoaded = true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the entire workspace.  This includes all blocks, pages, drawers, and trashed blocks.  
     * Also resets the undo/redo stack.  The language (i.e. genuses and shapes) is not reset.
     */
    public void resetWorkspace() {
        //clear all pages and their drawers
        //clear all drawers and their content
        //clear all block and renderable block instances
        workspace.reset();
        //clear action history
        //rum.reset();
        //clear runblock manager data
        //rbm.reset();
    }

    /**
     * This method creates and lays out the entire workspace panel with its 
     * different components.  Workspace and language data not loaded in 
     * this function.
     * Should be call only once at application startup.
     */
    private void initWorkspacePanel() {

        //add trashcan and prepare trashcan images

        //ImageIcon tc = new ImageIcon("support/images/trash.png");
        ImageIcon tc = ImageUtilities.loadImageIcon("org/botbeans/blocks/images/trash.png", true);
        //ImageIcon openedtc = new ImageIcon("support/images/trash_open.png");
        ImageIcon openedtc = ImageUtilities.loadImageIcon("org/botbeans/blocks/images/trash_open.png", true);

        TrashCan trash = new TrashCan(tc.getImage(), openedtc.getImage());
        workspace.addWidget(trash, true, true);


        workspacePanel = new JPanel();
        workspacePanel.setLayout(new BorderLayout());
        workspacePanel.add(workspace, BorderLayout.CENTER);

        isWorkspacePanelInitialized = true;
    }

    /**
     * Returns the JComponent of the entire workspace. 
     * @return the JComponent of the entire workspace. 
     */
    public JComponent getWorkspacePanel() {
        if (!isWorkspacePanelInitialized) {
            initWorkspacePanel();
        }
        return workspacePanel;
    }

    /**
     * Returns an unmodifiable Iterable of SearchableContainers
     * @return an unmodifiable Iterable of SearchableContainers
     */
    public Iterable<SearchableContainer> getAllSearchableContainers() {
        return workspace.getAllSearchableContainers();
    }

    /////////////////////////////////////
    // TESTING CODEBLOCKS SEPARATELY //
    /////////////////////////////////////
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void sendWidget(BotbeansWidget widget) {
        BlockCanvas bc = workspace.getBlockCanvas();
        Page p = bc.getPages().get(0);
        Collection<RenderableBlock> c = p.getBlocks();

        if (!c.isEmpty()) {
            ((ExpressionCanvas) widget.getComponent()).setC(c);

//            if (widget.isLabelActive()) {
//                widget.removeChild(widget.getLabelWidget());
//            }

//            widget.setLayout(LayoutFactory.createAbsoluteLayout());
//            widget.getComponentWidget().setPreferredLocation(new Point(0, 0));
//            widget.getImageWidget().setPreferredLocation(new Point(0, widget.getComponentWidget().getBounds().height));

            ((ExpressionCanvas) widget.getComponent()).setSavestring(getSaveString());
            ((ExpressionCanvas) widget.getComponent()).revalidate();
            ((ExpressionCanvas) widget.getComponent()).repaint();
        }
    }

    public void setBotbeansWidget(BotbeansWidget botbeansWidget) {
        botwidget = botbeansWidget;
    }

    public void setVariableNames(Vector<String> aux) {
        vnames = aux;
    }
}
