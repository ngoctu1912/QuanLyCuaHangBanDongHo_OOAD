package BUS;

import DAO.ThongKeDAO;
import DTO.ThongKe.ThongKeDoanhThuChiNhanhDTO;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import DTO.ThongKe.ThongKeKhachHangDTO;
import DTO.ThongKe.ThongKeNhaCungCapDTO;
import DTO.ThongKe.ThongKeNhanVienBanChayDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import DTO.ThongKe.ThongKeTopSanPhamDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;

import java.util.ArrayList;
import java.util.Date;

public class ThongKeBUS {

    ThongKeDAO thongkeDAO = new ThongKeDAO();
    ArrayList<ThongKeKhachHangDTO> tkkh;
    ArrayList<ThongKeNhaCungCapDTO> tkncc;
    ArrayList<ThongKeTonKhoDTO> listTonKho;

    public ThongKeBUS() {
        listTonKho = ThongKeDAO.getThongKeTonKho("", new Date(0), new Date(System.currentTimeMillis()));
    }

    // ================================================================
    // TỒN KHO
    // ================================================================
    public ArrayList<ThongKeTonKhoDTO> getTonKho() {
        return this.listTonKho;
    }

    public ArrayList<ThongKeTonKhoDTO> filterTonKho(String text, Date time_start, Date time_end) {
        return ThongKeDAO.getThongKeTonKho(text, time_start, time_end);
    }

    public int[] getSoluong(ArrayList<ThongKeTonKhoDTO> list) {
        int[] result = {0, 0, 0, 0};
        for (ThongKeTonKhoDTO item : list) {
            result[0] += item.getTondauky();
            result[1] += item.getNhaptrongky();
            result[2] += item.getXuattrongky();
            result[3] += item.getToncuoiky();
        }
        return result;
    }

    // ================================================================
    // KHÁCH HÀNG
    // ================================================================
    public ArrayList<ThongKeKhachHangDTO> getAllKhachHang() {
        this.tkkh = ThongKeDAO.getThongKeKhachHang("", new Date(0), new Date(System.currentTimeMillis()));
        return this.tkkh;
    }

    public ArrayList<ThongKeKhachHangDTO> FilterKhachHang(String text, Date start, Date end) {
        this.tkkh = ThongKeDAO.getThongKeKhachHang(text, start, end);
        return this.tkkh;
    }

    // ================================================================
    // NHÀ CUNG CẤP
    // ================================================================
    public ArrayList<ThongKeNhaCungCapDTO> getAllNCC() {
        this.tkncc = ThongKeDAO.getThongKeNCC("", new Date(0), new Date(System.currentTimeMillis()));
        return this.tkncc;
    }

    public ArrayList<ThongKeNhaCungCapDTO> FilterNCC(String text, Date start, Date end) {
        this.tkncc = ThongKeDAO.getThongKeNCC(text, start, end);
        return this.tkncc;
    }

    // ================================================================
    // DOANH THU THEO TỪNG NĂM — gộp tất cả CN qua Linked Server
    // ================================================================
    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int year_start, int year_end) {
        return thongkeDAO.getDoanhThuTheoTungNam(year_start, year_end);
    }

    // ================================================================
    // DOANH THU TỪNG THÁNG TRONG NĂM — gộp tất cả CN qua Linked Server
    // ================================================================
    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        return thongkeDAO.getThongKeTheoThang(nam);
    }

    // ================================================================
    // DOANH THU TỪNG NGÀY TRONG THÁNG — gộp tất cả CN qua Linked Server
    // ================================================================
    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTungNgayTrongThang(int thang, int nam) {
        return thongkeDAO.getThongKeTungNgayTrongThang(thang, nam);
    }

    // ================================================================
    // DOANH THU TỪ NGÀY ĐẾN NGÀY — gộp tất cả CN (sửa sang SQL Server)
    // ================================================================
    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTuNgayDenNgay(String start, String end, String selectedBranch) {
        return thongkeDAO.getThongKeTuNgayDenNgay(start, end,selectedBranch);
    }

    // ================================================================
    // 7 NGÀY GẦN NHẤT — gộp tất cả CN qua Linked Server
    // ================================================================
    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat(String selectedBranch) {
        return thongkeDAO.getThongKe7NgayGanNhat(selectedBranch);
    }

    // ================================================================
    // NHÂN VIÊN BÁN CHẠY — dùng Linked Server (giữ nguyên logic cũ)
    // ================================================================
    public ArrayList<ThongKeNhanVienBanChayDTO> getNhanVienBanChay(String chiNhanh) {
        return ThongKeDAO.getNhanVienBanChay(chiNhanh);
    }

    // ================================================================
    // DOANH THU THEO CHI NHÁNH — MỚI, dùng Linked Server
    // selectedBranch: "Tất cả chi nhánh" | "Chi nhánh 1" | "Chi nhánh 2" | "Chi nhánh 3"
    // ================================================================
    public ArrayList<ThongKeDoanhThuChiNhanhDTO> getDoanhThuTheoChiNhanh(String selectedBranch) {
        return thongkeDAO.getDoanhThuTheoChiNhanh(selectedBranch);
    }
    
    public ArrayList<ThongKeTopSanPhamDTO> getTopSanPham(
        String selectedBranch, int topN, Date timeStart, Date timeEnd) {
    return thongkeDAO.getTopSanPham(selectedBranch, topN, timeStart, timeEnd);
}
}