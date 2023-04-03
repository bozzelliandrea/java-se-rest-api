package server;

import com.sun.net.httpserver.HttpServer;
import server.handler.CarHandler;
import server.handler.IndexHandler;
import server.model.Car;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public final static Map<String, Car> dataSource = new HashMap<>();
    public final static String carBasePath = "/car";
    public final static String rootPath = "/";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(rootPath, IndexHandler.INSTANCE);
        server.createContext(carBasePath, CarHandler.INSTANCE);
        server.setExecutor(null);
        server.start();
        System.out.println("Java Server listening on http://localhost:8000");
    }
}
