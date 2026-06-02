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
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO SANPHAM (TEN, HINHANH, MNCC, THUONGHIEU, NAMSANXUAT, GIANHAP, GIABAN, THOIGIANBAOHANH, TT) VALUES (?,?,?,?,?,?,?, ?,1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getHINHANH());
            pst.setInt(3, t.getMNCC());
            pst.setString(4, t.getTHUONGHIEU());
            if (t.getNAMSANXUAT() != null) {
                pst.setInt(5, t.getNAMSANXUAT());
            } else {
                pst.setNull(5, java.sql.Types.INTEGER);
            }
            pst.setDouble(6, t.getGIANHAP());
            pst.setDouble(7, t.getGIABAN());
            pst.setInt(8, t.getTHOIGIANBAOHANH());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(SanPhamDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE SANPHAM SET TEN = ?, HINHANH = ?, MNCC = ?, THUONGHIEU = ?, NAMSANXUAT = ?, GIANHAP = ?, GIABAN = ?, THOIGIANBAOHANH = ? WHERE MSP=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTEN());
            pst.setString(2, t.getHINHANH());
            pst.setInt(3, t.getMNCC());
            pst.setString(4, t.getTHUONGHIEU());
            if (t.getNAMSANXUAT() != null) {
                pst.setInt(5, t.getNAMSANXUAT());
            } else {
                pst.setNull(5, java.sql.Types.INTEGER);
            }
            pst.setDouble(6, t.getGIANHAP());
            pst.setDouble(7, t.getGIABAN());
            pst.setInt(8, t.getTHOIGIANBAOHANH());
            pst.setInt(9, t.getMSP());

            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE SANPHAM SET TRANGTHAI = 0 WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<SanPhamDTO> selectAll() {
        // Gọi selectAll với MCN mặc định là null để lấy tất cả (không lọc chi nhánh)
        return selectAllByMCN(null);
    }

    /**
     * Load sản phẩm có tồn kho ở chi nhánh cụ thể
     * @param mcn Mã chi nhánh. Nếu null, lấy tất cả sản phẩm
     * @return Danh sách sản phẩm với số lượng tồn kho
     */
    public ArrayList<SanPhamDTO> selectAllByMCN(String mcn) {
        ArrayList<SanPhamDTO> result = new ArrayList<SanPhamDTO>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql;
            PreparedStatement pst;

            if (mcn != null && !mcn.isEmpty()) {
                sql = "SELECT sp.MSP, sp.TEN, sp.HINHANH, sp.MNCC, sp.THUONGHIEU, sp.NAMSANXUAT, " +
                      "sp.GIANHAP, sp.GIABAN, sp.THOIGIANBAOHANH, COALESCE(tk.SOLUONG, 0) AS SOLUONG " +
                      "FROM SANPHAM sp " +
                      "LEFT JOIN ( " +
                      "    SELECT tk.MSP, SUM(tk.SOLUONG) AS SOLUONG " +
                      "    FROM TONKHO tk " +
                      "    JOIN KHO k ON k.MKHO = tk.MKHO " +
                      "    WHERE k.MCN = ? " +
                      "    GROUP BY tk.MSP " +
                      ") tk ON sp.MSP = tk.MSP " +
                      "WHERE sp.TT = 1";
                pst = con.prepareStatement(sql);
                pst.setString(1, mcn);
            } else {
                sql = "SELECT sp.MSP, sp.TEN, sp.HINHANH, sp.MNCC, sp.THUONGHIEU, sp.NAMSANXUAT, " +
                      "sp.GIANHAP, sp.GIABAN, sp.THOIGIANBAOHANH, COALESCE(tk.SOLUONG, 0) AS SOLUONG " +
                      "FROM SANPHAM sp " +
                      "LEFT JOIN ( " +
                      "    SELECT MSP, SUM(SOLUONG) AS SOLUONG " +
                      "    FROM TONKHO " +
                      "    GROUP BY MSP " +
                      ") tk ON sp.MSP = tk.MSP " +
                      "WHERE sp.TT = 1";
                pst = con.prepareStatement(sql);
            }
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int msp = rs.getInt("MSP");
                    String ten = rs.getString("TEN");
                    String hinhanh = rs.getString("HINHANH");
                    int mncc = rs.getInt("MNCC");
                    String thuonghieu = rs.getString("THUONGHIEU");
                    Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                    double gianhap = rs.getDouble("GIANHAP");
                    double giaban = rs.getDouble("GIABAN");
                    int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                    int soluong = rs.getInt("SOLUONG");
                    SanPhamDTO sp = new SanPhamDTO(msp, ten, hinhanh, mncc, null, thuonghieu,
                        namsanxuat, gianhap, giaban, thoigianbaohanh, soluong);
                    result.add(sp);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, "Lỗi load danh sách sản phẩm", e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public SanPhamDTO selectById(String t) {
        SanPhamDTO result = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT MSP, TEN, HINHANH, MNCC, THUONGHIEU, NAMSANXUAT, GIANHAP, GIABAN, THOIGIANBAOHANH FROM SANPHAM WHERE MSP=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int msp = rs.getInt("MSP");
                    String ten = rs.getString("TEN");
                    String hinhanh = rs.getString("HINHANH");
                    int mncc = rs.getInt("MNCC");
                    String thuonghieu = rs.getString("THUONGHIEU");
                    Integer namsanxuat = rs.getObject("NAMSANXUAT") != null ? rs.getInt("NAMSANXUAT") : null;
                    double gianhap = rs.getDouble("GIANHAP");
                    double giaban = rs.getDouble("GIABAN");
                    int thoigianbaohanh = rs.getInt("THOIGIANBAOHANH");
                    result = new SanPhamDTO(msp, ten, hinhanh, mncc, null, thuonghieu,
                        namsanxuat, gianhap, giaban, thoigianbaohanh);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, "Lỗi tìm sản phẩm theo ID", e);
        }
        return result;
    }



    @Override
    public int getAutoIncrement() {
        int result = -1;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT CAST(IDENT_CURRENT('SANPHAM') AS INT) AS AUTO_INCREMENT";
            PreparedStatement pst = con.prepareStatement(sql);
            try (ResultSet rs2 = pst.executeQuery()) {
                if (!rs2.isBeforeFirst()) {
                    System.out.println("No data");
                } else {
                    while (rs2.next()) {
                        result = rs2.getInt("AUTO_INCREMENT");
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }



    public int updateGia(int MSP, int giaxuat) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE SANPHAM SET GIABAN=? WHERE MSP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, giaxuat);
            pst.setInt(2, MSP);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int getMaxMSP() {
        int maxMSP = -1;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT MAX(MSP) AS maxMSP FROM SANPHAM";
            PreparedStatement pst = con.prepareStatement(sql);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    maxMSP = rs.getInt("maxMSP");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxMSP;
    }



    public ArrayList<SanPhamDTO> getSPByMaViTri(int maViTri) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        for (SanPhamDTO sp : selectAll()) {
            if (sp.getMVT() != null && sp.getMVT() == maViTri) {
                result.add(sp);
            }
        }
        return result;
    }

    public ArrayList<SanPhamDTO> getSPByMaLoai(int maLoai) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        for (SanPhamDTO sp : selectAll()) {
            if (sp.getMNCC() == maLoai) {
                result.add(sp);
            }
        }
        return result;
    }

    public ArrayList<SanPhamDTO> getSPByMaDonVi(int maDonVi) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        for (SanPhamDTO sp : selectAll()) {
            if (sp.getMVT() != null && sp.getMVT() == maDonVi) {
                result.add(sp);
            }
        }
        return result;
    }
}
