/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import java.awt.Frame;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 *
 * @author Admin
 */
public class HocVienJDialogTest {
	HocVienJDialog instance;

	//Danh sách các tham số truyền vào mỗi testcase
    @DataProvider(name = "input")
    public static Object[][] input() {
        return new Object[][]{
            {"-1", "Không hợp lệ"},
            {"4.3", "Chưa đạt"},
            {"6.1", "Trung bình"},
            {"7.9", "Khá"},
            {"8.7", "Giỏi"},
            {"9.2", "Xuất sắc"},
            {"11", "Không hợp lệ"}
        };
    }

    /**
     * Test of fillComboBoxChuyenDe method, of class HocVienJDialog.
     */
    @BeforeTest
    public void init() {
    	 instance = new HocVienJDialog(new Frame(), true);
    }
    
    //Testcase sẽ thực thi tướng ứng với mỗi phần tử trong danh sách tham số
    @Test(dataProvider = "input")
    public void testGetXepLoai(String diem,String expResult) {     
    	System.out.println("Điểm :"+diem);
    	System.out.println("Kết quả mon muốn "+expResult);
        String result = instance.getXepLoai(Double.parseDouble(diem));
        assertEquals(expResult, result);
    }
}
