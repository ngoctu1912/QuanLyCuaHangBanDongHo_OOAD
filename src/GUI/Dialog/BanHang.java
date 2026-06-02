package GUI.Dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.sql.Timestamp;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import BUS.KhachHangBUS;
import BUS.MaKhuyenMaiBUS;
import BUS.DonViBUS;
import BUS.PhieuXuatBUS;
import BUS.SanPhamBUS;
import DAO.NhanVienDAO;
import DTO.ChiTietPhieuXuatDTO;
import DTO.ChiTietMaKhuyenMaiDTO;
import DTO.DonViDTO;
import DTO.KhachHangDTO;
import DTO.MaKhuyenMaiDTO;
import DTO.NhanVienDTO;
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;
import DTO.TaiKhoanDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.Notification;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.PanelBorderRadius;
import GUI.Component.SelectForm;
import helper.Formater;

public final class BanHang extends JFrame {
    
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    // gọi phương thức compoment tổ tiên có kiểu window của compoment hiện tại
    // kiểu như cái listKhachHang thì cho owner dô sẽ gọi đc cái jframe của
    // listkhachhang
    PanelBorderRadius right, left;
    JPanel contentCenter;
    JTable tablePhieuXuat, tableSanPham;
    JScrollPane scrollTablePhieuNhap, scrollTableSanPham;
    DefaultTableModel tblModel, tblModelSP; // table co san
    ButtonCustom btnTaoSp, btnAddSp, btnEditSP, btnDelete, btnRefresh, btnThanhToan;
    InputForm txtTenSp, txtMaSp, txtMaISBN, txtSoLuongSPxuat, txtMaGiamGia, txtGiaGiam;
    private Map<Integer, Double> giaGocMap = new HashMap<>();
    SelectForm cbxMaKM;
    JTextField txtTimKiem;
    Color BackgroundColor = new Color(193, 237, 220);

    int sum = 0, dungdiem = 0, diemtamtinh = 0; // do ctpxuất ko có sẵn tính tiền
    double giagiam = 0, khachcantra = 0, tienthua = 0, dathu = 0;

    int maphieu;
    int masp;
    // int manv;
    int makh = -1;
    int dtl = 0;
    String type;

    // ArrayList<SanPhamDTO> ctpb;
    SanPhamBUS spBUS = new SanPhamBUS();
    MaKhuyenMaiBUS mkmBUS = new MaKhuyenMaiBUS();
    PhieuXuatBUS phieuXuatBUS = new PhieuXuatBUS();
    // SanPhamBUS chiTietSanPhamBUS = new SanPhamBUS();
    KhachHangBUS khachHangBUS = new KhachHangBUS();
    ArrayList<ChiTietPhieuXuatDTO> chitietphieu = new ArrayList<>();
    ArrayList<DTO.SanPhamDTO> listSP = spBUS.getAll();
    ArrayList<DTO.ChiTietMaKhuyenMaiDTO> listctMKM = new ArrayList<>();

    TaiKhoanDTO tk;
    DonViBUS dvbus = new DonViBUS();

    public JLabel lbltongtien, lblgiamgia, lbldungdiem, lblkhachcantra, lbltienthua, lbldiemtamtinh;
    public JTextField txtKh, txtDTL, txtDTLG, txtDaThu;
    // private Main mainChinh;
    public InputForm txtGiaXuat;
    protected Frame mainChinh; // ???
    ArrayList<String> listMaKM = new ArrayList<>();

    // public BanHang(Main mainChinh, TaiKhoanDTO tk, String type) {
    public BanHang(TaiKhoanDTO tk, String type) {
        this.tk = tk;
        this.type = type;
        maphieu = phieuXuatBUS.getMPMAX() + 1;
        initComponent(type);
        loadDataTalbeSanPham(listSP);
    }

