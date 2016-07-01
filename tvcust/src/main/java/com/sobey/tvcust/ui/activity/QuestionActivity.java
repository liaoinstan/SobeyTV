package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterQuestion;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends BaseAppCompatActicity implements OnRecycleItemClickListener{

    private RecyclerView recyclerView;
    private List<TestEntity> results = new ArrayList<>();
    private RecycleAdapterQuestion adapter;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initData();
        initView();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }
    }

    private void initData() {
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
        results.add(new TestEntity());
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle_question);
    }

    private void initCtrl() {
        adapter = new RecycleAdapterQuestion(this,R.layout.item_recycle_question,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
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
        TestEntity question = adapter.getResults().get(position);
        Intent intent = new Intent();
        intent.putExtra("name",question.getName());
        intent.putExtra("id",question.getId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
