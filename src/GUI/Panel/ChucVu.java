package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import BUS.ChucVuBUS;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import helper.Formater;

public final class ChucVu extends JPanel {

    public JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    ChucVuBUS nvBus = new ChucVuBUS(this);
    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableChucVu;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    public IntegratedSearch search;
    Main m;
    ArrayList<DTO.ChucVuDTO> listnv = nvBus.getAll();
    public ChucVuBUS cvbus = new ChucVuBUS();

    Color BackgroundColor = new Color(248, 249, 250);
    private DefaultTableModel tblModel;

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4 chỉ để thêm contentCenter ở giữa mà contentCenter không bị dính sát vào các thành phần khác
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

        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        // functionBar là thanh bên trên chứa các nút chức năng như thêm xóa sửa, và tìm kiếm
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentCenter.add(functionBar, BorderLayout.NORTH);

        String[] action = {"create", "update", "delete", "detail", /* "import", */ "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "chucvu", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(nvBus);
        }
        functionBar.add(mainFunction);
        search = new IntegratedSearch(new String[]{"Tất cả", "Tên chức vụ", "Mức lương"});
        functionBar.add(search);
        search.btnReset.addActionListener(nvBus);
        search.cbxChoose.addActionListener(nvBus);
        search.txtSearchForm.getDocument().addDocumentListener(new ChucVuBUS(search.txtSearchForm, this));

        // main là phần ở dưới để thống kê bảng biểu
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
//        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentCenter.add(main, BorderLayout.CENTER);

        tableChucVu = new JTable();
        tableChucVu.setBackground(new Color(0xA1D6E2));
        scrollTableSanPham = new JScrollPane();
        tableChucVu = new JTable();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"MCV", "Tên chức vụ", "Mức lương"};

        tblModel.setColumnIdentifiers(header);
        tableChucVu.setModel(tblModel);
        tableChucVu.setFocusable(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        tableChucVu.setDefaultRenderer(Object.class, centerRenderer);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableChucVu.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableChucVu.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableChucVu.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        scrollTableSanPham.setViewportView(tableChucVu);
        main.add(scrollTableSanPham);
    }

    

    public ChucVu(Main m) {
        this.m = m;
        initComponent();
        tableChucVu.setDefaultEditor(Object.class, null);
        loadDataTalbe(listnv);
    }

    public int getRow() {
        return tableChucVu.getSelectedRow();
    }

    public DTO.ChucVuDTO getChucVu() {
        return listnv.get(tableChucVu.getSelectedRow());
    }

    public void loadDataTalbe(ArrayList<DTO.ChucVuDTO> list) {
        tblModel.setRowCount(0);
        for (DTO.ChucVuDTO nhanVien : list) {
            tblModel.addRow(new Object[]{
                nhanVien.getMCV(), nhanVien.getTENCV(), Formater.FormatVND(nhanVien.getMUCLUONG())
            });
        }
    }
}
