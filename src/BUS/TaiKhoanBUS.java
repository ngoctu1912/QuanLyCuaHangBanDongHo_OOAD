package BUS;

import DAO.NhomQuyenDAO;
import DAO.TaiKhoanDAO;
import DTO.NhomQuyenDTO;
import DTO.TaiKhoanDTO;
import java.util.ArrayList;

public class TaiKhoanBUS {
    private ArrayList<TaiKhoanDTO> listTaiKhoan;
    private ArrayList<TaiKhoanDTO> listTaikhoanKH;
    private NhomQuyenDAO nhomQuyenDAO = NhomQuyenDAO.getInstance();
    
    public TaiKhoanBUS(){
        this.listTaiKhoan  = TaiKhoanDAO.getInstance().selectAll();
    }
    
    public ArrayList<TaiKhoanDTO> getTaiKhoanAll(){
        this.listTaiKhoan  = TaiKhoanDAO.getInstance().selectAll();
        return listTaiKhoan;
    }
    
    public TaiKhoanDTO getTaiKhoan(int index){
        return listTaiKhoan.get(index);
    }
    public int getTaiKhoanByMaNV(int manv){
        int i = 0;
        int vitri = -1;
        while (i < this.listTaiKhoan.size() && vitri == -1) {
            if (listTaiKhoan.get(i).getMNV()== manv) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public int getTaiKhoanByMaKH(int manv){
        int i = 0;
        int vitri = -1;
        while (i < this.listTaikhoanKH.size() && vitri == -1) {
            if (listTaikhoanKH.get(i).getMNV()== manv) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
    
    public NhomQuyenDTO getNhomQuyenDTO(int manhom){
        return nhomQuyenDAO.selectById(manhom+"");
    }
    
    public void addAcc(TaiKhoanDTO tk){
        TaiKhoanDAO.getInstance().insert(tk);
    }
    

    public void updateAcc(TaiKhoanDTO tk){
        TaiKhoanDAO.getInstance().update(tk);
    }


    public boolean checkTDN(String TDN){
        TaiKhoanDTO tk = TaiKhoanDAO.getInstance().selectByUser(TDN);
        if(tk != null) return false;
        return true;
    }
    
    public void deleteAcc(int manv){
        
    }
    public ArrayList<TaiKhoanDTO> search(String txt, String type) {
        ArrayList<TaiKhoanDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (TaiKhoanDTO i : listTaiKhoan) {
                    System.out.println(i.getTDN());
                    if ((i.getMNV()+"").contains(txt)||i.getTDN().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhân viên" -> {
                for (TaiKhoanDTO i : listTaiKhoan) {
                    if (Integer.toString(i.getMNV()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Tên đăng nhập" -> {
                for (TaiKhoanDTO i : listTaiKhoan) {
                    if (i.getTDN().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<TaiKhoanDTO> searchKH(String txt, String type) {
        ArrayList<TaiKhoanDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (TaiKhoanDTO i : listTaikhoanKH) {
                    if (Integer.toString(i.getMNV()).contains(txt) || i.getTDN().contains(txt) ) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhân viên" -> {
                for (TaiKhoanDTO i : listTaikhoanKH) {
                    if (Integer.toString(i.getMNV()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Username" -> {
                for (TaiKhoanDTO i : listTaikhoanKH) {
                    if (i.getTDN().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }
    public void doiMatKhau(int id, String newPassword) {
        TaiKhoanDAO.getInstance().updatePassByMNV(id, newPassword);
    }
    public TaiKhoanDTO findTaiKhoanByMaNhanVien(int maNhanVien) {
    for (TaiKhoanDTO tk : getTaiKhoanAll()) {
        if (tk.getMNV() == maNhanVien) {
            return tk;
        }
    }
    return null; 
}
    public ArrayList<TaiKhoanDTO> getTaiKhoanExcludingAdmin() {
        return TaiKhoanDAO.getInstance().selectAllExcludingAdmin();
    }
    public boolean KT(String userName){
        return TaiKhoanDAO.getInstance().isAccountInactive(userName);
    }

}
