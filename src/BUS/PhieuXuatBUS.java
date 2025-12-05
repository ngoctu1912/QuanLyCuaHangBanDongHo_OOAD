package BUS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DAO.ChiTietPhieuXuatDAO;
// import DAO.SanPhamDAO;
import DAO.PhieuXuatDAO;
import DTO.ChiTietPhieuDTO;
import DTO.ChiTietPhieuXuatDTO;
import DTO.PhieuBaoHanhDTO;
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;


public class PhieuXuatBUS {

    private final PhieuXuatDAO phieuXuatDAO = PhieuXuatDAO.getInstance();

    private final ChiTietPhieuXuatDAO chiTietPhieuXuatDAO = ChiTietPhieuXuatDAO.getInstance();
    private ArrayList<PhieuXuatDTO> listPhieuXuat;

    NhanVienBUS nvBUS = new NhanVienBUS();
    KhachHangBUS khBUS = new KhachHangBUS();
    PhieuBaoHanhBUS pbhBUS = new PhieuBaoHanhBUS();
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

    public PhieuXuatDTO getSelect(int index) {
        return listPhieuXuat.get(index);
    }

    public boolean cancel(int maphieu, String lydohuy) {
        // Lấy chi tiết phiếu xuất để hoàn trả số lượng
        ArrayList<ChiTietPhieuXuatDTO> chiTiet = chiTietPhieuXuatDAO.selectAll(String.valueOf(maphieu));
        
        // Hoàn trả số lượng sản phẩm vào kho
        for (ChiTietPhieuXuatDTO ct : chiTiet) {
            spBUS.spDAO.updateSoLuongTon(ct.getMSP(), ct.getSL()); // Cộng lại số lượng đã xuất
        }
        
        // Cập nhật trạng thái phiếu xuất thành 0 (đã hủy)
        int result = phieuXuatDAO.cancel(maphieu, lydohuy);
        
        if (result > 0) {
            // Cập nhật trạng thái phiếu bảo hành thành 2 (đã hủy)
            pbhBUS.updateTrangThaiByMaHoaDon(maphieu, 2);
            
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

    public void insert(PhieuXuatDTO px, ArrayList<ChiTietPhieuXuatDTO> ct) {
        // Bước 1: Insert phiếu xuất vào database và lấy ID vừa tạo
        int mhdMoi = phieuXuatDAO.insertReturnId(px);
        
        if (mhdMoi > 0) {
            // Cập nhật MHD cho tất cả chi tiết phiếu xuất
            for (ChiTietPhieuXuatDTO chiTiet : ct) {
                chiTiet.setMP(mhdMoi);
            }
            
            // Bước 2: Insert chi tiết phiếu xuất (cập nhật số lượng tồn)
            chiTietPhieuXuatDAO.insert(ct);
            
            // Bước 3: Tự động tạo phiếu bảo hành cho mỗi sản phẩm
            createPhieuBaoHanhFromPhieuXuat(px, ct);
        }
    }
    
    /**
     * Tạo phiếu bảo hành tự động cho các sản phẩm trong phiếu xuất
     * @param px Phiếu xuất
     * @param ct Danh sách chi tiết phiếu xuất
     */
    private void createPhieuBaoHanhFromPhieuXuat(PhieuXuatDTO px, ArrayList<ChiTietPhieuXuatDTO> ct) {
        java.sql.Date ngayBatDau = new java.sql.Date(px.getTG().getTime());
        
        for (ChiTietPhieuXuatDTO chiTiet : ct) {
            // Lấy thông tin sản phẩm để biết thời gian bảo hành
            SanPhamDTO sanPham = spBUS.getByMaSP(chiTiet.getMSP());
            
            if (sanPham != null && sanPham.getTHOIGIANBAOHANH() > 0) {
                // Tính ngày kết thúc bảo hành (thêm số tháng bảo hành)
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ngayBatDau);
                calendar.add(Calendar.MONTH, sanPham.getTHOIGIANBAOHANH());
                java.sql.Date ngayKetThuc = new java.sql.Date(calendar.getTimeInMillis());
                
                // Tạo phiếu bảo hành cho từng sản phẩm (số lượng tạo bằng số lượng trong chi tiết)
                for (int i = 0; i < chiTiet.getSL(); i++) {
                    PhieuBaoHanhDTO pbh = new PhieuBaoHanhDTO(
                        px.getMP(),              // Mã hóa đơn (mã phiếu xuất)
                        chiTiet.getMSP(),        // Mã sản phẩm
                        px.getMKH(),             // Mã khách hàng
                        ngayBatDau,              // Ngày bắt đầu
                        ngayKetThuc,             // Ngày kết thúc
                        1                        // Trạng thái: 1 = còn hạn
                    );
                    
                    pbhBUS.add(pbh);
                }
            }
        }
    }

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

    public ArrayList<PhieuXuatDTO> fillerPhieuXuat(int type, String input, int makh, int manv, Date time_s, Date time_e, String price_minnn, String price_maxxx) {
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
        for (PhieuXuatDTO phieuXuat : getAll()) {
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

    public boolean checkSLPx(ArrayList <ChiTietPhieuXuatDTO> listctpx) {
        SanPhamBUS spBus = new SanPhamBUS();
        ArrayList<SanPhamDTO> SP = new ArrayList<SanPhamDTO>();
        for(ChiTietPhieuXuatDTO i : listctpx) SP.add(spBus.spDAO.selectById(i.getMSP() + ""));
        for (int i = 0; i < SP.size(); i++) {
            if(listctpx.get(i).getSL() > SP.get(i).getSL()){
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
