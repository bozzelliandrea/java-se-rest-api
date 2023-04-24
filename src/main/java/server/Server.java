package server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import server.filter.AuthFilter;
import server.filter.LoggerFilter;
import server.handler.AuthHandler;
import server.handler.CarHandler;
import server.handler.IndexHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    public final static String carBasePath = "/car";
    public final static String authBasePath = "/auth";
    public final static String rootPath = "/";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext(rootPath, IndexHandler.INSTANCE)
                .getFilters().add(new LoggerFilter());

        HttpContext authContext = server.createContext(authBasePath, AuthHandler.INSTANCE);
        authContext.getFilters().add(new LoggerFilter());
        authContext.getFilters().add(new AuthFilter(
                new HashMap<String, List<String>>() {{
                    put("HEAD", new ArrayList<String>() {{
                        add(authBasePath + "/logout");
                        add(authBasePath + "/refresh");
                    }});
                }}
        ));

        HttpContext carContext = server.createContext(carBasePath, CarHandler.INSTANCE);
        carContext.getFilters().add(new LoggerFilter());
        carContext.getFilters().add(new AuthFilter(new HashMap<String, List<String>>() {{
            put("POST", new ArrayList<String>() {{
                add(carBasePath + "/car");
            }});
            put("PUT", new ArrayList<String>() {{
                add(carBasePath + "/car");
            }});
            put("DELETE", new ArrayList<String>() {{
                add(carBasePath + "/car");
            }});
        }}));

        server.setExecutor(null);
        server.start();
        System.out.println("Java Server listening on http://localhost:8000");
    }
}
