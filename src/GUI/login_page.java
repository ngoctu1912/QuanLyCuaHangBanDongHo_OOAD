package GUI;

import BUS.TaiKhoanBUS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import GUI.Dialog.QuenMatKhau;
import javax.swing.border.EmptyBorder;
import GUI.Component.InputForm;
import helper.BCrypt;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;

public class login_page extends JFrame implements KeyListener {

    private JPanel login_nhap;
    private JLabel lb2;
    private InputForm txtUsername, txtPassword;
    private JButton bt;

    public login_page() {
        init();
        txtUsername.setText("admin1412");
        txtPassword.setPass("cc123123");
        this.setVisible(true);
    }

    private void init() {
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);

        this.setTitle("Đăng nhập");
        this.setSize(new Dimension(900, 500));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));
        JFrame jf = this;
        imgIntro();

        login_nhap = new JPanel();
        login_nhap.setBackground(Color.WHITE);
        login_nhap.setLayout(new FlowLayout(1, 0, 10));
        login_nhap.setBorder(new EmptyBorder(20, 0, 0, 0));
        login_nhap.setPreferredSize(new Dimension(490, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        JLabel lb1 = new JLabel("<html><p style='font-size: 28px; font-weight: bold; font-family: Tahoma; color: #111111;'>Đăng nhập</p></html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        login_nhap.add(lb1);
        JPanel paneldn = new JPanel();
        paneldn.setBackground(Color.BLACK);
        paneldn.setPreferredSize(new Dimension(300, 200));
        paneldn.setLayout(new GridLayout(2, 1));

        txtUsername = new InputForm("Tên đăng nhập");
        txtUsername.getLblTitle().setBorder(new EmptyBorder(20,0,0,0));
        txtUsername.getTxtForm().setBorder(BorderFactory.createLineBorder(Color.decode("#3A3A3A"), 2));
        txtUsername.getTxtForm().setBorder(BorderFactory.createCompoundBorder(
            txtUsername.getTxtForm().getBorder(), // Giữ viền gốc
            BorderFactory.createEmptyBorder(0, 7, 0, 0) // Padding trái 10px
        ));
        paneldn.add(txtUsername);

        txtPassword = new InputForm(" Mật khẩu", "password");
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.getLblTitle().setBorder(new EmptyBorder(20,0,0,0));
        txtPassword.getTxtPass().setBorder(BorderFactory.createLineBorder(Color.decode("#3A3A3A"),2)); // Add border
        txtPassword.getTxtPass().setBorder(BorderFactory.createCompoundBorder(
            txtPassword.getTxtPass().getBorder(), // Giữ viền gốc
            BorderFactory.createEmptyBorder(0, 7, 0, 0) // Padding trái 10px
        ));
        gbc.gridy++;
        gbc.gridx = 0;
        paneldn.add(txtPassword);

        txtUsername.getTxtForm().addKeyListener(this);
        txtPassword.getTxtPass().addKeyListener(this);

        login_nhap.add(paneldn);

        lb2 = new JLabel("<html><i style='font-size: 11px;'>Quên mật khẩu?</i></html>", JLabel.RIGHT);
        lb2.setPreferredSize(new Dimension(280,40));
        lb2.setForeground(Color.BLACK);
        lb2.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lb2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lb2.setForeground(Color.decode("#c0c0c0"));
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                setVisible(false); // Ẩn giao diện đăng nhập
                QuenMatKhau qmk = new QuenMatKhau(jf);
                qmk.setVisible(true);
            }

            public void mouseExited(MouseEvent e) {
                lb2.setForeground(Color.BLACK);
            }
        });

        login_nhap.add(lb2);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        bt = new JButton("Đăng nhập");
        bt.setPreferredSize(new Dimension(285, 45));
        bt.setLayout(new FlowLayout(1, 0, 15));
        bt.setBackground(Color.decode("#333333"));
        bt.setFont(new Font("Tahoma", Font.BOLD, 16));
        bt.setForeground(Color.WHITE);
        buttonPanel.add(bt);

        bt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bt.setBackground(Color.black);
            }

            public void mousePressed(MouseEvent evt) {
                try {
                    checkLogin();
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(login_page.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void mouseExited(MouseEvent e) {
                bt.setBackground(Color.decode("#111111"));
            }
        });

        login_nhap.add(buttonPanel);
        this.add(login_nhap, BorderLayout.EAST);
    }

    public void checkLogin() throws UnsupportedLookAndFeelException {
        String usernameCheck = txtUsername.getText();
        String passwordCheck = txtPassword.getPass();
        
        if (usernameCheck.equals("") || passwordCheck.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin đầy đủ", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else {
            // Disable button để tránh click nhiều lần
            bt.setEnabled(false);
            bt.setText("Đang đăng nhập...");
            
            // Chạy login check trên thread riêng để không block UI
            SwingWorker<TaiKhoanDTO, Void> worker = new SwingWorker<TaiKhoanDTO, Void>() {
                @Override
                protected TaiKhoanDTO doInBackground() throws Exception {
                    // Query database một lần duy nhất
                    return TaiKhoanDAO.getInstance().selectByUser(usernameCheck);
                }
                
                @Override
                protected void done() {
                    try {
                        TaiKhoanDTO tk = get();
                        
                        if (tk == null) {
                            JOptionPane.showMessageDialog(login_page.this, "Tài khoản của bạn không tồn tại trên hệ thống", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        } else if (tk.getTT() == 0) {
                            JOptionPane.showMessageDialog(login_page.this, "Tài khoản của bạn đang bị khóa", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        } else if (!BCrypt.checkpw(passwordCheck, tk.getMK())) {
                            JOptionPane.showMessageDialog(login_page.this, "Mật khẩu không khớp", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        } else {
                            // Đăng nhập thành công
                             System.out.println("dang nhap thanh cong !");
                            performLogin(tk);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(login_page.this, "Lỗi: " + ex.getMessage(), "Lỗi!", JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger(login_page.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        bt.setText("Đăng nhập");
                        bt.setEnabled(true);
                    }
                }
            };
            
            worker.execute();
        }
    }

    /**
     * Tối ưu: Chỉ load dữ liệu Main khi cần thiết (lazy loading)
     */
    private void performLogin(TaiKhoanDTO tk) {
        try {
           
            this.dispose();
            Main main = new Main(tk);
            main.setVisible(true);
           
        } catch (Exception ex) {
            Logger.getLogger(login_page.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imgIntro() {
        JPanel bo = new JPanel();
        bo.setPreferredSize(new Dimension(400, 556));
        bo.setBackground(Color.white);
        bo.setLayout(new BorderLayout());

        ImageIcon iconOrgin = new ImageIcon("./src/img/watchstore.jpg");
        Image imageOrgin = iconOrgin.getImage();

        int width = bo.getPreferredSize().width;
        int height = bo.getPreferredSize().height;
        Image scaleImage = imageOrgin.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaleImage);
        JLabel lb_img_1 = new JLabel(scaledIcon);
        bo.add(lb_img_1, BorderLayout.CENTER);

        this.add(bo, BorderLayout.WEST);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                checkLogin();
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(login_page.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
         java.sql.Connection conn = config.JDBCUtil.getConnection();
    if (conn != null) {
        System.out.println(" ket noi thanh cong !");
        config.JDBCUtil.closeConnection(conn);
    } else {
        System.out.println("❌ ket noi that bai !");
        return; // Dừng app nếu không kết nối được
    }

        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);
        login_page login = new login_page();
        login.setVisible(true);
    }
}