    private void initComponent(String type) {
        this.setSize(new Dimension(1400, 700));
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(BackgroundColor);
        // this.setVisible(true);
//        this.setLocationRelativeTo(null);
//        this.setTitle("POS");

        // Phiếu xuất
        tablePhieuXuat = new JTable();
        scrollTablePhieuNhap = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[] {"Mã SP", "Tên SP", "Đơn giá", "SL", "Đơn vị","Giảm","Số tiền",
                "Xóa" };
        tblModel.setColumnIdentifiers(header);
        tablePhieuXuat.setModel(tblModel);
        int[] columnWidths = { 140,360,30, 70, 70, 50, 150,50 }; // Mảng chứa kích thước các cột
        for (int i = 0; i < tablePhieuXuat.getColumnCount(); i++) {
            TableColumn column = tablePhieuXuat.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
        }
        scrollTablePhieuNhap.setViewportView(tablePhieuXuat);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tablePhieuXuat.getColumnModel();
        for (int i = 0; i < 8; i++) {
            if (i != 1) { // Ngoại trừ cột thứ 2 thì tất cả đều căn giữa
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
                int row = tablePhieuXuat.rowAtPoint(e.getPoint());
                int column = tablePhieuXuat.columnAtPoint(e.getPoint());
                if (row == -1 || column == -1) {
                    return; // Không có hàng hoặc cột nào được nhấp
                }
                // Kiểm tra nếu cột "Delete" được nhấp
                if (column == 7) {
                    tblModel.removeRow(row);
                    chitietphieu.remove(row);
                    tblModel.fireTableDataChanged(); // Cập nhật lại bảng
                    resetForm();
                    resetFormRight();
                    return;
                }
                int index = tablePhieuXuat.getSelectedRow();
                if (index >= 0 && index < chitietphieu.size()) {
                    actionbtn("update");
                    cbxMaKM.setSelectedItem(chitietphieu.get(index).getMKM());
                    setFormChiTietPhieu(chitietphieu.get(index));
                }
            }
        });
        // Table sản phẩm
        tableSanPham = new JTable();
        tableSanPham.setGridColor(Color.BLACK);
        tableSanPham.setBackground(new Color(0xA1D6E2));
        tblModelSP = new DefaultTableModel();
        String[] headerSP = new String[] { "Ảnh SP", "Tên SP", "SL","Mã sp"};
        tblModelSP.setColumnIdentifiers(headerSP);
        tableSanPham.setModel(tblModelSP);
        tableSanPham.setDefaultRenderer(Object.class, new CombinedCellRenderer());
        // kết hợp giữa ImageCellRenderer() và MultiLineCellRenderer()
        tableSanPham.setRowHeight(125);
        tableSanPham.getColumnModel().getColumn(0).setPreferredWidth(160);
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableSanPham.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableSanPham.setFocusable(false);
        tableSanPham.setDefaultEditor(Object.class, null);
        scrollTableSanPham = new JScrollPane();
        JScrollBar verticalScrollBar = scrollTableSanPham.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Giảm tốc độ cuộn xuống còn 16 pixel mỗi lần nhấn
        scrollTableSanPham.setViewportView(tableSanPham);
        tableSanPham.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            handleMousePressed(e);
        }
    });
        ///// content_CENTER (chứa hết tất cả left+right)
        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        // this.add(contentCenter, BorderLayout.CENTER); //MainChinh thì để vào center
        this.add(contentCenter);

        ///// LEFT
        left = new PanelBorderRadius();
        left.setLayout(new BorderLayout(4, 4));
        left.setBackground(Color.white);
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(60, 40));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tên sản phẩm, mã sản phẩm, ...");
        txtTimKiem.putClientProperty("JTextField.showClearButton", true);
        txtTimKiem.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("./icon/search.svg"));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) { // chạy khi nhả phím trong tìm kiếm
                ArrayList<SanPhamDTO> rs = spBUS.search(txtTimKiem.getText(), "Tất cả");
                loadDataTalbeSanPham(rs);
            }
        });

        left.add(txtTimKiem,BorderLayout.NORTH);
        left.add(scrollTableSanPham,BorderLayout.CENTER);

        ///// RIGHT
        right = new PanelBorderRadius();
        right.setPreferredSize(new Dimension(400, 0));
        right.setBorder(new EmptyBorder(10,10,10, 10));
        right.setLayout(new BorderLayout(5,5));

        JPanel right_top, right_bottom; // right_bottom=content_left+content_right;

        // right_top này là danh sách xuất ở RIGHT phía bắc, chứa tablelistnhap
        right_top = new JPanel(); // DOI THANH CONTENT_TOP BEN RIGHT
        right_top.setOpaque(false);
        right_top.setPreferredSize(new Dimension(0, 300));
        right_top.setBorder(new EmptyBorder(0, 5, 10, 10));
        BoxLayout boxly = new BoxLayout(right_top, BoxLayout.Y_AXIS);
        right_top.setLayout(boxly);
        right_top.add(scrollTablePhieuNhap);

        JPanel content_left, content_right, content_right_top, content_right_bottom;

        content_right = new JPanel(new BorderLayout());
        content_right.setPreferredSize(new Dimension(1000, 300));
        content_right.setBorder(new EmptyBorder(0, 10, 30, 5));
        content_right.setOpaque(false);

        content_left = new JPanel(new BorderLayout());
        content_left.setPreferredSize(new Dimension(1000, 300));
        content_left.setBorder(new EmptyBorder(0, 10, 30, 5));
        content_left.setOpaque(false);
        right_bottom = new JPanel(new GridLayout(1, 2, 0, 0));
        right_bottom.setPreferredSize(new Dimension(0, 300));

        txtTenSp = new InputForm("Tên sản phẩm");
        txtTenSp.setEditable(false);
        txtTenSp.setPreferredSize(new Dimension(100, 90));
        txtMaSp = new InputForm("Mã sản phẩm");
        txtMaSp.setEditable(false);
        txtMaISBN = new InputForm("Mã vạch");
        txtMaISBN.setEditable(false);
        txtGiaXuat = new InputForm("Giá xuất");
        PlainDocument dongia = (PlainDocument) txtGiaXuat.getTxtForm().getDocument();
        dongia.setDocumentFilter((new NumericDocumentFilter())); // chỉ cho nhập số

        txtSoLuongSPxuat = new InputForm("Số lượng");
        txtSoLuongSPxuat.setText(1 + "");
        PlainDocument soluong = (PlainDocument) txtSoLuongSPxuat.getTxtForm().getDocument();
        soluong.setDocumentFilter((new NumericDocumentFilter())); // chỉ cho nhập số

        String[] maGiamGia = { "Chọn" };
        cbxMaKM = new SelectForm("Mã giảm giá", maGiamGia);
        txtGiaGiam = new InputForm("Giá giảm");
        txtGiaGiam.setText(" ");
        txtGiaGiam.setEditable(false);
        cbxMaKM.cbb.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
        handleMaKMSelectionChanged(e);
    }
});
        // btnTaoSp = new ButtonCustom("Tạo sản phẩm", "success", 14);
        btnAddSp = new ButtonCustom("Thêm sản phẩm", "success", 14);
        btnEditSP = new ButtonCustom("Sửa sản phẩm", "warning", 14);
        btnDelete = new ButtonCustom("Xoá sản phẩm", "danger", 14);
        btnAddSp.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        handleAddProduct();
    }
});

       btnEditSP.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        handleEditProduct();
    }
});
        btnDelete.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        handleDeleteProduct();
    }
});

        btnEditSP.setEnabled(false);
        btnDelete.setEnabled(false);

        JPanel merge1 = new JPanel();
        merge1.setPreferredSize(new Dimension(0, 40));
        // merge1.setOpaque(false);
        merge1.add(btnAddSp);
        merge1.add(btnEditSP);
        JPanel merge2 = new JPanel(new GridLayout(1, 2));
        merge2.setPreferredSize(new Dimension(0, 80));
        txtSoLuongSPxuat.setOpaque(false);
        cbxMaKM.setOpaque(false);
        merge2.add(txtSoLuongSPxuat);
        merge2.add(cbxMaKM);
        JPanel khachJPanel = new JPanel(new BorderLayout());
        khachJPanel.setPreferredSize(new Dimension(0, 60));
        khachJPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        // khachJPanel.setOpaque(false);
        JPanel kJPanelLeft = new JPanel(new GridLayout(1, 1));
        kJPanelLeft.setPreferredSize(new Dimension(40, 0));
        ButtonCustom btnKh = new ButtonCustom("Chọn khách hàng", "success", 14);
        kJPanelLeft.add(btnKh);
        btnKh.addActionListener((ActionEvent e) -> {
            new ListKhachHang(BanHang.this, owner, "Chọn khách hàng", true);
        });

        txtKh = new JTextField("Họ và tên khách hàng");
        txtKh.setEditable(false);
        JLabel lbDTL = new JLabel("Điểm: ");
        txtDTL = new JTextField();
        // txtDTL.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtDTL.setEditable(false);
        JLabel lbDTLG = new JLabel("Dùng: ");
        txtDTLG = new JTextField();
        txtDTLG.setText("0");
        txtDTLG.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabels();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lblkhachcantra.setText("0đ");
                lbldiemtamtinh.setText("0đ");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabels();
            }

            private void updateLabels() {
                String text = txtDTLG.getText();
                lbldungdiem.setText(Formater.FormatVND(Integer.parseInt(text)));
                khachcantra = (double) (sum - giagiam - Integer.parseInt(text));
                lblkhachcantra.setText(Formater.FormatVND(khachcantra));
                diemtamtinh = (int) (khachcantra * 0.01);
                lbldiemtamtinh.setText(Formater.FormatVND(diemtamtinh));
                // loadDataMenuRight();
            }
        });

        khachJPanel.add(txtKh, BorderLayout.CENTER);
        khachJPanel.add(kJPanelLeft, BorderLayout.EAST);
        JPanel khPanel = new JPanel(new GridLayout(4, 1, 5, 0));
        // khPanel.setBackground(Color.WHITE);
        khPanel.setPreferredSize(new Dimension(0, 120));
        JLabel khachKhangJLabel = new JLabel("Khách hàng");
        khachKhangJLabel.setPreferredSize(new Dimension(0, 40));
        khachKhangJLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

        khPanel.add(merge2);
        khPanel.add(merge1);
        khPanel.add(khachKhangJLabel);
        khPanel.add(khachJPanel);

        JPanel dtlPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        dtlPanel.setPreferredSize(new Dimension(0, 40));
        dtlPanel.setOpaque(false);
        dtlPanel.add(lbDTL);
        dtlPanel.add(txtDTL);
        dtlPanel.add(lbDTLG);
        dtlPanel.add(txtDTLG);

        content_left.add(khPanel, BorderLayout.CENTER);
        content_left.add(dtlPanel, BorderLayout.SOUTH);

        content_right_top = new JPanel(new GridLayout(8, 2, 10, 0));
        content_right_top.setBorder(new EmptyBorder(5, 20, 5, 20));
        // content_right_top.setOpaque(false);

        JLabel lbl1 = new JLabel("Tổng tiền: ");
        lbl1.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        // lbl1.setForeground(new Color(255, 51, 51));
        lbltongtien = new JLabel("0đ");
        lbltongtien.setHorizontalAlignment(JLabel.RIGHT);
        lbltongtien.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        JLabel lbl2 = new JLabel("Giảm giá: ");
        lbl2.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        lblgiamgia = new JLabel("0đ");
        lblgiamgia.setHorizontalAlignment(JLabel.RIGHT);
        lblgiamgia.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        JLabel lbl3 = new JLabel("Dùng điểm: ");
        lbl3.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        lbldungdiem = new JLabel("0đ");
        lbldungdiem.setHorizontalAlignment(JLabel.RIGHT);
        lbldungdiem.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        JLabel lbl4 = new JLabel("Khách cần trả: ");
        lbl4.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        lblkhachcantra = new JLabel("0đ");
        lblkhachcantra.setHorizontalAlignment(JLabel.RIGHT);
        lblkhachcantra.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        JLabel lbl5 = new JLabel("Hình thức: ");
        lbl5.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        JLabel lblhinhthuc = new JLabel("Tiền mặt");
        lblhinhthuc.setHorizontalAlignment(JLabel.CENTER);
        lblhinhthuc.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        JLabel lbl6 = new JLabel("Đã thu: ");
        lbl6.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        txtDaThu = new JTextField();
        txtDaThu.setSize(40, 30);
        txtDaThu.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabels();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabels();
            }

            private void updateLabels() {
                Double text = Double.parseDouble(txtDaThu.getText());
                lbltienthua.setText(Formater.FormatVND(text - khachcantra));
            }
        });

        JLabel lbl7 = new JLabel("Tiền thừa: ");
        lbl7.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        lbltienthua = new JLabel("0đ");
        lbltienthua.setHorizontalAlignment(JLabel.RIGHT);
        lbltienthua.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        JLabel lbl8 = new JLabel("Điểm tạm tính: ");
        lbl8.setFont(new Font(FlatRobotoFont.FAMILY, 1, 16));
        lbldiemtamtinh = new JLabel("0đ");
        lbldiemtamtinh.setHorizontalAlignment(JLabel.RIGHT);
        lbldiemtamtinh.setFont(new Font(FlatRobotoFont.FAMILY, 4, 16));

        content_right_top.add(lbl1);
        content_right_top.add(lbltongtien);
        content_right_top.add(lbl3);
        content_right_top.add(lbldungdiem);
        content_right_top.add(lbl4);
        content_right_top.add(lblkhachcantra);
        content_right_top.add(lbl5);
        content_right_top.add(lblhinhthuc);
        content_right_top.add(lbl6);
        content_right_top.add(txtDaThu);
        content_right_top.add(lbl7);
        content_right_top.add(lbltienthua);
        content_right_top.add(lbl8);
        content_right_top.add(lbldiemtamtinh);

        content_right_bottom = new JPanel(new FlowLayout(1));
        content_right_bottom.setPreferredSize(new Dimension(350, 55));
        content_right_bottom.setBorder(new EmptyBorder(5, 10, 5, 10));
        content_right_bottom.setOpaque(false);

        btnRefresh = new ButtonCustom("Làm mới", "excel", 16, 100, 45);
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tblModel.setRowCount(0);
                chitietphieu.clear();
                resetForm();
                resetFormRight();
            }
        });
        // btnIn = new ButtonCustom("In", "excel", 16, 60, 45);

        btnThanhToan = new ButtonCustom("Thanh Toán", "excel", 16, 150, 45);
        btnThanhToan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBtnNhapHang();
            }
        });
        content_right_bottom.add(btnRefresh);
        // content_right_bottom.add(btnIn);
        content_right_bottom.add(btnThanhToan);

        content_right.add(content_right_top, BorderLayout.CENTER);
        content_right.add(content_right_bottom, BorderLayout.SOUTH);
        right_bottom.add(content_left);
        right_bottom.add(content_right);
        right.add(right_top, BorderLayout.CENTER);
        right.add(right_bottom, BorderLayout.SOUTH);

        contentCenter.add(left, BorderLayout.WEST);
        contentCenter.add(right, BorderLayout.CENTER);
        // actionbtn("add");
    }

    public void actionbtn(String type) {
        boolean val_1 = type.equals("add");
        boolean val_2 = type.equals("update");
        btnAddSp.setEnabled(val_1);
        btnEditSP.setEnabled(val_2);
        btnDelete.setEnabled(val_2);
    }

    public void resetForm() {
        this.txtTenSp.setText("");
        this.txtMaSp.setText("");
        this.txtMaISBN.setText("");
        this.txtGiaXuat.setText("");
        this.txtSoLuongSPxuat.setText("1");
        this.txtGiaGiam.setText(" ");
    }

    public String[] getMaGiamGiaTable(int masp) {
        listctMKM = mkmBUS.Getctmkm(masp);
        int size = listctMKM.size();
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!validateSelectDate(listctMKM.get(i)))
                arr.add(listctMKM.get(i).getMKM());
        }
        String[] tmp = new String[arr.size()];
        for (int i = 0; i < tmp.length; i++)
            tmp[i] = arr.get(i);
        tmp = Stream.concat(Stream.of("Chọn"), Arrays.stream(tmp)).toArray(String[]::new);
        return tmp;
    }

    public int getPcCp(int masp, String maKM) {
        listctMKM = mkmBUS.Getctmkm(masp);
        for (ChiTietMaKhuyenMaiDTO ctMKM : listctMKM) {
            if (!validateSelectDate(ctMKM) && maKM.equals(ctMKM.getMKM())) {
                return ctMKM.getPTG();
            }
        }
        return 0;
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

    public void setFormChiTietPhieu(ChiTietPhieuXuatDTO phieu) { // set info vào inputform khi nhan ben tablephieunhap
        SanPhamDTO ctsp = spBUS.getByMaSP(phieu.getMSP());
        // ChiTietMaKhuyenMaiDTO ctmkmi = mkmBUS.findCT(listctMKM, ctsp.getMSP());
        this.txtMaSp.setText(Integer.toString(ctsp.getMSP()));
        this.txtTenSp.setText(spBUS.getByMaSP(ctsp.getMSP()).getTEN());
        this.txtGiaXuat.setText(Integer.toString(phieu.getTIEN()));
        this.txtSoLuongSPxuat.setText(Integer.toString(phieu.getSL()));
        cbxMaKM.setArr(getMaGiamGiaTable(ctsp.getMSP()));
        cbxMaKM.setSelectedItem(phieu.getMKM());
    }

    // public void loadDataTalbeSanPham(ArrayList<DTO.SanPhamDTO> result) { //CŨ
    // tblModelSP.setRowCount(0);
    // for (SanPhamDTO sp : result) {
    // tblModelSP.addRow(new Object[] { sp.getMSP(), sp.getTEN(), sp.getSL() });
    // }
    // }
    public void loadDataTalbeSanPham(ArrayList<DTO.SanPhamDTO> result) {
    tblModelSP.setRowCount(0);  // Xóa các dòng hiện có trong bảng

    MultiLineData dataString;
    ImageIcon dataImage;
    for (SanPhamDTO sp : result) {
        dataImage = new ImageIcon("./src/img_product/" + sp.getHINHANH());
        dataString = new MultiLineData(sp.getTEN(), " ", sp.getTIENX() + "VND");

        // Lưu mã sản phẩm vào một cột ẩn (ví dụ: sp.getMSP())
        Object[] rowData = new Object[] {dataImage, dataString, sp.getSL(),sp.getMV()};  // Mã sản phẩm ở cột đầu tiên

        // Thêm dòng vào bảng
        tblModelSP.addRow(rowData);
    }

    // Ẩn cột mã sản phẩm (vẫn lưu trong TableModel)
    tableSanPham.getColumnModel().getColumn(3).setMinWidth(0);
    tableSanPham.getColumnModel().getColumn(3).setMaxWidth(0);
    tableSanPham.getColumnModel().getColumn(3).setPreferredWidth(0);
}



   public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuXuatDTO> ctPhieu) {
    tblModel.setRowCount(0);
    int size = ctPhieu.size();
    sum = 0;
    giagiam = 0;
    ArrayList<DonViDTO> listdv = dvbus.getAll();

    for (int i = 0; i < size; i++) {
        // Lấy phần trăm giảm từ mã khuyến mãi
        int percentCounpoint = getPcCp(ctPhieu.get(i).getMSP(), listMaKM.get(i));
        
        // Lấy thông tin sản phẩm
        SanPhamDTO sp = spBUS.getByMaSP(ctPhieu.get(i).getMSP());

        // Cộng dồn tổng tiền
        sum += ctPhieu.get(i).getTIEN() * ctPhieu.get(i).getSL();

        // Thêm dòng vào bảng
        tblModel.addRow(new Object[] {
            sp.getMV(),
            spBUS.getByMaSP(sp.getMSP()).getTEN(), // Lấy tên sản phẩm
            Formater.FormatVND(giaGocMap.get(i)),  // Lấy giá gốc từ HashMap
            ctPhieu.get(i).getSL(),
            listdv.get(sp.getMDV() - 1).getTENDV(),
            percentCounpoint,  // Phần trăm giảm giá
            Formater.FormatVND(ctPhieu.get(i).getTIEN() * ctPhieu.get(i).getSL()),  // Tổng tiền sản phẩm
            "X"  // Cột xóa
        });
    }

    lbltongtien.setText(Formater.FormatVND(sum));
    lblgiamgia.setText(Formater.FormatVND(giagiam));

    loadDataMenuRight();
}




   public boolean checkTonTai() {
    int maSP = -1;
    try {
        maSP = Integer.parseInt(txtMaSp.getText()); // Lấy mã sản phẩm từ txtMaSp
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Mã sản phẩm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;  // Trả về false nếu mã sản phẩm không hợp lệ
    }

    for (ChiTietPhieuXuatDTO p : chitietphieu) {
        if (p.getMSP()== maSP) { // Sử dụng phương thức đúng
            return true; 
        }
    }

    return false; // Nếu không tìm thấy sản phẩm trong danh sách, trả về false
}


    public boolean checkInfo() {
        boolean check = true;
        int index = tableSanPham.getSelectedRow();
        if (txtMaSp.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (txtGiaXuat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Giá nhập không được để rỗng !", "Cảnh báo !",
                    JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (txtSoLuongSPxuat.getText().equals("")
                || Integer.parseInt(txtSoLuongSPxuat.getText()) > listSP.get(index).getSL()) {
            JOptionPane.showMessageDialog(null, "Số lượng không được để rỗng và không lớn hơn đang có!", "Cảnh báo !",
                    JOptionPane.WARNING_MESSAGE);
            check = false;
        } else if (Integer.parseInt(txtSoLuongSPxuat.getText()) == 0) {
            JOptionPane.showMessageDialog(null, "Số lượng không được bằng 0!", "Cảnh báo !",
                    JOptionPane.WARNING_MESSAGE);
            check = false;
        }
        return check;
    }

    public void addCtPhieu(int index) { // them sp vao chitietphieu
        int masp = Integer.parseInt(txtMaSp.getText());
        int giaxuat;
        String mkm = listMaKM.get(index);
        if (!txtGiaGiam.getText().equals(" ")) {
            giaxuat = Integer.parseInt(txtGiaGiam.getText());
        } else
            giaxuat = Integer.parseInt(txtGiaXuat.getText());
        int soluong = Integer.parseInt(txtSoLuongSPxuat.getText());
        ChiTietPhieuXuatDTO ctphieu = new ChiTietPhieuXuatDTO(maphieu, masp, soluong, giaxuat, mkm);
        ChiTietPhieuXuatDTO p = phieuXuatBUS.findCT(chitietphieu, ctphieu.getMSP());
        if (p == null) {
            chitietphieu.add(ctphieu);
            loadDataTableChiTietPhieu(chitietphieu);
            resetForm();
        } else {
            int input = JOptionPane.showConfirmDialog(this,
                    "Sản phẩm đã tồn tại trong phiếu !\nBạn có muốn chỉnh sửa không ?", "Sản phẩm đã tồn tại !",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                setFormChiTietPhieu(ctphieu);
            }
        }
    }

    public void eventBtnNhapHang() {
        String tiemThua = lbltienthua.getText().replaceAll("[^0-9-]", "");
        if (chitietphieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong phiếu!", "Cảnh báo !",
                    JOptionPane.ERROR_MESSAGE);
        } else if (makh == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if (Integer.parseInt(txtDTLG.getText()) > dtl || Integer.parseInt(txtDTLG.getText()) > sum) {
            JOptionPane.showMessageDialog(null, "Điểm tích lũy không lớn hơn điểm đang có và giá đang bán!",
                    "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if(txtDaThu.getText().equals("") || Integer.parseInt(tiemThua) < 0 ) {
            JOptionPane.showMessageDialog(null, "Phải lớn hơn hoặc bằng số tiền khách hàng cần trả",
                    "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else {
            int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn tạo hóa đơn !",
                    "Xác nhận tạo phiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                // Lấy MCN trước
                NhanVienDTO nhanVienDTO = NhanVienDAO.getInstance().selectById(tk.getMNV() + "");
                String mcn = nhanVienDTO.getMCN();
                
                if (!phieuXuatBUS.checkSLPx(chitietphieu, mcn)) {
                    JOptionPane.showMessageDialog(null, "Không đủ số lượng để tạo phiếu!");
                } else {
                    long now = System.currentTimeMillis();
                    Timestamp currenTime = new Timestamp(now);
                    String khachtra = lblkhachcantra.getText().replaceAll("[^0-9]", "");
                    PhieuXuatDTO phieuXuat = new PhieuXuatDTO(makh, maphieu, tk.getMNV(), currenTime,
                            (long)Integer.parseInt(khachtra), 1,
                            Integer.parseInt(lbldiemtamtinh.getText().replaceAll("[^0-9]", "")), mcn);

                    for (ChiTietPhieuXuatDTO ct : chitietphieu) {
                        if ("Chọn".equals(ct.getMKM())) {
                            ct.setMKM(null);
                        }
                        for (SanPhamDTO sp : listSP) {
                            if (ct.getMSP() == sp.getMSP()) {
                                int solSPT = sp.getSL();
                                int soLMua = ct.getSL();
                                sp.setSL(solSPT - soLMua);
                                break;
                            }
                        }
                    }

                    phieuXuatBUS.insert(phieuXuat, chitietphieu, mcn); // update số lượng trong kho
                    /// gọi BUS, BUS gọi DAO, DAO chỉnh trong sql
                    // SanPhamBUS.updateXuat(chitietsanpham);
                    JOptionPane.showMessageDialog(null, "Xuất hàng thành công !");
                    khachHangBUS.update(makh, (dtl - Integer.parseInt(txtDTLG.getText())) + diemtamtinh);

                    chitietphieu.clear();
                    tblModel.setRowCount(0);
                    
                    loadDataTalbeSanPham(listSP); // Hiển thị lại bảng
                    resetForm();
                    resetFormRight();
                    maphieu = phieuXuatBUS.getMPMAX() + 1;
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    new ChiTietPhieuDialog(parentFrame, "Thông tin phiếu xuất", true, phieuXuat);

                    // this.setPanel(new HoaDon(this, tk));
                    // this.dispose();
                }
            }
        }
    }

    public void setKhachHang(int index) {
        makh = index;
        KhachHangDTO khachhang = khachHangBUS.selectKh(makh);
        txtKh.setText(khachhang.getHOTEN());
        if (index != 1) {
            dtl = khachhang.getDIEMTICHLUY();
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

    public void loadDataMenuRight() {
        if (!txtDTL.getText().equals("")) {
            dungdiem = Integer.parseInt(txtDTLG.getText());
            if (dungdiem <= dtl) {
                // thông báo
                lbldungdiem.setText(Formater.FormatVND(dungdiem));
                khachcantra = (double) (sum - giagiam - dungdiem);
                lbldiemtamtinh.setText(Formater.FormatVND(diemtamtinh));
                lblkhachcantra.setText(Formater.FormatVND(khachcantra));
            } else {
                lbldungdiem.setText("0đ");
                khachcantra = (double) (sum - giagiam);
                lbldiemtamtinh.setText("0đ");
                lblkhachcantra.setText(Formater.FormatVND(khachcantra));
            }
        } else {
            khachcantra = (double) (sum - giagiam);
            lbldiemtamtinh.setText(Formater.FormatVND(khachcantra * 0.01));
            lblkhachcantra.setText(Formater.FormatVND(khachcantra));
        }
    }

    public void resetFormRight() {
        khachcantra = 0;
        diemtamtinh = 0;
        dathu = 0;
        giagiam = 0;
        dungdiem = 0;
        sum = 0;
        makh = -1;
        lbltongtien.setText("0đ");
        lblgiamgia.setText("0đ");
        lbldungdiem.setText("0đ");
        lblkhachcantra.setText(khachcantra + "");
        txtDaThu.setText("");
        lbltienthua.setText("0đ");
        lbldiemtamtinh.setText(diemtamtinh + "");
        txtDTLG.setText("0");
        txtDTL.setText("");
        txtKh.setText("Chọn khách hàng");

    }
    public static void main(String[] args) {
        // Tạo một đối tượng TaiKhoanDTO giả để kiểm tra (có thể thay bằng đối tượng thực tế của bạn)
        TaiKhoanDTO taiKhoan = new TaiKhoanDTO();
        // Giả sử bạn có một phương thức để khởi tạo TaiKhoanDTO, bạn cần thay đổi theo cách bạn khởi tạo
        // taiKhoan.set... (các thuộc tính cần thiết của tài khoản)

        // Loại cửa sổ (ví dụ: "create", "update" hoặc "view", bạn có thể tùy chỉnh)
        String type = "create";

        // Tạo đối tượng BanHang và hiển thị
        SwingUtilities.invokeLater(() -> {
            BanHang banHang = new BanHang(taiKhoan, type);
//            banHang.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            banHang.setVisible(true); // Hiển thị cửa sổ
        });
    }
    private void handleMousePressed(MouseEvent e) {
    SanPhamBUS spb=new SanPhamBUS();
    int index = tableSanPham.getSelectedRow();
    if (index != -1) {
        String mavach = tableSanPham.getValueAt(index, 3).toString();
        System.out.println(mavach);
        SanPhamDTO selectedProduct = null;
        for (SanPhamDTO sp : spb.getAll()) {
            if (sp.getMV().equals(mavach)) {  // So sánh mã sản phẩm trong listSP với mã vạch từ bảng
                selectedProduct = sp;
                break; // Dừng vòng lặp khi tìm thấy sản phẩm
            }
        }
        setInfoSanPham(selectedProduct);
        if (!checkTonTai()) {
            actionbtn("add");
        } else {
            actionbtn("update");
        }
    }
    
}
   

  

private void handleAddProduct() {
    if (checkInfo()) {
        try {
            // Lấy số lượng người dùng nhập vào
            String soLuongText = txtSoLuongSPxuat.getText().trim();
            if (soLuongText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Số lượng không được để trống");
                return; // Dừng thực hiện nếu số lượng không hợp lệ
            }
            int soLuong = Integer.parseInt(soLuongText); // Chuyển đổi số lượng

            // Lấy mã sản phẩm từ chi tiết sản phẩm
            int maSP = Integer.parseInt(txtMaSp.getText());  // Giả sử mã sản phẩm được nhập vào trong một trường txtMaSP

            // Kiểm tra số lượng tồn kho của sản phẩm
            int soLuongTonKho = getSoLuongTonKho(maSP);  // Hàm lấy số lượng tồn kho của sản phẩm

            // Kiểm tra số lượng nhập vào có vượt quá số lượng tồn kho không
            if (soLuong > soLuongTonKho) {
                JOptionPane.showMessageDialog(null, "Số lượng sản phẩm trong kho không đủ!");
                return; // Dừng thực hiện nếu số lượng vượt quá
            }

            // Lưu giá gốc vào HashMap chỉ khi mã giảm giá chưa được áp dụng
            double giaXuat = Double.parseDouble(txtGiaXuat.getText());

            // Kiểm tra xem sản phẩm này có được thêm vào giaGocMap chưa
            if (!giaGocMap.containsKey(listMaKM.size())) {
                giaGocMap.put(listMaKM.size(), giaXuat);  // Lưu giá gốc với key là chỉ số sản phẩm trong danh sách
            }

            // Tiến hành thêm sản phẩm vào bảng chi tiết phiếu
            listMaKM.add(cbxMaKM.getSelectedItem().toString());
            addCtPhieu(listMaKM.size() - 1);

            // Cập nhật lại bảng dữ liệu
            loadDataTableChiTietPhieu(chitietphieu);

            // Thông báo thêm sản phẩm thành công
            Notification thongbaoNoi = new Notification(mainChinh, Notification.Type.SUCCESS,
                    Notification.Location.TOP_CENTER, "Thêm sản phẩm thành công!");
            thongbaoNoi.showNotification();

            // Reset form và combo box
            cbxMaKM.setSelectedItem("Chọn");
            resetForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Thông tin không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }
}




private void handleEditProduct() {
    int index = tablePhieuXuat.getSelectedRow();
    if (index < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn cấu hình cần chỉnh");
    } else {
        try {
            // Kiểm tra xem txtSoLuongSPxuat có rỗng hay không trước khi chuyển đổi
            String soLuongText = txtSoLuongSPxuat.getText().trim();
            if (soLuongText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Số lượng không được để trống");
                return; // Dừng thực hiện nếu số lượng không hợp lệ
            }
            int newQuantity = Integer.parseInt(soLuongText); // Chuyển đổi số lượng

            // Lấy mã sản phẩm từ chi tiết hóa đơn
            int maSP = chitietphieu.get(index).getMSP();

            // Kiểm tra số lượng tồn kho
            int soLuongTonKho = getSoLuongTonKho(maSP);  // Hàm lấy số lượng tồn kho của sản phẩm

            // Kiểm tra số lượng yêu cầu có vượt quá số lượng tồn kho không
            if (newQuantity > soLuongTonKho) {
                JOptionPane.showMessageDialog(null, "Số lượng sản phẩm trong kho không đủ!");
                return; // Dừng nếu số lượng vượt quá
            }

            chitietphieu.get(index).setSL(newQuantity); // Cập nhật số lượng

            // Kiểm tra mã khuyến mãi cũ và mới
            String selectedMaKM = cbxMaKM.getSelectedItem().toString();
            String oldMaKM = chitietphieu.get(index).getMKM();

            // Nếu mã khuyến mãi không thay đổi, chỉ cần cập nhật số lượng và giữ nguyên số tiền
            if (selectedMaKM.equals(oldMaKM)) {
                chitietphieu.get(index).setSL(newQuantity);
                // Không thay đổi số tiền khi mã giảm giá không thay đổi
            } else {
                // Nếu mã khuyến mãi thay đổi, cập nhật lại số tiền và mã giảm giá
                chitietphieu.get(index).setMKM(selectedMaKM); // Cập nhật mã khuyến mãi
                
                // Kiểm tra lại giá giảm sau khi mã khuyến mãi thay đổi
                String giaGiamText = txtGiaGiam.getText().trim();
                if (!giaGiamText.isEmpty()) {
                    try {
                        int newDiscountPrice = Integer.parseInt(giaGiamText);
                        chitietphieu.get(index).setTIEN(newDiscountPrice);  // Cập nhật tiền sau giảm
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Giá giảm không hợp lệ");
                        return; // Dừng thực hiện nếu giá giảm không hợp lệ
                    }
                } else {
                    // Nếu không có giá giảm (mã khuyến mãi không giảm giá), giữ nguyên giá gốc
                    Double giaGoc = giaGocMap.get(index);  // Lấy giá gốc từ HashMap
                    if (giaGoc != null) {
                        chitietphieu.get(index).setTIEN(giaGoc.intValue());  // Cập nhật lại giá gốc nếu không có giảm giá
                    } else {
                        // Nếu không có giá gốc (do chưa lưu), lấy giá xuất ban đầu
                        String giaXuatText = txtGiaXuat.getText().trim();
                        if (!giaXuatText.isEmpty()) {
                            try {
                                chitietphieu.get(index).setTIEN(Integer.parseInt(giaXuatText));
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Giá xuất không hợp lệ");
                                return;
                            }
                        }
                    }
                }
            }

            actionbtn("add");  // Cập nhật lại giao diện nếu cần
            loadDataTableChiTietPhieu(chitietphieu);  // Cập nhật lại bảng chi tiết
            cbxMaKM.setSelectedItem(chitietphieu.get(index).getMKM()); // Chọn lại mã khuyến mãi trong combo box

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Thông tin không hợp lệ. Vui lòng kiểm tra lại.");
        }
    }
    resetForm();  // Reset form sau khi sửa xong
}

// Hàm lấy số lượng tồn kho của sản phẩm
private int getSoLuongTonKho(int maSP) {
    SanPhamBUS spb=new SanPhamBUS();
    // Giả sử bạn có một phương thức trong BUS hoặc DAO để lấy số lượng tồn kho
    SanPhamDTO sp = spb.getByMaSP(maSP);
    if (sp != null) {
        return sp.getSL(); // Giả sử có trường này trong DTO của sản phẩm
    }
    return 0; // Nếu không tìm thấy sản phẩm, trả về 0
}







private void handleMaKMSelectionChanged(ItemEvent e) {
    System.out.println("KIỂM TRA GIÁ XUẤT");
    int index = cbxMaKM.cbb.getSelectedIndex();
    if (index != 0 && !txtGiaXuat.getText().isEmpty()) {
        try {
            // Chuyển đổi giá trị từ txtGiaXuat sang double
            double giaxuat = Double.parseDouble(txtGiaXuat.getText());
            double phantramgiam = (double) listctMKM.get(index - 1).getPTG();
            int giagiam = (int) (giaxuat * (1 - phantramgiam / 100));
            txtGiaGiam.setText(Integer.toString(giagiam));
        } catch (NumberFormatException ex) {
            // Xử lý ngoại lệ nếu giá xuất không hợp lệ
        }
    } else {
        txtGiaGiam.setText("");  // Nếu không có giá xuất, xóa giá giảm
    }
}



    private void handleDeleteProduct() {
    int index = tablePhieuXuat.getSelectedRow();
    if (index < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn cấu hình cần xóa");
    } else {
        chitietphieu.remove(index); // Xóa sản phẩm khỏi danh sách
        actionbtn("add"); // Thực hiện hành động sau khi xóa
        loadDataTableChiTietPhieu(chitietphieu); // Tải lại dữ liệu bảng
        resetForm(); // Reset các trường nhập liệu
    }
}
}