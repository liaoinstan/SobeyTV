package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Set;

public class EvaActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private ViewGroup showingroup;
    private View showin;

    private TextView text_eva_complain;
    private TagFlowLayout flow_serv;
    private TagFlowLayout flow_tech;
    private RatingBar rating_eva_server_attitude;
    private RatingBar rating_eva_server_speed;
    private RatingBar rating_eva_tech_attitude;
    private RatingBar rating_eva_tech_speed;
    private RatingBar rating_eva_tech_product;
    private EditText edit_eva_describe;
    private CircularProgressButton btn_go;

    private int orderId;

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

        initBase();
        initView();
        initData();
//        initCtrl();
    }

    private void initBase() {
        Intent intent = getIntent();
        if (intent.hasExtra("orderId")) {
            orderId = intent.getIntExtra("orderId", 0);
        }
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        text_eva_complain = (TextView) findViewById(R.id.text_eva_complain);

        flow_serv = (TagFlowLayout) findViewById(R.id.flow_tag_serv);
        flow_tech = (TagFlowLayout) findViewById(R.id.flow_tag_tech);
        rating_eva_server_attitude = (RatingBar) findViewById(R.id.rating_eva_server_attitude);
        rating_eva_server_speed = (RatingBar) findViewById(R.id.rating_eva_server_speed);
        rating_eva_tech_attitude = (RatingBar) findViewById(R.id.rating_eva_tech_attitude);
        rating_eva_tech_speed = (RatingBar) findViewById(R.id.rating_eva_tech_speed);
        rating_eva_tech_product = (RatingBar) findViewById(R.id.rating_eva_tech_product);
        edit_eva_describe = (EditText) findViewById(R.id.edit_eva_describe);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading,showin);
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
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);
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
            case R.id.btn_go:
                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(0);
                    }
                }, 800);

                int server_attitude = (int) rating_eva_server_attitude.getRating();
                int server_speed = (int) rating_eva_server_speed.getRating();
                int tech_attitude = (int) rating_eva_tech_attitude.getRating();
                int tech_speed = (int) rating_eva_tech_speed.getRating();
                int tech_product = (int) rating_eva_tech_product.getRating();
                Set<Integer> select_serv = flow_serv.getSelectedList();
                Set<Integer> select_tech = flow_tech.getSelectedList();
                String describe = edit_eva_describe.getText().toString();

                Log.e("liao","server_attitude:" + server_attitude);
                Log.e("liao","server_speed:" + server_speed);
                Log.e("liao","tech_attitude:" + tech_attitude);
                Log.e("liao","tech_speed:" + tech_speed);
                Log.e("liao","tech_product:" + tech_product);
                Log.e("liao","describe:" + describe);
                for (int index:select_serv){
                    Log.e("liao","" + mVals[index]);
                }
                for (int index:select_tech){
                    Log.e("liao","" + mVals[index]);
                }

                break;
        }
    }
}
