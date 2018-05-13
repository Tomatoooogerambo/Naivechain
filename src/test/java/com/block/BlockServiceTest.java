package com.block;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 越 on 2018/5/13.
 */
public class BlockServiceTest {
    private BlockService blockService = new BlockService();

    /**
     * 测试生成下一个区块是否成功
     */
    @Test
    public void testGenerateNextBlock() {
        Block latest = blockService.getLatestBlock();
        int difficulty = Math.max(latest.getDifficult(), 2);
        Block block = blockService.generateNextBlock(difficulty);
        System.out.println(block);
    }

    /**
     * 测试生成的区块值
     */
    @Test
    public void testCalculateHash() {
        Block latest = blockService.getLatestBlock();
        String s = blockService.calculateHash(latest.getIndex(),latest.getPreHash(), latest.getTimeStamp(),latest.getData(), latest.getNonce(), latest.getDifficult());
        System.out.println(s);
        System.out.println(s.length());
        String s1 = "aa212344fc10ea0a2cb885078fa9bc2354e55efc81be8f56b66e4a837157662e";
        System.out.println(s1.length());
    }

    /**
     * 测试生成前导0个数的方法是否正确
     */
    @Test
    public void testFindLeadingZero() {
        try {
            Method method = BlockService.class.getDeclaredMethod("findLeadingZero", String.class);
            method.setAccessible(true);
            String s = "000002344fc10ea0a2cb885078fa9bc2354e55efc81be8f56b66e4a837157662e";
            try {
                Object o = method.invoke(blockService, new Object[]{s});
                System.out.println((int)o);
            }catch (InvocationTargetException e) {
                System.out.println(e.toString());
            }catch (NullPointerException e) {
                System.out.println(e.toString());
            }catch (ExceptionInInitializerError e) {
                System.out.println(e.toString());
            } catch (IllegalAccessException e) {
                System.out.println(e.toString());
            }
        } catch (NoSuchMethodException e) {
            System.out.println(e.toString());
        }

    }

    @Test
    public void testAddBlock() {
        Block block = blockService.generateNextBlock(2);
        System.out.println("The new Block is : \n" +
                            block);

        blockService.addBlock(block);

        System.out.println("The new formed chain is : \n");
        for(Block block1 : blockService.getBlockChain()) {
            System.out.println(block1);
        }
    }
}
