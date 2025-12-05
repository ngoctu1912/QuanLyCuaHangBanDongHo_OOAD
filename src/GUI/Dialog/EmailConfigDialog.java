// File: GUI/Dialog/EmailConfigDialog.java
package GUI.Dialog;

import helper.SendEmailSMTP;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.Properties;

public class EmailConfigDialog extends JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSave, btnTest;
    
    public EmailConfigDialog(JFrame parent) {
        super(parent, "Cấu Hình Email", true);
        initComponents();
        setSize(500, 300);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        JLabel lblUsername = new JLabel("Email:");
        txtUsername = new JTextField(20);
        addComponent(lblUsername, 0, 0, 1, gbc);
        addComponent(txtUsername, 0, 1, 2, gbc);
        
        // Password
        JLabel lblPassword = new JLabel("App Password:");
        txtPassword = new JPasswordField(20);
        addComponent(lblPassword, 1, 0, 1, gbc);
        addComponent(txtPassword, 1, 1, 2, gbc);
        
        // Note
        JLabel lblNote = new JLabel("<html><i>Lưu ý: Cần sử dụng App Password từ Gmail, không phải mật khẩu thông thường</i></html>");
        lblNote.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        add(lblNote, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        btnTest = new JButton("Kiểm tra");
        btnSave = new JButton("Lưu");
        
        btnTest.addActionListener(e -> testEmail());
        btnSave.addActionListener(e -> saveConfig());
        
        buttonPanel.add(btnTest);
        buttonPanel.add(btnSave);
        
        gbc.gridy = 3;
        add(buttonPanel, gbc);
    }
    
    private void addComponent(Component component, int row, int col, int width, GridBagConstraints gbc) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = width;
        add(component, gbc);
    }
    
    private void testEmail() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        // Test gửi email
        SendEmailSMTP.updateEmailConfig(username, password);
        String testOTP = SendEmailSMTP.getOTP();
        
        try {
            SendEmailSMTP.sendOTP(username, testOTP);
            JOptionPane.showMessageDialog(this, 
                "Kiểm tra thành công! Vui lòng kiểm tra hộp thư của bạn.\nOTP test: " + testOTP);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi gửi email: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveConfig() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            Properties config = new Properties();
            config.setProperty("email.username", username);
            config.setProperty("email.password", password);
            
            FileOutputStream fos = new FileOutputStream("config/email.properties");
            config.store(fos, "Email Configuration");
            fos.close();
            
            SendEmailSMTP.updateEmailConfig(username, password);
            JOptionPane.showMessageDialog(this, "Lưu cấu hình thành công!");
            this.dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi lưu cấu hình: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}