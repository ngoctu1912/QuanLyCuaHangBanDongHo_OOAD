package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.ChiTietMaKhuyenMaiDTO;
import config.JDBCUtil;

public class ChiTietMaKhuyenMaiDAO implements DAOinterface<ChiTietMaKhuyenMaiDTO> {

    public static ChiTietMaKhuyenMaiDAO getInstance() {
        return new ChiTietMaKhuyenMaiDAO();
    }

    @Override
    public int insert(ChiTietMaKhuyenMaiDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO `CTMAKHUYENMAI`(`MKM`, `MSP`, `PTG`) VALUES (?,?,?)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getMKM());
            pst.setInt(2, t.getMSP());
            pst.setInt(3, t.getPTG());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietMaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int insert(ArrayList<ChiTietMaKhuyenMaiDTO> t) {
        int result = 0;
        for (int i = 0; i < t.size(); i++) {
            try {
                Connection con = (Connection) JDBCUtil.getConnection();
                String sql = "INSERT INTO `CTMAKHUYENMAI`(`MKM`, `MSP`, `PTG`) VALUES (?,?,?)";
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                pst.setString(1, t.get(i).getMKM());
                pst.setInt(2, t.get(i).getMSP());
                pst.setInt(3, t.get(i).getPTG());
                result = pst.executeUpdate();
                JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int update(ChiTietMaKhuyenMaiDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `CTMAKHUYENMAI` SET `MKM`=?,`MSP`=?,`PTG`=? WHERE MKM=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getMKM());
            pst.setInt(2, t.getMSP());
            pst.setInt(3, t.getPTG());
            pst.setString(5, t.getMKM());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietMaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM `CTMAKHUYENMAI` WHERE `MKM` = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietMaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietMaKhuyenMaiDTO> selectAll() {
        ArrayList<ChiTietMaKhuyenMaiDTO> result = new ArrayList<ChiTietMaKhuyenMaiDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTMAKHUYENMAI";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                ChiTietMaKhuyenMaiDTO kh = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
                result.add(kh);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public ArrayList<ChiTietMaKhuyenMaiDTO> selectAll(String t) {
        ArrayList<ChiTietMaKhuyenMaiDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTMAKHUYENMAI WHERE MKM = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                ChiTietMaKhuyenMaiDTO kh = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
                result.add(kh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public ChiTietMaKhuyenMaiDTO selectById(String t) {
        ChiTietMaKhuyenMaiDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTMAKHUYENMAI WHERE MKM=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                result = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
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
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'QuanLyCuaHangDongHo' AND   TABLE_NAME   = 'CTMAKHUYENMAI'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery(sql);
            if (!rs2.isBeforeFirst()) {
                System.out.println("No data");
            } else {
                while (rs2.next()) {
                    result = rs2.getInt("AUTO_INCREMENT");

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietMaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<ChiTietMaKhuyenMaiDTO> searchByMSP(int msp) {
        ArrayList<ChiTietMaKhuyenMaiDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTMAKHUYENMAI WHERE MSP = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, msp);  // Thiết lập mã sản phẩm cần tìm kiếm
            ResultSet rs = (ResultSet) pst.executeQuery();

            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSPValue = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                ChiTietMaKhuyenMaiDTO kh = new ChiTietMaKhuyenMaiDTO(MKM, MSPValue, PTG);
                result.add(kh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public ChiTietMaKhuyenMaiDTO selectByMKMAndMSP(String mkm, int msp) {
        ChiTietMaKhuyenMaiDTO result = null;
        try {
            // Kết nối với cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Câu lệnh SQL
            String sql = "SELECT * FROM CTMAKHUYENMAI WHERE MKM = ? AND MSP = ?";

            // Chuẩn bị truy vấn
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, mkm); // Thiết lập giá trị MKM
            pst.setInt(2, msp);    // Thiết lập giá trị MSP

            // Thực thi truy vấn
            ResultSet rs = pst.executeQuery();

            // Kiểm tra kết quả và gán vào DTO
            if (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                result = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
            }

            // Đóng kết nối
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

}
