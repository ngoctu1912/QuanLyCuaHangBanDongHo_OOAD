package DTO;


// import DTO.ChiTietPhieuDTO;

public class ChiTietPhieuNhapDTO extends ChiTietPhieuDTO{
    private int HINHTHUC;

    public ChiTietPhieuNhapDTO(int TIENNHAP, int HINHTHUC) {
        this.HINHTHUC = HINHTHUC;
    }

    public ChiTietPhieuNhapDTO(int MP, int MSP, int SL, int TIENNHAP, int HINHTHUC) {
        super(MP, MSP, SL, TIENNHAP);
        this.HINHTHUC = HINHTHUC;
    }

    public int getHINHTHUC() {
        return HINHTHUC;
    }

    public void setHINHTHUC(int HINHTHUC) {
        this.HINHTHUC = HINHTHUC;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.HINHTHUC;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChiTietPhieuNhapDTO other = (ChiTietPhieuNhapDTO) obj;
        return this.HINHTHUC == other.HINHTHUC;
    }
} 
