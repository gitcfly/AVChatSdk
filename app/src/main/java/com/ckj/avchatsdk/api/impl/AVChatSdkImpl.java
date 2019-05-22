package com.ckj.avchatsdk.api.impl;

import com.ckj.avchatsdk.VideoSdk;
import com.ckj.avchatsdk.api.AvChatSdk;
import com.ckj.avchatsdk.api.DataCodec;

import java.util.List;

public class AVChatSdkImpl implements AvChatSdk {

    public static AvChatSdk sdkinstence;

    public VideoSdk videoSdk;

    public DataCodec codec;

    @Override
    public void setCodec(DataCodec codec) {
        this.codec=codec;
    }

    @Override
    public void switchCamera() {

    }

    @Override
    public void SendVedio(byte[] videoData, List<String> targetIds) {

    }

    @Override
    public void SendAudio(byte[] AudioData, List<String> targetIds) {

    }

    @Override
    public void SendAV(byte[] AudioData, byte[] videoData, List<String> targetIds) {

    }
}
