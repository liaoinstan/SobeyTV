package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;

public class ModifyPswActivity extends AppCompatActivity implements View.OnClickListener{

    private CircularProgressButton btn_go;
    private EditText edit_phone;
    private EditText edit_vali;
    private EditText edit_password_new;
    private TextView text_getvali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypsw);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
        edit_phone = (EditText)findViewById(R.id.edit_modifypsw_phone);
        edit_vali = (EditText)findViewById(R.id.edit_modifypsw_vali);
        edit_password_new = (EditText)findViewById(R.id.edit_modifypsw_password_new);
        text_getvali = (TextView)findViewById(R.id.text_modifypsw_getvali);
    }

    private void initData() {
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        text_getvali.setOnClickListener(this);

        btn_go.setIndeterminateProgressMode(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go:
                btn_go.setProgress(50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(100);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 800);
                    }
                }, 2000);
                break;
            case R.id.text_modifypsw_getvali:
                sendTimeMessage();
                text_getvali.setEnabled(false);
                break;
        }
    }

    ////////////////////////////
    //获取验证码计时

    private int time = 60;
    private final static int WHAT_TIME = 0;
    private void sendTimeMessage() {
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_TIME);
            mHandler.sendEmptyMessageDelayed(WHAT_TIME, 1000);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_TIME) {
                if (time>0) {
                    text_getvali.setText(""+time);
                    time--;
                    ModifyPswActivity.this.sendTimeMessage();
                }else {
                    text_getvali.setEnabled(true);
                    text_getvali.setText("获取验证码");
                    time = 60;
                }
            }
        }
    };
}
