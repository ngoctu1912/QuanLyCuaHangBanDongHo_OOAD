package DTO;

public class ChiTietKiemKeDTO {
    private int MP;
    private int MSP;
    private int TRANGTHAISP; 
    private String GHICHU;
    
    public ChiTietKiemKeDTO(){
        
    }

    public ChiTietKiemKeDTO(int mP, int mSP, int tRANGTHAISP, String gHICHU ) {
        MP = mP;
        MSP = mSP;
        TRANGTHAISP = tRANGTHAISP;
        GHICHU = gHICHU;
    }

    public int getMP() {
        return MP;
    }

    public void setMP(int mP) {
        MP = mP;
    }

    public int getMSP() {
        return MSP;
    }

    public void setMSP(int mSP) {
        MSP = mSP;
    }

    public int getTRANGTHAISP() {
        return TRANGTHAISP;
    }

    public void setTRANGTHAISP(int tRANGTHAISP) {
        TRANGTHAISP = tRANGTHAISP;
    }

    public String getGHICHU() {
        return GHICHU;
    }

    public void setGHICHU(String gHICHU) {
        GHICHU = gHICHU;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + MP;
        result = prime * result + MSP;
        result = prime * result + TRANGTHAISP;
        result = prime * result + ((GHICHU == null) ? 0 : GHICHU.hashCode());
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
        ChiTietKiemKeDTO other = (ChiTietKiemKeDTO) obj;
        if (MP != other.MP)
            return false;
        if (MSP != other.MSP)
            return false;
        if (TRANGTHAISP != other.TRANGTHAISP)
            return false;
        if (GHICHU == null) {
            if (other.GHICHU != null)
                return false;
        } else if (!GHICHU.equals(other.GHICHU))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ChiTietKiemKeDTO [MP=" + MP + ", MSP=" + MSP + ", TRANGTHAISP=" + TRANGTHAISP + ", GHICHU=" + GHICHU +"]";
    }

}
