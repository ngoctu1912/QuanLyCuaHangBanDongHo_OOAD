package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.CallableStatement; 
import DTO.NhanVienDTO;
import config.JDBCUtil;

public class NhanVienDAO implements DAOinterface<NhanVienDTO>{
    public static NhanVienDAO getInstance(){
        return new NhanVienDAO();
    }

    
  public String getMCNByMNV(int mnv) {
    String mcn = null;
    try (Connection con = JDBCUtil.getConnection("DEFAULT");
         PreparedStatement ps = con.prepareStatement("SELECT MCN FROM NHANVIEN WHERE MNV = ?")) {
        ps.setInt(1, mnv);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                mcn = rs.getString("MCN");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return mcn;
}
  
  
    @Override
   
public int insert(NhanVienDTO t) {
    int result = 0;
    try {
        Connection con = JDBCUtil.getConnection();

        // ✅ gọi đúng SP global
        String sql = "{CALL InsertNhanVien(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = con.prepareCall(sql);

        cs.setString(1, t.getHOTEN());
        cs.setInt(2, t.getGIOITINH());
        cs.setString(3, t.getSDT());
        cs.setDate(4, new java.sql.Date(t.getNGAYSINH().getTime()));
        cs.setInt(5, t.getTT());
        cs.setString(6, t.getEMAIL());
        cs.setInt(7, t.getMCV());
        cs.setString(8, t.getMCN());

        result = cs.executeUpdate();

        cs.close();          // thêm cho sạch
        JDBCUtil.closeConnection(con);

    } catch (SQLException ex) {
        Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
}
   
    @Override
    public int update(NhanVienDTO t) {
        return update(t, null);
    }

    public int update(NhanVienDTO t, String sourceMcnHint) {
        int result = 0;
        String currentMcn = JDBCUtil.getCurrentMcn();
        String sourceMcn = sourceMcnHint == null ? null : sourceMcnHint.trim().toUpperCase();
        String loginMcn = currentMcn == null ? null : currentMcn.trim().toUpperCase();

        boolean useLocal = sourceMcn == null || sourceMcn.isBlank() || loginMcn == null || loginMcn.equalsIgnoreCase(sourceMcn);

        if (useLocal) {
            String sql = "UPDATE NHANVIEN SET HOTEN = ?, GIOITINH = ?, NGAYSINH = ?, SDT = ?, TT = ?, EMAIL = ?, MCV = ?, MCN = ? WHERE MNV = ?";
            try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, t.getHOTEN());
                pst.setInt(2, t.getGIOITINH());
                pst.setDate(3, new java.sql.Date(t.getNGAYSINH().getTime()));
                pst.setString(4, t.getSDT());
                pst.setInt(5, t.getTT());
                pst.setString(6, t.getEMAIL());
                pst.setInt(7, t.getMCV());
                pst.setString(8, t.getMCN());
                pst.setInt(9, t.getMNV());
                result = pst.executeUpdate();
                System.out.println("[NhanVienDAO] UPDATE LOCAL: MNV=" + t.getMNV() + " | affected=" + result);
            } catch (SQLException ex) {
                Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }

        if (!isSupportedMcn(sourceMcn)) {
            Logger.getLogger(NhanVienDAO.class.getName())
                    .log(Level.WARNING, "[NhanVienDAO] UPDATE skipped due to invalid source MCN: {0}", sourceMcn);
            return 0;
        }

        try (Connection con = JDBCUtil.getConnection();
             CallableStatement cst = con.prepareCall("{call UpdateNhanVienLinked(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            cst.setInt(1, t.getMNV());
            cst.setString(2, t.getHOTEN());
            cst.setInt(3, t.getGIOITINH());
            cst.setDate(4, new java.sql.Date(t.getNGAYSINH().getTime()));
            cst.setString(5, t.getSDT());
            cst.setInt(6, t.getTT());
            cst.setString(7, t.getEMAIL());
            cst.setInt(8, t.getMCV());
            cst.setString(9, t.getMCN());
            cst.setString(10, sourceMcn);

            boolean hasResultSet = cst.execute();
            if (hasResultSet) {
                try (ResultSet rs = cst.getResultSet()) {
                    if (rs.next()) {
                        result = rs.getInt("AffectedRows");
                    }
                }
            }
            System.out.println("[NhanVienDAO] UPDATE LINKED (SP): MNV=" + t.getMNV() + " source=" + sourceMcn + " | affected=" + result);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public int delete(String mnv, String sourceMcnHint) {
    int result = 0;
    String currentMcn = JDBCUtil.getCurrentMcn();
    String sourceMcn = sourceMcnHint == null ? null : sourceMcnHint.trim().toUpperCase();
    String loginMcn = currentMcn == null ? null : currentMcn.trim().toUpperCase();

    boolean useLocal = sourceMcn == null || sourceMcn.isBlank() || loginMcn == null || loginMcn.equalsIgnoreCase(sourceMcn);

    if (useLocal) {
        // local soft-delete
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement pst = con.prepareStatement("UPDATE NHANVIEN SET TT = -1 WHERE MNV = ?")) {
            pst.setInt(1, Integer.parseInt(mnv));
            result = pst.executeUpdate();
            System.out.println("[NhanVienDAO] DELETE LOCAL: MNV=" + mnv + " | affected=" + result);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    // linked case: perform update against linked server using four-part name (same approach as updateLinked)
    if (!isSupportedMcn(sourceMcn)) {
        Logger.getLogger(NhanVienDAO.class.getName()).log(Level.WARNING, "[NhanVienDAO] DELETE skipped due to invalid source MCN: {0}", sourceMcn);
        return 0;
    }

    String routeName = getMcnToLinkedServerName(sourceMcn);
    String sql = "UPDATE " + routeName + ".quanlycuahangdongho.dbo.NHANVIEN SET TT = -1 WHERE MNV = ?";

    try (Connection con = JDBCUtil.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, Integer.parseInt(mnv));
        result = pst.executeUpdate();
        System.out.println("[NhanVienDAO] DELETE LINKED: MNV=" + mnv + " source=" + sourceMcn + " | affected=" + result);
    } catch (SQLException ex) {
        Logger.getLogger(NhanVienDAO.class.getName()).log(Level.WARNING, "DELETE LINKED failed, will try DEFAULT DB fallback", ex);
        // fallback to default DB
        try (Connection defCon = JDBCUtil.getConnection("DEFAULT");
             PreparedStatement pst2 = defCon.prepareStatement("UPDATE NHANVIEN SET TT = -1 WHERE MNV = ?")) {
            pst2.setInt(1, Integer.parseInt(mnv));
            int defAffected = pst2.executeUpdate();
            result += defAffected;
            System.out.println("[NhanVienDAO] DELETE FALLBACK DEFAULT DB: MNV=" + mnv + " | affected=" + defAffected);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return result;
}
    
     //LOAD NHÂN VIÊN THEO MÃ CHI NHÁNH SELECT
    public ArrayList<NhanVienDTO> selectAllByMCN(String mcn) {
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        String selectedMcn = mcn == null ? "ALL" : mcn.trim().toUpperCase();

        try {
            try (Connection con = JDBCUtil.getConnection();
                 CallableStatement cst = con.prepareCall("{call sp_GetNhanVienByMCN(?, ?)}")) {
                cst.setString(1, selectedMcn);
                cst.setString(2, JDBCUtil.getCurrentMcn());
                try (ResultSet rs = cst.executeQuery()) {
                    while (rs.next()) {
                        result.add(mapNhanVien(rs));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    

    private String getMcnToLinkedServerName(String mcn) {
        if (mcn == null || mcn.isBlank()) {
            return "[CN1]";
        }
        return "[" + mcn.trim().toUpperCase() + "]";
    }

    private boolean isSupportedMcn(String mcn) {
        return "CN1".equalsIgnoreCase(mcn)
                || "CN2".equalsIgnoreCase(mcn)
                || "CN3".equalsIgnoreCase(mcn);
    }

    @Override
    public int delete(String t) {
        return delete(t, null);
    }


    @Override
    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> result = new ArrayList<NhanVienDTO>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM NHANVIEN WHERE TT = 1";
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
                String MCN=rs.getString("MCN");
                NhanVienDTO nv = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV,MCN);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
   
    
    private NhanVienDTO mapNhanVien(ResultSet rs) throws SQLException {
        int MNV = rs.getInt("MNV");
        String HOTEN = rs.getString("HOTEN");
        int GIOITINH = rs.getInt("GIOITINH");
        Date NGAYSINH = rs.getDate("NGAYSINH");
        String SDT = rs.getString("SDT");
        int TT = rs.getInt("TT");
        String EMAIL = rs.getString("EMAIL");
        int MCV = rs.getInt("MCV");
        String MCN = rs.getString("MCN");
        return new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV, MCN);
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
                 String MCN=rs.getString("MCN");
                NhanVienDTO nv = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV,MCN);
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
                 String MCN=rs.getString("MCN");
                NhanVienDTO nv = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV,MCN);
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
            String sql = "SELECT * FROM NHANVIEN WHERE MNV = ? AND TT = 1";
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
                 String MCN=rs.getString("MCN");
                result = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV,MCN);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ selectById() lỗi MNV=" + t);
        }
        return result;
    }
    
    // 🔥 Query NHANVIEN từ MANHDUNG1 (database trung tâm) để lấy MCN
    public NhanVienDTO selectByIdFromCentral(String mnv) {
        NhanVienDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection("DEFAULT");  // 🔥 MANHDUNG1
            String sql = "SELECT * FROM NHANVIEN WHERE MNV = ? AND TT = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, mnv);
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
                String MCN=rs.getString("MCN");
                result = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV,MCN);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ selectByIdFromCentral() lỗi MNV=" + mnv);
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
                 String MCN=rs.getString("MCN");
                result = new NhanVienDTO(MNV, HOTEN, GIOITINH, SDT, NGAYSINH, TT, EMAIL, MCV,MCN);
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
            String sql = "SELECT CAST(IDENT_CURRENT('NHANVIEN') AS INT) AS AUTO_INCREMENT";
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
