package com.example.pjr.proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pjr.proj.Items.AccountItem;
import com.example.pjr.proj.adapter.BillAdapter;
import com.example.pjr.proj.database.AccountHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*************************************************************************
 这里还差界面的跳转
 ************************************************************************/

public class MiddleActivity extends AppCompatActivity {

    ImageButton mAddBill,accountBook,statisticsChart;
    ImageView img;
    RelativeLayout.LayoutParams paramsImg,paramsListView;
    ListView billList;
    Button bookName;
    TextView monthlyIncomes,monthlyExpenses;
    AccountHelper accountHelper=new AccountHelper(this);
    List<AccountItem> items;
    BillAdapter billAdapter;
    double incomes=0,expenses=0;
    int flag=0;
    final int IN = 0;
    final int OUT = 1;
    private int bookid = 1;
    String ifChanged="";
    String book="";
    boolean ifHas=false;

    void findView(){
        mAddBill=(ImageButton)findViewById(R.id.add_bill);
        accountBook=(ImageButton)findViewById(R.id.account_book);
        statisticsChart=(ImageButton)findViewById(R.id.statistics_chart);
        img=(ImageView)findViewById(R.id.img);
        billList=(ListView)findViewById(R.id.bill_list);
        bookName=(Button)findViewById(R.id.book_name);
        monthlyIncomes=(TextView)findViewById(R.id.monthly_incomes);
        monthlyExpenses=(TextView)findViewById(R.id.monthly_expenses);
        paramsImg=(RelativeLayout.LayoutParams)img.getLayoutParams();
        paramsListView=(RelativeLayout.LayoutParams)billList.getLayoutParams();
    }

    //设置各个控件在屏幕上占据的位置
    void setView(){
        //获取屏幕宽高
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //设置图片高度为屏幕的四分之一
        paramsImg.height=displayMetrics.heightPixels/4;
        img.setLayoutParams(paramsImg);
        //设置按钮的中心和图片最高处对齐
        RelativeLayout.MarginLayoutParams m=new ViewGroup.MarginLayoutParams(mAddBill.getLayoutParams());
        m.setMargins(displayMetrics.widthPixels/2-mAddBill.getLayoutParams().width/2,displayMetrics.heightPixels/4-mAddBill.getLayoutParams().height/2,0,0);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(m);
        mAddBill.setLayoutParams(layoutParams);
        //设置ListView的高度为屏幕剩下的部分
        paramsListView.height=displayMetrics.heightPixels*3/4-mAddBill.getLayoutParams().height*3/4;
        billList.setLayoutParams(paramsListView);
    }

    //listview相关
    void updateListView() {
        ifHas=false;
        items=new ArrayList<AccountItem>();
        int size=0;
        Cursor cursor=accountHelper.getAllItem();
        while (cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndex("bookid")) == bookid) {
                size++;
                ifHas=true;
            }
        }
        final int[] ids=new int[size];
        final int[] inOrOuts=new int[size];
        final String[] names=new String[size];
        final double[] moneys=new double[size];
        final String[] remarkses=new String[size];
        final int[] years=new int[size];
        final int[] months=new int[size];
        final int[] days=new int[size];
        int i=0;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndex("bookid")) == bookid){
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


                Date date = accountHelper.getItemDate(cursor);

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                String year =sdf.format(date);
                years[i]=Integer.parseInt(year);

                SimpleDateFormat sdf1=new SimpleDateFormat("MM");
                String month =sdf1.format(date);
                months[i]=Integer.parseInt(month);

                SimpleDateFormat sdf2=new SimpleDateFormat("dd");
                String day =sdf2.format(date);
                days[i]=Integer.parseInt(day);
                i++;
            }

        }
        if(ifHas==false) {
        }
        else {
            for(i=0;i<size;i++){
                //add
                items.add(new AccountItem(ids[i],inOrOuts[i],names[i],moneys[i],remarkses[i],years[i],months[i],days[i],bookid));
                //计算收入和支出
                if(inOrOuts[i] == IN) incomes=incomes+moneys[i];
                else expenses=expenses+moneys[i];
            }
            billAdapter=new BillAdapter(MiddleActivity.this,items);
            billList.setAdapter(billAdapter);
            String in=String .format("%.2f",incomes);
            String out=String .format("%.2f",expenses);
            monthlyIncomes.setText("总收入\n"+in);
            monthlyExpenses.setText("总支出\n"+out);
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        bookid = Integer.parseInt(data.getExtras().getString("bookid"));
////        System.out.println(bookid);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        findView();
        setView();
        SQLiteDatabase database=accountHelper.getWritableDatabase();

//        Intent intent = getIntent();

        try{
            bookid = Integer.parseInt(getIntent().getStringExtra("bookid"));
            //bookid=1;
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println();
        }

        System.out.println("传入的"+bookid);

        //上方listview中的item添加相关
        //从数据库中读取账单相关并添加到list中
        updateListView();
        billList.setDivider(null);

        //点击进入每日账单页面
        billList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long l) {
                Intent intent=new Intent(MiddleActivity.this,DailyBill.class);
                intent.putExtra("year",Integer.toString(items.get(position).getYear()));
                intent.putExtra("month",Integer.toString(items.get(position).getMonth()));
                intent.putExtra("day",Integer.toString(items.get(position).getDay()));
                intent.putExtra("bookid",Integer.toString(bookid));
                startActivity(intent);
                finish();
            }
        });
        //长按删除
        billList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MiddleActivity.this);
                alertDialog.setTitle("删除记录").setMessage("确定删除记录" + items.get(position).getClassname() + "?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        accountHelper.deleteItem(items.get(position).getId());
                        items.remove(position);
                        incomes=0;expenses=0;
                        for(int i=0;i<items.size();i++) {
                            if(items.get(i).getInOrOut() == IN) incomes=incomes+items.get(i).getNumber();
                            else expenses=expenses+items.get(i).getNumber();
                        }
                        String in=String .format("%.2f",incomes);
                        String out=String .format("%.2f",expenses);
                        monthlyIncomes.setText("总收入\n"+in);
                        monthlyExpenses.setText("总支出\n"+out);
                        billAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });

        mAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MiddleActivity.this,AddBill.class);
                intent.putExtra("bookid",Integer.toString(bookid));
                startActivity(intent);
                finish();
            }
        });

        accountBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到账本列表界面
                Intent intent = new Intent(MiddleActivity.this,LeftActivity.class);
                startActivity(intent);
            }
        });

        statisticsChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到统计图界面
                Intent intent = new Intent(MiddleActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        Cursor cursor=accountHelper.getAllBook();
        while (cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndex("id"))==bookid) book=cursor.getString(cursor.getColumnIndex("name"));
        }
        bookName.setText(book);
        bookName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从数据库中读取当月收入和当月支出，相减计算余额
                if(flag==0){
                    Double money=incomes-expenses;
                    bookName.setText("余额："+String .format("%.2f",money));
                    flag=1;
                }
                else {
                    bookName.setText(book);
                    flag=0;
                }
            }
        });

    }

}

