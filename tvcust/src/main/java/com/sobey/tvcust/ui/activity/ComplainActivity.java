package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class ComplainActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private TextView text_complain_serv;
    private TextView text_complain_tech;
    private EditText edit_complain_describe;

    private CircularProgressButton btn_go;

    private int orderId;

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
        text_complain_serv = (TextView) findViewById(R.id.text_complain_serv);
        text_complain_tech = (TextView) findViewById(R.id.text_complain_tech);
        edit_complain_describe = (EditText) findViewById(R.id.edit_complain_describe);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }


    private void initCtrl() {
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
        text_complain_serv.setOnClickListener(this);
        text_complain_tech.setOnClickListener(this);
        text_complain_serv.setSelected(true);
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
        switch (v.getId()){
            case  R.id.text_complain_serv:
                text_complain_serv.setSelected(true);
                text_complain_tech.setSelected(false);
                break;
            case  R.id.text_complain_tech:
                text_complain_serv.setSelected(false);
                text_complain_tech.setSelected(true);
                break;
            case  R.id.btn_go:
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
                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(0);
                        }
                    }, 800);
                    Log.e("liao", "" + text_complain_serv.isSelected());
                    Log.e("liao", "" + describe);
                }

                break;
        }
    }
}
