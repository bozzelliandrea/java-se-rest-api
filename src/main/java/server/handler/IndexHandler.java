package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Server;
import server.model.Response;
import server.util.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class IndexHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new IndexHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Logger.forRequest(exchange.getRequestMethod(), exchange.getRequestURI().getPath());

        if (exchange.getRequestMethod().equals("GET")) {
            try {
                byte[] pageContent;

                if (exchange.getRequestURI().getPath().equals(Server.rootPath)) {
                    pageContent = Files.readAllBytes(Paths.get(Objects.requireNonNull(
                            IndexHandler.class.getResource("/public/index.html")).getPath()));
                } else {
                    pageContent = Files.readAllBytes(Paths.get(Objects.requireNonNull(
                            IndexHandler.class.getResource("/public" + exchange.getRequestURI().getPath())).getPath()));
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
                byte[] pageContent = Files.readAllBytes(Paths.get(IndexHandler.class.getResource("/public/404.html").getPath()));
                Response.html(exchange, pageContent);
                Logger.forResponse("404.html");
            }
        }
    }
}
