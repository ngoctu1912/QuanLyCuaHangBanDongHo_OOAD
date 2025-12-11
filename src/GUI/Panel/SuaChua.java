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
import BUS.NhanVienBUS;
import BUS.PhieuBaoHanhBUS;
import BUS.PhieuSuaChuaBUS;
import BUS.SanPhamBUS;
import DTO.PhieuSuaChuaDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietSuaChuaDialog;
import GUI.Dialog.ListPhieuBaoHanh;
import GUI.Dialog.SuaChuaDialog;
import helper.JTableExporter;

public class SuaChua extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableSuaChua;
    JScrollPane scrollTableSuaChua;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    
    public PhieuSuaChuaBUS pscBUS = new PhieuSuaChuaBUS();
    public PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
    public SanPhamBUS spBUS = new SanPhamBUS();
    public KhachHangBUS khBUS = new KhachHangBUS();
    public NhanVienBUS nvBUS = new NhanVienBUS();
    
    public ArrayList<PhieuSuaChuaDTO> listPSC = pscBUS.getAll();
    
    Main m;
    Color BackgroundColor = new Color(248, 249, 250);

    public SuaChua(Main m) {
        this.m = m;
        initComponent();
        tableSuaChua.setDefaultEditor(Object.class, null);
        loadDataTable(listPSC);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Khởi tạo bảng
        tableSuaChua = new JTable();
        scrollTableSuaChua = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã sửa chữa", "Mã phiếu BH", "Nhân viên", 
                                        "Ngày nhận", "Ngày trả", "Tình trạng", "Chi phí"};
        tblModel.setColumnIdentifiers(header);
        tableSuaChua.setModel(tblModel);
        tableSuaChua.setFocusable(false);
        tableSuaChua.setDefaultEditor(Object.class, null);
        scrollTableSuaChua.setViewportView(tableSuaChua);
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < header.length; i++) {
            tableSuaChua.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableSuaChua.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableSuaChua, 0, TableSorter.INTEGER_COMPARATOR);

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

        String[] action = {"create", "update", "delete", "detail", "export"};
        mainFunction = new MainFunction(m.user.getMNQ(), "suachua", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        // Search panel
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã sửa chữa", "Mã phiếu bảo hành", 
                                                    "Chờ xử lý", "Đang sửa", "Hoàn thành", "Không sửa được", "Đã hủy"});
        search.cbxChoose.addItemListener(this);
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                listPSC = pscBUS.search(txt, type);
                loadDataTable(listPSC);
            }
        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            listPSC = pscBUS.getAll();
            loadDataTable(listPSC);
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        // Main panel
        main = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
        contentCenter.add(main, BorderLayout.CENTER);
        main.add(scrollTableSuaChua);
    }

    public void loadDataTable(ArrayList<PhieuSuaChuaDTO> result) {
        tblModel.setRowCount(0);
        
        // Sắp xếp theo mã sửa chữa tăng dần
        result.sort((psc1, psc2) -> Integer.compare(psc1.getMSC(), psc2.getMSC()));
        
        for (PhieuSuaChuaDTO psc : result) {
            String tenNV = pscBUS.getTenNhanVien(psc.getMNV());
            String tinhTrang = psc.getTenTinhTrang();
            String chiPhi = psc.getCHIPHI() != null ? psc.getCHIPHI().toString() + " VNĐ" : "0 VNĐ";
            String ngayTra = psc.getNGAYTRA() != null ? psc.getNGAYTRA().toString() : "Chưa trả";
            
            tblModel.addRow(new Object[]{
                psc.getMSC(),
                psc.getMPB(),
                tenNV,
                psc.getNGAYNHAN(),
                ngayTra,
                tinhTrang,
                chiPhi
            });
        }
    }

    public int getSelectedRepairId() {
        int row = tableSuaChua.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu sửa chữa");
            return -1;
        }
        return (int) tblModel.getValueAt(row, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ListPhieuBaoHanh(this, owner, "Chọn phiếu bảo hành", true);
            listPSC = pscBUS.getAll();
            loadDataTable(listPSC);
        } else if (e.getSource() == mainFunction.btn.get("update")) {
            int msc = getSelectedRepairId();
            if (msc != -1) {
                PhieuSuaChuaDTO psc = pscBUS.getByMaPhieuSuaChua(msc);
                if (psc != null) {
                    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                    new SuaChuaDialog(this, owner, "Cập nhật phiếu sửa chửa", true, "update", psc);
                    listPSC = pscBUS.getAll();
                    loadDataTable(listPSC);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            int msc = getSelectedRepairId();
            if (msc != -1) {
                PhieuSuaChuaDTO psc = pscBUS.getByMaPhieuSuaChua(msc);
                if (psc != null) {
                    // Kiểm tra nếu đã hủy rồi thì không cho hủy nữa
                    if (psc.getTINHTRANG() == 4) {
                        JOptionPane.showMessageDialog(this, "Phiếu sửa chữa đã bị hủy trước đó!", 
                            "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // Kiểm tra nếu đang sửa thì không cho hủy
                    if (psc.getTINHTRANG() == 1) {
                        JOptionPane.showMessageDialog(this, "Không thể hủy phiếu sửa chữa đang trong quá trình sửa chữa!", 
                            "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "Bạn có chắc chắn muốn hủy phiếu sửa chữa " + msc + "?\n" +
                        "Phiếu sẽ được đánh dấu là 'Đã hủy' và vẫn lưu trong hệ thống.", 
                        "Xác nhận hủy", 
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (pscBUS.updateTinhTrang(msc, 4)) {
                            JOptionPane.showMessageDialog(this, "Hủy phiếu sửa chữa thành công!");
                            listPSC = pscBUS.getAll();
                            loadDataTable(listPSC);
                        } else {
                            JOptionPane.showMessageDialog(this, "Hủy phiếu sửa chữa thất bại!", 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int msc = getSelectedRepairId();
            if (msc != -1) {
                // Lấy lại thông tin mới nhất từ database
                PhieuSuaChuaDTO psc = pscBUS.getByMaPhieuSuaChua(msc);
                if (psc != null) {
                    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                    new ChiTietSuaChuaDialog(owner, psc, true);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableSuaChua);
            } catch (IOException ex) {
                Logger.getLogger(SuaChua.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String type = (String) search.cbxChoose.getSelectedItem();
        String txt = search.txtSearchForm.getText();
        listPSC = pscBUS.search(txt, type);
        loadDataTable(listPSC);
    }
    
    public void refreshTable() {
        listPSC = pscBUS.getAll();
        loadDataTable(listPSC);
    }
}
