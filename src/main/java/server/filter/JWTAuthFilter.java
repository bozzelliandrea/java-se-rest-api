package server.filter;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import server.util.Response;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;

public class JWTAuthFilter extends com.sun.net.httpserver.Authenticator {

    public final static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    public final static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());

    @Override
    public Result authenticate(HttpExchange exchange) {
        if (!exchange.getRequestHeaders().containsKey("Authorization")) {
            try {
                Response.e401(exchange);
            } catch (Exception exception) {
                exception.printStackTrace();
                try {
                    Response.e500(exchange, exception.toString());
                } catch (IOException e) {
                    // todo: refactor error response without checked exception
                    e.printStackTrace();
                    return new Failure(500);
                }
            }
        }

        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(exchange.getRequestHeaders().get("Authorization").get(0));
            return new Success(new HttpPrincipal((String) jwt.getBody().get("name"), "CenterAppRealm"));
        } catch (SignatureException signatureException) {
            return new Failure(401);
        }
    }

}
