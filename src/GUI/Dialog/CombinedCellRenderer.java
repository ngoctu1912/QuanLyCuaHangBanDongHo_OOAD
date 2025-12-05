package GUI.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class CombinedCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new BorderLayout());

        // Xử lý dữ liệu
        if (value instanceof ImageIcon) { // Nếu là hình ảnh
            JLabel label = new JLabel();
            ImageIcon originalIcon = (ImageIcon) value;
            Image scaledImage = originalIcon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            label.setIcon(scaledIcon);
            label.setPreferredSize(new Dimension(160, 120));
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); //them gach duoi
            panel.add(label, BorderLayout.CENTER);
        } else if (value instanceof MultiLineData) { // Nếu là kiểu MultiLineData
            MultiLineData data = (MultiLineData) value;
            JLabel label = new JLabel("<html>" + data.getLine1() + "<br>" + data.getLine2() + "<br><b>&nbsp;&nbsp;&nbsp;&nbsp;" + data.getLine3() + "</b></html>");
            label.setVerticalAlignment(JLabel.CENTER);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); //them gach duoi

            panel.add(label, BorderLayout.CENTER);
            panel.setOpaque(false);
        }

        // Xử lý trạng thái được chọn
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
            panel.setOpaque(true); // Đảm bảo hiển thị màu nền khi được chọn
        } else {
            panel.setBackground(table.getBackground());
            panel.setOpaque(true); // Đảm bảo hiển thị màu nền mặc định
        }

        return panel;
    }
}

// ...
// DefaultTableModel model = new DefaultTableModel();
// model.addColumn("Hình ảnh");
// model.addColumn("Mô tả");

// ImageIcon imageIcon = new ImageIcon("path/to/your/image.png");
// model.addRow(new Object[]{imageIcon, "Đây là một mô tả nhiều dòng"});

// JTable table = new JTable(model);
// table.setDefaultRenderer(Object.class, new CombinedCellRenderer());




/* S
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
public class WordWrapCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            MultiLineData data = (MultiLineData) value;
            JLabel label = new JLabel("<html>" + data.getLine1() + "<br>" + data.getLine2() + "</html>");
            label.setVerticalAlignment(JLabel.CENTER);
            // label.setHorizontalAlignment(JLabel.CENTER);
            panel.add(label,BorderLayout.CENTER);
            // Color bgcolor = new Color(0xA1D6E2);
            // Color bgcolor = new Color(193, 237, 220);
            // panel.setBackground(bgcolor);
            panel.setOpaque(false);
            return panel;
        }
    }
*/
/*
package GUI.Dialog;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.ImageIcon;
class ImageCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value instanceof ImageIcon) {
            ImageIcon originalIcon = (ImageIcon) value;
            Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            label.setIcon(scaledIcon);
            label.setPreferredSize(new Dimension(50, 50));
        }
        return label;
    }
}
*/