package server.model;

import com.sun.net.httpserver.HttpExchange;
import server.util.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Response {

    public static void json(HttpExchange exchange, byte[] stream) throws IOException {
        json(exchange, stream, 200);
    }

    public static void json(HttpExchange exchange, byte[] stream, int code) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, stream.length);
        exchange.getResponseBody().write(stream);
        exchange.getResponseBody().close();
    }

    public static void html(HttpExchange exchange, byte[] stream) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, stream.length);
        exchange.getResponseBody().write(stream);
        exchange.getResponseBody().close();
    }

    public static void icon(HttpExchange exchange, byte[] stream) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "image/x-icon");
        exchange.sendResponseHeaders(200, stream.length);
        exchange.getResponseBody().write(stream);
        exchange.getResponseBody().close();
    }

    public static void e404(HttpExchange exchange, String message) throws IOException {
        byte[] stream = String.format("{ \"message\": \"%s\" }", message).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(404, stream.length);
        exchange.getResponseBody().write(stream);
        exchange.getResponseBody().close();
        Logger.forResponseError("Bad request received from client");
    }

    public static void e500(HttpExchange exchange, String message) throws IOException {
        byte[] stream = String.format("{ \"message\": \"%s\" }", message).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(500, stream.length);
        exchange.getResponseBody().write(stream);
        exchange.getResponseBody().close();
        Logger.forResponseError("Internal server error " + message);
    }

    public static void e501(HttpExchange exchange, String method) throws IOException {
        byte[] stream = String.format("{ \"message\": \"%s not implemented\" }", method).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(501, stream.length);
        exchange.getResponseBody().write(stream);
        exchange.getResponseBody().close();
        Logger.forResponseError("Requested method is not implemented");
    }
}
