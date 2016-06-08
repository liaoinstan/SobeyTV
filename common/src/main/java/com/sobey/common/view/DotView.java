package com.sobey.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class DotView extends LinearLayout{

    private Context context;
    private ViewPager pager;
    private int tabCount;

    private int dot_size = 13;
    private GradientDrawable mUnSelectedGradientDrawable;
    private GradientDrawable mSelectedGradientDrawable;
    private int DEFAULT_BANNER_SIZE = -1;

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mSelectedGradientDrawable = new GradientDrawable();
        mUnSelectedGradientDrawable = new GradientDrawable();
        mSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
        mUnSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
        mSelectedGradientDrawable.setSize(dot_size, dot_size);
        mUnSelectedGradientDrawable.setSize(dot_size, dot_size);
        mSelectedGradientDrawable.setColor(Color.rgb(255, 255, 255));
        mUnSelectedGradientDrawable.setColor(Color.argb(33, 255, 255, 255));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setOrientation(HORIZONTAL);
        if (isInEditMode()) {
            addDots(5);
        }
    }

    private void addDots(int connt) {
        for (int i = 0; i < connt; i++) {
            ImageView imageView = new ImageView(context);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 10;
            imageView.setLayoutParams(lp);
            if (i==0){
                imageView.setImageDrawable(mSelectedGradientDrawable);
            }else {
                imageView.setImageDrawable(mUnSelectedGradientDrawable);
            }

            addView(imageView);
        }
    }

    private void setIndicator(int position) {
        for (int i=0;i<getChildCount();i++){
            ImageView child = (ImageView)getChildAt(i);
            if (position == i){
                child.setImageDrawable(mSelectedGradientDrawable);
            }else {
                child.setImageDrawable(mUnSelectedGradientDrawable);
            }
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            if (DEFAULT_BANNER_SIZE != -1) {
                position %= DEFAULT_BANNER_SIZE;
            }
            setIndicator(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //###########################################
    //###对外暴露方法
    //###########################################
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(new MyOnPageChangeListener());
        notifyDataSetChanged();
    }

    public void setViewPager(ViewPager pager, int DEFAULT_BANNER_SIZE) {
        this.DEFAULT_BANNER_SIZE = DEFAULT_BANNER_SIZE;
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(new MyOnPageChangeListener());
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        removeAllViews();
        if (DEFAULT_BANNER_SIZE != -1) {
            tabCount =  DEFAULT_BANNER_SIZE;
        }else {
            tabCount = pager.getAdapter().getCount();
        }
        addDots(tabCount);
    }
}
