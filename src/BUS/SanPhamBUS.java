package BUS;

import java.util.ArrayList;

import DAO.SanPhamDAO;
import DTO.SanPhamDTO;

public class SanPhamBUS {

    public final SanPhamDAO spDAO = new SanPhamDAO();
    public ArrayList<SanPhamDTO> listSP = new ArrayList<>();
    DonViBUS dvbus = new DonViBUS();
    LoaiBUS lbus = new LoaiBUS();

    public SanPhamBUS() {
    }

    public ArrayList<SanPhamDTO> getAll() {
        ensureLoaded();
        return this.listSP;
    }

    /**
     * Lấy danh sách sản phẩm có tồn kho ở chi nhánh cụ thể
     * @param mcn Mã chi nhánh
     * @return Danh sách sản phẩm
     */
    public ArrayList<SanPhamDTO> getAll(String mcn) {
        this.listSP = spDAO.selectAllByMCN(mcn);
        return this.listSP;
    }

    private void ensureLoaded() {
        if (this.listSP == null || this.listSP.isEmpty()) {
            this.listSP = spDAO.selectAll();
        }
    }

    public SanPhamDTO getByIndex(int index) {
        ensureLoaded();
        return this.listSP.get(index);
    }

    public SanPhamDTO getByMaSP(int masp) {
        ensureLoaded();
        int vitri = -1;
        int i = 0;
        while (i < this.listSP.size() && vitri == -1) {
            if (this.listSP.get(i).getMSP() == masp) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri != -1 ? this.listSP.get(vitri) : null;
    }
    
    public int getMaxMSP() {
        return spDAO.getMaxMSP();
    }
    
    public SanPhamDTO getMaSP(String maSP) {
        return spDAO.selectById(maSP);
    }

    public int getIndexByMaSP(int masanpham) {
        ensureLoaded();
        int i = 0;
        int vitri = -1;
        while (i < this.listSP.size() && vitri == -1) {
            if (listSP.get(i).getMSP() == masanpham) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public ArrayList<SanPhamDTO> getByMaViTri(int mavitri) {
        ensureLoaded();
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        for (SanPhamDTO sp : listSP) {
            if (sp.getMVT() != null && sp.getMVT() == mavitri) {
                result.add(sp);
            }
        }
        return result;
    }

    public Boolean add(SanPhamDTO lh) {
        boolean check = spDAO.insert(lh) != 0;
        if (check) {       
            ensureLoaded();
            this.listSP.add(lh);
        }
        return check;
    }

    public Boolean delete(SanPhamDTO lh) {
        boolean check = spDAO.delete(Integer.toString(lh.getMSP())) != 0;
        if (check) {
            ensureLoaded();
            this.listSP.remove(lh);
        }
        return check;
    }

    public Boolean update(SanPhamDTO lh) {
        boolean check = spDAO.update(lh) != 0;
        if (check) {
            ensureLoaded();
            this.listSP.set(getIndexByMaSP(lh.getMSP()), lh);
        }
        return check;
    }

    public ArrayList<SanPhamDTO> search(String text, String type) {
        ensureLoaded();
        text = text.toLowerCase();
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        switch (type) {
            case "Tất cả" -> {
                for (SanPhamDTO i : this.listSP) {
                    if (Integer.toString(i.getMSP()).toLowerCase().contains(text) || i.getTEN().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã sản phẩm" -> {
                for (SanPhamDTO i : this.listSP) {
                    if (Integer.toString(i.getMSP()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Tên sản phẩm" -> {
                for (SanPhamDTO i : this.listSP) {
                    if (i.getTEN().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã vạch" -> {
                if("".compareTo(text) == 0) {
                    result = listSP;
                }
                for (SanPhamDTO i : this.listSP) {
                    // if("".compareTo(text) == 0) {}
                    if (i.getMV().toLowerCase().compareTo(text) == 0) {
                        result.add(i);
                    }
                }
            }
        }
        
        return result;
    }

    public ArrayList<SanPhamDTO> search(ArrayList<SanPhamDTO> listSP, String text, String type) {
        if (listSP == null) {
            listSP = new ArrayList<>();
        }
        text = text.toLowerCase();
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        switch (type) {
            case "Tất cả" -> {
                for (SanPhamDTO i : listSP) {
                    if (Integer.toString(i.getMSP()).toLowerCase().contains(text) || i.getTEN().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã sản phẩm" -> {
                for (SanPhamDTO i : listSP) {
                    if (Integer.toString(i.getMSP()).toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Tên sản phẩm" -> {
                for (SanPhamDTO i : listSP) {
                    if (i.getTEN().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Mã vạch" -> {
                if("".compareTo(text) == 0) {
                    result = new ArrayList<>(listSP);
                }
                for (SanPhamDTO i : listSP) {
                    if (i.getMV().toLowerCase().compareTo(text) == 0) {
                        result.add(i);
                    }
                }
            }
        }
        
        return result;
    }

    public ArrayList<SanPhamDTO> getSp(int maLoai) {
        return spDAO.getSPByMaLoai(maLoai);
    }

    public int getQuantity() {
        ensureLoaded();
        int n = 0;
        for(SanPhamDTO i : this.listSP) {
            if (i.getSL() != 0) {
                n += i.getSL();
            }
        }
        return n;
    }

    public boolean checkMV(String ISBN) {
        ensureLoaded();
        for(SanPhamDTO i : this.listSP) {
            if(i.getMV().equals(ISBN)) return false;
        }
        System.out.println(ISBN);
        return true;
    }
    
    public boolean checkDuplicate(String tenSP, String donVi, String loai) {
        ensureLoaded();
        for(SanPhamDTO i : this.listSP) {
            String maDonVi = String.valueOf(i.getMDV());
            String maLoai = String.valueOf(i.getML());
            if(i.getTEN().equalsIgnoreCase(tenSP) && dvbus.getById(maDonVi).getTENDV().equals(donVi) && lbus.getById(maLoai).getTENL().equals(loai)){
                return false;
            }
        }
        return true;
    }

    public SanPhamDTO getSPbyMV(String ISBN) {
        ensureLoaded();
        for(SanPhamDTO i : this.listSP) {
            if(i.getMV().equals(ISBN)) return i;
        }
        return null;
    }
    
    public ArrayList<SanPhamDTO> getSPByMaLoai(int maLoai) {
        return spDAO.getSPByMaLoai(maLoai);
    }
    
    public ArrayList<SanPhamDTO> getSPByMaDonVi(int maDonVi) {
        return spDAO.getSPByMaDonVi(maDonVi);
    }
}
