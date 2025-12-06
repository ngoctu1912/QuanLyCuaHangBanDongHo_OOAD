package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
// import java.awt.GridBagConstraints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
// import javax.swing.OverlayLayout;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;

import BUS.KhachHangBUS;
import BUS.MaKhuyenMaiBUS;
import BUS.PhieuNhapBUS;
import BUS.PhieuXuatBUS;
import BUS.SanPhamBUS;
import BUS.ThongKeBUS;
import DTO.KhachHangDTO;
import DTO.MaKhuyenMaiDTO;
import DTO.PhieuNhapDTO;
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;
import DTO.TaiKhoanDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import GUI.Main;
import GUI.Component.PanelBorderRadius;
import helper.Formater;

public class TongQuan extends JPanel {

    private TaiKhoanDTO currentUser;
    private Main mainFrame;

    // Colors
    // private Color MainColor = new Color(255, 255, 255);
    private Color PrimaryColor = new Color(184, 134, 11); // Vàng đồng sang trọng
    private Color SuccessColor = new Color(52, 168, 83);
    private Color WarningColor = new Color(218, 165, 32); // Vàng gold
    private Color DangerColor = new Color(234, 67, 53);
    private Color BackgroundColor = new Color(248, 249, 250);
    private Color CardColor = new Color(255, 255, 255);

    // Business Logic
    private ThongKeBUS thongKeBUS;
    private SanPhamBUS sanPhamBUS;
    private PhieuXuatBUS hoaDonBUS;
    private PhieuNhapBUS phieuNhapBUS;
    private KhachHangBUS khachHangBUS;
    private MaKhuyenMaiBUS maKhuyenMaiBUS;

    public TongQuan() {
        this(null, null);
    }

    public TongQuan(TaiKhoanDTO user) {
        this(user, null);
    }

    public TongQuan(TaiKhoanDTO user, Main main) {
        this.currentUser = user;
        this.mainFrame = main;
        
        // Hiển thị loading screen ngay lập tức
        this.setLayout(new BorderLayout());
        this.setBackground(BackgroundColor);
        JLabel loadingLabel = new JLabel("Đang tải dữ liệu...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        loadingLabel.setForeground(new Color(117, 117, 117));
        this.add(loadingLabel, BorderLayout.CENTER);
        
        // Load data và UI trong background thread
        new Thread(() -> {
            initBUS();
            javax.swing.SwingUtilities.invokeLater(() -> {
                this.removeAll();
                initComponent();
                this.revalidate();
                this.repaint();
            });
        }).start();
        
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
    }

    private void initBUS() {
        thongKeBUS = new ThongKeBUS();
        sanPhamBUS = new SanPhamBUS();
        hoaDonBUS = new PhieuXuatBUS();
        phieuNhapBUS = new PhieuNhapBUS();
        khachHangBUS = new KhachHangBUS();
        maKhuyenMaiBUS = new MaKhuyenMaiBUS();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(BackgroundColor);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (currentUser != null) {
            int roleId = currentUser.getMNQ();
            switch (roleId) {
                case 1 -> createManagerWorkspace();
                case 2 -> createSalesWorkspace();
                default -> createDefaultWorkspace();
            }
        } else {
            createDefaultWorkspace();
        }
    }

    // ==================== ICON LOADER (QUAN TRỌNG) ====================
    private ImageIcon getIcon(String name) {
        java.net.URL url = getClass().getResource("/icon/" + name);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            System.err.println("Không tìm thấy icon: /icon/" + name);
            return null;
        }
    }

    private ImageIcon getIcon(String name, int w, int h) {
        ImageIcon icon = getIcon(name);
        if (icon == null)
            return null;
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // ==================== CARD CẢNH BÁO TỒN KHO THẤP ====================
    private JPanel createLowStockAlert() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBackground(CardColor);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Header với icon cảnh báo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CardColor);

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleWrapper.setBackground(CardColor);

        JLabel iconLabel = new JLabel();
        ImageIcon warningIcon = getIcon("warning.png", 32, 32);
        if (warningIcon != null) iconLabel.setIcon(warningIcon);

