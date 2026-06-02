package BUS;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import DAO.ChucVuDAO;
import DAO.NhanVienDAO;
import DAO.TaiKhoanDAO;
import DTO.ChucVuDTO;
import DTO.NhanVienDTO;
import GUI.Dialog.NhanVienDialog;
import GUI.Panel.NhanVien;
import config.JDBCUtil;
import helper.Validation;

public class NhanVienBUS implements ActionListener, DocumentListener {

    public GUI.Panel.NhanVien nv;
    private JTextField textField;
    public ArrayList<NhanVienDTO> listNv = NhanVienDAO.getInstance().selectAll();
    public ArrayList<ChucVuDTO> listCv = ChucVuDAO.getInstance().selectAll();
    public NhanVienDAO nhanVienDAO = NhanVienDAO.getInstance();
    private final Map<String, String> employeeNameCache = new HashMap<>();

    public NhanVienBUS() {
    }

    public NhanVienBUS(NhanVien nv) {
        this.nv = nv;
    }

    public NhanVienBUS(JTextField textField, NhanVien nv) {
        this.textField = textField;
        this.nv = nv;
    }

    public ArrayList<DTO.NhanVienDTO> getAll() {
        this.listNv = NhanVienDAO.getInstance().selectAll();
        employeeNameCache.clear();
        return this.listNv;
    }

    public ArrayList<NhanVienDTO> getAllByBranchLabel(String branchLabel) {
        String mcn = branchLabelToMcn(branchLabel);
        this.listNv = NhanVienDAO.getInstance().selectAllByMCN(mcn);
        employeeNameCache.clear();
        return this.listNv;
    }

    public String getCurrentBranchLabel() {
        return branchLabelForMcn(JDBCUtil.getCurrentMcn());
    }

    public NhanVienDTO getByIndex(int index) {
        return this.listNv.get(index);
    }

