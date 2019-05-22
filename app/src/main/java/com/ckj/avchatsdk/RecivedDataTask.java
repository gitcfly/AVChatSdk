package com.ckj.avchatsdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import java.net.DatagramPacket;

public class RecivedDataTask implements Runnable {
    static Bitmap bitmap;
    DatagramPacket packet;
    Handler videoHandler;
    SdkAudioPlayer audioPlayer;

    public RecivedDataTask(DatagramPacket packet){
        this.packet=packet;
    }
    @Override
    public void run() {
        try{
            String recivedData=new String(packet.getData(), 0, packet.getLength());
            RequestDataPack recivedObj= JSON.parseObject(recivedData,RequestDataPack.class);
            byte[] recivedBytes=recivedObj.getData();
            String type=recivedObj.type;
            System.out.println("recived data type:"+type);
            if(VideoSdk.isOpenRemoteVideo&&"video".equals(type)){
                bitmap = BitmapFactory.decodeByteArray(recivedBytes, 0, recivedBytes.length);
                Matrix matrix=new Matrix();
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                bitmap= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
                if(bitmap!=null&&videoHandler!=null){
                    videoHandler.sendMessage(new Message());
                }
            }else if(AudioSdk.isOpenRemoteAudio&&"audio".equals(type)){
                if(audioPlayer!=null){
                    audioPlayer.play(recivedBytes);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Handler getVideoHandler() {
        return videoHandler;
    }

    public void setVideoHandler(Handler videoHandler) {
        this.videoHandler = videoHandler;
    }

    public SdkAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(SdkAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }
}
