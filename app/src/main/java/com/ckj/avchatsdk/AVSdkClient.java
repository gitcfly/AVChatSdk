package com.ckj.avchatsdk;

import android.os.Build;
import com.alibaba.fastjson.JSON;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AVSdkClient {
    public static boolean isOpenVideo=true;
    public static boolean isOpenAudio=true;
    public static DatagramSocket sendClient;
    public static DatagramPacket sendPacket;
    public static String myselfId=Build.MODEL;
    public static ExecutorService siglePool= Executors.newSingleThreadExecutor();
    public static String serverHost;
    public static int serverPort;
    public AudioSdk audioSdk;
    public VideoSdk videoSdk;
    public ReciveServer reciveServer;
    public HeartBeatCheck heartBeatCheck;

    public AVSdkClient(String serverHost,int serverPort){
        try {
            this.serverHost=serverHost;
            this.serverPort=serverPort;
            if(sendClient!=null){
                sendClient.close();
            }
            sendClient = new DatagramSocket();
            byte[] openChannel=new byte[1];
            InetAddress address=InetAddress.getByName(serverHost);
            sendPacket = new DatagramPacket(openChannel,1,address,serverPort);
            reciveServer=new ReciveServer();
            heartBeatCheck=new HeartBeatCheck(reciveServer.reciveSocket,address,serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openAvChat(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> targetIds=new ArrayList<>();
                targetIds.add(myselfId);
                try{
                    heartBeatCheck.start();
                    reciveServer.start();
                    Thread.sleep(10);
                    while (isOpenAudio||isOpenVideo){
                        if(videoSdk!=null||VideoSdk.isOpenSelfVideo){
                            byte[] videoData=videoSdk.getVideoFrame();
                            if(videoData!=null){
                                sendData(videoData,targetIds,"video");
                            }
                        }
                        if(audioSdk!=null&&AudioSdk.isOpenSelfAudio){
                            byte[] audioData=audioSdk.getAudioFrame();
                            if(audioData!=null){
                                sendData(audioData,targetIds,"audio");
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void sendData(byte[] data, List<String> targetUsers,String type){
        switch (type){
            case "av":
                break;
            case "audio":
                AudioSendTask audioTask=new AudioSendTask(data,targetUsers);
                siglePool.execute(audioTask);
                break;
            case "video":
                VideoSendTask videoTask=new VideoSendTask(data,targetUsers);
                siglePool.execute(videoTask);
                break;
            case "text":
                break;
        }
    }

    public AudioSdk getAudioSdk() {
        return audioSdk;
    }

    public void setAudioSdk(AudioSdk audioSdk) {
        this.audioSdk = audioSdk;
        if(audioSdk!=null){
            reciveServer.setAudioPlayer(audioSdk.audioPlayer);
        }
    }

    public VideoSdk getVideoSdk() {
        return videoSdk;
    }

    public void setVideoSdk(VideoSdk videoSdk) {
        this.videoSdk = videoSdk;
        if(videoSdk!=null){
            reciveServer.setHandler(videoSdk.getRemoteView().nofication);
        }
    }

    public void closeAvChat(){
        if(videoSdk!=null){
            videoSdk.finishVideo();
        }
        if(audioSdk!=null){
            audioSdk.finishAudio();
        }
    }

}
