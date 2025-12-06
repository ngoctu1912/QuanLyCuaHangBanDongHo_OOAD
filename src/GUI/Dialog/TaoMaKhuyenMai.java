package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import BUS.MaKhuyenMaiBUS;
import BUS.NhaCungCapBUS;
import BUS.SanPhamBUS;
import DTO.ChiTietMaKhuyenMaiDTO;
import DTO.MaKhuyenMaiDTO;
import DTO.NhanVienDTO;
import DTO.SanPhamDTO;
import GUI.Main;
import GUI.Component.ButtonCustom;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.PanelBorderRadius;
import GUI.Panel.MaKhuyenMai;
import GUI.Panel.PhieuNhap;
import helper.Validation;

public final class TaoMaKhuyenMai extends JPanel implements ItemListener, ActionListener {

    PanelBorderRadius left, right;
    JTable tableMKM, tableSanPham;
    JScrollPane scrollTablePhieuNhap, scrollTableSanPham;
    JPanel contentCenter, left_top, content_btn, left_bottom;
    DefaultTableModel tblModel, tblModelSP; //table co san 
    ButtonCustom btnAddSp, btnEditSP, btnDelete, btnNhapMKM; //, btnImport
    InputForm txtMaKM, txtMaSp, txtTenSp, txtMaISBN, txtPTG;
    InputDate dateStart, dateEnd;
    JTextField txtTimKiem;

    Main m;
    Color BackgroundColor = new Color(248, 249, 250);

    SanPhamBUS spBUS = new SanPhamBUS();
    NhaCungCapBUS nccBus = new NhaCungCapBUS();
    MaKhuyenMaiBUS MaKhuyenMaiBus = new MaKhuyenMaiBUS();
    NhanVienDTO nvDto;

    ArrayList<SanPhamDTO> listSP = spBUS.getAll(); // list ben kho 
    ArrayList<SanPhamDTO> listSP_tmp = new ArrayList<>();
    ArrayList<ChiTietMaKhuyenMaiDTO> chitietMKM;
    int rowPhieuSelect = -1;

    public TaoMaKhuyenMai(NhanVienDTO nv, String type, Main m) {
        this.nvDto = nv;
        this.m = m;
        chitietMKM = new ArrayList<>();
        initComponent(type);
        loadDataTalbeSanPham(listSP);
    }

    public void initComponent(String type) {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        //Phieu Nhap
        tableMKM = new JTable();
        scrollTablePhieuNhap = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên sản phẩm", "Phần trăm giảm"};
        tblModel.setColumnIdentifiers(header); //thiết lập tiêu đề cột, nhận một tham số là một mảng các chuỗi
        tableMKM.setModel(tblModel);
        scrollTablePhieuNhap.setViewportView(tableMKM);//thiết lập thành phần hiển thị cho viewport. Thành phần hiển thị là một component có thể cuộn (scrollable)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); //phương thức để định dạng văn bản, màu sắc, căn chỉnh và các thuộc tính hiển thị khác cho các ô trong bản
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tableMKM.getColumnModel();
        for (int i = 0; i < 4; i++) {
            if (i != 2) {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        tableMKM.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableMKM.setDefaultEditor(Object.class, null);
        tableMKM.setFocusable(false);
        scrollTablePhieuNhap.setViewportView(tableMKM);

        tableMKM.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tableMKM.getSelectedRow();
                if (index != -1) {
                    tableSanPham.setSelectionMode(index);
                    setFormChiTietPhieu(chitietMKM.get(index));
                    rowPhieuSelect = index;
                    actionbtn("update");
                }
            }
        });

