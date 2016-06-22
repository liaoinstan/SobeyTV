package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class ComplainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView text_complain_serv;
    private TextView text_complain_tech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initCtrl();
    }

    private void initView() {
        text_complain_serv = (TextView) findViewById(R.id.text_complain_serv);
        text_complain_tech = (TextView) findViewById(R.id.text_complain_tech);
    }


    private void initCtrl() {
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
        }
    }
}
