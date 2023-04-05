package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.model.Car;
import database.model.Deserializable;
import database.model.Serializable;
import database.repository.CarRepository;
import database.repository.Repository;
import server.util.Response;

import java.io.IOException;

import static server.Server.carBasePath;

public class CarHandler implements HttpHandler {

    public static final HttpHandler INSTANCE = new CarHandler();

    private final Repository<Integer, Car> repository;

    public CarHandler() {
        repository = new CarRepository();
    }

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
                response = Serializable.forArray(this.repository.findAll());
            } else {
                String requestId = exchange.getRequestURI().getPath().replace(carBasePath + '/', "");
                response = this.repository.findById(Integer.valueOf(requestId)).serialize();
            }

            Response.json(exchange, response.getBytes());
            return;
        }

        // POST - http://localhost:8000/car -> receive Car Object as JSON format | return Car JSON
        if (exchange.getRequestMethod().equals("POST")) {
            try {
                Car car = this.repository.save(Deserializable.of(exchange.getRequestBody(), new Car()));
                Response.json(exchange, car.serialize().getBytes(), 201);
            } catch (Exception e) {
                Response.e500(exchange, e.toString());
            }
        }
    }
}
