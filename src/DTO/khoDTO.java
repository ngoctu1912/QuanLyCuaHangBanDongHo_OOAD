package DTO;

public class khoDTO {

    private int MKHO;
    private String TEN;
    private String DIACHI;
    private String MCN;
    private int TRANGTHAI;

    public khoDTO() {
    }

    public khoDTO(int MKHO, String TEN, String DIACHI, String MCN, int TRANGTHAI) {
        this.MKHO = MKHO;
        this.TEN = TEN;
        this.DIACHI = DIACHI;
        this.MCN = MCN;
        this.TRANGTHAI = TRANGTHAI;
    }

    public int getMKHO() {
        return MKHO;
    }

    public void setMKHO(int MKHO) {
        this.MKHO = MKHO;
    }

    public String getTEN() {
        return TEN;
    }

    public void setTEN(String TEN) {
        this.TEN = TEN;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI = DIACHI;
    }

    public String getMCN() {
        return MCN;
    }

    public void setMCN(String MCN) {
        this.MCN = MCN;
    }

    public int getTRANGTHAI() {
        return TRANGTHAI;
    }

    public void setTRANGTHAI(int TRANGTHAI) {
        this.TRANGTHAI = TRANGTHAI;
    }

    @Override
    public String toString() {
        return TEN + " - " + DIACHI + " (" + MCN + ")";
    }
}