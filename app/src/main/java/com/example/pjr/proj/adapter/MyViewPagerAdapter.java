package com.example.pjr.proj.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by PJR on 2017/12/31.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private List<View> datas;
    private List<String> titles;
    public  MyViewPagerAdapter(List<View> datas,List<String> titles ){
        this.datas=datas;
        this.titles=titles;
    }
    @Override
    public int getCount() {//返回页卡数量
        return datas.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {//判断View是否来自Object
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {//初始化一个页卡
        container.addView(datas.get(position));
        return datas.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁一个页卡
        container.removeView(datas.get(position));
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
