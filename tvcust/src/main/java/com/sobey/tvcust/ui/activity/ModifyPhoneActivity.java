package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.common.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.User;

import org.xutils.http.RequestParams;

public class ModifyPhoneActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private EditText edit_phone;
    private EditText edit_vali;
    private TextView text_getvali;
    private CircularProgressButton btn_go;

    private String valicode;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyphone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        edit_phone = (EditText) findViewById(R.id.edit_modifyphone_phone);
        edit_vali = (EditText) findViewById(R.id.edit_modifyphone_vali);
        text_getvali = (TextView) findViewById(R.id.text_modifyphone_getvali);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }

    private void initData() {
    }

    private void initCtrl() {
        text_getvali.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_modifyphone_getvali: {

                final String phone = edit_phone.getText().toString();

                String msg = AppVali.regist_vali(phone);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams(AppData.Url.getvali);
                    params.addBodyParameter("mobile", phone);
                    CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {
                            if (pojo==null) netSetError(code,"接口异常");
                            else {
                                Toast.makeText(ModifyPhoneActivity.this, text, Toast.LENGTH_SHORT).show();
                                CommonEntity common = (CommonEntity) pojo;
                                valicode = common.getValicode();
                                ModifyPhoneActivity.this.phone = phone;

                                time = 60;
                                sendTimeMessage();
                                text_getvali.setEnabled(false);
                            }
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(ModifyPhoneActivity.this, text, Toast.LENGTH_SHORT).show();
                            time = 0;
                        }
                    });
                }
                break;
            }
            case R.id.btn_go: {
                String vali = edit_vali.getText().toString();

                String msg = AppVali.regist_phone(edit_phone.getText().toString(),phone, vali, valicode);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }else {
                    btn_go.setProgress(50);

                    RequestParams params = new RequestParams(AppData.Url.updateInfo);
                    params.addHeader("token", AppData.App.getToken());
                    params.addBodyParameter("mobile", phone);
                    CommonNet.samplepost(params,CommonEntity.class,new CommonNet.SampleNetHander(){
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {

                            Toast.makeText(ModifyPhoneActivity.this,text,Toast.LENGTH_SHORT).show();
                            User user = AppData.App.getUser();
                            user.setMobile(phone);
                            AppData.App.saveUser(user);

                            btn_go.setProgress(100);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("phone", phone);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            }, 800);
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(ModifyPhoneActivity.this,text,Toast.LENGTH_SHORT).show();
                            btn_go.setProgress(-1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_go.setProgress(0);
                                }
                            }, 800);
                        }
                    });
                }
                break;
            }
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_TIME) {
                if (time > 0) {
                    text_getvali.setText("" + time);
                    time--;
                    ModifyPhoneActivity.this.sendTimeMessage();
                } else {
                    text_getvali.setEnabled(true);
                    text_getvali.setText("获取验证码");
                    time = 60;
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
