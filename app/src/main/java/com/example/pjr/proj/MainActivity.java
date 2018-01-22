package com.example.pjr.proj;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pjr.proj.Items.AccountItem;
import com.example.pjr.proj.Items.Item;
import com.example.pjr.proj.adapter.ItemListAdapter;
import com.example.pjr.proj.adapter.MyViewPagerAdapter;
import com.example.pjr.proj.database.AccountHelper;
import com.github.mikephil.charting.charts.BubbleChart;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private PagerTitleStrip pagerTitleStrip;
    private List<View> datas;
    private List<String> titles;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Button setTime;
    private DatePicker datePicker;
    private ImageButton back;
    private TextView timePeriod;
    private String startTime;
    private String endTime;
    private LineChartView lineChartView;
    private ProgressBar progressBar;
    private TextView rate;
    private TextView status;
    private TextView in,out,left;
    private TextView averageExpenses;
    private TextView averageExpenses2;
    private View view1,view2,view3;
    private TextView averageIncome;
    private PieChartView pieChartView1;
    private PieChartView pieChartView2;
    private ListView listView1;
    private ListView listView2;
    private List<AccountItem> list = new ArrayList<>();
    private Cursor cursor;
    private AccountHelper accountHelper = new AccountHelper(this);
    final int IN = 0;
    final int OUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        pagerTitleStrip=(PagerTitleStrip)findViewById(R.id.pagerTitleStrip);
        timePeriod = (TextView)findViewById(R.id.timePeriod);
        setTime = (Button)findViewById(R.id.setTime);
        back = (ImageButton) findViewById(R.id.back);
        initDate();
        initViewPager();
        myViewPagerAdapter=new MyViewPagerAdapter(datas,titles);
        viewPager.setAdapter(myViewPagerAdapter);
        setOnClickListener();
    }
    private void initViewPager() {
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        datas=new ArrayList<>();
        titles=new ArrayList<>();
        view1= LayoutInflater.from(this).inflate(R.layout.total,null);
        view2= LayoutInflater.from(this).inflate(R.layout.income,null);
        view3= LayoutInflater.from(this).inflate(R.layout.expenses,null);

        initTotal();
        initIncome();
        initExpenses();

        datas.add(view1);
        datas.add(view2);
        datas.add(view3);
        titles.add("汇总");
        titles.add("收入");
        titles.add("支出");
    }

    private void initTotal(){
        lineChartView = (LineChartView)view1.findViewById(R.id.lineChartView);
        rate = (TextView)view1.findViewById(R.id.rate);
        status = (TextView)view1.findViewById(R.id.status);
        progressBar = (ProgressBar)view1.findViewById(R.id.progressBar);

        in = (TextView)findViewById(R.id.in);
        out = (TextView)findViewById(R.id.out);
        left = (TextView)findViewById(R.id.left);
        averageExpenses = (TextView)findViewById(R.id.averageExpenses);

        cursor = accountHelper.getExpensesByTimePeriod(startTime,endTime);
        System.out.println(startTime + " " + endTime);

        lineChartView.animate();
        LineChartData lineChartData = new LineChartData();
        float expensesSum = 0;
        float incomeSum = 0;
        List values1 = new ArrayList();
        List values2 = new ArrayList();

        int firstDay = -1;
        float tmpIn = 0,tmpOut = 0;
        int pDayIn = -1,pDayOut = -1;

        while(cursor.moveToNext()){
//            System.out.println("haha");
            float number = accountHelper.getItemNumber(cursor);
//            Date date = string2date(accountHelper.)
            //获取日期
            Date date = accountHelper.getItemDate(cursor);
            SimpleDateFormat sdf1 = new SimpleDateFormat("DD");
            int day = Integer.parseInt(sdf1.format(date));
//            System.out.println(day);
            if(firstDay == -1){
                firstDay = day;
            }

            if(accountHelper.getItemInOrOut(cursor) == IN){
                if(tmpIn == 0){
                    tmpIn += accountHelper.getItemNumber(cursor);
                    pDayIn = day;
                }else if(day != pDayIn){
                    values2.add(new PointValue((float)(pDayIn - firstDay + 1),tmpIn));
                    tmpIn = accountHelper.getItemNumber(cursor);
                    pDayIn = day;
                }else{
                    tmpIn += accountHelper.getItemNumber(cursor);
                }
                incomeSum += number;
//                values2.add(new PointValue((float)20, 2));
            }
            if(accountHelper.getItemInOrOut(cursor) == OUT){
                if(tmpOut == 0){
                    tmpOut += accountHelper.getItemNumber(cursor);
                    pDayOut = day;
                }else if(day != pDayOut){
                    values1.add(new PointValue((float)(pDayOut - firstDay + 1),tmpOut));
//                    System.out.println("支出:" + tmpOut);
                    tmpOut = accountHelper.getItemNumber(cursor);
                    pDayOut = day;
                }else{
                    tmpOut += accountHelper.getItemNumber(cursor);
                }
                expensesSum += number;
            }
        }

        if(tmpIn != 0){
            values2.add(new PointValue((float)(pDayIn - firstDay + 1),tmpIn));
        }
        if(tmpOut != 0){
            values1.add(new PointValue((float)(pDayOut - firstDay + 1),tmpOut));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        DecimalFormat df = new DecimalFormat("0.00");

        if(startTime != null && endTime !=null && !startTime.equals("") && !endTime.equals("")){
            //计算天数差
            int days = differentDaysByMillisecond(string2date(startTime),string2date(endTime));
            double avg = 0;
            if(days == 0){
                avg = expensesSum;
            }else{
                avg = expensesSum/days;
            }

            averageExpenses.setText("当前日均消费" + df.format(avg) + "元");
            out.setText("" + df.format(expensesSum));
            in.setText("" + df.format(incomeSum));
            left.setText("" + df.format((incomeSum-expensesSum)));
        }

        float Rate = (incomeSum-expensesSum)/incomeSum*100;
        System.out.println(Rate);

        progressBar.setProgress((int)Rate);
        rate.setText("您的结余率为" + df.format((incomeSum-expensesSum)/incomeSum*100) + "%");

        if(Rate/100 > 0.8){
            status.setText("当前结余率健康，当前财务状况良好，继续加油哦^_^");
        }else if(Rate > 0.3){
            status.setText("当前结余率还不错，财务状况出现了一些小问题，但是可以调整，加油！");
        }else if(Rate > 0){
            status.setText("当前结余率可能出了点问题，基本属于月光族，您可能需要重新设计自己的理财计划，加油！");
        }else{
            status.setText("您最近貌似入不敷出了，您可能遇到了生活上的难关，也可能是理财的失败，但是别气馁，一切都会好起来的，加油^_^");
        }

//        values2.add(new PointValue((float)4,2));
//        values2.add(new PointValue((float)15,2));

//        Axis axisX = new Axis().setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
//        axisX.setName(startTime+"/"+endTime+"账单");

        Line line1 = new Line(values1).setColor(getResources().getColor(R.color.colorOrange));
        Line line2 = new Line(values2).setColor(getResources().getColor(R.color.colorLine2));
        List lines = new ArrayList();
        line1.setHasLabelsOnlyForSelected(true);
        line2.setHasLabelsOnlyForSelected(true);
        lines.add(line1);
        lines.add(line2);
        lineChartData.setLines(lines);
//        lineChartData.setAxisXBottom(axisX);
        lineChartData.setAxisYLeft(axisY);
        lineChartView.setLineChartData(lineChartData);
        lineChartView.callTouchListener();
    }

    private void initIncome(){
        pieChartView1 = (PieChartView)view2.findViewById(R.id.pieChartView);
        averageIncome = (TextView)view2.findViewById(R.id.averageIncome);
        listView1 = (ListView)view2.findViewById(R.id.listView);

        PieChartView chart;
        PieChartData data;

        boolean hasLabels = true;//是否在薄片上显示label
        boolean hasLabelsOutside = false;//是否在薄片外显示label
        boolean hasCenterCircle = false;//是否中间掏空一个圈
        boolean hasCenterText1 = true;//掏空圈是的title1
        boolean hasCenterText2 = true;//掏空圈是的title2
        boolean isExploded = false;//薄片是否分离
        boolean hasLabelForSelected = false;

        cursor = accountHelper.getExpensesByTimePeriod(startTime,endTime);

        //把数据按照类型不同放进map
        Map<String,Double> map = new HashMap<>();
        while(cursor.moveToNext()){
            if(accountHelper.getItemInOrOut(cursor) == IN){
                String name = cursor.getString(cursor.getColumnIndex("classname"));
                double num = accountHelper.getItemNumber(cursor);
                if(map.containsKey(name)){
                    map.put(name,map.get(name) + num);
                }else{
                    map.put(name,num);
                }
            }
        }

        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        List<SliceValue> values = new ArrayList<SliceValue>();
        List<Item> list = new ArrayList<>();

        double incomeSum = 0;
        while(it.hasNext()){
            String name = it.next();

            int color = getResources().getColor(R.color.colorLine2);
            if(name.equals("一般")){
                color = 0xfff5a623;
            }else if(name.equals("报销")){
                color = 0xffe85a15;
            }else if(name.equals("工资")){
                color = 0xffd4a035;
            }else if(name.equals("红包")){
                color = 0xffd0021b;
            }else if(name.equals("兼职")){
                color = 0xffe35181;
            }else if(name.equals("奖金")){
                color = 0xffe8971e;
            }else if(name.equals("投资")){
                color = 0xffdd5555;
            }

            SliceValue sliceValue = new SliceValue((Float.parseFloat(String.format(""+map.get(name)))),color);
            values.add(sliceValue);
            list.add(new Item(color,name,(Double)map.get(name),IN));
            incomeSum += map.get(name);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        pieChartView1.setPieChartData(data);
//        pieChartView1.setCircleFillRatio(0.5f);//设置放大缩小范围

        //将list按照价格从大到小进行排序
        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Item i = (Item)o1;
                Item j = (Item)o2;
                if(i.getPrice() > j.getPrice()){
                    return -1;
                }
                if(i.getPrice() < j.getPrice()){
                    return 1;
                }
                return 0;
            }
        });

        ItemListAdapter itemListAdapter = new ItemListAdapter(MainActivity.this,list);
        listView1.setAdapter(itemListAdapter);

        if(startTime != null && endTime!= null && !startTime.equals("") && !endTime.equals("")){
            //计算天数差
            int days = differentDaysByMillisecond(string2date(startTime),string2date(endTime));
            double avg = 0;
            if(days == 0){
                avg = incomeSum;
            }else{
                avg = incomeSum/days;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            averageIncome.setText(df.format(avg) + "元");
        }
    }

    private void initExpenses(){
        pieChartView2 = (PieChartView)view3.findViewById(R.id.pieChartView);
        averageExpenses2 = (TextView)view3.findViewById(R.id.averageExpenses2);
        listView2 = (ListView)view3.findViewById(R.id.listView);

        PieChartView chart;
        PieChartData data;

        boolean hasLabels = true;//是否在薄片上显示label
        boolean hasLabelsOutside = false;//是否在薄片外显示label
        boolean hasCenterCircle = false;//是否中间掏空一个圈
        boolean hasCenterText1 = true;//掏空圈是的title1
        boolean hasCenterText2 = true;//掏空圈是的title2
        boolean isExploded = false;//薄片是否分离
        boolean hasLabelForSelected = false;

        cursor = accountHelper.getExpensesByTimePeriod(startTime,endTime);

        //把数据按照类型不同放进map
        Map<String,Double> map = new HashMap<>();
        while(cursor.moveToNext()){
            if(accountHelper.getItemInOrOut(cursor) == OUT){
                String name = cursor.getString(cursor.getColumnIndex("classname"));
                double num = accountHelper.getItemNumber(cursor);
                if(map.containsKey(name)){
                    map.put(name,map.get(name) + num);
                }else{
                    map.put(name,num);
                }
            }
        }

        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        List<SliceValue> values = new ArrayList<SliceValue>();
        List<Item> list = new ArrayList<>();

        double expensesSum = 0;
        while(it.hasNext()){
            String name = it.next();

            int color = getResources().getColor(R.color.colorLine2);
            if(name.equals("一般")){
                color = 0xffafb341;
            }else if(name.equals("用餐")){
                color = 0xff81aca9;
            }else if(name.equals("交通")){
                color = 0xff6b83b7;
            }else if(name.equals("服饰")){
                color = 0xffb47ca7;
            }else if(name.equals("丽人")){
                color = 0xffe2728e;
            }else if(name.equals("日用品")){
                color = 0xff6797a8;
            }else if(name.equals("食材")){
                color = 0xfff19834;
            }else if(name.equals("零食")){
                color = 0xff9e7866;
            }else if(name.equals("酒水")){
                color = 0xff9c939e;
            }else if(name.equals("住房")){
                color = 0xffd1b95d;
            }else if(name.equals("通讯")){
                color = 0xff475999;
            }else if(name.equals("娱乐")){
                color = 0xffed9241;
            }else if(name.equals("家居")){
                color = 0xff52a24e;
            }else if(name.equals("人情")){
                color = 0xffd05b97;
            }else if(name.equals("学习")){
                color = 0xff6faa70;
            }else if(name.equals("医疗")){
                color = 0xffdba7bc;
            }else if(name.equals("旅游")){
                color = 0xff7f8b36;
            }else if(name.equals("数码")){
                color = 0xffb5a353;
            }

            SliceValue sliceValue = new SliceValue((Float.parseFloat(String.format(""+map.get(name)))),color);
            values.add(sliceValue);
            list.add(new Item(color,name,(Double)map.get(name),OUT));
            expensesSum += map.get(name);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        pieChartView2.setPieChartData(data);
//        pieChartView1.setCircleFillRatio(0.5f);//设置放大缩小范围

        //将list按照价格从大到小进行排序
        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Item i = (Item)o1;
                Item j = (Item)o2;
                if(i.getPrice() > j.getPrice()){
                    return -1;
                }
                if(i.getPrice() < j.getPrice()){
                    return 1;
                }
                return 0;
            }
        });

        ItemListAdapter itemListAdapter = new ItemListAdapter(MainActivity.this,list);
        listView2.setAdapter(itemListAdapter);

        if(startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")){
            //计算天数差
            int days = differentDaysByMillisecond(string2date(startTime),string2date(endTime));
            double avg = 0;
            if(days == 0){
                avg = expensesSum;
            }else{
                avg = expensesSum/days;
            }
            DecimalFormat df = new DecimalFormat("0.00");
            averageExpenses2.setText(df.format(avg) + "元");
        }

    }

    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    private void initDate(){
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE);
        String time1 = sharedPref.getString("startTime", null);
        String time2 = sharedPref.getString("endTime", null);
