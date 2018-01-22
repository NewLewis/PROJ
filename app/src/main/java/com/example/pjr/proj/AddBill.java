package com.example.pjr.proj;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by Py on 2017/12/19.
 */

//主界面点击加号以后跳出来的增加账目的界面
//add_bill对应界面
 /*************************************************************************
 这里还差一个日历的点击事件和一个备忘录的点击事件没做，坐等数据库
 具体实现是在tabhostExpenses和tabhostIncomes里
 ************************************************************************/
public class AddBill extends Activity {

    TabHost tabHost;
    Button back;
     LocalActivityManager manager;
     int bookid;

    void findView() {
        tabHost=(TabHost)findViewById(R.id.tabhost);
        back=(Button)findViewById(R.id.back);
    }

    //初始化tabhost
    void setTab(TabHost tabHost) {
        //载入activity
        Intent intent0=new Intent(this,AddIncomes.class);
        intent0.putExtra("bookid",Integer.toString(bookid));
        Intent intent1=new Intent(this,AddExpenses.class);
        intent1.putExtra("bookid",Integer.toString(bookid));
        //设置tabhost的标签
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("收入",null).setContent(intent0));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("支出",null).setContent(intent1));
        TabWidget tabWidget=tabHost.getTabWidget();
        //设置tabhost的字体大小
        for(int i=0;i<tabWidget.getChildCount();i++) {
            TextView textView=(TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            textView.setTextSize(20);
        }
        //默认选中标签为“支出”，颜色为橙色
        tabHost.setCurrentTab(1);
        updateTab(tabHost);
    }

    //设置选中的标签的颜色
    void updateTab(TabHost tabHost) {
        TabWidget tabWidget=tabHost.getTabWidget();
        for(int i=0;i<tabWidget.getChildCount();i++) {
            TextView textView=(TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            if(tabHost.getCurrentTab()==i) {
                textView.setTextColor(this.getResources().getColorStateList(android.R.color.holo_orange_dark));
            }
            else {
                textView.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);
        findView();
        //初始化activity管理者
        manager = new LocalActivityManager(AddBill.this, true);
        //通过管理者保存当前页面状态
        manager.dispatchCreate(savedInstanceState);
        //将管理者类对象添加至TabHost
        tabHost.setup(manager);

        Intent intent=getIntent();
        bookid=Integer.parseInt(intent.getStringExtra("bookid"));

        setTab(tabHost);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                updateTab(tabHost);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBill.this,MiddleActivity.class);
                intent.putExtra("bookid",Integer.toString(bookid));
                startActivity(intent);
                finish();
            }
        });

    }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         @SuppressWarnings("deprecation")
         Activity subActivity = manager.getCurrentActivity();  // 获取当前活动的Activity实例
         //判断是否实现返回值接口
         if (subActivity instanceof OnTabActivityResultListener) {
             //获取返回值接口实例
             OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
             //转发请求到子Activity
             listener.onTabActivityResult(requestCode, resultCode, data);
         }
     }
}
