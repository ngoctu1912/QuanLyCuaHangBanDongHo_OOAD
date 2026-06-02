package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.MaKhuyenMaiDTO;
import config.JDBCUtil;


public class MaKhuyenMaiDAO implements DAOinterface<MaKhuyenMaiDTO> {
    public static MaKhuyenMaiDAO getInstance() {
        return new MaKhuyenMaiDAO();
    }

    @Override
    public int insert(MaKhuyenMaiDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO MAKHUYENMAI(MKM, TGBD,TGKT, TT) VALUES (?,?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getMKM());
            pst.setTimestamp(2, t.getTGBD());
            pst.setTimestamp(3, t.getTGKT());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(MaKhuyenMaiDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE MAKHUYENMAI SET MKM=?,TGBD=?,TGKT=? WHERE MKM=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getMKM());
            pst.setTimestamp(2, t.getTGBD());
            pst.setTimestamp(3, t.getTGKT());
            pst.setString(4, t.getMKM());
            
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE  MAKHUYENMAI SET TT = 0 WHERE MKM = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<MaKhuyenMaiDTO> selectAll() {
        ArrayList<MaKhuyenMaiDTO> result = new ArrayList<MaKhuyenMaiDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM MAKHUYENMAI WHERE TT=1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                String MKM = rs.getString("MKM");
                Timestamp TGBD = rs.getTimestamp("TGBD");
                Timestamp TGKT = rs.getTimestamp("TGKT");
                MaKhuyenMaiDTO kh = new MaKhuyenMaiDTO(MKM, TGBD, TGKT);
                result.add(kh);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public MaKhuyenMaiDTO selectById(String t) {
        MaKhuyenMaiDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM MAKHUYENMAI WHERE MKM=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                String MKM = rs.getString("MKM");
                Timestamp TGBD = rs.getTimestamp("TGBD");
                Timestamp TGKT = rs.getTimestamp("TGKT");
                result = new MaKhuyenMaiDTO(MKM, TGBD, TGKT);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public int cancelMKM(String mkm){
        int result = 0;
        ChiTietMaKhuyenMaiDAO.getInstance().delete(mkm);
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM MAKHUYENMAI WHERE MKM = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, mkm);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT CAST(IDENT_CURRENT('MAKHUYENMAI') AS INT) AS AUTO_INCREMENT";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery();
            if (!rs2.isBeforeFirst() ) {
                System.out.println("No data");
            } else {
                while ( rs2.next() ) {
                    result = rs2.getInt("AUTO_INCREMENT");
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
