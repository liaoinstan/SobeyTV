package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.common.MyPlayer;
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
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrderDetail;
import com.sobey.tvcust.ui.dialog.DialogPopupDescribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private MyPlayer player = new MyPlayer();

    private DialogPopupDescribe pop_describe;

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
    private User user;
    private Order order;

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
        if (pop_describe != null) pop_describe.dismiss();
    }

    private void initBase() {
        user = AppData.App.getUser();
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 0);
        }
        if (intent.hasExtra("order")) {
            order = (Order) intent.getSerializableExtra("order");
        }
        pop_describe = new DialogPopupDescribe(this);
        pop_describe.setOnFinishListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFinish();
                pop_describe.hide();
            }
        });
        pop_describe.setOnValiPassListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popValiPass();
                pop_describe.hide();
            }
        });
        pop_describe.setOnValiRefuseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popValiRefuse();
                pop_describe.hide();
            }
        });
        pop_describe.setOnNextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popNext();
                pop_describe.hide();
            }
        });
        pop_describe.setOnBankListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBank();
                pop_describe.hide();
            }
        });
        pop_describe.setOnUserListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUser();
                pop_describe.hide();
            }
        });
        pop_describe.setOnDescribeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDescribe();
                pop_describe.hide();
            }
        });
        pop_describe.setOnHeadTechListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popHeadTech();
                pop_describe.hide();
            }
        });
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

        //如果未查看就查看订单
        if (user.getRoleType()==User.ROLE_CUSTOMER && order.getServiceCheck()!=1) {
            netUpdateCheck();
        }else if (user.getRoleType()==User.ROLE_FILIALETECH && order.getTechCheck()!=1){
            netUpdateCheck();
        }else if (user.getRoleType()==User.ROLE_HEADCOMTECH && order.getHeadTechCheck()!=1){
            netUpdateCheck();
        }else if (user.getRoleType()==User.ROLE_INVENT && order.getDevelopCheck()!=1){
            netUpdateCheck();
        }

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
                    Toast.makeText(OrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
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
        this.order = order;
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
        pop_describe.setType(user.getRoleType(), order);
        switch (user.getRoleType()) {
            //技术人员
            case User.ROLE_FILIALETECH:
                if (order.getTscIsAccept() != 1) {
                    //如果是技术人员，且未接受，则显示接受按钮
                    btn_go.setText("接受任务");
                    btn_go.setIdleText("接受任务");
                } else {
                    //如果已经接受，则显示接受按钮
                    btn_go.setText("操作");
                    btn_go.setIdleText("操作");
                }
                break;
            //总部技术
            case User.ROLE_HEADCOMTECH:
                //新需求变更：如果分公司技术不存在则选择一个到现场查看
                if (order.getHeadTechIsAccept() != 1) {
                    btn_go.setText("接受任务");
                    btn_go.setIdleText("接受任务");
                } else {
                    btn_go.setText("操作");
                    btn_go.setIdleText("操作");
                }
                break;
            //总部研发
            case User.ROLE_INVENT:
                if (order.getDevelopIsAccept() != 1) {
                    btn_go.setText("接受任务");
                    btn_go.setIdleText("接受任务");
                } else {
                    btn_go.setText("操作");
                    btn_go.setIdleText("操作");
                }
                break;
            //客服
            case User.ROLE_CUSTOMER:
                btn_go.setVisibility(View.GONE);
                break;
            //用户
            case User.ROLE_COMMOM:
                btn_go.setText("操作");
                btn_go.setIdleText("操作");
                break;
            default:
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
                intent.putExtra("orderId", id);
                startActivity(intent);
                break;
            case R.id.btn_go:
                if ("操作".equals(btn_go.getText().toString())) {
                    pop_describe.show();
                } else {
                    netAcceptOrder();
                }
                break;
//            case R.id.btn_last:
//                goAddDescribe(false);
//                break;
//            case R.id.btn_next:
//                goAddDescribe(true);
//                break;
        }
    }

    //接受订单
    private void netAcceptOrder() {
        //接受任务
        btn_go.setProgress(50);
        RequestParams params = new RequestParams(AppData.Url.acceptOrder);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", id + "");
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(OrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btn_go.getVisibility() == View.VISIBLE) {
                            btn_go.setProgress(0);
                            btn_go.setIdleText("操作");

                            order.setStatus(Order.ORDER_INDEAL);
                            pop_describe.setType(user.getRoleType(),order);
                        }
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
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
    }

    //去添加订单描述（上级或下级）
    private void goAddDescribe(int type) {
        Intent intent = new Intent();
        intent.setClass(this, ReqDescribeActicity.class);
        intent.putExtra("orderId", order.getId());
        intent.putExtra("categoryId", order.getCategory().getId());
        intent.putExtra("flag", type);
        intent.putExtra("userId", order.getUserId());
        intent.putExtra("title", type == 0 ? "申请协助" : "反馈");
        if (order != null) {
            //////////////////
            ///设置是否援助
            //////////////////
            boolean isAccept = false;
            if (type == 0) {
                //技术
                if (user.getRoleType() == User.ROLE_FILIALETECH) {
                    if (order.getHeadTechId() == null || order.getHeadTechId() == 0) {
                        isAccept = true;
                    } else {
                        isAccept = false;
                    }
                }
                //总技术
                else if (user.getRoleType() == User.ROLE_HEADCOMTECH) {
                    if (order.getDecelopId() == null || order.getDecelopId() == 0) {
                        isAccept = true;
                    } else {
                        isAccept = false;
                    }
                }
            } else {
                if (order.getTscId() == null || order.getTscId() == 0) {
                    isAccept = true;
                } else {
                    isAccept = false;
                }
            }
            intent.putExtra("isAccept", isAccept);

            //////////////////
            ///设置是否抄送
            //////////////////
            boolean isCopy = false;
            if (User.ROLE_HEADCOMTECH == user.getRoleType() && type == 0) {
                isCopy = false;
            } else {
                //技术可以抄送
                if (isAccept) {
                    isCopy = true;
                } else {
                    isCopy = false;
                }
            }
            intent.putExtra("isCopy", isCopy);

            /////////////////
            ////设置新需求标志
            /////////////////
            boolean newflag = false;
            if (type == 1 && (order.getTscId() == null || order.getTscId() == 0)) {
                newflag = true;
            } else {
                newflag = false;
            }
            intent.putExtra("newflag", newflag);
        }
        startActivity(intent);
    }

    private void popFinish() {
        Intent intent = new Intent(this, ReqDescribeOnlyActicity.class);
        intent.putExtra("orderId", id);
        intent.putExtra("type", 0);
        intent.putExtra("title", "完成任务");
        startActivity(intent);
    }

    private void popValiPass() {
        Intent intent = new Intent(this, ReqDescribeOnlyActicity.class);
        intent.putExtra("orderId", id);
        intent.putExtra("type", 1);
        intent.putExtra("title", "验收通过");
        startActivity(intent);
    }

    private void popValiRefuse() {
        Intent intent = new Intent(this, ReqDescribeOnlyActicity.class);
        intent.putExtra("orderId", id);
        intent.putExtra("type", 2);
        intent.putExtra("title", "验收拒绝");
        startActivity(intent);
    }

    private void popNext() {
        goAddDescribe(0);
    }

    private void popBank() {
        goAddDescribe(1);
    }

    private void popUser() {
        Intent intent = new Intent(this, ReqDescribeOnlyActicity.class);
        intent.putExtra("orderId", id);
        intent.putExtra("type", 3);
        intent.putExtra("title", "反馈用户");
        startActivity(intent);
    }

    private void popDescribe() {
        goAddDescribe(0);
    }

    private void popHeadTech() {
        Intent intent = new Intent(this, ReqDescribeOnlyActicity.class);
        intent.putExtra("orderId", id);
        intent.putExtra("type", 3);
        intent.putExtra("title", "向总技术反馈");
        startActivity(intent);
    }

    private void netUpdateCheck() {
        //进入页面之后立即查看操作
        RequestParams params = new RequestParams(AppData.Url.updateCheck);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", id + "");
        CommonNet.samplepost(params, User.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(OrderDetailActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
