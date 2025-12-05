package DTO;

import java.util.Objects;

public class NhomQuyenDTO {
    private int MNQ;
    private String TEN;

    public NhomQuyenDTO() {
    }

    public NhomQuyenDTO(int MNQ, String TEN) {
        this.MNQ = MNQ;
        this.TEN = TEN;
    }

    public int getManhomquyen() {
        return MNQ;
    }

    public void setManhomquyen(int MNQ) {
        this.MNQ = MNQ;
    }

    public String getTennhomquyen() {
        return TEN;
    }

    public void setTennhomquyen(String TEN) {
        this.TEN = TEN;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.MNQ;
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
        final NhomQuyenDTO other = (NhomQuyenDTO) obj;
        if (this.MNQ != other.MNQ) {
            return false;
        }
        return Objects.equals(this.TEN, other.TEN);
    }

    @Override
    public String toString() {
        return "NhomQuyenDTO{" + "Ma nhom quyen = " + MNQ + ", Ten nhom quyen = " + TEN + '}';
    }
    
}
