
package DAO;

import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeKhachHangDTO;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThongKeDAO {

    public static ThongKeDAO getInstance() {
        return new ThongKeDAO();
    }

    public static ArrayList<ThongKeTonKhoDTO> getThongKeTonKho(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeTonKhoDTO> result = new ArrayList<ThongKeTonKhoDTO>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeEnd.getTime());
        // Đặt giá trị cho giờ, phút, giây và mili giây của Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
                            WITH nhap AS (
                            SELECT MSP, SUM(SL) AS sl_nhap
                            FROM CTPHIEUNHAP
                            JOIN PHIEUNHAP ON PHIEUNHAP.MPN = CTPHIEUNHAP.MPN
                            WHERE TG BETWEEN ? AND ?
                            GROUP BY MSP
                            ),
                            xuat AS (
                            SELECT MSP, SUM(SL) AS sl_xuat
                            FROM CTPHIEUXUAT
                            JOIN PHIEUXUAT ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX
                            WHERE TG BETWEEN ? AND ?
                            GROUP BY MSP
                            ),
                            nhap_dau AS (
                            SELECT CTPHIEUNHAP.MSP, SUM(CTPHIEUNHAP.SL) AS sl_nhap_dau
                            FROM PHIEUNHAP
                            JOIN CTPHIEUNHAP ON PHIEUNHAP.MPN = CTPHIEUNHAP.MPN
                            WHERE PHIEUNHAP.TG < ?
                            GROUP BY CTPHIEUNHAP.MSP
                            ),
                            xuat_dau AS (
                            SELECT CTPHIEUXUAT.MSP, SUM(CTPHIEUXUAT.SL) AS sl_xuat_dau
                            FROM PHIEUXUAT
                            JOIN CTPHIEUXUAT ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX
                            WHERE PHIEUXUAT.TG < ?
                            GROUP BY CTPHIEUXUAT.MSP
                            ),
                            dau_ky AS (
                            SELECT
                                SANPHAM.MSP,
                                COALESCE(nhap_dau.sl_nhap_dau, 0) - COALESCE(xuat_dau.sl_xuat_dau, 0) AS SLdauky
                            FROM SANPHAM
                            LEFT JOIN nhap_dau ON SANPHAM.MSP = nhap_dau.MSP
                            LEFT JOIN xuat_dau ON SANPHAM.MSP = xuat_dau.MSP
                            ),
                            temp_table AS (
                            SELECT SANPHAM.MSP, SANPHAM.TEN, dau_ky.SLdauky, COALESCE(nhap.sl_nhap, 0) AS SLnhap, COALESCE(xuat.sl_xuat, 0)  AS SLxuat, (dau_ky.SLdauky + COALESCE(nhap.sl_nhap, 0) - COALESCE(xuat.sl_xuat, 0)) AS SLcuoiky
                            FROM dau_ky
                            LEFT JOIN nhap ON dau_ky.MSP = nhap.MSP
                            LEFT JOIN xuat ON dau_ky.MSP = xuat.MSP
                            JOIN SANPHAM ON dau_ky.MSP = SANPHAM.MSP
                            )
                            SELECT * FROM temp_table
                            WHERE TEN LIKE ? OR MSP LIKE ?
                            ORDER BY MSP;""";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
            pst.setTimestamp(3, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(4, new Timestamp(calendar.getTimeInMillis()));
            pst.setTimestamp(5, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(6, new Timestamp(timeStart.getTime()));
            pst.setString(7, "%" + text + "%");
            pst.setString(8, "%" + text + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MSP = rs.getInt("MSP");
                String TEN = rs.getString("TEN");
                int SLdauky = rs.getInt("SLdauky");
                int SLnhap = rs.getInt("SLnhap");
                int SLxuat = rs.getInt("SLxuat");
                int SLcuoiky = rs.getInt("SLcuoiky");
                ThongKeTonKhoDTO p = new ThongKeTonKhoDTO(MSP, TEN, SLdauky, SLnhap, SLxuat, SLcuoiky);
                result.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int year_start, int year_end) {
        ArrayList<ThongKeDoanhThuDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sqlSetStartYear = "SET @start_year = ?;";
            String sqlSetEndYear = "SET @end_year = ?;";
            String sql = """
                        WITH RECURSIVE years(year) AS (
                        SELECT @start_year
                        UNION ALL
                        SELECT year + 1
                        FROM years
                        WHERE year < @end_year
                        )
                        SELECT 
                        years.year AS nam,
                        COALESCE(SUM(CTPHIEUNHAP.TIENNHAP), 0) AS chiphi, 
                        COALESCE(SUM(CTPHIEUXUAT.TIENXUAT), 0) AS doanhthu
                        FROM years
                        LEFT JOIN PHIEUXUAT ON YEAR(PHIEUXUAT.TG) = years.year
                        LEFT JOIN CTPHIEUXUAT ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX
                        LEFT JOIN SANPHAM ON SANPHAM.MSP = CTPHIEUXUAT.MSP
                        LEFT JOIN CTPHIEUNHAP ON SANPHAM.MSP = CTPHIEUNHAP.MSP
                        GROUP BY years.year
                        ORDER BY years.year;""";
            PreparedStatement pstStartYear = con.prepareStatement(sqlSetStartYear);
            PreparedStatement pstEndYear = con.prepareStatement(sqlSetEndYear);
            PreparedStatement pstSelect = con.prepareStatement(sql);

            pstStartYear.setInt(1, year_start);
            pstEndYear.setInt(1, year_end);

            pstStartYear.execute();
            pstEndYear.execute();

            ResultSet rs = pstSelect.executeQuery();
            while (rs.next()) {
                int TG = rs.getInt("nam");
                Long chiphi = rs.getLong("chiphi");
                Long doanhthu = rs.getLong("doanhthu");
                ThongKeDoanhThuDTO x = new ThongKeDoanhThuDTO(TG, chiphi, doanhthu, doanhthu - chiphi);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ThongKeKhachHangDTO> getThongKeKhachHang(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeKhachHangDTO> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeEnd.getTime());
        // Đặt giá trị cho giờ, phút, giây và mili giây của Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
                            WITH kh AS (
                            SELECT KHACHHANG.MKH, KHACHHANG.HOTEN , COUNT(PHIEUXUAT.MPX) AS tongsophieu, SUM(PHIEUXUAT.TIEN) AS tongsotien
                            FROM KHACHHANG
                            JOIN PHIEUXUAT ON KHACHHANG.MKH = PHIEUXUAT.MKH
                            WHERE PHIEUXUAT.TG BETWEEN ? AND ? 
                            GROUP BY KHACHHANG.MKH, KHACHHANG.HOTEN)
                            SELECT MKH,HOTEN,COALESCE(kh.tongsophieu, 0) AS SL ,COALESCE(kh.tongsotien, 0) AS total 
                            FROM kh WHERE HOTEN LIKE ? OR MKH LIKE ?""";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
            pst.setString(3, "%" + text + "%");
            pst.setString(4, "%" + text + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int MKH = rs.getInt("MKH");
                String tenkh = rs.getString("HOTEN");
                int SL = rs.getInt("SL");
                long TIEN = rs.getInt("total");
                ThongKeKhachHangDTO x = new ThongKeKhachHangDTO(MKH, tenkh, SL, TIEN);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ThongKeNhaCungCapDTO> getThongKeNCC(String text, Date timeStart, Date timeEnd) {
        ArrayList<ThongKeNhaCungCapDTO> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeEnd.getTime());
        // Đặt giá trị cho giờ, phút, giây và mili giây của Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
                            WITH ncc AS (
                            SELECT NHACUNGCAP.MNCC, NHACUNGCAP.TEN , COUNT(PHIEUNHAP.MPN) AS tongsophieu, SUM(PHIEUNHAP.TIEN) AS tongsotien
                            FROM NHACUNGCAP
                            JOIN PHIEUNHAP ON NHACUNGCAP.MNCC = PHIEUNHAP.MNCC
                            WHERE PHIEUNHAP.TG BETWEEN ? AND ? 
                            GROUP BY NHACUNGCAP.MNCC, NHACUNGCAP.TEN)
                            SELECT MNCC,TEN,COALESCE(ncc.tongsophieu, 0) AS SL ,COALESCE(ncc.tongsotien, 0) AS total 
                            FROM ncc WHERE TEN LIKE ? OR MNCC LIKE ?""";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
            pst.setString(3, "%" + text + "%");
            pst.setString(4, "%" + text + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mancc = rs.getInt("MNCC");
                String tenncc = rs.getString("TEN");
                int SL = rs.getInt("SL");
                long TIEN = rs.getInt("total");
                ThongKeNhaCungCapDTO x = new ThongKeNhaCungCapDTO(mancc, tenncc, SL, TIEN);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT months.month AS thang, \n"
                    + "       COALESCE(SUM(CTPHIEUNHAP.TIENNHAP), 0) AS chiphi,\n"
                    + "       COALESCE(SUM(CTPHIEUXUAT.TIENXUAT), 0) AS doanhthu\n"
                    + "FROM (\n"
                    + "       SELECT 1 AS month\n"
                    + "       UNION ALL SELECT 2\n"
                    + "       UNION ALL SELECT 3\n"
                    + "       UNION ALL SELECT 4\n"
                    + "       UNION ALL SELECT 5\n"
                    + "       UNION ALL SELECT 6\n"
                    + "       UNION ALL SELECT 7\n"
                    + "       UNION ALL SELECT 8\n"
                    + "       UNION ALL SELECT 9\n"
                    + "       UNION ALL SELECT 10\n"
                    + "       UNION ALL SELECT 11\n"
                    + "       UNION ALL SELECT 12\n"
                    + "     ) AS months\n"
                    + "LEFT JOIN PHIEUXUAT ON MONTH(PHIEUXUAT.TG) = months.month AND YEAR(PHIEUXUAT.TG) = ? \n"
                    + "LEFT JOIN CTPHIEUXUAT ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX\n"
                    + "LEFT JOIN SANPHAM ON SANPHAM.MSP = CTPHIEUXUAT.MSP\n"
                    + "LEFT JOIN CTPHIEUNHAP ON SANPHAM.MSP = CTPHIEUNHAP.MSP\n"
                    + "GROUP BY months.month\n"
                    + "ORDER BY months.month;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, nam);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("thang");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTheoThangDTO thongke = new ThongKeTheoThangDTO(thang, chiphi, doanhthu, loinhuan);
                result.add(thongke);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTungNgayTrongThang(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            String ngayString = nam + "-" + thang + "-" + "01";
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT \n"
                    + "  dates.date AS ngay, \n"
                    + "  COALESCE(SUM(CTPHIEUNHAP.TIENNHAP), 0) AS chiphi, \n"
                    + "  COALESCE(SUM(CTPHIEUXUAT.TIENXUAT), 0) AS doanhthu\n"
                    + "FROM (\n"
                    + "  SELECT DATE('" + ngayString + "') + INTERVAL c.number DAY AS date\n"
                    + "  FROM (\n"
                    + "    SELECT 0 AS number\n"
                    + "    UNION ALL SELECT 1\n"
                    + "    UNION ALL SELECT 2\n"
                    + "    UNION ALL SELECT 3\n"
                    + "    UNION ALL SELECT 4\n"
                    + "    UNION ALL SELECT 5\n"
                    + "    UNION ALL SELECT 6\n"
                    + "    UNION ALL SELECT 7\n"
                    + "    UNION ALL SELECT 8\n"
                    + "    UNION ALL SELECT 9\n"
                    + "    UNION ALL SELECT 10\n"
                    + "    UNION ALL SELECT 11\n"
                    + "    UNION ALL SELECT 12\n"
                    + "    UNION ALL SELECT 13\n"
                    + "    UNION ALL SELECT 14\n"
                    + "    UNION ALL SELECT 15\n"
                    + "    UNION ALL SELECT 16\n"
                    + "    UNION ALL SELECT 17\n"
                    + "    UNION ALL SELECT 18\n"
                    + "    UNION ALL SELECT 19\n"
                    + "    UNION ALL SELECT 20\n"
                    + "    UNION ALL SELECT 21\n"
                    + "    UNION ALL SELECT 22\n"
                    + "    UNION ALL SELECT 23\n"
                    + "    UNION ALL SELECT 24\n"
                    + "    UNION ALL SELECT 25\n"
                    + "    UNION ALL SELECT 26\n"
                    + "    UNION ALL SELECT 27\n"
                    + "    UNION ALL SELECT 28\n"
                    + "    UNION ALL SELECT 29\n"
                    + "    UNION ALL SELECT 30\n"
                    + "  ) AS c\n"
                    + "  WHERE DATE('" + ngayString + "') + INTERVAL c.number DAY <= LAST_DAY('" + ngayString + "')\n"
                    + ") AS dates\n"
                    + "LEFT JOIN PHIEUXUAT ON DATE(PHIEUXUAT.TG) = dates.date\n"
                    + "LEFT JOIN CTPHIEUXUAT ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX\n"
                    + "LEFT JOIN SANPHAM ON SANPHAM.MSP = CTPHIEUXUAT.MSP\n"
                    + "LEFT JOIN CTPHIEUNHAP ON SANPHAM.MSP = CTPHIEUNHAP.MSP\n"
                    + "GROUP BY dates.date\n"
                    + "ORDER BY dates.date;";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
;WITH dates AS (
    SELECT CAST(DATEADD(DAY, -6, GETDATE()) AS DATE) AS date
    UNION ALL
    SELECT DATEADD(DAY, 1, date)
    FROM dates
    WHERE date < CAST(GETDATE() AS DATE)
)
SELECT 
    dates.date AS ngay,
    COALESCE(SUM(CTPHIEUXUAT.TIENXUAT), 0) AS doanhthu,
    COALESCE(SUM(CTPHIEUNHAP.TIENNHAP), 0) AS chiphi
FROM dates
LEFT JOIN PHIEUXUAT 
    ON CAST(PHIEUXUAT.TG AS DATE) = dates.date
LEFT JOIN CTPHIEUXUAT 
    ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX
LEFT JOIN SANPHAM 
    ON SANPHAM.MSP = CTPHIEUXUAT.MSP
LEFT JOIN CTPHIEUNHAP 
    ON SANPHAM.MSP = CTPHIEUNHAP.MSP
GROUP BY dates.date
ORDER BY dates.date
OPTION (MAXRECURSION 100);
""";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTuNgayDenNgay(String star, String end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String setStar = "SET @start_date = '" + star + "'";
            String setEnd = "SET @end_date = '" + end + "'  ;";
            String sqlSelect = "SELECT \n"
                    + "  dates.date AS ngay, \n"
                    + "  COALESCE(SUM(CTPHIEUNHAP.TIENNHAP), 0) AS chiphi, \n"
                    + "  COALESCE(SUM(CTPHIEUXUAT.TIENXUAT), 0) AS doanhthu\n"
                    + "FROM (\n"
                    + "  SELECT DATE_ADD(@start_date, INTERVAL c.number DAY) AS date\n"
                    + "  FROM (\n"
                    + "    SELECT a.number + b.number * 31 AS number\n"
                    + "    FROM (\n"
                    + "      SELECT 0 AS number\n"
                    + "      UNION ALL SELECT 1\n"
                    + "      UNION ALL SELECT 2\n"
                    + "      UNION ALL SELECT 3\n"
                    + "      UNION ALL SELECT 4\n"
                    + "      UNION ALL SELECT 5\n"
                    + "      UNION ALL SELECT 6\n"
                    + "      UNION ALL SELECT 7\n"
                    + "      UNION ALL SELECT 8\n"
                    + "      UNION ALL SELECT 9\n"
                    + "      UNION ALL SELECT 10\n"
                    + "      UNION ALL SELECT 11\n"
                    + "      UNION ALL SELECT 12\n"
                    + "      UNION ALL SELECT 13\n"
                    + "      UNION ALL SELECT 14\n"
                    + "      UNION ALL SELECT 15\n"
                    + "      UNION ALL SELECT 16\n"
                    + "      UNION ALL SELECT 17\n"
                    + "      UNION ALL SELECT 18\n"
                    + "      UNION ALL SELECT 19\n"
                    + "      UNION ALL SELECT 20\n"
                    + "      UNION ALL SELECT 21\n"
                    + "      UNION ALL SELECT 22\n"
                    + "      UNION ALL SELECT 23\n"
                    + "      UNION ALL SELECT 24\n"
                    + "      UNION ALL SELECT 25\n"
                    + "      UNION ALL SELECT 26\n"
                    + "      UNION ALL SELECT 27\n"
                    + "      UNION ALL SELECT 28\n"
                    + "      UNION ALL SELECT 29\n"
                    + "      UNION ALL SELECT 30\n"
                    + "    ) AS a\n"
                    + "    CROSS JOIN (\n"
                    + "      SELECT 0 AS number\n"
                    + "      UNION ALL SELECT 1\n"
                    + "      UNION ALL SELECT 2\n"
                    + "      UNION ALL SELECT 3\n"
                    + "      UNION ALL SELECT 4\n"
                    + "      UNION ALL SELECT 5\n"
                    + "      UNION ALL SELECT 6\n"
                    + "      UNION ALL SELECT 7\n"
                    + "      UNION ALL SELECT 8\n"
                    + "      UNION ALL SELECT 9\n"
                    + "      UNION ALL SELECT 10\n"
                    + "    ) AS b\n"
                    + "  ) AS c\n"
                    + "  WHERE DATE_ADD(@start_date, INTERVAL c.number DAY) <= @end_date\n"
                    + ") AS dates\n"
                    + "LEFT JOIN PHIEUXUAT ON DATE(PHIEUXUAT.TG) = dates.date\n"
                    + "LEFT JOIN CTPHIEUXUAT ON PHIEUXUAT.MPX = CTPHIEUXUAT.MPX\n"
                    + "LEFT JOIN SANPHAM ON SANPHAM.MSP = CTPHIEUXUAT.MSP\n"
                    + "LEFT JOIN CTPHIEUNHAP ON SANPHAM.MSP = CTPHIEUNHAP.MSP\n"
                    + "GROUP BY dates.date\n"
                    + "ORDER BY dates.date;";

            PreparedStatement pstStart = con.prepareStatement(setStar);
            PreparedStatement pstEnd = con.prepareStatement(setEnd);
            PreparedStatement pstSelect = con.prepareStatement(sqlSelect);

            pstStart.execute();
            pstEnd.execute();
            ResultSet rs = pstSelect.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
