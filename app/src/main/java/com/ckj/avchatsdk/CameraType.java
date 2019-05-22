package com.ckj.avchatsdk;

public enum  CameraType {

    Front(1) ,Back(0);

    private int type;

    CameraType(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }
}
