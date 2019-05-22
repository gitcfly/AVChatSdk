package com.ckj.avchatsdk;

import android.app.Application;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.ckj.avchatsdk.api.AvChatSdk;

import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AudioSdk {

    public static boolean isOpenSelfAudio = false;// 设置录制标记为true
    public static boolean isOpenRemoteAudio = false;// 设置录制标记为true
    private BlockingQueue<byte[]> audioQueue=new ArrayBlockingQueue(1);
    private AudioRecord audioRecord;// 录音对象
    private int frequence = 8000;// 采样率 8000
    private int channelInConfig = AudioFormat.CHANNEL_IN_MONO;// 定义采样通道
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;// 定义音频编码（16位）
    private byte[] buffer = null;// 录制的缓冲数组
    private int bufferSize;
    SdkAudioPlayer audioPlayer;

    public AudioSdk(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(false);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0,
                AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        bufferSize = AudioRecord.getMinBufferSize(frequence, channelInConfig, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, frequence, channelInConfig, audioEncoding, bufferSize);
        buffer = new byte[bufferSize];
        audioPlayer=new SdkAudioPlayer();
    }

    private void startCaptureAudio(){
        audioRecord.startRecording();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("录音开始");
                while (isOpenSelfAudio) {
                    int result = audioRecord.read(buffer, 0, bufferSize);
                    System.out.println("capture voice length:"+result);
                    audioQueue.offer(buffer);
                }
                audioRecord.stop();
            }
        }).start();
    }


    public byte[] getAudioFrame()  {
        return audioQueue.poll();
    }

    public void closeSelf(){
        isOpenSelfAudio=false;
        if(isOpenRemoteAudio==false){
            AVSdkClient.isOpenAudio=false;
        }
    }

    public void closeRemote(){
        isOpenRemoteAudio=false;
        audioPlayer.track.stop();
        if(isOpenSelfAudio==false){
            AVSdkClient.isOpenAudio=false;
        }
    }

    public void finishAudio(){
        closeRemote();
        closeSelf();
        AVSdkClient.isOpenAudio=false;
    }

    public void openSelf(){
        if(isOpenSelfAudio==false){
            isOpenSelfAudio=true;
            AVSdkClient.isOpenAudio=true;
            startCaptureAudio();
        }
    }

    public void openRemote(){
        if(isOpenRemoteAudio==false){
            AVSdkClient.isOpenAudio=true;
            audioPlayer.track.play();
            isOpenRemoteAudio=true;
        }
    }

    public void openAudio(){
        openSelf();
        openRemote();
    }
}
