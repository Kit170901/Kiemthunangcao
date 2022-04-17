/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.NguoiHoc;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NguoiHocDAO extends DAO<NguoiHoc, String>{

    @Override
    public void insert(NguoiHoc entity) {
        String sql= "INSERT    INTO    NguoiHoc    (MaNH,    HoTen,    NgaySinh,    GioiTinh,    DienThoai,Email, GhiChu,  MaNV,  NgayDK) VALUES  (?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?)";
        XJdbc.update(sql, entity.getMaNH(),entity.getHoTen(),entity.getNgaySinh(),entity.isGioiTinh(),
                entity.getDienThoai(),entity.getEmail(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayDK());
    }

    @Override
    public void update(NguoiHoc entity) {
        String sql= "UPDATE   NguoiHoc   SET   HoTen=?,   NgaySinh=?,   GioiTinh=?,   DienThoai=?,   Email=?, GhiChu=?,  MaNV=?,  NgayDK=?  WHERE  MaNH=?";
        XJdbc.update(sql,entity.getHoTen(),entity.getNgaySinh(),entity.isGioiTinh(),
                entity.getDienThoai(),entity.getEmail(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayDK(), entity.getMaNH());
    }

    @Override
    public void delete(String key) {
        String sql= "DELETE FROM NguoiHoc WHERE MaNH=?";
        XJdbc.update(sql, key);
    }

    @Override
    public List<NguoiHoc> selectAll() {
        String sql= "SELECT * FROM NguoiHoc";
        return selectBySql(sql);
    }

    @Override
    public NguoiHoc selectByID(String keys) {
        String sql= "SELECT * FROM NguoiHoc WHERE MaNH=?";
        return selectBySql(sql,keys).isEmpty()? null:selectBySql(sql,keys).get(0);
    }

    @Override
    protected List<NguoiHoc> selectBySql(String sql, Object... args) {
        List<NguoiHoc> list=new ArrayList<NguoiHoc>();       
        try {
            ResultSet rs=null;
            try {
                rs=XJdbc.query(sql,args);
                while (rs.next()) {
                    NguoiHoc e =new NguoiHoc();
                    e.setMaNH(rs.getString(1));
                    e.setHoTen(rs.getString(2));
                    e.setNgaySinh(rs.getDate(3));
                    e.setGioiTinh(rs.getBoolean(4));
                    e.setDienThoai(rs.getString(5));
                    e.setEmail(rs.getString(6));
                    e.setGhiChu(rs.getString(7));
                    e.setMaNV(rs.getString(8));
                    e.setNgayDK(rs.getDate(9));
                    list.add(e);
                }
            }finally{
            rs.getStatement().getConnection().close();
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }
        return list;
    }

    @Override
    public List<NguoiHoc> selectByKey(String keys) {
        String sql = "SELECT * FROM NguoiHoc WHERE MaNH LIKE ? OR HoTen LIKE ? OR MaNV like ?";
        keys= "%"+keys+"%";
        return selectBySql(sql, keys, keys,keys);
    }
    
    public List<NguoiHoc> selectNotInCourse(int maKH,String keys){
        String sql = "SELECT * FROM NguoiHoc WHERE (HoTen LIKE ? OR MaNH LIKE ?) AND MaNH NOT IN (SELECT MaNH FROM HocVien WHERE MaKH=?)";
        keys= "%"+keys+"%";
        return selectBySql(sql, keys, keys,maKH);
    }
}

