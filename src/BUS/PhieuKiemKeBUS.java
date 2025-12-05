package BUS;

import DAO.ChiTietKiemKeDAO;
import DAO.PhieuKiemKeDAO;
import DTO.ChiTietKiemKeDTO;
import DTO.PhieuKiemKeDTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class PhieuKiemKeBUS {
    
    private PhieuKiemKeDAO phieuKiemKeDAO = PhieuKiemKeDAO.getInstance();
    private ChiTietKiemKeDAO chiTietKiemKeDAO = ChiTietKiemKeDAO.getInstance();
    private NhanVienBUS nvBUS = new NhanVienBUS();
    private ArrayList<PhieuKiemKeDTO> danhSachPhieu;
    
    public PhieuKiemKeBUS(){
        danhSachPhieu = phieuKiemKeDAO.selectAll();
    }
    
    public void insert(PhieuKiemKeDTO phieuKiemKeDTO, ArrayList<ChiTietKiemKeDTO> dsPhieu){
        phieuKiemKeDAO.insert(phieuKiemKeDTO);
        chiTietKiemKeDAO.insert(dsPhieu);
    }

    public ArrayList<PhieuKiemKeDTO> getDanhSachPhieu() {
        return phieuKiemKeDAO.selectAll();
    }

    public void setDanhSachPhieu(ArrayList<PhieuKiemKeDTO> danhSachPhieu) {
        this.danhSachPhieu = danhSachPhieu;
    }
    
    public ArrayList<ChiTietKiemKeDTO> getChiTietPhieu(int maphieunhap) {
        return chiTietKiemKeDAO.selectAll(Integer.toString(maphieunhap));
    }
    
    public int getAutoIncrement(){
        return phieuKiemKeDAO.getAutoIncrement();
    }
    
    public ArrayList<PhieuKiemKeDTO> selectAll(){
        return phieuKiemKeDAO.selectAll();
    }
    
    public void cancel(int index){
        PhieuKiemKeDTO phieuKiemKeDTO = danhSachPhieu.get(index);
        chiTietKiemKeDAO.delete(phieuKiemKeDTO.getMaphieukiemke()+"");
        phieuKiemKeDAO.delete(phieuKiemKeDTO.getMaphieukiemke()+"");
        danhSachPhieu.remove(index);
    }
    
    public ArrayList<ChiTietKiemKeDTO> getChitietTiemKe(int maphieu){
        return chiTietKiemKeDAO.selectAll(maphieu+"");
    }
    
    public ArrayList<PhieuKiemKeDTO> fillerPhieuKiemKe(int type, String input, int manv, Date time_s, Date time_e) {
        Timestamp time_start = new Timestamp(time_s.getTime());
        Timestamp time_end = new Timestamp(time_e.getTime());
        ArrayList<PhieuKiemKeDTO> result = new ArrayList<>();
        for (PhieuKiemKeDTO phieuKiemKe : getDanhSachPhieu()) {
            boolean match = false;
            switch (type) {
                case 0 -> {
                    if (Integer.toString(phieuKiemKe.getMaphieukiemke()).contains(input)
                            || nvBUS.getNameById(phieuKiemKe.getNguoitao()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
                case 1 -> {
                    if (Integer.toString(phieuKiemKe.getMaphieukiemke()).contains(input)) {
                        match = true;
                    }
                }
                case 2 -> {
                    if (nvBUS.getNameById(phieuKiemKe.getNguoitao()).toLowerCase().contains(input)) {
                        match = true;
                    }
                }
            }
            if (match
                    && (manv == 0 || phieuKiemKe.getNguoitao()==manv)
                    && (phieuKiemKe.getThoigiantao().compareTo(time_start) >= 0)
                    && (phieuKiemKe.getThoigiantao().compareTo(time_end) <= 0)
                    ) {
                result.add(phieuKiemKe);
            }
        }
        return result;
    }

    public ChiTietKiemKeDTO findCT(ArrayList<ChiTietKiemKeDTO> ctphieu, int masp) {
        ChiTietKiemKeDTO p = null;
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

    public boolean add(PhieuKiemKeDTO phieu, ArrayList<ChiTietKiemKeDTO> ctPhieu) {
        boolean check = phieuKiemKeDAO.insert(phieu) != 0;
        if (check) {
            check = chiTietKiemKeDAO.insert(ctPhieu) != 0;
        }
        return check;
    }
}

