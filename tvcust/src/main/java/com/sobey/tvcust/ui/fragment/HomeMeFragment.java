package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.ui.activity.CountOrderActivity;
import com.sobey.tvcust.ui.activity.LoginActivity;
import com.sobey.tvcust.ui.activity.MeDetailActivity;
import com.sobey.tvcust.ui.activity.SettingActivity;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeMeFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private View item_me_order;
    private View item_me_question;
    private View item_me_warning;
    private View item_me_setting;
    private View btn_go_medetail;

    public static Fragment newInstance(int position) {
        HomeMeFragment f = new HomeMeFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData();
        //initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        item_me_order = getView().findViewById(R.id.item_me_order);
        item_me_question = getView().findViewById(R.id.item_me_question);
        item_me_warning = getView().findViewById(R.id.item_me_warning);
        item_me_setting = getView().findViewById(R.id.item_me_setting);

        btn_go_medetail = getView().findViewById(R.id.btn_go_medetail);
        btn_go_medetail.setOnClickListener(this);

        getView().findViewById(R.id.btn_go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading,false);
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
        }, 2000);
    }

    private void initCtrl() {
        item_me_order.setOnClickListener(this);
        item_me_question.setOnClickListener(this);
        item_me_warning.setOnClickListener(this);
        item_me_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go_medetail:
                intent.setClass(getActivity(), MeDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_order:
                intent.setClass(getActivity(), CountOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_question:
                break;
            case R.id.item_me_warning:
                break;
            case R.id.item_me_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
