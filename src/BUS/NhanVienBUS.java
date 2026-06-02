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
import helper.Validation;

public class NhanVienBUS implements ActionListener, DocumentListener {

    public GUI.Panel.NhanVien nv;
    private JTextField textField;
    public ArrayList<NhanVienDTO> listNv = NhanVienDAO.getInstance().selectAll();
    public ArrayList<ChucVuDTO> listCv = ChucVuDAO.getInstance().selectAll();
    public NhanVienDAO nhanVienDAO = NhanVienDAO.getInstance();

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
        return this.listNv;
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
    
    public String  getMcnbyMnv(int mnv)
    {
        return nhanVienDAO.getMCNByMNV(mnv);
    }
    
    public String getNameById(int manv) {
        return nhanVienDAO.selectById(manv+"").getHOTEN();
    }

    public NhanVienDTO getByMaNV(int manv) {
        return nhanVienDAO.selectById(manv+"");
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
    nv.loadDataTalbe(filterByCurrentMcn(listNv));
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
//        System.out.println("Text field changed: " + textField.getText());
    }

    public void insertNv(NhanVienDTO nv) {
        listNv.add(nv);
    }

    public void updateNv(int index, NhanVienDTO nv) {
        listNv.set(index, nv);
    }

    public int getIndex() {
        return nv.getRow();
    }

    public void deleteNv(NhanVienDTO nv) {
        NhanVienDAO.getInstance().delete(nv.getMNV() + "");
        TaiKhoanDAO.getInstance().delete(nv.getMNV() + "");
        listNv.removeIf(n -> (n.getMNV() == nv.getMNV()));
        loadTable();
    }

    public void loadTable() {
        nv.loadDataTalbe(filterByCurrentMcn(listNv));
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
                for (NhanVienDTO nv : list) {
                    Row row = sheet.createRow(rowIndex++);
                    writeNhanVien(nv, row);
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
    for (NhanVienDTO i : this.listNv) {
        if (i.getHOTEN().toLowerCase().contains(text) || 
            i.getEMAIL().toLowerCase().contains(text) ||
            i.getSDT().toLowerCase().contains(text) ||
            (i.getMNV() + "").contains(text) || // Thêm điều kiện tìm kiếm theo mã nhân viên
            (i.getGIOITINH() == 1 && "nam".contains(text)) || // Kiểm tra giới tính
            (i.getGIOITINH() == 0 && "nữ".contains(text)) || // Kiểm tra giới tính
            (i.getNGAYSINH() != null && i.getNGAYSINH().toString().contains(text))) { // Kiểm tra ngày sinh
            result.add(i);
        }
    }
}
        case "Họ tên" -> {
            for (NhanVienDTO i : this.listNv) {
                if (i.getHOTEN().toLowerCase().contains(text)) {
                    result.add(i);
                }
            }
        }
        case "Email" -> {
            for (NhanVienDTO i : this.listNv) {
                if (i.getEMAIL().toLowerCase().contains(text)) {
                    result.add(i);
                }
            }
        }
        default ->
            throw new AssertionError();
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
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
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
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
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
// check sau 
    /*public void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(null);
        jf.setDialogTitle("Open file");
        int k = 0;
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    int check = 1;
                    int gt;
                    XSSFRow excelRow = excelSheet.getRow(row);
                    int id = NhanVienDAO.getInstance().getAutoIncrement();
                    String tennv = excelRow.getCell(0).getStringCellValue();
                    String gioitinh = excelRow.getCell(1).getStringCellValue();
                    if (gioitinh.equals("Nam") || gioitinh.equals("nam")) {
                        gt = 1;
                    } else {
                        gt = 0;
                    }
                    String sdt = excelRow.getCell(3).getStringCellValue();
                    Date ngaysinh = (Date) excelRow.getCell(2).getDateCellValue();
                    java.sql.Date birth = new java.sql.Date(ngaysinh.getTime());
                    String email = excelRow.getCell(4).getStringCellValue();
                    String chucvu = excelRow.getCell(5).getStringCellValue();
                    int i = 0;
                    for(int j = 0; j < listCv.size(); j++) if(listCv.get(j).equals(chucvu)) i = j + 1;
                    if (Validation.isEmpty(tennv) || Validation.isEmpty(email) || Validation.isEmpty(chucvu)
                            || !Validation.isEmail(email) || Validation.isEmpty(sdt)
                            || Validation.isEmpty(sdt) || !isPhoneNumber(sdt)
                            || sdt.length() != 10 || Validation.isEmpty(gioitinh)) {
                        check = 0;
                    }
                    if (check == 0) {
                        k += 1;
                    } else {
                        NhanVienDTO nvdto = new NhanVienDTO(id, tennv, gt,sdt, birth, 1, email, i);
                        NhanVienDAO.getInstance().insert(nvdto);
                    }
                    JOptionPane.showMessageDialog(null, "Nhập thành công");
                }

            } catch (FileNotFoundException ex) {
                System.out.println("Lỗi đọc file");
            } catch (IOException ex) {
                System.out.println("Lỗi đọc file");
            }
        }
        if (k != 0) {
            JOptionPane.showMessageDialog(null, "Những dữ liệu không chuẩn không được thêm vào");
        }
    }*/

    
    
    
    
    public static boolean isPhoneNumber(String str) {
        // Loại bỏ khoảng trắng và dấu ngoặc đơn nếu có
        str = str.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");

        // Kiểm tra xem chuỗi có phải là một số điện thoại hợp lệ hay không
        if (str.matches("\\d{10}")) { // Kiểm tra số điện thoại 10 chữ số
            return true;
        } else if (str.matches("\\d{3}-\\d{3}-\\d{4}")) { // Kiểm tra số điện thoại có dấu gạch ngang
            return true;
        } else {
            return str.matches("\\(\\d{3}\\)\\d{3}-\\d{4}"); // Kiểm tra số điện thoại có dấu ngoặc đơn
        }        // Trả về false nếu chuỗi không phải là số điện thoại hợp lệ

    }
    public int getTotalNhanVien() {
    return nhanVienDAO.countAll();
}
    public int deactivateAccountByMNV(int mnv){
        return nhanVienDAO.deactivateAccountByMNV(mnv);
    }
}
