package BUS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DAO.ChiTietPhieuXuatDAO;
// import DAO.SanPhamDAO;
import DAO.PhieuXuatDAO;
import DAO.TonKhoDAO;
import DTO.ChiTietPhieuDTO;
import DTO.ChiTietPhieuXuatDTO;
// import DTO.PhieuBaoHanhDTO;  // Xóa bảo hành
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;


public class PhieuXuatBUS {

    private final PhieuXuatDAO phieuXuatDAO = PhieuXuatDAO.getInstance();

    private final ChiTietPhieuXuatDAO chiTietPhieuXuatDAO = ChiTietPhieuXuatDAO.getInstance();
    private final TonKhoDAO tonKhoDAO = new TonKhoDAO();
    private ArrayList<PhieuXuatDTO> listPhieuXuat;

    NhanVienBUS nvBUS = new NhanVienBUS();
    KhachHangBUS khBUS = new KhachHangBUS();

    SanPhamBUS spBUS = new SanPhamBUS();

    public PhieuXuatBUS() {
        this.listPhieuXuat = phieuXuatDAO.selectAll();
    }

    public ArrayList<PhieuXuatDTO> getAll() {
        this.listPhieuXuat = phieuXuatDAO.selectAll();
        return this.listPhieuXuat;
    }

    public ArrayList<PhieuXuatDTO> getAll(int mkh) {
        this.listPhieuXuat = phieuXuatDAO.selectByMKH(mkh+"");
        
        return this.listPhieuXuat;
    }

    public ArrayList<PhieuXuatDTO> getAllByBranch(String mcn) {
        this.listPhieuXuat = phieuXuatDAO.selectPhieuXuatByMCN(mcn);
        return this.listPhieuXuat;
    }

    public PhieuXuatDTO getSelect(int index) {
        return listPhieuXuat.get(index);
    }

    public boolean cancel(int maphieu, String lydohuy) {
        // Lấy chi tiết phiếu xuất để hoàn trả số lượng
        ArrayList<ChiTietPhieuXuatDTO> chiTiet = chiTietPhieuXuatDAO.selectAll(String.valueOf(maphieu));
        
        // Hoàn trả số lượng sản phẩm vào kho
        for (ChiTietPhieuXuatDTO ct : chiTiet) {
            // TODO: Cập nhật TONKHO để hoàn trả số lượng
            // spBUS.spDAO.updateSoLuongTon(ct.getMSP(), ct.getSL()); // Cộng lại số lượng đã xuất
        }
        
        // Cập nhật trạng thái phiếu xuất thành 0 (đã hủy)
        int result = phieuXuatDAO.cancel(maphieu, lydohuy);
        
        if (result > 0) {
            // Cập nhật lại danh sách
            this.listPhieuXuat = phieuXuatDAO.selectAll();
            return true;
        }
        return false;
    }

    public void remove(int px) {
        listPhieuXuat.remove(px);
    }

    public void update(PhieuXuatDTO px) {
        phieuXuatDAO.update(px);
        chiTietPhieuXuatDAO.updateSL(px.getMP()+"");
    }

    public void insert(PhieuXuatDTO px, ArrayList<ChiTietPhieuXuatDTO> ct, String mcn) {
        // Bước 1: Insert phiếu xuất vào database và lấy ID vừa tạo
        int mpxMoi = phieuXuatDAO.insertReturnId(px);
        
        if (mpxMoi > 0) {
            // Cập nhật MPX cho tất cả chi tiết phiếu xuất
            for (ChiTietPhieuXuatDTO chiTiet : ct) {
                chiTiet.setMP(mpxMoi);
            }
            
            // Bước 2: Insert chi tiết phiếu xuất và cập nhật tồn kho
            chiTietPhieuXuatDAO.insertAndUpdateTonKho(ct, mcn);
            
            // Bước 3: Tự động tạo phiếu bảo hành cho mỗi sản phẩm
          
        }
    }
    
    /**
     * Tạo phiếu bảo hành tự động cho các sản phẩm trong phiếu xuất
     * @param px Phiếu xuất
     * @param ct Danh sách chi tiết phiếu xuất
     */
  
    public ArrayList<ChiTietPhieuXuatDTO> selectCTP(int maphieu) {
        return chiTietPhieuXuatDAO.selectAll(Integer.toString(maphieu));
    }

