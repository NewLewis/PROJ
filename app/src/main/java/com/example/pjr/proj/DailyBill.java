package com.example.pjr.proj;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pjr.proj.Items.AccountItem;
import com.example.pjr.proj.adapter.DailyAdapter;
import com.example.pjr.proj.database.AccountHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Py on 2017/12/23.
 */


/*************************************************************************
 还差各种字的设置，比如当日收入，当日日期等以及listview
 坐等数据库
 ************************************************************************/

//主界面的listview点进去以后看到的每日账单界面
//daily_bill对应界面
public class DailyBill extends Activity {

    RelativeLayout r1,r2,r3;
    RelativeLayout.LayoutParams paramsImg;
    ViewGroup.LayoutParams paramsListView;
    ImageView dailyImg;
    ListView dailyList;
    Button dailyBack;
    TextView jilu;
    final int IN = 0;
    final int OUT = 1;
    //日历相关
    TextView dailyIncomes,dailyExpenses,dailyYear,dailyMonth,dailyDate;
    LinearLayout dailyCalendar;
    Calendar calendar1;
    DatePickerDialog datePicker;
    //listview相关
    AccountHelper accountHelper=new AccountHelper(this);
    List<AccountItem> items;
    DailyAdapter dailyAdapter;
    double incomes=0,expenses=0;
    boolean ifHas=false;

    void findView(){
        r1=(RelativeLayout)findViewById(R.id.relaytive1);
        r2=(RelativeLayout)findViewById(R.id.relaytive2);
        r3=(RelativeLayout)findViewById(R.id.relaytive3);
        dailyImg=(ImageView)findViewById(R.id.daily_img);
        dailyList=(ListView)findViewById(R.id.daily_list);
        paramsImg=(RelativeLayout.LayoutParams)dailyImg.getLayoutParams();
        paramsListView=(ViewGroup.LayoutParams) dailyList.getLayoutParams();
        dailyBack=(Button)findViewById(R.id.daily_back);
        dailyIncomes=(TextView)findViewById(R.id.daily_incomes);
        dailyExpenses=(TextView)findViewById(R.id.daily_expenses);
        dailyYear=(TextView)findViewById(R.id.daily_week);
        dailyMonth=(TextView)findViewById(R.id.daily_month);
        dailyDate=(TextView)findViewById(R.id.daily_date);
        dailyCalendar=(LinearLayout)findViewById(R.id.daily_calendar);
        jilu=(TextView)findViewById(R.id.jilu);
    }

    //设置各个控件在屏幕上占据的位置
    void setView(){
        //获取屏幕宽高
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //设置图片高度为屏幕的四分之一
        paramsImg.height=displayMetrics.heightPixels/4;
        dailyImg.setLayoutParams(paramsImg);
        //设置ListView的高度为屏幕剩下的部分
        paramsListView.height=displayMetrics.heightPixels*7/12;
        dailyList.setLayoutParams(paramsListView);
    }

