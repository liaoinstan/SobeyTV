package com.sobey.common.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsVoiceRecorder {
    MediaRecorder recorder;

    static final String PREFIX = "voice";
    static final String EXTENSION = ".amr";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    public InsVoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * start recording to the file
     */
    public String startRecording(Context appContext) {
        file = null;
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1); // MONO
            recorder.setAudioSamplingRate(8000); // 8000Hz
            recorder.setAudioEncodingBitRate(64); // seems if change this to
                                                    // 128, still got same file
                                                    // size.
            // one easy way is to use temp file
            // file = File.createTempFile(PREFIX + userId, EXTENSION,
            // User.getVoicePath());
            voiceFileName = getVoiceFileName(getVoiceFileUid());
            voiceFilePath = getVoiceDirPath() + "/" + voiceFileName;
            file = new File(voiceFilePath);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording = true;
            recorder.start();
        } catch (IOException e) {
            Log.e("voice", "prepare() failed");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
                        android.os.Message msg = new android.os.Message();
                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        handler.sendMessage(msg);
                        SystemClock.sleep(100);
                    }
                } catch (Exception e) {
                    // from the crash report website, found one NPE crash from
                    // one android 4.0.4 htc phone
                    // maybe handler is null for some reason
                    Log.e("voice", e.toString());
                }
            }
        }).start();
        startTime = new Date().getTime();
        Log.d("voice", "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * stop the recoding
     * 
     * @return seconds of the voice recorded
     */

    public void discardRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (IllegalStateException e) {
            } catch (RuntimeException e){}
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if(recorder != null){
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            
            if(file == null || !file.exists() || !file.isFile()){
                return -1;
            }
            if (file.length() == 0) {
                file.delete();
                return -1;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            Log.d("voice", "voice recording finished. seconds:" + seconds + " file length:" + file.length());
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }

    
    public String getVoiceFilePath() {
        return voiceFilePath;
    }
    
    public String getVoiceFileName() {
        return voiceFileName;
    }



    //////////////////
    public static String getVoiceFileUid() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'VOICE'_yyyyMMdd_HHmmss");
        return dateFormat.format(date);
    }

    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }
    public static String getVoiceDirPath() {
        String dir = getSDPath() + File.separator + "!voicetest";
        File dirFile = new File(dir);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        return dir;
    }
}
