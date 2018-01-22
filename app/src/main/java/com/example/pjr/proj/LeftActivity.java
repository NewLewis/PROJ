package com.example.pjr.proj;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pjr.proj.Items.AccountBook;
import com.example.pjr.proj.adapter.BookAdapter;
import com.example.pjr.proj.database.AccountHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PJR on 2018/1/5.
 */

public class LeftActivity extends AppCompatActivity {

    private GridView gview;
    private ImageButton combt;//=complete buttom "完成"按钮
    private ImageButton delbt;//=delete buttom "删除"按钮
    private ImageButton edibt;//=edit buttom"编辑"按钮
    private ImageView bot;
    private TextView text_bot;
    private List<AccountBook> book_list;
    private BookAdapter bookAdapter;
    private AccountHelper dbhelper = new AccountHelper(LeftActivity.this);
    private int[] btColors = {R.id.color_rb1, R.id.color_rb2, R.id.color_rb3, R.id.color_rb4};
    //alertdialog里按钮名字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_activity);
        gview =  findViewById(R.id.book_grid);
        combt = findViewById(R.id.com_bt);
        delbt = findViewById(R.id.del_bt);
        edibt = findViewById(R.id.edi_bt);
        bot = findViewById(R.id.bot);
        text_bot = findViewById(R.id.text_bot);

        book_list = new ArrayList<>();
        try{
            dbhelper = new AccountHelper(this);
        } catch(Exception e){
            Toast.makeText(LeftActivity.this, "Exeption1 "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Cursor c = dbhelper.getAllBook();
        while(c.moveToNext()) {
            AccountBook accountBook = new AccountBook();
            accountBook.setId(c.getInt(c.getColumnIndex("id")));
            accountBook.setName(c.getString(c.getColumnIndex("name")));
            accountBook.setColor(c.getInt(c.getColumnIndex("color")));
            accountBook.setNum(c.getInt(c.getColumnIndex("num")));
            book_list.add(accountBook);
        }

        System.out.println(book_list.size());

//        try{
//            bookAdapter = new BookAdapter(LeftActivity.this, R.layout.item_grid, book_list);
//            gview.setAdapter(bookAdapter);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        //配置适配器
        bookAdapter = new BookAdapter(LeftActivity.this, R.layout.item_grid, book_list);
        gview.setAdapter(bookAdapter);

        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountBook item = new AccountBook();
                EditDialog(item,1,book_list.size());
            }
        });

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到中间的界面
                Intent intent = new Intent(LeftActivity.this,MiddleActivity.class);
                int extraid = book_list.get(position).getId();
                intent.putExtra("bookid",Integer.toString(extraid));
                startActivity(intent);
            }
        });

        gview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                showbot();
                Animation shake = AnimationUtils.loadAnimation(LeftActivity.this, R.anim.shake);//加载动画资源文件
                parent.postInvalidate();
                view.startAnimation(shake);
                view.setAlpha((float)0.5);
                //按下完成键，隐藏底部栏的三个按钮
                combt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notshowbot();
                        view.clearAnimation();
                        view.setAlpha((float)1);
                    }
                });
                //按下删除键，弹出dialog
                delbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(LeftActivity.this)
                                .setTitle("确认删除？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbhelper.deleteBook(book_list.get(position).getId());
                                        book_list.remove(position);
                                        bookAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        notshowbot();
                        view.clearAnimation();
                        view.setAlpha((float)1);
                    }
                });
                //按下编辑键，弹出dialog
                edibt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditDialog(book_list.get(position),0,position);
                        notshowbot();
                        view.clearAnimation();
                        view.setAlpha((float)1);
                    }
                });
                return true;

            }
        });
    }

    //显示底部栏的三个按钮
    void showbot(){
        combt.setVisibility(View.VISIBLE);
        delbt.setVisibility(View.VISIBLE);
        edibt.setVisibility(View.VISIBLE);
        bot.setVisibility(View.INVISIBLE);
        text_bot.setVisibility(View.INVISIBLE);
    }

    //隐藏底部栏的三个按钮
    void notshowbot(){
        combt.setVisibility(View.INVISIBLE);
        delbt.setVisibility(View.INVISIBLE);
        edibt.setVisibility(View.INVISIBLE);
        bot.setVisibility(View.VISIBLE);
        text_bot.setVisibility(View.VISIBLE);
    }

    //changeOrCreate: 0=change, 1=create
    private void EditDialog(final AccountBook item, final int changeOrCreate, final int position){
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(LeftActivity.this);
        final View dialogView = LayoutInflater.from(LeftActivity.this)
                .inflate(R.layout.left_dialog, null);

        final EditText book_name = dialogView.findViewById(R.id.name_edit);
        final RadioGroup bt_group = dialogView.findViewById(R.id.bt_group);

        if(changeOrCreate==1){
            editDialog.setTitle("创建账本");
            book_name.setText("新建账本");
        } else{
            editDialog.setTitle("修改账本信息");
            //没有check就是灰色0，红色1，绿色2，黄色3
            if(item.getColor()>=0){
                bt_group.check(btColors[item.getColor()]);
            }
            book_name.setText(item.getName());
        }

        bt_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    //颜色设定：没有check就是0，灰色1，红色2，绿色3，黄色4
                    case R.id.color_rb1: item.setColor(0);break;
                    case R.id.color_rb2: item.setColor(1);break;
                    case R.id.color_rb3: item.setColor(2);break;
                    case R.id.color_rb4: item.setColor(3);break;
                }
            }
        });

        editDialog.setView(dialogView);

        editDialog.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(changeOrCreate==1){
                            item.setName(book_name.getText().toString());
                            int i = dbhelper.insertBook(item);
                            item.setId(i);
                            book_list.add(item);
                        } else {
                            item.setName(book_name.getText().toString());
                            dbhelper.updateBook(item.getId(),item);
                            book_list.get(position).setColor(item.getColor());
                            book_list.get(position).setName(book_name.getText().toString());
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                });
        editDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        editDialog.show();

    }
}

