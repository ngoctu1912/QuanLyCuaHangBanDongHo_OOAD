package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import BUS.KhachHangBUS;
import BUS.NhanVienBUS;
import BUS.PhieuSuaChuaBUS;
import BUS.SanPhamBUS;
import DTO.KhachHangDTO;
import DTO.NhanVienDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.PhieuSuaChuaDTO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;

public class ChiTietPhieuBaoHanhDialog extends JDialog {

    private PhieuBaoHanhDTO phieuBaoHanh;
    
    private PhieuSuaChuaBUS pscBUS = new PhieuSuaChuaBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();
    private KhachHangBUS khBUS = new KhachHangBUS();
    private NhanVienBUS nvBUS = new NhanVienBUS();
    
    private HeaderTitle titlePage;
    private JPanel pnlMain, pnlInfo, pnlHistory;
    private JTable tableHistory;
    private DefaultTableModel tblModel;
    private JScrollPane scrollTable;
    private ButtonCustom btnClose;

    public ChiTietPhieuBaoHanhDialog(JFrame owner, String title, boolean modal, PhieuBaoHanhDTO pbh) {
        super(owner, title, modal);
        this.phieuBaoHanh = pbh;
        initComponents();
    }

    private void initComponents() {
        this.setSize(new Dimension(900, 650));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);

        // Title
        titlePage = new HeaderTitle("CHI TIẾT PHIẾU BẢO HÀNH #" + phieuBaoHanh.getMPB());
        this.add(titlePage, BorderLayout.NORTH);

