package helper;

import java.io.File;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.IOException;

public class JTableExporter {

    public static void exportJTableToExcel(JTable table) throws IOException{
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            File file = new File(filePath);

            if (file.exists()) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "File '" + file.getName() + "' đã tồn tại. Có muốn ghi đè không?",
                        "Xác nhận ghi đè",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            try (Workbook workbook = new XSSFWorkbook();
                 FileOutputStream fileOut = new FileOutputStream(filePath)) {

                Sheet sheet = workbook.createSheet("Sheet1");

                // Tạo dòng header
                Row headerRow = sheet.createRow(0);
                TableModel model = table.getModel();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell headerCell = headerRow.createCell(i);
                    headerCell.setCellValue(model.getColumnName(i));
                }

                // Tạo các dòng dữ liệu
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row dataRow = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell dataCell = dataRow.createCell(j);
                        Object value = model.getValueAt(i, j);
                        if (value != null) {
                            dataCell.setCellValue(value.toString());
                        }
                    }
                }

                // Resize tất cả các cột cho vừa với nội dung
                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Xuất Excel thành công!");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Không thể ghi đè lên tệp vì tệp đang được mở trong một ứng dụng khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}