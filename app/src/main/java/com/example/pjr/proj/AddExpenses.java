package com.example.pjr.proj;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pjr.proj.Items.AccountItem;
import com.example.pjr.proj.database.AccountHelper;

import java.util.Calendar;

/**
 * Created by Py on 2017/12/31.
 */

public class AddExpenses extends Activity implements OnTabActivityResultListener {

    //上方按钮及栏目
    ImageButton expensesYiban,expensesYongcan,expensesJiaotong,expensesFushi,expensesLiren,expensesRiyongpin,expensesYule,expensesShicai,
            expensesLingshi,expensesJiushui,expensesZhufang,expensesTongxun,expensesJiaju,expensesRenqing,expensesXuexi,expensesYiliao,
            expensesLvyou,expensesShuma;
    ImageView expensesTypePic;
    TextView expensesTypeName,expensesMoney;
    RelativeLayout expensesDetail;
    //上方栏中点击“一般”跳出来的对话框
    ImageView dialogPic;
    TextView dialogText;
    EditText dialogEdit;
    //下方计算器部分
    Button cal1,cal2,cal3,cal4,cal5,cal6,cal7,cal8,cal9,cal0;
    Button OK,point,add,sub,clear;
    int cnt=0;                    //输入数字位数统计（小数点前不超过7位，小数点后不超过2位）
    boolean pointFlag=false;      //是否已经输入过小数点
    double numDouble=0;              //输入数字的double，用于计算
    String numString="";               //输入数字的string，用于显示在上方栏数字位
    int operator=0;                //运算符的种类，0为没有按过运算符，1为+，2为-
    final int IN = 0;
    final int OUT = 1;
    //其他
    TextView calendar;
    ImageButton remarks;
    Calendar calendar1;
    DatePickerDialog datePicker;
    //数据库
    AccountHelper accountHelper=new AccountHelper(this);
    String descrip;
    int bookid;

    void findView() {
        //上方按钮及栏目
        expensesYiban=(ImageButton)findViewById(R.id.expenses_yiban);
        expensesYongcan=(ImageButton)findViewById(R.id.expenses_yongcan);
        expensesJiaotong=(ImageButton)findViewById(R.id.expenses_jiaotong);
        expensesFushi=(ImageButton)findViewById(R.id.expenses_fushi);
        expensesLiren=(ImageButton)findViewById(R.id.expenses_liren);
        expensesRiyongpin=(ImageButton)findViewById(R.id.expenses_riyongpin);
        expensesYule=(ImageButton)findViewById(R.id.expenses_yule);
        expensesShicai=(ImageButton)findViewById(R.id.expenses_shicai);
        expensesLingshi=(ImageButton)findViewById(R.id.expenses_lingshi);
        expensesJiushui=(ImageButton)findViewById(R.id.expenses_jiushui);
        expensesZhufang=(ImageButton)findViewById(R.id.expenses_zhufang);
        expensesTongxun=(ImageButton)findViewById(R.id.expenses_tongxun);
        expensesJiaju=(ImageButton)findViewById(R.id.expenses_jiaju);
        expensesRenqing=(ImageButton)findViewById(R.id.expenses_renqing);
        expensesXuexi=(ImageButton)findViewById(R.id.expenses_xuexi);
        expensesYiliao=(ImageButton)findViewById(R.id.expenses_yiliao);
        expensesLvyou=(ImageButton)findViewById(R.id.expenses_lvyou);
        expensesShuma=(ImageButton)findViewById(R.id.expenses_shuma);
        expensesTypePic=(ImageView)findViewById(R.id.expenses_typePic);
        expensesTypeName=(TextView)findViewById(R.id.expenses_typeName);
        expensesMoney=(TextView)findViewById(R.id.expenses_money);
        expensesDetail=(RelativeLayout)findViewById(R.id.expenses_detail);
        //计算器
        cal1=(Button)findViewById(R.id.expenses_cal1);
        cal2=(Button)findViewById(R.id.expenses_cal2);
        cal3=(Button)findViewById(R.id.expenses_cal3);
        cal4=(Button)findViewById(R.id.expenses_cal4);
        cal5=(Button)findViewById(R.id.expenses_cal5);
        cal6=(Button)findViewById(R.id.expenses_cal6);
        cal7=(Button)findViewById(R.id.expenses_cal7);
        cal8=(Button)findViewById(R.id.expenses_cal8);
        cal9=(Button)findViewById(R.id.expenses_cal9);
        cal0=(Button)findViewById(R.id.expenses_cal0);
        OK=(Button)findViewById(R.id.expenses_ok);
        point=(Button)findViewById(R.id.expenses_point);
        add=(Button)findViewById(R.id.expenses_add);
        sub=(Button)findViewById(R.id.expenses_sub);
        clear=(Button)findViewById(R.id.expenses_clear);
        //其他（计算器上面那一条
        calendar=(TextView)findViewById(R.id.expenses_calendar);
        remarks=(ImageButton)findViewById(R.id.expenses_remarks);
    }

