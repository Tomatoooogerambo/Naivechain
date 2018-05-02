package com.block;

/**
 * Created by 越 on 2018/5/1.
 */
public class Block {
    // 记录区块的位置
    private int index;

    // 区块中记录的数据
    private String data;

    // 时间戳
    private long timeStamp;

    // 区块散列值
    private String hash;

    // 前一个区块的散列值
    private String preHash;

    public Block() {}

    public Block(int index, String preHash, long timeStamp, String data, String hash) {
        this.index = index;
        this.preHash = preHash;
        this.timeStamp = timeStamp;
        this.data = data;
        this.hash = hash;
    }

    public Block genesisBlock() {
        return new Block(1, "0", System.currentTimeMillis(), "Hello Block", "aa212344fc10ea0a2cb885078fa9bc2354e55efc81be8f56b66e4a837157662e");
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
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
}
