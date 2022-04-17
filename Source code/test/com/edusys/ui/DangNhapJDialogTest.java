/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Admin
 */
public class DangNhapJDialogTest {

    public DangNhapJDialogTest() {
    }

    //Test case xác nhận tài khoản đăng nhập đúng
    @Test
    public void testIsLogin1() {
        String manv = "admin";
        String matKhau = "123456";
        System.out.println("Account :");
        System.out.println("ID: " + manv);
        System.out.println("PW: " + matKhau);
        DangNhapJDialog instance = new DangNhapJDialog();
        boolean expResult = true;
        boolean result = instance.isLogin(manv, matKhau);
        assertEquals(expResult, result);
    }

  //Test case xác nhận tài khoản đăng nhập sai
    @Test
    public void testIsLogin2() {
        String manv = "admin";
        String matKhau = "123";
        System.out.println("Account :");
        System.out.println("ID: " + manv);
        System.out.println("PW: " + matKhau);
        DangNhapJDialog instance = new DangNhapJDialog();
        boolean expResult = false;
        boolean result = instance.isLogin(manv, matKhau);
        assertEquals(expResult, result);
    }

  //Test case tài khoản đăng nhập không tồn tại
    @Test(expectedExceptions  = NullPointerException.class)
    public void testIsLogin3() {
        String manv = "admin123";
        String matKhau = "123";
        System.out.println("Account :");
        System.out.println("ID: " + manv);
        System.out.println("PW: " + matKhau);
        DangNhapJDialog instance = new DangNhapJDialog();
        boolean expResult = false;
        boolean result = instance.isLogin(manv, matKhau);
        assertEquals(expResult, result);
    }
}
