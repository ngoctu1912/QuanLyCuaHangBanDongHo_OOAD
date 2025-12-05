package GUI.Dialog;

import BUS.DonViBUS;
import BUS.NhomQuyenBUS;
import BUS.SanPhamBUS;
import DTO.DonViDTO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Panel.ThuocTinhSanPham;
import helper.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class DonViDialog extends JDialog implements MouseListener {

    ThuocTinhSanPham qlttsp;
    HeaderTitle headTite;
    JPanel top, main, bottom, all;
    InputForm ms;
    DefaultTableModel tblModel;
    JTable table;
    JScrollPane scrollTable;
    ButtonCustom add, del, update;
    DonViBUS msBUS = new DonViBUS();
    ArrayList<DonViDTO> list = msBUS.getAll();
    private final NhomQuyenBUS nhomquyenBus = new NhomQuyenBUS();
    DonViBUS dvBus = new DonViBUS();
    SanPhamBUS spBus = new SanPhamBUS();

    public DonViDialog(JFrame onwer, ThuocTinhSanPham qltt, String title, boolean modal, int nhomquyen) {
        super(onwer, title, modal);
        initComponent(qltt);
        loadQuyen(nhomquyen);
        loadDataTable(list);
    }

    public void loadQuyen(int nhomquyen) {
        if (!nhomquyenBus.checkPermisson(nhomquyen, "thuoctinh", "create")) {
            add.setVisible(false);
        }
        if (!nhomquyenBus.checkPermisson(nhomquyen, "thuoctinh", "delete")) {
            del.setVisible(false);
        }
        if (!nhomquyenBus.checkPermisson(nhomquyen, "thuoctinh", "update")) {
            update.setVisible(false);
        }
    }

    public void initComponent(ThuocTinhSanPham qlttsp) {
        this.qlttsp = qlttsp;
        this.setSize(new Dimension(425, 500));
        this.setLayout(new BorderLayout(0, 0));
        this.setResizable(false);
        headTite = new HeaderTitle("ĐƠN VỊ SẢN PHẨM");
        this.setBackground(Color.white);
        top = new JPanel();
        main = new JPanel();
        bottom = new JPanel();

        top.setLayout(new GridLayout(1, 1));
        top.setBackground(Color.WHITE);
        top.setPreferredSize(new Dimension(0, 70));
        top.add(headTite);

        main.setBackground(Color.WHITE);
        main.setPreferredSize(new Dimension(420, 200));
        ms = new InputForm("Tên đơn vị");
        ms.setPreferredSize(new Dimension(250, 70));
        table = new JTable();
        table.setBackground(Color.WHITE);
        table.addMouseListener(this);
        scrollTable = new JScrollPane(table);
        scrollTable.setBackground(Color.WHITE);
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã đơn vị", "Tên đơn vị"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setDefaultEditor(Object.class, null);
        scrollTable.setViewportView(table);
        scrollTable.setPreferredSize(new Dimension(420, 250));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setFocusable(false);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        main.add(ms);
        main.add(scrollTable);

        add = new ButtonCustom("Thêm", "excel", 15, 100, 40);
        add.addMouseListener(this);
        del = new ButtonCustom("Xóa", "danger", 15, 100, 40);
        del.addMouseListener(this);
        update = new ButtonCustom("Sửa", "success", 15, 100, 40);
        update.addMouseListener(this);
        bottom.setBackground(Color.white);
        bottom.setLayout(new FlowLayout(1, 20, 20));
        bottom.add(add);
        bottom.add(del);
        bottom.add(update);
        bottom.setPreferredSize(new Dimension(0, 70));

        this.add(top, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
    }

    public void loadDataTable(ArrayList<DonViDTO> result) {
        tblModel.setRowCount(0);
        for (DonViDTO ncc : result) {
            tblModel.addRow(new Object[]{
                ncc.getMDV(), ncc.getTENDV()
            });
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == add) {
            if (Validation.isEmpty(ms.getText())) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đơn vị mới");
            } else {
                String tenmau = ms.getText();
                if (msBUS.checkDup(tenmau)) {
                    int id = msBUS.getMaxMaDonVi() + 1;
                    msBUS.add(new DonViDTO(id, tenmau));
                    loadDataTable(list);
                    JOptionPane.showMessageDialog(this, "Thêm đơn vị thành công!");
                    ms.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Đơn vị đã tồn tại !");
                }
            }
        } else if (e.getSource() == del) {
            int index = getRowSelected();
            if (index != -1) {
                String maDonVi = String.valueOf(table.getValueAt(index, 0));
                int choose = JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có chắc chắn muốn xóa đơn vị: " + maDonVi + "?",
                        "Xác nhận xoá",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (choose == JOptionPane.YES_OPTION) {
                    DonViDTO choosenDonVi = dvBus.getById(maDonVi);
                    ArrayList<SanPhamDTO> listSPTheoDonVi = spBus.getSPByMaDonVi(choosenDonVi.getMDV());
                    if (!listSPTheoDonVi.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Đơn vị đã được sử dụng trong danh mục sản phẩm, không thể xoá!");
                        return;
                    } else {
                        msBUS.delete(choosenDonVi, index);
                        loadDataTable(list);
                        JOptionPane.showMessageDialog(this, "Xoá đơn vị thành công !");
                        ms.setText("");
                    }

                }
            }
        } else if (e.getSource() == update) {
            int index = getRowSelected();
            if (index != -1) {
                if (Validation.isEmpty(ms.getText())) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đơn vị mới");
                } else {
                    String tenmau = ms.getText();
                    if (msBUS.checkDup(tenmau)) {
                        msBUS.update(new DonViDTO(list.get(index).getMDV(), tenmau));
                        loadDataTable(list);
                        JOptionPane.showMessageDialog(this, "Sửa đơn vị thành công");
                        ms.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Đơn vị đã tồn tại !");
                    }
                }
            }
        } else if (e.getSource() == table) {
            int index = table.getSelectedRow();
            ms.setText(list.get(index).getTENDV());
        }
    }

    public int getRowSelected() {
        int index = table.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn vị!");
        }
        return index;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
