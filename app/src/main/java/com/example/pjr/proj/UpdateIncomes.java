package com.example.pjr.proj;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Py on 2018/1/6.
 */

public class UpdateIncomes extends Activity implements OnTabActivityResultListener{

    ImageButton incomesYiban,incomesBaoxiao,incomesGongzi,incomesHongbao,incomesJianzhi,incomesJiangjin,incomesTouzi;
    ImageView incomesTypePic;
    TextView incomesTypeName,incomesMoney;
    RelativeLayout incomesDetail;
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
    String id;
    String descrip;
    String bookid;

    void findView() {
        incomesYiban=(ImageButton)findViewById(R.id.incomes_yiban);
        incomesBaoxiao=(ImageButton)findViewById(R.id.incomes_baoxiao);
        incomesGongzi=(ImageButton)findViewById(R.id.incomes_gongzi);
        incomesHongbao=(ImageButton)findViewById(R.id.incomes_hongbao);
        incomesJianzhi=(ImageButton)findViewById(R.id.incomes_jianzhi);
        incomesJiangjin=(ImageButton)findViewById(R.id.incomes_jiangjin);
        incomesTouzi=(ImageButton)findViewById(R.id.incomes_touzi);
        incomesTypePic=(ImageView)findViewById(R.id.incomes_typePic);
        incomesTypeName=(TextView)findViewById(R.id.incomes_typeName);
        incomesMoney=(TextView)findViewById(R.id.incomes_money);
        incomesDetail=(RelativeLayout)findViewById(R.id.incomes_detail);
        //计算器
        cal1=(Button)findViewById(R.id.incomes_cal1);
        cal2=(Button)findViewById(R.id.incomes_cal2);
        cal3=(Button)findViewById(R.id.incomes_cal3);
        cal4=(Button)findViewById(R.id.incomes_cal4);
        cal5=(Button)findViewById(R.id.incomes_cal5);
        cal6=(Button)findViewById(R.id.incomes_cal6);
        cal7=(Button)findViewById(R.id.incomes_cal7);
        cal8=(Button)findViewById(R.id.incomes_cal8);
        cal9=(Button)findViewById(R.id.incomes_cal9);
        cal0=(Button)findViewById(R.id.incomes_cal0);
        OK=(Button)findViewById(R.id.incomes_ok);
        point=(Button)findViewById(R.id.incomes_point);
        add=(Button)findViewById(R.id.incomes_add);
        sub=(Button)findViewById(R.id.incomes_sub);
        clear=(Button)findViewById(R.id.incomes_clear);
        //其他（计算器上面那一条
        calendar=(TextView) findViewById(R.id.incomes_calendar);
        remarks=(ImageButton)findViewById(R.id.incomes_remarks);
    }

    void findDialogView(View view) {
        dialogPic=(ImageView)view.findViewById(R.id.dialog_pic);
        dialogText=(TextView)view.findViewById(R.id.dialog_text);
        dialogEdit=(EditText)view.findViewById(R.id.dialog_edit);
    }

