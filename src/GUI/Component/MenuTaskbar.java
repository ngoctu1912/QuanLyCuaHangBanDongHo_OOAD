package GUI.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import GUI.Panel.BaoHanh;
import GUI.Panel.ChucVu;
import GUI.Panel.KhachHang;
import GUI.Panel.MaKhuyenMai;
import GUI.Panel.NhaCungCap;
import GUI.Panel.NhanVien;
import GUI.Panel.PhanQuyen;
import GUI.Panel.PhieuNhap;
import GUI.Panel.PhieuXuat;
import GUI.Panel.SanPham;
import GUI.Panel.SuaChua;
import GUI.Panel.TaiKhoan;
import GUI.Panel.TongQuan;
import GUI.Panel.ViTriTrungBay;
import GUI.Panel.ThongKe.ThongKe;

public class MenuTaskbar extends JPanel {

    TongQuan tongQuan;
    SanPham sanPham;
    ViTriTrungBay viTriTrungBay;
    MaKhuyenMai maKhuyenMai;
    NhanVien nhanVien;
    ChucVu chucVu;
    KhachHang khachHang;
    NhaCungCap nhacungcap;
    PhieuNhap phieuNhap;
    PhieuXuat phieuXuat;
    BaoHanh baoHanh;
    SuaChua suaChua;
    PhanQuyen phanQuyen;
    TaiKhoan taiKhoan;
    ThongKe thongKe;

    String[][] getSt = {
            { "Tổng quan", "Dashboard.svg", "tongQuan" },
            { "Sản phẩm", "Watch.svg", "sanpham" },
            { "Vị trí trưng bày", "locationdisplay.svg", "vitritrungbay" },
            { "Mã khuyến mãi", "sale.svg", "makhuyenmai" },
            { "Nhân viên", "staff.svg", "nhanvien" },
            { "Chức vụ", "position.svg", "chucvu" },
            { "Khách hàng", "customer.svg", "khachhang" },
            { "Nhà cung cấp", "supplier.svg", "nhacungcap" },
            { "Phiếu xuất", "export.svg", "phieuxuat" },
            { "Phiếu nhập", "import.svg", "phieunhap" },
            { "Bảo hành", "guarantee.svg", "baohanh" },
            { "Sửa chữa", "repair.svg", "suachua" },
            { "Phân quyền", "protect.svg", "nhomquyen" },
            { "Tài khoản", "account.svg", "taikhoan" },
            { "Thống kê", "statistical.svg", "thongke" },
            { "Đăng xuất", "logout.svg", "dangxuat" },
    };

    Main main;
    TaiKhoanDTO user;
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

    public MenuTaskbar(Main main, TaiKhoanDTO tk) {
        this.main = main;
        this.user = tk;
        this.nhomQuyenDTO = NhomQuyenDAO.getInstance().selectById(Integer.toString(tk.getMNQ()));
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(Integer.toString(tk.getMNV()));
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
        info.setBorder(new EmptyBorder(10, 10, 10, 15));
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
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBorder(new EmptyBorder(0, 0, 0, 10));

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 0, 5, 0));
        scrollPane.getViewport().setBackground(DefaultColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
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
                // Thêm khoảng cách giữa các menu item
                if (i < getSt.length - 2) {
                    pnlCenter.add(Box.createVerticalStrut(5));
                }
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

        // Thêm glue để chặn không gian thừa
        pnlCenter.add(Box.createVerticalGlue());
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
            case 0 -> { tongQuan = new TongQuan(user, main); main.setPanel(tongQuan); }
            case 1 -> { sanPham = new SanPham(main); main.setPanel(sanPham); }
            case 2 -> { viTriTrungBay = new ViTriTrungBay(main); main.setPanel(viTriTrungBay); }
            case 3 -> { maKhuyenMai = new MaKhuyenMai(main, nhanVienDTO); main.setPanel(maKhuyenMai); }
            case 4 -> { nhanVien = new NhanVien(main); main.setPanel(nhanVien); }
            case 5 -> { chucVu = new ChucVu(main); main.setPanel(chucVu); }
            case 6 -> { khachHang = new KhachHang(main); main.setPanel(khachHang); }
            case 7 -> { nhacungcap = new NhaCungCap(main); main.setPanel(nhacungcap); }
            case 8 -> { phieuXuat = new PhieuXuat(main, user); main.setPanel(phieuXuat); }
            case 9 -> { phieuNhap = new PhieuNhap(main, nhanVienDTO); main.setPanel(phieuNhap); }
            case 10 -> { baoHanh = new BaoHanh(main); main.setPanel(baoHanh); }
            case 11 -> { suaChua = new SuaChua(main); main.setPanel(suaChua); }
            case 12 -> { phanQuyen = new PhanQuyen(main); main.setPanel(phanQuyen); }
            case 13 -> { taiKhoan = new TaiKhoan(main); main.setPanel(taiKhoan); }
            case 14 -> { thongKe = new ThongKe(); main.setPanel(thongKe); }
            case 15 -> {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Bạn muốn đăng xuất?", "Đăng xuất", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (confirm == JOptionPane.OK_OPTION) {
                    main.dispose();
                    new login_page().setVisible(true);
                }
            }
        }
    }

    public boolean checkRole(String machucnang) {
        if (listQuyen == null) return true;
        return listQuyen.stream()
                .anyMatch(q -> "view".equals(q.getHanhdong()) && machucnang.equals(q.getMachucnang()));
    }

    public void resetChange() {
        this.nhanVienDTO = new NhanVienDAO().selectById(String.valueOf(nhanVienDTO.getMNV()));
        lblUsername.setText(nhanVienDTO.getHOTEN());
    }

    // Phương thức public để điều hướng từ các panel khác
    public void navigateToMenuItem(int index) {
        if (index >= 0 && index < listitem.size()) {
            selectItem(index);
            handleMenuClick(index);
        }
    }

    public void in4(JPanel info) {
        JPanel pnlIcon = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlIcon.setPreferredSize(new Dimension(60, 70));
        pnlIcon.setOpaque(false);
        info.add(pnlIcon, BorderLayout.WEST);

        JLabel lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(50, 50));
        lblIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        FlatSVGIcon icon = new FlatSVGIcon(nhanVienDTO.getGIOITINH() == 1 ? 
                "./icon/man.svg" : "./icon/woman.svg");
        icon.setColorFilter(new com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter(color -> new Color(31, 41, 59)));
        lblIcon.setIcon(icon.derive(50, 50));
        
        pnlIcon.add(lblIcon);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 4));
        info.add(pnlInfo, BorderLayout.CENTER);

        lblUsername = new JLabel(nhanVienDTO.getHOTEN());
        lblUsername.putClientProperty("FlatLaf.style", "font: 150% $semibold.font");
        lblUsername.setForeground(new Color(31, 41, 59));
        pnlInfo.add(lblUsername);

        lblTenNhomQuyen = new JLabel(nhomQuyenDTO.getTennhomquyen());
        lblTenNhomQuyen.putClientProperty("FlatLaf.style", "font: 110% $light.font");
        lblTenNhomQuyen.setForeground(new Color(107, 114, 128));
        pnlInfo.add(lblTenNhomQuyen);

        lblIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                new MyAccount(owner, MenuTaskbar.this, "Thông tin tài khoản", true);
            }
        });
    }
}
// </DOCUMENT>