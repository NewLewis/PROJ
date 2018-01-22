package com.example.pjr.proj;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pjr.proj.database.AccountHelper;

/**
 * Created by Py on 2018/1/6.
 */

public class UpdateRemarks extends Activity {


    Button back;
    TextView date,OK;
    EditText remarks;
    AccountHelper accountHelper=new AccountHelper(this);

    void findView() {
        back=(Button)findViewById(R.id.remarks_back);
        OK=(TextView)findViewById(R.id.remarks_ok);
        date=(TextView)findViewById(R.id.remarks_date);
        remarks=(EditText)findViewById(R.id.remarks_enter);
    }

    void setCalendar(String day) {
        date.setText(day);
    }

    void setEditText(String id) {
        Cursor cursor=accountHelper.getAllItem();
        while (cursor.moveToNext()) {
            if(id.equals(Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))))) {
                remarks.setText(cursor.getString(cursor.getColumnIndex("descrip")));
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remarks);

        findView();

        final Intent intent = getIntent();
        String id=intent.getStringExtra("id");
        String day=intent.getStringExtra("date");

        setCalendar(day);
        setEditText(id);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将输入的内容回传给添加收入界面或添加支出界面
                Intent intent1 = new Intent();
                intent1.putExtra("remarks", remarks.getText().toString());
                setResult(1,intent1);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
