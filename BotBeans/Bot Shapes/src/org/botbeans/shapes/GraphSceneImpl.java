package org.botbeans.shapes;

import org.botbeans.blocks.controller.ExpressionCanvas;
import java.awt.BasicStroke;
import org.botbeans.shapes.nodes.GenericNode;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JComponent;
import org.botbeans.blocks.BlocksTopComponent;
import org.botbeans.common.widgets.BotbeansWidget;
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
import org.botbeans.shapes.palette.Shape;
import org.botbeans.shapes.utilities.ShapesUtilities;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class GraphSceneImpl extends GraphScene<GenericNode, GenericEdge> {

    private LayerWidget mainLayer;
    //private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;
    private int nodes = 0;
    private final InstanceContent content;
    GenericPropertyNode old_prop = null;
    private final WidgetAction select;
    Widget old_widget = null;
    private final WidgetAction edit;

    public GraphSceneImpl(final InstanceContent content) {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);

        this.content = content;

        interractionLayer = new LayerWidget(this);
        addChild(interractionLayer);

        //mainLayer.bringToFront();

        //getActions().addAction(ActionFactory.createSelectAction(new CreateProvider()));

//        select = ActionFactory.createHoverAction(new HoverProvider() {
//
//            public void widgetHovered(Widget widget) {
//                ((GenericNode) findObject(widget)).updateDescription((BotbeansWidget) widget);
//            }
//        });

        edit = ActionFactory.createEditAction(new EditProvider() {

            public void edit(Widget widget) {
                BotbeansWidget w = null;
                if (widget instanceof BotbeansWidget) {
                    w = (BotbeansWidget) widget;
                } else {
                    w = (BotbeansWidget) widget.getParentWidget();
                }
                if (w.isLabelActive() && findObject(widget) instanceof ConditionNode) {
                    w.removeChild(w.getLabelWidget());
                }
                if (findObject(widget) instanceof FunctionNode) {
                    boolean aux = true;
                    String name_aux = ((FunctionNode) findObject(widget)).getFname();
                    for (TopComponent tc : TopComponent.getRegistry().getOpened()) {
                        if (tc.getName().equals(name_aux)) {
                            tc.requestActive();
                            aux = false;
                        }
                    }
                    if (aux) {
                        ShapeTopComponent tc = new ShapeTopComponent();
                        tc.setName(name_aux);
                        tc.setProjectName(name_aux);
                        tc.open();
                        tc.requestActive();
                        tc.getScene().bringMaintoFront();
                    }
                }
                loadExpressionBuilder(widget);
            }
        });

        select = ActionFactory.createSelectAction(new SelectProvider() {

            public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
                return false;
            }

            public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
                return true;
            }

            public void select(Widget widget, Point point, boolean bln) {
                selectShowProperties(widget);
                if (MemoryTopComponent.findInstance() != null) {
                    MemoryTopComponent.findInstance().updateMemory(getVariables());
                }
            }
        });

        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                Image recebido = getImageFromTransferable(transferable);
                JComponent view = getView();
                Graphics2D g2 = (Graphics2D) view.getGraphics();
                Rectangle visRect = view.getVisibleRect();
                view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                g2.drawImage(recebido, AffineTransform.getTranslateInstance(point.getLocation().getX(), point.getLocation().getY()), null);
                return ConnectorState.ACCEPT;
            }

            public void accept(Widget widget, Point point, Transferable transferable) {
                Image pic = getImageFromTransferable(transferable);
                int componente = ShapesUtilities.identifyPic(pic);
                GenericNode teste = null;

                switch (componente) {
                    case 0:
                    case 1:
                        teste = new MoveTimerNode(nodes, pic, componente);
                        break;
                    case 2:
                    case 3:
                        teste = new RotateTimerNode(nodes, pic, componente);
                        break;
                    case 4:
                    case 5:
                        teste = new MoveDistanceNode(nodes, pic, componente);
                        break;
                    case 6:
                    case 7:
                        teste = new RotateDistanceNode(nodes, pic, componente);
                        break;
                    case 8:
                        teste = new SonarNode(nodes, pic);
                        break;
                    case 9:
                        teste = new MagneticNode(nodes, pic);
                        break;
                    case 10:
                        teste = new VariableNode(nodes, pic);
                        break;
                    case 11:
                        teste = new ConditionNode(nodes, pic);
                        break;
                    case 12:
                        teste = new JunctionNode(nodes, pic);
                        break;
                    case 13:
                        teste = new TimerNode(nodes, pic);
                        break;
                    case 14:
                        teste = new StartNode(nodes, pic);
                        break;
                    case 15:
                        teste = new FinishNode(nodes, pic);
                        break;
                    case 16:
                        Object o = null;
                        try {
                            o = transferable.getTransferData(Shape.DATA_FLAVOR);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (UnsupportedFlavorException ex) {
                            ex.printStackTrace();
                        }

                        teste = new FunctionNode(nodes, pic);
                        ((FunctionNode) teste).fname = (((Shape) o).getTitle());
                        break;
                    case 17:
                        teste = new SpeakerNode(nodes, pic);
                        break;
                    case 18:
                        teste = new MicrophoneNode(nodes, pic);
                        break;
                    case 19:
                        teste = new MotorNode(nodes, pic);
                        break;
                    default:
                        return;
                }

                if (teste instanceof StartNode && getStartingNode() != null) {
                    Message msg = new NotifyDescriptor.Message("Starting node already present!", NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(msg);
                    ShapeTopComponent.getLastActivatedComponent().getScene().repaint();
                } else {
                    nodes++;
                    Widget w = GraphSceneImpl.this.addNode(teste);
                    w.setPreferredLocation(widget.convertLocalToScene(point));
                    selectShowProperties(w);
                }
            }
        }));
        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
    }

    public void addStartStop() {
        Widget w = GraphSceneImpl.this.addNode(new StartNode(nodes, ImageUtilities.loadImage(ShapesUtilities.items[14][2], true)));
        w.setPreferredLocation(new Point(350, 10));
        selectShowProperties(w);
        nodes++;
        w = GraphSceneImpl.this.addNode(new FinishNode(nodes, ImageUtilities.loadImage(ShapesUtilities.items[15][2], true)));
        w.setPreferredLocation(new Point(350, 350));
        selectShowProperties(w);
        nodes++;
    }

    public Vector<String> getVariablesNames() {
        Vector<String> vnames = new Vector<String>();
        Widget wd[] = mainLayer.getChildren().toArray(new Widget[0]);
        for (Widget w : wd) {
            if (findObject(w) instanceof VariableNode) {
                String vname = ((VariableNode) findObject(w)).getVname();
                if (!vnames.contains(vname)) {
                    vnames.add(vname);
                }
            }
        }
        return vnames;
    }

    public HashMap getVariables() {
        HashMap m = new HashMap();
        Widget wd[] = mainLayer.getChildren().toArray(new Widget[0]);
        for (Widget w : wd) {
            if (findObject(w) instanceof VariableNode) {
                String vname = ((VariableNode) findObject(w)).getVname();
                if (!m.containsKey(vname)) {
                    m.put(vname, 0);
                }
            }
        }
        return m;
    }

    public String getVariablesNamesString() {
        String aux = "";
        for (String v : getVariablesNames()) {
            aux += "," + v;
        }
        if (!aux.isEmpty()) {
            aux = aux.substring(1);
        }
        return aux;
    }

    @SuppressWarnings("unchecked")
    public void selectShowProperties(Widget w) {
        GenericNode[] testes = {((GenericNode) findObject(w))};
        setSelectedObjects(new HashSet(Arrays.asList(testes)));
        if (old_widget != w) {
            GenericPropertyNode nn = new GenericPropertyNode((GenericNode) findObject(w));
            if (old_prop != null) {
                content.remove(old_prop);
            }
            old_prop = nn;
            content.add(nn);
            old_widget = w;
        }
    }

    public GenericNode getStartingNode() {
        for (GenericNode n : this.getNodes()) {
            if (n instanceof StartNode) {
                return n;
            }
        }
        return null;
    }

    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(Shape.DATA_FLAVOR);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
        }

        return ImageUtilities.loadImage(((Shape) o).getImage(), true);
    }

    @Override
    protected void notifyNodeAdded(GenericNode node, Widget widget) {
        super.notifyNodeAdded(node, widget);

        if (MemoryTopComponent.findInstance() != null) {
            MemoryTopComponent.findInstance().updateMemory(getVariables());
        }
    }

    protected Widget attachNodeWidget(GenericNode node) {
        BotbeansWidget widget = new BotbeansWidget(this, true);
        //node.setIcon(widget);
        widget.setImage(node.getImage());
        //apocas
        if (node instanceof ConditionNode || node instanceof VariableNode) {
            ExpressionCanvas tc = new ExpressionCanvas();
            widget.setComponent(tc);
            if (node instanceof VariableNode) {
                //widget.setLayout(LayoutFactory.createAbsoluteLayout());
                widget.getComponentWidget().setPreferredLocation(new Point(40 - 100, -10));
                //widget.getLabelWidget().setPreferredLocation(new Point(35, 70));  
                //widget.getLabelWidget().setFont(new Font("Arial", Font.BOLD, 25));
                //widget.getLabelWidget().setForeground(Color.ORANGE);
                //widget.getImageWidget().setPreferredLocation(new Point(0, 10));

                Widget l = widget.getLabelWidget();
                Widget m = widget.getImageWidget();
                Widget c = widget.getComponentWidget();
                widget.removeChild(m);
                widget.removeChild(c);
                widget.removeChild(l);
                widget.addChild(c);
                widget.addChild(m);
                widget.addChild(l);
            }
            //JButton jb = new JButton("Original");
            //JPanel jp = new JPanel(new BorderLayout());
            //jp.add(new JColorChooser());
        }


        node.updateDescription(widget);
        //widget.setLabel(node.getDescription());

        widget.getActions().addAction(ActionFactory.createExtendedConnectAction(interractionLayer, new SceneConnectProvider()));

        widget.getActions().addAction(edit);
        widget.getLabelWidget().getActions().addAction(edit);
        //widget.getComponentWidget().getActions().addAction(edit);

        widget.getLabelWidget().getActions().addAction(select);
        widget.getComponentWidget().getActions().addAction(select);
        widget.getActions().addAction(select);

        widget.getActions().addAction(ActionFactory.createMoveAction());

        //widget.getActions().addAction(createObjectHoverAction());

        mainLayer.addChild(widget);

        if (nodes <= node.getId()) {
            nodes = nodes + ((node.getId() - nodes) + 1);
        }

        //this.revalidate();



        return widget;
    }

    @SuppressWarnings("unchecked")
    private void loadExpressionBuilder(Widget widget) {
        GenericNode node = (GenericNode) findObject(widget);
        if (node instanceof ConditionNode || node instanceof VariableNode) {
            GenericNode[] testes = {((GenericNode) findObject(widget))};
            setSelectedObjects(new HashSet(Arrays.asList(testes)));
            Vector<String> aux = getVariablesNames();
            Mode mode = WindowManager.getDefault().findMode("rightSlidingSide");
            BlocksTopComponent btc = BlocksTopComponent.findInstance();
            btc.loadWidget((BotbeansWidget) findWidget(node), aux);
            mode.dockInto(btc);
            btc.open();
            btc.requestActive();

//            ExpressionCanvas tc = new ExpressionCanvas(BotbeansUtilities.LANG, getVariablesNames(), ((ExpressionCanvas) ((BotbeansWidget) findWidget(node)).getComponent()).getSavestring());
//            BufferedImage bfi = tc.render();
//            if (bfi != null) {
//                try {
//                    ImageIO.write(bfi, "png", new File("bufo.png"));
//                } catch (IOException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
        }
    }

    protected Widget attachEdgeWidget(GenericEdge arg0) {
        ConnectionWidget connection = new ConnectionWidget(this);

        connection.setStroke(new BasicStroke(3.0f));

        if (arg0.getTipo() == 1) {
            connection.setLineColor(new java.awt.Color(0, 255, 0));
            LabelWidget label2 = new LabelWidget(GraphSceneImpl.this, "True");
            label2.setOpaque(true);
            label2.getActions().addAction(ActionFactory.createMoveAction());
            connection.addChild(label2);
            connection.setConstraint(label2, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        } else if (arg0.getTipo() == 2) {
            connection.setLineColor(new java.awt.Color(255, 0, 0));
            LabelWidget label2 = new LabelWidget(GraphSceneImpl.this, "False");
            label2.setOpaque(true);
            label2.getActions().addAction(ActionFactory.createMoveAction());
            connection.addChild(label2);
            connection.setConstraint(label2, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        }

        connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connection.setSourceAnchorShape(AnchorShape.TRIANGLE_OUT);

        connection.setPaintControlPoints(true);
        connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        connection.setRouter(RouterFactory.createOrthogonalSearchRouter(mainLayer));

        connection.getActions().addAction(ActionFactory.createAddRemoveControlPointAction(1.0, 5.0, ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY));
        connection.getActions().addAction(ActionFactory.createMoveControlPointAction(ActionFactory.createFreeMoveControlPointProvider(), ConnectionWidget.RoutingPolicy.ALWAYS_ROUTE));
        connection.getActions().addAction(createSelectAction());

        connectionLayer.addChild(connection);

        return connection;
    }

    protected void attachEdgeSourceAnchor(GenericEdge edge, GenericNode oldSourceNode, GenericNode sourceNode) {
        Widget w = sourceNode != null ? findWidget(sourceNode) : null;
        //((ConnectionWidget) findWidget(edge)).setSourceAnchor(AnchorFactory.createRectangularAnchor(w));
        //((ConnectionWidget) findWidget(edge)).setSourceAnchor(AnchorFactory.createCircularAnchor(w, 50));
        //((ConnectionWidget) findWidget(edge)).setSourceAnchor(AnchorFactory.createDirectionalAnchor(w, AnchorFactory.DirectionalAnchorKind.VERTICAL));
        ((ConnectionWidget) findWidget(edge)).setSourceAnchor(AnchorFactory.createCircularAnchor(w, 35));
    }

    protected void attachEdgeTargetAnchor(GenericEdge edge, GenericNode oldTargetNode, GenericNode targetNode) {
        Widget w = targetNode != null ? findWidget(targetNode) : null;
        //((ConnectionWidget) findWidget(edge)).setTargetAnchor(AnchorFactory.createRectangularAnchor(w));
        //((ConnectionWidget) findWidget(edge)).setTargetAnchor(AnchorFactory.createCircularAnchor(w, 50));
        //((ConnectionWidget) findWidget(edge)).setTargetAnchor(AnchorFactory.createDirectionalAnchor(w, AnchorFactory.DirectionalAnchorKind.VERTICAL));
        ((ConnectionWidget) findWidget(edge)).setTargetAnchor(AnchorFactory.createCircularAnchor(w, 35));
    }

    public void bringMaintoFront() {
        mainLayer.bringToFront();
    }

    private class SceneConnectProvider implements ConnectProvider {

        public boolean isSourceWidget(Widget arg0) {
            return true;
        }

        public ConnectorState isTargetWidget(Widget arg0, Widget arg1) {
            if ((arg0 != arg1) && ((arg1 instanceof BotbeansWidget) || (arg1 instanceof LabelWidget))) {
                return ConnectorState.ACCEPT;
            } else {
                return ConnectorState.REJECT;
            }
            //return arg0 != arg1 && arg1 instanceof IconNodeWidget ? ConnectorState.ACCEPT : ConnectorState.REJECT;
        }

        public boolean hasCustomTargetWidgetResolver(Scene arg0) {
            return false;
        }

        public Widget resolveTargetWidget(Scene arg0, Point arg1) {
            return null;
        }

        public void createConnection(Widget arg0, Widget arg1) {
            GenericNode node_origem = (GenericNode) findObject(arg0);
            GenericNode node_destino = (GenericNode) findObject(arg1);

            int edges_counter = GraphSceneImpl.this.getEdges().toArray().length;

            if (node_origem.validateSaidas() && node_destino.validateEntradas()) {
                GenericEdge edge = new GenericEdge();

                if (node_origem instanceof ConditionNode) {
                    if (node_origem.getSaidas() > 0) {
                        for (int i = 0; i < edges_counter; i++) {
                            GenericEdge edge_temp = (GenericEdge) (GraphSceneImpl.this.getEdges().toArray()[i]);
                            if (GraphSceneImpl.this.getEdgeSource(edge_temp).equals(node_origem)) {
                                if (edge_temp.getTipo() == 1) {
                                    edge.setTipo(2);
                                }
                                if (edge_temp.getTipo() == 2) {
                                    edge.setTipo(1);
                                }
                            }
                        }
                    } else {
                        edge.setTipo(1);
                    }
                } else {
                    edge.setTipo(0);
                }

                node_origem.addSaida();
                node_destino.addEntrada();

                addEdge(edge);
                setEdgeSource(edge, node_origem);
                setEdgeTarget(edge, node_destino);
                validate();
            } else {
                Message msg = new NotifyDescriptor.Message("Logic error!", NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(msg);
            }
        }
    }
}
