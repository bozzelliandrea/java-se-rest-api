package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Jwts;
import server.Server;
import server.util.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static server.filter.JWTAuthFilter.hmacKey;

public class AuthHandler implements HttpHandler {

    public final static HttpHandler INSTANCE = new AuthHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equals("POST")) {
            Response.e501(exchange, exchange.getRequestMethod());
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