//        System.out.println(time1+" "+ time2);

        if(time1 == null || time2 == null){
            //需要重新设置
            Toast.makeText(getApplicationContext(),"请设置日期范围",Toast.LENGTH_SHORT).show();
        }else{
            if(!time1.equals("")){
                startTime = time1;
            }
            if(!time2.equals("")){
                endTime = time2;
            }
            //不需要重新设置
            timePeriod.setText(time1 + "/" + time2);
        }
    }

    public Date string2date(String str) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(str);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    private void setOnClickListener(){
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String [] choice = {"设置起始时间","设置结束时间"};
                builder.setTitle("请选择设置的内容");
                builder.setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Calendar calendar = Calendar.getInstance();
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                                //选择完日期后会调用该回调函数
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    //因为monthOfYear会比实际月份少一月所以这边要加1
                                    startTime = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                                    timePeriod.setText(startTime + "/" + endTime);

                                    //用sharedPreferences来保存日期键值对
                                    Context context = MainActivity.this;
                                    SharedPreferences sharedPref = context.getSharedPreferences("MY_PREFERENCE",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("startTime", startTime);
                                    editor.commit();
                                }
                            }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                            //设置时间的上下限
                            DatePicker picker = datePickerDialog.getDatePicker();
                            if(endTime != null){
                                long maxTime = string2date(endTime).getTime();
                                picker.setMaxDate(maxTime);
                            }
                            datePickerDialog.show();
                        }else if(which == 1){
                            Calendar calendar = Calendar.getInstance();
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                                //选择完日期后会调用该回调函数
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    //因为monthOfYear会比实际月份少一月所以这边要加1
                                    endTime = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                                    timePeriod.setText(startTime + "/" + endTime);

                                    //用sharedPreferences来保存日期键值对
                                    Context context = MainActivity.this;
                                    SharedPreferences sharedPref = context.getSharedPreferences("MY_PREFERENCE",
                                            Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("endTime", endTime);
                                    editor.commit();
                                }
                            }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                            //设置时间的上下限
                            DatePicker picker = datePickerDialog.getDatePicker();
                            if(startTime != null){
                                long minTime = string2date(startTime).getTime();
                                picker.setMinDate(minTime);
                            }
                            datePickerDialog.show();
                        }
                    }
                }).create().show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
