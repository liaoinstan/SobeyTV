package com.lz.hybird.zqrlibrary.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.lz.hybird.zqrlibrary.R;
import com.lz.hybird.zqrlibrary.camera.CameraManager;
import com.lz.hybird.zqrlibrary.decoding.CaptureActivityHandler;
import com.lz.hybird.zqrlibrary.decoding.InactivityTimer;
import com.lz.hybird.zqrlibrary.decoding.RGBLuminanceSource;
import com.lz.hybird.zqrlibrary.view.ViewfinderView;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


public class MipcaActivityCapture extends AppCompatActivity implements Callback , View.OnClickListener{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;


	private static final int REQUEST_CODE = 100;
	private static final int PHOTO_RESULT = 200;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private ProgressDialog mProgress;
	private String photo_path;
	private String photo_path_crop;
	private Bitmap scanBitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

//		Button mButtonBack = (Button) findViewById(R.id.button_back);
//		mButtonBack.setOnClickListener(this);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

//		ImageButton mImageButton = (ImageButton) findViewById(R.id.button_function);
//		mImageButton.setOnClickListener(this);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
//		case R.id.button_back:
//			this.finish();
//			break;
//		case R.id.button_function:
//			//打开手机中的相册
//			Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
//	        innerIntent.setType("image/*");
//	        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//	        this.startActivityForResult(wrapperIntent, REQUEST_CODE);
//			break;
		}
	}


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mProgress.dismiss();
			switch (msg.what) {
				case PARSE_BARCODE_SUC:
					onResultHandler((String)msg.obj, scanBitmap);
					break;
				case PARSE_BARCODE_FAIL:
					Toast.makeText(MipcaActivityCapture.this, (String)msg.obj, Toast.LENGTH_LONG).show();
					break;

			}
		}

	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
				case REQUEST_CODE:
					//获取选中图片的路径
//				Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
//				if (cursor.moveToFirst()) {
//					photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//				}
//				cursor.close();

					photo_path = MyFileUtil.getRealFilePath(this, data.getData());
					if (photo_path==null || "".equals(photo_path)){
						Toast.makeText(this,"文件不存在或已损坏",Toast.LENGTH_SHORT).show();
						return;
					}

					photo_path_crop = MyFileUtil.getPhotoFullPathWithFile();
					startPhotoCrop(Uri.fromFile(new File(photo_path)), Uri.fromFile(new File(this.photo_path_crop)));

					break;

				case PHOTO_RESULT:
					if (resultCode == RESULT_OK){
						mProgress = new ProgressDialog(MipcaActivityCapture.this);
						mProgress.setMessage("正在扫描...");
						mProgress.setCancelable(false);
						mProgress.show();

						new Thread(new Runnable() {
							@Override
							public void run() {
								Result result = scanningImage(MipcaActivityCapture.this.photo_path_crop);
								/////扫描完成后删掉临时文件
								File file = new File(photo_path_crop);
								if (file!=null && file.exists()) {
									file.delete();
								}
								if (result != null) {
									Message m = mHandler.obtainMessage();
									m.what = PARSE_BARCODE_SUC;
									m.obj = result.getText();
									mHandler.sendMessage(m);
								} else {
									Message m = mHandler.obtainMessage();
									m.what = PARSE_BARCODE_FAIL;
									m.obj = "Scan failed!";
									mHandler.sendMessage(m);
								}
							}
						}).start();
					}else {
						File file = new File(this.photo_path_crop);
						if (file!=null && file.exists()) {
							file.delete();
						}
					}
					break;
			}

		}
	}

	/**
	 * 调用系统图片编辑进行裁剪
	 */
	public void startPhotoCrop(Uri uri,Uri out) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,out);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, PHOTO_RESULT);
	}

	/**
	 * 扫描二维码图片的方法
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); //设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		onResultHandler(resultString, barcode);
	}

	public static final String KEY_RESULT = "result";
	public static final String KEY_BITMAP = "bitmap";
	/**
	 * 跳转到上一个页面
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap){
		//TODO:在这里添加处理代码
		if(TextUtils.isEmpty(resultString)){
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(KEY_RESULT,resultString);
		intent.putExtra(KEY_BITMAP,bitmap);
		this.setResult(RESULT_OK, intent);
		finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_scan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==android.R.id.home){
			finish();
			return true;
		}else if(item.getItemId()==R.id.menu_scan_sltpic){
			//打开手机中的相册
			Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
			innerIntent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
			this.startActivityForResult(wrapperIntent, REQUEST_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}