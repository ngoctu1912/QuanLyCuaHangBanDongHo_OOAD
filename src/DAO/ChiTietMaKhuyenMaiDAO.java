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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL InsertChiTietMaKhuyenMai(?, ?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t.getMKM());
            cs.setInt(2, t.getMSP());
            cs.setInt(3, t.getPTG());
            result = cs.executeUpdate();
            cs.close();
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
                Connection con = JDBCUtil.getConnection();
                String sql = "{CALL InsertChiTietMaKhuyenMai(?, ?, ?)}";
                CallableStatement cs = con.prepareCall(sql);
                cs.setString(1, t.get(i).getMKM());
                cs.setInt(2, t.get(i).getMSP());
                cs.setInt(3, t.get(i).getPTG());
                result = cs.executeUpdate();
                cs.close();
                JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietMaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int update(ChiTietMaKhuyenMaiDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL UpdateChiTietMaKhuyenMai(?, ?, ?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t.getMKM());
            cs.setInt(2, t.getMSP());
            cs.setInt(3, t.getPTG());
            cs.setString(4, t.getMKM());
            result = cs.executeUpdate();
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL DeleteChiTietMaKhuyenMai(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            result = cs.executeUpdate();
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetAllChiTietMaKhuyenMai}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                ChiTietMaKhuyenMaiDTO kh = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
                result.add(kh);
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public ArrayList<ChiTietMaKhuyenMaiDTO> selectAll(String t) {
        ArrayList<ChiTietMaKhuyenMaiDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetChiTietMaKhuyenMaiByMKM(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                ChiTietMaKhuyenMaiDTO kh = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
                result.add(kh);
            }
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetChiTietMaKhuyenMaiByMKM(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, t);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                result = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
            }
            cs.close();
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
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetAutoIncrementChiTietMaKhuyenMai}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietMaKhuyenMaiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<ChiTietMaKhuyenMaiDTO> searchByMSP(int msp) {
        ArrayList<ChiTietMaKhuyenMaiDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetChiTietMaKhuyenMaiByMSP(?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setInt(1, msp);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSPValue = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                ChiTietMaKhuyenMaiDTO kh = new ChiTietMaKhuyenMaiDTO(MKM, MSPValue, PTG);
                result.add(kh);
            }
            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public ChiTietMaKhuyenMaiDTO selectByMKMAndMSP(String mkm, int msp) {
        ChiTietMaKhuyenMaiDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "{CALL GetChiTietMaKhuyenMaiByMKMAndMSP(?, ?)}";
            CallableStatement cs = con.prepareCall(sql);
            cs.setString(1, mkm);
            cs.setInt(2, msp);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                String MKM = rs.getString("MKM");
                int MSP = rs.getInt("MSP");
                int PTG = rs.getInt("PTG");
                result = new ChiTietMaKhuyenMaiDTO(MKM, MSP, PTG);
            }

            cs.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

}
