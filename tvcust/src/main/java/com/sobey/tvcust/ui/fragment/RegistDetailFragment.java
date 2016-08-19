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
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.utils.MD5Util;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.ui.activity.CompActivity;
import com.sobey.tvcust.ui.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class RegistDetailFragment extends BaseFragment implements View.OnClickListener,CommonNet.NetHander{

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

    public static String TYPE_USER = "user";        //客户
    public static String TYPE_GROUP_USER = "group_user";    //员工
    public static String TYPE_HEAD_USER = "head_user";      //员工中的总部技术
    private String type = TYPE_USER;
    private int officeId;


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
    public void onEventMainThread(CompEntity comp) {
        officeId = comp.getId();
        edit_comp.setText(comp.getCompName());
        if (comp.getId()==-1){
            type = TYPE_HEAD_USER;
        }
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
                btn_go.setClickable(false);
                String name = edit_name.getText().toString();
                String password = edit_password.getText().toString();
                String password_repet = edit_password_repet.getText().toString();
                String mail = edit_mail.getText().toString();

                String msg = AppVali.regist_detail(name,password,password_repet,mail,officeId);
                if (msg == null) {
                    RequestParams params = new RequestParams(AppData.Url.regist);
                    params.addBodyParameter("mobile", ((LoginActivity)getActivity()).getPhone());
                    params.addBodyParameter("password", MD5Util.md5(password));
                    params.addBodyParameter("realName", name);
                    params.addBodyParameter("email", mail);
                    if (officeId!=-1){
                        params.addBodyParameter("officeId", officeId +"");
                    }
                    params.addBodyParameter("type", type);
                    params.addBodyParameter("deviceType", "0");
                    CommonNet.post(this, params, 1, CommonEntity.class, null);

                    btn_go.setProgress(50);
                } else {
                    btn_go.setClickable(true);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.text_registdetail_select_customer:
                text_select_customer.setSelected(true);
                text_select_employ.setSelected(false);
                edit_comp.setText("");
                type = TYPE_USER;
                break;
            case R.id.text_registdetail_select_employ:
                text_select_customer.setSelected(false);
                text_select_employ.setSelected(true);
                edit_comp.setText("");
                type = TYPE_GROUP_USER;
                break;
            case R.id.edit_registdetail_comp:
                intent.setClass(getActivity(), CompActivity.class);
                intent.putExtra("type",getSelectType());
                startActivity(intent);
                break;
        }
    }

    private String getSelectType(){
        if (text_select_customer.isSelected()){
            return TYPE_USER;
        }else {
            return TYPE_GROUP_USER;
        }
    }

    @Override
    public void netGo(int code, Object pojo, String text, Object obj) {
        switch (code){
            case 1:{
                Toast.makeText(getActivity(),"小贝已收到您的申请，我们会尽快为您审核哒",Toast.LENGTH_SHORT).show();
                btn_go.setProgress(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sufatherPager.setCurrentItem(0);
                        fatherPager.setCurrentItem(0,false);
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);
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
        switch (code){
            case 1:{
                btn_go.setProgress(-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_go.setClickable(true);
                        btn_go.setProgress(0);
                    }
                }, 800);

                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void netException(int code, String text) {
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }

    public static class CompEntity {
        private Integer id;
        private String compName;

        public CompEntity(Integer id, String compName) {
            this.id = id;
            this.compName = compName;
        }

        public int getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }
    }
}