        JLabel titleLabel = new JLabel("Cảnh báo tồn kho thấp (< 3 sản phẩm)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleLabel.setForeground(new Color(238, 64, 76));

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        // Đếm số sản phẩm tồn kho < 3
        int lowStockCount = 0;
        List<SanPhamDTO> lowStockProducts = new ArrayList<>();
        for (SanPhamDTO sp : sanPhamBUS.getAll()) {
            if (sp.getSL() < 3 && sp.getSL() >= 0) {
                lowStockCount++;
                lowStockProducts.add(sp);
            }
        }

        JPanel countWrapper = new JPanel(new GridBagLayout());
        countWrapper.setBackground(lowStockCount > 0 ? new Color(255, 255, 255) : new Color(240, 253, 244));
        countWrapper.setPreferredSize(new Dimension(100, 32));
        
        JLabel countLabel = new JLabel(lowStockCount + " sản phẩm");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(lowStockCount > 0 ? new Color(0, 0, 0) : new Color(39, 174, 96));
        countWrapper.add(countLabel);

        headerPanel.add(titleWrapper, BorderLayout.WEST);
        headerPanel.add(countWrapper, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Container cho danh sách - KHÔNG dùng JScrollPane
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(CardColor);

        JPanel productList = new JPanel();
        productList.setLayout(new BoxLayout(productList, BoxLayout.Y_AXIS));
        productList.setBackground(CardColor);

        if (lowStockCount == 0) {
            JPanel successPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 15));
            successPanel.setBackground(new Color(240, 253, 244));
            successPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            
            JLabel successIcon = new JLabel("✓");
            successIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
            successIcon.setForeground(new Color(39, 174, 96));
            
            JLabel successText = new JLabel("Tất cả sản phẩm đều có tồn kho ổn định");
            successText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            successText.setForeground(new Color(39, 174, 96));
            
            successPanel.add(successIcon);
            successPanel.add(successText);
            productList.add(successPanel);
        } else {
            // Sắp xếp theo số lượng tăng dần (ít nhất lên đầu)
            lowStockProducts.sort((a, b) -> Integer.compare(a.getSL(), b.getSL()));
            
            for (int i = 0; i < lowStockProducts.size(); i++) {
                SanPhamDTO sp = lowStockProducts.get(i);
                productList.add(createLowStockItem(sp));
                if (i < lowStockProducts.size() - 1) {
                    productList.add(Box.createVerticalStrut(6));
                }
            }
        }

        // Dùng JScrollPane để cuộn nếu có nhiều hơn 2 sản phẩm
        if (lowStockCount > 2) {
            JScrollPane scrollPane = new JScrollPane(productList);
            scrollPane.setBorder(null);
            scrollPane.setBackground(CardColor);
            scrollPane.getViewport().setBackground(CardColor);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
            scrollPane.setPreferredSize(new Dimension(0, 180)); // Chiều cao cho hiển thị thoải mái hơn
            contentWrapper.add(scrollPane, BorderLayout.CENTER);
        } else {
            contentWrapper.add(productList, BorderLayout.NORTH);
        }
        panel.add(contentWrapper, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLowStockItem(SanPhamDTO sp) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setBackground(CardColor);
        item.setBorder(new EmptyBorder(10, 12, 10, 12));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Icon đồng hồ
        JPanel iconWrapper = new JPanel(new GridBagLayout());
        iconWrapper.setPreferredSize(new Dimension(40, 40));
        iconWrapper.setBackground(new Color(248, 249, 250));

        JLabel watchIcon = new JLabel();
        ImageIcon watchImg = getIcon("watch2.png", 28, 28);
        if (watchImg != null) {
            watchIcon.setIcon(watchImg);
        } else {
            watchIcon.setText("⌚");
            watchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        }
        iconWrapper.add(watchIcon);

        // Thông tin sản phẩm
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CardColor);

        JLabel nameLabel = new JLabel(sp.getTEN());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(new Color(44, 62, 80));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel codeLabel = new JLabel("Mã: " + sp.getMSP());
        codeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        codeLabel.setForeground(new Color(127, 140, 141));
        codeLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(codeLabel);

        // Số lượng tồn
        JPanel qtyPanel = new JPanel(new GridBagLayout());
        qtyPanel.setBackground(CardColor);
        qtyPanel.setPreferredSize(new Dimension(60, 40));

        JPanel qtyBadge = new JPanel(new GridBagLayout());
        qtyBadge.setBackground(Color.WHITE);
        qtyBadge.setPreferredSize(new Dimension(50, 50));
        
