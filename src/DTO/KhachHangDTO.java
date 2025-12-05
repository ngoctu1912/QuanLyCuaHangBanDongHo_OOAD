package DTO;

import java.util.Date;

public class KhachHangDTO {

    private int MKH;
    private String HOTEN;
    private String SDT;
    private String DIACHI;
    private String EMAIL;
    private Date NGAYTHAMGIA;

    public KhachHangDTO() {
    }

    public KhachHangDTO(int mKH, String hOTEN, String sDT, String dIACHI, String eMAIL, Date nGAYTHAMGIA) {
        MKH = mKH;
        HOTEN = hOTEN;
        SDT = sDT;
        DIACHI = dIACHI;
        EMAIL = eMAIL;
        NGAYTHAMGIA = nGAYTHAMGIA;
    }



    public int getMKH() {
        return MKH;
    }

    public void setMKH(int mKH) {
        MKH = mKH;
    }

    public String getHOTEN() {
        return HOTEN;
    }

    public void setHOTEN(String hOTEN) {
        HOTEN = hOTEN;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String sDT) {
        SDT = sDT;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public void setDIACHI(String dIACHI) {
        DIACHI = dIACHI;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String eMAIL) {
        EMAIL = eMAIL;
    }

    public Date getNGAYTHAMGIA() {
        return NGAYTHAMGIA;
    }

    public void setNGAYTHAMGIA(Date nGAYTHAMGIA) {
        NGAYTHAMGIA = nGAYTHAMGIA;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + MKH;
        result = prime * result + ((HOTEN == null) ? 0 : HOTEN.hashCode());
        result = prime * result + ((SDT == null) ? 0 : SDT.hashCode());
        result = prime * result + ((DIACHI == null) ? 0 : DIACHI.hashCode());
        result = prime * result + ((EMAIL == null) ? 0 : EMAIL.hashCode());
        result = prime * result + ((NGAYTHAMGIA == null) ? 0 : NGAYTHAMGIA.hashCode());
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
        KhachHangDTO other = (KhachHangDTO) obj;
        if (MKH != other.MKH)
            return false;
        if (HOTEN == null) {
            if (other.HOTEN != null)
                return false;
        } else if (!HOTEN.equals(other.HOTEN))
            return false;
        if (SDT == null) {
            if (other.SDT != null)
                return false;
        } else if (!SDT.equals(other.SDT))
            return false;
        if (DIACHI == null) {
            if (other.DIACHI != null)
                return false;
        } else if (!DIACHI.equals(other.DIACHI))
            return false;
        if (EMAIL == null) {
            if (other.EMAIL != null)
                return false;
        } else if (!EMAIL.equals(other.EMAIL))
            return false;
        if (NGAYTHAMGIA == null) {
            if (other.NGAYTHAMGIA != null)
                return false;
        } else if (!NGAYTHAMGIA.equals(other.NGAYTHAMGIA))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "KhachHangDTO [MKH=" + MKH + ", HOTEN=" + HOTEN + ", SDT=" + SDT + ", DIACHI=" + DIACHI + ", EMAIL="
                + EMAIL + ", NGAYTHAMGIA=" + NGAYTHAMGIA + "]";
    }

    


}
