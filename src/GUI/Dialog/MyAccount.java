package GUI.Dialog;

import BUS.NhanVienBUS;
import BUS.TaiKhoanBUS;
import DAO.NhanVienDAO;
import DAO.TaiKhoanDAO;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Component.MenuTaskbar;
import GUI.Component.NumericDocumentFilter;
import helper.BCrypt;
import helper.Validation;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;



public class MyAccount extends JDialog implements ActionListener {

    CardLayout card;
    ButtonCustom save, cancel;
    HeaderTitle title;
    JPanel top, center, bottom;
    InputForm current_pwd, phone, EMAIL, new_pwd, confirm;
    NhanVienDTO nv;
    TaiKhoanBUS tkbus;
    NhanVienBUS nvbus;
    MenuTaskbar menuTaskbar;
    JLabel[] jl;
    JPanel[] panel;
    JLabel change;
    JPanel pn_1 , pn_2 , pn_3;
    InputForm current_pass , new_pass, confirm_pass;
    Boolean check  = false ;

    public MyAccount(JFrame owner, MenuTaskbar menutaskbar, String title, boolean modal) {
        super(owner, title, modal);
        initComponent(menutaskbar);
        this.setLocationRelativeTo(null);
    }

    public void initComponent(MenuTaskbar menutaskbar) {
        tkbus = new TaiKhoanBUS();
        nvbus = new NhanVienBUS();
        this.menuTaskbar = menutaskbar;
        this.setSize(400, 500);
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(Color.WHITE);
        this.setResizable(false);
        nv = menuTaskbar.nhanVienDTO;
        top = new JPanel();
        top.setBackground(Color.WHITE);
        top.setLayout(new FlowLayout(0, 0, 0));
        title = new HeaderTitle("");
        top.add(title);
        this.add(top, BorderLayout.NORTH);

        center_1();

        bottom = new JPanel(new FlowLayout(1, 20, 10));
        bottom.setBackground(Color.WHITE);

        cancel = new ButtonCustom("Hủy", "danger", 15);
        cancel.addActionListener(this);
        bottom.add(cancel);
        save = new ButtonCustom("Lưu", "success", 15);
        save.addActionListener(this);
        bottom.add(save);
        this.add(bottom, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void center_1() {
        check =  false;
        title.setText("CHỈNH SỬA THÔNG TIN");
        center = new JPanel(new GridLayout(4,1));
        center.setBorder(new EmptyBorder(20, 10, 0, 10));
        center.setBackground(Color.WHITE);
        String opt[] = {"Số điện thoại", "Email", "Mật khẩu"};
        panel = new JPanel[3];
        panel[0] = new JPanel(new GridLayout(1, 1));
        panel[0].setPreferredSize(new Dimension(400, 100));
        phone = new InputForm(opt[0]);
        PlainDocument phonex = (PlainDocument) phone.getTxtForm().getDocument();
        phonex.setDocumentFilter((new NumericDocumentFilter()));
        phone.setText(nv.getSDT());
        panel[0].add(phone);
        panel[1] = new JPanel(new GridLayout(1, 1));
        panel[1].setPreferredSize(new Dimension(400, 100));
        EMAIL = new InputForm(opt[1]);
        EMAIL.setText(nv.getEMAIL());
        panel[1].add(EMAIL);
        panel[2] = new JPanel(new GridLayout(1, 1));
        panel[2].setPreferredSize(new Dimension(400, 100));
        current_pwd = new InputForm(opt[2]);
        current_pwd.setText("********");
        current_pwd.setDisable();
        panel[2].add(current_pwd);
        change = new JLabel("<html><p style = 'font-size : 11px;'>Thay đổi</p></html>" , JLabel.RIGHT);
        change.setPreferredSize(new Dimension(25,25));
        change.setBorder(new EmptyBorder(0 , 0 , 30,20));
        change.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseEntered(MouseEvent e) {
                change.setForeground(new Color(173, 216, 230));
            }
            @Override 
            public void mouseClicked(MouseEvent e) { 
                center_2();
           }
            @Override
            public void mouseExited(MouseEvent e) {
                change.setForeground(Color.BLACK);
            }
        });
        center.add(panel[0]);
        center.add(panel[1]);
        center.add(panel[2]);
        center.add(change);
        this.add(center, BorderLayout.CENTER);
    }

