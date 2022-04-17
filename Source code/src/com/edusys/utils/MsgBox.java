/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class MsgBox {
    public static void alert(Component parent,String message){
        JOptionPane.showMessageDialog(parent, message,"EduSys - Thông báo",JOptionPane.INFORMATION_MESSAGE);
    }
    public static int confirm(Component parent,String message){
        return JOptionPane.showConfirmDialog(parent, message,"EduSys - Xác nhận",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    }
    public static String promt(Component parent,String message){
        return JOptionPane.showInputDialog(parent, message,"EduSys - Vui lòng nhập",JOptionPane.INFORMATION_MESSAGE);
    }
}
