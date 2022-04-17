/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.KhoaHoc;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class KhoaHocDAO extends DAO<KhoaHoc, Integer>{

    @Override
    public void insert(KhoaHoc entity){
        String sql= "INSERT   INTO   KhoaHoc   (MaCD,   HocPhi,   ThoiLuong,   NgayKG,   GhiChu,   MaNV,   NgayTao) VALUES  (?,  ?,  ?,  ?,  ?,  ?,  ?)";
        XJdbc.update(sql, entity.getMaCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getNgayKG(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayTao());
    }

    @Override
    public void update(KhoaHoc entity){
        String sql= "UPDATE   KhoaHoc   SET   MaCD=?,   HocPhi=?,   ThoiLuong=?,  NgayKG=?,   GhiChu=?,   MaNV=?, NgayTao=? WHERE MaKH=?";
        XJdbc.update(sql, entity.getMaCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getNgayKG(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayTao(),entity.getMaKH());
    }

    @Override
    public void delete(Integer key){
        String sql= "DELETE FROM KhoaHoc WHERE MaKH=?";
        XJdbc.update(sql, key);
    }

    @Override
    public List<KhoaHoc> selectAll() {
        String sql= "SELECT * FROM KhoaHoc";
        return selectBySql(sql);
    }

    @Override
    public KhoaHoc selectByID(Integer keys) {
        String sql= "SELECT * FROM KhoaHoc WHERE MaKH=?";
        return selectBySql(sql,keys).isEmpty()? null:selectBySql(sql,keys).get(0);
    }

    @Override
    protected List<KhoaHoc> selectBySql(String sql, Object... args) {
        List<KhoaHoc> list=new ArrayList<KhoaHoc>();       
        try {
            ResultSet rs=null;
            try {
                rs=XJdbc.query(sql,args);
                while (rs.next()) {
                    KhoaHoc e =new KhoaHoc();
                    e.setMaKH(rs.getInt(1));
                    e.setMaCD(rs.getString(2));
                    e.setHocPhi(rs.getDouble(3));
                    e.setThoiLuong(rs.getInt(4));
                    e.setNgayKG(rs.getDate(5));
                    e.setGhiChu(rs.getString(6));
                    e.setMaNV(rs.getString(7));
                    e.setNgayTao(rs.getDate(8));
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
    public List<KhoaHoc> selectByKey(String keys){
        String sql= "SELECT * FROM KhoaHoc where MaCD like ?";
        keys= "%"+keys+"%";
        return selectBySql(sql,keys);
    }
}

