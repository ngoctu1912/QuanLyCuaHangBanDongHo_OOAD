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
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
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
import DTO.ChucVuDTO;
import GUI.Dialog.ChucVuDialog;
import GUI.Panel.ChucVu;
import helper.Validation;
public class ChucVuBUS implements ActionListener, DocumentListener  {
    
    public GUI.Panel.ChucVu cv;
    private JTextField textField;
    public final ChucVuDAO ChucVuDAO = new ChucVuDAO();
    public ArrayList<ChucVuDTO> listChucVu;

    public ChucVuBUS() {
        this.listChucVu = ChucVuDAO.selectAll();
    }

    public ArrayList<ChucVuDTO> getAll() {
        return this.listChucVu;
    }

    public ArrayList<ChucVuDTO> selectAll() {
        return ChucVuDAO.getAll();
    }

    public ChucVuBUS(ChucVu cv) {
        this.cv = cv;
        this.listChucVu = ChucVuDAO.selectAll();
    }

    public ChucVuBUS(JTextField textField, ChucVu cv) {
        this.textField = textField;
        this.cv = cv;
        this.listChucVu = ChucVuDAO.selectAll();
    }

    public ChucVuDTO getByIndex(int index) {
        return this.listChucVu.get(index);
    }

    public boolean add(ChucVuDTO lh) {
        boolean check = ChucVuDAO.insert(lh) != 0;
        if (check) {
            this.listChucVu.add(lh);
        }
        return check;
    }

    public boolean update(ChucVuDTO lh) {
        boolean check = ChucVuDAO.update(lh) != 0;
        if (check) {
            this.listChucVu = ChucVuDAO.selectAll();
        }
        return check;
    }

    public boolean delete(ChucVuDTO nqdto) {
        boolean check = ChucVuDAO.delete(Integer.toString(nqdto.getMCV())) != 0;
        if (check) {
            this.listChucVu.remove(nqdto);
        }
        return check;
    }

