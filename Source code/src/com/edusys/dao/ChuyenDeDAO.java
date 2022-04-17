/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.ChuyenDe;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ChuyenDeDAO extends DAO<ChuyenDe, String>{

    @Override
    public void insert(ChuyenDe entity){
        String sql= "INSERT  INTO  ChuyenDe(MaCD,TenCD,HocPhi,ThoiLuong,Hinh,MoTa) VALUES  (?,  ?, ?,  ?,  ?,  ?)";
        XJdbc.update(sql, entity.getMaCD(),entity.getTenCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getHinh(),entity.getMoTa());
    }

    @Override
    public void update(ChuyenDe entity){
        String sql= "UPDATE  ChuyenDe  SET  TenCD=?,  HocPhi=?,  ThoiLuong=?,  Hinh=?,  MoTa=?  WHERE  MaCD=?";
        XJdbc.update(sql,entity.getTenCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getHinh(),entity.getMoTa(), entity.getMaCD());
    }

    @Override
    public void delete(String key){
        String sql= "DELETE FROM ChuyenDe WHERE MaCD=?";
        XJdbc.update(sql, key);
    }

    @Override
    public List<ChuyenDe> selectAll() {
        String sql= "SELECT * FROM ChuyenDe";
        return selectBySql(sql);
    }

    @Override
    public ChuyenDe selectByID(String keys) {
        String sql= "SELECT * FROM ChuyenDe WHERE MaCD=?";
        return selectBySql(sql,keys).isEmpty()? null:selectBySql(sql,keys).get(0);
    }

    @Override
    protected List<ChuyenDe> selectBySql(String sql, Object... args) {
        List<ChuyenDe> list=new ArrayList<ChuyenDe>();       
        try {
            ResultSet rs=null;
            try {
                rs=XJdbc.query(sql,args);
                while (rs.next()) {
                    ChuyenDe e =new ChuyenDe();
                    e.setMaCD(rs.getString(1));
                    e.setTenCD(rs.getString(2));
                    e.setHocPhi(rs.getDouble(3));
                    e.setThoiLuong(rs.getInt(4));
                    e.setHinh(rs.getString(5));
                    e.setMoTa(rs.getString(6));
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
    public List<ChuyenDe> selectByKey(String keys) {
        String sql= "SELECT * FROM ChuyenDe WHERE MaCD LIKE ? OR TenCD LIKE ? ";
        keys= "%"+keys+"%";
        return selectBySql(sql, keys, keys);
    }
}

