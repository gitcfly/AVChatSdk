package com.ckj.avchatsdk;

import android.hardware.Camera;
import android.util.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class VideoSdk implements Camera.PreviewCallback {
    public static boolean isOpenSelfVideo = false;
    public static boolean isOpenRemoteVideo = false;
    public static int videoHight=240;
    public static int videoWidth=320;
    private SdkLocalView localView;
    private SdkRemoteView remoteView;
    private CameraType cameraType=CameraType.Front;
    private Camera mCamera;
    private BlockingQueue<byte[]> videoQueue=new ArrayBlockingQueue(1);

    private void startVideoChat() throws RuntimeException{
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        if(numberOfCameras<1){
            throw new RuntimeException("没有相机");
        }
        mCamera = Camera.open(cameraType.getType());
        if (mCamera != null) {
            mCamera.setPreviewCallback(this);
            Camera.Parameters params = mCamera.getParameters();
            params.setPictureSize(videoWidth,videoHight);
            params.setPreviewSize(videoWidth,videoHight);
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            mCamera.setParameters(params);
            localView.initLocalView(mCamera);
        }
    }

    public void stopVideoChat(){
        if(mCamera!=null){
            mCamera.release();
            mCamera.stopPreview();
        }
    }

    public static Camera.Size minVideoSize(List<Camera.Size> supportedVideoSizes) {
        //升序排列
        Collections.sort(supportedVideoSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.width > rhs.width) {
                    return 1;
                } else if (lhs.width == rhs.width) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return supportedVideoSizes.get(0);
    }

    public SdkRemoteView getRemoteView() {
        return remoteView;
    }

    public void setRemoteView(SdkRemoteView remoteView) {
        this.remoteView = remoteView;
    }

    public CameraType getCameraType() {
        return cameraType;
    }

    public void setCameraType(CameraType cameraType) {
        this.cameraType = cameraType;
    }

    public Camera getmCamera() {
        return mCamera;
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }

    public int getVideoHight() {
        return videoHight;
    }

    public void setVideoHight(int videoHight) {
        this.videoHight = videoHight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public SdkLocalView getLocalView() {
        return localView;
    }

    public void setLocalView(SdkLocalView localView) {
        this.localView = localView;
    }

    public byte[] getVideoFrame(){
        return videoQueue.poll();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(videoQueue.offer(data)){
            Log.e("dyx","视频采集成功");
        }else {
            Log.e("dyx","视频采集失败");
        }
    }

    public void closeSelf(){
        if(isOpenSelfVideo){
            isOpenSelfVideo=false;
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
        }
        if(isOpenRemoteVideo==false){
            AVSdkClient.isOpenVideo=false;
        }
    }

    public void closeRemote(){
        isOpenRemoteVideo=false;
        if(isOpenSelfVideo==false){
            AVSdkClient.isOpenVideo=false;
        }
    }

    public void finishVideo(){
        AVSdkClient.isOpenVideo=false;
        closeSelf();
        closeRemote();
    }

    public void openSelf(){
        if(isOpenSelfVideo==false){
            isOpenSelfVideo=true;
            AVSdkClient.isOpenVideo=true;
            startVideoChat();
        }
    }

    public void openRemote(){
        AVSdkClient.isOpenVideo=true;
        isOpenRemoteVideo=true;
    }

    public void openVideo(){
        openSelf();
        openRemote();
    }

    public void swichCamera(CameraType type){
        closeSelf();
        this.cameraType=type;
        openSelf();
    }

}
