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
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonPojo;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Set;

public class ComplainActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private EditText edit_complain_describe;
    private TagFlowLayout flow_complain;

    private CircularProgressButton btn_go;

    private int orderId;

    private String[] mVals = new String[]
            {"客服投诉", "技术投诉"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
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
        flow_complain = (TagFlowLayout) findViewById(R.id.flow_complain);
        edit_complain_describe = (EditText) findViewById(R.id.edit_complain_describe);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }


    private void initCtrl() {
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
        flow_complain.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) ComplainActivity.this.getLayoutInflater().inflate(R.layout.tv, parent, false);
                tv.setText(s);
                return tv;
            }
        });
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

                netCommitComplain();

                break;
        }
    }

    private int getType() {
        Set<Integer> selectedList = flow_complain.getSelectedList();
        if (selectedList == null || selectedList.size() == 0) {
            return 0;
        } else if (selectedList.size() == 1) {
            ArrayList<Integer> list = new ArrayList<>(selectedList);
            if (list.get(0) == 0) {
                return 1;
            } else if (list.get(0) == 1) {
                return 2;
            }
        } else if (selectedList.size() == 2) {
            return 3;
        }
        return 0;
    }

    private void netCommitComplain() {
        String describe = edit_complain_describe.getText().toString();

        btn_go.setProgress(50);
        String msg = AppVali.reqfix_addDescribe(describe);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            btn_go.setProgress(-1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_go.setProgress(0);
                }
            }, 800);
        } else {
            RequestParams params = new RequestParams(AppData.Url.addcomplain);
            params.addHeader("token", AppData.App.getToken());
            params.addBodyParameter("orderId", orderId + "");
            params.addBodyParameter("content", describe);
            params.addBodyParameter("type", getType() + "");
            CommonNet.samplepost(params, CommonPojo.class, new CommonNet.SampleNetHander() {
                @Override
                public void netGo(int code, Object pojo, String text, Object obj) {
                    Toast.makeText(ComplainActivity.this, text, Toast.LENGTH_SHORT).show();

                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 800);
                }

                @Override
                public void netSetError(int code, String text) {
                    Toast.makeText(ComplainActivity.this, text, Toast.LENGTH_SHORT).show();
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
    }
}
