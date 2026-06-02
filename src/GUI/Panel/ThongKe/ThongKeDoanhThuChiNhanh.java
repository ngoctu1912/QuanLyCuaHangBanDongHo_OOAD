package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeDoanhThuChiNhanhDTO;
import helper.Formater;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeDoanhThuChiNhanh extends JPanel {

    private JTable tableDoanhThu;
    private DefaultTableModel model;
    private ThongKeBUS thongkeBUS;
    private String selectedBranch = "Tất cả chi nhánh";

    public ThongKeDoanhThuChiNhanh(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(new Color(248, 249, 250));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Doanh thu theo chi nhánh");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[]{
            "Chi nhánh", "Tổng doanh thu", "Số hoá đơn", "Doanh thu trung bình/HĐ"
        });

        tableDoanhThu = new JTable(model);
        tableDoanhThu.setRowHeight(30);
        tableDoanhThu.setFont(new Font("Arial", Font.PLAIN, 13));
        tableDoanhThu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableDoanhThu.setFocusable(false);

        // Căn giữa tất cả cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            tableDoanhThu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableDoanhThu);
        this.add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    public void loadData() {
        model.setRowCount(0);
        ArrayList<ThongKeDoanhThuChiNhanhDTO> list = thongkeBUS.getDoanhThuTheoChiNhanh(selectedBranch);

        for (ThongKeDoanhThuChiNhanhDTO item : list) {
            // Hiển thị tên đẹp hơn: CN1 → Chi nhánh 1
            String tenHienThi = item.getChiNhanh()
                    .replace("CN1", "Chi nhánh 1")
                    .replace("CN2", "Chi nhánh 2")
                    .replace("CN3", "Chi nhánh 3");

            model.addRow(new Object[]{
                tenHienThi,
                Formater.FormatVND(item.getTongDoanhThu()),
                item.getSoHoaDon(),
                Formater.FormatVND(item.getDoanhThuTrungBinh())
            });
        }
    }

    public void refreshData(String selectedBranch) {
        this.selectedBranch = selectedBranch;
        loadData();
    }
}