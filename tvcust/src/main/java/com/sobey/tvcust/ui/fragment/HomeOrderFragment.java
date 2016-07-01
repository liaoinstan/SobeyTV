package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.CommonPojo;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderPojo;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.OrderProgActivity;
import com.sobey.tvcust.ui.activity.ReqfixActicity;
import com.sobey.tvcust.ui.adapter.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeOrderFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private TabLayout tab;
    private SpringView springView;
    private RecyclerView recyclerView;
    private RecycleAdapterOrder adapter;
    private View btn_go_reqfix;

    private List<Order> results = new ArrayList<>();
    private int page;
    private final int PAGE_COUNT = 5;

    public static Fragment newInstance(int position) {
        HomeOrderFragment f = new HomeOrderFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(Integer flag) {
        if (flag == AppConstant.EVENT_UPDATE_ORDERLIST){
            initData(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("订单");
        initBase();
        initView();
        initData(true);
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        tab = (TabLayout) getView().findViewById(R.id.tab_order);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_order);
        springView = (SpringView) getView().findViewById(R.id.spring);
        btn_go_reqfix = getView().findViewById(R.id.btn_go_reqfix);
    }

    private void initCtrl() {
        btn_go_reqfix.setOnClickListener(this);
        tab.addTab(tab.newTab().setText("全部订单"));
        tab.addTab(tab.newTab().setText("进行中"));
        tab.addTab(tab.newTab().setText("已完成"));
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                initData(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter = new RecycleAdapterOrder(getActivity(),R.layout.item_recycle_order,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        springView.setHeader(new AliHeader(getActivity(),false));
        springView.setFooter(new AliFooter(getActivity(),false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData(false);
            }

            @Override
            public void onLoadmore() {
                loadMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        results.add(new Order());
                        results.add(new Order());
                        freshCtrl();
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
        });
    }

    private void freshCtrl(){
        adapter.notifyDataSetChanged();
    }

    private void initData(final boolean isFirst) {
        final RequestParams params = new RequestParams(AppData.Url.orderlist);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("page", 1+"");
        params.addBodyParameter("pagecount", PAGE_COUNT+"");
        CommonNet.samplepost(params,CommonPojo.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null) netSetError(code,text);
                else{
                    OrderPojo orderPojo = (OrderPojo) pojo;
                    List<Order> orders = orderPojo.getDataList();
                    List<Order> results = adapter.getResults();
                    results.clear();
                    results.addAll(orders);
                    freshCtrl();
                    page = 1;
                    if (isFirst) {
                        LoadingViewUtil.showout(showingroup,showin);
                    }else {
                        springView.onFinishFreshAndLoad();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                if (isFirst) {
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData(false);
                        }
                    });
                }else {
                    springView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void netStart(int code) {
                if (isFirst) {
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading);
                }
            }
        });
    }

    private void loadMoreData() {
        final RequestParams params = new RequestParams(AppData.Url.orderlist);
        params.addBodyParameter("page", page+1+"");
        params.addBodyParameter("pagecount", PAGE_COUNT+"");
        CommonNet.samplepost(params,CommonPojo.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null) netSetError(code,text);
                else{
                    OrderPojo orderPojo = (OrderPojo) pojo;
                    List<Order> orders = orderPojo.getDataList();
                    List<Order> results = adapter.getResults();
//                    results.clear();
                    results.addAll(orders);
                    freshCtrl();
                    page++;
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netEnd(int code) {
                springView.onFinishFreshAndLoad();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go_reqfix:
                intent.setClass(getActivity(), ReqfixActicity.class);
                startActivity(intent);
                break;
        }
    }
}
