import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {

    final static Map<String, Car> dataSource = new HashMap<>();
    final static String carBasePath = "/car";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(carBasePath, new CarHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server listening on http://localhost:8000");
    }

    static class CarHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

            System.out.println("RECEIVED CALL METHOD: " + t.getRequestMethod());

            OutputStream os = t.getResponseBody();
            String response = "";

            if (!t.getRequestMethod().equals("GET") && !t.getRequestMethod().equals("POST")) {
                response = "NOT FOUND";
                t.sendResponseHeaders(404, response.length());
                os.write(response.getBytes());
                os.close();
                return;
            }

            // GET - http://localhost:8000/car -> return Car JSON Array
            // GET - http://localhost:8000/car/:id -> return Car JSON selected by Id
            if (t.getRequestMethod().equals("GET")) {

                if(t.getRequestURI().getPath().endsWith(carBasePath)) {
                    response = response + "[ ";
                    int counter = dataSource.size();
                    for(Car car: dataSource.values()) {
                        counter--;
                        response = response + car.toJSON() + (counter != 0 ? ',' : "");
                    }
                    response = response + " ]";
                } else {
                    String requestId = t.getRequestURI().getPath().replace(carBasePath + '/', "");

                    response = dataSource.get(requestId).toJSON();
                }
                t.getResponseHeaders().add("Content-Type", "application/json");
                t.sendResponseHeaders(200, response.length());
                os.write(response.getBytes());
                os.close();
                return;
            }

            // POST - http://localhost:8000/car -> receive Car Object as JSON format | return Car JSON
            if (t.getRequestMethod().equals("POST")) {
                StringBuilder textBuilder = new StringBuilder();
                try (Reader reader = new BufferedReader(new InputStreamReader
                        (t.getRequestBody(), Charset.forName(StandardCharsets.UTF_8.name())))) {
                    int c;
                    while ((c = reader.read()) != -1) {
                        textBuilder.append((char) c);
                    }
                }

                String json = textBuilder.toString().replaceAll("[ \n{}\"]", "");
                Car car = new Car();

                for(String prop: json.split(",")) {
                    try {
                        Field field = car.getClass().getDeclaredField(prop.substring(0, prop.indexOf(':')));
                        field.setAccessible(true);
                        field.set(car, prop.substring(prop.indexOf(':')+1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                dataSource.put(car.getName(), car);

                response = "Created resource :: Car " + car.getName();
                t.sendResponseHeaders(201, response.length());
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class Car {

        private String description;
        private String name;
        private String color;

        public Car() {
        }

        public String getName() {
            return name;
        }

        public String toJSON() {
            return "{" +
                    "\"description\": \"" + description + '\"' +
                    ",\"name\": \"" + name + '\"' +
                    ",\"color\": \"" + color + '\"' +
                    '}';
        }
    }
}
