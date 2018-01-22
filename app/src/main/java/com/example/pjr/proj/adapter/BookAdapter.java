package com.example.pjr.proj.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pjr.proj.Items.AccountBook;
import com.example.pjr.proj.R;

import java.util.List;


public class BookAdapter extends BaseAdapter {
    private Context context;
    private List<AccountBook>list;
    private int textviewid;
    private LayoutInflater inflater;
    public BookAdapter(Context mcontext, int textViewResouceId, List<AccountBook>mlist){
        context=mcontext;
        list=mlist;
        textviewid=textViewResouceId;
        inflater = LayoutInflater.from(mcontext);
    }
    @Override
    public int getCount() {
        if (list==null) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list==null) return null;
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertview;
        mViewHolder viewHolderr;

        if (view==null){
            //convertview= LayoutInflater.from(context).inflate(textviewid,null);
            Log.e("view", String.valueOf(R.layout.item_grid));
            convertview = inflater.inflate(R.layout.item_grid, parent,false);
            viewHolderr=new mViewHolder();
            viewHolderr.name=convertview.findViewById(R.id.book_name);
            viewHolderr.num=convertview.findViewById(R.id.book_num);
            viewHolderr.layout=convertview.findViewById(R.id.grid_layout);
            convertview.setTag(viewHolderr);
        }
        else{
            convertview=view;
            viewHolderr=(mViewHolder) convertview.getTag();
        }

        viewHolderr.name.setText(list.get(position).getName());
        //viewHolderr.layout.setBackgroundResource(gridColors[list.get(position).getColor()]);
        switch (list.get(position).getColor()){
            case 0:
                viewHolderr.layout.setBackgroundResource(R.drawable.grey_grid);
                break;
            case 1:
                viewHolderr.layout.setBackgroundResource(R.drawable.red_grid);
                break;
            case 2:
                viewHolderr.layout.setBackgroundResource(R.drawable.green_grid);
                break;
            case 3:
                viewHolderr.layout.setBackgroundResource(R.drawable.yellow_grid);
                break;
        }
        viewHolderr.num.setText(String.valueOf(list.get(position).getNum())+"笔");
        return convertview;
    }
    private class mViewHolder{
        public TextView name;
        public TextView num;
        public LinearLayout layout;
    }

}
