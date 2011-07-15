package org.botbeans.shapes;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;
import javax.swing.Action;
import org.botbeans.shapes.nodes.GenericNode;
import org.botbeans.shapes.dialogs.RunDialog;
import org.botbeans.common.BotBeansFileFilter;
import java.awt.BorderLayout;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.TransferHandler;
import org.botbeans.blocks.controller.ExpressionCanvas;
import org.botbeans.common.BotbeansUtilities;
import org.botbeans.common.widgets.BotbeansWidget;
import org.botbeans.control.ServerTopComponent;
import org.botbeans.shapes.dialogs.Configuration;
import org.botbeans.shapes.nodes.ConditionNode;
import org.botbeans.shapes.nodes.FinishNode;
import org.botbeans.shapes.nodes.FunctionNode;
import org.botbeans.shapes.nodes.JunctionNode;
import org.botbeans.shapes.nodes.MagneticNode;
import org.botbeans.shapes.nodes.MicrophoneNode;
import org.botbeans.shapes.nodes.MotorNode;
import org.botbeans.shapes.nodes.MoveDistanceNode;
import org.botbeans.shapes.nodes.MoveTimerNode;
import org.botbeans.shapes.nodes.RotateDistanceNode;
import org.botbeans.shapes.nodes.RotateTimerNode;
import org.botbeans.shapes.nodes.SonarNode;
import org.botbeans.shapes.nodes.SpeakerNode;
import org.botbeans.shapes.nodes.StartNode;
import org.botbeans.shapes.nodes.TimerNode;
import org.botbeans.shapes.nodes.VariableNode;
import org.botbeans.shapes.palette.CategoryChildFactory;
import org.botbeans.shapes.palette.Shape;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.NotifyDescriptor.Message;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.ImageUtilities;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Topcomponent
 * @author Apocas
 */
public class ShapeTopComponent extends TopComponent implements Runnable, LookupListener, Serializable {

    private final PaletteController pc;
    private boolean used = false;

    /**
     * @return the pc
     */
    public PaletteController getPaletteController() {
        return pc;
    }

    public boolean isUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    private static class ShapeTransferHandler extends TransferHandler {

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(Shape.DATA_FLAVOR);
        }

        @Override
        public boolean importData(TransferSupport support) {
            try {
                Shape s = (Shape) support.getTransferable().getTransferData(Shape.DATA_FLAVOR);
                return true;
            } catch (UnsupportedFlavorException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            return false;
        }
    }
    
    int flag;
    private Socket clientSocket;
    private JComponent myView;
    private GraphSceneImpl scene;
    private static ShapeTopComponent instance;
    static final String ICON_PATH = ShapesUtilities.icons_path + "Computer_File_053.gif";
    private static final String PREFERRED_ID = "ShapeTopComponent";
    private boolean connected;
    private boolean flag_thread;
    private RunDialog run_dialog;
    private JFileChooser chooser;
    private DataOutputStream out;
    private DataInputStream in;
    //private Hashtable memory_logic;
    private HashMap memory;
    private final InstanceContent content;
    private SaveCookieImpl savenode;
    private String endereco_server = "localhost";
    private int porta_server = 8888;
    private boolean activated_focused = false;
    private boolean threaded = true;
    private TransferHandler th = new ShapeTransferHandler();

