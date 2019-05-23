package com.ckj.avchatsdk;

import android.os.Build;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.ckj.avchatsdk.CodeType.forwardAudio;
import static com.ckj.avchatsdk.CodeType.forwardVideo;

public class AVSdkClient {
    public static boolean isOnline=true;
    List<String> targetIds=new ArrayList<>();
    public static boolean isOpenVideo=true;
    public static boolean isOpenAudio=true;
    public static DatagramSocket sendClient;
    public static String myselfId=Build.MODEL;
    public static ExecutorService siglePool= Executors.newSingleThreadExecutor();
    public static InetAddress address;
    public static String serverHost;
    public static int serverPort;
    public AudioSdk audioSdk;
    public VideoSdk videoSdk;
    public ReciveServer reciveServer;
    public HeartBeatCheck heartBeatCheck;
    public ChatSdkListener chatListener;
    public AVSdkClient(String serverHost,int serverPort){
        try {
            this.serverHost=serverHost;
            this.serverPort=serverPort;
            if(sendClient!=null){
                sendClient.close();
            }
            sendClient = new DatagramSocket();
            address=InetAddress.getByName(serverHost);
            reciveServer=new ReciveServer();
            heartBeatCheck=new HeartBeatCheck(reciveServer.reciveSocket,address,serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loginOnline(){
        isOnline=true;
        if(!heartBeatCheck.isAlive()){
            heartBeatCheck.start();
        }
        if(!reciveServer.isAlive()){
            reciveServer.start();
        }
    }

    public void openAvChat(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (isOpenAudio||isOpenVideo){
                        if(videoSdk!=null&&VideoSdk.isOpenSelfVideo){
                            byte[] videoData=videoSdk.getVideoFrame();
                            if(videoData!=null){
                                sendData(videoData,targetIds,forwardVideo);
                            }
                        }
                        if(audioSdk!=null&&AudioSdk.isOpenSelfAudio){
                            byte[] audioData=audioSdk.getAudioFrame();
                            if(audioData!=null){
                                sendData(audioData,targetIds,forwardAudio);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void sendData(byte[] data, List<String> targetUsers,CodeType type){
        switch (type){
            case forwardAuConnect:
                ChatReqTask auTask=new ChatReqTask(targetIds,CodeType.forwardAuConnect);
                auTask.chatSdkListener=chatListener;
                siglePool.execute(auTask);
                break;
            case forwardAvConnect:
                ChatReqTask avTtask=new ChatReqTask(targetIds,CodeType.forwardAvConnect);
                avTtask.chatSdkListener=chatListener;
                siglePool.execute(avTtask);
                break;
            case forwardAudio:
                AudioSendTask audioTask=new AudioSendTask(data,targetUsers);
                siglePool.execute(audioTask);
                break;
            case forwardVideo:
                VideoSendTask videoTask=new VideoSendTask(data,targetUsers);
                siglePool.execute(videoTask);
                break;
            case forwardText:
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

    public ChatSdkListener getChatListener() {
        return chatListener;
    }

    public void setChatListener(ChatSdkListener chatListener) {
        this.chatListener = chatListener;
        reciveServer.setChatSdkListener(chatListener);
    }

    public void requestAvChat(List<String> targetIds){
        sendData(null,targetIds,CodeType.forwardAvConnect);
    }

    public void requestAuChat(List<String> targetIds){
        sendData(null,targetIds,CodeType.forwardAuConnect);
    }
}
