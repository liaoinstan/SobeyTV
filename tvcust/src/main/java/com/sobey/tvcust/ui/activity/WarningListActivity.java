package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.SobeyNet;
import com.sobey.tvcust.entity.Warning;
import com.sobey.tvcust.entity.WarningPojo;
import com.sobey.tvcust.entity.SBGroup;
import com.sobey.tvcust.entity.SBGroupPojo;
import com.sobey.tvcust.entity.TVStation;
import com.sobey.tvcust.entity.TVStationPojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterWarning;
import com.sobey.tvcust.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarningListActivity extends BaseAppCompatActivity implements OnRecycleItemClickListener {

    private RecyclerView recyclerView;
    private SpringView springView;
    private List<Warning> results = new ArrayList<>();
    private RecycleAdapterWarning adapter;
    private TabLayout tab;

    private ViewGroup showingroup;
    private View showin;

    private Callback.Cancelable cancelable;
    private Callback.Cancelable cancelablemore;

    private String stationCode;
    private String groupCode = "";

    private int page;
    private final int PAGE_COUNT = 10;

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
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_device);
        springView = (SpringView) findViewById(R.id.spring);
        tab = (TabLayout) findViewById(R.id.tab_devicelist);
    }

    private void initData() {
        if (AppData.App.getUser().getRoleType() == User.ROLE_COMMOM){
            netGetStation_group();
        }else {
            netGroup();
        }
//        netlist();
    }

    private void initCtrl() {

        adapter = new RecycleAdapterWarning(this, R.layout.item_recycle_home_qw, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        springView.setHeader(new AliHeader(this, false));
        springView.setFooter(new AliFooter(this, false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netlist(false);
            }

            @Override
            public void onLoadmore() {
                loadMoreData();
            }
        });
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                groupCode = (String) tab.getTag();
                netlist(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
        Warning warning = adapter.getResults().get(viewHolder.getLayoutPosition());
        Intent intent = new Intent(this, DeviceDetailActivity.class);
        intent.putExtra("hostkey",warning.getHostKey());
        startActivity(intent);
    }

    private void netlist(final boolean isFirst) {
        if (cancelable != null) {
            cancelable.cancel();
        }
        if (cancelablemore != null) {
            cancelablemore.cancel();
        }
        final RequestParams params = new RequestParams(AppData.Url.warningList);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        if (AppData.App.getUser().getRoleType()!=User.ROLE_COMMOM) {
            params.addBodyParameter("stationCode", stationCode);
        }
        if (!StrUtils.isEmpty(groupCode)) {
            params.addBodyParameter("groupCode", groupCode);
        }
        cancelable = CommonNet.samplepost(params, WarningPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    WarningPojo devicePojo = (WarningPojo) pojo;
                    List<Warning> devices = devicePojo.getLists();
                    //有数据才添加，否则显示lack信息
                    if (devices != null && devices.size() != 0) {
                        List<Warning> results = adapter.getResults();
                        results.clear();
                        results.addAll(devices);
                        freshCtrl();
                        page = 1;
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            springView.onFinishFreshAndLoad();
                        }
                    } else {
                        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                netlist(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(WarningListActivity.this, text, Toast.LENGTH_SHORT).show();
                if (isFirst) {
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            netlist(true);
                        }
                    });
                } else {
                    springView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void netStart(int code) {
                if (isFirst) {
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
                }
            }
        });
    }

    private boolean isloadmore = false;

    private void loadMoreData() {
        if (isloadmore) return;

        final RequestParams params = new RequestParams(AppData.Url.warningList);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", page + 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        params.addBodyParameter("stationCode", stationCode);
        if (!StrUtils.isEmpty(groupCode)) {
            params.addBodyParameter("groupCode", groupCode);
        }
        cancelablemore = CommonNet.samplepost(params, WarningPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    WarningPojo devicePojo = (WarningPojo) pojo;
                    List<Warning> devices = devicePojo.getLists();
                    //有数据才添加，否则显示lack信息
                    if (devices != null && devices.size() != 0) {
                        List<Warning> results = adapter.getResults();
                        results.addAll(devices);
                        freshCtrl();
                        page++;
                        springView.onFinishFreshAndLoad();
                    } else {
                        Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(WarningListActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int code) {
                isloadmore = true;
            }

            @Override
            public void netEnd(int code) {
                isloadmore = false;
                springView.onFinishFreshAndLoad();
            }
        });
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
                    tab.addTab(tab.newTab().setText("全部").setTag(""));
                    for (SBGroup group : groupList) {
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

    private void netGetStation_group() {
        RequestParams params = new RequestParams(AppData.Url.getTVs);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, TVStationPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    TVStationPojo tvStationPojo = (TVStationPojo) pojo;
                    List<TVStation> tvStations = tvStationPojo.getDataList();
                    if (tvStations != null && tvStations.size() != 0) {
                        stationCode = tvStations.get(0).getStationCode();
                        netGroup();
                    } else {
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(WarningListActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
