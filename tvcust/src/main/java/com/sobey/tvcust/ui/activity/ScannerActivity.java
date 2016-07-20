package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.frame_content);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }

    public static final String KEY_RESULT = "result";
    private ZBarScannerView mScannerView;

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("liao", rawResult.getContents()); // Prints scan results
        Log.e("liao", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT,rawResult.getContents());
        setResult(RESULT_OK,intent);
        finish();

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
