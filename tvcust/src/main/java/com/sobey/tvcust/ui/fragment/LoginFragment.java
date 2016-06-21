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

import com.dd.CircularProgressButton;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.ui.activity.ModifyPswActivity;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;

    private EditText edit_name;
    private EditText edit_password;
    private CircularProgressButton btn_go;
    private TextView text_regist;
    private TextView text_modifypsw;

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
        text_modifypsw = (TextView) getView().findViewById(R.id.text_login_modifypsw);
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
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go:
                if (btn_go.getProgress() == 0) {
                    btn_go.setProgress(50);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_go.setProgress(100);
                        }
                    }, 2000);
                }
                if (btn_go.getProgress() == 100) {
                    btn_go.setProgress(0);
                }
                break;
            case R.id.text_login_regist:
                fatherPager.setCurrentItem(1);
                break;
            case R.id.text_login_modifypsw:
                intent.setClass(getActivity(), ModifyPswActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
    }
}
