package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.entity.CommonPojo;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Set;

public class FeedBackActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private EditText edit_complain_describe;

    private CircularProgressButton btn_go;

    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
        Intent intent = getIntent();
        if (intent.hasExtra("orderId")) {
            orderId = intent.getIntExtra("orderId", 0);
        }
    }

    private void initView() {
        edit_complain_describe = (EditText) findViewById(R.id.edit_complain_describe);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }


    private void initCtrl() {
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:

                btn_go.setClickable(false);

                netCommitComplain();

                break;
        }
    }

    private void netCommitComplain() {
        String describe = edit_complain_describe.getText().toString();

        btn_go.setProgress(50);
        String msg = AppVali.complain_commit(describe);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            btn_go.setProgress(-1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_go.setProgress(0);
                    btn_go.setClickable(true);
                }
            }, 800);
        } else {
            RequestParams params = new RequestParams(AppData.Url.feedback);
            params.addHeader("token", AppData.App.getToken());
            params.addBodyParameter("content", describe);
            CommonNet.samplepost(params, CommonPojo.class, new CommonNet.SampleNetHander() {
                @Override
                public void netGo(int code, Object pojo, String text, Object obj) {
                    //Toast.makeText(FeedBackActivity.this, text, Toast.LENGTH_SHORT).show();

                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FeedBackActivity.this,"谢谢您的建议",Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 800);
                }

                @Override
                public void netSetError(int code, String text) {
                    Toast.makeText(FeedBackActivity.this, text, Toast.LENGTH_SHORT).show();
                    btn_go.setProgress(-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(0);
                            btn_go.setClickable(true);
                        }
                    }, 800);
                }
            });
        }
    }
}
