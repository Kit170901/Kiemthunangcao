/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class XJdbc {
    static String driver ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dburl="jdbc:sqlserver://localhost:1433;databaseName=EduSys";
    static String user="sa";
    static String pass="";
    static {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    public static PreparedStatement getStmt (String sql, Object... args) throws SQLException{
        Connection con =DriverManager.getConnection(dburl,user,pass);
        PreparedStatement stsm;
        if(sql.trim().startsWith("{")){
            stsm= con.prepareCall(sql);
        }else{
            stsm= con.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            stsm.setObject(i+1,args[i]);
        }
        return stsm;
    }
    public static ResultSet query(String sql, Object... args) throws SQLException{
        PreparedStatement stsm= XJdbc.getStmt(sql, args);
        return stsm.executeQuery();
    }
    public static Object value(String sql, Object... args){
        try {
            ResultSet rs = query(sql, args);
            if(rs.next()){
                return rs.getObject(0);
            }
            rs.getStatement().getConnection().close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    public static int update(String sql, Object... args){
        try {
            PreparedStatement stsm = XJdbc.getStmt(sql, args);
            try {
                return stsm.executeUpdate();
            } finally {
                stsm.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        
    }
}
