package com.sobey.tvcust.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CountEntity;
import com.sobey.tvcust.entity.CountPojo;
import com.sobey.tvcust.ui.adapter.RecycleAdapterCountOrder;
import com.sobey.tvcust.ui.dialog.DialogMouthPicker;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CountQuestionActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private List<CountEntity> results = new ArrayList<>();
    private RecycleAdapterCountOrder adapter;
    private ViewGroup showingroup;
    private View showin;

    private PieChart chartView;
    private View btn_bank;
    private View btn_next;
    private TextView text_time;

    private DialogMouthPicker dialog;

    private String format = "yyyy年MM月";
    private String yearM;

    ArrayList<Integer> colors = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countquestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null) dialog.dismiss();
    }

    private void initBase() {
        dialog = new DialogMouthPicker(this);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        chartView = (PieChart) findViewById(R.id.chat_countorder);
        btn_bank = findViewById(R.id.btn_countorder_bank);
        btn_next = findViewById(R.id.btn_countorder_next);
        text_time = (TextView) findViewById(R.id.text_countorder_time);

        String datestr = TimeUtil.getTimeFor(format, new Date());
        yearM = TimeUtil.getStrByStr(format, "yyyyMM", datestr);
        text_time.setText(datestr);
    }

    private void initData() {
        RequestParams params = new RequestParams(AppData.Url.countOrderCategory);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("yearM",yearM);
        CommonNet.samplepost(params,CountPojo.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null){
                    netSetError(code,"错误:返回数据为空");
                }else {
                    CountPojo com = (CountPojo)pojo;

                    Map<String, Integer> dataList = com.getDataList();
                    List<CountEntity> counts = getCountListFromMap(dataList);

                    if (counts!=null && counts.size()!=0) {
                        results.clear();
                        results.addAll(counts);

                        IPieDataSet dataSet = chartView.getData().getDataSet();
                        dataSet.clear();

                        for (CountEntity count : counts) {
                            dataSet.addEntry(new PieEntry(count.getValue()/**count.getName()**/));
                        }

                        freshCtrl();

                        LoadingViewUtil.showout(showingroup, showin);
                    }else {
                        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initData();
                            }
                        });
                    }
                }
            }
            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(CountQuestionActivity.this,text, Toast.LENGTH_SHORT).show();
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }

            @Override
            public void netStart(int code) {
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin, false);
            }
        });
    }

    private void initCtrl() {
        adapter = new RecycleAdapterCountOrder(this,R.layout.item_recycle_countorder,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);

        btn_bank.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        text_time.setOnClickListener(this);
        dialog.setOnOKlistener(new DialogMouthPicker.OnOkListener() {
            @Override
            public void onOkClick(Date date) {
                String strdate = TimeUtil.getTimeFor("yyyy年MM月", date);
                text_time.setText(strdate);
                yearM = TimeUtil.getStrByStr(format, "yyyyMM", strdate);
                initData();
            }
        });
        //////////////////////////////

        initChat();
        setChartData();
    }

    private void initChat(){
        chartView.setUsePercentValues(true);
        chartView.setDescription("");
        chartView.setExtraOffsets(5, 10, 5, 5);

        chartView.setDragDecelerationFrictionCoef(0.95f);

//        chartView.setCenterText(generateCenterSpannableText());

        chartView.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chartView.setDrawHoleEnabled(true);
        chartView.setHoleColor(Color.WHITE);

        chartView.setTransparentCircleColor(Color.WHITE);
        chartView.setTransparentCircleAlpha(110);

        chartView.setHoleRadius(58f);
        chartView.setTransparentCircleRadius(61f);

        chartView.setDrawCenterText(false);

        chartView.setRotationAngle(0);
        // enable rotation of the chart by touch
        chartView.setRotationEnabled(true);
        chartView.setHighlightPerTapEnabled(true);

//        chartView.setOnChartValueSelectedListener(this);

        Legend l = chartView.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setEnabled(false);
    }

    private void setChartData() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
//         dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(tf);
        chartView.setData(data);

        // undo all highlights
        chartView.highlightValues(null);

        chartView.invalidate();
    }

    public void freshCtrl(){
        adapter.notifyDataSetChanged();
        chartView.notifyDataSetChanged();
        chartView.animateY(800, Easing.EasingOption.EaseInOutQuad);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_countorder_bank: {
                String datestr = text_time.getText().toString();
                String datelaststr = TimeUtil.add("yyyy年MM月", datestr, Calendar.MONTH, -1);
                text_time.setText(datelaststr);
                yearM = TimeUtil.getStrByStr(format, "yyyyMM", datelaststr);
                initData();
                break;
            }
            case R.id.btn_countorder_next: {
                String datestr = text_time.getText().toString();
                String datelaststr = TimeUtil.add("yyyy年MM月", datestr, Calendar.MONTH, 1);
                text_time.setText(datelaststr);
                yearM = TimeUtil.getStrByStr(format, "yyyyMM", datelaststr);
                initData();
                break;
            }
            case R.id.text_countorder_time:
                String datestr = text_time.getText().toString();
                Date date = TimeUtil.getDateByStr("yyyy年MM月", datestr);
                dialog.setDate(date);
                dialog.show();
                break;
        }
    }

    private List<CountEntity> getCountListFromMap(Map<String,Integer> map){
        ArrayList<CountEntity> counts = new ArrayList<>();
        if (map != null && map.size() > 0) {
            int i= 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();
                counts.add(new CountEntity(key,value,colors.get(i%colors.size())));
                i++;
            }
        }
        return counts;
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("Sobey\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
}
