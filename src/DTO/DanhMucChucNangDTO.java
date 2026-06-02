package DTO;

import java.util.Objects;

public class DanhMucChucNangDTO {
    private String MCHUCNANG;
    private String TEN;

    public DanhMucChucNangDTO() {
    }

    public DanhMucChucNangDTO(String MCHUCNANG, String TEN) {
        this.MCHUCNANG = MCHUCNANG;
        this.TEN = TEN;
    }

    public String getMCHUCNANG() {
        return MCHUCNANG;
    }

    public void setMCHUCNANG(String MCHUCNANG) {
        this.MCHUCNANG = MCHUCNANG;
    }

    public String getTEN() {
        return TEN;
    }

    public void setTEN(String TEN) {
        this.TEN = TEN;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.MCHUCNANG);
        hash = 37 * hash + Objects.hashCode(this.TEN);
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
        final DanhMucChucNangDTO other = (DanhMucChucNangDTO) obj;
        if (!Objects.equals(this.MCHUCNANG, other.MCHUCNANG)) {
            return false;
        }
        return Objects.equals(this.TEN, other.TEN);
    }

    @Override
    public String toString() {
        return "DanhMucChucNang{" + "Ma chuc nang = " + MCHUCNANG + ", Ten chuc nang = " + TEN + '}';
    }
}
