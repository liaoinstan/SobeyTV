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

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.common.MyPlayer;
import com.sobey.common.utils.FileUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrderDetail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private MyPlayer player = new MyPlayer();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe;
    private List<TestEntity> results = new ArrayList<>();
    private RecycleAdapterOrderDetail adapter;

    private ViewGroup showingroup;
    private View showin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_orderdetail);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        findViewById(R.id.btn_go_orderprog).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player!=null) player.onDestory();
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                String pathphoto = null;
                String pathvideo = null;
                String pathvoice = null;

                File dirphoto = new File(FileUtil.getPhotoFolder());
                File[] photos = dirphoto.listFiles();
                if (photos!=null && photos.length>0){
                    pathphoto = photos[photos.length-1].getPath();
                }

                File dirvideo = new File(FileUtil.getVideoFolder());
                File[] videos = dirvideo.listFiles();
                if (videos!=null && videos.length>0){
                    pathvideo = videos[videos.length-1].getPath();
                }

                File dirvoice = new File(FileUtil.getVoiceFolder());
                File[] voices = dirvoice.listFiles();
                if (voices!=null && voices.length>0){
                    pathvoice = voices[voices.length-1].getPath();
                }


                for (int i = 0; i < 20; i++) {
                    TestEntity entity = new TestEntity();
                    if (i % 2 == 0)
                        entity.setPathphotos(new String[]{pathphoto,pathphoto,pathphoto});
                    if (i % 3 == 0)
                        entity.setPathvideos(new String[]{pathvideo});
                    if (i % 4 == 0)
                        entity.setPathvoices(new String[]{pathvoice,pathvoice});
                    results.add(entity);
                }

                freshCtrl();
                LoadingViewUtil.showout(showingroup, showin);

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

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
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
            case R.id.btn_go_orderprog:
                intent.setClass(this,OrderProgActivity.class);
                startActivity(intent);
                break;
        }
    }
}
