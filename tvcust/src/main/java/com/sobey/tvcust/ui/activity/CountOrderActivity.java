package com.sobey.tvcust.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
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
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.CountEntity;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterCountOrder;
import com.sobey.tvcust.ui.dialog.DialogMouthPicker;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CountOrderActivity extends BaseAppCompatActicity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<CountEntity> results = new ArrayList<>();
    private RecycleAdapterCountOrder adapter;
    private ViewGroup showingroup;
    private View showin;

    private PieChart chart_finish;
    private PieChart chart_unfinish;
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
        setContentView(R.layout.activity_countorder);
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
        if (dialog != null) dialog.dismiss();
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

        chart_finish = (PieChart) findViewById(R.id.chat_finish);
        chart_unfinish = (PieChart) findViewById(R.id.chat_unfinish);
        btn_bank = findViewById(R.id.btn_countorder_bank);
        btn_next = findViewById(R.id.btn_countorder_next);
        text_time = (TextView) findViewById(R.id.text_countorder_time);

        String datestr = TimeUtil.getTimeFor(format, new Date());
        yearM = TimeUtil.getStrByStr(format, "yyyyMM", datestr);
        text_time.setText(datestr);
    }

    private void initData() {
        RequestParams params = new RequestParams(AppData.Url.countOrdersMonth);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("yearM",yearM);
        CommonNet.samplepost(params,CommonEntity.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null){
                    netSetError(code,"错误:返回数据为空");
                }else {
                    CommonEntity com = (CommonEntity) pojo;

                    if (com.getFinished()!=0||com.getNonFinished()!=0) {
                        results.clear();
                        results.add(new CountEntity("已完成", com.getFinished(), colors.get(0)));
                        results.add(new CountEntity("未完成", com.getNonFinished(), colors.get(1)));

                        IPieDataSet dataSet_finish = chart_finish.getData().getDataSet();
                        dataSet_finish.clear();
                        dataSet_finish.addEntry(new PieEntry(com.getFinished()));
                        dataSet_finish.addEntry(new PieEntry(com.getNonFinished()));

                        IPieDataSet dataSet_unfinish = chart_unfinish.getData().getDataSet();
                        dataSet_unfinish.clear();
                        dataSet_unfinish.addEntry(new PieEntry(com.getNonFinished()));
                        dataSet_unfinish.addEntry(new PieEntry(com.getFinished()));

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
                Toast.makeText(CountOrderActivity.this,text, Toast.LENGTH_SHORT).show();
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
        adapter = new RecycleAdapterCountOrder(this, R.layout.item_recycle_countorder, results);
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
        setChartData(chart_finish);
        setChartData(chart_unfinish);
    }

    private void initChat() {
        chart_finish.setUsePercentValues(true);
        chart_unfinish.setUsePercentValues(true);
        chart_finish.setDescription("");
        chart_unfinish.setDescription("");
        chart_finish.setExtraOffsets(5, 10, 5, 5);
        chart_unfinish.setExtraOffsets(5, 10, 5, 5);

        chart_finish.setDragDecelerationFrictionCoef(0.95f);
        chart_unfinish.setDragDecelerationFrictionCoef(0.95f);

//        chart_finish.setCenterText(generateCenterSpannableText());
//        chart_unfinish.setCenterText(generateCenterSpannableText());

//        chart_finish.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
//        chart_unfinish.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart_finish.setDrawHoleEnabled(true);
        chart_unfinish.setDrawHoleEnabled(true);
        chart_finish.setHoleColor(Color.WHITE);
        chart_unfinish.setHoleColor(Color.WHITE);

        chart_finish.setTransparentCircleColor(Color.WHITE);
        chart_unfinish.setTransparentCircleColor(Color.WHITE);
        chart_finish.setTransparentCircleAlpha(110);
        chart_unfinish.setTransparentCircleAlpha(110);

        chart_finish.setHoleRadius(85f);
        chart_unfinish.setHoleRadius(85f);
        chart_finish.setTransparentCircleRadius(61f);
        chart_unfinish.setTransparentCircleRadius(61f);

        chart_finish.setDrawCenterText(true);
        chart_unfinish.setDrawCenterText(true);

        chart_finish.setRotationAngle(0);
        chart_unfinish.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart_finish.setRotationEnabled(false);
        chart_unfinish.setRotationEnabled(false);
        chart_finish.setHighlightPerTapEnabled(false);
        chart_unfinish.setHighlightPerTapEnabled(false);

        chart_finish.getLegend().setEnabled(false);
        chart_unfinish.getLegend().setEnabled(false);
    }

    private void setChartData(PieChart chartV) {

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

        ////////
        dataSet.setDrawValues(false);
        chartV.setDrawEntryLabels(false);
        ////////

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(tf);

        chartV.setData(data);

        // undo all highlights
        chartV.highlightValues(null);

        chartV.invalidate();
    }

    public void freshCtrl() {
        adapter.notifyDataSetChanged();
        chart_finish.notifyDataSetChanged();
        chart_unfinish.notifyDataSetChanged();
        chart_finish.animateY(800, Easing.EasingOption.EaseInOutQuad);
        chart_unfinish.animateY(800, Easing.EasingOption.EaseInOutQuad);

        chart_finish.setCenterText(getCenterText(chart_finish.getData().getDataSet().getEntryForIndex(0).getValue() + "%", "已完成"));
        chart_unfinish.setCenterText(getCenterText(chart_unfinish.getData().getDataSet().getEntryForIndex(0).getValue() + "%", "未完成"));
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
        switch (v.getId()) {
            case R.id.btn_countorder_bank: {
                String datestr = text_time.getText().toString();
                String datelaststr = TimeUtil.add(format, datestr, Calendar.MONTH, -1);
                text_time.setText(datelaststr);
                yearM = TimeUtil.getStrByStr(format, "yyyyMM", datelaststr);
                initData();
                break;
            }
            case R.id.btn_countorder_next: {
                String datestr = text_time.getText().toString();
                String datelaststr = TimeUtil.add(format, datestr, Calendar.MONTH, 1);
                text_time.setText(datelaststr);
                yearM = TimeUtil.getStrByStr(format, "yyyyMM", datelaststr);
                initData();
                break;
            }
            case R.id.text_countorder_time:
                String datestr = text_time.getText().toString();
                Date date = TimeUtil.getDateByStr(format, datestr);
                dialog.setDate(date);
                dialog.show();
                break;
        }
    }

    private SpannableString getCenterText(String strPre, String strAft) {
        strPre += "\n";
        SpannableString s = new SpannableString(strPre + strAft);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, strPre.length(), 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sb_bk_dark)), 0, strPre.length(), 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sb_blue)), strPre.length(), s.length(), 0);
        return s;
    }
}
