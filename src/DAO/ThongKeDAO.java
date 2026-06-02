package DAO;

import DTO.ThongKe.ThongKeDoanhThuChiNhanhDTO;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeKhachHangDTO;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import DTO.ThongKe.ThongKeNhanVienBanChayDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import DTO.ThongKe.ThongKeTopSanPhamDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import config.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeDAO {

    private static final String DB_NAME = "quanlycuahangdongho";

    public static ThongKeDAO getInstance() {
        return new ThongKeDAO();
    }

    // ================================================================
    // HELPER — build tên bảng có schema rõ ràng
    // mcn = null → [dbo].[TABLE] (local)
    // mcn = "CN2" → [CN2].[db].[dbo].[TABLE] (Linked Server)
    // ================================================================
    private static String tbl(String mcn, String tableName) {
        if (mcn == null) {
            return "[dbo].[" + tableName + "]";
        }
        return "[" + mcn + "].[" + DB_NAME + "].[dbo].[" + tableName + "]";
    }

    private static String currentMcn() {
        String mcn = JDBCUtil.getCurrentMcn();
        return (mcn == null || mcn.isBlank()) ? "CN1" : mcn.trim().toUpperCase();
    }

    private static List<String> otherBranches(String currentMcn) {
        List<String> all = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
        all.remove(currentMcn.toUpperCase());
        return all;
    }

    // ================================================================
    // HELPER — build SELECT theo ngày cho 1 CN (dùng alias)
    // ================================================================
    private void appendNgayQuery(StringBuilder sb, String mcn) {
     String px  = tbl(mcn, "PHIEUXUAT");
     String cpx = tbl(mcn, "CTPHIEUXUAT");
     String pn  = tbl(mcn, "PHIEUNHAP");
     String cpn = tbl(mcn, "CTPHIEUNHAP");

     // Doanh thu: tính từ phiếu xuất theo ngày
     // Chi phí: tính từ phiếu nhập theo ngày (độc lập với phiếu xuất)
     sb.append("SELECT dates.date, ")
       .append("COALESCE(sub_nhap.chiphi, 0) AS chiphi, ")
       .append("COALESCE(sub_xuat.doanhthu, 0) AS doanhthu ")
       .append("FROM dates ")
       // subquery doanh thu
       .append("LEFT JOIN (")
       .append("  SELECT CAST(px.TG AS DATE) AS ngay, SUM(cpx.TIENXUAT) AS doanhthu ")
       .append("  FROM ").append(px).append(" px ")
       .append("  JOIN ").append(cpx).append(" cpx ON px.MPX = cpx.MPX ")
       .append("  WHERE px.TT = 1 ")
       .append("  GROUP BY CAST(px.TG AS DATE)")
       .append(") sub_xuat ON sub_xuat.ngay = dates.date ")
       // subquery chi phí
       .append("LEFT JOIN (")
       .append("  SELECT CAST(pn.TG AS DATE) AS ngay, SUM(cpn.TIENNHAP) AS chiphi ")
       .append("  FROM ").append(pn).append(" pn ")
       .append("  JOIN ").append(cpn).append(" cpn ON pn.MPN = cpn.MPN ")
       .append("  GROUP BY CAST(pn.TG AS DATE)")
       .append(") sub_nhap ON sub_nhap.ngay = dates.date");
    }

    // ================================================================
    // HELPER — build SELECT theo năm cho 1 CN (dùng alias)
    // ================================================================
        private void appendThangQuery(StringBuilder sb, String mcn) {
        String px  = tbl(mcn, "PHIEUXUAT");
        String cpx = tbl(mcn, "CTPHIEUXUAT");
        String pn  = tbl(mcn, "PHIEUNHAP");
        String cpn = tbl(mcn, "CTPHIEUNHAP");

        sb.append("SELECT months.month, ")
          .append("COALESCE(sub_nhap.chiphi, 0) AS chiphi, ")
          .append("COALESCE(sub_xuat.doanhthu, 0) AS doanhthu ")
          .append("FROM months ")
          .append("LEFT JOIN (")
          .append("  SELECT MONTH(px.TG) AS thang, SUM(cpx.TIENXUAT) AS doanhthu ")
          .append("  FROM ").append(px).append(" px ")
          .append("  JOIN ").append(cpx).append(" cpx ON px.MPX = cpx.MPX ")
          .append("  WHERE px.TT = 1 AND YEAR(px.TG) = ? ")
          .append("  GROUP BY MONTH(px.TG)")
          .append(") sub_xuat ON sub_xuat.thang = months.month ")
          .append("LEFT JOIN (")
          .append("  SELECT MONTH(pn.TG) AS thang, SUM(cpn.TIENNHAP) AS chiphi ")
          .append("  FROM ").append(pn).append(" pn ")
          .append("  JOIN ").append(cpn).append(" cpn ON pn.MPN = cpn.MPN ")
          .append("  WHERE YEAR(pn.TG) = ? ")
          .append("  GROUP BY MONTH(pn.TG)")
          .append(") sub_nhap ON sub_nhap.thang = months.month");
    }

    private void appendNamQuery(StringBuilder sb, String mcn) {
        String px  = tbl(mcn, "PHIEUXUAT");
        String cpx = tbl(mcn, "CTPHIEUXUAT");
        String pn  = tbl(mcn, "PHIEUNHAP");
        String cpn = tbl(mcn, "CTPHIEUNHAP");

        sb.append("SELECT years.year, ")
          .append("COALESCE(sub_nhap.chiphi, 0) AS chiphi, ")
          .append("COALESCE(sub_xuat.doanhthu, 0) AS doanhthu ")
          .append("FROM years ")
          .append("LEFT JOIN (")
          .append("  SELECT YEAR(px.TG) AS nam, SUM(cpx.TIENXUAT) AS doanhthu ")
          .append("  FROM ").append(px).append(" px ")
          .append("  JOIN ").append(cpx).append(" cpx ON px.MPX = cpx.MPX ")
          .append("  WHERE px.TT = 1 ")
          .append("  GROUP BY YEAR(px.TG)")
          .append(") sub_xuat ON sub_xuat.nam = years.year ")
          .append("LEFT JOIN (")
          .append("  SELECT YEAR(pn.TG) AS nam, SUM(cpn.TIENNHAP) AS chiphi ")
          .append("  FROM ").append(pn).append(" pn ")
          .append("  JOIN ").append(cpn).append(" cpn ON pn.MPN = cpn.MPN ")
          .append("  GROUP BY YEAR(pn.TG)")
          .append(") sub_nhap ON sub_nhap.nam = years.year");
    }

    // ================================================================
    // 1. TỒN KHO — FIX: thêm alias rõ ràng để tránh Ambiguous MSP
    // ================================================================
    public static ArrayList<ThongKeTonKhoDTO> getThongKeTonKho(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeTonKhoDTO> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeEnd.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = """
                WITH nhap AS (
                    SELECT cpn.MSP, SUM(cpn.SL) AS sl_nhap
                    FROM [dbo].[CTPHIEUNHAP] cpn
                    JOIN [dbo].[PHIEUNHAP] pn ON pn.MPN = cpn.MPN
                    WHERE pn.TG BETWEEN ? AND ?
                    GROUP BY cpn.MSP
                ),
                xuat AS (
                    SELECT cpx.MSP, SUM(cpx.SL) AS sl_xuat
                    FROM [dbo].[CTPHIEUXUAT] cpx
                    JOIN [dbo].[PHIEUXUAT] px ON px.MPX = cpx.MPX
                    WHERE px.TG BETWEEN ? AND ?
                    GROUP BY cpx.MSP
                ),
                nhap_dau AS (
                    SELECT cpn.MSP, SUM(cpn.SL) AS sl_nhap_dau
                    FROM [dbo].[PHIEUNHAP] pn
                    JOIN [dbo].[CTPHIEUNHAP] cpn ON pn.MPN = cpn.MPN
                    WHERE pn.TG < ?
                    GROUP BY cpn.MSP
                ),
                xuat_dau AS (
                    SELECT cpx.MSP, SUM(cpx.SL) AS sl_xuat_dau
                    FROM [dbo].[PHIEUXUAT] px
                    JOIN [dbo].[CTPHIEUXUAT] cpx ON px.MPX = cpx.MPX
                    WHERE px.TG < ?
                    GROUP BY cpx.MSP
                ),
                dau_ky AS (
                    SELECT sp.MSP,
                        COALESCE(nd.sl_nhap_dau,0) - COALESCE(xd.sl_xuat_dau,0) AS SLdauky
                    FROM [dbo].[SANPHAM] sp
                    LEFT JOIN nhap_dau nd ON sp.MSP = nd.MSP
                    LEFT JOIN xuat_dau xd ON sp.MSP = xd.MSP
                )
                SELECT sp.MSP, sp.TEN,
                    dk.SLdauky,
                    COALESCE(n.sl_nhap,0) AS SLnhap,
                    COALESCE(x.sl_xuat,0) AS SLxuat,
                    (dk.SLdauky + COALESCE(n.sl_nhap,0) - COALESCE(x.sl_xuat,0)) AS SLcuoiky
                FROM dau_ky dk
                LEFT JOIN nhap n  ON dk.MSP = n.MSP
                LEFT JOIN xuat x  ON dk.MSP = x.MSP
                JOIN [dbo].[SANPHAM] sp ON dk.MSP = sp.MSP
                WHERE sp.TEN LIKE ? OR CAST(sp.MSP AS NVARCHAR) LIKE ?
                ORDER BY sp.MSP
                """;
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
            pst.setTimestamp(3, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(4, new Timestamp(cal.getTimeInMillis()));
            pst.setTimestamp(5, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(6, new Timestamp(timeStart.getTime()));
            pst.setString(7, "%" + text + "%");
            pst.setString(8, "%" + text + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new ThongKeTonKhoDTO(
                    rs.getInt("MSP"), rs.getString("TEN"),
                    rs.getInt("SLdauky"), rs.getInt("SLnhap"),
                    rs.getInt("SLxuat"), rs.getInt("SLcuoiky")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 2. DOANH THU THEO CHI NHÁNH — FIX: dùng alias trong JOIN ON
    // ================================================================
    public ArrayList<ThongKeDoanhThuChiNhanhDTO> getDoanhThuTheoChiNhanh(String selectedBranch) {
        ArrayList<ThongKeDoanhThuChiNhanhDTO> result = new ArrayList<>();
        String mcn = currentMcn();

        String selectedMcn = null;
        if (selectedBranch != null && !selectedBranch.equalsIgnoreCase("Tất cả chi nhánh")) {
            String[] parts = selectedBranch.trim().split("\\s+");
            try {
                selectedMcn = "CN" + Integer.parseInt(parts[parts.length - 1]);
            } catch (NumberFormatException ex) {
                selectedMcn = selectedBranch.trim().toUpperCase().replaceAll("\\s+", "");
            }
        }

        List<String> targets = selectedMcn == null
                ? new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"))
                : new ArrayList<>(Arrays.asList(selectedMcn));

        try (Connection con = JDBCUtil.getConnection(mcn)) {
            for (String target : targets) {
                boolean isLocal = target.equalsIgnoreCase(mcn);
                // ✅ FIX: tên bảng đầy đủ chỉ trong FROM/JOIN, dùng alias trong ON
                String pxTable  = tbl(isLocal ? null : target, "PHIEUXUAT");
                String cpxTable = tbl(isLocal ? null : target, "CTPHIEUXUAT");

                String sql = "SELECT '" + target + "' AS chiNhanh, "
                        + "COALESCE(SUM(cpx.TIENXUAT), 0) AS tongDoanhThu, "
                        + "COUNT(DISTINCT px.MPX) AS soHoaDon, "
                        + "CASE WHEN COUNT(DISTINCT px.MPX) > 0 "
                        + "  THEN COALESCE(SUM(cpx.TIENXUAT),0) / COUNT(DISTINCT px.MPX) "
                        + "  ELSE 0 END AS trungBinh "
                        + "FROM " + pxTable + " px "
                        + "LEFT JOIN " + cpxTable + " cpx ON px.MPX = cpx.MPX "
                        + "WHERE px.TT = 1";

                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    result.add(new ThongKeDoanhThuChiNhanhDTO(
                        rs.getString("chiNhanh"),
                        rs.getLong("tongDoanhThu"),
                        rs.getInt("soHoaDon"),
                        rs.getLong("trungBinh")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 3. DOANH THU TỪNG NĂM — gộp tất cả CN
    // ================================================================
    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int year_start, int year_end) {
        ArrayList<ThongKeDoanhThuDTO> result = new ArrayList<>();
        String mcn = currentMcn();
        List<String> others = otherBranches(mcn);
        try (Connection con = JDBCUtil.getConnection(mcn)) {
            StringBuilder sql = new StringBuilder();
            sql.append("DECLARE @s INT = ").append(year_start).append("; ");
            sql.append("DECLARE @e INT = ").append(year_end).append("; ");
            sql.append("WITH years(year) AS (");
            sql.append("  SELECT @s UNION ALL SELECT year+1 FROM years WHERE year < @e");
            sql.append("), all_data AS (");
            appendNamQuery(sql, null);
            for (String branch : others) {
                sql.append(" UNION ALL ");
                appendNamQuery(sql, branch);
            }
            sql.append(") SELECT year AS nam, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu ");
            sql.append("FROM all_data GROUP BY year ORDER BY year OPTION (MAXRECURSION 100)");

            PreparedStatement pst = con.prepareStatement(sql.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                long chiphi = rs.getLong("chiphi");
                long doanhthu = rs.getLong("doanhthu");
                result.add(new ThongKeDoanhThuDTO(rs.getInt("nam"), chiphi, doanhthu, doanhthu - chiphi));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 4. DOANH THU TỪNG THÁNG — gộp tất cả CN
    // ================================================================
    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        String mcn = currentMcn();
        List<String> others = otherBranches(mcn);
        try (Connection con = JDBCUtil.getConnection(mcn)) {
            StringBuilder sql = new StringBuilder();
            sql.append("WITH months(month) AS (");
            sql.append("  SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 ");
            sql.append("  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 ");
            sql.append("  UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12");
            sql.append("), all_data AS (");
            appendThangQuery(sql, null);
            for (String branch : others) {
                sql.append(" UNION ALL ");
                appendThangQuery(sql, branch);
            }
            sql.append(") SELECT month AS thang, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu ");
            sql.append("FROM all_data GROUP BY month ORDER BY month");

            PreparedStatement pst = con.prepareStatement(sql.toString());
            // Trong getThongKeTheoThang — mỗi branch cần 2 param thay vì 1
            int idx = 1;
            pst.setInt(idx++, nam); // cho sub_xuat của local
            pst.setInt(idx++, nam); // cho sub_nhap của local
            for (int i = 0; i < others.size(); i++) {
                pst.setInt(idx++, nam); // sub_xuat của branch khác
                pst.setInt(idx++, nam); // sub_nhap của branch khác
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                result.add(new ThongKeTheoThangDTO(rs.getInt("thang"), chiphi, doanhthu, doanhthu - chiphi));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 5. DOANH THU TỪNG NGÀY TRONG THÁNG — gộp tất cả CN
    // ================================================================
    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTungNgayTrongThang(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        String mcn = currentMcn();
        List<String> others = otherBranches(mcn);
        String ngayString = nam + "-" + String.format("%02d", thang) + "-01";
        try (Connection con = JDBCUtil.getConnection(mcn)) {
            StringBuilder sql = new StringBuilder();
            sql.append("WITH dates(date) AS (");
            sql.append("  SELECT CAST('").append(ngayString).append("' AS DATE) ");
            sql.append("  UNION ALL SELECT DATEADD(DAY,1,date) FROM dates ");
            sql.append("  WHERE date < EOMONTH(CAST('").append(ngayString).append("' AS DATE))");
            sql.append("), all_data AS (");
            appendNgayQuery(sql, null);
            for (String branch : others) {
                sql.append(" UNION ALL ");
                appendNgayQuery(sql, branch);
            }
            sql.append(") SELECT date AS ngay, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu ");
            sql.append("FROM all_data GROUP BY date ORDER BY date OPTION (MAXRECURSION 100)");

            PreparedStatement pst = con.prepareStatement(sql.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                result.add(new ThongKeTungNgayTrongThangDTO(rs.getDate("ngay"), chiphi, doanhthu, doanhthu - chiphi));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 6. DOANH THU TỪ NGÀY ĐẾN NGÀY — SQL Server syntax
    // ================================================================
    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTuNgayDenNgay(
        String start, String end, String selectedBranch) {

    ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
    String mcn = currentMcn();

    // Xác định danh sách CN cần query
    List<String> targets;
    if (selectedBranch == null || selectedBranch.equalsIgnoreCase("Tất cả chi nhánh")) {
        targets = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
    } else {
        String[] parts = selectedBranch.trim().split("\\s+");
        try {
            String target = "CN" + Integer.parseInt(parts[parts.length - 1]);
            targets = new ArrayList<>(Arrays.asList(target));
        } catch (NumberFormatException ex) {
            targets = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
        }
    }

    try (Connection con = JDBCUtil.getConnection(mcn)) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH dates(date) AS (");
        sql.append("  SELECT CAST('").append(start).append("' AS DATE) ");
        sql.append("  UNION ALL SELECT DATEADD(DAY,1,date) FROM dates ");
        sql.append("  WHERE date < CAST('").append(end).append("' AS DATE)");
        sql.append("), all_data AS (");

        for (int i = 0; i < targets.size(); i++) {
            String target = targets.get(i);
            boolean isLocal = target.equalsIgnoreCase(mcn);
            appendNgayQuery(sql, isLocal ? null : target);
            if (i < targets.size() - 1) sql.append(" UNION ALL ");
        }

        sql.append(") SELECT date AS ngay, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu ");
        sql.append("FROM all_data GROUP BY date ORDER BY date OPTION (MAXRECURSION 500)");

        PreparedStatement pst = con.prepareStatement(sql.toString());
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int chiphi   = rs.getInt("chiphi");
            int doanhthu = rs.getInt("doanhthu");
            result.add(new ThongKeTungNgayTrongThangDTO(
                rs.getDate("ngay"), chiphi, doanhthu, doanhthu - chiphi));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return result;
}

    // ================================================================
    // 7. THỐNG KÊ 7 NGÀY GẦN NHẤT — gộp tất cả CN
    // ================================================================
   public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat(String selectedBranch) {
    ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
    String mcn = currentMcn();

    List<String> targets;
    if (selectedBranch == null || selectedBranch.equalsIgnoreCase("Tất cả chi nhánh")) {
        targets = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
    } else {
        String[] parts = selectedBranch.trim().split("\\s+");
        try {
            targets = new ArrayList<>(Arrays.asList("CN" + Integer.parseInt(parts[parts.length - 1])));
        } catch (NumberFormatException ex) {
            targets = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
        }
    }

    try (Connection con = JDBCUtil.getConnection(mcn)) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH dates(date) AS (");
        sql.append("  SELECT CAST(DATEADD(DAY,-6,GETDATE()) AS DATE) ");
        sql.append("  UNION ALL SELECT DATEADD(DAY,1,date) FROM dates ");
        sql.append("  WHERE date < CAST(GETDATE() AS DATE)");
        sql.append("), all_data AS (");

        for (int i = 0; i < targets.size(); i++) {
            String target = targets.get(i);
            boolean isLocal = target.equalsIgnoreCase(mcn);
            appendNgayQuery(sql, isLocal ? null : target);
            if (i < targets.size() - 1) sql.append(" UNION ALL ");
        }

        sql.append(") SELECT date AS ngay, SUM(chiphi) AS chiphi, SUM(doanhthu) AS doanhthu ");
        sql.append("FROM all_data GROUP BY date ORDER BY date OPTION (MAXRECURSION 100)");

        PreparedStatement pst = con.prepareStatement(sql.toString());
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            int chiphi   = rs.getInt("chiphi");
            int doanhthu = rs.getInt("doanhthu");
            result.add(new ThongKeTungNgayTrongThangDTO(
                rs.getDate("ngay"), chiphi, doanhthu, doanhthu - chiphi));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return result;
}

    // ================================================================
    // 8. NHÂN VIÊN BÁN CHẠY — FIX: dùng alias trong JOIN ON
    // ================================================================
    public static ArrayList<ThongKeNhanVienBanChayDTO> getNhanVienBanChay(String chiNhanh) {
        ArrayList<ThongKeNhanVienBanChayDTO> result = new ArrayList<>();
        try {
            String mcn = currentMcn();
            String selectedMcn = null;
            if (chiNhanh != null && !chiNhanh.equalsIgnoreCase("Tất cả chi nhánh")) {
                String[] parts = chiNhanh.trim().split("\\s+");
                try {
                    selectedMcn = "CN" + Integer.parseInt(parts[parts.length - 1]);
                } catch (NumberFormatException ex) {
                    selectedMcn = chiNhanh.trim().toUpperCase().replaceAll("\\s+", "");
                }
            }

            List<String> targets = selectedMcn == null
                    ? new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"))
                    : new ArrayList<>(Arrays.asList(selectedMcn));

            Connection con = JDBCUtil.getConnection(mcn);
            for (String target : targets) {
                boolean isLocal = target.equalsIgnoreCase(mcn);
                String px = tbl(isLocal ? null : target, "PHIEUXUAT");
                String nv = tbl(isLocal ? null : target, "NHANVIEN");

                // Đếm số lượng đơn bán thay vì tổng tiền
                String sql = "SELECT nv.MNV, nv.HOTEN, '"
                        + target + "' AS chiNhanh, COUNT(px.MPX) AS soLuongDon "
                        + "FROM " + px + " px "
                        + "JOIN " + nv + " nv ON px.MNV = nv.MNV "
                        + "WHERE px.TT = 1 "
                        + "GROUP BY nv.MNV, nv.HOTEN "
                        + "ORDER BY soLuongDon DESC";

                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    result.add(new ThongKeNhanVienBanChayDTO(
                        rs.getInt("MNV"), rs.getString("HOTEN"),
                        rs.getString("chiNhanh"), 0, rs.getInt("soLuongDon")
                    ));
                }
            }
            
            // Sắp xếp lại toàn bộ kết quả theo số lượng đơn giảm dần
            result.sort((a, b) -> Integer.compare(b.getSoLuongDon(), a.getSoLuongDon()));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 9. KHÁCH HÀNG
    // ================================================================
    public static ArrayList<ThongKeKhachHangDTO> getThongKeKhachHang(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeKhachHangDTO> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeEnd.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = """
                WITH kh AS (
                    SELECT kh.MKH, kh.HOTEN,
                        COUNT(px.MPX) AS tongsophieu,
                        SUM(px.TIEN) AS tongsotien
                    FROM [dbo].[KHACHHANG] kh
                    JOIN [dbo].[PHIEUXUAT] px ON kh.MKH = px.MKH
                    WHERE px.TG BETWEEN ? AND ?
                    GROUP BY kh.MKH, kh.HOTEN
                )
                SELECT MKH, HOTEN,
                    COALESCE(tongsophieu,0) AS SL,
                    COALESCE(tongsotien,0) AS total
                FROM kh WHERE HOTEN LIKE ? OR CAST(MKH AS NVARCHAR) LIKE ?
                """;
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
            pst.setString(3, "%" + text + "%");
            pst.setString(4, "%" + text + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new ThongKeKhachHangDTO(
                    rs.getInt("MKH"), rs.getString("HOTEN"),
                    rs.getInt("SL"), rs.getLong("total")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ================================================================
    // 10. NHÀ CUNG CẤP
    // ================================================================
    public static ArrayList<ThongKeNhaCungCapDTO> getThongKeNCC(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeNhaCungCapDTO> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeEnd.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = """
                WITH ncc AS (
                    SELECT ncc.MNCC, ncc.TEN,
                        COUNT(pn.MPN) AS tongsophieu,
                        SUM(pn.TIEN) AS tongsotien
                    FROM [dbo].[NHACUNGCAP] ncc
                    JOIN [dbo].[PHIEUNHAP] pn ON ncc.MNCC = pn.MNCC
                    WHERE pn.TG BETWEEN ? AND ?
                    GROUP BY ncc.MNCC, ncc.TEN
                )
                SELECT MNCC, TEN,
                    COALESCE(tongsophieu,0) AS SL,
                    COALESCE(tongsotien,0) AS total
                FROM ncc WHERE TEN LIKE ? OR CAST(MNCC AS NVARCHAR) LIKE ?
                """;
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
            pst.setString(3, "%" + text + "%");
            pst.setString(4, "%" + text + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new ThongKeNhaCungCapDTO(
                    rs.getInt("MNCC"), rs.getString("TEN"),
                    rs.getInt("SL"), rs.getLong("total")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    // ================================================================
// TOP SẢN PHẨM BÁN CHẠY — dùng Linked Server
// ================================================================
    public ArrayList<ThongKeTopSanPhamDTO> getTopSanPham(
            String selectedBranch, int topN, Date timeStart, Date timeEnd) {

        ArrayList<ThongKeTopSanPhamDTO> result = new ArrayList<>();
        String mcn = currentMcn();

        // Chuẩn hóa timeEnd về 23:59:59
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeEnd.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Timestamp tsStart = new Timestamp(timeStart.getTime());
        Timestamp tsEnd   = new Timestamp(cal.getTimeInMillis());

        List<String> targets;
        if (selectedBranch == null || selectedBranch.equalsIgnoreCase("Tất cả chi nhánh")) {
            targets = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
        } else {
            String[] parts = selectedBranch.trim().split("\\s+");
            try {
                targets = new ArrayList<>(Arrays.asList("CN" + Integer.parseInt(parts[parts.length - 1])));
            } catch (NumberFormatException ex) {
                targets = new ArrayList<>(Arrays.asList("CN1", "CN2", "CN3"));
            }
        }

        try (Connection con = JDBCUtil.getConnection(mcn)) {
            StringBuilder unionSql = new StringBuilder();
            int paramCount = 0; // đếm số ? cần bind

            for (int i = 0; i < targets.size(); i++) {
                String target = targets.get(i);
                boolean isLocal = target.equalsIgnoreCase(mcn);
                String px  = tbl(isLocal ? null : target, "PHIEUXUAT");
                String cpx = tbl(isLocal ? null : target, "CTPHIEUXUAT");
                String sp  = tbl(isLocal ? null : target, "SANPHAM");

                if (i > 0) unionSql.append(" UNION ALL ");
                unionSql.append("SELECT sp.MSP, sp.TEN, ")
                        .append("SUM(cpx.SL) AS soLuong, ")
                        .append("SUM(cpx.TIENXUAT) AS doanhThu ")
                        .append("FROM ").append(px).append(" px ")
                        .append("JOIN ").append(cpx).append(" cpx ON px.MPX = cpx.MPX ")
                        .append("JOIN ").append(sp).append(" sp ON cpx.MSP = sp.MSP ")
                        .append("WHERE px.TT = 1 ")
                        .append("AND px.TG BETWEEN ? AND ? ") // ✅ lọc theo ngày
                        .append("GROUP BY sp.MSP, sp.TEN");
                paramCount++;
            }

            String limitClause = (topN == Integer.MAX_VALUE) ? "" : "TOP " + topN + " ";
            String sql = "WITH raw AS (" + unionSql + "), "
                    + "grouped AS ("
                    + "  SELECT MSP, TEN, "
                    + "    SUM(soLuong) AS soLuong, "  // gộp cùng SP từ nhiều CN
                    + "    SUM(doanhThu) AS doanhThu "
                    + "  FROM raw GROUP BY MSP, TEN"
                    + "), "
                    + "total AS (SELECT SUM(doanhThu) AS tongDoanhThu FROM grouped) "
                    + "SELECT " + limitClause + "g.MSP, g.TEN, g.soLuong, g.doanhThu, "
                    + "CASE WHEN t.tongDoanhThu > 0 "
                    + "  THEN CAST(g.doanhThu * 100.0 / t.tongDoanhThu AS DECIMAL(5,2)) "
                    + "  ELSE 0 END AS tiLe "
                    + "FROM grouped g CROSS JOIN total t "
                    + "ORDER BY g.soLuong DESC"; // 

            PreparedStatement pst = con.prepareStatement(sql);
            int idx = 1;
            for (int i = 0; i < paramCount; i++) {
                pst.setTimestamp(idx++, tsStart);
                pst.setTimestamp(idx++, tsEnd);
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new ThongKeTopSanPhamDTO(
                    rs.getInt("MSP"),
                    rs.getString("TEN"),
                    rs.getInt("soLuong"),
                    rs.getLong("doanhThu"),
                    rs.getDouble("tiLe")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}