package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.ChucVuDTO;
import config.JDBCUtil;

public class ChucVuDAO implements DAOinterface<ChucVuDTO>{
    public static ChucVuDAO getInstance(){
        return new ChucVuDAO();
    }

    @Override
    public int insert(ChucVuDTO t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO `CHUCVU`(`TEN`, `MUCLUONG`,`TT`) VALUES (?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTENCV());
            pst.setInt(2, t.getMUCLUONG());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChucVuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(ChucVuDTO t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `CHUCVU` SET `TEN` = ?, `MUCLUONG` = ? WHERE `MCV` = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTENCV());
            pst.setInt(2, t.getMUCLUONG());
            pst.setInt(3, t.getMCV());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChucVuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE ChucVu SET `TT` = 0 WHERE MCV = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChucVuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<ChucVuDTO> selectAll() {
        ArrayList<ChucVuDTO> result = new ArrayList<ChucVuDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CHUCVU WHERE `TT` = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()) {
                int MCV = rs.getInt("MCV");
                String TEN = rs.getString("TEN");
                int MUCLUONG = rs.getInt("MUCLUONG");
                ChucVuDTO nv = new ChucVuDTO(MCV, TEN, MUCLUONG);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public ArrayList<ChucVuDTO> getAll() {
        ArrayList<ChucVuDTO> result = new ArrayList<ChucVuDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CHUCVU";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()) {
                int MCV = rs.getInt("MCV");
                String TEN = rs.getString("TEN");
                int MUCLUONG = rs.getInt("MUCLUONG");
                ChucVuDTO nv = new ChucVuDTO(MCV, TEN, MUCLUONG);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    

    @Override
    public ChucVuDTO selectById(String t) {
        ChucVuDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CHUCVU WHERE MCV = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MCV = rs.getInt("MCV");
                String TEN = rs.getString("TEN");
                int MUCLUONG = rs.getInt("MUCLUONG");
                result = new ChucVuDTO(MCV, TEN, MUCLUONG);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
    
    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'QuanLyCuaHangDongHo' AND   TABLE_NAME   = 'ChucVu'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery(sql);
            if (!rs2.isBeforeFirst() ) {
                System.out.println("No data");
            } else {
                while ( rs2.next() ) {
                    result = rs2.getInt("AUTO_INCREMENT");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChucVuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public String getTenChucVuByMCV(int mcv) {
    String tenChucVu = null;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT TEN FROM CHUCVU WHERE MCV = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, mcv);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            tenChucVu = rs.getString("TEN");
        }
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(ChucVuDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return tenChucVu;
}
    public int getRecordCount() {
    int recordCount = 0;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT COUNT(*) AS record_count FROM CHUCVU";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            recordCount = rs.getInt("record_count");
        }
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(ChucVuDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return recordCount;
}
}
