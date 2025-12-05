package GUI.Component;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public final class ComboRenderer implements ListCellRenderer {

    private JLabel label;

    @Override
    public Component getListCellRendererComponent(JList list, Object val, int index, boolean selected, boolean focused) {
        if (val instanceof Component) {
            Component c = (Component) val;
            if (selected) {
                c.setBackground(list.getSelectionBackground());
                c.setForeground(list.getSelectionForeground());
            } else {
                c.setBackground(list.getBackground());
                c.setForeground(list.getForeground());
            }
            return c;
        } else {
            if (label == null) {
                label = new JLabel(val.toString());
            } else {
                label.setText(val.toString());
            }
            return label;
        }
    }

}
