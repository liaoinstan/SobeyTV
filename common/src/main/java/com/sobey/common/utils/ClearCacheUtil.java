package com.sobey.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class ClearCacheUtil {

    /**
     * 获取内存和外出缓存大小总和
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除内存和外出缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 获取外出缓存大小
     */
    public static String getExternalCacheSize(Context context) throws Exception {
        long cacheSize = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize = getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除外出缓存
     */
    public static void clearExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    public static String getSobeyCacheSize(Context context) {
        try {
            List<File> folders = new ArrayList<>();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                folders.add(context.getExternalCacheDir());
            }
//            folders.add(context.getCacheDir());
            folders.add(new File("/storage/emulated/0/!croptest"));
            folders.add(new File("/storage/emulated/0/!videotest"));
            folders.add(new File("/storage/emulated/0/!voicetest"));

            return getFormatSize(getFoldersSize(folders.toArray(new File[]{})));
        } catch (Exception e) {
            e.printStackTrace();
            return "0K";
        }
    }

    public static void clearSobeyCache(Context context){
        try {
            List<File> folders = new ArrayList<>();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                folders.add(context.getExternalCacheDir());
            }
//            folders.add(context.getCacheDir());
            folders.add(new File("/storage/emulated/0/!croptest"));
            folders.add(new File("/storage/emulated/0/!videotest"));
            folders.add(new File("/storage/emulated/0/!voicetest"));

            clearFolders(folders.toArray(new File[]{}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getFoldersSize(File[] folders) {
        long total = 0;
        for (File folder : folders) {
            if (folder != null && folder.exists()) {
                total += getFolderSize(folder);
            }
        }
        return total;
    }

    private static void clearFolders(File[] folders) {
        for (File folder : folders) {
            if (folder != null && folder.exists()) {
                deleteDir(folder);
            }
        }
    }

    ///////////////////////////////////////
    ////////内部方法
    ///////////////////////////////////////


    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


}
