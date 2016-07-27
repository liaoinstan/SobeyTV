package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.AssisterPojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.RecycleAdapterCopy;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class CopyActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SpringView springView;
    private List<User> results = new ArrayList<>();
    private RecycleAdapterCopy adapter;

    private ViewGroup showingroup;
    private View showin;

    private CircularProgressButton btn_go;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData(true);
        initCtrl();
    }

    private void initBase() {
        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", 0);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        springView = (SpringView) findViewById(R.id.spring);

        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }

    private void initData(final boolean isFirst) {

        RequestParams params = new RequestParams(AppData.Url.copyer);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("userId",userId+"");
        CommonNet.samplepost(params, AssisterPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    AssisterPojo assisterPojo = (AssisterPojo) pojo;
                    List<User> assister = assisterPojo.getDataList();
                    //有数据才添加，否则显示lack信息
                    if (assister != null && assister.size() != 0) {
                        List<User> results = adapter.getResults();
                        results.clear();
                        results.addAll(assister);
                        freshCtrl();
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            springView.onFinishFreshAndLoad();
                        }
                    } else {
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
                    Toast.makeText(CopyActivity.this, text, Toast.LENGTH_SHORT).show();
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
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading,showin);
                }
            }
        });
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);

        adapter = new RecycleAdapterCopy(this, R.layout.item_recycle_orderallocate, results);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        springView.setHeader(new AliHeader(this, false));
        springView.setFooter(new AliFooter(this, false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData(false);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
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

        switch (v.getId()) {
            case R.id.btn_go:
                final ArrayList<Integer> ids = adapter.getSelectUsers();
                final String nameStr = adapter.getSelectNameStr();

                btn_go.setProgress(50);
                if (ids==null||ids.size()==0){
                    Toast.makeText(this, "请选择抄送对象", Toast.LENGTH_SHORT).show();
                    btn_go.setProgress(-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(0);
                        }
                    }, 800);
                }else {
                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("ids", ids);
                            intent.putExtra("nameStr", nameStr);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 1000);
                }
                break;
        }
    }

}
