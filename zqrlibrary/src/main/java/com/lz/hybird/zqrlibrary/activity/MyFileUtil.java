package com.lz.hybird.zqrlibrary.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/14.
 */
public class MyFileUtil {

//	public static final String tempFilePath = "mnt/sdcard/DCIM/test/";

    /**
     * 获取不重复的文件名
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }

    public static String getPhotoFullPath() {
        String dir = getSDPath() + File.separator + "!croptest";
        File dirFile = new File(dir);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        return dir + File.separator + getPhotoFileName();
    }

    /**
     * 返回文件全路径，同时初始化文件
     */
    public static String getPhotoFullPathWithFile(){
        String path = getPhotoFullPath();
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return path;
    }

    public static File getPhotoFile() {
        File file = new File(getPhotoFullPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public static Uri getPhotoUri() {
        return Uri.fromFile(getPhotoFile());
    }


    /**
     * 从uri 获取真实文件路径
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}
