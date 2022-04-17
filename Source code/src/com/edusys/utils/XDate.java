/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class XDate {
    static SimpleDateFormat formater = new SimpleDateFormat();
    public static Date toDate(String date,String patern){
        try {
            formater.applyPattern(patern);
            return formater.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }
    public static String toString(Date date,String patern){
        formater.applyPattern(patern);
        return formater.format(date);
    }
    public static Date addDays(Date date,long days){
        date.setTime(date.getTime()+days*24*60*60*1000);
        return date;
    }
}
