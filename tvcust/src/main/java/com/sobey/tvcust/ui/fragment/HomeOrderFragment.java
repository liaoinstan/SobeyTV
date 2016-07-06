package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderPojo;
import com.sobey.tvcust.ui.activity.ReqfixActicity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrder;
import com.sobey.tvcust.ui.dialog.DialogSure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeOrderFragment extends BaseFragment implements View.OnClickListener {

    private int position;
    private int cancleposition;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private TabLayout tab;
    private SpringView springView;
    private RecyclerView recyclerView;
    private RecycleAdapterOrder adapter;
    private View btn_go_reqfix;

    private DialogSure dialogSure;

    private List<Order> results = new ArrayList<>();
    private int page;
    private final int PAGE_COUNT = 5;

    private enum Select {ALL, PROG, FINISH}

    ;
    private Select selectflag = Select.ALL;

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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(Integer flag) {
        if (flag == AppConstant.EVENT_UPDATE_ORDERLIST) {
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
    public void onDestroyView() {
        super.onDestroyView();
        if (dialogSure!=null) dialogSure.dismiss();
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
        dialogSure = new DialogSure(getActivity());
        dialogSure.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netCancleOrder(cancleposition);
                dialogSure.hide();
            }
        });
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
                switch (tab.getPosition()) {
                    case 0:
                        selectflag = Select.ALL;
                        break;
                    case 1:
                        selectflag = Select.PROG;
                        break;
                    case 2:
                        selectflag = Select.FINISH;
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

        adapter = new RecycleAdapterOrder(getActivity(), R.layout.item_recycle_order, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOkListener(new RecycleAdapterOrder.OnOkListener() {
            @Override
            public void onOkClick(RecycleAdapterOrder.Holder holder) {
                dialogSure.show();
                cancleposition = holder.getLayoutPosition();
            }
        });

        springView.setHeader(new AliHeader(getActivity(), false));
        springView.setFooter(new AliFooter(getActivity(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData(false);
            }

            @Override
            public void onLoadmore() {
                loadMoreData();
            }
        });
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
    }

    private void initData(final boolean isFirst) {
        final RequestParams params = new RequestParams(AppData.Url.orderlist);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        switch (selectflag) {
            case PROG:
                params.addBodyParameter("status", 2006 + "");
                break;
            case FINISH:
                params.addBodyParameter("status", 2005 + "");
                break;
        }
        CommonNet.samplepost(params, OrderPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    OrderPojo orderPojo = (OrderPojo) pojo;
                    List<Order> orders = orderPojo.getDataList();
                    //有数据才添加，否则显示lack信息
                    if (orders != null && orders.size() != 0) {
                        List<Order> results = adapter.getResults();
                        results.clear();
                        results.addAll(orders);
                        freshCtrl();
                        page = 1;
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            springView.onFinishFreshAndLoad();
                        }
                    } else {
                        LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                if (isFirst) {
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData(false);
                        }
                    });
                } else {
                    springView.onFinishFreshAndLoad();
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

    private boolean isloadmore = false;

    private void loadMoreData() {
        if (isloadmore) return;
        final RequestParams params = new RequestParams(AppData.Url.orderlist);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", page + 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        switch (selectflag) {
            case PROG:
                params.addBodyParameter("status", 2006 + "");
                break;
            case FINISH:
                params.addBodyParameter("status", 2005 + "");
                break;
        }
        CommonNet.samplepost(params, OrderPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    OrderPojo orderPojo = (OrderPojo) pojo;
                    List<Order> orders = orderPojo.getDataList();
                    if (orders.size() != 0) {
                        List<Order> results = adapter.getResults();
                        results.addAll(orders);
                        freshCtrl();
                        page++;
                    } else {
                        Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(),"没有更多的数据了",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_go_reqfix:
                intent.setClass(getActivity(), ReqfixActicity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
        }
    }

    private void netCancleOrder(final int position){
        RequestParams params = new RequestParams(AppData.Url.cancleOrder);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", adapter.getResults().get(position).getId()+"");
        params.addBodyParameter("userId", adapter.getResults().get(position).getUserId()+"");
        CommonNet.samplepost(params,CommonEntity.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
                adapter.getResults().remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
