package server.filter;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import server.util.Response;

import static server.auth.AuthManager.hmacKey;

public class JWTAuthFilter extends com.sun.net.httpserver.Authenticator {

    @Override
    public Result authenticate(HttpExchange exchange) {
        if (!exchange.getRequestHeaders().containsKey("Authorization")) {
            Response.e401(exchange);
        }

        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(exchange.getRequestHeaders().get("Authorization").get(0));
            return new Success(new HttpPrincipal((String) jwt.getBody().get("name"), "CenterAppRealm"));
        } catch (JwtException signatureException) {
            Response.e401(exchange);
        }

        return new Failure(401);
    }

}
