package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DTO.ViTriTrungBayDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Panel.ViTriTrungBay;
import helper.Validation;

public class ViTriTrungBayDialog extends JDialog implements MouseListener {

    private ViTriTrungBay vtPanel;
    private ViTriTrungBayDTO vt;
    private String type;
    
    HeaderTitle titlePage;
    JPanel pnlMain, pnlTop, pnlBottom, pnlCenter;
    
    InputForm txtTenViTri;
    InputForm txtMoTa;
    InputForm txtGhiChu;
    
    ButtonCustom btnAdd, btnEdit, btnExit;

    public ViTriTrungBayDialog(ViTriTrungBay vtPanel, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.vtPanel = vtPanel;
        this.type = type;
        initComponents(title);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public ViTriTrungBayDialog(ViTriTrungBay vtPanel, JFrame owner, String title, boolean modal, String type, ViTriTrungBayDTO vt) {
        super(owner, title, modal);
        this.vtPanel = vtPanel;
        this.type = type;
        this.vt = vt;
        initComponents(title);
        loadDataToForm(vt);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void initComponents(String title) {
        this.setSize(new Dimension(500, 370));
        this.setLayout(new BorderLayout(0, 0));
        
        titlePage = new HeaderTitle(title.toUpperCase());
        pnlTop = new JPanel();
        pnlTop.setLayout(new GridLayout(1, 1));
        pnlTop.setPreferredSize(new Dimension(0, 70));
        pnlTop.add(titlePage);
        
        pnlCenter = new JPanel();
        pnlCenter.setPreferredSize(new Dimension(420, 200));
        pnlCenter.setBackground(Color.white);
        pnlCenter.setLayout(new FlowLayout(0, 0, 20));
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtTenViTri = new InputForm("Tên vị trí trưng bày");
        txtTenViTri.setPreferredSize(new Dimension(440, 70));
        
        txtMoTa = new InputForm("Mô tả");
        txtMoTa.setPreferredSize(new Dimension(440, 70));
        
        txtGhiChu = new InputForm("Ghi chú");
        txtGhiChu.setPreferredSize(new Dimension(440, 70));
        
        pnlCenter.add(txtTenViTri);
        pnlCenter.add(txtMoTa);
        pnlCenter.add(txtGhiChu);
        
        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(0, 70));
        pnlBottom.setBackground(Color.white);
        pnlBottom.setLayout(new FlowLayout(1, 20, 20));
        
        btnAdd = new ButtonCustom("Thêm vị trí", "success", 14);
        btnEdit = new ButtonCustom("Lưu thông tin", "success", 14);
        btnExit = new ButtonCustom("Hủy bỏ", "danger", 14);
        
        btnAdd.addMouseListener(this);
        btnEdit.addMouseListener(this);
        btnExit.addMouseListener(this);

        switch (type) {
            case "create" -> {
                pnlBottom.add(btnAdd);
                pnlBottom.add(btnExit);
            }
            case "update" -> {
                pnlBottom.add(btnEdit);
                pnlBottom.add(btnExit);
            }
            case "view" -> {
                pnlBottom.add(btnExit);
                txtTenViTri.setDisable();
                txtMoTa.setDisable();
                txtGhiChu.setDisable();
            }
        }
        
        this.add(pnlTop, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlBottom, BorderLayout.SOUTH);
    }

    public void loadDataToForm(ViTriTrungBayDTO vt) {
        txtTenViTri.setText(vt.getTEN());
        txtGhiChu.setText(vt.getGHICHU() != null ? vt.getGHICHU() : "");
    }

    public boolean validateInput() {
        if (Validation.isEmpty(txtTenViTri.getText())) {
            JOptionPane.showMessageDialog(this, "Tên vị trí trưng bày không được để trống!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public void addViTriTrungBay() {
        if (!validateInput()) {
            return;
        }
        
        String tenViTri = txtTenViTri.getText();
        String ghiChu = txtGhiChu.getText();
        
        ViTriTrungBayDTO newVT = new ViTriTrungBayDTO(0, tenViTri, ghiChu);
        
        if (vtPanel.vtBUS.add(newVT)) {
            JOptionPane.showMessageDialog(this, "Thêm vị trí trưng bày thành công!");
            vtPanel.listVT = vtPanel.vtBUS.getAll();
            vtPanel.loadDataTable(vtPanel.listVT);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm vị trí trưng bày thất bại!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateViTriTrungBay() {
        if (!validateInput()) {
            return;
        }
        
        String tenViTri = txtTenViTri.getText();
        String ghiChu = txtGhiChu.getText();
        
        vt.setTEN(tenViTri);
        vt.setGHICHU(ghiChu);
        
        if (vtPanel.vtBUS.update(vt)) {
            JOptionPane.showMessageDialog(this, "Cập nhật vị trí trưng bày thành công!");
            vtPanel.listVT = vtPanel.vtBUS.getAll();
            vtPanel.loadDataTable(vtPanel.listVT);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật vị trí trưng bày thất bại!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnAdd) {
            addViTriTrungBay();
        } else if (e.getSource() == btnEdit) {
            updateViTriTrungBay();
        } else if (e.getSource() == btnExit) {
            dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
