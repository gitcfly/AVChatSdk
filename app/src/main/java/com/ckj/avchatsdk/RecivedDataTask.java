package com.ckj.avchatsdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecivedDataTask implements Runnable {
    static Map<String,ReqCallBack> callBackMap=new HashMap<>();
    static Bitmap bitmap;
    DatagramPacket packet;
    Handler videoHandler;
    SdkAudioPlayer audioPlayer;

    public ChatSdkListener getChatlistener() {
        return chatlistener;
    }

    public void setChatlistener(ChatSdkListener chatlistener) {
        this.chatlistener = chatlistener;
    }

    ChatSdkListener chatlistener;
    public RecivedDataTask(DatagramPacket packet){
        this.packet=packet;
    }
    @Override
    public void run() {
        try{
            String recivedData=new String(packet.getData(), 0, packet.getLength());
            final RequestDataPack recivedObj= JSON.parseObject(recivedData,RequestDataPack.class);
            byte[] recivedBytes=recivedObj.getData();
            CodeType type=recivedObj.type;
            System.out.println("recived data type:"+type);
            switch (type){
                case forwardVideo:
                    if(!AudioSdk.isOpenRemoteAudio){
                        return;
                    }
                    bitmap = BitmapFactory.decodeByteArray(recivedBytes, 0, recivedBytes.length);
                    Matrix matrix=new Matrix();
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    bitmap= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
                    if(bitmap!=null&&videoHandler!=null){
                        videoHandler.sendMessage(new Message());
                    }
                    break;
                case forwardAudio:
                    if(!AudioSdk.isOpenRemoteAudio){
                        return;
                    }
                    if(audioPlayer!=null){
                        audioPlayer.play(recivedBytes);
                    }
                    break;
                case returnUsers:
                    NetRespCallback returnUserCallback=NetUtils.callbackMap.get(recivedObj.reqid);
                    if(returnUserCallback!=null){
                        returnUserCallback.call(recivedObj.extra);
                    }
                    break;
                case forwardAvConnect:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                long reqStart=System.currentTimeMillis();
                                long allTime=0;
                                Boolean result=chatlistener.onReceivedAvReq(recivedObj.sourceId,recivedObj.targetIds);
                                while (result==null&&allTime<20000){
                                    Thread.sleep(2000);
                                    allTime=System.currentTimeMillis()-reqStart;
                                    result=chatlistener.onReceivedAvReq(recivedObj.sourceId,recivedObj.targetIds);
                                }
                                if(result!=null){
                                    List<String> targetIds=new ArrayList<>();
                                    targetIds.add(recivedObj.sourceId);
                                    recivedObj.setSourceId(AVSdkClient.myselfId);
                                    recivedObj.setTargetIds(targetIds);
                                    if(result){
                                        recivedObj.setType(CodeType.agreeConnect);
                                    }else {
                                        recivedObj.setType(CodeType.disagreeConnect);
                                    }
                                    byte[] data=JSON.toJSONBytes(recivedObj);
                                    DatagramPacket response=new DatagramPacket(data,data.length,AVSdkClient.address,AVSdkClient.serverPort);
                                    AVSdkClient.sendClient.send(response);
                                }
                            }catch (Exception x){
                                x.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case forwardAuConnect:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                long reqStart=System.currentTimeMillis();
                                long allTime=0;
                                Boolean result=chatlistener.onReceivedAuReq(recivedObj.sourceId,recivedObj.targetIds);
                                while (result==null&&allTime<20000){
                                    Thread.sleep(2000);
                                    allTime=System.currentTimeMillis()-reqStart;
                                    result=chatlistener.onReceivedAuReq(recivedObj.sourceId,recivedObj.targetIds);
                                }
                                if(result!=null){
                                    List<String> targetIds=new ArrayList<>();
                                    targetIds.add(recivedObj.sourceId);
                                    recivedObj.setSourceId(AVSdkClient.myselfId);
                                    recivedObj.setTargetIds(targetIds);
                                    if(result){
                                        recivedObj.setType(CodeType.agreeConnect);
                                    }else {
                                        recivedObj.setType(CodeType.disagreeConnect);
                                    }
                                    byte[] data=JSON.toJSONBytes(recivedObj);
                                    DatagramPacket response=new DatagramPacket(data,data.length,AVSdkClient.address,AVSdkClient.serverPort);
                                    AVSdkClient.sendClient.send(response);
                                }
                            }catch (Exception x){
                                x.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case agreeConnect:
                    chatlistener.onAgreeChat(recivedObj.sourceId);
                    NetRespCallback avCallback=NetUtils.callbackMap.get(recivedObj.reqid+"#"+recivedObj.sourceId);
                    if(avCallback!=null){
                        avCallback.call(true);
                    }
                    break;
                case disagreeConnect:
                    chatlistener.onDisAgreeChat(recivedObj.sourceId);
                    NetRespCallback avCallbackt=NetUtils.callbackMap.get(recivedObj.reqid+"#"+recivedObj.sourceId);
                    if(avCallbackt!=null){
                        avCallbackt.call(false);
                    }
                    break;
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
