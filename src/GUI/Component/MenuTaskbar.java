package GUI.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import DAO.ChiTietQuyenDAO;
import DAO.NhanVienDAO;
import DAO.NhomQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.NhanVienDTO;
import DTO.NhomQuyenDTO;
import DTO.TaiKhoanDTO;
import GUI.Main;
import GUI.login_page;
import GUI.Dialog.MyAccount;

import GUI.Panel.ChucVu;
import GUI.Panel.KhachHang;
import GUI.Panel.MaKhuyenMai;
import GUI.Panel.NhaCungCap;
import GUI.Panel.NhanVien;
import GUI.Panel.PhanQuyen;
import GUI.Panel.PhieuNhap;
import GUI.Panel.PhieuXuat;
import GUI.Panel.SanPham;

import GUI.Panel.TaiKhoan;
import GUI.Panel.TongQuan;

import GUI.Panel.ThongKe.ThongKe;

public class MenuTaskbar extends JPanel {

    TongQuan tongQuan;
    SanPham sanPham;
   
    MaKhuyenMai maKhuyenMai;
    NhanVien nhanVien;
    ChucVu chucVu;
    KhachHang khachHang;
    NhaCungCap nhacungcap;
    PhieuNhap phieuNhap;
    PhieuXuat phieuXuat;
 
    PhanQuyen phanQuyen;
    TaiKhoan taiKhoan;
    ThongKe thongKe;

    String[][] getSt = {
            { "Tổng quan", "Dashboard.svg", "tongQuan" },
            { "Sản phẩm", "Watch.svg", "sanpham" },
          
            { "Mã khuyến mãi", "sale.svg", "makhuyenmai" },
            { "Nhân viên", "staff.svg", "nhanvien" },
            { "Chức vụ", "position.svg", "chucvu" },
            { "Khách hàng", "customer.svg", "khachhang" },
            { "Nhà cung cấp", "supplier.svg", "nhacungcap" },
            { "Phiếu xuất", "export.svg", "phieuxuat" },
            { "Phiếu nhập", "import.svg", "phieunhap" },
          
          
            { "Phân quyền", "protect.svg", "nhomquyen" },
            { "Tài khoản", "account.svg", "taikhoan" },
            { "Thống kê", "statistical.svg", "thongke" },
            { "Đăng xuất", "logout.svg", "dangxuat" },
    };

    Main main;
    TaiKhoanDTO user;
    String mcn;
    int mnv;
    public ArrayList<itemTaskbar> listitem;
    ArrayList<Integer> check = new ArrayList<>();

    JLabel lblTenNhomQuyen, lblUsername;
    JScrollPane scrollPane;

    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;

    // BẢNG MÀU MỚI - TRẮNG SẠCH + XÁM ĐẬM + VÀNG KIM (PHONG CÁCH ĐỒNG HỒ CAO CẤP)
    Color DefaultColor          = new Color(255, 255, 255);   // Nền trắng tinh
    Color FontColor             = new Color(55, 65, 81);      // Xám đậm (Gray-700) - chữ thường
    Color HoverBackgroundColor  = new Color(51, 65, 85);      // Xám nhạt hơn (Gray-700) - nền khi hover
    Color HoverFontColor        = new Color(251, 191, 36);    // Vàng kim Amber-400 - chữ khi chọn
    Color SelectedBorderColor   = new Color(251, 191, 36);    // Viền vàng kim khi chọn
    Color BorderLineColor       = new Color(229, 231, 235);   // Xám nhạt (Gray-300) - đường viền phân cách

    private ArrayList<ChiTietQuyenDTO> listQuyen;
    NhomQuyenDTO nhomQuyenDTO;
    public NhanVienDTO nhanVienDTO;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

    public MenuTaskbar(Main main) {
        this.main = main;
        initComponent();
    }

