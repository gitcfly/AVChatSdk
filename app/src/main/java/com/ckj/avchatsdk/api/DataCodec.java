package com.ckj.avchatsdk.api;

public interface DataCodec {

    public byte[] encodeData(byte[] data);

    public byte[] decodeData(byte[] data);

}
