package DTO;

public class DonViDTO {
    private int MDV;
    private String TENDV;

    public DonViDTO() {
    }

    public DonViDTO(int mDV, String tENDV) {
        MDV = mDV;
        TENDV = tENDV;
    }

    public int getMDV() {
        return MDV;
    }

    public void setMDV(int mDV) {
        MDV = mDV;
    }

    public String getTENDV() {
        return TENDV;
    }

    public void setTENDV(String tENDV) {
        TENDV = tENDV;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + MDV;
        result = prime * result + ((TENDV == null) ? 0 : TENDV.hashCode());
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
        DonViDTO other = (DonViDTO) obj;
        if (MDV != other.MDV)
            return false;
        if (TENDV == null) {
            if (other.TENDV != null)
                return false;
        } else if (!TENDV.equals(other.TENDV))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DonViDTO [MDV=" + MDV + ", TENDV=" + TENDV + "]";
    }
    
}
