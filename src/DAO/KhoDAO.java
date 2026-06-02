package DAO;

import DTO.khoDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

public class KhoDAO implements DAOinterface<khoDTO> {

    public static KhoDAO getInstance() {
        return new KhoDAO();
    }

    @Override
    public int insert(khoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO KHO (TEN, DIACHI, MCN, TRANGTHAI) VALUES (?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getDIACHI());
            pst.setString(3, t.getMCN());
            pst.setInt(4, t.getTRANGTHAI());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(khoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE KHO SET TEN = ?, DIACHI = ?, MCN = ?, TRANGTHAI = ? WHERE MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getDIACHI());
            pst.setString(3, t.getMCN());
            pst.setInt(4, t.getTRANGTHAI());
            pst.setInt(5, t.getMKHO());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE KHO SET TRANGTHAI = 0 WHERE MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<khoDTO> selectAll() {
        ArrayList<khoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM KHO WHERE TRANGTHAI = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mkho = rs.getInt("MKHO");
                String ten = rs.getString("TEN");
                String diachi = rs.getString("DIACHI");
                String mcn = rs.getString("MCN");
                int trangthai = rs.getInt("TRANGTHAI");
                khoDTO kho = new khoDTO(mkho, ten, diachi, mcn, trangthai);
                result.add(kho);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public khoDTO selectById(String t) {
        khoDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM KHO WHERE MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int mkho = rs.getInt("MKHO");
                String ten = rs.getString("TEN");
                String diachi = rs.getString("DIACHI");
                String mcn = rs.getString("MCN");
                int trangthai = rs.getInt("TRANGTHAI");
                result = new khoDTO(mkho, ten, diachi, mcn, trangthai);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT CAST(IDENT_CURRENT('KHO') AS INT) AS AUTO_INCREMENT";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Lấy kho theo mã chi nhánh
     */
    public ArrayList<khoDTO> selectByMCN(String mcn) {
        ArrayList<khoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM KHO WHERE MCN = ? AND TRANGTHAI = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, mcn);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mkho = rs.getInt("MKHO");
                String ten = rs.getString("TEN");
                String diachi = rs.getString("DIACHI");
                int trangthai = rs.getInt("TRANGTHAI");
                khoDTO kho = new khoDTO(mkho, ten, diachi, mcn, trangthai);
                result.add(kho);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Lấy kho mặc định của một chi nhánh (kho đầu tiên)
     */
    public khoDTO getDefaultKhoByCN(String mcn) {
        khoDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT TOP 1 * FROM KHO WHERE MCN = ? AND TRANGTHAI = 1 ORDER BY MKHO";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, mcn);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int mkho = rs.getInt("MKHO");
                String ten = rs.getString("TEN");
                String diachi = rs.getString("DIACHI");
                int trangthai = rs.getInt("TRANGTHAI");
                result = new khoDTO(mkho, ten, diachi, mcn, trangthai);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(KhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
