package GUI.Dialog;

import BUS.TaiKhoanBUS;
import DAO.NhomQuyenDAO;
import DTO.NhomQuyenDTO;
import DTO.TaiKhoanDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Component.SelectForm;
import GUI.Panel.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TaiKhoanDialog extends JDialog {
    private TaiKhoanBUS tkbus=new TaiKhoanBUS();
    private TaiKhoan taiKhoan;
    private HeaderTitle titlePage;
    private JPanel pnmain, pnbottom;
    private ButtonCustom btnThem, btnCapNhat, btnHuyBo;
    private InputForm username;
    private InputForm password;
    private SelectForm maNhomQuyen;
    private SelectForm trangthai;
    int manv;
    private ArrayList<NhomQuyenDTO> listNq = NhomQuyenDAO.getInstance().selectAll();
    TaiKhoanDTO tk;

    public TaiKhoanDialog(TaiKhoan taiKhoan, JFrame owner, String title, boolean modal, String type, int manv) {
        super(owner, title, modal);
        init(title, type);
        this.manv = manv;
        this.taiKhoan = taiKhoan;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public TaiKhoanDialog(TaiKhoan taiKhoan, JFrame owner, String title, boolean modal, String type, TaiKhoanDTO tk) {
        super(owner, title, modal);
        System.out.println("aaaaaa");
        init(title, type);
        System.out.println("aaaaaa");
          System.out.println("VVVVVVVVVVVVV");
        this.tk = tk;
        this.manv = tk.getMNV();
        this.taiKhoan = taiKhoan;
        username.setText(tk.getTDN());
        password.setPass("");
        maNhomQuyen.setSelectedItem(NhomQuyenDAO.getInstance().selectById(tk.getMNQ() + "").getTennhomquyen());
        System.out.println("aaaa");
        trangthai.setSelectedIndex(tk.getTT());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void init(String title, String type) {
        this.setSize(new Dimension(500, 620));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());
        pnmain = new JPanel(new GridLayout(4, 1, 5, 0));
        pnmain.setBackground(Color.white);
        username = new InputForm("Tên đăng nhập");
        password = new InputForm("Mật khẩu", "password");
        maNhomQuyen = new SelectForm("Nhóm quyền", getNhomQuyen());
        trangthai = new SelectForm("Trạng thái", new String[]{"Ngưng hoạt động", "Hoạt động"});
        pnmain.add(username);
        pnmain.add(password);
        pnmain.add(maNhomQuyen);
        pnmain.add(trangthai);
        pnbottom = new JPanel(new FlowLayout());
        pnbottom.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnbottom.setBackground(Color.white);
        btnThem = new ButtonCustom("Thêm tài khoản", "success", 14);
        btnCapNhat = new ButtonCustom("Lưu thông tin", "success", 14);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);

       btnThem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (validateInput()) {
            String tendangnhap = username.getText();
            int check = 0;

            // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
            if (!taiKhoan.taiKhoanBus.checkTDN(tendangnhap)) {
                check = 1;
            }

            if (check == 0) { // Nếu tên đăng nhập chưa tồn tại
                String pass = password.getPass(); 
                int manhom = listNq.get(maNhomQuyen.getSelectedIndex()).getManhomquyen();
                int tt = trangthai.getSelectedIndex();

                // Tạo đối tượng TaiKhoanDTO với mật khẩu không băm
                TaiKhoanDTO tk = new TaiKhoanDTO(manv, tendangnhap, pass, manhom, tt);

                // Gọi phương thức thêm tài khoản
                taiKhoan.taiKhoanBus.addAcc(tk);

                // Tải lại bảng hiển thị tài khoản
                taiKhoan.loadTable(taiKhoan.taiKhoanBus.getTaiKhoanAll());

                // Hiển thị thông báo thành công
                JOptionPane.showMessageDialog(null, "Thêm tài khoản thành công!");
                dispose(); // Đóng hộp thoại
            } else {
                JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại. Vui lòng đổi tên khác!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                username.getFocusCycleRootAncestor();
            }
        }
    }
});
      btnCapNhat.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("BBBBBBBBBBBBBB");
        // Áp dụng validateInputEDT() trước khi tiếp tục cập nhật
        if (validateInputEDT()) {
            if (!(username.getText().length() == 0)) {
                String tendangnhap = username.getText();
                String pass = tk.getMK(); // Sử dụng mật khẩu cũ mặc định
                boolean doiMatKhau = false;

                // Kiểm tra xem tên đăng nhập có bị thay đổi và có tồn tại hay không
                if (!tendangnhap.equals(tk.getTDN())) { 
                    // Kiểm tra tên đăng nhập mới, loại trừ tài khoản hiện tại đang sửa
                    System.out.println("ĐÃ VÔ ĐC RỒI");
                    boolean isExist = false;
                    for (TaiKhoanDTO existingTaiKhoan : taiKhoan.taiKhoanBus.getTaiKhoanAll()) {
                        // Kiểm tra tên đăng nhập của tài khoản khác và không phải tài khoản đang sửa
                        if (existingTaiKhoan.getTDN().equals(tendangnhap) && existingTaiKhoan.getMNV() != tk.getMNV()) {
                            isExist = true;
                            break;
                        }
                    }

                    if (isExist) { // Nếu tên tài khoản đã tồn tại
                        JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại. Vui lòng đổi tên khác!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        return; // Nếu tên đăng nhập đã tồn tại, không tiếp tục thực hiện cập nhật
                    }
                }

                // Kiểm tra nếu người dùng nhập mật khẩu mới
                if (!password.getPass().equals("")) {
                    pass = password.getPass(); // Sử dụng mật khẩu mới
                    doiMatKhau = true;
                }

                int manhom = listNq.get(maNhomQuyen.getSelectedIndex()).getManhomquyen();
                int tt = trangthai.getSelectedIndex();
                TaiKhoanDTO tk = new TaiKhoanDTO(manv, tendangnhap, pass, manhom, tt);
                taiKhoan.taiKhoanBus.updateAcc(tk);
                taiKhoan.loadTable(taiKhoan.taiKhoanBus.getTaiKhoanAll());
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");

                // Nếu người dùng muốn đổi mật khẩu, gọi phương thức đổi mật khẩu
                if (doiMatKhau) {
                    System.out.println(tk.getMNV());
                    tkbus.doiMatKhau(tk.getMNV(), pass);
                    JOptionPane.showMessageDialog(null, "Mật khẩu đã được cập nhật.");
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng không để trống tên");
            }
        }
    }
});


        btnHuyBo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        switch (type) {
            case "create" ->
                pnbottom.add(btnThem);
            case "update" -> {
                pnbottom.add(btnCapNhat);
            }
            case "view" -> {
                username.setDisable();
                pnmain.remove(password);
                maNhomQuyen.setDisable();
                trangthai.setDisable();
                this.setSize(new Dimension(500, 550));
            }
            default ->
                throw new AssertionError();
        }
        pnbottom.add(btnHuyBo);
        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.add(pnbottom, BorderLayout.SOUTH);
    }

    public String[] getNhomQuyen() {
        String[] listNhomQuyen = new String[listNq.size()];
        for (int i = 0; i < listNq.size(); i++) {
            listNhomQuyen[i] = listNq.get(i).getTennhomquyen();
        }
        return listNhomQuyen;
    }

    public boolean validateInput() {
        if (username.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống tên đăng nhập");
            return false;
        } else if (username.getText().length() < 6) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập ít nhất 6 kí tự");
            return false;
        } else if (password.getPass().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống mật khẩu");
            return false;
        } else if (password.getPass().length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu ít nhất 6 ký tự");
            return false;
        }
        return true;
    }
    public boolean validateInputEDT() {
    if (username.getText().length() == 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng không để trống tên đăng nhập");
        return false;
    } else if (username.getText().length() < 6) {
        JOptionPane.showMessageDialog(this, "Tên đăng nhập ít nhất 6 kí tự");
        return false;
    } else if (password.getPass().length() > 0 && password.getPass().length() < 6) {
        // Chỉ kiểm tra mật khẩu khi có mật khẩu mới
        JOptionPane.showMessageDialog(this, "Mật khẩu ít nhất 6 ký tự");
        return false;
    }
    return true;
}

}