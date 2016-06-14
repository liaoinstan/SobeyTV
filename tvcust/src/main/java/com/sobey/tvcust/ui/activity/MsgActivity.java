package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;
import com.sobey.tvcust.ui.adapter.RecycleAdapterQuestion;

import java.util.ArrayList;
import java.util.List;

public class MsgActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private List<TestEntity> results = new ArrayList<>();
    private RecycleAdapterMsg adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initView();
        initCtrl();
    }

    private void initData() {
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle_msg);
    }

    private void initCtrl() {
        adapter = new RecycleAdapterMsg(this,R.layout.item_recycle_msg,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
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
}
