use master
go
drop database EduSys
go
CREATE DATABASE EduSys
GO
USE EduSys
GO
CREATE TABLE NhanVien
(
	MaNV NVARCHAR(50) NOT NULL,
	MatKhau NVARCHAR(50) NOT NULL,
	HoTen NVARCHAR(50) NOT NULL,
	VaiTro BIT NOT NULL DEFAULT 0,
	PRIMARY KEY(MANV)
)
GO
CREATE TABLE ChuyenDe
(
	MaCD NCHAR(5) NOT NULL,
	TenCD NVARCHAR(50) NOT NULL,
	HocPhi FLOAT NOT NULL DEFAULT 0,
	ThoiLuong INT NOT NULL DEFAULT 30,
	Hinh NVARCHAR(50) NOT NULL DEFAULT '',
	MoTa NVARCHAR(255) NOT NULL DEFAULT '',
	UNIQUE (TenCD),
	CHECK (HocPhi>=0 and ThoiLuong>0),
	PRIMARY KEY(MaCD)
)
GO
CREATE TABLE KhoaHoc
(
	MaKH INT IDENTITY(1,1) NOT NULL,
	MaCD NCHAR(5) NOT NULL,
	HocPhi FLOAT NOT NULL,
	ThoiLuong INT NOT NULL,
	NgayKG DATE NOT NULL,
	GhiChu NVARCHAR(50) NOT NULL DEFAULT '',
	MaNV NVARCHAR(50) NOT NULL,
	NgayTao DATE NOT NULL DEFAULT getdate(),
	CHECK (HocPhi>=0 and ThoiLuong>0),
	PRIMARY KEY(MaKH),
)
GO
CREATE TABLE NguoiHoc
(
	MaNH NCHAR(7) NOT NULL,
	HoTen NVARCHAR(50) NOT NULL,
	NgaySinh DATE NOT NULL,
	GioiTinh BIT NOT NULL DEFAULT 0,
	DienThoai NVARCHAR(50) NOT NULL,
	Email NVARCHAR(50) NOT NULL,
	GhiChu NVARCHAR(max) NOT NULL DEFAULT '',
	MaNV NVARCHAR(50) NOT NULL,
	NgayDK DATE NOT NULL DEFAULT getdate(),
	PRIMARY KEY(MaNH),
)
GO
CREATE TABLE HocVien
(
	MaHV INT IDENTITY(1,1) NOT NULL,
	MaKH INT NOT NULL,
	MaNH NCHAR(7) NOT NULL,
	Diem FLOAT check (Diem>=0),
	UNIQUE(MaKH,maNH),
	PRIMARY KEY(MaHV)
)
GO
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'admin', N'123456', N'admin', 1)
GO
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'HoiNC', N'123456', N'Vòng Hồng Hội', 0)
GO
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'LucNC', N'123456', N'Dương Tấn Lực', 0)
GO
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'TeoNV', N'1234567', N'Nguyễn Văn Tèo', 1)
GO
INSERT [dbo].[NhanVien] ([MaNV], [MatKhau], [HoTen], [VaiTro]) VALUES (N'ThanhNC', N'123456', N'Phan Nguyễn Trung Thành', 0)
GO
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV01', N'Lập trình Java căn bản', 320, 65, N'JAV01.png', N'JAV01 - Lập trình Java căn bản')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV02', N'Lập trình Java nâng cao', 330, 90, N'JAV02.png', N'JAV02 - Lập trình Java nâng cao')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV03', N'Lập trình mạng với Java', 280, 70, N'JAV03.png', N'JAV03 - Lập trình mạng với Java')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'JAV04', N'Lập trình desktop với Swing', 240, 70, N'JAV04.png', N'JAV04 - Lập trình desktop với Swing')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO01', N'Dự án với công nghệ MS.NET MVC', 320, 90, N'PRO01.png', N'PRO01 - Dự án với công nghệ MS.NET MVC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO02', N'Dự án với công nghệ Spring MVC', 330, 90, N'PRO02.png', N'PRO02 - Dự án với công nghệ Spring MVC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO03', N'Dự án với công nghệ Servlet/JSP', 340, 90, N'PRO03.png', N'PRO03 - Dự án với công nghệ Servlet/JSP')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO04', N'Dự án với AngularJS & WebAPI', 290, 90, N'PRO04.png', N'PRO04 - Dự án với AngularJS & WebAPI')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO05', N'Dự án với Swing & JDBC', 380, 90, N'PRO05.png', N'PRO05 - Dự án với Swing & JDBC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'PRO06', N'Dự án với WindowForm', 320, 90, N'PRO06.png', N'PRO06 - Dự án với WindowForm')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'RDB01', N'Cơ sở dữ liệu SQL Server', 180, 50, N'RDB01.png', N'RDB01 - Cơ sở dữ liệu SQL Server')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'RDB02', N'Lập trình JDBC', 150, 60, N'RDB02.png', N'RDB02 - Lập trình JDBC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'RDB03', N'Lập trình cơ sở dữ liệu Hibernate', 250, 80, N'RDB03.png', N'RDB03 - Lập trình cơ sở dữ liệu Hibernate')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER01', N'Lập trình web với Servlet/JSP', 350, 100, N'SER01.png', N'SER01 - Lập trình web với Servlet/JSP')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER02', N'Lập trình Spring MVC', 420, 110, N'SER02.png', N'SER02 - Lập trình Spring MVC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER03', N'Lập trình MS.NET MVC', 410, 110, N'SER03.png', N'SER03 - Lập trình MS.NET MVC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'SER04', N'Xây dựng Web API với Spring MVC & ASP.NET MVC', 230, 70, N'SER04.png', N'SER04 - Xây dựng Web API với Spring MVC & ASP.NET MVC')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB01', N'Thiết kế web với HTML và CSS', 270, 70, N'WEB02.png', N'WEB01 - Thiết kế web với HTML và CSS')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB02', N'Thiết kế web với Bootstrap', 110, 40, N'WEB02.png', N'WEB02 - Thiết kế web với Bootstrap')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB03', N'Lập trình front-end với JavaScript và jQuery', 150, 60, N'WEB03.png', N'WEB03 - Lập trình front-end với JavaScript và jQuery')
GO
INSERT [dbo].[ChuyenDe] ([MaCD], [TenCD], [HocPhi], [ThoiLuong], [Hinh], [MoTa]) VALUES (N'WEB04', N'Lập trình AngularJS', 250, 80, N'WEB04.png', N'WEB04 - Lập trình AngularJS')
GO

