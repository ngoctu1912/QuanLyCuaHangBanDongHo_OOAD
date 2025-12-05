package DAO;

import DTO.DanhMucChucNangDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class DanhMucChucNangDAO {

    public static DanhMucChucNangDAO getInstance() {
        return new DanhMucChucNangDAO();
    }

    public ArrayList<DanhMucChucNangDTO> selectAll() {
        ArrayList<DanhMucChucNangDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM DANHMUCCHUCNANG";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                String machucnang = rs.getString("MCN");
                String tenchucnang = rs.getString("TEN");
                DanhMucChucNangDTO dvt = new DanhMucChucNangDTO(machucnang, tenchucnang);
                result.add(dvt);
            }
            JDBCUtil.closeConnection(con);
        } 
        catch (Exception e) {
            
        }
        return result;
    }
}
