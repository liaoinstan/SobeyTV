package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.SobeyNet;
import com.sobey.tvcust.entity.SBCountDevice;
import com.sobey.tvcust.entity.TVStation;
import com.sobey.tvcust.entity.TVStationPojo;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.activity.DeviceDetailActivity;
import com.sobey.tvcust.ui.activity.DeviceListActivity;
import com.sobey.tvcust.ui.activity.MsgActivity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.SelectStationActivity;
import com.sobey.tvcust.ui.activity.WarningListActivity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterQW;
import com.sobey.tvcust.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeQwFragment extends BaseFragment implements View.OnClickListener, OnRecycleItemClickListener {

    private RecyclerView recyclerView;
    private RecycleAdapterQW adapter;
    private View showin;
    private ViewGroup showingroup;
    private View btn_go_msg;

    private int position;
    private View rootView;
    private List<TestEntity> results = new ArrayList<>();

    public static Fragment newInstance(int position) {
        HomeQwFragment f = new HomeQwFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_qw, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("全网");

        initBase();
        initView();
        initData();
        initCtrl();
    }

    @Override
    public void onDestroy() {
        Log.e("liao", "onDestroy");
        super.onDestroy();
    }

    private void initBase() {
    }

    private void initView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_home_qw);
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        btn_go_msg = getView().findViewById(R.id.btn_go_msg);
    }

    private void initData() {
        netGetStations();

//        loadingDialog.show();
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());

//                initCtrl();
                freshCtrl();
                LoadingViewUtil.showout(showingroup, showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });

//                loadingDialog.hide();
            }
        }, 1000);
    }

    private void initCtrl() {
        adapter = new RecycleAdapterQW(getActivity(), results, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.setOnItemClickListener(this);
        btn_go_msg.setOnClickListener(this);
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_go_msg:
                intent.setClass(getActivity(), MsgActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getLayoutPosition();
        if (position == 0) {

        } else if (position > 0 && position < adapter.getResults().size() - 1) {
            Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            if (AppData.App.getUser().getRoleType()== User.ROLE_COMMOM){
                //用户直接进入告警列表
                intent.setClass(getActivity(), WarningListActivity.class);
                startActivity(intent);
            }else {
                //其他人需要先筛选电视台
                intent.setClass(getActivity(), SelectStationActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        }
    }

    private String getStationStr(List<TVStation> stations) {
        if (stations == null || stations.size() == 0) {
            return null;
        }
        String ret = "";
        for (TVStation station : stations) {
            ret += station.getStationCode() + "|";
        }
        return ret.substring(0, ret.length() - 1).replaceAll("\\|","%7C");
    }

    private void netGetStations() {
        RequestParams params = new RequestParams(AppData.Url.getTVs);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, TVStationPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    TVStationPojo tvStationPojo = (TVStationPojo) pojo;
                    List<TVStation> tvStations = tvStationPojo.getDataList();
                    String stationStr = getStationStr(tvStations);
                    if (!StrUtils.isEmpty(stationStr)) {
                        netCountDevice(stationStr);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void netCountDevice(String stationCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("station", stationCode);
        String myurl = UrlUtils.geturl(map, AppData.Url.conuntDevice);

        RequestParams params = new RequestParams(myurl);
        SobeyNet.sampleget(params, SBCountDevice.class, new SobeyNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                SBCountDevice countDevice = (SBCountDevice) pojo;
                int days = TimeUtil.subDate(new Date(AppData.App.getUser().getCreateDate()), new Date());
                countDevice.setDays(days);
                adapter.setInfo(countDevice);
                adapter.notifyItemChanged(0);
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
