/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import com.edusys.entity.NhanVien;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NhanVienDAO extends DAO<NhanVien, String> {

    @Override
    public void insert(NhanVien entity) {
        String sql = "INSERT  INTO  NhanVien  (MaNV,  MatKhau,  HoTen,  VaiTro)  VALUES  (?,  ?,  ?,  ?)";
        XJdbc.update(sql, entity.getMaNV(), entity.getMatKhau(), entity.getHoTen(), entity.isVaiTro());
    }

    @Override
    public void update(NhanVien entity){
        String sql = "UPDATE  NhanVien  SET  MatKhau=?,  HoTen=?,  VaiTro=? WHERE  MaNV=?";
        XJdbc.update(sql, entity.getMatKhau(), entity.getHoTen(), entity.isVaiTro(), entity.getMaNV());
    }

    @Override
    public void delete(String key) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        XJdbc.update(sql, key);
    }

    @Override
    public List<NhanVien> selectAll() {
        String sql = "SELECT * FROM NhanVien";
        return selectBySql(sql);
    }

    @Override
    public NhanVien selectByID(String keys) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        return selectBySql(sql, keys).isEmpty() ? null : selectBySql(sql, keys).get(0);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<NhanVien>();
        try {
            ResultSet rs = null;
            try {
                rs = XJdbc.query(sql, args);
                while (rs.next()) {
                    NhanVien e = new NhanVien();
                    e.setMaNV(rs.getString(1));
                    e.setMatKhau(rs.getString(2));
                    e.setHoTen(rs.getString(3));
                    e.setVaiTro(rs.getBoolean(4));
                    list.add(e);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (Exception ex) {
           throw new RuntimeException();
        }
        return list;
    }

    @Override
    public List<NhanVien> selectByKey(String keys) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV LIKE ? OR HoTen LIKE ?";
        keys= "%"+keys+"%";
        return selectBySql(sql, keys, keys);
    }
}
