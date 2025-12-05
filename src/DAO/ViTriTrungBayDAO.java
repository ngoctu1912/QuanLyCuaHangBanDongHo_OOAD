package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.ViTriTrungBayDTO;
import config.JDBCUtil;

public class ViTriTrungBayDAO implements DAOinterface<ViTriTrungBayDTO> {
    
    public static ViTriTrungBayDAO getInstance() {
        return new ViTriTrungBayDAO();
    }

    @Override
    public int insert(ViTriTrungBayDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO `VITRITRUNGBAY`(`TEN`, `GHICHU`) VALUES (?,?)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getGHICHU());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(ViTriTrungBayDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE ` VITRITRUNGBAY` SET `TEN`=?,`GHICHU`=? WHERE `MVT`=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getGHICHU());
            pst.setInt(3, t.getMVT());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM  VITRITRUNGBAY WHERE `MVT` = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<ViTriTrungBayDTO> selectAll() {
        ArrayList<ViTriTrungBayDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM    VITRITRUNGBAY ORDER BY MVT ASC";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mvt = rs.getInt("MVT");
                String ten = rs.getString("TEN");
                String ghichu = rs.getString("GHICHU");
                ViTriTrungBayDTO vt = new ViTriTrungBayDTO(mvt, ten, ghichu);
                result.add(vt);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public ViTriTrungBayDTO selectById(String t) {
        ViTriTrungBayDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM    VITRITRUNGBAY WHERE MVT=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mvt = rs.getInt("MVT");
                String ten = rs.getString("TEN");
                String ghichu = rs.getString("GHICHU");
                result = new ViTriTrungBayDTO(mvt, ten, ghichu);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlycuahangdongho' AND TABLE_NAME = 'VITRITRUNGBAY'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery(sql);
            if (!rs2.isBeforeFirst()) {
                System.out.println("No data");
            } else {
                while (rs2.next()) {
                    result = rs2.getInt("AUTO_INCREMENT");
                }
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<ViTriTrungBayDTO> search(String text, String type) {
        ArrayList<ViTriTrungBayDTO> result = new ArrayList<>();
        String sql = "";
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            if (type.equals("Tất cả")) {
                sql = "SELECT * FROM   VITRITRUNGBAY WHERE MVT LIKE ? OR TEN LIKE ? OR GHICHU LIKE ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, "%" + text + "%");
                pst.setString(2, "%" + text + "%");
                pst.setString(3, "%" + text + "%");
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    int mvt = rs.getInt("MVT");
                    String ten = rs.getString("TEN");
                    String ghichu = rs.getString("GHICHU");
                    ViTriTrungBayDTO vt = new ViTriTrungBayDTO(mvt, ten, ghichu);
                    result.add(vt);
                }
            } else if (type.equals("Mã vị trí")) {
                sql = "SELECT * FROM   VITRITRUNGBAY WHERE MVT LIKE ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, "%" + text + "%");
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    int mvt = rs.getInt("MVT");
                    String ten = rs.getString("TEN");
                    String ghichu = rs.getString("GHICHU");
                    ViTriTrungBayDTO vt = new ViTriTrungBayDTO(mvt, ten, ghichu);
                    result.add(vt);
                }
            } else if (type.equals("Tên vị trí")) {
                sql = "SELECT * FROM   VITRITRUNGBAY WHERE TEN LIKE ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, "%" + text + "%");
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    int mvt = rs.getInt("MVT");
                    String ten = rs.getString("TEN");
                    String ghichu = rs.getString("GHICHU");
                    ViTriTrungBayDTO vt = new ViTriTrungBayDTO(mvt, ten, ghichu);
                    result.add(vt);
                }
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(ViTriTrungBayDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
