package GUI.Component;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JButton;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;


public class ButtonToolBar extends JButton{
    String permisson;
    
    public ButtonToolBar(String text, String icon, String permisson) {
        this.permisson = permisson;
        this.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 1, 14));
        this.setForeground(new Color(184, 134, 11)); // Vàng đồng sang trọng
        this.setIcon(new FlatSVGIcon("./icon/"+icon));
        this.setText(text);
        this.setFocusable(false);
        this.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.putClientProperty("JButton.buttonType", "toolBarButton");
    }
    
    public String getPermisson() {
        return this.permisson;
    }
    
}
