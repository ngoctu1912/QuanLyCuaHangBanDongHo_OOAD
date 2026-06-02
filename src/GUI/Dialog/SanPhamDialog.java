package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.PlainDocument;

import BUS.PhieuNhapBUS;
import BUS.SanPhamBUS;
import DAO.SanPhamDAO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Component.InputImage;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.SelectForm;
import GUI.Panel.SanPham;
import helper.Validation;

public final class SanPhamDialog extends JDialog implements ActionListener {

    private HeaderTitle titlePage;
    private JPanel pninfosanpham, pnbottom, pnCenter, pninfosanphamright, pnmain;
    private ButtonCustom btnHuyBo, btnAddSanPham;
    private SelectForm nhaCC, thuongHieu;
    InputForm tenSP, namSX, giaNhap, giaBan, baoHanh;
    InputImage hinhanh;
    JTable tblcauhinh;
    JScrollPane scrolltblcauhinh;
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    GUI.Panel.SanPham jpSP;

    SanPhamBUS spBus = new SanPhamBUS();
    PhieuNhapBUS pnbus = new PhieuNhapBUS();

    SanPhamDTO sp;
    String[] arrkhuvuc;
    String[] arrnxb;
    int masp;
    private ButtonCustom btnSaveCH;

    public void init(SanPham jpSP) {
        this.jpSP = jpSP;
        masp = jpSP.spBUS.getMaxMSP() + 1;
    }

    public SanPhamDialog(SanPham jpSP, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        init(jpSP);
        initComponents(title, type);
    }

    public SanPhamDialog(SanPham jpSP, JFrame owner, String title, boolean modal, String type, SanPhamDTO sp) {
        super(owner, title, modal);
        init(jpSP);
        this.sp = sp;
        initComponents(title, type);
    }

    public void initCardOne(String type) {
        pnCenter = new JPanel(new BorderLayout());
        pninfosanpham = new JPanel(new GridLayout(4, 2, 5, 5));
        pninfosanpham.setBackground(Color.WHITE);
        pninfosanpham.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnCenter.add(pninfosanpham, BorderLayout.CENTER);

        pninfosanphamright = new JPanel();
        pninfosanphamright.setBackground(Color.WHITE);
        pninfosanphamright.setPreferredSize(new Dimension(300, 600));
        pninfosanphamright.setBorder(new EmptyBorder(0, 10, 0, 10));
        pnCenter.add(pninfosanphamright, BorderLayout.WEST);

        tenSP = new InputForm("Tên đồng hồ");
        String[] ncc = {"Anh Khuê Watch", "Citizen", "Orient", "Seiko", "Rolex", "Frederique Constant", "Fossil", "Daniel Wellington"};
        nhaCC = new SelectForm("Nhà cung cấp", ncc);
        String[] thuongHieuArr = {"Citizen", "Orient", "Seiko", "Rolex", "Frederique Constant", "Fossil", "Daniel Wellington", "Casio", "Tissot", "Hamilton"};
        thuongHieu = new SelectForm("Thương hiệu", thuongHieuArr);
        namSX = new InputForm("Năm sản xuất");
        PlainDocument namDoc = (PlainDocument) namSX.getTxtForm().getDocument();
        namDoc.setDocumentFilter((new NumericDocumentFilter()));
        
        giaNhap = new InputForm("Giá nhập");
        PlainDocument nhapDoc = (PlainDocument) giaNhap.getTxtForm().getDocument();
        nhapDoc.setDocumentFilter((new NumericDocumentFilter()));
        
        giaBan = new InputForm("Giá bán");
        PlainDocument banDoc = (PlainDocument) giaBan.getTxtForm().getDocument();
        banDoc.setDocumentFilter((new NumericDocumentFilter()));
        
        baoHanh = new InputForm("Bảo hành (tháng)");
        PlainDocument bhDoc = (PlainDocument) baoHanh.getTxtForm().getDocument();
        bhDoc.setDocumentFilter((new NumericDocumentFilter()));
        
        hinhanh = new InputImage("Hình minh họa");

        pninfosanpham.add(tenSP);
        pninfosanpham.add(thuongHieu);
        pninfosanpham.add(nhaCC);
        pninfosanpham.add(namSX);
        pninfosanpham.add(giaNhap);
        pninfosanpham.add(giaBan);
        pninfosanpham.add(baoHanh);
        
        pninfosanphamright.add(hinhanh);

        pnbottom = new JPanel(new FlowLayout());
        pnbottom.setBorder(new EmptyBorder(20, 0, 10, 0));
        pnbottom.setBackground(Color.white);

        switch (type) {
            case "update" -> {
                initView();
                btnSaveCH = new ButtonCustom("Lưu thông tin", "success", 14);
                btnSaveCH.addActionListener(this);
                pnbottom.add(btnSaveCH);
            }
            case "create" -> {
                initCreate();
                btnAddSanPham = new ButtonCustom("Thêm sản phẩm", "success", 14);
                btnAddSanPham.addActionListener(this);
                pnbottom.add(btnAddSanPham);
            }
        }

        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnHuyBo.addActionListener(this);
        pnbottom.add(btnHuyBo);
        pnCenter.add(pnbottom, BorderLayout.SOUTH);
    }

    public void initComponents(String title, String type) {
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.setSize(new Dimension(1150, 480));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());

        pnmain = new JPanel(new CardLayout());

        initCardOne(type);

        pnmain.add(pnCenter);

