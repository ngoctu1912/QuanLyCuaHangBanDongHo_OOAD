package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import BUS.KhachHangBUS;
import BUS.PhieuBaoHanhBUS;
import BUS.PhieuSuaChuaBUS;
import BUS.SanPhamBUS;
import DTO.KhachHangDTO;
import DTO.NhanVienDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.PhieuSuaChuaDTO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;

public class ChiTietSuaChuaDialog extends JDialog implements ActionListener {
    
    private PhieuSuaChuaDTO psc;
    private PhieuSuaChuaBUS pscBUS = new PhieuSuaChuaBUS();
    private PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();
    private KhachHangBUS khBUS = new KhachHangBUS();
    
    private HeaderTitle titlePage;
    private JPanel pnlMain, pnlInfo, pnlBaoHanh, pnlSuaChua, pnlMoTa, pnlBottom;
    private ButtonCustom btnClose;
    
    public ChiTietSuaChuaDialog(JFrame owner, PhieuSuaChuaDTO psc, boolean modal) {
        super(owner, "Chi tiết phiếu sửa chữa", modal);
        this.psc = psc;
        initComponents();
        loadData();
        this.setVisible(true);
    }
    
    private void initComponents() {
        this.setSize(new Dimension(950, 650));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        
        // Title
        titlePage = new HeaderTitle("CHI TIẾT PHIẾU SỬA CHỮA #" + psc.getMSC());
        titlePage.setBackground(Color.BLACK);
        this.add(titlePage, BorderLayout.NORTH);
        
        // Main panel
        pnlMain = new JPanel(new BorderLayout(0, 15));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));
        contentPanel.setBackground(Color.WHITE);
        
        // Panel thông tin tổng quan
        pnlInfo = createInfoPanel();
        contentPanel.add(pnlInfo, BorderLayout.NORTH);
        
        // Panel trung tâm chứa 2 panel
        JPanel centerContainer = new JPanel(new GridLayout(1, 2, 15, 0));
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setPreferredSize(new Dimension(0, 280));
        
        // Panel thông tin bảo hành
        pnlBaoHanh = createBaoHanhPanel();
        centerContainer.add(pnlBaoHanh);
        
        // Panel thông tin sửa chữa
        pnlSuaChua = createSuaChuaPanel();
        centerContainer.add(pnlSuaChua);
        
        contentPanel.add(centerContainer, BorderLayout.CENTER);
        
        // Panel mô tả
        pnlMoTa = createMoTaPanel();
        contentPanel.add(pnlMoTa, BorderLayout.SOUTH);
        
        pnlMain.add(contentPanel, BorderLayout.CENTER);
        
        // Bottom panel - Button
        pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlBottom.setBackground(Color.WHITE);
        
        btnClose = new ButtonCustom("Đóng", "danger", 14);
        btnClose.setPreferredSize(new Dimension(150, 40));
        btnClose.addActionListener(this);
        pnlBottom.add(btnClose);
        
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        JScrollPane scrollPane = new JScrollPane(pnlMain);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 15, 10));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
            new EmptyBorder(15, 20, 15, 20)
        ));
        panel.setPreferredSize(new Dimension(0, 120));
        
        return panel;
    }
    
    private JPanel createBaoHanhPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129), 2),
                "  Thông tin bảo hành  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(5, 150, 105)
            ),
            new EmptyBorder(10, 15, 15, 15)
        ));
        
        return panel;
    }
    
    private JPanel createSuaChuaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(249, 115, 22), 2),
                "  Chi tiết sửa chữa  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(234, 88, 12)
            ),
            new EmptyBorder(10, 15, 15, 15)
        ));
        
        return panel;
    }
    
    private JPanel createMoTaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(139, 92, 246), 2),
                "  Mô tả chi tiết  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(124, 58, 237)
            ),
            new EmptyBorder(10, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(0, 200));
        
        return panel;
    }
    
    private void loadData() {
        // Load thông tin tổng quan
        loadInfoData();
        
        // Load thông tin bảo hành
        loadBaoHanhData();
        
        // Load thông tin sửa chữa
        loadSuaChuaData();
        
        // Load mô tả
        loadMoTaData();
    }
    
    private void loadInfoData() {
        pnlInfo.removeAll();
        
        // Mã sửa chữa
        addInfoField(pnlInfo, "Mã sửa chữa:", String.valueOf(psc.getMSC()), 
                    new Color(59, 130, 246), true);
        
        // Mã phiếu bảo hành
        addInfoField(pnlInfo, "Mã phiếu BH:", String.valueOf(psc.getMPB()), 
                    new Color(16, 185, 129), false);
        
        // Ngày nhận
        addInfoField(pnlInfo, "Ngày nhận:", psc.getNGAYNHAN().toString(), 
                    new Color(251, 146, 60), false);
        
        // Ngày trả
        String ngayTra = psc.getNGAYTRA() != null ? psc.getNGAYTRA().toString() : "Chưa trả";
        addInfoField(pnlInfo, "Ngày trả:", ngayTra, 
                    new Color(139, 92, 246), false);
        
        // Tình trạng
        String tinhTrang = psc.getTenTinhTrang();
        Color colorTT = getTinhTrangColor(psc.getTINHTRANG());
        addInfoField(pnlInfo, "Tình trạng:", tinhTrang, colorTT, true);
        
        // Chi phí
        String chiPhi = psc.getCHIPHI() != null ? 
                       String.format("%,d VNĐ", psc.getCHIPHI().longValue()) : "0 VNĐ";
        addInfoField(pnlInfo, "Chi phí:", chiPhi, 
                    new Color(239, 68, 68), true);
        
        // Nhân viên
        NhanVienDTO nv = pscBUS.getNhanVienByPSC(psc);
        String tenNV = nv != null ? nv.getHOTEN() : "Chưa phân công";
        addInfoField(pnlInfo, "Nhân viên:", tenNV, 
                    new Color(99, 102, 241), false);
        
        // Số điện thoại NV
        String sdtNV = nv != null ? nv.getSDT() : "N/A";
        addInfoField(pnlInfo, "SĐT NV:", sdtNV, 
                    new Color(107, 114, 128), false);
    }
    
    private void loadBaoHanhData() {
        PhieuBaoHanhDTO pbh = pscBUS.getPhieuBaoHanhByPSC(psc);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(7, 1, 0, 8));
        contentPanel.setBackground(Color.WHITE);
        
        if (pbh != null) {
            SanPhamDTO sp = spBUS.getByMaSP(pbh.getMSP());
            KhachHangDTO kh = khBUS.getKhachHangById(pbh.getMKH());
            
            addDetailField(contentPanel, "Mã hóa đơn:", String.valueOf(pbh.getMPX()));
            addDetailField(contentPanel, "Sản phẩm:", sp != null ? sp.getTEN() : "N/A");
            addDetailField(contentPanel, "Khách hàng:", kh != null ? kh.getHOTEN() : "N/A");
            addDetailField(contentPanel, "SĐT:", kh != null ? kh.getSDT() : "N/A");
            addDetailField(contentPanel, "Địa chỉ:", kh != null ? kh.getDIACHI() : "N/A");
            addDetailField(contentPanel, "Bắt đầu BH:", pbh.getNGAYBATDAU().toString());
            addDetailField(contentPanel, "Kết thúc BH:", pbh.getNGAYKETTHUC().toString());
        } else {
            JLabel lblNoData = new JLabel("Không có thông tin bảo hành", JLabel.CENTER);
            lblNoData.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblNoData.setForeground(Color.GRAY);
            contentPanel.add(lblNoData);
        }
        
        pnlBaoHanh.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void loadSuaChuaData() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(7, 1, 0, 8));
        contentPanel.setBackground(Color.WHITE);
        
        NhanVienDTO nv = pscBUS.getNhanVienByPSC(psc);
        
        addDetailField(contentPanel, "Nhân viên sửa:", 
                      nv != null ? nv.getHOTEN() : "Chưa phân công");
        addDetailField(contentPanel, "Chức vụ:", 
                      nv != null ? String.valueOf(nv.getMCV()) : "N/A");
        addDetailField(contentPanel, "Ngày nhận:", psc.getNGAYNHAN().toString());
        addDetailField(contentPanel, "Ngày trả:", 
                      psc.getNGAYTRA() != null ? psc.getNGAYTRA().toString() : "Chưa trả");
        
        // Tính số ngày sửa
        String soNgay = "N/A";
        if (psc.getNGAYTRA() != null) {
            long diff = psc.getNGAYTRA().getTime() - psc.getNGAYNHAN().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            soNgay = days + " ngày";
        }
        addDetailField(contentPanel, "Thời gian sửa:", soNgay);
        
        addDetailField(contentPanel, "Tình trạng:", psc.getTenTinhTrang());
        
        String chiPhi = psc.getCHIPHI() != null ? 
                       String.format("%,d VNĐ", psc.getCHIPHI().longValue()) : "0 VNĐ";
        addDetailField(contentPanel, "Chi phí:", chiPhi);
        
        pnlSuaChua.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void loadMoTaData() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        contentPanel.setBackground(Color.WHITE);
        
        // Nguyên nhân
        JPanel pnlNguyenNhan = new JPanel(new BorderLayout(0, 5));
        pnlNguyenNhan.setBackground(Color.WHITE);
        
        JLabel lblNguyenNhan = new JLabel("Nguyên nhân:");
        lblNguyenNhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNguyenNhan.setForeground(new Color(55, 65, 81));
        pnlNguyenNhan.add(lblNguyenNhan, BorderLayout.NORTH);
        
        JTextArea txtNguyenNhan = new JTextArea(
            psc.getNGUYENNHAN() != null && !psc.getNGUYENNHAN().isEmpty() 
            ? psc.getNGUYENNHAN() 
            : "Không có thông tin"
        );
        txtNguyenNhan.setEditable(false);
        txtNguyenNhan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNguyenNhan.setLineWrap(true);
        txtNguyenNhan.setWrapStyleWord(true);
        txtNguyenNhan.setBackground(new Color(249, 250, 251));
        txtNguyenNhan.setBorder(new EmptyBorder(8, 10, 8, 10));
        
        JScrollPane scrollNguyenNhan = new JScrollPane(txtNguyenNhan);
        scrollNguyenNhan.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        scrollNguyenNhan.setPreferredSize(new Dimension(0, 60));
        pnlNguyenNhan.add(scrollNguyenNhan, BorderLayout.CENTER);
        
        contentPanel.add(pnlNguyenNhan);
        
        // Ghi chú
        JPanel pnlGhiChu = new JPanel(new BorderLayout(0, 5));
        pnlGhiChu.setBackground(Color.WHITE);
        
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblGhiChu.setForeground(new Color(55, 65, 81));
        pnlGhiChu.add(lblGhiChu, BorderLayout.NORTH);
        
        JTextArea txtGhiChu = new JTextArea(
            psc.getGHICHU() != null && !psc.getGHICHU().isEmpty() 
            ? psc.getGHICHU() 
            : "Không có ghi chú"
        );
        txtGhiChu.setEditable(false);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setBackground(new Color(249, 250, 251));
        txtGhiChu.setBorder(new EmptyBorder(8, 10, 8, 10));
        
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        scrollGhiChu.setPreferredSize(new Dimension(0, 60));
        pnlGhiChu.add(scrollGhiChu, BorderLayout.CENTER);
        
        contentPanel.add(pnlGhiChu);
        
        pnlMoTa.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void addInfoField(JPanel panel, String label, String value, Color valueColor, boolean bold) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fieldPanel.setBackground(new Color(248, 250, 252));
        
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(75, 85, 99));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        lblValue.setForeground(valueColor);
        
        fieldPanel.add(lblTitle);
        fieldPanel.add(lblValue);
        
        panel.add(fieldPanel);
    }
    
    private void addDetailField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(55, 65, 81));
        lblTitle.setPreferredSize(new Dimension(120, 0));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValue.setForeground(new Color(17, 24, 39));
        
        fieldPanel.add(lblTitle, BorderLayout.WEST);
        fieldPanel.add(lblValue, BorderLayout.CENTER);
        
        panel.add(fieldPanel);
    }
    
    private Color getTinhTrangColor(int tinhTrang) {
        return switch (tinhTrang) {
            case 0 -> new Color(251, 146, 60);   // Chờ xử lý - Orange
            case 1 -> new Color(59, 130, 246);   // Đang sửa - Blue
            case 2 -> new Color(34, 197, 94);    // Hoàn thành - Green
            case 3 -> new Color(239, 68, 68);    // Không sửa được - Red
            case 4 -> new Color(107, 114, 128);  // Đã hủy - Gray
            default -> new Color(107, 114, 128); // Unknown - Gray
        };
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClose) {
            this.dispose();
        }
    }
}
