package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.model.Deserializable;
import server.Server;
import server.auth.AuthManager;
import server.auth.AuthRequest;
import server.util.Response;

import java.util.Optional;


public class AuthHandler implements HttpHandler {

    public final static HttpHandler INSTANCE = new AuthHandler();

    @Override
    public void handle(HttpExchange exchange) {

        if (!exchange.getRequestMethod().equals("POST")
                && !exchange.getRequestMethod().equals("HEAD")) {
            Response.e501(exchange, exchange.getRequestMethod());
            return;
        }

        if (exchange.getRequestMethod().equals("POST")
                && exchange.getRequestURI().getPath().equals(Server.authBasePath + "/register")) {

            AuthRequest.Register request = null;
            try {
                request = Deserializable.of(exchange.getRequestBody(), new AuthRequest.Register());
            } catch (Exception e) {
                e.printStackTrace();
                Response.e400(exchange, e.toString());
                return;
            }

            Optional<String> token = AuthManager.register(request);

            if (!token.isPresent()) {
                Response.e400(exchange, "Wrong register request");
                return;
            }

            exchange.getResponseHeaders().add("Authorization", token.get());
            Response.json(exchange, new byte[0]);
            return;
        }

        if (exchange.getRequestMethod().equals("POST")
                && exchange.getRequestURI().getPath().equals(Server.authBasePath + "/login")) {

            AuthRequest.Login request = null;

            try {
                request = Deserializable.of(exchange.getRequestBody(), new AuthRequest.Login());
            } catch (Exception e) {
                e.printStackTrace();
                Response.e400(exchange, e.toString());
                return;
            }

            Optional<String> token = AuthManager.login(request);

            if (!token.isPresent()) {
                Response.e404(exchange, "User not registered");
                return;
            }

            exchange.getResponseHeaders().add("Authorization", token.get());
            Response.json(exchange, new byte[0]);
            return;
        }

        if (exchange.getRequestMethod().equals("HEAD")
                && exchange.getRequestURI().getPath().equals(Server.authBasePath + "/logout")) {

            AuthManager.logout(exchange.getRequestHeaders().get("Authorization").get(0));

            Response.json(exchange, new byte[0]);
            return;
        }

        if (exchange.getRequestMethod().equals("HEAD")
                && exchange.getRequestURI().getPath().equals(Server.authBasePath + "/refresh")) {

            String token = AuthManager.refresh(exchange.getRequestHeaders().get("Authorization").get(0));

            exchange.getResponseHeaders().add("Authorization", token);
            Response.json(exchange, new byte[0]);
        }

        Response.e404(exchange, "Invalid request");
    }

}
