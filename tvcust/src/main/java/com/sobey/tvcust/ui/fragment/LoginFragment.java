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
import com.sobey.common.common.CommonNet;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.MD5Util;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.activity.HomeActivity;
import com.sobey.tvcust.ui.activity.FindPswActivity;

import org.xutils.http.RequestParams;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener,CommonNet.NetHander{

    private int position;
    private View rootView;

    private EditText edit_name;
    private EditText edit_password;
    private CircularProgressButton btn_go;
    private TextView text_regist;
    private TextView text_modifypsw;

    private String registrationID;

    private ViewPager fatherPager;
    public void setFatherPager(ViewPager fatherPager){
        this.fatherPager = fatherPager;
    }

    public static Fragment newInstance(int position) {
        LoginFragment f = new LoginFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");

        registrationID = JPushInterface.getRegistrationID(ApplicationHelp.getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
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
        edit_name = (EditText) getView().findViewById(R.id.edit_login_name);
        edit_password = (EditText) getView().findViewById(R.id.edit_login_password);
        btn_go = (CircularProgressButton) getView().findViewById(R.id.btn_go);
        text_regist = (TextView) getView().findViewById(R.id.text_login_regist);
        text_modifypsw = (TextView) getView().findViewById(R.id.text_login_findpsw);
    }

    private void initData() {
    }

    private void initCtrl() {
        btn_go.setOnClickListener(this);
        text_regist.setOnClickListener(this);
        text_modifypsw.setOnClickListener(this);

        btn_go.setIndeterminateProgressMode(true);
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go:

                String name = edit_name.getText().toString();
                String password = edit_password.getText().toString();

                String msg = AppVali.login_go(name,password);
                if (msg == null) {
                    RequestParams params = new RequestParams(AppData.Url.login);
                    params.addBodyParameter("mobile", name);
                    params.addBodyParameter("password", MD5Util.md5(password));
                    params.addBodyParameter("deviceToken", registrationID);
                    params.addBodyParameter("deviceType", "0");
                    CommonNet.post(this, params, 1, User.class, null);

                    btn_go.setProgress(50);
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }

//                if (btn_go.getProgress() == 0) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            btn_go.setProgress(100);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    intent.setClass(getActivity(), HomeActivity.class);
//                                    startActivity(intent);
//                                    getActivity().finish();
//                                }
//                            }, 800);
//                        }
//                    }, 2000);
//                }
//                if (btn_go.getProgress() == 100) {
//                    btn_go.setProgress(0);
//                }
                break;
            case R.id.text_login_regist:
                fatherPager.setCurrentItem(1);
                break;
            case R.id.text_login_findpsw:
                intent.setClass(getActivity(), FindPswActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
    }

    @Override
    public void netGo(int code, Object pojo, String text, Object obj) {
        if (pojo==null) netSetError(code,"接口异常");
        else {
            User user = (User) pojo;
            AppData.App.removeUser();
            AppData.App.saveToken(user.getToken());
            AppData.App.saveUser(user);

            btn_go.setProgress(100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }, 800);
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
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        btn_go.setProgress(-1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_go.setProgress(0);
            }
        }, 800);
    }

    @Override
    public void netException(int code, String text) {

    }
}
