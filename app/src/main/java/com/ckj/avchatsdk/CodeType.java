package com.ckj.avchatsdk;

public enum CodeType {
    forwardVideo,//发送视频数据
    forwardAudio,//发送音频数据
    forwardText,//发送文本数据
    forward,//转发数据，未确定数据类型
    forwardAvConnect,//转发音视频通话连接请求
    forwardAuConnect,//转发音频通话连接请求
    forwardDisconnect,//转发结束通话连接请求
    forwardOffline,//用户离线退出
    online,//用户用于维持在线状态
    returnUsers,//请求在线用户
    agreeConnect,//同意连接
    disagreeConnect//不同意连接
    ;
}
