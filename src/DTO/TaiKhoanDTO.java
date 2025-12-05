package DTO;

import java.util.Objects;

public class TaiKhoanDTO {
    private int MNV;
    private String MK;
    private String TDN;
    private int MNQ;
    private int TT;

    public TaiKhoanDTO() {

    }
    
    public TaiKhoanDTO(int MNV, String TDN, String MK, int MNQ, int TT) {
        this.MNV = MNV;
        this.MK = MK;
        this.TDN = TDN;
        this.MNQ = MNQ;
        this.TT = TT;
    }

    public int getMNV() {
        return MNV;
    }

    public void setMNV(int mNV) {
        MNV = mNV;
    }

    public String getMK() {
        return MK;
    }

    public void setMK(String mK) {
        MK = mK;
    }

    public String getTDN() {
        return TDN;
    }

    public void setTDN(String tDN) {
        TDN = tDN;
    }

    public int getMNQ() {
        return MNQ;
    }

    public void setMNQ(int mNQ) {
        MNQ = mNQ;
    }

    public int getTT() {
        return TT;
    }

    public void setTT(int tT) {
        TT = tT;
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.MNV;
        hash = 19 * hash + Objects.hashCode(this.TDN);
        hash = 19 * hash + Objects.hashCode(this.MK);
        hash = 19 * hash + this.MNQ;
        hash = 19 * hash + this.TT;
        return hash;
    }

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
        final TaiKhoanDTO other = (TaiKhoanDTO) obj;
        if (this.MNV != other.MNV) {
            return false;
        }
        if (this.MNQ != other.MNQ) {
            return false;
        }
        if (this.TT != other.TT) {
            return false;
        }
        if (!Objects.equals(this.TDN, other.TDN)) {
            return false;
        }
        return Objects.equals(this.MK, other.MK);
    }

    public String toString() {
        return "AccountDTO{" + "Ma nhan vien =" + MNV + ", Username =" + TDN + ", Mat khau=" + MK + ", Ma nhom quyen=" + MNQ + ", Trang thai=" + TT + '}';
    }
    
}