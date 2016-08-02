package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.fragment.SelectStationFragment;

//type : 1 设备列表  2：告警列表
public class SelectStationActivity extends BaseAppCompatActivity {

    private Fragment contactFragment;

    private int type;

    public int getType() {
        return type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectstation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();

        contactFragment = SelectStationFragment.newInstance(0);

        // 添加显示第一个fragment
        FragmentTransaction ftx = getSupportFragmentManager().beginTransaction();
        if (!contactFragment.isAdded()) {
            ftx.add(R.id.fragment_container, contactFragment,0+"");
        }else {
            ftx.show(contactFragment);
        }
        ftx.commit();
    }

    private void initBase() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type",1);
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
}