    public int getIndexById(int manv) {
        int i = 0;
        int vitri = -1;
        int size = this.listNv.size();
        while (i < size && vitri == -1) {
            if (this.listNv.get(i).getMNV() == manv) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public String getMcnbyMnv(int mnv) {
        return nhanVienDAO.getMCNByMNV(mnv);
    }

    public int updateNhanVien(NhanVienDTO nV) {
        return updateNhanVien(nV, null);
    }

    public int updateNhanVien(NhanVienDTO nV, String sourceMcnHint) {
        String currentMcn = JDBCUtil.getCurrentMcn();
        String sourceMcn = sourceMcnHint;

        if (sourceMcn == null || sourceMcn.isBlank()) {
            sourceMcn = getMcnbyMnv(nV.getMNV());
        }

        if (sourceMcn == null || sourceMcn.isBlank()) {
            sourceMcn = currentMcn;
        }

        return nhanVienDAO.update(nV, sourceMcn);
    }

    private String branchLabelToMcn(String branchLabel) {
        if (branchLabel == null) {
            return "ALL";
        }
        return switch (branchLabel) {
            case "Chi nhánh 1" -> "CN1";
            case "Chi nhánh 2" -> "CN2";
            case "Chi nhánh 3" -> "CN3";
            case "Tất cả chi nhánh" -> "ALL";
            default -> "ALL";
        };
    }

    private String branchLabelForMcn(String mcn) {
        if ("CN1".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 1";
        }
        if ("CN2".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 2";
        }
        if ("CN3".equalsIgnoreCase(mcn)) {
            return "Chi nhánh 3";
        }
        return "Tất cả chi nhánh";
    }

    public String getNameById(int manv) {
        return getNameById(manv, JDBCUtil.getCurrentMcn());
    }

    public String getNameById(int manv, String mcn) {
        String normalizedMcn = mcn == null ? "" : mcn.trim().toUpperCase();
        String cacheKey = normalizedMcn + ":" + manv;
        if (employeeNameCache.containsKey(cacheKey)) {
            return employeeNameCache.get(cacheKey);
        }

        NhanVienDTO resolved;
        if (!normalizedMcn.isBlank() && !normalizedMcn.equalsIgnoreCase(JDBCUtil.getCurrentMcn())) {
            resolved = nhanVienDAO.selectByIdFromCentral(manv + "");
            if (resolved == null) {
                resolved = nhanVienDAO.selectById(manv + "");
            }
        } else {
            resolved = nhanVienDAO.selectById(manv + "");
            if (resolved == null) {
                resolved = nhanVienDAO.selectByIdFromCentral(manv + "");
            }
        }

        String name = resolved != null ? resolved.getHOTEN() : "";
        employeeNameCache.put(cacheKey, name);
        return name;
    }

    public NhanVienDTO getByMaNV(int manv) {
        return nhanVienDAO.selectById(manv + "");
    }

    public String[] getArrTenNhanVien() {
        int size = listNv.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNv.get(i).getHOTEN();
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = e.getActionCommand();
        switch (btn) {
            case "create" -> {
                try {
                    new NhanVienDialog(this, nv.owner, true, "Thêm nhân viên", "create");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thêm nhân viên: " + ex.getMessage());
                }
            }
            case "update" -> {
                try {
                    int index = nv.getRow();
                    if (index < 0) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần sửa");
                    } else {
                        new NhanVienDialog(this, nv.owner, true, "Sửa nhân viên", "update", nv.getNhanVien());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi sửa thông tin nhân viên: " + ex.getMessage());
                }
            }
            case "delete" -> {
                try {
                    if (nv.getRow() < 0) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa");
                    } else {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Bạn có chắc chắn muốn xóa nhân viên này không?",
                                "Xác nhận xóa",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteNv(nv.getNhanVien());
                            JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi xóa nhân viên: " + ex.getMessage());
                }
            }
            case "view" -> {
                try {
                    if (nv.getRow() < 0) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xem");
                    } else {
                        new NhanVienDialog(this, nv.owner, true, "Xem nhân viên", "detail", nv.getNhanVien());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi xem thông tin nhân viên: " + ex.getMessage());
                }
            }
            case "export" -> {
                try {
                    String[] header = new String[]{"MãNV", "Tên nhân viên", "Email nhân viên", "Số điên thoại", "Giới tính", "Ngày sinh"};
                    exportExcel(listNv, header);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi xuất dữ liệu: " + ex.getMessage());
                }
            }
        }
        loadTable();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        String text = textField.getText();
        if (text.length() == 0) {
            nv.loadDataTalbe(filterByCurrentMcn(listNv));
        } else {
            ArrayList<NhanVienDTO> listSearch = search(text);
            searchLoadTable(listSearch);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String text = textField.getText();
        if (text.length() == 0) {
            nv.loadDataTalbe(filterByCurrentMcn(listNv));
        } else {
            ArrayList<NhanVienDTO> listSearch = search(text);
            searchLoadTable(listSearch);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public void insertNv(NhanVienDTO nv) {
        listNv.add(nv);
        employeeNameCache.clear();
    }

    public void updateNv(int index, NhanVienDTO nv) {
        listNv.set(index, nv);
        employeeNameCache.clear();
    }

    public int getIndex() {
        return nv.getRow();
    }

    public void deleteNv(NhanVienDTO nv) {
        deleteNhanVien(nv);
        TaiKhoanDAO.getInstance().delete(nv.getMNV() + "");
        listNv.removeIf(n -> (n.getMNV() == nv.getMNV()));
        employeeNameCache.clear();
        loadTable();
    }

    public int deleteNhanVien(NhanVienDTO nV) {
        return deleteNhanVien(nV, null);
    }

    public int deleteNhanVien(NhanVienDTO nV, String sourceMcnHint) {
        String sourceMcn = sourceMcnHint;
        if (sourceMcn == null || sourceMcn.isBlank()) {
            sourceMcn = getMcnbyMnv(nV.getMNV());
        }
        return nhanVienDAO.delete(String.valueOf(nV.getMNV()), sourceMcn);
    }

    public void loadTable() {
        nv.refreshToCurrentServerBranch();
    }

    public void searchLoadTable(ArrayList<NhanVienDTO> list) {
        nv.loadDataTalbe(filterByCurrentMcn(list));
    }

    private ArrayList<NhanVienDTO> filterByCurrentMcn(ArrayList<NhanVienDTO> source) {
        if (nv == null) return source;
        Integer currentMnv = nv.getCurrentUserMNV();
        if (currentMnv == null) return source;
        try {
            String currentMcn = getMcnbyMnv(currentMnv);
            if (currentMcn == null) return source;
            ArrayList<NhanVienDTO> res = new ArrayList<>();
            for (NhanVienDTO dto : source) {
                if (dto.getMCN() != null && dto.getMCN().equals(currentMcn)) res.add(dto);
            }
            return res;
        } catch (Exception e) {
            return source;
        }
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void exportExcel(ArrayList<NhanVienDTO> list, String[] header) {
        if (!list.isEmpty()) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(nv.owner);
            File saveFile = jFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");

                if (saveFile.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Tệp đã tồn tại. Bạn có muốn ghi đè lên tệp cũ không?",
                            "Xác nhận ghi đè",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try (Workbook wb = new XSSFWorkbook();
                     FileOutputStream out = new FileOutputStream(saveFile)) {

                    Sheet sheet = wb.createSheet("Nhân viên");
                    writeHeader(header, sheet, 0);
                    int rowIndex = 1;
                    for (NhanVienDTO item : list) {
                        Row row = sheet.createRow(rowIndex++);
                        writeNhanVien(item, row);
                    }
                    wb.write(out);
                    openFile(saveFile.toString());
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null,
                            "Không thể ghi đè lên tệp vì tệp đang được mở trong một ứng dụng khác.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<NhanVienDTO> search(String text) {
        String luachon = (String) nv.search.cbxChoose.getSelectedItem();
        text = text.toLowerCase();
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        switch (luachon) {
            case "Tất cả" -> {
                for (NhanVienDTO item : this.listNv) {
                    if (item.getHOTEN().toLowerCase().contains(text)
                            || item.getEMAIL().toLowerCase().contains(text)
                            || item.getSDT().toLowerCase().contains(text)
                            || (item.getMNV() + "").contains(text)
                            || (item.getGIOITINH() == 1 && "nam".contains(text))
                            || (item.getGIOITINH() == 0 && "nữ".contains(text))
                            || (item.getNGAYSINH() != null && item.getNGAYSINH().toString().contains(text))) {
                        result.add(item);
                    }
                }
            }
            case "Họ tên" -> {
                for (NhanVienDTO item : this.listNv) {
                    if (item.getHOTEN().toLowerCase().contains(text)) {
                        result.add(item);
                    }
                }
            }
            case "Email" -> {
                for (NhanVienDTO item : this.listNv) {
                    if (item.getEMAIL().toLowerCase().contains(text)) {
                        result.add(item);
                    }
                }
            }
            default -> throw new AssertionError();
        }

        return result;
    }

    private static void writeHeader(String[] list, Sheet sheet, int rowIndex) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell;
        for (int i = 0; i < list.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(list[i]);
            sheet.autoSizeColumn(i);
        }
    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    private static void writeNhanVien(NhanVienDTO nv, Row row) {
        CellStyle cellStyleFormatNumber = null;
        if (cellStyleFormatNumber == null) {
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        Cell cell = row.createCell(0);
        cell.setCellValue(nv.getMNV());

        cell = row.createCell(1);
        cell.setCellValue(nv.getHOTEN());

        cell = row.createCell(2);
        cell.setCellValue(nv.getEMAIL());

        cell = row.createCell(3);
        cell.setCellValue(nv.getSDT());

        cell = row.createCell(4);
        cell.setCellValue(nv.getGIOITINH() == 1 ? "Nam" : "Nữ");

        cell = row.createCell(5);
        cell.setCellValue("" + nv.getNGAYSINH());
    }

    public static boolean isPhoneNumber(String str) {
        str = str.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");

        if (str.matches("\\d{10}")) {
            return true;
        } else if (str.matches("\\d{3}-\\d{3}-\\d{4}")) {
            return true;
        } else {
            return str.matches("\\(\\d{3}\\)\\d{3}-\\d{4}");
        }
    }

    public int getTotalNhanVien() {
        return nhanVienDAO.countAll();
    }

    public int deactivateAccountByMNV(int mnv) {
        return nhanVienDAO.deactivateAccountByMNV(mnv);
    }
}