    public MenuTaskbar(Main main, TaiKhoanDTO tk,String mcn) {
        this.main = main;
        this.user = tk;
        this.mcn=mcn;
        this.mnv=user.getMNV();
    
        this.nhomQuyenDTO = NhomQuyenDAO.getInstance().selectById(Integer.toString(tk.getMNQ()));
        
        // Try to load employee record from central DB first (to get MCN). Fall back to local if unavailable.
        NhanVienDTO centralNv = NhanVienDAO.getInstance().selectByIdFromCentral(Integer.toString(tk.getMNV()));
        if (centralNv != null) {
            this.nhanVienDTO = centralNv;
            System.out.println("✅ LOGIN (central): MNV=" + tk.getMNV() + " | MCN=" + centralNv.getMCN());
        } else {
            this.nhanVienDTO = NhanVienDAO.getInstance().selectById(Integer.toString(tk.getMNV()));
            System.out.println("✅ LOGIN (local): MNV=" + tk.getMNV() + " | nhanVienDTO=" + (nhanVienDTO != null ? "FOUND" : "NOT FOUND"));
        }
        
        listQuyen = ChiTietQuyenDAO.getInstance().selectAll(Integer.toString(tk.getMNQ()));
        initComponent();
    }

    private void initComponent() {
        listitem = new ArrayList<>();
        this.setOpaque(true);
        this.setBackground(DefaultColor);
        this.setLayout(new BorderLayout(0, 0));

        // === PHẦN TOP - Thông tin người dùng ===
        pnlTop = new JPanel(new BorderLayout());
        pnlTop.setPreferredSize(new Dimension(250, 90));
        pnlTop.setBackground(DefaultColor);
        this.add(pnlTop, BorderLayout.NORTH);

        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnlTop.add(info, BorderLayout.CENTER);

        in4(info);

        bar1 = new JPanel();
        bar1.setBackground(BorderLineColor);
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

        bar2 = new JPanel();
        bar2.setBackground(BorderLineColor);
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        // === PHẦN CENTER - Menu chính ===
        pnlCenter = new JPanel();
        pnlCenter.setBackground(DefaultColor);
        pnlCenter.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 0, 5, 0));
        this.add(scrollPane, BorderLayout.CENTER);

        // === PHẦN BOTTOM - Đăng xuất ===
        pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setPreferredSize(new Dimension(250, 55));
        pnlBottom.setBackground(DefaultColor);
        pnlBottom.setBorder(new EmptyBorder(5, 10, 5, 10));
        this.add(pnlBottom, BorderLayout.SOUTH);

        bar4 = new JPanel();
        bar4.setBackground(BorderLineColor);
        bar4.setPreferredSize(new Dimension(1, 1));
        pnlBottom.add(bar4, BorderLayout.EAST);

        bar3 = new JPanel();
        bar3.setBackground(BorderLineColor);
        bar3.setPreferredSize(new Dimension(1, 1));
        this.add(bar3, BorderLayout.EAST);

        // Tạo các item menu
        for (int i = 0; i < getSt.length; i++) {
            itemTaskbar item = new itemTaskbar(getSt[i][1], getSt[i][0]);
            // setIconSize(int,int) was removed because itemTaskbar does not define it;
            // rely on itemTaskbar's default icon sizing or add a sizing method to that class if needed.
            item.setForeground(FontColor);
            listitem.add(item);

            if (i + 1 == getSt.length) {
                pnlBottom.add(item, BorderLayout.CENTER);
            } else {
                if (i != 0 && !checkRole(getSt[i][2])) {
                    item.setVisible(false);
                    check.add(i);
                }
                pnlCenter.add(item);
            }
        }

        // Item đầu tiên được chọn mặc định
        listitem.get(0).isSelected = true;
        listitem.get(0).setBackground(HoverBackgroundColor);
        listitem.get(0).setForeground(Color.WHITE);
        listitem.get(0).setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, SelectedBorderColor));

        // Sự kiện chung cho tất cả item
        for (int i = 0; i < listitem.size(); i++) {
            final int index = i;
            listitem.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    selectItem(index);
                    handleMenuClick(index);
                }
            });
        }

        // Tự động tính chiều cao pnlCenter
        int itemHeight = 50;
        int gap = 5; // khoảng cách giữa các item
        int visibleItems = listitem.size() - check.size() - 1;
        pnlCenter.setPreferredSize(new Dimension(230, visibleItems * itemHeight + (visibleItems - 1) * gap + 20));
    }

    private void selectItem(int selectedIndex) {
        for (int i = 0; i < listitem.size(); i++) {
            itemTaskbar item = listitem.get(i);
            if (i == selectedIndex) {
                item.isSelected = true;
                item.setBackground(HoverBackgroundColor);
                item.setForeground(Color.WHITE);
                item.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, SelectedBorderColor));
            } else {
                item.isSelected = false;
                item.setBackground(DefaultColor);
                item.setForeground(FontColor);
                item.setBorder(null);
            }
        }
    }

    private void handleMenuClick(int index) {
        switch (index) {
            case 0 -> main.setPanel(getTongQuanPanel());
            case 1 -> main.setPanel(getSanPhamPanel());
            case 2 -> main.setPanel(getMaKhuyenMaiPanel());
            case 3 -> main.setPanel(getNhanVienPanel());
            case 4 -> main.setPanel(getChucVuPanel());
            case 5 -> main.setPanel(getKhachHangPanel());
            case 6 -> main.setPanel(getNhaCungCapPanel());
            case 7 -> main.setPanel(getPhieuXuatPanel());
            case 8 -> main.setPanel(getPhieuNhapPanel());
            case 9 -> main.setPanel(getPhanQuyenPanel());
            case 10 -> main.setPanel(getTaiKhoanPanel());
            case 11 -> main.setPanel(getThongKePanel());
         
            case 12 -> {
    int confirm = JOptionPane.showConfirmDialog(null, 
        "Bạn muốn đăng xuất?", "Đăng xuất", 
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

    if (confirm == JOptionPane.OK_OPTION) {
        // 🔥 Close tất cả database connections trước khi logout
        config.JDBCUtil.shutdownPools();
        System.out.println("✅ Đã close tất cả database connections");
        
        main.dispose();
        new login_page().setVisible(true);
    }
}
        }
    }

    private TongQuan getTongQuanPanel() {
        if (tongQuan == null) {
            logPageLoadStart("TONG QUAN");
            tongQuan = new TongQuan(user);
            logPageLoadDone("TONG QUAN");
        }
        return tongQuan;
    }

    private SanPham getSanPhamPanel() {
        if (sanPham == null) {
            logPageLoadStart("SAN PHAM");
            sanPham = new SanPham(main, nhanVienDTO);
            logPageLoadDone("SAN PHAM");
        }
        return sanPham;
    }

    private MaKhuyenMai getMaKhuyenMaiPanel() {
        if (maKhuyenMai == null) {
            logPageLoadStart("MA KHUYEN MAI");
            maKhuyenMai = new MaKhuyenMai(main, nhanVienDTO);
            logPageLoadDone("MA KHUYEN MAI");
        }
        return maKhuyenMai;
    }

    private NhanVien getNhanVienPanel() {
        if (nhanVien == null) {
            logPageLoadStart("NHAN VIEN");
            nhanVien = new NhanVien(main, nhanVienDTO);
            logPageLoadDone("NHAN VIEN");
        }
        return nhanVien;
    }

    private ChucVu getChucVuPanel() {
        if (chucVu == null) {
            logPageLoadStart("CHUC VU");
            chucVu = new ChucVu(main);
            logPageLoadDone("CHUC VU");
        }
        return chucVu;
    }

    private KhachHang getKhachHangPanel() {
        if (khachHang == null) {
            logPageLoadStart("KHACH HANG");
            khachHang = new KhachHang(main);
            logPageLoadDone("KHACH HANG");
        }
        return khachHang;
    }

    private NhaCungCap getNhaCungCapPanel() {
        if (nhacungcap == null) {
            logPageLoadStart("NHA CUNG CAP");
            nhacungcap = new NhaCungCap(main);
            logPageLoadDone("NHA CUNG CAP");
        }
        return nhacungcap;
    }

    private PhieuXuat getPhieuXuatPanel() {
        if (phieuXuat == null) {
            logPageLoadStart("PHIEU XUAT");
            phieuXuat = new PhieuXuat(main, nhanVienDTO);
            logPageLoadDone("PHIEU XUAT");
        }
        return phieuXuat;
    }

    private PhieuNhap getPhieuNhapPanel() {
        if (phieuNhap == null) {
            logPageLoadStart("PHIEU NHAP");
            phieuNhap = new PhieuNhap(main, nhanVienDTO);
            logPageLoadDone("PHIEU NHAP");
        }
        return phieuNhap;
    }

    private PhanQuyen getPhanQuyenPanel() {
        if (phanQuyen == null) {
            logPageLoadStart("PHAN QUYEN");
            phanQuyen = new PhanQuyen(main);
            logPageLoadDone("PHAN QUYEN");
        }
        return phanQuyen;
    }

    private TaiKhoan getTaiKhoanPanel() {
        if (taiKhoan == null) {
            logPageLoadStart("TAI KHOAN");
            taiKhoan = new TaiKhoan(main);
            logPageLoadDone("TAI KHOAN");
        }
        return taiKhoan;
    }

    private ThongKe getThongKePanel() {
        if (thongKe == null) {
            logPageLoadStart("THONG KE");
            thongKe = new ThongKe();  // 🔥 No parameters
            logPageLoadDone("THONG KE");
        }
        return thongKe;
    }

    private void logPageLoadStart(String pageName) {
        System.out.println("[MENU] Dang load trang: " + pageName + "...");
    }

    private void logPageLoadDone(String pageName) {
        System.out.println("[MENU] Hoan thanh load trang: " + pageName);
    }

  public boolean checkRole(String machucnang) {
    return true;
}


  public void in4(JPanel info) {
    // Check null nhanVienDTO
    if (nhanVienDTO == null) {
        JLabel lblError = new JLabel("Lỗi: Không tìm thấy dữ liệu nhân viên");
        info.add(lblError, BorderLayout.CENTER);
        return;
    }

    JPanel pnlIcon = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pnlIcon.setPreferredSize(new Dimension(70, 80));
    pnlIcon.setOpaque(false);
    info.add(pnlIcon, BorderLayout.WEST);

    JLabel lblIcon = new JLabel();
    lblIcon.setPreferredSize(new Dimension(50, 50));

    FlatSVGIcon icon = new FlatSVGIcon(
        nhanVienDTO.getGIOITINH() == 1 ? "./icon/man_50px.svg" : "./icon/women_50px.svg"
    );
    icon.setColorFilter(new com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter(
        color -> new Color(31, 41, 59)
    ));
    lblIcon.setIcon(icon.derive(50, 50));
    pnlIcon.add(lblIcon);

    // 👉 ĐỔI SANG LAYOUT DỌC
    JPanel pnlInfo = new JPanel();
    pnlInfo.setOpaque(false);
    pnlInfo.setLayout(new javax.swing.BoxLayout(pnlInfo, javax.swing.BoxLayout.Y_AXIS));
    info.add(pnlInfo, BorderLayout.CENTER);

    // 👉 Tên
    lblUsername = new JLabel(nhanVienDTO.getHOTEN());
    lblUsername.putClientProperty("FlatLaf.style", "font: 150% $semibold.font");
    lblUsername.setForeground(new Color(31, 41, 59));
    pnlInfo.add(lblUsername);

    // 👉 Nhóm quyền
    String tenNhomQuyen = (nhomQuyenDTO != null) ? nhomQuyenDTO.getTennhomquyen() : "N/A";
    lblTenNhomQuyen = new JLabel(tenNhomQuyen);
    lblTenNhomQuyen.putClientProperty("FlatLaf.style", "font: 110% $light.font");
    lblTenNhomQuyen.setForeground(new Color(107, 114, 128));
    pnlInfo.add(lblTenNhomQuyen);

    // 👉 MÃ CHI NHÁNH (dòng mới)
    JLabel lblMCN = new JLabel("Chi nhánh: " + (mcn != null ? mcn : "NULL"));
    lblMCN.setForeground(Color.GRAY);
    pnlInfo.add(lblMCN);

    // Click avatar
    lblIcon.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent evt) {
            new MyAccount(owner, MenuTaskbar.this, "Thông tin tài khoản", true);
        }
    });
}
    
    
    
    public void refreshUI() {
    this.removeAll();
    initComponent();
    this.revalidate();
    this.repaint();
}
}

// </DOCUMENT>