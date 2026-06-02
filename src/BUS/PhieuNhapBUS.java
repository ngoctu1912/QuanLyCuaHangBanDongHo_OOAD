package BUS;

import DAO.ChiTietPhieuNhapDAO;
import DAO.PhieuNhapDAO;
import DAO.SanPhamDAO;
import DTO.ChiTietPhieuDTO;
import DTO.ChiTietPhieuNhapDTO;
import DTO.SanPhamDTO;
import DTO.PhieuNhapDTO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class PhieuNhapBUS {

    public final PhieuNhapDAO phieunhapDAO = new PhieuNhapDAO();
    public final ChiTietPhieuNhapDAO ctPhieuNhapDAO = new ChiTietPhieuNhapDAO();

    NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    NhanVienBUS nvBUS = new NhanVienBUS();

    ArrayList<PhieuNhapDTO> listPhieuNhap;

    public PhieuNhapBUS() {
    }

    public ArrayList<PhieuNhapDTO> getAll() {
        this.listPhieuNhap = phieunhapDAO.selectAll();
        return this.listPhieuNhap;
    }

    public ArrayList<PhieuNhapDTO> getAllList() {
        return this.listPhieuNhap;
    }

    public ArrayList<PhieuNhapDTO> getAllByBranch(String mcn) {
        this.listPhieuNhap = phieunhapDAO.selectPhieuNhapByMCN(mcn);
        return this.listPhieuNhap;
    }

    public ArrayList<SanPhamDTO> convertHashMapToArray(HashMap<Integer, ArrayList<SanPhamDTO>> chitietsanpham) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        for (ArrayList<SanPhamDTO> ctsp : chitietsanpham.values()) {
            result.addAll(ctsp);
        }
        return result;
    }

    public ArrayList<ChiTietPhieuNhapDTO> getChiTietPhieu(int maphieunhap) {
        return ctPhieuNhapDAO.selectAll(Integer.toString(maphieunhap));
    }

    public ArrayList<ChiTietPhieuNhapDTO> getChiTietPhieu(int maphieunhap, String mcn) {
        return ctPhieuNhapDAO.selectAll(Integer.toString(maphieunhap), mcn);
    }

    public ArrayList<ChiTietPhieuDTO> getChiTietPhieu_Type(int maphieunhap) {
        ArrayList<ChiTietPhieuNhapDTO> arr = ctPhieuNhapDAO.selectAll(Integer.toString(maphieunhap));
        ArrayList<ChiTietPhieuDTO> result = new ArrayList<>();
        for (ChiTietPhieuDTO i : arr) {
            result.add(i);
        }
        return result;
    }

    public ArrayList<ChiTietPhieuDTO> getChiTietPhieu_Type(int maphieunhap, String mcn) {
        ArrayList<ChiTietPhieuNhapDTO> arr = ctPhieuNhapDAO.selectAll(Integer.toString(maphieunhap), mcn);
        ArrayList<ChiTietPhieuDTO> result = new ArrayList<>();
        for (ChiTietPhieuDTO i : arr) {
            result.add(i);
        }
        return result;
    }
    
    public ArrayList<ChiTietPhieuNhapDTO> selectCtpnByMaSanPham(String t) {
        return ctPhieuNhapDAO.selectCtpnByMaSanPham(t);
    }
    
    public int getGiaSanPhamByMaxMPN(String msp) {
        return ctPhieuNhapDAO.getGiaSanPhamByMaxMPN(msp);
    }

    public int getMPMAX() {
        ArrayList<PhieuNhapDTO> listPhieuNhap = phieunhapDAO.selectAll();
        int s = 1;
        for (PhieuNhapDTO i : listPhieuNhap) {
            if(i.getMP() > s) s = i.getMP();
        }
        return s;
    }

    public boolean add(PhieuNhapDTO phieu, ArrayList<ChiTietPhieuNhapDTO> ctPhieu, HashMap<Integer, ArrayList<SanPhamDTO>> chitietsanpham) {
        boolean check = phieunhapDAO.insert(phieu) != 0;
        if (check) {
            // ensure child detail records reference the newly generated parent MPN
            int parentMPN = phieu.getMP();
            for (ChiTietPhieuNhapDTO ct : ctPhieu) {
                ct.setMP(parentMPN);
            }
            check = ctPhieuNhapDAO.insertWithMCN(ctPhieu, phieu.getMCN()) != 0;
        }
        return check;
    }

    public ChiTietPhieuNhapDTO findCT(ArrayList<ChiTietPhieuNhapDTO> ctphieu, int masp) {
        ChiTietPhieuNhapDTO p = null;
        int i = 0;
        while (i < ctphieu.size() && p == null) {
            if (ctphieu.get(i).getMSP() == masp) {
                p = ctphieu.get(i);
            } else {
                i++;
            }
        }
        return p;
    }

    public long getTIEN(ArrayList<ChiTietPhieuNhapDTO> ctphieu) {
        long result = 0;
        for (ChiTietPhieuNhapDTO item : ctphieu) {
            result += item.getTIEN() * item.getSL();
        }
        return result;
    }

    public ArrayList<PhieuNhapDTO> fillerPhieuNhap(int type, String input, int mancc, int manv, Date time_s, Date time_e, String price_minnn, String price_maxxx) {
        Long price_min = !price_minnn.equals("") ? Long.valueOf(price_minnn) : 0L;
        Long price_max = !price_maxxx.equals("") ? Long.valueOf(price_maxxx) : Long.MAX_VALUE;
        Timestamp time_start = new Timestamp(time_s.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_e.getTime());
        // Đặt giá trị cho giờ, phút, giây và mili giây của Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Timestamp time_end = new Timestamp(calendar.getTimeInMillis());
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        for (PhieuNhapDTO phieuNhap : getAllList()) {
            boolean match = false;
            switch (type) {
                case 0 -> { 
                    if (Integer.toString(phieuNhap.getMP()).contains(input)
                            || nccBUS.getTenNhaCungCap(phieuNhap.getMNCC()).toLowerCase().contains(input)
                            || nvBUS.getNameById(phieuNhap.getMNV()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
                case 1 -> {
                    if (Integer.toString(phieuNhap.getMP()).contains(input)) {
                        match = true;
                    }
                }
                case 2 -> {
                    if (nccBUS.getTenNhaCungCap(phieuNhap.getMNCC()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
                case 3 -> {
                    if (nvBUS.getNameById(phieuNhap.getMNV()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
            }

            if (match
                    && (manv == 0 || phieuNhap.getMNV() == manv) && (mancc == 0 || phieuNhap.getMNCC() == mancc)
                    && (phieuNhap.getTG().compareTo(time_start) >= 0)
                    && (phieuNhap.getTG().compareTo(time_end) <= 0)
                    && phieuNhap.getTIEN() >= price_min
                    && phieuNhap.getTIEN() <= price_max) {
                result.add(phieuNhap);
            }
        }

        return result;
    }

    public boolean checkSLPn(int maphieu) {
        return phieunhapDAO.checkSLPn(maphieu);
    }

    public int cancelPhieuNhap(int maphieu) {
        // Lấy chi tiết phiếu nhập để hoàn trả số lượng (trừ đi)
        ArrayList<ChiTietPhieuNhapDTO> arrCt = ctPhieuNhapDAO.selectAll(Integer.toString(maphieu));
        for (ChiTietPhieuNhapDTO chiTiet : arrCt) {
            // TODO: Cập nhật TONKHO để hoàn trả số lượng
            // SanPhamDAO.getInstance().updateSoLuongTon(chiTiet.getMSP(), -(chiTiet.getSL()));
        }
        
        // Cập nhật trạng thái phiếu nhập thành 0 (đã hủy)
        int result = phieunhapDAO.cancel(maphieu);
        
        if (result > 0) {
            this.listPhieuNhap = phieunhapDAO.selectAll();
        }
        
        return result;
    }
    
    public ArrayList<ChiTietPhieuNhapDTO> searchByMSP(int msp) {
        return ctPhieuNhapDAO.searchByMSP(msp);
    }
    
    public ArrayList<PhieuNhapDTO> selectPhieuNhapByMNCC(int mncc) {
        return phieunhapDAO.selectPhieuNhapByMNCC(mncc);
    }
    public ArrayList<PhieuNhapDTO> search(String searchText, String searchType) {
    ArrayList<PhieuNhapDTO> result = new ArrayList<>();
    searchText = searchText.toLowerCase();  
    switch (searchType) {  
        case "Tất cả" -> {
            for (PhieuNhapDTO phieuNhap : getAllList()) {
                // Kiểm tra nếu Mã phiếu nhập, Tên nhà cung cấp hoặc Tên nhân viên chứa searchText
                if ((Integer.toString(phieuNhap.getMP()).contains(searchText)) ||
                    nccBUS.getTenNhaCungCap(phieuNhap.getMNCC()).toLowerCase().contains(searchText) ||
                    nvBUS.getNameById(phieuNhap.getMNV()).toLowerCase().contains(searchText)) {
                    result.add(phieuNhap);
                }
            }
        }
        case "Mã phiếu nhập" -> {
            for (PhieuNhapDTO phieuNhap : getAllList()) {
                // Kiểm tra nếu Mã phiếu nhập chứa searchText
                if (Integer.toString(phieuNhap.getMP()).contains(searchText)) {
                    result.add(phieuNhap);
                }
            }
        }
        case "Nhà cung cấp" -> {
            for (PhieuNhapDTO phieuNhap : getAllList()) {
                // Kiểm tra nếu Tên nhà cung cấp chứa searchText
                if (nccBUS.getTenNhaCungCap(phieuNhap.getMNCC()).toLowerCase().contains(searchText)) {
                    result.add(phieuNhap);
                }
            }
        }
        case "Nhân viên nhập" -> {
            for (PhieuNhapDTO phieuNhap : getAllList()) {
                // Kiểm tra nếu Tên nhân viên chứa searchText
                if (nvBUS.getNameById(phieuNhap.getMNV()).toLowerCase().contains(searchText)) {
                    result.add(phieuNhap);
                }
            }
        }
    }
    return result;
}


}
