package com.ckj.avchatsdk;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_user);
        userListView=findViewById(R.id.userListView);
        adapter=new UserAdapter(this,userList);
        userListView.setAdapter(adapter);
        if(MainActivity.avSdkClient==null){
            MainActivity.avSdkClient=new AVSdkClient("192.168.1.8",8888);
            SimpleChatListener simpleChatListener=new SimpleChatListener(OnlineUser.this);
            MainActivity.avSdkClient.setChatListener(simpleChatListener);
            MainActivity.avSdkClient.loginOnline();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.online_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.flash){
            RequestDataPack dataPack=new RequestDataPack();
            dataPack.setType(CodeType.returnUsers);
            Object result=NetUtils.requestNet(dataPack,3000);
            if(result!=null){
                userList.clear();
                userList.addAll((List<String>)result);
                adapter.notifyDataSetChanged();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
