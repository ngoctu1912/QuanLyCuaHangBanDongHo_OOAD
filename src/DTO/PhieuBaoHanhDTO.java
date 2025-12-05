package DTO;

import java.sql.Date;

public class PhieuBaoHanhDTO {
    private int MPB;      // Mã phiếu bảo hành
    private int MHD;      // Mã hóa đơn
    private int MSP;      // Mã sản phẩm
    private int MKH;      // Mã khách hàng
    private Date NGAYBATDAU;   // Ngày bắt đầu
    private Date NGAYKETTHUC;  // Ngày kết thúc
    private int TRANGTHAI;     // Trạng thái (1: còn hạn, 0: hết hạn)

    // Constructor mặc định
    public PhieuBaoHanhDTO() {
    }

    // Constructor đầy đủ tham số
    public PhieuBaoHanhDTO(int MPB, int MHD, int MSP, int MKH, Date NGAYBATDAU, Date NGAYKETTHUC, int TRANGTHAI) {
        this.MPB = MPB;
        this.MHD = MHD;
        this.MSP = MSP;
        this.MKH = MKH;
        this.NGAYBATDAU = NGAYBATDAU;
        this.NGAYKETTHUC = NGAYKETTHUC;
        this.TRANGTHAI = TRANGTHAI;
    }

    // Constructor không có MPB (cho insert)
    public PhieuBaoHanhDTO(int MHD, int MSP, int MKH, Date NGAYBATDAU, Date NGAYKETTHUC, int TRANGTHAI) {
        this.MHD = MHD;
        this.MSP = MSP;
        this.MKH = MKH;
        this.NGAYBATDAU = NGAYBATDAU;
        this.NGAYKETTHUC = NGAYKETTHUC;
        this.TRANGTHAI = TRANGTHAI;
    }

    // Getters and Setters
    public int getMPB() {
        return MPB;
    }

    public void setMPB(int MPB) {
        this.MPB = MPB;
    }

    public int getMHD() {
        return MHD;
    }

    public void setMHD(int MHD) {
        this.MHD = MHD;
    }

    public int getMSP() {
        return MSP;
    }

    public void setMSP(int MSP) {
        this.MSP = MSP;
    }

    public int getMKH() {
        return MKH;
    }

    public void setMKH(int MKH) {
        this.MKH = MKH;
    }

    public Date getNGAYBATDAU() {
        return NGAYBATDAU;
    }

    public void setNGAYBATDAU(Date NGAYBATDAU) {
        this.NGAYBATDAU = NGAYBATDAU;
    }

    public Date getNGAYKETTHUC() {
        return NGAYKETTHUC;
    }

    public void setNGAYKETTHUC(Date NGAYKETTHUC) {
        this.NGAYKETTHUC = NGAYKETTHUC;
    }

    public int getTRANGTHAI() {
        return TRANGTHAI;
    }

    public void setTRANGTHAI(int TRANGTHAI) {
        this.TRANGTHAI = TRANGTHAI;
    }

    @Override
    public String toString() {
        return "PhieuBaoHanhDTO{" +
                "MPB=" + MPB +
                ", MHD=" + MHD +
                ", MSP=" + MSP +
                ", MKH=" + MKH +
                ", NGAYBATDAU=" + NGAYBATDAU +
                ", NGAYKETTHUC=" + NGAYKETTHUC +
                ", TRANGTHAI=" + TRANGTHAI +
                '}';
    }
}
