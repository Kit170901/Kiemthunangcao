package com.edusys.utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class XImage {

    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/com/edusys/icons/EdusysIcon.png");
        return new ImageIcon(url).getImage();
    }
    public static ImageIcon getIconTabList() {
        URL url = XImage.class.getResource("/com/edusys/icons/list1.png");
        return new ImageIcon(url);
    }
    public static ImageIcon getIconTabEdit() {
        URL url = XImage.class.getResource("/com/edusys/icons/edit.png");
        return new ImageIcon(url);
    }
    public static ImageIcon getIconTabNguoiHoc() {
        URL url = XImage.class.getResource("/com/edusys/icons/students.png");
        return new ImageIcon(url);
    }
    public static ImageIcon getIconTabHocVien() {
        URL url = XImage.class.getResource("/com/edusys/icons/hocvien.png");
        return new ImageIcon(url);
    }
    public static ImageIcon read(String fileName) {
        File path = new File("logos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }

    public static void save(File src) {
        File dst = new File("logos", src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        try {
            Path form = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(form, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static ImageIcon reSizeImg(ImageIcon icon, int scaleWidth, int scaleHeight) {
        Image image = icon.getImage();
        double height = icon.getIconHeight();
        double width = icon.getIconWidth();
        if (icon.getIconWidth() > scaleWidth ) {
            width = scaleWidth;
            height = (scaleWidth * icon.getIconHeight()) / icon.getIconWidth();
        }
        else if (icon.getIconHeight() > scaleHeight) {
            height = scaleHeight;
            width = (scaleWidth * icon.getIconHeight()) / icon.getIconHeight();
        }
        Image newimg = image.getScaledInstance((int) width, (int) height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}
