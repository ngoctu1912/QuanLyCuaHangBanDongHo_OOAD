package DTO;

public class ChucVuDTO {

    private int MCV;
    private String TENCV;
    private int MUCLUONG;

    public ChucVuDTO() {

    }

    public ChucVuDTO(int mCV, String tENCV, int mUCLUONG) {
        MCV = mCV;
        TENCV = tENCV;
        MUCLUONG = mUCLUONG;
    }

    public int getMCV() {
        return MCV;
    }

    public void setMCV(int mCV) {
        MCV = mCV;
    }

    public String getTENCV() {
        return TENCV;
    }

    public void setTENCV(String tENCV) {
        TENCV = tENCV;
    }

    public int getMUCLUONG() {
        return MUCLUONG;
    }

    public void setMUCLUONG(int mUCLUONG) {
        MUCLUONG = mUCLUONG;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + MCV;
        result = prime * result + ((TENCV == null) ? 0 : TENCV.hashCode());
        result = prime * result + MUCLUONG;
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
        ChucVuDTO other = (ChucVuDTO) obj;
        if (MCV != other.MCV)
            return false;
        if (TENCV == null) {
            if (other.TENCV != null)
                return false;
        } else if (!TENCV.equals(other.TENCV))
            return false;
        if (MUCLUONG != other.MUCLUONG)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ChucVuDTO [MCV=" + MCV + ", TENCV=" + TENCV + ", MUCLUONG=" + MUCLUONG + "]";
    }
}