        switch (type) {
            case "view" ->
                setInfo(sp);
            case "update" ->
                setInfo(sp);
            default -> {
            }
        }
//                throw new AssertionError();

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public String addImage(String urlImg) {
        Random randomGenerator = new Random();
        int ram = randomGenerator.nextInt(1000);
        File sourceFile = new File(urlImg);
        String destPath = "./src/img_product";
        File destFolder = new File(destPath);
        String newName = ram + sourceFile.getName();
        try {
            Path dest = Paths.get(destFolder.getPath(), newName);
            Files.copy(sourceFile.toPath(), dest);
        } catch (IOException e) {
        }
        return newName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnHuyBo) {
            dispose();
        } else if (source == btnAddSanPham && checkCreate()) {
            eventAddSanPham();
        } else if (source == btnSaveCH) {
            if (checkUpdate()) {
                SanPhamDTO snNew = getInfo();
                snNew.setSOLUONG(spBus.getByMaSP(this.sp.getMSP()).getSOLUONG());
                if (!snNew.getHINHANH().equals(this.sp.getHINHANH())) {
                    snNew.setHINHANH(addImage(snNew.getHINHANH()));
                }
                snNew.setMSP(this.sp.getMSP());
                SanPhamDAO.getInstance().update(snNew);
                this.jpSP.spBUS.update(snNew);
                this.jpSP.loadDataTalbe(this.jpSP.spBUS.getAll());
                JOptionPane.showMessageDialog(this, "Sửa thông tin sản phẩm thành công !");
                dispose();
            }
        }

    }

    public void eventAddSanPham() {
        if (checkCreate()) {
            SanPhamDTO sp = getInfo();
            sp.setHINHANH(addImage(sp.getHINHANH()));
            if (jpSP.spBUS.add(sp)) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công !");
                jpSP.loadDataTalbe(jpSP.listSP);
                dispose();
            }
        }
    }

    public SanPhamDTO getInfo() {
        String vtensp = tenSP.getText();
        String hinhanh = this.hinhanh.getUrl_img();
        String vthuongHieu = (String) thuongHieu.getCbb().getSelectedItem();
        int vnhaCC = nhaCC.getSelectedIndex() + 1;
        Integer vnamSX = namSX.getText().trim().isEmpty() ? null : Integer.parseInt(namSX.getText().trim());
        double vgiaNhap = Double.parseDouble(giaNhap.getText());
        double vgiaBan = Double.parseDouble(giaBan.getText());
        int vbaoHanh = baoHanh.getText().trim().isEmpty() ? 12 : Integer.parseInt(baoHanh.getText().trim());
        
        SanPhamDTO result = new SanPhamDTO(
            masp, vtensp, hinhanh, vnhaCC, null, vthuongHieu, 
            vnamSX, vgiaNhap, vgiaBan, vbaoHanh
        );
        return result;
    }

    public void setInfo(SanPhamDTO sp) {
        hinhanh.setUrl_img(sp.getHINHANH());
        tenSP.setText(sp.getTEN());
        if (sp.getTHUONGHIEU() != null && !sp.getTHUONGHIEU().isEmpty()) {
            thuongHieu.getCbb().setSelectedItem(sp.getTHUONGHIEU());
        }
        nhaCC.setSelectedIndex(sp.getMNCC() - 1);
        namSX.setText(sp.getNAMSANXUAT() != null ? String.valueOf(sp.getNAMSANXUAT()) : "");
        giaNhap.setText(String.valueOf((int)sp.getGIANHAP()));
        giaBan.setText(String.valueOf((int)sp.getGIABAN()));
        baoHanh.setText(String.valueOf(sp.getTHOIGIANBAOHANH()));
    }

    public boolean checkCreate() {
        boolean check = true;
        if (Validation.isEmpty(tenSP.getText()) || Validation.isEmpty(giaNhap.getText()) 
                || Validation.isEmpty(giaBan.getText())) {
            check = false;
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc (Tên, Giá nhập, Giá bán)!");
        } else {
            if (hinhanh.getUrl_img() == null) {
                JOptionPane.showMessageDialog(this, "Chưa thêm ảnh sản phẩm!");
                check = false;
            }
            // Kiểm tra năm sản xuất
            if (!namSX.getText().trim().isEmpty()) {
                try {
                    int nam = Integer.parseInt(namSX.getText().trim());
                    int namHienTai = java.time.Year.now().getValue();
                    if (nam > namHienTai) {
                        JOptionPane.showMessageDialog(this, "Năm sản xuất không được lớn hơn năm hiện tại (" + namHienTai + ")!");
                        check = false;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Năm sản xuất không hợp lệ!");
                    check = false;
                }
            }
        }
        return check;
    }

    public boolean checkUpdate() {
        boolean check = true;
        if (Validation.isEmpty(tenSP.getText()) || Validation.isEmpty(giaNhap.getText()) 
                || Validation.isEmpty(giaBan.getText())) {
            check = false;
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
        } else {
            // Kiểm tra năm sản xuất
            if (!namSX.getText().trim().isEmpty()) {
                try {
                    int nam = Integer.parseInt(namSX.getText().trim());
                    int namHienTai = java.time.Year.now().getValue();
                    if (nam > namHienTai) {
                        JOptionPane.showMessageDialog(this, "Năm sản xuất không được lớn hơn năm hiện tại (" + namHienTai + ")!");
                        check = false;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Năm sản xuất không hợp lệ!");
                    check = false;
                }
            }
        }
        return check;
    }

    public void initView() {
    }

    public void initCreate() {
    }
}

