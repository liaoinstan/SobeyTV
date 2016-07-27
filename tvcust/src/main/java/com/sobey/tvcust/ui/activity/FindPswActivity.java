package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.utils.MD5Util;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonEntity;

import org.xutils.http.RequestParams;

public class FindPswActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private CircularProgressButton btn_go;
    private EditText edit_phone;
    private EditText edit_vali;
    private EditText edit_password_new;
    private TextView text_getvali;

    private String valicode;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpsw);

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

        findViewById(R.id.btn_bank).setOnClickListener(this);
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
            case R.id.btn_bank:
                finish();
                break;
            case R.id.text_modifypsw_getvali: {
                final String phone = edit_phone.getText().toString();

                String msg = AppVali.regist_vali(phone);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    RequestParams params = new RequestParams(AppData.Url.getvalipsw);
                    params.addBodyParameter("mobile", phone);
                    CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {
                            if (pojo == null) netSetError(code, "接口异常");
                            else {
                                Toast.makeText(FindPswActivity.this, text, Toast.LENGTH_SHORT).show();
                                CommonEntity common = (CommonEntity) pojo;
                                valicode = common.getValicode();
                                FindPswActivity.this.phone = phone;

                                time = 60;
                                sendTimeMessage();
                                text_getvali.setEnabled(false);
                            }
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(FindPswActivity.this, text, Toast.LENGTH_SHORT).show();
                            time = 0;
                        }
                    });
                }
                break;
            }
            case R.id.btn_go: {
                String vali = edit_vali.getText().toString();
                String psw = edit_password_new.getText().toString();

                String msg = AppVali.find_psw(edit_phone.getText().toString(), phone, vali, valicode,psw);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    btn_go.setProgress(50);

                    RequestParams params = new RequestParams(AppData.Url.findPassword);
                    params.addHeader("token", AppData.App.getToken());
                    params.addBodyParameter("mobile", phone);
                    params.addBodyParameter("password", MD5Util.md5(psw));
                    CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {

                            Toast.makeText(FindPswActivity.this, text, Toast.LENGTH_SHORT).show();

                            btn_go.setProgress(100);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 800);
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(FindPswActivity.this, text, Toast.LENGTH_SHORT).show();
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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_TIME) {
                if (time>0) {
                    text_getvali.setText(""+time);
                    time--;
                    FindPswActivity.this.sendTimeMessage();
                }else {
                    text_getvali.setEnabled(true);
                    text_getvali.setText("获取验证码");
                    time = 60;
                }
            }
        }
    };
}
