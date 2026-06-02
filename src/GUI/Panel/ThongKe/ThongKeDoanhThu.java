package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import GUI.Component.PanelBorderRadius;
import GUI.Component.Chart.BarChart.Chart;
import GUI.Component.Chart.BarChart.ModelChart;
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

public class ThongKeDoanhThu extends JPanel {

    private ThongKeBUS thongkeBUS;
    private String selectedBranch = "Tất cả chi nhánh";

    private JDateChooser dateFrom, dateTo;
    private JButton btnThongKe, btnReset, btnExport;

    private Chart chart;
    private PanelBorderRadius pnlChart;
    private JTable tableThongKe;
    private DefaultTableModel tblModel;
    private JScrollPane scrollTable;

    public ThongKeDoanhThu(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
        loadDefault(); // load 7 ngày gần nhất mặc định
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ── TOOLBAR ──────────────────────────────────────
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTop.setBackground(Color.white);

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

        btnThongKe = new JButton("Thống kê");
        btnReset   = new JButton("Làm mới");
        btnExport  = new JButton("Xuất Excel");
        pnlTop.add(btnThongKe);
        pnlTop.add(btnReset);
        pnlTop.add(btnExport);

        this.add(pnlTop, BorderLayout.NORTH);

        // ── CHART ────────────────────────────────────────
        pnlChart = new PanelBorderRadius();
        pnlChart.setLayout(new BoxLayout(pnlChart, BoxLayout.Y_AXIS));
        pnlChart.setPreferredSize(new Dimension(0, 280));
        chart = buildChart();
        pnlChart.add(chart);
        this.add(pnlChart, BorderLayout.CENTER);

        // ── TABLE ────────────────────────────────────────
        tblModel = new DefaultTableModel(
            new String[]{"Ngày", "Chi phí", "Doanh thu", "Lợi nhuận"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableThongKe = new JTable(tblModel);
        tableThongKe.setRowHeight(28);
        tableThongKe.setAutoCreateRowSorter(true);
        tableThongKe.setFocusable(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++)
            tableThongKe.getColumnModel().getColumn(i).setCellRenderer(center);

        scrollTable = new JScrollPane(tableThongKe);
        scrollTable.setPreferredSize(new Dimension(0, 280));
        this.add(scrollTable, BorderLayout.SOUTH);

        // ── EVENTS ───────────────────────────────────────
        btnThongKe.addActionListener(e -> onThongKe());
        btnReset.addActionListener(e -> onReset());
        btnExport.addActionListener(e -> {
            try { JTableExporter.exportJTableToExcel(tableThongKe); }
            catch (IOException ex) { ex.printStackTrace(); }
        });
    }

    // Được gọi từ ThongKe.java khi đổi chi nhánh
    public void refreshData(String selectedBranch) {
        this.selectedBranch = selectedBranch;
        onThongKe();
    }

    private void onThongKe() {
    Date from = dateFrom.getDate();
    Date to   = dateTo.getDate();

    if (from == null || to == null) {
        loadDefault();
        return;
    }
    if (from.after(to)) {
        JOptionPane.showMessageDialog(this,
            "Ngày bắt đầu không được lớn hơn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<ThongKeTungNgayTrongThangDTO> list =
        thongkeBUS.getThongKeTuNgayDenNgay(
            sdf.format(from), sdf.format(to), selectedBranch); // ✅ thêm selectedBranch
    renderData(list);
}

private void loadDefault() {
    Calendar cal = Calendar.getInstance();
    dateTo.setDate(cal.getTime());
    cal.add(Calendar.DAY_OF_MONTH, -6);
    dateFrom.setDate(cal.getTime());

    ArrayList<ThongKeTungNgayTrongThangDTO> list =
        thongkeBUS.getThongKe7NgayGanNhat(selectedBranch); // ✅ thêm selectedBranch
    renderData(list);
}

    private void onReset() {
        dateFrom.setDate(null);
        dateTo.setDate(null);
        loadDefault();
    }

    // Mặc định load 7 ngày gần nhất
    private void loadDefault(String selectedBranch) {
        Calendar cal = Calendar.getInstance();
        dateTo.setDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -6);
        dateFrom.setDate(cal.getTime());

        ArrayList<ThongKeTungNgayTrongThangDTO> list =
            thongkeBUS.getThongKe7NgayGanNhat(selectedBranch);
        renderData(list);
    }

    private void renderData(ArrayList<ThongKeTungNgayTrongThangDTO> list) {
        // Cập nhật chart
        pnlChart.remove(chart);
        chart = buildChart();
        for (ThongKeTungNgayTrongThangDTO item : list) {
            chart.addData(new ModelChart(
                item.getNgay().toString(),
                new double[]{item.getChiphi(), item.getDoanhthu(), item.getLoinhuan()}
            ));
        }
        pnlChart.add(chart);
        pnlChart.revalidate();
        pnlChart.repaint();

        // Cập nhật bảng
        tblModel.setRowCount(0);
        for (ThongKeTungNgayTrongThangDTO item : list) {
            tblModel.addRow(new Object[]{
                item.getNgay(),
                Formater.FormatVND(item.getChiphi()),
                Formater.FormatVND(item.getDoanhthu()),
                Formater.FormatVND(item.getLoinhuan())
            });
        }
    }

    private Chart buildChart() {
        Chart c = new Chart();
        c.addLegend("Chi phí",    new Color(203, 213, 225));
        c.addLegend("Doanh thu",  new Color(148, 163, 184));
        c.addLegend("Lợi nhuận", new Color(100, 116, 139));
        return c;
    }
}