package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.AssisterPojo;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.adapter.RecycleAdapterAssist;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 援助和选择用户页面
 * type为0 是援助页面 type为1 是选择用户页面
 */
public class AssistActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private SpringView springView;
    private List<User> results = new ArrayList<>();
    private RecycleAdapterAssist adapter;

    private ViewGroup showingroup;
    private View showin;

    private TextView text_copyers;
    private View lay_assist_tocopy;
    private CircularProgressButton btn_go;

    private int type = 0;
    private int orderId;
    private ArrayList<Integer> ids;

    private static final int RESULT_COPY = 0xf102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist);
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
        if (intent.hasExtra("orderId")) {
            orderId = intent.getIntExtra("orderId", 0);
        }
        if (getIntent().hasExtra("type")){
            type = getIntent().getIntExtra("type",0);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        springView = (SpringView) findViewById(R.id.spring);

        text_copyers = (TextView) findViewById(R.id.text_assist_copyers);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
        lay_assist_tocopy = findViewById(R.id.lay_assist_tocopy);
        switch (type){
            case 0:
                getSupportActionBar().setTitle("援助选择");
                btn_go.setText("请求协助");
                btn_go.setIdleText("请求协助");
                lay_assist_tocopy.setVisibility(View.VISIBLE);
                break;
            case 1:
                getSupportActionBar().setTitle("用户选择");
                btn_go.setText("确认");
                btn_go.setIdleText("确认");
                lay_assist_tocopy.setVisibility(View.GONE);
                break;
        }
    }

    private void initData(final boolean isFirst) {

        RequestParams params = new RequestParams(AppData.Url.assister);
        params.addHeader("token", AppData.App.getToken());
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
                        LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                if (isFirst) {
                    Toast.makeText(AssistActivity.this, text, Toast.LENGTH_SHORT).show();
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
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading);
                }
            }
        });
    }

    private void initCtrl() {
        lay_assist_tocopy.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);

        adapter = new RecycleAdapterAssist(this,R.layout.item_recycle_orderallocate,results);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        springView.setHeader(new AliHeader(this,false));
        springView.setFooter(new AliFooter(this,false));
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

    private void freshCtrl(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_COPY:
                if (resultCode == RESULT_OK) {
                    ids = (ArrayList<Integer>)data.getSerializableExtra("ids");
                    String nameStr = data.getStringExtra("nameStr");
                    text_copyers.setText(nameStr);
                }
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
            case R.id.lay_assist_tocopy:
                intent.setClass(this,CopyActivity.class);
                startActivityForResult(intent,RESULT_COPY);
                break;
            case R.id.btn_go:
                if (type==0) {
                    netAssistCommit();
                }else if(type==1){
                    selectUser();
                }
                break;
        }
    }

    private void netAssistCommit(){
        User assister = adapter.getSelectUser();

        String msg = AppVali.allocate_commit(assister);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            btn_go.setProgress(50);
            RequestParams params = new RequestParams(AppData.Url.assistCommit);
            params.addHeader("token", AppData.App.getToken());
            params.addBodyParameter("orderId", orderId + "");
            params.addBodyParameter("headTechId", assister.getId() + "");
            CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                @Override
                public void netGo(int code, Object pojo, String text, Object obj) {
                    Toast.makeText(AssistActivity.this, text, Toast.LENGTH_SHORT).show();
                    btn_go.setProgress(100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(0);
                            finish();
                            EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                        }
                    }, 800);
                }

                @Override
                public void netSetError(int code, String text) {
                    Toast.makeText(AssistActivity.this, text, Toast.LENGTH_SHORT).show();
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
    }

    private void selectUser(){
        final User selectUser = adapter.getSelectUser();

        btn_go.setProgress(50);
        String msg = AppVali.allocate_commit(selectUser);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
                    intent.putExtra("id", selectUser.getId());
                    intent.putExtra("name", selectUser.getRealName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }, 1000);
        }
    }
}
