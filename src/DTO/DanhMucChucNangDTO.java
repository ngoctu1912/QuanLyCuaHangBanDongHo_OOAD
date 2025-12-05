package DTO;

import java.util.Objects;

public class DanhMucChucNangDTO {
    private String MCN;
    private String TEN;

    public DanhMucChucNangDTO() {
    }

    public DanhMucChucNangDTO(String MCN, String TEN) {
        this.MCN = MCN;
        this.TEN = TEN;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.MCN);
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
        if (!Objects.equals(this.MCN, other.MCN)) {
            return false;
        }
        return Objects.equals(this.TEN, other.TEN);
    }

    @Override
    public String toString() {
        return "DanhMucChucNang{" + "Ma chuc nang = " + MCN + ", Ten chuc nang = " + TEN + '}';
    }
}
