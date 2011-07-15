/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.control.network;

import org.botbeans.control.bluetooth.Robot;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 * Classe que recebe as ligacoes de clientes, como o botbeans
 * @author pedrodias
 */
public class Server implements Runnable {

    private ServerSocket serverSocket = null;
    private boolean running = true;
    private Robot nxt;
    private JTextArea gui;
    private boolean debug = false;

    public Server(int port, Robot nxt, JTextArea j, boolean debug) {
        this.nxt = nxt;
        gui = j;
        this.debug = debug;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
            System.exit(-1);
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        Socket clientSocket = null;
        while (running) {
            try {
                clientSocket = serverSocket.accept();
                gui.append("Client connected: " + clientSocket.getInetAddress() + "\n");
                Client client = new Client(clientSocket, nxt, gui, debug);
                new Thread(client).start();
            } catch (IOException e) {
                System.out.println("Accept failed: 4444");
                System.exit(-1);
            }
        }

    }
}
