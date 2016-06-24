package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.fragment.BuildFragment;
import com.sobey.tvcust.ui.fragment.HomeInfoFragment;
import com.sobey.tvcust.ui.fragment.HomeQwFragment;
import com.sobey.tvcust.ui.fragment.HomeMeFragment;
import com.sobey.tvcust.ui.fragment.HomeOrderFragment;
import com.sobey.tvcust.ui.fragment.HomeServerFragment;
import com.sobey.tvcust.utils.PermissionsUtil;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTabIndex", currentTabIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            currentTabIndex = savedInstanceState.getInt("currentTabIndex");
        }
        setContentView(R.layout.activity_home);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        PermissionsUtil.checkAndRequestPermissions(this,R.id.coordinator);

        initBase();
        initView();
        initCtrl();

        if (currentTabIndex==-1){
            currentTabIndex = 2;
        }
        mTabs[currentTabIndex].setSelected(true);
        // 添加显示第一个fragment
        FragmentTransaction ftx = getSupportFragmentManager().beginTransaction();
        hideAllFragment(ftx);
        if (!fragments[currentTabIndex].isAdded()) {
            ftx.add(R.id.fragment_container, fragments[currentTabIndex],currentTabIndex+"");
        }else {
            ftx.show(fragments[currentTabIndex]);
        }
        ftx.commit();
        List fragmentss =getSupportFragmentManager().getFragments();
        if (fragmentss!=null)
        Log.e("liao",fragmentss.size()+"");
    }

    private void hideAllFragment(FragmentTransaction ftx){
        for (int i=0;i<fragments.length;i++){
            if (fragments[i].isAdded()){
                ftx.hide(fragments[i]);
            }
        }
    }

    private void initBase() {
        mTabs = new Button[5];
    }

    private void initView() {
        mTabs[0] = (Button) findViewById(R.id.btn_home_info);
        mTabs[1] = (Button) findViewById(R.id.btn_home_server);
        mTabs[2] = (Button) findViewById(R.id.btn_home_qw);
        mTabs[3] = (Button) findViewById(R.id.btn_home_order);
        mTabs[4] = (Button) findViewById(R.id.btn_home_me);

//        img_msg = (ImageView) findViewById(R.id.img_msg_home);
    }

    private void initCtrl() {
        FragmentManager fm= getSupportFragmentManager();
        //从回退栈获取，防止fragment重复创建
        homeFragment0 = fm.findFragmentByTag("0");
        if (homeFragment0 == null){
            homeFragment0 = HomeInfoFragment.newInstance(0);
        }
        homeFragment1 = fm.findFragmentByTag("1");
        if (homeFragment1 == null){
            homeFragment1 = HomeServerFragment.newInstance(1);
        }
        homeFragment2 = fm.findFragmentByTag("2");
        if (homeFragment2 == null){
            homeFragment2 = HomeQwFragment.newInstance(2);
        }
        homeFragment3 = fm.findFragmentByTag("3");
        if (homeFragment3 == null){
            homeFragment3 = HomeOrderFragment.newInstance(3);
        }
        homeFragment4 = fm.findFragmentByTag("4");
        if (homeFragment4 == null){
            homeFragment4 = HomeMeFragment.newInstance(4);
        }
        fragments = new Fragment[] { homeFragment0, homeFragment1, homeFragment2,homeFragment3,homeFragment4 };

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
            case R.id.btn_home_server:
                index = 1;
                //传入参数
//                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//                args.putString(EaseConstant.EXTRA_USER_ID, "liaoinstan");
                break;
            case R.id.btn_home_qw:
                index = 2;
                break;
            case R.id.btn_home_order:
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
                trx.add(R.id.fragment_container, fragments[index],index+"");
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;

        List fragmentss =getSupportFragmentManager().getFragments();
        if (fragmentss!=null)
            Log.e("liao",fragmentss.size()+"");
    }

    private long exitTime;
    @Override
    public void onBackPressed() {
        //双击退出
        if ((System.currentTimeMillis() - exitTime) > 2000){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else{
            super.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_go_msg:
                Intent intent = new Intent(this, MsgActivity.class);
//                Intent intent = new Intent(this, CountOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

//    public void setToolbarLight(){
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.white)));
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.sb_text_blank));
//    }
//    public void setToolbarDark(){
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.sb_bk_dark)));
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.sb_text_light));
//    }
}
