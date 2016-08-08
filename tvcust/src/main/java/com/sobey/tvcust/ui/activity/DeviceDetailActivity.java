package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.SobeyNet;
import com.sobey.tvcust.entity.SBDeviceDetailPojo;
import com.sobey.tvcust.entity.SBKeyValue;
import com.sobey.tvcust.entity.SBDevice;
import com.sobey.tvcust.ui.adapter.ListAdapterCenterProg;
import com.sobey.tvcust.utils.UrlUtils;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class DeviceDetailActivity extends BaseAppCompatActivity {

    private ViewGroup showingroup;
    private View showin;

    private ListView listView;
    private List<SBKeyValue> results = new ArrayList<>();
    private ListAdapterCenterProg adapter;

    private TextView text_name;
    private TextView text_ip;
    private TextView text_todaytotaltime;
    private TextView text_todayonlinerange;
    private TextView text_weektotaltime;
    private View card_centerprog;

    private String hostkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicedetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("hostkey")) {
            hostkey = getIntent().getStringExtra("hostkey");
        }
        //hostkey = "BFEBFBFF000206A7_D43D7E258717";
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        listView = (ListView) findViewById(R.id.list_centerprog);
        text_name = (TextView) findViewById(R.id.text_devicedetail_name);
        text_ip = (TextView) findViewById(R.id.text_devicedetail_ip);
        text_todaytotaltime = (TextView) findViewById(R.id.text_devicedetail_todaytotaltime);
        text_todayonlinerange = (TextView) findViewById(R.id.text_devicedetail_todayonlinerange);
        text_weektotaltime = (TextView) findViewById(R.id.text_devicedetail_weektotaltime);
        card_centerprog = findViewById(R.id.card_centerprog);
    }

    private void initData() {

        String myurl = UrlUtils.geturl(null, AppData.Url.deviceDetail + "/" + hostkey);
        final RequestParams params = new RequestParams(myurl);
        SobeyNet.sampleget(params, SBDeviceDetailPojo.class, new SobeyNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    SBDeviceDetailPojo detailPojo = (SBDeviceDetailPojo) pojo;
                    SBDevice device = detailPojo.getHostInfo();
                    if (device.getCoreProcess() != null && device.getCoreProcess().size()!=0) {
                        results.addAll(device.getCoreProcess());
                        freshCtrl(device);
                    }else {
                        card_centerprog.setVisibility(View.GONE);
                    }
                    LoadingViewUtil.showout(showingroup, showin);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(DeviceDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }

            @Override
            public void netStart(int code) {
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
            }
        });
    }

    private void initCtrl() {
        adapter = new ListAdapterCenterProg(this, R.layout.item_list_centerprog, results);
        listView.setAdapter(adapter);
    }

    private void freshCtrl(SBDevice device) {
        adapter.notifyDataSetChanged();
        setData(device);
    }

    private void setData(SBDevice device) {
        text_name.setText(device.getHostName());
        text_ip.setText(device.getInterIPAddress());
        List<SBKeyValue> attributes = device.getAttributes();
        String todayTotalTime = findSBValueByName(attributes, "TodayTotalTime");
        String thisWeekTotalTime = findSBValueByName(attributes, "ThisWeekTotalTime");
        text_todaytotaltime.setText(!StrUtils.isEmpty(todayTotalTime) ? todayTotalTime + "小时" : "");
        text_weektotaltime.setText(!StrUtils.isEmpty(thisWeekTotalTime) ? thisWeekTotalTime + "小时" : "");
        text_todayonlinerange.setText(findSBValueByName(attributes, "TodayOnlineRange"));
    }

    private String findSBValueByName(List<SBKeyValue> list, String name) {
        for (SBKeyValue keyValue : list) {
            if (name.equals(keyValue.getName())) {
                return keyValue.getValue();
            }
        }
        return "";
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
}
