package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import BUS.KhachHangBUS;
import BUS.PhieuBaoHanhBUS;
import BUS.SanPhamBUS;
import DTO.KhachHangDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputDate;
import GUI.Component.SelectForm;
import GUI.Panel.BaoHanh;

public class PhieuBaoHanhDialog extends JDialog implements ActionListener {

    private BaoHanh jpBH;
    private PhieuBaoHanhDTO phieuBaoHanh;
    
    private PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();
    private KhachHangBUS khBUS = new KhachHangBUS();
    
    private HeaderTitle titlePage;
    private JPanel pnlMain, pnlTop, pnlCenter, pnlBottom;
    private JScrollPane scrollPane;
    
    // Form fields
    private InputDate dateNgayBatDau, dateNgayKetThuc;
    private SelectForm cmbTrangThai;
    
    // Buttons
    private ButtonCustom btnSave, btnCancel;

    public PhieuBaoHanhDialog(BaoHanh jpBH, JFrame owner, String title, boolean modal, PhieuBaoHanhDTO pbh) {
        super(owner, title, modal);
        this.jpBH = jpBH;
        this.phieuBaoHanh = pbh;
        initComponents();
    }

    private void initComponents() {
        this.setSize(new Dimension(650, 550));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);

        // Title
        titlePage = new HeaderTitle("CẬP NHẬT PHIẾU BẢO HÀNH");
        this.add(titlePage, BorderLayout.NORTH);

        // Main content panel (will be scrollable)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(Color.white);
        
