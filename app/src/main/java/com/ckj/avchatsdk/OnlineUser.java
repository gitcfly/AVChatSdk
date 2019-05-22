package com.ckj.avchatsdk;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OnlineUser extends AppCompatActivity {

    ListView userListView;

    List<String> userList=new ArrayList<>();

    UserAdapter adapter;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            JSONObject result= JSON.parseObject((String)msg.obj);
            userList.clear();
            userList.addAll((List<String>)result.get("userList"));
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_user);
        userListView=findViewById(R.id.userListView);
        adapter=new UserAdapter(this,userList);
        userListView.setAdapter(adapter);
        if(MainActivity.avSdkClient==null){
            MainActivity.avSdkClient=new AVSdkClient("192.168.1.8",8888);
            MainActivity.avSdkClient.openAvChat();
        }
        JSONObject object=new JSONObject();
        object.put("code","getUserList");
        NetUtils.requestNet(handler,object);

    }
}
