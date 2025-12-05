package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import BUS.MaKhuyenMaiBUS;
import BUS.SanPhamBUS;
import DTO.ChiTietMaKhuyenMaiDTO;
import DTO.MaKhuyenMaiDTO;
import DTO.NhanVienDTO;
import DTO.SanPhamDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Dialog.MaKhuyenMaiDialog;
import GUI.Dialog.TaoMaKhuyenMai;
import helper.Formater;
import helper.JTableExporter;

public class MaKhuyenMai extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel contentCenter;
    JTable tableMKM;
    JScrollPane scrollPane;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    IntegratedSearch search;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    Color BackgroundColor = new Color(248, 249, 250);
    DefaultTableModel tblModel;
    public MaKhuyenMaiBUS mkmBUS = new MaKhuyenMaiBUS();
    public SanPhamBUS spBUS = new SanPhamBUS();

    public ArrayList<MaKhuyenMaiDTO> listMKM = mkmBUS.getAll();
    public ArrayList<ChiTietMaKhuyenMaiDTO> listCTMKM = mkmBUS.getAllct();
    public ArrayList<SanPhamDTO> listSP = spBUS.getAll();

    TaoMaKhuyenMai nhapMKM;
    Main m;
    NhanVienDTO nv;

    private void initComponent() {
        tableMKM = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã khuyến mãi", "Thời gian bắt đầu", "Thời gian kết thúc"};
        tblModel.setColumnIdentifiers(header);
        tableMKM.setModel(tblModel);
        scrollTableSanPham.setViewportView(tableMKM);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tableMKM.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(0).setPreferredWidth(2);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        tableMKM.setFocusable(false);


        this.setBackground(BackgroundColor);
        this.setLayout(new GridLayout(1, 1));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setOpaque(true);

        // pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4 chỉ để thêm contentCenter ở giữa mà contentCenter không bị dính sát vào các thành phần khác
        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter);

        // functionBar là thanh bên trên chứa các nút chức năng như thêm xóa sửa, và tìm kiếm
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] action = {"create", "detail", "delete", "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "makhuyenmai", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả"});
        search.cbxChoose.addItemListener(this);
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String txt = search.txtSearchForm.getText();
                listMKM = mkmBUS.search(txt);
                loadDataTable(listMKM);
            }
        });

        search.btnReset.addActionListener(this);
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);
        // main là phần ở dưới để thống kê bảng biểu
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
//        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentCenter.add(main, BorderLayout.CENTER);
        main.add(scrollTableSanPham);

    }

    public MaKhuyenMai(Main m, NhanVienDTO nv) {
        this.m = m;
        this.nv = nv;
        initComponent();
        tableMKM.setDefaultEditor(Object.class, null);
        loadDataTable(listMKM);
    }

    public void loadDataTable(ArrayList<MaKhuyenMaiDTO> result) {
        tblModel.setRowCount(0);
        for (MaKhuyenMaiDTO kvk : result) {
            tblModel.addRow(new Object[]{
                kvk.getMKM(), Formater.FormatTime(kvk.getTGBD()), Formater.FormatTime(kvk.getTGKT())
            });
        }
    }


    public int getRowSelected() {
        int index = tableMKM.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã khuyến mãi");
        }
        return index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            nhapMKM = new TaoMaKhuyenMai(nv, "create", m);
            m.setPanel(nhapMKM);
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                new MaKhuyenMaiDialog(m, "Thông tin mã khuyến mãi", true, listMKM.get(index));
            }
        }
        else if (e.getSource() == mainFunction.btn.get("delete")) {
            int index = getRowSelected();
            if (index != -1) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn xóa mã khuyến mãi!", "Xóa mã khuyến mãi",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    MaKhuyenMaiDTO mkm = listMKM.get(index);
                    int c = mkmBUS.cancelMKM(mkm.getMKM());
                        if (c == 0) {
                            JOptionPane.showMessageDialog(null, "Xóa mã khuyến mãi không thành công!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Xóa mã khuyến mãi thành công!");
                            loadDataTable(mkmBUS.getAll());
                        }
                }
            }
        } else if (e.getSource() == search.btnReset) {
            search.txtSearchForm.setText("");
            listMKM = mkmBUS.getAll();
            loadDataTable(listMKM);
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableMKM);
            } catch (IOException ex) {
                Logger.getLogger(MaKhuyenMai.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String txt = search.txtSearchForm.getText();
        listMKM = mkmBUS.search(txt);
        loadDataTable(listMKM);
    }
}
