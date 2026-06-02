package GUI.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;

import BUS.KhachHangBUS;
import BUS.NhanVienBUS;
import BUS.PhieuXuatBUS;
import DTO.PhieuXuatDTO;
import DTO.TaiKhoanDTO;
import DTO.NhanVienDTO;
import GUI.Main;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.Notification;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.PanelBorderRadius;
import GUI.Component.SelectForm;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietPhieuDialog;
import helper.Formater;
import helper.JTableExporter;

public final class PhieuXuat extends JPanel implements ActionListener, KeyListener, PropertyChangeListener, ItemListener {

    PanelBorderRadius main, functionBar, box;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tablePhieuXuat;
    JScrollPane scrollTablePhieuXuat;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    JComboBox<String> cbxChiNhanh;
    SelectForm cbxKhachHang, cbxNhanVien;
    InputDate dateStart, dateEnd;
    InputForm moneyMin, moneyMax;

    Main m;
    TaoPhieuXuat taoPhieuXuat;
    NhanVienDTO nv;
    String mcn;
    private boolean suppressBranchEvents;

    Color BackgroundColor = new Color(248, 249, 250);

    ArrayList<PhieuXuatDTO> listPhieuXuat;

    NhanVienBUS nvBUS = new NhanVienBUS();
    PhieuXuatBUS pxBUS = new PhieuXuatBUS();
    KhachHangBUS khachHangBUS = new KhachHangBUS();

    public PhieuXuat(Main m, NhanVienDTO nv) {
        this.m = m;
       this.nv=nv;
        initComponent();
        this.mcn=nv.getMCN();
        suppressBranchEvents = true;
        cbxChiNhanh.setSelectedItem(branchLabelForMcn(nv.getMCN()));
        suppressBranchEvents = false;
        this.listPhieuXuat = pxBUS.getAllByBranch(nv.getMCN());
        loadDataTalbe(this.listPhieuXuat);
    }

    private void initComponent() {

        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        initPadding();

        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new BorderLayout(10, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] action = {"create", "detail", "cancel", "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "phieuxuat", action);
        functionBar.add(mainFunction);

        //Add Event MouseListener
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }

        functionBar.add(mainFunction, BorderLayout.WEST);

        JPanel branchPanel = new JPanel(new BorderLayout());
        branchPanel.setBackground(Color.white);
        branchPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        cbxChiNhanh = new JComboBox<>(new String[]{"Tất cả chi nhánh", "Chi nhánh 1", "Chi nhánh 2", "Chi nhánh 3"});
        cbxChiNhanh.setPreferredSize(new Dimension(200, 35));
        cbxChiNhanh.addItemListener(this);
        branchPanel.add(cbxChiNhanh, BorderLayout.CENTER);
        functionBar.add(branchPanel, BorderLayout.CENTER);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã phiếu", "Khách hàng", "Nhân viên xuất"});
        search.cbxChoose.addItemListener(this);
        search.txtSearchForm.addKeyListener(this);
        search.btnReset.addActionListener(this);
        functionBar.add(search, BorderLayout.EAST);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        leftFunc();

        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
        contentCenter.add(main, BorderLayout.CENTER);

        tablePhieuXuat = new JTable();
        scrollTablePhieuXuat = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã phiếu xuất", "Khách hàng", "Nhân viên", "Thời gian", "Điểm tích lũy", "Tổng tiền", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        tablePhieuXuat.setModel(tblModel);
        tablePhieuXuat.setFocusable(false);
        tablePhieuXuat.setAutoCreateRowSorter(true);
        scrollTablePhieuXuat.setViewportView(tablePhieuXuat);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablePhieuXuat.setDefaultRenderer(Object.class, centerRenderer);
        scrollTablePhieuXuat.setViewportView(tablePhieuXuat);
        tablePhieuXuat.setFocusable(false);
        TableSorter.configureTableColumnSorter(tablePhieuXuat, 0, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tablePhieuXuat, 1, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tablePhieuXuat, 5, TableSorter.VND_CURRENCY_COMPARATOR);

        main.add(scrollTablePhieuXuat);

    }

