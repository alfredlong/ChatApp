/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import chatpackage.ChatUser;
import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Long
 */
public class ImageJPanel extends JPanel{
    ChatUser user;
    JLabel label;

    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }
    
    ImageJPanel(String txt, String imgPath, ChatUser user) {
        this.user = user;
        label = new JLabel();
        label.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
                .getResource(imgPath))
                .getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        label.setText(txt);
        this.add(label);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    
    public void setIcon(String imgPath) {
        label.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass()
                .getResource(imgPath))
                .getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    }
    
    public void setName(String txt) {
        label.setText(txt);
    }
}
