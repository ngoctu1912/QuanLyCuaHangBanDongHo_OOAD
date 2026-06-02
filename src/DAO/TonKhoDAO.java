package DAO;

import DTO.TonKhoDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

public class TonKhoDAO implements DAOinterface<TonKhoDTO> {

    public static TonKhoDAO getInstance() {
        return new TonKhoDAO();
    }

    @Override
    public int insert(TonKhoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO TONKHO (MSP, MKHO, SOLUONG) VALUES (?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMSP());
            pst.setInt(2, t.getMKHO());
            pst.setInt(3, t.getSOLUONG());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(TonKhoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE TONKHO SET SOLUONG = ? WHERE MSP = ? AND MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getSOLUONG());
            pst.setInt(2, t.getMSP());
            pst.setInt(3, t.getMKHO());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM TONKHO WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<TonKhoDTO> selectAll() {
        ArrayList<TonKhoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM TONKHO";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                int mkho = rs.getInt("MKHO");
                int soluong = rs.getInt("SOLUONG");
                TonKhoDTO tonkho = new TonKhoDTO(msp, mkho, soluong);
                result.add(tonkho);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public TonKhoDTO selectById(String t) {
        TonKhoDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM TONKHO WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int msp = rs.getInt("MSP");
                int mkho = rs.getInt("MKHO");
                int soluong = rs.getInt("SOLUONG");
                result = new TonKhoDTO(msp, mkho, soluong);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        // TONKHO không có auto increment, trả về -1
        return -1;
    }

    /**
     * Lấy tồn kho của một sản phẩm ở một kho cụ thể
     */
    public TonKhoDTO getTonKhoByMSPAndMKHO(int msp, int mkho) {
        TonKhoDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM TONKHO WHERE MSP = ? AND MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msp);
            pst.setInt(2, mkho);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int soluong = rs.getInt("SOLUONG");
                result = new TonKhoDTO(msp, mkho, soluong);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Cập nhật tồn kho (tăng hoặc giảm)
     */
    public int updateSoLuong(int msp, int mkho, int soluong) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE TONKHO SET SOLUONG = SOLUONG + ? WHERE MSP = ? AND MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, soluong);
            pst.setInt(2, msp);
            pst.setInt(3, mkho);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Lấy danh sách tồn kho của một kho
     */
    public ArrayList<TonKhoDTO> selectByMKHO(int mkho) {
        ArrayList<TonKhoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM TONKHO WHERE MKHO = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mkho);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                int soluong = rs.getInt("SOLUONG");
                TonKhoDTO tonkho = new TonKhoDTO(msp, mkho, soluong);
                result.add(tonkho);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Lấy danh sách tồn kho của một sản phẩm
     */
    public ArrayList<TonKhoDTO> selectByMSP(int msp) {
        ArrayList<TonKhoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM TONKHO WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mkho = rs.getInt("MKHO");
                int soluong = rs.getInt("SOLUONG");
                TonKhoDTO tonkho = new TonKhoDTO(msp, mkho, soluong);
                result.add(tonkho);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Lấy tổng số lượng tồn của một sản phẩm theo chi nhánh
     */
    public int getTonKhoByMSPAndMCN(int msp, String mcn) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT SUM(TONKHO.SOLUONG) as SOLUONG FROM TONKHO " +
                         "JOIN KHO ON TONKHO.MKHO = KHO.MKHO " +
                         "WHERE TONKHO.MSP = ? AND KHO.MCN = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msp);
            pst.setString(2, mcn);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("SOLUONG");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(TonKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
