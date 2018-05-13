package com.block;

import com.alibaba.fastjson.JSON;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 越 on 2018/5/12.
 */
public class P2PService {
    private ArrayList<WebSocket> sockets;
    private BlockService blockService;
    private final static int QUERY_LATEST = 0;          // 请求 最新区块块
    private final static int QUERY_ALL = 1;             // 请求 全部区块
    private final static int RESPONSE_LATEST = 2;       // 回复 最近区块
    private final static int RESPONSE_BLOCKCHAIN = 3;   // 回复 所有区块

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
                write(webSocket,queryChainLengthMsg());
                sockets.add(webSocket);
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                //显示退出这个p2pService
                System.out.println("connection failed to peer:" + webSocket.getRemoteSocketAddress());
                //将这个webSocket删掉
                sockets.remove(webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String s) {
                //对当前的这个webSocket携带的信息进行处理
                handelMessage(webSocket,s);
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                //显示当前的webSocket出现问题
                // 将这个webSocket从sockets中移除
                System.out.println("connection is error to peer:" + webSocket.getRemoteSocketAddress());
                sockets.remove(webSocket);
            }
        };
        socketServer.start();
        System.out.println("start p2p server on port: " + port);
    }

    /**
     * 处理得到的消息
     * @param webSocket
     * @param s
     */
    private void handelMessage(WebSocket webSocket, String s) {
        try {
            Message message = JSON.parseObject(s, Message.class);
            System.out.println("Reciver message: " + JSON.toJSONString(message));
            switch(message.getType()) {
                case QUERY_LATEST:
                    // 如果另一个节点发过来是查询最近一块块的请求，进行回复
                    write(webSocket, responseLatestBlockMsg());
                    break;
                case QUERY_ALL:
                    // 如果另一个节点发过来的是查询本节点的区块链请求， 进行回复
                    write(webSocket, responseChainMsg());
                    break;
                case RESPONSE_LATEST:
                    handleLatestBlock(message.getData());
                    break;
                case RESPONSE_BLOCKCHAIN:
                    // 是收到其他节点给本节点传过来的回复，进行处理
                    handleBlockChainResponse(message.getData());
                    break;
            }
        }catch (Exception e) {
            System.out.println("handle message is error: " + e.getMessage());
        }
    }

    /**
     * 对收到的最近一块区块进行处理
     * 如果收到的区块的索引大于目前链中最新一块的索引，广播全链请求
     * @param message
     */
    private void handleLatestBlock(String message) {
        List<Block> blocks = JSON.parseArray(message, Block.class);
        Block block = blocks.get(0);
        if(block.getIndex() > blockService.getLatestBlock().getIndex())
            broadcast(queryAllMsg());
    }
    /**
     * 对收到回复的消息进行处理
     * 1. 按照收到的消息得到最近的一块区块，进行验证和比较
     *      -如果验证成功且新链更长，替换区块链并且进行广播
     *      -如果验证失败，则保留原链
     */
    private void handleBlockChainResponse(String message) {
        List<Block> reciveBlocks = JSON.parseArray(message, Block.class);
        Collections.sort(reciveBlocks, new Comparator<Block>() {
            @Override
            public int compare(Block o1, Block o2) {
                return (int)(o1.getIndex() - o2.getIndex());
            }
        });
        System.out.println(reciveBlocks.get(1));
        if(blockService.isValidBlockChain(reciveBlocks)) {
            blockService.chooseLongestChain(reciveBlocks);
            broadcast(responseLatestBlockMsg());
        }
        else
            System.out.println("received chain is inValid, drop it \n"
                                + "retain the intrinsic chain");
    }

    public void connectToPeer(String peer) {
        try{
            final WebSocketClient webSocketClient = new WebSocketClient(new URI(peer)) {
                final WebSocket socket = this.getConnection();
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    write(socket, queryChainLengthMsg());
                    sockets.add(socket);
                }

                @Override
                public void onMessage(String s) {
                    handelMessage(socket, s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("client socket connection is closed");
                    sockets.remove(socket);
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("client connection is error");
                    sockets.remove(socket);
                }
            };
            webSocketClient.connect();
        }catch (URISyntaxException e) {
            System.out.println("p2p connection is error: " + e.getMessage());
        }
    }
    /**
     * 广播消息
     * @param message
     */
    public void broadcast(String message) {
        for(WebSocket socket : sockets)
            this.write(socket, message);
    }

    /**
     * 发送消息
     * @param webSocket
     * @param message
     */
     private void write(WebSocket webSocket, String message) {
        webSocket.send(message);
    }

    /**
     * 发送请求查询Blockchain的长度
     * @return
     */
    private String queryChainLengthMsg() {
        return JSON.toJSONString(new Message(QUERY_LATEST));
    }

    /**
     * 发送请求所有BlockChain的编号
     * @return
     */
    private String queryAllMsg() {
        return JSON.toJSONString(new Message(QUERY_ALL));
    }

    /**
     * 发送回复消息的编号以及整个区块链数据
     * @return
     */
    private String responseChainMsg() {
        return JSON.toJSONString(new Message(RESPONSE_BLOCKCHAIN, JSON.toJSONString(blockService.getBlockChain())));
    }

    /**
     * 主动广播消息 权限设置为public
     * 发送回复消息编号以及最新一个区块的数据
     * @return
     */
    public String responseLatestBlockMsg() {
        Block[] block = {blockService.getLatestBlock()};
        return JSON.toJSONString(new Message(RESPONSE_LATEST, JSON.toJSONString(block)));
    }

    public List<WebSocket> getSockets() {
        return sockets;
    }
}
