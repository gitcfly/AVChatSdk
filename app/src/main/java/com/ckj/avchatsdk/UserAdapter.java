package com.ckj.avchatsdk;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.ls.LSInput;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    List<String> userList;
    Activity activity;
    UserAdapter(Activity activity,List<String> userList){
        this.userList=userList;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public String getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,null);
        TextView userName=view.findViewById(R.id.userId);
        userName.setText(getItem(position));
        TextView avchat=view.findViewById(R.id.avchat);
        avchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,MainActivity.class);
                intent.putExtra("targetId",getItem(position));
                activity.startActivity(intent);
            }
        });
        return view;
    }
}
