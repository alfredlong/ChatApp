/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

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
    public ClientGUI(String host, int port) {
        defaultHost = host;
        defaultPort = port;
        
        this.setResizable(false);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogout = new javax.swing.JButton();
        FramePanel = new javax.swing.JPanel();
        LoginScreen = new javax.swing.JPanel();
        LogoPanel = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        LoginPanel = new javax.swing.JPanel();
        LoginArea = new javax.swing.JPanel();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        txtPassword = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        MainScreen = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        LeftFrame = new javax.swing.JPanel();
        StatusPane = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        FriendList = new javax.swing.JList<>();
        RightFrame = new javax.swing.JPanel();
        ActionPane = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        ChatArea = new javax.swing.JTextArea();
        InputPanel = new javax.swing.JPanel();
        txtInput = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        btnLogout.setText("Log out");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        FramePanel.setPreferredSize(new java.awt.Dimension(800, 500));
        FramePanel.setLayout(new java.awt.CardLayout());

        LoginScreen.setLayout(new java.awt.BorderLayout());

        LogoPanel.setPreferredSize(new java.awt.Dimension(400, 500));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chatapp/logo.jpg"))); // NOI18N

        javax.swing.GroupLayout LogoPanelLayout = new javax.swing.GroupLayout(LogoPanel);
        LogoPanel.setLayout(LogoPanelLayout);
        LogoPanelLayout.setHorizontalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LogoPanelLayout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        LogoPanelLayout.setVerticalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LogoPanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(lblLogo)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        LoginScreen.add(LogoPanel, java.awt.BorderLayout.LINE_START);

        LoginPanel.setPreferredSize(new java.awt.Dimension(400, 500));
        LoginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtUsername.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblUsername.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUsername.setText("Username:");

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPassword.setText("Password:");

        javax.swing.GroupLayout LoginAreaLayout = new javax.swing.GroupLayout(LoginArea);
        LoginArea.setLayout(LoginAreaLayout);
        LoginAreaLayout.setHorizontalGroup(
            LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
            .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LoginAreaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblPassword)
                        .addComponent(lblUsername)
                        .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassword)
                            .addGroup(LoginAreaLayout.createSequentialGroup()
                                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        LoginAreaLayout.setVerticalGroup(
            LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
            .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(LoginAreaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblUsername)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(lblPassword)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(34, 34, 34)
                    .addGroup(LoginAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        LoginPanel.add(LoginArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, -1));

        LoginScreen.add(LoginPanel, java.awt.BorderLayout.CENTER);

        FramePanel.add(LoginScreen, "card1");

        MainScreen.setPreferredSize(new java.awt.Dimension(800, 500));
        MainScreen.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setDividerSize(2);

        LeftFrame.setLayout(new java.awt.BorderLayout());

        StatusPane.setMaximumSize(new java.awt.Dimension(100, 100));
        StatusPane.setPreferredSize(new java.awt.Dimension(100, 100));
        StatusPane.setLayout(new java.awt.BorderLayout());

        jButton1.setText("jButton1");
        StatusPane.add(jButton1, java.awt.BorderLayout.CENTER);

        LeftFrame.add(StatusPane, java.awt.BorderLayout.PAGE_START);

        FriendList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(FriendList);

        LeftFrame.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(LeftFrame);

        RightFrame.setLayout(new java.awt.BorderLayout());

        ActionPane.setMaximumSize(new java.awt.Dimension(100, 100));
        ActionPane.setPreferredSize(new java.awt.Dimension(100, 100));
        ActionPane.setLayout(new java.awt.BorderLayout());

        jButton2.setText("jButton1");
        ActionPane.add(jButton2, java.awt.BorderLayout.CENTER);

        RightFrame.add(ActionPane, java.awt.BorderLayout.PAGE_START);

        ChatArea.setColumns(20);
        ChatArea.setRows(5);
        jScrollPane2.setViewportView(ChatArea);

        RightFrame.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        InputPanel.setLayout(new java.awt.BorderLayout());
        InputPanel.add(txtInput, java.awt.BorderLayout.CENTER);

        btnSend.setLabel("Send");
        btnSend.setMaximumSize(new java.awt.Dimension(63, 23));
        btnSend.setMinimumSize(new java.awt.Dimension(63, 23));
        btnSend.setPreferredSize(new java.awt.Dimension(63, 23));
        InputPanel.add(btnSend, java.awt.BorderLayout.LINE_END);

        RightFrame.add(InputPanel, java.awt.BorderLayout.PAGE_END);

        jSplitPane2.setRightComponent(RightFrame);

        MainScreen.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        FramePanel.add(MainScreen, "card2");

        getContentPane().add(FramePanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void append(String str) {
        ChatArea.append(str);
        ChatArea.setCaretPosition(ChatArea.getText().length() - 1);
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
    
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
             
        if (checkValid(username, password)) {
            client = new Client(defaultHost, defaultPort, username, password);
            if(!client.start())
                return;

            CardLayout card = (CardLayout) FramePanel.getLayout();
            card.show(FramePanel, "card2");
            this.setResizable(true);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        
        if (checkValid(username, password)) {
            
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        CardLayout card = (CardLayout) FramePanel.getLayout();
        card.show(FramePanel, "card1");
    }//GEN-LAST:event_btnLogoutActionPerformed

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
    private javax.swing.JTextArea ChatArea;
    private javax.swing.JPanel FramePanel;
    private javax.swing.JList<String> FriendList;
    private javax.swing.JPanel InputPanel;
    private javax.swing.JPanel LeftFrame;
    private javax.swing.JPanel LoginArea;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel LoginScreen;
    private javax.swing.JPanel LogoPanel;
    private javax.swing.JPanel MainScreen;
    private javax.swing.JPanel RightFrame;
    private javax.swing.JPanel StatusPane;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtInput;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

}