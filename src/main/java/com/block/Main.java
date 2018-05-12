package com.block;

/**
 * Created by 越 on 2018/5/1.
 */
public class Main {
    public static void main(String[] args) {
        if(args != null && (args.length == 2 || args.length == 3)) {
            try {
                // 获得http 以及 p2p端口
                int httpPort = Integer.valueOf(args[0]);
                int p2pPort = Integer.valueOf(args[1]);

                BlockService blockService = new BlockService();
                P2PService p2PService = new P2PService(blockService);

                // 对p2p、http初始化
                p2PService.initP2PServer(p2pPort);
                if(args.length == 3 && args[2] != null) {
                    p2PService.connectToPeer(args[2]);
                }
                HTTPService httpService = new HTTPService(blockService, p2PService);
                httpService.initHTTPService(httpPort);
            } catch (Exception e) {
                System.out.println("Init service error : " + e.getMessage());
            }
        }else {
            System.out.println("usage: java -jar naivechain.jar [httpPort] [p2pPort]");
        }
    }
}
