package com.ckj.avchatsdk;


import android.os.Handler;
import android.os.Message;


import com.alibaba.fastjson.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetUtils {

    public static void requestNet(final Handler handler,final JSONObject request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket=new Socket("192.168.1.8",8080);
                    BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
                    out.write(request.toString());
                    out.flush();
                    socket.shutdownOutput();
                    BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                    String line=null;
                    StringBuilder builder=new StringBuilder();
                    while ((line=in.readLine())!=null){
                        builder.append(line);
                    }
                    Message message=new Message();
                    message.obj=builder.toString();
                    handler.sendMessage(message);
                    out.close();
                    in.close();
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
