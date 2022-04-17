/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Admin
 */
public abstract class DAO<E,K> {
    public abstract void insert(E entity);
    public abstract void update(E entity);
    public abstract void delete(K key);
    public abstract List<E> selectAll();
    public abstract E selectByID(K keys);
    public abstract List<E> selectByKey(String keys);
    protected abstract List<E> selectBySql(String sql , Object... args);
}
