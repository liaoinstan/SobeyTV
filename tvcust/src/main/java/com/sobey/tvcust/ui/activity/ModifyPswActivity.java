package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.common.common.CommonNet;
import com.sobey.common.utils.MD5Util;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.MyActivityCollector;
import com.sobey.tvcust.entity.CommonEntity;

import org.xutils.http.RequestParams;

public class ModifyPswActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private CircularProgressButton btn_go;
    private EditText edit_password_old;
    private EditText edit_password_new;
    private EditText edit_password_new_repeat;


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
        edit_password_old = (EditText)findViewById(R.id.edit_modifypsw_password_old);
        edit_password_new = (EditText)findViewById(R.id.edit_modifypsw_password_new);
        edit_password_new_repeat = (EditText)findViewById(R.id.edit_modifypsw_password_new_repeat);

        findViewById(R.id.btn_bank).setOnClickListener(this);
    }

    private void initData() {
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
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
            case R.id.btn_go: {
                String psw_old = edit_password_old.getText().toString();
                String psw_new = edit_password_new.getText().toString();
                String psw_new_repeat = edit_password_new_repeat.getText().toString();

                String msg = AppVali.modify_psw(psw_old,psw_new,psw_new_repeat);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    btn_go.setProgress(50);

                    RequestParams params = new RequestParams(AppData.Url.updatePassword);
                    params.addHeader("token", AppData.App.getToken());
                    params.addBodyParameter("oldpwd", MD5Util.md5(psw_old));
                    params.addBodyParameter("password", MD5Util.md5(psw_new));
                    CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {

                            Toast.makeText(ModifyPswActivity.this, text, Toast.LENGTH_SHORT).show();

                            btn_go.setProgress(100);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AppData.App.removeUser();
                                    AppData.App.removeToken();

                                    Intent intent = new Intent(ModifyPswActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    MyActivityCollector.finishAll();
                                }
                            }, 800);
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(ModifyPswActivity.this, text, Toast.LENGTH_SHORT).show();
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
}
