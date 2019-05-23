package com.ckj.avchatsdk;

import java.util.List;
import java.util.Map;

public class RequestDataPack {

	public String reqid;

	public List<String> targetIds;

	public String sourceId;

	public byte[] data;

	public CodeType type;

	public Object extra;//额外序列化数据对象

	public RequestDataPack(){
	}

	public RequestDataPack(String sourceId,CodeType codeType){
		this.sourceId=sourceId;
		this.type=codeType;
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

	public CodeType getType() {
		return type;
	}

	public void setType(CodeType type) {
		this.type = type;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	public String getReqid() {
		return reqid;
	}

	public void setReqid(String reqid) {
		this.reqid = reqid;
	}
}
