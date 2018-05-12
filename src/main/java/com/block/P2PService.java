package com.block;

import com.alibaba.fastjson.JSON;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Created by 越 on 2018/5/12.
 */
public class P2PService {
    private ArrayList<WebSocket> sockets;
    private BlockService blockService;
    private final static int QUERY_LATEST = 0;
    private final static int QUERY_ALL = 1;
    private final static int RESPONSE_BLOCKCHAIN = 2;

    public P2PService(BlockService blockService) {
        this.blockService = blockService;
        this.sockets = new ArrayList<WebSocket>();
    }

    public void initP2PServer(int port) {
        final WebSocketServer socketServer = new WebSocketServer(new InetSocketAddress(port)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                //对外查找当前网络中区块的长度信息
                //装配这个webSocket
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                //显示退出这个p2pService
                //将这个webSocket删掉
            }

            @Override
            public void onMessage(WebSocket webSocket, String s) {
                //对当前的这个webSocket携带的信息进行处理
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                //显示当前的webSocket出现问题
                // 将这个webSocket从sockets中移除
            }
        };
        socketServer.start();
        System.out.println("start p2p server on port: " + port);
    }

    private void handelMessage(WebSocket webSocket, String s) {
        try {
            Message message = JSON.parseObject(s, Message.class);
            System.out.println("Reciver message: " + JSON.toJSONString(message));
            switch(message.getType()) {
                case QUERY_LATEST:
                    // 如果另一个节点发过来是查询最近一块块的请求，进行回复

                    break;
                case QUERY_ALL:
                    // 如果另一个节点发过来的是查询本节点的区块链请求， 进行回复

                    break;
                case RESPONSE_BLOCKCHAIN:
                    // 是收到其他节点给我传过来的回复，进行处理

                    break;
            }
        }catch (Exception e) {
            System.out.println("handle message is error: " + e.getMessage());
        }
    }


}
