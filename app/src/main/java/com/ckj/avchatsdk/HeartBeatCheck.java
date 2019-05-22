package com.ckj.avchatsdk;

import com.alibaba.fastjson.JSON;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HeartBeatCheck extends Thread{

    DatagramSocket socket;

    DatagramPacket heartBeat;

    public HeartBeatCheck(DatagramSocket socket, InetAddress address, int serverPort) {
        this.socket=socket;
        RequestDataPack request=new RequestDataPack(AVSdkClient.myselfId,null,null);
        request.setType("connect");
        byte[] requestBytes= JSON.toJSONBytes(request);
        this.heartBeat=new DatagramPacket(requestBytes,requestBytes.length,address,serverPort);
    }

    @Override
    public void run() {
        while (AVSdkClient.isOpenAudio||AVSdkClient.isOpenVideo){
            try{
                socket.send(heartBeat);
                Thread.sleep(3000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
