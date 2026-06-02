package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTopSanPhamDTO;
import com.toedter.calendar.JDateChooser;
import helper.Formater;
import helper.JTableExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongKeTopSanPham extends JPanel {

    private JTable tableTopSanPham;
    private DefaultTableModel model;
    private ThongKeBUS thongkeBUS;
    private String selectedBranch = "Tất cả chi nhánh";

    private JDateChooser dateFrom, dateTo;
    private JComboBox<String> cbxTopN;
    private JButton btnThongKe, btnReset, btnExport;

    public ThongKeTopSanPham(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
        loadDefault();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(new Color(248, 249, 250));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ── TOOLBAR ──────────────────────────────────────
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTop.setBackground(new Color(248, 249, 250));

        pnlTop.add(new JLabel("Từ ngày:"));
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setPreferredSize(new Dimension(130, 30));
        pnlTop.add(dateFrom);

        pnlTop.add(new JLabel("Đến ngày:"));
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setPreferredSize(new Dimension(130, 30));
        pnlTop.add(dateTo);

        pnlTop.add(new JLabel("Top:"));
        cbxTopN = new JComboBox<>(new String[]{"Top 5", "Top 10", "Top 20", "Tất cả"});
        cbxTopN.setPreferredSize(new Dimension(90, 30));
        pnlTop.add(cbxTopN);

        btnThongKe = new JButton("Thống kê");
        btnReset   = new JButton("Làm mới");
        btnExport  = new JButton("Xuất Excel");
        pnlTop.add(btnThongKe);
        pnlTop.add(btnReset);
        pnlTop.add(btnExport);

        this.add(pnlTop, BorderLayout.NORTH);

        // ── TABLE ────────────────────────────────────────
        model = new DefaultTableModel(
            new String[]{"#", "Mã SP", "Tên sản phẩm", "Số lượng bán", "Doanh thu", "Tỉ lệ %"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableTopSanPham = new JTable(model);
        tableTopSanPham.setRowHeight(28);
        tableTopSanPham.setFont(new Font("Arial", Font.PLAIN, 13));
        tableTopSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableTopSanPham.setFocusable(false);
        tableTopSanPham.setAutoCreateRowSorter(true);

        // Căn giữa tất cả cột trừ cột tên
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 6; i++)
            tableTopSanPham.getColumnModel().getColumn(i).setCellRenderer(center);

        // Độ rộng cột
        tableTopSanPham.getColumnModel().getColumn(0).setPreferredWidth(40);  // #
        tableTopSanPham.getColumnModel().getColumn(1).setPreferredWidth(70);  // Mã SP
        tableTopSanPham.getColumnModel().getColumn(2).setPreferredWidth(300); // Tên
        tableTopSanPham.getColumnModel().getColumn(3).setPreferredWidth(120); // SL
        tableTopSanPham.getColumnModel().getColumn(4).setPreferredWidth(150); // Doanh thu
        tableTopSanPham.getColumnModel().getColumn(5).setPreferredWidth(80);  // Tỉ lệ

        JScrollPane scrollPane = new JScrollPane(tableTopSanPham);
        this.add(scrollPane, BorderLayout.CENTER);

        // ── EVENTS ───────────────────────────────────────
        btnThongKe.addActionListener(e -> loadData());
        btnReset.addActionListener(e -> {
            dateFrom.setDate(null);
            dateTo.setDate(null);
            cbxTopN.setSelectedIndex(0);
            loadDefault();
        });
        btnExport.addActionListener(e -> {
            try { JTableExporter.exportJTableToExcel(tableTopSanPham); }
            catch (IOException ex) { ex.printStackTrace(); }
        });
    }

    private int getTopN() {
        String selected = (String) cbxTopN.getSelectedItem();
        switch (selected) {
            case "Top 5":  return 5;
            case "Top 10": return 10;
            case "Top 20": return 20;
            default:       return Integer.MAX_VALUE; // Tất cả
        }
    }

    private void loadDefault() {
        // Mặc định: 30 ngày gần nhất, Top 10
        Calendar cal = Calendar.getInstance();
        dateTo.setDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -30);
        dateFrom.setDate(cal.getTime());
        cbxTopN.setSelectedItem("Top 10");
        loadData();
    }

        public void loadData() {
        // Kiểm tra ngày
        Date from = dateFrom.getDate();
        Date to   = dateTo.getDate();

        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng thời gian!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (from.after(to)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Truyền ngày vào BUS
        ArrayList<ThongKeTopSanPhamDTO> list =
            thongkeBUS.getTopSanPham(selectedBranch, getTopN(), from, to);

        model.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            ThongKeTopSanPhamDTO item = list.get(i);
            model.addRow(new Object[]{
                i + 1,                              // Hạng
                item.getMsp(),                      // Mã SP
                item.getTen(),                      // Tên
                item.getSoLuongBan(),               // Số lượng bán — tiêu chí xếp hạng
                Formater.FormatVND(item.getDoanhThu()), // Doanh thu
                item.getTiLe() + "%"                // Tỉ lệ % trên tổng doanh thu
            });
        }
    }

    public void refreshData(String selectedBranch) {
        this.selectedBranch = selectedBranch;
        loadData();
    }
}