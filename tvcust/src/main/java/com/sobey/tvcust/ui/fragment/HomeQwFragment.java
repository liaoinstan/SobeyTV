package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
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
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.entity.Warning;
import com.sobey.tvcust.entity.WarningPojo;
import com.sobey.tvcust.ui.activity.DeviceDetailActivity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.SelectStationActivity;
import com.sobey.tvcust.ui.activity.WarningListActivity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterQW;
import com.sobey.tvcust.utils.AppHelper;
import com.sobey.tvcust.utils.UrlUtils;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeQwFragment extends BaseFragment implements OnRecycleItemClickListener {

    private RecyclerView recyclerView;
    private RecycleAdapterQW adapter;
    private SpringView springView;
    private View showin;
    private ViewGroup showingroup;


    private int position;
    private View rootView;
    private List<Warning> results = new ArrayList<>();

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
        initData(true);
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
        springView = (SpringView) getView().findViewById(R.id.spring);
    }

    private void initData(final boolean isFirst) {
        netGetStations_countDevice();

        final RequestParams params = new RequestParams(AppData.Url.warningList);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", 1 + "");
        params.addBodyParameter("pageSize", 3 + "");
        CommonNet.samplepost(params, WarningPojo.class, new CommonNet.SampleNetHander() {
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
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        }else {
                            springView.onFinishFreshAndLoad();
                        }
                    } else {
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        }else {
                            springView.onFinishFreshAndLoad();
                        }
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                if (isFirst) {
                    LoadingViewUtil.showout(showingroup, showin);
                }else {
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

    private void initCtrl() {
        adapter = new RecycleAdapterQW(getActivity(), results, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.setOnItemClickListener(this);
        springView.setHeader(new AliHeader(getActivity(),false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData(false);
            }

            @Override
            public void onLoadmore() {
            }
        });
    }

    private void freshCtrl() {
//        adapter.notifyDataSetChanged();
        adapter.notifyItemRangeChanged(1,adapter.getResults().size());
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getLayoutPosition();
        if (position == 0) {

        } else if (position > 0 && position < adapter.getResults().size() +
                1) {
            Warning warning = adapter.getResults().get(position - 1);
            Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
            intent.putExtra("hostkey", warning.getHostKey());
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            if (AppData.App.getUser().getRoleType() == User.ROLE_COMMOM) {
                //用户直接进入告警列表
                intent.setClass(getActivity(), WarningListActivity.class);
                startActivity(intent);
            } else {
                //其他人需要先筛选电视台
                intent.setClass(getActivity(), SelectStationActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        }
    }

    private void netGetStations_countDevice() {
        RequestParams params = new RequestParams(AppData.Url.getTVs);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, TVStationPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    TVStationPojo tvStationPojo = (TVStationPojo) pojo;
                    List<TVStation> tvStations = tvStationPojo.getDataList();
                    if (tvStations!=null && tvStations.size()!=0) {
                        String stationStr = AppHelper.getStationCodeStr(tvStations);
                        if (!StrUtils.isEmpty(stationStr)) {
                            netCountDevice(stationStr);
                        }
                    }else {
                        //电视台为空
                        Toast.makeText(getActivity(),"你所在的电视台没有全文数据",Toast.LENGTH_SHORT).show();
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
                int days = TimeUtil.subDay(new Date(AppData.App.getUser().getCreateDate()), new Date());
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
