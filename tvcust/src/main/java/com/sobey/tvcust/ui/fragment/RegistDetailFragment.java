package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.activity.CompActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class RegistDetailFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;

    private ViewPager sufatherPager;
    private ViewPager fatherPager;

    private CircularProgressButton btn_go;
    private TextView text_select_customer;
    private TextView text_select_employ;
    private EditText edit_name;
    private EditText edit_password;
    private EditText edit_password_repet;
    private EditText edit_mail;
    private EditText edit_comp;


    public void setFatherPager(ViewPager fatherPager){
        this.fatherPager = fatherPager;
    }

    public void setSufatherPager(ViewPager sufatherPager) {
        this.sufatherPager = sufatherPager;
    }

    public static Fragment newInstance(int position) {
        RegistDetailFragment f = new RegistDetailFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        edit_comp.setText(msg);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regist_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        btn_go = (CircularProgressButton) getView().findViewById(R.id.btn_go);
        text_select_customer = (TextView) getView().findViewById(R.id.text_registdetail_select_customer);
        text_select_employ = (TextView) getView().findViewById(R.id.text_registdetail_select_employ);
        edit_name = (EditText) getView().findViewById(R.id.edit_registdetail_name);
        edit_password = (EditText) getView().findViewById(R.id.edit_registdetail_password);
        edit_password_repet = (EditText) getView().findViewById(R.id.edit_registdetail_password_repet);
        edit_mail = (EditText) getView().findViewById(R.id.edit_registdetail_mail);
        edit_comp = (EditText) getView().findViewById(R.id.edit_registdetail_comp);
    }

    private void initData() {
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        text_select_customer.setSelected(true);
        text_select_customer.setOnClickListener(this);
        text_select_employ.setOnClickListener(this);
        edit_comp.setOnClickListener(this);

        btn_go.setIndeterminateProgressMode(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go:
                btn_go.setProgress(50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setProgress(100);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sufatherPager.setCurrentItem(0);
                                fatherPager.setCurrentItem(0,false);
                                btn_go.setProgress(0);
                            }
                        }, 800);
                    }
                }, 2000);

                break;
            case R.id.text_registdetail_select_customer:
                text_select_customer.setSelected(true);
                text_select_employ.setSelected(false);
                break;
            case R.id.text_registdetail_select_employ:
                text_select_customer.setSelected(false);
                text_select_employ.setSelected(true);
                break;
            case R.id.edit_registdetail_comp:
                intent.setClass(getActivity(), CompActivity.class);
                startActivity(intent);
                break;
        }
    }

}
