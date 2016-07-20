package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.OrderAllocatePojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrderAllocate;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class OrderAllocateOnlyActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe;
    private List<User> results1 = new ArrayList<>();
    private List<User> results2 = new ArrayList<>();
    private RecycleAdapterOrderAllocate adapter;

    private CircularProgressButton btn_go;


    private ViewGroup showingroup;
    private View showin;

    private int orderId;
    private int userId;
    private int categoryId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderallocateonly);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData(true);
        initCtrl();
    }

    private void initBase() {
        user = AppData.App.getUser();
        Intent intent = getIntent();
        if (intent.hasExtra("orderId")) {
            orderId = intent.getIntExtra("orderId", 0);
        }
        if (intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", 0);
        }
        if (intent.hasExtra("categoryId")) {
            categoryId = intent.getIntExtra("categoryId", 0);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_orderallocate);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);

    }

    private void initData(final boolean isFirst) {

        final RequestParams params = new RequestParams(AppData.Url.getTSCOnly);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("userId", userId + "");
        params.addBodyParameter("categoryId", categoryId + "");
        CommonNet.samplepost(params, OrderAllocatePojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    OrderAllocatePojo allocatePojo = (OrderAllocatePojo) pojo;
                    List<User> feibian = allocatePojo.getFeibian();
                    List<User> tsc = allocatePojo.getTsc();
                    //有数据才添加，否则显示lack信息
                    if (!StrUtils.isEmpty(feibian) || !StrUtils.isEmpty(tsc)) {
                        List<User> results_fb = adapter.getResults_fb();
                        List<User> results_tsc = adapter.getResults_tcs();
                        results_fb.clear();
                        results_tsc.clear();
                        if (!StrUtils.isEmpty(feibian)) {
                            results_fb.addAll(feibian);
                        }
                        if (!StrUtils.isEmpty(tsc)) {
                            results_tsc.addAll(tsc);
                        }
                        freshCtrl();
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            swipe.setRefreshing(false);
                        }
                    } else {
                        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initData(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                if (isFirst) {
                    Toast.makeText(OrderAllocateOnlyActivity.this, text, Toast.LENGTH_SHORT).show();
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData(true);
                        }
                    });
                } else {
                    swipe.setRefreshing(false);
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

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);

        adapter = new RecycleAdapterOrderAllocate(this, results1, results2);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 1000);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                User allocater = adapter.getSelectUser();

                String msg = AppVali.allocate_commit(allocater);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    returnValue(allocater);
                }
                break;
        }
    }

    private void returnValue(final User allocater) {
        btn_go.setProgress(50);
        String msg = AppVali.allocate_commit(allocater);
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
                    Intent intent = new Intent();
                    intent.putExtra("id", allocater.getId());
                    intent.putExtra("name", allocater.getRealName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }, 1000);
        }
    }
}