SET IDENTITY_INSERT [dbo].[KhoaHoc] ON 
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (1, N'WEB04', 300, 90, CAST(N'2020-09-24' AS Date), N'', N'HoiNC', CAST(N'2020-09-19' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (2, N'JAV03', 300, 70, CAST(N'2020-09-15' AS Date), N'', N'LucNC', CAST(N'2020-09-10' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (3, N'JAV04', 300, 90, CAST(N'2020-09-16' AS Date), N'', N'LucNC', CAST(N'2020-09-11' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (4, N'JAV02', 250, 90, CAST(N'2020-09-17' AS Date), N'', N'LucNC', CAST(N'2020-09-12' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (5, N'PRO06', 250, 90, CAST(N'2020-09-18' AS Date), N'', N'ThanhNC', CAST(N'2020-09-13' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (6, N'PRO04', 250, 90, CAST(N'2020-09-19' AS Date), N'', N'ThanhNC', CAST(N'2020-09-14' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (7, N'SER01', 350, 90, CAST(N'2020-09-20' AS Date), N'', N'ThanhNC', CAST(N'2020-09-15' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (8, N'SER02', 250, 90, CAST(N'2020-09-21' AS Date), N'', N'HoiNC', CAST(N'2020-09-16' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (9, N'WEB02', 350, 90, CAST(N'2020-09-22' AS Date), N'', N'HoiNC', CAST(N'2020-09-17' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (10, N'WEB03', 300, 90, CAST(N'2020-09-23' AS Date), N'', N'HoiNC', CAST(N'2020-09-18' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (11, N'JAV02', 250, 90, CAST(N'2017-01-11' AS Date), N'JAV02(42746)', N'ThanhNC', CAST(N'2016-12-27' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (12, N'PRO04', 380, 90, CAST(N'2015-05-23' AS Date), N'PRO04(42147)', N'HoiNC', CAST(N'2015-05-08' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (13, N'SER03', 220, 75, CAST(N'2015-01-27' AS Date), N'SER03(42031)', N'HoiNC', CAST(N'2015-01-12' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (14, N'JAV02', 310, 60, CAST(N'2017-09-06' AS Date), N'JAV02(42984)', N'HoiNC', CAST(N'2017-08-22' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (15, N'PRO05', 370, 105, CAST(N'2015-05-18' AS Date), N'PRO05(42142)', N'LucNC', CAST(N'2015-05-03' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (16, N'PRO02', 350, 60, CAST(N'2015-06-19' AS Date), N'PRO02(42174)', N'LucNC', CAST(N'2015-06-04' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (17, N'SER01', 270, 75, CAST(N'2016-03-14' AS Date), N'SER01(42443)', N'HoiNC', CAST(N'2016-02-28' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (18, N'PRO01', 300, 45, CAST(N'2017-06-02' AS Date), N'PRO01(42888)', N'ThanhNC', CAST(N'2017-05-18' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (19, N'PRO06', 360, 75, CAST(N'2019-07-27' AS Date), N'PRO06(43673)', N'ThanhNC', CAST(N'2019-07-12' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (20, N'WEB01', 280, 45, CAST(N'2015-02-27' AS Date), N'WEB01(42062)', N'LucNC', CAST(N'2015-02-12' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (21, N'WEB02', 270, 45, CAST(N'2016-09-02' AS Date), N'WEB02(42615)', N'LucNC', CAST(N'2016-08-18' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (22, N'RDB03', 310, 120, CAST(N'2019-07-24' AS Date), N'RDB03(43670)', N'HoiNC', CAST(N'2019-07-09' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (23, N'RDB01', 390, 120, CAST(N'2018-05-01' AS Date), N'RDB01(43221)', N'ThanhNC', CAST(N'2018-04-16' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (24, N'PRO03', 360, 90, CAST(N'2018-02-17' AS Date), N'PRO03(43148)', N'HoiNC', CAST(N'2018-02-02' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (25, N'JAV03', 350, 90, CAST(N'2015-11-16' AS Date), N'JAV03(42324)', N'HoiNC', CAST(N'2015-11-01' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (26, N'JAV01', 210, 75, CAST(N'2017-11-13' AS Date), N'JAV01(43052)', N'HoiNC', CAST(N'2017-10-29' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (27, N'PRO01', 330, 45, CAST(N'2016-09-21' AS Date), N'PRO01(42634)', N'ThanhNC', CAST(N'2016-09-06' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (28, N'PRO01', 250, 120, CAST(N'2017-05-19' AS Date), N'PRO01(42874)', N'HoiNC', CAST(N'2017-05-04' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (29, N'PRO06', 370, 75, CAST(N'2019-09-06' AS Date), N'PRO06(43714)', N'ThanhNC', CAST(N'2019-08-22' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (30, N'JAV03', 230, 75, CAST(N'2019-05-26' AS Date), N'JAV03(43611)', N'LucNC', CAST(N'2019-05-11' AS Date))
GO
INSERT [dbo].[KhoaHoc] ([MaKH], [MaCD], [HocPhi], [ThoiLuong], [NgayKG], [GhiChu], [MaNV], [NgayTao]) VALUES (31, N'SER03', 380, 90, CAST(N'2017-11-17' AS Date), N'SER03(43056)', N'HoiNC', CAST(N'2017-11-02' AS Date))
GO
SET IDENTITY_INSERT [dbo].[KhoaHoc] OFF
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PG00679', N'Lê Thị Ngọc Hạnh', CAST(N'1999-09-19' AS Date), 0, N'0975834576', N'PG00679@fpt.edu.com', N'PG00679 - Lê Thị Ngọc Hạnh', N'LucNC', CAST(N'2015-01-08' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11012', N'Chế Thành Đại', CAST(N'2001-10-15' AS Date), 1, N'0982855882', N'PS11012@fpt.edu.com', N'PS11012 - Chế Thành Đại', N'LucNC', CAST(N'2019-12-18' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11419', N'Đoàn Công Thành', CAST(N'2002-06-09' AS Date), 1, N'0938357735', N'PS11419@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2019-04-14' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11478', N'Trần Phúc Thịnh', CAST(N'2002-12-30' AS Date), 1, N'0946219377', N'PS11478@fpt.edu.vn', N' ', N'LucNC', CAST(N'2018-08-25' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11503', N'Kiều Tiến Đạt', CAST(N'2000-12-24' AS Date), 1, N'0938357735', N'PS11503@fpt.edu.vn', N' ', N'LucNC', CAST(N'2020-11-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11518', N'Đinh Văn Tâm', CAST(N'2000-04-13' AS Date), 1, N'0924696779', N'PS11518@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2019-06-01' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11604', N'Trần Nhựt Duy', CAST(N'2001-03-10' AS Date), 1, N'0912880267', N'PS11604@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2018-08-14' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11609', N'Nguyễn Huy Hoàng', CAST(N'2001-11-10' AS Date), 1, N'0962030316', N'PS11609@fpt.edu.vn', N' ', N'LucNC', CAST(N'2018-03-04' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11618', N'Nguyễn Đức Anh', CAST(N'2002-11-08' AS Date), 1, N'0990588721', N'PS11618@fpt.edu.com', N'PS11618 - Nguyễn Đức Anh', N'HoiNC', CAST(N'2018-02-05' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11636', N'Nguyễn Ngọc Bảo', CAST(N'2002-04-26' AS Date), 1, N'0973203388', N'PS11636@fpt.edu.com', N'PS11636 - Nguyễn Ngọc Bảo', N'HoiNC', CAST(N'2019-03-01' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11681', N'Đặng Thanh Duy', CAST(N'2002-04-05' AS Date), 1, N'0912277868', N'PS11681@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2020-03-20' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11719', N'Trần Quang Nhân', CAST(N'2000-02-02' AS Date), 1, N'0984605773', N'PS11719@fpt.edu.com', N'PS11719 - Trần Quang Nhân', N'HoiNC', CAST(N'2017-12-27' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11766', N'Nguyễn Trường Vũ', CAST(N'2000-05-14' AS Date), 1, N'0958729899', N'PS11766@fpt.edu.com', N'PS11766 - Nguyễn Trường Vũ', N'LucNC', CAST(N'2019-08-02' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS11906', N'Triệu Văn Sơn', CAST(N'2001-07-18' AS Date), 1, N'0914082094', N'PS11906@fpt.edu.vn', N' ', N'LucNC', CAST(N'2018-01-29' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS12422', N'Phan Thị Thẩm Hằng', CAST(N'2001-05-03' AS Date), 0, N'0997845999', N'PS12422@fpt.edu.com', N'PS12422 - Phan Thị Thẩm Hằng', N'ThanhNC', CAST(N'2020-07-31' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS12464', N'Đỗ Thảo Ly', CAST(N'2001-01-29' AS Date), 0, N'0930260679', N'PS12464@fpt.edu.vn', N' ', N'LucNC', CAST(N'2020-08-10' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS12651', N'Nguyễn Minh Vương', CAST(N'1998-04-08' AS Date), 1, N'0911637415', N'PS12651@fpt.edu.vn', N' ', N'LucNC', CAST(N'2019-08-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS13683', N'Nguyễn Hoàng Nhật Tân', CAST(N'1998-12-20' AS Date), 1, N'0940711328', N'PS13683@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2020-03-31' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS13736', N'Huỳnh Nguyễn Phúc', CAST(N'1999-07-18' AS Date), 1, N'0918161783', N'PS13736@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2019-11-06' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS13891', N'Phạm Duy Nam', CAST(N'2000-11-09' AS Date), 1, N'0910165437', N'PS13891@fpt.edu.com', N'PS13891 - Phạm Duy Nam', N'ThanhNC', CAST(N'2016-02-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14009', N'Nguyễn Ngọc Huy', CAST(N'2001-01-05' AS Date), 1, N'0918563549', N'PS14009@fpt.edu.com', N'PS14009 - Nguyễn Ngọc Huy', N'LucNC', CAST(N'2020-01-15' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14211', N'Lâm Gia Thuận', CAST(N'2001-09-17' AS Date), 1, N'0917886371', N'PS14211@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2019-10-09' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14266', N'Nguyễn Hữu Phương Uyên', CAST(N'2000-07-17' AS Date), 1, N'0958550028', N'PS14266@fpt.edu.com', N'PS14266 - Nguyễn Hữu Phương Uyên', N'HoiNC', CAST(N'2020-12-31' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14354', N'Trần Lê Nhã Uyên', CAST(N'1999-08-28' AS Date), 1, N'0941355287', N'PS14354@fpt.edu.com', N'PS14354 - Trần Lê Nhã Uyên', N'ThanhNC', CAST(N'2017-04-24' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14421', N'Tô Phan Khánh Ngọc', CAST(N'2000-08-10' AS Date), 1, N'0955007036', N'PS14421@fpt.edu.com', N'PS14421 - Tô Phan Khánh Ngọc', N'ThanhNC', CAST(N'2018-08-01' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14572', N'Nguyễn Mai Thu Cúc', CAST(N'2002-01-16' AS Date), 1, N'0969705191', N'PS14572@fpt.edu.com', N'PS14572 - Nguyễn Mai Thu Cúc', N'ThanhNC', CAST(N'2019-02-24' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14579', N'Nguyễn Nhật Cao Thăng', CAST(N'1998-10-30' AS Date), 1, N'0916050164', N'PS14579@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2020-04-30' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14601', N'Phạm Mai Khanh', CAST(N'2002-06-11' AS Date), 1, N'0928554469', N'PS14601@fpt.edu.com', N'PS14601 - Phạm Mai Khanh', N'LucNC', CAST(N'2019-02-03' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14667', N'Nguyễn Doãn Thắng', CAST(N'1999-10-03' AS Date), 1, N'0973118567', N'PS14667@fpt.edu.com', N'PS14667 - Nguyễn Doãn Thắng', N'ThanhNC', CAST(N'2017-03-16' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14692', N'Trần Nguyên Hải', CAST(N'1998-08-02' AS Date), 1, N'0991509408', N'PS14692@fpt.edu.vn', N' ', N'LucNC', CAST(N'2020-02-16' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14707', N'Nguyễn Quang Huy', CAST(N'1998-10-27' AS Date), 1, N'0914531913', N'PS14707@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2020-03-24' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS14733', N'Bùi Đức Minh', CAST(N'2002-06-16' AS Date), 1, N'0927771672', N'PS14733@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2019-03-24' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15048', N'Đỗ Tiến Thịnh', CAST(N'2001-06-24' AS Date), 1, N'0912234437', N'PS15048@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2020-03-21' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15059', N'Từ Khương Duy', CAST(N'2000-09-25' AS Date), 1, N'0948238929', N'PS15059@fpt.edu.com', N'PS15059 - Từ Khương Duy', N'ThanhNC', CAST(N'2019-07-02' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15078', N'Nguyễn Thị Thanh Hiền', CAST(N'2001-03-13' AS Date), 1, N'0922569595', N'PS15078@fpt.edu.com', N'PS15078 - Nguyễn Thị Thanh Hiền', N'HoiNC', CAST(N'2018-02-14' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15254', N'Hoàng Minh Tiệp', CAST(N'2001-05-09' AS Date), 1, N'0955806177', N'PS15254@fpt.edu.com', N'PS15254 - Hoàng Minh Tiệp', N'ThanhNC', CAST(N'2020-12-06' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15492', N'Chu Nguyễn Cẩm Phương', CAST(N'1999-04-05' AS Date), 1, N'0972550776', N'PS15492@fpt.edu.com', N'PS15492 - Chu Nguyễn Cẩm Phương', N'ThanhNC', CAST(N'2018-06-03' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15695', N'Huỳnh Phạm Yến Ngọc', CAST(N'1999-04-17' AS Date), 1, N'0934966366', N'PS15695@fpt.edu.com', N'PS15695 - Huỳnh Phạm Yến Ngọc', N'HoiNC', CAST(N'2018-06-21' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15786', N'Phạm Công Việt', CAST(N'2000-11-27' AS Date), 1, N'0902286971', N'PS15786@fpt.edu.com', N'PS15786 - Phạm Công Việt', N'LucNC', CAST(N'2020-11-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS15979', N'Trần Thanh Thuận', CAST(N'2001-07-30' AS Date), 1, N'0984499166', N'PS15979@fpt.edu.com', N'PS15979 - Trần Thanh Thuận', N'ThanhNC', CAST(N'2018-05-31' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16014', N'Võ Thị Mỹ Linh', CAST(N'1999-10-25' AS Date), 0, N'0994004134', N'PS16014@fpt.edu.com', N'PS16014 - Võ Thị Mỹ Linh', N'LucNC', CAST(N'2020-03-14' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16217', N'Trần Tấn Phát', CAST(N'2002-07-06' AS Date), 1, N'0920397511', N'PS16217@fpt.edu.com', N'PS16217 - Trần Tấn Phát', N'HoiNC', CAST(N'2018-03-13' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16398', N'Nguyễn Bằng Phi', CAST(N'2000-07-13' AS Date), 1, N'0910545901', N'PS16398@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2019-11-26' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16425', N'Nguyễn Tiến Anh Khoa', CAST(N'2002-03-16' AS Date), 1, N'0905182347', N'PS16425@fpt.edu.com', N'PS16425 - Nguyễn Tiến Anh Khoa', N'LucNC', CAST(N'2015-10-29' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16521', N'Phan Văn Vương', CAST(N'1999-12-04' AS Date), 1, N'0936608066', N'PS16521@fpt.edu.com', N'PS16521 - Phan Văn Vương', N'ThanhNC', CAST(N'2016-06-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16590', N'Nguyễn Lê Duy Tiến', CAST(N'2001-01-04' AS Date), 1, N'0918358164', N'PS16590@fpt.edu.vn', N' ', N'LucNC', CAST(N'2019-04-28' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS16902', N'Dương Tuấn Kiệt', CAST(N'1999-12-03' AS Date), 1, N'0973750254', N'PS16902@fpt.edu.com', N'PS16902 - Dương Tuấn Kiệt', N'HoiNC', CAST(N'2019-04-24' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS17263', N'Phạm Nguyễn Minh Triết', CAST(N'2002-10-04' AS Date), 1, N'0939020097', N'PS17263@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2018-08-13' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS17380', N'Lê Việt Tiến', CAST(N'2001-09-20' AS Date), 1, N'0999794115', N'PS17380@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2019-08-31' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS17432', N'Hà Văn Cường', CAST(N'2001-02-06' AS Date), 0, N'0916264970', N'PS17432@fpt.edu.com', N'PS17432 - Hà Văn Cường', N'LucNC', CAST(N'2015-03-09' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18239', N'Đào Đức Minh Khôi', CAST(N'2000-10-08' AS Date), 1, N'0905992611', N'PS18239@fpt.edu.com', N'PS18239 - Đào Đức Minh Khôi', N'LucNC', CAST(N'2015-07-12' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18404', N'Vòng Nhật Hòa', CAST(N'2002-09-07' AS Date), 1, N'0951049442', N'PS18404@fpt.edu.com', N'PS18404 - Vòng Nhật Hòa', N'LucNC', CAST(N'2019-03-20' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18597', N'Trương Anh Thọ', CAST(N'2000-01-08' AS Date), 1, N'0960620997', N'PS18597@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2020-12-25' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18650', N'Nguyên Phúc Hậu', CAST(N'2001-07-22' AS Date), 1, N'0943043273', N'PS18650@fpt.edu.com', N'PS18650 - Nguyên Phúc Hậu', N'ThanhNC', CAST(N'2020-01-25' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18709', N'Nguyễn Hữu Phúc', CAST(N'1999-08-25' AS Date), 1, N'0928365692', N'PS18709@fpt.edu.com', N'PS18709 - Nguyễn Hữu Phúc', N'HoiNC', CAST(N'2016-01-17' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18713', N'Đàm Đình Cường', CAST(N'2002-03-26' AS Date), 1, N'0918182551', N'PS18713@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2019-04-14' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18730', N'Nguyễn Lê Hồng Ngọc', CAST(N'2000-06-20' AS Date), 1, N'0979118924', N'PS18730@fpt.edu.com', N'PS18730 - Nguyễn Lê Hồng Ngọc', N'LucNC', CAST(N'2020-10-31' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS18941', N'Lê Hà Vy', CAST(N'2001-09-06' AS Date), 0, N'0978980765', N'PS18941@fpt.edu.com', N'PS18941 - Lê Hà Vy', N'HoiNC', CAST(N'2015-11-24' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19001', N'Đỗ Thị Anh Thư', CAST(N'1999-05-21' AS Date), 0, N'0907664003', N'PS19001@fpt.edu.com', N'PS19001 - Đỗ Thị Anh Thư', N'ThanhNC', CAST(N'2019-04-04' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19009', N'Trương Vĩnh Nghi', CAST(N'1998-05-08' AS Date), 1, N'0941528106', N'PS19009@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2019-08-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19064', N'Nguyễn Như Tài', CAST(N'2000-06-12' AS Date), 1, N'0948199564', N'PS19064@fpt.edu.vn', N' ', N'HoiNC', CAST(N'2019-07-17' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19077', N'Đỗ Thụy Du', CAST(N'2002-10-27' AS Date), 1, N'0908436207', N'PS19077@fpt.edu.com', N'PS19077 - Đỗ Thụy Du', N'ThanhNC', CAST(N'2020-07-20' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19083', N'Từ Tú Hào', CAST(N'1999-03-25' AS Date), 1, N'0938578125', N'PS19083@fpt.edu.com', N'PS19083 - Từ Tú Hào', N'ThanhNC', CAST(N'2020-07-07' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19182', N'Nguyễn Thị Thủy Tiên', CAST(N'1999-06-13' AS Date), 1, N'0960160764', N'PS19182@fpt.edu.com', N'PS19182 - Nguyễn Thị Thủy Tiên', N'HoiNC', CAST(N'2020-09-22' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19205', N'Nguyễn Bá Nguyên Chương', CAST(N'2000-06-04' AS Date), 1, N'0971067434', N'PS19205@fpt.edu.com', N'PS19205 - Nguyễn Bá Nguyên Chương', N'ThanhNC', CAST(N'2020-02-03' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19229', N'Trần Hoàng Thúy Vân', CAST(N'2001-03-21' AS Date), 0, N'0937891282', N'PS19229@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2018-02-09' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19244', N'Phạm Bá Trung', CAST(N'1999-04-28' AS Date), 1, N'0915134551', N'PS19244@fpt.edu.vn', N' ', N'ThanhNC', CAST(N'2019-08-03' AS Date))
GO
INSERT [dbo].[NguoiHoc] ([MaNH], [HoTen], [NgaySinh], [GioiTinh], [DienThoai], [Email], [GhiChu], [MaNV], [NgayDK]) VALUES (N'PS19360', N'Phạm Phú Nhật Minh', CAST(N'2000-12-19' AS Date), 1, N'0973425310', N'PS19360@fpt.edu.com', N'PS19360 - Phạm Phú Nhật Minh', N'HoiNC', CAST(N'2018-10-08' AS Date))
GO
SET IDENTITY_INSERT [dbo].[HocVien] ON 
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (1, 1, N'PS11478', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (2, 3, N'PS14692', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (3, 3, N'PS19244', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (4, 1, N'PS14211', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (5, 4, N'PS18597', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (6, 3, N'PS11604', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (7, 4, N'PS11681', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (8, 1, N'PS14579', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (9, 3, N'PS15048', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (10, 2, N'PS19229', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (11, 3, N'PS11609', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (12, 2, N'PS12651', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (14, 2, N'PS14692', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (15, 2, N'PS19244', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (17, 1, N'PS11503', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (18, 4, N'PS11419', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (19, 6, N'PS14707', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (20, 1, N'PS17380', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (21, 1, N'PS11681', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (24, 3, N'PS19229', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (25, 3, N'PS13683', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (26, 4, N'PS17263', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (27, 4, N'PS14733', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (28, 2, N'PS16590', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (29, 2, N'PS12464', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (30, 4, N'PS11906', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (31, 4, N'PS11478', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (33, 1, N'PS19244', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (34, 3, N'PS14211', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (35, 3, N'PS18597', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (36, 4, N'PS11604', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (39, 4, N'PS15048', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (40, 1, N'PS19229', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (41, 4, N'PS11609', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (42, 6, N'PS12651', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (43, 3, N'PS11478', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (49, 1, N'PS14707', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (50, 4, N'PS17380', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (51, 2, N'PS11681', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (52, 4, N'PS14579', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (53, 1, N'PS15048', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (54, 4, N'PS19229', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (56, 2, N'PS17263', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (57, 6, N'PS14733', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (58, 1, N'PS16590', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (59, 4, N'PS12464', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (60, 2, N'PS11906', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (61, 16, N'PS11618', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (62, 16, N'PS18650', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (63, 29, N'PS17380', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (64, 24, N'PS15695', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (65, 25, N'PS11766', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (66, 18, N'PS11503', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (67, 11, N'PS19009', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (68, 10, N'PS19182', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (69, 10, N'PS18597', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (70, 31, N'PS18650', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (71, 14, N'PS18239', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (72, 14, N'PS11766', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (73, 7, N'PS17380', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (74, 26, N'PS18404', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (75, 21, N'PS11636', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (76, 15, N'PS16217', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (77, 14, N'PS19009', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (78, 5, N'PS18597', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (79, 9, N'PS18730', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (80, 16, N'PS11478', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (81, 8, N'PS14211', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (82, 6, N'PS15979', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (83, 11, N'PS14667', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (84, 30, N'PS11503', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (85, 15, N'PS14667', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (86, 25, N'PS17380', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (87, 11, N'PS19083', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (88, 16, N'PS19360', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (89, 16, N'PS14667', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (90, 7, N'PS19083', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (91, 28, N'PS19229', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (92, 31, N'PS17263', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (93, 17, N'PS18239', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (94, 23, N'PS15254', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (95, 5, N'PS11012', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (96, 13, N'PS17432', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (97, 27, N'PS12464', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (98, 17, N'PS16902', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (99, 31, N'PS16398', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (100, 30, N'PS16521', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (101, 21, N'PS19001', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (102, 10, N'PS15492', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (103, 12, N'PS11503', null )
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (104, 10, N'PS15786', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (105, 26, N'PS19064', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (106, 11, N'PS19064', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (107, 27, N'PS14354', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (108, 14, N'PS11618', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (109, 11, N'PS11419', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (110, 16, N'PS11419', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (111, 22, N'PS19064', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (112, 28, N'PG00679', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (113, 16, N'PS19229', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (114, 6, N'PS13736', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (115, 25, N'PS19064', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (116, 27, N'PG00679', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (117, 8, N'PS15492', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (118, 11, N'PS15979', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (119, 27, N'PS14579', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (120, 14, N'PS18941', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (121, 17, N'PS19077', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (122, 25, N'PS13683', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (123, 10, N'PS14733', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (124, 8, N'PS18650', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (125, 16, N'PS16590', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (126, 8, N'PS19182', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (127, 25, N'PS19001', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (128, 15, N'PS16014', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (129, 24, N'PS19182', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (130, 11, N'PS11609', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (131, 8, N'PS14692', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (132, 19, N'PS15078', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (133, 25, N'PS18404', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (135, 26, N'PS12651', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (136, 29, N'PS19009', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (137, 24, N'PS13683', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (138, 12, N'PS18404', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (139, 26, N'PS16902', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (140, 24, N'PS16590', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (141, 15, N'PS14266', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (142, 12, N'PS14354', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (143, 5, N'PS19360', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (144, 19, N'PS12422', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (145, 11, N'PS18713', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (146, 17, N'PS14572', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (147, 14, N'PS19244', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (148, 25, N'PS17432', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (149, 31, N'PS11604', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (150, 26, N'PS15254', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (151, 27, N'PS19205', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (152, 20, N'PS14579', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (153, 23, N'PS11766', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (154, 27, N'PS16014', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (155, 27, N'PS19009', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (156, 8, N'PG00679', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (157, 11, N'PS19205', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (159, 11, N'PS14421', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (160, 18, N'PS18404', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (161, 27, N'PS19001', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (162, 13, N'PS19205', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (163, 13, N'PG00679', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (164, 13, N'PS13683', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (165, 10, N'PS14692', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (166, 21, N'PS18941', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (168, 7, N'PS16521', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (169, 22, N'PS11609', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (170, 30, N'PS18597', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (171, 15, N'PS11419', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (172, 7, N'PS13683', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (173, 5, N'PS16521', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (174, 20, N'PS15254', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (175, 27, N'PS16521', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (176, 30, N'PS11012', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (177, 8, N'PS13683', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (178, 23, N'PS18713', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (179, 11, N'PS18239', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (180, 16, N'PS14572', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (181, 30, N'PS12464', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (182, 15, N'PS15786', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (183, 15, N'PS11604', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (184, 24, N'PS18239', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (185, 16, N'PG00679', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (187, 9, N'PS15492', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (188, 6, N'PS14579', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (189, 17, N'PS19064', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (191, 28, N'PS15492', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (192, 13, N'PS11766', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (193, 12, N'PS11681', 1)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (194, 14, N'PS15048', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (195, 11, N'PS16425', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (196, 20, N'PS11503', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (197, 17, N'PS16521', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (198, 30, N'PS12651', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (200, 9, N'PS11478', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (201, 26, N'PS19182', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (203, 28, N'PS14667', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (204, 29, N'PS11419', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (205, 31, N'PS14707', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (206, 11, N'PS18597', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (207, 15, N'PS19083', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (208, 26, N'PS18650', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (209, 26, N'PS15979', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (210, 19, N'PS15695', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (211, 20, N'PS18239', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (212, 19, N'PS11012', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (213, 26, N'PS14692', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (214, 24, N'PS16217', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (215, 21, N'PS18597', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (216, 24, N'PS14667', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (217, 8, N'PS18730', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (218, 16, N'PS18941', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (219, 19, N'PS14733', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (220, 8, N'PS14009', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (221, 28, N'PS11503', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (222, 31, N'PS14266', 10)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (223, 16, N'PS14211', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (224, 30, N'PS11478', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (225, 24, N'PS14733', 5)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (227, 6, N'PS17432', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (228, 27, N'PS15059', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (229, 18, N'PS11609', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (230, 23, N'PS11609', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (231, 13, N'PS16014', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (232, 23, N'PG00679', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (233, 25, N'PS14733', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (234, 29, N'PS15786', 9)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (236, 21, N'PS14733', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (237, 12, N'PS19182', 4)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (238, 14, N'PS15979', null)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (239, 28, N'PS13891', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (240, 20, N'PS16902', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (241, 14, N'PS18650', 2)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (242, 12, N'PS11618', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (243, 5, N'PS14579', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (244, 7, N'PS18404', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (245, 17, N'PS14579', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (246, 28, N'PS19360', 7)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (247, 13, N'PS19244', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (248, 11, N'PS14266', 8)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (250, 16, N'PS19182', 0)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (251, 26, N'PS15048', 6)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (252, 10, N'PS11906', 3)
GO
INSERT [dbo].[HocVien] ([MaHV], [MaKH], [MaNH], [Diem]) VALUES (253, 22, N'PS19182', 9)
GO
SET IDENTITY_INSERT [dbo].[HocVien] OFF
GO


ALTER TABLE dbo.KhoaHoc  WITH CHECK ADD  CONSTRAINT FK_KhoaHoc_ChuyenDe FOREIGN KEY(MaCD)
REFERENCES dbo.ChuyenDe (MaCD)
ON UPDATE CASCADE
ALTER TABLE dbo.KhoaHoc  WITH CHECK ADD  CONSTRAINT FK_KhoaHoc_NhanVien FOREIGN KEY(MaNV)
REFERENCES dbo.NhanVien (MaNV)
ON UPDATE CASCADE

ALTER TABLE dbo.NguoiHoc  WITH CHECK ADD  CONSTRAINT FK_NguoiHoc_NhanVien FOREIGN KEY(MaNV)
REFERENCES dbo.NhanVien (MaNV)

ALTER TABLE dbo.HocVien  WITH CHECK ADD  CONSTRAINT FK_HocVien_KhoaHoc FOREIGN KEY(MaKH)
REFERENCES dbo.KhoaHoc (MaKH)
ON UPDATE CASCADE
ON DELETE CASCADE
ALTER TABLE dbo.HocVien  WITH CHECK ADD  CONSTRAINT FK_HocVien_NguoiHoc FOREIGN KEY(MaNH)
REFERENCES dbo.NguoiHoc (MaNH)
ON UPDATE CASCADE
GO
--Thủ tục lưu xem bảng điểm sp_BangDiem
CREATE  PROC  sp_BangDiem(@MaKH  INT) 
AS  BEGIN
	SELECT 
		nh.MaNH, 
		nh.HoTen, 
		hv.Diem
	FROM  HocVien  hv
		JOIN  NguoiHoc  nh  ON  nh.MaNH=hv.MaNH 
	WHERE  hv.MaKH  =  @MaKH
	ORDER  BY  hv.Diem  DESC
END
GO
--Thủ tục lưu số người học từng năm sp_ThongKeNguoiHoc
--Drop  PROC  sp_ThongKeNguoiHoc 
CREATE  PROC  sp_ThongKeNguoiHoc 
AS  BEGIN
	SELECT
		YEAR(NgayDK)  Nam, 
		COUNT(*)  SoLuong, 
		MIN(NgayDK)  DauTien, 
		MAX(NgayDK)  CuoiCung
	FROM  NguoiHoc
	GROUP  BY  YEAR(NgayDK)
END
GO
--Thủ tục lưu doanh thu theo chuyên đề sp_ThongKeDoanhThu
CREATE  PROC  sp_ThongKeDoanhThu(@Year  INT) 
AS  BEGIN
	SELECT
		TenCD  ChuyenDe, 
		COUNT(DISTINCT  kh.MaKH)  SoKH, 
		COUNT(hv.MaHV)  SoHV,
		SUM(kh.HocPhi)  DoanhThu, 
		MIN(kh.HocPhi)  ThapNhat, 
		MAX(kh.HocPhi)  CaoNhat, 
		AVG(kh.HocPhi)  TrungBinh
	FROM KhoaHoc kh
		JOIN  HocVien  hv  ON  kh.MaKH=hv.MaKH 
		JOIN  ChuyenDe  cd  ON  cd.MaCD=kh.MaCD
	WHERE  YEAR(NgayKG)  =  @Year GROUP BY TenCD
END
GO
--Thủ tục lưu tổng hợp điểm học viên theo chuyên đề sp_ThongKeDiem
CREATE  PROC  sp_ThongKeDiem 
AS  BEGIN
	SELECT
		TenCD  ChuyenDe, 
		COUNT(MaHV)  SoHV,
		MIN(Diem)  ThapNhat, 
		MAX(Diem)  CaoNhat, 
		AVG(Diem)  TrungBinh
	FROM KhoaHoc kh
		JOIN  HocVien  hv  ON  kh.MaKH=hv.MaKH 
		JOIN  ChuyenDe  cd  ON  cd.MaCD=kh.MaCD
	GROUP BY TenCD
END


