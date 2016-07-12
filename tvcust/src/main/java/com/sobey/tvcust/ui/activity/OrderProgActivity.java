package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.common.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.OrderStatusHelper;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderCategory;
import com.sobey.tvcust.entity.OrderTrack;
import com.sobey.tvcust.entity.OrderTrackPojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.ListAdapterOrderTrack;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class OrderProgActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private ListView listView_full;
    private List<OrderTrack> results = new ArrayList<>();
    private ListAdapterOrderTrack adapter;

    private ViewGroup showingroup;
    private View showin;

    private TextView text_orderprog_question;
    private TextView text_orderprog_num;
    private TextView text_orderprog_status;
    private TextView btn_go_evadetail;
    private TextView btn_go_eva;

    private int orderId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderprog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();

        toolbar.setFocusable(true);
        toolbar.setFocusableInTouchMode(true);
        toolbar.requestFocus();
    }

    private void initBase() {
        user = AppData.App.getUser();
        Intent intent = getIntent();
        if (intent.hasExtra("orderId")) {
            orderId = intent.getIntExtra("orderId", 0);
        }
    }

    private void initData() {

        RequestParams params = new RequestParams(AppData.Url.getOrderTrack);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        CommonNet.samplepost(params,OrderTrackPojo.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    OrderTrackPojo trackPojo = (OrderTrackPojo) pojo;
                    List<OrderTrack> tracks = trackPojo.getDataList();
                    if (tracks != null && tracks.size() != 0) {
                        List<OrderTrack> results = adapter.getResults();
                        results.clear();
                        results.addAll(tracks);
                        freshCtrl();
                        setData(trackPojo.getOrder());
                        LoadingViewUtil.showout(showingroup, showin);
                    } else {
                        LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(OrderProgActivity.this, text, Toast.LENGTH_SHORT).show();
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

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        listView_full = (ListView) findViewById(R.id.listfull_ordertrack);
        text_orderprog_question = (TextView) findViewById(R.id.text_orderprog_question);
        text_orderprog_num = (TextView) findViewById(R.id.text_orderprog_num);
        text_orderprog_status = (TextView) findViewById(R.id.text_orderprog_status);
        btn_go_evadetail = (TextView) findViewById(R.id.text_orderprog_go_evadetail);
        btn_go_eva = (TextView) findViewById(R.id.text_orderprog_go_eva);
    }

    private void initCtrl() {
        adapter = new ListAdapterOrderTrack(this,R.layout.item_list_track,results);
        listView_full.setAdapter(adapter);
        btn_go_evadetail.setOnClickListener(this);
        btn_go_eva.setOnClickListener(this);
    }

    private void freshCtrl(){
        adapter.notifyDataSetChanged();
    }

    private void setData(Order order){
        if (order!=null){
            OrderCategory category = order.getCategory();
            text_orderprog_question.setText((category.getType() == 0 ? "软件问题：" : "硬件问题：") + category.getCategoryName());
            text_orderprog_num.setText("订单编号：" + order.getOrderNumber());
            text_orderprog_status.setText(OrderStatusHelper.getStatusStr(user.getRoleType(), order));
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
        switch (v.getId()){
            case R.id.text_orderprog_go_evadetail:
                intent.setClass(this,EvaDetailActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
                break;
            case R.id.text_orderprog_go_eva:
                intent.setClass(this,EvaActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
                break;
        }
    }
}