    public ArrayList<ChiTietPhieuDTO> getChiTietPhieu_Type(int maphieunhap) {
        ArrayList<ChiTietPhieuXuatDTO> arr = chiTietPhieuXuatDAO.selectAll(Integer.toString(maphieunhap));
        ArrayList<ChiTietPhieuDTO> result = new ArrayList<>();
        for (ChiTietPhieuDTO i : arr) {
            result.add(i);
        }
        return result;
    }

    public int getMPMAX() {
        ArrayList<PhieuXuatDTO> listPhieuXuat = phieuXuatDAO.selectAll();
        int s = 1;
        for (PhieuXuatDTO i : listPhieuXuat) {
            if(i.getMP() > s) s = i.getMP();
        }
        return s;
    }
    //moi them, nó tương tự insert nhưng có check
    // public boolean add(PhieuXuatDTO phieu, ArrayList<ChiTietHoaDonDTO> ctPhieu, HashMap<Integer, ArrayList<SanPhamDTO>> chitietsanpham) {
    //     boolean check = phieuXuatDAO.insert(phieu) != 0;
    //     if (check) {
    //         check = chiTietPhieuXuatDAO.insert(ctPhieu) != 0;
    //     }
    //     return check;
    // }

    public ChiTietPhieuXuatDTO findCT(ArrayList<ChiTietPhieuXuatDTO> ctphieu, int masp) {
        ChiTietPhieuXuatDTO p = null;
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

    public ArrayList<PhieuXuatDTO> fillerPhieuXuat(int type, String input, int makh, int manv, Date time_s, Date time_e, String price_minnn, String price_maxxx, String mcn) {
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

        ArrayList<PhieuXuatDTO> result = new ArrayList<>();
        ArrayList<PhieuXuatDTO> source = (mcn == null || mcn.isEmpty()) ? getAll() : getAllByBranch(mcn);
        for (PhieuXuatDTO phieuXuat : source) {
            boolean match = false;
            switch (type) {
                case 0 -> {
                    if (Integer.toString(phieuXuat.getMP()).contains(input)
                            || khBUS.getTenKhachHang(phieuXuat.getMKH()).toLowerCase().contains(input)
                            || nvBUS.getNameById(phieuXuat.getMNV()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
                case 1 -> {
                    if (Integer.toString(phieuXuat.getMP()).contains(input)) {
                        match = true;
                    }
                }
                case 2 -> {
                    if (khBUS.getTenKhachHang(phieuXuat.getMKH()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
                case 3 -> {
                    if (nvBUS.getNameById(phieuXuat.getMNV()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
            }

            if (match
                    && (manv == 0 || phieuXuat.getMNV() == manv) && (makh == 0 || phieuXuat.getMKH() == makh)
                    && (phieuXuat.getTG().compareTo(time_start) >= 0)
                    && (phieuXuat.getTG().compareTo(time_end) <= 0)
                    && phieuXuat.getTIEN() >= price_min
                    && phieuXuat.getTIEN() <= price_max) {
                result.add(phieuXuat);
            }
        }

        return result;
    }

    public String[] getArrMPX() {
        int size = listPhieuXuat.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = Integer.toString(listPhieuXuat.get(i).getMP());
        }
        return result;
    }

    public PhieuXuatDTO getByIndex(int index) {
        return this.listPhieuXuat.get(index);
    }

    public boolean checkSLPx(ArrayList <ChiTietPhieuXuatDTO> listctpx, String mcn) {
        for(ChiTietPhieuXuatDTO ct : listctpx) {
            // Lấy tồn kho của sản phẩm theo chi nhánh
            int slTonChiNhanh = tonKhoDAO.getTonKhoByMSPAndMCN(ct.getMSP(), mcn);
            if(ct.getSL() > slTonChiNhanh) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSLPx(int maphieu) {
        return phieuXuatDAO.checkSLPx(maphieu);
    }

    public int cancelPhieuNhap(int maphieu) {
        return phieuXuatDAO.cancelPHIEUXUAT(maphieu);
    }

    public ArrayList<ChiTietPhieuXuatDTO> searchByMSP(int msp) {
        return chiTietPhieuXuatDAO.searchByMSP(msp);
    }
}
