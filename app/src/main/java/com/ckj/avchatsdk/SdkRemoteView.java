package com.ckj.avchatsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SdkRemoteView extends ImageView {

    Handler nofication =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setImageBitmap(RecivedDataTask.bitmap);
        }
    };

    public Handler getNofication() {
        return nofication;
    }

    public void setNofication(Handler nofication) {
        this.nofication = nofication;
    }

    public SdkRemoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
