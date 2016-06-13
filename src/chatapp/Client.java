/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import chatpackage.ChatPackage;
import chatpackage.ChatUser;
import chatpackage.PackageConversation;
import chatpackage.PackageFriendList;
import chatpackage.PackageFriendRequest;
import chatpackage.PackageLogin;
import chatpackage.PackageMessage;
import chatpackage.PackageRegister;
import chatpackage.PackageSearchUser;
import chatpackage.PackageStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import static java.lang.Math.log;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.JOptionPane;
import static sun.util.logging.LoggingSupport.log;

/**
 *
 * @author Long
 */
public class Client {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    
    private ClientGUI cg;
    
    private String server, username, password;
    private int port;
    
    ArrayList<ChatUser> friends;
    ArrayList<ChatUser> pendingFriends;

    String text = "";
    int client_id;
    String client_username;
    boolean isLoggedIn = false;
    
    String id_con;
    
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
    
    void disconnect() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {} // not much else I can do
        try {
            if(sOutput != null) sOutput.close();
        } catch (Exception e) {}
        try {
            if(in != null) in.close();
        }
        catch(Exception e) {} // not much else I can do
        try {
            if(out != null) out.close();
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

    public void start() {
        try {
            socket = new Socket(server, port);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();
    }
    
    void sendObject(Object obj) {
        try {
            sOutput.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class ListenFromServer extends Thread {
        @Override
        public void run() {
            while (true) {
                ChatPackage inputPackage = null;

                try {
                    inputPackage = (ChatPackage) sInput.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                analyseInput(inputPackage);
            }
        }
    }
    
    public void analyseInput(ChatPackage inputPackage) {
        switch (inputPackage.getType()) {
            case "LOGIN":
                PackageLogin login = (PackageLogin) inputPackage;
                if (login.isConfirm()) {
                    text += "YAY" + "\n";
                    isLoggedIn = true;
                    client_id = login.getId();
                    client_username = login.getUsername();
                    
                    cg.setStatus(login.getUser().getStatus());

                    PackageFriendList friendList = new PackageFriendList();
                    sendObject(friendList);
                } else {
                    text += "NOPE" + "\n";
                }
                System.out.println(text);
                break;
            case "REGISTER":
                PackageRegister register = (PackageRegister) inputPackage;
                if (register.isAvailable()) {
                    text += "AVAILABLE" + "\n";
                    if (register.isSuccessful()) {
                        text += "REGISTER SUCCESSFUL" + "\n";
                    } else {

                        text += "REGISTER FAILED" + "\n";
                    }

                } else {
                    text += "NOT_AVAILABLE" + "\n";
                }
                System.out.println(text);
                break;
            case "SEARCH_USER":
                PackageSearchUser search_user = (PackageSearchUser) inputPackage;
                ArrayList<ChatUser> search_user_list = search_user.getList();
                for (int i = 0; i < search_user_list.size(); i++) {
                    text += search_user_list.get(i).getUsername() + "\n";
                }
                System.out.println(text);
                break;
            case "FRIEND_REQUEST":
                PackageFriendRequest request = (PackageFriendRequest) inputPackage;

                if (request.isRequest()) {
                    Object[] options = {"Yes",
                        "No"};
                    int n = cg.friendReqOption(options, request);

                    if (n == 1) {
                        request.setAccept(false);
                    } else {
                        request.setAccept(true);
                    }
                    
                    request.setRequest(false);
                    sendObject(request);
                } else if (request.isAccept()) {
                    cg.showDialog("FRIEND REQUEST ACCEPTED");
                } //do something like add the friend to the list

                break;
            case "FRIEND_LIST":
                PackageFriendList friendList = (PackageFriendList) inputPackage;
                friends = friendList.getFriends();
                pendingFriends = friendList.getPendingFriends();
                for (ChatUser friend : friends) {
                    text += friend.getUsername() + " ";
                    if (friend.isOnline()) {
                        text += friend.getStatus();
                    } else {
                        text += "OFFLINE";
                    }
                    text += "\n";
                }

                for (ChatUser friend : pendingFriends) {
                    text += friend.getUsername() + " ";
                    text += "FRIEND REQUEST";
                    text += "\n";
                }

                System.out.println(text);
                break;
            case "STATUS":
                PackageStatus status = (PackageStatus) inputPackage;
                
                if (status.getFriend_id() == client_id)
                {
                    cg.showDialog("FRIEND IS " + status.getStatus());
                }
                break;
            case "CONVERSATION":
                PackageConversation con = (PackageConversation) inputPackage;
                
                id_con = con.getId_con();
                ArrayList<chatpackage.ChatMessage> conversation = con.getConversation();
                for (int i = 0; i < conversation.size(); i++) {
                    text += conversation.get(i).getSender() + ":" + conversation.get(i).getMessage() + "\n";
                }
                
                System.out.println(text);
                break;
            case "MESSAGE":
                PackageMessage message = (PackageMessage) inputPackage;
                
                if (message.getReceiver() == client_id) {
                    text += message.getSender() + ":" + message.getMessage().getMessage() + "\n";
                }
                
                System.out.println(text);
                break;
            default:
                break;

        }
    }
}
