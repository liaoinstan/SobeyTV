package com.sobey.common.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sobey.common.R;
import com.sobey.common.helper.MyItemTouchCallback;
import com.sobey.common.helper.OnRecyclerItemClickListener;
import com.sobey.common.utils.VibratorUtil;
import com.sobey.common.view.bundle.BundleEntity;
import com.sobey.common.view.bundle.RecycleAdapterBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class BundleView2 extends FrameLayout {

    private RecyclerView recyclerView;
    private RecycleAdapterBundle adapter;
    private List<BundleEntity> results = new ArrayList<>();

    private ViewGroup root;

    private Context context;
    private LayoutInflater inflater;

    private boolean needDrag = true;

    public void setNeedDrag(boolean needDrag) {
        this.needDrag = needDrag;
    }

    public List<BundleEntity> getResults() {
        return results;
    }

    public BundleView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        root = (ViewGroup) inflater.inflate(R.layout.bundle2, this, true);
        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recycle_bundle);
    }

    private void initCtrl() {
//        results.add(new BundleEntity(BundleEntity.Type.PHOTE,"11111"));
//        results.add(new BundleEntity(BundleEntity.Type.PHOTE,"22222"));
//        results.add(new BundleEntity(BundleEntity.Type.VIDEO,"33333"));
//        results.add(new BundleEntity(BundleEntity.Type.PHOTE,"44444"));
//        results.add(new BundleEntity(BundleEntity.Type.VOICE,"55555"));
        adapter = new RecycleAdapterBundle(context, R.layout.item_recycle_bundle, results);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        recyclerView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (needDrag) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(context, 70);   //震动70ms
                }
            }
        });
    }

    public void freshCtrl() {
        adapter.notifyDataSetChanged();
    }

//    private OnBundleClickListener bundleClickListener;

    public void setOnBundleClickListener(OnBundleClickListener bundleClickListener) {
//        this.bundleClickListener = bundleClickListener;
        adapter.setBundleClickListener(bundleClickListener);
    }

    public interface OnBundleClickListener {
        void onPhotoDelClick(View v);

        void onVideoDelClick(View v);

        void onVoiceDelClick(View v);

        void onPhotoShowClick(String path);

        void onVideoShowClick(String path);

        void onVoiceShowClick(String path);
    }

    public void setDelEnable(boolean enable) {
        adapter.setDelEnable(enable);
    }

    public void addPhoto(String path) {
        adapter.getResults().add(new BundleEntity(BundleEntity.Type.PHOTE, path));
        adapter.notifyItemInserted(adapter.getResults().size());
    }

    public void addVideo(String path) {
        adapter.getResults().add(new BundleEntity(BundleEntity.Type.VIDEO, path));
        adapter.notifyItemInserted(adapter.getResults().size());
    }

    public void addVoice(String path) {
        adapter.getResults().add(new BundleEntity(BundleEntity.Type.VOICE, path));
        adapter.notifyItemInserted(adapter.getResults().size());
    }

    public String[] getPhotoPaths() {
        ArrayList<String> list = new ArrayList<>();
        for (BundleEntity bundle : results) {
            if (bundle.getType() == BundleEntity.Type.PHOTE) {
                list.add(bundle.getPath());
            }
        }
        return list.toArray(new String[]{});
    }

    public String[] getVideoPaths() {
        ArrayList<String> list = new ArrayList<>();
        for (BundleEntity bundle : results) {
            if (bundle.getType() == BundleEntity.Type.VIDEO) {
                list.add(bundle.getPath());
            }
        }
        return list.toArray(new String[]{});
    }

    public String[] getVoicePaths() {
        ArrayList<String> list = new ArrayList<>();
        for (BundleEntity bundle : results) {
            if (bundle.getType() == BundleEntity.Type.VOICE) {
                list.add(bundle.getPath());
            }
        }
        return list.toArray(new String[]{});
    }


    ///////////////接口
    public void setOnBundleLoadImgListener(OnBundleLoadImgListener onBundleLoadImgListener) {
        adapter.setOnBundleLoadImgListener(onBundleLoadImgListener);
    }

    public interface OnBundleLoadImgListener {
        void onloadImg(ImageView imageView, String imgurl, int defaultSrc);
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
//            dest.w
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
