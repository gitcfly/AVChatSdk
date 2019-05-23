package com.ckj.avchatsdk;

import android.util.Log;

public class NetRespCallback {

    public boolean returned=false;

    public Object result;

    public Object getResult() {
        return result;
    }

    public void call(Object result) {
        synchronized (this){
            this.returned=true;
            this.result = result;
            this.notify();
            Log.e("dyx","callback is called");
        }
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
