package server.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Response {

    public static void json(HttpExchange exchange, byte[] stream) {
        json(exchange, stream, 200);
    }

    public static void json(HttpExchange exchange, byte[] stream, int code) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        flush(exchange, stream, code);
    }

    public static void html(HttpExchange exchange, byte[] stream) {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        flush(exchange, stream);
    }

    public static void icon(HttpExchange exchange, byte[] stream) {
        exchange.getResponseHeaders().add("Content-Type", "image/x-icon");
        flush(exchange, stream);
    }

    public static void css(HttpExchange exchange, byte[] stream) {
        exchange.getResponseHeaders().add("Content-Type", "text/css");
        flush(exchange, stream);
    }

    public static void e400(HttpExchange exchange, String message) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        flush(exchange, errorMessage(message), 400);
        Logger.forResponseError("Bad request received from client");
    }

    public static void e401(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("WWW-Authenticate", "Basic realm=CenterAppRealm");
        flush(exchange, errorMessage("Unauthorized"), 401);
        Logger.forResponseError("Request Unauthorized");
    }

    public static void e404(HttpExchange exchange, String message) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        flush(exchange, errorMessage(message), 404);
        Logger.forResponseError("Requested resource from client not found");
    }

    public static void e409(HttpExchange exchange, String message) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        flush(exchange, errorMessage(message), 409);
        Logger.forResponseError("Resource conflict for input request");
    }

    public static void e500(HttpExchange exchange, String message) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        flush(exchange, errorMessage(message), 500);
        Logger.forResponseError("Internal server error " + message);
    }

    public static void e501(HttpExchange exchange, String method) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        flush(exchange, errorMessage(method + " not implemented"), 501);
        Logger.forResponseError("Requested method is not implemented");
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    private static void flush(HttpExchange exchange, byte[] stream) {
        flush(exchange, stream, 200);
    }

    private static void flush(HttpExchange exchange, byte[] stream, int code) {
        try {
            exchange.sendResponseHeaders(code, stream.length);
            exchange.getResponseBody().write(stream);
            exchange.getResponseBody().close();
        } catch (IOException ioException) {
            sneakyThrow(ioException);
        }
    }

    private static byte[] errorMessage(String message) {
        return String.format("{ \"message\": \"%s\" }", message).getBytes(StandardCharsets.UTF_8);
    }
}
