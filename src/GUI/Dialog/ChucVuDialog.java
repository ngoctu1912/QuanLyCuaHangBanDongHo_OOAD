package GUI.Dialog;

import BUS.ChucVuBUS;
import DAO.ChucVuDAO;
import DTO.ChucVuDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Component.NumericDocumentFilter;
import helper.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

public class ChucVuDialog extends JDialog {

    private ChucVuBUS nv;
    private HeaderTitle titlePage;
    private JPanel main, bottom;
    private ButtonCustom btnAdd, btnEdit, btnExit;
    private InputForm name;
    private InputForm mucluong;
    private ChucVuDTO nhanVien;

    public ChucVuBUS cvbus = new ChucVuBUS();

    public ChucVuDialog(ChucVuBUS nv, JFrame owner, boolean modal, String title, String type) {
        super(owner, title, modal);
        this.nv = nv;
        init(title, type);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public ChucVuDialog(ChucVuBUS nv, JFrame owner, boolean modal, String title, String type, DTO.ChucVuDTO nhanVien) {
        super(owner, title, modal);
        this.nv = nv;
        this.nhanVien = nhanVien;
        init(title, type);
        name.setText(nhanVien.getTENCV());
        mucluong.setText(nhanVien.getMUCLUONG()+"");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void init(String title, String type) {
        this.setSize(new Dimension(450, 500));
        this.setLayout(new BorderLayout(0, 0));

        titlePage = new HeaderTitle(title.toUpperCase());

        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.white);
        name = new InputForm("Tên chức vụ");
        mucluong = new InputForm("Mức lương");
        PlainDocument ml = (PlainDocument)mucluong.getTxtForm().getDocument();
        ml.setDocumentFilter((new NumericDocumentFilter()));
        PlainDocument phonex = (PlainDocument) mucluong.getTxtForm().getDocument();
        phonex.setDocumentFilter((new NumericDocumentFilter()));
        JPanel jpanelG = new JPanel(new GridLayout(2, 1, 0, 2));
        jpanelG.setBackground(Color.white);
        jpanelG.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel jgender = new JPanel(new GridLayout(1, 2));
        jgender.setSize(new Dimension(500, 80));
        jgender.setBackground(Color.white);
        jpanelG.add(jgender);
        JPanel jpaneljd = new JPanel();
        jpaneljd.setBorder(new EmptyBorder(10, 10, 10, 10));
        jpaneljd.setSize(new Dimension(500, 100));
        jpaneljd.setLayout(new FlowLayout(FlowLayout.LEFT));
        jpaneljd.setBackground(Color.white);
        main.add(name);
        main.add(mucluong);
        main.add(jpanelG);

        bottom = new JPanel(new FlowLayout());
        bottom.setBorder(new EmptyBorder(10, 0, 10, 0));
        bottom.setBackground(Color.white);
        btnAdd = new ButtonCustom("Thêm chức vụ", "success", 14);
        btnEdit = new ButtonCustom("Lưu thông tin", "success", 14);
        btnExit = new ButtonCustom("Hủy bỏ", "danger", 14);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    btnAdd.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            
            if (ValidationInput()) {
                String txtName = name.getText();
                
                // Kiểm tra trùng lặp tên chức vụ
                boolean isDuplicate = nv.getAll().stream()
                    .anyMatch(cv -> cv.getTENCV().equalsIgnoreCase(txtName));
                
                if (isDuplicate) {
                    JOptionPane.showMessageDialog(null, "Tên chức vụ đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int manv = nv.getRecordCount() + 1;
                String txtSdt = mucluong.getText();
                ChucVuDTO nV = new ChucVuDTO(manv, txtName, Integer.parseInt(txtSdt));
                ChucVuDAO.getInstance().insert(nV);
                nv.insertNv(nV);
                nv.loadTable();
                dispose();
            }
        } catch (ParseException ex) {
            Logger.getLogger(ChucVuDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
});

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (ValidationInput()) {
                        String txtName = name.getText();
                        String txtSdt = mucluong.getText();
                        ChucVuDTO nV = new ChucVuDTO(nhanVien.getMCV(), txtName, Integer.parseInt(txtSdt));
                        ChucVuDAO.getInstance().update(nV);
                        System.out.println("Index:" + nv.getIndex());
                        nv.listChucVu.set(nv.getIndex(), nV);
                        nv.loadTable();
                        dispose();
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ChucVuDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        switch (type) {
            case "create" ->
                bottom.add(btnAdd);
            case "update" ->
                bottom.add(btnEdit);
            case "detail" -> {
                name.setDisable();
                mucluong.setDisable();
            }
            default ->
                throw new AssertionError();
        }

        bottom.add(btnExit);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);

    }

    boolean ValidationInput() throws ParseException {
        if (Validation.isEmpty(name.getText())) {
            JOptionPane.showMessageDialog(this, "Tên chức vụ không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if(name.getText().length()<6){
            JOptionPane.showMessageDialog(this, "Tên chức vụ ít nhất 6 kí tự!");
            return false;
        }
        else if (Validation.isEmpty(mucluong.getText()) && !Validation.isNumber(mucluong.getText())) {
            JOptionPane.showMessageDialog(this, "Mức lương không được rỗng và phải là ký tự số", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
}
