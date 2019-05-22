package com.ckj.avchatsdk.api;

public interface AudioChatListener {

    public void onCaptureAudio(byte [] originalData);

    public void onReciveAudio(byte[] encodedData,String sourceId);
}
