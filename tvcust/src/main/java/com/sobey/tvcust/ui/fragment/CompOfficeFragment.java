package com.sobey.tvcust.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.common.common.CommonNet;
import com.sobey.common.view.SideBar;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CharacterParser;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.PinyinComparator;
import com.sobey.tvcust.entity.CarType;
import com.sobey.tvcust.entity.Office;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.Office;
import com.sobey.tvcust.entity.OfficePojo;
import com.sobey.tvcust.ui.activity.CompActivity;
import com.sobey.tvcust.ui.activity.LoginActivity;
import com.sobey.tvcust.ui.adapter.ListAdapterCompOffice;
import com.sobey.tvcust.ui.adapter.ListAdapterCompTVStation;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class CompOfficeFragment extends BaseFragment implements CommonNet.NetHander {

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private ListView sortListView;
    private ListAdapterCompOffice adapter;
    private EditText mClearEditText;

    private View text_search;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<Office> SourceDateList = new ArrayList<Office>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparatorOffice pinyinComparator;

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

    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        text_search = getView().findViewById(R.id.text_comp_search);
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading);

        RequestParams params = new RequestParams(AppData.Url.getOffice);
        CommonNet.post(this, params, 1, OfficePojo.class, null);
    }

    private void initCtrl() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparatorOffice();

        sortListView = (ListView) getView().findViewById(R.id.country_lvcountry);
        SourceDateList = filledData(SourceDateList);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new ListAdapterCompOffice(getActivity(), SourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompActivity activity = (CompActivity) getActivity();

                if(activity.getType().equals(RegistDetailFragment.TYPE_USER)){
                    EventBus.getDefault().post(new OfficeEntity(adapter.getItem(position).getId()));
                    activity.go();
                }else {
                    Office office = adapter.getItem(position);
                    RegistDetailFragment.CompEntity compEntity = new RegistDetailFragment.CompEntity(office.getId(), office.getCar_title());
                    EventBus.getDefault().post(compEntity);
                    getActivity().finish();
                }
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
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter.updateListView(SourceDateList);
//        adapter.notifyDataSetChanged();
    }

    /**
     * 为ListView填充数据
     *
     * @return
     */
    private List<Office> filledData(List<Office> data) {
        for (int i = 0; i < data.size(); i++) {
            Office sortModel = data.get(i);
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
        List<Office> filterDateList = new ArrayList<Office>();

        if (TextUtils.isEmpty(filterStr)) {
            for (Office carType : SourceDateList) {
                if (carType.getCar_title_html() != null) {
                    carType.setCar_title_html(null);
                }
            }
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (Office sortModel : SourceDateList) {
                String name = sortModel.getCar_title();
                match(filterDateList, sortModel, filterStr);
            }
        }

//        if (StrUtils.isEmpty(filterDateList)) {
//            showin = LoadingViewUtil.showinlack(this, R.drawable.icon_theme_search, "没有搜索结果", showin);
//        }else{
//            LoadingViewUtil.showout(this, showin);
//        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    /**
     * 匹配字符串
     *
     * @param sortModel
     * @param filterStr
     */
    private int matchText(Office sortModel, String filterStr) {
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

    private void match(List<Office> filterDateList, Office sortModel, String filterStr) {
        boolean isMatch = false;
        String car_title = sortModel.getCar_title();
        int sellingCount = matchText(sortModel, filterStr);
        if (sellingCount != 0) {
            isMatch = true;
            sortModel.setCar_title_html("<font color='#f08519'><b>" + car_title.substring(0, sellingCount) + "</b></font>" + car_title.substring(sellingCount));
        }

        int index = car_title.toLowerCase().indexOf(filterStr.toLowerCase().toString());
        int length = filterStr.length();
        if (index != -1) {
            isMatch = true;
            sortModel.setCar_title_html(car_title.substring(0, index) + "<font color='#f08519'><b>" + filterStr + "</b></font>" + car_title.substring(index + length));
        }

        if (isMatch) {
            filterDateList.add(sortModel);
        }
    }

    @Override
    public void netGo(int code, Object pojo, String text, Object obj) {
        OfficePojo officePojo = (OfficePojo) pojo;
        List<Office> offices = officePojo.getDataList();
        SourceDateList.clear();
        SourceDateList.addAll(offices);
//        for (Office office: offices) {
//            SourceDateList.add(new Office(office.getId(),office.getOfficeName()));
//        }

        freshCtrl();
        LoadingViewUtil.showout(showingroup, showin);
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
        LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
        });
    }

    @Override
    public void netException(int code, String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public class OfficeEntity {
        private int data;

        public OfficeEntity(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }

    public class PinyinComparatorOffice implements Comparator<Office> {

        public int compare(Office o1, Office o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }

    }
}