    public void initPadding() {
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 20));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);

        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 20));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(20, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(20, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);
    }

    public void leftFunc() {
        box = new PanelBorderRadius();
        box.setPreferredSize(new Dimension(250, 0));
        box.setLayout(new GridLayout(6, 1, 10, 0));
        box.setBorder(new EmptyBorder(0, 5, 150, 5));
        contentCenter.add(box, BorderLayout.WEST);

        // Handel
        String[] listKh = khachHangBUS.getArrTenKhachHang();
        listKh = Stream.concat(Stream.of("Tất cả"), Arrays.stream(listKh)).toArray(String[]::new);
        String[] listNv = nvBUS.getArrTenNhanVien();
        listNv = Stream.concat(Stream.of("Tất cả"), Arrays.stream(listNv)).toArray(String[]::new);

        // init
        cbxKhachHang = new SelectForm("Khách hàng", listKh);
        cbxNhanVien = new SelectForm("Nhân viên xuất", listNv);
        dateStart = new InputDate("Từ ngày");
        dateEnd = new InputDate("Đến ngày");
        moneyMin = new InputForm("Từ số tiền (VND)");
        moneyMax = new InputForm("Đến số tiền (VND)");

        PlainDocument doc_min = (PlainDocument) moneyMin.getTxtForm().getDocument();
        doc_min.setDocumentFilter(new NumericDocumentFilter());

        PlainDocument doc_max = (PlainDocument) moneyMax.getTxtForm().getDocument();
        doc_max.setDocumentFilter(new NumericDocumentFilter());

        // add listener
        cbxKhachHang.getCbb().addItemListener(this);
        cbxNhanVien.getCbb().addItemListener(this);
        dateStart.getDateChooser().addPropertyChangeListener(this);
        dateEnd.getDateChooser().addPropertyChangeListener(this);
        moneyMin.getTxtForm().addKeyListener(this);
        moneyMax.getTxtForm().addKeyListener(this);

        box.add(cbxKhachHang);
        box.add(cbxNhanVien);
        box.add(dateStart);
        box.add(dateEnd);
        box.add(moneyMin);
        box.add(moneyMax);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == mainFunction.btn.get("create")) {
            taoPhieuXuat = new TaoPhieuXuat(m, nv, "create");
            m.setPanel(taoPhieuXuat);
        } else if (source == mainFunction.btn.get("detail")) {
            if (getRow() < 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu cần xem!");
            } else {
                new ChiTietPhieuDialog(m, "Thông tin phiếu xuất", true, pxBUS.getSelect(getRow()));
            }
        } else if (source == mainFunction.btn.get("cancel")) {
            if (tablePhieuXuat.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu!");
            } else {
                PhieuXuatDTO px = pxBUS.getSelect(tablePhieuXuat.getSelectedRow());
                
                // Kiểm tra trạng thái phiếu
                if (px.getTT() == 0) {
                    JOptionPane.showMessageDialog(null, "Phiếu đã bị hủy trước đó!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Hiển thị dialog nhập lý do hủy
                String lydohuy = JOptionPane.showInputDialog(null, 
                    "Vui lòng nhập lý do hủy phiếu:", 
                    "Lý do hủy", 
                    JOptionPane.QUESTION_MESSAGE);
                
                // Nếu user nhấn Cancel hoặc không nhập gì
                if (lydohuy == null || lydohuy.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập lý do hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int n = JOptionPane.showConfirmDialog(null, 
                    "Bạn có chắc muốn hủy phiếu này?\nLý do: " + lydohuy + "\nSố lượng sản phẩm sẽ được hoàn trả vào kho.", 
                    "Hủy phiếu", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                    
                if (n == JOptionPane.YES_OPTION) {
                    boolean success = pxBUS.cancel(px.getMP(), lydohuy.trim());
                        if (success) {
                        loadDataTalbe(pxBUS.getAllByBranch(nv.getMCN()));
                        Notification notification = new Notification(m, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Hủy phiếu thành công!");
                        notification.showNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "Hủy phiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else if (source == search.btnReset) {
            resetForm();
        } else if (source == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tablePhieuXuat);
            } catch (IOException ex) {
                Logger.getLogger(PhieuXuat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String branchLabelToMcn(String branchLabel) {
        if (branchLabel == null) {
            return "ALL";
        }
        return switch (branchLabel) {
            case "Chi nhánh 1" -> "CN1";
            case "Chi nhánh 2" -> "CN2";
            case "Chi nhánh 3" -> "CN3";
            case "Tất cả chi nhánh" -> "ALL";
            default -> "ALL";
        };
    }

    private String branchLabelForMcn(String mcn) {
        if ("CN1".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 1";
        }
        if ("CN2".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 2";
        }
        if ("CN3".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 3";
        }
        return "Tất cả chi nhánh";
    }

    private void refreshNhanVienFilter(String branchLabel) {
        String[] listNv = nvBUS.getAllByBranchLabel(branchLabel).stream()
                .map(DTO.NhanVienDTO::getHOTEN)
                .toArray(String[]::new);
        listNv = Stream.concat(Stream.of("Tất cả"), Arrays.stream(listNv)).toArray(String[]::new);
        cbxNhanVien.setArr(listNv);
    }

    private void reloadByBranch(String branchLabel, boolean reapplyFilters) {
        String mcnSelected = branchLabelToMcn(branchLabel);
        this.mcn = mcnSelected;
        suppressBranchEvents = true;
        refreshNhanVienFilter(branchLabel);
        cbxKhachHang.setSelectedIndex(0);
        cbxNhanVien.setSelectedIndex(0);
        suppressBranchEvents = false;

        this.listPhieuXuat = pxBUS.getAllByBranch(mcnSelected);

        if (reapplyFilters) {
            try {
                Fillter();
            } catch (ParseException ex) {
                Logger.getLogger(PhieuXuat.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            loadDataTalbe(this.listPhieuXuat);
        }
    }

    public void loadDataTalbe(ArrayList<PhieuXuatDTO> listphieuxuat) {
        tblModel.setRowCount(0);
        int size = listphieuxuat.size();

        for (int i = 0; i < size; i++) {
            String trangthaiString = "";
            switch (listphieuxuat.get(i).getTT()) {
                case 1 -> {
                    trangthaiString = "Đã duyệt";
                }
                case 0 -> {
                    trangthaiString = "Đã hủy";
                }
                case 2 -> {
                    trangthaiString = "Chờ xử lý";
                }
            }
            tblModel.addRow(new Object[]{
                i + 1,
                listphieuxuat.get(i).getMP(),
                khachHangBUS.getTenKhachHang(listphieuxuat.get(i).getMKH()),
                nvBUS.getNameById(listphieuxuat.get(i).getMNV(), listphieuxuat.get(i).getMCN()),
                Formater.FormatTime(listphieuxuat.get(i).getTG()),
                listphieuxuat.get(i).getDIEMTICHLUY(),
                Formater.FormatVND(listphieuxuat.get(i).getTIEN()), trangthaiString});
        }
    }

    public int getRow() {
        return tablePhieuXuat.getSelectedRow();
    }

    public void Fillter() throws ParseException {
        if (validateSelectDate()) {
            int makh = cbxKhachHang.getSelectedIndex() == 0 ? 0 : khachHangBUS.getByIndex(cbxKhachHang.getSelectedIndex() - 1).getMKH();
            int manv = cbxNhanVien.getSelectedIndex() == 0 ? 0 : nvBUS.getByIndex(cbxNhanVien.getSelectedIndex() - 1).getMNV();
            Date time_start = dateStart.getDate() != null ? dateStart.getDate() : new Date(0);
            Date time_end = dateEnd.getDate() != null ? dateEnd.getDate() : new Date(System.currentTimeMillis());
            String min_price = moneyMin.getText();
            String max_price = moneyMax.getText();
            String branchFilter = branchLabelToMcn((String) cbxChiNhanh.getSelectedItem());
            
            // Lọc theo các tiêu chí bộ lọc bên trái + chi nhánh đang chọn
            this.listPhieuXuat = pxBUS.fillerPhieuXuat(0, "", makh, manv, time_start, time_end, min_price, max_price, branchFilter);
            
            // Sau đó lọc theo tìm kiếm văn bản
            String searchText = search.txtSearchForm.getText().toLowerCase().trim();
            if (!searchText.isEmpty()) {
                ArrayList<PhieuXuatDTO> filteredList = new ArrayList<>();
                String searchType = (String) search.cbxChoose.getSelectedItem();
                
                for (PhieuXuatDTO px : listPhieuXuat) {
                    boolean match = false;
                    switch (searchType) {
                        case "Tất cả" -> {
                            match = String.valueOf(px.getMP()).toLowerCase().contains(searchText)
                                || khachHangBUS.getTenKhachHang(px.getMKH()).toLowerCase().contains(searchText)
                                || nvBUS.getNameById(px.getMNV(), px.getMCN()).toLowerCase().contains(searchText);
                        }
                        case "Mã phiếu" -> {
                            match = String.valueOf(px.getMP()).toLowerCase().contains(searchText);
                        }
                        case "Khách hàng" -> {
                            match = khachHangBUS.getTenKhachHang(px.getMKH()).toLowerCase().contains(searchText);
                        }
                        case "Nhân viên xuất" -> {
                            match = nvBUS.getNameById(px.getMNV(), px.getMCN()).toLowerCase().contains(searchText);
                        }
                    }
                    if (match) {
                        filteredList.add(px);
                    }
                }
                this.listPhieuXuat = filteredList;
            }
            
            loadDataTalbe(listPhieuXuat);
        }
    }

    public void resetForm() {
        suppressBranchEvents = true;
        cbxChiNhanh.setSelectedItem(branchLabelForMcn(nv.getMCN()));
        suppressBranchEvents = false;
        cbxKhachHang.setSelectedIndex(0);
        cbxNhanVien.setSelectedIndex(0);
        search.cbxChoose.setSelectedIndex(0);
        search.txtSearchForm.setText("");
        moneyMin.setText("");
        moneyMax.setText("");
        dateStart.getDateChooser().setCalendar(null);
        dateEnd.getDateChooser().setCalendar(null);
        this.listPhieuXuat = pxBUS.getAllByBranch(nv.getMCN());
        loadDataTalbe(listPhieuXuat);
    }

    public boolean validateSelectDate() throws ParseException {
        Date time_start = dateStart.getDate();
        Date time_end = dateEnd.getDate();

        Date current_date = new Date();
        if (time_start != null && time_start.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateStart.getDateChooser().setCalendar(null);
            return false;
        }
        if (time_end != null && time_end.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateEnd.getDateChooser().setCalendar(null);
            return false;
        }
        if (time_start != null && time_end != null && time_start.after(time_end)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn ngày bắt đầu", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateEnd.getDateChooser().setCalendar(null);
            return false;
        }
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            Fillter();
        } catch (ParseException ex) {
            Logger.getLogger(PhieuXuat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            Fillter();
        } catch (ParseException ex) {
            Logger.getLogger(PhieuXuat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        try {
            if (suppressBranchEvents) {
                return;
            }
            if (e.getSource() == cbxChiNhanh && e.getStateChange() == ItemEvent.SELECTED) {
                reloadByBranch((String) cbxChiNhanh.getSelectedItem(), true);
                return;
            }
            Fillter();
        } catch (ParseException ex) {
            Logger.getLogger(PhieuXuat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}