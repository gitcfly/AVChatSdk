package com.ckj.avchatsdk;

public class ReqCallBack {

    boolean iscalled;

    Object result;

    public boolean isIscalled() {
        return iscalled;
    }

    public void setIscalled(boolean iscalled) {
        this.iscalled = iscalled;
    }

    public Object getResult() {
        return result;
    }

    public void call(Object result) {
        this.result = result;
    }
}
