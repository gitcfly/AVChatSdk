package com.ckj.avchatsdk.api;

public interface VideoChatListener {

    public void onCaptureVideo(byte [] originalData);

    public void onReciveVideo(byte[] encodedData,String sourceId);

}
