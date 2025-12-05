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
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import BUS.NhanVienBUS;
import BUS.PhieuSuaChuaBUS;
import DTO.NhanVienDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.PhieuSuaChuaDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Component.SelectForm;
import GUI.Panel.BaoHanh;

public class PhieuSuaChuaDialog extends JDialog implements ActionListener {

    private BaoHanh jpBH;
    private PhieuBaoHanhDTO phieuBaoHanh;
    private PhieuSuaChuaBUS pscBUS = new PhieuSuaChuaBUS();
    private NhanVienBUS nvBUS = new NhanVienBUS();
    
    private HeaderTitle titlePage;
    private JPanel pnlMain, pnlTop, pnlCenter, pnlBottom;
    private JPanel pnlInfo, pnlForm, pnlTable;
    
    // Thông tin phiếu bảo hành
    private JLabel lblMaPBH, lblMaHD, lblMaSP, lblMaKH;
    
    // Form thêm sửa chữa
    private SelectForm cmbNhanVien, cmbTinhTrang;
    private InputDate dateNgayNhan, dateNgayTra;
    private InputForm txtNguyenNhan, txtChiPhi, txtGhiChu;
    
    // Buttons
    private ButtonCustom btnThemSuaChua, btnCapNhat, btnXoa, btnDong;
    
    // Table lịch sử sửa chữa
    private JTable tableSuaChua;
    private DefaultTableModel tblModel;
    private JScrollPane scrollTable;
    
    private PhieuSuaChuaDTO selectedPSC = null;

    public PhieuSuaChuaDialog(BaoHanh jpBH, JFrame owner, String title, boolean modal, PhieuBaoHanhDTO pbh) {
        super(owner, title, modal);
        this.jpBH = jpBH;
        this.phieuBaoHanh = pbh;
        initComponents();
        loadDataTable();
    }

    private void initComponents() {
        this.setSize(new Dimension(900, 700));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);

        // Title
        titlePage = new HeaderTitle("QUẢN LÝ SỬA CHỮA BẢO HÀNH");
        this.add(titlePage, BorderLayout.NORTH);

        // Main panel
        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(Color.white);
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel - Thông tin phiếu bảo hành
        pnlTop = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlTop.setBackground(Color.white);
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu bảo hành"));
        
        lblMaPBH = new JLabel("Mã PBH: " + phieuBaoHanh.getMPB());
        lblMaHD = new JLabel("Mã HĐ: " + phieuBaoHanh.getMHD());
        lblMaSP = new JLabel("Mã SP: " + phieuBaoHanh.getMSP());
        lblMaKH = new JLabel("Mã KH: " + phieuBaoHanh.getMKH());
        
        JLabel lblNgayBD = new JLabel("Ngày BĐ: " + phieuBaoHanh.getNGAYBATDAU());
        JLabel lblNgayKT = new JLabel("Ngày KT: " + phieuBaoHanh.getNGAYKETTHUC());
        
        pnlTop.add(lblMaPBH);
        pnlTop.add(lblMaHD);
        pnlTop.add(lblMaSP);
        pnlTop.add(lblMaKH);
        pnlTop.add(lblNgayBD);
        pnlTop.add(lblNgayKT);

