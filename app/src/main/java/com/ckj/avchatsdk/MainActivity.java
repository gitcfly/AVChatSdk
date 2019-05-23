package com.ckj.avchatsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SdkRemoteView remoteView;

    SdkLocalView localView;

    VideoSdk videoSdk;

    AudioSdk audioSdk;

    static AVSdkClient avSdkClient;

    Button closeSelf;
    Button openSelf;
    Button requestAv;
    Button closeAv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remoteView=findViewById(R.id.remoteView);
        localView=findViewById(R.id.localView);
        openSelf=findViewById(R.id.openSelf);
        requestAv=findViewById(R.id.requestAv);
        closeAv=findViewById(R.id.closeAv);
        closeSelf=findViewById(R.id.closeSelf);
        String targetId=getIntent().getStringExtra("targetId");
        if(!avSdkClient.targetIds.contains(targetId)){
            avSdkClient.targetIds.add(getIntent().getStringExtra("targetId"));
        }
        videoSdk=new VideoSdk(localView);
        videoSdk.setRemoteView(remoteView);
        avSdkClient.setVideoSdk(videoSdk);
        audioSdk=new AudioSdk();
        avSdkClient.setAudioSdk(audioSdk);
        videoSdk.openVideo();
        audioSdk.openAudio();
        requestAv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avSdkClient.requestAvChat(avSdkClient.targetIds);
            }
        });
        closeAv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avSdkClient.closeAvChat();
            }
        });
    }
}
