package GUI.Component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class InputImage extends JPanel implements ActionListener {

    private JButton btnChooseImg;
    private JLabel img;
    private String url_img;

    public InputImage() {

    }

    public InputImage(String title) {
        this.setBackground(Color.white);
        btnChooseImg = new JButton(title);
        img = new JLabel();
        img.setPreferredSize(new Dimension(250, 280));
        btnChooseImg.addActionListener(this);
        this.add(btnChooseImg);
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        ImageIcon imgicon = new ImageIcon("./src/img_product/" + url_img);
        imgicon = new ImageIcon(scale(imgicon));
        btnChooseImg.setIcon(imgicon);
        btnChooseImg.setText("");
        this.url_img = url_img;
    }

    public void setUnable() {
        this.btnChooseImg.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc;
        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and  GIF images", "png", "gif", "jpg", "jpeg");
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(jfc.getSelectedFile().getPath());
            this.url_img = (String) jfc.getSelectedFile().getPath();
            File file = jfc.getSelectedFile();
            ImageIcon imgicon = new ImageIcon(String.valueOf(jfc.getSelectedFile()));
            BufferedImage b;
            try {
                b = ImageIO.read(file);
                imgicon = new ImageIcon(scale(imgicon));
                System.out.println(imgicon.getIconWidth() + ":" + imgicon.getIconHeight());
                btnChooseImg.setText("");
                btnChooseImg.setIcon(imgicon);
            } catch (IOException ex) {
                Logger.getLogger(InputImage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public Image scale(ImageIcon x) {
        int WIDTH = 250;
        int HEIGHT = 280;
        
        // Progressive scaling - resize từng bước để ảnh nét hơn
        BufferedImage img = toBufferedImage(x.getImage());
        return getScaledImage(img, WIDTH, HEIGHT);
    }

    public static ImageIcon resizeImage(ImageIcon imageIcon, int newWidth) {
        // Kiểm tra imageIcon hợp lệ
        if (imageIcon == null || imageIcon.getIconWidth() <= 0 || imageIcon.getIconHeight() <= 0) {
            // Trả về ảnh mặc định
            BufferedImage defaultImage = new BufferedImage(newWidth, newWidth, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = defaultImage.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, newWidth, newWidth);
            g.dispose();
            return new ImageIcon(defaultImage);
        }
        
        int newHeight = (int) (imageIcon.getIconHeight() * ((double) newWidth / imageIcon.getIconWidth()));
        
        // Progressive scaling
        BufferedImage img = toBufferedImage(imageIcon.getImage());
        BufferedImage scaled = getScaledImage(img, newWidth, newHeight);
        
        return new ImageIcon(scaled);
    }
    
    // Chuyển Image thành BufferedImage
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        
        // Kiểm tra kích thước ảnh hợp lệ
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        
        if (width <= 0 || height <= 0) {
            // Trả về ảnh mặc định nếu ảnh không hợp lệ
            BufferedImage defaultImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = defaultImage.createGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 100, 100);
            g.setColor(Color.DARK_GRAY);
            g.drawString("No Image", 20, 50);
            g.dispose();
            return defaultImage;
        }
        
        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
    
    // Progressive scaling - resize từng bước nhỏ để giữ chất lượng
    private static BufferedImage getScaledImage(BufferedImage src, int targetWidth, int targetHeight) {
        int type = (src.getTransparency() == java.awt.Transparency.OPAQUE) 
                   ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        
        BufferedImage ret = src;
        int w = src.getWidth();
        int h = src.getHeight();
        
        // Resize từng bước 50% cho đến khi đạt kích thước mong muốn
        while (w > targetWidth || h > targetHeight) {
            if (w > targetWidth) {
                w = Math.max(targetWidth, w / 2);
            }
            if (h > targetHeight) {
                h = Math.max(targetHeight, h / 2);
            }
            
            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();
            
            ret = tmp;
        }
        
        return ret;
    }
}
