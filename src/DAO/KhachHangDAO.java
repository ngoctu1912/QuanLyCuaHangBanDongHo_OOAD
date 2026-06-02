package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.KhachHangDTO;
import config.JDBCUtil;

public class KhachHangDAO implements DAOinterface<KhachHangDTO> {

    public static KhachHangDAO getInstance() {
        return new KhachHangDAO();
    }

    @Override
    public int insert(KhachHangDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO KHACHHANG(MKH, HOTEN, DIACHI,SDT, EMAIL,NGAYTHAMGIA, DIEMTICHLUY, TT) VALUES (?,?,?,?,?,?,0,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t.getMKH());
            pst.setString(2, t.getHOTEN());
            pst.setString(3, t.getDIACHI());
            pst.setString(4, t.getSDT());
            long now = System.currentTimeMillis();
            Timestamp currenTime = new Timestamp(now);
            pst.setString(5, t.getEMAIL());
            pst.setTimestamp(6, currenTime);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(KhachHangDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE KHACHHANG SET HOTEN=?, DIACHI=?, SDT=?, EMAIL=?, DIEMTICHLUY=? WHERE MKH=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getHOTEN());
            pst.setString(2, t.getDIACHI());
            pst.setString(3, t.getSDT());
            pst.setString(4, t.getEMAIL());
            pst.setInt(5, t.getDIEMTICHLUY());
            pst.setInt(6, t.getMKH());
            
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateDiem(int MKH, int dtl) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE KHACHHANG SET DIEMTICHLUY = ? WHERE MKH=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, dtl);
            pst.setInt(2, MKH);            
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE  KHACHHANG SET TT=0 WHERE MKH = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<KhachHangDTO> selectAll() {
        ArrayList<KhachHangDTO> result = new ArrayList<KhachHangDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM KHACHHANG WHERE TT = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MKH = rs.getInt("MKH");
                String HOTEN = rs.getString("HOTEN");
                String DIACHI = rs.getString("DIACHI");
                String SDT = rs.getString("SDT");
                Date ngaythamgia = rs.getDate("NGAYTHAMGIA");
                String EMAIL = rs.getString("EMAIL");
                int diemtichluy = rs.getInt("DIEMTICHLUY");
                KhachHangDTO kh = new KhachHangDTO(MKH, HOTEN, SDT, DIACHI, EMAIL,ngaythamgia, diemtichluy);
                result.add(kh);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public ArrayList<KhachHangDTO> selectAlll() {
        ArrayList<KhachHangDTO> result = new ArrayList<KhachHangDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM KHACHHANG";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MKH = rs.getInt("MKH");
                String HOTEN = rs.getString("HOTEN");
                String DIACHI = rs.getString("DIACHI");
                String SDT = rs.getString("SDT");
                Date ngaythamgia = rs.getDate("NGAYTHAMGIA");
                String EMAIL = rs.getString("EMAIL");
                int diemtichluy = rs.getInt("DIEMTICHLUY");
                KhachHangDTO kh = new KhachHangDTO(MKH, HOTEN, SDT, DIACHI, EMAIL,ngaythamgia, diemtichluy);
                result.add(kh);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public KhachHangDTO selectById(String t) {
        KhachHangDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM KHACHHANG WHERE MKH=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int MKH = rs.getInt("MKH");
                String HOTEN = rs.getString("HOTEN");
                String DIACHI = rs.getString("DIACHI");
                String SDT = rs.getString("SDT");
                Date ngaythamgia = rs.getDate("NGAYTHAMGIA");
                String EMAIL = rs.getString("EMAIL");
                int diemtichluy = rs.getInt("DIEMTICHLUY");
                result = new KhachHangDTO(MKH, HOTEN, SDT, DIACHI, EMAIL,ngaythamgia, diemtichluy);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT CAST(IDENT_CURRENT('KHACHHANG') AS INT) AS AUTO_INCREMENT";
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
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public int countAllRecords() {
    int count = 0;
    try {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT COUNT(*) AS total FROM KHACHHANG";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            count = rs.getInt("total");
        }
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return count;
}
}
