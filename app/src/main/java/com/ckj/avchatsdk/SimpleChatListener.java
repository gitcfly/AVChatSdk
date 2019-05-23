package com.ckj.avchatsdk;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class SimpleChatListener implements ChatSdkListener {
    Context context;
    SimpleChatListener(Context context){
        this.context=context;
    }
    @Override
    public Boolean onReceivedAvReq(String sourceId, List<String> targetIds) {
        Log.e("dyx", "onReceivedAvReq from " + sourceId);
        return true;
    }

    @Override
    public Boolean onReceivedAuReq(String sourceId, List<String> targetIds) {
        Log.e("dyx", "onReceivedAuReq from "+sourceId);
        return true;
    }

    @Override
    public void onAgreeChat(String remoteId) {
        Log.e("dyx", "onAgreeChat from "+remoteId);
        MainActivity.avSdkClient.openAvChat();
    }

    @Override
    public void onDisAgreeChat(String remoteId) {
        Log.e("dyx", "onDisAgreeChat from "+remoteId);
    }

    @Override
    public void onRespTimeOut(String remoteId) {
        Log.e("dyx", "onRespTimeOut from "+remoteId);
    }
}
