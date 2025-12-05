package DTO;

import java.sql.Timestamp;
import java.util.Objects;


public class PhieuDTO {
    private int MP;
    private int MNV;
    private Timestamp TG;
    private long TIEN;
    private int TT;

    public PhieuDTO() {
    }

    public PhieuDTO(int MP, int MNV, Timestamp TG, long TIEN, int TT) {
        this.MP = MP;
        this.MNV = MNV;
        this.TG = TG;
        this.TIEN = TIEN;
        this.TT = TT;
    }
    
    public int getMP() {
        return MP;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }

    public int getMNV() {
        return MNV;
    }

    public void setMNV(int MNV) {
        this.MNV = MNV;
    }

    public Timestamp getTG() {
        return TG;
    }

    public void setTG(Timestamp TG) {
        this.TG = TG; 
    }

    public long getTIEN() {
        return TIEN;
    }

    public void setTIEN(long TIEN) {
        this.TIEN = TIEN;
    }

    public int getTT() {
        return TT;
    }

    public void setTT(int TT) {
        this.TT = TT;
    }

        @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.MP;
        hash = 59 * hash + this.MNV;
        hash = 59 * hash + Objects.hashCode(this.TG);
        hash = 59 * hash + (int) (this.TIEN ^ (this.TIEN >>> 32));
        hash = 59 * hash + this.TT;
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
        final PhieuDTO other = (PhieuDTO) obj;
        if (this.MP != other.MP) {
            return false;
        }
        if (this.MNV != other.MNV) {
            return false;
        }
        if (this.TIEN != other.TIEN) {
            return false;
        }
        if (this.TT != other.TT) {
            return false;
        }
        return Objects.equals(this.TG, other.TG);
    }

    @Override
    public String toString() {
        return "PhieuDTO{" + "MP=" + MP + ", MNV=" + MNV + ", TG=" + TG + ", TIEN=" + TIEN + ", TT=" + TT + '}';
    }
}
