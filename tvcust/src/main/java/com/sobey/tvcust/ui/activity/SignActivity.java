package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.OrderTrack;
import com.sobey.tvcust.entity.OrderTrackPojo;

import org.xutils.http.RequestParams;

import java.util.List;

public class SignActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private ViewGroup showingroup;
    private View showin;

    private TextView text_sign_day;
    private TextView text_sign_inte;
    private TextView text_sign_do;
    private View btn_sign_do;

    private boolean isSign;
    private int signDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        text_sign_day = (TextView) findViewById(R.id.text_sign_day);
        text_sign_inte = (TextView) findViewById(R.id.text_sign_inte);
        text_sign_do = (TextView) findViewById(R.id.text_sign_do);
        btn_sign_do = findViewById(R.id.btn_sign_do);

        btn_sign_do.setVisibility(View.INVISIBLE);
    }

    private void initData() {

        RequestParams params = new RequestParams(AppData.Url.getUserSign);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "错误:返回数据为空");
                else {
                    CommonEntity com = (CommonEntity) pojo;
                    isSign = com.getIsSign() == 0 ? true : false;
                    signDays = com.getSignDays();
                    int signGrades = com.getSignGrades();
                    freshCtrl(signDays, signGrades);

                    LoadingViewUtil.showout(showingroup, showin);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(SignActivity.this, text, Toast.LENGTH_SHORT).show();
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
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

//        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //加载成功
//                isSign = false;
//                freshCtrl(19, 5698);
//                LoadingViewUtil.showout(showingroup, showin);
//
//                //加载失败
////                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
////                    @Override
////                    public void onClick(View v) {
////                        initData();
////                    }
////                });
//            }
//        }, 2000);
    }

    private void initCtrl() {
        findViewById(R.id.btn_go_signlist).setOnClickListener(this);
    }

    private void freshCtrl(int days, int grades) {
        btn_sign_do.setOnClickListener(this);
        {
            String strpre = "您已累计签到";
            String strday = "" + days;
            String straft = "天";
            SpannableString strSpan = new SpannableString(strpre + strday + straft);
            strSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sb_blue)), strpre.length(), strpre.length() + strday.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text_sign_day.setText(strSpan);
        }
        {
            String strpre = "共有";
            String strday = "" + grades;
            String straft = "积分";
            SpannableString strSpan = new SpannableString(strpre + strday + straft);
            strSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sb_blue)), strpre.length(), strpre.length() + strday.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text_sign_inte.setText(strSpan);
        }

        if (isSign) {
            text_sign_do.setText("已签到");
        } else {
            text_sign_do.setText("马上签到");
        }

        //出场动画
        YoYo.with(Techniques.Landing)
                .duration(1000)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        btn_sign_do.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(findViewById(R.id.btn_sign_do));
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
        switch (v.getId()) {
            case R.id.btn_go_signlist:
                intent.setClass(this, SignListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sign_do:
                if (!isSign) {
                    RequestParams params = new RequestParams(AppData.Url.sign);
                    params.addHeader("token", AppData.App.getToken());
                    CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {
                            if (pojo == null) netSetError(code, "错误:返回数据为空");
                            else {
                                Toast.makeText(SignActivity.this, text, Toast.LENGTH_SHORT).show();
                                CommonEntity com = (CommonEntity) pojo;
                                int signGrades = com.getSignGrades();
                                freshCtrl(signDays+1,signGrades);
                                YoYo.with(Techniques.Landing)
                                        .duration(700)
                                        .playOn(findViewById(R.id.btn_sign_do));
                                isSign = true;
                                text_sign_do.setText("已签到");
                            }
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(SignActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }
}
