package DTO;

public class ChiTietMaKhuyenMaiDTO {
    private String MKM;
    private int MSP;
    private int PTG;
    
    public ChiTietMaKhuyenMaiDTO() {

    }
    public ChiTietMaKhuyenMaiDTO(String mKM, int mSP, int pTG) {
        MKM = mKM;
        MSP = mSP;
        PTG = pTG;
    }
    public String getMKM() {
        return MKM;
    }
    public void setMKM(String mKM) {
        MKM = mKM;
    }
    public int getMSP() {
        return MSP;
    }
    public void setMSP(int mSP) {
        MSP = mSP;
    }
    public int getPTG() {
        return PTG;
    }
    public void setPTG(int pTG) {
        PTG = pTG;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((MKM == null) ? 0 : MKM.hashCode());
        result = prime * result + MSP;
        result = prime * result + PTG;
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
        ChiTietMaKhuyenMaiDTO other = (ChiTietMaKhuyenMaiDTO) obj;
        if (MKM == null) {
            if (other.MKM != null)
                return false;
        } else if (!MKM.equals(other.MKM))
            return false;
        if (MSP != other.MSP)
            return false;
        if (PTG != other.PTG)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ChiTietMaKhuyenMai{" + "Ma khuyen mai = " + MKM + ", Ma san pham = " + MSP + ", Phan tram giam = " + PTG + '}';
    }
    
}
