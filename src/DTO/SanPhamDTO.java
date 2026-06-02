package DTO;

public class SanPhamDTO {

    private Integer MSP;
    private String TEN;
    private String HINHANH;
    private int MNCC;
    private String THUONGHIEU;
    private Integer NAMSANXUAT;
    private double GIANHAP;
    private double GIABAN;
    private int THOIGIANBAOHANH;
    private int SOLUONG; // Số lượng tồn kho

    public SanPhamDTO() {

    }

    // Constructor đầy đủ (không có SOLUONG)
    public SanPhamDTO(Integer mSP, String tEN, String hINHANH, int mNCC, String tHUONGHIEU, 
                      Integer nAMSANXUAT, double gIANHAP, double gIABAN, int tHOIGIANBAOHANH) {
        MSP = mSP;
        TEN = tEN;
        HINHANH = hINHANH;
        MNCC = mNCC;
        THUONGHIEU = tHUONGHIEU;
        NAMSANXUAT = nAMSANXUAT;
        GIANHAP = gIANHAP;
        GIABAN = gIABAN;
        THOIGIANBAOHANH = tHOIGIANBAOHANH;
        SOLUONG = 0;
    }

    // Constructor đầy đủ (có SOLUONG)
    public SanPhamDTO(Integer mSP, String tEN, String hINHANH, int mNCC, String tHUONGHIEU, 
                      Integer nAMSANXUAT, double gIANHAP, double gIABAN, int tHOIGIANBAOHANH, int sOLUONG) {
        MSP = mSP;
        TEN = tEN;
        HINHANH = hINHANH;
        MNCC = mNCC;
        THUONGHIEU = tHUONGHIEU;
        NAMSANXUAT = nAMSANXUAT;
        GIANHAP = gIANHAP;
        GIABAN = gIABAN;
        THOIGIANBAOHANH = tHOIGIANBAOHANH;
        SOLUONG = sOLUONG;
    }

    // Constructor tương thích ngược với code cũ (để tránh lỗi biên dịch)
    public SanPhamDTO(Integer mSP, String tEN, String hINHANH, int mL, int tIENX, int sL, String mV) {
        MSP = mSP;
        TEN = tEN;
        HINHANH = hINHANH;
        MNCC = mL;
        GIABAN = tIENX;
        THUONGHIEU = mV;
        GIANHAP = tIENX * 0.7;
        THOIGIANBAOHANH = 12;
    }

    public Integer getMSP() {
        return MSP;
    }

    public void setMSP(Integer mSP) {
        MSP = mSP;
    }

    public String getTEN() {
        return TEN;
    }

    public void setTEN(String tEN) {
        TEN = tEN;
    }

    public String getHINHANH() {
        return HINHANH;
    }

    public void setHINHANH(String hINHANH) {
        HINHANH = hINHANH;
    }

    public int getMNCC() {
        return MNCC;
    }

    public void setMNCC(int mNCC) {
        MNCC = mNCC;
    }

    public String getTHUONGHIEU() {
        return THUONGHIEU;
    }

    public void setTHUONGHIEU(String tHUONGHIEU) {
        THUONGHIEU = tHUONGHIEU;
    }

    public Integer getNAMSANXUAT() {
        return NAMSANXUAT;
    }

    public void setNAMSANXUAT(Integer nAMSANXUAT) {
        NAMSANXUAT = nAMSANXUAT;
    }

    public double getGIANHAP() {
        return GIANHAP;
    }

    public void setGIANHAP(double gIANHAP) {
        GIANHAP = gIANHAP;
    }

    public double getGIABAN() {
        return GIABAN;
    }

    public void setGIABAN(double gIABAN) {
        GIABAN = gIABAN;
    }

    public int getTHOIGIANBAOHANH() {
        return THOIGIANBAOHANH;
    }

    public void setTHOIGIANBAOHANH(int tHOIGIANBAOHANH) {
        THOIGIANBAOHANH = tHOIGIANBAOHANH;
    }

    // Getter/Setter cho SOLUONG (số lượng tồn kho)
    public int getSOLUONG() {
        return SOLUONG;
    }

    public void setSOLUONG(int sOLUONG) {
        SOLUONG = sOLUONG;
    }

    // Getter tương thích ngược cho getSL() - số lượng được quản lý ở TONKHO
    public int getSL() {
        return SOLUONG; // Trả về số lượng từ TONKHO
    }

    public void setSL(int sL) {
        SOLUONG = sL; // Gán số lượng vào TONKHO
    }

    // Getter tương thích ngược
    public int getML() {
        return MNCC;
    }

    public void setML(int mL) {
        MNCC = mL;
    }

    public int getMDV() {
        // MVT đã bị xóa, trả về 0 để tương thích ngược
        return 0;
    }

    public void setMDV(int mDV) {
        // MVT đã bị xóa, method này không có tác dụng
    }

    public int getTIENX() {
        return (int) GIABAN;
    }

    public void setTIENX(int tIENX) {
        GIABAN = tIENX;
    }

    public String getMV() {
        return THUONGHIEU != null ? THUONGHIEU : "";
    }

    public void setMV(String mV) {
        THUONGHIEU = mV;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + MSP;
        result = prime * result + ((TEN == null) ? 0 : TEN.hashCode());
        result = prime * result + ((HINHANH == null) ? 0 : HINHANH.hashCode());
        result = prime * result + MNCC;
        result = prime * result + (int) GIABAN;
        result = prime * result + ((THUONGHIEU == null) ? 0 : THUONGHIEU.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SanPhamDTO other = (SanPhamDTO) obj;
        if (MSP != other.MSP)
            return false;
        if (TEN == null) {
            if (other.TEN != null)
                return false;
        } else if (!TEN.equals(other.TEN))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SanPhamDTO [MSP=" + MSP + ", TEN=" + TEN + ", HINHANH=" + HINHANH + 
               ", MNCC=" + MNCC + ", THUONGHIEU=" + THUONGHIEU + 
               ", NAMSANXUAT=" + NAMSANXUAT + ", GIANHAP=" + GIANHAP + ", GIABAN=" + GIABAN +
               ", THOIGIANBAOHANH=" + THOIGIANBAOHANH + ", SOLUONG=" + SOLUONG + "]";
    }
    

}