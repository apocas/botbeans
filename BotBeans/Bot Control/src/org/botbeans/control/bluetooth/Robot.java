/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.control.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.nxt.remote.NXTCommand;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

/**
 * Classe que representa o robot
 * @author Apocas
 */
public class Robot implements Runnable {

    private NXTConnector conn = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private byte data[] = new byte[255];
    private boolean running = false;
    private int size = 0;

    public Robot() {
    }

    public boolean connect(NXTInfo ni) throws IOException, InterruptedException {
        conn = new NXTConnector();
        //boolean connected = conn.connectTo("btspp://");
        boolean connected = conn.connectTo(ni, NXTComm.PACKET);

        if (!connected) {
            return false;
        }

        dos = conn.getDataOut();
        dis = conn.getDataIn();
        running = true;
        //new Thread(this).start();
        return running;
    }

    /**
     * Inicia o programa de controlo no robot
     * @param program
     * @throws InterruptedException
     * @throws IOException
     */
    public static void startProgram(String program) throws InterruptedException, IOException {
        NXTConnector con = new NXTConnector();
        con.connectTo("btspp://");
        NXTCommand nxtc = new NXTCommand();
        nxtc.setNXTComm(con.getNXTComm());

        //DebugDialog dd = new DebugDialog("" + nxtc.startProgram(program));
        nxtc.startProgram(program);

        con.close();
    }

    /**
     * Envia uma mensagem, em bytes, para o robot
     * @param data
     * @throws IOException
     */
    public void send(byte data[]) throws IOException {
        dos.write(data);
        dos.flush();
    }

    /**
     * Envia uma mesangem para o robot, utilizando valores inteiros.
     * @param data
     * @throws IOException
     */
    public void send(int data[]) throws IOException {
        for (int i = 0; i < data.length; i++) {
            dos.writeInt(data[i]);
            dos.flush();
        }
    }

    public void run() {
        while (running) {
            try {
                size = dis.read(data);
            } catch (IOException ex) {
                Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Desliga a ligação bluetooth ao robot.
     * @throws IOException
     */
    public void disconnect() throws IOException {
        running = false;
        dis.close();
        dos.close();
        conn.close();
    }

    /**
     * Verifica se esta ligado
     * @return
     */
    public boolean isConnected() {
        return running;
    }

    /**
     * Le um valor inteiro
     * @return
     * @throws IOException
     */
    public int readInt() throws IOException {
        return dis.readInt();
    }

    /**
     * Le valor float
     * @return
     * @throws IOException
     */
    public float readFloat() throws IOException {
        return dis.readFloat();
    }

    /**
     * Pede uma nova medicao do sensor de ultrasons
     * @return
     * @throws IOException
     */
    public int getUltrasonic() throws IOException {
        int d[] = {1, 16};
        int dist = -1;
        if (isConnected()) {
            send(d);
            dist = dis.readInt();
        }
        return dist;
    }

    public int getMicrophone() throws IOException {
        int d[] = {1, 19};
        int db = -1;
        if (isConnected()) {
            send(d);
            db = dis.readInt();
        }
        return db;
    }

    public int getMagnetic() throws IOException {
        int d[] = {1, 17};
        int dist = -1;
        if (isConnected()) {
            send(d);
            dist = dis.readInt();
        }
        return dist;
    }
    
    public DataOutputStream getDos() {
        return dos;
    }
}
