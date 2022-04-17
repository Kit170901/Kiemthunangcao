/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.HocVienDAO;
import com.edusys.dao.NguoiHocDAO;
import com.edusys.entity.HocVien;
import com.edusys.entity.NguoiHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.Contraints;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import com.edusys.utils.XFile;
import com.edusys.utils.XImage;
import java.awt.Color;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class NguoiHocJDialog extends javax.swing.JDialog {

    /**
     * Creates new form NhanVienJFrame
     *
     * @param parent
     * @param modal
     */
    public NguoiHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();

    }

    private void init() {
        this.setLocationRelativeTo(null);
        this.setIconImage(XImage.getAppIcon());
        this.setTitle("QUẢN LÝ NGƯỜI HỌC");
        this.getContentPane().setBackground(Contraints.BG_COLOR);

        tabs.setIconAt(0, XImage.getIconTabEdit());
        tabs.setIconAt(1, XImage.getIconTabList());

        fillTable();
        clearForm();
        clearWarning();
        tabs.setSelectedIndex(1);
    }
    NguoiHocDAO dao = new NguoiHocDAO();
    int row = -1;
    boolean isEdit = false;

    void fillTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
            model.setRowCount(0);
            //List<ChuyenDe> list = dao.selectAll();
            List<NguoiHoc> list = dao.selectByKey(txtTimKiem.getText().trim());
            list.forEach((nh) -> {
                model.addRow(new Object[]{nh.getMaNH(), nh.getHoTen(), nh.isGioiTinh() ? "Nam" : "Nữ",
                    XDate.toString(nh.getNgaySinh(), "dd/MM/yyy"), nh.getDienThoai(), nh.getEmail(), nh.getMaNV(), 
                    XDate.toString(nh.getNgayDK(), "dd/MM/yyy")});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void setForm(NguoiHoc nh) {
        txtMaNH.setText(nh.getMaNH());
        txtHoTen.setText(nh.getHoTen());
        cbxGioiTinh.setSelectedIndex(nh.isGioiTinh() ? 1 : 0);
        txtNgaySinh.setDate(nh.getNgaySinh());
        txtDienThoai.setText(nh.getDienThoai());
        txtEmail.setText(nh.getEmail());
        txtGhiChu.setText(nh.getGhiChu());
    }

    NguoiHoc getForm() {
        String err = "";
        clearWarning();
        String sMaNH = txtMaNH.getText().trim();
        String sHoTen = txtHoTen.getText().trim();
        String sDienThoai = txtDienThoai.getText().trim();
        String sEmail = txtEmail.getText().trim();
        String EmailPater1 = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        String sGhiChu = txtGhiChu.getText().trim();
        Date date = txtNgaySinh.getDate();
        if (sMaNH.length() == 0) {
            err += "Không được để trống mã người học \n";
            showWaring(1);
        } else if (!sMaNH.matches("[\\w]{7}")) {
            err += "Mã người học phải đúng 7 kí tự và không chứa khoảng trắng\n";
            showWaring(1);
        } else if (!isEdit && dao.selectByID(sMaNH) != null) {
            err += "Mã người học này đã tồn tại\n";
            showWaring(1);
        }
        if (sHoTen.length() == 0) {
            err += "Không được để trống họ tên \n";
            showWaring(2);
        } else if (!sHoTen.matches("[\\D]{3,}")) {
            err += "Họ tên phải từ 3 kí tự trở lên và không chứa số\n";
            showWaring(2);
        }
        if (sDienThoai.length() == 0) {
            err += "Không được để trống số điện thoại \n";
            showWaring(3);
        } else if (!sDienThoai.matches("^[0-9]{7,15}$")) {
            err += "số điện thoại không đúng định dạng\n";
            showWaring(3);
        }
        if (sEmail.length() == 0) {
            err += "Không được để trống email \n";
            showWaring(4);
        } else if (!sEmail.matches(EmailPater1)) {
            err += "Email không đúng định dạng\n";
            showWaring(4);
        }
        if (date == null) {
            err += "Không được để trống ngày sinh \n";
            showWaring(5);
        } else {
            LocalDate birth = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            int tuoi = Period.between(birth, now).getYears();
            if (tuoi < 16) {
                err += "Người học phải ít nhất 16 tuổi \n";
            }
        }
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                
            }
            clearIcon();
        }).start();
        if (err.length() == 0) {
            NguoiHoc nh = new NguoiHoc();
            nh.setMaNH(sMaNH);
            nh.setHoTen(sHoTen);
            nh.setGioiTinh((cbxGioiTinh.getSelectedIndex() != 0));
            nh.setNgaySinh(date);
            nh.setDienThoai(sDienThoai);
            nh.setEmail(sEmail);
            nh.setGhiChu(sGhiChu);
            if (isEdit) {
                nh.setMaNV(dao.selectByID(nh.getMaNH()).getMaNV());
                nh.setNgayDK(dao.selectByID(nh.getMaNH()).getNgayDK());
            } else {
                nh.setMaNV(Auth.USER.getMaNV());
                Date now = new Date();
                nh.setNgayDK(now);
            }
            return nh;
        } else {
            MsgBox.alert(this, err);
            return null;
        }
    }

    void edit() {
        String maCD = (String) tblGridView.getValueAt(this.row, 0);
        NguoiHoc nh = dao.selectByID(maCD);
        this.setForm(nh);
        clearWarning();
        tabs.setSelectedIndex(0);
        this.updateStatus();
        isEdit = true;
    }

    void clearForm() {
        NguoiHoc nh = new NguoiHoc();
        nh.setGioiTinh(true);
        setForm(nh);
        clearWarning();
        this.row = -1;
        this.updateStatus();
        isEdit = false;
    }
    
    void showWaring(int error) {
        switch (error) {
            case 1:
                txtMaNH.setBackground(Contraints.INPUT_ERROR_BG);
                lblMaNHW.setVisible(true);
                break;
            case 2:
                txtHoTen.setBackground(Contraints.INPUT_ERROR_BG);
                lblHoTenW.setVisible(true);
                break;
            case 3:
                txtDienThoai.setBackground(Contraints.INPUT_ERROR_BG);
                lblDienThoaiW.setVisible(true);
                break;
            case 4:
                txtEmail.setBackground(Contraints.INPUT_ERROR_BG);
                lblEmailW.setVisible(true);
                break;
            case 5:
                txtNgaySinh.setBackground(Contraints.INPUT_ERROR_BG);
                lblNgaySinhW.setVisible(true);
                break;
        }
    }

    void clearWarning() {
        txtMaNH.setBackground(Contraints.INPUT_NORMAL_BG);
        txtHoTen.setBackground(Contraints.INPUT_NORMAL_BG);
        txtDienThoai.setBackground(Contraints.INPUT_NORMAL_BG);
        txtEmail.setBackground(Contraints.INPUT_NORMAL_BG);
        txtNgaySinh.setBackground(Color.white);
        clearIcon();
    }
    void clearIcon(){
        lblMaNHW.setVisible(false);
        lblHoTenW.setVisible(false);
        lblDienThoaiW.setVisible(false);
        lblEmailW.setVisible(false);
        lblNgaySinhW.setVisible(false);
    }
    void insert() {
        NguoiHoc nh = getForm();
        if (nh == null) {
            return;
        }

        try {
            dao.insert(nh);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");
        }

    }

    void update() {
        NguoiHoc nh = getForm();
        if (nh == null) {
            return;
        }

        try {
            dao.update(nh);
            this.fillTable();
            //this.clearForm();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");

        }

    }

    void delete() {
        if (Auth.isManager()) {
            String maNH = txtMaNH.getText().trim();
            if (MsgBox.confirm(this, "Bạn có chắc muốn xóa người học này?") == 0) {
                try {
                    HocVienDAO hvdao = new HocVienDAO();
                    List<HocVien> list = hvdao.selectByMaNH(maNH);
                    if(!list.isEmpty()){
                        if (MsgBox.confirm(this, "Người học này đã tham gia vào một số khóa học \n . Bạn có muốn tiếp tục xóa ?") != 0) return;
                        list.forEach((HocVien) -> {
                            hvdao.delete(HocVien.getMaHV());
                        });
                    }
                    dao.delete(maNH);
                    fillTable();
                    clearForm();
                    MsgBox.alert(this, "Xóa người học thành công");
                } catch (Exception ex) {
                    MsgBox.alert(this, "Xóa người học thất bại");
                }
            }
        } else {
            MsgBox.alert(this, "Bạn không có quyền xóa người học");
        }
    }

    void first() {
        row = 0;
        edit();
    }

    void prev() {
        if (row > 0) {
            row--;
            edit();
        }
    }

    void next() {
        if (row < tblGridView.getRowCount() - 1) {
            row++;
            edit();
        }
    }

    void last() {
        row = tblGridView.getRowCount() - 1;
        edit();
    }

    void updateStatus() {
        boolean edit = (row >= 0);
        boolean first = (row == 0);
        boolean last = (row == tblGridView.getRowCount() - 1);

        txtMaNH.setEditable(!edit);
        btnInsert.setEnabled(!edit);
        btnUpdate.setEnabled(edit);
        btnDelete.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);

