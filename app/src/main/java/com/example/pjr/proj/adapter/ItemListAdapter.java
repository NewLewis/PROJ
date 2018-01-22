package com.example.pjr.proj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pjr.proj.Items.Item;
import com.example.pjr.proj.R;

import java.util.List;

/**
 * Created by PJR on 2018/1/1.
 */

public class ItemListAdapter extends BaseAdapter {
    private Context context;
    private List<Item> list;
    final int IN = 0;
    final int OUT = 1;

    private class ViewHolder{
        public ImageView icon;
        public TextView kind;
        public ProgressBar progressBar;
        public TextView price;
        public View line;
    }

    public ItemListAdapter(Context context, List<Item> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        if(list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i){
        if(list == null){
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        View convertView;
        ViewHolder viewHolder;

        if(view == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.kind = (TextView)convertView.findViewById(R.id.kind);
            viewHolder.price = (TextView)convertView.findViewById(R.id.price);
            viewHolder.line = (View)convertView.findViewById(R.id.line1);
            viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar2);
            convertView.setTag(viewHolder);
        }else{
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(list.get(i).getKind().equals("一般")){
            if(list.get(i).getInOrOut() == IN)
                viewHolder.icon.setImageResource(R.drawable.incomes_yiban);
            else
                viewHolder.icon.setImageResource(R.drawable.expenses_yiban);
        }else if(list.get(i).getKind().equals("报销")){
            viewHolder.icon.setImageResource(R.drawable.incomes_baoxiao);
        }else if(list.get(i).getKind().equals("工资")){
            viewHolder.icon.setImageResource(R.drawable.incomes_gongzi);
        }else if(list.get(i).getKind().equals("红包")){
            viewHolder.icon.setImageResource(R.drawable.incomes_hongbao);
        }else if(list.get(i).getKind().equals("兼职")){
            viewHolder.icon.setImageResource(R.drawable.incomes_jianzhi);
        }else if(list.get(i).getKind().equals("奖金")){
            viewHolder.icon.setImageResource(R.drawable.incomes_jiangjin);
        }else if(list.get(i).getKind().equals("用餐")){
            viewHolder.icon.setImageResource(R.drawable.expenses_yongcan);
        }else if(list.get(i).getKind().equals("交通")){
            viewHolder.icon.setImageResource(R.drawable.expenses_jiaotong);
        }else if(list.get(i).getKind().equals("服饰")){
            viewHolder.icon.setImageResource(R.drawable.expenses_fushi);
        }else if(list.get(i).getKind().equals("丽人")){
            viewHolder.icon.setImageResource(R.drawable.expenses_liren);
        }else if(list.get(i).getKind().equals("日用品")){
            viewHolder.icon.setImageResource(R.drawable.expenses_riyongpin);
        }else if(list.get(i).getKind().equals("娱乐")){
            viewHolder.icon.setImageResource(R.drawable.expenses_yule);
        }else if(list.get(i).getKind().equals("食材")){
            viewHolder.icon.setImageResource(R.drawable.expenses_shicai);
        }else if(list.get(i).getKind().equals("零食")){
            viewHolder.icon.setImageResource(R.drawable.expenses_lingshi);
        }else if(list.get(i).getKind().equals("酒水")){
            viewHolder.icon.setImageResource(R.drawable.expenses_jiushui);
        }else if(list.get(i).getKind().equals("住房")){
            viewHolder.icon.setImageResource(R.drawable.expenses_zhufang);
        }else if(list.get(i).getKind().equals("通讯")){
            viewHolder.icon.setImageResource(R.drawable.expenses_tongxun);
        }else if(list.get(i).getKind().equals("家居")){
            viewHolder.icon.setImageResource(R.drawable.expenses_jiaju);
        }else if(list.get(i).getKind().equals("人情")){
            viewHolder.icon.setImageResource(R.drawable.expenses_renqing);
        }else if(list.get(i).getKind().equals("学习")){
            viewHolder.icon.setImageResource(R.drawable.expenses_xuexi);
        }else if(list.get(i).getKind().equals("医疗")){
            viewHolder.icon.setImageResource(R.drawable.expenses_yiliao);
        }else if(list.get(i).getKind().equals("旅游")){
            viewHolder.icon.setImageResource(R.drawable.expenses_lvyou);
        }else if(list.get(i).getKind().equals("数码")){
            viewHolder.icon.setImageResource(R.drawable.expenses_shuma);
        }
        viewHolder.kind.setText(list.get(i).getKind());
        viewHolder.price.setText("" + list.get(i).getPrice());

        viewHolder.progressBar.setDrawingCacheBackgroundColor(list.get(i).getColor());

        if(i == 0){
            viewHolder.progressBar.setProgress(100);
        }else{
            double rate = list.get(i).getPrice()/list.get(0).getPrice()*100;
            viewHolder.progressBar.setProgress((int)rate);
        }

        return convertView;
    }

    public void removeItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    private boolean isNULL(){
        return list == null;
    }
}

