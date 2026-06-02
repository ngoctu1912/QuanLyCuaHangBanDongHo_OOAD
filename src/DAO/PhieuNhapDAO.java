package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import BUS.SanPhamBUS;
import DTO.ChiTietPhieuNhapDTO;
import DTO.PhieuNhapDTO;
import DTO.SanPhamDTO;
import config.JDBCUtil;

public class PhieuNhapDAO implements DAOinterface<PhieuNhapDTO> {

    public static PhieuNhapDAO getInstance() {
        return new PhieuNhapDAO();
    }

    @Override
    public int insert(PhieuNhapDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO PHIEUNHAP (MNV, MNCC, TIEN, TG, TT, MCN) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, t.getMNV());
            pst.setInt(2, t.getMNCC());
            pst.setDouble(3, t.getTIEN());
            pst.setTimestamp(4, t.getTG());
            pst.setInt(5, t.getTT());
            pst.setString(6, t.getMCN());
            result = pst.executeUpdate();
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    int generated = keys.getInt(1);
                    t.setMP(generated);
                }
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(PhieuNhapDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE PHIEUNHAP SET TG=?,MNCC=?,TIEN=?,TT=? WHERE MPN=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setTimestamp(1, t.getTG());
            pst.setInt(2, t.getMNCC());
            pst.setLong(3, t.getTIEN());
            pst.setInt(4, t.getTT());
            pst.setInt(5, t.getMP());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE PHIEUNHAP SET TT = 0 WHERE MPN = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public int cancel(int maphieu) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE PHIEUNHAP SET TT = 0 WHERE MPN = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, maphieu);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<PhieuNhapDTO> selectAll() {
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT MPN, TG, MNCC, MNV, TIEN, TT, MCN FROM PHIEUNHAP ORDER BY MPN DESC";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int MPN = rs.getInt("MPN");
                Timestamp TG = rs.getTimestamp("TG");
                int MNCC = rs.getInt("MNCC");
                int MNV = rs.getInt("MNV");
                long TIENN = rs.getLong("TIEN");
                int TT = rs.getInt("TT");
                String MCN = rs.getString("MCN");
                PhieuNhapDTO phieunhap = new PhieuNhapDTO(MNCC, MPN, MNV, TG, TIENN, TT, MCN);
                result.add(phieunhap);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, "Lỗi load PHIEUNHAP", e);
        }
        return result;
    }

    @Override
    public PhieuNhapDTO selectById(String t) {
        PhieuNhapDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT MPN, TG, MNCC, MNV, TIEN, TT, MCN FROM PHIEUNHAP WHERE MPN=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int MPN = rs.getInt("MPN");
                Timestamp TG = rs.getTimestamp("TG");
                int MNCC = rs.getInt("MNCC");
                int MNV = rs.getInt("MNV");
                long TIENN = rs.getLong("TIEN");
                int TT = rs.getInt("TT");
                String MCN = rs.getString("MCN");
                result = new PhieuNhapDTO(MNCC, MPN, MNV, TG, TIENN, TT, MCN);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, "Lỗi load PHIEUNHAP theo ID: " + t, e);
        }
        return result;
    }

    public ArrayList<PhieuNhapDTO> statistical(long min, long max) {
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM PHIEUNHAP WHERE TIEN BETWEEN ? AND ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setLong(1, min);
            pst.setLong(2, max);

            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int MPN = rs.getInt("MPN");
                Timestamp TG = rs.getTimestamp("TG");
                int MNCC = rs.getInt("MNCC");
                int MNV = rs.getInt("MNV");
                long TIENN = rs.getLong("TIEN");
                int TT = rs.getInt("TT");
                String MCN=rs.getString("MCN");
                PhieuNhapDTO phieunhap = new PhieuNhapDTO(MNCC, MPN, MNV, TG, TIENN, TT,MCN);
                result.add(phieunhap);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }

    public boolean checkSLPn(int maphieu) {
        SanPhamBUS spBus = new SanPhamBUS();
        ArrayList<SanPhamDTO> SP = new ArrayList<SanPhamDTO>();
        ArrayList<ChiTietPhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM CTPHIEUNHAP WHERE MPN=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, maphieu);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int masp = rs.getInt("MSP");
                int soluong = rs.getInt("SL");
                int tiennhap = rs.getInt("TIENNHAP");
                int hinhthucnhap = rs.getInt("HINHTHUC");
                ChiTietPhieuNhapDTO ct = new ChiTietPhieuNhapDTO(maphieu, masp, soluong, tiennhap, hinhthucnhap);
                result.add(ct);
                SP.add(spBus.spDAO.selectById(ct.getMSP() + ""));
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        for (int i = 0; i < SP.size(); i++) {
            if (result.get(i).getSL() > SP.get(i).getSL()) {
                return false;
            }
        }
        return true;
    }

    public int cancelPhieuNhap(int maphieu) {
        int result = 0;
        ArrayList<ChiTietPhieuNhapDTO> arrCt = ChiTietPhieuNhapDAO.getInstance().selectAll(Integer.toString(maphieu));
        for (ChiTietPhieuNhapDTO chiTietPhieuNhapDTO : arrCt) {
            // TODO: Cập nhật TONKHO để hoàn trả số lượng
            // SanPhamDAO.getInstance().updateSoLuongTon(chiTietPhieuNhapDTO.getMSP(), -(chiTietPhieuNhapDTO.getSL()));
        }
        ChiTietPhieuNhapDAO.getInstance().delete(Integer.toString(maphieu));
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM PHIEUNHAP WHERE MPN = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maphieu);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT CAST(IDENT_CURRENT('PHIEUNHAP') AS INT) AS AUTO_INCREMENT";
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
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<PhieuNhapDTO> selectPhieuNhapByMNCC(int mncc) {
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM PHIEUNHAP WHERE MNCC = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mncc);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MPN = rs.getInt("MPN");
                Timestamp TG = rs.getTimestamp("TG");
                int MNCC = rs.getInt("MNCC");
                int MNV = rs.getInt("MNV");
                long TIEN = rs.getLong("TIEN");
                int TT = rs.getInt("TT");
                String MCN=rs.getString("MCN");
                PhieuNhapDTO phieuNhap = new PhieuNhapDTO(MNCC, MPN, MNV, TG, TIEN, TT,MCN);
                result.add(phieuNhap);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<PhieuNhapDTO> selectPhieuNhapByMCN(String mcn) {
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT MPN, TG, MNCC, MNV, TIEN, TT, MCN FROM PHIEUNHAP WHERE MCN = ? ORDER BY MPN DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, mcn);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MPN = rs.getInt("MPN");
                Timestamp TG = rs.getTimestamp("TG");
                int MNCC = rs.getInt("MNCC");
                int MNV = rs.getInt("MNV");
                long TIEN = rs.getLong("TIEN");
                int TT = rs.getInt("TT");
                String MCN = rs.getString("MCN");
                PhieuNhapDTO phieuNhap = new PhieuNhapDTO(MNCC, MPN, MNV, TG, TIEN, TT, MCN);
                result.add(phieuNhap);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, "Lỗi load PHIEUNHAP theo MCN: " + mcn, ex);
        }
        return result;
    }

}
