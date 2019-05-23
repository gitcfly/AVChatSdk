package com.ckj.avchatsdk;

import java.util.List;

public interface ChatSdkListener {

    public Boolean onReceivedAvReq(String sourceId, List<String> targetIds);

    public Boolean onReceivedAuReq(String sourceId, List<String> targetIds);

    public void onAgreeChat(String remoteId);

    public void onDisAgreeChat(String remoteId);

    public void onRespTimeOut(String remoteId);

}
