package com.block;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 越 on 2018/5/1.
 */
public class BlockService {
    // 定义区块链
    private List<Block> blockChain;

    public BlockService() {
        this.blockChain = new ArrayList<Block>();
        generaterGenesisBlock();
    }


    /**
     * 计算生成hash值
     * @param index         区块在整个区块中的顺序
     * @param preHash       前一个区块的hash值
     * @param timeStamp     时间戳
     * @param data          区块中存放的数据
     * @return
     */
    private String calculateHash(int index,  String preHash, long timeStamp, String data) {
        StringBuilder record = new StringBuilder(index);
        record.append(preHash)
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
      int nextIndex = preBlock.getIndex() + 1;
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
        if(isValidBlock(thisBlock, getLatestBlock()) || thisBlock.getIndex() == 1)
            blockChain.add(thisBlock);
    }

    /**
     * 获得当前的区块链
     * @return
     */
    public List<Block> getBlockChain() {
        return blockChain;
    }

    public void generaterGenesisBlock() {
        blockChain.add(new Block().genesisBlock());
    }
}
