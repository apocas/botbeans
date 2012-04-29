/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.control.network;

import org.botbeans.control.bluetooth.Robot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *Classe que escuta novas mensagens com origem no botbeans
 * @author pedrodias
 */
public class Client implements Runnable {

    private Socket sock = null;
    private Robot nxt = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    boolean running = false;
    private final JTextArea gui;
    private boolean debug = false;

    public Client(Socket sock, Robot nxt, JTextArea gui, boolean debug) {
        this.nxt = nxt;
        this.gui = gui;
        this.running = true;
        this.debug = debug;
        try {
            this.sock = sock;
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Thread principal que reenvia as mensagens recebidas para o robot, reenvia tambem as mensagens do robot para o botbeans.
     */
    @Override
    public void run() {
        int[] data = new int[255];
        try {
            while (running && data != null && data.length >= 1) {
                data = receive();
                if (data != null) {
                    if (!debug) {
                        for (int i = 0; i < data.length; i++) {
                            System.out.print(data[i] + " ");
                        }
                        System.out.println("");
                        switch (data[0]) {
                            case 15:
                                nxt.send(buildPacket(data));
                                nxt.getDos().writeFloat(in.readFloat());
                                nxt.getDos().writeFloat(in.readFloat());
                                nxt.getDos().writeInt(in.readInt());
                                nxt.getDos().writeInt(in.readInt());
                                nxt.getDos().writeInt(in.readInt());
                                nxt.getDos().writeInt(in.readInt());
                                nxt.getDos().writeInt(in.readInt());
                                nxt.getDos().flush();
                                break;
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 18:
                            case 20:
                                nxt.send(buildPacket(data));
                                out.writeInt(nxt.readInt());
                                out.flush();
                                break;
                            case 16:
                                int d = nxt.getUltrasonic();
                                gui.append("Ultrasonic: " + d + "\n");
                                out.writeInt(d);
                                out.flush();
                                break;
                            case 17:
                                int dd = nxt.getMagnetic();
                                gui.append("Magnetic: " + dd + "\n");
                                out.writeInt(dd);
                                out.flush();
                                break;
                            case 19:
                                int ddd = nxt.getMicrophone();
                                gui.append("Microphone: " + ddd + "\n");
                                out.writeInt(ddd);
                                out.flush();
                                break;
                        }
                    } else {
                        gui.append("Received: " + data);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constroi um pacote de dados para ser enviado atavés de bluetooth
     * @param d
     * @return
     */
    public int[] buildPacket(int d[]) {
        int[] data = new int[d.length + 1];
        data[0] = d.length;
        for (int i = 1; i <= d.length; i++) {
            data[i] = d[i - 1];
        }
        return data;
    }

    public int[] receive() throws IOException {
        int size = in.readInt();
        if (size > 0) {
            System.out.println(size);
            int dd[] = new int[size];
            for (int i = 0; i < size; i++) {
                dd[i] = in.readInt();
            }
            return dd;
        } else {
            return null;
        }
    }

    public void send(int data[]) throws IOException {
        for (int i = 0; i < data.length; i++) {
            out.writeInt(data[i]);
            out.flush();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
