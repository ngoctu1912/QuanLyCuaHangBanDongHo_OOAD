package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import BUS.KhachHangBUS;
import BUS.NhanVienBUS;
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
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.SelectForm;
import GUI.Panel.SuaChua;

public class SuaChuaDialog extends JDialog implements ActionListener {

    private SuaChua jpSC;
    private PhieuSuaChuaDTO phieuSuaChua;
    private String mode;
    
    private PhieuSuaChuaBUS pscBUS = new PhieuSuaChuaBUS();
    private PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
    private NhanVienBUS nvBUS = new NhanVienBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();
    private KhachHangBUS khBUS = new KhachHangBUS();
    
    private HeaderTitle titlePage;
    private JPanel pnlMain, pnlTop, pnlCenter, pnlBottom;
    
    // Form fields
    private SelectForm cmbNhanVien, cmbTinhTrang;
    private InputDate dateNgayNhan, dateNgayTra;
    private InputForm txtNguyenNhan, txtChiPhi, txtGhiChu;
    
    // Buttons
    private ButtonCustom btnSave, btnCancel;
    
    private PhieuBaoHanhDTO selectedPBH = null;

    // Constructor cho create mode với PhieuBaoHanhDTO
    public SuaChuaDialog(SuaChua jpSC, JFrame owner, String title, boolean modal, String mode, PhieuBaoHanhDTO pbh) {
        super(owner, title, modal);
        this.jpSC = jpSC;
        this.mode = mode;
        this.selectedPBH = pbh;
        initComponents();
    }

    // Constructor cho update mode với PhieuSuaChuaDTO
    public SuaChuaDialog(SuaChua jpSC, JFrame owner, String title, boolean modal, String mode, PhieuSuaChuaDTO psc) {
        super(owner, title, modal);
        this.jpSC = jpSC;
        this.mode = mode;
        this.phieuSuaChua = psc;
        if (psc != null) {
            this.selectedPBH = pbhBUS.getByMaPhieuBaoHanh(psc.getMPB());
        }
        initComponents();
        
        if (mode.equals("update") && psc != null) {
            fillFormData();
        }
    }

    private void initComponents() {
        this.setSize(new Dimension(850, 650));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);

        // Title
        String titleText = mode.equals("create") ? "THÊM PHIẾU SỬA CHỮA MỚI" : "CẬP NHẬT PHIẾU SỬA CHỮA";
        titlePage = new HeaderTitle(titleText);
        this.add(titlePage, BorderLayout.NORTH);

