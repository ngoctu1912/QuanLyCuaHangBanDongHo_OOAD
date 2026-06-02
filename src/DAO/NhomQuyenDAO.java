package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.NhomQuyenDTO;
import config.JDBCUtil;

public class NhomQuyenDAO implements DAOinterface<NhomQuyenDTO> {

    public static NhomQuyenDAO getInstance() {
        return new NhomQuyenDAO();
    }

    @Override
    public int insert(NhomQuyenDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO NHOMQUYEN(TEN,TT) VALUES (?,1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTennhomquyen());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhomQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(NhomQuyenDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE NHOMQUYEN SET TEN= ? WHERE MNQ = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTennhomquyen());
            pst.setInt(2, t.getManhomquyen());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhomQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE NHOMQUYEN SET TT = 0 WHERE MNQ = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhomQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<NhomQuyenDTO> selectAll() {
        ArrayList<NhomQuyenDTO> result = new ArrayList<NhomQuyenDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHOMQUYEN WHERE TT = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int MNQ = rs.getInt("MNQ");
                String TEN = rs.getString("TEN");
                NhomQuyenDTO dvt = new NhomQuyenDTO(MNQ, TEN);
                result.add(dvt);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public NhomQuyenDTO selectById(String t) {
        NhomQuyenDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHOMQUYEN WHERE MNQ=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int MNQ = rs.getInt("MNQ");
                String TEN = rs.getString("TEN");
                result = new NhomQuyenDTO(MNQ, TEN);
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
            String sql = "SELECT CAST(IDENT_CURRENT('NHOMQUYEN') AS INT) AS AUTO_INCREMENT";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery();
            if (!rs2.isBeforeFirst()) {
                System.out.println("No data");
            } else {
                while (rs2.next()) {
                    result = rs2.getInt("AUTO_INCREMENT");

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhomQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    // Kiểm tra xem nhóm quyền có liên kết với tài khoản không
public boolean isLinkedToAccount(int manhomquyen) {
    boolean isLinked = false;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT COUNT(*) FROM TAIKHOAN WHERE MNQ = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, manhomquyen);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            isLinked = rs.getInt(1) > 0; 
        }
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(NhomQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return isLinked;
}

}
