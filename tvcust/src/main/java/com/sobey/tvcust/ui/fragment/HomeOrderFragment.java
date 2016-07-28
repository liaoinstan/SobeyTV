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
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.CountTab;
import com.sobey.tvcust.entity.CountTabPojo;
import com.sobey.tvcust.entity.Order;
import com.sobey.tvcust.entity.OrderPojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.activity.ReqDescribeOnlyActicity;
import com.sobey.tvcust.ui.activity.ReqfixActicity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrder;
import com.sobey.tvcust.ui.dialog.DialogSure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeOrderFragment extends BaseFragment implements View.OnClickListener {

    private int position;
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

    private User user;
    private Integer status;
    private Integer isCheck;
    private Integer isComment;
    private Integer isAccept;

    private Callback.Cancelable cancelable;
    private Callback.Cancelable cancelablemore;

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
            netGetCount();
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
        if (dialogSure != null) dialogSure.dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("订单");
        initBase();
        initView();
        netGetCount();
        initData(true);
        initCtrl();
    }

    private void initBase() {
        user = AppData.App.getUser();
        dialogSure = new DialogSure(getActivity());
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        tab = (TabLayout) getView().findViewById(R.id.tab_order);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_order);
        springView = (SpringView) getView().findViewById(R.id.spring);
        btn_go_reqfix = getView().findViewById(R.id.btn_go_reqfix);

        //只有用户、客服才能申报维修
        if (user.getRoleType() == User.ROLE_COMMOM || user.getRoleType() == User.ROLE_CUSTOMER) {
            btn_go_reqfix.setVisibility(View.VISIBLE);
        } else {
            btn_go_reqfix.setVisibility(View.GONE);
        }

        initViewTab();
    }

    private void initViewTab() {

        switch (user.getRoleType()) {
            //用户
            case User.ROLE_COMMOM:
                tab.addTab(tab.newTab().setText("全部"));
                tab.addTab(tab.newTab().setText("待处理"));
                tab.addTab(tab.newTab().setText("处理中"));
                tab.addTab(tab.newTab().setText("待验收"));
                tab.addTab(tab.newTab().setText("待评价"));
                break;
            //技术
            case User.ROLE_FILIALETECH:
                tab.addTab(tab.newTab().setText("全部"));
                tab.addTab(tab.newTab().setText("待办任务"));
                tab.addTab(tab.newTab().setText("待处理"));
                tab.addTab(tab.newTab().setText("处理中"));
                tab.addTab(tab.newTab().setText("待验收"));
                tab.addTab(tab.newTab().setText("待评价"));
                break;
            //客服
            case User.ROLE_CUSTOMER:
                tab.addTab(tab.newTab().setText("全部"));
                tab.addTab(tab.newTab().setText("待办任务"));
                tab.addTab(tab.newTab().setText("待分配"));
                tab.addTab(tab.newTab().setText("待处理"));    //ORDER_UNDEAL
                tab.addTab(tab.newTab().setText("处理中"));    //ORDER_INDEAL
                tab.addTab(tab.newTab().setText("待验收"));    //ORDER_UNVALI
                tab.addTab(tab.newTab().setText("待评价"));    //ORDER_UNEVA
                break;
            //总部技术
            case User.ROLE_HEADCOMTECH:
                tab.addTab(tab.newTab().setText("全部"));
                tab.addTab(tab.newTab().setText("待办任务"));
                tab.addTab(tab.newTab().setText("待处理"));
                tab.addTab(tab.newTab().setText("处理中"));
                tab.addTab(tab.newTab().setText("待验收"));
                tab.addTab(tab.newTab().setText("待评价"));
                break;
            //总部研发
            case User.ROLE_INVENT:
                tab.addTab(tab.newTab().setText("全部"));
                tab.addTab(tab.newTab().setText("待办任务"));
                tab.addTab(tab.newTab().setText("待处理"));
                tab.addTab(tab.newTab().setText("处理中"));
                tab.addTab(tab.newTab().setText("待验收"));
                tab.addTab(tab.newTab().setText("待评价"));
                break;
            //其他被抄送人员
            default:
                tab.addTab(tab.newTab().setText("全部"));
                tab.addTab(tab.newTab().setText("进行中"));
                tab.addTab(tab.newTab().setText("待验收"));
                tab.addTab(tab.newTab().setText("已完成"));    //ORDER_FINSH
                break;
        }

        for (int i = 0; i < tab.getTabCount(); i++) {
            TabLayout.Tab t = tab.getTabAt(i);
            if (t.getCustomView() == null) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab, null);
                t.setCustomView(view);
            }
            ViewGroup content = (ViewGroup) t.getCustomView();
            TextView tabText = (TextView) content.getChildAt(0);
            TextView tabCount = (TextView) content.getChildAt(1);
            tabText.setText(t.getText());
            if (i==0){
                tabCount.setVisibility(View.GONE);
            }
        }

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String text = tab.getText().toString();
                if (text.startsWith("全部")) {
                    status = null;
                    isCheck = null;
                    isComment = null;
                    isAccept = null;
                } else if (text.startsWith("待处理")) {
                    if (user.getRoleType() == User.ROLE_CUSTOMER) {
                        isCheck = 3;
                        status = Order.ORDER_UNDEAL;
                        isAccept = null;
                    } else if (user.getRoleType() == User.ROLE_COMMOM) {
                        isCheck = null;
                        status = Order.ORDER_UNDEAL;
                        isAccept = null;
                    } else if (user.getRoleType() == User.ROLE_FILIALETECH || user.getRoleType() == User.ROLE_HEADCOMTECH || user.getRoleType() == User.ROLE_INVENT) {
                        isCheck = 1;
                        status = null;
                        isAccept = 0;
                    } else {
                        isCheck = null;
                        status = Order.ORDER_UNDEAL;
                        isAccept = null;
                    }
                    isComment = null;
                } else if (text.startsWith("处理中")) {
                    if (user.getRoleType() == User.ROLE_FILIALETECH || user.getRoleType() == User.ROLE_HEADCOMTECH || user.getRoleType() == User.ROLE_INVENT) {
                        isCheck = 1;
                        isAccept = 1;
                    } else {
                        isCheck = null;
                        isAccept = null;
                    }
                    status = Order.ORDER_INDEAL;
                    isComment = null;
                } else if (text.startsWith("待验收")) {
                    status = Order.ORDER_UNVALI;
                    isCheck = null;
                    isComment = null;
                    isAccept = null;
                } else if (text.startsWith("待评价")) {
                    if (user.getRoleType() == User.ROLE_COMMOM) {
                        status = Order.ORDER_UNEVA;
                    }else {
                        status = null;
                    }
                    isCheck = null;
                    isComment = 0;
                    isAccept = null;
                } else if (text.startsWith("已完成")) {
                    status = 2007;
                    isCheck = null;
                    isComment = 1;
                    isAccept = null;
                } else if (text.startsWith("待办任务")) {
                    //特殊情况
                    if (user.getRoleType() == User.ROLE_FILIALETECH || user.getRoleType() == User.ROLE_HEADCOMTECH) {
                        status = null;
                    } else if (user.getRoleType() == User.ROLE_INVENT) {
                        status = Order.ORDER_INDEAL;
                    } else {
                        status = Order.ORDER_UNDEAL;
                    }
                    isCheck = 0;
                    isComment = null;
                    isAccept = null;
                } else if (text.startsWith("待分配")) {
                    status = Order.ORDER_UNDEAL;
                    isCheck = 1;
                    isComment = null;
                    isAccept = null;
                } else if (text.startsWith("进行中")) {
                    status = 2006;
                    isCheck = null;
                    isComment = null;
                    isAccept = null;
                } else {
                    Toast.makeText(getActivity(), "没有该分类", Toast.LENGTH_SHORT).show();
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
    }

    private void initCtrl() {
        btn_go_reqfix.setOnClickListener(this);

        adapter = new RecycleAdapterOrder(getActivity(), R.layout.item_recycle_order, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOkListener(new RecycleAdapterOrder.OnOkListener() {
            @Override
            public void onCancleClick(final RecycleAdapterOrder.Holder holder) {
                dialogSure.setMsg("确定删除订单？");
                dialogSure.setOnOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        netCancleOrder(holder.getLayoutPosition());
                        dialogSure.hide();
                    }
                });
                dialogSure.show();
            }

            @Override
            public void onCuiClick(final RecycleAdapterOrder.Holder holder) {
                Snackbar.make(showingroup, "已经为您提醒工作人员，我们会尽快处理", Snackbar.LENGTH_LONG).show();
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
        if (cancelable != null) {
            cancelable.cancel();
        }
        if (cancelablemore != null) {
            cancelablemore.cancel();
        }
        final RequestParams params = new RequestParams(AppData.Url.orderlist);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        if (status != null) {
            params.addBodyParameter("status", status + "");
        }
        if (isCheck != null) {
            params.addBodyParameter("isCheck", isCheck + "");
        }
        if (isComment != null) {
            params.addBodyParameter("isComment", isComment + "");
        }
        if (isAccept != null) {
            params.addBodyParameter("isAccept", isAccept + "");
        }
        cancelable = CommonNet.samplepost(params, OrderPojo.class, new CommonNet.SampleNetHander() {
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
//                        LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin);
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
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData(true);
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
        final RequestParams params = new RequestParams(AppData.Url.orderlist);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", page + 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        if (status != null) {
            params.addBodyParameter("status", status + "");
        }
        if (isCheck != null) {
            params.addBodyParameter("isCheck", isCheck + "");
        }
        if (isComment != null) {
            params.addBodyParameter("isComment", isComment + "");
        }
        if (isAccept != null) {
            params.addBodyParameter("isAccept", isAccept + "");
        }
        cancelablemore = CommonNet.samplepost(params, OrderPojo.class, new CommonNet.SampleNetHander() {
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
                startActivity(intent);
                break;
        }
    }

    private void netCancleOrder(final int pos) {
        RequestParams params = new RequestParams(AppData.Url.cancleOrder);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", adapter.getResults().get(pos).getId() + "");
        params.addBodyParameter("userId", adapter.getResults().get(pos).getUserId() + "");
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                adapter.getResults().remove(pos);
                adapter.notifyItemRemoved(pos);
                EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void netGetCount(){
        RequestParams params = new RequestParams(AppData.Url.count);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params,CountTab.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo!=null){
                    CountTab countTab = (CountTab) pojo;
                    setTabCount(countTab);
                }
            }

            @Override
            public void netSetError(int code, String text) {
            }
        });
    }

    private void setTabCount(CountTab count){
        for (int i = 0; i < tab.getTabCount(); i++) {
            TabLayout.Tab t = tab.getTabAt(i);
            if (t.getCustomView() == null) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab, null);
                t.setCustomView(view);
            }
            ViewGroup content = (ViewGroup) t.getCustomView();
            String text = t.getText().toString();
            TextView tabCount = (TextView) content.getChildAt(1);

            if (text.startsWith("待处理")){
                tabCount.setText(count.getPengding()+"");
            }else if (text.startsWith("处理中")){
                tabCount.setText(count.getHandling()+"");
            }else if (text.startsWith("待验收")){
                tabCount.setText(count.getAccepting()+"");
            }else if (text.startsWith("待评价")){
                tabCount.setText(count.getEvaluating()+"");
            }else if (text.startsWith("已完成")){
                tabCount.setText(count.getSolved()+"");
            }else if (text.startsWith("待办任务")){
                tabCount.setText(count.getBacklog()+"");
            }else if (text.startsWith("待分配")){
                tabCount.setText(count.getAssigning()+"");
            }else if (text.startsWith("进行中")){
                tabCount.setText(count.getHappening()+"");
            }
        }
    }
}
