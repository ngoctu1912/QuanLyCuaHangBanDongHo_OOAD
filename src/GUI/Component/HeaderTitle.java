package GUI.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;


/**
 *
 * @author Tran Nhat Sinh
 */
public class HeaderTitle extends JPanel{
    private JLabel lblTitle;
    
    public void initComponent(String title) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 0)); // Vàng đồng sang trọng
        this.setPreferredSize(new Dimension(400, 60));
        
        
        lblTitle = new JLabel();
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, 1, 18));
        lblTitle.setForeground(new Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setText(title);
        lblTitle.setPreferredSize(new Dimension(400, 50));
        this.add(lblTitle, BorderLayout.CENTER);
    }
    
    public HeaderTitle(String title) {
        initComponent(title);
    }
    public void setText (String title) {
        lblTitle.setText(title);
    }
    public void setColor(String color) {
        int hexColor  = Integer.parseInt(color ,16);
        this.setBackground(new Color(hexColor));
    }
}
