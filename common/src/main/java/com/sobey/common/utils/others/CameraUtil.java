package com.sobey.common.utils.others;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import com.sobey.common.utils.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by MagicBean on 2015/06/25 15:15:57
 */
public class CameraUtil {
//    private static String cameraDir = getSDCardPath() + "DCIM/Camera/";
    private static String cameraDir = FileUtil.getSDPath() + File.separator + "!croptest/";
    private static StringBuilder fileName = new StringBuilder();

    /**
     * 获取拍照的Intent
     *
     * @return
     */
    public static Intent getCameraIntent() {
        resetStringBuilder();
        fileName.append("IMG_");
        fileName.append(getCurrentTime());
        fileName.append(".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        creatDir(cameraDir);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraDir, fileName.toString())));
        return intent;
    }

    public static String createImageName() {
        return "IMG_" + getCurrentTime() + ".jpg";
    }

    /**
     * 图片裁剪
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @return
     */
    public static Intent getCropIntent(Uri uri, int outputX, int outputY) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        // intent.putExtra("outputFormat",
        // Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        return intent;
    }

    /**
     * 获取相册intent
     *
     * @return
     */
    public static Intent getAlbumIntent() {
//        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return pickIntent;
    }

    /**
     * 获取当前时间<br>
     */
    private static CharSequence getCurrentTime() {
        return DateFormat.format("yyyyMMdd_hhmmss", new Date());
    }

    /**
     * 重置fileName
     */
    private static void resetStringBuilder() {
        if (fileName.length() > 0) {
            fileName.delete(0, fileName.length());
        }
    }

    /**
     * 获取图片真实路径
     */
    public static String getRealFilePath() {
        return cameraDir + fileName.toString();
    }

    /**
     * 创建文件夹
     */
    private static String creatDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

    /**
     * 判断SD卡是否可用
     */
    private static boolean sdCardIsExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        if (sdCardIsExit()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        }
        return null;
    }
}
