DROP DATABASE IF EXISTS QuanLyCuaHangDongHo;
CREATE DATABASE QuanLyCuaHangDongHo;
USE QuanLyCuaHangDongHo;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*Tạo bảng*/
CREATE TABLE `DANHMUCCHUCNANG` (
    `MCHUCNANG` VARCHAR(50) NOT NULL COMMENT 'Mã chức năng',
    `TEN` VARCHAR(255) NOT NULL COMMENT 'Tên chức năng',
    `TT` INT NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MCHUCNANG)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `CTQUYEN` (
    `MNQ` INT(11) NOT NULL COMMENT 'Mã nhóm quyền',
    `MCN` VARCHAR(50) NOT NULL COMMENT 'Mã chức năng',
    `HANHDONG` VARCHAR(255) NOT NULL COMMENT 'Hành động thực hiện',
    PRIMARY KEY(MNQ, MCN, HANHDONG)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `NHOMQUYEN` (
    `MNQ` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã nhóm quyền',
    `TEN` VARCHAR(255) NOT NULL COMMENT 'Tên nhóm quyền',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MNQ) 
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `CHUCVU` (
    `MCV` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã chức vụ',
    `TEN` VARCHAR(255) NOT NULL COMMENT 'Tên chức vụ',
    `MUCLUONG` INT(11) NOT NULL COMMENT 'Mức lương',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MCV)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `NHANVIEN` (
    `MNV` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã nhân viên',
    `HOTEN` VARCHAR(255) NOT NULL COMMENT 'Họ và tên NV',
    `GIOITINH` INT(11) NOT NULL COMMENT 'Giới tính',
    `NGAYSINH` DATE NOT NULL COMMENT 'Ngày sinh',
    `SDT` VARCHAR(11) NOT NULL COMMENT 'Số điện thoại',
    `EMAIL` VARCHAR(50) NOT NULL UNIQUE COMMENT 'Email',
    `MCV` INT(11) NOT NULL COMMENT 'Mã chức vụ',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MNV)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `TAIKHOAN` (
    `MNV` INT(11) NOT NULL COMMENT 'Mã nhân viên',
    `MK` VARCHAR(255) NOT NULL COMMENT 'Mật khẩu',
    `TDN` VARCHAR(255) NOT NULL UNIQUE COMMENT 'Tên đăng nhập',
    `MNQ` INT(11) NOT NULL COMMENT 'Mã nhóm quyền',
    `TRANGTHAI` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    `OTP` VARCHAR(50) DEFAULT NULL COMMENT 'Mã OTP',
    PRIMARY KEY(MNV, TDN)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `KHACHHANG` (
    `MKH` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã khách hàng',
    `HOTEN` VARCHAR(255) NOT NULL COMMENT 'Họ và tên KH',
    `NGAYTHAMGIA` DATE NOT NULL COMMENT 'Ngày tạo dữ liệu',
    `DIACHI` VARCHAR(255) COMMENT 'Địa chỉ',
    `SDT` VARCHAR(11) UNIQUE NOT NULL COMMENT 'Số điện thoại',
    `DIEMTICHLUY` INT(11) DEFAULT 0 COMMENT 'Điểm tích lũy',
    `EMAIL` VARCHAR(50) UNIQUE COMMENT 'Email',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MKH)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `LICHSUDIEM` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã lịch sử',
    `MKH` INT(11) NOT NULL COMMENT 'Mã khách hàng',
    `DIEM` INT(11) NOT NULL COMMENT 'Số điểm',
    `LOAI` INT(11) NOT NULL COMMENT 'Loại (1: cộng, 0: trừ)',
    `NOIDUNG` VARCHAR(255) COMMENT 'Nội dung',
    `THOIGIAN` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian',
    PRIMARY KEY(ID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `NHACUNGCAP` (
    `MNCC` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã nhà cung cấp',
    `TEN` VARCHAR(255) NOT NULL COMMENT 'Tên NCC',
    `DIACHI` VARCHAR(255) COMMENT 'Địa chỉ',
    `SDT` VARCHAR(12) COMMENT 'Số điện thoại',
    `EMAIL` VARCHAR(50) COMMENT 'Email',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MNCC)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `VITRITUNGBAY` (
    `MVT` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã vị trí trưng bày',
    `TEN` VARCHAR(255) NOT NULL COMMENT 'Tên vị trí',
    `GHICHU` TEXT COMMENT 'Ghi chú',
    PRIMARY KEY(MVT)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `SANPHAM` (
    `MSP` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã sản phẩm',
    `TEN` VARCHAR(255) NOT NULL COMMENT 'Tên sản phẩm',
    `HINHANH` VARCHAR(255) NOT NULL COMMENT 'Hình sản phẩm',
    `MNCC` INT(11) NOT NULL COMMENT 'Mã nhà cung cấp',
    `MVT` INT(11) COMMENT 'Mã vị trí trưng bày',
    `THUONGHIEU` VARCHAR(100) COMMENT 'Thương hiệu',
    `NAMSANXUAT` YEAR COMMENT 'Năm sản xuất',
    `GIANHAP` DECIMAL(15,2) NOT NULL COMMENT 'Giá nhập',
    `GIABAN` DECIMAL(15,2) NOT NULL COMMENT 'Giá bán',
    `SOLUONG` INT(11) DEFAULT 0 COMMENT 'Số lượng',
    `THOIGIANBAOHANH` INT(11) DEFAULT 12 COMMENT 'Thời gian bảo hành (tháng)',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MSP)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `PHIEUNHAP` (
    `MPN` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã phiếu nhập',
    `MNV` INT(11) NOT NULL COMMENT 'Mã nhân viên',
    `MNCC` INT(11) NOT NULL COMMENT 'Mã nhà cung cấp',
    `TIEN` INT(11) NOT NULL COMMENT 'Tổng tiền',
    `TG` DATETIME DEFAULT current_timestamp() COMMENT 'Thời gian tạo',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MPN)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `CTPHIEUNHAP` (
    `MPN` INT(11) NOT NULL COMMENT 'Mã phiếu nhập',
    `MSP` INT(11) NOT NULL COMMENT 'Mã sản phẩm',
    `SL` INT(11) NOT NULL COMMENT 'Số lượng',
    `TIENNHAP` INT(11) NOT NULL COMMENT 'Tiền nhập',
    `HINHTHUC` INT(11) NOT NULL DEFAULT 0 COMMENT 'Hình thức',
    PRIMARY KEY(MPN, MSP)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `PHIEUKIEMKE` (
    `MPKK` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã phiếu kiểm kê',
    `MNV` INT(11) NOT NULL COMMENT 'Mã nhân viên',
    `TG` DATETIME DEFAULT current_timestamp() COMMENT 'Thời gian tạo',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MPKK)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `CTPHIEUKIEMKE` (
    `MPKK` INT(11) NOT NULL COMMENT 'Mã phiếu kiểm kê',
    `MSP` INT(11) NOT NULL COMMENT 'Mã sản phẩm',
    `TRANGTHAISP` INT(11) NOT NULL COMMENT 'Trạng thái sản phẩm',
    `GHICHU` VARCHAR(255) COMMENT 'Ghi chú',
    PRIMARY KEY(MPKK, MSP)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `MAKHUYENMAI` (
    `MKM` VARCHAR(255) NOT NULL COMMENT 'Mã khuyến mãi',
    `TGBD` DATE NOT NULL COMMENT 'Thời gian bắt đầu',
    `TGKT` DATE NOT NULL COMMENT 'Thời gian kết thúc',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    PRIMARY KEY(MKM)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `CTMAKHUYENMAI` (
    `MKM` VARCHAR(255) NOT NULL COMMENT 'Mã khuyến mãi',
    `MSP` INT(11) NOT NULL COMMENT 'Mã sản phẩm',
    `PTG` INT(11) NOT NULL COMMENT 'Phần trăm giảm',
    PRIMARY KEY(MKM, MSP)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `PHIEUXUAT` (
    `MPX` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã phiếu xuất',
    `MNV` INT(11) DEFAULT 1 COMMENT 'Mã nhân viên',
    `MKH` INT(11) NOT NULL COMMENT 'Mã khách hàng',
    `TIEN` INT(11) NOT NULL COMMENT 'Tổng tiền',
    `TG` DATETIME DEFAULT current_timestamp() COMMENT 'Thời gian tạo',
    `DIEMTICHLUY` INT(11) DEFAULT 0 COMMENT 'Điểm tích lũy giảm giá',
    `TT` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái',
    `LYDOHUY` VARCHAR(255) NULL COMMENT 'Lý do hủy phiếu',
    PRIMARY KEY(MPX)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `CTPHIEUXUAT` (
    `MPX` INT(11) NOT NULL COMMENT 'Mã phiếu xuất',
    `MSP` INT(11) NOT NULL COMMENT 'Mã sản phẩm',
    `MKM` VARCHAR(255) COMMENT 'Mã khuyến mãi',
    `SL` INT(11) NOT NULL COMMENT 'Số lượng',
    `TIENXUAT` INT(11) NOT NULL COMMENT 'Tiền xuất',
    PRIMARY KEY(MPX, MSP)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- Xóa bảo hành
/*
CREATE TABLE `PHIEUBAOHANH` (
    `MPB` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã phiếu bảo hành',
    `MPX` INT(11) NOT NULL COMMENT 'Mã phiếu xuất',
    `MSP` INT(11) NOT NULL COMMENT 'Mã sản phẩm',
    `MKH` INT(11) NOT NULL COMMENT 'Mã khách hàng',
    `NGAYBATDAU` DATE NOT NULL COMMENT 'Ngày bắt đầu',
    `NGAYKETTHUC` DATE NOT NULL COMMENT 'Ngày kết thúc',
    `TRANGTHAI` INT(11) NOT NULL DEFAULT 1 COMMENT 'Trạng thái (1: còn hạn, 0: hết hạn)',
    PRIMARY KEY(MPB)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;
*/

CREATE TABLE `PHIEUSUACHUA` (
    `MSC` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Mã sửa chữa',
    -- `MPB` INT(11) NOT NULL COMMENT 'Mã phiếu bảo hành',  -- Xóa bảo hành
    `MNV` INT(11) COMMENT 'Mã nhân viên',
    `NGAYNHAN` DATE NOT NULL COMMENT 'Ngày nhận',
    `NGAYTRA` DATE COMMENT 'Ngày trả',
    `NGUYENNHAN` TEXT COMMENT 'Nguyên nhân',
    `TINHTRANG` INT(11) DEFAULT 0 COMMENT 'Tình trạng (0: chờ, 1: đang sửa, 2: hoàn thành, 3: không sửa được)',
    `CHIPHI` DECIMAL(15,2) DEFAULT 0 COMMENT 'Chi phí',
    `GHICHU` TEXT COMMENT 'Ghi chú',
    PRIMARY KEY(MSC)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

/*Thêm dữ liệu*/

INSERT INTO `DANHMUCCHUCNANG`(`MCHUCNANG`, `TEN`, `TT`)
VALUES 
        ('sanpham', 'Quản lý sản phẩm', 1),
        ('khachhang', 'Quản lý khách hàng', 1),
        ('nhacungcap', 'Quản lý nhà cung cấp', 1),
        ('nhanvien', 'Quản lý nhân viên', 1),
        ('chucvu', 'Quản lý chức vụ', 1),
        ('phieunhap', 'Quản lý nhập hàng', 1),
        ('phieuxuat', 'Quản lý phiếu xuất', 1),
        ('baohanh', 'Quản lý phiếu bảo hành', 1),
        ('suachua', 'Quản lý phiếu sửa chữa', 1),
        ('vitritrungbay', 'Quản lý vị trí trưng bày', 1),
        ('nhomquyen', 'Quản lý nhóm quyền', 1),
        ('taikhoan', 'Quản lý tài khoản', 1),
        ('makhuyenmai', 'Quản lý mã khuyến mãi', 1),
        ('thongke', 'Quản lý thống kê', 1);

INSERT INTO `CTQUYEN` (`MNQ`, `MCHUCNANG`, `HANHDONG`)
VALUES
		(1, 'sanpham', 'create'),
        (1, 'sanpham', 'delete'),
        (1, 'sanpham', 'update'),
        (1, 'sanpham', 'view'),
        (1, 'khachhang', 'create'),
        (1, 'khachhang', 'delete'),
        (1, 'khachhang', 'update'),
        (1, 'khachhang', 'view'),
        (1, 'nhacungcap', 'create'),
        (1, 'nhacungcap', 'delete'),
        (1, 'nhacungcap', 'update'),
        (1, 'nhacungcap', 'view'),
        (1, 'nhanvien', 'create'),
        (1, 'nhanvien', 'delete'),
        (1, 'nhanvien', 'update'),
        (1, 'nhanvien', 'view'),
        (1, 'chucvu', 'create'),
        (1, 'chucvu', 'delete'),
        (1, 'chucvu', 'update'),
        (1, 'chucvu', 'view'),
        (1, 'phieunhap', 'create'),
        (1, 'phieunhap', 'cancel'),
        (1, 'phieunhap', 'view'),
        (1, 'phieunhap', 'export'),
        (1, 'phieuxuat', 'create'),
		(1, 'phieuxuat', 'cancel'),
        (1, 'phieuxuat', 'view'),
        (1, 'phieuxuat', 'export'),
        (1, 'baohanh', 'update'),
        (1, 'baohanh', 'view'),
        (1, 'baohanh', 'export'),
        (1, 'suachua', 'create'),
        (1, 'suachua', 'delete'),
        (1, 'suachua', 'update'),
        (1, 'suachua', 'view'),
        (1, 'suachua', 'export'),
        (1, 'vitritrungbay', 'create'),
        (1, 'vitritrungbay', 'delete'),
        (1, 'vitritrungbay', 'update'),
        (1, 'vitritrungbay', 'view'),
        (1, 'nhomquyen', 'create'),
        (1, 'nhomquyen', 'delete'),
        (1, 'nhomquyen', 'update'),
        (1, 'nhomquyen', 'view'),
        (1, 'taikhoan', 'create'),
        (1, 'taikhoan', 'delete'),
        (1, 'taikhoan', 'update'),
        (1, 'taikhoan', 'view'),
        (1, 'makhuyenmai', 'create'),
        (1, 'makhuyenmai', 'delete'),
        (1, 'makhuyenmai', 'update'),
        (1, 'makhuyenmai', 'view'),
        (1, 'thongke', 'create'),
        (1, 'thongke', 'delete'),
        (1, 'thongke', 'update'),
        (1, 'thongke', 'view'),
        (2, 'sanpham', 'view'),
        (2, 'khachhang', 'create'),
        (2, 'khachhang', 'delete'),
        (2, 'khachhang', 'update'),
        (2, 'khachhang', 'view'),
        (2, 'phieuxuat', 'create'),
        (2, 'phieuxuat', 'view'),
        (2, 'phieuxuat', 'export'),
        (2, 'baohanh', 'update'),
        (2, 'baohanh', 'view'),
        (2, 'baohanh', 'export'),
        (2, 'suachua', 'create'),
        (2, 'suachua', 'delete'),
        (2, 'suachua', 'update'),
        (2, 'suachua', 'view'),
        (2, 'suachua', 'export'),
        (2, 'vitritrungbay', 'view');

INSERT INTO `NHOMQUYEN` (`TEN`, `TT`)
VALUES
        ('Quản lý cửa hàng', 1),
        ('Nhân viên bán hàng', 1);

INSERT INTO `CHUCVU` (`TEN`, `MUCLUONG`, `TT`)
VALUES
        ('Quản lý cửa hàng', 12000000, 1),
        ('Nhân viên bán hàng', 4500000, 1);

INSERT INTO `NHANVIEN` (`HOTEN`, `GIOITINH`, `NGAYSINH`, `SDT`, `EMAIL`, `MCV`, `TT`)
VALUES
        ('Võ Thị Thu Luyện', 0, '2005-05-01', '0963841257', 'luyn0505.@gmail.com', 1, 1),
        ('Nguyễn Thị Ngọc Tú', 0, '2005-01-28', '0396532145', 'ngoctu@gmail.com', 2, 1),
        ('Trần Thị Xuân Thanh', 0, '2005-01-22', '0387913347', 'xuanthanh.@gmail.com', 2, 1),
        ('Đỗ Hữu Lộc', 1, '2003-04-11', '0355374322', 'musicanime2501@gmail.com', 2, 1),
        ('Đỗ Nam Anh', 1, '2003-04-11', '0123456789', 'chinchin@gmail.com', 2, 1),
        ('Đinh Ngọc Ánh', 1, '2003-04-03', '0123456789', 'ngocan@gmail.com', 2, 1);

INSERT INTO `TAIKHOAN` (`MNV`, `TDN`, `MK`, `MNQ`, `TRANGTHAI`, `OTP`)
VALUES
        (1, 'admin', '$2a$12$6GSkiQ05XjTRvCW9MB6MNuf7hOJEbbeQx11Eb8oELil1OrCq6uBXm', 1, 1, 'null'),
        (2, 'NV1', '$2a$12$6GSkiQ05XjTRvCW9MB6MNuf7hOJEbbeQx11Eb8oELil1OrCq6uBXm', 2, 1, 'null'),
        (3, 'NV2', '$2a$12$6GSkiQ05XjTRvCW9MB6MNuf7hOJEbbeQx11Eb8oELil1OrCq6uBXm', 2, 1, 'null');

INSERT INTO `KHACHHANG` (`HOTEN`, `DIACHI`, `SDT`, `TT`, `NGAYTHAMGIA`, `DIEMTICHLUY`)
VALUES
        ('Mặc định', '', '', 1, '2025-04-15 09:52:29',0),
        ('Nguyễn Văn Anh', '45 An Dương Vương, Phường Chợ Quán, Thành phố Hồ Chí Minh', '0387913347', 1, '2025-04-15 09:52:29',300),
        ('Trần Nhất Nhất', '270 Hưng Phú, phường Chánh Hưng, Thành phố Hồ Chí Minh', '0123456789', 1, '2025-04-15 09:52:29',300),
        ('Hoàng Gia Bảo', '45 Trương Đình Hội, phường Phú Định, Thành phố Hồ Chí Minh', '0987654321', 1, '2025-04-15 09:52:29',0),
        ('Hồ Minh Hưng', '5 Võ Thị Sáu, phường Xuân Hòa, Thành phố Hồ Chí Minh', '0867987456', 1, '2025-04-15 09:52:29',0),
        ('Nguyễn Thị Minh Anh', '50 Phạm Văn Chí, phường Bình Tiên, Thành phố Hồ Chí Minh', '0935123456', 1, '2025-04-16 17:59:57',0),
        ('Trần Đức Minh', '789 Lê Hồng Phong, Thành Phố Đà Nẵng', '0983456789', 1, '2025-04-16 18:08:12',0),
        ('Lê Hải Yến', '180 Hoàng Ngân, Xã Trung Hòa, Hà Nội', '0977234567', 1, '2025-04-16 18:08:47',0),
        ('Phạm Thanh Hằng', '325 Nguyễn Văn Tăng, Phường Long Bình, Thành phố Hồ Chí Minh', '0965876543', 1, '2025-04-16 18:12:59',0),
        ('Hoàng Đức Anh', '321 Lý Thường Kiệt, Thành phố Cần Thơ', '0946789012', 1, '2025-04-16 18:13:47',0),
        ('Ngô Thanh Tùng', '393 Điện Biên Phủ, Phường Bàn Cờ, Thành phố Hồ Chí Minh', '0912345678', 1, '2025-04-16 18:14:12',0),
        ('Võ Thị Kim Ngân', '123 Đường Lê Lợi, Phường Hồng Bàng 2, Thành phố Hải Phòng', '0916789123', 1, '2025-04-16 18:15:11',0),
        ('Đỗ Văn Tú', '777 Hùng Vương, Thành phố Huế', '0982345678', 1, '2025-04-30 18:15:56',0),
        ('Lý Thanh Trúc', '81 Hoàng Cầm, Phường Linh Xuân, Thành phố Hồ Chí Minh', '0982123456', 1, '2025-04-16 18:16:22',0),
        ('Bùi Văn Hoàng', '222 Đường 2/4, Thành phố Nha Trang', '0933789012', 1, '2025-04-16 18:16:53',0),
        ('Lê Văn Thành', '23 Đường 3 Tháng 2, phường Hòa Hưng, TP. Hồ Chí Minh', '0933456789', 1, '2025-04-16 18:17:46',0),
        ('Nguyễn Thị Lan Anh', '45 Hàng Bạc, phường Hoàn Kiếm, TP. Hà Nội', '0965123456', 1, '2025-04-16 18:18:10',0),
        ('Phạm Thị Mai', '234 Nguyễn Trãi, phường Chợ Quán, TP. Hồ Chí Minh', '0946789013', 1, '2025-04-17 18:18:34',0),
        ('Hoàng Văn Nam', ' 567 Phố Huế, phường Hai Bà Trưng, Hà Nội', '0912345679', 1, '2025-04-17 18:19:16',0);

INSERT INTO `NHACUNGCAP` (`TEN`, `DIACHI`, `SDT`, `EMAIL`, `TT`)
VALUES
        ('Công Ty Cổ Phân Anh Khuê Watch', 'Số 20 Đường 3 Tháng 2, Phường Hòa Hưng, Thành phố Hồ Chí Minh', '1900866858', 'online@anhkhuewatch.com.vn', 1),
		('Công Ty TNHH Citizen Việt Nam', '160 đường số 30, Phường An Lạc, Thành phố Hồ Chí Minh', '0903996733', 'contact@citizen.com.vn', 1),
		('Công ty Cổ phần Orient Việt Nam', '157 Cách Mạng Tháng Tám, Phường Bàn Cờ, Thành phố Hồ Chí Minh', '02822539787', 'info@lpd.com.vn', 1),
		('Công ty TNHH Seiko Việt Nam', 'Khu Công nghiệp Đại An, Phường Việt Hòa, Tỉnh Hải Phòng', '02438621520', 'support@tissot.com', 1),
		('Công ty TNHH Rolex Việt Nam', 'Tầng Trệt, số 88 Đường Đồng Khởi, Phường Sài Gòn, Thành phố Hồ Chí Minh', '02462821922', 'service@rolex.com', 1),
        ('Công ty TNHH Frederique Constant Việt Nam', '393 Điện Biên Phủ, Phường Bàn Cờ, Thành phố Hồ Chí Minh', '18006785', 'info@lpd.com.vn', 1),
        ('Công ty TNHH Fossil Việt Nam', 'Tầng 7, 215 Nguyễn Văn Thủ, phường Tân Định, Thành phố Hồ Chí Minh', '0932523679', ' ecom@dragonflyapac.vn', 1),
        ('Công ty TNHH Dragonfly Select Brands Việt Nam', 'Số 222 Đường Điện Biên Phủ, Phường Xuân Hòa, Thành phố Hồ Chí Minh', '0932029606', 'danielwellingtonvn@dragonflyapac.com', 1),
        ('SKMEI Official', '41 Dawang Road, Zhaoqing High-tech Zone, Sihui City,  Guangdong Province China', '07583988367', 'alex@skmei.com', 1),
		('Timex Vietnam Distributor', 'Tòa nhà Sarimi, khu đô thị Sala, Phường An Lợi Đông, TP Thủ Đức', '0839555959', 'kdonline@nvl.com.vn', 1);

INSERT INTO `VITRITUNGBAY` (`TEN`, `GHICHU`)
VALUES
        ('Khu A1 - Đồng hồ cơ', 'Automatic, Hand-wound'),
        ('Khu A2 - Đồng hồ Quartz', 'Nhiều mẫu phổ thông'),
        ('Khu A3 - Đồng hồ điện tử', 'Casio G-Shock, Baby-G'),
        ('Khu A4 - Smartwatch', 'Đồng hồ thông minh'),
        ('Khu B1 - Casio Corner', 'Kệ riêng thương hiệu Casio'),
        ('Khu B2 - Seiko Corner', 'Vị trí thương hiệu Seiko'),
        ('Khu B3 - Orient Corner', 'Kệ dành cho Orient');

INSERT INTO `SANPHAM` (`TEN`, `HINHANH`, `MNCC`, `MVT`, `THUONGHIEU`, `NAMSANXUAT`, `GIANHAP`, `GIABAN`, `SOLUONG`, `THOIGIANBAOHANH`, `TT`)
VALUES
    -- Đồng hồ Citizen (phân theo loại)
    ('Citizen Eco-Drive BM7108-14E', 'citizen_bm7108.jpg', 2, 2, 'Citizen', 2024, 3500000, 4500000, 15, 24, 1),  -- Quartz
    ('Citizen Promaster NY0040-09E', 'citizen_ny0040.jpg', 2, 1, 'Citizen', 2024, 8500000, 11500000, 8, 24, 1),  -- Automatic
    ('Citizen NH8390-20E', 'citizen_nh8390.png', 2, 1, 'Citizen', 2023, 4200000, 5800000, 12, 24, 1),            -- Automatic

    -- Đồng hồ Orient (Orient Corner)
    ('Orient Bambino RA-AC0E03S', 'orient_bambino.jpg', 3, 7, 'Orient', 2024, 3800000, 5200000, 10, 12, 1),
    ('Orient Mako III RA-AA0008B', 'orient_mako3.jpg', 3, 7, 'Orient', 2024, 5500000, 7500000, 7, 12, 1),
    ('Orient Sun and Moon RA-AS0103S', 'orient_sunmoon.jpg', 3, 7, 'Orient', 2023, 7200000, 9800000, 5, 12, 1),

    -- Đồng hồ Seiko (Seiko Corner)
    ('Seiko 5 Sports SRPD55K1', 'seiko_srpd55.jpg', 4, 6, 'Seiko', 2024, 4800000, 6500000, 20, 12, 1),
    ('Seiko Presage SPB041J1', 'seiko_spb041.jpg', 4, 6, 'Seiko', 2024, 12000000, 16500000, 6, 24, 1),
    ('Seiko Prospex SRPE99K1', 'seiko_srpe99.jpg', 4, 6, 'Seiko', 2023, 8500000, 11200000, 9, 12, 1),
    ('Seiko 5 SNK809K2', 'seiko_snk809.jpg', 4, 6, 'Seiko', 2024, 2200000, 3200000, 25, 12, 1),

    -- Đồng hồ Rolex (cao cấp → máy cơ → để ở khu A1)
    ('Rolex Submariner Date 126610LN', 'rolex_sub.jpg', 5, 1, 'Rolex', 2024, 185000000, 245000000, 2, 60, 1),
    ('Rolex Datejust 41 126300', 'rolex_dj41.jpg', 5, 1, 'Rolex', 2024, 165000000, 215000000, 1, 60, 1),
    ('Rolex Air-King 126900', 'rolex_airking.jpg', 5, 1, 'Rolex', 2023, 145000000, 195000000, 1, 60, 1),

    -- Frederique Constant (đa phần Automatic → khu A1)
    ('Frederique Constant Classic FC-303', 'fc_classic.png', 6, 1, 'Frederique Constant', 2024, 8500000, 12500000, 6, 24, 1),
    ('Frederique Constant Slimline FC-200', 'fc_slimline.png', 6, 2, 'Frederique Constant', 2024, 9200000, 13800000, 4, 24, 1), -- Quartz

    -- Fossil (tùy loại)
    ('Fossil Grant FS4736IE', 'fossil_grant.jpg', 7, 2, 'Fossil', 2024, 2500000, 3800000, 18, 12, 1),  -- Quartz
    ('Fossil Neutra FS5380', 'fossil_neutra.jpg', 7, 2, 'Fossil', 2024, 2200000, 3200000, 22, 12, 1), -- Quartz
    ('Fossil Hybrid Smartwatch FTW1163', 'fossil_hybrid.jpg', 7, 4, 'Fossil', 2024, 3800000, 5500000, 12, 12, 1), -- Smartwatch

    -- Daniel Wellington (Quartz)
    ('Daniel Wellington Classic Sheffield DW00100020', 'dw_sheffield.jpg', 8, 2, 'Daniel Wellington', 2024, 2800000, 4200000, 16, 24, 1),
    ('Daniel Wellington Petite Sterling DW00100306', 'dw_petite.jpg', 8, 2, 'Daniel Wellington', 2024, 3200000, 4800000, 14, 24, 1),
    ('Daniel Wellington Classic Black DW00100133', 'dw_black.jpg', 8, 2, 'Daniel Wellington', 2024, 2500000, 3800000, 20, 24, 1),

    -- Sản phẩm thuộc NCC 1 (Anh Khuê – phân phối Casio)
    ('Casio G-Shock GA-2100-1A1', 'gshock_ga2100.jpg', 1, 5, 'Casio', 2024, 2800000, 3900000, 25, 12, 1), -- Casio Corner
    ('Casio Edifice EFR-556DB-2AV', 'edifice_efr556.jpg', 1, 5, 'Casio', 2024, 3200000, 4500000, 15, 12, 1), -- Casio Corner
    ('Tissot PRX T137.410.11.041.00', 'tissot_prx.jpg', 1, 2, 'Tissot', 2024, 9500000, 13500000, 8, 24, 1), -- Quartz
    ('Hamilton Khaki Field H70455533', 'hamilton_khaki.jpg', 1, 1, 'Hamilton', 2023, 11500000, 16500000, 5, 24, 1), -- Automatic
    
	-- Casio G-Shock (điện tử)
    ('Casio G-Shock DW-5600E-1V', 'gshock_dw5600.jpg', 1, 3, 'Casio', 2024, 1800000, 2600000, 20, 12, 1),
    ('Casio G-Shock AE-1200WH-1A', 'gshock_ae1200.png', 1, 3, 'Casio', 2024, 550000, 890000, 30, 12, 1),

    -- Casio Standard Digital
    ('Casio F-91W', 'casio_f91w.png', 1, 3, 'Casio', 2023, 200000, 350000, 40, 12, 1),
    ('Casio A168WG-9WDF', 'casio_a168w.png', 1, 3, 'Casio', 2024, 650000, 950000, 18, 12, 1),

    -- Baby-G (điện tử nữ)
    ('Casio Baby-G BGD-565-7DR', 'babyg_bgd565.jpg', 1, 3, 'Casio', 2024, 1600000, 2100000, 12, 12, 1),
    ('Casio Baby-G BA-110-1ADR', 'babyg_ba110.png', 1, 3, 'Casio', 2024, 2150000, 2900000, 10, 12, 1),

    -- ProTrek (điện tử outdoor)
    ('Casio ProTrek PRG-270-1A', 'protrek_prg270.png', 1, 3, 'Casio', 2024, 3800000, 5200000, 5, 12, 1),

    -- SKMEI (giá rẻ – điện tử)
    ('SKMEI 1251 Digital', 'skmei_1251.jpg', 9, 3, 'SKMEI', 2024, 150000, 250000, 25, 6, 1),
    ('SKMEI 1456 Digital Military', 'skmei_1456.jpg', 9, 3, 'SKMEI', 2024, 180000, 300000, 22, 6, 1),

    -- Timex (điện tử thể thao)
    ('Timex Ironman Classic 30', 'timex_ironman30.jpg', 10, 3, 'Timex', 2024, 850000, 1350000, 14, 12, 1);

/*Tạo quan hệ*/

ALTER TABLE `CTQUYEN` ADD CONSTRAINT FK_MNQ_CTQUYEN FOREIGN KEY (MNQ) REFERENCES `NHOMQUYEN`(MNQ) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `CTQUYEN` ADD CONSTRAINT FK_MCHUCNANG_CTQUYEN FOREIGN KEY (MCHUCNANG) REFERENCES `DANHMUCCHUCNANG`(MCHUCNANG) ON DELETE NO ACTION ON UPDATE NO ACTION;           

ALTER TABLE `NHANVIEN` ADD CONSTRAINT FK_MCV_NHANVIEN FOREIGN KEY (MCV) REFERENCES `CHUCVU`(MCV) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `TAIKHOAN` ADD CONSTRAINT FK_MNV_TAIKHOAN FOREIGN KEY (MNV) REFERENCES `NHANVIEN`(MNV) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `TAIKHOAN` ADD CONSTRAINT FK_MNQ_TAIKHOAN FOREIGN KEY (MNQ) REFERENCES `NHOMQUYEN`(MNQ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `LICHSUDIEM` ADD CONSTRAINT FK_MKH_LICHSUDIEM FOREIGN KEY (MKH) REFERENCES `KHACHHANG`(MKH) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `SANPHAM` ADD CONSTRAINT FK_MNCC_SANPHAM FOREIGN KEY (MNCC) REFERENCES `NHACUNGCAP`(MNCC) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `SANPHAM` ADD CONSTRAINT FK_MVT_SANPHAM FOREIGN KEY (MVT) REFERENCES `VITRITUNGBAY`(MVT) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `PHIEUNHAP` ADD CONSTRAINT FK_MNV_PHIEUNHAP FOREIGN KEY (MNV) REFERENCES `NHANVIEN`(MNV) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `PHIEUNHAP` ADD CONSTRAINT FK_MNCC_PHIEUNHAP FOREIGN KEY (MNCC) REFERENCES `NHACUNGCAP`(MNCC) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `CTPHIEUNHAP` ADD CONSTRAINT FK_MPN_CTPHIEUNHAP FOREIGN KEY (MPN) REFERENCES `PHIEUNHAP`(MPN) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `CTPHIEUNHAP` ADD CONSTRAINT FK_MSP_CTPHIEUNHAP FOREIGN KEY (MSP) REFERENCES `SANPHAM`(MSP) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `CTMAKHUYENMAI` ADD CONSTRAINT FK_MKM_CTMAKHUYENMAI FOREIGN KEY (MKM) REFERENCES `MAKHUYENMAI`(MKM) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `CTMAKHUYENMAI` ADD CONSTRAINT FK_MSP_CTMAKHUYENMAI FOREIGN KEY (MSP) REFERENCES `SANPHAM`(MSP) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `PHIEUXUAT` ADD CONSTRAINT FK_MNV_HOADON FOREIGN KEY (MNV) REFERENCES `NHANVIEN`(MNV) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `PHIEUXUAT` ADD CONSTRAINT FK_MKH_HOADON FOREIGN KEY (MKH) REFERENCES `KHACHHANG`(MKH) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `CTPHIEUXUAT` ADD CONSTRAINT FK_MPX_CTHOADON FOREIGN KEY (MPX) REFERENCES `PHIEUXUAT`(MPX) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `CTPHIEUXUAT` ADD CONSTRAINT FK_MSP_CTHOADON FOREIGN KEY (MSP) REFERENCES `SANPHAM`(MSP) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `CTPHIEUXUAT` ADD CONSTRAINT FK_MKM_CTHOADON FOREIGN KEY (MKM) REFERENCES `MAKHUYENMAI`(MKM) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Xóa bảo hành
/*
ALTER TABLE `PHIEUBAOHANH` ADD CONSTRAINT FK_MPX_PHIEUBAOHANH FOREIGN KEY (MPX) REFERENCES `PHIEUXUAT`(MPX) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `PHIEUBAOHANH` ADD CONSTRAINT FK_MSP_PHIEUBAOHANH FOREIGN KEY (MSP) REFERENCES `SANPHAM`(MSP) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `PHIEUBAOHANH` ADD CONSTRAINT FK_MKH_PHIEUBAOHANH FOREIGN KEY (MKH) REFERENCES `KHACHHANG`(MKH) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `PHIEUSUACHUA` ADD CONSTRAINT FK_MPB_PHIEUSUACHUA FOREIGN KEY (MPB) REFERENCES `PHIEUBAOHANH`(MPB) ON DELETE NO ACTION ON UPDATE NO ACTION;
*/
ALTER TABLE `PHIEUSUACHUA` ADD CONSTRAINT FK_MNV_PHIEUSUACHUA FOREIGN KEY (MNV) REFERENCES `NHANVIEN`(MNV) ON DELETE NO ACTION ON UPDATE NO ACTION;

COMMIT;