    public void center_2() {
        check = true;
        title.setText("Chỉnh sửa mật khẩu");
        center.removeAll();
        center.setLayout(new GridLayout(3,1));
        center.setBorder(new EmptyBorder(10,10,20,10));
        pn_1 = new JPanel();
        pn_1.setLayout(new GridLayout(1,1));
        current_pass = new InputForm("Nhập mật khẩu hiện tại :","password");
        pn_1.add(current_pass);
        pn_2 = new JPanel();
        pn_2.setLayout(new GridLayout(1,1));
        new_pass = new InputForm("Nhập mật khẩu mới :","password");
        pn_2.add(new_pass);
        pn_3 = new JPanel();
        pn_3.setLayout(new GridLayout(1,1));
        confirm_pass = new InputForm("Nhập lại mật khẩu mới :","password");
        pn_3.add(confirm_pass);

        center.add(pn_1);
        center.add(pn_2);
        center.add(pn_3);
        center.repaint();
        center.validate();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            if(check ) {
                center.removeAll();
                center_1();
            } else {
                this.dispose();
            }
        }
        if(e.getSource() == save) {
            if(check)  {
                System.out.println("22222222222222");
                            TaiKhoanDTO tkdto = tkbus.getTaiKhoan(tkbus.getTaiKhoanByMaNV(nv.getMNV()));
                            if (Validation.isEmpty(current_pass.getPass())) {
                                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không được rỗng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                            } else if (Validation.isEmpty(new_pass.getPass())||new_pass.getPass().length()<6) {
                                JOptionPane.showMessageDialog(this, "Mật khẩu mới không được rỗng và có ít nhất 6 ký tự", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                            } else if (Validation.isEmpty(confirm_pass.getPass())) {
                                JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không được rỗng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                                return;
                            } else if (!new_pass.getPass().equals(confirm_pass.getPass()) ) {
                                JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp với mật khẩu mới", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                                return;
                            } else {
                                if (BCrypt.checkpw(current_pass.getPass(), tkdto.getMK())) {
                                    String pass = BCrypt.hashpw(confirm_pass.getPass(), BCrypt.gensalt(12));
                                    TaiKhoanDAO.getInstance().updatePass(nv.getEMAIL(), pass);
                                    JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                                    current_pass.setPass("");
                                    new_pass.setPass("");
                                    confirm_pass.setPass("");
                                    center.removeAll();
                                    center_1();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                    }
            else if (!check) {
                String text_phone = phone.getText(); 
                String text_email = EMAIL.getText(); 
                
                if(text_phone.equals(nv.getSDT()) &&  text_email.equals(nv.getEMAIL()) ) JOptionPane.showMessageDialog(this, "Đã sửa gì đâu mà lưu ?" , "Chỉnh sửa PHONE and EMAIL", JOptionPane.WARNING_MESSAGE);
                else {
                    boolean changed = false;
                    if (!text_phone.equals(nv.getSDT()) ) {
                        if (Validation.isEmpty(phone.getText()) || phone.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Số điện thoại không được rỗng và phải có 10 ký tự sô", "Chỉnh sửa số điện thoại", JOptionPane.WARNING_MESSAGE);
                    } else {
                        NhanVienDTO nvdto = new NhanVienDTO(nv.getMNV(), nv.getHOTEN(), nv.getGIOITINH(), text_phone, nv.getNGAYSINH(), nv.getTT(), nv.getEMAIL(), nv.getMCV());
                        NhanVienDAO.getInstance().update(nvdto);
                        changed = true;
                        }
                    }

                    if (!text_email.equals(nv.getEMAIL()) ) {
                        if (Validation.isEmpty(EMAIL.getText()) || !Validation.isEmail(EMAIL.getText())) {
                        JOptionPane.showMessageDialog(this, "Email không được rỗng và phải đúng định dạng", "Chỉnh sửa EMAIL", JOptionPane.WARNING_MESSAGE);
                        } else {
                            NhanVienDTO nvdto = new NhanVienDTO(nv.getMNV(), nv.getHOTEN(), nv.getGIOITINH(), text_phone, nv.getNGAYSINH(), nv.getTT(), text_email, nv.getMCV());
                            NhanVienDAO.getInstance().update(nvdto);
                            changed = true;
                        }
                    }
                    if(changed) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                        this.dispose();
                    }
                }
            }
        } 
        menuTaskbar.resetChange();
    }
}
