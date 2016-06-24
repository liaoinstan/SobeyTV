package com.sobey.tvcust.common;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.tvcust.R;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class ServerDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private TextView text_phone;
    private TextView text_qq;
    private TextView text_mail;

    public ServerDialog(Context context) {
        super(context,R.style.MyDialog);
        this.context = context;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
    	LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_server, null);// 得到加载view

        v.findViewById(R.id.item_server_dialog_phone).setOnClickListener(this);
        v.findViewById(R.id.item_server_dialog_qq).setOnClickListener(this);
        v.findViewById(R.id.item_server_dialog_mail).setOnClickListener(this);

        text_phone = (TextView) v.findViewById(R.id.text_server_dialog_phone);
        text_qq = (TextView) v.findViewById(R.id.text_server_dialog_qq);
        text_mail = (TextView) v.findViewById(R.id.text_server_dialog_mail);

        this.setCanceledOnTouchOutside(true);
        super.setContentView(v);

    }


    private OnRecordListener onRecordListener;
    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    @Override
    public void onClick(View v) {
        ClipboardManager c = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        switch (v.getId()){
            case R.id.item_server_dialog_phone:
                c.setPrimaryClip(ClipData.newPlainText(null, text_phone.getText().toString()));
                break;
            case R.id.item_server_dialog_qq:
                c.setPrimaryClip(ClipData.newPlainText(null, text_qq.getText().toString()));
                break;
            case R.id.item_server_dialog_mail:
                c.setPrimaryClip(ClipData.newPlainText(null, text_mail.getText().toString()));
                break;
        }
        Toast.makeText(context,"文字已复制到剪切板",Toast.LENGTH_SHORT).show();
        hide();
    }

    public interface OnRecordListener{
        void onRecordFinish(String voiceFilePath, int voiceTimeLength);
    }
}
