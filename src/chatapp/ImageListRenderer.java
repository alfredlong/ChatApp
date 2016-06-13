/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.awt.Component;
import java.awt.Font;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Long
 */
public class ImageListRenderer extends DefaultListCellRenderer {
    Map<String, ImageIcon> imageMap;
    Font font = new Font("helvitica", Font.BOLD, 24);

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        label.setIcon(imageMap.get((String) value));
        label.setHorizontalTextPosition(JLabel.RIGHT);
        label.setFont(font);
        return label;
    }
}
