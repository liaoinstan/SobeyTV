package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterSignList;

import java.util.ArrayList;
import java.util.List;

public class SignActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup showingroup;
    private View showin;

    private TextView text_sign_day;
    private TextView text_sign_inte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
//        initCtrl();
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        text_sign_day = (TextView) findViewById(R.id.text_sign_day);
        text_sign_inte = (TextView) findViewById(R.id.text_sign_inte);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                initCtrl();
                LoadingViewUtil.showout(showingroup, showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 2000);
    }

    private void initCtrl() {
        findViewById(R.id.btn_go_signlist).setOnClickListener(this);

        {
            String strpre = "您已连续签到";
            String strday = "19";
            String straft = "天";
            SpannableString strSpan = new SpannableString(strpre + strday + straft);
            strSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sb_blue)), strpre.length(), strpre.length() + strday.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text_sign_day.setText(strSpan);
        }
        {
            String strpre = "共有";
            String strday = "5698";
            String straft = "积分";
            SpannableString strSpan = new SpannableString(strpre + strday + straft);
            strSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sb_blue)), strpre.length(), strpre.length() + strday.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text_sign_inte.setText(strSpan);
        }
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
        switch (v.getId()) {
            case R.id.btn_go_signlist:
                intent.setClass(this, SignListActivity.class);
                startActivity(intent);
                break;
        }
    }
}