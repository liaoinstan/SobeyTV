package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class RegistPhoneFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;

    private ViewPager fatherpager;

    private CircularProgressButton btn_go;
    private EditText edit_phone;
    private EditText edit_vali;
    private TextView text_getvali;

    public void setFatherpager(ViewPager fatherpager){
        this.fatherpager = fatherpager;
    }

    public static Fragment newInstance(int position) {
        RegistPhoneFragment f = new RegistPhoneFragment();
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
        rootView = inflater.inflate(R.layout.fragment_regist_phone, container, false);
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
        edit_phone = (EditText) getView().findViewById(R.id.edit_regist_phone);
        edit_vali = (EditText) getView().findViewById(R.id.edit_regist_vali);
        text_getvali = (TextView) getView().findViewById(R.id.text_regist_getvali);
    }

    private void initData() {
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        text_getvali.setOnClickListener(this);

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
                                fatherpager.setCurrentItem(1);
                                btn_go.setProgress(0);
                            }
                        }, 800);
                    }
                }, 2000);
//                EventBus.getDefault().post("ahjaskdaksdjk哈哈");

                break;
            case R.id.text_regist_getvali:
                sendTimeMessage();
                text_getvali.setEnabled(false);
                break;
        }
    }

    ////////////////////////////
    //获取验证码计时

    private int time = 60;
    private final static int WHAT_TIME = 0;
    private void sendTimeMessage() {
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_TIME);
            mHandler.sendEmptyMessageDelayed(WHAT_TIME, 1000);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_TIME) {
                if (time>0) {
                    text_getvali.setText(""+time);
                    time--;
                    RegistPhoneFragment.this.sendTimeMessage();
                }else {
                    text_getvali.setEnabled(true);
                    text_getvali.setText("获取验证码");
                    time = 60;
                }
            }
        }
    };
}
