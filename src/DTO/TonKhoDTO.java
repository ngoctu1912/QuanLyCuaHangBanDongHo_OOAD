package DTO;

public class TonKhoDTO {

    private int MSP;
    private int MKHO;
    private int SOLUONG;

    public TonKhoDTO() {
    }

    public TonKhoDTO(int MSP, int MKHO, int SOLUONG) {
        this.MSP = MSP;
        this.MKHO = MKHO;
        this.SOLUONG = SOLUONG;
    }

    public int getMSP() {
        return MSP;
    }

    public void setMSP(int MSP) {
        this.MSP = MSP;
    }

    public int getMKHO() {
        return MKHO;
    }

    public void setMKHO(int MKHO) {
        this.MKHO = MKHO;
    }

    public int getSOLUONG() {
        return SOLUONG;
    }

    public void setSOLUONG(int SOLUONG) {
        this.SOLUONG = SOLUONG;
    }
}