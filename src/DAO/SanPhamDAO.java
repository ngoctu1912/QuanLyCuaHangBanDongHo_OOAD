package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DTO.SanPhamDTO;
import config.JDBCUtil;

public class SanPhamDAO implements DAOinterface<SanPhamDTO> {

    public static SanPhamDAO getInstance() {
        return new SanPhamDAO();
    }

    @Override
    public int insert(SanPhamDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO `SANPHAM` (`TEN`, `HINHANH`, `MNCC`, `MVT`, `THUONGHIEU`, `NAMSANXUAT`, `GIANHAP`, `GIABAN`, `SOLUONG`, `THOIGIANBAOHANH`, `TT`) VALUES (?,?,?,?,?,?,?,?,?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getHINHANH());
            pst.setInt(3, t.getMNCC());
            if (t.getMVT() != null) {
                pst.setInt(4, t.getMVT());
            } else {
                pst.setNull(4, java.sql.Types.INTEGER);
            }
            pst.setString(5, t.getTHUONGHIEU());
            if (t.getNAMSANXUAT() != null) {
                pst.setInt(6, t.getNAMSANXUAT());
            } else {
                pst.setNull(6, java.sql.Types.INTEGER);
            }
            pst.setDouble(7, t.getGIANHAP());
            pst.setDouble(8, t.getGIABAN());
            pst.setInt(9, t.getSOLUONG());
            pst.setInt(10, t.getTHOIGIANBAOHANH());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(SanPhamDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `SANPHAM` SET `TEN` = ?, `HINHANH` = ?, `MNCC` = ?, `MVT` = ?, `THUONGHIEU` = ?, `NAMSANXUAT` = ?, `GIANHAP` = ?, `GIABAN` = ?, `SOLUONG` = ?, `THOIGIANBAOHANH` = ? WHERE `MSP`=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getHINHANH());
            pst.setInt(3, t.getMNCC());
            if (t.getMVT() != null) {
                pst.setInt(4, t.getMVT());
            } else {
                pst.setNull(4, java.sql.Types.INTEGER);
            }
            pst.setString(5, t.getTHUONGHIEU());
            if (t.getNAMSANXUAT() != null) {
                pst.setInt(6, t.getNAMSANXUAT());
            } else {
                pst.setNull(6, java.sql.Types.INTEGER);
            }
            pst.setDouble(7, t.getGIANHAP());
            pst.setDouble(8, t.getGIABAN());
            pst.setInt(9, t.getSOLUONG());
            pst.setInt(10, t.getTHOIGIANBAOHANH());
            pst.setInt(11, t.getMSP());

            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `SANPHAM` SET `TRANGTHAI` = 0 WHERE MSP = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<SanPhamDTO> selectAll() {
        ArrayList<SanPhamDTO> result = new ArrayList<SanPhamDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE `TT`= 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                String ten = rs.getString("TEN");
                String hinhanh = rs.getString("HINHANH");
                int mncc = rs.getInt("MNCC");
                Integer mvt = rs.getObject("MVT") != null ? rs.getInt("MVT") : null;
                String thuonghieu = rs.getString("THUONGHIEU");
                Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                double gianhap = rs.getDouble("GIANHAP");
                double giaban = rs.getDouble("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                SanPhamDTO sp = new SanPhamDTO(msp, ten, hinhanh, mncc, mvt, thuonghieu, 
                    namsanxuat, gianhap, giaban, soluong, thoigianbaohanh);
                result.add(sp);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public SanPhamDTO selectById(String t) {
        SanPhamDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE MSP=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                String ten = rs.getString("TEN");
                String hinhanh = rs.getString("HINHANH");
                int mncc = rs.getInt("MNCC");
                Integer mvt = rs.getObject("MVT") != null ? rs.getInt("MVT") : null;
                String thuonghieu = rs.getString("THUONGHIEU");
                Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                double gianhap = rs.getDouble("GIANHAP");
                double giaban = rs.getDouble("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                result = new SanPhamDTO(msp, ten, hinhanh, mncc, mvt, thuonghieu, 
                    namsanxuat, gianhap, giaban, soluong, thoigianbaohanh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }



    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'QuanLyCuaHangDongHo' AND   TABLE_NAME   = 'SANPHAM'";
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
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateSoLuongTon(int MSP, int soluong) {
        int quantity_current = this.selectById(Integer.toString(MSP)).getSL();
        int result = 0;
        int quantity_change = quantity_current + soluong;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `SANPHAM` SET `SOLUONG`=? WHERE MSP = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, quantity_change);
            pst.setInt(2, MSP);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateSoLuongTon(int MSP, int soluong, int tiennhap) {
        SanPhamDTO tmp = this.selectById(Integer.toString(MSP));
        if (tmp.getTIENX() < tiennhap * (120 / 100)) {
            tiennhap = tiennhap * (120 / 100);
        } else {
            tiennhap = tmp.getTIENX();
        }
        int quantity_current = tmp.getSL();
        int result = 0;
        int quantity_change = quantity_current + soluong;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `SANPHAM` SET `SOLUONG`=?, `GIABAN` = ? WHERE MSP = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, quantity_change);
            pst.setInt(2, tiennhap);
            pst.setInt(3, MSP);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateGia(int MSP, int giaxuat) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE `SANPHAM` SET `GIABAN`=? WHERE MSP = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, giaxuat);
            pst.setInt(2, MSP);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int getMaxMSP() {
        int maxMSP = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT MAX(MSP) AS maxMSP FROM SANPHAM";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                maxMSP = rs.getInt("maxMSP");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxMSP;
    }



    public ArrayList<SanPhamDTO> getSPByMaViTri(int maViTri) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE MVT = ? AND TT = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maViTri);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                String ten = rs.getString("TEN");
                String hinhanh = rs.getString("HINHANH");
                int mncc = rs.getInt("MNCC");
                Integer mvt = rs.getObject("MVT") != null ? rs.getInt("MVT") : null;
                String thuonghieu = rs.getString("THUONGHIEU");
                Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                double gianhap = rs.getDouble("GIANHAP");
                double giaban = rs.getDouble("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                SanPhamDTO sp = new SanPhamDTO(msp, ten, hinhanh, mncc, mvt, thuonghieu, 
                    namsanxuat, gianhap, giaban, soluong, thoigianbaohanh);
                result.add(sp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public ArrayList<SanPhamDTO> getSPByMaLoai(int maLoai) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE ML = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maLoai);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                String ten = rs.getString("TEN");
                String hinhanh = rs.getString("HINHANH");
                int mncc = rs.getInt("MNCC");
                Integer mvt = rs.getObject("MVT") != null ? rs.getInt("MVT") : null;
                String thuonghieu = rs.getString("THUONGHIEU");
                Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                double gianhap = rs.getDouble("GIANHAP");
                double giaban = rs.getDouble("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                SanPhamDTO sp = new SanPhamDTO(msp, ten, hinhanh, mncc, mvt, thuonghieu, 
                    namsanxuat, gianhap, giaban, soluong, thoigianbaohanh);
                result.add(sp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public ArrayList<SanPhamDTO> getSPByMaDonVi(int maDonVi) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE MDV = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maDonVi);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int msp = rs.getInt("MSP");
                String ten = rs.getString("TEN");
                String hinhanh = rs.getString("HINHANH");
                int mncc = rs.getInt("MNCC");
                Integer mvt = rs.getObject("MVT") != null ? rs.getInt("MVT") : null;
                String thuonghieu = rs.getString("THUONGHIEU");
                Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                double gianhap = rs.getDouble("GIANHAP");
                double giaban = rs.getDouble("GIABAN");
                int soluong = rs.getInt("SOLUONG");
                int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                SanPhamDTO sp = new SanPhamDTO(msp, ten, hinhanh, mncc, mvt, thuonghieu, 
                    namsanxuat, gianhap, giaban, soluong, thoigianbaohanh);
                result.add(sp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
