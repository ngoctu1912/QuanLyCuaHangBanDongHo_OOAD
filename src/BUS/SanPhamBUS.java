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
        listSP = spDAO.selectAll();
    }

    public ArrayList<SanPhamDTO> getAll() {
        
        return this.listSP;
    }

    public SanPhamDTO getByIndex(int index) {
        return this.listSP.get(index);
    }

    public SanPhamDTO getByMaSP(int masp) {
        int vitri = -1;
        int i = 0;
        while (i <= this.listSP.size() && vitri == -1) {
            if (this.listSP.get(i).getMSP() == masp) {
                vitri = i;
            } else {
                i++;
            }
        }
        return this.listSP.get(vitri);
    }
    
    public int getMaxMSP() {
        return spDAO.getMaxMSP();
    }
    
    public SanPhamDTO getMaSP(String maSP) {
        return spDAO.selectById(maSP);
    }

    public int getIndexByMaSP(int masanpham) {
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
            this.listSP.add(lh);
        }
        return check;
    }

    public Boolean delete(SanPhamDTO lh) {
        boolean check = spDAO.delete(Integer.toString(lh.getMSP())) != 0;
        if (check) {
            this.listSP.remove(lh);
        }
        return check;
    }

    public Boolean update(SanPhamDTO lh) {
        boolean check = spDAO.update(lh) != 0;
        if (check) {
            this.listSP.set(getIndexByMaSP(lh.getMSP()), lh);
        }
        return check;
    }

    public ArrayList<SanPhamDTO> search(String text, String type) {
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

    public ArrayList<SanPhamDTO> getSp(int maLoai) {
        return spDAO.getSPByMaLoai(maLoai);
    }

    public int getQuantity() {
        int n = 0;
        for(SanPhamDTO i : this.listSP) {
            if (i.getSL() != 0) {
                n += i.getSL();
            }
        }
        return n;
    }

    public boolean checkMV(String ISBN) {
        for(SanPhamDTO i : this.listSP) {
            if(i.getMV().equals(ISBN)) return false;
        }
        System.out.println(ISBN);
        return true;
    }
    
    public boolean checkDuplicate(String tenSP, String donVi, String loai) {
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
