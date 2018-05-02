package com.block;

import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 越 on 2018/5/1.
 */
public class HTTPService {
    private BlockService blockService;

    public HTTPService(BlockService blockService) {
        this.blockService = blockService;
    }

    public void initHTTPService(int port) {
        try{
            Server server = new Server(port);
            System.out.println("listening http port on: " + port);
            ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            contextHandler.setContextPath("/");
            server.setHandler(contextHandler);
            contextHandler.addServlet(new ServletHolder(new BlockServlet()), "/blocks");
            server.start();
            server.join();
        }catch(Exception e) {
            System.out.println("init http server error: " + e.getMessage());
        }
    }

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
}
