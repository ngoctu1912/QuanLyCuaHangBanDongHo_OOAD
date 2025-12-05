package DTO;

public class SanPhamDTO {

    private Integer MSP;
    private String TEN;
    private String HINHANH;
    private int MNCC;
    private Integer MVT;
    private String THUONGHIEU;
    private Integer NAMSANXUAT;
    private double GIANHAP;
    private double GIABAN;
    private int SOLUONG;
    private int THOIGIANBAOHANH;

    public SanPhamDTO() {

    }

    // Constructor đầy đủ
    public SanPhamDTO(Integer mSP, String tEN, String hINHANH, int mNCC, Integer mVT, String tHUONGHIEU, 
                      Integer nAMSANXUAT, double gIANHAP, double gIABAN, int sOLUONG, 
                      int tHOIGIANBAOHANH) {
        MSP = mSP;
        TEN = tEN;
        HINHANH = hINHANH;
        MNCC = mNCC;
        MVT = mVT;
        THUONGHIEU = tHUONGHIEU;
        NAMSANXUAT = nAMSANXUAT;
        GIANHAP = gIANHAP;
        GIABAN = gIABAN;
        SOLUONG = sOLUONG;
        THOIGIANBAOHANH = tHOIGIANBAOHANH;
    }

    // Constructor tương thích ngược với code cũ (để tránh lỗi biên dịch)
    public SanPhamDTO(Integer mSP, String tEN, String hINHANH, int mL, int tIENX, int sL, int mDV, String mV) {
        MSP = mSP;
        TEN = tEN;
        HINHANH = hINHANH;
        MNCC = mL;
        GIABAN = tIENX;
        SOLUONG = sL;
        MVT = mDV;
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

    public Integer getMVT() {
        return MVT;
    }

    public void setMVT(Integer mVT) {
        MVT = mVT;
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

    public int getSOLUONG() {
        return SOLUONG;
    }

    public void setSOLUONG(int sOLUONG) {
        SOLUONG = sOLUONG;
    }

    public int getTHOIGIANBAOHANH() {
        return THOIGIANBAOHANH;
    }

    public void setTHOIGIANBAOHANH(int tHOIGIANBAOHANH) {
        THOIGIANBAOHANH = tHOIGIANBAOHANH;
    }

    // Getter tương thích ngược
    public int getML() {
        return MNCC;
    }

    public void setML(int mL) {
        MNCC = mL;
    }

    public int getTIENX() {
        return (int) GIABAN;
    }

    public void setTIENX(int tIENX) {
        GIABAN = tIENX;
    }

    public int getSL() {
        return SOLUONG;
    }

    public void setSL(int sL) {
        SOLUONG = sL;
    }

    public int getMDV() {
        return MVT != null ? MVT : 0;
    }

    public void setMDV(int mDV) {
        MVT = mDV;
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
        result = prime * result + SOLUONG;
        result = prime * result + ((MVT == null) ? 0 : MVT.hashCode());
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
               ", MNCC=" + MNCC + ", MVT=" + MVT + ", THUONGHIEU=" + THUONGHIEU + 
               ", NAMSANXUAT=" + NAMSANXUAT + ", GIANHAP=" + GIANHAP + ", GIABAN=" + GIABAN +
               ", SOLUONG=" + SOLUONG + ", THOIGIANBAOHANH=" + THOIGIANBAOHANH + "]";
    }
    

}