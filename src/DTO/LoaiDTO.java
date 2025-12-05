package DTO;

public class LoaiDTO {
    private int ML;
    private String TENL;

    public LoaiDTO() {
    }

    public LoaiDTO(int mDV, String tENDV) {
        ML = mDV;
        TENL = tENDV;
    }

    public int getML() {
        return ML;
    }

    public void setML(int mDV) {
        ML = mDV;
    }

    public String getTENL() {
        return TENL;
    }

    public void setTENL(String tENDV) {
        TENL = tENDV;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ML;
        result = prime * result + ((TENL == null) ? 0 : TENL.hashCode());
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
        LoaiDTO other = (LoaiDTO) obj;
        if (ML != other.ML)
            return false;
        if (TENL == null) {
            if (other.TENL != null)
                return false;
        } else if (!TENL.equals(other.TENL))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LoaiDTO [MDV=" + ML + ", TENDV=" + TENL + "]";
    }
    
}
