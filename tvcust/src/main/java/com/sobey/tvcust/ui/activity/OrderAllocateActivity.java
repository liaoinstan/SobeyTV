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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.OrderStatusHelper;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderAllocatePojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrderAllocate;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class OrderAllocateActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe;
    private List<User> results1 = new ArrayList<>();
    private List<User> results2 = new ArrayList<>();
    private RecycleAdapterOrderAllocate adapter;

    private ImageView img_orderallocate_header;
    private TextView text_orderallocate_name;
    private TextView text_orderallocate_problem;
    private TextView text_orderallocate_num;
    private TextView text_orderallocate_user_name;
    private TextView text_orderallocate_user_phone;
    private CircularProgressButton btn_go;


    private ViewGroup showingroup;
    private View showin;

    private int orderId;
    private int userId;
    private int categoryId;
    private int type;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderallocate);
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
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 0);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_orderallocate);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        img_orderallocate_header = (ImageView) findViewById(R.id.img_orderallocate_header);
        text_orderallocate_name = (TextView) findViewById(R.id.text_orderallocate_name);
        text_orderallocate_problem = (TextView) findViewById(R.id.text_orderallocate_problem);
        text_orderallocate_num = (TextView) findViewById(R.id.text_orderallocate_num);
        text_orderallocate_user_name = (TextView) findViewById(R.id.text_orderallocate_user_name);
        text_orderallocate_user_phone = (TextView) findViewById(R.id.text_orderallocate_user_phone);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);

        if (type==0){
            getSupportActionBar().setTitle("订单分配");
            btn_go.setIdleText("分配任务");
            btn_go.setText("分配任务");
        }else if(type==1){
            getSupportActionBar().setTitle("分配TSC");
            btn_go.setIdleText("分配TSC");
            btn_go.setText("分配TSC");
        }
    }

    private void initData(final boolean isFirst) {

        final RequestParams params = new RequestParams(AppData.Url.getTSC);
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
                        freshCtrl(allocatePojo.getUser(), allocatePojo.getOrder());
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            swipe.setRefreshing(false);
                        }
                    } else {
                        setData(allocatePojo.getUser(), allocatePojo.getOrder());
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
                    Toast.makeText(OrderAllocateActivity.this, text, Toast.LENGTH_SHORT).show();
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
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading,showin);
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

    private void freshCtrl(User mainperson, Order order) {
        setData(mainperson, order);
        adapter.notifyDataSetChanged();
    }

    private void setData(User mainperson, Order order) {
        if (order != null) {
            text_orderallocate_problem.setText((order.getCategory().getType() == 0 ? "软件问题：" : "硬件问题：") + order.getCategory().getCategoryName());
            text_orderallocate_num.setText("订单编号：" + order.getOrderNumber());
        }
        if (mainperson != null) {
            text_orderallocate_name.setText(mainperson.getTvName());
            text_orderallocate_user_name.setText("申请人：" + mainperson.getRealName());
            text_orderallocate_user_phone.setText("电话：" + mainperson.getMobile());
        }
        //普通客户只能看见进度标示图，其他角色可以看见台标
        if (user.getRoleType() == User.ROLE_COMMOM) {
            img_orderallocate_header.setImageResource(OrderStatusHelper.getStatusImgSrc(order));
        } else {
            Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.icon_order_fix).centerCrop().crossFade().into(img_orderallocate_header);
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
        switch (v.getId()) {
            case R.id.btn_go:
                User allocater = adapter.getSelectUser();

                String msg = AppVali.allocate_commit(allocater);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    if (type==0) {
                        netCommit(allocater);
                    }else if(type==1){
                        returnValue(allocater);
                    }
                }
                break;
        }
    }

    private void returnValue(final User allocater){
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

    private void netCommit(User allocater){
        btn_go.setProgress(50);
        RequestParams params = new RequestParams(AppData.Url.allotorder);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("tscId", allocater.getId() + "");
        params.addBodyParameter("roleType", allocater.getRoleId() + "");
        CommonNet.samplepost(params, User.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(OrderAllocateActivity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                        finish();
                    }
                }, 800);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(OrderAllocateActivity.this, text, Toast.LENGTH_SHORT).show();
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
