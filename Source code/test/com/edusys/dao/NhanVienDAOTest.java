/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.NhanVien;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

/**
 *
 * @author Admin
 */
public class NhanVienDAOTest {
    
    public NhanVienDAOTest() {
    }

    //Testcase
    //Thêm một bản ghi mới vào table
    //Thõa mãn nếu số lượng bản ghi +1 sau insert
    @Test(priority = 5)
    public void testInsert() {
        NhanVien entity = new NhanVien("NV1", "123", "NV1", true);
        NhanVienDAO instance = new NhanVienDAO();
        int expResult = instance.selectAll().size()+1;
        instance.insert(entity);
        int result = instance.selectAll().size();
        assertTrue(expResult==result);

    }

    //Testcase
    //Cập nhật một bản ghi mới vào table
    //Thõa mãn nếu thông tin trong bản ghi khớp với thông tin cập nhật
    @Test(priority = 6)
    public void testUpdate() {
        NhanVien entity = new NhanVien("NV1", "123", "NV1", true);
        NhanVienDAO instance = new NhanVienDAO();

        entity.setHoTen("NV2");
        instance.update(entity);
        String expResult1 = "NV2";
        String result1 = instance.selectByID(entity.getMaNV()).getHoTen();
        assertEquals(expResult1,result1);
    }
    
    //Testcase
    //Xóa một bản ghi mới vào table
  //Thõa mãn nếu số lượng bản ghi -1 sau delete
    @Test(priority = 7)
    public void testDelete() {
        NhanVien entity = new NhanVien("NV1", "123", "NV1", true);
        NhanVienDAO instance = new NhanVienDAO();
        
        int expResult2 = instance.selectAll().size()-1;
        instance.delete(entity.getMaNV());

        int result2 = instance.selectAll().size();
        assertTrue(expResult2==result2);
    }

    /**
     * Test of selectAll method, of class NhanVienDAO.
     */
    //Testcase
    //Truy vấn tất cả bản ghi trong bảng NhanVien
    //Thõa mãn nếu số lượng bản ghi đúng với số lượng thực tế trong DB
    @Test(priority = 2)
    public void testSelectAll() {
        System.out.println("selectAll");
        NhanVienDAO instance = new NhanVienDAO();
        int expResult = 6;
        int result = instance.selectAll().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of selectByID method, of class NhanVienDAO.
     */
    //Testcase
    //Truy vấn bản ghi theo ID trong bảng NhanVien
    //Thõa mãn nếu bản ghi trả về khác null
    @Test(priority = 3)
    public void testSelectByID() {
        System.out.println("selectByID");
        String keys = "admin1";
        NhanVienDAO instance = new NhanVienDAO();
        NhanVien result = instance.selectByID(keys);
        assertFalse(result==null);
    }

    /**
     * Test of selectBySql method, of class NhanVienDAO.
     */
    //Testcase
    //Truy vấn bản ghi theo câu lệnh SQL trong bảng NhanVien
    //Thõa mãn nếu bản ghi trả về khớp với kết quả mong muốn (1)
    @Test(priority = 1)
    public void testSelectBySql() {
        System.out.println("selectBySql");
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        Object[] args = new Object[]{"admin1"};
        NhanVienDAO instance = new NhanVienDAO();
        List<NhanVien> result = instance.selectBySql(sql, args);
        assertTrue(result.size()==1);
    }

    /**
     * Test of selectByKey method, of class NhanVienDAO.
     */
    //Testcase
    //Truy vấn các bản ghi theo từ khóa trong bảng NhanVien
    //Thõa mãn nếu bản ghi trả về bằng số lượng ước tính (2)
    @Test(priority = 4)
    public void testSelectByKey() {
        System.out.println("selectByKey");
        String keys = "admin";
        NhanVienDAO instance = new NhanVienDAO();
        int expResult = 2;
        int result = instance.selectByKey(keys).size();
        assertTrue(expResult== result);
    }
}
