package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.ListAdapterOrderTrack;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ListView listView_full;
    private List<TestEntity> results = new ArrayList<>();
    private ListAdapterOrderTrack adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initView();
        initCtrl();

        toolbar.setFocusable(true);
        toolbar.setFocusableInTouchMode(true);
        toolbar.requestFocus();
    }

    private void initData() {
        results.add(new TestEntity("维修人员已报给完成"));
        results.add(new TestEntity("维修人员已到达，开始维修"));
        results.add(new TestEntity("已为您分配维修人员，请耐心等待"));
        results.add(new TestEntity("已收到您的订单，正在为您分配"));
        results.add(new TestEntity("您的订单已发出"));
    }

    private void initView() {
        listView_full = (ListView) findViewById(R.id.listfull_ordertrack);
    }

    private void initCtrl() {
        adapter = new ListAdapterOrderTrack(this,R.layout.item_list_track,results);
        listView_full.setAdapter(adapter);
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
