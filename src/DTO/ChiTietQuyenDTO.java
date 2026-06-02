package DTO;

import java.util.Objects;

public class ChiTietQuyenDTO {
    private int MNQ;
    private String MCHUCNANG;
    private String HANHDONG;

    public ChiTietQuyenDTO() {
        
    }

    public ChiTietQuyenDTO(int MNQ, String MCHUCNANG, String HANHDONG) {
        this.MNQ = MNQ;
        this.MCHUCNANG = MCHUCNANG;
        this.HANHDONG = HANHDONG;
    }

    public int getManhomquyen() {
        return MNQ;
    }

    public void setManhomquyen(int MNQ) {
        this.MNQ = MNQ;
    }

    public String getMachucnang() {
        return MCHUCNANG;
    }

    public void setMachucnang(String MCHUCNANG) {
        this.MCHUCNANG = MCHUCNANG;
    }

    public String getHanhdong() {
        return HANHDONG;
    }

    public void setHanhdong(String HANHDONG) {
        this.HANHDONG = HANHDONG;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.MNQ);
        hash = 83 * hash + Objects.hashCode(this.MCHUCNANG);
        hash = 83 * hash + Objects.hashCode(this.HANHDONG);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChiTietQuyenDTO other = (ChiTietQuyenDTO) obj;
        if (!Objects.equals(this.MNQ, other.MNQ)) {
            return false;
        }
        if (!Objects.equals(this.MCHUCNANG, other.MCHUCNANG)) {
            return false;
        }
        return Objects.equals(this.HANHDONG, other.HANHDONG);
    }

    @Override
    public String toString() {
        return "ChiTietQuyen{" + "Ma nhom quyen = " + MNQ + ", Ma chuc nang = " + MCHUCNANG + ", Hanh dong = " + HANHDONG + '}';
    }

    
    
}
