package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.NhanVienDTO;
import config.JDBCUtil;

public class NhanVienDAO implements DAOinterface<NhanVienDTO>{
    public static NhanVienDAO getInstance(){
        return new NhanVienDAO();
    }

    @Override
    public int insert(NhanVienDTO t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO `NHANVIEN`(`HOTEN`, `GIOITINH`,`SDT`,`NGAYSINH`,`TT`,`EMAIL`, `MCV`) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getHOTEN());
            pst.setInt(2, t.getGIOITINH());
            pst.setString(3, t.getSDT());
            pst.setDate(4, (Date) (t.getNGAYSINH()));
            pst.setInt(5, t.getTT());
            pst.setString(6, t.getEMAIL());
            pst.setInt(7, t.getMCV());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(NhanVienDTO t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `NHANVIEN` SET `HOTEN` = ?,`GIOITINH` = ?,`NGAYSINH` = ?,`SDT` = ?, `TT` = ?, `EMAIL` = ?, `MCV` = ?  WHERE `MNV` = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getHOTEN());
            pst.setInt(2, t.getGIOITINH());
            pst.setDate(3, (Date) t.getNGAYSINH());
            pst.setString(4, t.getSDT());
            pst.setInt(5, t.getTT());
            pst.setString(6, t.getEMAIL());
            pst.setInt(7, t.getMCV());
            pst.setInt(8, t.getMNV());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE NHANVIEN SET `TT` = -1 WHERE MNV = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> result = new ArrayList<NhanVienDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHANVIEN WHERE `TT` = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()) {
                int MNV = rs.getInt("MNV");
                String HOTEN = rs.getString("HOTEN");
                int GIOITINH = rs.getInt("GIOITINH");
                Date NGAYSINH = rs.getDate("NGAYSINH");
                String SDT = rs.getString("SDT");
                int TT = rs.getInt("TT");
                String EMAIL = rs.getString("EMAIL");
                int MCV = rs.getInt("MCV");
                NhanVienDTO nv = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    public ArrayList<NhanVienDTO> selectAlll() {
        ArrayList<NhanVienDTO> result = new ArrayList<NhanVienDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHANVIEN";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MNV = rs.getInt("MNV");
                String HOTEN = rs.getString("HOTEN");
                int GIOITINH = rs.getInt("GIOITINH");
                Date NGAYSINH = rs.getDate("NGAYSINH");
                String SDT = rs.getString("SDT");
                int TT = rs.getInt("TT");
                String EMAIL = rs.getString("EMAIL");
                int MCV = rs.getInt("MCV");
                NhanVienDTO nv = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public ArrayList<NhanVienDTO> selectAllNV() {
        ArrayList<NhanVienDTO> result = new ArrayList<NhanVienDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHANVIEN nv where nv.TT = 1 and not EXISTS(SELECT * FROM taikhoan tk WHERE nv.MNV=tk.MNV)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MNV = rs.getInt("MNV");
                String HOTEN = rs.getString("HOTEN");
                int GIOITINH = rs.getInt("GIOITINH");
                Date NGAYSINH = rs.getDate("NGAYSINH");
                String SDT = rs.getString("SDT");
                int TT = rs.getInt("TT");
                String EMAIL = rs.getString("EMAIL");
                int MCV = rs.getInt("MCV");
                NhanVienDTO nv = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    

    @Override
    public NhanVienDTO selectById(String t) {
        NhanVienDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHANVIEN WHERE MNV = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MNV = rs.getInt("MNV");
                String HOTEN = rs.getString("HOTEN");
                int GIOITINH = rs.getInt("GIOITINH");
                Date NGAYSINH = rs.getDate("NGAYSINH");
                String SDT = rs.getString("SDT");
                int TT = rs.getInt("TT");
                String EMAIL = rs.getString("EMAIL");
                int MCV = rs.getInt("MCV");
                result = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
    
    public NhanVienDTO selectByEmail(String t) {
        NhanVienDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHANVIEN WHERE EMAIL = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MNV = rs.getInt("MNV");
                String HOTEN = rs.getString("HOTEN");
                int GIOITINH = rs.getInt("GIOITINH");
                Date NGAYSINH = rs.getDate("NGAYSINH");
                String SDT = rs.getString("SDT");
                int TT = rs.getInt("TT");
                String EMAIL = rs.getString("EMAIL");
                int MCV = rs.getInt("MCV");
                result = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV);
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
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'QuanLyCuaHangDongHo' AND   TABLE_NAME   = 'NHANVIEN'";
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
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public int countAll() {
    int count = 0;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT COUNT(*) AS total FROM NHANVIEN";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            count = rs.getInt("total");
        }
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return count;
}
    public int deactivateAccountByMNV(int mnv) {
    int result = 0;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE TAIKHOAN SET TT = -1 WHERE MNV = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, mnv);
        result = pst.executeUpdate();
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
}
}
