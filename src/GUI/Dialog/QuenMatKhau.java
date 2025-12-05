package GUI.Dialog;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import BUS.TaiKhoanBUS;
import com.formdev.flatlaf.FlatLightLaf;
import helper.SendEmailSMTP;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuenMatKhau extends JDialog {
    private TaiKhoanDTO tk;
    private TaiKhoanBUS tkbus;
    private boolean daGui;
    private String maXN;
    private JButton btnCheck, btnSend, btnBack, btnSave;
    private JTextField txtEmail, txtCode;
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private Timer timer;
    private int countdown = 60;
    private JFrame loginFrame; // Tham chiếu đến login_page
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[a-zA-z0-9]{1,}@gmail.com$", Pattern.CASE_INSENSITIVE);

    public QuenMatKhau(JFrame parent) {
        super(parent, "Quên Mật Khẩu", true);
        this.loginFrame = parent; // Lưu tham chiếu đến login_page
        setResizable(false);
        daGui = false;
        maXN = null;
        tk = null;
        tkbus = new TaiKhoanBUS();
        initializeUI();
        setSize(500, 400);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(230, 240, 250));
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblEmail = new JLabel("Nhập email");
        txtEmail = new JTextField(20);
        btnSend = new JButton("Gửi");
        configureButton(btnSend, "/icon/send_16.png", new Color(200, 220, 240));
        btnSend.addActionListener(this::btnGuiActionPerformed);
        addComponent(lblEmail, 0, 0, 1, gbc);
        addComponent(txtEmail, 0, 1, 2, gbc);
        addComponent(btnSend, 0, 3, 1, gbc);

        JLabel lblCode = new JLabel("Mã xác nhận");
        txtCode = new JTextField(20);
        btnCheck = new JButton("Kiểm tra");
        configureButton(btnCheck, "/icon/check_16.png", new Color(200, 220, 240));
        btnCheck.addActionListener(this::btnKiemtraActionPerformed);
        addComponent(lblCode, 1, 0, 1, gbc);
        addComponent(txtCode, 1, 1, 2, gbc);
        addComponent(btnCheck, 1, 3, 1, gbc);

        JLabel lblNewPassword = new JLabel("Mật khẩu mới");
        txtNewPassword = new JPasswordField(20);
        txtNewPassword.setEnabled(false); // Vô hiệu hóa ban đầu
        addComponent(lblNewPassword, 2, 0, 1, gbc);
        addComponent(txtNewPassword, 2, 1, 3, gbc);

        JLabel lblConfirmPassword = new JLabel("Xác nhận MK");
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setEnabled(false); // Vô hiệu hóa ban đầu
        addComponent(lblConfirmPassword, 3, 0, 1, gbc);
        addComponent(txtConfirmPassword, 3, 1, 3, gbc);

        btnBack = new JButton("Quay lại");
        configureButton(btnBack, "/icon/back_24.png", new Color(200, 220, 240));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(btnBack, gbc);
        btnBack.addActionListener(e -> {
            this.dispose(); // Đóng QuenMatKhau
            loginFrame.setVisible(true); // Hiển thị lại login_page
        });

        btnSave = new JButton("Lưu thay đổi");
        configureButton(btnSave, "/icon/save.png", new Color(200, 220, 240));
        btnSave.setEnabled(false); // Vô hiệu hóa ban đầu
        btnSave.addActionListener(this::btnLuuActionPerformed);
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(btnSave, gbc);
    }

    private void addComponent(Component component, int row, int col, int width, GridBagConstraints gbc) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = width;
        add(component, gbc);
    }

    private void configureButton(JButton button, String iconPath, Color bgColor) {
        button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        button.setBackground(bgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void btnGuiActionPerformed(ActionEvent evt) {
        String emailTo = txtEmail.getText();
        if (isValid(emailTo)) {
            tk = TaiKhoanDAO.getInstance().selectByEmail(emailTo);
            if (tk == null) {
                JOptionPane.showMessageDialog(this, "Tài khoản của email này không tồn tại trên hệ thống");
            } else {
                maXN = SendEmailSMTP.getOTP();
                SendEmailSMTP.sendOTP(emailTo, maXN);
                daGui = true;
                JOptionPane.showMessageDialog(this, "Gửi thành công. Vui lòng kiểm tra email để lấy mã xác nhận");
                startCountdown();
                // Mở khóa các trường và nút khi gửi lại mã
                txtCode.setEnabled(true);
                btnCheck.setEnabled(true);
                // Vô hiệu hóa các trường mật khẩu
                txtNewPassword.setEnabled(false);
                txtConfirmPassword.setEnabled(false);
                btnSave.setEnabled(false);
            }
        }
    }

    private void startCountdown() {
        btnSend.setEnabled(false);
        timer = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdown > 0) {
                    btnSend.setText("Gửi lại (" + countdown + "s)");
                    countdown--;
                } else {
                    timer.stop();
                    btnSend.setText("Gửi");
                    btnSend.setEnabled(true);
                    countdown = 60;
                }
            }
        });
        timer.start();
    }

    private void btnKiemtraActionPerformed(ActionEvent evt) {
        String txtMaXN = txtCode.getText();
        if (daGui) {
            if (txtMaXN == null || txtMaXN.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bạn chưa nhập mã xác nhận");
                txtCode.requestFocus();
            } else if (txtMaXN.equals(maXN)) {
                JOptionPane.showMessageDialog(this, "Mã xác nhận trùng khớp!");
                txtCode.setEnabled(false);
                btnCheck.setEnabled(false);
                txtNewPassword.setEnabled(true); // Kích hoạt trường mật khẩu mới
                txtConfirmPassword.setEnabled(true); // Kích hoạt trường xác nhận mật khẩu
                btnSave.setEnabled(true); // Kích hoạt nút lưu
            } else {
                JOptionPane.showMessageDialog(this, "Mã xác nhận không trùng khớp!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng nhấn nút gửi để lấy mã xác nhận");
        }
    }

    private void btnLuuActionPerformed(ActionEvent evt) {
        String mk = new String(txtNewPassword.getPassword());
        String xnmk = new String(txtConfirmPassword.getPassword());
        if (mk.isEmpty() || xnmk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin các trường");
        } else {
            if (mk.equals(xnmk)) {
                tkbus.doiMatKhau(tk.getMNV(), mk);
                JOptionPane.showMessageDialog(this, "Thay đổi mật khẩu thành công");
                this.dispose(); // Đóng QuenMatKhau
                loginFrame.setVisible(true); // Hiển thị lại login_page
            } else {
                JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không trùng khớp.");
            }
        }
    }

    private boolean isValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (email == null || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập email");
            txtEmail.requestFocus();
            return false;
        }

        if (matcher.matches()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng email. Vd: abc@gmail.com");
            txtEmail.requestFocus();
            return false;
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup(); // Sử dụng FlatLaf
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quên Mật Khẩu");
            QuenMatKhau dialog = new QuenMatKhau(frame);
            dialog.setVisible(true);
        });
    }
}