package GUI.Component;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JSeparator;
import javax.swing.JToolBar;

import BUS.NhomQuyenBUS;

public final class MainFunction extends JToolBar {

    public ButtonToolBar btnAdd, btnDelete, btnEdit, btnDetail, /* btnNhapExcel, */ btnXuatExcel, btnHuyPhieu;
    public JSeparator separator1;
    public HashMap<String, ButtonToolBar> btn = new HashMap<>();
    private final NhomQuyenBUS nhomquyenBus = new NhomQuyenBUS();

    public MainFunction(int manhomquyen, String chucnang, String[] listBtn) {
        initData();
        initComponent(manhomquyen, chucnang, listBtn);
    }

    public void initData() {
        btn.put("create", new ButtonToolBar("Thêm", "add.svg", "create"));
        btn.put("delete", new ButtonToolBar("Xóa", "delete.svg", "delete"));
        btn.put("update", new ButtonToolBar("Sửa", "edit.svg", "update"));
        btn.put("cancel", new ButtonToolBar("Hủy", "cancel.svg", "cancel"));
        btn.put("detail", new ButtonToolBar("Chi tiết", "detail.svg", "view"));
        // btn.put("import", new ButtonToolBar("NHẬP EXCEL", "import_excel.svg", "create"));
        btn.put("export", new ButtonToolBar("Xuất Excel", "excel.svg", "view"));
    }

    private void initComponent(int manhomquyen, String chucnang, String[] listBtn) {
        this.setBackground(Color.WHITE);
        this.setRollover(true);
        initData();
        for (String btnn : listBtn) {
            this.add(btn.get(btnn));
            if (!nhomquyenBus.checkPermisson(manhomquyen, chucnang, btn.get(btnn).getPermisson())) {
                btn.get(btnn).setEnabled(false);
            }
        }
    }
}
