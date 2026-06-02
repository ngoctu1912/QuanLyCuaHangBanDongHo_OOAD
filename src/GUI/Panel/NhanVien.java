package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
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
    JComboBox<String> cbxBranch;
    Main m;
    ArrayList<DTO.NhanVienDTO> listnv = new ArrayList<>();
    public ChucVuBUS cvbus = new ChucVuBUS();
    private boolean suppressBranchEvents;

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
        functionBar.setLayout(new BorderLayout(10, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentCenter.add(functionBar, BorderLayout.NORTH);

        String[] action = {"create", "update", "delete", "detail"/* , "import" */, "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "nhanvien", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(nvBus);
        }
        functionBar.add(mainFunction, BorderLayout.WEST);
        
        // Thêm branch selector ở giữa
        JPanel branchPanel = new JPanel(new BorderLayout());
        branchPanel.setBackground(Color.white);
        branchPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        cbxBranch = new JComboBox<>(new String[]{"Tất cả chi nhánh", "Chi nhánh 1", "Chi nhánh 2", "Chi nhánh 3"});
        cbxBranch.setPreferredSize(new Dimension(200, 35));
        branchPanel.add(cbxBranch, BorderLayout.CENTER);
        functionBar.add(branchPanel, BorderLayout.CENTER);
        
        // Event listener: load employees by selected branch
        cbxBranch.addActionListener(e -> {
            if (suppressBranchEvents) {
                return;
            }
            String selectedBranch = (String) cbxBranch.getSelectedItem();
            loadDataTalbe(nvBus.getAllByBranchLabel(selectedBranch));
        });
        
        search = new IntegratedSearch(new String[]{"Tất cả", "Họ tên", "Email"});
        functionBar.add(search, BorderLayout.EAST);
        
        // 🔥 Reset button: clear search + reload full data
        search.btnReset.addActionListener((java.awt.event.ActionEvent e) -> {
            search.txtSearchForm.setText("");
            search.cbxChoose.setSelectedIndex(0);
            refreshToCurrentServerBranch();
        });
        
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
        String[] header = new String[]{"MNV", "Họ tên", "Giới tính", "Ngày Sinh", "SDT", "Email", "Chức vụ", "Chi nhánh"};

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
        tableNhanVien.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        scrollTableSanPham.setViewportView(tableNhanVien);
        main.add(scrollTableSanPham);
    }

    

    public NhanVien(Main m) {
        this.m = m;
        initComponent();
        tableNhanVien.setDefaultEditor(Object.class, null);
        initDefaultBranchLoad(null);
    }

    public NhanVien(Main m, DTO.NhanVienDTO currentUser) {
        this.m = m;
        initComponent();
        tableNhanVien.setDefaultEditor(Object.class, null);
        initDefaultBranchLoad(currentUser);
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
                listcv.get(nhanVien.getMCV()-1).getTENCV(), nhanVien.getMCN()
            });
        }
    }

    private void initDefaultBranchLoad(DTO.NhanVienDTO currentUser) {
        String defaultBranchLabel = currentUser != null && currentUser.getMCN() != null
                ? branchLabelForMcn(currentUser.getMCN())
                : nvBus.getCurrentBranchLabel();

        suppressBranchEvents = true;
        cbxBranch.setSelectedItem(defaultBranchLabel);
        suppressBranchEvents = false;

        loadDataTalbe(nvBus.getAllByBranchLabel(defaultBranchLabel));
    }

    public void refreshToCurrentServerBranch() {
        String currentBranchLabel = nvBus.getCurrentBranchLabel();
        suppressBranchEvents = true;
        cbxBranch.setSelectedItem(currentBranchLabel);
        suppressBranchEvents = false;
        loadDataTalbe(nvBus.getAllByBranchLabel(currentBranchLabel));
    }

    private String branchLabelForMcn(String mcn) {
        if ("CN1".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 1";
        }
        if ("CN2".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 2";
        }
        if ("CN3".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 3";
        }
        return "Tất cả chi nhánh";
    }
    
}
