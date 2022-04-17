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
import com.edusys.utils.XFile;
import com.edusys.utils.XImage;
import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class ChuyenDeJDialog extends javax.swing.JDialog {

    /**
     * Creates new form NhanVienJFrame
     * @param parent
     * @param model
     */
    public ChuyenDeJDialog(Frame parent, boolean model) {
        super(parent, model);
        initComponents();
        init();
    }

    private void init() {
        this.setLocationRelativeTo(null);
        this.setIconImage(XImage.getAppIcon());
        this.setTitle("QUẢN LÝ CHUYÊN ĐỀ");
        this.getContentPane().setBackground(Contraints.BG_COLOR);

        tabs.setIconAt(0, XImage.getIconTabEdit());
        tabs.setIconAt(1, XImage.getIconTabList());

        tabs.setSelectedIndex(1);
        fillTable();
        clearWarning();
        row = -1;
        updateStatus();
    }
    ChuyenDeDAO dao = new ChuyenDeDAO();
    int row = -1;
    boolean isChoose = false;
    boolean isEdit = false;

    void fillTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
            model.setRowCount(0);
            //List<ChuyenDe> list = dao.selectAll();
            List<ChuyenDe> list = dao.selectByKey(txtTimKiem.getText().trim());
            list.forEach((cd) -> {
                model.addRow(new Object[]{cd.getMaCD(), cd.getTenCD(), cd.getHocPhi(), cd.getThoiLuong(), cd.getHinh()});
            });
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void setForm(ChuyenDe cd) {
        txtMaCD.setText(cd.getMaCD());
        txtTenCD.setText(cd.getTenCD());
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        txtMoTa.setText(cd.getMoTa());
        if (cd.getHinh() != null) {
            ImageIcon icon = XImage.reSizeImg(XImage.read(cd.getHinh()), lblHinh.getWidth(), lblHinh.getHeight());
            lblHinh.setIcon(icon);
            lblHinh.setToolTipText(cd.getHinh());
            lblHinh.setText("");
        }else{
            lblHinh.setText("Click để chọn logo");
        }
    }
    
    ChuyenDe getForm() {
        String err = "";
        clearWarning();
        String sMaCD = txtMaCD.getText().trim();
        String sTenCD = txtTenCD.getText().trim();
        String sHocPhi = txtHocPhi.getText().trim();
        String sThoiLuong = txtThoiLuong.getText().trim();
        String sMoTa = txtMoTa.getText().trim();
        double hocphi = 0;
        int thoiLuong = 0;
        if (sMaCD.length() == 0) {
            err += "Không được để trống mã chuyên đề \n";
            showWaring(1);
        } else if (!sMaCD.matches("[\\w]{5}")) {
            err += "Mã chuyên đề phải đúng 5 kí tự và không chứa khoảng trắng\n";
            showWaring(1);
        } else if (!isEdit && dao.selectByID(sMaCD) != null) {
            err += "Mã chuyên đề này đã tồn tại";
            showWaring(1);
        }
        if (sTenCD.length() == 0) {
            err += "Không được để trống tên chuyên đề \n";
            showWaring(2);
        } else if (!sTenCD.matches("[\\D]{3,}")) {
            err += "Tên chuyên đề phải từ 3 kí tự trở lên và không chứa số";
            showWaring(2);
        }
        try {
            hocphi = Double.parseDouble(sHocPhi);
            if (hocphi <= 0) {
                err += "Học phí phải lớn hơn 0 \n";
                showWaring(3);
            }
        } catch (NumberFormatException e) {
            err += "Học phí phải là số \n";
            showWaring(3);
        }
        try {
            thoiLuong = Integer.parseInt(sThoiLuong);
            if (thoiLuong <= 0) {
                err += "Thời lượng phải lớn hơn 0 \n";
                showWaring(4);
            }
        } catch (NumberFormatException e) {
            err += "Thời lượng phải là số \n";
            showWaring(4);
        }
        if (isChoose == false) {
            err += "Bạn chưa chọn logo \n";
            showWaring(5);
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
            ChuyenDe cd = new ChuyenDe();
            cd.setMaCD(sMaCD);
            cd.setTenCD(sTenCD);
            cd.setHocPhi(hocphi);
            cd.setThoiLuong(thoiLuong);
            cd.setMoTa(sMoTa);
            cd.setHinh(lblHinh.getToolTipText());
            return cd;
        } else {
            MsgBox.alert(this, err);
            return null;
        }
        
    }

    void chonAnh() {
        FileNameExtensionFilter FindImg = new FileNameExtensionFilter("Hình ảnh(.JPG,.PNG)", "jpg", "png");
        fileChooser.setFileFilter(FindImg);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle("Chọn logo chuyên đề");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon icon = XImage.reSizeImg(XImage.read(file.getName()), lblHinh.getWidth(), lblHinh.getHeight());
            lblHinh.setIcon(icon);
            lblHinh.setToolTipText(file.getName());
            isChoose = true;
            lblHinh.setText("");
        }
    }

    void edit() {
        String maCD = (String) tblGridView.getValueAt(this.row, 0);
        ChuyenDe cd = dao.selectByID(maCD);
        this.setForm(cd);
        tabs.setSelectedIndex(0);
        this.updateStatus();
        clearWarning();
        isChoose = true;
        isEdit = true;
    }

    void clearForm() {
        ChuyenDe cd = new ChuyenDe();
        clearWarning();
        setForm(cd);
        lblHinh.setIcon(new ImageIcon());
        this.row = -1;
        this.updateStatus();
        isChoose = false;
        isEdit = false;
    }
    void showWaring(int error){
        switch(error){
            case 1:
                txtMaCD.setBackground(Contraints.INPUT_ERROR_BG);
                lblMaCDW.setVisible(true);
                break;
            case 2:
                txtTenCD.setBackground(Contraints.INPUT_ERROR_BG);
                lblTenCDW.setVisible(true);
                break;
            case 3:
                txtHocPhi.setBackground(Contraints.INPUT_ERROR_BG);
                lblHocPhiW.setVisible(true);
                break;
            case 4:
                txtThoiLuong.setBackground(Contraints.INPUT_ERROR_BG);
                lblThoiLuongW.setVisible(true);
                break;
            case 5:
                lblHinh.setBackground(Contraints.INPUT_ERROR_BG);
                lblHinhW.setVisible(true);
                break;
        }
    }
    void clearWarning(){
        txtMaCD.setBackground(Contraints.INPUT_NORMAL_BG);
        txtTenCD.setBackground(Contraints.INPUT_NORMAL_BG);
        txtThoiLuong.setBackground(Contraints.INPUT_NORMAL_BG);
        txtHocPhi.setBackground(Contraints.INPUT_NORMAL_BG);
        lblHinh.setBackground(Contraints.INPUT_NORMAL_BG);
        clearIcon();
    }
    void clearIcon(){
        lblHinhW.setVisible(false);
        lblMaCDW.setVisible(false);
        lblTenCDW.setVisible(false);
        lblHocPhiW.setVisible(false);
        lblThoiLuongW.setVisible(false);
    }
    void insert() {
        ChuyenDe cd = getForm();
        if (cd == null) {
            return;
        }

        try {
            dao.insert(cd);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");
        }

    }

    void update() {

        ChuyenDe cd = getForm();
        if (cd == null) {
            return;
        }
        try {
            dao.update(cd);
            this.fillTable();
            //this.clearForm();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
        }

    }

    void delete() {
        if (Auth.isManager()) {
            String maCD = txtMaCD.getText().trim();
            if (MsgBox.confirm(this, "Bạn có chắc muốn xóa chuyên đề này?") == 0) {
                try {
                    KhoaHocDAO khdao = new KhoaHocDAO();
                    List<KhoaHoc> list = khdao.selectByKey(maCD);
                    if(!list.isEmpty()){
                        if (MsgBox.confirm(this, "Chuyên đề này đã chứa khóa học . Bạn có muốn tiếp tục xóa ?") != 0) return;
                        list.forEach((khoaHoc) -> {
                            khdao.delete(khoaHoc.getMaKH());
                        });
                    }
                    dao.delete(maCD);
                    fillTable();
                    clearForm();
                    MsgBox.alert(this, "Xóa chuyên đề thành công");
                } catch (Exception ex) {
                    MsgBox.alert(this, "Xóa chuyên đề thất bại");
                }
            }
        } else {
            MsgBox.alert(this, "Bạn không có quyền xóa chuyên đề");
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

        txtMaCD.setEditable(!edit);
        btnInsert.setEnabled(!edit);
        btnUpdate.setEnabled(edit);
        btnDelete.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);

        btnInsert.setBackground(edit ? Color.lightGray:Contraints.BLUEBUTTON_COLOR);
        btnUpdate.setBackground(!edit ? Color.lightGray:Contraints.BLUEBUTTON_COLOR);
        btnDelete.setBackground(!edit ? Color.lightGray:Contraints.REDBUTTON_COLOR);
        
        btnFirst.setBackground(edit && !first ? Contraints.GREENBUTTON_COLOR :Color.lightGray);
        btnPrev.setBackground(edit && !first ? Contraints.GREENBUTTON_COLOR:Color.lightGray);
        btnNext.setBackground(edit && !last ? Contraints.GREENBUTTON_COLOR:Color.lightGray);
        btnLast.setBackground(edit && !last ? Contraints.GREENBUTTON_COLOR:Color.lightGray);
    }

    void timKiem() {
        fillTable();
        clearForm();
    }
    void xuatFile() {
        String[] header = new String[]{"Mã chuyên đề", "Tên chuyên đề", "Học phí", "Thời lượng", "Hình", "Mô tả"};
        List<ChuyenDe> list = dao.selectByKey(txtTimKiem.getText().trim());
        List<Object[]> listObjs = new ArrayList<>();
        list.forEach((cd) -> {
            listObjs.add(cd.toObjects());
        });
        String fileName = "DSChuyende";
        String title ="Danh sách chuyên đề ("+txtTimKiem.getText().trim()+")";
        XFile.xuatFile(this, header, listObjs, fileName,title);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        lblTitle = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        pnlEdit = new javax.swing.JPanel();
        lblHinhW = new javax.swing.JLabel();
        lblHocPhiW = new javax.swing.JLabel();
        lblThoiLuongW = new javax.swing.JLabel();
        lblTenCDW = new javax.swing.JLabel();
        lblMaCDW = new javax.swing.JLabel();
        lblMoTa = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        lblMaCD = new javax.swing.JLabel();
        txtMaCD = new javax.swing.JTextField();
        lblTenCD = new javax.swing.JLabel();
        txtTenCD = new javax.swing.JTextField();
        lblThoiLuong = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        lblHocPhi = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        pnlGroupBtn = new javax.swing.JPanel();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        lblHinh = new javax.swing.JLabel();
        lblLogoText = new javax.swing.JLabel();
        pnlList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        pnlTimKiem = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        lblTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        btnInsert3 = new javax.swing.JButton();

        fileChooser.setCurrentDirectory(new java.io.File("C:\\Users\\Admin\\Downloads"));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(Contraints.TEXT1_COLOR);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ CHUYÊN ĐỀ");

        tabs.setBackground(new java.awt.Color(255, 255, 255));
        tabs.setFocusable(false);

        pnlEdit.setBackground(new java.awt.Color(255, 255, 255));
        pnlEdit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHinhW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblHinhW, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        lblHocPhiW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblHocPhiW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 300, -1, -1));

        lblThoiLuongW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblThoiLuongW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 217, -1, -1));

        lblTenCDW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblTenCDW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 134, -1, -1));

        lblMaCDW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/warning.png"))); // NOI18N
        pnlEdit.add(lblMaCDW, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 51, -1, -1));

        lblMoTa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMoTa.setText("Mô tả chuyên đề");
        pnlEdit.add(lblMoTa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, 30));

        jScrollPane2.setBorder(null);

        txtMoTa.setBackground(new java.awt.Color(204, 204, 204));
        txtMoTa.setColumns(20);
        txtMoTa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMoTa.setLineWrap(true);
        txtMoTa.setRows(4);
        txtMoTa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        jScrollPane2.setViewportView(txtMoTa);

        pnlEdit.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 810, 92));

        lblMaCD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMaCD.setText("Mã chuyên đề");
        pnlEdit.add(lblMaCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 13, -1, 30));

        txtMaCD.setBackground(new java.awt.Color(204, 204, 204));
        txtMaCD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMaCD.setForeground(Contraints.TEXT1_COLOR);
        txtMaCD.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtMaCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 43, 564, 40));

        lblTenCD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTenCD.setText("Tên chuyên đề");
        pnlEdit.add(lblTenCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 96, -1, 30));

        txtTenCD.setBackground(new java.awt.Color(204, 204, 204));
        txtTenCD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTenCD.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtTenCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 126, 564, 40));

        lblThoiLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblThoiLuong.setText("Thời lượng(giờ)");
        pnlEdit.add(lblThoiLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 179, -1, 30));

        txtThoiLuong.setBackground(new java.awt.Color(204, 204, 204));
        txtThoiLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtThoiLuong.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtThoiLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 209, 564, 40));

        lblHocPhi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblHocPhi.setText("Học phí");
        pnlEdit.add(lblHocPhi, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 262, 57, 30));

        txtHocPhi.setBackground(new java.awt.Color(204, 204, 204));
        txtHocPhi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtHocPhi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 7, 1, 1));
        pnlEdit.add(txtHocPhi, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 292, 564, 40));

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

        pnlEdit.add(pnlGroupBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 475, 810, -1));

        lblHinh.setBackground(new java.awt.Color(255, 255, 255));
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("Click để chọn logo");
        lblHinh.setAlignmentY(0.0F);
        lblHinh.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 153), 1, true));
        lblHinh.setOpaque(true);
        lblHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhMouseClicked(evt);
            }
        });
        pnlEdit.add(lblHinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 50, 234, 277));

        lblLogoText.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblLogoText.setText("Hình logo");
        pnlEdit.add(lblLogoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, -1, 30));

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
                "MÃ CHUYÊN ĐỀ", "TÊN CHUYÊN ĐỀ", "HỌC PHÍ", "THỜI GIAN", "HÌNH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        if (tblGridView.getColumnModel().getColumnCount() > 0) {
            tblGridView.getColumnModel().getColumn(1).setPreferredWidth(300);
        }

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

        btnInsert3.setBackground(new java.awt.Color(0, 102, 51));
        btnInsert3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnInsert3.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icons/excel.png"))); // NOI18N
        btnInsert3.setText("Xuất file Excel");
        btnInsert3.setBorderPainted(false);
        btnInsert3.setDefaultCapable(false);
        btnInsert3.setFocusable(false);
        btnInsert3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsert3ActionPerformed(evt);
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
                        .addComponent(btnInsert3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInsert3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnInsert3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsert3ActionPerformed
        // TODO add your handling code here:
        xuatFile();
    }//GEN-LAST:event_btnInsert3ActionPerformed

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

    private void lblHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhMouseClicked
        // TODO add your handling code here:
        chonAnh();
    }//GEN-LAST:event_lblHinhMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnInsert3;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblHinhW;
    private javax.swing.JLabel lblHocPhi;
    private javax.swing.JLabel lblHocPhiW;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblLogoText;
    private javax.swing.JLabel lblMaCD;
    private javax.swing.JLabel lblMaCDW;
    private javax.swing.JLabel lblMoTa;
    private javax.swing.JLabel lblTenCD;
    private javax.swing.JLabel lblTenCDW;
    private javax.swing.JLabel lblThoiLuong;
    private javax.swing.JLabel lblThoiLuongW;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlEdit;
    private javax.swing.JPanel pnlGroupBtn;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtMaCD;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtTenCD;
    private javax.swing.JTextField txtThoiLuong;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