//        btnInsert.setBackground(edit ? Color.lightGray : Contraints.BLUEBUTTON_COLOR);
//        btnUpdate.setBackground(!edit ? Color.lightGray : Contraints.BLUEBUTTON_COLOR);
//        btnDelete.setBackground(!edit ? Color.lightGray : Contraints.REDBUTTON_COLOR);
//
//        btnFirst.setBackground(edit && !first ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
//        btnPrev.setBackground(edit && !first ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
//        btnNext.setBackground(edit && !last ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
//        btnLast.setBackground(edit && !last ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
    }

    void timKiem() {
        fillTable();
        clearForm();
    }

    void xuatFile() {
        String[] header = new String[]{"Mã người học", "Họ tên", "Ngày sinh", "Giới tính", "Số điện thoại", "Email", "Ghi chú", "Mã nhân viên", "Ngày đăng kí"};
        List<NguoiHoc> list = dao.selectByKey(txtTimKiem.getText().trim());
        List<Object[]> listObjs = new ArrayList<>();
        list.forEach((nguoiHoc) -> {
            listObjs.add(nguoiHoc.toObjects());
        });
        String fileName = "DSNguoiHoc";
        String title = "Danh sách người học (" + txtTimKiem.getText().trim() + ")";
        XFile.xuatFile(this, header, listObjs, fileName, title);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        lblTitle = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        pnlEdit = new javax.swing.JPanel();
        lblMaNHW = new javax.swing.JLabel();
        lblNgaySinhW = new javax.swing.JLabel();
        lblEmailW = new javax.swing.JLabel();
        lblDienThoaiW = new javax.swing.JLabel();
        lblHoTenW = new javax.swing.JLabel();
        lblMaNH = new javax.swing.JLabel();
        txtMaNH = new javax.swing.JTextField();
        lblNgaySinh = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        lblGhiChu = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        lblGioiTinh = new javax.swing.JLabel();
        lblDienThoai = new javax.swing.JLabel();
        txtDienThoai = new javax.swing.JTextField();
        lblHoTen = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        cbxGioiTinh = new javax.swing.JComboBox<>();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        pnlGroupBtn = new javax.swing.JPanel();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        pnlList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        pnlTimKiem = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        lblTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        btnInsert1 = new javax.swing.JButton();

        jFileChooser1.setSelectedFile(new java.io.File("C:\\Users\\Admin\\Documents\\DSNguoiHoc.xlsx"));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(Contraints.TEXT1_COLOR);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ NGƯỜI HỌC");

        tabs.setBackground(new java.awt.Color(255, 255, 255));
        tabs.setFocusable(false);

        pnlEdit.setBackground(new java.awt.Color(255, 255, 255));
        pnlEdit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMaNHW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblMaNHW, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 51, -1, -1));

        lblNgaySinhW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblNgaySinhW, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 134, -1, -1));

        lblEmailW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblEmailW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 217, -1, -1));

        lblDienThoaiW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblDienThoaiW, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 217, -1, -1));

        lblHoTenW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblHoTenW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 51, -1, -1));

        lblMaNH.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMaNH.setText("Mã người học");
        pnlEdit.add(lblMaNH, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, -1, 30));

        txtMaNH.setBackground(new java.awt.Color(204, 204, 204));
        txtMaNH.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaNH.setForeground(new java.awt.Color(0, 0, 153));
        txtMaNH.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtMaNH, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 43, 400, 40));

        lblNgaySinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNgaySinh.setText("Ngày sinh");
        pnlEdit.add(lblNgaySinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 96, -1, 30));

        txtEmail.setBackground(new java.awt.Color(204, 204, 204));
        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtEmail.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        txtEmail.setMargin(new java.awt.Insets(2, 5, 2, 5));
        pnlEdit.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 209, 400, 40));

        lblEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblEmail.setText("Email");
        pnlEdit.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 179, -1, 30));

        lblGhiChu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblGhiChu.setText("Ghi chú");
        pnlEdit.add(lblGhiChu, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 262, -1, 30));

        jScrollPane2.setBorder(null);

        txtGhiChu.setBackground(new java.awt.Color(204, 204, 204));
        txtGhiChu.setColumns(20);
        txtGhiChu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setRows(4);
        txtGhiChu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        jScrollPane2.setViewportView(txtGhiChu);

        pnlEdit.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 292, 812, 92));

        lblGioiTinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblGioiTinh.setText("Giới tính");
        pnlEdit.add(lblGioiTinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 96, -1, 30));

        lblDienThoai.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDienThoai.setText("Điện thoại");
        pnlEdit.add(lblDienThoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 179, -1, 30));

        txtDienThoai.setBackground(new java.awt.Color(204, 204, 204));
        txtDienThoai.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtDienThoai.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        txtDienThoai.setMargin(new java.awt.Insets(2, 5, 2, 5));
        pnlEdit.add(txtDienThoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 209, 400, 40));

        lblHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblHoTen.setText("Họ tên");
        pnlEdit.add(lblHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 13, -1, 30));

        txtHoTen.setBackground(new java.awt.Color(204, 204, 204));
        txtHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtHoTen.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        txtHoTen.setMargin(new java.awt.Insets(2, 5, 2, 5));
        pnlEdit.add(txtHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 43, 400, 40));

        cbxGioiTinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nữ", "Nam" }));
        cbxGioiTinh.setSelectedIndex(1);
        cbxGioiTinh.setBorder(null);
        cbxGioiTinh.setFocusable(false);
        pnlEdit.add(cbxGioiTinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 126, 400, 40));

        txtNgaySinh.setBackground(new java.awt.Color(153, 255, 153));
        txtNgaySinh.setDateFormatString("dd/MM/yyyy");
        txtNgaySinh.setFocusable(false);
        txtNgaySinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pnlEdit.add(txtNgaySinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 126, 400, 40));

        pnlGroupBtn.setBackground(new java.awt.Color(255, 255, 255));
        pnlGroupBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnInsert.setBackground(new java.awt.Color(0, 0, 153));
        btnInsert.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnInsert.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/confirmation (1).png"))); // NOI18N
        btnInsert.setText("Thêm");
        btnInsert.setBorderPainted(false);
        btnInsert.setDefaultCapable(false);
        btnInsert.setFocusable(false);
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(0, 0, 153));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/confirmation (1).png"))); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.setBorderPainted(false);
        btnUpdate.setDefaultCapable(false);
        btnUpdate.setFocusable(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(153, 0, 0));
        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/close (1).png"))); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.setBorderPainted(false);
        btnDelete.setDefaultCapable(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(0, 0, 153));
        btnClear.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/add-button (1).png"))); // NOI18N
        btnClear.setText("Mới");
        btnClear.setBorderPainted(false);
        btnClear.setDefaultCapable(false);
        btnClear.setFocusable(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnFirst.setBackground(new java.awt.Color(0, 102, 51));
        btnFirst.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnFirst.setForeground(new java.awt.Color(255, 255, 255));
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/previous.png"))); // NOI18N
        btnFirst.setBorderPainted(false);
        btnFirst.setDefaultCapable(false);
        btnFirst.setFocusable(false);
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setBackground(new java.awt.Color(0, 102, 51));
        btnPrev.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPrev.setForeground(new java.awt.Color(255, 255, 255));
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/play.png"))); // NOI18N
        btnPrev.setBorderPainted(false);
        btnPrev.setDefaultCapable(false);
        btnPrev.setFocusable(false);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(0, 102, 51));
        btnNext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/play (1).png"))); // NOI18N
        btnNext.setBorderPainted(false);
        btnNext.setDefaultCapable(false);
        btnNext.setFocusable(false);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setBackground(new java.awt.Color(0, 102, 51));
        btnLast.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLast.setForeground(new java.awt.Color(255, 255, 255));
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/next.png"))); // NOI18N
        btnLast.setBorderPainted(false);
        btnLast.setDefaultCapable(false);
        btnLast.setFocusable(false);
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlGroupBtnLayout = new javax.swing.GroupLayout(pnlGroupBtn);
        pnlGroupBtn.setLayout(pnlGroupBtnLayout);
        pnlGroupBtnLayout.setHorizontalGroup(
            pnlGroupBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGroupBtnLayout.createSequentialGroup()
                .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlGroupBtnLayout.setVerticalGroup(
            pnlGroupBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGroupBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlEdit.add(pnlGroupBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 399, 812, -1));

        tabs.addTab("CẬP NHẬT", pnlEdit);

        pnlList.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tblGridView.setAutoCreateRowSorter(true);
        tblGridView.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "GIỚI TÍNH", "NGÀY SINH", "ĐIỆN THOẠI", "EMAIL", "MÃ NV", "NGÀY ĐK"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGridView.setFocusable(false);
        tblGridView.setGridColor(new java.awt.Color(255, 255, 255));
        tblGridView.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView.setRowHeight(24);
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGridView);
        if (tblGridView.getColumnModel().getColumnCount() > 0) {
            tblGridView.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblGridView.getColumnModel().getColumn(2).setPreferredWidth(30);
            tblGridView.getColumnModel().getColumn(3).setPreferredWidth(30);
            tblGridView.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblGridView.getColumnModel().getColumn(6).setPreferredWidth(30);
            tblGridView.getColumnModel().getColumn(7).setPreferredWidth(30);
        }
        tblGridView.setSelectionBackground(Contraints.BG_COLOR);
        tblGridView.setSelectionForeground(Color.WHITE);

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

        javax.swing.GroupLayout pnlListLayout = new javax.swing.GroupLayout(pnlList);
        pnlList.setLayout(pnlListLayout);
        pnlListLayout.setHorizontalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlListLayout.createSequentialGroup()
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInsert1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(15, 15, 15))
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlListLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(pnlTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInsert1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("DANH SÁCH", pnlList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 845, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            row = tblGridView.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        // TODO add your handling code here:
        lblTimKiem.setVisible(txtTimKiem.getText().isEmpty());
        timKiem();

    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnInsert1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert1ActionPerformed
        // TODO add your handling code here:
        xuatFile();
    }//GEN-LAST:event_btnInsert1ActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        timKiem();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnInsert1;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbxGioiTinh;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDienThoai;
    private javax.swing.JLabel lblDienThoaiW;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmailW;
    private javax.swing.JLabel lblGhiChu;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblHoTenW;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblMaNH;
    private javax.swing.JLabel lblMaNHW;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblNgaySinhW;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlEdit;
    private javax.swing.JPanel pnlGroupBtn;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtDienThoai;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNH;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
