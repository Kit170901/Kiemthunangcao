/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.HocVienDAO;
import com.edusys.dao.KhoaHocDAO;
import com.edusys.dao.NguoiHocDAO;
import com.edusys.dao.ThongKeDAO;
import com.edusys.entity.KhoaHoc;
import com.edusys.entity.NguoiHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.Contraints;
import com.edusys.utils.MailSender;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import com.edusys.utils.XFile;
import com.edusys.utils.XImage;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class ThongKeJDialog extends javax.swing.JDialog {

    /**
     * Creates new form NhanVienJFrame
     *
     * @param parent
     * @param modal
     */
    Frame parent;

    public ThongKeJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        init();
    }

    private void init() {
        this.setLocationRelativeTo(null);
        this.setIconImage(XImage.getAppIcon());
        this.setTitle("TỔNG HỢP VÀ THỐNG KÊ");
        this.getContentPane().setBackground(Contraints.BG_COLOR);

        tabs.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/envelop.png")));
        tabs.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/lecture.png")));
        tabs.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/podium.png")));
        tabs.setIconAt(3, new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/money-bag.png")));

        if (!Auth.isManager()) {
            tabs.remove(3);
        }
        fillComboBoxKhoaHoc();
        fillComboBoxNam();
        fillTableBangDiem();
        fillTableTKNguoiHoc();
        fillTableTKDiemChuyenDe();
        fillTableTKDoanhThu();
    }
    KhoaHocDAO KHDao = new KhoaHocDAO();
    ThongKeDAO TKdao = new ThongKeDAO();
    HocVienDAO HVDao = new HocVienDAO();

    void selectTab(int index) {
        tabs.setSelectedIndex(index);
    }

    void fillComboBoxKhoaHoc() {
        List<KhoaHoc> list = new ArrayList<>();
        DefaultComboBoxModel model1 = (DefaultComboBoxModel) cbxKhoaHoc.getModel();
        try {
            model1.removeAllElements();
            list = KHDao.selectAll();
            for (KhoaHoc kh : list) {
                model1.addElement(kh);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void fillComboBoxNam() {
        DefaultComboBoxModel model1 = (DefaultComboBoxModel) cbxNam.getModel();
        try {
            model1.removeAllElements();
            List<Object[]> list = TKdao.getLuongNguoiHoc();
            list.forEach((row) -> {
                model1.addElement(row[0]);
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void fillTableBangDiem() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblBangDiem.getModel();
            model.setRowCount(0);
            KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
            List<Object[]> list = TKdao.getBangDiem(kh.getMaKH());
            list.forEach((row) -> {
                double diem=-1;
                String xepLoai=null;
                if (row[2] != null) {
                    diem = (double) row[2];
                    diem = (double) Math.round(diem * 100) / 100;
                    xepLoai = getXepLoai(diem);
                }
                model.addRow(new Object[]{row[0], row[1], diem==-1?null:diem,xepLoai});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void fillTableTKNguoiHoc() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblTKNguoiHoc.getModel();
            model.setRowCount(0);
            List<Object[]> list = TKdao.getLuongNguoiHoc();
            list.forEach((row) -> {
                model.addRow(new Object[]{row[0], row[1], XDate.toString((Date)row[2], "dd/MM/yyy"), XDate.toString((Date)row[3], "dd/MM/yyy")});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void fillTableTKDiemChuyenDe() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblTKChuyenDe.getModel();
            model.setRowCount(0);
            List<Object[]> list = TKdao.getDiemChuyenDe();
            list.forEach((row) -> {
                double diem = (double) row[4];
                diem = (double) Math.round(diem * 100) / 100;
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], diem});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void fillTableTKDoanhThu() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblTKDoanhThu.getModel();
            model.setRowCount(0);
            int nam = (int) cbxNam.getSelectedItem();
            List<Object[]> list = TKdao.getDoanhThu(nam);
            list.forEach((row) -> {
                double tb = (double) row[6];
                tb = (double) Math.round(tb * 100) / 100;
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], tb});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    public String getXepLoai(double diem) {
        if (diem < 5) {
            return "Chưa đạt";
        }
        if (diem < 6.5) {
            return "Trung bình";
        }
        if (diem < 8) {
            return "Khá";
        }
        if (diem < 9) {
            return "Giỏi";
        }
        if (diem <= 10) {
            return "Xuất sắc";
        }
        return "Không hợp lệ";
    }

    void xuatFileBangDiem() {
        String[] header = new String[]{"Mã người học", "Họ tên", "Điểm", "Xếp Loại"};
        KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
        List<Object[]> list = TKdao.getBangDiem(kh.getMaKH());
        List<Object[]> listObjs = new ArrayList<>();
        list.forEach((row) -> {
            double diem = (double) row[2];
            diem = (double) Math.round(diem * 100) / 100;
            listObjs.add(new Object[]{row[0], row[1], diem, getXepLoai(diem)});
        });
        String fileName = "Bangdiem" + kh.toString();
        String title = "Bảng điểm khóa (" + kh.toString() + ")";
        XFile.xuatFile(this, header, listObjs, fileName, title);
    }

    void xuatFileLuongNguoiHoc() {
        String[] header = new String[]{"Năm", "Số người học", "Đầu tiên", "Sau cùng"};
        List<Object[]> listObjs = TKdao.getLuongNguoiHoc();
        String fileName = "LuongNguoiHoc";
        String title = "Thống kê lượng người học";
        XFile.xuatFile(this, header, listObjs, fileName, title);
    }

    void xuatFileDiemChuyenDe() {
        String[] header = new String[]{"Chuyên đề", "Tổng số học viên", "Điểm Cao nhất", "Điểm thấp nhất", "Điểm trung bình"};
        List<Object[]> listObjs = TKdao.getDiemChuyenDe();
        String fileName = "DiemChuyenDe";
        String title = "Danh sách điểm theo chuyên đề ";
        XFile.xuatFile(this, header, listObjs, fileName, title);
    }

    void xuatFileDoanhThu() {
        String[] header = new String[]{"Chuyên đề", "Số khóa học", "Số học viên", "Doanh thu", "Học phí cao nhất", "Học phí thấp nhất", "Học phí trung bình"};
        int nam = (int) cbxNam.getSelectedItem();
        List<Object[]> listObjs = TKdao.getDoanhThu(nam);
        String fileName = "Thongkedoanhthu" + nam;
        String title = "Thống kê doanh thu năm (" + nam + ")";
        XFile.xuatFile(this, header, listObjs, fileName, title);
    }

    void sendMail() {
        if (tblBangDiem.getRowCount() == 0) {
            MsgBox.alert(this, "Không có học viên nào");
            return;
        }

        new Thread(() -> {
            for (int i = 0; i < tblBangDiem.getRowCount(); i++) {
                String maNH = tblBangDiem.getValueAt(i, 0).toString();
                NguoiHoc nh = new NguoiHocDAO().selectByID(maNH);
                String diem = tblBangDiem.getValueAt(i, 2) == null ? "chưa có" : tblBangDiem.getValueAt(i, 2).toString();
                String xepLoai = tblBangDiem.getValueAt(i, 2) == null ? "chưa có" : tblBangDiem.getValueAt(i, 3).toString();
                String mess = "Điểm của sinh viên " + nh.getHoTen() + " là " + diem
                        + "\nXếp loại của bạn là " + xepLoai;
                MailSender.send("duongtanluc3565@gmail.com", "John@cena2019", "Thông báo điểm", mess, nh.getEmail());
            }
        }).start();
        WaitingJDialog w = new WaitingJDialog(parent, true, tblBangDiem.getRowCount());
        w.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        btnInsert1 = new javax.swing.JButton();
        tabs = new javax.swing.JTabbedPane();
        pnlBangDiem = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBangDiem = new javax.swing.JTable();
        lblKhoaHoc = new javax.swing.JLabel();
        cbxKhoaHoc = new javax.swing.JComboBox<>();
        btnXuatBangDiem = new javax.swing.JButton();
        btnSendMail = new javax.swing.JButton();
        pnlNguoiHoc = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTKNguoiHoc = new javax.swing.JTable();
        btnXuatLuongNH = new javax.swing.JButton();
        pnlTKChuyenDe = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTKChuyenDe = new javax.swing.JTable();
        btnXuatDiemCD = new javax.swing.JButton();
        pnlTKDoanhThu = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTKDoanhThu = new javax.swing.JTable();
        lblNam = new javax.swing.JLabel();
        cbxNam = new javax.swing.JComboBox<>();
        btnXuatDT = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnInsert1.setBackground(new java.awt.Color(0, 102, 51));
        btnInsert1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnInsert1.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnInsert1.setText("Xuất file Excel");
        btnInsert1.setBorderPainted(false);
        btnInsert1.setDefaultCapable(false);
        btnInsert1.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlBangDiem.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBorder(null);

        tblBangDiem.setAutoCreateRowSorter(true);
        tblBangDiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBangDiem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "ĐIỂM", "XẾP LOẠI"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangDiem.setRowHeight(24);
        tblBangDiem.setSelectionBackground(Contraints.BG_COLOR);
        jScrollPane3.setViewportView(tblBangDiem);

        lblKhoaHoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblKhoaHoc.setText("KHÓA HỌC");

        cbxKhoaHoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxKhoaHoc.setFocusable(false);
        cbxKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxKhoaHocActionPerformed(evt);
            }
        });

        btnXuatBangDiem.setBackground(new java.awt.Color(0, 102, 51));
        btnXuatBangDiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnXuatBangDiem.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatBangDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnXuatBangDiem.setText("Xuất file Excel");
        btnXuatBangDiem.setBorderPainted(false);
        btnXuatBangDiem.setDefaultCapable(false);
        btnXuatBangDiem.setFocusable(false);
        btnXuatBangDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatBangDiemActionPerformed(evt);
            }
        });

        btnSendMail.setBackground(new java.awt.Color(241, 67, 54));
        btnSendMail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSendMail.setForeground(new java.awt.Color(255, 255, 255));
        btnSendMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/gmail.png"))); // NOI18N
        btnSendMail.setText("Gửi mail thông báo");
        btnSendMail.setBorderPainted(false);
        btnSendMail.setDefaultCapable(false);
        btnSendMail.setFocusable(false);
        btnSendMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBangDiemLayout = new javax.swing.GroupLayout(pnlBangDiem);
        pnlBangDiem.setLayout(pnlBangDiemLayout);
        pnlBangDiemLayout.setHorizontalGroup(
            pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangDiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBangDiemLayout.createSequentialGroup()
                        .addComponent(lblKhoaHoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxKhoaHoc, 0, 736, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBangDiemLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSendMail)
                        .addGap(18, 18, 18)
                        .addComponent(btnXuatBangDiem)))
                .addContainerGap())
        );
        pnlBangDiemLayout.setVerticalGroup(
            pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangDiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXuatBangDiem)
                    .addComponent(btnSendMail))
                .addGap(13, 13, 13))
        );

        tabs.addTab("BẢNG ĐIỂM", pnlBangDiem);

        pnlNguoiHoc.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        tblTKNguoiHoc.setAutoCreateRowSorter(true);
        tblTKNguoiHoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTKNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NĂM", "SỐ NGƯỜI HỌC", "ĐẦU TIÊN ", "SAU CÙNG"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTKNguoiHoc.setRowHeight(24);
        tblTKNguoiHoc.setSelectionBackground(Contraints.BG_COLOR);
        jScrollPane1.setViewportView(tblTKNguoiHoc);

        btnXuatLuongNH.setBackground(new java.awt.Color(0, 102, 51));
        btnXuatLuongNH.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnXuatLuongNH.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatLuongNH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnXuatLuongNH.setText("Xuất file Excel");
        btnXuatLuongNH.setBorderPainted(false);
        btnXuatLuongNH.setDefaultCapable(false);
        btnXuatLuongNH.setFocusable(false);
        btnXuatLuongNH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatLuongNHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNguoiHocLayout = new javax.swing.GroupLayout(pnlNguoiHoc);
        pnlNguoiHoc.setLayout(pnlNguoiHocLayout);
        pnlNguoiHocLayout.setHorizontalGroup(
            pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNguoiHocLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnXuatLuongNH)))
                .addContainerGap())
        );
        pnlNguoiHocLayout.setVerticalGroup(
            pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXuatLuongNH)
                .addContainerGap())
        );

        tabs.addTab("NGƯỜI HỌC", pnlNguoiHoc);

        pnlTKChuyenDe.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBorder(null);

        tblTKChuyenDe.setAutoCreateRowSorter(true);
        tblTKChuyenDe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTKChuyenDe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CHUYÊN ĐỀ", "TỔNG SỐ HV", "ĐIỂM TN", "ĐIỂM CN", "ĐIỂM TB"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTKChuyenDe.setRowHeight(24);
        tblTKChuyenDe.setSelectionBackground(Contraints.BG_COLOR);
        jScrollPane2.setViewportView(tblTKChuyenDe);
        if (tblTKChuyenDe.getColumnModel().getColumnCount() > 0) {
            tblTKChuyenDe.getColumnModel().getColumn(0).setResizable(false);
            tblTKChuyenDe.getColumnModel().getColumn(0).setPreferredWidth(180);
            tblTKChuyenDe.getColumnModel().getColumn(1).setResizable(false);
            tblTKChuyenDe.getColumnModel().getColumn(1).setPreferredWidth(65);
            tblTKChuyenDe.getColumnModel().getColumn(2).setResizable(false);
            tblTKChuyenDe.getColumnModel().getColumn(2).setPreferredWidth(65);
            tblTKChuyenDe.getColumnModel().getColumn(3).setResizable(false);
            tblTKChuyenDe.getColumnModel().getColumn(3).setPreferredWidth(65);
            tblTKChuyenDe.getColumnModel().getColumn(4).setResizable(false);
            tblTKChuyenDe.getColumnModel().getColumn(4).setPreferredWidth(65);
        }

        btnXuatDiemCD.setBackground(new java.awt.Color(0, 102, 51));
        btnXuatDiemCD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnXuatDiemCD.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatDiemCD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnXuatDiemCD.setText("Xuất file Excel");
        btnXuatDiemCD.setBorderPainted(false);
        btnXuatDiemCD.setDefaultCapable(false);
        btnXuatDiemCD.setFocusable(false);
        btnXuatDiemCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatDiemCDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTKChuyenDeLayout = new javax.swing.GroupLayout(pnlTKChuyenDe);
        pnlTKChuyenDe.setLayout(pnlTKChuyenDeLayout);
        pnlTKChuyenDeLayout.setHorizontalGroup(
            pnlTKChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTKChuyenDeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTKChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTKChuyenDeLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnXuatDiemCD)))
                .addContainerGap())
        );
        pnlTKChuyenDeLayout.setVerticalGroup(
            pnlTKChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTKChuyenDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXuatDiemCD)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("TỔNG HỢP ĐIỂM", pnlTKChuyenDe);

        pnlTKDoanhThu.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane4.setBorder(null);

        tblTKDoanhThu.setAutoCreateRowSorter(true);
        tblTKDoanhThu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTKDoanhThu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CHUYÊN ĐỀ", "SỐ KH", "SỐ HV", "DOANH THU", "HP. CN", "HP. TN", "HP. TB"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTKDoanhThu.setRowHeight(24);
        tblTKDoanhThu.setSelectionBackground(Contraints.BG_COLOR);
        jScrollPane4.setViewportView(tblTKDoanhThu);
        if (tblTKDoanhThu.getColumnModel().getColumnCount() > 0) {
            tblTKDoanhThu.getColumnModel().getColumn(0).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblTKDoanhThu.getColumnModel().getColumn(1).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(1).setPreferredWidth(65);
            tblTKDoanhThu.getColumnModel().getColumn(2).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(2).setPreferredWidth(65);
            tblTKDoanhThu.getColumnModel().getColumn(3).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblTKDoanhThu.getColumnModel().getColumn(4).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(4).setPreferredWidth(65);
            tblTKDoanhThu.getColumnModel().getColumn(5).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(5).setPreferredWidth(65);
            tblTKDoanhThu.getColumnModel().getColumn(6).setResizable(false);
            tblTKDoanhThu.getColumnModel().getColumn(6).setPreferredWidth(65);
        }

        lblNam.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNam.setText("NĂM");

        cbxNam.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxNam.setFocusable(false);
        cbxNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNamActionPerformed(evt);
            }
        });

        btnXuatDT.setBackground(new java.awt.Color(0, 102, 51));
        btnXuatDT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnXuatDT.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatDT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnXuatDT.setText("Xuất file Excel");
        btnXuatDT.setBorderPainted(false);
        btnXuatDT.setDefaultCapable(false);
        btnXuatDT.setFocusable(false);
        btnXuatDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatDTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTKDoanhThuLayout = new javax.swing.GroupLayout(pnlTKDoanhThu);
        pnlTKDoanhThu.setLayout(pnlTKDoanhThuLayout);
        pnlTKDoanhThuLayout.setHorizontalGroup(
            pnlTKDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTKDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTKDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                    .addGroup(pnlTKDoanhThuLayout.createSequentialGroup()
                        .addComponent(lblNam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxNam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTKDoanhThuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnXuatDT)))
                .addContainerGap())
        );
        pnlTKDoanhThuLayout.setVerticalGroup(
            pnlTKDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTKDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTKDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxNam, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXuatDT)
                .addGap(13, 13, 13))
        );

        tabs.addTab("DOANH THU", pnlTKDoanhThu);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(Contraints.TEXT1_COLOR);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("TỔNG HỢP VÀ THỐNG KÊ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabs))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNamActionPerformed
        // TODO add your handling code here:
        fillTableTKDoanhThu();
    }//GEN-LAST:event_cbxNamActionPerformed

    private void cbxKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxKhoaHocActionPerformed
        // TODO add your handling code here:
        fillTableBangDiem();
    }//GEN-LAST:event_cbxKhoaHocActionPerformed

    private void btnXuatBangDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatBangDiemActionPerformed
        // TODO add your handling code here:
        xuatFileBangDiem();
    }//GEN-LAST:event_btnXuatBangDiemActionPerformed

    private void btnXuatLuongNHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatLuongNHActionPerformed
        // TODO add your handling code here:
        xuatFileLuongNguoiHoc();
    }//GEN-LAST:event_btnXuatLuongNHActionPerformed

    private void btnXuatDiemCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatDiemCDActionPerformed
        // TODO add your handling code here:
        xuatFileDiemChuyenDe();
    }//GEN-LAST:event_btnXuatDiemCDActionPerformed

    private void btnXuatDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatDTActionPerformed
        // TODO add your handling code here:
        xuatFileDoanhThu();
    }//GEN-LAST:event_btnXuatDTActionPerformed

    private void btnSendMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMailActionPerformed
        // TODO add your handling code here:
        sendMail();
    }//GEN-LAST:event_btnSendMailActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInsert1;
    private javax.swing.JButton btnSendMail;
    private javax.swing.JButton btnXuatBangDiem;
    private javax.swing.JButton btnXuatDT;
    private javax.swing.JButton btnXuatDiemCD;
    private javax.swing.JButton btnXuatLuongNH;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxKhoaHoc;
    private javax.swing.JComboBox<String> cbxNam;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblKhoaHoc;
    private javax.swing.JLabel lblNam;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlBangDiem;
    private javax.swing.JPanel pnlNguoiHoc;
    private javax.swing.JPanel pnlTKChuyenDe;
    private javax.swing.JPanel pnlTKDoanhThu;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblBangDiem;
    private javax.swing.JTable tblTKChuyenDe;
    private javax.swing.JTable tblTKDoanhThu;
    private javax.swing.JTable tblTKNguoiHoc;
    // End of variables declaration//GEN-END:variables
}
