package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL InsertNhomQuyen(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t.getTennhomquyen());
            result = cs.executeUpdate();
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL UpdateNhomQuyen(?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t.getTennhomquyen());
            cs.setInt(2, t.getManhomquyen());
            result = cs.executeUpdate();
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL DeleteNhomQuyen(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setInt(1, Integer.parseInt(t));
            result = cs.executeUpdate();
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetAllNhomQuyen}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int MNQ = rs.getInt("MNQ");
                String TEN = rs.getString("TEN");
                NhomQuyenDTO dvt = new NhomQuyenDTO(MNQ, TEN);
                result.add(dvt);
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public NhomQuyenDTO selectById(String t) {
        NhomQuyenDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetNhomQuyenByMNQ(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int MNQ = rs.getInt("MNQ");
                String TEN = rs.getString("TEN");
                result = new NhomQuyenDTO(MNQ, TEN);
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetAutoIncrementNhomQuyen}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            cs.close();
            JDBCUtil.closeConnection(con);
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
        String sql = "{CALL CheckNhomQuyenLinkedToAccount(?, ?)}";
        CallableStatement cs = con.prepareCall(sql);
        cs.setInt(1, manhomquyen);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.execute();
        isLinked = cs.getInt(2) > 0;
        cs.close();
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(NhomQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return isLinked;
}

}
