package com.sobey.tvcust.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sobey.common.entity.Images;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.InfoDetailActivity;
import com.sobey.tvcust.ui.adapter.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterInfoQuan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class InfoQuanFragment extends BaseFragment implements OnRecycleItemClickListener{

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private RecyclerView recyclerView;
    private RecycleAdapterInfoQuan adapter;
    private List<TestEntity> results = new ArrayList<>();
    private List<Images> images = new ArrayList<>();

    private SwipeRefreshLayout swipe;

    public static Fragment newInstance(int position) {
        InfoQuanFragment f = new InfoQuanFragment();
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
        rootView = inflater.inflate(R.layout.fragment_info_quan, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_info_quan);
        swipe = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_info_quan);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading,showin);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                results.clear();
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                images.clear();
                images.add(new Images(1,"夏季衬衫，清凉一夏","http://img2.imgtn.bdimg.com/it/u=2401368128,869327646&fm=21&gp=0.jpg"));
                images.add(new Images(2,"男子怒打妻儿，竟然只为了买一件衣服","http://img1.imgtn.bdimg.com/it/u=839795904,770645941&fm=21&gp=0.jpg"));
                images.add(new Images(3,"冠希复出，陈妍希表示呵呵","http://pic44.nipic.com/20140726/6205649_111852997000_2.jpg"));
                images.add(new Images(4,"iphon7预览版发售，你还在等什么","http://img4.imgtn.bdimg.com/it/u=3831361042,2579496760&fm=21&gp=0.jpg"));
                images.add(new Images(5,"马云：成功不只是嘴上说说","http://img0.imgtn.bdimg.com/it/u=1415714570,832901974&fm=21&gp=0.jpg"));
                //加载成功
                freshCtrl();
                LoadingViewUtil.showout(showingroup,showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 1000);
    }

    private void initCtrl() {
        adapter = new RecycleAdapterInfoQuan(getActivity(),results,images,position==0?true:false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    boolean b = !ViewCompat.canScrollVertically(recyclerView, 1);
                    if (b){
                        Toast.makeText(getActivity(),"loadmore",Toast.LENGTH_SHORT).show();
                        Log.e("liao","more");
                    }
                }
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                },2000);
            }
        });
//        !ViewCompat.canScrollVertically(recyclerView, 1);
        adapter.setOnItemClickListener(this);
    }

    private void freshCtrl(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        if (position==0 && viewHolder.getLayoutPosition()==0){
            //banner
        }else {
            Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
            intent.putExtra("url","http://cn.bing.com");
            startActivity(intent);
        }
    }
}
