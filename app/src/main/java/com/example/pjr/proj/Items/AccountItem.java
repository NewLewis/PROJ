package com.example.pjr.proj.Items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountItem {
    private int id;
    private int inOrOut;
    private int classId; //类别（食品）
    private String classname;
    private int classpic;//icon
    private int bookId; //账本id
    private Date date;  
    private double number; //price
    private String descrip; //描述

    public AccountItem(int id, int inOrOut, String classname, double number, String descrip,int year,int month,int day,int bookId) {
        this.id = id;
        this.inOrOut = inOrOut;
        this.classname = classname;

        String time = ""+ year + "-"+month+"-"+day;

        this.date = string2date(time);
        this.number = number;
        this.descrip = descrip;
        this.bookId=bookId;
    }

    public Date string2date(String str) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(str);
        }catch (ParseException e){
            System.out.println("hah");
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getClasspic() {
        return classpic;
    }

    public void setClasspic(int classpic) {
        this.classpic = classpic;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public int getYear(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
        String year=sdf.format(date);
        return Integer.parseInt(year);
    }

    public int getMonth(){
        SimpleDateFormat sdf=new SimpleDateFormat("MM");
        String month=sdf.format(date);
        return Integer.parseInt(month);
    }

    public int getDay(){
        SimpleDateFormat sdf=new SimpleDateFormat("dd");
        String day =sdf.format(date);
        return Integer.parseInt(day);
    }
}
