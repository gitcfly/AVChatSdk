package com.ckj.avchatsdk.api;

import java.util.List;

public interface AvChatSdk {

    public void setCodec(DataCodec codec);

    public void switchCamera();

    public void SendVedio(byte[] videoData, List<String> targetIds);

    public void SendAudio(byte[] AudioData,List<String> targetIds);

    public void SendAV(byte[] AudioData,byte[] videoData,List<String> targetIds);

}
