package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import BUS.SanPhamBUS;
import BUS.ViTriTrungBayBUS;
import DTO.SanPhamDTO;
import DTO.ViTriTrungBayDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.itemTaskbar;
import GUI.Dialog.ViTriTrungBayDialog;
import helper.JTableExporter;

public final class ViTriTrungBay extends JPanel implements ActionListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter, right;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    JTable tableViTriTrungBay;
    JScrollPane scrollTableViTriTrungBay, scrollPane;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    Main m;
    public ViTriTrungBayBUS vtBUS = new ViTriTrungBayBUS();
    public SanPhamBUS spBUS = new SanPhamBUS();

    public ArrayList<ViTriTrungBayDTO> listVT = vtBUS.getAll();
    public ArrayList<SanPhamDTO> listSP = spBUS.getAll();

    Color BackgroundColor = new Color(248, 249, 250);

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);
        
        tableViTriTrungBay = new JTable();
        scrollTableViTriTrungBay = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã vị trí", "Tên vị trí", "Ghi chú"};
        tblModel.setColumnIdentifiers(header);
        tableViTriTrungBay.setModel(tblModel);
        scrollTableViTriTrungBay.setViewportView(tableViTriTrungBay);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableViTriTrungBay.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableViTriTrungBay.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableViTriTrungBay.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableViTriTrungBay.getColumnModel().getColumn(2).setPreferredWidth(300);
        
        tableViTriTrungBay.setFocusable(false);
        tableViTriTrungBay.setAutoCreateRowSorter(false);
        tableViTriTrungBay.setDefaultEditor(Object.class, null);
        
        tableViTriTrungBay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tableViTriTrungBay.getSelectedRow();
                if (index != -1) {
                    ArrayList<SanPhamDTO> listSP = spBUS.getByMaViTri(listVT.get(index).getMVT());
                    ListProductsInLocation(listSP);
                }
            }
        });
        
        initPadding();

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        // functionBar là thanh bên trên chứa các nút chức năng
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] action = {"create", "update", "delete", "detail", "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "vitritrungbay", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã vị trí", "Tên vị trí"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                listVT = vtBUS.search(txt, type);
                loadDataTable(listVT);
            }
        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            listVT = vtBUS.getAll();
            loadDataTable(listVT);
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        // main là phần ở dưới để hiển thị bảng
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
        main.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentCenter.add(main, BorderLayout.CENTER);
        main.add(scrollTableViTriTrungBay);
        
        right = new JPanel();
        right.setBackground(Color.WHITE);
        right.setLayout(new FlowLayout(0, 4, 10));
        right.setPreferredSize(new Dimension(400, 800));
        JLabel tit = new JLabel("Danh sách sản phẩm trong vị trí");
        tit.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 1, 16));
        right.add(tit);
        scrollPane = new JScrollPane(right, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBackground(Color.WHITE);
        contentCenter.add(scrollPane, BorderLayout.EAST);
    }

    public ViTriTrungBay(Main m) {
        this.m = m;
        initComponent();
        loadDataTable(listVT);
    }

    public void loadDataTable(ArrayList<ViTriTrungBayDTO> result) {
        tblModel.setRowCount(0);
        for (ViTriTrungBayDTO vt : result) {
            tblModel.addRow(new Object[]{
                vt.getMVT(), 
                vt.getTEN(), 
                vt.getGHICHU() != null ? vt.getGHICHU() : ""
            });
        }
    }

    public void ListProductsInLocation(ArrayList<SanPhamDTO> result) {
        right.removeAll();
        JLabel tit = new JLabel("Danh sách sản phẩm đang có ở vị trí");
        tit.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 1, 16));
        right.add(tit);
        itemTaskbar listItem[] = new itemTaskbar[result.size()];
        int i = 0;
        for (SanPhamDTO sp : result) {
            if (sp.getSOLUONG() != 0) {
                listItem[i] = new itemTaskbar(sp.getHINHANH(), sp.getTEN(), sp.getSOLUONG());
                right.add(listItem[i]);
                i++;
            }
        }

        if (i == 0) {
            if (result.isEmpty()) {
                JLabel lblIcon = new JLabel("Không có sản phẩm");
                lblIcon.setPreferredSize(new Dimension(380, 300));
                lblIcon.setIcon(new FlatSVGIcon("./icon/null.svg"));
                lblIcon.setHorizontalTextPosition(SwingConstants.CENTER);
                lblIcon.setVerticalTextPosition(SwingConstants.TOP);
                right.add(lblIcon);
            }
        }
        right.repaint();
        right.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            new ViTriTrungBayDialog(this, owner, "Thêm vị trí trưng bày mới", true, "create");
        } else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = getRowSelected();
            if (index != -1) {
                new ViTriTrungBayDialog(this, owner, "Chỉnh sửa vị trí trưng bày", true, "update", listVT.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            int index = getRowSelected();
            if (index != -1) {
                // Lấy danh sách sản phẩm hiện tại trong vị trí này
                ArrayList<SanPhamDTO> productsInLocation = spBUS.getByMaViTri(listVT.get(index).getMVT());
                
                // Kiểm tra xem có sản phẩm nào còn tồn kho không
                int productCount = 0;
                for (SanPhamDTO sp : productsInLocation) {
                    if (sp.getSOLUONG() > 0) {
                        productCount++;
                    }
                }
                
                if (productCount > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể xóa vị trí trưng bày này!\nVẫn còn " + productCount + " sản phẩm đang được trưng bày tại vị trí này.", 
                        "Không thể xóa", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int input = JOptionPane.showConfirmDialog(null, 
                    "Bạn có chắc chắn muốn xóa vị trí trưng bày này?", 
                    "Xóa vị trí trưng bày", 
                    JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    ViTriTrungBayDTO vt = listVT.get(index);
                    boolean success = vtBUS.delete(vt);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Xóa vị trí trưng bày thành công!");
                        loadDataTable(listVT);
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa vị trí trưng bày thất bại!");
                    }
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                new ViTriTrungBayDialog(this, owner, "Xem chi tiết vị trí trưng bày", true, "view", listVT.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableViTriTrungBay);
            } catch (IOException ex) {
                Logger.getLogger(ViTriTrungBay.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getRowSelected() {
        int index = tableViTriTrungBay.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vị trí trưng bày");
        }
        return index;
    }

    private void initPadding() {
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 20));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);

        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 20));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(20, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(20, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);
    }
}
