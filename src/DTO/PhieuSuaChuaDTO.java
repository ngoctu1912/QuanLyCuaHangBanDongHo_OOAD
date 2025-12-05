package DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class PhieuSuaChuaDTO {
    private int MSC;           // Mã sửa chữa
    private int MPB;           // Mã phiếu bảo hành
    private Integer MNV;       // Mã nhân viên (nullable)
    private Date NGAYNHAN;     // Ngày nhận
    private Date NGAYTRA;      // Ngày trả (nullable)
    private String NGUYENNHAN; // Nguyên nhân
    private int TINHTRANG;     // Tình trạng (0: chờ, 1: đang sửa, 2: hoàn thành, 3: không sửa được)
    private BigDecimal CHIPHI; // Chi phí
    private String GHICHU;     // Ghi chú

    // Constructor mặc định
    public PhieuSuaChuaDTO() {
    }

    // Constructor đầy đủ tham số
    public PhieuSuaChuaDTO(int MSC, int MPB, Integer MNV, Date NGAYNHAN, Date NGAYTRA, 
                           String NGUYENNHAN, int TINHTRANG, BigDecimal CHIPHI, String GHICHU) {
        this.MSC = MSC;
        this.MPB = MPB;
        this.MNV = MNV;
        this.NGAYNHAN = NGAYNHAN;
        this.NGAYTRA = NGAYTRA;
        this.NGUYENNHAN = NGUYENNHAN;
        this.TINHTRANG = TINHTRANG;
        this.CHIPHI = CHIPHI;
        this.GHICHU = GHICHU;
    }

    // Constructor không có MSC (cho insert)
    public PhieuSuaChuaDTO(int MPB, Integer MNV, Date NGAYNHAN, Date NGAYTRA, 
                           String NGUYENNHAN, int TINHTRANG, BigDecimal CHIPHI, String GHICHU) {
        this.MPB = MPB;
        this.MNV = MNV;
        this.NGAYNHAN = NGAYNHAN;
        this.NGAYTRA = NGAYTRA;
        this.NGUYENNHAN = NGUYENNHAN;
        this.TINHTRANG = TINHTRANG;
        this.CHIPHI = CHIPHI;
        this.GHICHU = GHICHU;
    }

    // Getters and Setters
    public int getMSC() {
        return MSC;
    }

    public void setMSC(int MSC) {
        this.MSC = MSC;
    }

    public int getMPB() {
        return MPB;
    }

    public void setMPB(int MPB) {
        this.MPB = MPB;
    }

    public Integer getMNV() {
        return MNV;
    }

    public void setMNV(Integer MNV) {
        this.MNV = MNV;
    }

    public Date getNGAYNHAN() {
        return NGAYNHAN;
    }

    public void setNGAYNHAN(Date NGAYNHAN) {
        this.NGAYNHAN = NGAYNHAN;
    }

    public Date getNGAYTRA() {
        return NGAYTRA;
    }

    public void setNGAYTRA(Date NGAYTRA) {
        this.NGAYTRA = NGAYTRA;
    }

    public String getNGUYENNHAN() {
        return NGUYENNHAN;
    }

    public void setNGUYENNHAN(String NGUYENNHAN) {
        this.NGUYENNHAN = NGUYENNHAN;
    }

    public int getTINHTRANG() {
        return TINHTRANG;
    }

    public void setTINHTRANG(int TINHTRANG) {
        this.TINHTRANG = TINHTRANG;
    }

    public BigDecimal getCHIPHI() {
        return CHIPHI;
    }

    public void setCHIPHI(BigDecimal CHIPHI) {
        this.CHIPHI = CHIPHI;
    }

    public String getGHICHU() {
        return GHICHU;
    }

    public void setGHICHU(String GHICHU) {
        this.GHICHU = GHICHU;
    }

    // Phương thức lấy tên trạng thái
    public String getTenTinhTrang() {
        switch (TINHTRANG) {
            case 0: return "Chờ xử lý";
            case 1: return "Đang sửa";
            case 2: return "Hoàn thành";
            case 3: return "Không sửa được";
            default: return "Không xác định";
        }
    }

    @Override
    public String toString() {
        return "PhieuSuaChuaDTO{" +
                "MSC=" + MSC +
                ", MPB=" + MPB +
                ", MNV=" + MNV +
                ", NGAYNHAN=" + NGAYNHAN +
                ", NGAYTRA=" + NGAYTRA +
                ", NGUYENNHAN='" + NGUYENNHAN + '\'' +
                ", TINHTRANG=" + TINHTRANG +
                ", CHIPHI=" + CHIPHI +
                ", GHICHU='" + GHICHU + '\'' +
                '}';
    }
}
