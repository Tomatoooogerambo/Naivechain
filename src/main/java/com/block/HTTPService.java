package com.block;

import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.java_websocket.WebSocket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by 越 on 2018/5/1.
 */
public class HTTPService {
    private BlockService blockService;
    private P2PService p2PService;

    public HTTPService(BlockService blockService, P2PService p2PService) {
        this.blockService = blockService;
        this.p2PService = p2PService;
    }

    public void initHTTPService(int port) {
        try{
            Server server = new Server(port);
            System.out.println("listening http port on: " + port);
            ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            contextHandler.setContextPath("/");
            server.setHandler(contextHandler);
            contextHandler.addServlet(new ServletHolder(new BlockServlet()), "/blocks");
            contextHandler.addServlet(new ServletHolder(new AddPeerServlet()), "/addPeer");
            contextHandler.addServlet(new ServletHolder(new PeersServlet()), "/peers");
            server.start();
            server.join();
        }catch(Exception e) {
            System.out.println("init http server error: " + e.getMessage());
        }
    }

    /**
     *查看区块链的Servlet
     */
    private class BlockServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().println(JSON.toJSONString(blockService.getBlockChain()));
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doPost(req, resp);
        }
    }

    /**
     * 添加节点的Servlet
     */
    private class AddPeerServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doGet(req,resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
            String peer = req.getParameter("peer");
            p2PService.connectToPeer(peer);
            resp.getWriter().println("\n"+"Successfully add the new peer");
        }
    }

    /**Ht
     * 查看所有加点的Servlet
     */
    private class PeersServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
            for(WebSocket socket : p2PService.getSockets()) {
                InetSocketAddress remoteAddress = socket.getRemoteSocketAddress();
                resp.getWriter().println("\n" + remoteAddress.getHostName() + "  : " + remoteAddress.getPort());
            }
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            doGet(req, resp);
        }
    }
}
