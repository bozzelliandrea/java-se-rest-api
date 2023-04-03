package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.model.Car;
import server.model.Deserializable;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;

import static server.Server.carBasePath;
import static server.Server.dataSource;

public class CarHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new CarHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("REGISTER CALL METHOD: " + exchange.getRequestMethod() + " | " +
                "REQUESTED PATH: " + exchange.getRequestURI().getPath() + " | " +
                ZonedDateTime.now());

        OutputStream os = exchange.getResponseBody();
        String response = "";

        if (!exchange.getRequestMethod().equals("GET") && !exchange.getRequestMethod().equals("POST")) {
            response = "NOT FOUND";
            exchange.sendResponseHeaders(404, response.length());
            os.write(response.getBytes());
            os.close();
            return;
        }

        // GET - http://localhost:8000/car -> return Car JSON Array
        // GET - http://localhost:8000/car/:id -> return Car JSON selected by Id
        if (exchange.getRequestMethod().equals("GET")) {

            if (exchange.getRequestURI().getPath().endsWith(carBasePath)) {
                response = response + "[ ";
                int counter = dataSource.size();
                for (Car car : dataSource.values()) {
                    counter--;
                    response = response + car.serialize() + (counter != 0 ? ',' : "");
                }
                response = response + " ]";
            } else {
                String requestId = exchange.getRequestURI().getPath().replace(carBasePath + '/', "");

                response = dataSource.get(requestId).serialize();
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.close();
            return;
        }

        // POST - http://localhost:8000/car -> receive Car Object as JSON format | return Car JSON
        if (exchange.getRequestMethod().equals("POST")) {

            try {
                Car car = Deserializable.of(exchange.getRequestBody(), new Car());
                dataSource.put(car.getName(), car);
                response = "Created resource :: Car " + car.getName();
                exchange.sendResponseHeaders(201, response.length());
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
