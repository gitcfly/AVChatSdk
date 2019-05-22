package com.ckj.avchatsdk;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;

public class SdkLocalView extends TextureView implements TextureView.SurfaceTextureListener {

    Camera camera;

    public SdkLocalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initLocalView(Camera mCamera){
        camera=mCamera;
        this.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            camera.setPreviewTexture(surface);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        }catch (Exception e){
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if(camera!=null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
