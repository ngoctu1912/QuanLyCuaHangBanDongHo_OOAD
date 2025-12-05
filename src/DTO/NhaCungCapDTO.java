package DTO;

import java.util.Objects;

public class NhaCungCapDTO {
    private int MANCC;
    private String TEN;
    private String DIACHI;
    private String EMAIL;
    private String SDT;

    public NhaCungCapDTO() {
    }

    public NhaCungCapDTO(int MANCC, String TEN, String DIACHI, String EMAIL, String SDT) {
        this.MANCC = MANCC;
        this.TEN = TEN;
        this.DIACHI = DIACHI;
        this.EMAIL = EMAIL;
        this.SDT = SDT;
    }

    public int getMancc() {
        return MANCC;
    }

    public void setMancc(int MANCC) {
        this.MANCC = MANCC;
    }

    public String getTenncc() {
        return TEN;
    }

    public void setTenncc(String TEN) {
        this.TEN = TEN;
    }

    public String getDiachi() {
        return DIACHI;
    }

    public void setDiachi(String DIACHI) {
        this.DIACHI = DIACHI;
    }

    public String getEmail() {
        return EMAIL;
    }

    public void setEmail(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getSdt() {
        return SDT;
    }

    public void setSdt(String SDT) {
        this.SDT = SDT;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.MANCC;
        hash = 67 * hash + Objects.hashCode(this.TEN);
        hash = 67 * hash + Objects.hashCode(this.DIACHI);
        hash = 67 * hash + Objects.hashCode(this.EMAIL);
        hash = 67 * hash + Objects.hashCode(this.SDT);
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
        final NhaCungCapDTO other = (NhaCungCapDTO) obj;
        if (this.getMancc() != other.getMancc()) {
            return false;
        }
        if (!Objects.equals(this.getTenncc(), other.getTenncc())) {
            return false;
        }
        if (!Objects.equals(this.getDiachi(), other.getDiachi())) {
            return false;
        }
        if (!Objects.equals(this.getEmail(), other.getEmail())) {
            return false;
        }
        return !Objects.equals(this.getSdt(), other.getSdt());
    }

    @Override
    public String toString() {
        return "NhaCungCap{" + "Ma nha cung cap = " + MANCC + ", Ten nha cung cap = " + TEN + ", Dia chi = " + DIACHI + ", Email = " + EMAIL + ", So dien thoai = " + SDT + '}';
    }
    
}
