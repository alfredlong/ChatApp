/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Client {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;
    
    private ClientGUI cg;
    
    private String server, username, password;
    private int port;
    
    Client(String server, int port, String username, String password) {
        this(server, port, username, password, null);
    }
    
    Client(String server, int port, String username, String password, ClientGUI cg) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        this.cg = cg;
    }
    
    public boolean start() {
        try {
            socket = new Socket(server, port);
        } catch (IOException ex) {
            display("Error connectiong to server:" + ex);
            return false;
        }
        
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);
        
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            display("Exception creating new Input/output Streams: " + ex);
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try
        {
            sOutput.writeObject(username);
        } catch (IOException ex) {
            display("Exception doing login : " + ex);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    private void display(String msg) {
        if (cg == null)
            System.out.println(msg);
        else
            cg.append(msg + "\n");
    }
    
    void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException ex) {
            display("Exception writing to server: " + ex);
        }
    }
    
    private void disconnect() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {} // not much else I can do
        
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {} // not much else I can do

        try{
            if(socket != null) socket.close();
        }

        catch(Exception e) {} // not much else I can do

        // inform the GUI
        if(cg != null)
            cg.connectionFailed();
    }
    
    class ListenFromServer extends Thread {
        public void run() {
            while(true) {
                try {
                    String msg = (String)sInput.readObject();
                    cg.append(msg);
                } catch (IOException ex) {
                    display("Server has close the connection: " + ex);
                    if(cg != null)
                        cg.connectionFailed();
                    break;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