        // Main panel
        pnlMain = new JPanel(new BorderLayout(0, 15));
        pnlMain.setBackground(Color.white);
        pnlMain.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Top panel - Thông tin cố định
        pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.white);
        pnlTop.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                "  Thông tin phiếu bảo hành  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13),
                new Color(70, 70, 70)
            ),
            new EmptyBorder(8, 10, 8, 10)
        ));
        
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 8));
        infoPanel.setBackground(Color.white);
        infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        SanPhamDTO sp = spBUS.getByMaSP(phieuBaoHanh.getMSP());
        KhachHangDTO kh = khBUS.getKhachHangById(phieuBaoHanh.getMKH());
        
        addInfoRow(infoPanel, "Mã phiếu BH:", String.valueOf(phieuBaoHanh.getMPB()));
        addInfoRow(infoPanel, "Mã hóa đơn:", String.valueOf(phieuBaoHanh.getMPX()));
        addInfoRow(infoPanel, "Mã sản phẩm:", String.valueOf(phieuBaoHanh.getMSP()));
        addInfoRow(infoPanel, "Tên sản phẩm:", sp != null ? sp.getTEN() : "N/A");
        addInfoRow(infoPanel, "Khách hàng:", kh != null ? kh.getHOTEN() : "N/A");
        
        pnlTop.add(infoPanel, BorderLayout.CENTER);
        pnlMain.add(pnlTop, BorderLayout.NORTH);

        // Center panel - Form chỉnh sửa
        pnlCenter = new JPanel(new GridLayout(3, 1, 0, 10));
        pnlCenter.setBackground(Color.white);
        pnlCenter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                "  Thông tin có thể chỉnh sửa  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13),
                new Color(70, 70, 70)
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Dates
        dateNgayBatDau = new InputDate("Ngày bắt đầu bảo hành");
        dateNgayKetThuc = new InputDate("Ngày kết thúc bảo hành");
        
        // Trạng thái
        String[] arrTrangThai = {"Hết hạn", "Còn hạn", "Đã hủy"};
        cmbTrangThai = new SelectForm("Trạng thái", arrTrangThai);
        
        pnlCenter.add(dateNgayBatDau);
        pnlCenter.add(dateNgayKetThuc);
        pnlCenter.add(cmbTrangThai);

        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        
        contentPanel.add(pnlMain, BorderLayout.CENTER);
        
        // Scroll pane for main content
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        this.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel - Buttons (fixed at bottom, not scrollable)
        pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlBottom.setBackground(Color.white);
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        
        btnSave = new ButtonCustom("Lưu thông tin", "success", 14);
        btnSave.setPreferredSize(new Dimension(140, 38));
        
        btnCancel = new ButtonCustom("Hủy bỏ", "danger", 14);
        btnCancel.setPreferredSize(new Dimension(140, 38));
        
        btnSave.addActionListener(this);
        btnCancel.addActionListener(this);
        
        pnlBottom.add(btnSave);
        pnlBottom.add(btnCancel);

        this.add(pnlBottom, BorderLayout.SOUTH);
        
        // Fill form data BEFORE showing
        fillFormData();
        
        this.setVisible(true);
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblTitle.setForeground(new Color(70, 70, 70));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        lblValue.setForeground(new Color(40, 40, 40));
        
        panel.add(lblTitle);
        panel.add(lblValue);
    }

    private void fillFormData() {
        // Load dates - Convert sql.Date to util.Date for JDateChooser
        if (phieuBaoHanh.getNGAYBATDAU() != null) {
            dateNgayBatDau.setDate(new java.util.Date(phieuBaoHanh.getNGAYBATDAU().getTime()));
        }
        if (phieuBaoHanh.getNGAYKETTHUC() != null) {
            dateNgayKetThuc.setDate(new java.util.Date(phieuBaoHanh.getNGAYKETTHUC().getTime()));
        }
        
        // Load trạng thái
        cmbTrangThai.setSelectedIndex(phieuBaoHanh.getTRANGTHAI());
    }

    private boolean validateForm() {
        try {
            if (dateNgayBatDau.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                dateNgayBatDau.requestFocus();
                return false;
            }
            
            if (dateNgayKetThuc.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                dateNgayKetThuc.requestFocus();
                return false;
            }
            
            // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
            if (dateNgayKetThuc.getDate().before(dateNgayBatDau.getDate())) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                dateNgayKetThuc.requestFocus();
                return false;
            }
            
            // Kiểm tra trạng thái với thời gian bảo hành
            Date currentDate = new Date(System.currentTimeMillis());
            Date ngayKetThuc = new Date(dateNgayKetThuc.getDate().getTime());
            int trangThai = cmbTrangThai.getSelectedIndex();
            
            // Nếu chọn trạng thái "Hết hạn" (0) nhưng ngày kết thúc vẫn còn hạn
            if (trangThai == 0 && ngayKetThuc.after(currentDate)) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể đặt trạng thái 'Hết hạn' khi thời gian bảo hành vẫn còn hiệu lực!\n" +
                    "Ngày kết thúc: " + ngayKetThuc + "\n" +
                    "Ngày hiện tại: " + currentDate, 
                    "Lỗi trạng thái", 
                    JOptionPane.ERROR_MESSAGE);
                cmbTrangThai.requestFocus();
                return false;
            }
            
            // Nếu chọn trạng thái "Còn hạn" (1) nhưng đã hết hạn
            if (trangThai == 1 && (ngayKetThuc.before(currentDate) || ngayKetThuc.equals(currentDate))) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể đặt trạng thái 'Còn hạn' khi thời gian bảo hành đã hết!\n" +
                    "Ngày kết thúc: " + ngayKetThuc + "\n" +
                    "Ngày hiện tại: " + currentDate, 
                    "Lỗi trạng thái", 
                    JOptionPane.ERROR_MESSAGE);
                cmbTrangThai.requestFocus();
                return false;
            }
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void saveData() {
        if (!validateForm()) return;
        
        try {
            Date ngayBatDau = new Date(dateNgayBatDau.getDate().getTime());
            Date ngayKetThuc = new Date(dateNgayKetThuc.getDate().getTime());
            int trangThai = cmbTrangThai.getSelectedIndex();
            
            // Cập nhật thông tin
            phieuBaoHanh.setNGAYBATDAU(ngayBatDau);
            phieuBaoHanh.setNGAYKETTHUC(ngayKetThuc);
            phieuBaoHanh.setTRANGTHAI(trangThai);
            
            if (pbhBUS.update(phieuBaoHanh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu bảo hành thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                jpBH.loadDataTable(pbhBUS.getAll());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu bảo hành thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            saveData();
        } else if (e.getSource() == btnCancel) {
            this.dispose();
        }
    }
}