        // Main panel
        pnlMain = new JPanel(new BorderLayout(0, 12));
        pnlMain.setBackground(Color.white);
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Top panel - Thông tin phiếu bảo hành
        pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(new Color(248, 250, 252));
        pnlTop.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                "Thông tin phiếu bảo hành  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14),
                new Color(37, 99, 235)
            ),
            new EmptyBorder(8, 8, 8, 8)
        ));
        pnlTop.setPreferredSize(new Dimension(0, 135));
        
        if (selectedPBH != null) {
            JPanel infoPanel = new JPanel(new GridLayout(4, 4, 15, 8));
            infoPanel.setBackground(new Color(248, 250, 252));
            infoPanel.setBorder(new EmptyBorder(8, 15, 8, 15));
            
            SanPhamDTO sp = spBUS.getByMaSP(selectedPBH.getMSP());
            KhachHangDTO kh = khBUS.getKhachHangById(selectedPBH.getMKH());
            
            addInfoRow(infoPanel, "Mã phiếu BH:", String.valueOf(selectedPBH.getMPB()));
            addInfoRow(infoPanel, "Mã hóa đơn:", String.valueOf(selectedPBH.getMHD()));
            addInfoRow(infoPanel, "Sản phẩm:", sp != null ? sp.getTEN() : "N/A");
            addInfoRow(infoPanel, "Khách hàng:", kh != null ? kh.getHOTEN() : "N/A");
            addInfoRow(infoPanel, "Ngày bắt đầu:", String.valueOf(selectedPBH.getNGAYBATDAU()));
            addInfoRow(infoPanel, "Ngày kết thúc:", String.valueOf(selectedPBH.getNGAYKETTHUC()));
            
            // Hiển thị trạng thái bảo hành
            Date currentDate = new Date(System.currentTimeMillis());
            String trangThai;
            Color statusColor;
            if (selectedPBH.getTRANGTHAI() == 0 || selectedPBH.getNGAYKETTHUC().before(currentDate)) {
                trangThai = "Hết hạn";
                statusColor = new Color(220, 38, 38);
            } else {
                trangThai = "Còn hạn";
                statusColor = new Color(22, 163, 74);
            }
            
            JLabel lblTTTitle = new JLabel("Trạng thái:");
            lblTTTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
            JLabel lblTTValue = new JLabel(trangThai);
            lblTTValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
            lblTTValue.setForeground(statusColor);
            
            infoPanel.add(lblTTTitle);
            infoPanel.add(lblTTValue);
            
            pnlTop.add(infoPanel, BorderLayout.CENTER);
        } else {
            JLabel lblNoInfo = new JLabel("Chưa chọn phiếu bảo hành", JLabel.CENTER);
            lblNoInfo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 13));
            lblNoInfo.setForeground(Color.GRAY);
            pnlTop.add(lblNoInfo, BorderLayout.CENTER);
        }
        
        pnlMain.add(pnlTop, BorderLayout.NORTH);

        // Center panel - Form
        pnlCenter = new JPanel(new GridLayout(7, 1, 0, 10));
        pnlCenter.setBackground(Color.white);
        pnlCenter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129), 2),
                "  Chi tiết sửa chữa  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14),
                new Color(5, 150, 105)
            ),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        // Combobox nhân viên
        ArrayList<NhanVienDTO> listNV = nvBUS.getAll();
        String[] arrNV = new String[listNV.size() + 1];
        arrNV[0] = "Chưa phân công";
        for (int i = 0; i < listNV.size(); i++) {
            arrNV[i + 1] = listNV.get(i).getMNV() + " - " + listNV.get(i).getHOTEN();
        }
        cmbNhanVien = new SelectForm("Nhân viên", arrNV);
        
        // Dates
        dateNgayNhan = new InputDate("Ngày nhận");
        dateNgayTra = new InputDate("Ngày trả (tùy chọn)");
        
        // Combobox tình trạng
        String[] arrTinhTrang = {"Chờ xử lý", "Đang sửa", "Hoàn thành", "Không sửa được"};
        cmbTinhTrang = new SelectForm("Tình trạng", arrTinhTrang);
        
        // Other fields
        txtNguyenNhan = new InputForm("Nguyên nhân");
        
        txtChiPhi = new InputForm("Chi phí (VNĐ)");
        PlainDocument docChiPhi = (PlainDocument) txtChiPhi.getTxtForm().getDocument();
        docChiPhi.setDocumentFilter(new NumericDocumentFilter());
        txtChiPhi.setText("0");
        
        txtGhiChu = new InputForm("Ghi chú (tùy chọn)");
        
        pnlCenter.add(cmbNhanVien);
        pnlCenter.add(dateNgayNhan);
        pnlCenter.add(dateNgayTra);
        pnlCenter.add(cmbTinhTrang);
        pnlCenter.add(txtNguyenNhan);
        pnlCenter.add(txtChiPhi);
        pnlCenter.add(txtGhiChu);

        pnlMain.add(pnlCenter, BorderLayout.CENTER);

        // Bottom panel - Buttons
        pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlBottom.setBackground(Color.white);
        pnlBottom.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        btnSave = new ButtonCustom("Lưu thông tin", "success", 14);
        btnSave.setPreferredSize(new Dimension(160, 40));
        
        btnCancel = new ButtonCustom("Hủy bỏ", "danger", 14);
        btnCancel.setPreferredSize(new Dimension(160, 40));
        
        btnSave.addActionListener(this);
        btnCancel.addActionListener(this);
        
        pnlBottom.add(btnSave);
        pnlBottom.add(btnCancel);

        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        // Thêm scroll pane cho nội dung
        JScrollPane scrollPane = new JScrollPane(pnlMain);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblTitle.setForeground(new Color(55, 65, 81));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        lblValue.setForeground(new Color(17, 24, 39));
        
        panel.add(lblTitle);
        panel.add(lblValue);
    }

    private void fillFormData() {
        if (phieuSuaChua == null) return;
        
        // Load nhân viên
        if (phieuSuaChua.getMNV() != null) {
            NhanVienDTO nv = nvBUS.getByMaNV(phieuSuaChua.getMNV());
            if (nv != null) {
                String nvText = nv.getMNV() + " - " + nv.getHOTEN();
                cmbNhanVien.setSelectedItem(nvText);
            }
        } else {
            cmbNhanVien.setSelectedIndex(0);
        }
        
        // Load dates
        dateNgayNhan.setDate(phieuSuaChua.getNGAYNHAN());
        if (phieuSuaChua.getNGAYTRA() != null) {
            dateNgayTra.setDate(phieuSuaChua.getNGAYTRA());
        }
        
        // Load tình trạng
        cmbTinhTrang.setSelectedIndex(phieuSuaChua.getTINHTRANG());
        
        // Load other fields
        txtNguyenNhan.setText(phieuSuaChua.getNGUYENNHAN() != null ? phieuSuaChua.getNGUYENNHAN() : "");
        txtChiPhi.setText(phieuSuaChua.getCHIPHI() != null ? phieuSuaChua.getCHIPHI().toString() : "0");
        txtGhiChu.setText(phieuSuaChua.getGHICHU() != null ? phieuSuaChua.getGHICHU() : "");
    }

    private boolean validateForm() {
        if (mode.equals("create") && selectedPBH == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu bảo hành!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            if (dateNgayNhan.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                dateNgayNhan.requestFocus();
                return false;
            }
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày nhận không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dateNgayNhan.requestFocus();
            return false;
        }
        
        if (txtNguyenNhan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập nguyên nhân sửa chữa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNguyenNhan.requestFocus();
            return false;
        }
        
        String chiPhiText = txtChiPhi.getText().trim();
        if (!chiPhiText.isEmpty()) {
            try {
                BigDecimal chiPhi = new BigDecimal(chiPhiText);
                if (chiPhi.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Chi phí không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtChiPhi.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Chi phí không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtChiPhi.requestFocus();
                return false;
            }
        }
        
        return true;
    }

    private void saveData() {
        if (!validateForm()) return;
        
        try {
            // Get nhân viên
            Integer mnv = null;
            String selectedNV = (String) cmbNhanVien.getSelectedItem();
            if (!selectedNV.equals("Chưa phân công")) {
                mnv = Integer.parseInt(selectedNV.split(" - ")[0]);
            }
            
            Date ngayNhan = new Date(dateNgayNhan.getDate().getTime());
            Date ngayTra = dateNgayTra.getDate() != null ? new Date(dateNgayTra.getDate().getTime()) : null;
            int tinhTrang = cmbTinhTrang.getSelectedIndex();
            String nguyenNhan = txtNguyenNhan.getText().trim();
            BigDecimal chiPhi = new BigDecimal(txtChiPhi.getText().trim().isEmpty() ? "0" : txtChiPhi.getText().trim());
            String ghiChu = txtGhiChu.getText().trim();
            
            if (mode.equals("create")) {
                // Tạo mới
                PhieuSuaChuaDTO psc = new PhieuSuaChuaDTO(
                    selectedPBH.getMPB(), mnv, ngayNhan, ngayTra,
                    nguyenNhan, tinhTrang, chiPhi, ghiChu
                );
                
                if (pscBUS.add(psc)) {
                    JOptionPane.showMessageDialog(this, "Thêm phiếu sửa chữa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    jpSC.refreshTable();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm phiếu sửa chữa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Cập nhật
                phieuSuaChua.setMNV(mnv);
                phieuSuaChua.setNGAYNHAN(ngayNhan);
                phieuSuaChua.setNGAYTRA(ngayTra);
                phieuSuaChua.setTINHTRANG(tinhTrang);
                phieuSuaChua.setNGUYENNHAN(nguyenNhan);
                phieuSuaChua.setCHIPHI(chiPhi);
                phieuSuaChua.setGHICHU(ghiChu);
                
                if (pscBUS.update(phieuSuaChua)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật phiếu sửa chữa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    jpSC.refreshTable();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật phiếu sửa chữa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
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
