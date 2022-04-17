/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.NhanVienDAO;
import com.edusys.entity.NhanVien;
import com.edusys.utils.Auth;
import com.edusys.utils.Contraints;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XFile;
import com.edusys.utils.XImage;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class NhanVienJDialog extends javax.swing.JDialog {

    /**
     * Creates new form NhanVienJFrame
     *
     * @param parent
     * @param model
     */
    public NhanVienJDialog(Frame parent, boolean model) {
        super(parent, model);
        initComponents();
        init();
    }

    private void init() {
        this.setLocationRelativeTo(null);
        this.setIconImage(XImage.getAppIcon());
        this.setTitle("QUẢN LÝ NHÂN VIÊN");
        this.getContentPane().setBackground(Contraints.BG_COLOR);

        tabs.setIconAt(0, XImage.getIconTabEdit());
        tabs.setIconAt(1, XImage.getIconTabList());

        tabs.setSelectedIndex(1);
        fillTable();
        clearWarning();
        row = -1;
        updateStatus();
    }
    NhanVienDAO dao = new NhanVienDAO();
    int row = -1;
    boolean isEdit = false;

    void fillTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
            model.setRowCount(0);
            List<NhanVien> list = dao.selectByKey(txtTimKiem.getText().trim());
            //List<NhanVien> list = dao.selectAll();
            list.forEach((nv) -> {
                String mk = Auth.isManager() ? nv.getMatKhau() : "•••••";
                model.addRow(new Object[]{nv.getMaNV(), mk, nv.getHoTen(), nv.isVaiTro() ? "Trưởng phòng" : "Nhân viên"});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void setForm(NhanVien nv) {
        txtMaNv.setText(nv.getMaNV());
        txtMatKhau.setText(nv.getMaNV());
        txtXacNhanMK.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        rdoTruongPhong.setSelected(nv.isVaiTro());
        rdoNhanVien.setSelected(!nv.isVaiTro());
    }

    NhanVien getForm() {
        String err = "";
        clearWarning();
        String sMaNV = txtMaNv.getText().trim();
        String sMatKhau = new String(txtMatKhau.getPassword()).trim();
        String sMatKhau2 = new String(txtXacNhanMK.getPassword()).trim();
        String sHoTen = txtHoTen.getText().trim();
        boolean sVaitro = rdoTruongPhong.isSelected();
        if (sMaNV.length() == 0) {
            err += "Không được để trống mã nv \n";
            showWaring(1);
        } else if (!sMaNV.matches("[\\w]{2,}")) {
            err += "MaNV phải từ 2 kí tự trở lên và không chứa khoảng trắng\n";
            showWaring(1);
        } else if (!isEdit && dao.selectByID(sMaNV) != null) {
            err += "Mã nhân viên này đã tồn tại";
            showWaring(1);
        }
        if (sMatKhau.length() == 0) {
            err += "Không được để trống mật khẩu \n";
            showWaring(2);
        } else if (!sMatKhau.matches("[\\w]{3,}")) {
            err += "Mật khẩu phải từ 3 kí tự trở lên\n";
            showWaring(2);
        }
        if (sHoTen.length() == 0) {
            err += "Không được để trống họ tên \n";
            showWaring(4);
        } else if (!sHoTen.matches("[\\D]{3,}")) {
            err += "Họ tên phải từ 3 kí tự trở lên và không chứa số\n";
            showWaring(4);
        }
        if (sMatKhau2.length() == 0) {
            err += "Không được để trống xác nhận mật khẩu \n";
            showWaring(3);
        } else if (!sMatKhau.equals(sMatKhau2)) {
            err += "Xác nhận mật khẩu không đúng \n";
            showWaring(3);
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
            NhanVien nv = new NhanVien();
            nv.setMaNV(sMaNV);
            nv.setMatKhau(sMatKhau);
            nv.setHoTen(sHoTen);
            nv.setVaiTro(sVaitro);
            return nv;
        } else {
            MsgBox.alert(this, err);
            return null;
        }
    }

    void edit() {
        String manv = (String) tblGridView.getValueAt(this.row, 0);
        NhanVien nv = dao.selectByID(manv);
        this.setForm(nv);
        tabs.setSelectedIndex(0);
        this.updateStatus();
        clearWarning();
        isEdit = true;
    }

    void clearForm() {
        NhanVien nv = new NhanVien();
        setForm(nv);
        clearWarning();
        this.row = -1;
        this.updateStatus();
        isEdit = false;
    }
    
    void showWaring(int error) {
        switch (error) {
            case 1:
                txtMaNv.setBackground(Contraints.INPUT_ERROR_BG);
                lblMaNVW.setVisible(true);
                break;
            case 2:
                txtMatKhau.setBackground(Contraints.INPUT_ERROR_BG);
                lblMatKhauW.setVisible(true);
                break;
            case 3:
                txtXacNhanMK.setBackground(Contraints.INPUT_ERROR_BG);
                lblXacNhanMKW.setVisible(true);
                break;
            case 4:
                txtHoTen.setBackground(Contraints.INPUT_ERROR_BG);
                lblHoTenW.setVisible(true);
                break;
        }
    }

    void clearWarning() {
        txtMaNv.setBackground(Contraints.INPUT_NORMAL_BG);
        txtMatKhau.setBackground(Contraints.INPUT_NORMAL_BG);
        txtXacNhanMK.setBackground(Contraints.INPUT_NORMAL_BG);
        txtHoTen.setBackground(Contraints.INPUT_NORMAL_BG);
        clearIcon();
    }
    void clearIcon() {
        lblMaNVW.setVisible(false);
        lblMatKhauW.setVisible(false);
        lblXacNhanMKW.setVisible(false);
        lblHoTenW.setVisible(false);
    }
    void insert() {
        NhanVien nv = getForm();
        if (nv == null) {
            return;
        }

        try {
            dao.insert(nv);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");
        }

    }

    void update() {
        if (Auth.isManager()) {
            NhanVien nv = getForm();
            if (nv == null) {
                return;
            }
            try {
                dao.update(nv);
                this.fillTable();
                //this.clearForm();
                MsgBox.alert(this, "Cập nhật thành công");
            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật thất bại");
            }
        } else {
            MsgBox.alert(this, "Bạn không có quyền sửa thông tin nhân viên");
        }

    }

    void delete() {
        if (Auth.isManager()) {
            String maNV = txtMaNv.getText().trim();
            if (maNV.equals(Auth.USER.getMaNV())) {
                MsgBox.alert(this, "Bạn không có quyền xóa chính bạn");
            } else if (MsgBox.confirm(this, "Bạn có chắc muốn xóa nhân viên này?") == 0) {
                try {
                    dao.delete(maNV);
                    fillTable();
                    clearForm();
                    MsgBox.alert(this, "Xóa nhân viên thành công");
                } catch (Exception ex) {
                    MsgBox.alert(this, "Xóa nhân viên thất bại \n Có thể do nhân viên này đã tạo một số khóa học và người học");
                    
                }
            }
        } else {
            MsgBox.alert(this, "Bạn không có quyền xóa nhân viên");
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

        txtMaNv.setEditable(!edit);
        btnInsert.setEnabled(!edit);
        btnUpdate.setEnabled(edit);
        btnDelete.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);

        rdoTruongPhong.setEnabled(Auth.isManager());
        rdoNhanVien.setEnabled(Auth.isManager());
    }

    void timKiem() {
        fillTable();
        clearForm();
    }

    void xuatFile() {
        String[] header = new String[]{"Mã người học", "Mật khẩu", "Họ tên", "Vai trò"};
        List<NhanVien> list = dao.selectByKey(txtTimKiem.getText().trim());
        List<Object[]> listObjs = new ArrayList<>();
        list.forEach((nv) -> {
            listObjs.add(nv.toObjects(Auth.isManager()));
        });
        String fileName = "DSNhanVien";
        String title = "Danh sách nhân viên (" + txtTimKiem.getText().trim() + ")";
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

        grpVaiTro = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        pnlEdit = new javax.swing.JPanel();
        lblMatKhauW = new javax.swing.JLabel();
        lblXacNhanMKW = new javax.swing.JLabel();
        lblHoTenW = new javax.swing.JLabel();
        lblMaNVW = new javax.swing.JLabel();
        txtMaNv = new javax.swing.JTextField();
        lblMaNV = new javax.swing.JLabel();
        lblMatKhau = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JPasswordField();
        lblXacNhanMK = new javax.swing.JLabel();
        txtXacNhanMK = new javax.swing.JPasswordField();
        lblHoTen = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        lblVaiTro = new javax.swing.JLabel();
        rdoNhanVien = new javax.swing.JRadioButton();
        rdoTruongPhong = new javax.swing.JRadioButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(Contraints.TEXT1_COLOR);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ NHÂN VIÊN");

        tabs.setBackground(new java.awt.Color(255, 255, 255));
        tabs.setFocusable(false);

        pnlEdit.setBackground(new java.awt.Color(255, 255, 255));
        pnlEdit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMatKhauW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblMatKhauW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 137, -1, -1));

        lblXacNhanMKW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblXacNhanMKW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 220, -1, -1));

        lblHoTenW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblHoTenW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 305, -1, -1));

        lblMaNVW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblMaNVW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 53, -1, -1));

        txtMaNv.setBackground(new java.awt.Color(204, 204, 204));
        txtMaNv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaNv.setForeground(Contraints.TEXT1_COLOR);
        txtMaNv.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtMaNv, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 45, 810, 40));

        lblMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMaNV.setText("Mã nhân viên");
        pnlEdit.add(lblMaNV, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, -1, 30));

        lblMatKhau.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMatKhau.setText("Mật khẩu");
        pnlEdit.add(lblMatKhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 98, -1, 30));

        txtMatKhau.setBackground(new java.awt.Color(204, 204, 204));
        txtMatKhau.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMatKhau.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        txtMatKhau.setEchoChar('•');
        pnlEdit.add(txtMatKhau, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 129, 810, 40));

        lblXacNhanMK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblXacNhanMK.setText("Xác nhận mật khẩu");
        pnlEdit.add(lblXacNhanMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 182, -1, 30));

        txtXacNhanMK.setBackground(new java.awt.Color(204, 204, 204));
        txtXacNhanMK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtXacNhanMK.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        txtXacNhanMK.setEchoChar('•');
        pnlEdit.add(txtXacNhanMK, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 213, 810, 40));

        lblHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblHoTen.setText("Họ Và Tên");
        pnlEdit.add(lblHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 266, -1, 30));

        txtHoTen.setBackground(new java.awt.Color(204, 204, 204));
        txtHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtHoTen.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtHoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 297, 810, 40));

        lblVaiTro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblVaiTro.setText("Vai trò");
        pnlEdit.add(lblVaiTro, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 350, -1, 30));

        rdoNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        grpVaiTro.add(rdoNhanVien);
        rdoNhanVien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdoNhanVien.setSelected(true);
        rdoNhanVien.setText("Nhân viên");
        rdoNhanVien.setFocusable(false);
        rdoNhanVien.setSelected(true);
        pnlEdit.add(rdoNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 383, -1, -1));

        rdoTruongPhong.setBackground(new java.awt.Color(255, 255, 255));
        grpVaiTro.add(rdoTruongPhong);
        rdoTruongPhong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdoTruongPhong.setText("Trưởng phòng");
        rdoTruongPhong.setFocusable(false);
        pnlEdit.add(rdoTruongPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 383, -1, -1));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
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

        pnlEdit.add(pnlGroupBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 417, -1, -1));

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
                "MÃ NV", "MẬT KHẨU", "HỌ VÀ TÊN", "VAI TRÒ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
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
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(tabs)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        // TODO add your handling code here:
        lblTimKiem.setVisible(txtTimKiem.getText().isEmpty());
        timKiem();

    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        timKiem();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnInsert1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert1ActionPerformed
        // TODO add your handling code here:
        xuatFile();
    }//GEN-LAST:event_btnInsert1ActionPerformed

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
    private javax.swing.ButtonGroup grpVaiTro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblHoTenW;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMaNVW;
    private javax.swing.JLabel lblMatKhau;
    private javax.swing.JLabel lblMatKhauW;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVaiTro;
    private javax.swing.JLabel lblXacNhanMK;
    private javax.swing.JLabel lblXacNhanMKW;
    private javax.swing.JPanel pnlEdit;
    private javax.swing.JPanel pnlGroupBtn;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JRadioButton rdoNhanVien;
    private javax.swing.JRadioButton rdoTruongPhong;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNv;
    private javax.swing.JPasswordField txtMatKhau;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JPasswordField txtXacNhanMK;
    // End of variables declaration//GEN-END:variables
}
