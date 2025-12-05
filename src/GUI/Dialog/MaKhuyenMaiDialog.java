package GUI.Dialog;

import BUS.SanPhamBUS;
import BUS.MaKhuyenMaiBUS;
import DAO.SanPhamDAO;
import DTO.ChiTietMaKhuyenMaiDTO;
import DTO.SanPhamDTO;
import DTO.MaKhuyenMaiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import helper.Formater;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class MaKhuyenMaiDialog extends JDialog implements ActionListener {

    HeaderTitle titlePage;
    JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_btn; //bỏ pnmain_bottom_right, pnmain_bottom_left 
    InputForm txtMaPhieu, txtThoiGianBD, txtThoiGianKT;
    DefaultTableModel tblModel;
    JTable table, tblImei;
    JScrollPane scrollTable;

    MaKhuyenMaiDTO makhuyenmai;
    SanPhamBUS spBus = new SanPhamBUS();
    MaKhuyenMaiBUS makhuyenmaiBus;

    ButtonCustom btnHuyBo;

    ArrayList<ChiTietMaKhuyenMaiDTO> chitietmkm;

    public MaKhuyenMaiDialog(JFrame owner, String title, boolean modal, MaKhuyenMaiDTO phieunhapDTO) {
        super(owner, title, modal);
        this.makhuyenmai = phieunhapDTO;
        makhuyenmaiBus = new MaKhuyenMaiBUS();
        chitietmkm = makhuyenmaiBus.getChiTietMKM(phieunhapDTO.getMKM());
        // chitietsanpham = ctspBus.getByMaSP(phieunhapDTO.getMP());
        initComponent(title);
        initPhieuNhap();
        loadDataTable(chitietmkm);
        this.setVisible(true);
    }

    public void initPhieuNhap() {
        txtMaPhieu.setText(this.makhuyenmai.getMKM());
        txtThoiGianBD.setText(Formater.FormatTime(makhuyenmai.getTGBD()));
        txtThoiGianKT.setText(Formater.FormatTime(makhuyenmai.getTGKT()));
    }


    public void loadDataTable(ArrayList<ChiTietMaKhuyenMaiDTO> ctMkm) {
        tblModel.setRowCount(0);
        for (int i = 0; i < ctMkm.size(); i++) {
            SanPhamDTO sp = spBus.getByMaSP(ctMkm.get(i).getMSP());
            tblModel.addRow(new Object[]{
                i + 1, sp.getMSP(), SanPhamDAO.getInstance().selectById(sp.getMSP()+"").getTEN(), 
                ctMkm.get(i).getPTG() + "%"
            });
        }
    }


    public void initComponent(String title) {
        this.setSize(new Dimension(1100, 500));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());

        pnmain = new JPanel(new BorderLayout());

        pnmain_top = new JPanel(new GridLayout(1, 4));
        txtMaPhieu = new InputForm("Mã phiếu");
        txtThoiGianBD = new InputForm("Thời gian bắt đầu");
        txtThoiGianKT = new InputForm("Thời gian kết thúc");

        txtMaPhieu.setEditable(false);
        txtThoiGianBD.setEditable(false);
        txtThoiGianKT.setEditable(false);

        pnmain_top.add(txtMaPhieu);
        pnmain_top.add(txtThoiGianBD);
        pnmain_top.add(txtThoiGianKT);

        pnmain_bottom = new JPanel(new GridLayout(1, 5));
        pnmain_bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnmain_bottom.setBackground(Color.WHITE);

        // pnmain_bottom_left = new JPanel(new GridLayout(1, 1));
        table = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên SP", "Phần trăm giảm"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        scrollTable.setViewportView(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
    
        pnmain_bottom.add(scrollTable);


        pnmain_btn = new JPanel(new FlowLayout());
        pnmain_btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnmain_btn.setBackground(Color.white);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnHuyBo.addActionListener(this);
        pnmain_btn.add(btnHuyBo);

        pnmain.add(pnmain_top, BorderLayout.NORTH);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);
        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnHuyBo) {
            dispose();
        }
    }
}
