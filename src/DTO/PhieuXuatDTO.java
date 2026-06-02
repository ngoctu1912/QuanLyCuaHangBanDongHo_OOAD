package DTO;

import java.sql.Timestamp;
// import java.util.ArrayList;

public class PhieuXuatDTO extends PhieuDTO {
    private int MKH;
    private int DIEMTICHLUY;
    private String LYDOHUY;
    private String MCN;

    public PhieuXuatDTO(int MKH) {
        this.MKH = MKH;
    }

    public PhieuXuatDTO(int MKH, int MP, int MNV, Timestamp TG, long TIENX, int TT, int DIEMTICHLUY,String MCN) {
        super(MP, MNV, TG, TIENX, TT);
        this.MKH = MKH;
        this.DIEMTICHLUY = DIEMTICHLUY;
        this.MCN=MCN;
    }

    public PhieuXuatDTO(int MKH, int MP, int MNV, Timestamp TG, long TIENX, int TT, int DIEMTICHLUY, String LYDOHUY,String MCN) {
        super(MP, MNV, TG, TIENX, TT);
        this.MKH = MKH;
        this.DIEMTICHLUY = DIEMTICHLUY;
        this.LYDOHUY = LYDOHUY;
        this.MCN=MCN;
    }

    public int getMKH() {
        return MKH;
    }

    public void setMKH(int MKH) {
        this.MKH = MKH;
    }
    
    public int getDIEMTICHLUY() {
        return DIEMTICHLUY;
    }

    public void setDIEMTICHLUY(int dIEMTICHLUY) {
        DIEMTICHLUY = dIEMTICHLUY;
    }

    public String getLYDOHUY() {
        return LYDOHUY;
    }

    public void setLYDOHUY(String LYDOHUY) {
        this.LYDOHUY = LYDOHUY;
    }

    public String getMCN() {
        return MCN;
    }

    public void setMCN(String MCN) {
        this.MCN = MCN;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + MKH;
        result = prime * result + DIEMTICHLUY;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhieuXuatDTO other = (PhieuXuatDTO) obj;
        if (MKH != other.MKH)
            return false;
        if (DIEMTICHLUY != other.DIEMTICHLUY)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "HoaDonDTO [MKH=" + MKH + ", DIEMTICHLUY=" + DIEMTICHLUY + "]";
    }
    
}
