package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

public class IndexHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new IndexHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("REGISTER CALL METHOD: " + exchange.getRequestMethod() + " | " +
                "REQUESTED PATH: " + exchange.getRequestURI().getPath() + " | " +
                ZonedDateTime.now());

        if (exchange.getRequestURI().getPath().equals(Server.rootPath) && exchange.getRequestMethod().equals("GET")) {
            byte[] pageContent = Files.readAllBytes(Paths.get(IndexHandler.class.getResource("/public/index.html").getPath()));

            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, pageContent.length);
            exchange.getResponseBody().write(pageContent);
            exchange.getResponseBody().close();
            return;
        } else {
            byte[] pageContent = Files.readAllBytes(Paths.get(IndexHandler.class.getResource("/public/404.html").getPath()));

            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, pageContent.length);
            exchange.getResponseBody().write(pageContent);
            exchange.getResponseBody().close();
        }
    }
}
