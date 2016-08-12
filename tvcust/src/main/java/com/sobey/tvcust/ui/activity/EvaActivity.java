package com.sobey.tvcust.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonPojo;
import com.sobey.tvcust.entity.Eva;
import com.sobey.tvcust.entity.Lable;
import com.sobey.tvcust.entity.LablePojo;
import com.sobey.tvcust.entity.User;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EvaActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ViewGroup showingroup;
    private View showin;

    private TextView text_eva_complain;
    private TagFlowLayout flow_serv;
    private TagFlowLayout flow_tech;
    private TagFlowLayout flow_headtech;
    private TagFlowLayout flow_develop;
    private RatingBar rating_eva_server_attitude;
    private RatingBar rating_eva_server_speed;

    private RatingBar rating_eva_tech_attitude;
    private RatingBar rating_eva_tech_speed;
    private RatingBar rating_eva_tech_product;

    private RatingBar rating_eva_headtech_attitude;
    private RatingBar rating_eva_headtech_speed;

    private RatingBar rating_eva_develop_attitude;
    private RatingBar rating_eva_develop_speed;

    private View lay_eva_server;
    private View lay_eva_tech;
    private View lay_eva_headtech;
    private View lay_eva_develop;

    private EditText edit_eva_describe;
    private CircularProgressButton btn_go;

    private int orderId;
    private User user;

    private Callback.Cancelable cancelable;

    private static final int RESULT_COMPLAIN = 0xf101;

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
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cancelable != null) cancelable.cancel();
    }

    private void initBase() {
        user = AppData.App.getUser();
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
        rating_eva_server_attitude = (RatingBar) findViewById(R.id.rating_eva_server_attitude);
        rating_eva_server_speed = (RatingBar) findViewById(R.id.rating_eva_server_speed);

        rating_eva_tech_attitude = (RatingBar) findViewById(R.id.rating_eva_tech_attitude);
        rating_eva_tech_speed = (RatingBar) findViewById(R.id.rating_eva_tech_speed);
        rating_eva_tech_product = (RatingBar) findViewById(R.id.rating_eva_tech_product);
        if (AppData.App.getUser().getRoleType() == User.ROLE_COMMOM) {
            rating_eva_tech_product.setVisibility(View.VISIBLE);
        } else {
            rating_eva_tech_product.setVisibility(View.GONE);
        }

        rating_eva_headtech_attitude = (RatingBar) findViewById(R.id.rating_eva_headtech_attitude);
        rating_eva_headtech_speed = (RatingBar) findViewById(R.id.rating_eva_headtech_speed);

        rating_eva_develop_attitude = (RatingBar) findViewById(R.id.rating_eva_develop_attitude);
        rating_eva_develop_speed = (RatingBar) findViewById(R.id.rating_eva_develop_speed);

        lay_eva_server = findViewById(R.id.lay_eva_server);
        lay_eva_tech = findViewById(R.id.lay_eva_teach);
        lay_eva_headtech = findViewById(R.id.lay_eva_headteach);
        lay_eva_develop = findViewById(R.id.lay_eva_develop);

        edit_eva_describe = (EditText) findViewById(R.id.edit_eva_describe);
        btn_go = (CircularProgressButton) findViewById(R.id.btn_go);
    }

    private void initData() {
        //设置投诉状态
        netIsComplain();
        /////////////
        RequestParams params = new RequestParams(AppData.Url.getlables);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        CommonNet.samplepost(params, LablePojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    LablePojo lablePojo = (LablePojo) pojo;
                    setData(lablePojo);
                    LoadingViewUtil.showout(showingroup, showin);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(EvaActivity.this, text, Toast.LENGTH_SHORT).show();
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

    private void setData(LablePojo lablePojo) {
        if (lablePojo.getServiceLable() == null || lablePojo.getServiceLable().size() == 0) {
            lay_eva_server.setVisibility(View.GONE);
        } else {
            List<Lable> lables = lablePojo.getServiceLable();
            serverLables.clear();
            serverLables.addAll(lables);
            adapterServ.notifyDataChanged();
        }
        if (lablePojo.getTscLable() == null || lablePojo.getTscLable().size() == 0) {
            lay_eva_tech.setVisibility(View.GONE);
        } else {
            List<Lable> lables = lablePojo.getTscLable();
            techLables.clear();
            techLables.addAll(lables);
            adapterTech.notifyDataChanged();
        }
        if (lablePojo.getHeadTechLable() == null || lablePojo.getHeadTechLable().size() == 0) {
            lay_eva_headtech.setVisibility(View.GONE);
        } else {
            List<Lable> lables = lablePojo.getHeadTechLable();
            headTechLables.clear();
            headTechLables.addAll(lables);
            adapterHeadTeach.notifyDataChanged();
        }
        if (lablePojo.getHeadDevelopLable() == null || lablePojo.getHeadDevelopLable().size() == 0) {
            lay_eva_develop.setVisibility(View.GONE);
        } else {
            List<Lable> lables = lablePojo.getHeadDevelopLable();
            developLables.clear();
            developLables.addAll(lables);
            adapterDevelop.notifyDataChanged();
        }
    }

    private List<Lable> serverLables = new ArrayList<>();
    private List<Lable> techLables = new ArrayList<>();
    private List<Lable> headTechLables = new ArrayList<>();
    private List<Lable> developLables = new ArrayList<>();
    private TagAdapter adapterServ;
    private TagAdapter adapterTech;
    private TagAdapter adapterHeadTeach;
    private TagAdapter adapterDevelop;

    private void initCtrl() {
        text_eva_complain.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_go.setIndeterminateProgressMode(true);

        adapterServ = new TagAdapterEva(this, serverLables);
        adapterTech = new TagAdapterEva(this, techLables);
        adapterHeadTeach = new TagAdapterEva(this, headTechLables);
        adapterDevelop = new TagAdapterEva(this, developLables);

        flow_serv.setAdapter(adapterServ);
        flow_tech.setAdapter(adapterTech);
        flow_headtech.setAdapter(adapterHeadTeach);
        flow_develop.setAdapter(adapterDevelop);
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
            case R.id.btn_go:
                btn_go.setClickable(false);
                netCommitEVA();

                break;
        }
    }

    private List<Integer> getSelectIds(Set<Integer> indexs, List<Lable> results) {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int index : indexs) {
            ret.add(results.get(index).getId());
        }
        return ret;
    }

    private void netCommitEVA() {
        String describe = edit_eva_describe.getText().toString();

        btn_go.setProgress(50);
//        String msg = AppVali.complain_commit(describe);
//        if (msg != null) {
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//            btn_go.setProgress(-1);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    btn_go.setClickable(true);
//                    btn_go.setProgress(0);
//                }
//            }, 800);
//        } else {
        RequestParams params = new RequestParams(AppData.Url.commitEva);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("commentContent", describe);
        if (lay_eva_server.getVisibility() == View.VISIBLE) {
            Eva eva = new Eva();
            eva.setServiceAttitude((int) (rating_eva_server_attitude.getRating() * 20));
            eva.setDisposeSpeed((int) (rating_eva_server_speed.getRating() * 20));
            eva.setCommentLableIds(getSelectIds(flow_serv.getSelectedList(), serverLables));
            params.addBodyParameter("serviceData", new Gson().toJson(eva));
            if (eva.getServiceAttitude() == 0 || eva.getDisposeSpeed() == 0) {
                if (eva.getServiceAttitude() == 0) {
                    Toast.makeText(this, "客服的服务态度还没有打分哦", Toast.LENGTH_SHORT).show();
                } else if (eva.getDisposeSpeed() == 0) {
                    Toast.makeText(this, "客服的处理速度还没有打分哦", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
                return;
            }
        }
        if (lay_eva_tech.getVisibility() == View.VISIBLE) {
            Eva eva = new Eva();
            eva.setServiceAttitude((int) (rating_eva_tech_attitude.getRating() * 20));
            eva.setDisposeSpeed((int) (rating_eva_tech_speed.getRating() * 20));
            eva.setProductComment((int) (rating_eva_tech_product.getRating() * 20));
            eva.setCommentLableIds(getSelectIds(flow_tech.getSelectedList(), techLables));
            params.addBodyParameter("TSCData", new Gson().toJson(eva));
            if (eva.getServiceAttitude() == 0) {
                Toast.makeText(this, "技术的服务态度还没有打分哦", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
                return;
            } else if (eva.getDisposeSpeed() == 0) {
                Toast.makeText(this, "技术的处理速度还没有打分哦", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
                return;
            } else if (eva.getProductComment() == 0 && user.getRoleType() == User.ROLE_COMMOM) {
                Toast.makeText(this, "产品评价还没有打分哦", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
                return;
            }
        }
        if (lay_eva_headtech.getVisibility() == View.VISIBLE) {
            Eva eva = new Eva();
            eva.setServiceAttitude((int) (rating_eva_headtech_attitude.getRating() * 20));
            eva.setDisposeSpeed((int) (rating_eva_headtech_speed.getRating() * 20));
            eva.setCommentLableIds(getSelectIds(flow_headtech.getSelectedList(), headTechLables));
            params.addBodyParameter("headTechData", new Gson().toJson(eva));
            if (eva.getServiceAttitude() == 0 || eva.getDisposeSpeed() == 0 || eva.getProductComment() == 0) {
                if (eva.getServiceAttitude() == 0) {
                    Toast.makeText(this, "总部技术的服务态度还没有打分哦", Toast.LENGTH_SHORT).show();
                } else if (eva.getDisposeSpeed() == 0) {
                    Toast.makeText(this, "总部技术的处理速度还没有打分哦", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
                return;
            }
        }
        if (lay_eva_develop.getVisibility() == View.VISIBLE) {
            Eva eva = new Eva();
            eva.setServiceAttitude((int) (rating_eva_develop_attitude.getRating() * 20));
            eva.setDisposeSpeed((int) (rating_eva_develop_speed.getRating() * 20));
            eva.setCommentLableIds(getSelectIds(flow_develop.getSelectedList(), developLables));
            params.addBodyParameter("headDevelopData", new Gson().toJson(eva));
            if (eva.getServiceAttitude() == 0 || eva.getDisposeSpeed() == 0 || eva.getProductComment() == 0) {
                if (eva.getServiceAttitude() == 0) {
                    Toast.makeText(this, "总部研发的服务态度还没有打分哦", Toast.LENGTH_SHORT).show();
                } else if (eva.getDisposeSpeed() == 0) {
                    Toast.makeText(this, "总部研发的处理速度还没有打分哦", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
                return;
            }
        }
//        btn_go.setClickable(true);
//        Toast.makeText(this, "严重通过", Toast.LENGTH_SHORT).show();
        cancelable = CommonNet.samplepost(params, CommonPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(EvaActivity.this, text, Toast.LENGTH_SHORT).show();

                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERLIST);
                        EventBus.getDefault().post(AppConstant.EVENT_UPDATE_ORDERDESCRIBE);
                        setResult(RESULT_OK);
                        finish();
                    }
                }, 800);
            }

            @Override
            public void netSetError(int code, final String text) {
                Toast.makeText(EvaActivity.this, text, Toast.LENGTH_SHORT).show();
                btn_go.setProgress(-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
            }
        });
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
