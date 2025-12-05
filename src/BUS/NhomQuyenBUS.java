package BUS;

import DAO.ChiTietQuyenDAO;
import DAO.NhomQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.NhomQuyenDTO;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class NhomQuyenBUS {

    private final NhomQuyenDAO nhomquyenDAO = new NhomQuyenDAO();
    private final ChiTietQuyenDAO chitietquyenDAO = new ChiTietQuyenDAO();
    private final ArrayList<NhomQuyenDTO> listNhomQuyen;

    public NhomQuyenBUS() {
        this.listNhomQuyen = nhomquyenDAO.selectAll();
    }

    public ArrayList<NhomQuyenDTO> getAll() {
        return this.listNhomQuyen;
    }

    public NhomQuyenDTO getByIndex(int index) {
        return this.listNhomQuyen.get(index);
    }

    public boolean add(String nqdto, ArrayList<ChiTietQuyenDTO> ctquyen) {
        NhomQuyenDTO nq = new NhomQuyenDTO(nhomquyenDAO.getAutoIncrement(), nqdto);
        boolean check = nhomquyenDAO.insert(nq) != 0;
        if (check) {
            this.listNhomQuyen.add(nq);
            this.addChiTietQuyen(ctquyen);
        }
        return check;
    }

    public boolean update(NhomQuyenDTO nhomquyen, ArrayList<ChiTietQuyenDTO> chitietquyen,int index) {
        boolean check = nhomquyenDAO.update(nhomquyen) != 0;
        if (check) {
            this.removeChiTietQuyen(Integer.toString(nhomquyen.getManhomquyen()));
            this.addChiTietQuyen(chitietquyen);
            this.listNhomQuyen.set(index, nhomquyen);
        }
        return check;
    }

//    public boolean delete(NhomQuyenDTO nqdto) {
//        boolean check = nhomquyenDAO.delete(Integer.toString(nqdto.getManhomquyen())) != 0;
//        if (check) {
//            this.listNhomQuyen.remove(nqdto);
//        }
//        return check;
//    }
    public boolean delete(NhomQuyenDTO nq) {
    // Kiểm tra xem nhóm quyền có liên kết với tài khoản không
    boolean isLinked = nhomquyenDAO.isLinkedToAccount(nq.getManhomquyen());
    if (isLinked) {
        // Nếu có tài khoản liên kết, không cho phép xóa
        JOptionPane.showMessageDialog(null, "Không thể xóa nhóm quyền này vì nó đang được liên kết với một tài khoản.");
        return false;
    }
    
    // Nếu không có tài khoản liên kết, thực hiện xóa
    boolean check = nhomquyenDAO.delete(Integer.toString(nq.getManhomquyen())) != 0;
    if (check) {
        this.listNhomQuyen.remove(nq); // Xóa nhóm quyền khỏi danh sách trong bộ nhớ
    }
    return check;
}


    public ArrayList<ChiTietQuyenDTO> getChiTietQuyen(String manhomquyen) {
        return chitietquyenDAO.selectAll(manhomquyen);
    }

    public boolean addChiTietQuyen(ArrayList<ChiTietQuyenDTO> listctquyen) {
        boolean check = chitietquyenDAO.insert(listctquyen) != 0;
        return check;
    }

    public boolean removeChiTietQuyen(String manhomquyen) {
        boolean check = chitietquyenDAO.delete(manhomquyen) != 0;
        return check;
    }

    public boolean checkPermisson(int maquyen, String chucnang, String hanhdong) {
        ArrayList<ChiTietQuyenDTO> ctquyen = this.getChiTietQuyen(Integer.toString(maquyen));
        boolean check = false;
        int i = 0;
        while (i < ctquyen.size() && check==false) {
            if(ctquyen.get(i).getMachucnang().equals(chucnang) && ctquyen.get(i).getHanhdong().equals(hanhdong)) {
                check = true;
            } else {
                i++;
            }
        }
        return check;
    }
    
    public ArrayList<NhomQuyenDTO> search(String text) {
        ArrayList<NhomQuyenDTO> result = new ArrayList<>();
        for(NhomQuyenDTO i : this.listNhomQuyen) {
            if(Integer.toString(i.getManhomquyen()).contains(text) || i.getTennhomquyen().toLowerCase().contains(text.toLowerCase())) {
                result.add(i);
            }
        }
        return result;
    }
    public boolean isDuplicateName(String tennhomquyen) {
    for (NhomQuyenDTO nhom : this.listNhomQuyen) {
        if (nhom.getTennhomquyen().equalsIgnoreCase(tennhomquyen)) {
            return true;
        }
    }
    return false; 
}

}
