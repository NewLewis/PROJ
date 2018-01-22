package com.example.pjr.proj.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pjr.proj.Items.AccountBook;
import com.example.pjr.proj.Items.AccountItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="Account.db";//数据库名称
    private static final int SCHEMA_VERSION=3;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    private static final String BOOK_TABLE = "books";
    private static final String CLASS_TABLE = "classes";
    private static final String ITEM_TABLE = "items";

    final int IN = 0; //收入
    final int OUT = 1; //支出

    final private String  BOOK_CREATE_CATEGORY =
            "CREATE TABLE " + BOOK_TABLE +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, color INTEGER, num INTEGER);";

    final private String  CLASS_CREATE_CATEGORY =
            "CREATE TABLE " + CLASS_TABLE +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, picid INTEGER);";


    final private String  ITEM_CREATE_CATEGORY =
            "CREATE TABLE " + ITEM_TABLE +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, inorout INTEGER, classid INTEGER, classname TEXT, classpic INTEGER, bookid INTEGER, date DATE, number FLOAT, descrip TEXT);";

    public AccountHelper(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists "+ BOOK_TABLE + ";");
        db.execSQL("drop table if exists "+ ITEM_TABLE + ";");
        db.execSQL(BOOK_CREATE_CATEGORY);
        db.execSQL(CLASS_CREATE_CATEGORY);
        db.execSQL(ITEM_CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 2:
                db.execSQL("drop table if exists "+ BOOK_TABLE + ";");
                db.execSQL(BOOK_CREATE_CATEGORY);
                db.execSQL("drop table if exists "+ ITEM_TABLE + ";");
                db.execSQL(ITEM_CREATE_CATEGORY);
            default:
        }
    }

    public Cursor getAllBook() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(BOOK_TABLE,null,null,null,null,null,null);
//        db.close();
        return c;
    }

    public int insertBook(AccountBook accountBook) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name", accountBook.getName());
        cv.put("color", accountBook.getColor());
        cv.put("num", accountBook.getNum());
        long i = db.insert(BOOK_TABLE, null, cv);
        db.close();
        return (int)i;

    }

    public void updateBook(int bookId, AccountBook accountBook) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(bookId)};
        ContentValues cv=new ContentValues();
        cv.put("name", accountBook.getName());
        cv.put("color", accountBook.getColor());
        cv.put("num", accountBook.getNum());
        db.update(BOOK_TABLE, cv, "ID=?", args);
        db.close();
    }

    public int getLastId(){
        String sql = "select id from " + BOOK_TABLE;
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        c.moveToLast();
        return c.getInt(0);
    }

    public void deleteBook(int bookId){
        String[] args = {String.valueOf(bookId)};
        getWritableDatabase().delete(BOOK_TABLE, "ID=?", args);
    }

    public Cursor getAllItem() {
        String sql = String.format("select * from %s order by date desc",ITEM_TABLE);
        return(getReadableDatabase().rawQuery(sql,null));
    }

    public void insertItem(AccountItem accountItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("inorout",accountItem.getInOrOut());
        cv.put("classid",accountItem.getClassId());
        cv.put("classname",accountItem.getClassname());
        cv.put("classpic",accountItem.getClasspic());
        cv.put("bookid",accountItem.getBookId());
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put("date", formater.format(accountItem.getDate()));
        cv.put("number",accountItem.getNumber());
        cv.put("descrip",accountItem.getDescrip());
        db.insert(ITEM_TABLE, null, cv);

        String[] args_book = {String.valueOf(accountItem.getBookId())};
        //得到该账本的项目数量
        Cursor c = db.query(BOOK_TABLE,null,"ID=?",args_book,null,null,null);
        if(c.moveToNext()) {
            int booknum = c.getInt(c.getColumnIndex("num"));
            //更新该账本的项目数量
            booknum ++;
            ContentValues cv2=new ContentValues();
            cv2.put("num", booknum);
            db.update(BOOK_TABLE, cv2 ,"ID=?",args_book);
        }

        db.close();
    }

    public void updateItem(int itemId, AccountItem accountItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv=new ContentValues();
        String id = "" + itemId;
        String []args = {id};

        cv.put("inorout",accountItem.getInOrOut());
        cv.put("classid",accountItem.getClassId());
        cv.put("classname",accountItem.getClassname());
        cv.put("classpic",accountItem.getClasspic());
        cv.put("bookid",accountItem.getBookId());
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put("date", formater.format(accountItem.getDate()));
        cv.put("number",accountItem.getNumber());
        cv.put("descrip",accountItem.getDescrip());
        db.update(ITEM_TABLE, cv, "ID=?",args);
        db.close();
    }

    public void deleteItem(int itemId){
        String[] args = {String.valueOf(itemId)};
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.query(ITEM_TABLE,null,"ID=?",args,null,null,null);
        //得到该项对应账本号
        int bookid = 0;
        if(c.moveToNext()){
            bookid = c.getInt(c.getColumnIndex("bookid"));
        }
        String[] args_book = {String.valueOf(bookid)};
        //得到该账本的项目数量
        c = db.query(BOOK_TABLE,null,"ID=?",args_book,null,null,null);
        if(c.moveToNext()){
            int booknum = c.getInt(c.getColumnIndex("num"));
            //更新该账本的项目数量
            booknum --;
            ContentValues cv=new ContentValues();
            cv.put("num", booknum);
            db.update(BOOK_TABLE, cv ,"ID=?",args_book);
        }


        db.delete(ITEM_TABLE, "ID=?", args);
    }

    public Cursor getItemByName(String className) {//根据点击事件获取id,查询数据库
        String sql = String.format("select * from %s where classname=%s",ITEM_TABLE,className);
        return(getReadableDatabase().rawQuery(sql,null));
    }

    public Cursor getItemByTimePeriod(String startTime,String endTime){
        String sql = String.format("select * from %s where date>'%s' and date<'%s' order by date",ITEM_TABLE,startTime,endTime);
        return(getReadableDatabase().rawQuery(sql,null));
    }

    public Cursor getExpensesByTimePeriod(String startTime,String endTime){
        String sql = String.format("select * from %s order by date",ITEM_TABLE,startTime,endTime);
        return(getReadableDatabase().rawQuery(sql,null));
    }

    public int getItemId(Cursor c){
        return c.getInt(0);
    }

    public float getItemNumber(Cursor c){
        return c.getInt(7);
    }

    public int getItemInOrOut(Cursor c){
        return c.getInt(1);
    }

    public Date getItemDate(Cursor c){
        String time = c.getString(6);
        Date date = string2date(time);
        return date;
    }

    public java.util.Date string2date(String str) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(str);
        }catch (ParseException e){
            System.out.println("hah");
            e.printStackTrace();
        }
        return null;
    }

    public int CountNum(int bookid){
        String sql = "select count(*) from item where bookid="+ bookid;
        Cursor c = getReadableDatabase().rawQuery(sql,null);
        c.moveToFirst();
        return  c.getInt(0);
    }

}
