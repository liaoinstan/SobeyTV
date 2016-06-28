package com.sobey.common.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobey.common.R;

import org.xutils.x;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class BundleView extends LinearLayout implements View.OnClickListener {

    private View lay_bundle_photo;
    private View lay_bundle_video;
    private View lay_bundle_voice;
    private ImageView img_bundle_photo;
    private ImageView img_bundle_video;
    private ImageView img_bundle_voice;
    private ImageView img_bundle_delete_photo;
    private ImageView img_bundle_delete_video;
    private ImageView img_bundle_delete_voice;
    private ViewGroup root;

    private Context context;
    private LayoutInflater inflater;

    public BundleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        root = (ViewGroup) inflater.inflate(R.layout.bundle, this, true);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        initBase();
        initView();
        initCtrl();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int expandSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(expandSpec, expandSpec);
        }
    }

    private void initBase() {
    }

    private void initView() {
        img_bundle_photo = (ImageView) root.findViewById(R.id.img_bundle_photo);
        img_bundle_video = (ImageView) root.findViewById(R.id.img_bundle_video);
        img_bundle_voice = (ImageView) root.findViewById(R.id.img_bundle_voice);
        img_bundle_delete_photo = (ImageView) root.findViewById(R.id.img_bundle_delete_photo);
        img_bundle_delete_video = (ImageView) root.findViewById(R.id.img_bundle_delete_video);
        img_bundle_delete_voice = (ImageView) root.findViewById(R.id.img_bundle_delete_voice);
        lay_bundle_photo = root.findViewById(R.id.lay_bundle_photo);
        lay_bundle_video = root.findViewById(R.id.lay_bundle_video);
        lay_bundle_voice = root.findViewById(R.id.lay_bundle_voice);
    }

    private void initCtrl() {
        img_bundle_photo.setOnClickListener(this);
        img_bundle_video.setOnClickListener(this);
        img_bundle_voice.setOnClickListener(this);

        img_bundle_delete_photo.setOnClickListener(this);
        img_bundle_delete_video.setOnClickListener(this);
        img_bundle_delete_voice.setOnClickListener(this);

        if (!isInEditMode()) {
            lay_bundle_photo.setVisibility(GONE);
            lay_bundle_video.setVisibility(GONE);
            lay_bundle_voice.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_bundle_photo) {
            if (bundleClickListener != null) {
                bundleClickListener.onPhotoShowClick(v);
            }
        } else if (i == R.id.img_bundle_video) {
            if (bundleClickListener != null) {
                bundleClickListener.onVideoShowClick(v);
            }
        } else if (i == R.id.img_bundle_voice) {
            if (bundleClickListener != null) {
                bundleClickListener.onVoiceShowClick(v);
            }
        } else if (i == R.id.img_bundle_delete_photo) {
            lay_bundle_photo.setVisibility(GONE);
            if (bundleClickListener != null) {
                bundleClickListener.onPhotoDelClick(v);
            }
        } else if (i == R.id.img_bundle_delete_video) {
            lay_bundle_video.setVisibility(GONE);
            if (bundleClickListener != null) {
                bundleClickListener.onVideoDelClick(v);
            }
        } else if (i == R.id.img_bundle_delete_voice) {
            lay_bundle_voice.setVisibility(GONE);
            if (bundleClickListener != null) {
                bundleClickListener.onVoiceDelClick(v);
            }
        }
    }

    private OnBundleClickListener bundleClickListener;

    public void setOnBundleClickListener(OnBundleClickListener bundleClickListener) {
        this.bundleClickListener = bundleClickListener;
    }

    public interface OnBundleClickListener {
        void onPhotoDelClick(View v);

        void onVideoDelClick(View v);

        void onVoiceDelClick(View v);

        void onPhotoShowClick(View v);

        void onVideoShowClick(View v);

        void onVoiceShowClick(View v);
    }

    private String pathphoto;
    private String pathvideo;
    private String pathvoice;

    public void setPhoto(Bitmap bitmap) {
        lay_bundle_photo.setVisibility(VISIBLE);
        img_bundle_photo.setImageBitmap(bitmap);
    }

    public void setVideo(Bitmap bitmap) {
        lay_bundle_video.setVisibility(VISIBLE);
        img_bundle_video.setImageBitmap(bitmap);
    }

    public void setVoice() {
        lay_bundle_voice.setVisibility(VISIBLE);
    }

    public void setPhoto(String path) {
        pathphoto = path;
        lay_bundle_photo.setVisibility(VISIBLE);
        Glide.with(context).load(path).placeholder(R.drawable.test).into(img_bundle_photo);
    }

    public void setVideo(String path) {
        pathvideo = path;
        lay_bundle_video.setVisibility(VISIBLE);
        Glide.with(context).load(path).placeholder(R.drawable.test).into(img_bundle_video);
    }

    public void setVoice(String path) {
        pathvoice = path;
        lay_bundle_voice.setVisibility(VISIBLE);
    }

    public void closePhoto() {
        lay_bundle_photo.setVisibility(GONE);
    }

    public void closeVideo() {
        lay_bundle_video.setVisibility(GONE);
    }

    public void closeVoice() {
        lay_bundle_voice.setVisibility(GONE);
    }


    public void setDelEnable(boolean enable) {
        if (enable) {
            img_bundle_delete_photo.setVisibility(VISIBLE);
            img_bundle_delete_video.setVisibility(VISIBLE);
            img_bundle_delete_voice.setVisibility(VISIBLE);
        } else {
            img_bundle_delete_photo.setVisibility(GONE);
            img_bundle_delete_video.setVisibility(GONE);
            img_bundle_delete_voice.setVisibility(GONE);
            lay_bundle_photo.setPadding(0,0,0,0);
            lay_bundle_video.setPadding(0,0,0,0);
            lay_bundle_voice.setPadding(0,0,0,0);
        }
    }

//    //恢复状态
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if(!(state instanceof SavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//        SavedState savedState=(SavedState)state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        this.status=savedState.status;
//    }
//
//    //保存状态
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState=super.onSaveInstanceState();
//        SavedState savedState=new SavedState(superState);
//        savedState.status=this.status;
//        return savedState;
//
//    }
//
//    public static class SavedState extends BaseSavedState {
//        public boolean status;
//
//        public SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            super.writeToParcel(dest,flags);
//            dest.writeByte(this.status ? (byte) 1 : (byte) 0);
//        }
//
//        private SavedState(Parcel in) {
//            super(in);
//            this.status = in.readByte() != 0;
//        }
//
//        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
//            @Override
//            public SavedState createFromParcel(Parcel source) {
//                return new SavedState(source);
//            }
//
//            @Override
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }
}
