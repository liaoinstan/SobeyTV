package com.sobey.tvcust.ui.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterCountOrder;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;
import com.sobey.tvcust.ui.dialog.DialogMouthPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CountOrderActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<TestEntity> results = new ArrayList<>();
    private RecycleAdapterCountOrder adapter;
    private ViewGroup showingroup;
    private View showin;

    private WebView webView;
    private View btn_bank;
    private View btn_next;
    private TextView text_time;

    private DialogMouthPicker dialog;

    private String format = "yyyy年MM月";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null) dialog.dismiss();
    }

    private void initBase() {
        dialog = new DialogMouthPicker(this);
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        webView = (WebView) findViewById(R.id.web_countorder);
        btn_bank = findViewById(R.id.btn_countorder_bank);
        btn_next = findViewById(R.id.btn_countorder_next);
        text_time = (TextView) findViewById(R.id.text_countorder_time);

        String datestr = TimeUtil.getTimeFor(format, new Date());
        text_time.setText(datestr);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading,false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                results.clear();
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                LoadingViewUtil.showout(showingroup,showin);

                freshCtrl();
                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 1000);
    }

    private void initCtrl() {
        adapter = new RecycleAdapterCountOrder(this,R.layout.item_recycle_countorder,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);

        btn_bank.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        text_time.setOnClickListener(this);
        dialog.setOnOKlistener(new DialogMouthPicker.OnOkListener() {
            @Override
            public void onOkClick(Date date) {
                String strdate = TimeUtil.getTimeFor("yyyy年MM月", date);
                text_time.setText(strdate);
                initData();
            }
        });
        //////////////////////////////
        //打开本包内asset目录下的index.html文件
        //wView.loadUrl(" file:///android_asset/index.html ");
        //打开本地sd卡内的index.html文件
        //wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
        //打开指定URL的html文件
        //wView.loadUrl(" http://m.oschina.net");

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);

        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);

//        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    public void freshCtrl(){
        adapter.notifyDataSetChanged();
        webView.loadUrl("file:///android_asset/count_test.html");
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
            case R.id.btn_countorder_bank: {
                String datestr = text_time.getText().toString();
                String datelaststr = TimeUtil.add("yyyy年MM月", datestr, Calendar.MONTH, -1);
                text_time.setText(datelaststr);
                initData();
                break;
            }
            case R.id.btn_countorder_next: {
                String datestr = text_time.getText().toString();
                String datelaststr = TimeUtil.add("yyyy年MM月", datestr, Calendar.MONTH, 1);
                text_time.setText(datelaststr);
                initData();
                break;
            }
            case R.id.text_countorder_time:
                String datestr = text_time.getText().toString();
                Date date = TimeUtil.getDateByStr("yyyy年MM月", datestr);
                dialog.setDate(date);
                dialog.show();
                break;
        }
    }
}
