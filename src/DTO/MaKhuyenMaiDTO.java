package DTO;

import java.sql.Timestamp;

public class MaKhuyenMaiDTO {
    private String MKM;
    private Timestamp TGBD;
    private Timestamp TGKT;

    public MaKhuyenMaiDTO() {

    }

    public MaKhuyenMaiDTO(String mKM, Timestamp tGBD, Timestamp tGKT) {
        MKM = mKM;
        TGBD = tGBD;
        TGKT = tGKT;
    }

    public String getMKM() {
        return MKM;
    }

    public void setMKM(String mKM) {
        MKM = mKM;
    }

    public Timestamp getTGBD() {
        return TGBD;
    }

    public void setTGBD(Timestamp tGBD) {
        TGBD = tGBD;
    }

    public Timestamp getTGKT() {
        return TGKT;
    }

    public void setTGKT(Timestamp tGKT) {
        TGKT = tGKT;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((MKM == null) ? 0 : MKM.hashCode());
        result = prime * result + ((TGBD == null) ? 0 : TGBD.hashCode());
        result = prime * result + ((TGKT == null) ? 0 : TGKT.hashCode());
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
        MaKhuyenMaiDTO other = (MaKhuyenMaiDTO) obj;
        if (MKM == null) {
            if (other.MKM != null)
                return false;
        } else if (!MKM.equals(other.MKM))
            return false;
        if (TGBD == null) {
            if (other.TGBD != null)
                return false;
        } else if (!TGBD.equals(other.TGBD))
            return false;
        if (TGKT == null) {
            if (other.TGKT != null)
                return false;
        } else if (!TGKT.equals(other.TGKT))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MaKhuyenMaiDTO [MKM=" + MKM + ", TGBD=" + TGBD + ", TGKT=" + TGKT + "]";
    }
    
}
