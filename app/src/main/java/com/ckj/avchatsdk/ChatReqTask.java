package com.ckj.avchatsdk;

import com.alibaba.fastjson.JSON;

import java.net.DatagramPacket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ChatReqTask implements Runnable {

    List<String> targetIds;

    ChatSdkListener chatSdkListener;

    CodeType codeType;

    public ChatReqTask(List<String> targetIds,CodeType type) {
        this.targetIds = targetIds;
        this.codeType=type;
    }

    @Override
    public void run() {
        try{
            final String reqId= UUID.randomUUID().toString();
            RequestDataPack pack=new RequestDataPack(AVSdkClient.myselfId,codeType);
            pack.setReqid(reqId);
            pack.setTargetIds(targetIds);
            byte[] data= JSON.toJSONBytes(pack);
            DatagramPacket datagramPacket=new DatagramPacket(data,data.length,AVSdkClient.address,AVSdkClient.serverPort);
            AVSdkClient.sendClient.send(datagramPacket);
            for(final String targetId:targetIds){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            NetRespCallback callback=new NetRespCallback();
                            String userKey=reqId+"#"+targetId;
                            NetUtils.callbackMap.put(userKey,callback);
                            synchronized (callback){
                                callback.wait(30000);
                            }
                            NetUtils.callbackMap.remove(userKey);
                            if(!callback.isReturned()){
                                chatSdkListener.onRespTimeOut(targetId);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
