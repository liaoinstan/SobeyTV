package com.sobey.tvcust.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sobey.common.utils.DensityUtil;
import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author Tom.Cai
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 */
public class DialogMouthPicker extends Dialog implements View.OnClickListener {

    private View root;
    private Date date;

    private Context context;
    private WheelView wheel_year;
    private WheelView wheel_mouth;
    //    private View dialog_cancel;
    private View dialog_ok;
    private List<String> datayears = new ArrayList<>();
    private List<String> datamouths = new ArrayList<>();

    public DialogMouthPicker(Context context) {
        super(context, R.style.PopupDialog);
        this.context = context;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        root = inflater.inflate(R.layout.dialog_mouthpicker, null);// 得到加载view

        initBase();
        initView();
        initData();
        initCtrl();

        Window win = this.getWindow();
        win.setGravity(Gravity.BOTTOM);    //从下方弹出
        this.setCanceledOnTouchOutside(true);
        super.setContentView(root);
    }

    private int color_select;

    private void initBase() {
        color_select = ContextCompat.getColor(context, R.color.sb_text_blank);
    }

    private void initView() {
        wheel_year = (WheelView) root.findViewById(R.id.wheel_year);
        wheel_mouth = (WheelView) root.findViewById(R.id.wheel_mouth);
//        dialog_cancel = root.findViewById(R.id.dialog_cancel);
        dialog_ok = root.findViewById(R.id.dialog_ok);
    }

    private void initData() {
        for (int i = 2015; i <= 2030; i++) {
            datayears.add(i + "年");
        }
        for (int i = 1; i <= 12; i++) {
            datamouths.add(i + "月");
        }
    }

    private void initCtrl() {
//        dialog_cancel.setOnClickListener(this);
        dialog_ok.setOnClickListener(this);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = color_select;//Color.parseColor("#0288ce");
        style.holoBorderColor = Color.parseColor("#00cccccc");
        style.textAlpha = 0.5f;
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;

        wheel_year.setWheelAdapter(new ArrayWheelAdapter(context));
        wheel_year.setSkin(WheelView.Skin.Holo);
        wheel_year.setWheelData(datayears);
        wheel_year.setStyle(style);
        wheel_year.setWheelSize(5);
//        wheel_year.setExtraText("年", color_select, DensityUtil.sp2px(context, 20), DensityUtil.dp2px(context, 50));

        wheel_mouth.setWheelAdapter(new ArrayWheelAdapter(context));
        wheel_mouth.setSkin(WheelView.Skin.Holo);
        wheel_mouth.setWheelData(datamouths);
        wheel_mouth.setStyle(style);
        wheel_mouth.setWheelSize(5);
//        wheel_mouth.setExtraText("月", color_select, DensityUtil.sp2px(context, 20), DensityUtil.dp2px(context, 35));
    }


    /////////////////
    public void setDate(Date date) {
        this.date = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mouth = cal.get(Calendar.MONTH) + 1;
        System.out.println(cal.get(Calendar.YEAR));
        System.out.println(cal.get(Calendar.MONTH) + 1);
        int yearIndex = 0;
        int mouthIndex = 0;
        for (int i = 0; i < datayears.size(); i++) {
            if (datayears.get(i).startsWith("" + year)) {
                yearIndex = i;
            }
        }
        for (int i = 0; i < datamouths.size(); i++) {
            if (datamouths.get(i).startsWith("" + mouth)) {
                mouthIndex = i;
            }
        }
        wheel_year.setSelection(yearIndex);
        wheel_mouth.setSelection(mouthIndex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;
            case R.id.dialog_ok:
                if (onOKlistener != null) {
                    String selectYear = getNumStr(datayears.get(wheel_year.getCurrentPosition()));
                    String selectMouth = getNumStr(datamouths.get(wheel_mouth.getCurrentPosition()));
                    Date datesele = TimeUtil.getDateByStr("yyyy/MM", selectYear + "/" + selectMouth);
                    onOKlistener.onOkClick(datesele);
                }
                dismiss();
                break;
        }
    }

    private String getNumStr(String str){
        return str.substring(0, str.length() - 1);
    }

    private OnOkListener onOKlistener;

    public void setOnOKlistener(OnOkListener onOKlistener) {
        this.onOKlistener = onOKlistener;
    }

    public interface OnOkListener {
        void onOkClick(Date date);
    }
}
