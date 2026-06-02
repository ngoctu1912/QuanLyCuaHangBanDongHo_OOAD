package DTO;

public class chinhanhDTO {

    private String MCN;
    private String TEN;
    private String DIACHI;
    private String SDT;

    public chinhanhDTO() {
    }

    public chinhanhDTO(String MCN, String TEN, String DIACHI, String SDT) {
        this.MCN = MCN;
        this.TEN = TEN;
        this.DIACHI = DIACHI;
        this.SDT = SDT;
    }

    public String getMCN() {
        return MCN;
    }

    public void setMCN(String MCN) {
        this.MCN = MCN;
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

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
}