package GUI.Panel.ThongKe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import BUS.ThongKeBUS;

public class ThongKeNhanVienTotNhat extends JPanel {

    private JTable tableNhanVien;
    private DefaultTableModel model;
    private ThongKeBUS thongkeBUS;
    private String selectedBranch = "Tất cả chi nhánh";

    public ThongKeNhanVienTotNhat(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(new Color(248, 249, 250));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Nhân viên bán tốt nhất theo chi nhánh");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(lblTitle, BorderLayout.NORTH);

        // Tạo bảng
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã NV", "Tên nhân viên", "Chi nhánh", "Doanh thu", "Số hoá đơn", "Doanh thu TB"});
        
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
        // TODO: Gọi BUS để lấy dữ liệu nhân viên bán tốt nhất theo chi nhánh đã chọn
        // Tạm thời để trống
    }

    public void refreshData(String selectedBranch) {
        this.selectedBranch = selectedBranch;
        loadData();
    }
}
