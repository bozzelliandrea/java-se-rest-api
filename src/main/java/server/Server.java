package server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import server.filter.JWTAuthFilter;
import server.filter.LoggerFilter;
import server.handler.AuthHandler;
import server.handler.CarHandler;
import server.handler.IndexHandler;

import java.net.InetSocketAddress;

public class Server {


    public final static String carBasePath = "/car";
    public final static String authBasePath = "/auth";
    public final static String rootPath = "/";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext(rootPath, IndexHandler.INSTANCE)
                .getFilters().add(new LoggerFilter());

        server.createContext(authBasePath, AuthHandler.INSTANCE)
                .getFilters().add(new LoggerFilter());

        HttpContext carContext = server.createContext(carBasePath, CarHandler.INSTANCE);
        carContext.getFilters().add(new LoggerFilter());
        carContext.setAuthenticator(new JWTAuthFilter());

        server.setExecutor(null);
        server.start();
        System.out.println("Java Server listening on http://localhost:8000");
    }
}
