package BUS;

import DAO.LoaiDAO;
import DTO.LoaiDTO;
import java.util.ArrayList;

public class LoaiBUS {

    private LoaiDAO mausacDAO = new LoaiDAO();
    public ArrayList<LoaiDTO> listLoai = new ArrayList<>();

    public LoaiBUS() {
        this.listLoai = mausacDAO.selectAll();
    }

    public ArrayList<LoaiDTO> getAll() {
        return this.listLoai;
    }

    public ArrayList<LoaiDTO> getAlll() {
        return mausacDAO.getAll();
    }

    public String[] getArrTenLoai() {
        String[] result = new String[listLoai.size()];
        for (int i = 0; i < listLoai.size(); i++) {
            result[i] = listLoai.get(i).getTENL();
        }
        return result;
    }

    public LoaiDTO getByIndex(int index) {
        return this.listLoai.get(index);
    }
    
    public LoaiDTO getById(String id) {
        return mausacDAO.selectById(id);
    }
    
    public LoaiDTO getByName(String name) {
        return mausacDAO.getByName(name);
    }

    public boolean add(LoaiDTO msac) {
        boolean check = mausacDAO.insert(msac) != 0;
        if (check) {
            this.listLoai.add(msac);
        }
        return check;
    }

    public boolean delete(LoaiDTO msac, int index) {
        boolean check = mausacDAO.delete(Integer.toString(msac.getML())) != 0;
        if (check) {
            this.listLoai.remove(index);
        }
        return check;
    }

    public int getIndexByMaMau(int mamau) {
        int i = 0;
        int vitri = -1;
        while (i < this.listLoai.size() && vitri == -1) {
            if (listLoai.get(i).getML() == mamau) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public String getTenMau(int mamau) {
        int index = this.getIndexByMaMau(mamau);
        System.out.println(index);
        return this.listLoai.get(index).getTENL();
    }

    public boolean update(LoaiDTO msac) {
        boolean check = mausacDAO.update(msac) != 0;
        if (check) {
            this.listLoai.set(getIndexByMaMau(msac.getML()), msac);
        }
        return check;
    }

    public boolean checkDup(String name) {
        boolean check = true;
        int i = 0;
        while (i < this.listLoai.size() && check == true) {
            if (this.listLoai.get(i).getTENL().toLowerCase().contains(name.toLowerCase())) {
                check = false;
            } else {
                i++;
            }
        }
        return check;
    }

    public int getMaxMaLoai() {
        return mausacDAO.getMaxMaLoai();
    }
}
