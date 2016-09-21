package com.sobey.common.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import com.sobey.common.utils.FileUtil;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.others.BitmapUtil;
import com.sobey.common.utils.others.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/1/19.
 */
public class CropHelperSys {
    //    private File file;
    private String path;
    private static final int PHOTO_CAPTURE = 0xf311;// 拍照
    private static final int PHOTO_RESULT = 0xf312;// 结果
    private static final int PHOTO_CROP = 0xf313;// 裁剪
    private boolean needCrop = false;   //默认不需要裁剪
    private boolean needPress = true;   //默认需要压缩
    private CropInterface cropInterface;
    private Context context;

    public CropHelperSys(CropInterface cropInterface) {
        this.cropInterface = cropInterface;

        if (cropInterface instanceof Activity) {
            context = ((Activity) cropInterface);
        } else if (cropInterface instanceof Fragment) {
            context = ((Fragment) cropInterface).getActivity();
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) cropInterface).getActivity();
        }
    }

    public void setNeedCrop(boolean needCrop) {
        this.needCrop = needCrop;
    }

    public void setNeedPress(boolean needPress) {
        this.needPress = needPress;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    if (needCrop) {
                        startPhotoCrop(Uri.fromFile(new File(path)));
                    } else {
                        if (needPress){
                            path = compress(path,path);
                        }
                        cropInterface.cropResult(path);
                    }
                } else {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
                break;
            case PHOTO_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        //获取真实的图片路径，下面2种方法都可以
                        //String pathFromUri = getPathFromUri(context, uri);
                        String pathFromUri = FileUtil.getRealFilePath(context, uri);
                        if (needPress){
                            pathFromUri = compress(pathFromUri,FileUtil.getPhotoFullPath());
                        }
                        if (!StrUtils.isEmpty(pathFromUri)) {
                            cropInterface.cropResult(pathFromUri);
                        }else {
                            Toast.makeText(context,"图片解析失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //没选择，不删除
                }
                break;
            case PHOTO_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    cropInterface.cropResult(path);
                } else {
                    File file = new File(path);
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
                break;
        }
    }

    private String getPathFromUri(Context context,Uri uri) {
        String pathm = null;

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            pathm = picturePath;
            cursor.close();
            cursor = null;
            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(context, "没有找到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            }
        } else {
            File file = new File(uri.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(context, "没有找到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            }
        }
        return pathm;
    }

    private String compress(String FromPath,String toPath){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapUtil.revitionImageSize(FromPath);
            toPath = BitmapUtil.saveBitmap(bitmap, toPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toPath;
    }



    /**
     * 调用摄像头拍照
     */
    public void startCamera() {
        path = FileUtil.getPhotoFullPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));

        if (cropInterface instanceof Activity) {
            ((Activity) cropInterface).startActivityForResult(intent, PHOTO_CAPTURE);
        } else if (cropInterface instanceof Fragment) {
            ((Fragment) cropInterface).startActivityForResult(intent, PHOTO_CAPTURE);
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) cropInterface).startActivityForResult(intent, PHOTO_CAPTURE);
        }
    }

    /**
     * 调用相册并裁剪
     */
    public void startPhoto() {
        Intent intent;
        int mark;
        if (needCrop) {
            path = FileUtil.getPhotoFullPath();
            intent = getPhotoCropIntent();
            mark = PHOTO_CROP;
        } else {
            intent = getPhotoIntent();
            mark = PHOTO_RESULT;
        }

        if (cropInterface instanceof Activity) {
            ((Activity) cropInterface).startActivityForResult(intent, mark);
        } else if (cropInterface instanceof Fragment) {
            ((Fragment) cropInterface).startActivityForResult(intent, mark);
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) cropInterface).startActivityForResult(intent, mark);
        }
    }

    /**
     * 进行裁剪
     */
    public void startPhotoCrop(Uri uri) {
        Intent intent = getCropIntent(uri);

        if (cropInterface instanceof Activity) {
            ((Activity) cropInterface).startActivityForResult(intent, PHOTO_CROP);
        } else if (cropInterface instanceof Fragment) {
            ((Fragment) cropInterface).startActivityForResult(intent, PHOTO_CROP);
        } else if (cropInterface instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) cropInterface).startActivityForResult(intent, PHOTO_CROP);
        }
    }

    private Intent getCropIntent(Uri uri) {
        Intent intent;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片输出大小
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        return intent;
    }

    private Intent getPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }

    private Intent getPhotoCropIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片输出大小
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        return intent;
    }

    /**
     * 根据URI获取位图
     *
     * @param uri
     * @return 对应的位图
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream is = null;
            if (cropInterface instanceof Activity) {
                is = ((Activity) cropInterface).getContentResolver().openInputStream(uri);
            } else if (cropInterface instanceof Fragment) {
                is = ((Fragment) cropInterface).getActivity().getContentResolver().openInputStream(uri);
            } else if (cropInterface instanceof android.support.v4.app.Fragment) {
                is = ((android.support.v4.app.Fragment) cropInterface).getActivity().getContentResolver().openInputStream(uri);
            }
//            bitmap = BitmapFactory.decodeStream(is);
            bitmap = BitmapFactory.decodeFile(uri.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private Bitmap decodeFileAsBitmap(File file) {
        return BitmapFactory.decodeFile(file.getPath());
    }

    public interface CropInterface {
        void cropResult(String path);
    }
}