    //日历相关
    void datePicker(int year1, int month1, int day1, final int bookid) {
        calendar1 = Calendar.getInstance();
        //设置日历按钮上默认显示的字
        dailyYear.setText(Integer.toString(year1)+"年");
        dailyMonth.setText(Integer.toString(month1)+"月");
        dailyDate.setText(Integer.toString(day1));
        //datePicker相关
        datePicker = new DatePickerDialog(DailyBill.this, null,
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setCanceledOnTouchOutside(true);
        datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定的逻辑代码在监听中实现
                        DatePicker picker = datePicker.getDatePicker();
                        int year = picker.getYear();
                        int monthOfYear = picker.getMonth();
                        int dayOfMonth = picker.getDayOfMonth();
                        //month默认返回值-1，因此要调整为正确的值
                        monthOfYear++;
                        //设置按钮上的字
                        dailyYear.setText(Integer.toString(year)+"年");
                        dailyMonth.setText(Integer.toString(monthOfYear)+"月");
                        dailyDate.setText(Integer.toString(dayOfMonth));
                        //根据获取到的时间在数据库中搜索到当日的所有账单，添加到listview中
                        //总收入和总支出的数字改变
                        updateListView(year,monthOfYear,dayOfMonth,bookid);
                    }
                });
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消什么也不用做
                    }
                });
        dailyCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });
    }

    void updateListView(int year,int month,int day,int bookid) {
        ifHas=false;
        incomes=0;
        expenses=0;
        //根据获取到的时间在数据库中搜索到当日的所有账单，添加到listview中
        items=new ArrayList<AccountItem>();
        Cursor cursor=accountHelper.getAllItem();
        int size=0;
        //获取时间

        while(cursor.moveToNext()) {

            Date date = accountHelper.getItemDate(cursor);


            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            String year1 =sdf.format(date);
            int year2 = Integer.parseInt(year1);

            SimpleDateFormat sdf1=new SimpleDateFormat("MM");
            String month1 =sdf1.format(date);
            int month2 = Integer.parseInt(month1);

            SimpleDateFormat sdf2=new SimpleDateFormat("dd");
            String day1 =sdf2.format(date);
            int day2 = Integer.parseInt(day1);

            if(year == year2 && month == month2 && day == day2 && bookid==cursor.getInt(cursor.getColumnIndex("bookid"))) {
                size++;
            }
        }
        final int[] ids=new int[size];
        final int[] inOrOuts=new int[size];
        final String[] names=new String[size];
        final double[] moneys=new double[size];
        final String[] remarkses=new String[size];
        int i=0;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {

            Date date = accountHelper.getItemDate(cursor);


            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            String year1 =sdf.format(date);
            int year2 = Integer.parseInt(year1);

            SimpleDateFormat sdf1=new SimpleDateFormat("MM");
            String month1 =sdf1.format(date);
            int month2 = Integer.parseInt(month1);

            SimpleDateFormat sdf2=new SimpleDateFormat("dd");
            String day1 =sdf2.format(date);
            int day2 = Integer.parseInt(day1);

            if(year==year2 && month==month2 && day==day2 && bookid==cursor.getInt(cursor.getColumnIndex("bookid"))) {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                ids[i]=id;
                int inOrOut=Integer.parseInt(cursor.getString(cursor.getColumnIndex("inorout")));
                inOrOuts[i]=inOrOut;
                String name=cursor.getString(cursor.getColumnIndex("classname"));
                names[i]=name;
                double money=cursor.getDouble(cursor.getColumnIndex("number"));
                moneys[i]=money;
                String remarks=cursor.getString(cursor.getColumnIndex("descrip"));
                remarkses[i]=remarks;
                i++;
                ifHas=true;
            }
        }
        //如果最终没有记录，显示“没有记录哦”
        if(ifHas==false) {
            jilu.setVisibility(View.VISIBLE);
            r3.setVisibility(View.INVISIBLE);
            dailyList.setVisibility(View.INVISIBLE);
        }
        else {
            jilu.setVisibility(View.INVISIBLE);
            r3.setVisibility(View.VISIBLE);
            dailyList.setVisibility(View.VISIBLE);
            for(i=0;i<size;i++){
                //add
                items.add(new AccountItem(ids[i],inOrOuts[i],names[i],moneys[i],remarkses[i],year,month,day,bookid));
                //计算收入和支出
                if(inOrOuts[i] == IN) incomes=incomes+moneys[i];
                else expenses=expenses+moneys[i];
            }
            dailyAdapter=new DailyAdapter(DailyBill.this,items);
            dailyList.setAdapter(dailyAdapter);
            //总收入和总支出的数字改变
            String in=String .format("%.2f",incomes);
            String out=String .format("%.2f",expenses);
            dailyIncomes.setText("总收入\n"+in);
            dailyExpenses.setText("总支出\n"+out);
        }

        //点击条目编辑
        dailyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long l) {
                Intent intent=new Intent(DailyBill.this,UpdateBill.class);
                intent.putExtra("id",Integer.toString(items.get(position).getId()));
                intent.putExtra("bookid",Integer.toString(items.get(position).getBookId()));
                startActivity(intent);
                finish();
            }
        });
        //长按删除
        dailyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DailyBill.this);
                alertDialog.setTitle("删除记录").setMessage("确定删除记录" + items.get(position).getClassname() + "?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        accountHelper.deleteItem(items.get(position).getId());
                        items.remove(position);
                        dailyAdapter.notifyDataSetChanged();
                        incomes=0;expenses=0;
                        for(int i=0;i<items.size();i++) {
                            if(items.get(i).getInOrOut() == IN) incomes=incomes+items.get(i).getNumber();
                            else expenses=expenses+items.get(i).getNumber();
                        }
                        String in=String .format("%.2f",incomes);
                        String out=String .format("%.2f",expenses);
                        dailyIncomes.setText("总收入\n"+in);
                        dailyExpenses.setText("总支出\n"+out);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_bill);
        findView();
        setView();

        final Intent intent=getIntent();
        int year=Integer.parseInt(intent.getStringExtra("year"));
        int month=Integer.parseInt(intent.getStringExtra("month"));
        int day=Integer.parseInt(intent.getStringExtra("day"));
        final int bookid=Integer.parseInt(intent.getStringExtra("bookid"));


        datePicker(year,month,day,bookid);
        updateListView(year,month,day,bookid);



        dailyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(DailyBill.this,MiddleActivity.class);
                intent1.putExtra("bookid",Integer.toString(bookid));
                startActivity(intent1);
                finish();
            }
        });

    }
}
