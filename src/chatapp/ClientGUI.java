/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import chatpackage.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Long
 */
public class ClientGUI extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private boolean connected;
    private Client client;
    private int defaultPort = 2833;
    private String defaultHost = "localhost";
    private boolean isSearching = false;

    DefaultListModel friendModel;
    DefaultListModel pendingModel;
    DefaultListModel groupModel;
    DefaultListModel searchModel;
    DefaultListModel addModel;
    DefaultListModel removeModel;

    /**
     * Creates new form MainScreen
     */
    private ImageIcon getImage(String path, int width, int height) {
        return new ImageIcon(new ImageIcon(getClass()
                .getResource(path))
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public void addToGroup(String id, ChatUser newMem) {
        //client.groupConversations.get(id).getList_user().add(newMem);
        PackageGroupConversation conver = new PackageGroupConversation();
        conver.setAction("CONVERSATION");
        conver.setId_sender(client.client_id);
        client.sendObject(conver);
    }

    public void kickOutGroup(String idCon, int idUser) {
        PackageGroupConversation conver = new PackageGroupConversation();
        conver.setAction("CONVERSATION");
        conver.setId_sender(client.client_id);
        client.sendObject(conver);
    }

    public void leaveGroup(String idCon, int idUser) {
        if (idUser != client.client_id) {
            for (int i = 0; i < client.groupConversations.get(idCon).getList_user().size(); i++) {
                if (client.groupConversations.get(idCon).getList_user().get(i).getId() == idUser) {
                    client.groupConversations.get(idCon).getList_user().remove(i);
                }
            }
            for (int i = 0; i < groupModel.getSize(); i++) {
                ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(i);
                GroupConversation group = (GroupConversation) pane.getUser();
                if (group.getId_con().equals(idCon)) {
                    String imgPath = "/chatapp/res/group.png";
                    for (ChatUser u : group.getList_user()) {
                        if (u.getId() == idUser) {
                            group.getList_user().remove(u);
                            break;
                        }
                    }
                    groupModel.setElementAt(new ImageJPanel(group.getName(), imgPath, group), i);
                }
            }
        } else {
            client.groupConversations.remove(idCon);
            for (int i = 0; i < groupModel.getSize(); i++) {
                ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(i);
                GroupConversation group = (GroupConversation) pane.getUser();
                if (group.getId_con().equals(idCon)) {
                    groupModel.removeElementAt(i);
                }
            }
        }
    }

    public void deleteGroup(String idCon) {
        for (int i = 0; i < groupModel.getSize(); i++) {
            ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(i);
            GroupConversation group = (GroupConversation) pane.getUser();
            if (group.getId_con().equals(idCon)) {
                groupModel.removeElementAt(i);
            }
        }
        client.groupConversations.remove(idCon);
    }

    public void renameGroup(String idCon, String name) {
        for (int i = 0; i < groupModel.getSize(); i++) {
            ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(i);
            GroupConversation group = (GroupConversation) pane.getUser();
            if (group.getId_con().equals(idCon)) {
                String imgPath = "/chatapp/res/group.png";
                group.setName(name);
                groupModel.setElementAt(new ImageJPanel(name, imgPath, group), i);
            }
        }
        client.groupConversations.get(idCon).setName(name);
    }

    public int getSelectingTab() {
        return TabbedPanel.getSelectedIndex();
    }

    public int getSelectingFriend() {
        return ((ChatUser) ((ImageJPanel) friendModel.getElementAt(FriendList.getSelectedIndex())).getUser())
                .getId();
    }

    public int getSelectingPendingFriend() {
        return ((ChatUser) ((ImageJPanel) pendingModel.getElementAt(PendingList.getSelectedIndex())).getUser())
                .getId();
    }

    public String getSelectingGroup() {
        return ((GroupConversation) ((ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex())).getUser())
                .getId_con();
    }

    public void LoginToMainScr() {
        CardLayout card = (CardLayout) FramePanel.getLayout();
        card.show(FramePanel, "card2");
        this.setResizable(true);
    }

    public void initSearchList(ArrayList<ChatUser> search_user_list) {
        searchModel = new DefaultListModel();

        String imgPath = "/chatapp/res/stranger.png";
        for (ChatUser user : search_user_list) {
            searchModel.addElement(new ImageJPanel(user.getUsername(), imgPath, user));
        }

        SearchList.setModel(searchModel);
        SearchList.setCellRenderer(new PanelRenderer());
        SearchList.validate();
    }

    public void initFriendList() {
        friendModel = new DefaultListModel();
        pendingModel = new DefaultListModel();

        for (Map.Entry<String, ChatUser> entry : client.friends.entrySet()) {
            String imgPath = "";
            if (entry.getValue().isOnline()) {
                switch (entry.getValue().getStatus()) {
                    case "ONLINE":
                        imgPath = "/chatapp/res/awake.png";
                        break;
                    case "AWAY":
                        imgPath = "/chatapp/res/away.png";
                        break;
                    case "BUSY":
                        imgPath = "/chatapp/res/busy.png";
                        break;
                    case "HIDDEN":
                    case "OFFLINE":
                        imgPath = "/chatapp/res/sleep.png";
                        break;
                }
            } else {
                imgPath = "/chatapp/res/sleep.png";
            }
            friendModel.addElement(new ImageJPanel(entry.getValue().getUsername(), imgPath, entry.getValue()));
        }

        for (Map.Entry<String, ChatUser> entry : client.pendingFriends.entrySet()) {
            String imgPath = "/chatapp/res/stranger.png";
            pendingModel.addElement(new ImageJPanel(entry.getValue().getUsername(), imgPath, entry.getValue()));
        }

        FriendList.setModel(friendModel);
        FriendList.setCellRenderer(new PanelRenderer());
        FriendList.validate();

        PendingList.setModel(pendingModel);
        PendingList.setCellRenderer(new PanelRenderer());
        PendingList.validate();
    }

    public void addGroup(GroupConversation gr) {
        String imgPath = "/chatapp/res/group.png";
        groupModel.addElement(new ImageJPanel(gr.getName(), imgPath, gr));
    }

    public void initGroups() {
        groupModel = new DefaultListModel();

        for (Map.Entry<String, GroupConversation> entry : client.groupConversations.entrySet()) {
            String imgPath = "/chatapp/res/group.png";
            groupModel.addElement(new ImageJPanel(entry.getValue().getName(), imgPath, entry.getValue()));
        }

        GroupList.setModel(groupModel);
        GroupList.setCellRenderer(new PanelRenderer());
        GroupList.validate();
    }

    public ClientGUI() {
        this.setResizable(false);

        initComponents();

        lblLogo.setIcon(getImage("/chatapp/res/logo.png",
                lblLogo.getWidth(),
                lblLogo.getHeight()));
        lblYourAvatar.setIcon(getImage("/chatapp/res/avatar.png",
                lblYourAvatar.getWidth(),
                lblYourAvatar.getHeight()));
        btnAddContacts.setVisible(false);
        btnGroupFunction.setVisible(false);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (connected) {
                    client.disconnect();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopupSetting = new javax.swing.JPopupMenu();
        iViewMembers = new javax.swing.JMenuItem();
        iRename = new javax.swing.JMenuItem();
        iKick = new javax.swing.JMenuItem();
        iLeave = new javax.swing.JMenuItem();
        iDelete = new javax.swing.JMenuItem();
        AddToGroupPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        AddList = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        btnAddToGroup = new javax.swing.JButton();
        RemoveFromGroupPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        RemoveList = new javax.swing.JList<>();
        btnRemoveFromGroup = new javax.swing.JButton();
        FramePanel = new javax.swing.JPanel();
        LoginScreen = new javax.swing.JPanel();
        LogoPanel = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        LoginArea = new javax.swing.JPanel();
        txtAddress = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        MainScreen = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        LeftFrame = new javax.swing.JPanel();
        StatusPane = new javax.swing.JPanel();
        lblYourAvatar = new javax.swing.JLabel();
        lblYourName = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        ListPane = new javax.swing.JPanel();
        SearchPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        ListCardPanel = new javax.swing.JPanel();
        TabbedPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        FriendList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        PendingList = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        GroupList = new javax.swing.JList<>();
        SearchResult = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        SearchList = new javax.swing.JList<>();
        btnSearchReturn = new javax.swing.JButton();
        RightFrame = new javax.swing.JPanel();
        ActionPane = new javax.swing.JPanel();
        Members = new javax.swing.JPanel();
        ContactInfoPanel = new javax.swing.JPanel();
        Actions = new javax.swing.JPanel();
        btnGroupFunction = new javax.swing.JButton();
        btnAddContacts = new javax.swing.JButton();
        btnStream = new javax.swing.JButton();
        btnCreateGroup = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        InputPanel = new javax.swing.JPanel();
        txtInput = new javax.swing.JTextField();
        SendFuncs = new javax.swing.JPanel();
        btnFile = new javax.swing.JButton();
        btnImage = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        ChatArea = new javax.swing.JTextPane();

        iViewMembers.setText("View members");
        iViewMembers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iViewMembersActionPerformed(evt);
            }
        });
        PopupSetting.add(iViewMembers);

        iRename.setText("Rename group");
        iRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iRenameActionPerformed(evt);
            }
        });
        PopupSetting.add(iRename);

        iKick.setText("Remove member");
        iKick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iKickActionPerformed(evt);
            }
        });
        PopupSetting.add(iKick);

        iLeave.setText("Leave group");
        iLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iLeaveActionPerformed(evt);
            }
        });
        PopupSetting.add(iLeave);

        iDelete.setText("Delete group");
        iDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iDeleteActionPerformed(evt);
            }
        });
        PopupSetting.add(iDelete);

        AddToGroupPanel.setLayout(new java.awt.BorderLayout());

        AddList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(AddList);

        AddToGroupPanel.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        btnAddToGroup.setText("Add");
        btnAddToGroup.setMaximumSize(new java.awt.Dimension(60, 30));
        btnAddToGroup.setMinimumSize(new java.awt.Dimension(60, 30));
        btnAddToGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToGroupActionPerformed(evt);
            }
        });
        jPanel1.add(btnAddToGroup, java.awt.BorderLayout.CENTER);

        AddToGroupPanel.add(jPanel1, java.awt.BorderLayout.SOUTH);

        RemoveFromGroupPanel.setLayout(new java.awt.BorderLayout());

        RemoveList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(RemoveList);

        RemoveFromGroupPanel.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        btnRemoveFromGroup.setText("Remove");
        btnRemoveFromGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFromGroupActionPerformed(evt);
            }
        });
        RemoveFromGroupPanel.add(btnRemoveFromGroup, java.awt.BorderLayout.PAGE_END);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Live Chat!");

        FramePanel.setBackground(new java.awt.Color(140, 198, 62));
        FramePanel.setPreferredSize(new java.awt.Dimension(800, 400));
        FramePanel.setLayout(new java.awt.CardLayout());

        LoginScreen.setBackground(new java.awt.Color(140, 198, 62));
        LoginScreen.setLayout(new java.awt.BorderLayout());

        LogoPanel.setBackground(new java.awt.Color(140, 198, 62));
        LogoPanel.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout LogoPanelLayout = new javax.swing.GroupLayout(LogoPanel);
        LogoPanel.setLayout(LogoPanelLayout);
        LogoPanelLayout.setHorizontalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LogoPanelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        LogoPanelLayout.setVerticalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LogoPanelLayout.createSequentialGroup()
                .addContainerGap(168, Short.MAX_VALUE)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        LoginScreen.add(LogoPanel, java.awt.BorderLayout.LINE_START);

        LoginPanel.setBackground(new java.awt.Color(140, 198, 62));
        LoginPanel.setPreferredSize(new java.awt.Dimension(300, 500));
        LoginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LoginArea.setBackground(new java.awt.Color(140, 198, 62));

        txtAddress.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("IP Address:");

        btnLogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegister.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        txtUsername.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblUsername.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUsername.setText("Username:");

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPassword.setText("Password:");

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout LoginAreaLayout = new javax.swing.GroupLayout(LoginArea);
        LoginArea.setLayout(LoginAreaLayout);
        LoginAreaLayout.setHorizontalGroup(
            LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginAreaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblPassword)
                                .addComponent(lblUsername)
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(LoginAreaLayout.createSequentialGroup()
                        .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LoginAreaLayout.createSequentialGroup()
                                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        LoginAreaLayout.setVerticalGroup(
            LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        LoginPanel.add(LoginArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 250));

        LoginScreen.add(LoginPanel, java.awt.BorderLayout.CENTER);

        FramePanel.add(LoginScreen, "card1");

        MainScreen.setBackground(new java.awt.Color(140, 198, 62));
        MainScreen.setPreferredSize(new java.awt.Dimension(800, 500));
        MainScreen.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setBackground(new java.awt.Color(140, 198, 62));
        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setDividerSize(2);

        LeftFrame.setBackground(new java.awt.Color(140, 198, 62));
        LeftFrame.setLayout(new java.awt.BorderLayout());

        StatusPane.setBackground(new java.awt.Color(140, 198, 62));
        StatusPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        StatusPane.setMaximumSize(new java.awt.Dimension(100, 100));
        StatusPane.setPreferredSize(new java.awt.Dimension(100, 100));
        StatusPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblYourAvatar.setText("Image");
        StatusPane.add(lblYourAvatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 70));

        lblYourName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblYourName.setText("Your name");
        StatusPane.add(lblYourName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        cbStatus.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ONLINE", "AWAY", "BUSY", "HIDDEN" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });
        StatusPane.add(cbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 80, 20));

        LeftFrame.add(StatusPane, java.awt.BorderLayout.PAGE_START);

        ListPane.setBackground(new java.awt.Color(140, 198, 62));
        ListPane.setLayout(new java.awt.BorderLayout());

        SearchPanel.setBackground(new java.awt.Color(140, 198, 62));
        SearchPanel.setLayout(new java.awt.BorderLayout());
        SearchPanel.add(txtSearch, java.awt.BorderLayout.CENTER);

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnSearch.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
            .getResource("/chatapp/res/search.png"))
        .getImage()
        .getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
