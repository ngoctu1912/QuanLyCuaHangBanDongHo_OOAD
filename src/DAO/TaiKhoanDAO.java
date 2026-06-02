package DAO;

import config.JDBCUtil;
import helper.BCrypt;
import DTO.TaiKhoanDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaiKhoanDAO implements DAOinterface<TaiKhoanDTO>{
    
    public static TaiKhoanDAO getInstance(){
        return new TaiKhoanDAO();
    }

    @Override
    public int insert(TaiKhoanDTO t) {
        int result = 0 ;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL InsertTaiKhoan(?, ?, ?, ?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setInt(1, t.getMNV());
            cs.setString(2, t.getTDN());
            cs.setString(3, BCrypt.hashpw(t.getMK(), BCrypt.gensalt(12)));
            cs.setInt(4, t.getMNQ());
            cs.setInt(5, t.getTT());
            result = cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(TaiKhoanDTO t) {
        int result = 0 ;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL UpdateTaiKhoan(?, ?, ?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t.getTDN());
            cs.setInt(2, t.getTT());
            cs.setInt(3, t.getMNQ());
            cs.setInt(4, t.getMNV());
            result = cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateTTCXL(String t) {
        int result = 0 ;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL UpdateTTCXL(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            result = cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    
    public void updatePass(String email, String password){
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL UpdatePasswordByEmail(?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, BCrypt.hashpw(password, BCrypt.gensalt(12)));
            cs.setString(2, email);
            cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updatePassByMNV(int mnv, String password) {
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL UpdatePasswordByMNV(?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setInt(1, mnv);
            cs.setString(2, BCrypt.hashpw(password, BCrypt.gensalt(12)));
            cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public TaiKhoanDTO selectByEmail(String t) {
        TaiKhoanDTO tk = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetTaiKhoanByEmail(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int MNV = rs.getInt("MNV");
                String TDN = rs.getString("TDN");
                String MK = rs.getString("MK");
                int TT = rs.getInt("TRANGTHAI");
                int MNQ = rs.getInt("MNQ");
                tk = new TaiKhoanDTO(MNV, TDN, MK, MNQ, TT);
                return tk;
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return tk;
    }
    
    public void sendOpt(String email, String opt){
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL SendOTP(?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, opt);
            cs.setString(2, email);
            cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkOtp(String email, String otp){
        boolean check = false;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL CheckOTP(?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, email);
            cs.setString(2, otp);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                check = true;
                return check;
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return check;
    }

    @Override
    public int delete(String t) {
        int result = 0 ;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL DeleteTaiKhoan(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setInt(1, Integer.parseInt(t));
            result = cs.executeUpdate();
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<TaiKhoanDTO> selectAll() {
        ArrayList<TaiKhoanDTO> result = new ArrayList<TaiKhoanDTO>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetAllTaiKhoan}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                int MNV = rs.getInt("MNV");
                String TDN = rs.getString("TDN");
                String MK = rs.getString("MK");
                int MNQ = rs.getInt("MNQ");
                int TT = rs.getInt("TRANGTHAI");
                TaiKhoanDTO tk = new TaiKhoanDTO(MNV, TDN, MK, MNQ, TT);
                result.add(tk);
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public TaiKhoanDTO selectById(String t) {
        TaiKhoanDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetTaiKhoanByMNV(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            ResultSet rs = cs.executeQuery();
            while(rs.next()) {
                int MNV = rs.getInt("MNV");
                String TDN = rs.getString("TDN");
                String MK = rs.getString("MK");
                int TT = rs.getInt("TRANGTHAI");
                int MNQ = rs.getInt("MNQ");
                TaiKhoanDTO tk = new TaiKhoanDTO(MNV, TDN, MK, MNQ, TT);
                result = tk;
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
    
    public TaiKhoanDTO selectByUser(String t) {
        TaiKhoanDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetTaiKhoanByUser(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                int MNV = rs.getInt("MNV");
                String TDN = rs.getString("TDN");
                String MK = rs.getString("MK");
                int TT = rs.getInt("TRANGTHAI");
                int MNQ = rs.getInt("MNQ");
                TaiKhoanDTO tk = new TaiKhoanDTO(MNV, TDN, MK, MNQ, TT);
                result = tk;
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
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT CAST(IDENT_CURRENT('TAIKHOAN') AS INT) AS AUTO_INCREMENT";
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
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public ArrayList<TaiKhoanDTO> selectAllExcludingAdmin() {
    ArrayList<TaiKhoanDTO> result = new ArrayList<>();
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM TAIKHOAN WHERE (TRANGTHAI = '0' OR TRANGTHAI = '1' OR TRANGTHAI = '2') AND TDN != 'admin'";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int MNV = rs.getInt("MNV");
            String TDN = rs.getString("TDN");
            String MK = rs.getString("MK");
            int MNQ = rs.getInt("MNQ");
            int TT = rs.getInt("TRANGTHAI");
            TaiKhoanDTO tk = new TaiKhoanDTO(MNV, TDN, MK, MNQ, TT);
            result.add(tk);
        }
        JDBCUtil.closeConnection(con);
    } catch (Exception e) {
        Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, e);
    }
    return result;
}
    public boolean isAccountInactive(String username) {
    boolean isInactive = false;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT TRANGTHAI FROM TAIKHOAN WHERE TDN = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int status = rs.getInt("TRANGTHAI");
            if (status == -1) {
                isInactive = true;
            }
        }

        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return isInactive;
}
    


}
