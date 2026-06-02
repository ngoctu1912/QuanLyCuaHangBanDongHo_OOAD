package GUI.Panel.ThongKe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.Component.PanelBorderRadius;

import BUS.ThongKeBUS;
import config.JDBCUtil;

public final class ThongKe extends JPanel {

    JTabbedPane tabbedPane;
    PanelBorderRadius functionBar;
    JComboBox<String> cbxChiNhanh;
    JButton btnRefresh;
    ThongKeNhanVienBanChay nhanVienBanChay;
    ThongKeDoanhThu doanhThu; 
    ThongKeTopSanPham topSanPham;
    ThongKeNhanVienTotNhat nhanVienTotNhat;
    Color BackgroundColor = new Color(248, 249, 250);
    ThongKeBUS thongkeBUS = new ThongKeBUS();
    
    // Track lazy loading state
    private boolean doanhThuLoaded = false;
    private boolean topSanPhamLoaded = false;
    private boolean nhanVienTotNhatLoaded = false;

    public ThongKe() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(BackgroundColor);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 70));
        functionBar.setLayout(new BorderLayout(10, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblChiNhanh = new JLabel("Chi nhánh");
        lblChiNhanh.setBorder(new EmptyBorder(0, 0, 0, 8));
        functionBar.add(lblChiNhanh, BorderLayout.WEST);

        JPanel branchPanel = new JPanel(new BorderLayout());
        branchPanel.setBackground(Color.white);
        branchPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        cbxChiNhanh = new JComboBox<>(new String[]{"Tất cả chi nhánh", "Chi nhánh 1", "Chi nhánh 2", "Chi nhánh 3"});
        cbxChiNhanh.setPreferredSize(new Dimension(220, 35));
        branchPanel.add(cbxChiNhanh, BorderLayout.CENTER);
        functionBar.add(branchPanel, BorderLayout.CENTER);

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setPreferredSize(new Dimension(110, 35));
        btnRefresh.setMinimumSize(new Dimension(110, 35));
        btnRefresh.setMaximumSize(new Dimension(110, 35));
        btnRefresh.setMargin(new Insets(0, 12, 0, 12));
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRefreshClicked();
            }
        });

        JPanel refreshPanel = new JPanel(new BorderLayout());
        refreshPanel.setBackground(functionBar.getBackground());
        refreshPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        refreshPanel.add(btnRefresh, BorderLayout.CENTER);
        functionBar.add(refreshPanel, BorderLayout.EAST);

        this.add(functionBar, BorderLayout.NORTH);

        // Chỉ load tab đầu tiên (Nhân viên bán chạy)
        nhanVienBanChay = new ThongKeNhanVienBanChay(thongkeBUS);
        // Các tab khác để null, sẽ được load khi click vào
        doanhThu = null;
        topSanPham = null;
        nhanVienTotNhat = null;

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Nhân viên bán chạy", nhanVienBanChay);
        tabbedPane.addTab("Doanh thu", new JPanel()); // Placeholder
        tabbedPane.addTab("Top sản phẩm", new JPanel()); // Placeholder

        // Thêm listener để lazy load các tab khi click
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onTabChanged();
            }
        });

        this.add(tabbedPane, BorderLayout.CENTER);

        String currentBranchLabel = resolveCurrentBranchLabel();
        cbxChiNhanh.setSelectedItem(currentBranchLabel);
        cbxChiNhanh.addActionListener(e -> onBranchChanged());
        onBranchChanged();
    }

    private void onTabChanged() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String currentBranch = (String) cbxChiNhanh.getSelectedItem();

        switch (selectedIndex) {
            case 1: // Doanh thu
                if (!doanhThuLoaded) {
                    doanhThu = new ThongKeDoanhThu(thongkeBUS);
                    tabbedPane.setComponentAt(1, doanhThu);
                    doanhThu.refreshData(currentBranch);
                    doanhThuLoaded = true;
                }
                break;
            case 2: // Top sản phẩm
                if (!topSanPhamLoaded) {
                    topSanPham = new ThongKeTopSanPham(thongkeBUS);
                    tabbedPane.setComponentAt(2, topSanPham);
                    topSanPham.refreshData(currentBranch);
                    topSanPhamLoaded = true;
                }
                break;
        }
    }

    private void onBranchChanged() {
        String selectedBranch = (String) cbxChiNhanh.getSelectedItem();
        refreshAllData(selectedBranch);
    }

    private void onRefreshClicked() {
        // Tự động set lại chi nhánh mà user đang login
        String currentBranchLabel = resolveCurrentBranchLabel();
        cbxChiNhanh.setSelectedItem(currentBranchLabel);
        // Luôn refresh để chắc chắn lấy dữ liệu mới từ database
        refreshAllData(currentBranchLabel);
    }

    private void refreshAllData(String branch) {
        // Luôn refresh tab đầu tiên
        nhanVienBanChay.refreshData(branch);
        
        // Chỉ refresh những tab đã được load
        if (doanhThuLoaded && doanhThu != null) {
            doanhThu.refreshData(branch);
        }
        if (topSanPhamLoaded && topSanPham != null) {
            topSanPham.refreshData(branch);
        }
        if (nhanVienTotNhatLoaded && nhanVienTotNhat != null) {
            nhanVienTotNhat.refreshData(branch);
        }
    }

    private String resolveCurrentBranchLabel() {
        String currentMcn = JDBCUtil.getCurrentMcn();
        if (currentMcn == null || currentMcn.isBlank()) {
            return "Tất cả chi nhánh";
        }
        switch (currentMcn.trim().toUpperCase()) {
            case "CN1":
                return "Chi nhánh 1";
            case "CN2":
                return "Chi nhánh 2";
            case "CN3":
                return "Chi nhánh 3";
            default:
                return "Tất cả chi nhánh";
        }
    }
}
