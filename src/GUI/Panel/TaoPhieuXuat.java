package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import BUS.KhachHangBUS;
import BUS.MaKhuyenMaiBUS;
import BUS.PhieuXuatBUS;
import BUS.SanPhamBUS;
import DAO.NhanVienDAO;
import DAO.TonKhoDAO;
import DTO.ChiTietMaKhuyenMaiDTO;
import DTO.ChiTietPhieuXuatDTO;
import DTO.KhachHangDTO;
import DTO.MaKhuyenMaiDTO;
import DTO.NhanVienDTO;
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;
import DTO.TaiKhoanDTO;
import GUI.Main;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.Notification;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.PanelBorderRadius;
import GUI.Component.SelectForm;
import GUI.Dialog.ListKhachHang;
import helper.Formater;

public final class TaoPhieuXuat extends JPanel {

    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this); //gọi phương thức compoment tổ tiên có kiểu window của compoment hiện tại
    // kiểu như cái listKhachHang thì cho owner dô sẽ gọi đc cái jframe của listkhachhang
    PanelBorderRadius right, left;
    JPanel contentCenter, left_top, content_btn, left_bottom; //là cái main cữ
    JTable tablePhieuXuat, tableSanPham;
    JScrollPane scrollTablePhieuNhap, scrollTableSanPham;
    DefaultTableModel tblModel, tblModelSP; //table co san 
    ButtonCustom btnTaoSp, btnAddSp, btnEditSP, btnDelete, btnNhapHang;
    InputForm txtMaphieu, txtNhanVien, txtTenSp, txtMaSp, txtMaISBN, txtSoLuongSPxuat, txtDTL, txtDTLG, txtMaGiamGia, txtGiaGiam;
    SelectForm cbxMaKM;
    JTextField txtTimKiem;
    Color BackgroundColor = new Color(248, 249, 250);

    int sum; //do ctpxuất ko có sẵn tính tiền 
    int maphieu;
    int masp;
    int manv;
    int makh = -1;
    int dtl = 0;
    String type;

    // ArrayList<SanPhamDTO> ctpb;
    SanPhamBUS spBUS = new SanPhamBUS();
    MaKhuyenMaiBUS mkmBUS = new MaKhuyenMaiBUS();
    PhieuXuatBUS phieuXuatBUS = new PhieuXuatBUS();
    TonKhoDAO tonKhoDAO = new TonKhoDAO();
    KhachHangBUS khachHangBUS = new KhachHangBUS();
    ArrayList<ChiTietPhieuXuatDTO> chitietphieu = new ArrayList<>();
    ArrayList<DTO.SanPhamDTO> listSP = spBUS.getAll();
    ArrayList<DTO.ChiTietMaKhuyenMaiDTO> listctMKM = new ArrayList<>();

    NhanVienDTO nv;
    private JLabel lbltongtien;
    private JTextField txtKh;
    private Main mainChinh;
    // private ButtonCustom btnQuayLai; //chua use
    private InputForm txtGiaXuat;

    public TaoPhieuXuat(Main mainChinh, NhanVienDTO nv, String type) {
        this.mainChinh = mainChinh;
        this.nv = nv;
        this.type = type;
        maphieu = phieuXuatBUS.getMPMAX() + 1;
        initComponent(type);
        loadDataTalbeSanPham(listSP);
    }

    private void initComponent(String type) {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Phiếu xuất
        tablePhieuXuat = new JTable();
        scrollTablePhieuNhap = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng"};
        tblModel.setColumnIdentifiers(header);
        tablePhieuXuat.setModel(tblModel);
        scrollTablePhieuNhap.setViewportView(tablePhieuXuat);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tablePhieuXuat.getColumnModel();
        for (int i = 0; i < 5; i++) {
            if (i != 2) {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        tablePhieuXuat.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablePhieuXuat.setFocusable(false);
        tablePhieuXuat.setDefaultEditor(Object.class, null);
        scrollTablePhieuNhap.setViewportView(tablePhieuXuat);

        tablePhieuXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tablePhieuXuat.getSelectedRow();
                if (index != -1) {
                    tableSanPham.setSelectionMode(index);
                    setFormChiTietPhieu(chitietphieu.get(index));
                    // ChiTietHoaDonDTO ctphieu = chitietphieu.get(index);
                    // SanPhamDTO ctspSell = spBUS.getByMaSP(ctphieu.getMSP());
                    // setInfoSanPham(ctspSell);
                    actionbtn("update");
                }
            }
        });

        // Table sản phẩm
        tableSanPham = new JTable();
        tableSanPham.setBackground(Color.WHITE);
        scrollTableSanPham = new JScrollPane();
        tblModelSP = new DefaultTableModel();
        String[] headerSP = new String[]{"Mã SP", "Tên sản phẩm", "Số lượng tồn"};
        tblModelSP.setColumnIdentifiers(headerSP);
        tableSanPham.setModel(tblModelSP);
        scrollTableSanPham.setViewportView(tableSanPham);
        tableSanPham.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableSanPham.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableSanPham.setFocusable(false);
        scrollTableSanPham.setViewportView(tableSanPham);

        tableSanPham.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tableSanPham.getSelectedRow();
                if (index != -1) {
                    resetForm();
                    setInfoSanPham(listSP.get(index));
                    if (!checkTonTai()) {
                        actionbtn("add");
                    } else {
                        actionbtn("update");
                    }
                }
            }
        });

        //content_CENTER (chứa hết tất cả left+right) 
        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(5, 5));
        this.add(contentCenter, BorderLayout.CENTER);

        //LEFT
        left = new PanelBorderRadius();
        left.setLayout(new BorderLayout(0, 5));
        left.setBackground(Color.white);

        left_top = new JPanel(); // Chứa tất cả phần ở phía trái trên cùng, chứa {content_top, content_btn}
        left_top.setLayout(new BorderLayout());
        left_top.setBorder(new EmptyBorder(5, 5, 10, 10));
        left_top.setOpaque(false);

        JPanel content_top, content_left, content_right, content_right_top; //content_top {content_left + content_right} -> trong left_top
        content_top = new JPanel(new GridLayout(1, 2, 5, 5));
        content_top.setOpaque(false);

        content_left = new JPanel(new BorderLayout(5, 5));
        content_left.setOpaque(false);
        content_left.setPreferredSize(new Dimension(0, 300));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(100, 40));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tên sản phẩm, mã sản phẩm, ...");
        txtTimKiem.putClientProperty("JTextField.showClearButton", true);
        txtTimKiem.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("./icon/search.svg"));

        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) { //chạy khi nhả phím trong tìm kiếm
                ArrayList<SanPhamDTO> rs = spBUS.search(txtTimKiem.getText(), "Tất cả");
                loadDataTalbeSanPham(rs);
            }
        });

        content_left.add(txtTimKiem, BorderLayout.NORTH);
        content_left.add(scrollTableSanPham, BorderLayout.CENTER);

        content_right = new JPanel(new BorderLayout(5, 5));
        content_right.setOpaque(false);
        // content_right.setBackground(Color.WHITE);

        content_right_top = new JPanel(new BorderLayout());
        content_right_top.setPreferredSize(new Dimension(100, 335));
        txtTenSp = new InputForm("Tên sản phẩm");
        txtTenSp.setEditable(false);
        txtTenSp.setPreferredSize(new Dimension(100, 90));
        txtMaSp = new InputForm("Mã sản phẩm");
        txtMaSp.setEditable(false);
        txtMaISBN = new InputForm("Mã vạch");
        txtMaISBN.setEditable(false);
        txtGiaXuat = new InputForm("Giá xuất");
        txtGiaXuat.setEditable(false);
        PlainDocument dongia = (PlainDocument) txtGiaXuat.getTxtForm().getDocument();
        dongia.setDocumentFilter((new NumericDocumentFilter()));   //chỉ cho nhập số
        txtSoLuongSPxuat = new InputForm("Số lượng");
        PlainDocument soluong = (PlainDocument) txtSoLuongSPxuat.getTxtForm().getDocument();
        soluong.setDocumentFilter((new NumericDocumentFilter())); //chỉ cho nhập số
        // txtMaGiamGia = new InputForm("Mã giảm giá");
        String[] maGiamGia = {"Chọn"};
        cbxMaKM = new SelectForm("Mã giảm giá", maGiamGia);
        txtGiaGiam = new InputForm("Giá giảm");
        txtGiaGiam.setText(" ");
        txtGiaGiam.setEditable(false);
        cbxMaKM.cbb.addItemListener((ItemListener) new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = cbxMaKM.cbb.getSelectedIndex();
                String maKhuyenMaiString = String.valueOf(cbxMaKM.cbb.getSelectedItem());
                if (index != 0) {
                    MaKhuyenMaiDTO mkm = mkmBUS.selectById(maKhuyenMaiString);
                    ChiTietMaKhuyenMaiDTO ctmkm = mkmBUS.selectByMKMAndMSP(mkm.getMKM(), Integer.parseInt(txtMaSp.getText()));
                    double giaxuat = Integer.parseInt(txtGiaXuat.getText());
                    double phantramgiam = (double) ctmkm.getPTG();
                    int giagiam = (int) (giaxuat * (1 - phantramgiam / 100));
                    txtGiaGiam.setText(Integer.toString(giagiam));
                }
            }

        });
        // txtMaGiamGia.getTxtForm().addKeyListener(new KeyAdapter() {
        //         @Override
        //         public void keyReleased(java.awt.event.KeyEvent evt) {
        //             ArrayList<MaKhuyenMaiDTO> rs = mkmBUS.search(txtMaGiamGia.getText());
        //     // hhhhhhhhhhhhhhhhhhhhhhhhhhhhh
        //         }
        //     });

        JPanel merge1 = new JPanel(new BorderLayout());
        merge1.setPreferredSize(new Dimension(100, 50));
        merge1.add(txtMaSp, BorderLayout.WEST);
        merge1.add(txtMaISBN, BorderLayout.CENTER);

        JPanel merge2 = new JPanel(new GridLayout(2, 2));
        merge2.setPreferredSize(new Dimension(100, 160));
        merge2.add(txtGiaXuat);
        merge2.add(txtSoLuongSPxuat);
        // merge2.add(txtMaGiamGia);
        merge2.add(cbxMaKM);
        merge2.add(txtGiaGiam);

        content_right_top.add(txtTenSp, BorderLayout.NORTH);
        content_right_top.add(merge1, BorderLayout.CENTER);
        content_right_top.add(merge2, BorderLayout.SOUTH);
        content_right.add(content_right_top, BorderLayout.NORTH);

        content_top.add(content_left);
        content_top.add(content_right);
        left_top.add(content_top, BorderLayout.CENTER);

        //content_btn  -  4 nút ở left_top (South) 
        content_btn = new JPanel();
        content_btn.setPreferredSize(new Dimension(0, 47));
        content_btn.setLayout(new GridLayout(1, 4, 5, 5));
        content_btn.setBorder(new EmptyBorder(8, 5, 0, 10));
        content_btn.setOpaque(false);
        btnTaoSp = new ButtonCustom("Tạo sản phẩm", "success", 14);
        btnAddSp = new ButtonCustom("Thêm sản phẩm", "success", 14);
        btnEditSP = new ButtonCustom("Sửa sản phẩm", "warning", 14);
        btnDelete = new ButtonCustom("Xoá sản phẩm", "danger", 14);
        btnAddSp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkInfo()) {
                    addCtPhieu();
                    //thông báo dạng popup dùng của Notification trong Compoment của Gui
                    Notification thongbaoNoi = new Notification(mainChinh, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Thêm sản phẩm thành công!");
                    thongbaoNoi.showNotification();
                    loadDataTableChiTietPhieu(chitietphieu);
                    actionbtn("update");
                }

            }

        });

        btnEditSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = tablePhieuXuat.getSelectedRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần chỉnh");
                } else if (checkInfoEdit()) {
                    chitietphieu.get(index).setSL(Integer.parseInt(txtSoLuongSPxuat.getText()));
                    if (!txtGiaGiam.getText().equals(" ")) {
                        chitietphieu.get(index).setTIEN(Integer.parseInt(txtGiaGiam.getText()));
                    } else {
                        chitietphieu.get(index).setTIEN(Integer.parseInt(txtGiaXuat.getText()));
                    }
                    Notification thongbaoNoi = new Notification(mainChinh, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Sửa sản phẩm thành công!");
                    thongbaoNoi.showNotification();
                    loadDataTableChiTietPhieu(chitietphieu);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = tablePhieuXuat.getSelectedRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xóa");
                } else {
                    chitietphieu.remove(index);
                    actionbtn("add");
                    loadDataTableChiTietPhieu(chitietphieu);
                    resetForm();
                }
            }
        });

        btnEditSP.setEnabled(false);
        btnDelete.setEnabled(false);
        content_btn.add(btnAddSp);
        content_btn.add(btnEditSP);
        content_btn.add(btnDelete);
        left_top.add(content_btn, BorderLayout.SOUTH);

        //left_bottom này là danh sách xuất ở left phía nam, chứa tablelistnhap
        left_bottom = new JPanel();
        left_bottom.setOpaque(false);
        left_bottom.setPreferredSize(new Dimension(0, 250));
        left_bottom.setBorder(new EmptyBorder(0, 5, 10, 10));
        BoxLayout boxly = new BoxLayout(left_bottom, BoxLayout.Y_AXIS);
        left_bottom.setLayout(boxly);
        left_bottom.add(scrollTablePhieuNhap);
        left.add(left_top, BorderLayout.CENTER);
        left.add(left_bottom, BorderLayout.SOUTH);

        // RIGHT 
        right = new PanelBorderRadius();
        right.setPreferredSize(new Dimension(320, 0));
        right.setBorder(new EmptyBorder(5, 5, 5, 5));
        right.setLayout(new BorderLayout());

        JPanel right_top, right_center, right_bottom, pn_tongtien;
        right_top = new JPanel(new GridLayout(3, 1, 0, 0));
        right_top.setPreferredSize(new Dimension(300, 270));
        txtMaphieu = new InputForm("Mã phiếu xuất");
        txtMaphieu.setEditable(false);
        txtNhanVien = new InputForm("Nhân viên xuất");
        txtNhanVien.setEditable(false);
        txtDTL = new InputForm("Điểm tích lũy đang có");
        txtDTL.setEditable(false);
        manv = nv.getMNV();
        txtMaphieu.setText("HD" + maphieu);
        NhanVienDTO nhanvien = NhanVienDAO.getInstance().selectById(nv.getMNV() + "");
        txtNhanVien.setText(nhanvien.getHOTEN());
        right_top.add(txtMaphieu);
        right_top.add(txtNhanVien);
        right_top.add(txtDTL);

        right_center = new JPanel(new BorderLayout());
        right_center.setOpaque(false);

        JPanel khachJPanel = new JPanel(new BorderLayout());
        khachJPanel.setPreferredSize(new Dimension(0, 40));
        khachJPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        khachJPanel.setOpaque(false);
        JPanel kJPanelLeft = new JPanel(new GridLayout(1, 1));
        kJPanelLeft.setOpaque(false);
        kJPanelLeft.setPreferredSize(new Dimension(40, 0));
        ButtonCustom btnKh = new ButtonCustom("Chọn khách hàng", "success", 14);
        kJPanelLeft.add(btnKh);
        btnKh.addActionListener((ActionEvent e) -> {
            new ListKhachHang(TaoPhieuXuat.this, owner, "Chọn khách hàng", true);
        });

        txtKh = new JTextField("");
        txtKh.setEditable(false);
        txtDTLG = new InputForm("Điểm tích lũy giảm");
        txtDTLG.setText("0");
        txtDTLG.setEditable(false);
        PlainDocument dtgpd = (PlainDocument) txtDTLG.getTxtForm().getDocument();
        dtgpd.setDocumentFilter((new NumericDocumentFilter()));
        khachJPanel.add(kJPanelLeft, BorderLayout.EAST);
        khachJPanel.add(txtKh, BorderLayout.CENTER);
        JPanel khPanel = new JPanel(new GridLayout(2, 1, 5, 0));
        khPanel.setBackground(Color.WHITE);
        khPanel.setPreferredSize(new Dimension(0, 80));
        JLabel khachKhangJLabel = new JLabel("Khách hàng");
        khachKhangJLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        khPanel.add(khachKhangJLabel);
        khPanel.add(khachJPanel);
        right_center.add(khPanel, BorderLayout.NORTH);
        right_center.add(txtDTLG, BorderLayout.SOUTH);

        right_bottom = new JPanel(new GridLayout(2, 1));
        right_bottom.setPreferredSize(new Dimension(300, 100));
        right_bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        right_bottom.setOpaque(false);

        pn_tongtien = new JPanel(new FlowLayout(1, 20, 0));
        pn_tongtien.setOpaque(false);
        JLabel lbltien = new JLabel("TỔNG TIỀN: ");
        lbltien.setFont(new Font(FlatRobotoFont.FAMILY, 1, 18));
        lbltongtien = new JLabel("0đ");
        lbltongtien.setFont(new Font(FlatRobotoFont.FAMILY, 1, 18));
        lbltien.setForeground(new Color(255, 51, 51));
        pn_tongtien.add(lbltien);
        pn_tongtien.add(lbltongtien);
        right_bottom.add(pn_tongtien);

        btnNhapHang = new ButtonCustom("Bán hàng", "excel", 14);
        btnNhapHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBtnNhapHang();
            }
        });
        right_bottom.add(btnNhapHang);

        right.add(right_top, BorderLayout.NORTH);
        right.add(right_center, BorderLayout.CENTER);
        right.add(right_bottom, BorderLayout.SOUTH);

        contentCenter.add(left, BorderLayout.CENTER);
        contentCenter.add(right, BorderLayout.EAST);
        // actionbtn("add");
    }

    public void actionbtn(String type) {
        boolean val_1 = type.equals("add");
        boolean val_2 = type.equals("update");
        btnAddSp.setEnabled(val_1);
        btnEditSP.setEnabled(val_2);
        btnDelete.setEnabled(val_2);
        content_btn.revalidate();
        content_btn.repaint();
    }

    public void resetForm() {
        this.txtTenSp.setText("");
        this.txtMaSp.setText("");
        this.txtMaISBN.setText("");
        this.txtGiaXuat.setText("");
        this.txtSoLuongSPxuat.setText("");
        // this.txtMaGiamGia.setText("");
        this.txtGiaGiam.setText(" ");
    }

    public String[] getMaGiamGiaTable(int masp) {
        listctMKM = mkmBUS.Getctmkm(masp);
        int size = listctMKM.size();
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!validateSelectDate(listctMKM.get(i))) {
                arr.add(listctMKM.get(i).getMKM());
            }
        }
        String[] tmp = new String[arr.size()];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = arr.get(i);
        }
        tmp = Stream.concat(Stream.of("Chọn"), Arrays.stream(tmp)).toArray(String[]::new);
        return tmp;
    }

    public boolean validateSelectDate(DTO.ChiTietMaKhuyenMaiDTO tmp) {
        MaKhuyenMaiDTO a = mkmBUS.selectMkm(tmp.getMKM());
        Date time_start = a.getTGBD();
        Date time_end = a.getTGKT();
        Date current_date = new Date();
        if (time_start != null && time_start.after(current_date)) {
            return false;
        }
        if (time_end != null && time_end.after(current_date)) {
            return false;
        }
        if (time_start != null && time_end != null && time_start.after(time_end)) {
            return false;
        }
        return true;
    }

    public void setInfoSanPham(SanPhamDTO sp) {
        masp = sp.getMSP();
        this.txtMaSp.setText(Integer.toString(sp.getMSP()));
        this.txtTenSp.setText(sp.getTEN());
        this.txtMaISBN.setText(sp.getMV());
        this.txtGiaXuat.setText(Integer.toString(sp.getTIENX()));
        cbxMaKM.setArr(getMaGiamGiaTable(sp.getMSP()));

    }

    public void setFormChiTietPhieu(ChiTietPhieuXuatDTO phieu) { //set info vào inputform khi nhan ben tablephieunhap
        SanPhamDTO ctsp = spBUS.getByMaSP(phieu.getMSP());
        // ChiTietMaKhuyenMaiDTO ctmkm = mkmBUS.findCT(listctMKM, ctsp.getMSP());
        this.txtMaSp.setText(Integer.toString(ctsp.getMSP()));
        this.txtTenSp.setText(spBUS.getByMaSP(ctsp.getMSP()).getTEN());
        this.txtGiaXuat.setText(Integer.toString(phieu.getTIEN()));
        this.txtSoLuongSPxuat.setText(Integer.toString(phieu.getSL()));
        cbxMaKM.setArr(getMaGiamGiaTable(ctsp.getMSP()));
    }

    public void loadDataTalbeSanPham(ArrayList<DTO.SanPhamDTO> result) {
        tblModelSP.setRowCount(0);
        for (SanPhamDTO sp : result) {
            // Lấy số lượng tồn theo chi nhánh của nhân viên
            int slTonChiNhanh = tonKhoDAO.getTonKhoByMSPAndMCN(sp.getMSP(), nv.getMCN());
            tblModelSP.addRow(new Object[]{sp.getMSP(), sp.getTEN(), slTonChiNhanh});
        }
    }

    public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuXuatDTO> ctPhieu) {
        tblModel.setRowCount(0);
        int size = ctPhieu.size();
        sum = 0;
        for (int i = 0; i < size; i++) {
            SanPhamDTO phienban = spBUS.getByMaSP(ctPhieu.get(i).getMSP());
            sum += ctPhieu.get(i).getTIEN() * ctPhieu.get(i).getSL();
            tblModel.addRow(new Object[]{
                i + 1, phienban.getMSP(), spBUS.getByMaSP(phienban.getMSP()).getTEN(),
                Formater.FormatVND(ctPhieu.get(i).getTIEN()), ctPhieu.get(i).getSL()
            });
        }
        lbltongtien.setText(Formater.FormatVND(sum));
    }

    public boolean checkTonTai() {
        ChiTietPhieuXuatDTO p = phieuXuatBUS.findCT(chitietphieu, Integer.parseInt(txtMaSp.getText()));
        //kiểm tra coi masp này có trong chitietphieu này chưa 

        return p != null;
    }

    public boolean checkInfo() {
        boolean check = true;
        int index = tableSanPham.getSelectedRow();
        if (txtMaSp.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (txtGiaXuat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Giá nhập không được để rỗng !", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (txtSoLuongSPxuat.getText().equals("") || Integer.parseInt(txtSoLuongSPxuat.getText()) > listSP.get(index).getSL()) {
            JOptionPane.showMessageDialog(null, "Số lượng không được để rỗng và không lớn hơn đang có!", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (Integer.parseInt(txtSoLuongSPxuat.getText()) == 0) {
            JOptionPane.showMessageDialog(null, "Số lượng không được bằng 0!", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        }
        return check;
    }

    public boolean checkInfoEdit() {
        boolean check = true;
        int index = tablePhieuXuat.getSelectedRow();
        int sl = 0;
        for (SanPhamDTO i : listSP) {
            if (i.getMSP() == chitietphieu.get(index).getMSP()) {
                sl = i.getSL();
            }
        }
        if (txtMaSp.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (txtGiaXuat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Giá nhập không được để rỗng !", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (txtSoLuongSPxuat.getText().equals("") || Integer.parseInt(txtSoLuongSPxuat.getText()) > sl) {
            JOptionPane.showMessageDialog(null, "Số lượng không được để rỗng và không lớn hơn đang có!", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (Integer.parseInt(txtSoLuongSPxuat.getText()) == 0) {
            JOptionPane.showMessageDialog(null, "Số lượng không được bằng 0!", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        }
        return check;
    }

    public void addCtPhieu() { // them sp vao chitietphieu
        int masp = Integer.parseInt(txtMaSp.getText());
        int giaxuat;
        String mkm = null;
        int index = cbxMaKM.cbb.getSelectedIndex();
        if (index != 0) {
            mkm = listctMKM.get(index - 1).getMKM();
        }
        if (!txtGiaGiam.getText().equals(" ")) {
            giaxuat = Integer.parseInt(txtGiaGiam.getText());
        } else {
            giaxuat = Integer.parseInt(txtGiaXuat.getText());
        }
        int soluong = Integer.parseInt(txtSoLuongSPxuat.getText());
        ChiTietPhieuXuatDTO ctphieu = new ChiTietPhieuXuatDTO(maphieu, masp, soluong, giaxuat, mkm);
        ChiTietPhieuXuatDTO p = phieuXuatBUS.findCT(chitietphieu, ctphieu.getMSP());
        if (p == null) {
            chitietphieu.add(ctphieu);
            loadDataTableChiTietPhieu(chitietphieu);
            resetForm();
        } else {
            int input = JOptionPane.showConfirmDialog(this, "Sản phẩm đã tồn tại trong phiếu !\nBạn có muốn chỉnh sửa không ?", "Sản phẩm đã tồn tại !", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                setFormChiTietPhieu(ctphieu);
            }
        }
    }

    public void eventBtnNhapHang() {
        String maKhString = String.valueOf(makh);
        if (chitietphieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong phiếu!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if (Integer.parseInt(txtDTLG.getText()) > dtl || Integer.parseInt(txtDTLG.getText()) > sum) {
            JOptionPane.showMessageDialog(null, "Điểm tích lũy không lớn hơn điểm đang có và giá đang bán!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else {
            int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn tạo phiếu xuất !", "Xác nhận tạo phiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                if (!phieuXuatBUS.checkSLPx(chitietphieu, nv.getMCN())) {
                    JOptionPane.showMessageDialog(null, "Không đủ số lượng để tạo phiếu!");
                } else {
                    // Prefer the already-passed NhanVienDTO (should contain MCN loaded at login).
                    String mcn = (nv != null) ? nv.getMCN() : null;
                    if (mcn == null) {
                        // Fallback: try loading from central DB to retrieve MCN
                        try {
                            NhanVienDTO central = NhanVienDAO.getInstance().selectByIdFromCentral(String.valueOf(nv.getMNV()));
                            if (central != null) mcn = central.getMCN();
                        } catch (Exception ex) {
                            mcn = null;
                        }
                    }
                    if (makh == -1 || makh == 1) {
                        long now = System.currentTimeMillis();
                        Timestamp currenTime = new Timestamp(now);
                        makh = 1;
                        PhieuXuatDTO phieuXuat = new PhieuXuatDTO(makh, maphieu, nv.getMNV(), currenTime, (long)(sum - Integer.parseInt(txtDTLG.getText())), 1, Integer.parseInt(txtDTLG.getText()), mcn);
                        phieuXuatBUS.insert(phieuXuat, chitietphieu, mcn);
                        JOptionPane.showMessageDialog(null, "Xuất hàng thành công !");
                        mainChinh.setPanel(new PhieuXuat(mainChinh, nv));
                    } else {
                        long now = System.currentTimeMillis();
                        Timestamp currenTime = new Timestamp(now);
                        PhieuXuatDTO phieuXuat = new PhieuXuatDTO(makh, maphieu, nv.getMNV(), currenTime, (long)(sum - Integer.parseInt(txtDTLG.getText())), 1, Integer.parseInt(txtDTLG.getText()), mcn);
                        phieuXuatBUS.insert(phieuXuat, chitietphieu, mcn);
                        JOptionPane.showMessageDialog(null, "Xuất hàng thành công !");
                        dtl = khachHangBUS.selectById(maKhString).getDIEMTICHLUY();
                        khachHangBUS.update(makh, dtl + (int) Math.ceil(sum / 100.0) - Integer.parseInt(txtDTLG.getText()));
                        mainChinh.setPanel(new PhieuXuat(mainChinh, nv));
                    }

                }
            }
        }
    }

    public void setKhachHang(int index) {
        makh = index;
        KhachHangDTO khachhang = khachHangBUS.selectKh(makh);
        txtKh.setText(khachhang.getHOTEN());
        if (index != 1) {
            dtl = 0;
            txtDTLG.setEditable(true);
            txtDTL.setText(khachhang.getDIEMTICHLUY() + "");
            txtDTLG.setText("0");
        } else {
            dtl = 0;
            txtDTLG.setEditable(false);
            txtDTLG.setText("0");
            txtDTL.setText("");

        }
    }
}
