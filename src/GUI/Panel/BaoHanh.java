package GUI.Panel;

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
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import BUS.KhachHangBUS;
import BUS.PhieuBaoHanhBUS;
import BUS.PhieuSuaChuaBUS;
import BUS.SanPhamBUS;
import DTO.PhieuBaoHanhDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietPhieuBaoHanhDialog;
import GUI.Dialog.PhieuBaoHanhDialog;
import helper.JTableExporter;

public class BaoHanh extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableBaoHanh;
    JScrollPane scrollTableBaoHanh;
    MainFunction mainFunction;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    IntegratedSearch search;
    DefaultTableModel tblModel;
    
    public PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
    public PhieuSuaChuaBUS pscBUS = new PhieuSuaChuaBUS();
    public SanPhamBUS spBUS = new SanPhamBUS();
    public KhachHangBUS khBUS = new KhachHangBUS();
    
    public ArrayList<PhieuBaoHanhDTO> listPBH = pbhBUS.getAll();
    
    Main m;
    Color BackgroundColor = new Color(248, 249, 250);

    public BaoHanh(Main m) {
        this.m = m;
        initComponent();
        tableBaoHanh.setDefaultEditor(Object.class, null);
        loadDataTable(listPBH);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Khởi tạo bảng
        tableBaoHanh = new JTable();
        scrollTableBaoHanh = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã phiếu BH", "Mã hóa đơn", "Tên sản phẩm", "Tên khách hàng", 
                                        "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        tableBaoHanh.setModel(tblModel);
        tableBaoHanh.setFocusable(false);
        tableBaoHanh.setDefaultEditor(Object.class, null);
        scrollTableBaoHanh.setViewportView(tableBaoHanh);
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < header.length; i++) {
            tableBaoHanh.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableBaoHanh.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableBaoHanh, 0, TableSorter.INTEGER_COMPARATOR);

        // Padding panels
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

        // Content center
        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        // Function bar
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] action = {"update", "detail", "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "baohanh", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        // Search panel
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã phiếu bảo hành", "Mã hóa đơn", 
                                                    "Mã sản phẩm", "Mã khách hàng", "Còn hạn", "Hết hạn", "Đã hủy"});
        search.cbxChoose.addItemListener(this);
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                listPBH = pbhBUS.search(txt, type);
                loadDataTable(listPBH);
            }
        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            listPBH = pbhBUS.getAll();
            loadDataTable(listPBH);
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        // Main panel
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
        contentCenter.add(main, BorderLayout.CENTER);
        main.add(scrollTableBaoHanh);
    }

    public void loadDataTable(ArrayList<PhieuBaoHanhDTO> result) {
        tblModel.setRowCount(0);
        for (PhieuBaoHanhDTO pbh : result) {
            String tenSP = pbhBUS.getTenSanPham(pbh.getMSP());
            String tenKH = pbhBUS.getTenKhachHang(pbh.getMKH());
            String trangThai = getTrangThaiText(pbh);
            
            tblModel.addRow(new Object[]{
                pbh.getMPB(),
                pbh.getMPX(),
                tenSP,
                tenKH,
                pbh.getNGAYBATDAU(),
                pbh.getNGAYKETTHUC(),
                trangThai
            });
        }
    }

    private String getTrangThaiText(PhieuBaoHanhDTO pbh) {
        Date currentDate = new Date(System.currentTimeMillis());
        if (pbh.getTRANGTHAI() == 2) {
            return "Đã hủy";
        } else if (pbh.getTRANGTHAI() == 0) {
            return "Hết hạn";
        } else if (pbh.getNGAYKETTHUC().before(currentDate)) {
            return "Hết hạn";
        } else {
            return "Còn hạn";
        }
    }

    public int getSelectedWarrantyId() {
        int row = tableBaoHanh.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu bảo hành");
            return -1;
        }
        return (int) tblModel.getValueAt(row, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("update")) {
            int mpb = getSelectedWarrantyId();
            if (mpb != -1) {
                PhieuBaoHanhDTO pbh = pbhBUS.getByMaPhieuBaoHanh(mpb);
                if (pbh != null) {
                    // Mở dialog chỉnh sửa phiếu bảo hành
                    new PhieuBaoHanhDialog(this, owner, "Cập nhật phiếu bảo hành", true, pbh);
                    listPBH = pbhBUS.getAll();
                    loadDataTable(listPBH);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int mpb = getSelectedWarrantyId();
            if (mpb != -1) {
                PhieuBaoHanhDTO pbh = pbhBUS.getByMaPhieuBaoHanh(mpb);
                if (pbh != null) {
                    new ChiTietPhieuBaoHanhDialog(owner, "Chi tiết phiếu bảo hành", true, pbh);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableBaoHanh);
            } catch (IOException ex) {
                Logger.getLogger(BaoHanh.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String type = (String) search.cbxChoose.getSelectedItem();
        String txt = search.txtSearchForm.getText();
        listPBH = pbhBUS.search(txt, type);
        loadDataTable(listPBH);
    }
}
