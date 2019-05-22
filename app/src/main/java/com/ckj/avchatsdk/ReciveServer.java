package com.ckj.avchatsdk;

import android.os.Handler;
import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReciveServer extends Thread {
    public static ExecutorService recivedPool= Executors.newSingleThreadExecutor();
    Handler handler;
    SdkAudioPlayer audioPlayer;
    DatagramSocket reciveSocket;

    public SdkAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
    public void setAudioPlayer(SdkAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }
    public ReciveServer(){
        try{
            reciveSocket=new DatagramSocket();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        Log.e("dyx","接收服务已启动");
        while (AVSdkClient.isOpenAudio||AVSdkClient.isOpenVideo){
            try{
                byte data[] = new byte[65535];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                reciveSocket.receive(packet);
                RecivedDataTask recivedDataTask=new RecivedDataTask(packet);
                recivedDataTask.setVideoHandler(handler);
                recivedDataTask.setAudioPlayer(audioPlayer);
                recivedPool.execute(recivedDataTask);
            }catch (SocketException e){
                e.printStackTrace();
                reciveSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
