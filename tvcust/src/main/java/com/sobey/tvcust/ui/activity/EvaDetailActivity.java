package com.sobey.tvcust.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonPojo;
import com.sobey.tvcust.entity.Eva;
import com.sobey.tvcust.entity.EvaPojo;
import com.sobey.tvcust.entity.Lable;
import com.sobey.tvcust.entity.User;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class EvaDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ViewGroup showingroup;
    private View showin;

    private TextView text_eva_complain;
    private TagFlowLayout flow_serv;
    private TagFlowLayout flow_tech;
    private TagFlowLayout flow_headtech;
    private TagFlowLayout flow_develop;
    private TagFlowLayout flow_userserver;
    private TagFlowLayout flow_usertech;
    private RatingBar rating_eva_server_attitude;
    private RatingBar rating_eva_server_speed;

    private RatingBar rating_eva_tech_attitude;
    private RatingBar rating_eva_tech_speed;
    private RatingBar rating_eva_tech_product;

    private RatingBar rating_eva_headtech_attitude;
    private RatingBar rating_eva_headtech_speed;

    private RatingBar rating_eva_develop_attitude;
    private RatingBar rating_eva_develop_speed;

    private RatingBar rating_eva_userserver_attitude;
    private RatingBar rating_eva_userserver_speed;

    private RatingBar rating_eva_usertech_attitude;
    private RatingBar rating_eva_usertech_speed;
    private RatingBar rating_eva_usertech_product;

    private View lay_eva_server;
    private View lay_eva_tech;
    private View lay_eva_headtech;
    private View lay_eva_develop;
    private View lay_eva_userserver;
    private View lay_eva_usertech;
    private TextView text_eva_describe;

    private int orderId;

    private static final int RESULT_COMPLAIN = 0xf101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evadetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
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
        flow_headtech = (TagFlowLayout) findViewById(R.id.flow_tag_headtech);
        flow_develop = (TagFlowLayout) findViewById(R.id.flow_tag_develop);
        flow_userserver = (TagFlowLayout) findViewById(R.id.flow_tag_userserver);
        flow_usertech = (TagFlowLayout) findViewById(R.id.flow_tag_usertech);
        rating_eva_server_attitude = (RatingBar) findViewById(R.id.rating_eva_server_attitude);
        rating_eva_server_speed = (RatingBar) findViewById(R.id.rating_eva_server_speed);

        rating_eva_tech_attitude = (RatingBar) findViewById(R.id.rating_eva_tech_attitude);
        rating_eva_tech_speed = (RatingBar) findViewById(R.id.rating_eva_tech_speed);
        rating_eva_tech_product = (RatingBar) findViewById(R.id.rating_eva_tech_product);
        //只有用户有产品评价
        if (AppData.App.getUser().getRoleType() == User.ROLE_COMMOM) {
            rating_eva_tech_product.setVisibility(View.VISIBLE);
        } else {
            rating_eva_tech_product.setVisibility(View.GONE);
        }

        rating_eva_headtech_attitude = (RatingBar) findViewById(R.id.rating_eva_headtech_attitude);
        rating_eva_headtech_speed = (RatingBar) findViewById(R.id.rating_eva_headtech_speed);

        rating_eva_develop_attitude = (RatingBar) findViewById(R.id.rating_eva_develop_attitude);
        rating_eva_develop_speed = (RatingBar) findViewById(R.id.rating_eva_develop_speed);

        rating_eva_userserver_attitude = (RatingBar) findViewById(R.id.rating_eva_userserver_attitude);
        rating_eva_userserver_speed = (RatingBar) findViewById(R.id.rating_eva_userserver_speed);

        rating_eva_usertech_attitude = (RatingBar) findViewById(R.id.rating_eva_usertech_attitude);
        rating_eva_usertech_speed = (RatingBar) findViewById(R.id.rating_eva_usertech_speed);
        rating_eva_usertech_product = (RatingBar) findViewById(R.id.rating_eva_usertech_product);

        lay_eva_server = findViewById(R.id.lay_eva_server);
        lay_eva_tech = findViewById(R.id.lay_eva_teach);
        lay_eva_headtech = findViewById(R.id.lay_eva_headteach);
        lay_eva_develop = findViewById(R.id.lay_eva_develop);
        lay_eva_userserver = findViewById(R.id.lay_eva_userserver);
        lay_eva_usertech = findViewById(R.id.lay_eva_usertech);

        text_eva_describe = (TextView) findViewById(R.id.text_eva_describe);
    }

    private void initData() {
        //设置投诉状态
        netIsComplain();
        /////////////
        RequestParams params = new RequestParams(AppData.Url.getEva);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        CommonNet.samplepost(params, EvaPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) {
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData();
                        }
                    });
                } else {
                    EvaPojo evaPojo = (EvaPojo) pojo;
                    setData(evaPojo);
                    LoadingViewUtil.showout(showingroup, showin);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(EvaDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }

            @Override
            public void netStart(int code) {
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
            }
        });
    }

    private void setData(EvaPojo evaPojo) {
        if (evaPojo.getServiceData() == null) {
            lay_eva_server.setVisibility(View.GONE);
        } else {
            Eva eva = evaPojo.getServiceData();
            rating_eva_server_attitude.setRating(eva.getServiceAttitude() / 20);
            rating_eva_server_speed.setRating(eva.getDisposeSpeed() / 20);

            List<Lable> lables = eva.getLables();
            serverLables.clear();
            serverLables.addAll(lables);
            adapterServ.notifyDataChanged();
        }
        if (evaPojo.getTscdata() == null) {
            lay_eva_tech.setVisibility(View.GONE);
        } else {
            Eva eva = evaPojo.getTscdata();
            rating_eva_tech_attitude.setRating(eva.getServiceAttitude() / 20);
            rating_eva_tech_speed.setRating(eva.getDisposeSpeed() / 20);
            rating_eva_tech_product.setRating(eva.getProductComment() / 20);

            List<Lable> lables = eva.getLables();
            techLables.clear();
            techLables.addAll(lables);
            adapterTech.notifyDataChanged();
        }
        if (evaPojo.getHeadTechData() == null) {
            lay_eva_headtech.setVisibility(View.GONE);
        } else {
            Eva eva = evaPojo.getHeadTechData();
            rating_eva_headtech_attitude.setRating(eva.getServiceAttitude() / 20);
            rating_eva_headtech_speed.setRating(eva.getDisposeSpeed() / 20);

            List<Lable> lables = eva.getLables();
            headTechLables.clear();
            headTechLables.addAll(lables);
            adapterHeadTeach.notifyDataChanged();
        }
        if (evaPojo.getHeadDevelopData() == null) {
            lay_eva_develop.setVisibility(View.GONE);
        } else {
            Eva eva = evaPojo.getHeadDevelopData();
            rating_eva_develop_attitude.setRating(eva.getServiceAttitude() / 20);
            rating_eva_develop_speed.setRating(eva.getDisposeSpeed() / 20);

            List<Lable> lables = eva.getLables();
            developLables.clear();
            developLables.addAll(lables);
            adapterDevelop.notifyDataChanged();
        }
        if (evaPojo.getUserServerData() == null) {
            lay_eva_userserver.setVisibility(View.GONE);
        } else {
            Eva eva = evaPojo.getUserServerData();
            rating_eva_userserver_attitude.setRating(eva.getServiceAttitude() / 20);
            rating_eva_userserver_speed.setRating(eva.getDisposeSpeed() / 20);

            List<Lable> lables = eva.getLables();
            userServerLables.clear();
            userServerLables.addAll(lables);
            adapterUserServer.notifyDataChanged();
        }
        if (evaPojo.getUserTechData() == null) {
            lay_eva_usertech.setVisibility(View.GONE);
        } else {
            Eva eva = evaPojo.getUserTechData();
            rating_eva_usertech_attitude.setRating(eva.getServiceAttitude() / 20);
            rating_eva_usertech_speed.setRating(eva.getDisposeSpeed() / 20);
            rating_eva_usertech_product.setRating(eva.getProductComment() / 20);

            List<Lable> lables = eva.getLables();
            userTechLables.clear();
            userTechLables.addAll(lables);
            adapterUserTech.notifyDataChanged();
        }

        String commentContent = evaPojo.getCommentContent();
        if (StrUtils.isEmpty(commentContent)){
            commentContent = "没有填写评价内容";
        }
        text_eva_describe.setText(commentContent);
    }

    private List<Lable> serverLables = new ArrayList<>();
    private List<Lable> techLables = new ArrayList<>();
    private List<Lable> headTechLables = new ArrayList<>();
    private List<Lable> developLables = new ArrayList<>();
    private List<Lable> userServerLables = new ArrayList<>();
    private List<Lable> userTechLables = new ArrayList<>();
    private TagAdapter adapterServ;
    private TagAdapter adapterTech;
    private TagAdapter adapterHeadTeach;
    private TagAdapter adapterDevelop;
    private TagAdapter adapterUserServer;
    private TagAdapter adapterUserTech;

    private void initCtrl() {
        text_eva_complain.setOnClickListener(this);

        adapterServ = new TagAdapterEva(this, serverLables);
        adapterTech = new TagAdapterEva(this, techLables);
        adapterHeadTeach = new TagAdapterEva(this, headTechLables);
        adapterDevelop = new TagAdapterEva(this, developLables);
        adapterUserServer = new TagAdapterEva(this, userServerLables);
        adapterUserTech = new TagAdapterEva(this, userTechLables);

        flow_serv.setAdapter(adapterServ);
        flow_tech.setAdapter(adapterTech);
        flow_headtech.setAdapter(adapterHeadTeach);
        flow_develop.setAdapter(adapterDevelop);
        flow_userserver.setAdapter(adapterUserServer);
        flow_usertech.setAdapter(adapterUserTech);

        flow_serv.setEnabled(false);
        flow_tech.setEnabled(false);
        flow_headtech.setEnabled(false);
        flow_develop.setEnabled(false);
        flow_userserver.setEnabled(false);
        flow_usertech.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_COMPLAIN:
                if (resultCode == RESULT_OK) {
                    text_eva_complain.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.text_eva_complain:
                intent.setClass(this, ComplainActivity.class);
                intent.putExtra("orderId", orderId);
                startActivityForResult(intent, RESULT_COMPLAIN);
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

    private class TagAdapterEva extends TagAdapter<Lable> {

        private Context context;

        public TagAdapterEva(Context context, List<Lable> datas) {
            super(datas);
            this.context = context;
        }

        @Override
        public View getView(FlowLayout parent, int position, Lable lable) {
            TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv, parent, false);
            tv.setText(lable.getLable());
            return tv;
        }

        @Override
        public boolean setSelected(int position, Lable lable) {
            return true;
        }
    }

    private void netIsComplain() {
        RequestParams params = new RequestParams(AppData.Url.isComplain);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        CommonNet.samplepost(params, CommonPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                text_eva_complain.setVisibility(View.VISIBLE);
            }

            @Override
            public void netSetError(int code, String text) {
                text_eva_complain.setVisibility(View.GONE);
            }
        });
    }
}
