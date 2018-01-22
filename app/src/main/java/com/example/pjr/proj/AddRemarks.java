package com.example.pjr.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pjr.proj.database.AccountHelper;

/**
 * Created by Py on 2018/1/1.
 */

public class AddRemarks extends Activity {

    Button back;
    TextView date,OK;
    EditText remarks;
    AccountHelper accountHelper=new AccountHelper(this);
    Intent intent;

    void findView() {
        back=(Button)findViewById(R.id.remarks_back);
        OK=(TextView)findViewById(R.id.remarks_ok);
        date=(TextView)findViewById(R.id.remarks_date);
        remarks=(EditText)findViewById(R.id.remarks_enter);
    }

    void setCalendar(String day) {
        date.setText(day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remarks);

        findView();
        intent = getIntent();
        setResult(0,intent);
        String day=intent.getStringExtra("date");
        setCalendar(day);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(remarks.getText().toString());
                //将输入的内容回传给添加收入界面或添加支出界面
               Intent intent2 = new Intent();
                intent2.putExtra("remarks", remarks.getText().toString());
                setResult(1,intent2);
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
