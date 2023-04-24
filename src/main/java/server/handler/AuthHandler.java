package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.model.Deserializable;
import io.jsonwebtoken.Jwts;
import server.Server;
import server.auth.AuthManager;
import server.auth.AuthRequest;
import server.util.Response;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static server.auth.AuthManager.hmacKey;


public class AuthHandler implements HttpHandler {

    public final static HttpHandler INSTANCE = new AuthHandler();

    @Override
    public void handle(HttpExchange exchange) {

        if (!exchange.getRequestMethod().equals("POST")
                && !exchange.getRequestMethod().equals("HEAD")) {
            Response.e501(exchange, exchange.getRequestMethod());
            return;
        }

        if (exchange.getRequestURI().getPath().equals(Server.authBasePath + "/register")) {
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

        if (exchange.getRequestURI().getPath().equals(Server.authBasePath + "/login")) {

            Instant now = Instant.now();
            exchange.getResponseHeaders().add("Authorization",
                    Jwts.builder()
                            .claim("name", "Jane Doe")
                            .claim("email", "jane@example.com")
                            .setSubject("jane")
                            .setId(UUID.randomUUID().toString())
                            .setIssuedAt(Date.from(now))
                            .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                            .signWith(hmacKey)
                            .compact());

            Response.json(exchange, new byte[]{});
            return;
        }

        if (exchange.getRequestURI().getPath().equals(Server.authBasePath + "/logout")) {

            Response.json(exchange, new byte[]{});
            return;
        }

        if (exchange.getRequestURI().getPath().equals(Server.authBasePath + "/refresh")) {

            Response.json(exchange, new byte[]{});
        }
    }

}
