package com.ckj.avchatsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    SdkRemoteView remoteView;

    SdkLocalView localView;

    VideoSdk videoSdk=new VideoSdk();

    AudioSdk audioSdk;

    static AVSdkClient avSdkClient;

    Button startAv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remoteView=findViewById(R.id.remoteView);
        localView=findViewById(R.id.localView);
        startAv=findViewById(R.id.startAv);
        String targetId=getIntent().getStringExtra("targetId");
        if(!avSdkClient.targetIds.contains(targetId)){
            avSdkClient.targetIds.add(getIntent().getStringExtra("targetId"));
        }
        videoSdk.setRemoteView(remoteView);
        videoSdk.setLocalView(localView);
        avSdkClient.setVideoSdk(videoSdk);
        audioSdk=new AudioSdk(this);
        avSdkClient.setAudioSdk(audioSdk);
        videoSdk.openVideo();
        audioSdk.openAudio();
        startAv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avSdkClient.openAvChat();
            }
        });
    }
}
