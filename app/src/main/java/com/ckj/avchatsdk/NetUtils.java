package com.ckj.avchatsdk;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetUtils {

    public static Map<String,NetRespCallback> callbackMap=new HashMap<>();

    public static Object requestNet(final RequestDataPack dataPack,long timeOut){
        Log.e("dyx",dataPack.type.toString());
        NetRespCallback callback=new NetRespCallback();
        final String reqId=UUID.randomUUID().toString();
        callbackMap.put(reqId,callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataPack.setSourceId(AVSdkClient.myselfId);
                    dataPack.setReqid(reqId);
                    byte[] data= JSON.toJSONBytes(dataPack);
                    DatagramPacket datagramPacket=new DatagramPacket(data,data.length,AVSdkClient.address,AVSdkClient.serverPort);
                    AVSdkClient.sendClient.send(datagramPacket);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        synchronized (callback){
            try{
                if(!callback.returned){
                    callback.wait(timeOut);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        callbackMap.remove(reqId);
        Log.e("dyx","result:"+JSON.toJSONString(callback.result));
        return callback.result;
    }
}
