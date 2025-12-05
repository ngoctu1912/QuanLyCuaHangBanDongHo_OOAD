package DTO;

import java.util.Date;
public class NhanVienDTO {

    private int MNV;
    private String HOTEN;
    private int GIOITINH;
    private String SDT;
    private Date NGAYSINH;
    private int TT;
    private String EMAIL;
    private int MCV;

    public NhanVienDTO() {
    }


    public NhanVienDTO(int mNV, String hOTEN, int gIOITINH, String sDT, Date nGAYSINH, int tT, String eMAIL, int mCV) {
        MNV = mNV;
        HOTEN = hOTEN;
        GIOITINH = gIOITINH;
        SDT = sDT;
        NGAYSINH = nGAYSINH;
        TT = tT;
        EMAIL = eMAIL;
        MCV = mCV;
    }

    


    public int getMNV() {
        return MNV;
    }


    public void setMNV(int mNV) {
        MNV = mNV;
    }


    public String getHOTEN() {
        return HOTEN;
    }


    public void setHOTEN(String hOTEN) {
        HOTEN = hOTEN;
    }


    public int getGIOITINH() {
        return GIOITINH;
    }


    public void setGIOITINH(int gIOITINH) {
        GIOITINH = gIOITINH;
    }


    public String getSDT() {
        return SDT;
    }


    public void setSDT(String sDT) {
        SDT = sDT;
    }


    public Date getNGAYSINH() {
        return NGAYSINH;
    }


    public void setNGAYSINH(Date nGAYSINH) {
        NGAYSINH = nGAYSINH;
    }


    public int getTT() {
        return TT;
    }


    public void setTT(int tT) {
        TT = tT;
    }


    public String getEMAIL() {
        return EMAIL;
    }


    public void setEMAIL(String eMAIL) {
        EMAIL = eMAIL;
    }


    public int getMCV() {
        return MCV;
    }


    public void setMCV(int mCV) {
        MCV = mCV;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + MNV;
        result = prime * result + ((HOTEN == null) ? 0 : HOTEN.hashCode());
        result = prime * result + GIOITINH;
        result = prime * result + ((SDT == null) ? 0 : SDT.hashCode());
        result = prime * result + ((NGAYSINH == null) ? 0 : NGAYSINH.hashCode());
        result = prime * result + TT;
        result = prime * result + ((EMAIL == null) ? 0 : EMAIL.hashCode());
        result = prime * result + MCV;
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
        NhanVienDTO other = (NhanVienDTO) obj;
        if (MNV != other.MNV)
            return false;
        if (HOTEN == null) {
            if (other.HOTEN != null)
                return false;
        } else if (!HOTEN.equals(other.HOTEN))
            return false;
        if (GIOITINH != other.GIOITINH)
            return false;
        if (SDT == null) {
            if (other.SDT != null)
                return false;
        } else if (!SDT.equals(other.SDT))
            return false;
        if (NGAYSINH == null) {
            if (other.NGAYSINH != null)
                return false;
        } else if (!NGAYSINH.equals(other.NGAYSINH))
            return false;
        if (TT != other.TT)
            return false;
        if (EMAIL == null) {
            if (other.EMAIL != null)
                return false;
        } else if (!EMAIL.equals(other.EMAIL))
            return false;
        if (MCV != other.MCV)
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "NhanVienDTO [MNV=" + MNV + ", HOTEN=" + HOTEN + ", GIOITINH=" + GIOITINH + ", SDT=" + SDT
                + ", NGAYSINH=" + NGAYSINH + ", TT=" + TT + ", EMAIL=" + EMAIL + ", MCV=" + MCV + "]";
    }


    public int getColumnCount() {
        return getClass().getDeclaredFields().length;
    }

}
