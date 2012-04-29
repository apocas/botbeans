package org.botbeans.shapes.nodes;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.botbeans.common.widgets.BotbeansWidget;
import org.botbeans.shapes.ShapeTopComponent;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;

public abstract class GenericNode implements Serializable {

    protected Image image;
    protected int componente;
    private int entradas = 0;
    private int saidas = 0;
    protected int entradas_limite;
    protected int saidas_limite;
    protected int id;
    protected Set prop = Sheet.createPropertiesSet();
    protected String description = "Unimplemented";
    protected String term = ";";

    /**
     * Actualiza a descricao do node
     */
    abstract void updateDescription();

    abstract public String getHash();

    public String getHashNode() {
        String aux = "";
        aux += componente + term + id + term + entradas + term + saidas;
        return aux;
    }

    abstract protected void processHash(String hash);

    /**
     * Executa a funcionalidade do node
     * @param topcomponent
     * @return proximo node
     */
    abstract public GenericNode execute(ShapeTopComponent tp);

    /**
     * Carrega atributos do node
     * @param hash
     */
    public void loadHash(String hash) {
        String[] aux = hash.split(term);
        id = Integer.parseInt(aux[1]);
        entradas = Integer.parseInt(aux[2]);
        saidas = Integer.parseInt(aux[3]);
        processHash(hash);
    }

    /**
     * Actualiza descricao do node e widget
     * @param w
     */
    public void updateDescription(BotbeansWidget w) {
        updateDescription();
//        if (this.componente != 14) {
        w.setLabel(description);
//        }
    }

    /**
     * Retorna o icon do node
     * @return icon do node
     */
    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return (this.id + "\n" + this.componente + "\n" + this.entradas + "\n" + this.entradas_limite + "\n" + this.saidas + "\n" + this.saidas_limite + "\n");
    }

    /**
     * Devolve o tipo de componente
     * @return componente
     */
    public int getComponente() {
        return componente;
    }

    /**
     * Adiciona um entrada
     */
    public void addEntrada() {
        entradas++;
    }

    /**
     * Adiciona uma saida
     */
    public void addSaida() {
        saidas++;
    }

    /**
     * Remove uma entrada
     */
    public void removeEntrada() {
        entradas--;
    }

    /**
     * Remove uma saida
     */
    public void removeSaida() {
        saidas--;
    }

    /**
     * Valida o numero de entradas
     * @return
     */
    public boolean validateEntradas() {
        if (getEntradas() >= getEntradasLimite()) {
            return false;
        }
        return true;
    }

    /**
     * Valida o numero de saidas
     * @return
     */
    public boolean validateSaidas() {
        if (getSaidas() >= getSaidasLimite()) {
            return false;
        }
        return true;
    }

    /**
     * Valida o node, validando o numero de entradas e saidas
     * @return
     */
    public boolean validatePortas() {
        if (this.validateEntradas() && this.validateSaidas()) {
            return true;
        }
        return false;
    }

    /**
     * Devolve o numero de entradas do node
     * @return
     */
    public int getEntradas() {
        return this.entradas;
    }

    /**
     * Devolve o numero de saidas do node
     * @return
     */
    public int getSaidas() {
        return this.saidas;
    }

    /**
     * Devolve o numero maximo de entradas valido
     * @return
     */
    public int getEntradasLimite() {
        return entradas_limite;
    }

    /**
     * Devolve o numero maximo de saidas valido
     * @return
     */
    public int getSaidasLimite() {
        return saidas_limite;
    }

    /**
     * Modifica o numero de entrada do node
     * @param entradas
     */
    public void setEntradas(int entradas) {
        this.entradas = entradas;
    }

    /**
     * Modifica o numero de saidas do node
     * @param saidas
     */
    public void setSaidas(int saidas) {
        this.saidas = saidas;
    }

    public int getId() {
        return id;
    }
    @SuppressWarnings("unchecked")
    private List listeners = Collections.synchronizedList(new LinkedList());

    @SuppressWarnings("unchecked")
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    @SuppressWarnings("unchecked")
    protected void fire(String propertyName, Object old, Object nue) {
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }

    /**
     * @return the prop
     */
    public Set getProp() {
        return prop;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the icon
     */
    public BotbeansWidget getIcon() {
        return (BotbeansWidget) ShapeTopComponent.getLastActivatedComponent().getScene().findWidget(this);
    }
}
