package com.sobey.common.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sobey.common.R;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class BundleView extends FrameLayout implements View.OnClickListener{

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

    private int width;
    private int height;

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
        root = (ViewGroup) inflater.inflate(R.layout.bundle,this,true);
        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
        if (!isInEditMode()) {
            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            width = dm.widthPixels;
            height = dm.heightPixels;
        }
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

        lay_bundle_photo.getLayoutParams().width = width/3;
        lay_bundle_video.getLayoutParams().width = width/3;
        lay_bundle_voice.getLayoutParams().width = width/3;
        lay_bundle_photo.setVisibility(GONE);
        lay_bundle_video.setVisibility(GONE);
        lay_bundle_voice.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_bundle_photo) {
            if (bundleClickListener!=null){
                bundleClickListener.onPhotoShowClick(v);
            }
        } else if (i == R.id.img_bundle_video) {
            if (bundleClickListener!=null){
                bundleClickListener.onVideoShowClick(v);
            }
        } else if (i == R.id.img_bundle_voice) {
            if (bundleClickListener!=null){
                bundleClickListener.onVoiceShowClick(v);
            }
        }else if (i == R.id.img_bundle_delete_photo) {
            lay_bundle_photo.setVisibility(GONE);
            if (bundleClickListener!=null){
                bundleClickListener.onPhotoDelClick(v);
            }
        } else if (i == R.id.img_bundle_delete_video) {
            lay_bundle_video.setVisibility(GONE);
            if (bundleClickListener!=null){
                bundleClickListener.onVideoDelClick(v);
            }
        } else if (i == R.id.img_bundle_delete_voice) {
            lay_bundle_voice.setVisibility(GONE);
            if (bundleClickListener!=null){
                bundleClickListener.onVoiceDelClick(v);
            }
        }
    }

    private OnBundleClickListener bundleClickListener;
    public void setOnBundleClickListener(OnBundleClickListener bundleClickListener) {
        this.bundleClickListener = bundleClickListener;
    }
    public interface OnBundleClickListener{
        void onPhotoDelClick(View v);
        void onVideoDelClick(View v);
        void onVoiceDelClick(View v);
        void onPhotoShowClick(View v);
        void onVideoShowClick(View v);
        void onVoiceShowClick(View v);
    }

    public void setPhoto(Bitmap bitmap){
        lay_bundle_photo.setVisibility(VISIBLE);
        img_bundle_photo.setImageBitmap(bitmap);
    }
    public void setVideo(Bitmap bitmap){
        lay_bundle_video.setVisibility(VISIBLE);
        img_bundle_video.setImageBitmap(bitmap);
    }
    public void setVoice(){
        lay_bundle_voice.setVisibility(VISIBLE);
    }
}
