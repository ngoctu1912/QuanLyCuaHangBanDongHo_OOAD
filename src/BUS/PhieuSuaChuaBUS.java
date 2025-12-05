package BUS;

import java.math.BigDecimal;
import java.util.ArrayList;

import DAO.NhanVienDAO;
import DAO.PhieuBaoHanhDAO;
import DAO.PhieuSuaChuaDAO;
import DTO.NhanVienDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.PhieuSuaChuaDTO;

public class PhieuSuaChuaBUS {

    private final PhieuSuaChuaDAO pscDAO = PhieuSuaChuaDAO.getInstance();
    private final PhieuBaoHanhDAO pbhDAO = PhieuBaoHanhDAO.getInstance();
    private final NhanVienDAO nvDAO = NhanVienDAO.getInstance();
    public ArrayList<PhieuSuaChuaDTO> listPhieuSuaChua = new ArrayList<>();

    public PhieuSuaChuaBUS() {
        listPhieuSuaChua = pscDAO.selectAll();
    }

    public ArrayList<PhieuSuaChuaDTO> getAll() {
        return this.pscDAO.selectAll();
    }

    public PhieuSuaChuaDTO getByIndex(int index) {
        return this.listPhieuSuaChua.get(index);
    }

    public PhieuSuaChuaDTO getByMaPhieuSuaChua(int msc) {
        return pscDAO.selectById(String.valueOf(msc));
    }

    public ArrayList<PhieuSuaChuaDTO> getByMaPhieuBaoHanh(int mpb) {
        return pscDAO.selectByMaPhieuBaoHanh(mpb);
    }

    public int getIndexByMaPSC(int msc) {
        int i = 0;
        int vitri = -1;
        while (i < this.listPhieuSuaChua.size() && vitri == -1) {
            if (listPhieuSuaChua.get(i).getMSC() == msc) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public Boolean add(PhieuSuaChuaDTO psc) {
        boolean check = pscDAO.insert(psc) != 0;
        if (check) {
            this.listPhieuSuaChua.add(psc);
        }
        return check;
    }

    public Boolean delete(PhieuSuaChuaDTO psc) {
        boolean check = pscDAO.delete(Integer.toString(psc.getMSC())) != 0;
        if (check) {
            this.listPhieuSuaChua.remove(getIndexByMaPSC(psc.getMSC()));
        }
        return check;
    }

    public Boolean update(PhieuSuaChuaDTO psc) {
        boolean check = pscDAO.update(psc) != 0;
        if (check) {
            this.listPhieuSuaChua.set(getIndexByMaPSC(psc.getMSC()), psc);
        }
        return check;
    }

    public Boolean updateTinhTrang(int msc, int tinhTrang) {
        boolean check = pscDAO.updateTinhTrang(msc, tinhTrang) != 0;
        if (check) {
            int index = getIndexByMaPSC(msc);
            if (index != -1) {
                this.listPhieuSuaChua.get(index).setTINHTRANG(tinhTrang);
            }
        }
        return check;
    }

    // Lấy thông tin phiếu bảo hành từ phiếu sửa chữa
    public PhieuBaoHanhDTO getPhieuBaoHanhByPSC(PhieuSuaChuaDTO psc) {
        return pbhDAO.selectById(String.valueOf(psc.getMPB()));
    }

    // Lấy thông tin nhân viên từ phiếu sửa chữa
    public NhanVienDTO getNhanVienByPSC(PhieuSuaChuaDTO psc) {
        if (psc.getMNV() != null) {
            return nvDAO.selectById(String.valueOf(psc.getMNV()));
        }
        return null;
    }

    public ArrayList<PhieuSuaChuaDTO> search(String text, String type) {
        ArrayList<PhieuSuaChuaDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (Integer.toString(i.getMSC()).toLowerCase().contains(text) 
                        || Integer.toString(i.getMPB()).toLowerCase().contains(text)
                        || (i.getMNV() != null && Integer.toString(i.getMNV()).toLowerCase().contains(text))
                        || (i.getNGUYENNHAN() != null && i.getNGUYENNHAN().toLowerCase().contains(text))
                        || (i.getGHICHU() != null && i.getGHICHU().toLowerCase().contains(text))) {
                        result.add(i);
                    }
                }
            }
            case "Mã sửa chữa" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (Integer.toString(i.getMSC()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã phiếu bảo hành" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (Integer.toString(i.getMPB()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Chờ xử lý" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (i.getTINHTRANG() == 0) {
                        result.add(i);
                    }
                }
            }
            case "Đang sửa" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (i.getTINHTRANG() == 1) {
                        result.add(i);
                    }
                }
            }
            case "Hoàn thành" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (i.getTINHTRANG() == 2) {
                        result.add(i);
                    }
                }
            }
            case "Không sửa được" -> {
                for (PhieuSuaChuaDTO i : this.listPhieuSuaChua) {
                    if (i.getTINHTRANG() == 3) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public String getTenNhanVien(Integer mnv) {
        if (mnv != null) {
            NhanVienDTO nv = nvDAO.selectById(String.valueOf(mnv));
            return nv != null ? nv.getHOTEN() : "Chưa phân công";
        }
        return "Chưa phân công";
    }

    // Tính tổng chi phí sửa chữa
    public BigDecimal getTongChiPhi() {
        BigDecimal tong = BigDecimal.ZERO;
        for (PhieuSuaChuaDTO psc : listPhieuSuaChua) {
            if (psc.getCHIPHI() != null) {
                tong = tong.add(psc.getCHIPHI());
            }
        }
        return tong;
    }

    // Đếm số lượng phiếu theo tình trạng
    public int countByTinhTrang(int tinhTrang) {
        int count = 0;
        for (PhieuSuaChuaDTO psc : listPhieuSuaChua) {
            if (psc.getTINHTRANG() == tinhTrang) {
                count++;
            }
        }
        return count;
    }
}
