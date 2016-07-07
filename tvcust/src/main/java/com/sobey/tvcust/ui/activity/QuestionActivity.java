package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.CommonPojo;
import com.sobey.tvcust.entity.OrderCategory;
import com.sobey.tvcust.entity.OrderCategoryPojo;
import com.sobey.tvcust.ui.adapter.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterQuestion;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends BaseAppCompatActicity implements OnRecycleItemClickListener{

    private RecyclerView recyclerView;
    private SpringView springView;
    private List<OrderCategory> results = new ArrayList<>();
    private RecycleAdapterQuestion adapter;

    private ViewGroup showingroup;
    private View showin;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        springView = (SpringView) findViewById(R.id.spring);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_question);
    }

    private void initData() {

        final RequestParams params = new RequestParams(AppData.Url.question);
        //params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("type", type);
        CommonNet.samplepost(params,OrderCategoryPojo.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null) netSetError(code,text);
                else{
                    OrderCategoryPojo categoryPojo = (OrderCategoryPojo) pojo;
                    List<OrderCategory> questions = categoryPojo.getDataList();
                    List<OrderCategory> results = adapter.getResults();
                    results.clear();
                    results.addAll(questions);
                    freshCtrl();
                    LoadingViewUtil.showout(showingroup,showin);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(QuestionActivity.this,text,Toast.LENGTH_SHORT).show();
                LoadingViewUtil.showin(showingroup,R.layout.layout_fail,showin,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }

            @Override
            public void netStart(int code) {
                showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading,showin);
            }

            @Override
            public void netEnd(int code) {

            }
        });

    }

    private void initCtrl() {
        adapter = new RecycleAdapterQuestion(this,R.layout.item_recycle_question,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        springView.setHeader(new AliHeader(this, false));
        springView.setFooter(new AliFooter(this, false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData();new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getLayoutPosition();
        OrderCategory question = adapter.getResults().get(position);
        Intent intent = new Intent();
        intent.putExtra("name",question.getCategoryName());
        intent.putExtra("id",question.getId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
