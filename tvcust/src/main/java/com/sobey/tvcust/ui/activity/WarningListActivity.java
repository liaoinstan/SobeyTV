package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.SobeyNet;
import com.sobey.tvcust.entity.SBDevice;
import com.sobey.tvcust.entity.SBGroup;
import com.sobey.tvcust.entity.SBGroupPojo;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterDevice;
import com.sobey.tvcust.utils.UrlUtils;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarningListActivity extends BaseAppCompatActivity implements OnRecycleItemClickListener {

    private RecyclerView recyclerView;
    private SpringView springView;
    private List<SBDevice> results = new ArrayList<>();
    private RecycleAdapterDevice adapter;
    private TabLayout tab;

    private ViewGroup showingroup;
    private View showin;

    private String stationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warninglist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("stationCode")) {
            stationCode = getIntent().getStringExtra("stationCode");
            Log.e("liao", stationCode);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_device);
        springView = (SpringView) findViewById(R.id.spring);
        tab = (TabLayout) findViewById(R.id.tab_devicelist);
    }

    private void initData() {
        netGroup();

        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                results.add(new SBDevice());
                results.add(new SBDevice());
                results.add(new SBDevice());
                results.add(new SBDevice());
                results.add(new SBDevice());
                results.add(new SBDevice());
                results.add(new SBDevice());
                freshCtrl();
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
//        tab.addTab(tab.newTab().setText("所有"));
//        tab.addTab(tab.newTab().setText("警告"));
//        tab.addTab(tab.newTab().setText("存储"));
//        tab.addTab(tab.newTab().setText("数据库"));
//        tab.addTab(tab.newTab().setText("服务器"));
//        tab.addTab(tab.newTab().setText("工作站"));

        adapter = new RecycleAdapterDevice(this, R.layout.item_recycle_home_qw, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        springView.setHeader(new AliHeader(this, false));
        springView.setFooter(new AliFooter(this, false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        results.add(new SBDevice());
                        results.add(new SBDevice());
                        freshCtrl();
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
        });
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
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
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        Intent intent = new Intent(this, DeviceDetailActivity.class);
        startActivity(intent);
    }

    private void netGroup() {
        HashMap<String, String> map = new HashMap<>();
        map.put("station", stationCode);
        String myurl = UrlUtils.geturl(map, AppData.Url.group);

        RequestParams params = new RequestParams(myurl);
        SobeyNet.sampleget(params, SBGroupPojo.class, new SobeyNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                SBGroupPojo groupPojo = (SBGroupPojo) pojo;
                List<SBGroup> groupList = groupPojo.getGroupList();
                if (groupList != null && groupList.size() != 0) {
                    tab.addTab(tab.newTab().setText("全部").setTag("0"));
                    for (SBGroup group:groupList){
                        tab.addTab(tab.newTab().setText(group.getName()).setTag(group.getCode()));
                    }
                } else {
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(WarningListActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
