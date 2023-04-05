package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.model.Car;
import server.model.Deserializable;
import server.model.Response;

import java.io.IOException;

import static server.Server.carBasePath;
import static server.Server.dataSource;

public class CarHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new CarHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = "";

        if (!exchange.getRequestMethod().equals("GET") && !exchange.getRequestMethod().equals("POST")) {
            Response.e501(exchange, exchange.getRequestMethod());
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

            Response.json(exchange, response.getBytes());
            return;
        }

        // POST - http://localhost:8000/car -> receive Car Object as JSON format | return Car JSON
        if (exchange.getRequestMethod().equals("POST")) {
            try {
                Car car = Deserializable.of(exchange.getRequestBody(), new Car());
                dataSource.put(car.getName(), car);
                Response.json(exchange, car.serialize().getBytes(), 201);
            } catch (Exception e) {
                Response.e500(exchange, e.toString());
            }
        }
    }
}
