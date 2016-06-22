package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class EvaDetailActivity extends AppCompatActivity {

    private ViewGroup showingroup;
    private View showin;

    private TagFlowLayout flow_serv;
    private TagFlowLayout flow_tech;

    private String[] mVals = new String[]
            {"亲切有理", "声音好听", "解答到位 ", "派单迅速", "有耐心", "技术强",
                    "长得帅", "会打篮球"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evadetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
//        initCtrl();
    }


    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        flow_serv = (TagFlowLayout) findViewById(R.id.flow_tag_serv);
        flow_tech = (TagFlowLayout) findViewById(R.id.flow_tag_tech);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                initCtrl();
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
        TagAdapter adapterServ = new TagAdapter<String>(mVals){
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) EvaDetailActivity.this.getLayoutInflater().inflate(R.layout.tv,parent, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                return true;
            }
        };
        TagAdapter adapterTech = new TagAdapter<String>(mVals){
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) EvaDetailActivity.this.getLayoutInflater().inflate(R.layout.tv,parent, false);
                tv.setText(s);
                return tv;
            }
            @Override
            public boolean setSelected(int position, String s) {
                return true;
            }
        };
        flow_serv.setAdapter(adapterServ);
        flow_tech.setAdapter(adapterTech);
        flow_serv.setEnabled(false);
        flow_tech.setEnabled(false);
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
