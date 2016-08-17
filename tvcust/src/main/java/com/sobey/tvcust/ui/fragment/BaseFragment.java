package com.sobey.tvcust.ui.fragment;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class BaseFragment extends Fragment{
    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("MainScreen");
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd("MainScreen");
    }
}
