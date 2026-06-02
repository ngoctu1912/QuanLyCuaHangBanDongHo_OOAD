package DTO.ThongKe;

public class ThongKeNhanVienBanChayDTO {
    private int maNhanVien;
    private String tenNhanVien;
    private String chiNhanh;
    private long tongTienBan;
    private int soLuongDon;

    public ThongKeNhanVienBanChayDTO(int maNhanVien, String tenNhanVien, String chiNhanh, int soDonBan) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.chiNhanh = chiNhanh;
        this.tongTienBan = tongTienBan;
        this.soLuongDon = 0;
    }

    public ThongKeNhanVienBanChayDTO(int maNhanVien, String tenNhanVien, String chiNhanh, long tongTienBan, int soLuongDon) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.chiNhanh = chiNhanh;
        this.tongTienBan = tongTienBan;
        this.soLuongDon = soLuongDon;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getChiNhanh() {
        return chiNhanh;
    }

    public void setChiNhanh(String chiNhanh) {
        this.chiNhanh = chiNhanh;
    }

    public int getSoDonBan() {
        return soDonBan;
    }

    public void setSoDonBan(int soDonBan) {
        this.soDonBan = soDonBan;
    }

    public int getSoLuongDon() {
        return soLuongDon;
    }

    public void setSoLuongDon(int soLuongDon) {
        this.soLuongDon = soLuongDon;
    }
}
