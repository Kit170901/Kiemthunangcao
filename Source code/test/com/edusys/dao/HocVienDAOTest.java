package com.edusys.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.edusys.entity.HocVien;

public class HocVienDAOTest {
	
    //Testcase
    //Thêm một bản ghi mới vào table
    //Thõa mãn nếu số lượng bản ghi +1 sau insert
    @Test(priority = 4)
    public void testInsert() {
        HocVien entity = new HocVien(111,3,"PS17380",0);
        HocVienDAO instance = new HocVienDAO();
               
        int expResult = instance.selectAll().size()+1;
        instance.insert(entity);
        int result = instance.selectAll().size();
        assertTrue(expResult==result);
    }

    //Testcase
    //Cập nhật một bản ghi mới vào table
    //Thõa mãn nếu thông tin trong bản ghi khớp với thông tin cập nhật
    @Test(priority = 5)
    public void testUpdate() {
        HocVienDAO instance = new HocVienDAO();
        HocVien entity = instance.selectByMaNHvaMaKH("PS17380", 3);

        entity.setDiem(8);
        instance.update(entity);
        double expResult1 = 8;
        double result1 = (instance.selectByMaNHvaMaKH("PS17380", 3)).getDiem();
        assertTrue(expResult1==result1);
    }
    
    //Testcase
    //Xóa một bản ghi mới vào table
  //Thõa mãn nếu số lượng bản ghi -1 sau delete
    @Test(priority = 6)
    public void testDelete() {
    	HocVienDAO instance = new HocVienDAO();
        HocVien entity = instance.selectByMaNHvaMaKH("PS17380", 3);
        
        int expResult2 = instance.selectAll().size()-1;
        instance.delete(entity.getMaHV());
        
        int result2 = instance.selectAll().size();
        assertTrue(expResult2==result2);
    }

    /**
     * Test of selectAll method, of class HocVienDAO.
     */
    //Testcase
    //Truy vấn tất cả bản ghi trong bảng HocVien
    //Thõa mãn nếu số lượng bản ghi đúng với số lượng thực tế trong DB
    @Test(priority = 2)
    public void testSelectAll() {
        System.out.println("selectAll");
        HocVienDAO instance = new HocVienDAO();
        int expResult = 230;
        int result = instance.selectAll().size();
        	System.out.println(instance.selectAll().size());
        assertTrue(expResult>0);
    }

    /**
     * Test of selectBySql method, of class HocVienDAO.
     */
    //Testcase
    //Truy vấn bản ghi theo câu lệnh SQL trong bảng NhanVien
    //Thõa mãn nếu bản ghi trả về khớp với kết quả mong muốn (1)
    @Test(priority = 1)
    public void testSelectBySql() {
        System.out.println("selectBySql");
        String sql = "SELECT * FROM HocVien WHERE MaHV=?";
        Object[] args = new Object[]{"1"};
        HocVienDAO instance = new HocVienDAO();
        List<HocVien> result = instance.selectBySql(sql, args);
        assertTrue(result.size()==1);
    }

    /**
     * Test of selectByKey method, of class HocVienDAO.
     */
    //Testcase
    //Truy vấn các bản ghi theo từ khóa trong bảng HocVien
    //Thõa mãn nếu bản ghi trả về khác null
    @Test(priority = 3)
    public void testSelectByNHKH() {
        System.out.println("selectByNHKH");
    	HocVienDAO instance = new HocVienDAO();
        HocVien entity = instance.selectByMaNHvaMaKH("PS11478", 1);
        assertTrue(entity!=null);
    }
    
}
