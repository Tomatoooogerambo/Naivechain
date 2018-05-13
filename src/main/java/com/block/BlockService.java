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
    private Block genesisBlock = new Block(0, "0", Long.parseLong("1526203339735"),"Hello Block","aa212344fc10ea0a2cb885078fa9bc2354e55efc81be8f56b66e4a837157662e",0,0);

    public BlockService() {
        this.blockChain = new ArrayList<Block>();
        blockChain.add(genesisBlock);
    }


    /**
     * 返回最新一个区块
     * @return
     */
    public Block getLatestBlock() {
        return blockChain.get(blockChain.size() - 1);
    }

    /**
     * 添加区块进入区块链中
     * @param thisBlock
     */
    public void addBlock(Block thisBlock) {
        if(isValidBlock(getLatestBlock(),thisBlock )) {
            blockChain.add(thisBlock);
            System.out.println("Successfully add the new Block chain");
        }else {
            System.out.println("The new Block is invalid, drop it");
        }
    }

    /**
     * 获得当前的区块链
     * @return
     */
    public List<Block> getBlockChain() {
        return blockChain;
    }

    public void setBlockChain(List<Block> blockChain) {
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
    public boolean isValidBlockChain(List<Block> blockchainToValid) {
        boolean isValidGenesis = JSON.toJSONString(blockchainToValid.get(0)).equals(JSON.toJSONString(genesisBlock));
        if(!isValidGenesis)
            return false;

        for(int i = 1; i < blockchainToValid.size(); i++) {
            if(!isValidBlock(blockchainToValid.get(i - 1), blockchainToValid.get(i)))
                return false;
        }
        return true;
    }

    /**
     * key!
     * 验证区块数据有效性
     * 本分支采用pow共识机制
     * 使用验证总难度的方式来进行验证
     * 这是一个关键方法
     * @param thisBlock
     * @param preBlock
     * @return
     */
    public boolean isValidBlock(Block preBlock, Block thisBlock) {
        if(preBlock.getIndex() + 1 != thisBlock.getIndex()) {
            System.out.println("Invalid index");
            return false;
        }else if(!preBlock.getHash().equals(thisBlock.getPreHash())) {
            System.out.println("Invalid previous hash");
            return false;
        }else {
            String hash = calculateHash(thisBlock.getIndex(), thisBlock.getPreHash(),
                    thisBlock.getTimeStamp(), thisBlock.getData(), thisBlock.getNonce(), thisBlock.getDifficult());
            if(!hash.equals(thisBlock.getHash())) {
                System.out.println("Invalid hash :" + hash);
                return false;
            }else{
                // 验证难度值
                int leadingZero = findLeadingZero(hash);
                if(leadingZero < preBlock.getDifficult()){
                    System.out.println("Invalid difficulty!: " + leadingZero);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 选择最长的有效链
     * @param chainToChoose
     */
    public void chooseLongestChain(List<Block> chainToChoose) {
        if(chainToChoose.size() > this.getBlockChain().size()) {
            setBlockChain(chainToChoose);
            // broadcast

            System.out.println("Update the blockchain successfully");
        }else
            System.out.println("Recivied chain length is invalid,  so remain the intrinsic chain");
    }
    /**
     * 计算生成hash值
     * @param index         区块在整个区块中的顺序
     * @param preHash       前一个区块的hash值
     * @param timeStamp     时间戳
     * @param data          区块中存放的数据
     * @return
     */
    public String calculateHash(long index,  String preHash, long timeStamp, String data, int nonce,int difficulty) {
        StringBuilder record = new StringBuilder();
        record.append(index)
                .append(preHash)
                .append(timeStamp)
                .append(data)
                .append(nonce)
                .append(difficulty);
        return DigestUtils.sha256Hex(record.toString());
    }

    /**
     * key!
     * 生成下一个区块
     * 这是体现区块链技术的一个关键位置
     * 本分支使用pow的证明方法生成下一个区块
     * 相应的使用验证工作量的机制来替代最长链的方式来验证接收到的链，防止低难度长脸攻击
     *
     * @return
     */
    public Block generateNextBlock(int difficult) {
        Block newBlock = new Block();
        Block latest = getLatestBlock();
        newBlock.setIndex(latest.getIndex() + 1);
        newBlock.setData("This is the " + (latest.getIndex() + 1) +"th block");
        newBlock.setPreHash(latest.getHash());
        newBlock.setTimeStamp(System.currentTimeMillis());
        newBlock.setDifficult(difficult);
        int nonce = 0;
        newBlock.setNonce(nonce);
        while(true) {
            String newHash = calculateHash(newBlock.getIndex(),newBlock.getPreHash(),newBlock.getTimeStamp(),newBlock.getData(),newBlock.getNonce(),newBlock.getDifficult());
            if(findLeadingZero(newHash) >= difficult) {
                newBlock.setHash(newHash);
                break;
            }
            else
                newBlock.setNonce(newBlock.getNonce() + 1);
        }
        return newBlock;
    }

    /**
     * 计算难度值
     * @param hash
     * @return
     */
    private int findLeadingZero(String hash) {
        char[] chars = hash.toCharArray();
        int leadingZero = 0;
        if(chars == null || chars[0] != '0')
            return 0;
        while(chars[++leadingZero] == '0');
        return leadingZero;
    }

}
