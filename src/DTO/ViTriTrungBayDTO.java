package DTO;

public class ViTriTrungBayDTO {
    private int MVT;
    private String TEN;
    private String GHICHU;

    public ViTriTrungBayDTO() {
    }

    public ViTriTrungBayDTO(int MVT, String TEN, String GHICHU) {
        this.MVT = MVT;
        this.TEN = TEN;
        this.GHICHU = GHICHU;
    }

    public int getMVT() {
        return MVT;
    }

    public void setMVT(int MVT) {
        this.MVT = MVT;
    }

    public String getTEN() {
        return TEN;
    }

    public void setTEN(String TEN) {
        this.TEN = TEN;
    }

    public String getGHICHU() {
        return GHICHU;
    }

    public void setGHICHU(String GHICHU) {
        this.GHICHU = GHICHU;
    }

    @Override
    public String toString() {
        return "ViTriTrungBayDTO{" +
                "MVT=" + MVT +
                ", TEN='" + TEN + '\'' +
                ", GHICHU='" + GHICHU + '\'' +
                '}';
    }
}