    void setView(final String id) {
        Cursor cursor=accountHelper.getAllItem();
        while (cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndex("id"))==Integer.parseInt(id) && cursor.getInt(cursor.getColumnIndex("bookid"))==Integer.parseInt(bookid)) {
                incomesTypeName.setText(cursor.getString(cursor.getColumnIndex("classname")));
                double money=cursor.getDouble(cursor.getColumnIndex("number"));
                String s=String .format("%.2f",money);
                incomesMoney.setText("￥"+s);

                Date date = accountHelper.getItemDate(cursor);


                SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                String year =sdf.format(date);


                SimpleDateFormat sdf1=new SimpleDateFormat("MM");
                String month =sdf1.format(date);


                SimpleDateFormat sdf2=new SimpleDateFormat("dd");
                String day =sdf2.format(date);

//                String year=Integer.toString(cursor.getInt(cursor.getColumnIndex("year")));
//                String month=Integer.toString(cursor.getInt(cursor.getColumnIndex("month")));
//                String day=Integer.toString(cursor.getInt(cursor.getColumnIndex("day")));
                calendar.setText(year+"年"+month+"月"+day+"日");
                break;
            }
        }
        if(incomesTypeName.getText().toString().equals("一般")) {
            incomesTypePic.setImageResource(R.drawable.incomes_yiban);
            incomesDetail.setBackgroundColor(0xfff5a623);
        }
        else if(incomesTypeName.getText().toString().equals("报销")) {
            incomesTypePic.setImageResource(R.drawable.incomes_baoxiao);
            incomesDetail.setBackgroundColor(0xffe85a15);
        }
        else if(incomesTypeName.getText().toString().equals("工资")) {
            incomesTypePic.setImageResource(R.drawable.incomes_gongzi);
            incomesDetail.setBackgroundColor(0xffd4a035);
        }
        else if(incomesTypeName.getText().toString().equals("红包")) {
            incomesTypePic.setImageResource(R.drawable.incomes_hongbao);
            incomesDetail.setBackgroundColor(0xffd0021b);
        }
        else if(incomesTypeName.getText().toString().equals("兼职")) {
            incomesTypePic.setImageResource(R.drawable.incomes_jianzhi);
            incomesDetail.setBackgroundColor(0xffe35181);
        }
        else if(incomesTypeName.getText().toString().equals("奖金")) {
            incomesTypePic.setImageResource(R.drawable.incomes_jiangjin);
            incomesDetail.setBackgroundColor(0xffe8971e);
        }
        else{
            incomesTypePic.setImageResource(R.drawable.incomes_touzi);
            incomesDetail.setBackgroundColor(0xffdd5555);
        }
    }

    //收入部分点击事件
    //更改上方栏图片，底色和名字
    void Click() {
        incomesYiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_yiban);
                incomesTypeName.setText("一般");
                incomesDetail.setBackgroundColor(0xfff5a623);
            }
        });
        incomesBaoxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_baoxiao);
                incomesTypeName.setText("报销");
                incomesDetail.setBackgroundColor(0xffe85a15);
            }
        });
        incomesGongzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_gongzi);
                incomesTypeName.setText("工资");
                incomesDetail.setBackgroundColor(0xffd4a035);
            }
        });
        incomesHongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_hongbao);
                incomesTypeName.setText("红包");
                incomesDetail.setBackgroundColor(0xffd0021b);
            }
        });
        incomesJianzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_jianzhi);
                incomesTypeName.setText("兼职");
                incomesDetail.setBackgroundColor(0xffe35181);
            }
        });
        incomesJiangjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_jiangjin);
                incomesTypeName.setText("奖金");
                incomesDetail.setBackgroundColor(0xffe8971e);
            }
        });
        incomesTouzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomesTypePic.setImageResource(R.drawable.incomes_touzi);
                incomesTypeName.setText("投资");
                incomesDetail.setBackgroundColor(0xffdd5555);
            }
        });
    }

    //计算器相关
    void calculater(final String s) {
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    //超过位数
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                //输入小数点时
                else {
                    if(cnt==0) {
                        //上方栏右边数字显示
                        numString=numString+"1";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"1";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    //超过位数
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"2";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"2";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"3";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"3";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"4";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"4";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"5";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"5";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"6";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"6";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"7";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"7";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"8";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"8";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"9";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"9";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                        incomesMoney.setText("￥"+numString+".00");
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入七位整数", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(cnt==0) {
                        numString=numString+"0";
                        incomesMoney.setText("￥"+numString+"0");
                        cnt++;
                    }
                    else if(cnt==1) {
                        numString=numString+"0";
                        incomesMoney.setText("￥"+numString);
                        cnt++;
                    }
                    else {
                        Toast.makeText(UpdateIncomes.this,"最多只能输入两位小数", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(UpdateIncomes.this,"不能输入两次小数点", Toast.LENGTH_SHORT).show();
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
                    incomesMoney.setText("￥"+temp);
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
                    String str1 = incomesMoney.getText().toString();
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
                    AccountItem accountItem=new AccountItem(0,IN,incomesTypeName.getText().toString(),number,descrip,
                            Integer.parseInt(year1),Integer.parseInt(month1),Integer.parseInt(day1),Integer.parseInt(bookid));

                    accountHelper.updateItem(Integer.parseInt(s),accountItem);
                    Intent intent1=new Intent(UpdateIncomes.this,DailyBill.class);
                    intent1.putExtra("year",year1);
                    intent1.putExtra("month",month1);
                    intent1.putExtra("day",day1);
                    intent1.putExtra("bookid",bookid);
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
                incomesMoney.setText("￥0.00");
            }
        });
    }

    //日历相关
    void datePicker() {
        calendar1 = Calendar.getInstance();
        //datePicker相关
        datePicker = new DatePickerDialog(UpdateIncomes.this, null,
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
        setContentView(R.layout.tabhost_incomes);
        findView();
        Click();

        final Intent intent=getIntent();
        id=intent.getStringExtra("id");
        bookid=intent.getStringExtra("bookid");
        calculater(id);
        setView(id);
        datePicker();

        remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setClass(UpdateIncomes.this,AddRemarks.class);
                intent1.putExtra("date",calendar.getText().toString());
                //startActivityForResult(intent1,0);
                UpdateIncomes.this.getParent().startActivityForResult(intent1,0);
            }
        });

        incomesTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factor = LayoutInflater.from(UpdateIncomes.this);
                final View view_in = factor.inflate(R.layout.dialog, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateIncomes.this);
                alertDialog.setView(view_in);
                //初始化控件
                findDialogView(view_in);
                //初始化显示的内容
                //图片
                String type=incomesTypeName.getText().toString();
                if(type.equals("一般")) dialogPic.setImageResource(R.drawable.incomes_yiban);
                else if(type.equals("报销")) dialogPic.setImageResource(R.drawable.incomes_baoxiao);
                else if(type.equals("工资")) dialogPic.setImageResource(R.drawable.incomes_gongzi);
                else if(type.equals("红包")) dialogPic.setImageResource(R.drawable.incomes_hongbao);
                else if(type.equals("兼职")) dialogPic.setImageResource(R.drawable.incomes_jianzhi);
                else if(type.equals("奖金")) dialogPic.setImageResource(R.drawable.incomes_jiangjin);
                else dialogPic.setImageResource(R.drawable.incomes_touzi);
                //字和输入框
                dialogText.setText(type);
                dialogEdit.setHint(type);
                //修改条目内容
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        incomesTypeName.setText(dialogEdit.getText().toString());
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
