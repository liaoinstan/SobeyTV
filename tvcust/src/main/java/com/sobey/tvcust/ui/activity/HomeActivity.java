package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.shelwee.update.UpdateHelper;
import com.shelwee.update.listener.OnUpdateListener;
import com.shelwee.update.pojo.UpdateInfo;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.ui.dialog.DialogSure;
import com.sobey.tvcust.ui.fragment.HomeInfoFragment;
import com.sobey.tvcust.ui.fragment.HomeQwFragment;
import com.sobey.tvcust.ui.fragment.HomeMeFragment;
import com.sobey.tvcust.ui.fragment.HomeOrderFragment;
import com.sobey.tvcust.ui.fragment.HomeServerFragment;
import com.sobey.common.utils.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.util.List;

public class HomeActivity extends BaseAppCompatActivity implements View.OnClickListener {

    UpdateHelper updateHelper;

    private Fragment homeFragment0;
    private Fragment homeFragment1;
    private Fragment homeFragment2;
    private Fragment homeFragment3;
    private Fragment homeFragment4;
    private Fragment[] fragments;
    private Button[] mTabs;
    private ImageView img_msg;
//    private Toolbar toolbar;

    private int currentTabIndex = -1;

    private DialogSure dialogSure;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTabIndex", currentTabIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt("currentTabIndex");
        }
        setContentView(R.layout.activity_home);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        PermissionsUtil.checkAndRequestPermissions(this, findViewById(R.id.coordinator));

        initBase();
        initView();
        initCtrl();
        netIsSign();

        if (currentTabIndex == -1) {
            currentTabIndex = 2;
        }
        mTabs[currentTabIndex].setSelected(true);
        // 添加显示第一个fragment
        FragmentTransaction ftx = getSupportFragmentManager().beginTransaction();
        hideAllFragment(ftx);
        if (!fragments[currentTabIndex].isAdded()) {
            ftx.add(R.id.fragment_container, fragments[currentTabIndex], currentTabIndex + "");
        } else {
            ftx.show(fragments[currentTabIndex]);
        }
        ftx.commit();
        List fragmentss = getSupportFragmentManager().getFragments();
        if (fragmentss != null)
            Log.e("liao", fragmentss.size() + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogSure != null) dialogSure.dismiss();
        if (updateHelper != null) updateHelper.onDestory();
    }

    private void hideAllFragment(FragmentTransaction ftx) {
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i].isAdded()) {
                ftx.hide(fragments[i]);
            }
        }
    }

    private void initBase() {
        dialogSure = new DialogSure(this, "亲，您还没有签到哦~", "取消", "立刻签到");
        dialogSure.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSure.hide();
                netSign();
            }
        });
        mTabs = new Button[5];
        updateHelper = new UpdateHelper.Builder(this)
                .checkUrl(AppData.Url.version)
                .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
                .isHintNewVersion(false)
                .build();

        updateHelper.check(new OnUpdateListener() {
            @Override
            public void onStartCheck() {
                Log.e("liao", "onStartCheck");
            }

            @Override
            public void onFinishCheck(UpdateInfo info) {
                Log.e("liao", "onFinishCheck");
            }

            @Override
            public void onStartDownload() {
                Log.e("liao", "onStartDownload");
            }

            @Override
            public void onInstallApk() {
                Log.e("liao", "onInstallApk");
            }

            @Override
            public void onFinshDownload() {
                Log.e("liao", "onFinshDownload");
            }

            @Override
            public void onDownloading(int progress) {
//                        Log.e("liao","onDownloading:"+progress);
            }
        });
    }

    private void initView() {
        mTabs[0] = (Button) findViewById(R.id.btn_home_info);
        mTabs[1] = (Button) findViewById(R.id.btn_home_qw);
        mTabs[2] = (Button) findViewById(R.id.btn_home_order);
        mTabs[3] = (Button) findViewById(R.id.btn_home_server);
        mTabs[4] = (Button) findViewById(R.id.btn_home_me);

//        img_msg = (ImageView) findViewById(R.id.img_msg_home);
    }

    private void initCtrl() {
        FragmentManager fm = getSupportFragmentManager();
        //从回退栈获取，防止fragment重复创建
        homeFragment0 = fm.findFragmentByTag("0");
        if (homeFragment0 == null) {
            homeFragment0 = HomeInfoFragment.newInstance(0);
        }
        homeFragment1 = fm.findFragmentByTag("1");
        if (homeFragment1 == null) {
            homeFragment1 = HomeQwFragment.newInstance(1);
        }
        homeFragment2 = fm.findFragmentByTag("2");
        if (homeFragment2 == null) {
            homeFragment2 = HomeOrderFragment.newInstance(2);
        }
        homeFragment3 = fm.findFragmentByTag("3");
        if (homeFragment3 == null) {
            homeFragment3 = HomeServerFragment.newInstance(3);
        }
        homeFragment4 = fm.findFragmentByTag("4");
        if (homeFragment4 == null) {
            homeFragment4 = HomeMeFragment.newInstance(4);
        }
        fragments = new Fragment[]{homeFragment0, homeFragment1, homeFragment2, homeFragment3, homeFragment4};

//        img_msg.setOnClickListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent();
//        switch (item.getItemId()) {
//            case R.id.action_chat:
//                intent.setClass(this, ChatActivity.class);
//                startActivity(intent);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * tab点击事件
     */
    public void onTabClicked(View view) {
        Bundle args = new Bundle();
        int index = 0;
        switch (view.getId()) {
            case R.id.btn_home_info:
                index = 0;
                break;
            case R.id.btn_home_qw:
                index = 1;
                break;
            case R.id.btn_home_order:
                index = 2;
                break;
            case R.id.btn_home_server:
                index = 3;
                break;
            case R.id.btn_home_me:
                index = 4;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                fragments[index].setArguments(args);
                trx.add(R.id.fragment_container, fragments[index], index + "");
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;

        List fragmentss = getSupportFragmentManager().getFragments();
        if (fragmentss != null)
            Log.e("liao", fragmentss.size() + "");
    }

    private long exitTime;

    @Override
    public void onBackPressed() {
        //双击退出
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_medetail:
                Intent intent = new Intent(this, MsgActivity.class);
//                Intent intent = new Intent(this, CountOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

    private int signDays;

    private void netIsSign() {
        RequestParams params = new RequestParams(AppData.Url.getUserSign);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "错误:返回数据为空");
                else {
                    CommonEntity com = (CommonEntity) pojo;
                    boolean isSign = com.getIsSign() == 0 ? true : false;
                    signDays = com.getSignDays();
                    if (!isSign) {
                        dialogSure.show();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void netSign() {
        RequestParams params = new RequestParams(AppData.Url.sign);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "错误:返回数据为空");
                else {
                    Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(AppConstant.makeFlagStr(AppConstant.FLAG_UPDATE_ME_SIGN, signDays + 1 + ""));
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
