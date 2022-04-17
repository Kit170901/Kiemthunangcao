/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.ChuyenDeDAO;
import com.edusys.dao.HocVienDAO;
import com.edusys.dao.KhoaHocDAO;
import com.edusys.dao.NguoiHocDAO;
import com.edusys.entity.ChuyenDe;
import com.edusys.entity.HocVien;
import com.edusys.entity.KhoaHoc;
import com.edusys.entity.NguoiHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.Contraints;
import static com.edusys.utils.Contraints.TEXT1_COLOR;
import com.edusys.utils.MailSender;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import com.edusys.utils.XFile;
import com.edusys.utils.XImage;
import java.awt.Frame;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class HocVienJDialog extends javax.swing.JDialog {

    /**
     * Creates new form HocVienJFrame
     *
     * @param parent
     * @param modal
     */
    Frame parent;

    public HocVienJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        init();
    }

    private void init() {
        this.setLocationRelativeTo(null);
        this.setIconImage(XImage.getAppIcon());
        this.setTitle("QUẢN LÝ HỌC VIÊN CỦA KHÓA HỌC");
        this.getContentPane().setBackground(Contraints.BG_COLOR);

        tabs.setIconAt(0, XImage.getIconTabHocVien());
        tabs.setIconAt(1, XImage.getIconTabNguoiHoc());
        fillComboBoxChuyenDe();
        isRun = true;
    }
    ChuyenDeDAO CDDao = new ChuyenDeDAO();
    KhoaHocDAO KHDao = new KhoaHocDAO();
    NguoiHocDAO NHDao = new NguoiHocDAO();
    HocVienDAO HVDao = new HocVienDAO();
    int row = -1;
    boolean isRun = false;

    void fillComboBoxChuyenDe() {
        try {
            DefaultComboBoxModel model1 = (DefaultComboBoxModel) cbxChuyenDe.getModel();
            model1.removeAllElements();
            List<ChuyenDe> list = CDDao.selectAll();
            list.forEach((cd) -> {
                model1.addElement(cd);
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
        fillComboBoxKhoaHoc();
    }

    void fillComboBoxKhoaHoc() {
        List<KhoaHoc> list = new ArrayList<KhoaHoc>();
        DefaultComboBoxModel model1 = (DefaultComboBoxModel) cbxKhoaHoc.getModel();
        try {
            model1.removeAllElements();
            ChuyenDe cd = (ChuyenDe) cbxChuyenDe.getSelectedItem();
            list = KHDao.selectByKey(cd.getMaCD());
            for (KhoaHoc kh : list) {
                model1.addElement(kh);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
        fillTableHocVien();
    }

    void fillTableHocVien() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblHocVien.getModel();
            model.setRowCount(0);
            if (cbxKhoaHoc.getItemCount() == 0) {
                fillTableNguoiHoc();
                return;
            }
            KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
            List<HocVien> list = HVDao.selectByMaKH(kh.getMaKH());
            int i = 1;
            for (HocVien hv : list) {
                String hoTen = NHDao.selectByID(hv.getMaNH()).getHoTen();
                model.addRow(new Object[]{i, hv.getMaHV(), hv.getMaNH(), hoTen, hv.getDiem() == -1 ? null : hv.getDiem()});
                i++;
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
        fillTableNguoiHoc();
    }

    void fillTableNguoiHoc() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblNguoiHoc.getModel();
            model.setRowCount(0);
            if (cbxKhoaHoc.getItemCount() == 0) {
                return;
            }
            KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
            List<NguoiHoc> list = NHDao.selectNotInCourse(kh.getMaKH(), txtTimKiem.getText());
            list.forEach((nh) -> {
                model.addRow(new Object[]{nh.getMaNH(), nh.getHoTen(), nh.isGioiTinh() ? "Nam" : "Nữ", XDate.toString(nh.getNgaySinh(), "dd/MM/yyy"), nh.getDienThoai(), nh.getEmail()});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void addHocVien() {
        if (cbxKhoaHoc.getItemCount() == 0) {
            MsgBox.alert(this, "Không có khóa học nào được chọn");
            return;
        }
        KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
        int i = 0;
        for (int rowTable : tblNguoiHoc.getSelectedRows()) {
            HocVien hv = new HocVien();
            hv.setMaNH((String) tblNguoiHoc.getValueAt(rowTable, 0));
            hv.setMaKH(kh.getMaKH());
            hv.setDiem(-1);
            HVDao.insert(hv);
            i++;
        }
        MsgBox.alert(this, "Thêm học viên thành công " + i + "/" + tblNguoiHoc.getSelectedRowCount());
        fillTableHocVien();
        tabs.setSelectedIndex(0);
    }

    void removeHocVien() {
        if (Auth.isManager()) {
            if (cbxKhoaHoc.getItemCount() == 0) {
                MsgBox.alert(this, "Không có khóa học nào được chọn");
                return;
            }
            if (MsgBox.confirm(this, "Bạn có muốn xóa những học viên được chọn không ?") == 0) {
                int i = 0;
                for (int rowTable : tblHocVien.getSelectedRows()) {
                    Integer maHV = (Integer) tblHocVien.getValueAt(rowTable, 1);
                    HVDao.delete(maHV);
                    i++;
                }
                MsgBox.alert(this, "Xóa học viên thành công " + i + "/" + tblHocVien.getSelectedRowCount());
                fillTableHocVien();
            }
        } else {
            MsgBox.alert(this, "Bạn không có quyền xóa học viên !");
        }
    }

    void updateDiemHV() {
        int dem = 0;
        tblHocVien.setFocusable(false);
        if (cbxKhoaHoc.getItemCount() == 0) {
            MsgBox.alert(this, "Không có khóa học nào được chọn");
            return;
        }
        for (int i = 0; i < tblHocVien.getRowCount(); i++) {
            Integer maHV = (Integer) tblHocVien.getValueAt(i, 1);

            HocVien hv = HVDao.selectByID(maHV);
            Object obj = tblHocVien.getValueAt(i, 4);
            if (obj != null) {
                hv.setDiem((double) tblHocVien.getValueAt(i, 4));
                if (hv.getDiem() >= 0 && hv.getDiem() <= 10) {
                    HVDao.update(hv);
                    dem++;
                }
            }

        }
        fillTableHocVien();
        String mess = "Cập nhật điểm học viên \n "
                + "Thành công : " + dem + "/" + tblHocVien.getRowCount() + "\n"
                + "Thất bại : " + (tblHocVien.getRowCount() - dem) + "/" + tblHocVien.getRowCount() + "\n"
                + "'Lưu ý : Điểm phải từ 0 đến 10'";
        MsgBox.alert(this, mess);
    }

    void xuatFile() {
        try {
            if (cbxKhoaHoc.getItemCount() == 0) {
                MsgBox.alert(this, "Không có khóa học nào");
                return;
            }
            String[] header = new String[]{"Mã học viên", "Mã người học", "Họ tên", "Điểm"};
            KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
            List<HocVien> list = HVDao.selectByMaKH(kh.getMaKH());
            List<Object[]> listObjs = new ArrayList<>();
            list.forEach((HocVien) -> {
                listObjs.add(HocVien.toObjects());
            });
            String fileName = "DSHocVienLop" + cbxKhoaHoc.getSelectedItem().toString();
            String title = "Danh sách học viên " + cbxKhoaHoc.getSelectedItem().toString();
            XFile.xuatFile(this, header, listObjs, fileName, title);
        } catch (Exception ex) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void timKiem() {
        fillTableNguoiHoc();
    }

    void sendMail() {
        if (tblHocVien.getRowCount() == 0) {
            MsgBox.alert(this, "Không có học viên nào");
            return;
        }

        new Thread(() -> {
            for (int i = 0; i < tblHocVien.getRowCount(); i++) {
                String maNH = tblHocVien.getValueAt(i, 2).toString();
                NguoiHoc nh = NHDao.selectByID(maNH);
                String diem = tblHocVien.getValueAt(i, 4) == null ? "chưa có" : tblHocVien.getValueAt(i, 4).toString();
                String xepLoai = tblHocVien.getValueAt(i, 4) == null ? "chưa có" : getXepLoai((double) tblHocVien.getValueAt(i, 4));
                String mess = "Điểm của sinh viên " + nh.getHoTen() + " là " + diem
                        + "\nXếp loại của bạn là " + xepLoai;
                MailSender.send("duongtanluc3565@gmail.com", "John@cena2019", "Thông báo điểm", mess, nh.getEmail());
            }
        }).start();
        WaitingJDialog w = new WaitingJDialog(parent, true, tblHocVien.getRowCount());
        w.setVisible(true);
    }

    //Phương thức trả về xếp loại học viên theo điểm
    public String getXepLoai(double diem) {
        if(diem<0){
            return "Không hợp lệ";
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        pnlHocVien = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHocVien = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnXoa = new javax.swing.JButton();
        btnInsert1 = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnSendMail = new javax.swing.JButton();
        pnlNguoiHoc = new javax.swing.JPanel();
        pnlTimKiem = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        lblTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNguoiHoc = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        lblCbxChuyenDe = new javax.swing.JLabel();
        lblCbxKhoaHoc = new javax.swing.JLabel();
        cbxKhoaHoc = new javax.swing.JComboBox<>();
        cbxChuyenDe = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(Contraints.TEXT1_COLOR);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ HỌC VIÊN");

        pnlHocVien.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        tblHocVien.setAutoCreateRowSorter(true);
        tblHocVien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblHocVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TT", "MÃ HV", "MÃ NH", "TÊN HV", "ĐIỂM"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHocVien.setFocusable(false);
        tblHocVien.setRowHeight(24);
        tblHocVien.setSelectionBackground(Contraints.BG_COLOR);
        jScrollPane1.setViewportView(tblHocVien);
        if (tblHocVien.getColumnModel().getColumnCount() > 0) {
            tblHocVien.getColumnModel().getColumn(0).setResizable(false);
            tblHocVien.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblHocVien.getColumnModel().getColumn(1).setResizable(false);
            tblHocVien.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblHocVien.getColumnModel().getColumn(2).setResizable(false);
            tblHocVien.getColumnModel().getColumn(2).setPreferredWidth(50);
            tblHocVien.getColumnModel().getColumn(3).setResizable(false);
            tblHocVien.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblHocVien.getColumnModel().getColumn(4).setResizable(false);
            tblHocVien.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnXoa.setBackground(new java.awt.Color(153, 0, 0));
        btnXoa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/close (1).png"))); // NOI18N
        btnXoa.setText("Xóa khỏi khóa học");
        btnXoa.setBorderPainted(false);
        btnXoa.setDefaultCapable(false);
        btnXoa.setFocusable(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnInsert1.setBackground(new java.awt.Color(0, 102, 51));
        btnInsert1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnInsert1.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnInsert1.setText("Xuất file Excel");
        btnInsert1.setBorderPainted(false);
        btnInsert1.setDefaultCapable(false);
        btnInsert1.setFocusable(false);
        btnInsert1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert1ActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(0, 0, 153));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/refresh (1).png"))); // NOI18N
        btnUpdate.setText("Cập nhật");
        btnUpdate.setBorderPainted(false);
        btnUpdate.setDefaultCapable(false);
        btnUpdate.setFocusable(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnInsert1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSendMail, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUpdate))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnInsert1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnSendMail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlHocVienLayout = new javax.swing.GroupLayout(pnlHocVien);
        pnlHocVien.setLayout(pnlHocVienLayout);
        pnlHocVienLayout.setHorizontalGroup(
            pnlHocVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHocVienLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlHocVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        pnlHocVienLayout.setVerticalGroup(
            pnlHocVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHocVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("HỌC VIÊN", pnlHocVien);

        pnlNguoiHoc.setBackground(new java.awt.Color(255, 255, 255));

        pnlTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pnlTimKiem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblIcon.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblIcon.setForeground(new java.awt.Color(204, 204, 204));
        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/search (2).png"))); // NOI18N
        pnlTimKiem.add(lblIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, 40, 40));

        lblTimKiem.setBackground(new java.awt.Color(153, 153, 153));
        lblTimKiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTimKiem.setForeground(new java.awt.Color(153, 153, 153));
        lblTimKiem.setText("Tìm kiếm");
        pnlTimKiem.add(lblTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 0, 70, 40));

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimKiem.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 5)));
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });
        pnlTimKiem.add(txtTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 40));

        btnTimKiem.setBackground(new java.awt.Color(0, 0, 153));
        btnTimKiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("Tìm");
        btnTimKiem.setBorderPainted(false);
        btnTimKiem.setDefaultCapable(false);
        btnTimKiem.setFocusable(false);
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });
        pnlTimKiem.add(btnTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 0, 90, 40));

        jScrollPane2.setBorder(null);

        tblNguoiHoc.setAutoCreateRowSorter(true);
        tblNguoiHoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "GIỚI TÍNH", "NGÀY SINH", "ĐIỆN THOẠI", "EMAIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNguoiHoc.setFocusable(false);
        tblNguoiHoc.setRowHeight(24);
        tblNguoiHoc.setSelectionBackground(Contraints.BG_COLOR);
        jScrollPane2.setViewportView(tblNguoiHoc);
        if (tblNguoiHoc.getColumnModel().getColumnCount() > 0) {
            tblNguoiHoc.getColumnModel().getColumn(0).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblNguoiHoc.getColumnModel().getColumn(1).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblNguoiHoc.getColumnModel().getColumn(2).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(2).setPreferredWidth(40);
            tblNguoiHoc.getColumnModel().getColumn(3).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(3).setPreferredWidth(40);
            tblNguoiHoc.getColumnModel().getColumn(4).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(4).setPreferredWidth(50);
            tblNguoiHoc.getColumnModel().getColumn(5).setResizable(false);
            tblNguoiHoc.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        btnThem.setBackground(new java.awt.Color(0, 0, 153));
        btnThem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/add-button (1).png"))); // NOI18N
        btnThem.setText("Thêm vào khóa học");
        btnThem.setBorderPainted(false);
        btnThem.setDefaultCapable(false);
        btnThem.setFocusable(false);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNguoiHocLayout = new javax.swing.GroupLayout(pnlNguoiHoc);
        pnlNguoiHoc.setLayout(pnlNguoiHocLayout);
        pnlNguoiHocLayout.setHorizontalGroup(
            pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNguoiHocLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnThem)
                    .addGroup(pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pnlTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        pnlNguoiHocLayout.setVerticalGroup(
            pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNguoiHocLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(pnlTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("NGƯỜI HỌC", pnlNguoiHoc);

        lblCbxChuyenDe.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCbxChuyenDe.setForeground(TEXT1_COLOR);
        lblCbxChuyenDe.setText("Chuyên đề");

        lblCbxKhoaHoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCbxKhoaHoc.setForeground(TEXT1_COLOR);
        lblCbxKhoaHoc.setText("Khóa học");

        cbxKhoaHoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxKhoaHoc.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        cbxKhoaHoc.setFocusable(false);
        cbxKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxKhoaHocActionPerformed(evt);
            }
        });

        cbxChuyenDe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxChuyenDe.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        cbxChuyenDe.setFocusable(false);
        cbxChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxChuyenDeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tabs)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCbxChuyenDe)
                                    .addComponent(cbxChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCbxKhoaHoc)
                                    .addComponent(cbxKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTitle)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCbxChuyenDe)
                    .addComponent(lblCbxKhoaHoc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        removeHocVien();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        addHocVien();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateDiemHV();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void cbxChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxChuyenDeActionPerformed
        // TODO add your handling code here:
        if (isRun) {
            isRun = false;
            fillComboBoxKhoaHoc();
            isRun = true;
        }
    }//GEN-LAST:event_cbxChuyenDeActionPerformed

    private void cbxKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxKhoaHocActionPerformed
        if (isRun) {
            fillTableHocVien();
        }
    }//GEN-LAST:event_cbxKhoaHocActionPerformed

    private void btnInsert1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert1ActionPerformed
        // TODO add your handling code here:
        xuatFile();
    }//GEN-LAST:event_btnInsert1ActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        // TODO add your handling code here:
        lblTimKiem.setVisible(txtTimKiem.getText().isEmpty());
        timKiem();
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        timKiem();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnSendMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMailActionPerformed
        sendMail();
    }//GEN-LAST:event_btnSendMailActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInsert1;
    private javax.swing.JButton btnSendMail;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxChuyenDe;
    private javax.swing.JComboBox<String> cbxKhoaHoc;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCbxChuyenDe;
    private javax.swing.JLabel lblCbxKhoaHoc;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlHocVien;
    private javax.swing.JPanel pnlNguoiHoc;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblHocVien;
    private javax.swing.JTable tblNguoiHoc;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
