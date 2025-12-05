package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.ChiTietPhieuXuatDTO;
import config.JDBCUtil;

public class ChiTietPhieuXuatDAO implements ChiTietInterface<ChiTietPhieuXuatDTO> {

    public static ChiTietPhieuXuatDAO getInstance() {
        return new ChiTietPhieuXuatDAO();
    }

    @Override
    public int insert(ArrayList<ChiTietPhieuXuatDTO> t) {
        int result = 0;
        for (int i = 0; i < t.size(); i++) {
            try {
                Connection con = (Connection) JDBCUtil.getConnection();
                // Cập nhật số lượng tồn trước
                int SL = -(t.get(i).getSL());
                SanPhamDAO.getInstance().updateSoLuongTon(t.get(i).getMSP(), SL);
                
                // Sau đó insert chi tiết phiếu xuất
                String sql = "INSERT INTO `CTPHIEUXUAT` (`MHD`, `MSP`, `SL`, `TIENXUAT`, `MKM`) VALUES (?,?,?,?,?)";
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                pst.setInt(1, t.get(i).getMP());
                pst.setInt(2, t.get(i).getMSP());
                pst.setInt(3, t.get(i).getSL());
                pst.setInt(4, t.get(i).getTIEN());
                pst.setString(5, t.get(i).getMKM());
                result = pst.executeUpdate();
                JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public int reset(ArrayList<ChiTietPhieuXuatDTO> t) {
        int result = 0;
        for (int i = 0; i < t.size(); i++) {
            SanPhamDAO.getInstance().updateSoLuongTon(t.get(i).getMSP(), +(t.get(i).getSL()));
            delete(t.get(i).getMP() + "");
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM CTPHIEUXUAT WHERE MHD = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(ArrayList<ChiTietPhieuXuatDTO> t, String pk) {
        int result = this.delete(pk);
        if (result != 0) {
            result = this.insert(t);
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietPhieuXuatDTO> selectAll(String t) {
        ArrayList<ChiTietPhieuXuatDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTPHIEUXUAT WHERE MHD = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("MHD");
                int MSP = rs.getInt("MSP");
                int SL = rs.getInt("SL");
                int tienxuat = rs.getInt("TIENXUAT");
                String mkm = rs.getString("MKM");
                ChiTietPhieuXuatDTO ctphieu = new ChiTietPhieuXuatDTO(maphieu, MSP, SL, tienxuat, mkm);
                result.add(ctphieu);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public void updateSL(String t) {
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTPHIEUXUAT WHERE MHD = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("MHD");
                int MSP = rs.getInt("MSP");
                int SL = rs.getInt("SL");
                int tienxuat = rs.getInt("TIENXUAT");
                String mkm = rs.getString("MKM");
                ChiTietPhieuXuatDTO ctphieu = new ChiTietPhieuXuatDTO(maphieu, MSP, SL, tienxuat, mkm);
                int SLsp = -(ctphieu.getSL());
                SanPhamDAO.getInstance().updateSoLuongTon(ctphieu.getMSP(), SLsp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ArrayList<ChiTietPhieuXuatDTO> searchByMSP(int msp) {
        ArrayList<ChiTietPhieuXuatDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();

            String sql = "SELECT * FROM CTPHIEUXUAT WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, msp);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int mhd = rs.getInt("MHD");
                int msP = rs.getInt("MSP");
                int sl = rs.getInt("SL");
                int tienXuat = rs.getInt("TIENXUAT");
                String mkm = rs.getString("MKM");

                ChiTietPhieuXuatDTO chiTiet = new ChiTietPhieuXuatDTO(mhd, msP, sl, tienXuat, mkm);
                result.add(chiTiet);
            }

            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }

        return result;
    }

}