        JLabel qtyLabel = new JLabel(String.valueOf(sp.getSL()));
        qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        qtyLabel.setForeground(new Color(220, 53, 69));
        qtyBadge.add(qtyLabel);
        
        qtyPanel.add(qtyBadge);

        item.add(iconWrapper, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);
        item.add(qtyPanel, BorderLayout.EAST);

        // Hover effect
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(248, 249, 250));
                infoPanel.setBackground(new Color(248, 249, 250));
                qtyPanel.setBackground(new Color(248, 249, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(CardColor);
                infoPanel.setBackground(CardColor);
                qtyPanel.setBackground(CardColor);
            }
        });

        return item;
    }

    // ==================== WORKSPACE CHO QUẢN LÝ CỬA HÀNG ====================
    private void createManagerWorkspace() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BackgroundColor);

        JPanel header = createHeader("Tổng quan", "Tổng quan hoạt động cửa hàng");
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BackgroundColor);

        // Stats cards
        JPanel statsCards = createStatsCards();
        statsCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        content.add(statsCards);
        content.add(Box.createVerticalStrut(15));

        // Cảnh báo tồn kho
        JPanel lowStockAlert = createLowStockAlert();
        lowStockAlert.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        content.add(lowStockAlert);
        content.add(Box.createVerticalStrut(15));

        // Chart và Quick Actions
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(BackgroundColor);
        centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JPanel chartPanel = createRevenueChart();
        centerPanel.add(chartPanel);

        JPanel actionsPanel = createManagerQuickActions();
        centerPanel.add(actionsPanel);
        
        content.add(centerPanel);
        content.add(Box.createVerticalStrut(15));

        // Recent activities
        JPanel recentPanel = createRecentActivities();
        recentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        content.add(recentPanel);
        
        // Thêm glue để đẩy nội dung lên trên, không tạo khoảng trống thừa
        content.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BackgroundColor);
        scrollPane.getViewport().setBackground(BackgroundColor);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        this.add(mainPanel);
    }

    // ==================== WORKSPACE CHO NHÂN VIÊN BÁN HÀNG ====================
    private void createSalesWorkspace() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BackgroundColor);

        JPanel header = createHeader("Workspace Bán Hàng", "Chào mừng bạn quay trở lại!");
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BackgroundColor);

        // Cảnh báo tồn kho
        JPanel lowStockAlert = createLowStockAlert();
        lowStockAlert.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        content.add(lowStockAlert);
        content.add(Box.createVerticalStrut(20));

        // Layout chính: Grid 2x2 như cũ nhưng đẹp hơn
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBackground(BackgroundColor);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        PanelBorderRadius sellPanel = createLargeActionCard(
                "shopping-cart.png", "BÁN HÀNG", "Bắt đầu giao dịch mới",
                new Color(255, 255, 255), "Shift + B");
        cardsPanel.add(sellPanel);

        PanelBorderRadius customerPanel = createCustomerQuickAccess();
        cardsPanel.add(customerPanel);

        PanelBorderRadius promotionPanel = createPromotionPanel();
        cardsPanel.add(promotionPanel);

        PanelBorderRadius personalStats = createPersonalStats();
        cardsPanel.add(personalStats);

        content.add(cardsPanel);
        content.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BackgroundColor);
        scrollPane.getViewport().setBackground(BackgroundColor);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        this.add(mainPanel);
    }


    // ==================== DEFAULT WORKSPACE CHO NHÓM QUYỀN MỚI ====================
    private void createDefaultWorkspace() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BackgroundColor);

        JPanel header = createHeader("Chào mừng", "Hệ thống quản lý cửa hàng đồng hồ");
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BackgroundColor);
        content.setBorder(new EmptyBorder(50, 100, 50, 100));

        PanelBorderRadius centerCard = new PanelBorderRadius();
        centerCard.setLayout(new BorderLayout(20, 20));
        centerCard.setBackground(CardColor);
        centerCard.setBorder(new EmptyBorder(80, 100, 80, 100));

        // Icon và thông báo chào mừng
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(CardColor);

        // Icon
        JLabel iconLabel = new JLabel();
        ImageIcon watchIcon = getIcon("watch2.png", 120, 120);
        if (watchIcon != null) {
            iconLabel.setIcon(watchIcon);
        } else {
            iconLabel.setText("⌚");
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Chào mừng bạn!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(25, 0, 0, 0));

        // Tên người dùng
        String userName = currentUser != null ? currentUser.getTDN() : "Người dùng";
        JLabel userLabel = new JLabel("Xin chào, " + userName);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setForeground(PrimaryColor);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Thêm các thành phần
        welcomePanel.add(iconLabel);
        welcomePanel.add(titleLabel);
        welcomePanel.add(userLabel);

        centerCard.add(welcomePanel, BorderLayout.CENTER);
        content.add(centerCard, BorderLayout.CENTER);

        mainPanel.add(content, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    // ==================== HELPER METHODS ====================
    private JPanel createHeader(String title, String subtitle) {
        PanelBorderRadius headerPanel = new PanelBorderRadius();
        headerPanel.setLayout(new BorderLayout(10, 5));
        headerPanel.setBackground(CardColor);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(CardColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 33, 33));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(117, 117, 117));

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        JLabel timeLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(new Color(117, 117, 117));

        headerPanel.add(textPanel, BorderLayout.WEST);
        headerPanel.add(timeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    // ==================== STAT CARDS ====================
    private JPanel createStatsCards() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(BackgroundColor);

        ArrayList<ThongKeTungNgayTrongThangDTO> today = thongKeBUS.getThongKe7NgayGanNhat();
        long doanhThuToday = today.isEmpty() ? 0 : today.get(today.size() - 1).getDoanhthu();

        panel.add(createStatCard("Doanh thu hôm nay", Formater.FormatVND(doanhThuToday),
                "revenue.png", SuccessColor));
        panel.add(createStatCard("Tổng đơn hàng", String.valueOf(hoaDonBUS.getAll().size()),
                "order.png", PrimaryColor));
        panel.add(createStatCard("Sản phẩm trong kho", String.valueOf(sanPhamBUS.getAll().size()),
                "checklist.png", WarningColor));
        panel.add(createStatCard("Khách hàng", String.valueOf(khachHangBUS.getAll().size()),
                "costumer.png", DangerColor));

        return panel;
    }

    // ==================== STAT CARD ====================
    private PanelBorderRadius createStatCard(String title, String value, String icon, Color accentColor) {
        PanelBorderRadius card = new PanelBorderRadius();
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(CardColor);
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel iconWrapper = new JPanel(new GridBagLayout());
        iconWrapper.setPreferredSize(new Dimension(70, 70));
        iconWrapper.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));

        JLabel iconLabel = new JLabel();
        ImageIcon ic = getIcon(icon, 40, 40);
        if (ic != null)
            iconLabel.setIcon(ic);
        iconWrapper.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CardColor);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(new Color(33, 33, 33));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(117, 117, 117));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        textPanel.add(valueLabel);
        textPanel.add(titleLabel);

        card.add(iconWrapper, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(248, 249, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(CardColor);
            }
        });

        return card;
    }

    // ==================== REVENUE CHART ====================
    private JPanel createRevenueChart() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBackground(CardColor);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Doanh thu 7 ngày gần nhất");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setIcon(getIcon("graph.png", 24, 24));

        JLabel period = new JLabel("");
        period.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        period.setForeground(new Color(100, 100, 100));

        header.add(title, BorderLayout.WEST);
        header.add(period, BorderLayout.EAST);

        // Danh sách 7 ngày
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setBorder(new EmptyBorder(10, 0, 15, 10));

        ArrayList<ThongKeTungNgayTrongThangDTO> data = thongKeBUS.getThongKe7NgayGanNhat();

        // Cập nhật khoảng ngày
        if (!data.isEmpty()) {
            String from = new SimpleDateFormat("dd/MM").format(data.get(0).getNgay());
            String to = new SimpleDateFormat("dd/MM").format(data.get(data.size() - 1).getNgay());
            period.setText("Từ " + from + " → " + to);
        }

        for (ThongKeTungNgayTrongThangDTO item : data) {
            list.add(createSimpleRevenueRow(
                    new SimpleDateFormat("EEE").format(item.getNgay()),
                    new SimpleDateFormat("dd/MM").format(item.getNgay()),
                    item.getDoanhthu()));
            list.add(Box.createVerticalStrut(12));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        // DÒNG QUAN TRỌNG – TĂNG TỐC ĐỘ CUỘN SIÊU MƯỢT
        scroll.getVerticalScrollBar().setUnitIncrement(30);  
        scroll.getVerticalScrollBar().setBlockIncrement(80);

        // Ẩn thanh cuộn khi không cần thiết (vẫn cuộn được bằng chuột giữa)
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // HÀNG DOANH THU SIÊU TỐI GIẢN – CHỈ HIỆN GIÁ TIỀN
    private JPanel createSimpleRevenueRow(String weekday, String date, long revenue) {
        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 10, 6, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        // Bên trái: Thứ + ngày
        String leftHtml = "<html>"
                + "<div style='text-align:left; line-height:1.4;'>"
                + "<span style='font-size:14px; font-weight:bold; color:#2c3e50;'>" + weekday + "</span><br>"
                + "<span style='font-size:12px; color:#95a5a6;'>" + date + "</span>"
                + "</div></html>";

        JLabel leftLabel = new JLabel(leftHtml);

        // Bên phải: Giá tiền
        JLabel moneyLabel = new JLabel(revenue > 0 ? Formater.FormatVND(revenue) : "0đ");
        moneyLabel.setFont(new Font("Segoe UI", revenue > 0 ? Font.BOLD : Font.PLAIN, revenue > 0 ? 17 : 15));
        moneyLabel.setForeground(revenue > 0 ? new Color(46, 159, 83) : new Color(160, 160, 160));
        moneyLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Hover nhẹ (tô nền nhạt khi di chuột)
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                row.setBackground(new Color(248, 252, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                row.setBackground(new Color(0, 0, 0, 0));
            }
        });

        row.add(leftLabel, BorderLayout.WEST);
        row.add(moneyLabel, BorderLayout.EAST);

        return row;
    }

    private JPanel createManagerQuickActions() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBackground(CardColor);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleWrapper.setBackground(CardColor);

        JLabel iconLabel = new JLabel();
        ImageIcon quickIcon = getIcon("lightning.png", 20, 20);
        if (quickIcon != null)
            iconLabel.setIcon(quickIcon);

        JLabel titleLabel = new JLabel("Truy cập nhanh");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 33, 33));

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        panel.add(titleWrapper, BorderLayout.NORTH);

        JPanel actionsGrid = new JPanel(new GridLayout(3, 2, 12, 12));
        actionsGrid.setBackground(CardColor);

        actionsGrid.add(createQuickActionButton("Thống kê", "graph.png", new Color(191, 225, 255), "ThongKe"));
        actionsGrid.add(createQuickActionButton("Phiếu xuất", "export.png", new Color(198, 240, 198), "PhieuXuat"));
        actionsGrid.add(createQuickActionButton("Sản phẩm", "product.png", new Color(255, 250, 194), "SanPham"));
        actionsGrid.add(createQuickActionButton("Bảo hành", "guarantee.png", new Color(255, 210, 210), "PhieuBaoHanh"));
        actionsGrid.add(createQuickActionButton("Nhân viên", "staff.png", new Color(220, 200, 255), "NhanVien"));
        actionsGrid.add(createQuickActionButton("Phân quyền", "decentralized.png", new Color(255, 225, 195), "PhanQuyen"));

        panel.add(actionsGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRecentActivities() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBackground(CardColor);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setPreferredSize(new Dimension(0, 220));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CardColor);

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleWrapper.setBackground(CardColor);

        JLabel iconLabel = new JLabel();
        ImageIcon notiIcon = getIcon("notification.png", 20, 20);
        if (notiIcon != null)
            iconLabel.setIcon(notiIcon);

        JLabel titleLabel = new JLabel("Hoạt động gần đây");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 33, 33));

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        headerPanel.add(titleWrapper, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel activitiesList = new JPanel();
        activitiesList.setLayout(new BoxLayout(activitiesList, BoxLayout.Y_AXIS));
        activitiesList.setBackground(CardColor);

        List<Object[]> events = new ArrayList<>();
        for (PhieuXuatDTO hd : hoaDonBUS.getAll()) {
            if (hd.getTG() != null)
                events.add(new Object[] { new Date(hd.getTG().getTime()), "Hóa đơn #" + hd.getMP() + " được tạo",
                        "shopping-cart.png", SuccessColor });
        }
        for (PhieuNhapDTO pn : phieuNhapBUS.getAll()) {
            if (pn.getTG() != null)
                events.add(new Object[] { new Date(pn.getTG().getTime()), "Phiếu nhập #" + pn.getMP() + " được tạo",
                        "import.png", PrimaryColor });
        }
        for (KhachHangDTO kh : khachHangBUS.getAll()) {
            if (kh.getNGAYTHAMGIA() != null)
                events.add(new Object[] { kh.getNGAYTHAMGIA(), "Khách hàng mới: " + kh.getHOTEN(), "client.png",
                        WarningColor });
        }
        events.sort((a, b) -> ((Date) b[0]).compareTo((Date) a[0]));

        int count = 0;
        for (Object[] ev : events) {
            if (count >= 5)
                break;
            String rel = relativeTime((Date) ev[0]);
            activitiesList.add(createActivityItem((String) ev[1], rel, (String) ev[2], (Color) ev[3]));
            count++;
        }

        JScrollPane scrollPane = new JScrollPane(activitiesList);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CardColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActivityItem(String activity, String time, String icon, Color accentColor) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setBackground(CardColor);
        item.setBorder(new EmptyBorder(12, 5, 12, 5));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel iconWrapper = new JPanel(new GridBagLayout());
        iconWrapper.setPreferredSize(new Dimension(40, 40));
        iconWrapper.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));

        JLabel iconLabel = new JLabel();
        ImageIcon it = getIcon(icon, 20, 20);
        if (it != null) {
            iconLabel.setIcon(it);
        } else {
            iconLabel.setText("•");
            iconLabel.setForeground(accentColor);
        }
        iconWrapper.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CardColor);

        JLabel activityLabel = new JLabel(activity);
        activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activityLabel.setForeground(new Color(33, 33, 33));
        activityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(new Color(158, 158, 158));
        timeLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(activityLabel);
        textPanel.add(timeLabel);

        item.add(iconWrapper, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);

        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(248, 249, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(CardColor);
            }
        });

        return item;
    }

    private JButton createQuickActionButton(String text, String icon, Color bgColor, String panelName) {
        JButton btn = new JButton(text);

        ImageIcon ic = getIcon(icon, 48, 48);
        if (ic != null)
            btn.setIcon(ic);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(getBestTextColor(bgColor)); // màu chữ tự động đẹp
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 100));
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setIconTextGap(12);

        // Xử lý click để chuyển panel
        btn.addActionListener(e -> {
            if (mainFrame != null) {
                navigateToPanel(panelName);
            }
        });

        // HOVER SIÊU ĐẸP — đây là phần quan trọng nhất!
        Color originalBg = bgColor;
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Cách 1: Làm sáng nhẹ + thêm chút độ bão hòa (đẹp nhất cho pastel)
                float[] hsb = Color.RGBtoHSB(
                        originalBg.getRed(),
                        originalBg.getGreen(),
                        originalBg.getBlue(), null);
                Color hoverColor = Color.getHSBColor(hsb[0], Math.min(0.35f, hsb[1] + 0.15f),
                        Math.min(1.0f, hsb[2] + 0.12f));

                btn.setBackground(hoverColor);
                btn.setForeground(Color.WHITE); // hover thì chữ trắng luôn, cực sang
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalBg);
                btn.setForeground(getBestTextColor(originalBg));
            }
        });

        return btn;
    }

    private Color getBestTextColor(Color bg) {
        // Tự động chọn màu chữ đẹp nhất cho nền pastel nhạt
        int r = bg.getRed();
        int g = bg.getGreen();
        int b = bg.getBlue();

        // Nếu nền quá nhạt (như pastel), dùng màu đậm nhẹ để nổi
        if (r > 230 && g > 230 && b > 230)
            return new Color(80, 80, 80); // xám đậm nhẹ

        if (r > g && r > b)
            return new Color(150, 80, 60); // nền đỏ/hồng/cam → nâu đỏ
        if (g > r && g > b)
            return new Color(60, 120, 80); // nền xanh lá → xanh đậm
        if (b > r && b > g)
            return new Color(60, 100, 180); // nền xanh dương → xanh dương đậm
        return new Color(70, 70, 100); // tím/xám → tím xám
    }

    private PanelBorderRadius createLargeActionCard(String icon, String title, String subtitle, Color color,
            String shortcut) {
        PanelBorderRadius card = new PanelBorderRadius();
        card.setLayout(new GridBagLayout());
        card.setBackground(color);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(color);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel();
        ImageIcon largeIc = getIcon(icon, 80, 80);
        if (largeIc != null)
            iconLabel.setIcon(largeIc);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0xd7, 0xa6, 0x1e));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(117, 117, 117));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(Box.createVerticalGlue());
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(15));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitleLabel);
        content.add(Box.createVerticalGlue());

        card.add(content);
        
        // Thêm sự kiện click để chuyển sang trang Bán hàng (Phiếu xuất)
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (mainFrame != null) {
                    navigateToPanel("PhieuXuat");
                }
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(248, 249, 250));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(color);
            }
        });
        
        return card;
    }

    private PanelBorderRadius createCustomerQuickAccess() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBackground(CardColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header với icon
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        headerPanel.setBackground(CardColor);
        
        JLabel iconLabel = new JLabel();
        ImageIcon customerIcon = getIcon("client.png", 20, 20);
        if (customerIcon != null)
            iconLabel.setIcon(customerIcon);
        
        JLabel titleLabel = new JLabel("Khách hàng mới gần đây");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 33, 33));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setBackground(CardColor);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        List<KhachHangDTO> khs = new ArrayList<>(khachHangBUS.getAll());
        khs.sort((a, b) -> {
            Date da = a.getNGAYTHAMGIA(), db = b.getNGAYTHAMGIA();
            if (da == null && db == null)
                return 0;
            if (da == null)
                return 1;
            if (db == null)
                return -1;
            return db.compareTo(da);
        });

        int limit = Math.min(5, khs.size());
        for (int i = 0; i < limit; i++) {
            KhachHangDTO kh = khs.get(i);
            list.add(createCustomerItem(kh));
            if (i < limit - 1) {
                list.add(Box.createVerticalStrut(8));
            }
        }
        
        if (limit == 0) {
            JLabel empty = new JLabel("Chưa có khách hàng mới");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            empty.setForeground(new Color(158, 158, 158));
            empty.setBorder(new EmptyBorder(5, 0, 0, 0));
            list.add(empty);
        }

        panel.add(list, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createCustomerItem(KhachHangDTO kh) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(CardColor);
        item.setBorder(new EmptyBorder(8, 10, 8, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Icon người dùng
        JLabel userIcon = new JLabel();
        ImageIcon clientIcon = getIcon("client1.png", 18, 18);
        if (clientIcon != null) {
            userIcon.setIcon(clientIcon);
        } else {
            userIcon.setText("👤");
            userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        
        // Thông tin khách hàng
        JPanel infoPanel = new JPanel(new BorderLayout(8, 0));
        infoPanel.setBackground(CardColor);
        
        String dateStr = kh.getNGAYTHAMGIA() != null 
            ? new SimpleDateFormat("dd/MM").format(kh.getNGAYTHAMGIA())
            : "";
        
        JLabel nameLabel = new JLabel(kh.getHOTEN() + (dateStr.isEmpty() ? "" : " (" + dateStr + ")"));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(new Color(33, 33, 33));
        
        infoPanel.add(nameLabel, BorderLayout.WEST);
        
        item.add(userIcon, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);
        
        // Hover effect
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(248, 249, 250));
                infoPanel.setBackground(new Color(248, 249, 250));
                item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(CardColor);
                infoPanel.setBackground(CardColor);
                item.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return item;
    }

    private PanelBorderRadius createPromotionPanel() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleWrapper.setBackground(new Color(255, 255, 255));

        JLabel iconLabel = new JLabel();
        ImageIcon giftIcon = getIcon("gift.png", 20, 20);
        if (giftIcon != null)
            iconLabel.setIcon(giftIcon);

        JLabel titleLabel = new JLabel("Khuyến mãi hôm nay");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(WarningColor);

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        panel.add(titleWrapper, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(255, 255, 255));

        Date now = new Date();
        int shown = 0;
        for (MaKhuyenMaiDTO m : maKhuyenMaiBUS.getAll()) {
            Date start = m.getTGBD() != null ? new Date(m.getTGBD().getTime()) : null;
            Date end = m.getTGKT() != null ? new Date(m.getTGKT().getTime()) : null;
            boolean active = (start == null || !start.after(now)) && (end == null || !end.before(now));
            if (active) {
                JLabel line = new JLabel("• Mã " + m.getMKM() + " (" +
                        (start != null ? new SimpleDateFormat("dd/MM").format(start) : "?") + " - " +
                        (end != null ? new SimpleDateFormat("dd/MM").format(end) : "?") + ")");
                line.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                line.setBorder(new EmptyBorder(2, 0, 2, 0));
                content.add(line);
                shown++;
            }
        }
        if (shown == 0) {
            JLabel noPromo = new JLabel("Hôm nay chưa có khuyến mãi hoạt động");
            noPromo.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            noPromo.setForeground(new Color(120, 120, 120));
            content.add(noPromo);
        }

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private PanelBorderRadius createPersonalStats() {
        PanelBorderRadius panel = new PanelBorderRadius();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(CardColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Thống kê của tôi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel statsGrid = new JPanel(new GridLayout(3, 1, 0, 15));
        statsGrid.setBackground(CardColor);

        Date today = new Date();
        int ordersToday = 0;
        long revenueToday = 0;
        for (PhieuXuatDTO hd : hoaDonBUS.getAll()) {
            if (hd.getTG() != null && isSameDay(today, new Date(hd.getTG().getTime()))) {
                ordersToday++;
                revenueToday += hd.getTIEN();
            }
        }
        int newCustomers = 0;
        for (KhachHangDTO kh : khachHangBUS.getAll()) {
            if (kh.getNGAYTHAMGIA() != null && isSameDay(today, kh.getNGAYTHAMGIA()))
                newCustomers++;
        }

        statsGrid.add(createMiniStat("Đơn hôm nay", String.valueOf(ordersToday), PrimaryColor));
        statsGrid.add(createMiniStat("Doanh số", Formater.FormatVND(revenueToday), SuccessColor));
        statsGrid.add(createMiniStat("Khách hàng mới", String.valueOf(newCustomers), WarningColor));

        panel.add(statsGrid, BorderLayout.CENTER);
        return panel;
    }



    private JPanel createMiniStat(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(CardColor);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelText.setForeground(new Color(117, 117, 117));

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueText.setForeground(color);

        panel.add(labelText, BorderLayout.NORTH);
        panel.add(valueText, BorderLayout.CENTER);

        return panel;
    }

    private boolean isSameDay(Date a, Date b) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        return f.format(a).equals(f.format(b));
    }

    private String relativeTime(Date when) {
        long diffMs = Math.abs(System.currentTimeMillis() - when.getTime());
        long minutes = diffMs / (60 * 1000);
        long hours = minutes / 60;
        long days = hours / 24;
        if (minutes < 60)
            return minutes <= 1 ? "1 phút trước" : minutes + " phút trước";
        if (hours < 24)
            return hours == 1 ? "1 giờ trước" : hours + " giờ trước";
        if (days == 1)
            return "Hôm qua";
        return days + " ngày trước";
    }

    private void navigateToPanel(String panelName) {
        if (mainFrame == null) return;
        
        try {
            // Tìm MenuTaskbar từ Main frame
            GUI.Component.MenuTaskbar menuTaskbar = null;
            for (java.awt.Component comp : mainFrame.getContentPane().getComponents()) {
                if (comp instanceof GUI.Component.MenuTaskbar) {
                    menuTaskbar = (GUI.Component.MenuTaskbar) comp;
                    break;
                }
            }
            
            if (menuTaskbar == null) return;
            
            // Map tên panel sang index trong menu
            int menuIndex = switch (panelName) {
                case "ThongKe" -> 14;      // Thống kê
                case "PhieuXuat" -> 8;     // Phiếu xuất
                case "SanPham" -> 1;       // Sản phẩm
                case "PhieuBaoHanh" -> 10; // Bảo hành
                case "NhanVien" -> 4;      // Nhân viên
                case "PhanQuyen" -> 12;    // Phân quyền
                default -> -1;
            };
            
            if (menuIndex >= 0) {
                // Kiểm tra xem menu item có bị ẩn do phân quyền không
                if (menuIndex < menuTaskbar.listitem.size()) {
                    GUI.Component.itemTaskbar item = menuTaskbar.listitem.get(menuIndex);
                    if (!item.isVisible()) {
                        javax.swing.JOptionPane.showMessageDialog(this, 
                            "Bạn không có quyền truy cập chức năng này!", 
                            "Không có quyền", 
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                menuTaskbar.navigateToMenuItem(menuIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Không thể mở panel này: " + e.getMessage(), 
                "Lỗi", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
