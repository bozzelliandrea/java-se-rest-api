package server.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import server.util.Logger;

import java.io.IOException;

public class LoggerFilter extends Filter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Logger.forRequest(exchange.getRequestMethod(), exchange.getRequestURI().getPath());
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "Logger middleware for incoming requests";
    }
}