    void findDialogView(View view) {
        dialogPic=(ImageView)view.findViewById(R.id.dialog_pic);
        dialogText=(TextView)view.findViewById(R.id.dialog_text);
        dialogEdit=(EditText)view.findViewById(R.id.dialog_edit);
    }

    //支出部分点击事件
    //更改上方栏图片，底色和名字
    void Click() {
        expensesYiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_yiban);
                expensesTypeName.setText("一般");
                expensesDetail.setBackgroundColor(0xffafb341);
            }
        });
        expensesYongcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_yongcan);
                expensesTypeName.setText("用餐");
                expensesDetail.setBackgroundColor(0xff81aca9);
            }
        });
        expensesJiaotong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_jiaotong);
                expensesTypeName.setText("交通");
                expensesDetail.setBackgroundColor(0xff6b83b7);
            }
        });
        expensesFushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_fushi);
                expensesTypeName.setText("服饰");
                expensesDetail.setBackgroundColor(0xffb47ca7);
            }
        });
        expensesLiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_liren);
                expensesTypeName.setText("丽人");
                expensesDetail.setBackgroundColor(0xffe2728e);
            }
        });
        expensesRiyongpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_riyongpin);
                expensesTypeName.setText("日用品");
                expensesDetail.setBackgroundColor(0xff6797a8);
            }
        });
        expensesYule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_yule);
                expensesTypeName.setText("娱乐");
                expensesDetail.setBackgroundColor(0xffed9241);
            }
        });
        expensesShicai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_shicai);
                expensesTypeName.setText("食材");
                expensesDetail.setBackgroundColor(0xfff19834);
            }
        });
        expensesLingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_lingshi);
                expensesTypeName.setText("零食");
                expensesDetail.setBackgroundColor(0xff9e7866);
            }
        });
        expensesJiushui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_jiushui);
                expensesTypeName.setText("酒水");
                expensesDetail.setBackgroundColor(0xff9c939e);
            }
        });
        expensesZhufang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_zhufang);
                expensesTypeName.setText("住房");
                expensesDetail.setBackgroundColor(0xffd1b95d);
            }
        });
        expensesTongxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_tongxun);
                expensesTypeName.setText("通讯");
                expensesDetail.setBackgroundColor(0xff475999);
            }
        });
        expensesJiaju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_jiaju);
                expensesTypeName.setText("家居");
                expensesDetail.setBackgroundColor(0xff52a24e);
            }
        });
        expensesRenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_renqing);
                expensesTypeName.setText("人情");
                expensesDetail.setBackgroundColor(0xffd05b97);
            }
        });
        expensesXuexi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_xuexi);
                expensesTypeName.setText("学习");
                expensesDetail.setBackgroundColor(0xff6faa70);
            }
        });
        expensesYiliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_yiliao);
                expensesTypeName.setText("医疗");
                expensesDetail.setBackgroundColor(0xffdba7bc);
            }
        });
        expensesLvyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_lvyou);
                expensesTypeName.setText("旅游");
                expensesDetail.setBackgroundColor(0xff7f8b36);
            }
        });
        expensesShuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesTypePic.setImageResource(R.drawable.expenses_shuma);
                expensesTypeName.setText("数码");
                expensesDetail.setBackgroundColor(0xffb5a353);
            }
        });
    }

    //计算器相关
    void calculater() {
        //点击数字按钮输入数字
        cal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //没输入过小数点时
                if(pointFlag==false) {
                    //首先判断是否超过位数
                    if(cnt<7) {
                        //上方栏右边数字显示
                        numString=numString+"1";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    //超过位数
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                //输入小数点时
                else {
                    if(cnt==0) {
                        //上方栏右边数字显示
                        numString=numString+"1";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"1";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    //超过位数
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"2";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"2";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"2";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"3";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"3";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"3";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"4";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"4";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"4";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"5";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"5";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"5";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"6";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"6";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"6";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"7";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"7";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"7";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"8";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"8";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"8";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"9";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"9";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"9";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cal0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointFlag==false) {
                    if(cnt<7) {
                        numString=numString+"0";
                        expensesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"0";
                        expensesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"0";
                        expensesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(AddExpenses.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //点击小数点输入小数点
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //没输入过小数点时
                if(pointFlag==false) {
                    pointFlag=true;
                    cnt=0;
                    numString=numString+".";
                }
                //输入过小数点时
                else {
                    Toast.makeText(AddExpenses.this,"不能输入两次小数点", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击计算符号计算
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double temp;
                temp=Double.parseDouble(numString);
                if(operator<2) numDouble=numDouble+temp;
                else numDouble=numDouble-temp;
                operator=1;
                //OK的图标换成“=”
                OK.setText("=");
                //复位工作
                numString="";
                cnt=0;
                pointFlag=false;
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double temp;
                temp=Double.parseDouble(numString);
                if(operator<2) numDouble=numDouble+temp;
                else numDouble=numDouble-temp;
                operator=2;
                //OK的图标换成“=”
                OK.setText("=");
                //复位工作
                numString="";
                cnt=0;
                pointFlag=false;
            }
        });

        //点击OK按钮结算
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //=时
                if(operator!=0) {
                    //计算
                    if(operator==1) {
                        Double temp;
                        temp=Double.parseDouble(numString);
                        numDouble=numDouble+temp;
                    }
                    else {
                        Double temp;
                        temp=Double.parseDouble(numString);
                        numDouble=numDouble-temp;
                    }
                    //将numDouble中的数字显示到上方栏中
                    String temp;
                    temp=String.format("%.2f",numDouble);
                    expensesMoney.setText("￥"+temp);
                    //复位工作
                    numString="";
                    numDouble=0;
                    cnt=0;
                    pointFlag=false;
                    operator=0;
                    OK.setText("OK");
                }
                //OK按钮时
                else {
                    //保存各种数据到数据库中并关闭activity
                    //数字
                    String str1 = expensesMoney.getText().toString();
                    String str2 = str1.substring(1, str1.length());
                    double number=Double.parseDouble(str2);
                    //年月日相关
                    String date=calendar.getText().toString();
                    String year1="",month1="",day1="";
                    String temp="";
                    for(int i=0;i<date.length();i++){
                        if(date.charAt(i)=='年') {
                            year1=temp;
                            temp="";
                        }
                        else if(date.charAt(i)=='月') {
                            month1=temp;
                            temp="";
                        }
                        else if(date.charAt(i)=='日') {
                            day1=temp;
                            temp="";
                        }
                        else {
                            temp=temp+date.charAt(i);
                        }
                    }
                    AccountItem accountItem=new AccountItem(-1,OUT,expensesTypeName.getText().toString(),number,descrip,
                                                            Integer.parseInt(year1),Integer.parseInt(month1),Integer.parseInt(day1),bookid);
                    accountHelper.insertItem(accountItem);
                    Intent intent1=new Intent(AddExpenses.this,MiddleActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });

        //点击清零按钮清零
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numString="";
                numDouble=0;
                cnt=0;
                pointFlag=false;
                operator=0;
                OK.setText("OK");
                expensesMoney.setText("￥0.00");
            }
        });
    }

    void datePicker() {
        calendar1 = Calendar.getInstance();
        //设置日历按钮上默认显示的字
        int year1,month1,day1;
        year1=calendar1.get(Calendar.YEAR);
        month1=calendar1.get(Calendar.MONTH);
        day1=calendar1.get(Calendar.DAY_OF_MONTH);
        month1++;
        calendar.setText(Integer.toString(year1)+"年"+Integer.toString(month1)+"月"+Integer.toString(day1)+"日");
        //datePicker相关
        datePicker = new DatePickerDialog(AddExpenses.this, null,
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
                        calendar.setText(Integer.toString(year)+"年"+Integer.toString(monthOfYear)+"月"+Integer.toString(dayOfMonth)+"日");
                    }
                });
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消什么也不用做
                    }
                });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });
    }

    //回调相关
    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==1) {
            descrip=data.getStringExtra("remarks");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_expenses);

        findView();
        Click();
        calculater();
        datePicker();

        Intent intent=getIntent();
        bookid=Integer.parseInt(intent.getStringExtra("bookid"));
        System.out.println("hehe"+bookid);

        remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setClass(AddExpenses.this,AddRemarks.class);
                intent1.putExtra("date",calendar.getText().toString());
                //startActivityForResult(intent1,0);
                AddExpenses.this.getParent().startActivityForResult(intent1,0);
            }
        });

        expensesTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factor = LayoutInflater.from(AddExpenses.this);
                final View view_in = factor.inflate(R.layout.dialog, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddExpenses.this);
                alertDialog.setView(view_in);
                //初始化控件
                findDialogView(view_in);
                //初始化显示的内容
                //图片
                String type=expensesTypeName.getText().toString();
                if(type.equals("一般")) dialogPic.setImageResource(R.drawable.expenses_yiban);
                else if(type.equals("用餐")) dialogPic.setImageResource(R.drawable.expenses_yongcan);
                else if(type.equals("交通")) dialogPic.setImageResource(R.drawable.expenses_jiaotong);
                else if(type.equals("服饰")) dialogPic.setImageResource(R.drawable.expenses_fushi);
                else if(type.equals("丽人")) dialogPic.setImageResource(R.drawable.expenses_liren);
                else if(type.equals("日用品")) dialogPic.setImageResource(R.drawable.expenses_riyongpin);
                else if(type.equals("娱乐")) dialogPic.setImageResource(R.drawable.expenses_yule);
                else if(type.equals("食材")) dialogPic.setImageResource(R.drawable.expenses_shicai);
                else if(type.equals("零食")) dialogPic.setImageResource(R.drawable.expenses_lingshi);
                else if(type.equals("酒水")) dialogPic.setImageResource(R.drawable.expenses_jiushui);
                else if(type.equals("住房")) dialogPic.setImageResource(R.drawable.expenses_zhufang);
                else if(type.equals("通讯")) dialogPic.setImageResource(R.drawable.expenses_tongxun);
                else if(type.equals("家居")) dialogPic.setImageResource(R.drawable.expenses_jiaju);
                else if(type.equals("人情")) dialogPic.setImageResource(R.drawable.expenses_renqing);
                else if(type.equals("学习")) dialogPic.setImageResource(R.drawable.expenses_xuexi);
                else if(type.equals("医疗")) dialogPic.setImageResource(R.drawable.expenses_yiliao);
                else if(type.equals("旅游")) dialogPic.setImageResource(R.drawable.expenses_lvyou);
                else dialogPic.setImageResource(R.drawable.expenses_shuma);
                //字和输入框
                dialogText.setText(type);
                dialogEdit.setHint(type);
                //修改条目内容
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        expensesTypeName.setText(dialogEdit.getText().toString());
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
        });
    }
}
