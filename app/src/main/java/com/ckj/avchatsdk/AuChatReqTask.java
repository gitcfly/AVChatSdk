package com.ckj.avchatsdk;

import com.alibaba.fastjson.JSON;

import java.net.DatagramPacket;
import java.util.List;
import java.util.UUID;

public class AuChatReqTask implements Runnable{

    List<String> targetIds;

    ChatSdkListener chatSdkListener;

    public AuChatReqTask(List<String> targetIds) {
        this.targetIds = targetIds;
    }

    @Override
    public void run() {
        try{
            String reqId= UUID.randomUUID().toString();
            RequestDataPack pack=new RequestDataPack(AVSdkClient.myselfId,CodeType.forwardAuConnect);
            pack.setTargetIds(targetIds);
            pack.setReqid(reqId);
            byte[] data= JSON.toJSONBytes(pack);
            DatagramPacket datagramPacket=new DatagramPacket(data,data.length,AVSdkClient.address,AVSdkClient.serverPort);
            AVSdkClient.sendClient.send(datagramPacket);
            NetRespCallback callback=new NetRespCallback();
            NetUtils.callbackMap.put(reqId,callback);
            synchronized (callback){
                callback.wait(20000);
            }
            NetUtils.callbackMap.remove(reqId);
            if(!callback.isReturned()){
                chatSdkListener.onRespTimeOut(null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
