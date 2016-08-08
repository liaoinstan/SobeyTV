package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.R;

public class MsgSelectActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private ViewGroup showingroup;
    private View showin;

    private View lay_msgselect_sysmsg;
    private View lay_msgselect_ordermsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgselect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        lay_msgselect_sysmsg = findViewById(R.id.lay_msgselect_sysmsg);
        lay_msgselect_ordermsg = findViewById(R.id.lay_msgselect_ordermsg);
    }

    private void initData() {
    }

    private void initCtrl() {
        lay_msgselect_sysmsg.setOnClickListener(this);
        lay_msgselect_ordermsg.setOnClickListener(this);
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
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.lay_msgselect_sysmsg:
                intent.setClass(this, MsgSysActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_msgselect_ordermsg:
                intent.setClass(this, MsgOrderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
