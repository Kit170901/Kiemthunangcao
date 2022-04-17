/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.ChuyenDeDAO;
import com.edusys.dao.KhoaHocDAO;
import com.edusys.entity.ChuyenDe;
import com.edusys.entity.KhoaHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.Contraints;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import com.edusys.utils.XFile;
import com.edusys.utils.XImage;
import java.awt.Color;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class KhoaHocJDialog extends javax.swing.JDialog {

    /**
     * Creates new form NhanVienJFrame
     *
     * @param parent
     * @param modal
     */
    public KhoaHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        this.setLocationRelativeTo(null);
        this.setIconImage(XImage.getAppIcon());
        this.setTitle("QUẢN LÝ KHÓA HỌC");
        this.getContentPane().setBackground(Contraints.BG_COLOR);

        tabs.setIconAt(0, XImage.getIconTabEdit());
        tabs.setIconAt(1, XImage.getIconTabList());

        fillComboBoxChuyenDe();
        fillTable();
        clearForm();
        clearWarning();
        updateStatus();
        isFill = true;
        tabs.setSelectedIndex(1);
    }
    ChuyenDeDAO CDDao = new ChuyenDeDAO();
    KhoaHocDAO KHDao = new KhoaHocDAO();
    int row = -1;
    boolean isFill = false;
    boolean isEdit = false;

    void fillComboBoxChuyenDe() {
        try {
            DefaultComboBoxModel model1 = (DefaultComboBoxModel) cbxChuyenDe1.getModel();
            DefaultComboBoxModel model2 = (DefaultComboBoxModel) cbxChuyenDe2.getModel();
            model1.removeAllElements();
            model2.removeAllElements();
            List<ChuyenDe> list = CDDao.selectAll();
            model1.addElement("---- TẤT CẢ ----");
            model2.addElement("---- CHỌN ----");
            for (ChuyenDe cd : list) {
                model1.addElement(cd);
                model2.addElement(cd);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void fillTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
            model.setRowCount(0);
            //List<ChuyenDe> list = dao.selectAll();
            String keys = cbxChuyenDe1.getSelectedIndex() == 0 ? "" : ((ChuyenDe) cbxChuyenDe1.getSelectedItem()).getMaCD();
            List<KhoaHoc> list = KHDao.selectByKey(keys);
            list.forEach((kh) -> {
                model.addRow(new Object[]{kh.getMaKH(), kh.getMaCD(), kh.getThoiLuong(), kh.getHocPhi(),
                    XDate.toString(kh.getNgayKG(), "dd/MM/yyy") , kh.getMaNV(),XDate.toString(kh.getNgayTao(), "dd/MM/yyy")});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void setForm(KhoaHoc kh) {
        txtHocPhi.setText(String.valueOf(kh.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(kh.getThoiLuong()));
        txtNgayKG.setDate(kh.getNgayKG());
        txtMaNV.setText(kh.getMaNV());
        txtNgayTao.setDate(kh.getNgayTao());
        txtGhiChu.setText(kh.getGhiChu());
    }

    KhoaHoc getForm() {
        String err = "";
        clearWarning();
        String sGhiChu = txtGhiChu.getText().trim();
        Date sNgayKG = txtNgayKG.getDate();
        if (cbxChuyenDe2.getSelectedIndex() == 0) {
            err += "Bạn phải chọn chuyên đề \n";
            showWaring(1);
        }
        if (sNgayKG == null) {
            err += "Không được để trống ngày khai giảng \n";
            showWaring(2);
        } else {
            LocalDate now;
            if (isEdit) {
                Date d = KHDao.selectByID(Integer.parseInt(lblMaKH2.getText())).getNgayTao();
                long l = d.getTime();
                Date d1 = new Date(l);

                now = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                now = LocalDate.now();
            }
            LocalDate ngayKG = sNgayKG.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int ngay = Period.between(now, ngayKG).getDays();
            if (ngay < 5) {
                err += "Bạn phải tạo trước ít nhất 5 ngày \n";
                showWaring(2);
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {

                }
                clearIcon();
            }
        }).start();
        if (err.length() == 0) {
            KhoaHoc kh = new KhoaHoc();
            ChuyenDe cd = (ChuyenDe) cbxChuyenDe2.getSelectedItem();
            kh.setMaCD(cd.getMaCD());
            kh.setHocPhi(cd.getHocPhi());
            kh.setThoiLuong(cd.getThoiLuong());
            kh.setGhiChu(sGhiChu);
            kh.setNgayKG(sNgayKG);
            if (!isEdit) {
                kh.setMaNV(Auth.USER.getMaNV());
                kh.setNgayTao(new Date());
            } else {
                kh.setMaKH(Integer.parseInt(lblMaKH2.getText()));
                kh.setMaNV(KHDao.selectByID(kh.getMaKH()).getMaNV());
                kh.setNgayTao(KHDao.selectByID(kh.getMaKH()).getNgayTao());
            }
            return kh;
        } else {
            MsgBox.alert(this, err);
            return null;
        }
    }

    void edit() {
        int maKH = (int) tblGridView.getValueAt(this.row, 0);
        KhoaHoc kh = KHDao.selectByID(maKH);
        for (int i = 1; i < cbxChuyenDe2.getItemCount(); i++) {
            String ma = tblGridView.getValueAt(this.row, 1).toString();
            Object a = cbxChuyenDe2.getModel().getElementAt(i);
            ChuyenDe cd = (ChuyenDe) a;
            if (cd.getMaCD().contains(ma)) {
                cbxChuyenDe2.setSelectedIndex(i);
                break;
            }
        }
        this.setForm(kh);
        clearWarning();
        tabs.setSelectedIndex(0);
        this.updateStatus();
        isEdit = true;

    }

    void clearForm() {
        KhoaHoc kh = new KhoaHoc();
        kh.setNgayTao(new Date());
        kh.setMaNV(Auth.USER.getMaNV());
        clearWarning();
        setForm(kh);
        this.row = -1;
        this.updateStatus();
        isEdit = false;
        cbxChuyenDe2.setSelectedIndex(0);
    }

    void showWaring(int error) {
        switch (error) {
            case 1:
                cbxChuyenDe2.setBackground(Contraints.INPUT_ERROR_BG);
                lblChuyenDeW.setVisible(true);
                break;
            case 2:
                txtNgayKG.setBackground(Contraints.INPUT_ERROR_BG);
                lblNgayKGW.setVisible(true);
                break;

        }
    }

    void clearWarning() {
        cbxChuyenDe2.setBackground(Color.white);
        txtNgayKG.setBackground(Color.white);
        clearIcon();
    }
    void clearIcon() {
        lblChuyenDeW.setVisible(false);
        lblNgayKGW.setVisible(false);
    }
    void insert() {
        KhoaHoc kh = getForm();
        if (kh == null) {
            return;
        }
        try {
            KHDao.insert(kh);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");
        }

    }

    void update() {

        KhoaHoc kh = getForm();
        if (kh == null) {
            return;
        }

        try {
            KHDao.update(kh);
            this.fillTable();
            //this.clearForm();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
        }

    }

    void delete() {
        if (Auth.isManager()) {
            int maKH = (int) tblGridView.getValueAt(this.row, 0);
            KhoaHoc kh = KHDao.selectByID(maKH);
            if (MsgBox.confirm(this, "Bạn có chắc muốn xóa khóa học này?") == 0) {
                try {
                    KHDao.delete(maKH);
                    fillTable();
                    clearForm();
                    MsgBox.alert(this, "Xóa khóa học thành công");
                } catch (Exception ex) {
                    MsgBox.alert(this, "Xóa khóa học thất bại");
                }
            }
        } else {
            MsgBox.alert(this, "Bạn không có quyền xóa khóa học");
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

        cbxChuyenDe2.setEnabled(!edit);
        txtHocPhi.setEditable(false);
        txtThoiLuong.setEditable(false);
        txtMaNV.setEditable(false);
        txtNgayTao.setEnabled(false);
        btnInsert.setEnabled(!edit);
        btnUpdate.setEnabled(edit);
        btnDelete.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);

        btnInsert.setBackground(edit ? Color.lightGray : Contraints.BLUEBUTTON_COLOR);
        btnUpdate.setBackground(!edit ? Color.lightGray : Contraints.BLUEBUTTON_COLOR);
        btnDelete.setBackground(!edit ? Color.lightGray : Contraints.REDBUTTON_COLOR);

        btnFirst.setBackground(edit && !first ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
        btnPrev.setBackground(edit && !first ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
        btnNext.setBackground(edit && !last ? Contraints.GREENBUTTON_COLOR : Color.lightGray);
        btnLast.setBackground(edit && !last ? Contraints.GREENBUTTON_COLOR : Color.lightGray);

        if (row < 0 || tblGridView.getRowCount() < 0) {
            lblMaKH2.setText("Editing");
        } else {
            lblMaKH2.setText(String.valueOf(tblGridView.getValueAt(row, 0)));
        }
    }

    void chonChuyenDe() {
        if (cbxChuyenDe2.getSelectedIndex() > 0) {
            ChuyenDe cd = (ChuyenDe) cbxChuyenDe2.getSelectedItem();
            txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
            txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
            txtMaNV.setText(Auth.USER.getMaNV());
            txtNgayTao.setDate(new Date());
        }
    }

    int getIndexChuyenDe(String maCD) {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbxChuyenDe1.getModel();
        for (int i = 1; i < model.getSize(); i++) {
            if (((ChuyenDe) model.getElementAt(i)).getMaCD().equals(maCD)) {
                return i;
            }
        }
        return 0;
    }

    void xuatFile() {
        String[] header = new String[]{"Mã KH", "Chuyên đề", "Học phí", "Thời lượng", "Ngày khai giảng", "Mã NV tạo", "Ngày tạo"};
        String keys = cbxChuyenDe1.getSelectedIndex() == 0 ? "" : ((ChuyenDe) cbxChuyenDe1.getSelectedItem()).getMaCD();
        List<KhoaHoc> list = KHDao.selectByKey(keys);
        List<Object[]> listObjs = new ArrayList<>();
        list.forEach((nguoiHoc) -> {
            listObjs.add(nguoiHoc.toObjects());
        });
        String fileName = "DSKhoaHoc";
        String title = "Danh sách khoá học (" + (keys.isEmpty() ? "Tất cả" : keys) + ")";
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

        jPanel2 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        pnlChuyenDe = new javax.swing.JPanel();
        cbxChuyenDe1 = new javax.swing.JComboBox<>();
        lblChuyenDe1 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        pnlEdit = new javax.swing.JPanel();
        lblNgayKGW = new javax.swing.JLabel();
        lblChuyenDeW = new javax.swing.JLabel();
        pnlGroupBtn = new javax.swing.JPanel();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        lblNgayKG = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        lblThoiLuong = new javax.swing.JLabel();
        lblNgayTao = new javax.swing.JLabel();
        lblGhiChu = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        lblChuyenDe = new javax.swing.JLabel();
        lblHocPhi = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        lblMaNV = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        lblMaKH = new javax.swing.JLabel();
        lblMaKH2 = new javax.swing.JLabel();
        txtNgayTao = new com.toedter.calendar.JDateChooser();
        txtNgayKG = new com.toedter.calendar.JDateChooser();
        cbxChuyenDe2 = new javax.swing.JComboBox<>();
        pnlList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        btnInsert1 = new javax.swing.JButton();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlChuyenDe.setBackground(Contraints.BG_COLOR);
        pnlChuyenDe.setForeground(Contraints.BG_COLOR);
        pnlChuyenDe.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        pnlChuyenDe.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbxChuyenDe1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxChuyenDe1.setBorder(null);
        cbxChuyenDe1.setFocusable(false);
        cbxChuyenDe1.setOpaque(false);
        cbxChuyenDe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxChuyenDe1ActionPerformed(evt);
            }
        });
        pnlChuyenDe.add(cbxChuyenDe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 850, 40));

        lblChuyenDe1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblChuyenDe1.setForeground(Contraints.TEXT1_COLOR);
        lblChuyenDe1.setText("Chuyên đề");
        pnlChuyenDe.add(lblChuyenDe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -3, -1, 30));

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(Contraints.TEXT1_COLOR);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ KHOÁ HỌC");

        tabs.setBackground(new java.awt.Color(255, 255, 255));
        tabs.setFocusable(false);

        pnlEdit.setBackground(new java.awt.Color(255, 255, 255));
        pnlEdit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNgayKGW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblNgayKGW, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 51, -1, -1));

        lblChuyenDeW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblChuyenDeW, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 51, -1, -1));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
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

        pnlEdit.add(pnlGroupBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 409, -1, -1));

        lblNgayKG.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNgayKG.setText("Khai giảng");
        pnlEdit.add(lblNgayKG, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 13, -1, 30));

        txtThoiLuong.setBackground(new java.awt.Color(204, 204, 204));
        txtThoiLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtThoiLuong.setForeground(new java.awt.Color(0, 0, 153));
        txtThoiLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 7));
        pnlEdit.add(txtThoiLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 126, 400, 40));

        lblThoiLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblThoiLuong.setText("Thời lượng");
        pnlEdit.add(lblThoiLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 96, -1, 30));

        lblNgayTao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNgayTao.setText("Ngày tạo");
        pnlEdit.add(lblNgayTao, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 179, -1, 30));

        lblGhiChu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblGhiChu.setText("Ghi chú");
        pnlEdit.add(lblGhiChu, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 262, -1, 30));

        jScrollPane2.setBorder(null);

        txtGhiChu.setBackground(new java.awt.Color(204, 204, 204));
        txtGhiChu.setColumns(10);
        txtGhiChu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtGhiChu.setRows(5);
        txtGhiChu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 7));
        jScrollPane2.setViewportView(txtGhiChu);

        pnlEdit.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 292, 818, 104));

        lblChuyenDe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblChuyenDe.setText("Chuyên đề");
        pnlEdit.add(lblChuyenDe, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, -1, 30));

        lblHocPhi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblHocPhi.setText("Học phí");
        pnlEdit.add(lblHocPhi, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 96, 59, 30));

        txtHocPhi.setBackground(new java.awt.Color(204, 204, 204));
        txtHocPhi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtHocPhi.setForeground(new java.awt.Color(0, 0, 153));
        txtHocPhi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 7));
        pnlEdit.add(txtHocPhi, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 126, 400, 40));

        lblMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMaNV.setText("Người tạo");
        pnlEdit.add(lblMaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 179, -1, 30));

        txtMaNV.setBackground(new java.awt.Color(204, 204, 204));
        txtMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaNV.setForeground(new java.awt.Color(0, 0, 153));
        txtMaNV.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 7));
        pnlEdit.add(txtMaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 209, 400, 40));

        lblMaKH.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMaKH.setText("Mã KH :");
        pnlEdit.add(lblMaKH, new org.netbeans.lib.awtextra.AbsoluteConstraints(201, 13, -1, 30));

        lblMaKH2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblMaKH2.setForeground(new java.awt.Color(0, 0, 153));
        lblMaKH2.setText("O");
        pnlEdit.add(lblMaKH2, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 13, -1, 30));

        txtNgayTao.setBackground(new java.awt.Color(255, 255, 255));
        txtNgayTao.setDateFormatString("dd/MM/yyyy");
        txtNgayTao.setFocusable(false);
        txtNgayTao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pnlEdit.add(txtNgayTao, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 209, 400, 40));

        txtNgayKG.setBackground(new java.awt.Color(255, 255, 255));
        txtNgayKG.setDateFormatString("dd/MM/yyyy");
        txtNgayKG.setFocusable(false);
        txtNgayKG.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pnlEdit.add(txtNgayKG, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 43, 400, 40));

        cbxChuyenDe2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbxChuyenDe2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbxChuyenDe2.setBorder(null);
        cbxChuyenDe2.setFocusable(false);
        cbxChuyenDe2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxChuyenDe2ActionPerformed(evt);
            }
        });
        pnlEdit.add(cbxChuyenDe2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 43, 400, 40));

        tabs.addTab("CẬP NHẬT ", pnlEdit);

        pnlList.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tblGridView.setAutoCreateRowSorter(true);
        tblGridView.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ KH", "CHUYÊN ĐỀ", "HỌC PHÍ", "THỜI LƯỢNG", "NGÀY KG", "TẠO BỞI", "NGÀY TẠO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGridView.setFocusable(false);
        tblGridView.setGridColor(new java.awt.Color(255, 255, 255));
        tblGridView.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tblGridView.setRowHeight(24);
        tblGridView.setSelectionBackground(Contraints.BG_COLOR);
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGridView);

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlListLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInsert1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("DANH SÁCH ", pnlList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tabs)
                    .addComponent(pnlChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTitle)
                .addGap(13, 13, 13)
                .addComponent(pnlChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxChuyenDe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxChuyenDe1ActionPerformed
        if (isFill) {
            cbxChuyenDe2.setSelectedIndex(cbxChuyenDe1.getSelectedIndex());
            chonChuyenDe();
            fillTable();
            clearForm();
            tabs.setSelectedIndex(1);
        }
    }//GEN-LAST:event_cbxChuyenDe1ActionPerformed

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

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            row = tblGridView.getSelectedRow();
            edit();
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void btnInsert1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert1ActionPerformed
        // TODO add your handling code here:
        xuatFile();
    }//GEN-LAST:event_btnInsert1ActionPerformed

    private void cbxChuyenDe2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxChuyenDe2ActionPerformed
        // TODO add your handling code here:
        chonChuyenDe();
    }//GEN-LAST:event_cbxChuyenDe2ActionPerformed

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
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxChuyenDe1;
    private javax.swing.JComboBox<String> cbxChuyenDe2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblChuyenDe;
    private javax.swing.JLabel lblChuyenDe1;
    private javax.swing.JLabel lblChuyenDeW;
    private javax.swing.JLabel lblGhiChu;
    private javax.swing.JLabel lblHocPhi;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaKH2;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblNgayKG;
    private javax.swing.JLabel lblNgayKGW;
    private javax.swing.JLabel lblNgayTao;
    private javax.swing.JLabel lblThoiLuong;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlChuyenDe;
    private javax.swing.JPanel pnlEdit;
    private javax.swing.JPanel pnlGroupBtn;
    private javax.swing.JPanel pnlList;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtMaNV;
    private com.toedter.calendar.JDateChooser txtNgayKG;
    private com.toedter.calendar.JDateChooser txtNgayTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
