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
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.ui.activity.LoginActivity;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class RegistPhoneFragment extends BaseFragment implements View.OnClickListener, CommonNet.NetHander {

    private int position;
    private View rootView;

    private ViewPager fatherpager;

    private CircularProgressButton btn_go;
    private EditText edit_phone;
    private EditText edit_vali;
    private TextView text_getvali;

    private String valicode;

    public void setFatherpager(ViewPager fatherpager) {
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
        switch (v.getId()) {
            case R.id.btn_go:
                btn_go.setClickable(false);
                btn_go.setProgress(50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String phone = ((LoginActivity)getActivity()).getPhone();
                        String vali = edit_vali.getText().toString();

                        String msg = AppVali.regist_phone(edit_phone.getText().toString(),phone, vali, valicode);
                        if (msg==null) {
                            btn_go.setProgress(100);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    valicode = "";
                                    fatherpager.setCurrentItem(1);
                                    btn_go.setClickable(true);
                                    btn_go.setProgress(0);
                                }
                            }, 800);
                        }else {
                            btn_go.setProgress(-1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_go.setClickable(true);
                                    btn_go.setProgress(0);
                                }
                            }, 800);
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);

                break;
            case R.id.text_regist_getvali:
                String phone = edit_phone.getText().toString();
                String msg = AppVali.regist_vali(phone);
                if (msg == null) {
                    RequestParams params = new RequestParams(AppData.Url.getvali);
                    params.addBodyParameter("mobile", phone);
                    CommonNet.post(this, params, 1, CommonEntity.class, null);
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_TIME) {
                if (time > 0) {
                    text_getvali.setText("" + time);
                    time--;
                    RegistPhoneFragment.this.sendTimeMessage();
                } else {
                    text_getvali.setEnabled(true);
                    text_getvali.setText("获取验证码");
                    time = 60;
                }
            }
        }
    };

    @Override
    public void netGo(int code, Object pojo, String text, Object obj) {
        switch (code) {
            case 1: {
                if (pojo==null) netSetError(code,"接口异常");
                else {
                    CommonEntity common = (CommonEntity) pojo;
                    valicode = common.getValicode();
                    ((LoginActivity) getActivity()).setPhone(edit_phone.getText().toString());

                    time = 60;
                    sendTimeMessage();
                    text_getvali.setEnabled(false);
                }
                break;
            }
        }
    }

    @Override
    public void netStart(int code) {

    }

    @Override
    public void netEnd(int code) {

    }

    @Override
    public void netSetFalse(int code, int status, String text) {

    }

    @Override
    public void netSetFail(int code, int errorCode, String text) {

    }

    @Override
    public void netSetError(int code, String text) {
        switch (code) {
            case 1: {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                time = 0;
                break;
            }
        }
    }

    @Override
    public void netException(int code, String text) {
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}