btnSearch.setMaximumSize(new java.awt.Dimension(30, 21));
btnSearch.setMinimumSize(new java.awt.Dimension(30, 21));
btnSearch.setPreferredSize(new java.awt.Dimension(30, 21));
btnSearch.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchActionPerformed(evt);
    }
    });
    SearchPanel.add(btnSearch, java.awt.BorderLayout.EAST);

    ListPane.add(SearchPanel, java.awt.BorderLayout.PAGE_START);

    ListCardPanel.setLayout(new java.awt.CardLayout());

    TabbedPanel.setBackground(new java.awt.Color(140, 198, 62));
    TabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            TabbedPanelStateChanged(evt);
        }
    });

    jScrollPane1.setBackground(new java.awt.Color(140, 198, 62));

    FriendList.setFixedCellHeight(30);
    FriendList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            FriendListValueChanged(evt);
        }
    });
    jScrollPane1.setViewportView(FriendList);

    TabbedPanel.addTab("Friends", jScrollPane1);

    jScrollPane2.setBackground(new java.awt.Color(140, 198, 62));

    PendingList.setFixedCellHeight(30);
    PendingList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            PendingListValueChanged(evt);
        }
    });
    jScrollPane2.setViewportView(PendingList);

    TabbedPanel.addTab("Pending", jScrollPane2);

    jScrollPane4.setBackground(new java.awt.Color(140, 198, 62));

    GroupList.setFixedCellHeight(30);
    GroupList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            GroupListValueChanged(evt);
        }
    });
    jScrollPane4.setViewportView(GroupList);

    TabbedPanel.addTab("Groups", jScrollPane4);

    ListCardPanel.add(TabbedPanel, "list1");

    SearchResult.setBackground(new java.awt.Color(140, 198, 62));
    SearchResult.setLayout(new java.awt.BorderLayout());

    jScrollPane5.setBackground(new java.awt.Color(140, 198, 62));

    SearchList.setModel(new javax.swing.AbstractListModel<String>() {
        String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
        public int getSize() { return strings.length; }
        public String getElementAt(int i) { return strings[i]; }
    });
    jScrollPane5.setViewportView(SearchList);

    SearchResult.add(jScrollPane5, java.awt.BorderLayout.CENTER);

    btnSearchReturn.setText("Return");
    btnSearchReturn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSearchReturnActionPerformed(evt);
        }
    });
    SearchResult.add(btnSearchReturn, java.awt.BorderLayout.PAGE_END);

    ListCardPanel.add(SearchResult, "list2");

    ListPane.add(ListCardPanel, java.awt.BorderLayout.CENTER);

    LeftFrame.add(ListPane, java.awt.BorderLayout.CENTER);

    jSplitPane2.setLeftComponent(LeftFrame);

    RightFrame.setBackground(new java.awt.Color(140, 198, 62));
    RightFrame.setLayout(new java.awt.BorderLayout());

    ActionPane.setBackground(new java.awt.Color(140, 198, 62));
    ActionPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    ActionPane.setMaximumSize(new java.awt.Dimension(100, 100));
    ActionPane.setPreferredSize(new java.awt.Dimension(100, 100));
    ActionPane.setLayout(new java.awt.BorderLayout());

    Members.setBackground(new java.awt.Color(140, 198, 62));
    Members.setLayout(new java.awt.BorderLayout());

    ContactInfoPanel.setBackground(new java.awt.Color(140, 198, 62));

    javax.swing.GroupLayout ContactInfoPanelLayout = new javax.swing.GroupLayout(ContactInfoPanel);
    ContactInfoPanel.setLayout(ContactInfoPanelLayout);
    ContactInfoPanelLayout.setHorizontalGroup(
        ContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 0, Short.MAX_VALUE)
    );
    ContactInfoPanelLayout.setVerticalGroup(
        ContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 96, Short.MAX_VALUE)
    );

    Members.add(ContactInfoPanel, java.awt.BorderLayout.CENTER);

    ActionPane.add(Members, java.awt.BorderLayout.CENTER);

    Actions.setBackground(new java.awt.Color(140, 198, 62));
    Actions.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5));
    Actions.setMaximumSize(new java.awt.Dimension(360, 52));
    Actions.setMinimumSize(new java.awt.Dimension(360, 52));
    Actions.setLayout(new javax.swing.BoxLayout(Actions, javax.swing.BoxLayout.LINE_AXIS));

    btnGroupFunction.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/settings.png"))
    .getImage()
    .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    btnGroupFunction.setBorder(null);
    btnGroupFunction.setMaximumSize(new java.awt.Dimension(50, 50));
    btnGroupFunction.setMinimumSize(new java.awt.Dimension(50, 50));
    btnGroupFunction.setOpaque(false);
    btnGroupFunction.setPreferredSize(new java.awt.Dimension(50, 50));
    btnGroupFunction.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGroupFunctionActionPerformed(evt);
        }
    });
    Actions.add(btnGroupFunction);

    btnAddContacts.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/add-contacts.png"))
    .getImage()
    .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    btnAddContacts.setBorder(null);
    btnAddContacts.setBorderPainted(false);
    btnAddContacts.setMaximumSize(new java.awt.Dimension(50, 50));
    btnAddContacts.setMinimumSize(new java.awt.Dimension(50, 50));
    btnAddContacts.setOpaque(false);
    btnAddContacts.setPreferredSize(new java.awt.Dimension(50, 50));
    btnAddContacts.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAddContactsActionPerformed(evt);
        }
    });
    Actions.add(btnAddContacts);

    btnStream.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/video-chat.png"))
    .getImage()
    .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    btnStream.setBorder(null);
    btnStream.setMaximumSize(new java.awt.Dimension(50, 50));
    btnStream.setMinimumSize(new java.awt.Dimension(50, 50));
    btnStream.setOpaque(false);
    btnStream.setPreferredSize(new java.awt.Dimension(50, 50));
    btnStream.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnStreamActionPerformed(evt);
        }
    });
    Actions.add(btnStream);

    btnCreateGroup.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/add-group.png"))
    .getImage()
    .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    btnCreateGroup.setBorder(null);
    btnCreateGroup.setMaximumSize(new java.awt.Dimension(50, 50));
    btnCreateGroup.setMinimumSize(new java.awt.Dimension(50, 50));
    btnCreateGroup.setPreferredSize(new java.awt.Dimension(50, 50));
    btnCreateGroup.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCreateGroupActionPerformed(evt);
        }
    });
    Actions.add(btnCreateGroup);

    btnLogout.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/logout.png"))
    .getImage()
    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    btnLogout.setBorder(null);
    btnLogout.setMaximumSize(new java.awt.Dimension(50, 50));
    btnLogout.setMinimumSize(new java.awt.Dimension(50, 50));
    btnLogout.setPreferredSize(new java.awt.Dimension(50, 50));
    btnLogout.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnLogoutActionPerformed(evt);
        }
    });
    Actions.add(btnLogout);

    ActionPane.add(Actions, java.awt.BorderLayout.EAST);

    RightFrame.add(ActionPane, java.awt.BorderLayout.PAGE_START);

    InputPanel.setBackground(new java.awt.Color(140, 198, 62));
    InputPanel.setLayout(new java.awt.BorderLayout());
    InputPanel.add(txtInput, java.awt.BorderLayout.CENTER);

    SendFuncs.setBackground(new java.awt.Color(140, 198, 62));
    SendFuncs.setMaximumSize(new java.awt.Dimension(120, 23));
    SendFuncs.setMinimumSize(new java.awt.Dimension(120, 23));
    SendFuncs.setPreferredSize(new java.awt.Dimension(120, 23));
    SendFuncs.setLayout(new javax.swing.BoxLayout(SendFuncs, javax.swing.BoxLayout.LINE_AXIS));

    btnFile.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/file.png"))
    .getImage()
    .getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
    btnFile.setMaximumSize(new java.awt.Dimension(35, 27));
    btnFile.setMinimumSize(new java.awt.Dimension(35, 27));
    btnFile.setPreferredSize(new java.awt.Dimension(35, 27));
    btnFile.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnFileActionPerformed(evt);
        }
    });
    SendFuncs.add(btnFile);

    btnImage.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/image.png"))
    .getImage()
    .getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
    btnImage.setMaximumSize(new java.awt.Dimension(35, 27));
    btnImage.setMinimumSize(new java.awt.Dimension(35, 27));
    btnImage.setPreferredSize(new java.awt.Dimension(35, 27));
    SendFuncs.add(btnImage);

    btnSend.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/send.png"))
    .getImage()
    .getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
    btnSend.setMaximumSize(new java.awt.Dimension(50, 27));
    btnSend.setMinimumSize(new java.awt.Dimension(50, 27));
    btnSend.setPreferredSize(new java.awt.Dimension(50, 27));
    btnSend.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSendActionPerformed(evt);
        }
    });
    SendFuncs.add(btnSend);

    InputPanel.add(SendFuncs, java.awt.BorderLayout.LINE_END);

    RightFrame.add(InputPanel, java.awt.BorderLayout.PAGE_END);

    jScrollPane3.setBackground(new java.awt.Color(140, 198, 62));

    ChatArea.setEditable(false);
    jScrollPane3.setViewportView(ChatArea);

    RightFrame.add(jScrollPane3, java.awt.BorderLayout.CENTER);

    jSplitPane2.setRightComponent(RightFrame);

    MainScreen.add(jSplitPane2, java.awt.BorderLayout.CENTER);

    FramePanel.add(MainScreen, "card2");

    getContentPane().add(FramePanel, java.awt.BorderLayout.CENTER);

    pack();
    }// </editor-fold>//GEN-END:initComponents

    void append(String str) {
        StyledDocument document = (StyledDocument) ChatArea.getDocument();
        try {
            document.insertString(document.getLength(), str, null);
            ChatArea.setCaretPosition(document.getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setConversation(String str) {
        ChatArea.setText(str);
    }

    void setStatus(String stt) {
        cbStatus.setSelectedItem(stt);
    }

    void updateFriendStatus(int id, String status) {
        int selectingIdx = FriendList.getSelectedIndex();

        int pos = 0;
        for (int i = 0; i < friendModel.size(); i++) {
            ImageJPanel pane = (ImageJPanel) friendModel.getElementAt(i);
            int curId = ((ChatUser) pane.getUser()).getId();
            if (curId == id) {
                pos = i;
            }
        }
        ImageJPanel friend = (ImageJPanel) friendModel.getElementAt(pos);
        String imgPath = "";
        switch (status) {
            case "ONLINE":
                imgPath = "/chatapp/res/awake.png";
                break;
            case "AWAY":
                imgPath = "/chatapp/res/away.png";
                break;
            case "BUSY":
                imgPath = "/chatapp/res/busy.png";
                break;
            case "HIDDEN":
            case "OFFLINE":
                imgPath = "/chatapp/res/sleep.png";
                break;
        }

        friend.setIcon(imgPath);
        friendModel.setElementAt(friend, pos);
    }

    int friendReqOption(Object[] options, PackageFriendRequest request) {
        int ans = JOptionPane.showOptionDialog(this,
                "You have friend request from " + request.getUser().getUsername(),
                "Friend request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (ans == 0) {
            String imgPath = "";
            if (request.getUser().isOnline()) {
                switch (request.getUser().getStatus()) {
                    case "ONLINE":
                        imgPath = "/chatapp/res/awake.png";
                        break;
                    case "AWAY":
                        imgPath = "/chatapp/res/away.png";
                        break;
                    case "BUSY":
                        imgPath = "/chatapp/res/busy.png";
                        break;
                    case "HIDDEN":
                    case "OFFLINE":
                        imgPath = "/chatapp/res/sleep.png";
                        break;
                }
            } else {
                imgPath = "/chatapp/res/sleep.png";
            }
            friendModel.addElement(new ImageJPanel(request.getUser().getUsername(), imgPath, request.getUser()));
        }

        return ans;
    }

    void showDialog(String str) {
        JOptionPane.showMessageDialog(null, str);
    }

    void connectionFailed() {
        connected = false;
    }

    private boolean checkValid(String name, String pass) {
        if (name.length() == 0 || pass.length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Username and Password cannot be blank",
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        String special = "!@#$%^&*()";
        String pattern = ".*[" + Pattern.quote(special) + "].*";
        if (name.matches(pattern)
                || pass.matches(pattern)
                || name.contains(" ")
                || pass.contains(" ")) {
            JOptionPane.showMessageDialog(null,
                    "Username and Password cannot contain special character: !@#$%^&*()",
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void resetAll() {
        txtInput.setText("");
        ChatArea.setText("");
    }

    private String PassToStr(char[] pass) {
        String str = "";
        for (int i = 0; i < pass.length; i++) {
            str += pass[i];
        }
        return str;
    }
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username = txtUsername.getText().trim();
        String password = PassToStr(txtPassword.getPassword()).trim();

        if (checkValid(username, password)) {
            String host = "";
            if (txtAddress.getText() != "") {
                host = txtAddress.getText();
            } else {
                host = defaultHost;
            }

            lblYourName.setText(username);
            connected = true;
            client = new Client(host, defaultPort, username, password, this);
            client.start();

            PackageLogin login = new PackageLogin();
            login.setUsername(username);
            login.setPassword(password);
            client.sendObject(login);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String username = txtUsername.getText().trim();
        String password = PassToStr(txtPassword.getPassword()).trim();

        if (checkValid(username, password)) {
            lblYourName.setText(username);

            client = new Client(defaultHost, defaultPort, username, password, this);
            client.start();

            PackageRegister register = new PackageRegister();
            register.setUsername(username);
            register.setPassword(password);
            register.setEmail("");
            client.sendObject(register);
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        PackageLogout logout = new PackageLogout();
        logout.setId(client.client_id);
        client.sendObject(logout);

        client.disconnect();
        resetAll();
        CardLayout card = (CardLayout) FramePanel.getLayout();
        card.show(FramePanel, "card1");
        this.setResizable(false);
        this.setSize(800, 420);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        int pane = TabbedPanel.getSelectedIndex();
        if (!(FriendList.getSelectedIndex() == -1
                && PendingList.getSelectedIndex() == -1
                && GroupList.getSelectedIndex() == -1)) {
            if (txtInput.getText().length() != 0) {
                if (connected) {
                    if (pane != 2) {
                        append(client.client_username + ": " + txtInput.getText() + "\n");
                        int friendId;
                        if (pane == 0) {
                            friendId = ((ChatUser) ((ImageJPanel) friendModel.getElementAt(FriendList.getSelectedIndex()))
                                    .getUser())
                                    .getId();
                        } else {
                            friendId = ((ChatUser) ((ImageJPanel) pendingModel.getElementAt(PendingList.getSelectedIndex()))
                                    .getUser())
                                    .getId();
                        }
                        PackageMessage message = new PackageMessage(client.client_id, friendId, txtInput.getText());
                        message.setId_con(client.id_con);
                        client.sendObject(message);

                        client.pCon.put(String.valueOf(friendId),
                                client.pCon.get(String.valueOf(friendId))
                                + String.valueOf(friendId) + ": " + txtInput.getText() + "\n");
                    } else {
                        String receiver = ((GroupConversation) (((ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex())).getUser()))
                                .getId_con();
                        PackageMessage message = new PackageMessage(client.client_id, receiver, txtInput.getText());
                        
                        client.sendObject(message);

//                        client.grCon.put(String.valueOf(receiver),
//                                client.grCon.get(String.valueOf(receiver))
//                                + String.valueOf(receiver) + ": " + txtInput.getText() + "\n");
                    }
                }
            }
            txtInput.setText("");
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFileActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        PackageSearchUser search = new PackageSearchUser(txtSearch.getText());
        client.sendObject(search);

        btnAddContacts.setVisible(true);
        CardLayout card = (CardLayout) ListCardPanel.getLayout();
        card.show(ListCardPanel, "list2");

        isSearching = true;
        btnAddContacts.setVisible(true);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        if (client.friends.size() == 0) {
            return;
        }

        PackageStatus status = new PackageStatus(client.client_id, cbStatus.getSelectedItem().toString());
        client.sendObject(status);
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btnSearchReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchReturnActionPerformed
        CardLayout card = (CardLayout) ListCardPanel.getLayout();
        card.show(ListCardPanel, "list1");

        isSearching = false;

        if (TabbedPanel.getSelectedIndex() != 1) {
            btnAddContacts.setVisible(false);
        }
    }//GEN-LAST:event_btnSearchReturnActionPerformed

    private void FriendListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_FriendListValueChanged
        if (FriendList.getSelectedIndex() != -1) {
            ImageJPanel obj = (ImageJPanel) friendModel.getElementAt(FriendList.getSelectedIndex());
            int friend_id = ((ChatUser) obj.getUser()).getId();

            setConversation(client.pCon.get(String.valueOf(friend_id)));
        }
    }//GEN-LAST:event_FriendListValueChanged

    private void btnAddContactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddContactsActionPerformed
        if (!isSearching) {
            if (TabbedPanel.getSelectedIndex() == 1) {
                if (PendingList.getSelectedIndex() != -1) {
                    ImageJPanel pane = (ImageJPanel)pendingModel.getElementAt(PendingList.getSelectedIndex());
                    ChatUser pFriend = (ChatUser)pane.getUser();
                    PackageFriendRequest request = new PackageFriendRequest(client.client_id,
                            pFriend.getId());
                    request.setRequest(false);
                    request.setAccept(true);
                    client.sendObject(request);

                    //Add friend here
                    String imgPath = "";
                    if (pFriend.isOnline()) {
                        switch (pFriend.getStatus()) {
                            case "ONLINE":
                                imgPath = "/chatapp/res/awake.png";
                                break;
                            case "AWAY":
                                imgPath = "/chatapp/res/away.png";
                                break;
                            case "BUSY":
                                imgPath = "/chatapp/res/busy.png";
                                break;
                            case "HIDDEN":
                            case "OFFLINE":
                                imgPath = "/chatapp/res/sleep.png";
                                break;
                        }
                    } else {
                        imgPath = "/chatapp/res/sleep.png";
                    }
                    friendModel.addElement(new ImageJPanel(pFriend.getUsername(), imgPath, pFriend));
                    
                    client.pendingFriends.remove(String.valueOf(pFriend.getId()));
                    pendingModel.removeElementAt(PendingList.getSelectedIndex());
                }
            } else if (GroupList.getSelectedIndex() != -1) {
                JFrame frame = new JFrame();

                ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
                GroupConversation obj = (GroupConversation) pane.getUser();
                String groupId = obj.getId_con();

                addModel = new DefaultListModel();

                for (Map.Entry<String, ChatUser> entry : client.friends.entrySet()) {
                    String imgPath = "";
                    if (entry.getValue().isOnline()) {
                        switch (entry.getValue().getStatus()) {
                            case "ONLINE":
                                imgPath = "/chatapp/res/awake.png";
                                break;
                            case "AWAY":
                                imgPath = "/chatapp/res/away.png";
                                break;
                            case "BUSY":
                                imgPath = "/chatapp/res/busy.png";
                                break;
                            case "HIDDEN":
                            case "OFFLINE":
                                imgPath = "/chatapp/res/sleep.png";
                                break;
                        }
                    } else {
                        imgPath = "/chatapp/res/sleep.png";
                    }

                    boolean isExisted = false;
                    if (client.groupConversations.get(groupId).getList_user() != null) {
                        for (ChatUser u : client.groupConversations.get(groupId).getList_user()) {
                            if (entry.getValue().getId() == u.getId()) {
                                isExisted = true;
                                break;
                            }
                        }
                    }
                    if (!isExisted) {
                        addModel.addElement(new ImageJPanel(entry.getValue().getUsername(), imgPath, entry.getValue()));
                    }
                }

                AddList.setModel(addModel);
                AddList.setCellRenderer(new PanelRenderer());
                AddList.validate();

                frame.add(AddToGroupPanel);
                frame.setTitle("Add new members");
                frame.setMinimumSize(new Dimension(200, 300));
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
                frame.setVisible(true);
            }
        } else if (SearchList.getSelectedIndex() != -1) {
            ImageJPanel obj = (ImageJPanel) searchModel.getElementAt(SearchList.getSelectedIndex());
            PackageFriendRequest request = new PackageFriendRequest(client.client_id,
                    ((ChatUser) obj.getUser()).getId());
            request.setRequest(true);
            client.sendObject(request);
        }
    }//GEN-LAST:event_btnAddContactsActionPerformed

    private void TabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPanelStateChanged
        if (TabbedPanel.getSelectedIndex() != 0) {
            btnAddContacts.setVisible(true);
        } else {
            btnAddContacts.setVisible(false);
        }

        if (TabbedPanel.getSelectedIndex() == 2) {
            btnGroupFunction.setVisible(true);
        } else {
            btnGroupFunction.setVisible(false);
        }
    }//GEN-LAST:event_TabbedPanelStateChanged

    private void btnCreateGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateGroupActionPerformed
        String groupName = JOptionPane.showInputDialog("Please choose your group name");
        if (groupName != null) {
            PackageGroupConversation pkg = new PackageGroupConversation();
            pkg.setAction("CREATE");
            pkg.setName(groupName);
            pkg.setId_sender(client.client_id);
            client.sendObject(pkg);
        }
    }//GEN-LAST:event_btnCreateGroupActionPerformed

    private void btnGroupFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupFunctionActionPerformed
        if (GroupList.getSelectedIndex() != -1) {
            PopupSetting.show(btnGroupFunction,
                    btnGroupFunction.getX(),
                    btnGroupFunction.getY() + btnGroupFunction.getHeight() / 2 + 5);
        }
    }//GEN-LAST:event_btnGroupFunctionActionPerformed

    private void iViewMembersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iViewMembersActionPerformed
        JFrame frame = new JFrame();

        JList list = new JList();
        DefaultListModel model = new DefaultListModel();

        ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
        ArrayList<ChatUser> members = ((GroupConversation) pane.getUser()).getList_user();

        for (ChatUser u : members) {
            if (u.getId() != client.client_id) {
                model.addElement(u.getUsername());
            }
        }

        list.setModel(model);

        frame.add(list);
        frame.setTitle("Members");
        frame.setMinimumSize(new Dimension(200, 300));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        frame.setVisible(true);
    }//GEN-LAST:event_iViewMembersActionPerformed

    private void iRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iRenameActionPerformed
        String groupName = JOptionPane.showInputDialog("Please type your group name:");
        if (groupName != null) {
            ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
            GroupConversation group = (GroupConversation) pane.getUser();

            PackageGroupConversation pkg = new PackageGroupConversation();
            pkg.setAction("RENAME");
            pkg.setId_sender(client.client_id);
            pkg.setId_con(group.getId_con());
            pkg.setName(groupName);
            client.sendObject(pkg);
        }
    }//GEN-LAST:event_iRenameActionPerformed

    private void iKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iKickActionPerformed
        JFrame frame = new JFrame();
        removeModel = new DefaultListModel();

        ImageJPanel pane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
        ArrayList<ChatUser> members = ((GroupConversation) pane.getUser()).getList_user();

        for (ChatUser u : members) {
            String imgPath = "";
            if (u.isOnline()) {
                switch (u.getStatus()) {
                    case "ONLINE":
                        imgPath = "/chatapp/res/awake.png";
                        break;
                    case "AWAY":
                        imgPath = "/chatapp/res/away.png";
                        break;
                    case "BUSY":
                        imgPath = "/chatapp/res/busy.png";
                        break;
                    case "HIDDEN":
                    case "OFFLINE":
                        imgPath = "/chatapp/res/sleep.png";
                        break;
                }
            } else {
                imgPath = "/chatapp/res/sleep.png";
            }
            if (u.getId() != client.client_id) {
                removeModel.addElement(new ImageJPanel(u.getUsername(), imgPath, u));
            }
        }

        RemoveList.setModel(removeModel);
        RemoveList.setCellRenderer(new PanelRenderer());
        RemoveList.validate();

        frame.add(RemoveFromGroupPanel);
        frame.setTitle("Remove members");
        frame.setMinimumSize(new Dimension(200, 300));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        frame.setVisible(true);
    }//GEN-LAST:event_iKickActionPerformed

    private void iLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iLeaveActionPerformed
        ImageJPanel gPane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());

        PackageGroupConversation pkg = new PackageGroupConversation();
        pkg.setAction("LEAVE");
        pkg.setId_sender(client.client_id);
        pkg.setId_con(((GroupConversation) gPane.getUser()).getId_con());
        client.sendObject(pkg);
    }//GEN-LAST:event_iLeaveActionPerformed

    private void PendingListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_PendingListValueChanged
        if (PendingList.getSelectedIndex() != -1) {
            ImageJPanel obj = (ImageJPanel) pendingModel.getElementAt(PendingList.getSelectedIndex());
            int friend_id = ((ChatUser) obj.getUser()).getId();

            setConversation(client.pCon.get(String.valueOf(friend_id)));
        }
    }//GEN-LAST:event_PendingListValueChanged

    private void GroupListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_GroupListValueChanged
        if (GroupList.getSelectedIndex() != -1) {
            ImageJPanel obj = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
            String id = ((GroupConversation) obj.getUser()).getId_con();
            int idMaster = ((GroupConversation) obj.getUser()).getId_master();

            setConversation(client.grCon.get(String.valueOf(id)));

            if (client.client_id == idMaster) {
                iViewMembers.setEnabled(true);
                iRename.setEnabled(true);
                iKick.setEnabled(true);
                iDelete.setEnabled(true);
                iLeave.setEnabled(false);
                btnAddContacts.setEnabled(true);
            } else {
                iViewMembers.setEnabled(true);
                iRename.setEnabled(false);
                iKick.setEnabled(false);
                iDelete.setEnabled(false);
                iLeave.setEnabled(true);
                btnAddContacts.setEnabled(false);
            }
        }
    }//GEN-LAST:event_GroupListValueChanged

    private void btnAddToGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToGroupActionPerformed
        if (AddList.getSelectedIndex() != -1) {
            PackageGroupConversation pkg = new PackageGroupConversation();

            ImageJPanel gPane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
            ImageJPanel aPane = (ImageJPanel) addModel.getElementAt(AddList.getSelectedIndex());

            pkg.setId_con(((GroupConversation) gPane.getUser()).getId_con());
            pkg.setId_sender(client.client_id);
            pkg.setId_receiver(((ChatUser) aPane.getUser()).getId());
            pkg.setAction("ADD");

            client.sendObject(pkg);
            addModel.remove(AddList.getSelectedIndex());
        }
    }//GEN-LAST:event_btnAddToGroupActionPerformed

    private void btnRemoveFromGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFromGroupActionPerformed
        if (RemoveList.getSelectedIndex() != -1) {
            PackageGroupConversation pkg = new PackageGroupConversation();

            ImageJPanel gPane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());
            ImageJPanel rPane = (ImageJPanel) removeModel.getElementAt(RemoveList.getSelectedIndex());

            pkg.setId_con(((GroupConversation) gPane.getUser()).getId_con());
            pkg.setId_sender(client.client_id);
            pkg.setId_receiver(((ChatUser) rPane.getUser()).getId());
            pkg.setAction("KICK");

            client.sendObject(pkg);
            removeModel.remove(RemoveList.getSelectedIndex());
        }
    }//GEN-LAST:event_btnRemoveFromGroupActionPerformed

    private void iDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iDeleteActionPerformed
        ImageJPanel gPane = (ImageJPanel) groupModel.getElementAt(GroupList.getSelectedIndex());

        PackageGroupConversation pkg = new PackageGroupConversation();
        pkg.setAction("DELETE");
        pkg.setId_sender(client.client_id);
        pkg.setId_con(((GroupConversation) gPane.getUser()).getId_con());
        client.sendObject(pkg);
    }//GEN-LAST:event_iDeleteActionPerformed

    private void btnStreamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStreamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnStreamActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActionPane;
    private javax.swing.JPanel Actions;
    private javax.swing.JList<String> AddList;
    private javax.swing.JPanel AddToGroupPanel;
    private javax.swing.JTextPane ChatArea;
    private javax.swing.JPanel ContactInfoPanel;
    private javax.swing.JPanel FramePanel;
    private javax.swing.JList<String> FriendList;
    private javax.swing.JList<String> GroupList;
    private javax.swing.JPanel InputPanel;
    private javax.swing.JPanel LeftFrame;
    private javax.swing.JPanel ListCardPanel;
    private javax.swing.JPanel ListPane;
    private javax.swing.JPanel LoginArea;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel LoginScreen;
    private javax.swing.JPanel LogoPanel;
    private javax.swing.JPanel MainScreen;
    private javax.swing.JPanel Members;
    private javax.swing.JList<String> PendingList;
    private javax.swing.JPopupMenu PopupSetting;
    private javax.swing.JPanel RemoveFromGroupPanel;
    private javax.swing.JList<String> RemoveList;
    private javax.swing.JPanel RightFrame;
    private javax.swing.JList<String> SearchList;
    private javax.swing.JPanel SearchPanel;
    private javax.swing.JPanel SearchResult;
    private javax.swing.JPanel SendFuncs;
    private javax.swing.JPanel StatusPane;
    private javax.swing.JTabbedPane TabbedPanel;
    private javax.swing.JButton btnAddContacts;
    private javax.swing.JButton btnAddToGroup;
    private javax.swing.JButton btnCreateGroup;
    private javax.swing.JButton btnFile;
    private javax.swing.JButton btnGroupFunction;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRemoveFromGroup;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchReturn;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnStream;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JMenuItem iDelete;
    private javax.swing.JMenuItem iKick;
    private javax.swing.JMenuItem iLeave;
    private javax.swing.JMenuItem iRename;
    private javax.swing.JMenuItem iViewMembers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblYourAvatar;
    private javax.swing.JLabel lblYourName;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtInput;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    class PanelRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ImageJPanel renderer = (ImageJPanel) value;
            renderer.setBackground(isSelected ? Color.green : Color.white);
            return renderer;
        }
    }
}
