/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.NhanVienDAO;
import com.edusys.entity.NhanVien;
import com.edusys.utils.Auth;
import javax.swing.JFrame;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Admin
 */
public class DoiMatKhauJDialogTest {

    public DoiMatKhauJDialogTest() {

    }

    //Khôi phục tài khoản admin1 sau mỗi testcase
    //Cập nhật người dùng đang đăng nhập của hệ thống
    @BeforeMethod
    public void setUp() {
        new NhanVienDAO().update(new NhanVien("admin1", "123456", "admin1", true));
        Auth.USER = new NhanVienDAO().selectByID("admin1");
    }

    @AfterMethod
    public void tearDown() {
        new NhanVienDAO().update(new NhanVien("admin1", "123456", "admin1", true));
    }

    /**
     * Test of changePass method, of class DoiMatKhauJDialog.
     */
    //Trường hợp kiểm tra khi không có tài khoản đăng nhập
    @Test(expectedExceptions = NullPointerException.class)
    public void testChangePass1() {
        Auth.USER = null;
        String oldPw = "123";
        String newPw = "1234";
        String cfPw = "1234";
        DoiMatKhauJDialog instance = new DoiMatKhauJDialog(new JFrame(), true);
        String expResult = "";
        String result = instance.changePass(oldPw, newPw, cfPw);
        assertEquals(expResult, result);
    }

  //Trường hợp kiểm tra khi mật khẩu sai
    @Test
    public void testChangePass2() {
        String oldPw = "124";
        String newPw = "123";
        String cfPw = "123";
        DoiMatKhauJDialog instance = new DoiMatKhauJDialog(new JFrame(), true);
        String expResult = "Sai mật khẩu";
        String result = instance.changePass(oldPw, newPw, cfPw);
        assertEquals(expResult, result);
    }

    //Trường hợp kiểm tra khi mật khẩu không hợp lệ
    @Test
    public void testChangePass3() {
        String oldPw = "123456";
        String newPw = "12";
        String cfPw = "124";
        DoiMatKhauJDialog instance = new DoiMatKhauJDialog(new JFrame(), true);
        String expResult = "Mật khẩu mới phải từ 3 kí tự trở lên\n";
        String result = instance.changePass(oldPw, newPw, cfPw);
        assertEquals(expResult, result);
    }

    //Trường hợp kiểm tra khi xác nhận mật khẩu không khớp với mật khẩu mới
    @Test
    public void testChangePass4() {
        String oldPw = "123456";
        String newPw = "123";
        String cfPw = "124";
        DoiMatKhauJDialog instance = new DoiMatKhauJDialog(new JFrame(), true);
        String expResult = "Xác nhận mật khẩu không đúng";
        String result = instance.changePass(oldPw, newPw, cfPw);
        assertEquals(expResult, result);
    }

    //Trường hợp kiểm tra khi mật khẩu đổi mật khẩu thành công
    @Test
    public void testChangePass5() {
        String oldPw = "123456";
        String newPw = "123";
        String cfPw = "123";
        DoiMatKhauJDialog instance = new DoiMatKhauJDialog(new JFrame(), true);
        String expResult = "Đổi mật khẩu thành công";
        String result = instance.changePass(oldPw, newPw, cfPw);
        assertEquals(expResult, result);
    }
}
