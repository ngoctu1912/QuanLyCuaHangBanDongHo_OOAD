package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.PhieuBaoHanhDTO;
import config.JDBCUtil;

public class PhieuBaoHanhDAO implements DAOinterface<PhieuBaoHanhDTO> {

    public static PhieuBaoHanhDAO getInstance() {
        return new PhieuBaoHanhDAO();
    }

    @Override
    public int insert(PhieuBaoHanhDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO `PHIEUBAOHANH`(`MPX`, `MSP`, `MKH`, `NGAYBATDAU`, `NGAYKETTHUC`, `TRANGTHAI`) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMPX());
            pst.setInt(2, t.getMSP());
            pst.setInt(3, t.getMKH());
            pst.setDate(4, t.getNGAYBATDAU());
            pst.setDate(5, t.getNGAYKETTHUC());
            pst.setInt(6, t.getTRANGTHAI());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(PhieuBaoHanhDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `PHIEUBAOHANH` SET `MPX`=?, `MSP`=?, `MKH`=?, `NGAYBATDAU`=?, `NGAYKETTHUC`=?, `TRANGTHAI`=? WHERE `MPB`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMPX());
            pst.setInt(2, t.getMSP());
            pst.setInt(3, t.getMKH());
            pst.setDate(4, t.getNGAYBATDAU());
            pst.setDate(5, t.getNGAYKETTHUC());
            pst.setInt(6, t.getTRANGTHAI());
            pst.setInt(7, t.getMPB());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM `PHIEUBAOHANH` WHERE `MPB`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<PhieuBaoHanhDTO> selectAll() {
        ArrayList<PhieuBaoHanhDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUBAOHANH` ORDER BY MPB ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MPB = rs.getInt("MPB");
                int MPX = rs.getInt("MPX");
                int MSP = rs.getInt("MSP");
                int MKH = rs.getInt("MKH");
                Date NGAYBATDAU = rs.getDate("NGAYBATDAU");
                Date NGAYKETTHUC = rs.getDate("NGAYKETTHUC");
                int TRANGTHAI = rs.getInt("TRANGTHAI");
                PhieuBaoHanhDTO pbh = new PhieuBaoHanhDTO(MPB, MPX, MSP, MKH, NGAYBATDAU, NGAYKETTHUC, TRANGTHAI);
                result.add(pbh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public PhieuBaoHanhDTO selectById(String t) {
        PhieuBaoHanhDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUBAOHANH` WHERE `MPB`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int MPB = rs.getInt("MPB");
                int MPX = rs.getInt("MPX");
                int MSP = rs.getInt("MSP");
                int MKH = rs.getInt("MKH");
                Date NGAYBATDAU = rs.getDate("NGAYBATDAU");
                Date NGAYKETTHUC = rs.getDate("NGAYKETTHUC");
                int TRANGTHAI = rs.getInt("TRANGTHAI");
                result = new PhieuBaoHanhDTO(MPB, MPX, MSP, MKH, NGAYBATDAU, NGAYKETTHUC, TRANGTHAI);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Lấy danh sách phiếu bảo hành theo mã hóa đơn
    public ArrayList<PhieuBaoHanhDTO> selectByMaHoaDon(int MPX) {
        ArrayList<PhieuBaoHanhDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUBAOHANH` WHERE `MPX`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, MPX);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MPB = rs.getInt("MPB");
                int mpxResult = rs.getInt("MPX");
                int MSP = rs.getInt("MSP");
                int MKH = rs.getInt("MKH");
                Date NGAYBATDAU = rs.getDate("NGAYBATDAU");
                Date NGAYKETTHUC = rs.getDate("NGAYKETTHUC");
                int TRANGTHAI = rs.getInt("TRANGTHAI");
                PhieuBaoHanhDTO pbh = new PhieuBaoHanhDTO(MPB, mpxResult, MSP, MKH, NGAYBATDAU, NGAYKETTHUC, TRANGTHAI);
                result.add(pbh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Lấy danh sách phiếu bảo hành theo mã khách hàng
    public ArrayList<PhieuBaoHanhDTO> selectByMaKhachHang(int MKH) {
        ArrayList<PhieuBaoHanhDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUBAOHANH` WHERE `MKH`=? ORDER BY MPB ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, MKH);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MPB = rs.getInt("MPB");
                int MPX = rs.getInt("MPX");
                int MSP = rs.getInt("MSP");
                int mkhResult = rs.getInt("MKH");
                Date NGAYBATDAU = rs.getDate("NGAYBATDAU");
                Date NGAYKETTHUC = rs.getDate("NGAYKETTHUC");
                int TRANGTHAI = rs.getInt("TRANGTHAI");
                PhieuBaoHanhDTO pbh = new PhieuBaoHanhDTO(MPB, MPX, MSP, mkhResult, NGAYBATDAU, NGAYKETTHUC, TRANGTHAI);
                result.add(pbh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Cập nhật trạng thái phiếu bảo hành
    public int updateTrangThai(int MPB, int trangThai) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `PHIEUBAOHANH` SET `TRANGTHAI`=? WHERE `MPB`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, trangThai);
            pst.setInt(2, MPB);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'QuanLyCuaHangDongHo' AND TABLE_NAME = 'PHIEUBAOHANH'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    // Xóa phiếu bảo hành theo mã hóa đơn
    public int deleteByMaHoaDon(int maHoaDon) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM `PHIEUBAOHANH` WHERE `MPX`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maHoaDon);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    // Cập nhật trạng thái tất cả phiếu bảo hành theo mã hóa đơn
    public int updateTrangThaiByMaHoaDon(int maHoaDon, int trangThai) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `PHIEUBAOHANH` SET `TRANGTHAI`=? WHERE `MPX`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, trangThai);
            pst.setInt(2, maHoaDon);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuBaoHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
