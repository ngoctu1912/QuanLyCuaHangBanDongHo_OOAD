CREATE OR ALTER PROCEDURE dbo.UpdateNhanVienLinked
    @MNV INT,
    @HOTEN NVARCHAR(100),
    @GIOITINH INT,
    @NGAYSINH DATE,
    @SDT VARCHAR(20),
    @TT INT,
    @EMAIL VARCHAR(100),
    @MCV INT,
    @NEW_MCN VARCHAR(10),
    @SOURCE_MCN VARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @source NVARCHAR(10) = UPPER(LTRIM(RTRIM(ISNULL(@SOURCE_MCN, ''))));
    DECLARE @affected INT = 0;
    DECLARE @sql NVARCHAR(MAX);

    IF (@source = '')
    BEGIN
        UPDATE NHANVIEN
        SET HOTEN = @HOTEN,
            GIOITINH = @GIOITINH,
            NGAYSINH = @NGAYSINH,
            SDT = @SDT,
            TT = @TT,
            EMAIL = @EMAIL,
            MCV = @MCV,
            MCN = @NEW_MCN
        WHERE MNV = @MNV;

        SET @affected = @@ROWCOUNT;
        SELECT @affected AS AffectedRows;
        RETURN;
    END

    IF (@source NOT IN ('CN1', 'CN2', 'CN3'))
    BEGIN
        THROW 50001, 'Invalid SOURCE_MCN. Allowed values: CN1, CN2, CN3.', 1;
    END

    SET @sql = N'
        UPDATE ' + QUOTENAME(@source) + N'.quanlycuahangdongho.dbo.NHANVIEN
        SET HOTEN = @HOTEN,
            GIOITINH = @GIOITINH,
            NGAYSINH = @NGAYSINH,
            SDT = @SDT,
            TT = @TT,
            EMAIL = @EMAIL,
            MCV = @MCV,
            MCN = @NEW_MCN
        WHERE MNV = @MNV;
        SET @AffectedRows = @@ROWCOUNT;
    ';

    EXEC sp_executesql
        @sql,
        N'@MNV INT,
          @HOTEN NVARCHAR(100),
          @GIOITINH INT,
          @NGAYSINH DATE,
          @SDT VARCHAR(20),
          @TT INT,
          @EMAIL VARCHAR(100),
          @MCV INT,
          @NEW_MCN VARCHAR(10),
          @AffectedRows INT OUTPUT',
        @MNV = @MNV,
        @HOTEN = @HOTEN,
        @GIOITINH = @GIOITINH,
        @NGAYSINH = @NGAYSINH,
        @SDT = @SDT,
        @TT = @TT,
        @EMAIL = @EMAIL,
        @MCV = @MCV,
        @NEW_MCN = @NEW_MCN,
        @AffectedRows = @affected OUTPUT;

    SELECT @affected AS AffectedRows;
END
GO

/*
  DeleteNhanVien: soft-delete an employee either locally or on a linked branch.
  Parameters:
    @MNV INT
    @MCN VARCHAR(20)        -- current login branch (target host when doing linked)
    @SOURCE_MCN VARCHAR(20) -- source branch where the record originally resides
*/
IF OBJECT_ID('dbo.DeleteNhanVien', 'P') IS NOT NULL
    DROP PROCEDURE dbo.DeleteNhanVien;
GO

CREATE PROCEDURE dbo.DeleteNhanVien
    @MNV INT,
    @MCN VARCHAR(20),
    @SOURCE_MCN VARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @source VARCHAR(20) = UPPER(LTRIM(RTRIM(ISNULL(@SOURCE_MCN, ''))));
    DECLARE @target VARCHAR(20) = UPPER(LTRIM(RTRIM(ISNULL(@MCN, ''))));
    DECLARE @sql NVARCHAR(MAX);

    IF (@target = @source OR @source = '')
    BEGIN
        UPDATE NHANVIEN
        SET TT = -1
        WHERE MNV = @MNV;
        RETURN;
    END

    -- validate linked server exists and is linked
    IF NOT EXISTS (
        SELECT 1 FROM sys.servers s WHERE UPPER(s.name) = @target AND s.is_linked = 1
    )
    BEGIN
        THROW 50001, 'MCN không tồn tại trong linked servers.', 1;
    END

    SET @sql = N'UPDATE ' + QUOTENAME(@target) + N'.quanlycuahangdongho.dbo.NHANVIEN SET TT = -1 WHERE MNV = @MNV';
    EXEC sp_executesql @sql, N'@MNV INT', @MNV = @MNV;
END
GO
