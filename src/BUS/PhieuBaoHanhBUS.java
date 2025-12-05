package BUS;

import java.sql.Date;
import java.util.ArrayList;

import DAO.KhachHangDAO;
import DAO.PhieuBaoHanhDAO;
import DAO.SanPhamDAO;
import DTO.KhachHangDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.SanPhamDTO;

public class PhieuBaoHanhBUS {

    private final PhieuBaoHanhDAO pbhDAO = PhieuBaoHanhDAO.getInstance();
    private final SanPhamDAO spDAO = SanPhamDAO.getInstance();
    private final KhachHangDAO khDAO = KhachHangDAO.getInstance();
    public ArrayList<PhieuBaoHanhDTO> listPhieuBaoHanh = new ArrayList<>();

    public PhieuBaoHanhBUS() {
        listPhieuBaoHanh = pbhDAO.selectAll();
    }

    public ArrayList<PhieuBaoHanhDTO> getAll() {
        return this.pbhDAO.selectAll();
    }

    public PhieuBaoHanhDTO getByIndex(int index) {
        return this.listPhieuBaoHanh.get(index);
    }

    public PhieuBaoHanhDTO getByMaPhieuBaoHanh(int mpb) {
        return pbhDAO.selectById(String.valueOf(mpb));
    }

    public ArrayList<PhieuBaoHanhDTO> getByMaHoaDon(int mhd) {
        return pbhDAO.selectByMaHoaDon(mhd);
    }

    public ArrayList<PhieuBaoHanhDTO> getByMaKhachHang(int mkh) {
        return pbhDAO.selectByMaKhachHang(mkh);
    }

    public int getIndexByMaPBH(int mpb) {
        int i = 0;
        int vitri = -1;
        while (i < this.listPhieuBaoHanh.size() && vitri == -1) {
            if (listPhieuBaoHanh.get(i).getMPB() == mpb) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public Boolean add(PhieuBaoHanhDTO pbh) {
        boolean check = pbhDAO.insert(pbh) != 0;
        if (check) {
            this.listPhieuBaoHanh.add(pbh);
        }
        return check;
    }

    public Boolean delete(PhieuBaoHanhDTO pbh) {
        boolean check = pbhDAO.delete(Integer.toString(pbh.getMPB())) != 0;
        if (check) {
            this.listPhieuBaoHanh.remove(getIndexByMaPBH(pbh.getMPB()));
        }
        return check;
    }

    public Boolean update(PhieuBaoHanhDTO pbh) {
        boolean check = pbhDAO.update(pbh) != 0;
        if (check) {
            this.listPhieuBaoHanh.set(getIndexByMaPBH(pbh.getMPB()), pbh);
        }
        return check;
    }

    public Boolean updateTrangThai(int mpb, int trangThai) {
        boolean check = pbhDAO.updateTrangThai(mpb, trangThai) != 0;
        if (check) {
            int index = getIndexByMaPBH(mpb);
            if (index != -1) {
                this.listPhieuBaoHanh.get(index).setTRANGTHAI(trangThai);
            }
        }
        return check;
    }
    
    // Cập nhật trạng thái tất cả phiếu bảo hành theo mã hóa đơn
    public Boolean updateTrangThaiByMaHoaDon(int maHoaDon, int trangThai) {
        boolean check = pbhDAO.updateTrangThaiByMaHoaDon(maHoaDon, trangThai) != 0;
        if (check) {
            // Cập nhật trong danh sách local
            for (PhieuBaoHanhDTO pbh : this.listPhieuBaoHanh) {
                if (pbh.getMHD() == maHoaDon) {
                    pbh.setTRANGTHAI(trangThai);
                }
            }
        }
        return check;
    }

    // Kiểm tra xem phiếu bảo hành còn hạn hay không
    public boolean checkConHan(PhieuBaoHanhDTO pbh) {
        Date currentDate = new Date(System.currentTimeMillis());
        return pbh.getNGAYKETTHUC().after(currentDate);
    }

    // Lấy thông tin sản phẩm từ phiếu bảo hành
    public SanPhamDTO getSanPhamByPBH(PhieuBaoHanhDTO pbh) {
        return spDAO.selectById(String.valueOf(pbh.getMSP()));
    }

    // Lấy thông tin khách hàng từ phiếu bảo hành
    public KhachHangDTO getKhachHangByPBH(PhieuBaoHanhDTO pbh) {
        return khDAO.selectById(String.valueOf(pbh.getMKH()));
    }

    public ArrayList<PhieuBaoHanhDTO> search(String text, String type) {
        ArrayList<PhieuBaoHanhDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (Integer.toString(i.getMPB()).toLowerCase().contains(text) 
                        || Integer.toString(i.getMHD()).toLowerCase().contains(text)
                        || Integer.toString(i.getMSP()).toLowerCase().contains(text)
                        || Integer.toString(i.getMKH()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã phiếu bảo hành" -> {
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (Integer.toString(i.getMPB()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã hóa đơn" -> {
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (Integer.toString(i.getMHD()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã sản phẩm" -> {
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (Integer.toString(i.getMSP()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã khách hàng" -> {
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (Integer.toString(i.getMKH()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Còn hạn" -> {
                Date currentDate = new Date(System.currentTimeMillis());
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (i.getTRANGTHAI() == 1 && i.getNGAYKETTHUC().after(currentDate)) {
                        result.add(i);
                    }
                }
            }
            case "Hết hạn" -> {
                Date currentDate = new Date(System.currentTimeMillis());
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (i.getTRANGTHAI() == 0 || i.getNGAYKETTHUC().before(currentDate)) {
                        result.add(i);
                    }
                }
            }
            case "Đã hủy" -> {
                for (PhieuBaoHanhDTO i : this.listPhieuBaoHanh) {
                    if (i.getTRANGTHAI() == 2) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public String[] getArrTenSanPham() {
        String[] result = new String[listPhieuBaoHanh.size()];
        for (int i = 0; i < listPhieuBaoHanh.size(); i++) {
            SanPhamDTO sp = spDAO.selectById(String.valueOf(listPhieuBaoHanh.get(i).getMSP()));
            result[i] = sp != null ? sp.getTEN() : "N/A";
        }
        return result;
    }

    public String getTenSanPham(int msp) {
        SanPhamDTO sp = spDAO.selectById(String.valueOf(msp));
        return sp != null ? sp.getTEN() : "N/A";
    }

    public String getTenKhachHang(int mkh) {
        KhachHangDTO kh = khDAO.selectById(String.valueOf(mkh));
        return kh != null ? kh.getHOTEN() : "N/A";
    }
}
