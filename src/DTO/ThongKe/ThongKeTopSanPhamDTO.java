package DTO.ThongKe;

public class ThongKeTopSanPhamDTO {
    private int msp;
    private String ten;
    private int soLuongBan;
    private long doanhThu;
    private double tiLe; // % so với tổng

    public ThongKeTopSanPhamDTO(int msp, String ten, int soLuongBan, long doanhThu, double tiLe) {
        this.msp = msp;
        this.ten = ten;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
        this.tiLe = tiLe;
    }

    public int getMsp() { return msp; }
    public String getTen() { return ten; }
    public int getSoLuongBan() { return soLuongBan; }
    public long getDoanhThu() { return doanhThu; }
    public double getTiLe() { return tiLe; }
}