package com.example.reminder.view;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.reminder.R;

public class taskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        TextView t1=(TextView)findViewById(R.id.detail_title);
        TextView t2=(TextView)findViewById(R.id.detail_detail);

        int position=getIntent().getIntExtra("position",0);

        t1.setText(getIntent().getStringExtra("title"));
        t2.setText(getIntent().getStringExtra("detail"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
