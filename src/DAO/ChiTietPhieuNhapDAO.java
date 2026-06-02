package DAO;

import DAO.KhoDAO;
import DAO.TonKhoDAO;
import DTO.ChiTietPhieuNhapDTO;
import DTO.khoDTO;
import DTO.TonKhoDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

public class ChiTietPhieuNhapDAO implements ChiTietInterface<ChiTietPhieuNhapDTO> {

    public static ChiTietPhieuNhapDAO getInstance() {
        return new ChiTietPhieuNhapDAO();
    }

    @Override
    public int insert(ArrayList<ChiTietPhieuNhapDTO> t) {
        // Không sử dụng, hãy sử dụng insertWithMCN thay vì
        return 0;
    }

    /**
     * Thêm chi tiết phiếu nhập và cập nhật tồn kho
     * @param t Danh sách chi tiết phiếu nhập
     * @param mcn Mã chi nhánh để xác định kho
     * @return Số hàng được insert
     */
    public int insertWithMCN(ArrayList<ChiTietPhieuNhapDTO> t, String mcn) {
        int result = 0;
        
        // Lấy kho mặc định của chi nhánh
        KhoDAO khoDAO = new KhoDAO();
        khoDTO kho = khoDAO.getDefaultKhoByCN(mcn);
        
        if (kho == null) {
            Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.WARNING, 
                "Không tìm thấy kho cho chi nhánh: " + mcn);
            return 0;
        }
        
        int mkho = kho.getMKHO();
        TonKhoDAO tonKhoDAO = new TonKhoDAO();
        
        for (int i = 0; i < t.size(); i++) {
            try {
                Connection con = (Connection) JDBCUtil.getConnection();
                String sql = "INSERT INTO [CTPHIEUNHAP] ([MPN], [MSP], [SL], [TIENNHAP], [HINHTHUC]) VALUES (?,?,?,?,?)";
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                pst.setInt(1, t.get(i).getMP());
                pst.setInt(2, t.get(i).getMSP());
                pst.setInt(3, t.get(i).getSL());
                pst.setInt(4, t.get(i).getTIEN());
                pst.setInt(5, t.get(i).getHINHTHUC());
                result = pst.executeUpdate();
                JDBCUtil.closeConnection(con);
                
                // Cập nhật tồn kho
                if (result > 0) {
                    int msp = t.get(i).getMSP();
                    int sl = t.get(i).getSL();
                    
                    // Kiểm tra xem đã có tồn kho cho sản phẩm này ở kho này chưa
                    TonKhoDTO existingTonKho = tonKhoDAO.getTonKhoByMSPAndMKHO(msp, mkho);
                    
                    if (existingTonKho != null) {
                        // Nếu đã tồn tại, cập nhật
                        tonKhoDAO.updateSoLuong(msp, mkho, sl);
                    } else {
                        // Nếu chưa tồn tại, thêm mới
                        TonKhoDTO newTonKho = new TonKhoDTO(msp, mkho, sl);
                        tonKhoDAO.insert(newTonKho);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM CTPHIEUNHAP WHERE MPN = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(ArrayList<ChiTietPhieuNhapDTO> t, String pk) {
        // Không sử dụng, hãy sử dụng updateWithMCN thay vì
        return 0;
    }

    /**
     * Cập nhật chi tiết phiếu nhập với MCN
     */
    public int updateWithMCN(ArrayList<ChiTietPhieuNhapDTO> t, String pk, String mcn) {
        int result = this.delete(pk);
        if (result != 0) {
            result = this.insertWithMCN(t, mcn);
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietPhieuNhapDTO> selectAll(String t) {
        return selectAll(t, JDBCUtil.getCurrentMcn());
    }

    public ArrayList<ChiTietPhieuNhapDTO> selectAll(String t, String mcn) {
        ArrayList<ChiTietPhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String normalizedMcn = mcn == null ? "" : mcn.trim().toUpperCase();
            String currentMcn = JDBCUtil.getCurrentMcn();
            String tableSource;
            if (normalizedMcn.isBlank() || "ALL".equalsIgnoreCase(normalizedMcn) || (currentMcn != null && currentMcn.trim().equalsIgnoreCase(normalizedMcn))) {
                tableSource = "CTPHIEUNHAP";
            } else {
                tableSource = "[" + normalizedMcn + "].quanlycuahangdongho.dbo.CTPHIEUNHAP";
            }
            String sql = "SELECT * FROM " + tableSource + " WHERE MPN = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("MPN");
                int masp = rs.getInt("MSP");
                int soluong = rs.getInt("SL");
                int tiennhap = rs.getInt("TIENNHAP");
                int hinhthucnhap = rs.getInt("HINHTHUC");
                ChiTietPhieuNhapDTO ctphieu = new ChiTietPhieuNhapDTO(maphieu, masp, soluong, tiennhap, hinhthucnhap);
                result.add(ctphieu);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public ArrayList<ChiTietPhieuNhapDTO> selectCtpnByMaSanPham(String t) {
        ArrayList<ChiTietPhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTPHIEUNHAP WHERE MSP = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("MPN");
                int masp = rs.getInt("MSP");
                int soluong = rs.getInt("SL");
                int tiennhap = rs.getInt("TIENNHAP");
                int hinhthucnhap = rs.getInt("HINHTHUC");
                ChiTietPhieuNhapDTO ctphieu = new ChiTietPhieuNhapDTO(maphieu, masp, soluong, tiennhap, hinhthucnhap);
                result.add(ctphieu);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public int getGiaSanPhamByMaxMPN(String msp) {
        int gia = -1; // Giá trị mặc định nếu không tìm thấy
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT TIENNHAP FROM CTPHIEUNHAP WHERE MSP = ? AND MPN = (SELECT MAX(MPN) FROM CTPHIEUNHAP WHERE MSP = ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, msp);
            pst.setString(2, msp);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                gia = rs.getInt("TIENNHAP");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return gia;
    }

    public ArrayList<ChiTietPhieuNhapDTO> searchByMSP(int msp) {
        ArrayList<ChiTietPhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTPHIEUNHAP WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msp); 
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int maphieu = rs.getInt("MPN");
                int masp = rs.getInt("MSP");
                int soluong = rs.getInt("SL");
                int tiennhap = rs.getInt("TIENNHAP");
                int hinhthucnhap = rs.getInt("HINHTHUC");
                ChiTietPhieuNhapDTO ctphieu = new ChiTietPhieuNhapDTO(maphieu, masp, soluong, tiennhap, hinhthucnhap);
                result.add(ctphieu);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

}
