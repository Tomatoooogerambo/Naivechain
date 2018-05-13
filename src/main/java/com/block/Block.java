package com.block;

import com.alibaba.fastjson.JSON;

/**
 * Created by 越 on 2018/5/1.
 */
public class Block {
    // 记录区块的位置
    private long index;

    // 区块中记录的数据
    private String data;

    // 时间戳
    private long timeStamp;

    // 区块散列值
    private String hash;

    // 前一个区块的散列值
    private String preHash;

    // 区块的目标难度值
    private int difficult;

    // 为了寻找到该区块所需要的nonce
    private int nonce;

    public Block() {}

    public Block(long index, String preHash, long timeStamp, String data, String hash, int difficult, int nonce) {
        this.index = index;
        this.preHash = preHash;
        this.timeStamp = timeStamp;
        this.data = data;
        this.hash = hash;
        this.difficult = difficult;
        this.nonce = nonce;
    }


    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHash() {
        return hash;
}

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
