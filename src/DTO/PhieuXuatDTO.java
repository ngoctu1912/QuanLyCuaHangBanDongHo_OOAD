package DTO;

import java.sql.Timestamp;
// import java.util.ArrayList;

public class PhieuXuatDTO extends PhieuDTO {
    private int MKH;
    private String LYDOHUY;

    public PhieuXuatDTO(int MKH) {
        this.MKH = MKH;
    }

    public PhieuXuatDTO(int MKH, int MP, int MNV, Timestamp TG, long TIENX, int TT) {
        super(MP, MNV, TG, TIENX, TT);
        this.MKH = MKH;
    }

    public PhieuXuatDTO(int MKH, int MP, int MNV, Timestamp TG, long TIENX, int TT, String LYDOHUY) {
        super(MP, MNV, TG, TIENX, TT);
        this.MKH = MKH;
        this.LYDOHUY = LYDOHUY;
    }

    public int getMKH() {
        return MKH;
    }

    public void setMKH(int MKH) {
        this.MKH = MKH;
    }

    public String getLYDOHUY() {
        return LYDOHUY;
    }

    public void setLYDOHUY(String LYDOHUY) {
        this.LYDOHUY = LYDOHUY;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + MKH;
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
        return true;
    }

    @Override
    public String toString() {
        return "HoaDonDTO [MKH=" + MKH + "]";
    }
    
}
