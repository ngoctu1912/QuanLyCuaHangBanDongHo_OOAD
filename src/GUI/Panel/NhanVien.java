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
import BUS.NhanVienBUS;
import DTO.ChucVuDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;

public final class NhanVien extends JPanel {

    public JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    NhanVienBUS nvBus = new NhanVienBUS(this);
    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableNhanVien;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    public IntegratedSearch search;
    Main m;
    ArrayList<DTO.NhanVienDTO> listnv = new ArrayList<>();
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

        String[] action = {"create", "update", "delete", "detail"/* , "import" */, "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "nhanvien", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(nvBus);
        }
        functionBar.add(mainFunction);
        search = new IntegratedSearch(new String[]{"Tất cả", "Họ tên", "Email"});
        functionBar.add(search);
        search.btnReset.addActionListener(nvBus);
        search.cbxChoose.addActionListener(nvBus);
        search.txtSearchForm.getDocument().addDocumentListener(new NhanVienBUS(search.txtSearchForm, this));

        // main là phần ở dưới để thống kê bảng biểu
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
//        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentCenter.add(main, BorderLayout.CENTER);

        tableNhanVien = new JTable();
        tableNhanVien.setBackground(new Color(0xA1D6E2));
        scrollTableSanPham = new JScrollPane();
        tableNhanVien = new JTable();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"MNV", "Họ tên", "Giới tính", "Ngày Sinh", "SDT", "Email", "Chức vụ"};

        tblModel.setColumnIdentifiers(header);
        tableNhanVien.setModel(tblModel);
        tableNhanVien.setFocusable(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        tableNhanVien.setDefaultRenderer(Object.class, centerRenderer);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableNhanVien.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        scrollTableSanPham.setViewportView(tableNhanVien);
        main.add(scrollTableSanPham);
    }

    

    public NhanVien(Main m) {
        this.m = m;
        initComponent();
        tableNhanVien.setDefaultEditor(Object.class, null);
        // Lấy mã chi nhánh của người đang đăng nhập và chỉ hiển thị nhân viên cùng chi nhánh
        try {
            String currentMcn = nvBus.getMcnbyMnv(m.user.getMNV());
            ArrayList<DTO.NhanVienDTO> all = nvBus.getAll();
            for (DTO.NhanVienDTO nvDto : all) {
                if (nvDto.getMCN() != null && nvDto.getMCN().equals(currentMcn)) {
                    listnv.add(nvDto);
                }
            }
        } catch (Exception e) {
            // nếu có lỗi, fallback: hiển thị tất cả
            listnv = nvBus.getAll();
        }
        loadDataTalbe(listnv);
    }

    public NhanVien(Main m, DTO.NhanVienDTO currentUser) {
        this.m = m;
        initComponent();
        tableNhanVien.setDefaultEditor(Object.class, null);
        try {
            String currentMcn = nvBus.getMcnbyMnv(currentUser.getMNV());
            ArrayList<DTO.NhanVienDTO> all = nvBus.getAll();
            for (DTO.NhanVienDTO nvDto : all) {
                if (nvDto.getMCN() != null && nvDto.getMCN().equals(currentMcn)) {
                    listnv.add(nvDto);
                }
            }
        } catch (Exception e) {
            listnv = nvBus.getAll();
        }
        loadDataTalbe(listnv);
    }

    public Integer getCurrentUserMNV() {
        if (this.m == null || this.m.user == null) return null;
        return this.m.user.getMNV();
    }

    public int getRow() {
        return tableNhanVien.getSelectedRow();
    }

    public DTO.NhanVienDTO getNhanVien() {
        return listnv.get(tableNhanVien.getSelectedRow());
    }

    public void loadDataTalbe(ArrayList<DTO.NhanVienDTO> list) {
        listnv = list;
        ArrayList<ChucVuDTO> listcv = cvbus.selectAll();
        tblModel.setRowCount(0);
        for (DTO.NhanVienDTO nhanVien : listnv) {
            tblModel.addRow(new Object[]{
                nhanVien.getMNV(), nhanVien.getHOTEN(), nhanVien.getGIOITINH() == 1 ? "Nam" : "Nữ", nhanVien.getNGAYSINH(), nhanVien.getSDT(), nhanVien.getEMAIL(),
                listcv.get(nhanVien.getMCV()-1).getTENCV()
            });
        }
    }
}