        // Main panel with scroll
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(Color.white);
        contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Info Panel - Thông tin phiếu bảo hành
        pnlInfo = new JPanel(new BorderLayout());
        pnlInfo.setBackground(Color.white);
        pnlInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(160, 160, 160), 2),
                "  THÔNG TIN PHIẾU BẢO HÀNH  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(70, 70, 70)
            ),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        contentPanel.add(pnlInfo, BorderLayout.NORTH);

        // History Panel - Lịch sử sửa chữa
        pnlHistory = new JPanel(new BorderLayout(0, 10));
        pnlHistory.setBackground(Color.white);
        pnlHistory.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(160, 160, 160), 2),
                "  LỊCH SỬ SỬA CHỮA & BẢO DƯỠNG  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(70, 70, 70)
            ),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Table
        String[] headers = {"Mã SC", "Nhân viên", "Ngày nhận", "Ngày trả", "Tình trạng", "Chi phí", "Nguyên nhân"};
        tblModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableHistory = new JTable(tblModel);
        tableHistory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableHistory.setRowHeight(30);
        tableHistory.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHistory.getTableHeader().setBackground(new Color(80, 80, 80));
        tableHistory.getTableHeader().setForeground(new Color(240, 240, 240));
        
        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < headers.length; i++) {
            if (i != 6) { // Nguyên nhân căn trái
                tableHistory.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        scrollTable = new JScrollPane(tableHistory);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnlHistory.add(scrollTable, BorderLayout.CENTER);
        
        contentPanel.add(pnlHistory, BorderLayout.CENTER);
        
        // Scroll pane for main content
        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(12);
        this.add(mainScroll, BorderLayout.CENTER);

        // Bottom panel - Button
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlBottom.setBackground(Color.white);
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        
        btnClose = new ButtonCustom("Đóng", "danger", 14);
        btnClose.setPreferredSize(new Dimension(120, 38));
        btnClose.addActionListener(e -> dispose());
        
        pnlBottom.add(btnClose);
        this.add(pnlBottom, BorderLayout.SOUTH);
        
        // Load data before showing
        loadData();
        
        this.setVisible(true);
    }

    private void loadData() {
        // Load thông tin phiếu bảo hành
        SanPhamDTO sp = spBUS.getByMaSP(phieuBaoHanh.getMSP());
        KhachHangDTO kh = khBUS.getKhachHangById(phieuBaoHanh.getMKH());
        
        // Main container with vertical layout
        JPanel infoContainer = new JPanel();
        infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
        infoContainer.setBackground(Color.white);
        infoContainer.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Row 1 - Mã phiếu & Mã hóa đơn
        JPanel row1 = createInfoRow(
            "Mã phiếu BH:", String.valueOf(phieuBaoHanh.getMPB()), true,
            "Mã hóa đơn:", String.valueOf(phieuBaoHanh.getMPX()), false
        );
        infoContainer.add(row1);
        infoContainer.add(createSpacer(8));
        
        // Row 2 - Mã SP & Tên SP
        JPanel row2 = createInfoRow(
            "Mã sản phẩm:", String.valueOf(phieuBaoHanh.getMSP()), false,
            "Tên sản phẩm:", sp != null ? sp.getTEN() : "N/A", false
        );
        infoContainer.add(row2);
        infoContainer.add(createSpacer(8));
        
        // Row 3 - Mã KH & Tên KH
        JPanel row3 = createInfoRow(
            "Mã khách hàng:", String.valueOf(phieuBaoHanh.getMKH()), false,
            "Khách hàng:", kh != null ? kh.getHOTEN() : "N/A", false
        );
        infoContainer.add(row3);
        infoContainer.add(createSpacer(8));
        
        // Row 4 - SĐT & Email
        JPanel row4 = createInfoRow(
            "Số điện thoại:", kh != null ? kh.getSDT() : "N/A", false,
            "Email:", kh != null ? (kh.getEMAIL() != null ? kh.getEMAIL() : "N/A") : "N/A", false
        );
        infoContainer.add(row4);
        infoContainer.add(createSpacer(8));
        
        // Row 5 - Ngày bắt đầu & Ngày kết thúc
        JPanel row5 = createInfoRow(
            "Ngày bắt đầu:", String.valueOf(phieuBaoHanh.getNGAYBATDAU()), false,
            "Ngày kết thúc:", String.valueOf(phieuBaoHanh.getNGAYKETTHUC()), false
        );
        infoContainer.add(row5);
        infoContainer.add(createSpacer(8));
        
        // Row 6 - Trạng thái & Thời hạn
        Date currentDate = new Date(System.currentTimeMillis());
        String trangThai;
        Color statusColor;
        if (phieuBaoHanh.getTRANGTHAI() == 2) {
            trangThai = "Đã hủy";
            statusColor = new Color(150, 150, 150);
        } else if (phieuBaoHanh.getTRANGTHAI() == 0 || phieuBaoHanh.getNGAYKETTHUC().before(currentDate)) {
            trangThai = "Hết hạn";
            statusColor = new Color(180, 50, 50);
        } else {
            trangThai = "Còn hạn";
            statusColor = new Color(60, 120, 60);
        }
        
        long diffInMillies = phieuBaoHanh.getNGAYKETTHUC().getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        String thoiHan;
        if (diffInDays > 0) {
            thoiHan = diffInDays + " ngày";
        } else if (diffInDays == 0) {
            thoiHan = "Hết hạn hôm nay";
        } else {
            thoiHan = "Đã hết " + Math.abs(diffInDays) + " ngày";
        }
        
        JPanel row6 = new JPanel(new GridLayout(1, 2, 20, 0));
        row6.setBackground(Color.white);
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setBackground(Color.white);
        JLabel lblTTTitle = new JLabel("Trạng thái: ");
        lblTTTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTTTitle.setForeground(new Color(70, 70, 70));
        JLabel lblTTValue = new JLabel(trangThai);
        lblTTValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTTValue.setForeground(statusColor);
        statusPanel.add(lblTTTitle);
        statusPanel.add(lblTTValue);
        
        JPanel thoiHanPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        thoiHanPanel.setBackground(Color.white);
        JLabel lblTHTitle = new JLabel("Thời hạn còn lại: ");
        lblTHTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTHTitle.setForeground(new Color(70, 70, 70));
        JLabel lblTHValue = new JLabel(thoiHan);
        lblTHValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTHValue.setForeground(new Color(40, 40, 40));
        thoiHanPanel.add(lblTHTitle);
        thoiHanPanel.add(lblTHValue);
        
        row6.add(statusPanel);
        row6.add(thoiHanPanel);
        infoContainer.add(row6);
        
        // Add to panel and refresh
        pnlInfo.removeAll();
        pnlInfo.add(infoContainer, BorderLayout.CENTER);
        pnlInfo.revalidate();
        pnlInfo.repaint();
        
        // Load lịch sử sửa chữa
        ArrayList<PhieuSuaChuaDTO> listPSC = pscBUS.getByMaPhieuBaoHanh(phieuBaoHanh.getMPB());
        tblModel.setRowCount(0);
        
        pnlHistory.removeAll();
        
        if (listPSC.isEmpty()) {
            // Nếu chưa có lịch sử, hiển thị thông báo đơn giản
            JLabel lblNoData = new JLabel("Chưa có lịch sử sửa chữa & bảo dưỡng", JLabel.CENTER);
            lblNoData.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblNoData.setForeground(new Color(150, 150, 150));
            lblNoData.setBorder(new EmptyBorder(30, 0, 30, 0));
            pnlHistory.add(lblNoData, BorderLayout.CENTER);
        } else {
            // Có lịch sử thì hiển thị bảng
            for (PhieuSuaChuaDTO psc : listPSC) {
                NhanVienDTO nv = psc.getMNV() != null ? nvBUS.getByMaNV(psc.getMNV()) : null;
                Object[] row = {
                    psc.getMSC(),
                    nv != null ? nv.getHOTEN() : "Chưa phân công",
                    psc.getNGAYNHAN(),
                    psc.getNGAYTRA() != null ? psc.getNGAYTRA() : "Chưa trả",
                    psc.getTenTinhTrang(),
                    psc.getCHIPHI() != null ? String.format("%,d VNĐ", psc.getCHIPHI().longValue()) : "0 VNĐ",
                    psc.getNGUYENNHAN() != null ? psc.getNGUYENNHAN() : ""
                };
                tblModel.addRow(row);
            }
            pnlHistory.add(scrollTable, BorderLayout.CENTER);
        }
        
        pnlHistory.revalidate();
        pnlHistory.repaint();
    }
    
    private JPanel createInfoRow(String label1, String value1, boolean highlight1, 
                                   String label2, String value2, boolean highlight2) {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setBackground(Color.white);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        // Left side
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.white);
        
        JLabel lbl1 = new JLabel(label1 + " ");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl1.setForeground(new Color(70, 70, 70));
        
        JLabel val1 = new JLabel(value1);
        val1.setFont(new Font("Segoe UI", highlight1 ? Font.BOLD : Font.PLAIN, highlight1 ? 14 : 13));
        val1.setForeground(highlight1 ? new Color(184, 134, 11) : new Color(40, 40, 40));
        
        leftPanel.add(lbl1);
        leftPanel.add(val1);
        
        // Right side
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rightPanel.setBackground(Color.white);
        
        JLabel lbl2 = new JLabel(label2 + " ");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl2.setForeground(new Color(70, 70, 70));
        
        JLabel val2 = new JLabel(value2);
        val2.setFont(new Font("Segoe UI", highlight2 ? Font.BOLD : Font.PLAIN, highlight2 ? 14 : 13));
        val2.setForeground(highlight2 ? new Color(184, 134, 11) : new Color(40, 40, 40));
        
        rightPanel.add(lbl2);
        rightPanel.add(val2);
        
        row.add(leftPanel);
        row.add(rightPanel);
        
        return row;
    }
    
    private JPanel createSpacer(int height) {
        JPanel spacer = new JPanel();
        spacer.setBackground(Color.white);
        spacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        spacer.setPreferredSize(new Dimension(0, height));
        return spacer;
    }
}
