package com.sobey.common.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

import com.czt.mp3recorder.MP3Recorder;
import com.sobey.common.utils.FileUtil;
import com.sobey.common.utils.PermissionsUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsVoiceRecorder {
    MediaRecorder recorder;
    MP3Recorder mRecorder;

    static final String PREFIX = "voice";
    static final String EXTENSION = ".mp3";

    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    private boolean success = false;

    public InsVoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * start recording to the file
     */
    public String startRecording(Context appContext) {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
            }

            voiceFileName = getVoiceUidFileName();
            voiceFilePath = FileUtil.getVoiceFolder() + "/" + voiceFileName;
            file = new File(voiceFilePath);
            mRecorder = new MP3Recorder(file);
            mRecorder.start();
        } catch (Exception e) {
            Log.e("voice", "prepare() failed");
            PermissionsUtil.showDialog(appContext, "需要您授权录音权限");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (mRecorder.isRecording()) {
                        android.os.Message msg = new android.os.Message();

                        int realVolume = mRecorder.getRealVolume();

//                        int level = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        int level = realVolume / 1178;

                        if (realVolume != 0){
                            success = true;
                        }
                        Log.e("level", "level:" + level + "Volume:" + realVolume);

                        msg.what = level;
                        handler.sendMessage(msg);
                        SystemClock.sleep(100);
                    }
                } catch (Exception e) {
                    Log.e("voice", e.toString());
                }
            }
        }).start();
        startTime = System.currentTimeMillis();
        Log.d("voice", "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }

    public void discardRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
        if (file != null && file.exists() && !file.isDirectory()) {
            file.delete();
        }
    }

    public int stopRecoding() {
        try {
            if (success) {
                success = false;//重新设为初始状态
                if (mRecorder != null) {
                    mRecorder.stop();

                    int seconds = (int) (System.currentTimeMillis() - startTime) / 1000;
                    return seconds;
                }
            }else {
                //全是静音
                return -1;
            }
        } catch (Exception e) {
            Log.e("voice", "stopRecoding() failed");
        }
        return 0;
//        if(recorder != null){
//            isRecording = false;
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//
//            if(file == null || !file.exists() || !file.isFile()){
//                return -1;
//            }
//            if (file.length() == 0) {
//                file.delete();
//                return -1;
//            }
//            int seconds = (int) (new Date().getTime() - startTime) / 1000;
//            Log.d("voice", "voice recording finished. seconds:" + seconds + " file length:" + file.length());
//            return seconds;
//        }
//        return 0;
    }

    /**
     * 生成一个不重复的文件名
     */
    private String getVoiceUidFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'VOICE'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + System.currentTimeMillis() + EXTENSION;
    }

    public boolean isRecording() {
        return mRecorder.isRecording();
    }


    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }


    //////////////////
    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
//    public static String getSDPath() {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
//        }
//        return sdDir.toString();
//    }
//    public static String getVoiceDirPath() {
//        String dir = getSDPath() + File.separator + "!voicetest";
//        File dirFile = new File(dir);
//        if (!dirFile.exists()){
//            dirFile.mkdirs();
//        }
//        return dir;
//    }
}
