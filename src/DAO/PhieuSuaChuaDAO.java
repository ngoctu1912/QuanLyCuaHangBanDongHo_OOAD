package DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.PhieuSuaChuaDTO;
import config.JDBCUtil;

public class PhieuSuaChuaDAO implements DAOinterface<PhieuSuaChuaDTO> {

    public static PhieuSuaChuaDAO getInstance() {
        return new PhieuSuaChuaDAO();
    }

    @Override
    public int insert(PhieuSuaChuaDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO `PHIEUSUACHUA`(`MPB`, `MNV`, `NGAYNHAN`, `NGAYTRA`, `NGUYENNHAN`, `TINHTRANG`, `CHIPHI`, `GHICHU`) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMPB());
            if (t.getMNV() != null) {
                pst.setInt(2, t.getMNV());
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }
            pst.setDate(3, t.getNGAYNHAN());
            if (t.getNGAYTRA() != null) {
                pst.setDate(4, t.getNGAYTRA());
            } else {
                pst.setNull(4, java.sql.Types.DATE);
            }
            pst.setString(5, t.getNGUYENNHAN());
            pst.setInt(6, t.getTINHTRANG());
            pst.setBigDecimal(7, t.getCHIPHI());
            pst.setString(8, t.getGHICHU());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(PhieuSuaChuaDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `PHIEUSUACHUA` SET `MPB`=?, `MNV`=?, `NGAYNHAN`=?, `NGAYTRA`=?, `NGUYENNHAN`=?, `TINHTRANG`=?, `CHIPHI`=?, `GHICHU`=? WHERE `MSC`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMPB());
            if (t.getMNV() != null) {
                pst.setInt(2, t.getMNV());
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }
            pst.setDate(3, t.getNGAYNHAN());
            if (t.getNGAYTRA() != null) {
                pst.setDate(4, t.getNGAYTRA());
            } else {
                pst.setNull(4, java.sql.Types.DATE);
            }
            pst.setString(5, t.getNGUYENNHAN());
            pst.setInt(6, t.getTINHTRANG());
            pst.setBigDecimal(7, t.getCHIPHI());
            pst.setString(8, t.getGHICHU());
            pst.setInt(9, t.getMSC());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM `PHIEUSUACHUA` WHERE `MSC`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<PhieuSuaChuaDTO> selectAll() {
        ArrayList<PhieuSuaChuaDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUSUACHUA` ORDER BY MSC DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MSC = rs.getInt("MSC");
                int MPB = rs.getInt("MPB");
                Integer MNV = rs.getObject("MNV", Integer.class);
                Date NGAYNHAN = rs.getDate("NGAYNHAN");
                Date NGAYTRA = rs.getDate("NGAYTRA");
                String NGUYENNHAN = rs.getString("NGUYENNHAN");
                int TINHTRANG = rs.getInt("TINHTRANG");
                BigDecimal CHIPHI = rs.getBigDecimal("CHIPHI");
                String GHICHU = rs.getString("GHICHU");
                PhieuSuaChuaDTO psc = new PhieuSuaChuaDTO(MSC, MPB, MNV, NGAYNHAN, NGAYTRA, NGUYENNHAN, TINHTRANG, CHIPHI, GHICHU);
                result.add(psc);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public PhieuSuaChuaDTO selectById(String t) {
        PhieuSuaChuaDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUSUACHUA` WHERE `MSC`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int MSC = rs.getInt("MSC");
                int MPB = rs.getInt("MPB");
                Integer MNV = rs.getObject("MNV", Integer.class);
                Date NGAYNHAN = rs.getDate("NGAYNHAN");
                Date NGAYTRA = rs.getDate("NGAYTRA");
                String NGUYENNHAN = rs.getString("NGUYENNHAN");
                int TINHTRANG = rs.getInt("TINHTRANG");
                BigDecimal CHIPHI = rs.getBigDecimal("CHIPHI");
                String GHICHU = rs.getString("GHICHU");
                result = new PhieuSuaChuaDTO(MSC, MPB, MNV, NGAYNHAN, NGAYTRA, NGUYENNHAN, TINHTRANG, CHIPHI, GHICHU);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Lấy danh sách phiếu sửa chữa theo mã phiếu bảo hành
    public ArrayList<PhieuSuaChuaDTO> selectByMaPhieuBaoHanh(int MPB) {
        ArrayList<PhieuSuaChuaDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM `PHIEUSUACHUA` WHERE `MPB`=? ORDER BY MSC DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, MPB);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MSC = rs.getInt("MSC");
                Integer MNV = rs.getObject("MNV", Integer.class);
                Date NGAYNHAN = rs.getDate("NGAYNHAN");
                Date NGAYTRA = rs.getDate("NGAYTRA");
                String NGUYENNHAN = rs.getString("NGUYENNHAN");
                int TINHTRANG = rs.getInt("TINHTRANG");
                BigDecimal CHIPHI = rs.getBigDecimal("CHIPHI");
                String GHICHU = rs.getString("GHICHU");
                PhieuSuaChuaDTO psc = new PhieuSuaChuaDTO(MSC, MPB, MNV, NGAYNHAN, NGAYTRA, NGUYENNHAN, TINHTRANG, CHIPHI, GHICHU);
                result.add(psc);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Cập nhật tình trạng phiếu sửa chữa
    public int updateTinhTrang(int MSC, int tinhTrang) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `PHIEUSUACHUA` SET `TINHTRANG`=? WHERE `MSC`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, tinhTrang);
            pst.setInt(2, MSC);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'QuanLyCuaHangDongHo' AND TABLE_NAME = 'PHIEUSUACHUA'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuSuaChuaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
