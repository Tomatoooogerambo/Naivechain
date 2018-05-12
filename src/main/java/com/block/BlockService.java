package com.block;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 越 on 2018/5/1.
 */
public class BlockService {
    // 定义区块链
    private List<Block> blockChain;

    // 创建初始块
    private Block genesisBlock = new Block(0, "0", System.currentTimeMillis(),"Hello Block","aa212344fc10ea0a2cb885078fa9bc2354e55efc81be8f56b66e4a837157662e");

    public BlockService() {
        this.blockChain = new ArrayList<Block>();
        blockChain.add(genesisBlock);
    }


    /**
     * 计算生成hash值
     * @param index         区块在整个区块中的顺序
     * @param preHash       前一个区块的hash值
     * @param timeStamp     时间戳
     * @param data          区块中存放的数据
     * @return
     */
    private String calculateHash(long index,  String preHash, long timeStamp, String data) {
        StringBuilder record = new StringBuilder();
        record.append(index)
                .append(preHash)
                .append(timeStamp)
                .append(data);
        return DigestUtils.sha256Hex(record.toString());
    }

    /**
     * 返回前一个区块
     * @return
     */
    public Block getLatestBlock() {
        return blockChain.get(blockChain.size() - 1);
    }

    /**
     * 生成下一个区块
     * @param data
     * @return
     */
    public Block generateNextBlock(String data) {
      Block preBlock = getLatestBlock();
      long nextIndex = preBlock.getIndex() + 1;
      long timeStamp = System.currentTimeMillis();
      String nextHash = calculateHash(nextIndex, preBlock.getHash(), timeStamp, data);
      return new Block(nextIndex, preBlock.getHash(), timeStamp, data, nextHash);
    }

    /**
     * 验证区块数据有效性
     * @param thisBlock
     * @param preBlock
     * @return
     */
    public boolean isValidBlock(Block thisBlock, Block preBlock) {
        if(preBlock.getIndex() + 1 != thisBlock.getIndex()) {
            System.out.println("Invalid index");
            return false;
        }else if(!preBlock.getHash().equals(thisBlock.getPreHash())) {
            System.out.println("Invalid previous hash");
            return false;
        }else {
            String hash = calculateHash(thisBlock.getIndex(), thisBlock.getPreHash(),
                                        thisBlock.getTimeStamp(), thisBlock.getData());
            if(!hash.equals(thisBlock.getHash())) {
                System.out.println("Invalid hash :" + hash);
                return false;
            }
        }
        return true;
    }

    /**
     * 添加区块进入区块链中
     * @param thisBlock
     */
    public void addBlock(Block thisBlock) {
        if(isValidBlock(thisBlock, getLatestBlock()))
            blockChain.add(thisBlock);
    }

    /**
     * 获得当前的区块链
     * @return
     */
    public List<Block> getBlockChain() {
        return blockChain;
    }

    public void setBlockChain(ArrayList<Block> blockChain) {
        this.blockChain = blockChain;
    }

    /**
     * 验证区块链是否有效
     * 1. 验证创世快是否一致
     * 2. 验证区块的链上的区块是否都有效
     * 如果以上验证都通过，则承认其实有效的区块链（但未必是最长的）
     * @param blockchainToValid
     * @return
     */
    public boolean isValidBlockChain(ArrayList<Block> blockchainToValid) {
        boolean isValidGenesis = JSON.toJSONString(blockchainToValid.get(0)) == JSON.toJSONString(genesisBlock);

        if(!isValidGenesis)
            return false;

        for(int i = 1; i < blockchainToValid.size(); i++) {
            if(!isValidBlock(blockchainToValid.get(i - 1), blockchainToValid.get(i)))
                return false;
        }
        return true;
    }

    public void chooseLongestChain(ArrayList<Block> chainToChoose) {
        if(isValidBlockChain(chainToChoose) && chainToChoose.size() > getBlockChain().size()) {
            setBlockChain(chainToChoose);
            // broadcast

            System.out.println("Update the blockchain successfully");
        }else
            System.out.println("Recivied chain is invalid, remain the intrinsic chain");
    }
}
