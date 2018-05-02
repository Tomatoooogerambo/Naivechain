package com.block;

/**
 * Created by 越 on 2018/5/1.
 */
public class Main {
    public static void main(String[] args) {
        if(args != null && (args.length == 2 || args.length == 3)) {
            try {
                int httpPort = Integer.valueOf(args[0]);
                BlockService blockService = new BlockService();
                HTTPService httpService = new HTTPService(blockService);
                httpService.initHTTPService(httpPort);
            } catch (Exception e) {
                System.out.println("Init service error : " + e.getMessage());
            }
        }else {
            System.out.println("usage: java -jar naivechain.jar [httpPort] [p2pPort]");
        }
    }
}