    public String[] getArrTenCV() {
        int size = listChucVu.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listChucVu.get(i).getTENCV();
        }
        return result;
    }

    public int getIndexByMCV(int mancc) {
        int i = 0;
        int vitri = -1;
        while (i < this.listChucVu.size() && vitri == -1) {
            if (listChucVu.get(i).getMCV() == mancc) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (cv == null) {
            return;
        }
        
        String btn = e.getActionCommand();
        String btnLower = btn.toLowerCase().trim();
        
        if (btnLower.contains("th") && btnLower.contains("m")) { // "thêm"
            try {
                new ChucVuDialog(this, cv.owner, true, "Thêm chức vụ", "create");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (btnLower.contains("s") && (btnLower.contains("a") || btnLower.contains("?"))) { // "sửa"
            try {
                int index = cv.getRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ cần sửa");
                } else {
                    new ChucVuDialog(this, cv.owner, true, "Sửa chức vụ", "update", cv.getChucVu());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (btnLower.contains("xóa") || btnLower.contains("xo")) { // "xóa"
            try {
                if (cv.getRow() < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ cần xóa");
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc chắn muốn xóa chức vụ này không?",
                            "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteNv(cv.getChucVu());
                        JOptionPane.showMessageDialog(null, "Xóa chức vụ thành công!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (btnLower.contains("chi") && btnLower.contains("ti")) { // "chi tiết"
            try {
                if (cv.getRow() < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ cần xem");
                } else {
                    new ChucVuDialog(this, cv.owner, true, "Xem chức vụ", "detail", cv.getChucVu());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (btnLower.contains("nh") && btnLower.contains("p") && btnLower.contains("excel")) { // "nhập excel"
            try {
                importExcel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (btnLower.contains("xu") && btnLower.contains("t") && btnLower.contains("excel")) { // "xuất excel"
            try {
                String[] header = new String[]{"MãNV", "Tên chức vụ","Mức lương"};
                exportExcel(listChucVu, header);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (btnLower.contains("l") && btnLower.contains("m") && btnLower.contains("i")) { // "làm mới"
            try {
                cv.search.txtSearchForm.setText("");
                cv.loadDataTalbe(listChucVu);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        if (cv != null) {
            cv.loadDataTalbe(listChucVu);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        String text = textField.getText();
        if (text.length() == 0) {
            cv.loadDataTalbe(listChucVu);
        } else {
            ArrayList<ChucVuDTO> listSearch = search(text);
            searchLoadTable(listSearch);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String text = textField.getText();
        if (text.length() == 0) {
            cv.loadDataTalbe(listChucVu);
        } else {
            ArrayList<ChucVuDTO> listSearch = search(text);
            searchLoadTable(listSearch);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
//        System.out.println("Text field changed: " + textField.getText());
    }

    public void insertNv(ChucVuDTO cv) {
        listChucVu.add(cv);
    }

    public void updateNv(int index, ChucVuDTO cv) {
        listChucVu.set(index, cv);
    }

    public int getIndex() {
        return cv.getRow();
    }

    public void deleteNv(ChucVuDTO cv) {
        ChucVuDAO.delete(cv.getMCV() + "");
        listChucVu.removeIf(n -> (n.getMCV() == cv.getMCV()));
        loadTable();
    }

    public void loadTable() {
        cv.loadDataTalbe(listChucVu);
    }

    public void searchLoadTable(ArrayList<ChucVuDTO> list) {
        cv.loadDataTalbe(list);
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void exportExcel(ArrayList<ChucVuDTO> list, String[] header) {
    try {
        
        if (!list.isEmpty()) {
            JFileChooser jFileChooser = new JFileChooser();
            int userSelection = jFileChooser.showSaveDialog(cv.owner);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File saveFile = jFileChooser.getSelectedFile();
                if (saveFile != null) {
                    String filePath = saveFile.toString();

                    // Kiểm tra và thêm phần mở rộng nếu cần
                    if (!filePath.toLowerCase().endsWith(".xlsx")) {
                        filePath += ".xlsx";
                    }

                    saveFile = new File(filePath);

                    // Kiểm tra xem tệp đã tồn tại chưa
                    if (saveFile.exists()) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Tệp đã tồn tại. Bạn có muốn ghi đè lên tệp cũ không?",
                                "Xác nhận ghi đè",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm != JOptionPane.YES_OPTION) {
                            return; // Nếu người dùng chọn "No", thoát khỏi phương thức
                        }
                    }

                    try (Workbook wb = new XSSFWorkbook();
                         FileOutputStream out = new FileOutputStream(saveFile)) {

                        Sheet sheet = wb.createSheet("Chức vụ");
                        writeHeader(header, sheet, 0);
                        int rowIndex = 1;
                        for (ChucVuDTO cv : list) {
                            Row row = sheet.createRow(rowIndex++);
                            writeChucVu(cv, row);
                        }
                        wb.write(out);
                        JOptionPane.showMessageDialog(null, "Xuất tệp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        openFile(saveFile.toString());
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null, "Không thể ghi đè lên tệp vì tệp đang được mở trong một ứng dụng khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi xuất tệp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    public ArrayList<ChucVuDTO> search(String text) {
    String luachon = (String) cv.search.cbxChoose.getSelectedItem();
    text = text.toLowerCase();
    ArrayList<ChucVuDTO> result = new ArrayList<>();
    switch (luachon) {
        case "Tất cả" -> {
            for (ChucVuDTO i : this.listChucVu) {
                if (i.getTENCV().toLowerCase().contains(text) || 
                    (i.getMUCLUONG() + "").contains(text) || 
                    (i.getMCV() + "").contains(text)) { // Thêm điều kiện kiểm tra mã chức vụ
                    result.add(i);
                }
            }
        }
        case "Tên chức vụ" -> {
            for (ChucVuDTO i : this.listChucVu) {
                if (i.getTENCV().toLowerCase().contains(text)) {
                    result.add(i);
                }
            }
        }
        case "Mức lương" -> {
            for (ChucVuDTO i : this.listChucVu) {
                if ((i.getMUCLUONG() + "").contains(text)) {
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

    private static void writeChucVu(ChucVuDTO cv, Row row) {
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
        cell.setCellValue(cv.getMCV());

        cell = row.createCell(1);
        cell.setCellValue(cv.getTENCV());

        cell = row.createCell(2);
        cell.setCellValue(cv.getMUCLUONG());
    }

  public void importExcel() {
    File excelFile;
    FileInputStream excelFIS = null;
    BufferedInputStream excelBIS = null;
    XSSFWorkbook excelJTableImport = null;
    JFileChooser jf = new JFileChooser();
    int result = jf.showOpenDialog(null);
    jf.setDialogTitle("Open file");
    List<String[]> invalidRows = new ArrayList<>(); // Danh sách các dòng không hợp lệ

    if (result == JFileChooser.APPROVE_OPTION) {
        try {
            excelFile = jf.getSelectedFile();
            excelFIS = new FileInputStream(excelFile);
            excelBIS = new BufferedInputStream(excelFIS);
            excelJTableImport = new XSSFWorkbook(excelBIS);
            XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

            for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                int check = 1;
                XSSFRow excelRow = excelSheet.getRow(row);
                int id = ChucVuDAO.getAutoIncrement();
                String tencv = excelRow.getCell(1).getStringCellValue();
                int ml = 0;

                // Kiểm tra và chuyển đổi dữ liệu mức lương
                if (excelRow.getCell(2).getCellType() == CellType.NUMERIC) {
                    ml = (int) excelRow.getCell(2).getNumericCellValue();
                } else if (excelRow.getCell(2).getCellType() == CellType.STRING) {
                    try {
                        ml = Integer.parseInt(excelRow.getCell(1).getStringCellValue());
                    } catch (NumberFormatException e) {
                        check = 0; // Đánh dấu dòng không hợp lệ nếu không thể chuyển đổi
                    }
                }

                // Kiểm tra tính hợp lệ của tên chức vụ
                if (Validation.isEmpty(tencv)) {
                    check = 0;
                }

                if (check == 0) {
                    // Thêm thông tin dòng không hợp lệ vào danh sách
                    invalidRows.add(new String[]{String.valueOf(row + 1), tencv, String.valueOf(ml)});
                } else {
                    ChucVuDTO nvdto = new ChucVuDTO(id, tencv, ml);
                    ChucVuDAO.insert(nvdto);
                    listChucVu.add(nvdto);
                }
            }

            JOptionPane.showMessageDialog(null, "Nhập thành công");

        } catch (FileNotFoundException ex) {
            System.out.println("Lỗi đọc file");
        } catch (IOException ex) {
            System.out.println("Lỗi đọc file");
        }
    }

    if (!invalidRows.isEmpty()) {
        // Tạo bảng thông báo các dòng không hợp lệ
        String[] columnNames = {"Row", "Tên chức vụ", "Mức Lương"};
        JTable table = new JTable(invalidRows.toArray(new String[0][]), columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(null, scrollPane, "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, "Những dữ liệu không chuẩn không được thêm vào");
    }
    cv.loadDataTalbe(listChucVu);
}
  public String getTenChucVuByMCV(int mcv) {
    return ChucVuDAO.getInstance().getTenChucVuByMCV(mcv);
}
  public int getRecordCount() {
        return ChucVuDAO.getRecordCount();
    }

}
