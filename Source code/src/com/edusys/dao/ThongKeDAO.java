/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ThongKeDAO {
    private List<Object[]> getListOfArray (String sql, String[] col, Object... args){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {                
                Object[] vals = new Object[col.length];
                for (int i = 0; i < col.length; i++) {
                    vals[i]= rs.getObject(col[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public List<Object[]> getBangDiem(int maKH){
        String sql="{CALL sp_BangDiem(?)}";
        String[] col ={"MaNH","HoTen","Diem"};
        return this.getListOfArray(sql, col, maKH);
    }
    public List<Object[]> getLuongNguoiHoc(){
        String sql="{CALL sp_ThongKeNguoiHoc}";
        String[] col ={"Nam","SoLuong","DauTien","CuoiCung"};
        return this.getListOfArray(sql, col);
    }
    public List<Object[]> getDiemChuyenDe(){
        String sql="{CALL sp_ThongKeDiem}";
        String[] col ={"ChuyenDe","SoHV","ThapNhat","CaoNhat","TrungBinh"};
        return this.getListOfArray(sql, col);
    }
    public List<Object[]> getDoanhThu(int nam){
        String sql="{CALL sp_ThongKeDoanhThu(?)}";
        String[] col ={"ChuyenDe","SoKH","SoHV","DoanhThu","ThapNhat","CaoNhat","TrungBinh"};
        return this.getListOfArray(sql, col, nam);
    }
}
