package GUI.Dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import BUS.KhachHangBUS;
import BUS.PhieuBaoHanhBUS;
import BUS.SanPhamBUS;
import DTO.KhachHangDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Panel.SuaChua;

public class ListPhieuBaoHanh extends JDialog {

    private SuaChua guiSuaChua;
    private JTable tablePhieuBaoHanh;
    private JScrollPane scrollTable;
    private DefaultTableModel tblModel;
    private PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();
    private KhachHangBUS khBUS = new KhachHangBUS();
    private ArrayList<PhieuBaoHanhDTO> listPBH;
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    
    public ListPhieuBaoHanh(SuaChua suaChua, JFrame owner, String title, boolean modal){
        super(owner, title, modal);
        this.guiSuaChua = suaChua;
        // Chỉ lấy các phiếu bảo hành còn hiệu lực hoặc tất cả
        listPBH = pbhBUS.getAll();
        init();
    }

    public void init(){
        this.setSize(new Dimension(1000, 650));
        this.setLayout(new BorderLayout());
        
        // Panel tìm kiếm
        JPanel panelSearch = new JPanel(new BorderLayout());
        panelSearch.setSize(new Dimension(0, 80));
        panelSearch.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel jLabelSearch = new JLabel("Tìm kiếm  ");
        jLabelSearch.setSize(new Dimension(100, 0));
        
        JTextField jTextFieldSearch = new JTextField();
        jTextFieldSearch.setFont(new Font(FlatRobotoFont.FAMILY, 0, 13));
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo mã phiếu BH, mã hóa đơn, tên sản phẩm, tên khách hàng...");
        jTextFieldSearch.putClientProperty("JTextField.showClearButton", true);
        jTextFieldSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String txt = jTextFieldSearch.getText();
                listPBH = search(txt);
                loadDataTable(listPBH);
            }
        });
        
        ButtonCustom buttonAdd = new ButtonCustom("Chọn phiếu bảo hành", "success", 14);
        buttonAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = getRow();
                if(selectedRow < 0){
                    JOptionPane.showMessageDialog(null, 
                        "Vui lòng chọn phiếu bảo hành!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                } else {
                    PhieuBaoHanhDTO pbh = listPBH.get(selectedRow);
                    
                    // Kiểm tra phiếu bảo hành còn hạn không
                    Date currentDate = new Date(System.currentTimeMillis());
                    if (pbh.getTRANGTHAI() == 2) {
                        JOptionPane.showMessageDialog(null, 
                            "Phiếu bảo hành đã bị hủy. Không thể tạo phiếu sửa chữa!",
                            "Thông báo", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (pbh.getTRANGTHAI() == 0 || pbh.getNGAYKETTHUC().before(currentDate)) {
                        int confirm = JOptionPane.showConfirmDialog(null, 
                            "Phiếu bảo hành đã hết hạn. Bạn vẫn muốn tạo phiếu sửa chữa?",
                            "Cảnh báo", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    
                    dispose();
                    new SuaChuaDialog(guiSuaChua, (JFrame) getOwner(), "Thêm phiếu sửa chữa", true, "create", pbh);
                }
            }
        });
        
        panelSearch.add(jLabelSearch, BorderLayout.WEST);
        panelSearch.add(jTextFieldSearch, BorderLayout.CENTER);
        panelSearch.add(buttonAdd, BorderLayout.EAST);
        this.add(panelSearch, BorderLayout.NORTH);
        
        // Panel bảng
        JPanel jPanelTable = new JPanel();
        jPanelTable.setBorder(new EmptyBorder(0, 20, 20, 20));
        jPanelTable.setLayout(new GridLayout(1, 1));
        
        tablePhieuBaoHanh = new JTable();
        tablePhieuBaoHanh.setFocusable(false);
        tablePhieuBaoHanh.setFont(new Font("Segoe UI", 0, 14));
        tablePhieuBaoHanh.setDefaultEditor(Object.class, null);
        tablePhieuBaoHanh.setRowHeight(35);
        
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã PBH", "Mã HĐ", "Sản phẩm", "Khách hàng", "Ngày BĐ", "Ngày KT", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablePhieuBaoHanh.setDefaultRenderer(Object.class, centerRenderer);
        tablePhieuBaoHanh.setModel(tblModel);
        
        scrollTable = new JScrollPane(tablePhieuBaoHanh);
        jPanelTable.add(scrollTable);
        this.add(jPanelTable, BorderLayout.CENTER);
        
        loadDataTable(listPBH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public int getRow(){
        return tablePhieuBaoHanh.getSelectedRow();
    }
    
    public void loadDataTable(ArrayList<PhieuBaoHanhDTO> list) {
        listPBH = list;
        tblModel.setRowCount(0);
        for (PhieuBaoHanhDTO pbh : listPBH) {
            String tenSP = "";
            SanPhamDTO sp = spBUS.getByMaSP(pbh.getMSP());
            if (sp != null) {
                tenSP = sp.getTEN();
            }
            
            String tenKH = "";
            KhachHangDTO kh = khBUS.getKhachHangById(pbh.getMKH());
            if (kh != null) {
                tenKH = kh.getHOTEN();
            }
            
            // Kiểm tra trạng thái
            Date currentDate = new Date(System.currentTimeMillis());
            String trangThai;
            if (pbh.getTRANGTHAI() == 2) {
                trangThai = "Đã hủy";
            } else if (pbh.getTRANGTHAI() == 0) {
                trangThai = "Hết hạn";
            } else if (pbh.getNGAYKETTHUC().before(currentDate)) {
                trangThai = "Hết hạn";
            } else {
                trangThai = "Còn hạn";
            }
            
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
    
    public ArrayList<PhieuBaoHanhDTO> search(String text) {
        if (text.length() > 0) {
            text = text.toLowerCase();
            ArrayList<PhieuBaoHanhDTO> result = new ArrayList<>();
            
            for (PhieuBaoHanhDTO pbh : pbhBUS.getAll()) {
                // Tìm theo mã phiếu BH
                if (String.valueOf(pbh.getMPB()).contains(text)) {
                    result.add(pbh);
                    continue;
                }
                
                // Tìm theo mã hóa đơn
                if (String.valueOf(pbh.getMPX()).contains(text)) {
                    result.add(pbh);
                    continue;
                }
                
                // Tìm theo tên sản phẩm
                SanPhamDTO sp = spBUS.getByMaSP(pbh.getMSP());
                if (sp != null && sp.getTEN().toLowerCase().contains(text)) {
                    result.add(pbh);
                    continue;
                }
                
                // Tìm theo tên khách hàng
                KhachHangDTO kh = khBUS.getKhachHangById(pbh.getMKH());
                if (kh != null && kh.getHOTEN().toLowerCase().contains(text)) {
                    result.add(pbh);
                    continue;
                }
            }
            return result;
        } else {
            return pbhBUS.getAll();
        }
    }
}
