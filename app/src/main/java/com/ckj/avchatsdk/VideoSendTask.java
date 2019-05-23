package com.ckj.avchatsdk;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.util.List;

public class VideoSendTask implements Runnable {

    List<String> targetIds;
    byte[] data;

    public VideoSendTask(byte[] data, List<String> targetIds){
        this.data=data;
        this.targetIds=targetIds;
    }

    @Override
    public void run() {
        try{
            YuvImage image = new YuvImage(data, ImageFormat.NV21, VideoSdk.videoWidth, VideoSdk.videoHight, null);
            if(image!=null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, VideoSdk.videoWidth, VideoSdk.videoHight), 90, stream);
                RequestDataPack request=new RequestDataPack(AVSdkClient.myselfId,targetIds,stream.toByteArray());
                request.setType(CodeType.forwardVideo);
                byte[] requestBytes= JSON.toJSONBytes(request);
                DatagramPacket datagramPacket=new DatagramPacket(requestBytes,requestBytes.length,AVSdkClient.address,AVSdkClient.serverPort);
                Log.e("dyx","send video data length:"+requestBytes.length);
                AVSdkClient.sendClient.send(datagramPacket);
                stream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
