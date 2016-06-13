/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import chatpackage.ChatUser;
import chatpackage.PackageConversation;
import chatpackage.PackageFriendRequest;
import chatpackage.PackageLogin;
import chatpackage.PackageMessage;
import chatpackage.PackageRegister;
import chatpackage.PackageSearchUser;
import chatpackage.PackageStatus;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    private int defaultPort;
    private String defaultHost;
    
    /**
     * Creates new form MainScreen
     */
    
    private ImageIcon getImage(String path, int width, int height) {
        return new ImageIcon(new ImageIcon(getClass()
                .getResource(path))
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    
    private JPanel createListItem(String name, String path) {
        JLabel label = new JLabel();
        label.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
                .getResource(path))
                .getImage()
                .getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
        label.setText(name);
        JPanel pane = new JPanel();
        pane.add(label);
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        return pane;
    }
    
    public ClientGUI(String host, int port) {
        defaultHost = host;
        defaultPort = port;
        this.setResizable(false);
        
        initComponents();
               
        JPanel testpane = createListItem("Balala", "/chatapp/res/awake.png");
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < 3; i++) {
            model.addElement(testpane);
        }
        model.addElement(createListItem("thisisit", "/chatapp/res/sleep.png"));
        for (int i = 0; i < 3; i++) {
            model.addElement(testpane);
        }
        
//        for (int i = 0; i < model.size(); i++) {
//            JPanel p = (JPanel)model.getElementAt(i);
//            if (p.)
//        }
        FriendList.setModel(model);
        FriendList.setCellRenderer(new PanelRenderer());
        FriendList.validate();
        
        lblLogo.setIcon(getImage("/chatapp/res/logo.png",
                lblLogo.getWidth(),
                lblLogo.getHeight()));
        lblYourAvatar.setIcon(getImage("/chatapp/res/avatar.png",
                lblYourAvatar.getWidth(),
                lblYourAvatar.getHeight()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FramePanel = new javax.swing.JPanel();
        LoginScreen = new javax.swing.JPanel();
        LogoPanel = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        LoginArea = new javax.swing.JPanel();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        FriendList = new javax.swing.JList<>();
        RightFrame = new javax.swing.JPanel();
        ActionPane = new javax.swing.JPanel();
        Members = new javax.swing.JPanel();
        dummyFriend = new javax.swing.JTextField();
        dummyFriendId = new javax.swing.JTextField();
        dummyAddFriend = new javax.swing.JButton();
        dummnyGetCon = new javax.swing.JButton();
        Actions = new javax.swing.JPanel();
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
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LoginAreaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblPassword)
                        .addComponent(lblUsername)
                        .addGroup(LoginAreaLayout.createSequentialGroup()
                            .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        LoginAreaLayout.setVerticalGroup(
            LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginAreaLayout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
            .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LoginAreaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblUsername)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(lblPassword)
                    .addGap(63, 63, 63)
                    .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        LoginPanel.add(LoginArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

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
        StatusPane.add(lblYourAvatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 60));

        lblYourName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblYourName.setText("Your name");
        StatusPane.add(lblYourName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, -1));

        cbStatus.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ONLINE", "AWAY", "BUSY", "HIDDEN" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });
        StatusPane.add(cbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 80, 20));

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

    jScrollPane1.setBackground(new java.awt.Color(140, 198, 62));

    jScrollPane1.setViewportView(FriendList);

    ListPane.add(jScrollPane1, java.awt.BorderLayout.CENTER);

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

    dummyFriend.setText("friend");

    dummyFriendId.setText("friend id");

    dummyAddFriend.setText("add friend");
    dummyAddFriend.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            dummyAddFriendActionPerformed(evt);
        }
    });

    dummnyGetCon.setText("get con");
    dummnyGetCon.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            dummnyGetConActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout MembersLayout = new javax.swing.GroupLayout(Members);
    Members.setLayout(MembersLayout);
    MembersLayout.setHorizontalGroup(
        MembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MembersLayout.createSequentialGroup()
            .addGap(43, 43, 43)
            .addGroup(MembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dummyFriendId)
                .addComponent(dummyFriend))
            .addGap(18, 18, 18)
            .addGroup(MembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dummyAddFriend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dummnyGetCon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(246, Short.MAX_VALUE))
    );
    MembersLayout.setVerticalGroup(
        MembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MembersLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(MembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(dummyFriend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dummyAddFriend))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(MembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(dummyFriendId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dummnyGetCon))
            .addContainerGap(28, Short.MAX_VALUE))
    );

    ActionPane.add(Members, java.awt.BorderLayout.CENTER);

    Actions.setBackground(new java.awt.Color(140, 198, 62));
    Actions.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5));
    Actions.setLayout(new javax.swing.BoxLayout(Actions, javax.swing.BoxLayout.LINE_AXIS));

    btnStream.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/video-chat.png"))
    .getImage()
    .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    btnStream.setMaximumSize(new java.awt.Dimension(50, 50));
    btnStream.setMinimumSize(new java.awt.Dimension(50, 50));
    btnStream.setOpaque(false);
    btnStream.setPreferredSize(new java.awt.Dimension(50, 50));
    Actions.add(btnStream);

    btnCreateGroup.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/add-group.png"))
    .getImage()
    .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    btnCreateGroup.setMaximumSize(new java.awt.Dimension(50, 50));
    btnCreateGroup.setMinimumSize(new java.awt.Dimension(50, 50));
    btnCreateGroup.setPreferredSize(new java.awt.Dimension(50, 50));
    Actions.add(btnCreateGroup);

    btnLogout.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
        .getResource("/chatapp/res/logout.png"))
    .getImage()
    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
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
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void setStatus(String stt) {
        cbStatus.setSelectedItem(stt);
    }
    
    int friendReqOption(Object[] options, PackageFriendRequest request) {
        return JOptionPane.showOptionDialog(this,
                    "Friend request from " + request.getUserSender(),
                    "Friend request",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
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
        for (int i = 0; i < pass.length; i++)
            str += pass[i];
        return str;
    }
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username = txtUsername.getText().trim();
        String password = PassToStr(txtPassword.getPassword()).trim();
             
        if (checkValid(username, password)) {
            lblYourName.setText(username);
            
            client = new Client(defaultHost, defaultPort, username, password, this);
            client.start();
            
            PackageLogin login = new PackageLogin();
            login.setUsername(username);
            login.setPassword(password);
            client.sendObject(login);

            CardLayout card = (CardLayout) FramePanel.getLayout();
            card.show(FramePanel, "card2");
            this.setResizable(true);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String username = txtUsername.getText().trim();
        String password = PassToStr(txtPassword.getPassword()).trim();
        
        if (checkValid(username, password)) {
            PackageRegister register = new PackageRegister();
            register.setUsername(username);
            register.setPassword(password);
            register.setEmail("");
            client.sendObject(register);
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        client.disconnect();
        resetAll();
        CardLayout card = (CardLayout) FramePanel.getLayout();
        card.show(FramePanel, "card1");
        this.setResizable(false);
        this.setSize(800, 420);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        append(txtInput.getText() + "\n");
        txtInput.setText("");
        if (connected) {
            PackageMessage message = new PackageMessage(client.client_id, Integer.parseInt(dummyFriendId.getText()), txtInput.getText());
            message.setId_con(client.id_con);
            client.sendObject(message);
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFileActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        PackageSearchUser search = new PackageSearchUser(txtSearch.getText());
        client.sendObject(search);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        if (client.friends == null) 
            return;
        
        PackageStatus status = new PackageStatus(client.client_id, cbStatus.getSelectedItem().toString());
        client.sendObject(status);
        
        //if the friend_id != 0 you sent it to friends
        for (ChatUser friend : client.friends) {
            status = new PackageStatus(client.client_id, cbStatus.getSelectedItem().toString());
            status.setFriend_id(friend.getId());
            client.sendObject(status);
        }
    }//GEN-LAST:event_cbStatusActionPerformed

    private void dummyAddFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dummyAddFriendActionPerformed
        PackageFriendRequest request = new PackageFriendRequest(client.client_id, Integer.parseInt(dummyFriend.getText()));
        request.setRequest(true);
        client.sendObject(request);
    }//GEN-LAST:event_dummyAddFriendActionPerformed

    private void dummnyGetConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dummnyGetConActionPerformed
        PackageConversation conversation = new PackageConversation();
        conversation.setId_userA(client.client_id);
        conversation.setId_userB(Integer.parseInt(dummyFriendId.getText()));
        client.sendObject(conversation);
    }//GEN-LAST:event_dummnyGetConActionPerformed
   
    
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
                int portNumber = 2833;
                String serverAddress = "localhost";
                new ClientGUI(serverAddress, portNumber).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActionPane;
    private javax.swing.JPanel Actions;
    private javax.swing.JTextPane ChatArea;
    private javax.swing.JPanel FramePanel;
    private javax.swing.JList<String> FriendList;
    private javax.swing.JPanel InputPanel;
    private javax.swing.JPanel LeftFrame;
    private javax.swing.JPanel ListPane;
    private javax.swing.JPanel LoginArea;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel LoginScreen;
    private javax.swing.JPanel LogoPanel;
    private javax.swing.JPanel MainScreen;
    private javax.swing.JPanel Members;
    private javax.swing.JPanel RightFrame;
    private javax.swing.JPanel SearchPanel;
    private javax.swing.JPanel SendFuncs;
    private javax.swing.JPanel StatusPane;
    private javax.swing.JButton btnCreateGroup;
    private javax.swing.JButton btnFile;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnStream;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JButton dummnyGetCon;
    private javax.swing.JButton dummyAddFriend;
    private javax.swing.JTextField dummyFriend;
    private javax.swing.JTextField dummyFriendId;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblYourAvatar;
    private javax.swing.JLabel lblYourName;
    private javax.swing.JTextField txtInput;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    class PanelRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel renderer = (JPanel) value;
            renderer.setBackground(isSelected ? Color.green : Color.white);
            return renderer;
        }
    }
}
