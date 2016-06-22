package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class EvaActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewGroup showingroup;
    private View showin;

    private TagFlowLayout flow_serv;
    private TagFlowLayout flow_tech;
    private TextView text_eva_complain;

    private String[] mVals = new String[]
            {"亲切有理", "声音好听", "解答到位 ", "派单迅速", "有耐心", "技术强",
                    "长得帅", "会打篮球"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eva);
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
        text_eva_complain = (TextView) findViewById(R.id.text_eva_complain);
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
        text_eva_complain.setOnClickListener(this);
        TagAdapter adapterServ = new TagAdapter<String>(mVals){
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) EvaActivity.this.getLayoutInflater().inflate(R.layout.tv,parent, false);
                tv.setText(s);
                return tv;
            }

        };
        TagAdapter adapterTech = new TagAdapter<String>(mVals){
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) EvaActivity.this.getLayoutInflater().inflate(R.layout.tv,parent, false);
                tv.setText(s);
                return tv;
            }
        };
        flow_serv.setAdapter(adapterServ);
        flow_tech.setAdapter(adapterTech);
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
            case R.id.text_eva_complain:
                intent.setClass(this,ComplainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
