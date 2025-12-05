package BUS;

import java.util.ArrayList;

import DAO.KhachHangDAO;
import DTO.KhachHangDTO;

public class KhachHangBUS {

    private final KhachHangDAO khDAO = new KhachHangDAO();
    public ArrayList<KhachHangDTO> listKhachHang = new ArrayList<>();

    public KhachHangBUS() {
        listKhachHang = khDAO.selectAll();
    }

    public ArrayList<KhachHangDTO> getAll() {
        return this.khDAO.selectAll();
    }

    public KhachHangDTO getByIndex(int index) {
        return this.listKhachHang.get(index);
    }

    public int getByTen(String index) {
        int i = 0;
        int vitri = -1;
        while (i < this.listKhachHang.size() && vitri == -1) {
            if (listKhachHang.get(i).getHOTEN().equals(index)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public int getIndexByMaDV(int makhachhang) {
        int i = 0;
        int vitri = -1;
        while (i < this.listKhachHang.size() && vitri == -1) {
            if (listKhachHang.get(i).getMKH() == makhachhang) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public Boolean add(KhachHangDTO kh) {
        boolean check = khDAO.insert(kh) != 0;
        if (check) {
            this.listKhachHang.add(kh);
        }
        return check;
    }

    public Boolean delete(KhachHangDTO kh) {
        boolean check = khDAO.delete(Integer.toString(kh.getMKH())) != 0;
        if (check) {
            this.listKhachHang.remove(getIndexByMaDV(kh.getMKH()));
        }
        return check;
    }

    public Boolean update(KhachHangDTO kh) {
        boolean check = khDAO.update(kh) != 0;
        if (check) {
            this.listKhachHang.set(getIndexByMaDV(kh.getMKH()), kh);
        }
        return check;
    }

    public ArrayList<KhachHangDTO> search(String text, String type) {
        ArrayList<KhachHangDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (KhachHangDTO i : this.listKhachHang) {
                    if (Integer.toString(i.getMKH()).toLowerCase().contains(text) || i.getHOTEN().toLowerCase().contains(text) || i.getDIACHI().toLowerCase().contains(text) || i.getSDT().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã khách hàng" -> {
                for (KhachHangDTO i : this.listKhachHang) {
                    if (Integer.toString(i.getMKH()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Tên khách hàng" -> {
                for (KhachHangDTO i : this.listKhachHang) {
                    if (i.getHOTEN().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Địa chỉ" -> {
                for (KhachHangDTO i : this.listKhachHang) {
                    if (i.getDIACHI().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Số điện thoại" -> {
                for (KhachHangDTO i : this.listKhachHang) {
                    if (i.getSDT().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
        }

        return result;
    }

    public String getTenKhachHang(int makh) {
        String name = "";
        for (KhachHangDTO khachHangDTO : listKhachHang) {
            if (khachHangDTO.getMKH() == makh) {
                name = khachHangDTO.getHOTEN();
            }
        }
        return name;
    }

    public String[] getArrTenKhachHang() {
        int size = listKhachHang.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listKhachHang.get(i).getHOTEN();
        }
        return result;
    }

    public KhachHangDTO selectKh(int makh) {
        return khDAO.selectById(makh + "");
    }

    public int getMKHMAX() {
        int s = 1;
        listKhachHang = khDAO.selectAlll();
        for (KhachHangDTO i : listKhachHang) {
            if(i.getMKH() > s) s = i.getMKH();
        }
        return s;
    }
    public int total(){
        return khDAO.countAllRecords();
    }
    public KhachHangDTO getKhachHangById(int makhachhang) {
    for (KhachHangDTO kh : this.listKhachHang) {
        if (kh.getMKH() == makhachhang) {
            return kh;  // Trả về đối tượng khách hàng nếu tìm thấy
        }
    }
    return null;  // Trả về null nếu không tìm thấy khách hàng
}


    public KhachHangDTO selectById(String t) {
        return khDAO.selectById(t);
    }
}
