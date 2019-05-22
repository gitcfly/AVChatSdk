package com.ckj.avchatsdk;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.RyuDouble;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AudioSendTask implements Runnable {

    List<String> targetIds;
    byte[] data;

    public AudioSendTask(byte[] data, List<String> targetIds){
        this.data=data;
        this.targetIds=targetIds;
    }

    @Override
    public void run() {
        try{
            RequestDataPack request=new RequestDataPack(AVSdkClient.myselfId,targetIds,data);
            request.setType("audio");
            byte[] requestBytes= JSON.toJSONBytes(request);
            AVSdkClient.sendPacket.setData(requestBytes);
            Log.e("dyx","send audio data length:"+requestBytes.length);
            AVSdkClient.sendClient.send(AVSdkClient.sendPacket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