        // Center panel - Form và Table
        pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.white);

        // Form panel
        pnlForm = new JPanel(new GridLayout(6, 1, 5, 5));
        pnlForm.setBackground(Color.white);
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin sửa chữa"));
        
        // Combobox nhân viên
        ArrayList<NhanVienDTO> listNV = nvBUS.getAll();
        String[] arrNV = new String[listNV.size() + 1];
        arrNV[0] = "Chưa phân công";
        for (int i = 0; i < listNV.size(); i++) {
            arrNV[i + 1] = listNV.get(i).getMNV() + " - " + listNV.get(i).getHOTEN();
        }
        cmbNhanVien = new SelectForm("Nhân viên", arrNV);
        
        // Combobox tình trạng
        String[] arrTinhTrang = {"Chờ xử lý", "Đang sửa", "Hoàn thành", "Không sửa được"};
        cmbTinhTrang = new SelectForm("Tình trạng", arrTinhTrang);
        
        dateNgayNhan = new InputDate("Ngày nhận");
        dateNgayTra = new InputDate("Ngày trả");
        txtNguyenNhan = new InputForm("Nguyên nhân");
        txtChiPhi = new InputForm("Chi phí");
        txtGhiChu = new InputForm("Ghi chú");
        
        pnlForm.add(cmbNhanVien);
        pnlForm.add(dateNgayNhan);
        pnlForm.add(dateNgayTra);
        pnlForm.add(cmbTinhTrang);
        pnlForm.add(txtNguyenNhan);
        pnlForm.add(txtChiPhi);
        pnlForm.add(txtGhiChu);

        // Table panel
        pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.white);
        pnlTable.setBorder(BorderFactory.createTitledBorder("Lịch sử sửa chữa"));
        
        String[] header = {"Mã SC", "Nhân viên", "Ngày nhận", "Ngày trả", "Tình trạng", "Chi phí"};
        tblModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableSuaChua = new JTable(tblModel);
        tableSuaChua.setFocusable(false);
        tableSuaChua.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableSuaChua.getSelectedRow() != -1) {
                loadFormData();
            }
        });
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < header.length; i++) {
            tableSuaChua.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        scrollTable = new JScrollPane(tableSuaChua);
        pnlTable.add(scrollTable, BorderLayout.CENTER);

        pnlCenter.add(pnlForm, BorderLayout.NORTH);
        pnlCenter.add(pnlTable, BorderLayout.CENTER);

        // Bottom panel - Buttons
        pnlBottom = new JPanel(new FlowLayout());
        pnlBottom.setBackground(Color.white);
        pnlBottom.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        btnThemSuaChua = new ButtonCustom("Thêm sửa chữa", "success", 14);
        btnCapNhat = new ButtonCustom("Cập nhật", "warning", 14);
        btnXoa = new ButtonCustom("Xóa", "danger", 14);
        btnDong = new ButtonCustom("Đóng", "secondary", 14);
        
        btnThemSuaChua.addActionListener(this);
        btnCapNhat.addActionListener(this);
        btnXoa.addActionListener(this);
        btnDong.addActionListener(this);
        
        pnlBottom.add(btnThemSuaChua);
        pnlBottom.add(btnCapNhat);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnDong);

        // Add to main
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        this.add(pnlMain, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void loadDataTable() {
        tblModel.setRowCount(0);
        ArrayList<PhieuSuaChuaDTO> list = pscBUS.getByMaPhieuBaoHanh(phieuBaoHanh.getMPB());
        for (PhieuSuaChuaDTO psc : list) {
            String tenNV = pscBUS.getTenNhanVien(psc.getMNV());
            String ngayTra = psc.getNGAYTRA() != null ? psc.getNGAYTRA().toString() : "Chưa trả";
            String chiPhi = psc.getCHIPHI() != null ? psc.getCHIPHI().toString() : "0";
            
            tblModel.addRow(new Object[]{
                psc.getMSC(),
                tenNV,
                psc.getNGAYNHAN(),
                ngayTra,
                psc.getTenTinhTrang(),
                chiPhi
            });
        }
    }

    private void loadFormData() {
        int row = tableSuaChua.getSelectedRow();
        if (row != -1) {
            int msc = (int) tblModel.getValueAt(row, 0);
            selectedPSC = pscBUS.getByMaPhieuSuaChua(msc);
            
            if (selectedPSC != null) {
                // Load nhân viên
                if (selectedPSC.getMNV() != null) {
                    NhanVienDTO nv = nvBUS.getByMaNV(selectedPSC.getMNV());
                    if (nv != null) {
                        cmbNhanVien.setSelectedItem(nv.getMNV() + " - " + nv.getHOTEN());
                    }
                } else {
                    cmbNhanVien.setSelectedIndex(0);
                }
                
                // Load dates
                dateNgayNhan.setDate(selectedPSC.getNGAYNHAN());
                if (selectedPSC.getNGAYTRA() != null) {
                    dateNgayTra.setDate(selectedPSC.getNGAYTRA());
                }
                
                // Load tình trạng
                cmbTinhTrang.setSelectedIndex(selectedPSC.getTINHTRANG());
                
                // Load other fields
                txtNguyenNhan.setText(selectedPSC.getNGUYENNHAN() != null ? selectedPSC.getNGUYENNHAN() : "");
                txtChiPhi.setText(selectedPSC.getCHIPHI() != null ? selectedPSC.getCHIPHI().toString() : "0");
                txtGhiChu.setText(selectedPSC.getGHICHU() != null ? selectedPSC.getGHICHU() : "");
            }
        }
    }

    private void clearForm() {
        cmbNhanVien.setSelectedIndex(0);
        cmbTinhTrang.setSelectedIndex(0);
        dateNgayNhan.getDateChooser().setDate(null);
        dateNgayTra.getDateChooser().setDate(null);
        txtNguyenNhan.setText("");
        txtChiPhi.setText("");
        txtGhiChu.setText("");
        selectedPSC = null;
        tableSuaChua.clearSelection();
    }

    private void themSuaChua() {
        try {
            // Validate
            if (dateNgayNhan.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận!");
                return;
            }
            
            // Get nhân viên
            Integer mnv = null;
            String selectedNV = (String) cmbNhanVien.getSelectedItem();
            if (!selectedNV.equals("Chưa phân công")) {
                mnv = Integer.parseInt(selectedNV.split(" - ")[0]);
            }
            
            Date ngayNhan = new Date(dateNgayNhan.getDate().getTime());
            Date ngayTra = dateNgayTra.getDate() != null ? new Date(dateNgayTra.getDate().getTime()) : null;
            int tinhTrang = cmbTinhTrang.getSelectedIndex();
            String nguyenNhan = txtNguyenNhan.getText();
            BigDecimal chiPhi = new BigDecimal(txtChiPhi.getText().isEmpty() ? "0" : txtChiPhi.getText());
            String ghiChu = txtGhiChu.getText();
            
            PhieuSuaChuaDTO psc = new PhieuSuaChuaDTO(
                phieuBaoHanh.getMPB(), mnv, ngayNhan, ngayTra,
                nguyenNhan, tinhTrang, chiPhi, ghiChu
            );
            
            if (pscBUS.add(psc)) {
                JOptionPane.showMessageDialog(this, "Thêm phiếu sửa chữa thành công!");
                loadDataTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phiếu sửa chữa thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void capNhatSuaChua() {
        if (selectedPSC == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu sửa chữa cần cập nhật!");
            return;
        }
        
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
            String nguyenNhan = txtNguyenNhan.getText();
            BigDecimal chiPhi = new BigDecimal(txtChiPhi.getText().isEmpty() ? "0" : txtChiPhi.getText());
            String ghiChu = txtGhiChu.getText();
            
            selectedPSC.setMNV(mnv);
            selectedPSC.setNGAYNHAN(ngayNhan);
            selectedPSC.setNGAYTRA(ngayTra);
            selectedPSC.setTINHTRANG(tinhTrang);
            selectedPSC.setNGUYENNHAN(nguyenNhan);
            selectedPSC.setCHIPHI(chiPhi);
            selectedPSC.setGHICHU(ghiChu);
            
            if (pscBUS.update(selectedPSC)) {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu sửa chữa thành công!");
                loadDataTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật phiếu sửa chữa thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void xoaSuaChua() {
        if (selectedPSC == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu sửa chữa cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa phiếu sửa chữa này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (pscBUS.delete(selectedPSC)) {
                JOptionPane.showMessageDialog(this, "Xóa phiếu sửa chữa thành công!");
                loadDataTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phiếu sửa chữa thất bại!");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThemSuaChua) {
            themSuaChua();
        } else if (e.getSource() == btnCapNhat) {
            capNhatSuaChua();
        } else if (e.getSource() == btnXoa) {
            xoaSuaChua();
        } else if (e.getSource() == btnDong) {
            this.dispose();
        }
    }
}
