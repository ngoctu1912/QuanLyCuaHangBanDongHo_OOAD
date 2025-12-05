package BUS;

import DAO.DonViDAO;
import DTO.DonViDTO;
import java.util.ArrayList;

public class DonViBUS {

    private DonViDAO mausacDAO = new DonViDAO();
    public ArrayList<DonViDTO> listDonVi = new ArrayList<>();

    public DonViBUS() {
        this.listDonVi = mausacDAO.selectAll();
    }

    public ArrayList<DonViDTO> getAll() {
        return this.listDonVi;
    }

    public ArrayList<DonViDTO> getAlll() {
        return mausacDAO.getAll();
    }

    public String[] getArrTenDonVi() {
        String[] result = new String[listDonVi.size()];
        for (int i = 0; i < listDonVi.size(); i++) {
            result[i] = listDonVi.get(i).getTENDV();
        }
        return result;
    }

    public DonViDTO getByIndex(int index) {
        return this.listDonVi.get(index);
    }
    
    public DonViDTO getById(String id) {
        return mausacDAO.selectById(id);
    }

    public DonViDTO getByName(String name) {
        return mausacDAO.getByName(name);
    }
    
    public boolean add(DonViDTO msac) {
        boolean check = mausacDAO.insert(msac) != 0;
        if (check) {
            this.listDonVi.add(msac);
        }
        return check;
    }

    public boolean delete(DonViDTO msac, int index) {
        boolean check = mausacDAO.delete(Integer.toString(msac.getMDV())) != 0;
        if (check) {
            this.listDonVi.remove(index);
        }
        return check;
    }

    public int getIndexByMaMau(int mamau) {
        int i = 0;
        int vitri = -1;
        while (i < this.listDonVi.size() && vitri == -1) {
            if (listDonVi.get(i).getMDV() == mamau) {
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
        return this.listDonVi.get(index).getTENDV();
    }

    public boolean update(DonViDTO msac) {
        boolean check = mausacDAO.update(msac) != 0;
        if (check) {
            this.listDonVi.set(getIndexByMaMau(msac.getMDV()), msac);
        }
        return check;
    }

    public boolean checkDup(String name) {
        boolean check = true;
        int i = 0;
        while (i < this.listDonVi.size() && check == true) {
            if (this.listDonVi.get(i).getTENDV().toLowerCase().contains(name.toLowerCase())) {
                check = false;
            } else {
                i++;
            }
        }
        return check;
    }

    public int getMaxMaDonVi() {
        return mausacDAO.getMaxMaDonVi();
    }
}
