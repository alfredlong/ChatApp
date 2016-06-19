/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import chatpackage.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import javax.swing.JOptionPane;

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

    HashMap<String, ChatUser> friends;
    HashMap<String, ChatUser> pendingFriends;
    HashMap<String, GroupConversation> groupConversations;

    HashMap<String, String> pCon;
    HashMap<String, String> grCon;

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
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        } // not much else I can do

        // inform the GUI
        if (cg != null) {
            cg.connectionFailed();
        }
    }

    public void start() {
        pCon = new HashMap<>();
        grCon = new HashMap<>();
        friends = new HashMap<>();
        pendingFriends = new HashMap<>();
        groupConversations = new HashMap<>();

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
        Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread th, Throwable ex) {
                return;
            }
        };

        ListenFromServer listener = new ListenFromServer();
        Thread thread = new Thread(listener);
        thread.setUncaughtExceptionHandler(h);
        thread.start();
    }

    void sendObject(Object obj) {
        try {
            sOutput.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void PackageFriendDelProcess(ChatPackage inputPackage) {
        PackageFriendList friendList = new PackageFriendList();
        sendObject(friendList);
    }

    private void PackageClearProcess(ChatPackage inputPackage) {
        PackageClearConversation pkg = (PackageClearConversation)inputPackage;
        int friendId = pkg.getSender() == client_id ? pkg.getReceiver() : pkg.getSender();
        
        PackageConversation conversation = new PackageConversation();
        conversation.setId_userA(client_id);
        conversation.setId_userB(friendId);
        sendObject(conversation);

        cg.setConversation("");
    }

    class ListenFromServer implements Runnable {

        @Override
        public void run() {
            while (true) {

                if (socket.isClosed()) {
                    break;
                }

                ChatPackage inputPackage = null;

                try {
                    inputPackage = (ChatPackage) sInput.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                analyseInput(inputPackage);
            }
        }
    }

    public void analyseInput(ChatPackage inputPackage) {
        switch (inputPackage.getType()) {
            case "LOGIN":
                PackageLoginProcess(inputPackage);
                break;
            case "REGISTER":
                PackageRegisterProcess(inputPackage);
                break;
            case "SEARCH_USER":
                PackageSearchUserProcess(inputPackage);
                break;
            case "FRIEND_REQUEST":
                PackageFriendReqProcess(inputPackage);
                break;
            case "FRIEND_DELETE":
                PackageFriendDelProcess(inputPackage);
                break;
            case "FRIEND_LIST":
                PackageFriendListProcess(inputPackage);
                break;
            case "STATUS":
                PackageStatusProcess(inputPackage);
                break;
            case "CONVERSATION":
                PackageConversationProcess(inputPackage);
                break;
            case "MESSAGE":
                PackageMessageProcess(inputPackage);
                break;
            case "GROUP_CONVERSATION":
                PackageGroupConversationProcess(inputPackage);
                break;
            case "IMAGE":
                PackageImageProcess(inputPackage);
                break;
            case "CLEAR":
                PackageClearProcess(inputPackage);
                break;
            default:
                break;

        }
    }

    private void PackageImageProcess(ChatPackage inputPackage) {

    }

    private void PackageLoginProcess(ChatPackage inputPackage) {
        PackageLogin login = (PackageLogin) inputPackage;
        if (login.isConfirm()) {
            isLoggedIn = true;
            client_id = login.getId();
            client_username = login.getUsername();

            cg.setStatus(login.getUser().getStatus());

            PackageFriendList friendList = new PackageFriendList();
            sendObject(friendList);

            PackageStatus status = new PackageStatus(client_id, login.getUser().getStatus());
            sendObject(status);

            cg.LoginToMainScr();
        } else {
            cg.showDialog("Account does not exist");
        }
    }

    private void PackageRegisterProcess(ChatPackage inputPackage) {
        PackageRegister register = (PackageRegister) inputPackage;
        if (register.isAvailable()) {
            if (register.isSuccessful()) {
                cg.showDialog("Successfully registered!");
            } else {
                cg.showDialog("Register failed\nPlease try again");
            }
        } else {
            cg.showDialog("Username not available");
        }
    }

    private void PackageFriendListProcess(ChatPackage inputPackage) {
        friends.clear();
        pendingFriends.clear();

        PackageFriendList friendList = (PackageFriendList) inputPackage;
        ArrayList<ChatUser> f = friendList.getFriends();
        ArrayList<ChatUser> pf = friendList.getPendingFriends();

        for (ChatUser u : f) {
            friends.put(String.valueOf(u.getId()), u);

            PackageConversation conversation = new PackageConversation();
            conversation.setId_userA(client_id);
            conversation.setId_userB(u.getId());
            sendObject(conversation);
        }

        for (ChatUser u : pf) {
            pendingFriends.put(String.valueOf(u.getId()), u);

            PackageConversation conversation = new PackageConversation();
            conversation.setId_userA(client_id);
            conversation.setId_userB(u.getId());
            sendObject(conversation);
        }

        cg.initFriendList();

        PackageGroupConversation conver = new PackageGroupConversation();
        conver.setAction("CONVERSATION");
        conver.setId_sender(client_id);
        sendObject(conver);
    }

    private void PackageConversationProcess(ChatPackage inputPackage) {
        PackageConversation con = (PackageConversation) inputPackage;
        id_con = con.getId_con();
        ArrayList<chatpackage.ChatMessage> conversation = con.getConversation();
        String text = "";
        for (int i = 0; i < conversation.size(); i++) {
            String name = "";
            if (conversation.get(i).getSender() != client_id) {
                name = friends.get(String.valueOf(conversation.get(i).getSender()))
                        .getUsername();
            } else {
                name = client_username;
            }
            text += name + ": " + conversation.get(i).getContent() + "\n";
        }

        pCon.put(String.valueOf(con.getId_userB()), text);
        //cg.setConversation(text);
    }

    private void PackageSearchUserProcess(ChatPackage inputPackage) {
        PackageSearchUser search_user = (PackageSearchUser) inputPackage;
        ArrayList<ChatUser> search_user_list = search_user.getList();
        cg.initSearchList(search_user_list);
    }

    private void PackageFriendReqProcess(ChatPackage inputPackage) {
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
            PackageFriendList friendList = new PackageFriendList();
            sendObject(friendList);

            cg.showDialog("FRIEND REQUEST ACCEPTED");
        } //do something like add the friend to the list
    }

    private void PackageStatusProcess(ChatPackage inputPackage) {
        PackageStatus status = (PackageStatus) inputPackage;
        cg.updateFriendStatus(status.getId(), status.getStatus());
    }

    private void PackageMessageProcess(ChatPackage inputPackage) {
        PackageMessage message = (PackageMessage) inputPackage;

        String text = "";
        if (!message.isGroupMessage()) {
            if (message.getReceiver() == client_id) {
                String senderName = friends.get(String.valueOf(message.getSender())).getUsername();
                if (senderName == null) {
                    senderName = pendingFriends.get(String.valueOf(message.getSender())).getUsername();
                }

                text += senderName + ": " + message.getMessage().getContent() + "\n";
                if (cg.getSelectingTab() == 0) {
                    if (cg.getSelectingFriend() == message.getSender()) {
                        cg.append(text);
                    }
                } else if (cg.getSelectingTab() == 1) {
                    if (cg.getSelectingPendingFriend() == message.getSender()) {
                        cg.append(text);
                    }
                }

                pCon.put(String.valueOf(message.getSender()),
                        pCon.get(String.valueOf(message.getSender())) + text);
            }
        } else {
            String senderName = "[A lost soul]";
            for (ChatUser u : groupConversations.get(message.getId_con()).getList_user()) {
                if (message.getSender() == u.getId()) {
                    senderName = u.getUsername();
                    break;
                }
            }

            text += senderName + ": " + message.getMessage().getContent() + "\n";
            if (cg.getSelectingTab() == 2 && message.getId_con().equals(cg.getSelectingGroup())) {
                cg.append(text);
            }
            grCon.put(String.valueOf(message.getId_con()),
                    grCon.get(String.valueOf(message.getId_con())) + text);
        }
    }

    private void PackageGroupConversationProcess(ChatPackage inputPackage) {
        PackageGroupConversation conver = (PackageGroupConversation) inputPackage;
        switch (conver.getAction()) {
            case "CONVERSATION":
                groupConversations.clear();
                ArrayList<GroupConversation> convers = conver.getList_con();
                for (GroupConversation cv : convers) {
                    groupConversations.put(cv.getId_con(), cv);
                }

                for (GroupConversation gr : convers) {
                    String text = "";

                    for (ChatMessage msg : gr.getConversation()) {
                        String name = "[A lost soul]";
                        for (ChatUser user : gr.getList_user()) {
                            if (msg.getSender() == user.getId()) {
                                name = user.getUsername();
                                break;
                            }
                        }
                        text += name + ": " + msg.getContent() + "\n";
                    }
                    grCon.put(gr.getId_con(), text);
                }
                cg.initGroups();
                break;
            case "CREATE":
                GroupConversation gr = new GroupConversation();
                gr.setId_con(conver.getId_con());
                gr.setName(conver.getName());
                gr.setId_master(conver.getId_sender());
                groupConversations.put(conver.getId_con(), gr);
                cg.addGroup(gr);
                break;
            case "RENAME":
                cg.renameGroup(conver.getId_con(), conver.getName());
                break;
            case "ADD":
                cg.addToGroup(conver.getId_con(), conver.getUser());
                break;
            case "KICK":
                cg.kickOutGroup(conver.getId_con(), conver.getId_receiver());
                break;
            case "LEAVE":
                cg.leaveGroup(conver.getId_con(), conver.getId_sender());
                break;
//            case "PASS_MASTER":
//                break;
            case "DELETE":
                cg.deleteGroup(conver.getId_con());
                break;
        }
    }
}
