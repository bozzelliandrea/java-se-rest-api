package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Server;
import server.util.IOUtils;
import server.util.Logger;
import server.util.Response;

import java.io.IOException;

public class IndexHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new IndexHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] pageContent;

        if (exchange.getRequestMethod().equals("GET")) {
            try {

                if (exchange.getRequestURI().getPath().equals(Server.rootPath)) {
                    pageContent = IOUtils.getByteForFile("/public/index.html");
                } else {
                    pageContent = IOUtils.getByteForFile("/public" + exchange.getRequestURI().getPath());
                }

                if (exchange.getRequestURI().getPath().endsWith(".css")) {
                    Response.css(exchange, pageContent);
                }

                if (exchange.getRequestURI().getPath().endsWith(".ico"))
                    Response.icon(exchange, pageContent);
                else
                    Response.html(exchange, pageContent);

                Logger.forResponse(exchange.getRequestURI().getPath());
            } catch (NullPointerException npe) {
                pageContent = IOUtils.getByteForFile("/public/404.html");
                Response.html(exchange, pageContent);
                Logger.forResponse("404.html");
            }
        }
    }
}
