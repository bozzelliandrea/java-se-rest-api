package database;

import database.model.Car;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryDataLayer {

    public static final Map<Integer, Car> car = new HashMap<>();
    public static final AtomicInteger carSequence = new AtomicInteger();

    static {
        Car c = new Car(carSequence.incrementAndGet(),
                "a nice card",
                "ford",
                "blue");
        car.put(c.getId(), c);
        c = new Car(carSequence.incrementAndGet(),
                "very expensive car",
                "ferrari",
                "yellow");
        car.put(c.getId(), c);
        c = new Car(carSequence.incrementAndGet(),
                "kind of a big deal",
                "volkswagen",
                "rust");
        car.put(c.getId(), c);
    }
}
