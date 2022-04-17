/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.utils;

import com.edusys.entity.NguoiHoc;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Admin
 */
public class XFile {

    public static void xuatFile(Component parent, String[] header, List<Object[]> row, String fileName,String title) {
        JFileChooser fileChooser = new JFileChooser(); 
        FileNameExtensionFilter Findxlsx = new FileNameExtensionFilter("Excel(.xlsx)", "xlsx", "xlsx");
        fileChooser.setFileFilter(Findxlsx);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Admin\\Documents"));
        fileChooser.setDialogTitle("Chọn nơi lưu");
        fileChooser.setSelectedFile(new File(fileName));
        int x = fileChooser.showDialog(parent, "Chọn thư mục");
        if (x == JFileChooser.APPROVE_OPTION) {
            try {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith("xlsx")) {
                    path += ".xlsx";
                }
                XExcel.clear();
                XExcel.setHeader(header);
                XExcel.setTitle(title);
                XExcel.setObjects(row);

                XExcel.create(path);
                if (MsgBox.confirm(parent, "Lưu thành công \n Bạn có muốn mở file không") == 0) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(new File(path));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                MsgBox.alert(parent, "Lưu thất bại");
            }
        }
    }
}
