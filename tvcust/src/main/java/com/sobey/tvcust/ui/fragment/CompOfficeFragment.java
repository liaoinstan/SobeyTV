package com.sobey.tvcust.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CharacterParser;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.interfaces.CharSort;
import com.sobey.tvcust.entity.Company;
import com.sobey.tvcust.entity.Office;
import com.sobey.tvcust.entity.OfficePojo;
import com.sobey.tvcust.interfaces.ActivityGo;
import com.sobey.tvcust.ui.adapter.ListAdapterComp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class CompOfficeFragment extends BaseFragment{

    private int position;

    private View rootView;
    private ViewGroup showingroup;
    private View showin;
    private ActivityGo activityGo;

    private ListView sortListView;
    private ListAdapterComp adapter;
    private EditText mClearEditText;

    private int companyId;

    private View text_search;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<CharSort> SourceDateList = new ArrayList<CharSort>();

    public static Fragment newInstance(int position) {
        CompOfficeFragment f = new CompOfficeFragment();
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

    @Subscribe
    public void onEventMainThread(Company company) {
        companyId = company.getId();
        initData();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_comp_office, container, false);
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
        activityGo = (ActivityGo) getActivity();
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        text_search = getView().findViewById(R.id.text_comp_search);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading,showin);

        RequestParams params = new RequestParams(AppData.Url.getOffice);
        if (RegistDetailFragment.TYPE_GROUP_USER.equals(activityGo.getType())){
            //员工才需要公司id
            params.addBodyParameter("companyId",companyId+"");
        }
        CommonNet.samplepost(params,OfficePojo.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null) netSetError(code,"接口异常");
                else {
                    OfficePojo officePojo = (OfficePojo) pojo;
                    List<Office> offices = officePojo.getDataList();
                    SourceDateList.clear();
                    SourceDateList.addAll(offices);
                    freshCtrl();
                    LoadingViewUtil.showout(showingroup, showin);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                showin = LoadingViewUtil.showin(showingroup,R.layout.layout_fail,showin,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        });
    }

    private void initCtrl() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        sortListView = (ListView) getView().findViewById(R.id.country_lvcountry);
        SourceDateList = filledData(SourceDateList);

//        boolean needNextButton;
//        if (RegistDetailFragment.TYPE_GROUP_USER.equals(activityGo.getType())){
//            needNextButton = false;
//        }else {
//            needNextButton = true;
//        }
        adapter = new ListAdapterComp(getActivity(), SourceDateList,false);
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (RegistDetailFragment.TYPE_GROUP_USER.equals(activityGo.getType())){
                    //员工才能选择到办事处
                    CharSort charSort = adapter.getResults().get(pos);
                    EventBus.getDefault().post(new RegistDetailFragment.CompEntity(charSort.getId(),charSort.getCar_title(),RegistDetailFragment.TYPE_GROUP_USER));
                    getActivity().finish();
                }else {
                    //用户必须前往下一级
                    goNext(pos);
                }
            }
        });
        adapter.setOnNextClickListenner(new ListAdapterComp.OnNextClickListenner() {
            @Override
            public void onNextClick(int pos) {
                goNext(pos);
            }
        });

        mClearEditText = (EditText) getView().findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());

                if ("".equals(s.toString())) {
                    text_search.setVisibility(View.VISIBLE);
                } else {
                    text_search.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void freshCtrl() {
        SourceDateList = filledData(SourceDateList);
        adapter.updateListView(SourceDateList);
    }


    private void goNext(int pos){
        CharSort charSort = adapter.getResults().get(pos);
        EventBus.getDefault().post(charSort);
        if (activityGo.getTitles().length - 1 >= position + 1) {
            activityGo.getTitles()[position + 1] = charSort.getCar_title();
        }
        activityGo.next();
    }

    /**
     * 为ListView填充数据
     *
     * @return
     */
    private List<CharSort> filledData(List<CharSort> data) {
        for (int i = 0; i < data.size(); i++) {
            CharSort sortModel = data.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(data.get(i).getCar_title());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
        }
        return data;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CharSort> filterDateList = new ArrayList<CharSort>();

        if (TextUtils.isEmpty(filterStr)) {
            for (CharSort carType : SourceDateList) {
                if (carType.getCar_title_html() != null) {
                    carType.setCar_title_html(null);
                }
            }
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (CharSort sortModel : SourceDateList) {
                String name = sortModel.getCar_title();
                match(filterDateList, sortModel, filterStr);
            }
        }

        adapter.updateListView(filterDateList);
    }

    /**
     * 匹配字符串
     *
     * @param sortModel
     * @param filterStr
     */
    private int matchText(CharSort sortModel, String filterStr) {
        int sellingcount = 0;
        String name = sortModel.getCar_title();
        String[] sellingArray = characterParser.getSellingArray(name);
        for (String selling : sellingArray) {
            if ("".equals(filterStr)) {
                return sellingcount;
            }
            if (filterStr.startsWith(selling)) {
                sellingcount++;
                filterStr = filterStr.substring(selling.length(), filterStr.length());
            } else if (selling.startsWith(filterStr)) {
                sellingcount++;
                return sellingcount;
            } else {
                return 0;
            }
        }
        return sellingcount;
    }

    private void match(List<CharSort> filterDateList, CharSort sortModel, String filterStr) {
        boolean isMatch = false;
        String car_title = sortModel.getCar_title();
        int sellingCount = matchText(sortModel, filterStr);
        if (sellingCount != 0) {
            isMatch = true;
            sortModel.setCar_title_html("<font color='#5793e6'><b>" + car_title.substring(0, sellingCount) + "</b></font>" + car_title.substring(sellingCount));
        }

        int index = car_title.toLowerCase().indexOf(filterStr.toLowerCase().toString());
        int length = filterStr.length();
        if (index != -1) {
            isMatch = true;
            sortModel.setCar_title_html(car_title.substring(0, index) + "<font color='#5793e6'><b>" + filterStr + "</b></font>" + car_title.substring(index + length));
        }

        if (isMatch) {
            filterDateList.add(sortModel);
        }
    }

//    public class OfficeEntity {
//        private int data;
//
//        public OfficeEntity(int data) {
//            this.data = data;
//        }
//
//        public int getData() {
//            return data;
//        }
//
//        public void setData(int data) {
//            this.data = data;
//        }
//    }
}
