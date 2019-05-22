package com.ckj.avchatsdk;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SdkAudioPlayer {
    public AudioTrack track = null;// 录音文件播放对象
    public int frequence = 8000;// 采样率 8000
    public int channelInConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;// 定义采样通道
    public int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;// 定义音频编码（16位）
    public int bufferSize = -1;// 播放缓冲大小

    public SdkAudioPlayer(){
        bufferSize = AudioTrack.getMinBufferSize(frequence, channelInConfig, audioEncoding);
        track = new AudioTrack(AudioManager.STREAM_MUSIC, frequence, channelInConfig, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
    }

    public void play(byte[] data){
        track.write(data, 0, data.length);
    }

    public void stop(){
        track.stop();
    }
}
