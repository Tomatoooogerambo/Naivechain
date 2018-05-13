package com.block;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 越 on 2018/5/13.
 */
public class P2PServiceTest {
    BlockService blockService = new BlockService();
    P2PService p2PService = new P2PService(blockService);

    @Test
    public void testHandleBlockChainResponse() {
        Block block = blockService.generateNextBlock(4);
        blockService.addBlock(block);
        String s = JSON.toJSONString(new Message(3, JSON.toJSONString(blockService.getBlockChain())));
        Message message =JSON.parseObject(s, Message.class);
        String data = message.getData();
        try {
            Method targetMethod = P2PService.class.getDeclaredMethod("handleBlockChainResponse",String.class);
            targetMethod.setAccessible(true);
            try {
                targetMethod.invoke(p2PService, new Object[]{data});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            System.out.println(e.toString());
        }
    }
}
