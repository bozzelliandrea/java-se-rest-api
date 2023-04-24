package server.filter;


import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.JwtException;
import server.auth.AuthManager;
import server.util.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthFilter extends Filter {

    private final Map<String, List<String>> securedApi;

    public AuthFilter(Map<String, List<String>> securedApi) {
        this.securedApi = securedApi;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        if (securedApi.containsKey(exchange.getRequestMethod())
                && securedApi.get(exchange.getRequestMethod()).contains(exchange.getRequestURI().getPath())) {

            if (!exchange.getRequestHeaders().containsKey("Authorization")) {
                Response.e401(exchange);
            }

            try {
                AuthManager.validateToken(exchange.getRequestHeaders().get("Authorization").get(0));
                chain.doFilter(exchange);
            } catch (JwtException signatureException) {
                Response.e401(exchange);
            }
        }

        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "JWT Authentication middleware for incoming requests with security enabled";
    }
}
