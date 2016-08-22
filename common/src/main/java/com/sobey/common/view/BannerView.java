package com.sobey.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sobey.common.R;
import com.sobey.common.common.CustomBitmapLoadCallBack;
import com.sobey.common.entity.Images;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class BannerView extends FrameLayout implements Runnable {

    private static final String TAG = "Banner";

    private Context context;
    private LayoutInflater inflater;
    private TextView text_banner;
    private ViewPager mViewPager;
    private DotView dotView;
    private BannerAdapter mBannerAdapter;
    private List<Images> images;

    private int mUnSelectedSrc;
    private int mSelectedSrc;
    private int selectedColor;
    private int unSelectedColor;

    private int mBannerPosition = 0;
    private final int FAKE_BANNER_SIZE = 100;
    private int DEFAULT_BANNER_SIZE = 5;
    private boolean mIsUserTouched = false;

    private boolean isAutoScroll = true;
    private final static int AUTO_SCROLL_WHAT = 0;
    private long mDelayTimeInMills = 3000;

    private int dot_size = 13;

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);

        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);

            selectedColor = attributes.getColor(R.styleable.BannerView_banner_selected_color, Color.rgb(255, 255, 255));
            unSelectedColor = attributes.getColor(R.styleable.BannerView_banner_unselected_color, Color.argb(33, 255, 255, 255));

            mSelectedSrc = attributes.getResourceId(R.styleable.BannerView_banner_selected_drawable, 0);
            mUnSelectedSrc = attributes.getResourceId(R.styleable.BannerView_banner_unselected_drawable, 0);
        }
    }

    @Override
    protected void onFinishInflate() {
        inflater.inflate(R.layout.banner, this, true);
        super.onFinishInflate();
        initCtrl();
        if (mSelectedSrc != 0) {
            dotView.setSelectedDotResource(mSelectedSrc);
        }
        if (mUnSelectedSrc != 0) {
            dotView.setUnSelectedDotResource(mUnSelectedSrc);
        }
    }

    private void sendScrollMessage() {
        if (mHandler != null && images != null && images.size() != 0) {
            mHandler.removeMessages(AUTO_SCROLL_WHAT);
            if (isAutoScroll && mViewPager.getAdapter().getCount() > 1) {
                mHandler.sendEmptyMessageDelayed(AUTO_SCROLL_WHAT, mDelayTimeInMills);
            }
        }
    }

    private void initCtrl() {
        text_banner = (TextView) findViewById(R.id.text_banner);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        dotView = (DotView) findViewById(R.id.dotView);
    }

    private void initView() {
        //无图片时隐藏banner,这才是正确的做法，狗屎需求要显示一张默认图，代码在下面
//        if (images != null && images.size() == 0) {
//            mHandler.removeMessages(AUTO_SCROLL_WHAT);
//            setVisibility(GONE);
//            return;
//        } else {
//            setVisibility(VISIBLE);
//        }

        //没有图片展示默认图，
        if (images != null && images.size() == 0) {
            images.add(new Images("default"));
            //dotView.setVisibility(GONE);
            //mHandler.removeMessages(AUTO_SCROLL_WHAT);
        } else {
        }
        DEFAULT_BANNER_SIZE = images.size();
        mBannerAdapter = new BannerAdapter(context);
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.addOnPageChangeListener(mBannerAdapter);
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    sendScrollMessage();
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    sendScrollMessage();
                    mIsUserTouched = false;
                }
                return false;
            }
        });


        dotView.setViewPager(mViewPager, DEFAULT_BANNER_SIZE);

//        if (images.size()>=1) {
//            mViewPager.setCurrentItem(1);
//        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUTO_SCROLL_WHAT) {
                if (images.size()>=2) {
                    //至少2张图才滚蛋
                    int size = mViewPager.getAdapter().getCount();
                    int position = (mViewPager.getCurrentItem() + 1) % size;
                    mViewPager.setCurrentItem(position, true);
                    BannerView.this.sendScrollMessage();
                }
            }
        }
    };

    @Override
    public void run() {
        if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
            mViewPager.setCurrentItem(DEFAULT_BANNER_SIZE - 1, false);
        } else {
            mViewPager.setCurrentItem(mBannerPosition);
        }
    }

    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private LayoutInflater mInflater;

        public BannerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //计算实际位置
            position %= DEFAULT_BANNER_SIZE;

            //初始化imageView
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);


            if ("default".equals(images.get(position).getImg())){
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageResource(R.drawable.default_banner);
            }else {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //绑定网络图片
//            x.image().bind(imageView, images.get(position).getImg(), new CustomBitmapLoadCallBack(imageView));
                //Glide.with(context).load(images.get(position).getImg()).placeholder(R.drawable.default_bk).crossFade().into(imageView);
                if (onLoadImgListener!=null){
                    onLoadImgListener.onloadImg(imageView,images.get(position).getImg(),R.drawable.default_bk);
                }
            }

            //点击事件
            final int pos = position;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "click banner item :" + pos, Toast.LENGTH_SHORT).show();
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onBannerClick(pos);
                    }
                }
            });

            //添加到容器
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = mViewPager.getCurrentItem();
            if (position == 0) {
                position = DEFAULT_BANNER_SIZE;
                mViewPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = DEFAULT_BANNER_SIZE - 1;
                mViewPager.setCurrentItem(position, false);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBannerPosition = position;
//            setIndicator(position);
            position %= DEFAULT_BANNER_SIZE;
            if (position <= images.size() - 1) {
                text_banner.setText(images.get(position).getTitle());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    //#######################接口
    private OnBannerClickListener onBannerClickListener;

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }

    private OnLoadImgListener onLoadImgListener;

    public void setOnLoadImgListener(OnLoadImgListener onLoadImgListener) {
        this.onLoadImgListener = onLoadImgListener;
    }

    public interface OnLoadImgListener {
        void onloadImg(ImageView imageView,String imgurl,int defaultSrc);
    }
    //#######################对外方法
    public void setDatas(List<Images> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        initView();
        sendScrollMessage();
    }

    public void showTitle(boolean needShow) {
        if (needShow) {
            text_banner.setVisibility(VISIBLE);
        } else {
            text_banner.setVisibility(GONE);
        }
        requestLayout();
    }
}
