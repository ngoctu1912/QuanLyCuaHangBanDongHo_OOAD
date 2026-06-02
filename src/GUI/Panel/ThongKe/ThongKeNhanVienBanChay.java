package GUI.Panel.ThongKe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeNhanVienBanChayDTO;

public class ThongKeNhanVienBanChay extends JPanel {

    private JTable tableNhanVien;
    private DefaultTableModel model;
    private ThongKeBUS thongkeBUS;
    private String selectedBranch = "Tất cả chi nhánh";

    public ThongKeNhanVienBanChay(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(new Color(248, 249, 250));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Nhân viên bán chạy theo chi nhánh");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(lblTitle, BorderLayout.NORTH);

        // Tạo bảng
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã NV", "Tên nhân viên", "Chi nhánh", "Số lượng đơn"});
        
        tableNhanVien = new JTable(model);
        tableNhanVien.setRowHeight(25);
        tableNhanVien.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        this.add(scrollPane, BorderLayout.CENTER);

        // Load dữ liệu mặc định
        loadData();
    }

    public void loadData() {
        model.setRowCount(0);
        try {
            java.util.List<ThongKeNhanVienBanChayDTO> data = thongkeBUS.getNhanVienBanChay(selectedBranch);
            for (ThongKeNhanVienBanChayDTO item : data) {
                model.addRow(new Object[]{
                    item.getMaNhanVien(),
                    item.getTenNhanVien(),
                    item.getChiNhanh() != null ? item.getChiNhanh() : selectedBranch,
                    item.getSoLuongDon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshData(String selectedBranch) {
        this.selectedBranch = selectedBranch;
        loadData();
    }
}
