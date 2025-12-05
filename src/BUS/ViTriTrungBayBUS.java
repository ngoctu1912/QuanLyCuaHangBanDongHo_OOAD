package BUS;

import java.util.ArrayList;

import DAO.ViTriTrungBayDAO;
import DTO.ViTriTrungBayDTO;

public class ViTriTrungBayBUS {
    
    private final ViTriTrungBayDAO vtDAO = new ViTriTrungBayDAO();
    private ArrayList<ViTriTrungBayDTO> listViTriTrungBay = new ArrayList<>();

    public ViTriTrungBayBUS() {
        listViTriTrungBay = vtDAO.selectAll();
    }

    public ArrayList<ViTriTrungBayDTO> getAll() {
        return this.listViTriTrungBay;
    }

    public ViTriTrungBayDTO getByIndex(int index) {
        return this.listViTriTrungBay.get(index);
    }

    public ViTriTrungBayDTO getByMaViTri(int mvt) {
        for (ViTriTrungBayDTO vt : listViTriTrungBay) {
            if (vt.getMVT() == mvt) {
                return vt;
            }
        }
        return null;
    }

    public int getIndexByMaViTri(int mvt) {
        int i = 0;
        int vitri = -1;
        while (i < this.listViTriTrungBay.size() && vitri == -1) {
            if (listViTriTrungBay.get(i).getMVT() == mvt) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public Boolean add(ViTriTrungBayDTO vt) {
        boolean check = vtDAO.insert(vt) != 0;
        if (check) {
            this.listViTriTrungBay.add(vt);
        }
        return check;
    }

    public Boolean delete(ViTriTrungBayDTO vt) {
        boolean check = vtDAO.delete(Integer.toString(vt.getMVT())) != 0;
        if (check) {
            this.listViTriTrungBay.remove(vt);
        }
        return check;
    }

    public Boolean update(ViTriTrungBayDTO vt) {
        boolean check = vtDAO.update(vt) != 0;
        if (check) {
            this.listViTriTrungBay.set(getIndexByMaViTri(vt.getMVT()), vt);
        }
        return check;
    }

    public ArrayList<ViTriTrungBayDTO> search(String text, String type) {
        ArrayList<ViTriTrungBayDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (ViTriTrungBayDTO i : listViTriTrungBay) {
                    if (Integer.toString(i.getMVT()).toLowerCase().contains(text) || 
                        i.getTEN().toLowerCase().contains(text) ||
                        (i.getGHICHU() != null && i.getGHICHU().toLowerCase().contains(text))) {
                        result.add(i);
                    }
                }
            }
            case "Mã vị trí" -> {
                for (ViTriTrungBayDTO i : listViTriTrungBay) {
                    if (Integer.toString(i.getMVT()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Tên vị trí" -> {
                for (ViTriTrungBayDTO i : listViTriTrungBay) {
                    if (i.getTEN().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public String[] getArrTenViTri() {
        String[] result = new String[listViTriTrungBay.size()];
        for (int i = 0; i < listViTriTrungBay.size(); i++) {
            result[i] = listViTriTrungBay.get(i).getTEN();
        }
        return result;
    }

    public String getTenViTri(int mvt) {
        for (ViTriTrungBayDTO vt : listViTriTrungBay) {
            if (vt.getMVT() == mvt) {
                return vt.getTEN();
            }
        }
        return "";
    }
}
