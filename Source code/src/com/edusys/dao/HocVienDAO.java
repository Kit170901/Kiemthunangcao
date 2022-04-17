/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.HocVien;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HocVienDAO extends DAO<HocVien, Integer>{

    @Override
    public void insert(HocVien entity){
        String sql= "INSERT  INTO  HocVien  (MaKH,  MaNH,  Diem) VALUES  (?,  ?,  ?)";
        XJdbc.update(sql, entity.getMaKH(),entity.getMaNH(),entity.getDiem()==-1?null:entity.getDiem());
    }

    @Override
    public void update(HocVien entity)  {
        String sql= "UPDATE  HocVien  SET  MaKH=?,  MaNH=?,  Diem=?  WHERE  MaHV=?";
        XJdbc.update(sql, entity.getMaKH(),entity.getMaNH(),entity.getDiem(),entity.getMaHV());
    }

    @Override
    public void delete(Integer key) {
        String sql= "DELETE FROM HocVien WHERE MaHV=?";
        XJdbc.update(sql, key);
    }

    @Override
    public List<HocVien> selectAll() {
        String sql= "SELECT * FROM HocVien";
        return selectBySql(sql);
    }

    @Override
    public HocVien selectByID(Integer keys) {
        String sql= "SELECT * FROM HocVien WHERE MaHV=?";
        return selectBySql(sql,keys).isEmpty() ? null:selectBySql(sql,keys).get(0);
    }

    @Override
    protected List<HocVien> selectBySql(String sql, Object... args) {
        List<HocVien> list=new ArrayList<HocVien>();       
        try {
            ResultSet rs=null;
            try {
                rs=XJdbc.query(sql,args);
                while (rs.next()) {
                    HocVien e =new HocVien();
                    e.setMaHV(rs.getInt(1));
                    e.setMaKH(rs.getInt(2));
                    e.setMaNH(rs.getString(3));
                    e.setDiem(rs.getString(4)==null?-1:rs.getDouble(4));
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
    public List<HocVien> selectByKey(String keys) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public List<HocVien> selectByMaKH(int keys){
        String sql= "SELECT * FROM HocVien WHERE MaKH=?";
        return selectBySql(sql,keys);
    }
    public List<HocVien> selectByMaNH(String key) {
        String sql= "SELECT * FROM HocVien WHERE MaNH=?";
        return selectBySql(sql,key);
    }
    public HocVien selectByMaNHvaMaKH(String manh,int makh) {
        String sql= "SELECT * FROM HocVien WHERE MaNH=? and MaKH=?";
        return selectBySql(sql,manh,makh).size()==1?selectBySql(sql,manh,makh).get(0):null;
    }
}