    public ShapeTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ShapeTopComponent.class, "CTL_ShapeTopComponent"));
        setToolTipText(NbBundle.getMessage(ShapeTopComponent.class, "HINT_ShapeTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        flag = 0;
        content = new InstanceContent();

        scene = new GraphSceneImpl(content);
        myView = scene.createView();

        shapePane.setViewportView(myView);
        add(scene.createSatelliteView(), BorderLayout.WEST);

        savenode = new SaveCookieImpl();

        //PaletteController pc = PaletteSupport.createPalette();
        pc = PaletteFactory.createPalette(new AbstractNode(Children.create(new CategoryChildFactory(), true)), new PaletteActions() {

            @Override
            public Action[] getImportActions() {
                return null;
            }

            @Override
            public Action[] getCustomPaletteActions() {
                return null;
            }

            @Override
            public Action[] getCustomCategoryActions(Lookup lkp) {
                return null;
            }

            @Override
            public Action[] getCustomItemActions(Lookup lkp) {
                return null;
            }

            @Override
            public Action getPreferredAction(Lookup lkp) {
                return null;
            }
        });

        //associateLookup(Lookups.fixed(new Object[]{pc}));
        //associateLookup(new ProxyLookup(Lookups.fixed(new Object[]{content})));
        associateLookup(new AbstractLookup(content));
        content.add(savenode);
        content.add(pc);

        /*
        AbstractNode c = pc.getRoot().lookup(AbstractNode.class);
        Children ch = c.getChildren();
        
        final Shape item = new Shape();
        item.setCategory(ShapesUtilities.categories[5]);
        item.setImage(ShapesUtilities.items[17][2]);
        item.setTitle("Function");
        AbstractNode node = new AbstractNode(Children.LEAF) {
        
        @Override
        public Transferable drag() throws IOException {
        return item;
        }
        };
        node.setName("Function");
        node.setIconBaseWithExtension(ShapesUtilities.items[17][2]);
        ((AbstractNode) ch.getNodes()[5]).getChildren().add(new Node[]{node});
         */


        connected = false;

        BotBeansFileFilter filter = new BotBeansFileFilter();
        filter.addExtension("btb");
        filter.setDescription("Projecto BotBeans");
        chooser = new JFileChooser();
        chooser.setFileFilter(filter);
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        activated_focused = true;
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
        if (TopComponent.getRegistry().getActivated() instanceof ShapeTopComponent) {
            activated_focused = false;
        }
    }

    public RunDialog getRunDialog() {
        return run_dialog;
    }

    public void removeCustomBlocks() {
        AbstractNode c = pc.getRoot().lookup(AbstractNode.class);
        Children ch = c.getChildren();

        ((AbstractNode) ch.getNodes()[5]).getChildren().remove(((AbstractNode) ch.getNodes()[5]).getChildren().getNodes());
    }

    /*
    public void mergeMemory(Hashtable m) {
    Hashtable aux = new Hashtable();
    aux.putAll(m);
    aux.putAll(memory);
    memory = aux;
    }*/
    public static ArrayList<String> getProjectNames() {
        ArrayList<String> names = new ArrayList<String>();

        for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
            if (tc instanceof ShapeTopComponent) {
                if (((ShapeTopComponent) tc).isUsed()) {
                    names.add(((ShapeTopComponent) tc).getProjectName());
                }
            }
        }
        return names;
    }

    public static ShapeTopComponent getLastActivatedComponent() {
        for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
            if (tc instanceof ShapeTopComponent) {
                ShapeTopComponent stc = (ShapeTopComponent) tc;

                if (stc.activated_focused) {
                    return stc;
                }
            }
        }
        return null;
    }

    /**
     * @return the endereco_server
     */
    public String getEndereco_server() {
        return endereco_server;
    }

    /**
     * @param endereco_server the endereco_server to set
     */
    public void setEndereco_server(String endereco_server) {
        this.endereco_server = endereco_server;
    }

    /**
     * @return the porta_server
     */
    public int getPorta_server() {
        return porta_server;
    }

    /**
     * @param porta_server the porta_server to set
     */
    public void setPorta_server(int porta_server) {
        this.porta_server = porta_server;
    }

    /**
     * @return the scene
     */
    public GraphSceneImpl getScene() {
        return scene;
    }

    /**
     * @return the memory
     */
    public HashMap getMemory() {
        return memory;
    }

    /**
     * @return the out
     */
    public DataOutputStream getOut() {
        return out;
    }

    /**
     * @return the in
     */
    public DataInputStream getIn() {
        return in;
    }

    private class SaveCookieImpl implements SaveCookie {

        public void save() throws IOException {
            //Confirmation msg = new NotifyDescriptor.Confirmation("Do you want to save?", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
            //Object result = DialogDisplayer.getDefault().notify(msg);
            //if (NotifyDescriptor.YES_OPTION.equals(result)) {
            guarda();
            //}
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        shapePane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        project_name = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());
        add(shapePane, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(645, 27));

        project_name.setBackground(java.awt.SystemColor.control);
        project_name.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                project_nameFocusLost(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Project name:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(410, Short.MAX_VALUE)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(project_name, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 164, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(project_name, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void project_nameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_project_nameFocusLost
        // TODO add your handling code here:
        project_name.setText(formatString(project_name.getText()));
        this.setName(project_name.getText());
        used = true;

        ShapesUtilities.updateBlocks();
        //Message msg = new NotifyDescriptor.Message("Name normalized!", NotifyDescriptor.ERROR_MESSAGE);
        //DialogDisplayer.getDefault().notify(msg);
    }//GEN-LAST:event_project_nameFocusLost

    public static String formatString(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);

        return temp.replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Apaga widget
     */
    public void deleteWidget() {
        for (int u = 0; u < getScene().getSelectedObjects().toArray().length; u++) {
            Object xpto = getScene().getSelectedObjects().toArray()[u];


            if (xpto instanceof GenericEdge && getScene().isEdge(xpto)) {
                getScene().getEdgeSource((GenericEdge) xpto).removeSaida();
                getScene().getEdgeTarget((GenericEdge) xpto).removeEntrada();
                getScene().removeEdge((GenericEdge) xpto);
                getScene().validate();
            } else if (xpto instanceof GenericNode && getScene().isNode(xpto)) {
                GenericNode xptonode = (GenericNode) xpto;
                int flagg = 0;

                while (flagg == 0) {
                    flagg = 1;
                    
                    for (int i = 0; i
                            < getScene().getEdges().toArray().length; i++) {
                        GenericEdge xptoedge = (GenericEdge) (getScene().getEdges().toArray()[i]);
                        GenericNode xptorigem = getScene().getEdgeSource(xptoedge);
                        GenericNode xptodestino = getScene().getEdgeTarget(xptoedge);

                        if (xptorigem.equals(xptonode) && xptodestino.equals(xptonode)) {
                        } else {
                            if (xptorigem.equals(xptonode) || xptodestino.equals(xptonode)) {
                                xptorigem.removeSaida();
                                xptodestino.removeEntrada();
                                getScene().removeEdge(xptoedge);
                                flagg = 0;
                            }
                        }
                    }
                }
                getScene().removeNode((GenericNode) xpto);
                getScene().validate();
            }
        }
    }

    public void setRunDialog(RunDialog d) {
        this.run_dialog = d;
    }

    public void executa(boolean threaded) {
        this.threaded = threaded;

        if (!connected) {
            this.liga();
        }

        if (connected && !flag_thread) {
            flag_thread = true;

            if (threaded) {
                //memory_logic = new Hashtable();
                memory = new HashMap();
                run_dialog = new RunDialog();

                new Thread(this).start();
            } else {
                run();
            }
        } else {
            Message msg = new NotifyDescriptor.Message("Not connected, connect first!", NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(msg);
        }
    }

    public void para() {
        flag_thread = false;
    }

    /**
     * Devolve o proximo node a executar
     * @param node
     * @return proximo node
     */
    public GenericNode proximoNode(GenericNode node) {
        GenericNode selecionado = node;

        for (int i = 0; i < getScene().getEdges().toArray().length; i++) {
            GenericEdge edgetemp = (GenericEdge) getScene().getEdges().toArray()[i];
            GenericNode nodeorigem = getScene().getEdgeSource(edgetemp);

            if (nodeorigem.equals(selecionado)) {
                return getScene().getEdgeTarget(edgetemp);
            }
        }
        return null;
    }

    /**
     * Liga ao servidor de controlo
     */
    public void liga() {
        if (connected == false) {
            ServerTopComponent stc = ServerTopComponent.findInstance();

            try {
                clientSocket = new Socket(getEndereco_server(), getPorta_server());
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                connected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Message msg = new NotifyDescriptor.Message("Already Connected!", NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(msg);
        }
    }

    /**
     * Desliga do servidor de controlo
     */
    public void desliga() {
        if (connected == true) {
            try {
                clientSocket.close();
                connected = false;
            } catch (Exception e) {
                System.out.println("ERRO Form Ligar Main Server:" + e.getMessage());
            }
        } else {
            Message msg = new NotifyDescriptor.Message("Not connected, connect first!", NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(msg);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField project_name;
    private javax.swing.JScrollPane shapePane;
    // End of variables declaration//GEN-END:variables

    public static synchronized ShapeTopComponent getDefault() {
        if (instance == null) {
            instance = new ShapeTopComponent();
        }

        return instance;
    }

    public static synchronized ShapeTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);

        if (win == null) {
            Logger.getLogger(ShapeTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }

        if (win instanceof ShapeTopComponent) {
            return (ShapeTopComponent) win;
        }

        Logger.getLogger(ShapeTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");

        return getDefault();
    }

    public String getProjectName() {
        return project_name.getText();
    }

    public void setProjectName(String n) {
        project_name.setText(n);
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        Confirmation msg = new NotifyDescriptor.Confirmation("Do you want to save the closed project before leaving?", NotifyDescriptor.YES_NO_OPTION,
                NotifyDescriptor.QUESTION_MESSAGE);
        Object result = DialogDisplayer.getDefault().notify(msg);


        if (NotifyDescriptor.YES_OPTION.equals(result)) {
            guarda();
        }

        if (used) {
            ShapesUtilities.removeBlock(project_name.getText());
        }
        //ShapesUtilities.updateBlocks();
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public void resultChanged(LookupEvent le) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ShapeTopComponent.getDefault();
        }
    }

    /**
     * Abre um projecto previamente gravado
     * @return
     */
    public int abre() {
        String lines[];
        String line = "";
        File ficheiro = null;

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ficheiro = chooser.getSelectedFile();
        }

        if (ficheiro != null) {
            if (ficheiro.toString().contains(".btb")) {
                this.setName(ficheiro.getName().replace(".btb", ""));

                try {
                    FileReader fstream = new FileReader(ficheiro);
                    BufferedReader inf = new BufferedReader(fstream);

                    line = inf.readLine();

                    while (!line.contains("#") && line != null) {
                        lines = line.split(";");
                        int componente = Integer.parseInt(lines[0]);
                        GenericNode noded = null;

                        switch (componente) {
                            case 0:
                            case 1:
                                noded = new MoveTimerNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[componente]), componente);
                                break;
                            case 2:
                            case 3:
                                noded = new RotateTimerNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[componente]), componente);
                                break;
                            case 4:
                            case 5:
                                noded = new MoveDistanceNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[componente]), componente);
                                break;
                            case 6:
                            case 7:
                                noded = new RotateDistanceNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[componente]), componente);
                                break;
                            case 8:
                                noded = new SonarNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[8]));
                                break;
                            case 9:
                                noded = new MagneticNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[9]));
                                break;
                            case 10:
                                noded = new VariableNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[10]));
                                break;
                            case 11:
                                noded = new ConditionNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[11]));
                                break;
                            case 12:
                                noded = new JunctionNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[12]));
                                break;
                            case 13:
                                noded = new TimerNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[13]));
                                break;
                            case 14:
                                noded = new StartNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[14]));
                                break;
                            case 15:
                                noded = new FinishNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[15]));
                                break;
                            case 16:
                                noded = new FunctionNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[16]));
                                break;
                            case 17:
                                noded = new SpeakerNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[17]));
                                break;
                            case 18:
                                noded = new MicrophoneNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[18]));
                                break;
                            case 19:
                                noded = new MotorNode(Integer.parseInt(lines[1]), ImageUtilities.loadImage(ShapesUtilities.figures[19]));
                                break;
                        }

                        noded.loadHash(line);

                        float x = Float.parseFloat(lines[lines.length - 2]);
                        float y = Float.parseFloat(lines[lines.length - 1]);

                        BotbeansWidget bw = (BotbeansWidget) getScene().addNode(noded);
                        bw.setPreferredLocation(new Point((int) x, (int) y));

                        if (noded instanceof ConditionNode || noded instanceof VariableNode) {
                            if (noded instanceof ConditionNode) {
                                bw.removeChild(bw.getLabelWidget());
                            }
                            Vector<String> vv = new Vector<String>();

                            if (!lines[4].isEmpty()) {
                                String[] vars = lines[4].trim().split(",");
                                vv.addAll(Arrays.asList(vars));
                            }
                            ((ExpressionCanvas) bw.getComponent()).setSavestringAndLoad(BotbeansUtilities.LANG, lines[5], vv);
                        }

                        getScene().validate();
                        line = inf.readLine();
                    }

                    if (line.equals("#")) {
                        while (line != null && !line.isEmpty()) {
                            line = inf.readLine();

                            if (line != null) {
                                if (line.equals("#")) {
                                    break;
                                } else {
                                    lines = line.split(";");
                                    int origem = Integer.parseInt(lines[0]);
                                    int destino = Integer.parseInt(lines[1]);
                                    int tipo = Integer.parseInt(lines[2]);
                                    
                                    GenericNode origem_temp = getNode(origem);
                                    GenericNode destino_temp = getNode(destino);

                                    GenericEdge edge_temp = new GenericEdge(tipo);

                                    getScene().addEdge(edge_temp);
                                    getScene().setEdgeSource(edge_temp, origem_temp);
                                    getScene().setEdgeTarget(edge_temp, destino_temp);
                                }
                            }
                        }
                    }

                    this.project_name.setText(inf.readLine());
                    inf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.setName(this.project_name.getText());
                getScene().validate();
                //getScene().repaint();
                //getScene().bringMaintoFront();
            } else {
                Message msg = new NotifyDescriptor.Message("Invalid file!", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
                return -1;
            }
        } else {
            return -1;
        }
        used = true;
        return 1;
    }

    /**
     * Guarda o projecto para um ficheiro
     */
    public void guarda() {
        File ficheiro = new File("projecto.btb");
        int returnVal = chooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ficheiro = chooser.getSelectedFile();
        }

        String str_ficheiro = "";

        if (!ficheiro.toString().contains(".btb")) {
            str_ficheiro = ficheiro.toString() + ".btb";
        } else {
            str_ficheiro = ficheiro.toString();
        }

        //this.setName(ficheiro.getName().replace(".btb", ""));

        try {
            FileWriter fstream = new FileWriter(str_ficheiro);
            BufferedWriter outf = new BufferedWriter(fstream);

            for (int j = 0; j < getScene().getNodes().toArray().length; j++) {
                GenericNode node_aux = (GenericNode) (getScene().getNodes().toArray()[j]);

                outf.write(node_aux.getHash());
                outf.write(";" + getScene().findWidget(node_aux).getLocation().getX() + ";");
                outf.write(getScene().findWidget(node_aux).getLocation().getY() + "\n");
            }
            outf.write("#\n");


            for (int j = 0; j < getScene().getEdges().toArray().length; j++) {
                GenericEdge edge_aux = (GenericEdge) (getScene().getEdges().toArray()[j]);

                outf.write(getScene().getEdgeSource(edge_aux).getId() + ";");
                outf.write(getScene().getEdgeTarget(edge_aux).getId() + ";");
                outf.write(edge_aux.getTipo() + "\n");
            }
            outf.write("#\n");
            outf.write(project_name.getText());

            outf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Devolve o node com o id correspondente
     * @param id
     * @return node com o atributo id
     */
    private GenericNode getNode(int id) {
        for (int i = 0; i < getScene().getNodes().toArray().length; i++) {
            GenericNode node_aux = (GenericNode) getScene().getNodes().toArray()[i];

            if (node_aux.getId() == id) {
                return node_aux;
            }
        }
        return null;
    }

    public void run() {
        //GenericNode selecionado = (GenericNode) (getScene().getSelectedObjects().toArray()[0]);
        GenericNode selecionado = getScene().getStartingNode();
        GenericNode old = null;


        while (selecionado != null && flag_thread) {
            if (MemoryTopComponent.findInstance() != null) {
                MemoryTopComponent.findInstance().updateMemory(memory);
            }
            run_dialog.refreshMem(memory);
            old = selecionado;
            selecionado = executeNode(selecionado);

            if (selecionado != null) {
                Object[] edges = this.getScene().findEdgesBetween(old, selecionado).toArray();

                if (edges.length > 0) {
                    getScene().setFocusedWidget(getScene().findWidget(edges[0]));
                    getScene().validate();
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            getScene().setFocusedWidget(getScene().findWidget(selecionado));
            getScene().validate();
        }

        if (threaded) {
            run_dialog.destroi(true);
        }
        flag_thread = false;
    }

    public void config() {
        new Configuration().setVisible(true);
    }

    /**
     * Envia a configuracao para o robot
     * @param roda_d
     * @param eixo_d
     * @param motor_e
     * @param motor_d
     */
    public void sendConfig(float roda_d, float eixo_d, int motor_e, int motor_d, int porta_sonar, int porta_comp, int porta_som) {
        int d3[] = {1, 15};

        try {
            send(d3);
            getOut().writeFloat(roda_d);
            getOut().writeFloat(eixo_d);
            getOut().writeInt(motor_e);
            getOut().writeInt(motor_d);
            getOut().writeInt(porta_sonar);
            getOut().writeInt(porta_comp);
            getOut().writeInt(porta_som);
            getOut().flush();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Executa um determinado node
     * @param node para executar
     * @return proximo node a executar
     */
    public GenericNode executeNode(GenericNode node_exec) {
        return node_exec.execute(this);
    }

    public int[] receive() throws IOException {
        int size = getIn().readInt();
        int dd[] = new int[size];

        for (int i = 0; i
                < size; i++) {
            dd[i] = getIn().readInt();
        }
        return dd;
    }

    /**
     * Envia uma mensagem para o servidor de controlo
     * @param data
     * @throws IOException
     */
    public void send(int data[]) throws IOException {
        for (int i = 0; i < data.length; i++) {
            getOut().writeInt(data[i]);
            getOut().flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
