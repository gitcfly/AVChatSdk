package com.ckj.avchatsdk;

import java.util.List;

public class RequestDataPack {

	public List<String> targetIds;

	public String sourceId;

	public byte[] data;

	public String type="video";//audio,video,disconnect,connect
	
	public RequestDataPack(){
	}

	public RequestDataPack(String sourceId,List<String>targetIds,byte[]data){
		this.sourceId=sourceId;
		this.targetIds=targetIds;
		this.data=data;
	}
	
	public List<String> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<String> targetIds) {
		this.targetIds = targetIds;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
