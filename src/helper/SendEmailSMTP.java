package helper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailSMTP {
    public static String getOTP() {
        int min = 100000;
        int max = 999999;
        return Integer.toString((int) ((Math.random() * (max - min)) + min));
    }
    
    public static void sendOTP(String emailTo, String otp) {
        // QUAN TRỌNG: Thay bằng email và App Password của bạn
        final String username = "thuluyen234@gmail.com"; 
        final String password = "ksjq yuld gbus ifnr"; // Phải là App Password 16 ký tự
        
        Properties prop = new Properties();
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.starttls.required", "true"); // Bắt buộc TLS
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Chỉ định phiên bản TLS

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Bật debug mode để xem chi tiết lỗi
            session.setDebug(true);
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject("Recover Password");
            
            String htmlContent = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">" +
                "<div style=\"margin:50px auto;width:70%;padding:20px 0\">" +
                "<div style=\"border-bottom:1px solid #eee\">" +
                "    <a href=\"#\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">WarehouseManagement</a>" +
                "</div>" +
                "<p style=\"font-size:1.1em\">Xin chào,</p>" +
                "<p>Cảm ơn bạn đã sử dụng hệ thống. Sử dụng mã OTP sau để hoàn tất việc lấy lại mật khẩu:</p>" +
                "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + otp + "</h2>" +
                "<p style=\"font-size:0.9em;\">Trân trọng,<br />Đội ngũ hỗ trợ</p>" +
                "<hr style=\"border:none;border-top:1px solid #eee\" />" +
                "<div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">" +
                "    <p>App Management</p>" +
                "    <p>Số 273 An Dương Vương, Phường 3, Quận 5, TP. HCM</p>" +
                "    <p>Việt Nam</p>" +
                "</div>" +
                "</div>" +
                "</div>";
            
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            System.out.println("The Email has been sent successfully!");
            
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateEmailConfig(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEmailConfig'");
    }
}