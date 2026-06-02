package DTO.ThongKe;

public class ThongKeDoanhThuChiNhanhDTO {

    private String chiNhanh;
    private long tongDoanhThu;
    private int soHoaDon;
    private long doanhThuTrungBinh;

    public ThongKeDoanhThuChiNhanhDTO(String chiNhanh, long tongDoanhThu, int soHoaDon, long doanhThuTrungBinh) {
        this.chiNhanh = chiNhanh;
        this.tongDoanhThu = tongDoanhThu;
        this.soHoaDon = soHoaDon;
        this.doanhThuTrungBinh = doanhThuTrungBinh;
    }

    public String getChiNhanh() { return chiNhanh; }
    public long getTongDoanhThu() { return tongDoanhThu; }
    public int getSoHoaDon() { return soHoaDon; }
    public long getDoanhThuTrungBinh() { return doanhThuTrungBinh; }

    public void setChiNhanh(String chiNhanh) { this.chiNhanh = chiNhanh; }
    public void setTongDoanhThu(long tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
    public void setSoHoaDon(int soHoaDon) { this.soHoaDon = soHoaDon; }
    public void setDoanhThuTrungBinh(long doanhThuTrungBinh) { this.doanhThuTrungBinh = doanhThuTrungBinh; }
}