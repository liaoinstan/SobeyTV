package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.gson.Gson;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.common.CommonNet;
import com.sobey.common.common.MyPlayer;
import com.sobey.common.utils.FileUtil;
import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.OrderStatusHelper;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderDescribe;
import com.sobey.tvcust.entity.OrderDescribePojo;
import com.sobey.tvcust.entity.OrderPojo;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrderDetail;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private MyPlayer player = new MyPlayer();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe;
    private List<OrderDescribe> results = new ArrayList<>();
    private RecycleAdapterOrderDetail adapter;

    private ImageView img_orderdetail_header;
    private TextView text_orderdetail_name;
    private TextView text_orderdetail_problem;
    private TextView text_orderdetail_num;
    private TextView text_orderdetail_tsc_name;
    private TextView text_orderdetail_tsc_phone;
    private TextView text_orderdetail_user_name;
    private TextView text_orderdetail_user_phone;
    private View lay_orderdetail_tsc;
    private CircularProgressButton btn_go;

    private ViewGroup showingroup;
    private View showin;

    private int id;
    private boolean check;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventBus.getDefault().register(this);

        initBase();
        initView();
        initData(true);
        initCtrl();
    }

    @Subscribe
    public void onEventMainThread(Integer flag) {
        if (flag == AppConstant.EVENT_UPDATE_ORDERDESCRIBE) {
            initData(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (player != null) player.onDestory();
    }

    private void initBase() {
        user = AppData.App.getUser();
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 0);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_orderdetail);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        img_orderdetail_header = (ImageView) findViewById(R.id.img_orderdetail_header);
        text_orderdetail_name = (TextView) findViewById(R.id.text_orderdetail_name);
        text_orderdetail_problem = (TextView) findViewById(R.id.text_orderdetail_problem);
        text_orderdetail_num = (TextView) findViewById(R.id.text_orderdetail_num);
        text_orderdetail_tsc_name = (TextView) findViewById(R.id.text_orderdetail_tsc_name);
        text_orderdetail_tsc_phone = (TextView) findViewById(R.id.text_orderdetail_tsc_phone);
        text_orderdetail_user_name = (TextView) findViewById(R.id.text_orderdetail_user_name);
        text_orderdetail_user_phone = (TextView) findViewById(R.id.text_orderdetail_user_phone);
        lay_orderdetail_tsc = findViewById(R.id.lay_orderdetail_tsc);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);

        findViewById(R.id.btn_go_orderprog).setOnClickListener(this);
    }

    private void initData(final boolean isFirst) {

        final RequestParams params = new RequestParams(AppData.Url.getOrderdecribe);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", id + "");
        CommonNet.samplepost(params, OrderDescribePojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    OrderDescribePojo orderDescribePojo = (OrderDescribePojo) pojo;
                    List<OrderDescribe> dataList = ((OrderDescribePojo) pojo).getDataList();
                    //有数据才添加，否则显示lack信息
                    if (dataList != null && dataList.size() != 0) {
                        List<OrderDescribe> results = adapter.getResults();
                        results.clear();
                        results.addAll(dataList);
                        freshCtrl(orderDescribePojo.getTsc(), orderDescribePojo.getUser(), orderDescribePojo.getOrder());
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            swipe.setRefreshing(false);
                        }
                    } else {
                        setData(orderDescribePojo.getTsc(), orderDescribePojo.getUser(), orderDescribePojo.getOrder());
                        LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                if (isFirst) {
                    Toast.makeText(OrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                    LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData(false);
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

//        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //加载成功
//                String pathphoto = null;
//                String pathvideo = null;
//                String pathvoice = null;
//
//                File dirphoto = new File(FileUtil.getPhotoFolder());
//                File[] photos = dirphoto.listFiles();
//                if (photos!=null && photos.length>0){
//                    pathphoto = photos[photos.length-1].getPath();
//                }
//
//                File dirvideo = new File(FileUtil.getVideoFolder());
//                File[] videos = dirvideo.listFiles();
//                if (videos!=null && videos.length>0){
//                    pathvideo = videos[videos.length-1].getPath();
//                }
//
//                File dirvoice = new File(FileUtil.getVoiceFolder());
//                File[] voices = dirvoice.listFiles();
//                if (voices!=null && voices.length>0){
//                    pathvoice = voices[voices.length-1].getPath();
//                }
//
//
//                for (int i = 0; i < 20; i++) {
//                    TestEntity entity = new TestEntity();
//                    if (i % 2 == 0)
//                        entity.setPathphotos(new String[]{pathphoto,pathphoto,pathphoto});
//                    if (i % 3 == 0)
//                        entity.setPathvideos(new String[]{pathvideo});
//                    if (i % 4 == 0)
//                        entity.setPathvoices(new String[]{pathvoice,pathvoice});
//                    results.add(entity);
//                }
//
//                freshCtrl();
//                LoadingViewUtil.showout(showingroup, showin);

        //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
//            }
//        }, 1000);
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);

        adapter = new RecycleAdapterOrderDetail(this, R.layout.item_recycle_orderdetail, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
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

        adapter.setVoiceListener(new RecycleAdapterOrderDetail.onVoiceListener() {
            @Override
            public void onPlay(String path) {
                player.setUrl(path);
                player.play();
            }
        });
    }

    private void freshCtrl(User tsc, User user, Order order) {
        setData(tsc, user, order);
        adapter.notifyDataSetChanged();
    }

    private void setData(User tsc, User mainperson, Order order) {
        if (order != null) {
            text_orderdetail_problem.setText((order.getCategory().getType() == 0 ? "软件问题：" : "硬件问题：") + order.getCategory().getCategoryName());
            text_orderdetail_num.setText("订单编号：" + order.getOrderNumber());
        }
        if (mainperson != null) {
            text_orderdetail_name.setText(mainperson.getTvName());
            text_orderdetail_user_name.setText("申请人：" + mainperson.getRealName());
            text_orderdetail_user_phone.setText("电话：" + mainperson.getMobile());
        }
        if (tsc != null) {
            lay_orderdetail_tsc.setVisibility(View.VISIBLE);
            text_orderdetail_tsc_name.setText("处理人：" + tsc.getRealName());
            text_orderdetail_tsc_phone.setText("电话：" + tsc.getMobile());
        } else {
            lay_orderdetail_tsc.setVisibility(View.GONE);
        }
        //普通客户只能看见进度标示图，其他角色可以看见台标
        if (user.getRoleType() == User.ROLE_COMMOM) {
            img_orderdetail_header.setImageResource(OrderStatusHelper.getStatusImgSrc(order));
        } else {
            Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.icon_order_fix).centerCrop().crossFade().into(img_orderdetail_header);
        }

        //根据角色类型设置提交按钮的状态和功能
        switch (user.getRoleType()) {
            case User.ROLE_FILIALETECH:
                btn_go.setVisibility(View.VISIBLE);
                if (order.getTechCheck() != null && order.getTechCheck() == 1) {
                    check = true;
                    btn_go.setText("用户反馈");
                    btn_go.setIdleText("用户反馈");
                } else {
                    check = false;
                    btn_go.setText("接受任务");
                    btn_go.setIdleText("接受任务");
                }
                break;
            case User.ROLE_COMMOM:
                btn_go.setVisibility(View.VISIBLE);
                btn_go.setText("追加描述");
                btn_go.setIdleText("追加描述");
                break;
            default:
                btn_go.setVisibility(View.GONE);
                break;
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
            case R.id.btn_go_orderprog:
                intent.setClass(this, OrderProgActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_go:
                btn_go();
                break;
        }
    }

    private void btn_go() {
        if (user.getRoleType() == User.ROLE_FILIALETECH && !check) {
            //接受任务
            btn_go.setProgress(50);
            RequestParams params = new RequestParams(AppData.Url.acceptOrder);
            params.addHeader("token", AppData.App.getToken());
            params.addBodyParameter("orderId", id+"");
            CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                @Override
                public void netGo(int code, Object pojo, String text, Object obj) {
                    Toast.makeText(OrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(0);
                            btn_go.setIdleText("用户反馈");
                            check = true;
                        }
                    }, 800);
                }
                @Override
                public void netSetError(int code, String text) {
                    Toast.makeText(OrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                    btn_go.setProgress(-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(0);
                        }
                    }, 800);
                }
            });
        } else {
            Intent intent = new Intent();
            intent.setClass(this, ReqfixActicity.class);
            intent.putExtra("type", 1);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}
