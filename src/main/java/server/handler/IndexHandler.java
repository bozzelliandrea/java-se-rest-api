package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Server;
import server.model.Response;
import server.util.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IndexHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new IndexHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Logger.forRequest(exchange.getRequestMethod(), exchange.getRequestURI().getPath());

        if (exchange.getRequestURI().getPath().equals("/favicon.ico") && exchange.getRequestMethod().equals("GET")) {

            byte[] pageContent = Files.readAllBytes(Paths.get(IndexHandler.class.getResource("/public/favicon.ico").getPath()));
            Response.icon(exchange, pageContent);
            Logger.forResponse("favicon.ico");
        }

        if (exchange.getRequestURI().getPath().equals(Server.rootPath) && exchange.getRequestMethod().equals("GET")) {
            byte[] pageContent = Files.readAllBytes(Paths.get(IndexHandler.class.getResource("/public/index.html").getPath()));
            Response.html(exchange, pageContent);
            Logger.forResponse("index.html");
        } else {
            byte[] pageContent = Files.readAllBytes(Paths.get(IndexHandler.class.getResource("/public/404.html").getPath()));
            Response.html(exchange, pageContent);
            Logger.forResponse("404.html");
        }
    }
}
