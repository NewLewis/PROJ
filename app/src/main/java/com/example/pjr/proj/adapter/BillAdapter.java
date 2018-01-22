package com.example.pjr.proj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pjr.proj.Items.AccountItem;
import com.example.pjr.proj.R;

import java.util.List;

/**
 * Created by Py on 2018/1/5.
 */

//主界面listview的adapter
public class BillAdapter extends BaseAdapter {
    private Context context;
    private List<AccountItem> accountItems;
    final int IN = 0;
    final int OUT = 1;

    public BillAdapter(Context context,List<AccountItem> accountItems) {
        this.context=context;
        this.accountItems=accountItems;
    }

    private class ViewHolder {
        public TextView incomesName,incomesMoney,incomesRemarks;
        public TextView expensesName,expensesMoney,expensesRemarks;
        public ImageView img;
        public TextView date,year;
    }

    public void setView(int position,ViewHolder holder){
        //判断为收入时
        if(accountItems.get(position).getInOrOut() == IN) {
            holder.incomesName.setText(accountItems.get(position).getClassname());
            String money=String .format("%.2f",accountItems.get(position).getNumber());
            holder.incomesMoney.setText(money);
            holder.incomesRemarks.setText(accountItems.get(position).getDescrip());
            holder.expensesName.setText("");
            holder.expensesMoney.setText("");
            holder.expensesRemarks.setText("");
            if(accountItems.get(position).getClassname().equals("一般")) holder.img.setImageResource(R.drawable.incomes_yiban);
            else if(accountItems.get(position).getClassname().equals("报销")) holder.img.setImageResource(R.drawable.incomes_baoxiao);
            else if(accountItems.get(position).getClassname().equals("工资")) holder.img.setImageResource(R.drawable.incomes_gongzi);
            else if(accountItems.get(position).getClassname().equals("红包")) holder.img.setImageResource(R.drawable.incomes_hongbao);
            else if(accountItems.get(position).getClassname().equals("兼职")) holder.img.setImageResource(R.drawable.incomes_jianzhi);
            else if(accountItems.get(position).getClassname().equals("奖金")) holder.img.setImageResource(R.drawable.incomes_jiangjin);
            else holder.img.setImageResource(R.drawable.incomes_touzi);
        }
        else if(accountItems.get(position).getInOrOut() == OUT){
            holder.incomesName.setText("");
            holder.incomesMoney.setText("");
            holder.incomesRemarks.setText("");
            holder.expensesName.setText(accountItems.get(position).getClassname());
            String money=String .format("%.2f",accountItems.get(position).getNumber());
            holder.expensesMoney.setText(money);
            holder.expensesRemarks.setText(accountItems.get(position).getDescrip());
            if(accountItems.get(position).getClassname().equals("一般")) holder.img.setImageResource(R.drawable.expenses_yiban);
            else if(accountItems.get(position).getClassname().equals("用餐")) holder.img.setImageResource(R.drawable.expenses_yongcan);
            else if(accountItems.get(position).getClassname().equals("交通")) holder.img.setImageResource(R.drawable.expenses_jiaotong);
            else if(accountItems.get(position).getClassname().equals("服饰")) holder.img.setImageResource(R.drawable.expenses_fushi);
            else if(accountItems.get(position).getClassname().equals("丽人")) holder.img.setImageResource(R.drawable.expenses_liren);
            else if(accountItems.get(position).getClassname().equals("日用品")) holder.img.setImageResource(R.drawable.expenses_riyongpin);
            else if(accountItems.get(position).getClassname().equals("娱乐")) holder.img.setImageResource(R.drawable.expenses_yule);
            else if(accountItems.get(position).getClassname().equals("食材")) holder.img.setImageResource(R.drawable.expenses_shicai);
            else if(accountItems.get(position).getClassname().equals("零食")) holder.img.setImageResource(R.drawable.expenses_lingshi);
            else if(accountItems.get(position).getClassname().equals("酒水")) holder.img.setImageResource(R.drawable.expenses_jiushui);
            else if(accountItems.get(position).getClassname().equals("住房")) holder.img.setImageResource(R.drawable.expenses_zhufang);
            else if(accountItems.get(position).getClassname().equals("通讯")) holder.img.setImageResource(R.drawable.expenses_tongxun);
            else if(accountItems.get(position).getClassname().equals("家居")) holder.img.setImageResource(R.drawable.expenses_jiaju);
            else if(accountItems.get(position).getClassname().equals("人情")) holder.img.setImageResource(R.drawable.expenses_renqing);
            else if(accountItems.get(position).getClassname().equals("学习")) holder.img.setImageResource(R.drawable.expenses_xuexi);
            else if(accountItems.get(position).getClassname().equals("医疗")) holder.img.setImageResource(R.drawable.expenses_yiliao);
            else if(accountItems.get(position).getClassname().equals("旅游")) holder.img.setImageResource(R.drawable.expenses_lvyou);
            else holder.img.setImageResource(R.drawable.expenses_shuma);
        }
        holder.date.setText(Integer.toString(accountItems.get(position).getMonth())+"月"+Integer.toString(accountItems.get(position).getDay())+"日");
        holder.year.setText(Integer.toString(accountItems.get(position).getYear()));
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder holder;
        if(view==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.bill_list,null);
            holder=new ViewHolder();
            holder.incomesName=(TextView) convertView.findViewById(R.id.list_incomes_name);
            holder.incomesMoney = (TextView) convertView.findViewById(R.id.list_incomes_money);
            holder.incomesRemarks = (TextView) convertView.findViewById(R.id.list_incomes_remarks);
            holder.expensesName=(TextView) convertView.findViewById(R.id.list_expenses_name);
            holder.expensesMoney = (TextView) convertView.findViewById(R.id.list_expenses_money);
            holder.expensesRemarks = (TextView) convertView.findViewById(R.id.list_expenses_remarks);
            holder.img=(ImageView) convertView.findViewById(R.id.list_button);
            holder.date=(TextView)convertView.findViewById(R.id.list_date);
            holder.year=(TextView)convertView.findViewById(R.id.list_account);
            convertView.setTag(holder);
        }
        else {
            convertView=view;
            holder=(ViewHolder)convertView.getTag();
        }
        setView(position,holder);
        return convertView;
    }

    @Override
    public int getCount() {
        if (accountItems != null) {
            return accountItems.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(accountItems!=null) {
            return accountItems.get(i);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