        //Table san pham
        tableSanPham = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModelSP = new DefaultTableModel();
        String[] headerSP = new String[]{"Mã SP", "Tên sản phẩm", "SL tồn"};
        tblModelSP.setColumnIdentifiers(headerSP);
        tableSanPham.setModel(tblModelSP);
        scrollTableSanPham.setViewportView(tableSanPham);
        tableSanPham.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableSanPham.setDefaultEditor(Object.class, null);
        tableSanPham.setFocusable(false);
        scrollTableSanPham.setViewportView(tableSanPham);

        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tableSanPham.getSelectedRow();
                if (index != -1) {
                    String maSanPham = String.valueOf(tableSanPham.getValueAt(index, 0));
                    int maSanPhamInteger = Integer.parseInt(maSanPham);
                    resetForm();
                    setInfoSanPham(spBUS.getByMaSP(maSanPhamInteger));
                    ChiTietMaKhuyenMaiDTO ctp = checkTonTai();
                    if (ctp == null) {
                        actionbtn("add");
                    } else {
                        actionbtn("update");
                        setFormChiTietPhieu(ctp);
                    }
                }
            }
        });

        left = new PanelBorderRadius();
        left.setLayout(new BorderLayout(0, 5));
        left.setBackground(Color.white);

        //content_CENTER (content_top + content_bottom) }-> left_top
        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1200, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(5, 5));
        this.add(contentCenter, BorderLayout.CENTER);

        left_top = new JPanel(); // Chứa tất cả phần ở phía trái trên cùng
        left_top.setLayout(new BorderLayout());
        left_top.setBorder(new EmptyBorder(5, 5, 10, 10));
        left_top.setOpaque(false);

        JPanel content_top, content_left, content_right, content_right_top;
        content_top = new JPanel(new GridLayout(1, 2, 5, 5));
        content_top.setOpaque(false);

        content_left = new JPanel(new BorderLayout(5, 5));
        content_left.setOpaque(false);
        content_left.setPreferredSize(new Dimension(0, 300));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(100, 40));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tên sản phẩm, mã sản phẩm ...");
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

        content_right_top = new JPanel(new BorderLayout());
        content_right_top.setPreferredSize(new Dimension(100, 250));
        txtTenSp = new InputForm("Tên sản phẩm");
        txtTenSp.setEditable(false);
        txtTenSp.setPreferredSize(new Dimension(100, 90));
        txtMaSp = new InputForm("Mã sản phẩm");
        txtMaSp.setEditable(false);
        txtMaISBN = new InputForm("Mã vạch");
        txtMaISBN.setEditable(false);
        txtMaISBN.getTxtForm().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ArrayList<SanPhamDTO> rs = spBUS.search(txtMaISBN.getText(), "ISBN");
                loadDataTalbeSanPham(rs);
                //thêm load lại inputform
            }
        });
        txtPTG = new InputForm("Phần trăm giảm");
        PlainDocument ptg = (PlainDocument) txtPTG.getTxtForm().getDocument();
        ptg.setDocumentFilter((new NumericDocumentFilter())); //chỉ cho nhập số

        JPanel merge1 = new JPanel(new BorderLayout());
        merge1.setPreferredSize(new Dimension(100, 50));
        merge1.add(txtMaSp, BorderLayout.WEST);
        merge1.add(txtMaISBN, BorderLayout.CENTER);

        JPanel merge2 = new JPanel(new GridLayout());
        merge2.setPreferredSize(new Dimension(100, 80));
        merge2.add(txtPTG);

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
        btnAddSp = new ButtonCustom("Thêm mã khuyến mãi", "success", 14);
        btnEditSP = new ButtonCustom("Sửa mã khuyến mãi", "warning", 14);
        btnDelete = new ButtonCustom("Xoá mã khuyến mãi", "danger", 14);
        // btnImport = new ButtonCustom("Nhập Excel", "excel", 14);
        btnAddSp.addActionListener(this);
        btnEditSP.addActionListener(this);
        btnDelete.addActionListener(this);
        // btnImport.addActionListener(this);
        btnEditSP.setEnabled(false);
        btnDelete.setEnabled(false);
        content_btn.add(btnAddSp);
        // content_btn.add(btnImport);
        content_btn.add(btnEditSP);
        content_btn.add(btnDelete);
        left_top.add(content_btn, BorderLayout.SOUTH);

        //left_bottom này là danh sách nhập ở left phía nam
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

        JPanel right_top, right_center, right_bottom;
        right_top = new JPanel(new GridLayout(4, 1, 0, 0));
        right_top.setPreferredSize(new Dimension(300, 360));
        right_top.setOpaque(false);
        txtMaKM = new InputForm("Mã khuyến mãi");
        dateStart = new InputDate("Từ ngày");
        dateEnd = new InputDate("Đến ngày");
        right_top.add(txtMaKM);
        right_top.add(dateStart);
        right_top.add(dateEnd);

        right_center = new JPanel();
        right_center.setPreferredSize(new Dimension(100, 100));
        right_center.setOpaque(false);

        right_bottom = new JPanel(new GridLayout(2, 1));
        right_bottom.setPreferredSize(new Dimension(300, 100));
        right_bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        right_bottom.setOpaque(false);

        btnNhapMKM = new ButtonCustom("Tạo mã khuyến mãi", "excel", 14);
        btnNhapMKM.addActionListener(this);
        right_bottom.add(btnNhapMKM);

        right.add(right_top, BorderLayout.NORTH);
        right.add(right_center, BorderLayout.CENTER);// để trống
        right.add(right_bottom, BorderLayout.SOUTH);

        contentCenter.add(left, BorderLayout.CENTER);
        contentCenter.add(right, BorderLayout.EAST);
    }

    public void actionbtn(String type) { //ẩn hiện button
        boolean val_1 = type.equals("add");
        boolean val_2 = type.equals("update");
        btnAddSp.setEnabled(val_1);
        // btnImport.setEnabled(val_1);
        btnEditSP.setEnabled(val_2);
        btnDelete.setEnabled(val_2);
        content_btn.revalidate();
        content_btn.repaint();
    }

    public void setInfoSanPham(SanPhamDTO sp) { //set info vào inputform khi nhan ben tablesanpham
        this.txtMaSp.setText(Integer.toString(sp.getMSP()));
        this.txtTenSp.setText(sp.getTEN());
        this.txtMaISBN.setText(sp.getMV());
    }

    public void setFormChiTietPhieu(ChiTietMaKhuyenMaiDTO phieu) { //set info vào inputform khi nhan ben tablephieunhap
        SanPhamDTO ctsp = spBUS.getByMaSP(phieu.getMSP());
        this.txtMaSp.setText(Integer.toString(ctsp.getMSP()));
        this.txtTenSp.setText(spBUS.getByMaSP(ctsp.getMSP()).getTEN());
        this.txtMaISBN.setText(ctsp.getMV());
        this.txtPTG.setText(Integer.toString(phieu.getPTG()));
    }

    public ChiTietMaKhuyenMaiDTO getInfoChiTietPhieu() {
        int masp = Integer.parseInt(txtMaSp.getText());
        int PTG = Integer.parseInt(txtPTG.getText());
        String mkm = txtMaKM.getText();
        ChiTietMaKhuyenMaiDTO ctmkm = new ChiTietMaKhuyenMaiDTO(mkm, masp, PTG);
        return ctmkm;
    }

    public void loadDataTalbeSanPham(ArrayList<SanPhamDTO> result) {
        tblModelSP.setRowCount(0);
        for (SanPhamDTO sp : result) {
            tblModelSP.addRow(new Object[]{sp.getMSP(), sp.getTEN(), sp.getSL()});
        }
    }

    public void loadDataTableChiTietMKM(ArrayList<ChiTietMaKhuyenMaiDTO> ctMKM) {
        tblModel.setRowCount(0);
        int size = ctMKM.size();
        for (int i = 0; i < size; i++) {
            SanPhamDTO pb = spBUS.getByMaSP(ctMKM.get(i).getMSP());
            tblModel.addRow(new Object[]{
                i + 1, pb.getMSP(), spBUS.getByMaSP(pb.getMSP()).getTEN(),
                ctMKM.get(i).getPTG() + "%"
            });
        }
    }

    public boolean validateNhap() {
        if (Validation.isEmpty(txtMaSp.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm", "Chọn sản phẩm", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (Validation.isEmpty(txtPTG.getText()) || Integer.parseInt(txtPTG.getText()) > 100 || Integer.parseInt(txtPTG.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Phần trăm giảm không được để rỗng và phải lốn hơn 0%, nhỏ hơn 100%!", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (Validation.isEmpty(txtMaKM.getText())) {
            JOptionPane.showMessageDialog(this, "Mã khuyến mãi không được để rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public void resetForm() {
        this.txtMaSp.setText("");
        this.txtTenSp.setText("");
        this.txtPTG.setText("");
        this.txtMaISBN.setText("");
    }

    public ChiTietMaKhuyenMaiDTO checkTonTai() {
        ChiTietMaKhuyenMaiDTO p = MaKhuyenMaiBus.findCT(chitietMKM, Integer.parseInt(txtMaSp.getText()));
        return p;
    }

    public void addCtMKM() { // them sp vao chitietphieu
        ChiTietMaKhuyenMaiDTO ctphieu = getInfoChiTietPhieu();
        ChiTietMaKhuyenMaiDTO p = MaKhuyenMaiBus.findCT(chitietMKM, ctphieu.getMSP());
        txtMaKM.setEditable(false);
        if (p == null) {
            chitietMKM.add(ctphieu);
            loadDataTableChiTietMKM(chitietMKM);
            resetForm();
        } else {
            int input = JOptionPane.showConfirmDialog(this, "Sản phẩm đã tồn tại trong mã !\nBạn có muốn chỉnh sửa không ?", "Sản phẩm đã tồn tại !", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                setFormChiTietPhieu(ctphieu);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'itemStateChanged'");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");

        Object source = e.getSource();
        if (source == btnAddSp && validateNhap()) {
            addCtMKM();

        } else if (source == btnDelete) {
            int index = tableMKM.getSelectedRow();
            chitietMKM.remove(index);
            actionbtn("add");
            loadDataTableChiTietMKM(chitietMKM);
            resetForm();
        } else if (source == btnEditSP) {
            if (Integer.parseInt(txtPTG.getText()) <= 0 || Integer.parseInt(txtPTG.getText()) > 100 || txtPTG.getText() == "") {
                JOptionPane.showMessageDialog(this, "Phần trăm giảm không được để trống và phải lớn hơn 0%, nhỏ hơn 100%");
            } else {
                chitietMKM.get(rowPhieuSelect).setPTG(Integer.parseInt(txtPTG.getText()));
                JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!");
                loadDataTableChiTietMKM(chitietMKM);
            }
        } else if (source == btnNhapMKM) {
            try {
                eventBtnTao();
            } catch (ParseException ex) {
                Logger.getLogger(PhieuNhap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean validateSelectDate() throws ParseException {
        Date time_start = dateStart.getDate();
        Date time_end = dateEnd.getDate();

        // Chuẩn hóa ngày hiện tại về 00:00:00 để so sánh chính xác
        Calendar current_calendar = Calendar.getInstance();
        current_calendar.set(Calendar.HOUR_OF_DAY, 0);
        current_calendar.set(Calendar.MINUTE, 0);
        current_calendar.set(Calendar.SECOND, 0);
        current_calendar.set(Calendar.MILLISECOND, 0);
        Date current_date = current_calendar.getTime();
        
        if (time_start != null) {
            Calendar start_calendar = Calendar.getInstance();
            start_calendar.setTime(time_start);
            start_calendar.set(Calendar.HOUR_OF_DAY, 0);
            start_calendar.set(Calendar.MINUTE, 0);
            start_calendar.set(Calendar.SECOND, 0);
            start_calendar.set(Calendar.MILLISECOND, 0);
            Date time_start_normalized = start_calendar.getTime();
            
            if (time_start_normalized.before(current_date)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được là ngày trong quá khứ", "Lỗi !", JOptionPane.ERROR_MESSAGE);
                dateStart.getDateChooser().setCalendar(null);
                return false;
            }
        }

        if (time_start != null && time_end != null && time_start.after(time_end)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn ngày bắt đầu", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateEnd.getDateChooser().setCalendar(null);
            return false;
        }
        if (time_end != null && time_end.before(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được bé hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateEnd.getDateChooser().setCalendar(null);
            return false;
        }
        return true;
    }

    public void eventBtnTao() throws ParseException {
        if (chitietMKM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong mã !", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if (Validation.isEmpty(txtMaKM.getText())) {
            JOptionPane.showMessageDialog(this, "Mã khuyến mãi không được để rỗng!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if (!MaKhuyenMaiBus.checkTT(txtMaKM.getText())) {
            JOptionPane.showMessageDialog(this, "Mã khuyến mãi đã tồn tại!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if (dateStart.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Không được để trống ngày từ, ngày đến!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else if (dateEnd.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Không được để trống ngày từ, ngày đến!", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
        } else {
            int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn tạo mã khuyến mãi!", "Xác nhận tạo phiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                if (validateSelectDate()) {
                    Date time_start_tmp = dateStart.getDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(time_start_tmp);
                    calendar.add(Calendar.DAY_OF_MONTH, 10);
                    Date time_end_tmp = dateEnd.getDate();
                    Timestamp time_start = new Timestamp(time_start_tmp.getTime());
                    Timestamp time_end = new Timestamp(time_end_tmp.getTime());
                    MaKhuyenMaiDTO MKM = new MaKhuyenMaiDTO(txtMaKM.getText(), time_start, time_end);
                    boolean result = MaKhuyenMaiBus.add(MKM, chitietMKM);
                    if (result) {
                        JOptionPane.showMessageDialog(this, "Tạo mã thành công !");
                        MaKhuyenMai pnlMKM = new MaKhuyenMai(m, nvDto);
                        m.setPanel(pnlMKM);
                    } else {
                        JOptionPane.showMessageDialog(this, "Tạo mã không thành công !", "Cảnh báo !", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

}
